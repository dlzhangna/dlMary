package com.bjyt.flink.window;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

public class TumbingWindow {
	public static void main(String[] args) throws Exception {
		  StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		  Properties props = new Properties();
		  //指定Kafka的Broker地址
		  props.setProperty("bootstrap.servers", "localhost:9092");
		  //指定组ID
		  props.setProperty("group.id", "s10");
		  //如果没有记录偏移量,第一次从最开始消费
		  props.setProperty("auto.offset.reset", "earliest");
		  //Kafka的消费者不自动提交偏移量
		  //props.setProperty("enable.auto.commit", "false");
		  FlinkKafkaConsumer<String> kafkaSource = new FlinkKafkaConsumer<String>("summer", new SimpleStringSchema(), props);
	      //kafkaSource.setStartFromEarliest();
		  /*
		   >a,1
           >b,1
           >a,1
           >a,1
           >a,1
           >b,1
           >c,1
           >d,1
           >e,1
           >a,1
           >a,1
           >a,1
           >a,1
           >b,1
           >b,1
           >b,1
           >b,1
           >c,1
           >c,1
           >c,1
		  */
		  DataStream lines = env.addSource(kafkaSource);
		  SingleOutputStreamOperator<Tuple2<String,Integer>> wordAndCount = lines.map(new MapFunction<String,Tuple2<String,Integer>>(){
			@Override
			public Tuple2<String, Integer> map(String value) throws Exception {
				String[] fields = value.split(",");
				String word = fields[0];
				Integer count = Integer.parseInt(fields[1]);
				return Tuple2.of(word, count);
			}	 
		  });
		  //divide group first, then divide to window
		  KeyedStream<Tuple2<String, Integer>, Tuple> keyed = wordAndCount.keyBy(0);
		  //divide Tumbing window
		  WindowedStream<Tuple2<String,Integer>,Tuple,TimeWindow> window = keyed.timeWindow(Time.seconds(5));
		  //WindowedStream<Tuple2<String,Integer>,Tuple,TimeWindow> window = keyed.window(TumblingProcessingTimeWindows.of(Time.of(5, TimeUnit.SECONDS)));
		  //sum of the values of windows
		  SingleOutputStreamOperator<Tuple2<String,Integer>> summed = window.sum(1);
		  /*
		   2> (b,1)
           6> (a,1)
           6> (a,1)
           6> (a,1)
           6> (a,1)
           2> (b,1)
           4> (c,1)
           5> (d,1)
           1> (e,1)
           6> (a,3)
           2> (b,2)
           6> (a,1)
           2> (b,2) 
		   */
		  summed.print();
		  env.execute("TumbingWindow");
}
}

package com.bjyt.flink.window;

import java.util.Properties;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.api.datastream.KeyedStream;

/*Divide to many group, every group reach limit records, will be triggered the task to execute*/
public class CountWindow {
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
		   >hadoop,2
           >hadoop,3
           >spark,1
           >spark,2
           >spark,3
           >hue,1
           >hue,2
           >hue,3
           >hadoop,1
           >hadoop,1
           >hadoop,1
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
		  //divide to window
		  WindowedStream<Tuple2<String,Integer>,Tuple,GlobalWindow> window = keyed.countWindow(5);
		  //sum of the values of windows
		  SingleOutputStreamOperator<Tuple2<String,Integer>> summed = window.sum(1);
		  /*
		   8> (hadoop,8) 
		   */
		  summed.print();
		  env.execute("CountWindow");
  }
}

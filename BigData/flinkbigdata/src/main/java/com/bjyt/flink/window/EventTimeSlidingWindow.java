package com.bjyt.flink.window;

import java.util.Properties;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

/*
 * when we use EventTime, divide window
 * if we use Kafka as the source,create topic in many different partition
 * all of these partition match trigger condition, will trigger the whole window
 */
public class EventTimeSlidingWindow {
	public static void main(String[] args) throws Exception {
		  StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		  env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
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
		   >1000,a,1
           >1999,a,1
           >2222,b,1
           >2999,a,1
           >4000,a,1
		  */
//		  DataStream lines = env.addSource(kafkaSource);
		  DataStream lines = env.addSource(kafkaSource).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<String>(Time.seconds(0)) {
//		  DataStream lines = env.addSource(kafkaSource).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<String>(Time.seconds(2)) {//set delay time
			//Abstract the time field from the line
			@Override
			public long extractTimestamp(String line) {
				String[] fields = line.split(",");
				return Long.parseLong(fields[0]);
			}

		  });
		  //lines.print();
		  SingleOutputStreamOperator<Tuple3<String,String,Integer>> wordAndCount = lines.map(new MapFunction<String,Tuple3<String,String,Integer>>(){
			@Override
			public Tuple3<String,String,Integer> map(String value) throws Exception {
				String[] fields = value.split(",");
				String time = fields[0];
				String word = fields[1];
				Integer count = Integer.parseInt(fields[2]);
				return Tuple3.of(time,word, count);
			}	 
		  });
//		  //divide group first, then divide to window
		  KeyedStream<Tuple3<String,String,Integer>, Tuple> keyed = wordAndCount.keyBy(1);
//		  //divide Tumbing window
		  WindowedStream<Tuple3<String,String,Integer>,Tuple,TimeWindow> window = keyed.window(SlidingEventTimeWindows.of(Time.seconds(6),Time.seconds(2)));
//		  //keyed.window(SlidingProcessingTimeWindows.of(Time.seconds(10),Time.seconds(5)));
//		  //sum of the values of windows
		  SingleOutputStreamOperator<Tuple3<String,String,Integer>> summed = window.sum(2);
//		  /*
//		  6> (1000,a,3)
//		  2> (2222,b,1)
//		  */
		  summed.print();
		  env.execute("EventTimeSlidingWindow");
  }
}

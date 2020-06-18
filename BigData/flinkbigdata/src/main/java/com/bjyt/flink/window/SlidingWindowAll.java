package com.bjyt.flink.window;

import java.util.Properties;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.AllWindowedStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

public class SlidingWindowAll {
	public static void main(String[] args) throws Exception {
		  StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		  Properties props = new Properties();
		  //指定Kafka的Broker地址
		  props.setProperty("bootstrap.servers", "localhost:9092");
		  //指定组ID
		  props.setProperty("group.id", "s10");
		  props.setProperty("auto.offset.reset", "earliest");
		  //Kafka的消费者不自动提交偏移量
		  //props.setProperty("enable.auto.commit", "false");
		  FlinkKafkaConsumer<String> kafkaSource = new FlinkKafkaConsumer<String>("summer", new SimpleStringSchema(), props);
	      //kafkaSource.setStartFromEarliest();
		  /*
		   >1
           >2
           >3
		   */
		  DataStream lines = env.addSource(kafkaSource);
		  SingleOutputStreamOperator<Integer> nums = lines.map(new MapFunction<String,Integer>(){
			  @Override
			  public Integer map(String value) throws Exception{
				  return Integer.parseInt(value);
			  }
		  });
		  //doesn't divide group
		  AllWindowedStream<Integer, TimeWindow> window = nums.timeWindowAll(Time.seconds(10),Time.seconds(5));
		  SingleOutputStreamOperator<Integer> summed = window.sum(0);
		  /*
		   2> 3
           3> 6
           4> 3
		   */
		  summed.print();
		  env.execute("SlidingWindowAll");
  }
}

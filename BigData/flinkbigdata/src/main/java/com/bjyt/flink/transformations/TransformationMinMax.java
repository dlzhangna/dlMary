package com.bjyt.flink.transformations;

import java.util.Properties;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

public class TransformationMinMax {
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
		  //从最早开始消费
	      //kafkaSource.setStartFromEarliest();
		  //a,1
		  //b,5
		  //a,10
		  //a,8
		  //b,9
		  //b,6
		  DataStream lines = env.addSource(kafkaSource);
		  
		  SingleOutputStreamOperator<Tuple2<String,Integer>> wordAndNum = lines.map(new MapFunction<String,Tuple2<String,Integer>>(){
			@Override
			public Tuple2<String,Integer> map(String line) throws Exception{
				String[] fields = line.split(",");
				String word = fields[0];
				int num = Integer.parseInt(fields[1]);
				return Tuple2.of(word, num);
			}
		  });
		  KeyedStream<Tuple2<String,Integer>,Tuple> keyed = wordAndNum.keyBy(0);
		  SingleOutputStreamOperator<Tuple2<String,Integer>> maxRes = keyed.max(1);
		  //keyed.min(1);
		  /*
		  6> (a,1)
		  2> (b,5)
		  6> (a,10)
		  6> (a,10)
		  2> (b,9)
		  2> (b,9)
          */
		  maxRes.print();
		  env.execute("TransformationMinMax");	 
  }
}

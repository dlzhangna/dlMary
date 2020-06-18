package com.bjyt.flink.transformations;

import java.util.Properties;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

public class TransformationKeyBy {
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
		  DataStream lines = env.addSource(kafkaSource);
		  SingleOutputStreamOperator<Tuple2<String,Integer>> wordAndOne = lines.map(w->Tuple2.of(w, 1)).returns(Types.TUPLE(Types.STRING,Types.INT));
		  //在java中，认为元组是一个特殊的集合，脚标是从0开始
		  KeyedStream<Tuple2<String,Integer>,Tuple> keyed = wordAndOne.keyBy(0);
		  //1> (spark,1)
		  //1> (spark,1) 
          //8> (hadoop,1)
          //1> (spark,1)
          //8> (hadoop,1)
          //1> (hive,1)
		  //3> (hello,1)
		  //1> (hive,1)
		  //3> (hello,1)
		  keyed.print();
		  env.execute("TransformationKeyBy");
  }
}

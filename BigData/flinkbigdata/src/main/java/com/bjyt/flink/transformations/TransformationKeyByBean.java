package com.bjyt.flink.transformations;

import java.util.Properties;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

public class TransformationKeyByBean {
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
		  SingleOutputStreamOperator<WordCounts> wordAndOne = lines.map(new MapFunction<String,WordCounts>(){
		    @Override
			public WordCounts map(String value) throws Exception{
		    	//return new WordCounts(value,1L);
		    	return WordCounts.of(value,1L);
		    }
		  });
		  //在java中，认为元组是一个特殊的集合，脚标是从0开始
		  KeyedStream<WordCounts,Tuple> keyed = wordAndOne.keyBy("word");
		 //keyed.print();
		  /*
		   8> WordCounts [word=hadoop, counts=1]
           1> WordCounts [word=spark, counts=1]
           1> WordCounts [word=hive, counts=1]
           1> WordCounts [word=spark, counts=1]
		   */
		  //聚合
		  SingleOutputStreamOperator<WordCounts> summed = keyed.sum("counts");
		  summed.print();
		  /*
		  4> WordCounts [word=keep, counts=1]
		  1> WordCounts [word=spark, counts=1]
		  8> WordCounts [word=hadoop, counts=1]
		  1> WordCounts [word=spark, counts=2]
		  4> WordCounts [word=keep, counts=2]
		  3> WordCounts [word=hello, counts=1]
		  1> WordCounts [word=spark, counts=3]
		  4> WordCounts [word=keep, counts=3]
		  4> WordCounts [word=keep, counts=4]
		  */
		  env.execute("TransformationKeyByBean");
		 
  }
}

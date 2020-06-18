package com.bjyt.flink.transformations;

import java.util.Properties;
import org.apache.flink.api.common.functions.FoldFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

public class TransformationFold {
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
		  //spark
		  //spark
		  //hadoop
		  //spark
		  //hue
		  //hue
		  DataStream lines = env.addSource(kafkaSource);
		  
		  SingleOutputStreamOperator<Tuple2<String,Integer>> wordAndOne = lines.map(w->Tuple2.of(w, 1)).returns(Types.TUPLE(Types.STRING,Types.INT));
		  //在java中，认为元组是一个特殊的集合，脚标是从0开始
		  KeyedStream<Tuple2<String,Integer>,Tuple> keyed = wordAndOne.keyBy(0);
		  //返回值和初始值类型一样
//		  SingleOutputStreamOperator<String> result = keyed.fold("",new FoldFunction<Tuple2<String,Integer>,String>(){
//			@Override
//			public String fold(String accumulator,Tuple2<String,Integer> value) throws Exception{
//				String word = "";
//				int sum = 0;
//				if(accumulator.equals("")) {
//					word = value.f0;
//					sum += value.f1;
//				}else {
//					String[] fields = accumulator.split("-");
//					word = fields[0];
//					sum = Integer.parseInt(fields[1])+value.f1;
//				}
//				return word + "-" + sum;
//			}
//		  });
		  /*
		   1> spark-1
           1> spark-2
           8> hadoop-1
           1> spark-3
           6> hue-1
           6> hue-2
		   */
		  //result.print();
		  /*input:
		   >spark
           >spark
           >hadoop
           >spark
           >hue
           >haddoop
           >hue
           >hadoop
		   */
		  SingleOutputStreamOperator<Tuple2<String,Integer>> result = keyed.fold(new Tuple2<String,Integer>("",0),
				  new FoldFunction<Tuple2<String,Integer>,Tuple2<String,Integer>>(){

					@Override
					public Tuple2<String, Integer> fold(Tuple2<String, Integer> accumulator,
							Tuple2<String, Integer> value) throws Exception {
						String key = value.f0;
					    Integer count = value.f1;
					    accumulator.f0 = key;
					    accumulator.f1 += count;
					    return accumulator;
					}
				
			  });
		  /*
		   //spark
           //spark
           //hadoop
           //spark
           //hue
           //hue
		   */
		  result.print();
		  env.execute("TransformationFold");
  }
}

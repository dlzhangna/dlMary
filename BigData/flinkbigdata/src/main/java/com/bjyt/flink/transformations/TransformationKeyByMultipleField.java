package com.bjyt.flink.transformations;

import java.util.Properties;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

public class TransformationKeyByMultipleField {
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
		  //LiaoNing,ShenYang,1000
		  //LiaoNing,ShenYang,1000
		  //ShanDong,QingDao,2000
		  //ShanDong,QingDao,2000
		  //ShanDong,YanTai,1000
		  //LiaoNing,BenXi,2000
		  DataStream lines = env.addSource(kafkaSource);
//		  SingleOutputStreamOperator<Tuple3<String,String,Double>> provinceCityAndMoney = lines.map(new MapFunction<String,Tuple3<String,String,Double>>(){
//
//			@Override
//			public Tuple3<String, String, Double> map(String value) throws Exception {
//				String[] fields = value.split(",");
//				String province = fields[0];
//				String city = fields[1];
//				double money = Double.parseDouble(fields[2]);
//				return Tuple3.of(province, city, money);
//			}
//		    
//		  });
		  //在java中，认为元组是一个特殊的集合，脚标是从0开始
		  //KeyedStream<Tuple3<String,String,Double>,Tuple> keyed = provinceCityAndMoney.keyBy(0,1);
		  //SingleOutputStreamOperator<Tuple3<String,String,Double>> summed = keyed.sum(2);
		  //summed.print();
		  SingleOutputStreamOperator<OrderBean> provinceCityAndMoney = lines.map(new MapFunction<String,OrderBean>(){

				@Override
				public OrderBean map(String value) throws Exception {
					String[] fields = value.split(",");
					String province = fields[0];
					String city = fields[1];
					double money = Double.parseDouble(fields[2]);
					return OrderBean.of(province, city, money);
				}
			    
			  });
		  KeyedStream<OrderBean,Tuple> keyedBean = provinceCityAndMoney.keyBy("province","city");
		  //keyed.print();
		  SingleOutputStreamOperator<OrderBean> summedBean = keyedBean.sum("money");
		  summedBean.print();
		  //聚合
		 
		  /*
		  7> OrderBean [province=LiaoNing, city=ShenYang, money=1000.0]
          7> OrderBean [province=LiaoNing, city=ShenYang, money=2000.0]
          5> OrderBean [province=ShanDong, city=QingDao, money=2000.0]
          5> OrderBean [province=ShanDong, city=QingDao, money=4000.0]
          7> OrderBean [province=ShanDong, city=YanTai, money=1000.0]
          2> OrderBean [province=LiaoNing, city=BenXi, money=2000.0]
		  */
		  env.execute("TransformationKeyByMultipleField");
		 
  }
}

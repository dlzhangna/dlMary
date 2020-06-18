package com.bjyt.flink.kafka;

import java.util.Properties;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

public class KafkaConsumer {
	public static void main(String[] args) throws Exception {
		// set up the streaming execution environment
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
		lines.print();
		// execute program
		env.execute("KafkaConsumer");
   }
}

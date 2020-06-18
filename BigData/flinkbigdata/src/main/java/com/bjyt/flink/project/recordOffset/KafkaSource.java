package com.bjyt.flink.project.recordOffset;

import java.util.Properties;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

public class KafkaSource {
  public static void main(String[] args) throws Exception{
	// set up the streaming execution environment
	StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
	//env.setParallelism(1);
	//open checkpointing, open restart strategy at the same time,will store offset
	env.enableCheckpointing(5000);
	//set stateBackEnd
	env.setStateBackend(new FsStateBackend("file:///EclipseJDK1.8Workspace/flinkbigdata/stateBackend/ck1"));
	
	env.getCheckpointConfig().enableExternalizedCheckpoints(ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
	
	env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
	String topic = args[0];
	String groupid = args[1];
	String brokerList = args[2];

	Properties props = new Properties();
	// Kafka的Broker地址
	props.setProperty("bootstrap.servers", brokerList);
	// ID
	props.setProperty("group.id", groupid);

	//props.setProperty("auto.offset.reset", "earliest");
	/*
	 * earliest 当分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费.
     * latest 当分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据.
     * none 当该topic下所有分区中存在未提交的offset时，抛出异常.
	 */
	props.setProperty("auto.offset.reset", "earliest");

	// Kafka的消费者不自动提交偏移量
	props.setProperty("enable.auto.commit", "false");
	FlinkKafkaConsumer<String> kafkaSource = new FlinkKafkaConsumer<String>(topic, new SimpleStringSchema(), props);
    
	//decide if commit offset to Kafka's special topic
	//if doesn't set savePoint,after exception, restart Flink programme,will re-cover from Kafka's special topic, default restore data from savePoint.
	//kafkaSource.setCommitOffsetsOnCheckpoints(false);
	// kafkaSource.setStartFromEarliest();
	DataStream lines = env.addSource(kafkaSource);
	
	lines.print();
	env.execute("KafkaSource");
	
  }
}
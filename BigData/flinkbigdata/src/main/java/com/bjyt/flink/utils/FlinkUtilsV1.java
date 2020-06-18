package com.bjyt.flink.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

public class FlinkUtilsV1 {

	public static StreamExecutionEnvironment env = null;

	public static DataStream<String> createKafkaStream(String[] args, SimpleStringSchema simpleStringSchema) {
		// set up the streaming execution environment
		env = StreamExecutionEnvironment.getExecutionEnvironment();
		//env.setParallelism(1);
		String topic = args[0];
		String groupid = args[1];
		String brokerList = args[2];

		Properties props = new Properties();
		// Kafka的Broker地址
		props.setProperty("bootstrap.servers", brokerList);
		// ID
		props.setProperty("group.id", groupid);

		//props.setProperty("auto.offset.reset", "earliest");
		props.setProperty("auto.offset.reset", "latest");

		// Kafka的消费者不自动提交偏移量
		//props.setProperty("enable.auto.commit", "false");
		FlinkKafkaConsumer<String> kafkaSource = new FlinkKafkaConsumer<String>(topic, new SimpleStringSchema(), props);

		// kafkaSource.setStartFromEarliest();
		DataStream lines = env.addSource(kafkaSource);
		return lines;
	}
	
	public static <T> DataStream<T> createKafkaStreamFromProperties(ParameterTool parameters,Class<? extends DeserializationSchema<T>> Clazz) throws Exception {
		// set up the streaming execution environment
		env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		env.getConfig().setGlobalJobParameters(parameters);
		
		env.enableCheckpointing(parameters.getLong("checkpoint.interval",5000L),CheckpointingMode.EXACTLY_ONCE);
		
		env.getCheckpointConfig().enableExternalizedCheckpoints(ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
		
		env.setStateBackend(new FsStateBackend("file:///EclipseJDK1.8Workspace/flinkbigdata/stateBackend/ck2"));

		Properties props = new Properties();
		// Kafka的Broker地址
		props.setProperty("bootstrap.servers", parameters.getRequired("bootstrap.servers"));
		// ID
		props.setProperty("group.id", parameters.getRequired("group.id"));
        
		/*
		 * earliest 当分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费.
	     * latest 当分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据.
	     * none 当该topic下所有分区中存在未提交的offset时，抛出异常.
		 */
		props.setProperty("auto.offset.reset", parameters.get("auto.offset.reset","earliest"));
		
		// Kafka的消费者不自动提交偏移量
		props.setProperty("enable.auto.commit", parameters.get("enable.auto.commit","false"));
		
		String topics = parameters.getRequired("topics");
		List<String> topicList = Arrays.asList(topics.split(","));
		FlinkKafkaConsumer<T> kafkaConsumer = new FlinkKafkaConsumer<T>(topicList, Clazz.newInstance(), props);
		//Submit offset to special topic in Kafka
		kafkaConsumer.setCommitOffsetsOnCheckpoints(true);
		DataStream lines = env.addSource(kafkaConsumer);
		return lines;
	}
	
	public static <T> DataStream<T> createKafkaStreamFromPropertiesKey(ParameterTool parameters,String bootstrapServers,String groupId,String topics,Class<? extends DeserializationSchema<T>> Clazz) throws Exception {
		// set up the streaming execution environment
		env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		env.getConfig().setGlobalJobParameters(parameters);
		
		env.enableCheckpointing(parameters.getLong("checkpoint.interval",5000L),CheckpointingMode.EXACTLY_ONCE);
		
		env.getCheckpointConfig().enableExternalizedCheckpoints(ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
		
		env.setStateBackend(new FsStateBackend("file:///EclipseJDK1.8Workspace/flinkbigdata/stateBackend/ck2"));

		Properties props = new Properties();
		// Kafka的Broker地址
		props.setProperty("bootstrap.servers", bootstrapServers);
		// ID
		props.setProperty("group.id", groupId);
        
		/*
		 * earliest 当分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费.
	     * latest 当分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据.
	     * none 当该topic下所有分区中存在未提交的offset时，抛出异常.
		 */
		props.setProperty("auto.offset.reset", parameters.get("auto.offset.reset","earliest"));
		
		// Kafka的消费者不自动提交偏移量
		props.setProperty("enable.auto.commit", parameters.get("enable.auto.commit","false"));
		
		//String topics = parameters.getRequired("topics");
		List<String> topicList = Arrays.asList(topics.split(","));
		FlinkKafkaConsumer<T> kafkaConsumer = new FlinkKafkaConsumer<T>(topicList, Clazz.newInstance(), props);
		//Submit offset to special topic in Kafka
		kafkaConsumer.setCommitOffsetsOnCheckpoints(true);
		DataStream lines = env.addSource(kafkaConsumer);
		return lines;
	}

	public static StreamExecutionEnvironment getEnv() {
		return env;
	}
}

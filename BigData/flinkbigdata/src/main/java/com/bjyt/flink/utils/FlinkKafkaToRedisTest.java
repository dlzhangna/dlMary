package com.bjyt.flink.utils;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;

public class FlinkKafkaToRedisTest {
  public static void main(String[] args) throws Exception{
	  //Run as->:Program arguments:--topics summer --group.id s10
	  //get parameter from Run as
//	  ParameterTool parameters = ParameterTool.fromArgs(args);
//	  String groupId = parameters.get("group.id");
//	  String topics = parameters.getRequired("topics");
//	  System.out.println("topics:" + topics);
//	  System.out.println("groupId:" + groupId);
	  //get parameter from config file
	  //Run as->Program arguments:C:\flink-1.10.0-bin-scala_2.11\flink-1.10.0\myConfig\config.properties
//	  ParameterTool parameters = ParameterTool.fromPropertiesFile(args[0]);
//	  String groupId = parameters.get("group.id");
//	  String topics = parameters.getRequired("topics");
//	  System.out.println("topics:" + topics);
//	  System.out.println("groupId:" + groupId);
	  //Create data from Kafka
	  ParameterTool parameters = ParameterTool.fromPropertiesFile(args[0]);
	  DataStream<String> lines = FlinkUtilsV1.createKafkaStreamFromProperties(parameters,SimpleStringSchema.class);
	  lines.print();
	  FlinkUtilsV1.getEnv().execute("FlinkKafkaToRedis");
  }
}

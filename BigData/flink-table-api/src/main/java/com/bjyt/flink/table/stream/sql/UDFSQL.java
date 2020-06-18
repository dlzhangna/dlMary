package com.bjyt.flink.table.stream.sql;

import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import com.bjyt.flink.table.function.IpLocation;
import com.bjyt.flink.table.utils.FlinkUtilsV1;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

//comes one ip address, then associate province,city and region
public class UDFSQL {
	public static void main(String[] args)throws Exception{
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		//register a Cached File, then send to TaskManager through network
		env.registerCachedFile("C:\\EclipseJDK1.8Workspace\\flink\\data\\ip.txt", "ip-rules");
		StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
		//106.121.4.252
		//42.57.88.186
		//DataStreamSource
		DataStream<String> lines = FlinkUtilsV1.createKafkaStream(args,new SimpleStringSchema());
		tableEnv.registerDataStream("t_lines", lines,"ip");
		//Register a identify function, it is a UDF, input a ip address, then response a Row<Province,City>
		tableEnv.registerFunction("ipLocation", new IpLocation());
		Table table = tableEnv.sqlQuery("SELECT ip,ipLocation(ip) FROM t_lines");
		tableEnv.toAppendStream(table, Row.class).print();
		env.execute();
	    }
	}

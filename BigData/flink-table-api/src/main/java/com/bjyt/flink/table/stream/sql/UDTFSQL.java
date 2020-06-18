package com.bjyt.flink.table.stream.sql;

import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import com.bjyt.flink.table.function.Split;
import com.bjyt.flink.table.utils.FlinkUtilsV1;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

//comes one ip address, then associate province,city and region
public class UDTFSQL {
	public static void main(String[] args)throws Exception{
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
		
		//spark hadoop flink spark
		DataStream<String> kafkaDataStream = FlinkUtilsV1.createKafkaStream(args,new SimpleStringSchema());
		tableEnv.registerDataStream("t_lines", kafkaDataStream,"line");
		//hello tom jerry tom
		tableEnv.registerFunction("split", new Split("\\W+"));
		//LATERAL:table generate function
		//change 1 row to multiple rows, change 1 row:hello tom jerry tom to multiple rows
		Table table = tableEnv.sqlQuery("SELECT word FROM t_lines,LATERAL TABLE(split(line)) as T(word)");
		/*1>spark
		 *1>hadoop
		 *1>flink
		 *1>spark
		 */
		tableEnv.toAppendStream(table, Row.class).print();
		env.execute();
	    }
	}

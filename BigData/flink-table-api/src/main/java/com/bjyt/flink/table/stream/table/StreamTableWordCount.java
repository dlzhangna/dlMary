package com.bjyt.flink.table.stream.table;

import java.util.Arrays;
import org.apache.calcite.interpreter.Row;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.util.Collector;
import com.bjyt.flink.table.utils.FlinkUtilsV1;


public class StreamTableWordCount {
  public void main(String[] args) throws Exception{
	  //Realtime DataStreamAPI
	  StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
	  //Create a realtime's Table context
	  StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
	  
	  DataStream<String> lines = FlinkUtilsV1.createKafkaStream(args,new SimpleStringSchema());
	  SingleOutputStreamOperator<String> words = lines.flatMap(new FlatMapFunction<String,String>(){
			@Override
			public void flatMap(String line, Collector<String> out) throws Exception {
				Arrays.stream(line.split("")).forEach(out::collect);
			}	  
		  });

	  //Regist as a table
	  Table table = tableEnv.fromDataStream(words,"word");
	  Table t2 = table.groupBy("word").select("word,count(1) as counts");
	  DataStream<Tuple2<Boolean,Row>> dataStream = tableEnv.toRetractStream(t2, Row.class);
	  /*input:spark spark
	   *output:(true,spark,1)
	   *       (false,spark,1)
	   *       (true,spark,2)
	   *input:hadoop hadoop
	   *output:(true,hadoop,1)
	   *       (false,hadoop,1)
	   *       (true,hadoop,2)
	   */
	  dataStream.print();
	  //Write SQL
//	  TableSchema schema = table.getSchema();
//	  
//	  /*
//	   * root
//	   * !--word:STRING
//	   */
//	  System.out.println(schema);
	  env.execute();
  }
}

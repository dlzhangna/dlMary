package com.bjyt.flink.table.stream.sql;

import java.util.Arrays;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.apache.flink.util.Collector;
import com.bjyt.flink.table.utils.FlinkUtilsV1;


public class StreamWordCountSQL {
  public void main(String[] args) throws Exception{
	  //Realtime DataStreamAPI
	  StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
	  //Create a realtime's Table context
	  StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
	  
	  DataStream<String> lines = FlinkUtilsV1.createKafkaStream(args,new SimpleStringSchema());
	  SingleOutputStreamOperator<String> words = lines.flatMap(new FlatMapFunction<String,String>(){
			@Override
			public void flatMap(String line, Collector<String> out) throws Exception {
				String[] fields = line.split("\\W+");
				Arrays.stream(fields).forEach(out::collect);
			}	  
		  });

	  //Regist table
	  Table table = tableEnv.fromDataStream(words,"word");
	  //Write SQL
	  Table result = table.groupBy("word").select("word,count(1) as counts");
	  //
	  DataStream<Tuple2<Boolean,Row>> resStream = tableEnv.toRetractStream(result, Row.class);
	  /*input:spark hadoop flink spark
	   *output:
	   *(true,WordCount{word='hadoop',counts='1'})
	   *(true,WordCount{word='spark',counts='1'})
	   *(true,WordCount{word='flink',counts='1'})
	   *(false,WordCount{word='spark',counts='1'})//the data before leijia with the old data
	   *(true,WordCount{word='spark',counts='2'})/the data after leijia with the old data
	   */
	  resStream.print();
	  //only print after leijia data
//	  dataStream.filter(new FilterFunction<Tuple2<Boolean,WordCount>>(){
//		  public boolean filter(Tuple2<Boolean,WordCount> tp)throws Exception{
//			  return tp.f0;
//		  }
//	  }).print();
	  env.execute();
  }
}

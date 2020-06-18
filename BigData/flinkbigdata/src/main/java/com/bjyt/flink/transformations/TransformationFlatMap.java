package com.bjyt.flink.transformations;

import java.util.Arrays;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/*
 * 对DataStream进行操作
 * */
public class TransformationFlatMap {
  public static void main(String[] args) throws Exception {
	  StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
	  DataStreamSource<String> lines = env.fromElements("spark flink hadoop","spark flink hello","hadoop hello flink");
//	  SingleOutputStreamOperator<String> words = lines.flatMap(new FlatMapFunction<String,String>(){
//		@Override
//		public void flatMap(String line, Collector<String> out) throws Exception {
//			Arrays.stream(line.split(" ")).forEach(out::collect);
//			//Arrays.asList(line.split(" ")).forEach(w->out.collect(w));
////			String[] words = line.split(" ");
////			for(String word : words) {
////				out.collect(word);
////			}
//		}	  
//	  });
	  
	  SingleOutputStreamOperator<String> words2 = lines.flatMap((String line,Collector<String> out)->
	    Arrays.stream(line.split(" ")).forEach(out::collect)).returns(Types.STRING);
	  //flatMap方法还可以传入RichFlatMapFunction,open(),close()
	  words2.print();
	  env.execute("TransformationFlatMap");
   }
}

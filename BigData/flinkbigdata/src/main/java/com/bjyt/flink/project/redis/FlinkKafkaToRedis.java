package com.bjyt.flink.project.redis;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.util.Collector;
import com.bjyt.flink.utils.FlinkUtilsV1;

public class FlinkKafkaToRedis {
	public static void main(String[] args) throws Exception {
		ParameterTool parameters = ParameterTool.fromPropertiesFile(args[0]);
		DataStream<String> lines = FlinkUtilsV1.createKafkaStreamFromProperties(parameters,SimpleStringSchema.class);
		System.out.println("lines:" + lines.toString());
		SingleOutputStreamOperator<String> words = lines.flatMap(new FlatMapFunction<String,String>(){

			@Override
			public void flatMap(String line, Collector<String> out) throws Exception {
				String[] wordArray = line.split(",");
				for(String word : wordArray) {
					System.out.println("word0:" + word);
					out.collect(word);
				}
			}
		});
		System.out.println("words:");
		words.print();
		//let words Combine
		SingleOutputStreamOperator<Tuple2<String,Integer>> wordAndOne = words.map(new MapFunction<String,Tuple2<String,Integer>>(){
			
			@Override
			public Tuple2<String,Integer> map(String word) throws Exception{
				System.out.println("word1:" + word);
				return Tuple2.of(word, 1);
			}
		});
		System.out.println("wordAndOne:");
		wordAndOne.print();
		SingleOutputStreamOperator<Tuple2<String,Integer>> summed = wordAndOne.keyBy(0).sum(1);
		System.out.println("summed:");
		summed.print();
		summed.map(new MapFunction<Tuple2<String,Integer>,Tuple3<String,String,String>>(){

			@Override
			//Get Tuple3 from Tuple2
			public Tuple3<String, String, String> map(Tuple2<String, Integer> tp) throws Exception {
				System.out.println("tp.f0:" + tp.f0);
				System.out.println("tp.f1:" + tp.f1.toString());
				return Tuple3.of("WORD_COUNT",tp.f0, tp.f1.toString());
			}
		     
		}).addSink(new RedisSink());
		FlinkUtilsV1.getEnv().execute("FlinkKafkaToRedis");
	}
}

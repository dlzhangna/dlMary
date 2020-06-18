package com.bjyt.flink.textFile;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

//multiple parallelism
public class TextFileSource {
	public static void main(String[] args) throws Exception {
		// set up the streaming execution environment
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		DataStreamSource<String> lines = env.readTextFile(args[0]);
		int parallelismTextFile = lines.getParallelism();
		System.out.println("parallelismTextFile--------------:" + parallelismTextFile);
		SingleOutputStreamOperator<Tuple2<String,Integer>> words = lines.flatMap(new FlatMapFunction<String,Tuple2<String,Integer>>(){
		
			public void flatMap(String line, Collector<Tuple2<String, Integer>> out) throws Exception {
				String[] words = line.split(" ");
				for(String word:words) {
					out.collect(Tuple2.of(word, 1));
				}
			}
		});
		int parallelismTextFile1 = words.getParallelism();
		System.out.println("parallelismTextFile1--------------:" + parallelismTextFile1);
		SingleOutputStreamOperator<Tuple2<String,Integer>> summed = words.keyBy(0).sum(1);
		summed.print();
		// execute program
		//task如何划分的:
		//1.并行度发生变化
		//2.KeyBy发生re-detributing
		env.execute("TextFileSource");
   }
}

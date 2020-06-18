package com.bjyt.flink.transformations;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/*
 * 对DataStream进行操作
 * */
public class StreamWordCountSharingGroup {
  public static void main(String[] args) throws Exception {
	  StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
	  DataStreamSource<String> lines = env.fromElements("spark flink hadoop","spark flink hello","hadoop hello flink");
	  //Socket, Parallelism will change, one task
	  SingleOutputStreamOperator<String> words = lines.flatMap(new FlatMapFunction<String,String>(){
		@Override
		public void flatMap(String line, Collector<String> out) throws Exception {
			String[] words = line.split(" ");
			for(String word : words) {
				out.collect(word);
			}
		}	  
	  }).slotSharingGroup("doit");//同一个job不同时期的subTask在同夜光表slot中
	  
	  SingleOutputStreamOperator<String> filtered = words.filter(new FilterFunction<String>() {
		  @Override
		  public boolean filter(String value) throws Exception{
			  return value.startsWith("h");
		  }
	  });
	  //.disableChaining();//set current filter with one task,old:flatMap,filter,map(Task1), now:flatMap(Task1),Filter(Task2) and Map(Task3),there is no operator chain with other operation
	  //startNewChain();//start a new chain,before this new chain, send Reditributing
	  //if invoke .startNewChain,old:flatMap,filter,map together, now:flatMap(Task1),Filter and Map(Task2)
	  SingleOutputStreamOperator<Tuple2<String,Integer>> wordAndNum = filtered.map(new MapFunction<String,Tuple2<String,Integer>>(){
			@Override
			public Tuple2<String,Integer> map(String word) throws Exception{
//				String[] fields = line.split(",");
//				String word = fields[0];
//				int num = Integer.parseInt(fields[1]);
				return Tuple2.of(word, 1);
			}
		  });//
	  //KeyBy Parallelism will change, one task,Hash
	  SingleOutputStreamOperator<Tuple2<String,Integer>> summed = wordAndNum.keyBy(0).sum(1);
	  //if change parallelism here, will generate one task
	  //summed.print().setParallelism(2);
	  summed.print();
	  //Task1(Socket:Parallelism:1)->RedistributingTask2(Flat Map->Filter->Map(chain):Parallelism:4)->Task3（KeyedBy->Sink,Parallelism:4)
	  //startNewChain
	  //Disable chaining
	  env.execute("StartNewOperation");
   }
}

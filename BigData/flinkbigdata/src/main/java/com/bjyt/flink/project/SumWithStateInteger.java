package com.bjyt.flink.project;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup;
import org.apache.flink.util.Collector;

import com.bjyt.flink.utils.FlinkUtilsV1;

public class SumWithStateInteger {
  public static void main(String[] args) throws Exception{
	  StreamExecutionEnvironment env = FlinkUtilsV1.getEnv();
	  //start checkpointing
	  DataStream<String> lines = FlinkUtilsV1.createKafkaStream(args, new SimpleStringSchema());
	  //fix-delay is default restart strategy
	  env.enableCheckpointing(5000);
	  // enable externalized checkpoints which are retained after job cancellation
	  env.getCheckpointConfig().enableExternalizedCheckpoints(ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
	  //set mode to exactly-once (this is the default), deal with the data only one time,must record position
	  env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
	  
	  env.setStateBackend(new FsStateBackend("hdfs://localhost:2000/ck1"));

	  System.out.println("lines:" + lines);
	  
	  SingleOutputStreamOperator<String> words = lines.flatMap(new FlatMapFunction<String,String>(){
			@Override
			public void flatMap(String line, Collector<String> out) throws Exception {
				String[] words = line.split(" ");
				for(String word : words) {
					out.collect(word);
				}
			}	  
		  });
	  
	  SingleOutputStreamOperator<Tuple2<String,Integer>> wordAndOne = lines.map(new MapFunction<String,Tuple2<String,Integer>>(){
		  @Override
		  public Tuple2<String,Integer> map(String word) throws Exception{
			  System.out.println("word:" + word);
			  return Tuple2.of(word, 1);
		  }
	  });

	  KeyedStream<Tuple2<String,Integer>,Tuple> keyed = wordAndOne.keyBy(0);
	  keyed.map(new RichMapFunction<Tuple2<String,Integer>,Tuple2<String,Integer>>(){
		  
		  private transient ValueState<Integer> valuestate;
		  @Override
		  public void open(Configuration parameters) throws Exception{
			  //init status or recover history status
			  //define a status describtion
			  ValueStateDescriptor<Integer> descriptor = new ValueStateDescriptor(
					  "ck-keyed-state-v2",
					  TypeInformation.of(new TypeHint<Integer>(){})
					  //Types.INT
					  );
			  //第一次，在内存中什么都取不到，如果后来存过，根据描述器在内存中就能找到对应的状态，用于恢复
			  //won't find anything when first time, if store it, then will find the status from description for recoving the data
			  //get the key state
			  valuestate = getRuntimeContext().getState(descriptor);
		  }

		  @Override
		  public Tuple2<String, Integer> map(Tuple2<String, Integer> tp) throws Exception {
			//input key(word)
			String word = tp.f0;
			//input value(time)
			Integer count = tp.f1;
			//get history data from state
			Integer total = valuestate.value();
			if(total == null) {
				//update state data
				valuestate.update(count);
				//return result
				return Tuple2.of(word, count);
			 }else {
				 total = total + count;
				 valuestate.update(total);
				 return Tuple2.of(word, total);
			  }
		    }
		  });
	  keyed.print();
	  FlinkUtilsV1.getEnv().execute("MapWithState");
    }
}
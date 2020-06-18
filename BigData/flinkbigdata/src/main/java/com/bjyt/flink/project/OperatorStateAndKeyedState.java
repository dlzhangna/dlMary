package com.bjyt.flink.project;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup;
import org.apache.flink.util.Collector;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import com.bjyt.flink.utils.FlinkUtilsV1;

/*
 * 用来观察OperatorState和KeyedState
 * Kafka消费者消费数据记录偏移量，消费者对应SubTask使用OperatorState记录偏移量
 * keyBy之后，进行聚合操作，进行历史数据累加，这些SubTask使用累加分组后的历史就是KeyedState
 */
public class OperatorStateAndKeyedState {
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
//
//	  // make sure 500 ms of progress happen between checkpoints
//	  env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);
//
//	  // checkpoints have to complete within one minute, or are discarded
//	  env.getCheckpointConfig().setCheckpointTimeout(60000);
//
//	  // allow only one checkpoint to be in progress at the same time
//	  env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
//
//	  
//	  //Restart 3 times, delay 2000ms/every time
//	  env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, 2000));
	  //env.setStateBackend(new FsStateBackend("file:///EclipseJDK1.8Workspace/flinkbigdata/stateBackend"));
	  //env.setStateBackend(new FsStateBackend("hdfs://localhost:2000/ck"));
	  //env.setStateBackend(new FsStateBackend(args[0]));
	  //CheckpointConfig checkpointConfig = FlinkUtilsV1.getEnv().getCheckpointConfig();
	  //if the programme is cancelled or has exception, don't delete checkpoint's data
	  //FlinkUtilsV1.getEnv().getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION);
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
	  //To ensure the programme has exception, will store middle result of group and aggressive
	  SingleOutputStreamOperator<Tuple2<String,Integer>> summed = wordAndOne.keyBy(0).sum(1);
	  summed.print();
	  FlinkUtilsV1.getEnv().execute("OperatorStateAndKeyedState");
    }
}

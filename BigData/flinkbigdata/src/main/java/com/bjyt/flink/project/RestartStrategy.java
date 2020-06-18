package com.bjyt.flink.project;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.bjyt.flink.utils.FlinkUtilsV1;

public class RestartStrategy {
  public static void main(String[] args) throws Exception{
	  //start checkpointing
	  DataStream<String> lines = FlinkUtilsV1.createKafkaStream(args, new SimpleStringSchema());
	  StreamExecutionEnvironment env = FlinkUtilsV1.getEnv();
	  //fix-delay is default restart strategy
	  env.enableCheckpointing(5000);
	// set mode to exactly-once (this is the default)
	  env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);

	  // make sure 500 ms of progress happen between checkpoints
	  env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);

	  // checkpoints have to complete within one minute, or are discarded
	  env.getCheckpointConfig().setCheckpointTimeout(60000);

	  // allow only one checkpoint to be in progress at the same time
	  env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);

	  // enable externalized checkpoints which are retained after job cancellation
	  env.getCheckpointConfig().enableExternalizedCheckpoints(ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
	  //Restart 3 times, delay 2000ms/every time
	  env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, 2000));
	  //CheckpointConfig checkpointConfig = FlinkUtilsV1.getEnv().getCheckpointConfig();
	  //FlinkUtilsV1.getEnv().getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
	  //FlinkUtilsV1.getEnv().getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION);
	  System.out.println("lines:" + lines);
	  SingleOutputStreamOperator<Tuple2<String,Integer>> wordAndOne = lines.map(new MapFunction<String,Tuple2<String,Integer>>(){
		  public Tuple2<String,Integer> map(String word) throws Exception{
			  System.out.println("word:" + word);
			  if(word.startsWith("exception3")) {
				  throw new RuntimeException("exception comes,the progress throws exception");
			  }
			  return Tuple2.of(word, 1);
		  }
	  });
	  SingleOutputStreamOperator<Tuple2<String,Integer>> result = wordAndOne.keyBy(0).sum(1);
	  result.print();
	  FlinkUtilsV1.getEnv().execute("RestartStrategy");
    }
}

package com.bjyt.flink.project.recordOffset;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup;

public class OperatorExactlyOnceState {
  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(2);
    env.enableCheckpointing(5000);
    env.setRestartStrategy(RestartStrategies.fixedDelayRestart(2,2000));
    env.setStateBackend(new FsStateBackend("file:///EclipseJDK1.8Workspace/flinkbigdata/stateBackend/offset"));
    env.getCheckpointConfig().enableExternalizedCheckpoints(ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
    DataStreamSource<Tuple2<String, String>> tp = env.addSource(new MyExactlyOnceParFileSource("/flink-1.10.0-bin-scala_2.11/flink-1.10.0/data/input/offset"));
    /*
     2> (1,1)
     1> (0,a)
     2> (1,2)
     1> (0,b)
     2> (1,3)
     1> (0,c)
     1> (0,d)
     2> (1,4)
     */
    /*
     * echo w >> /flink-1.10.0-bin-scala_2.11/flink-1.10.0/data/input/offset/0.txt
     * echo 8 >> /flink-1.10.0-bin-scala_2.11/flink-1.10.0/data/input/offset/1.txt
     * 1>(0,w)
     * 2>(1,8)
     */
    /*
     * 
     * if has exception, will restart 2 time, then read data from last line offset , read data only one time
     * echo f >> /flink-1.10.0-bin-scala_2.11/flink-1.10.0/data/input/offset/0.txt
     * echo 2222 >> /flink-1.10.0-bin-scala_2.11/flink-1.10.0/data/input/offset/0.txt
     * 1>(0,f)
     * 2>(1,2222)
     *
     */
    tp.print();
    env.execute("OperatorExactlyOnceState");
  }
}

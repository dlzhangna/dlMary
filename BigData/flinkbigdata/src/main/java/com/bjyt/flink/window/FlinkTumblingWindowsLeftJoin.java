package com.bjyt.flink.window;


import org.apache.flink.api.common.functions.CoGroupFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/*
 * Use EventTime to divide windows to impleme
 */
public class FlinkTumblingWindowsLeftJoin {
  public static void main(String[] args) throws Exception{
	  int windowSize = 10;
	  long delay = 5002L;//5000L //5002L
	  final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
	  //Set EventTime as the time standard
	  env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
	  env.setParallelism(1);
	  DataStream<Tuple3<String,String,Long>> leftSource = env.addSource(new StreamDataSourceA()).name("SourceA").setParallelism(1);
	  DataStream<Tuple3<String,String,Long>> rightSource = env.addSource(new StreamDataSourceB()).name("SourceB").setParallelism(1);
	  
	  //Set WaterMark
      DataStream<Tuple3<String, String, Long>> leftStream = leftSource.assignTimestampsAndWatermarks(
          new BoundedOutOfOrdernessTimestampExtractor<Tuple3<String, String, Long>>(Time.milliseconds(delay)) {
              @Override
              public long extractTimestamp(Tuple3<String, String, Long> element) {
                  return element.f2;
              }
          }
      );
      
      DataStream<Tuple3<String, String, Long>> rigjhtStream = rightSource.assignTimestampsAndWatermarks(
              new BoundedOutOfOrdernessTimestampExtractor<Tuple3<String, String, Long>>(Time.milliseconds(delay)) {
                  @Override
                  public long extractTimestamp(Tuple3<String, String, Long> element) {
                      return element.f2;
                  }
              }
          );
      // join 操作
      leftStream.coGroup(rigjhtStream)
          .where(new LeftSelectKey()).equalTo(new RightSelectKey())
          .window(TumblingEventTimeWindows.of(Time.seconds(windowSize)))
          .apply(new LeftJoin())
          .print();
          //0L
          //(a,1,hangzhou,1000000050000,1000000059000)
          //(a,2,hangzhou,1000000054000,1000000059000)
          //(a,3,null,1000000079900,-1)
          //(a,4,null,1000000115000,-1)
          //5000L
          //(a,1,hangzhou,1000000050000,1000000059000)
          //(a,2,hangzhou,1000000054000,1000000059000)
          //(a,3,null,1000000079900,-1)
          //(a,4,null,1000000115000,-1)
          //b is discard
          //5002L
          //(a,1,hangzhou,1000000050000,1000000059000)
          //(a,2,hangzhou,1000000054000,1000000059000)
          //(a,3,null,1000000079900,-1)
          //(b,5,beijing,1000000100000,1000000105000)
          //(b,6,beijing,1000000108000,1000000105000)
          //(a,4,null,1000000115000,-1)
      env.execute("FlinkTumblingWindowsLeftJoin");
    }
  public static class LeftSelectKey implements KeySelector<Tuple3<String, String, Long>, String> {
      @Override
      public String getKey(Tuple3<String, String, Long> w) {
          return w.f0;
      }
  }

  public static class RightSelectKey implements KeySelector<Tuple3<String, String, Long>, String> {
      @Override
      public String getKey(Tuple3<String, String, Long> w) {
          return w.f0;
      }
  }
  //if this method is invoke, it means the data in the same winodws
  public static class LeftJoin implements CoGroupFunction<Tuple3<String, String, Long>, Tuple3<String, String, Long>, Tuple5<String, String, String, Long, Long>> {
      @Override
      public void coGroup(Iterable<Tuple3<String, String, Long>> leftElements, Iterable<Tuple3<String, String, Long>> rightElements, Collector<Tuple5<String, String, String, Long, Long>> out) {
          //get all the record from left, then circle it.
          for (Tuple3<String, String, Long> leftElem : leftElements) {
              boolean hadElements = false;
              ////get all the record from right, then circle it.
              for (Tuple3<String, String, Long> rightElem : rightElements) {
            	  //if key is same, then collect to new record
                  out.collect(new Tuple5<>(leftElem.f0, leftElem.f1, rightElem.f1, leftElem.f2, rightElem.f2));
                  hadElements = true;
              }
              //if didn't join successful, only print left record, right is default value
              if (!hadElements) {
                  out.collect(new Tuple5<>(leftElem.f0, leftElem.f1, "null", leftElem.f2, -1L));
              }
          }
      }
  }
 }

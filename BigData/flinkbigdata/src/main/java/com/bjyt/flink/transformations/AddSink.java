package com.bjyt.flink.transformations;

import java.util.Properties;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

/*
 * 对DataStream进行操作
 * */
public class AddSink {
  public static void main(String[] args) throws Exception {
	  StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
	  Properties props = new Properties();
	  //指定Kafka的Broker地址
	  props.setProperty("bootstrap.servers", "localhost:9092");
	  //指定组ID
	  props.setProperty("group.id", "s10");
	  //如果没有记录偏移量,第一次从最开始消费
	  props.setProperty("auto.offset.reset", "earliest");
	  //Kafka的消费者不自动提交偏移量
	  //props.setProperty("enable.auto.commit", "false");
	  FlinkKafkaConsumer<String> kafkaSource = new FlinkKafkaConsumer<String>("summer", new SimpleStringSchema(), props);
	  //从最早开始消费
      //kafkaSource.setStartFromEarliest();
	  /*
	   >hadoop hadoop
       >hello
       >hello hello
       >hadoop hadoop 
	   */
	  DataStream lines = env.addSource(kafkaSource);
	  SingleOutputStreamOperator<Tuple2<String,Integer>> words = lines.flatMap(new FlatMapFunction<String,Tuple2<String,Integer>>(){
		@Override
		public void flatMap(String line, Collector<Tuple2<String, Integer>> out) throws Exception {
			String[] words = line.split(" ");
			for(String word : words) {
				Tuple2<String,Integer> tp = Tuple2.of(word, 1);
				out.collect(tp);
			}
		}	  
	  });
	  SingleOutputStreamOperator<Tuple2<String,Integer>> summed = words.keyBy(0).sum(1);
	  //summed.addSink(new SinkFunction<Tuple2<String,Integer>>(){
	    summed.addSink(new RichSinkFunction<Tuple2<String,Integer>>(){
	      
		  @Override
		  public void invoke(Tuple2<String,Integer> value,Context context) throws Exception{
			  int index = getRuntimeContext().getIndexOfThisSubtask();
			  System.out.println(index + ">" + value);
		  }
	  });
	  /*没有subTask indexId,用SinkFunction时
	   (hadoop,1)
       (hadoop,2)
       (hello,1)
       (hello,2)
       (hello,3)
       (hadoop,3)
       (hadoop,4)
	   */
	    /*可以取到subTask indexId并拼接到输出中,用RichSinkFunction时
		  7>(hadoop,1)
          7>(hadoop,2)
          2>(hello,1)
          2>(hello,2)
          2>(hello,3)
          7>(hadoop,3)
          7>(hadoop,4)
		 */
	  env.execute("AddSink");
   }
}

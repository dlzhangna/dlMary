package com.bjyt.flink.transformations;

import java.util.Properties;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

public class WriteAsCSV {
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
		   2> spark hadoop hadoop spark hello spark hello
           2> spark
           2> hadoop
           2> aaa
           2> ggggggggg
           2> uuuuuuu
           2> tttt uuuuu 99999
           2> 99999
           2> yyyyyy
		   */
		  DataStream lines = env.addSource(kafkaSource);
		  //8个文件，只记录到第二个文件中
		  //lines.writeAsText("C:\\flink-1.10.0-bin-scala_2.11\\flink-1.10.0\\data\\output\\txt");		  /*
		  /*
		  2> spark hadoop hadoop spark hello spark hello
		  2> spark
		  2> hadoop
		  2> aaa
		  2> ggggggggg
		  2> uuuuuuu
		  2> tttt uuuuu 99999
		  2> 99999
		  2> yyyyyy
          */
		 //lines.print();
		  /*input:
		   >spark hadoop spark hello spark hello
           >hadoop
           >aaa
           >gggggggg
           >uuuuuuuuu
           >tttttt
           >uuuuuu
           >999999
           >999999 
		   */
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
		 summed.writeAsCsv("C:\\flink-1.10.0-bin-scala_2.11\\flink-1.10.0\\data\\output\\csv",FileSystem.WriteMode.OVERWRITE);
		 /*
		  1> (spark,1)
          3> (hello,1)
          8> (hadoop,1)
          1> (spark,2)
          3> (hello,2)
          1> (spark,3)
          8> (hadoop,2)
          5> (aaa,1)
          3> (gggggggg,1)
          1> (uuuuuuuuu,1)
          7> (tttttt,1)
          1> (uuuuuu,1)
          8> (999999,1)
          8> (999999,2)
		  */
		 summed.print();
		 env.execute("WriteAsCSV");
  }
}

package com.bjyt.flink.table.stream.sql;


import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.table.descriptors.Json;
import org.apache.flink.table.descriptors.Kafka;
import org.apache.flink.table.descriptors.Schema;
import org.apache.flink.types.Row;

/*
 * https://www.cnblogs.com/Springmoon-venn/p/10570056.html
 * https://github.com/hequn8128/TableApiDemo
 */
public class KafkaWordCountSQL {
   public static void main(String[] args)throws Exception{
     StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
     env.setParallelism(1);
     env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
     StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
     tableEnv.connect(new Kafka()
    		 .version("universal")
    		 .topic("json-input")
    		 .startFromEarliest()
    		 .property("bootstrap.servers","localhost:9092")
    		 ).withFormat(new Json().deriveSchema()).withSchema(new Schema()
    		  .field("name",TypeInformation.of(String.class))
    		  .field("gender",TypeInformation.of(String.class))
    		  ).inAppendMode().registerTableSource("kafkaSource");
      Table result = tableEnv.scan("kafkaSource")
    		                 .groupBy("gender")
    		                 .select("gender,count(1) as counts");
      tableEnv.toRetractStream(result, Row.class).print();
      env.execute();
      }
}

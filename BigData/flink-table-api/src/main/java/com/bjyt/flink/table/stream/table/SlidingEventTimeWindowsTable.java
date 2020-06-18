package com.bjyt.flink.table.stream.table;

import java.util.Arrays;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.table.api.Slide;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.Tumble;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import com.bjyt.flink.table.utils.FlinkUtilsV1;

//implete huadong chuangkou
public class SlidingEventTimeWindowsTable {
   public static void main(String[] args)throws Exception{
     StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
     env.setParallelism(1);
     env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
     StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
     
     //1000,u1,p1,5
     //2000,u1,p1,5
     
     //2000,u2,p1,3
     //3000,u1,p1,5
     //3999,u2,p1,5
     
     //15000,u1,p1,8
     //18888,u2,p2,10
     DataStream<String> lines = FlinkUtilsV1.createKafkaStream(args,new SimpleStringSchema());
     SingleOutputStreamOperator<Row> rowDataStream = lines.map(new MapFunction<String,Row>(){

		@Override
		public Row map(String line) throws Exception {
			String[] fields = line.split(",");
			Long time = Long.parseLong(fields[0]);
			String uid = fields[1];
			String pid = fields[2];
			Double money = Double.parseDouble(fields[3]);
			return Row.of(time,uid,pid,money);
		} 
     }).returns(Types.ROW(Types.LONG, Types.STRING,Types.DOUBLE));
     
     SingleOutputStreamOperator<Row> waterMarksRow = rowDataStream.assignTimestampsAndWatermarks(
         new BoundedOutOfOrdernessTimestampExtractor<Row>(Time.seconds(0)) {
          @Override
          public long extractTimestamp(Row row) {
             return (long) row.getField(0);
           }
         });

      tableEnv.registerDataStream("t_orders", waterMarksRow,"atime,uid,pid,money,rowtime.rowtime");//rowtime is eventTime
      //[0,10000)
      //[0,9999]
      //win.start 0
      //win.end 10000
      //win.rowtime 9999
      Table table = tableEnv.scan("t_orders")
        		            .window(Slide.over("10.seconds").every("2.seconds").on("rowtime").as("win"))
        		            .groupBy("uid,win")
        		            .select("uid,win.start,win.end,win.rowtime,money.sum as total");
      /*u1,1969-12-31 23:59:52.0(start time),1970-01-01 00:00:02.0(end time),1970-01-01 00:00:01.999(rowtime time),5.0
       *u1,1969-12-31 23:59:54.0,1970-01-01 00:00:04.0,1970-01-01 00:00:03.999,15.0(trigger every 2 secs, sum from the first to the last here)
       *u2,1969-12-31 23:59:54.0,1970-01-01 00:00:04.0,1970-01-01 00:00:03.999,8.0
       */
      tableEnv.toAppendStream(table, Row.class).print();
      env.execute();
      }
}
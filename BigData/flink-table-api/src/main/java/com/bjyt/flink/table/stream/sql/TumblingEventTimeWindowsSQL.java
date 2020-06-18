package com.bjyt.flink.table.stream.sql;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import com.bjyt.flink.table.utils.FlinkUtilsV1;

//implete gundong chuangkou
public class TumblingEventTimeWindowsSQL {
   public static void main(String[] args)throws Exception{
     StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
     env.setParallelism(1);
     env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
     StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
     
     //1000,u1,p1,5
     //2000,u1,p1,5
     //2000,u2,p1,3
     //3000,u1,p1,5
     //9999,u2,p1,3
     
     //15000,u1,p1,8
     //18888,u2,p2,10
     //19999,u1,p1,5
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
          public long extractTimestamp(Row row) {return (long) row.getField(0);}
         });

      tableEnv.registerDataStream("t_orders", waterMarksRow,"etime,uid,pid,money,rowtime.rowtime");//rowtime is eventTime
      //[0,10000)
      //[0,9999]
      //win.start 0
      //win.end 10000
      //win.rowtime 9999
      Table result = tableEnv.sqlQuery("SELECT uid,SUM(money),TUMBLE_START(rowtime,INTERVAL '10' SECOND) as wstart FROM t_orders GROUP BY TUMBLE(rowtime,INTERVAL '10' SECOND),uid");
      /*(true,u1,15.0,1970-01-01 00:00:00.0 1970-01-01 00:00:10.0)
       *(true,u2,6.0,1970-01-01 00:00:00.0,1970-01-01 00:00:10.0)
       *(true,u2,10.0,1970-01-01 00:00:10.0,1970-01-01 00:00:20.0)
       */
      tableEnv.toRetractStream(result, Row.class).print();
      env.execute();
      }
}

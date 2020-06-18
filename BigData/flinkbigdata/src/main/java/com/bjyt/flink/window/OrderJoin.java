package com.bjyt.flink.window;


import org.apache.flink.api.common.functions.CoGroupFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjyt.flink.project.bean.OrderDetail;
import com.bjyt.flink.project.bean.OrderMain;
import com.bjyt.flink.utils.FlinkUtilsV1;

/*
 * Use EventTime to divide scroll window
 * OrderDetail data and Order data to join
 * Use CoGroup to implement real time left join
 * Will deal with the data which is not join successful
 */
public class OrderJoin {
  public static void main(String[] args) throws Exception{
	  ParameterTool parameters = ParameterTool.fromPropertiesFile(args[0]);
	  long windowTime = parameters.getLong("window.time",2000);
	  long delayTime = parameters.getLong("delay.time",1000);
	  StreamExecutionEnvironment env = FlinkUtilsV1.getEnv();
	  env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
	  env.setParallelism(1);
	  //for test simple
	  DataStream<String> orderMainStream = FlinkUtilsV1.createKafkaStreamFromPropertiesKey(parameters,parameters.getRequired("bootstrap.servers"),parameters.getRequired("order.main.group.id"),parameters.getRequired("order.main.topics"),SimpleStringSchema.class);
	  DataStream<String> orderDetailStream = FlinkUtilsV1.createKafkaStreamFromPropertiesKey(parameters,parameters.getRequired("bootstrap.servers"),parameters.getRequired("order.detail.group.id"),parameters.getRequired("order.detail.topics"),SimpleStringSchema.class);
	  
	//commit EventTime and generate waterMark
	  //Underlying method,implemete FlatMap and Filter function
	  SingleOutputStreamOperator<OrderMain> orderMainBeanStream = orderMainStream.process(new ProcessFunction<String,OrderMain>(){

		@Override
		public void processElement(String value, Context ctx,Collector<OrderMain> out) throws Exception {
			try {
			  JSONObject jsonObject = JSON.parseObject(value);
			  //get operate type
			  String type = jsonObject.getString("type");
			  //get data
			  JSONArray data = jsonObject.getJSONArray("data");
			  for(int i = 0; i< data.size(); i++) {
				  OrderMain orderMain = data.getObject(i, OrderMain.class);
				  if(type.equals("INSERT") || type.equals("UPDATE")) {
					  orderMain.setType(type);
					  out.collect(orderMain);
				  }
			  }
			}catch (Exception e) {
				//store the problem's data
				e.printStackTrace();
			}
		}
	  });
	  
	  SingleOutputStreamOperator<OrderMain> orderMainWithWaterMark = orderMainBeanStream.assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<OrderMain>(Time.milliseconds(delayTime)) {
			@Override
			public long extractTimestamp(OrderMain element) {
				// TODO Auto-generated method stub
				return element.getUpdate_time().getTime();
			}	  
		  });
	  
	  //commit EventTime and generate waterMark
	  //Underlying method,implemete FlatMap and Filter function
	  SingleOutputStreamOperator<OrderDetail> orderDetailBeanStream = orderDetailStream.process(new ProcessFunction<String,OrderDetail>(){

		@Override
		public void processElement(String value, Context ctx,Collector<OrderDetail> out) throws Exception {
			try {
			  JSONObject jsonObject = JSON.parseObject(value);
			  //get operate type
			  String type = jsonObject.getString("type");
			  //get data
			  JSONArray data = jsonObject.getJSONArray("data");
			  for(int i = 0; i< data.size(); i++) {
				  OrderDetail orderDetail = data.getObject(i, OrderDetail.class);
				  if(type.equals("INSERT") || type.equals("UPDATE")) {
					  orderDetail.setType(type);
					  out.collect(orderDetail);
				  }
			  }
			}catch (Exception e) {
				//store the problem's data
				e.printStackTrace();
			}
		}
	  });
	  SingleOutputStreamOperator<OrderDetail> orderDetailWithWaterMark = orderDetailBeanStream.assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<OrderDetail>(Time.milliseconds(delayTime)) {
		@Override
		public long extractTimestamp(OrderDetail element) {
			// TODO Auto-generated method stub
			return element.getUpdate_time().getTime();
		}	  
	  });
	  
	
	  //Detail table as left table
	  //left table's data join right table's data, right table's data delay, don't join successful, get right table's data from db
	  //left table's data delay, use test stream ouput to get left data
	  DataStream<Tuple2<OrderDetail,OrderMain>> result = orderDetailWithWaterMark.coGroup(orderMainWithWaterMark)
	                          .where(new KeySelector<OrderDetail,Long>(){

								@Override
								public Long getKey(OrderDetail value) throws Exception {
									// TODO Auto-generated method stub
									return value.getOrder_id();
								}
	                          })
	                          .equalTo(new KeySelector<OrderMain,Long>(){

								@Override
								public Long getKey(OrderMain value) throws Exception {
									// TODO Auto-generated method stub
									return value.getOid();
								}
	                            })
	                           .window(TumblingEventTimeWindows.of(Time.milliseconds(windowTime)))
	                           .apply(new CoGroupFunction<OrderDetail, OrderMain, Tuple2<OrderDetail,OrderMain>>(){

								@Override
								public void coGroup(Iterable<OrderDetail> first, Iterable<OrderMain> second,
										Collector<Tuple2<OrderDetail, OrderMain>> out) throws Exception {
									for (OrderDetail orderDetail : first) {
										boolean isJoined = false;
										for(OrderMain orderMain : second) {
											out.collect(Tuple2.of(orderDetail, orderMain));
											isJoined = true;
										}
										if(!isJoined) {
											out.collect(Tuple2.of(orderDetail, null));
										}
									}
									
								}
	                          });
	  result.print();
	  env.execute("OrderJoin");
    }
 }

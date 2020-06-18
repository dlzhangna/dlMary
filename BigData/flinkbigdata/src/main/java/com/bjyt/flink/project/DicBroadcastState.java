package com.bjyt.flink.project;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjyt.flink.utils.FlinkUtilsV1;

/*DicData
 * Get the rule data with real time when you type business data
 */
public class DicBroadcastState {
public static void main(String[] args) throws Exception {
	  ParameterTool parameters = ParameterTool.fromPropertiesFile(args[0]);
	  
	  //Read full data every time, implemented with randomUUID,changed group id every time
	  DataStream<String> actDicStream = FlinkUtilsV1.createKafkaStreamFromPropertiesKey(parameters,parameters.getRequired("bootstrap.servers"),UUID.randomUUID().toString(),"actdic",SimpleStringSchema.class);
	  SingleOutputStreamOperator<Tuple3<String,String,String>> actDicDataStream = actDicStream.process(new ProcessFunction<String,Tuple3<String,String,String>>(){
	  @Override
	  public void processElement(String line, Context ctx,Collector<Tuple3<String, String, String>> out) throws Exception {
		  try {
			  JSONObject jsonObject = JSON.parseObject(line);
			  JSONArray jsonArray = jsonObject.getJSONArray("data");
			  String type = jsonObject.getString("type");
			  if("INSERT".equals(type)||"UPDATE".equals(type)||"DELETE".equals(type)) {
				for(int i=0;i<jsonArray.size();i++){
					JSONObject obj = jsonArray.getJSONObject(i);
					String id = obj.getString("id");
					String name = obj.getString("name");
					out.collect(Tuple3.of(id, name, type));
				}
			}	
	    }catch(Exception e) {
	    	e.printStackTrace();
	      }
	    }
	  });
   //}).print();
	  //Need to define a broadcast descriptor
	MapStateDescriptor<String,String> stateDescriptor = new MapStateDescriptor<>(
			  "dic-state",
			  String.class,
			  String.class
			  );
	  BroadcastStream <Tuple3<String,String,String>> broadcastDataState = actDicDataStream.broadcast(stateDescriptor);
	  //uid01,A1,2020-01-09(activity data)
	  //(A1,"new gift",INSERT) and (A2,"chu xiao",INSERT) rule data
	  DataStream<String> actDataStream = FlinkUtilsV1.createKafkaStreamFromPropertiesKey(parameters,parameters.getRequired("bootstrap.servers"),UUID.randomUUID().toString(),"actdic",SimpleStringSchema.class);
	  SingleOutputStreamOperator<Tuple3<String,String,String>> tp2DataStream = actDataStream.map(new MapFunction<String,Tuple3<String,String,String>>(){

		@Override
		public Tuple3<String, String, String> map(String line) throws Exception {
			String[] fields = line.split(",");
			return Tuple3.of(fields[0], fields[1], fields[2]);
		}
	  });
	  //It need to connect to the data which already broadcast
	  SingleOutputStreamOperator<Tuple4<String,String,String,String>> connected = tp2DataStream.connect(broadcastDataState)
	               .process(new BroadcastProcessFunction<Tuple3<String,String,String>,
	            		                                 Tuple3<String,String,String>,
	            		                                 Tuple4<String,String,String,String>>(){
	            	 //comes 1 rule data, will put it into Memory,deal with rule data
					@Override
					public void processBroadcastElement(
							Tuple3<String, String, String> tp,
							Context ctx,
							Collector<Tuple4<String, String, String, String>> out) throws Exception {
						      String id = tp.f0;
						      String name = tp.f1;
						      String type = tp.f2;
						      BroadcastState<String,String> mapState = ctx.getBroadcastState(stateDescriptor);
						      //if the rule in DB is deleted, we hope it also be deleted in memory, will update the data in every slot
						      if("DELETE".equals(type)) {
						    	  mapState.remove(id);
						      }else {
						    	  mapState.put(id, name);
						      }
						      System.out.println(mapState.toString());
						      Iterator<Map.Entry<String,String>> iterator = mapState.iterator();
						      while(iterator.hasNext()) {
						    	  Map.Entry<String, String> next = iterator.next();
						    	  System.out.println("key:" + next.getKey() + "value:" + next.getValue());
						      }
					}
                    
					//comes 1 data,will invoke this method, comes the second data, will continue to invoke this method, deal with activity data for calculate
					public void processElement(
							Tuple3<String, String, String> input,
							ReadOnlyContext ctx,
							Collector<Tuple4<String, String, String, String>> out) throws Exception {
						      ReadOnlyBroadcastState<String,String> mapState = ctx.getBroadcastState(stateDescriptor);
						      String uid = input.f0;
						      String aid = input.f1;
						      String date = input.f2;
						      //Associate corresponding data from broadcast stateMap with activity id
						      String name = mapState.get(aid);
						      //out associated data
						      out.collect(Tuple4.of(uid, aid, name, date));
					}
	                 
	               });
	  /* rule data
	   * insert into activity values('A3','double 11 activity',NOW(),NOW());
	   * key:A1 value:new gift
	   * key:A2 value:chu xiao
	   * key:A3 value:double 11 activity
	   * key:A1 value:new gift
       * key:A2 value:chu xiao
       * key:A3 value:double 11 activity
       * key:A1 value:new gift
       * key:A2 value:chu xiao
       * key:A3 value:double 11 activity
       * key:A1 value:new gift
       * key:A2 value:chu xiao
       * key:A3 value:double 11 activity
       * update activity set name='double 12 activity' where id='A3'
       * key:A1 value:new gift
	   * key:A2 value:chu xiao
	   * key:A3 value:double 12 activity
	   * key:A1 value:new gift
       * key:A2 value:chu xiao
       * key:A3 value:double 12 activity
       * key:A1 value:new gift
       * key:A2 value:chu xiao
       * key:A3 value:double 12 activity
       * key:A1 value:new gift
       * key:A2 value:chu xiao
       * key:A3 value:double 12 activity
       * delete from activity where id='A3'
       * key:A1 value:new gift
	   * key:A2 value:chu xiao
	   * key:A1 value:new gift
       * key:A2 value:chu xiao
       * key:A1 value:new gift
       * key:A2 value:chu xiao
       * key:A1 value:new gift
       * key:A2 value:chu xiao
	   */
	  /*
	   * uid01,A1,2020-01-09->(uid01,A1,'new gift',2020-01-09)
	   */
	  connected.print();
	  FlinkUtilsV1.getEnv().execute();
}
}
package com.bjyt.flink.window;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.guava18.com.google.common.hash.BloomFilter;
import org.apache.flink.shaded.guava18.com.google.common.hash.Funnels;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;

import com.bjyt.flink.project.bean.ActBean;
import com.bjyt.flink.utils.FlinkUtilsV1;

/*
 * 实时大数据去重：
bloom过滤器:一定能判断出它不存在，原理：把每个数据都Hash,然后使其到不同的Hash桶里，如果每个Hash桶里都没有，就说明不存在-不能计数
BitMap:把数据映射成二进制
hyperLogLog-能计数(size)

让相同的用户进到相同的task里，在task里取到用户id,然后按活动id和用户id做keyBy,使其进入到相同的task里

实时统计活动的情况[多维统计],将活动类型存储到MySQL数据库作为广播变量
用户ID,活动名称,时间,活动的事件类型[1:曝光,2:点击,3:参与],省份
A1:新人礼包
A2:月末活动
实时计算去重:
计算活动的点击人数，次数
A1点击次数:3,人数:2
u001,A1,2019-09-02 10:10:11,1,"BeiJing"
u002,A1,2019-09-02 10:11:11,1,"LiaoNing"
u001,A1,2019-09-02 10:11:11,2,"BeiJing"
u001,A1,2019-09-02 10:11:30,3,"BeiJing"
u002,A1,2019-09-02 10:12:11,2,"LiaoNing"
u003,A2,2019-09-02 10:13:11,1,"ShanDong"
u003,A2,2019-09-02 10:13:20,2,"ShanDong"
u001,A1,2019-09-02 11:11:11,2,"Beijing"
Delete duplication really(实时的分布式去重，可以使用Redis，但效率比较低，我们可以将用户的ID存储到State中
1.定义一个state,state中存储的是HashSet,HashSet的特点是去重，但是HashSet中的数据可能很大甚至内存溢出
2.优化:定义一个State,使用Bloom过滤器（BitWay,转换成Hash，然后落到相应的位置，但是Bloom过虑器不能计数，还要再定义State,用来计数
 */
public class ActivityCountAdv {
  public static void main(String[] args) throws Exception{
	  ParameterTool parameters = ParameterTool.fromPropertiesFile(args[0]);
	  DataStream<String> lines = FlinkUtilsV1.createKafkaStreamFromProperties(parameters,SimpleStringSchema.class);
	  SingleOutputStreamOperator<ActBean> beanDataStream = lines.map(new MapFunction<String,ActBean>(){

		@Override
		public ActBean map(String line) throws Exception {
			String[] fields = line.split(",");
			String uid = fields[0];
			String aid = fields[1];
			String time = fields[2];
			String date = time.split("")[0];
			Integer type = Integer.parseInt(fields[3]);
			String province = fields[3];
			return ActBean.of(uid, aid, date, type, province);
		}	  
	  });
	  //follow special rule to group
	  //Count times
	  SingleOutputStreamOperator<ActBean> summed = beanDataStream.keyBy("aid","time","type").sum("count");
	  summed.print();
	  //Count peoples numbers:if the activity is click by one person, then can't count after you click again(delete duplicate data)
	  //if group by userid and activity id,u001,A1 will go to 0-subTask,u005,A1 will go to 3-subTask
	  KeyedStream<ActBean,Tuple> keyed = beanDataStream.keyBy("aid","type");
	  keyed.map(new RichMapFunction<ActBean,Tuple3<String,Integer,Long>>(){
        //Use KeyState
		private transient ValueState<BloomFilter> uidState;
		
		private transient ValueState<Long> countState;
		
		@Override
		public void open(Configuration parameters)throws Exception{
			//Define a state Descriptor
			ValueStateDescriptor<BloomFilter> stateDescriptor =new ValueStateDescriptor<BloomFilter>("uid-state",BloomFilter.class);
		    //Define times
			ValueStateDescriptor<Long> countDescriptor =new ValueStateDescriptor<Long>("count-state",Long.class);
			//Use RuntimeContext获取状态
			uidState = getRuntimeContext().getState(stateDescriptor);
			countState = getRuntimeContext().getState(countDescriptor);
		}
		@Override
		public Tuple3<String, Integer, Long> map(ActBean bean) throws Exception {
			String uid  = bean.uid;
			BloomFilter bloomFilter = uidState.value();
			if(bloomFilter==null) {//never use 1 time
				//init bloomFilter
				bloomFilter.create(Funnels.unencodedCharsFunnel(), 10000000);
				countState.update(0L);
			}
			Long counts = countState.value();
			//BloomFilter can make sure it can't exists
			if(!bloomFilter.mightContain(uid)) {
				//add current user to bloomFilter
				bloomFilter.put(uid);
				countState.update(counts+=1);
			}
			//update
			uidState.update(bloomFilter);
			return Tuple3.of(bean.aid, bean.type, counts);
		}		  
	  }).print();
	  
	  FlinkUtilsV1.getEnv().execute("ActivityCount");
  }
}

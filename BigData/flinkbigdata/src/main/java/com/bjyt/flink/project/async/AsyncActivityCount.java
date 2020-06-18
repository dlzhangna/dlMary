package com.bjyt.flink.project.async;

import java.util.concurrent.TimeUnit;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;

import com.bjyt.flink.project.bean.ActivityBean;
import com.bjyt.flink.utils.FlinkUtilsV1;

public class AsyncActivityCount {
	public static void main(String[] args) throws Exception {
		/*
		 * summer s10 localhost:9092 u001,A1,2019-09-02,1,115.908923,39.267291
		 * u001,A1,2019-09-02,1,115.908923,39.267291
         * u001,A1,2019-09-02,1,115.908923,39.267291
		 */
		// Source
		DataStream<String> lines = FlinkUtilsV1.createKafkaStream(args, new SimpleStringSchema());
		// Transformation
		SingleOutputStreamOperator<ActivityBean> beans = AsyncDataStream.unorderedWait(lines,
				new AsyncGeoToActivityBeanFunction(), 0, TimeUnit.MILLISECONDS, 10);
		// SingleOutputStreamOperator<ActivityBean> summed1 =
		// beans.keyBy("aid","eventType").sum("count");
		SingleOutputStreamOperator<ActivityBean> summed2 = beans.keyBy("aid", "eventType", "province").sum("count");
		
		FlinkJedisPoolConfig conf = new FlinkJedisPoolConfig.Builder().setHost("127.0.0.1").setPassword("root").setDatabase(1).build();
		// Sink
		// summed1.addSink(new MySqlSink());
		summed2.addSink(new RedisSink<ActivityBean>(conf, new RedisActivityBeanMapper()));
		/*
		{"status":"1","regeocode":{"addressComponent":{"city":"保定市","province":"河北省","adcode":"130684","district":"高碑店市","towncode":"130684109000","streetNumber":{"number":[],"direction":[],"distance":[],"street":[]},"country":"中国","township":"梁家营镇","businessAreas":[[]],"building":{"name":[],"type":[]},"neighborhood":{"name":[],"type":[]},"citycode":"0312"},"formatted_address":"河北省保定市高碑店市梁家营镇富民路"},"info":"OK","infocode":"10000"}
        province:河北省
        2> ActivityBean [uid=u001, aid=A1, activityName=null, time=2019-09-02, eventType=1, province=河北省, longitude=0.0, latitude=0.0, count=1]
        {"status":"1","regeocode":{"addressComponent":{"city":"保定市","province":"河北省","adcode":"130684","district":"高碑店市","towncode":"130684109000","streetNumber":{"number":[],"direction":[],"distance":[],"street":[]},"country":"中国","township":"梁家营镇","businessAreas":[[]],"building":{"name":[],"type":[]},"neighborhood":{"name":[],"type":[]},"citycode":"0312"},"formatted_address":"河北省保定市高碑店市梁家营镇富民路"},"info":"OK","infocode":"10000"}
        province:河北省
        2> ActivityBean [uid=u001, aid=A1, activityName=null, time=2019-09-02, eventType=1, province=河北省, longitude=0.0, latitude=0.0, count=1]
		 */
		/*Redis 中
		 * row key          value
		 *  1   A1_1_河北省        2
		 */
		beans.print();
		// execute
		FlinkUtilsV1.getEnv().execute("ActivityCount");
	}

	public static class RedisActivityBeanMapper implements RedisMapper<ActivityBean> {

		@Override
		//Invoke Redis's wriiten method
		public RedisCommandDescription getCommandDescription() {
			return new RedisCommandDescription(RedisCommand.HSET, "ACT_COUNT");
		}

		@Override
		//Write to Radis's key
		public String getKeyFromData(ActivityBean activityBean) {
			return activityBean.aid + "_" + activityBean.eventType + "_" + activityBean.province;
		}

		@Override
		//Write to Radis's value
		public String getValueFromData(ActivityBean activityBean) {
			return String.valueOf(activityBean.count);
		}
		
	}
}

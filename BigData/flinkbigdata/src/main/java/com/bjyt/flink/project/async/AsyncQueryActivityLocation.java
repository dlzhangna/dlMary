package com.bjyt.flink.project.async;

import java.util.concurrent.TimeUnit;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;

import com.bjyt.flink.project.bean.ActivityBean;
import com.bjyt.flink.utils.FlinkUtilsV1;

public class AsyncQueryActivityLocation {
	public static void main(String[] args) throws Exception {
		/*
		 summer s10 localhost:9092
		 u001,A1,2019-09-02,1,115.908923,39.267291
		 u002,A1,2019-09-03,1,123.818817,41.312458
		 u003,A2,2019-09-04,1,121.26757,37.49794
		 */
		DataStream<String> lines = FlinkUtilsV1.createKafkaStream(args,new SimpleStringSchema());
		//SingleOutputStreamOperator<ActivityBean> beans = lines.map((MapFunction<String, R>) new AsyncGeoToActivityBeanFunction());
	    SingleOutputStreamOperator<ActivityBean> result = AsyncDataStream.unorderedWait(lines, new AsyncGeoToActivityBeanFunction(), 0, TimeUnit.MILLISECONDS,10);
		/*
	    {"status":"1","regeocode":{"addressComponent":{"city":"保定市","province":"河北省","adcode":"130684","district":"高碑店市","towncode":"130684109000","streetNumber":{"number":[],"direction":[],"distance":[],"street":[]},"country":"中国","township":"梁家营镇","businessAreas":[[]],"building":{"name":[],"type":[]},"neighborhood":{"name":[],"type":[]},"citycode":"0312"},"formatted_address":"河北省保定市高碑店市梁家营镇富民路"},"info":"OK","infocode":"10000"}
        province:河北省
        {"status":"1","regeocode":{"addressComponent":{"city":"本溪市","province":"辽宁省","adcode":"210504","district":"明山区","towncode":"210504008000","streetNumber":{"number":"344号","location":"123.819326,41.3124228","direction":"东","distance":"42.7085","street":"峪明路"},"country":"中国","township":"新明街道","businessAreas":[[]],"building":{"name":[],"type":[]},"neighborhood":{"name":[],"type":[]},"citycode":"0414"},"formatted_address":"辽宁省本溪市明山区新明街道峪明路"},"info":"OK","infocode":"10000"}
        province:辽宁省
        {"status":"1","regeocode":{"addressComponent":{"city":"烟台市","province":"山东省","adcode":"370611","district":"福山区","towncode":"370611001000","streetNumber":{"number":"185号","location":"121.267741,37.4982461","direction":"东北","distance":"37.2368","street":"县府街"},"country":"中国","township":"清洋街道","businessAreas":[{"location":"121.261147,37.493702","name":"清洋","id":"370611"}],"building":{"name":[],"type":[]},"neighborhood":{"name":[],"type":[]},"citycode":"0535"},"formatted_address":"山东省烟台市福山区清洋街道县府街烟台市福山区人民政府"},"info":"OK","infocode":"10000"}
        province:山东省
	    */
		//beans.print();
	    FlinkUtilsV1.getEnv().execute("AsyncQueryActivityLocation");
	}
}

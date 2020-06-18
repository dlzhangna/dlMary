package com.bjyt.flink.project;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;

import com.bjyt.flink.project.bean.ActivityBean;
import com.bjyt.flink.utils.FlinkUtilsV1;

public class QueryActivityName {
	public static void main(String[] args) throws Exception {
		/*
		 u001,A1,2019-09-02,1,Beijing
         u002,A2,2019-09-01,2,Liao Ning
		 */
		DataStream<String> lines = FlinkUtilsV1.createKafkaStream(args,new SimpleStringSchema());
//	    System.out.println("lines:" + lines);
		SingleOutputStreamOperator<ActivityBean> beans = lines.map(new DataToActivityBeanFunction());
	    /*
	     ActivityBean [uid=u001, aid=A1, activityName=new gift8888, time=2019-09-02, eventType=1, province=Beijing]
         ActivityBean [uid=u001, aid=A1, activityName=new gift, time=2019-09-02, eventType=1, province=Beijing]
	     */
		beans.print();
	    FlinkUtilsV1.getEnv().execute("QueryActivityName");
	}
}

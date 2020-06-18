package com.bjyt.flink.project.async;

import java.util.concurrent.TimeUnit;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import com.bjyt.flink.utils.FlinkUtilsV1;

public class AsyncQueryFromES{
	public static void main(String[] args) throws Exception {
		DataStream<String> lines = FlinkUtilsV1.createKafkaStream(args,new SimpleStringSchema());
		SingleOutputStreamOperator<Tuple2<String,String>> stream = AsyncDataStream.unorderedWait(lines, new AsyncEsRequest(), 0, TimeUnit.MILLISECONDS);
		FlinkUtilsV1.getEnv().execute("AsyncQueryFromES");
	}
}

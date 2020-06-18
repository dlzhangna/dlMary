package com.bjyt.flink.socket;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.NumberSequenceIterator;

/*
 * parallel nums > 1
 */
public class ParallelStreamingJob {
	public static void main(String[] args) throws Exception {
		// set up the streaming execution environment
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		//DataStreamSource<Long> nums = env.fromParallelCollection(new NumberSequenceIterator(1,10), Long.class);
		
		//DataStreamSource<Long> nums = env.fromParallelCollection(new NumberSequenceIterator(1,10), TypeInformation.of(Long.TYPE));
		
		DataStreamSource<Long> nums = env.generateSequence(1, 100);
		
		int parallelism = nums.getParallelism();
		//get the number of DataStream's parallelism
		System.out.println("fromCollectionParallelism=======================>" + parallelism);
		DataStream<Long> filtered = nums.filter(new FilterFunction<Long>(){
			public boolean filter(Long value) throws Exception {
				return value % 2 == 0;
			}
	     });//.setParallelism(3);
		 int parallelismFilter = filtered.getParallelism();
		 System.out.println("parallelismFilter&&&&&&&&&&&&&&&&&&&&&&&&&&&>" + parallelismFilter);
	     filtered.print();
		// execute program
		env.execute("ParallelStreamingJob");
	}
}

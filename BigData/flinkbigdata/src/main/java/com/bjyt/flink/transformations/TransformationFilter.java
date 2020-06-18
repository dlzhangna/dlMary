package com.bjyt.flink.transformations;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/*
 * 对DataStream进行操作
 * */
public class TransformationFilter {
  public static void main(String[] args) throws Exception {
	  StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
	  DataStreamSource<Integer> nums = env.fromElements(1,2,3,4,5,6,7,8,9);
//	  SingleOutputStreamOperator<Integer> odd = nums.filter(new FilterFunction<Integer>(){
//		@Override
//		public boolean filter(Integer value) throws Exception {
//			  return value % 2 !=0;
//		}	  
//	  });
//	  odd.print();
	  //lamdba表达式
	  SingleOutputStreamOperator<Integer>  filteredNums = nums.filter(i-> i>=5);
//	  SingleOutputStreamOperator<Integer>  filteredNums = nums.filter(i->{
//		  return i>=5;
//	  });
	 
	  filteredNums.print();
	  env.execute("TransformationFilter");
   }
}

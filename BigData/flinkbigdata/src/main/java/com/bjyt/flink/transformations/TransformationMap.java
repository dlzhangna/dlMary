package com.bjyt.flink.transformations;

//import org.apache.flink.api.common.functions.MapFunction;
//import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.typeinfo.Types;
//import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/*
 * 对DataStream进行操作
 * */
public class TransformationMap {
  public static void main(String[] args) throws Exception {
	  StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
	  DataStreamSource<Integer> nums = env.fromElements(1,2,3,4,5);
//	  SingleOutputStreamOperator<Integer> res1 = nums.map(new MapFunction<Integer,Integer>(){
//		  public Integer map(Integer value)throws Exception{
//			  return value *2;
//		  }
//		  
//	  });
	  SingleOutputStreamOperator<Integer> res2 = nums.map(i->i*2).returns(Types.INT);//returns(Integer.class);
	  
	  //传入功能更加强大的RichMapFunction
//	  nums.map(new RichMapFunction<Integer,Integer>(){
//		  //open,在构造方法之后,map方法执行之前,执行一次，Configuration可以拿到全局配置
//		  //用来初始化一下连接,或者初始化state，或恢复历史state
//		  public void open(Configuration parameters) throws Exception{
//			  super.open(parameters);
//		  }
//		  //销毁之前,执行一次,通常是做资源释放
//		  public void close() throws Exception{
//			  super.close();
//		  }
//		  
//		  public Integer map(Integer value) throws Exception{
//			  return value * 10;
//		  }
//	  });
	  res2.print();
	  env.execute("TransformationMap");
   }
}

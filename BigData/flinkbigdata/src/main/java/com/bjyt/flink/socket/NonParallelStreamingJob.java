/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bjyt.flink.socket;

import java.util.Arrays;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Skeleton for a Flink Streaming Job.
 *
 * <p>For a tutorial how to write a Flink streaming application, check the
 * tutorials and examples on the <a href="https://flink.apache.org/docs/stable/">Flink Website</a>.
 *
 * <p>To package your application into a JAR file for execution, run
 * 'mvn clean package' on the command line.
 *
 * <p>If you change the name of the main class (with the public static void main(String[] args))
 * method, change the respective entry in the POM.xml file (simply search for 'mainClass').
 */
public class NonParallelStreamingJob {

	public static void main(String[] args) throws Exception {
		// set up the streaming execution environment
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		/*
		 * Here, you can start creating your execution plan for Flink.
		 *
		 * Start with getting some data from the environment, like
		 * 	env.readTextFile(textPath);
		 *
		 * then, transform the resulting DataStream<String> using operations
		 * like
		 * 	.filter()
		 * 	.flatMap()
		 * 	.join()
		 * 	.coGroup()
		 *
		 * and many more.
		 * Have a look at the programming guide for the Java API:
		 *
		 * https://flink.apache.org/docs/latest/apis/streaming/index.html
		 *
		 */
		//DataStream是一个抽象数据集
		//DataStream<String> socketTextStream = env.socketTextStream("localhost", 8888);
//		int parallelismSocket = socketTextStream.getParallelism();
//		System.out.println("**********************>" + parallelismSocket);
		//fromElements将客户端的集合并行化成一个抽象的数据集，通常用来做测试和实验
		//DataStreamSource<Integer> nums = env.fromElements(1,2,3,4,5,6,7,8,9);
		//parallelism = 1
		DataStreamSource<Integer> nums = env.fromCollection(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
		//get the number of DataStream's parallelism
		int parallelism = nums.getParallelism();
		System.out.println("fromCollectionParallelism=======================>" + parallelism);
		SingleOutputStreamOperator<Integer> filtered = nums.filter(new FilterFunction<Integer>(){
            public boolean filter(Integer value) throws Exception{
			   return value % 2 == 0;
		   }
	     });//.setParallelism(3);
		 int parallelismFilter = filtered.getParallelism();
		 System.out.println("parallelismFilter&&&&&&&&&&&&&&&&&&&&&&&&&&&>" + parallelismFilter);
	     filtered.print();
		// execute program
		env.execute("NonParallelStreamingJob");
	}
}

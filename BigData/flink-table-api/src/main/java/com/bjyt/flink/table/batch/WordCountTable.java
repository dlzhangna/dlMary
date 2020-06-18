package com.bjyt.flink.table.batch;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.BatchTableEnvironment;
import com.bjyt.flink.table.bean.WordCount;

public class WordCountTable {
	public static void main(String[] args)throws Exception{
		ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		BatchTableEnvironment tEnv = BatchTableEnvironment.create(env);
		//Simulate test data
		DataSet<WordCount> input = env.fromElements(
				new WordCount("Storm",1L),
				new WordCount("Spark",1L),
				new WordCount("Flink",1L),
				new WordCount("Spark",1L),
				new WordCount("Flink",1L),
				new WordCount("Flink",1L)
			);
	//Create table through DataSet
	Table table = tEnv.fromDataSet(input);
	Table filtered = table.groupBy("word")
			              .select("word,counts.sum as counts")
			              .filter("counts>=2")
			              .orderBy("counts.desc");
	//Transfer table to DataSet
	DataSet<WordCount> result = tEnv.toDataSet(filtered, WordCount.class);
	/*
	 * Flink:3
	 * Spark:2
	 */
	result.print();
	}

}

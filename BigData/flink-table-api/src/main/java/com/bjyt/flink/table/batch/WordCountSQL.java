package com.bjyt.flink.table.batch;


import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.BatchTableEnvironment;

import com.bjyt.flink.table.bean.WordCount;

public class WordCountSQL {
	public static void main(String[] args)throws Exception{
		ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		BatchTableEnvironment tEnv = BatchTableEnvironment.create(env);
		//simulate test data
		DataSet<WordCount> input = env.fromElements(
				new WordCount("Storm",1L),
				new WordCount("Spark",1L),
				new WordCount("Flink",1L),
				new WordCount("Spark",1L),
				new WordCount("Flink",1L),
				new WordCount("Flink",1L));
		tEnv.registerDataSet("WordCount", input,"word,counts");
		String sql = "SELECT word,SUM(counts) as counts FROM WordCount GROUP BY word " + "HAVING SUM(counts) >=2 ORDER BY counts DESC";
		Table table = tEnv.sqlQuery(sql);
		DataSet<WordCount> result = tEnv.toDataSet(table, WordCount.class);
		result.print();
	}

}

package com.bjyt.flink.table.bean;

public class WordCount {
  public String word;

  public Long count;
  
  public WordCount() {
	  
  }
  
  public WordCount(String word, Long count) {
		super();
		this.word = word;
		this.count = count;
	}
  
  @Override
  public String toString() {
  	return "WordCount [word=" + word + ", count=" + count + "]";
  }
}

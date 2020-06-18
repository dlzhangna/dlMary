package com.bjyt.flink.transformations;

public class WordCounts {
	//private String word;
	public String word;
	
	//private Long counts;
	public Long counts;
	
	public WordCounts() {
		
	}
	public WordCounts(String word, Long counts) {
		super();
		this.word = word;
		this.counts = counts;
	}
	
//	  
//  public String getWord() {
//		return word;
//	}
//	public void setWord(String word) {
//		this.word = word;
//	}
//	public Long getCounts() {
//		return counts;
//	}
//	public void setCounts(Long counts) {
//		this.counts = counts;
//	}
	
    public static WordCounts of(String word,Long counts) {
    	return new WordCounts(word,counts);
    }
	@Override
	public String toString() {
		return "WordCounts [word=" + word + ", counts=" + counts + "]";
	}
}

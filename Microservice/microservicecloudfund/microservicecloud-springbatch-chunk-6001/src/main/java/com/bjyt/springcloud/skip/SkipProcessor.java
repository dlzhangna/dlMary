package com.bjyt.springcloud.skip;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component("skipProcessor")
public class SkipProcessor implements ItemProcessor<String,String>{
 
   private int attemptCount = 0;
   
   @Override
   public String process(String item) throws Exception {
	     System.out.println("processing item " + item);
	     if(item.equalsIgnoreCase("23")) {
	       attemptCount++;
	       if(attemptCount >= 3) {
	    	 System.out.println("Retried " + attemptCount + " times success.");
	         return String.valueOf(Integer.valueOf(item) * -1);
	       }else {
	    	 System.out.println("Processed the " + attemptCount + " times fail.");
		     throw new CustomSkipableException("Process failed.Attempt: " + attemptCount);
	       }
	    }else {
	    	return String.valueOf(Integer.valueOf(item) * -1);
	    }
   }
}

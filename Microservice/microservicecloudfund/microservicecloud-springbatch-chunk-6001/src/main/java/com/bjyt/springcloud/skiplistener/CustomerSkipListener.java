package com.bjyt.springcloud.skiplistener;

import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

@Component("customerSkipListener")
public class CustomerSkipListener implements SkipListener<String,String>{
    
	@Override
	public void onSkipInRead(Throwable t) {
		
	}

	@Override
	public void onSkipInWrite(String item, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSkipInProcess(String item, Throwable t) {
		System.out.println(item + " got exception: " + t);
		
	}
}

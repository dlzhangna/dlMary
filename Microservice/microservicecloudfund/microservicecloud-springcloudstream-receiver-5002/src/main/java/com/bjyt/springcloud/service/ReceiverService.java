package com.bjyt.springcloud.service;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;
import com.bjyt.springcloud.common.IReceiverService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Service
@EnableBinding(IReceiverService.class)
public class ReceiverService {
	
	private static final Log LOGGER = LogFactory.getLog(ReceiverService.class);

    @StreamListener("dpb-exchange")
    public void onReceiver(byte[] msg){
    	LOGGER.info("Consumer:"+new String(msg));
    	System.out.println("Consumer:"+new String(msg));
    }
}

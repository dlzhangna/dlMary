package com.bjyt.springcloud.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import com.bjyt.springcloud.common.MyProcessor;

@Component
public class LogMessageListener {
	    private static final Log LOGGER = LogFactory.getLog(LogMessageListener.class);
	    
	    @StreamListener(MyProcessor.MESSAGE_INPUT)
	    @SendTo(MyProcessor.LOG_FORMAT_OUTPUT)
	    public String processLogMessage(String message) {
	    	LOGGER.info("Init message we received:" + message);
	    	System.out.println("Init message we received:" + message);
	        return "[" + message +"]";
	    }

	    /**
	     * 接收来自 MyProcessor.LOG_FORMAT_INPUT 的消息
	     * 也就是加工后的消息，也就是通过上面的 SendTo 发送来的
	     * 因为 MyProcessor.LOG_FORMAT_OUTPUT 和 MyProcessor.LOG_FORMAT_INPUT 是指向同一 exchange
	     * @param message
	     */
	    @StreamListener(MyProcessor.LOG_FORMAT_INPUT)
	    public void processFormatLogMessage(String message) {
	    	LOGGER.info("The message formatted we received:" + message);
	        System.out.println("The message formatted we received:" + message);
	    }
}

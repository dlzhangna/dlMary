package com.bjyt.springcloud.common;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

public interface ISendeService {
	/**
     * 指定输出的交换器名称
     * @return
     */
    @Output("dpb-exchange")
    SubscribableChannel send();
}  

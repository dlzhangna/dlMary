package com.bjyt.springcloud.common;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface IReceiverService {
	/**
     * 指定接收的交换器名称
     * @return
     */
    @Input("dpb-exchange")
    SubscribableChannel receiver();
}

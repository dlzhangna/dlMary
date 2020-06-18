package com.bjyt.springcloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjyt.springcloud.common.MyProcessor;

@RestController
@EnableBinding(value = {MyProcessor.class})
public class MessageSenderController {
	@Autowired
    private MyProcessor myProcessor;

    @GetMapping(value = "sendLogMessage")
    public void sendLogMessage(String message){
        Message<String> stringMessage = org.springframework.messaging.support.MessageBuilder.withPayload(message).build();
        myProcessor.logOutput().send(stringMessage);
    }
}

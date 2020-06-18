package com.bjyt.springcloud;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjyt.springcloud.common.MyProcessor;
import com.bjyt.springcloud.controller.MessageSenderController;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MessageSenderController.class)
@RestController
@EnableBinding(value = { MyProcessor.class })
public class StreamLogTest {
	@Autowired
	private MyProcessor myProcessor;

	@GetMapping(value = "sendLogMessage")
	public void sendLogMessage(String message) {
	  Message<String> stringMessage = org.springframework.messaging.support.MessageBuilder.withPayload(message).build();
	  myProcessor.logOutput().send(stringMessage);
	}
}

package com.bjyt.springcloud.writer;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class ServerStartCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		//boolean serverStartFlag = ApplicationRunnerImpl.serverStartFlag;
		System.out.println("serverStartFlag:");
		return true;
	}

}

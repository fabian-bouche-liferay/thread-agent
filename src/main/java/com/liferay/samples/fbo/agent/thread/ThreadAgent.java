package com.liferay.samples.fbo.agent.thread;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.ThreadFactory;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.matcher.ElementMatchers;

public class ThreadAgent {

	public static void premain(String argument, Instrumentation inst) {

		System.out.println("Thread Agent premain");
		
        new AgentBuilder.Default()
        	.type(ElementMatchers.isSubTypeOf(ThreadFactory.class))
        	.transform(
        			new Transformer.ForAdvice().advice(ElementMatchers.named("newThread"),
        			ThreadInfoAdvice.class.getName()))
        	.installOn(inst);

	}
	
}

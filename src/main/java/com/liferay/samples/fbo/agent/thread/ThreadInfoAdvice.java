package com.liferay.samples.fbo.agent.thread;

import net.bytebuddy.asm.Advice;

public class ThreadInfoAdvice {

    @Advice.OnMethodEnter
    static ParentThreadInfo invokeBeforeEnterMethod() {
    	
    	ParentThreadInfo parentThreadInfo = new ParentThreadInfo(
    			Thread.currentThread().getName(),
    			Thread.currentThread().getStackTrace());
    	
        return parentThreadInfo;
    }
    
    @Advice.OnMethodExit
    static void invokeAfterExitMethod(@Advice.Enter ParentThreadInfo parentThreadInfo, @Advice.Return Thread newThread) {
    	
    	System.out.println("Thread [" + newThread.getName() + "] was created by parent thread [" + parentThreadInfo.getName() + "] at:");
    	
    	StackTraceElement[] stackTrace = parentThreadInfo.getStackTrace();

    	for(int i = 0; i < stackTrace.length; i++) {
        	System.out.println(" - " + stackTrace[i]);
    	}
    	
    }
	
}

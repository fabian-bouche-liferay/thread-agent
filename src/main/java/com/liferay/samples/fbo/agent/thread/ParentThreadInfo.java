package com.liferay.samples.fbo.agent.thread;

public class ParentThreadInfo {
	
	private String _name;
	private StackTraceElement[] _stackTrace;
	
	public ParentThreadInfo(String name, StackTraceElement[] stackTrace) {
		_name = name;
		_stackTrace = stackTrace;
	}
	
	public String getName() {
		return _name;
	}
	
	public StackTraceElement[] getStackTrace() {
		return _stackTrace;
	}
	
}
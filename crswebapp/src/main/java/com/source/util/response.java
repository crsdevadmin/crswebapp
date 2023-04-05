package com.source.util;


public class response {
	private boolean success;
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	private Object data;
	
	private exception exception;
	public exception getException() {
		return exception;
	}
	public void setException(exception exception) {
		this.exception = exception;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
}

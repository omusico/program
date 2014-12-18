package com.dabaicong.webClient;

public class WebServerException extends Exception {
	
	public WebServerException() {
		super();
	}
	
	public WebServerException(String error) {
		super(error);
	}
	
	public WebServerException(Throwable t) {
		super(t);
	}
	
	public WebServerException(String error, Throwable t) {
		super(error, t);
	}
	
}
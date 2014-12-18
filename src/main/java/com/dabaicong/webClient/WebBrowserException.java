package com.dabaicong.webClient;

public class WebBrowserException extends Exception {
	
	public WebBrowserException() {
		super();
	}
	
	public WebBrowserException(String error) {
		super(error);
	}
	
	public WebBrowserException(Throwable t) {
		super(t);
	}
	
	public WebBrowserException(String error, Throwable t) {
		super(error, t);
	}
	
}

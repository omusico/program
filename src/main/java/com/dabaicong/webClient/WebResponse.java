package com.dabaicong.webClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WebResponse {
	
	private static final Log logger = LogFactory.getLog(WebResponse.class);
	private static final String DEFAULT_ENCODING = "UTF-8";
	
	private final String responseUrl;
	private final byte[] bodyAsBytes;
	private final String encoding;
	
	public WebResponse(String responseUrl, byte[] bodyAsBytes) {
		this(responseUrl, bodyAsBytes, DEFAULT_ENCODING);
	}
	
	public WebResponse(String responseUrl, byte[] bodyAsBytes, String encoding) {
		this.responseUrl = responseUrl; 
		this.bodyAsBytes = bodyAsBytes;
		this.encoding = encoding;
	}
	
	public String getResponseUrl() {
		return responseUrl;
	}
	
	public String getEncoding() {
		return encoding;
	}
	
	public String getBodyAsString() {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Convert Encoding=" + encoding);
			}
			return new String(bodyAsBytes, encoding);
		} catch (UnsupportedEncodingException e) {
			logger.error("Encoding Error[encoding=" + encoding + "]", e);
			try {
				return new String(bodyAsBytes, DEFAULT_ENCODING);
			} catch (UnsupportedEncodingException e1) {
				logger.error("Encoding Error[encoding=" + DEFAULT_ENCODING + "]", e);
				return new String(bodyAsBytes);
			}
		}
	}
	
	public byte[] getBodyAsBytes() {
		return bodyAsBytes;
	}
	
	public InputStream getBodyAsStream() {
		return new ByteArrayInputStream(bodyAsBytes);
	}
}

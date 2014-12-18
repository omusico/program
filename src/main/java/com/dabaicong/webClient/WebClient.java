package com.dabaicong.webClient;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WebClient {
	private static final Log logger = LogFactory.getLog(WebClient.class);
	private static final String DEFAULT_ENCODING = "UTF-8";
	
	private HttpClient httpClient;
	protected String encoding;
	protected int bufferLength = 4096;
	protected String lastVisitUrl;
	
	
	public WebClient() {
		this(DEFAULT_ENCODING);
	}
	
	public WebClient(String encoding) {
		this.encoding = encoding;
		httpClient = new HttpClient();
		httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encoding);
		httpClient.getParams().setParameter(HttpMethodParams.SINGLE_COOKIE_HEADER, true);
	}
	
	//----------- callable methods -------------//
	public WebResponse doGet(String url) throws WebBrowserException, WebServerException {
		return doGet(url, "");
	}
	
	public WebResponse doGet(String url, String referer) throws WebBrowserException, WebServerException {
		try {
			GetMethod getMethod = new GetMethod(url);
			setHttpRequestHeader(getMethod);
			if (referer != null && referer.trim().length() != 0) {
				getMethod.setRequestHeader("Referer", referer);
			}
			logHttpGetRequest(getMethod);
			int status =httpClient.executeMethod(getMethod);
			String strResp = getMethod.getResponseBodyAsString();
			byte[] byteResp = getMethod.getResponseBody();
			String respEnc = getResponseEncoding(getMethod);
			logHttpResponse(getMethod, strResp);
			getMethod.releaseConnection();
			
			//http:301,302,303,307
			if (status == HttpStatus.SC_MOVED_PERMANENTLY || 
				status == HttpStatus.SC_MOVED_TEMPORARILY ||
				status == HttpStatus.SC_SEE_OTHER ||
				status == HttpStatus.SC_TEMPORARY_REDIRECT) {
				Header locationHeader = getMethod.getResponseHeader("Location");
				String location = locationHeader.getValue();
				if (logger.isDebugEnabled()) {
					logger.debug("Redirect To Location = " + location);
				}
		        if (location.startsWith("http")) {
		        	return doGet(location);
		        } else {
		        	return doGet("http://" + getResponseHost(getMethod) + location);
		        }
			} else if (status == HttpStatus.SC_OK) { //http:200
				return new WebResponse(getMethod.getURI().toString(), byteResp, respEnc);
			} else {
				throw new WebServerException("Server Exception[code=" + status + "]");
			}
		} catch (HttpException e) { 
			throw new WebBrowserException(e);
		} catch (IOException e) {
			throw new WebBrowserException(e);
		}
	}
	
	public WebResponse doPost(String url, NameValuePair[] params) throws WebBrowserException, WebServerException {
		return doPost(url, params, "");
	}
	
	public WebResponse doPost(String url, NameValuePair[] params, String referer) throws WebBrowserException, WebServerException {
		try {
			PostMethod postMethod = new PostMethod(url);
			setHttpRequestHeader(postMethod);
			if (referer != null && referer.trim().length() != 0) {
				postMethod.setRequestHeader("Referer", referer);
			}
			postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			postMethod.setRequestBody(params);
			logHttpPostRequest(postMethod);
			int status = httpClient.executeMethod(postMethod);
			String strResp = postMethod.getResponseBodyAsString();
			byte[] byteResp = postMethod.getResponseBody();
			String respEnc = getResponseEncoding(postMethod);
			logHttpResponse(postMethod, strResp);
			postMethod.releaseConnection();
			
			//http:301,302,303,307
			if (status == HttpStatus.SC_MOVED_PERMANENTLY || 
				status == HttpStatus.SC_MOVED_TEMPORARILY ||
				status == HttpStatus.SC_SEE_OTHER ||
				status == HttpStatus.SC_TEMPORARY_REDIRECT) {
				Header locationHeader = postMethod.getResponseHeader("Location");
				String location = locationHeader.getValue();
				if (logger.isDebugEnabled()) {
					logger.debug("Redirect To Location = " + location);
				}
		        if (location.startsWith("http")) {
		        	return doGet(location);
		        } else {
		        	return doGet("http://" + getResponseHost(postMethod) + location);
		        }
			} else if (status == HttpStatus.SC_OK) { //http:200
				return new WebResponse(postMethod.getURI().toString(), byteResp, respEnc);
			} else {
				throw new WebServerException("Server Exception[code=" + status + "]");
			}
		} catch (HttpException e) { 
			throw new WebBrowserException(e);
		} catch (IOException e) {
			throw new WebBrowserException(e);
		}
	}
	
	public Cookie[] getCurrentCookies() {
		Cookie[] cookies = httpClient.getState().getCookies();
		return cookies;
	}
	
	protected void setHttpRequestHeader(HttpMethod method) {
        method.setRequestHeader("Accept",
                "text/html,application/xhtml+xml,application/xml,application/json,image/jpeg,image/gif,*/*");
        method.setRequestHeader("Accept-Language", "zh-cn");
        method.setRequestHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
        method.setRequestHeader("Accept-Charset", encoding);
        method.setRequestHeader("Keep-Alive", "300");
        method.setRequestHeader("Connection", "Keep-Alive");
        method.setRequestHeader("Cache-Control", "no-cache");
    }
	
	//------------- log methods ----------------//
	private void logHttpGetRequest(HttpMethod method) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("/n/n============= HTTP Request Start =============");
				logger.debug("HTTP Get Request URL ==>/n" + method.getURI().toString());
				logger.debug("HTTP Get Request Headers ==>/n" + getHttpRequestHeader(method));
				logger.debug("HTTP Get Request Cookies ==>/n" + getHttpCookie());
				logger.debug("HTTP Get Request QueryString ==>/n" + method.getQueryString());
				logger.debug("============= HTTP Request End =============/n/n");
			}
		} catch (URIException e) {
			logger.error(e);
		}
	}
	
	private void logHttpPostRequest(PostMethod method) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("/n/n============= HTTP Request Start =============");
				logger.debug("HTTP Post Request URL ==>/n" + method.getURI().toString());
				logger.debug("HTTP Post Request Headers ==>/n" + getHttpRequestHeader(method));
				logger.debug("HTTP Post Request Cookies ==>/n" + getHttpCookie());
				logger.debug("HTTP Post Request QueryString ==>/n" + method.getQueryString());
				logger.debug("HTTP Post Request Body ==>/n" + getHttpRequestBody(method));
				logger.debug("============= HTTP Request End =============/n/n");
			}
		} catch (URIException e) {
			logger.error("URIException", e);
		}
	}
	
	private void logHttpResponse(HttpMethod method, String strResp) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("/n/n============= HTTP Response Start =============");
				logger.debug("HTTP Response URL ==>/n" + method.getURI().toString());
				logger.debug("HTTP Response Headers ==>/n" + getHttpResponseHeader(method));
		        logger.debug("HTTP Response Cookies ==>/n" + getHttpCookie());
		        logger.debug("HTTP Response Body ==>/n" + strResp);
		        logger.debug("============= HTTP Response End =============/n/n");
			}
		} catch (URIException e) {
			logger.error("URIException", e);
		}
	}
	
	//---------- util methods -------------//
	private String getResponseEncoding(HttpMethod method) {
		Header header = method.getResponseHeader("Content-Type");
		String encoding = DEFAULT_ENCODING;
		if (header != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Content-Type=" + header.getValue());
			}
			if (header != null) {
				String value = header.getValue();
				int idx1 = value.indexOf("charset=");
				if (idx1 > -1) {
					encoding = value.substring(idx1 + 8);
					if (logger.isDebugEnabled()) {
						logger.debug("Response Encoding=" + encoding);
					}
				}
			}
		}
		return encoding;
	}
	
	private String getResponseHost(HttpMethod method) {
		try {
			String hostRegexp = "http://([^/]+)/.*";
			Pattern p = Pattern.compile(hostRegexp);
			String url = method.getURI().toString();
			Matcher matcher = p.matcher(url);
		    if (!matcher.find()) {
		    	String msg =  "[Type=ResponseHost,Regexp=" + p.pattern() + "]";
		        logger.error(msg);
		    }
		    if (logger.isDebugEnabled()) {
		    	logger.debug("Host=" + matcher.group(1));
		    }
		    return matcher.group(1);
		} catch (Exception e) {
			logger.error(e);
		}
		return "";
	}
	
	private String getHttpRequestBody(PostMethod method) {
		StringBuilder strBody = new StringBuilder();
		NameValuePair[] pairs = method.getParameters();
		for (NameValuePair pair : pairs) {
			String name = pair.getName();
			String value = pair.getValue();
			strBody.append(name + "=" + value + ";");
		}
		return strBody.toString();
	}
	
	private String getHttpCookie() {
		StringBuilder strHeader = new StringBuilder();
		Cookie[] cookies = httpClient.getState().getCookies();
		for (Cookie cookie : cookies) {
			String domain = cookie.getDomain();
			String path = cookie.getPath();
			String name = cookie.getName();
			String value = cookie.getValue();
			Date expired = cookie.getExpiryDate();
			boolean isSecure = cookie.getSecure();
			strHeader.append("domain=" + domain + ";");
			strHeader.append("path=" + path + ";");
			strHeader.append(name + "=" + value + ";");
			if (expired != null) {
				strHeader.append("expired=" + expired.toGMTString() + ";");
			}
			strHeader.append("isSecure=" + isSecure+ "/n");
		}
		return strHeader.toString();
	}
	
	private String getHttpRequestHeader(HttpMethod method) {
		StringBuilder strHeader = new StringBuilder();
		Header[] headers = method.getRequestHeaders();
		for (Header header : headers) {
			String name = header.getName();
			String value = header.getValue();
			strHeader.append(name + "=" + value + ";");
		}
		return strHeader.toString();
	}
	
	private String getHttpResponseHeader(HttpMethod method) {
		StringBuilder strHeader = new StringBuilder();
		Header[] headers = method.getResponseHeaders();
		for (Header header : headers) {
			String name = header.getName();
			String value = header.getValue();
			strHeader.append(name + "=" + value + ";");
		}
		return strHeader.toString();
	}
}

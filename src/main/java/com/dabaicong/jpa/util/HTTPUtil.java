package com.dabaicong.jpa.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class HTTPUtil {
	private static Logger logger=LoggerFactory.getLogger(HTTPUtil.class);
    private static final String APPLICATION_JSON = "application/json";
    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";
    
	/** 回应超时时间, 由bean factory设置，缺省为30秒钟 */
	private static int defaultSoTimeout = 30000;
	/** 连接超时时间，由bean factory设置，缺省为8秒钟 */

	private static String DEFAULT_CHARSET = CharsetConstant.CHARSET_UTF8;


	private static MultiThreadedHttpConnectionManager connectionManager = null;

	private static int connectionTimeOut = 30000;
	private static int socketTimeOut = 30000;
	private static int maxConnectionPerHost = 100;
	private static int maxTotalConnections = 100;

	private static HttpClient client;

	static {
		connectionManager = new MultiThreadedHttpConnectionManager();
		connectionManager.getParams().setConnectionTimeout(connectionTimeOut);
		connectionManager.getParams().setSoTimeout(socketTimeOut);
		connectionManager.getParams().setDefaultMaxConnectionsPerHost(maxConnectionPerHost);
		connectionManager.getParams().setMaxTotalConnections(maxTotalConnections);
		client = new HttpClient(connectionManager);
	}
	

	private static String[] httpProxyHeaderName = new String[] {
		"CDN-SRC-IP",
		"HTTP_CDN_SRC_IP",
		"X-FORWARDED-FOR"
	};
	/**
	 * 获得ip
	 * */
	public static String getIP(HttpServletRequest request) {
		for (String headerName : httpProxyHeaderName) {
			String clientIP = request.getHeader(headerName);
			if (StringUtils.isNotEmpty(clientIP)) {
				return clientIP;
			}
		}
		return request.getRemoteAddr();
	}
	
	/**
	 * @描述:解析key=value&key=value的键值
	 * @param contents
	 * @param encoding
	 * @return
	 */
	protected static Map<String, String> parseQueryString(String contents) {
		Map<String, String> map = new HashMap<String, String>();
		String[] keyValues = contents.split("&");
		for (int i = 0; i < keyValues.length; i++) {
			String key = keyValues[i].substring(0, keyValues[i].indexOf("="));
			String value = keyValues[i].substring(keyValues[i].indexOf("=") + 1);
			map.put(key, value);
		}
		return map;
	}
	
	/**
	 * httpclient post请求
	 * @param url    请求地址
	 * @param parameters 请求参数
	 * @return    返回结果集
	 * @throws Exception 
	 * @throws HttpException
	 * @throws IOException
	 */
	protected static String post(String url,NameValuePair[] parameters,String charSet,int timeOut) throws Exception{
		String resultStr = null;
		PostMethod postMethod = null;
	     try {
	    	    postMethod = new PostMethod(url);
		 		postMethod.getParams().setCredentialCharset(DEFAULT_CHARSET);
				postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charSet);
				postMethod.addParameters(parameters);
				int statusCode = client.executeMethod(postMethod);
				 if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
			            // 从头中取出转向的地址
			            Header locationHeader = postMethod.getResponseHeader("location");
			            String location = null;
			            if (locationHeader != null) {
			                location = locationHeader.getValue();
			                logger.error("The page was redirected to:" + location);
			            } else {
			            	logger.error("Location field value is null.");
			            }
			            return resultStr;
			        } else if(statusCode==HttpStatus.SC_OK){
						String responseEncoding = postMethod.getResponseCharSet();
						if(responseEncoding==null||responseEncoding.trim().length()==0){
							responseEncoding = charSet;
						}
						InputStream is = postMethod.getResponseBodyAsStream();  
						//这里的编码规则要与上面的相对应  
						BufferedReader br = new BufferedReader(new InputStreamReader(is,responseEncoding));  
						StringBuffer sb = new StringBuffer();  
						//String tempbf = null;
						
					/*	while (true) {
							tempbf = br.readLine();
							if (tempbf == null) {
								break;
							} else {
								sb.append(tempbf);  
							}
						}*/
						int charCount = -1;
						while ((charCount = br.read()) != -1) {
							sb.append((char) charCount);//如果对方数据有空格，可以保留空格
			    		}
						resultStr = sb.toString();
						logger.debug("POST请求响应内容："+resultStr);
						br.close();
						responseEncoding = null;
				 }else{
					 logger.error("http请求url={},返回code:{}错误",new Object[]{url,postMethod.getStatusCode()});
				 }
				
			} catch (HttpException e) {
				logger.error("发生致命的异常，可能是协议不对或者返回的内容有问题,post请求url:"+url,e);
			} catch (IOException e) {
				logger.error("发生网络异常,请求url:"+url,e);
			}
		 	if (postMethod != null) {
				postMethod.releaseConnection();
				//postMethod = null;
			}
			return resultStr;
	}

	/**
	 * 发送http请求
	 * 
	 * @param url
	 *            请求地址，param请求参数
	 * @throws Exception
	 * */
	public static String post(String url, String param) {
		String resultStr = null;
		OutputStreamWriter reqOut = null;
		InputStream in = null;
		try {
			URL reqUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) reqUrl.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");// 请求提交方法
			connection.setConnectTimeout(30 * 1000);
			connection.setReadTimeout(30 * 1000);
			connection.setRequestProperty("Content-Type", "text/xml");  
			
//	conn.setRequestProperty("accept", "*/*");
	//		conn.setRequestProperty("connection", "Keep-Alive");
		//	conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
		
			if (param != null) {
				reqOut = new OutputStreamWriter(connection.getOutputStream(), CharsetConstant.CHARSET_UTF8);
				reqOut.write(param);
				reqOut.flush();
			}
			int charCount = -1;
			int status=connection.getResponseCode();
			if(status==200){
				in = connection.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(in, CharsetConstant.CHARSET_UTF8));
				StringBuffer responseMessage = new StringBuffer();
				while ((charCount = br.read()) != -1) {
					responseMessage.append((char) charCount);
				}
				resultStr = responseMessage.toString();
			}else{
				 logger.error("http请求url={},返回code:{}错误",new Object[]{url,status});
			}
	
		} catch (Exception e) {
			logger.error("发生网络异常,请求url:" + url, e);
		} finally {
			try {
				if (reqOut != null)
					reqOut.close();
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {

			}
		}
        return resultStr;
	}
	
	/**
	 * http发送json请求
	 * 
	 * @param url
	 *            请求地址，
	 * @param json 原始json串
	 * @throws Exception
	 * */

	public static String postJson(String url, String json) {
		String resultStr = null;
		try {
			String encoderJson = URLEncoder.encode(json, HTTP.UTF_8);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);

			StringEntity se = new StringEntity(encoderJson);
			se.setContentType(CONTENT_TYPE_TEXT_JSON);
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
			httpPost.setEntity(se);
			HttpResponse response = httpClient.execute(httpPost);
			int code = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			if (code == HttpStatus.SC_OK) {
				/*
				 * InputStream is=entity.getContent(); BufferedReader br = new
				 * BufferedReader(new InputStreamReader(is,HTTP.UTF_8));
				 * StringBuffer sb = new StringBuffer(); int charCount = -1;
				 * while ((charCount = br.read()) != -1) { sb.append((char)
				 * charCount);//如果对方数据有空格，可以保留空格 }
				 */
				resultStr = EntityUtils.toString(entity);
			} else {
				logger.error("发送json请求url={},返回：code={},完整值为:{}", new Object[] { url, code ,EntityUtils.toString(entity)});
			}
		} catch (Exception e) {
			logger.error("发生网络异常,请求url:" + url, e);
		}
		return resultStr;
	}
	/**
	 * 发送http请求
	 * @param url 请求地址，param请求参数
	 * @throws Exception 
	 * */
	public static String get(String url, String param) throws Exception {
        try{
        	URL reqUrl = new URL(url);
    		final HttpURLConnection connection = (HttpURLConnection) reqUrl.openConnection();
    		connection.setDoOutput(true);
    		connection.setRequestMethod("GET");
    		connection.setConnectTimeout(30* 1000);
    		connection.setReadTimeout(30* 1000);
    		OutputStreamWriter reqOut = null;
    		if (param != null) {
    			reqOut = new OutputStreamWriter(connection.getOutputStream(),CharsetConstant.CHARSET_UTF8);
    			reqOut.write(param);
    			reqOut.flush();
    		}
    		int charCount = -1;
    		InputStream in = connection.getInputStream();
    		BufferedReader br = new BufferedReader(new InputStreamReader(in, CharsetConstant.CHARSET_UTF8));
    		StringBuffer responseMessage = new StringBuffer();
    		while ((charCount = br.read()) != -1) {
    			responseMessage.append((char) charCount);
    		}
    		in.close();
    		if (reqOut != null)
    			reqOut.close();
    		return responseMessage.toString();
        }catch(Exception e){
        	throw e;
        }
		
	}
	
	/**
	 * get发送消息
	 * @param strUrl
	 * @param map
	 * @return
	 * @throws IOException
	 */
	 public static String get(String url, String paramStr, int timeout, String charset) throws Exception{
			StringBuilder sb = null;
			BufferedReader br = null;
			try{
				sb = new StringBuilder();
				URL u = new URL(url+"?"+paramStr);
				HttpURLConnection uc = (HttpURLConnection) u.openConnection();
				uc.setConnectTimeout(30000);
				uc.setDoOutput(true);
				uc.setRequestMethod("GET");
				uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset="+charset);
				String line = null;
				br = new BufferedReader(new InputStreamReader(uc.getInputStream(),charset));
				while ((line = br.readLine()) != null) {
					sb.append(line);
					sb.append("\n");
				}
			}catch(Exception ex){
				throw ex;
			}finally{
			   try{
			       if(br!=null){
			          br.close();
			          br=null;
			       }
			    }catch(IOException ex){
			       ex.printStackTrace();
			    }
		    }
			return sb.toString();
		}
	 

		public static  String sendPostMsg(String url,Map<String,String> paramMap) throws Exception{
				NameValuePair[] parameters = generatNameValuePair(paramMap);
				String result =post(url, parameters,CharsetConstant.CHARSET_UTF8,defaultSoTimeout);
				return result;
			
		}
		
		public static String sendPostMsg(String url,String param) throws Exception{
			return sendPostMsg(url, parseQueryString(param));
		}
		public static String sendPostMsg(String url,String param,String charSet,int timeOut) throws Exception{
			return sendPostMsg(url, parseQueryString(param), charSet, timeOut);
			
		}
		public static String sendPostMsg(String url,Map<String,String> paramMap,String charSet,int timeOut) throws Exception{
			return post(url, generatNameValuePair(paramMap),charSet,defaultSoTimeout);
		}
	
	 protected static NameValuePair[] generatNameValuePair(Map<String, String> properties) {
		    if(properties==null||properties.size()==0){
		    	return null;
		    }
	        NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
	        int i = 0;
	        for (Map.Entry<String, String> entry : properties.entrySet()) {
	            nameValuePair[i++] = new NameValuePair(entry.getKey(), entry.getValue());
	        }
	        return nameValuePair;
	    }
	 
	 public static String sendSocket(String url,int port,String message,String charSet,int timeOut){
		 Socket soc =null;
		 try{
			 if(StringUtils.isBlank(charSet))
			 charSet=CharsetConstant.CHARSET_UTF8;
			 if(timeOut==0){
				 timeOut=defaultSoTimeout;
			 }
			 soc=new  Socket(url,port);
			 OutputStream out = soc.getOutputStream();
				soc.setSoTimeout(timeOut);
				// 接收服务器的反馈
				BufferedReader br = new BufferedReader(new InputStreamReader(soc.getInputStream(),charSet));
				// 向服务器发送请求
				out.write(message.getBytes());
				soc.shutdownOutput();
				br.close();
				String msg = null;
				while ((msg = br.readLine()) != null) {
					return msg;
				}
		 }catch(Exception e){
			 //logger.error("发送socket请求出错",e);
			 logger.error("socket请求url={}，port={}错误，原因是：{}",new Object[]{url,port,e.getMessage()});
		 }finally{
				 try {
					 if(soc!=null)
					soc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		 }
		 return null;
	 }
	 
	 public static String sendMsgCommon(byte[] command, String ip, int port,
				int length) throws Exception {
			Socket socket = null;
			byte[] temp = new byte[length];
			OutputStream o = null;
			InputStream in = null;
			try {
				socket = new Socket(ip, port);
				socket.setSoTimeout(10000);
				o = socket.getOutputStream();
				in = socket.getInputStream();
				o.write(command);
				o.flush();
				in.read(temp);
				if (socket != null)
					socket.close();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (o != null)
						o.close();
					if (in != null)
						in.close();
					if (socket != null)
						socket.close();
				} catch (IOException e) {
				}
			}
			 String t = new String(temp);
			return t;
		}
}

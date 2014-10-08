package com.dabaicong.jpa.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DyjUtil {

	private Logger logger = LoggerFactory.getLogger(DyjUtil.class);
	
	/**
	 * 发送请求获取返回参数
	 * @throws Exception 
	 */
	public DyjResult getResponse(DyjParam reqparam) throws Exception {
		String requestUrl = "http://t.zc310.net:8089/bin/LotSaleHttp.dll";
		String agentCode = "3897";
		String key = "a8b8c8d8e8f8g8h8";
		String parms = getParms(reqparam, agentCode, key);
		//System.out.println(parms);
		String result = HTTPUtil.sendPostMsg(requestUrl, parms, "GB2312", 30000);
		//System.out.println(result);
		boolean xml = false;
		if(reqparam.getWaction().equals(DyjActionCode.DL_TO_SELL_LUCKYRACINGPRIZE.value())) {
			xml = true;
		}
		DyjResult actionResult = covertToActionResult(result, xml);
		if(!compareSign(actionResult, key)) {
			logger.error("大赢家MD5校验错误");
			throw new Exception("大赢家MD5校验错误");
		}
		return actionResult;
	}
	
	/**
	 * 返回投注请求参数串 格式：
	 * @throws Exception 
	 * 
	 */
	protected String getParms(DyjParam reqparam, String wAgent, String agentKey) throws Exception {
		StringBuffer bf = new StringBuffer();
		bf.append("wAgent=").append(wAgent).append("&wAction=")
				.append(reqparam.getWaction()).append("&wMsgID=")
				.append(reqparam.getWmsgID()).append("&wParam=")
				.append(reqparam.getWparam());
		try {
			bf.append("&wSign=").append(getSign(reqparam, wAgent, agentKey));
		} catch (IOException e) {
			logger.error("大赢家md5加密出错");
			throw new Exception("大赢家md5加密出错");
		}
		return bf.toString();
	}
	
	/**
	 * 生成加密字段
	 * 
	 * @param reqparam
	 * @return
	 * @throws IOException
	 */
	protected String getSign(DyjParam reqparam, String wAgent, String agentKey) throws IOException {
		String sign = "";
		sign += wAgent;
		sign += reqparam.getWaction();
		sign += reqparam.getWmsgID();
		sign += reqparam.getWparam();
		sign += agentKey;
		String md5 = MD5Util.toMd5(sign, "GB2312");
		logger.debug("sign is "+sign+"sign MD5 is " + md5);
		return md5;
	}
	
	@SuppressWarnings("unchecked")
	protected DyjResult covertToActionResult(String content,boolean xml) throws Exception {
		DyjResult actionresult = new DyjResult();
		Document doc = DocumentHelper.parseText(content);
		// 解析XML
		Element root = doc.getRootElement();
		List<Element> elements = root.elements();
		for (Iterator<Element> it = elements.iterator(); it.hasNext();) {
			Element element = it.next();
			if (element.getName().endsWith("xMsgID")) {
				actionresult.setXmsgID(element.getTextTrim());
			} else if (element.getName().endsWith("xCode")) {
				actionresult.setXcode(element.getTextTrim());
			} else if (element.getName().endsWith("xMessage")) {
				actionresult.setXmessage(element.getTextTrim());
			} else if (element.getName().endsWith("xSign")) {
				actionresult.setXsign(element.getTextTrim());
			} else if (element.getName().endsWith("xValue")) {
				if(xml) {
					actionresult.setXvalue(element.selectSingleNode("issue").asXML());
				}else {
					actionresult.setXvalue(element.asXML().replace("<xValue>", "").replace("</xValue>", "").replace("<xValue/>", ""));
				}
			}
		}
		logger.info("actionresult: " + actionresult.toString());
		return actionresult;
	}
	
	protected boolean compareSign(DyjResult actionResult, String agentkey) throws IOException {
		String sign = "";
		sign = actionResult.getXmsgID() + actionResult.getXcode() + actionResult.getXvalue() + agentkey;
		return actionResult.getXsign().equals(MD5Util.toMd5(sign, "GB2312"));
	}
	
}

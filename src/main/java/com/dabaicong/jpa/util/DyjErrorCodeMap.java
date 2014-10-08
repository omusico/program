package com.dabaicong.jpa.util;

import java.util.HashMap;
import java.util.Map;


public class DyjErrorCodeMap {
	public static Map<String, String> errorCodeMap = new HashMap<String, String>();
	static{
		//  0 表示彩票平台已经接受代理商投注请求并正在准备出票，但不一定能出票成功
		errorCodeMap.put("0", "成功");
		//	1 表示彩票平台已经接受代理商投注请求并已经完成出票，即销售成功
		errorCodeMap.put("1", "成功");
		errorCodeMap.put("100", "登录失败");
		errorCodeMap.put("101", "参数错误");
		errorCodeMap.put("102", "签名验证失败");
		errorCodeMap.put("103", "不支持的功能请求");
		errorCodeMap.put("104", "签名验证失败，帐户被临时冻结");
		errorCodeMap.put("198", "系统忙");
		errorCodeMap.put("199", "调用彩票平台的IP地址不是代理商绑定的投注IP地址");
		errorCodeMap.put("1001", "彩票玩法不存在或禁止销售");
		errorCodeMap.put("1002", "彩票期号不存在或已过期");
		errorCodeMap.put("1003", "代理商帐户余额不足");
		errorCodeMap.put("1004", "代理商不存在或已经被冻结");
		errorCodeMap.put("1005", "投注号码格式错误");
		errorCodeMap.put("1006", "投注金额不相符。代理商提交的投注金额跟销售平台计算出的投注金额不相等");
		errorCodeMap.put("1007", "系统限号，投注失败");
		errorCodeMap.put("1008", "代理商订单号重复");
		errorCodeMap.put("1009", "代理商订单号格式错误");
		errorCodeMap.put("1010", "交易参数包数据错误");
		errorCodeMap.put("1011", "代理商未开通该彩票玩法");
		errorCodeMap.put("1012", "投注倍数超过最大倍数限制");
		errorCodeMap.put("1013", "单次投注注数超过规定限额");
		errorCodeMap.put("1014", "某场次比赛未开售其投注方式未开售");
		errorCodeMap.put("1015", "单注金额超过2万限制");
		errorCodeMap.put("2001", "代理商订单号不存在");
		errorCodeMap.put("2002", "投注中");
		errorCodeMap.put("2003", "投注失败，订单取消");
		errorCodeMap.put("2004", "查询结果为空");
		errorCodeMap.put("2005", "票号为空");
		errorCodeMap.put("2006", "结算中");
		errorCodeMap.put("2007", "代理商订单号已存在，但投注失败");
		errorCodeMap.put("8001", "系统错误导致数据保存失败");
		errorCodeMap.put("8002", "系统不支持的功能，请使用客户端软件操作");
		errorCodeMap.put("9999", "系统未知异常");
		
	}
	
	//	根据key获得错误原因
	public static String getByErrorCode(String key){
		if(StringUtil.isBlank(key)||key.length()>=5){
			return null;
		}
		return errorCodeMap.get(key);
	}
}

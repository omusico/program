package com.dabaicong.jpa.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class DyjParam {

	/** 交易消息序号（合作系统生成），建议使用时间戳或交易流水号 (20位 必填) */
	private String wmsgID = "";
	/** 业务参数 (必填) */
	private String wparam = "";
	/** 业务参数 (必填) */
	private String waction = "";

	public DyjParam(String wmsgID, String wparam, String waction) {
		this.wmsgID = StringUtils.isEmpty(wmsgID) ? new SimpleDateFormat(
		"yyyyMMddhhmmss").format(new Date()) : wmsgID;
		this.wparam = wparam;
		this.waction = waction;
	}

	public String getWmsgID() {

		
		return wmsgID;
	}

	public String getWparam() {
		
		return wparam;
	}

	public String getWaction() {
		
		return waction;
	}
	@Override
	public String toString() {
		String result = "wmsgID="+wmsgID
				+"waction="+waction
				+"wparam"+wparam;
		return result;
	}
}

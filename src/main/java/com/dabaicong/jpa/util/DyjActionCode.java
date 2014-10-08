package com.dabaicong.jpa.util;

public enum DyjActionCode {

	/** 请求方向 代理商=>销售平台 /代理商登录（Socket接入时使用）/ */
	DL_TO_SELL_SOCKETCODE("100"),
	/** 请求方向 代理商=>销售平台 /投注/ */
	DL_TO_SELL_TOUZHUCODE("101"),
	/** 请求方向 代理商=>销售平台 /投注处理结果查询/ */
	DL_TO_SELL_TZRESCODE("102"),
	/** 请求方向 代理商=>销售平台 /可用彩种/ */
	DL_TO_SELL_CZCODE("103"),
	/** 请求方向 代理商=>销售平台 /查询期号/ */
	DL_TO_SELL_FINDISSU("104"),
	/** 请求方向 代理商=>销售平台 /查询账户余额/ */
	DL_TO_SELL_FINDBALANCE("108"),
	/** 请求方向 代理商=>销售平台 /单期中奖单查询/ */
	DL_TO_SELL_FINDPRIZE("112"),
	/** 请求方向 代理商=>销售平台 /投注订单奖金查询/ */
	DL_TO_SELL_FINDMONEY("111"),
	/** 请求放心 代理商=>销售平台 /开奖号码查询/*/
	DL_TO_SELL_DRAWLOTTERY("110"),
	DL_TO_SELL_TZRESCODES("114"),
	/** 查询销售中心当前时间 **/
	DL_TO_SELL_LOTCENTERTIME("115"),
	/** 请求方向 代理商=>销售平台 /查询期号列表/ */
	DL_TO_SELL_FINDISSUES("116"),
	//单期订单汇总查询（118）
	DL_TO_SELL_FINDHUIZHONG("118"),
	DL_TO_SELL_JINGCAIZQRESULTSP("120"),
	DL_TO_SELL_JINGCAILQRESULTSP("102"),
	DL_TO_SELL_LUCKYRACINGPRIZE("121");

	String state;

	public String value() {
		return state;
	}

	DyjActionCode(String val) {
		this.state = val;
	}
}

package com.dabaicong.lottery.queryInterface;

public abstract class AbstractTerminalQuery  implements IQuery{

	//  查询某个彩种的期号
	public abstract  String queryPhase(String lotteryNo,String phase);
	
	//  查询某个彩种的开奖号码
	public abstract  String queryWinCode(String lotteryNo,String phase);
	//  查询出票商的余额
	//  其他查询
}

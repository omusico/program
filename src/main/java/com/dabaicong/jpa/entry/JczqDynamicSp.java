package com.dabaicong.jpa.entry;

import java.util.Map;

public class JczqDynamicSp {

	private String id;
	private String match_num;
	private Map<String, Map<String, String>>lottery_type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMatch_num() {
		return match_num;
	}

	public void setMatch_num(String matchNum) {
		match_num = matchNum;
	}

	public Map<String, Map<String, String>> getLottery_type() {
		return lottery_type;
	}

	public void setLottery_type(Map<String, Map<String, String>> lottery_type) {
		this.lottery_type = lottery_type;
	}



	
}

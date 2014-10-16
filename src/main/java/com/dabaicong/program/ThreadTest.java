package com.dabaicong.program;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ThreadTest {
	public static void main(String[] args) throws InterruptedException {
		List<String> list = new ArrayList<String>();
		list.add("test");
		list.add("moen");
		StringBuffer status = new StringBuffer();
		for (String s : list) {
			status.append(s).append(",");
		}
		status = status.deleteCharAt(status.length() - 1);
		System.out.println(status.toString());
	}
	
	public static  String caculatePrizeLevel(String betcode, String wincode,int oneAmount) {
		String[] winsH = new String[]{wincode.split(",")[0].substring(0,1),wincode.split(",")[1].substring(0,1),wincode.split(",")[2].substring(0,1)};
		// betcode.split("-")[1].split(",")   source betcode.split("\\-")[1].split("\\^")
		betcode = betcode.replace("^", "");
		List<String> bets = Arrays.asList(betcode.split("\\-")[1].split(","));
		
		StringBuilder prize = new StringBuilder();
		
		for(String bet:bets) {
			if("07".equals(bet)) {
				if(isTonghua(winsH)) {
					prize.append("BX_TH").append(",");
				}
			}else if("08".equals(bet)) {
				if(isTonghua(winsH)&&isShunzi(wincode)) {
					prize.append("BX_THS").append(",");
				}
			}else if("09".equals(bet)) {
				if(isShunzi(wincode)) {
					prize.append("BX_SZ").append(",");
				}
			}else if("10".equals(bet)) {
				if(isBaozi(wincode)) {
					prize.append("BX_BZ").append(",");
				}
			}else if("11".equals(bet)) {
				if(isDuizi(wincode)) {
					prize.append("BX_DZ").append(",");
				}
			}
		}
		if(prize.toString().endsWith(",")) {
			prize.deleteCharAt(prize.length()-1);
		}
		return prize.toString();
	}
	protected static boolean isBaozi(String wincode) {
		String[] wins = new String[]{wincode.split(",")[0].substring(1),wincode.split(",")[1].substring(1),wincode.split(",")[2].substring(1)};
		if(wins[0].equals(wins[1])&&wins[1].equals(wins[2])) {
			return true;
		}
		return false;
	}
	
	protected static boolean isDuizi(String wincode) {
		String[] wins = new String[]{wincode.split(",")[0].substring(1),wincode.split(",")[1].substring(1),wincode.split(",")[2].substring(1)};
		Arrays.sort(wins);
		if(wins[0].equals(wins[1])&&(!wins[1].equals(wins[2]))) {
			return true;
		}else if (wins[1].equals(wins[2])&&(!wins[1].equals(wins[0]))) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * 是否连号，三个号码
	 * @param wins
	 * @return
	 */
	protected static boolean isShunzi(String wincode) {
		String[] wins = new String[]{wincode.split(",")[0].substring(1),wincode.split(",")[1].substring(1),wincode.split(",")[2].substring(1)};
		int[] winInts = new int[wins.length];
		for(int i=0;i<wins.length;i++) {
			winInts[i] = Integer.parseInt(wins[i]);
		}
		Arrays.sort(winInts);
		if(winInts[0]+1==winInts[1]&&winInts[1]+1==winInts[2]) {
			return true;
		}else if(winInts[0]==1&&winInts[1]==12&&winInts[2]==13) {
			return true;
		}
		return false;
	}
	
	
	protected static boolean isTonghua(String[] wins) {
		if(wins[0].equals(wins[1])&&wins[1].equals(wins[2])) {
			return true;
		}
		return false;
	}
	
}

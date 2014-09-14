package com.dabaicong.program;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Hello world!
 *
 */
public class App 
{
	
    public static void main( String[] args )
    {
    	String beatCode = "100511-1,2,3,4^";
    	
    	System.out.println("s is :"+convert(beatCode));
    	
    	
    	Map<Integer, String> map = new HashMap<Integer, String>();
    	map.put(1001, "dale");
    	map.put(1002, "dabaicong");
    	map.put(1003, "dabaicai");
    	map.put(1004, "tmd");
    	System.out.println("get by String:"+map.get("1001"));
    	System.out.println("get by integer:"+map.get(new Integer("1001")));
    }
    public static String convert(String ticket) {
		// 2,3,4,5,6
		String contentStr=ticket.split("-")[1].replace("^","");
		String returnStr = "";
		String[] code = contentStr.split(",");
		for(int i=0;i<code.length;i++){
			code[i]+=(code[i]+"*");
			returnStr+=(code[i]+",");
		}
		
		return returnStr.substring(0, returnStr.length()-1);
	}
    public static String caculatePrizeLevel(String betcode, String wincode) throws Exception {
    	if(!isErTong(wincode)) {
			return "";
		}
    	StringBuilder prize = new StringBuilder();
		
		betcode = betcode.split("-")[1];
		
		for(String code:betcode.split("\\^")) {
			int[] codes = convertToInt(code);
			int[] wincodes = convertToInt(wincode);
			Arrays.sort(codes);
			Arrays.sort(wincodes);
			if(wincodes[0]==codes[0]&&wincodes[1]==codes[1]&&wincodes[2]==codes[2]) {
				prize.append("2").append(",");
				throw new NullPointerException();
			}
		}
		check2delete(prize);
		return prize.toString();
	}
	protected static int totalHits(String code,String wincode) {
		String[] codeArray = code.split(",");
		List<String> wincodeArray = Arrays.asList(wincode.split(","));
		int total = 0;
		
		for(String codeOne:codeArray) {
			if(wincodeArray.contains(codeOne)) {
				total = total + 1;
			}
		}
		return total;
	}
	protected static String caculatePrizeDS(String betcode,String wincode,int hit,String prize,int type) {
		betcode = betcode.split("\\-")[1].replace("^", "");
		StringBuilder sb = new StringBuilder("");
		String dan = betcode.split("\\#")[0];
		String tuo = betcode.split("\\#")[1];
		int danlength = dan.split(",").length;
		int tuolength = tuo.split(",").length;
		int totalDan = totalHits(dan, wincode);
		int totalTuo = totalHits(tuo, wincode);
		long total = 0;
		if(type<=5) {
			if(totalDan==danlength&&totalTuo>=hit-totalDan) {
				total = MathUtils.combine(totalTuo, hit-totalDan);
			}
		}else {
			if((totalDan+totalTuo>=5)&&(danlength+5-totalDan<=type)) {
				if(totalDan==5) {
					total = 1*MathUtils.combine(tuolength,type-danlength);
				}else {
					total = MathUtils.combine(totalTuo, hit-totalDan)*MathUtils.combine(tuolength-(hit-totalDan), type-danlength-(hit-totalDan));
				}
				
			}
		}
		
		for(long i=1;i<=total;i++) {
			sb.append(prize).append(",");
		}
		
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
    protected static void check2delete(StringBuilder builder) {
		if (builder.toString().endsWith(",")) {
			builder.deleteCharAt(builder.length() - 1);
		}
	}
    protected static int[] convertToInt(String code) {
		String[] codes = code.split(",");
		int[] ints = new int[codes.length];
		for(int i=0;i<codes.length;i++) {
			ints[i] = Integer.parseInt(codes[i]);
		}
		return ints;
	}
    protected static boolean isSanBuTong(String code) {
		String[] codes = code.split(",");
		if(codes[0].equals(codes[1])||codes[0].equals(codes[2])||codes[1].equals(codes[2])) {
			return false;
		}
		return true;
	}
    protected static boolean isErTong(String wincode) {
		String[] wincodes = wincode.split(",");
		if (wincodes[0].equals(wincodes[1]) && (!wincodes[0].equals(wincodes[2]))) {
			return true;
		}
		if (wincodes[1].equals(wincodes[2]) && (!wincodes[0].equals(wincodes[1]))) {
			return true;
		}
		return false;
	}
}

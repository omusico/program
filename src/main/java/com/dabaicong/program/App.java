package com.dabaicong.program;

import java.util.Arrays;
import java.util.List;



/**
 * Hello world!
 *
 */
public class App 
{
	
    public static void main( String[] args )
    {
    	String beatCode = "200728-01,10,11#02,03,04,05,06,07,08,09^";
    	
    	System.out.println("s is :"+convert(beatCode));
    }
    public static String convert(String ticket) {
    	String playType = ticket.split("-")[0];
    	System.out.println("playtype is :"+playType);
		// ,换成空格    #换成$  吃掉最后的尾巴
		String content = ticket.split("-")[1].replace(",", " ").replace("#", "$").replace("^", "");
		return "R2|"+content;
	}
    public static String caculatePrizeLevel(String betcode, String wincode) {
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

package com.dabaicong.program;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Hello world!
 *
 */
public class App  implements Runnable
{
	
	
    public static void main( String[] args )
    {
    	String beatCode = "01,02,03,04,05";
    	String winCode = "01,01,01";
    	System.out.println(caculatePrizeLevel(beatCode,winCode,1));

    }
    public static String caculatePrizeLevel(String betcode, String wincode,
			int oneAmount) {
		betcode = betcode.replace("^", "");
		List<String> wins = Arrays.asList(new String[]{wincode.split(",")[0].substring(1),wincode.split(",")[1].substring(1),wincode.split(",")[2].substring(1)});
		StringBuilder prize = new StringBuilder();
		
		List<List<String>> codeCollection = MathUtils.getCodeCollection(Arrays.asList(betcode.split("\\-")[1].split(",")), 4);
		
		for(List<String> codes:codeCollection) {
			if(codes.contains(wins.get(0))&&codes.contains(wins.get(1))&&codes.contains(wins.get(2))) {
				prize.append("R4").append(",");
			}
		}
		if(prize.toString().endsWith(",")) {
			prize = prize.deleteCharAt(prize.length()-1);
		}
		return prize.toString();
	}
    
    
	public void run() {
		for(int i=1 ;true;i++){
			System.out.println("test"+Thread.currentThread().getName());
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

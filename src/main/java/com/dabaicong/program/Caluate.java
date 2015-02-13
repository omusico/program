package com.dabaicong.program;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.dabaicong.lottery.ZchLotteryDef;



public class Caluate {
	
	
	public static final String sfc = "SFC|150202303(0)=1(8.73)/2(6.98),150202304(0)=2(6.22)|2*1";
	public static final String sfd = "SFD|121208306(0)=12(50.0)|1*1";
	public static final String dxf = "DXF|121207309(+192.5)=1(1.63),121207313(+185.5)=2(1.82)|2*1";
	public static final String rfsf = "RSF|121207301(-3.5)=1(1.7),121207304(-3.5)=1(1.7),121207306(+6.5)=2(1.7),121207307(-1.5)=1(1.75),121207310(-7.5)=1(1.65),121207311(+4.5)=2(1.65),121207317(-7.5)=2(1.75),121207318(-3.5)=2(1.75)|8*1";
	public static final String lht = "LHT|130618301:01(0)=1(1.250),130618302:02(-2.50)=1(1.700)|2*1";
	public static final String lhtsfc = "LHT|150202303:02(+9.5)=1(1.84),150202304:03(0)=02(3.4)/04(7.25)|2*1]";
	public static final String dxfreal = "DXF|150202301(192.5)=2(1.75),150202303(203.5)=2(1.76),150202304(202.5)=2(1.83),150202305(198.5)=2(1.72),150202306(208.5)=2(1.67)|5*5";
	public static final String lht3003="LHT|150202303:02(+9.5)=1(1.84),150202304:03(0)=03(4.6)/13(12.5),150202305:03(0)=13(5.5),150202306:02(-13.5)=1(1.75)|4*1";
	public static final String lt3003 = "LHT|150202303:03(0)=01(7.68)/02(8.95)/03(8.76)/05(8.36)/11(4.33),150202304:04(+202.50)=1(3.83)/2(6.74)|2*1";
	public static final String rsf = "RSF|150202301(-16.50)=2(8.08)/1(9.59),150202303(+8.50)=1(4.97),150202304(-6.50)=2(5.34)/1(3.71)|3*4";
	public static final String rsf1 = "RSF|150202302(-7.50)=2(9.73)/1(1.77),150202303(+8.50)=2(6.08),150202304(-6.50)=2(8.77)|3*1";
	
	public static final String error = "LHT|150203302:02(-4.50)=1(2.63),150203304:01(0)=1(8.56)|2*1";
	public static final String error1 = "LHT|150204301:02(-3.50)=2(6.66)/1(1.63),150204302:03(0)=02(6.23)/11(5.46)|2*1";
	public static void main(String[] args) {
//		System.out.println(dealOdds(error1));
		
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date start = sdf.parse("2015-02-07 00:40:00");
			Date end = sdf.parse("2015-02-06 23:50:00");
			
			System.out.println(returnLong(end, start));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public static String dealOdds (String odds){
		String strs[] = StringUtils.split(odds, "|");
		String dd = strs[1].replace("(", "_").replace(",", "|").replace(")", "").replace("/", ",");
		String[] dds = StringUtils.split(dd, "|");
		String result = "";
		int i = 0;
		for (String ds : dds) {
			result += "20" + ds;
			if (i != dds.length - 1) {
				result += ")|";
			}
			i++;
		}
		odds = result.replace("=", "(") + ")";
		StringBuilder stringBuilder = new StringBuilder();

		String numbers[] = StringUtils.split(odds, "|");
		if ("ZHT".equals(strs[0])) {
			int f = 0;
			for (String ss : numbers) {
				String[] s = StringUtils.split(ss.replace("-", ""), "(");
				String num = StringUtils.split(s[0], ":")[1].split("\\_")[0];
				if ("01".equals(num) || "02".equals(num) || "05".equals(num) || "03".equals(num) || "04".equals(num)) {
					stringBuilder.append(StringUtils.split(ss, ":")[0]).append("*").append(ZchLotteryDef.playTypeMapJc.get("jczq" + num)).append("(").append(s[1].replace(":", ""));
				}
				if (f != numbers.length - 1) {
					stringBuilder.append("|");
				}
				f++;
			}
		} else if ("LHT".equals(strs[0])) {
			int f = 0;
			for (String ss : numbers) {
				String[] s = StringUtils.split(ss, "(");
				String num = StringUtils.split(s[0], ":")[1].split("\\_")[0];
				stringBuilder.append(StringUtils.split(ss, ":")[0]).append("*").append(ZchLotteryDef.playTypeMapJc.get("jclq" + num)).append("(");
				if ("03".equals(num) || "02".equals(num)) {
					stringBuilder.append(s[1]);
				} else if ("01".equals(num)) {
					String odd = getOddString(ss.split("\\(")[1].replace(")", ""));
					stringBuilder.append(odd.replaceFirst("\\(", ""));
				} else if ("04".equals(num)) {
					stringBuilder.append(StringUtils.split(s[0], ":")[1].split("\\_")[1]).append(":").append(s[1]);
				}
				
				if (f != numbers.length - 1) {
					stringBuilder.append("|");
				}
				f++;
			}
		} else {
			int j = 0;
			for (String ss : numbers) {
				if ("RQS".equals(strs[0]) || "SFD".equals(strs[0]) || "SPF".equals(strs[0])) {
					String[] s = StringUtils.split(ss, "(")[0].split("\\_");
					stringBuilder.append(s[0]).append("(").append(ss.split("\\(")[1]);
				} else if ("SFC".equals(strs[0])) {
					String[] s = StringUtils.split(ss, "(")[0].split("\\_");
					stringBuilder.append(s[0]);
//					 添加赔率串信息
					stringBuilder.append(getOddString(ss.split("\\(")[1].replace(")", "")));
				} else if ("JQS".equals(strs[0]) || "CBF".equals(strs[0])) {
					stringBuilder.append(ss);
				} else if ("BQC".equals(strs[0])) {
					stringBuilder.append(ss.replace("-", ""));
				} else if ("RSF".equals(strs[0])) {
					String[] s = StringUtils.split(ss, "(")[0].split("\\_");
					stringBuilder.append(s[0]).append("(").append(s[1]).append(":");
					String odd = getOddString(ss.split("\\(")[1].replace(")", "")).replace("(", "");
					stringBuilder.append(odd);

				} else if ("DXF".equals(strs[0])) {
					String[] s = StringUtils.split(ss, "(")[0].split("\\_");
					stringBuilder.append(s[0]).append("(").append(s[1]).append(":").append(ss.split("\\(")[1]);
				}
				if (j != numbers.length - 1) {
					stringBuilder.append("|");
				}
				j++;

			}
		}

		odds = stringBuilder.toString();
		if ("CBF".equals(strs[0])) {
			odds = odds.replace(":", "");
		}
		return odds;
	}
	
	
	private static String getOddString(String context ){
		StringBuilder sb = new StringBuilder() ;
		sb.append("(");
		for(String s :context.split(",")){
			sb.append(s.split("\\_")[0].replace("2", "0").replace("1", "3"));
			sb.append("_");
			sb.append(s.split("\\_")[1]);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(")");
		return  sb.toString();
	}
	
	public static  boolean returnLong (Date start ,Date end ){
		if((start.getTime()-(end.getTime()))>=0){
			return true ;
		}
		return false ;
	}
	
	
	public static Date fun(Date  matchDate){
		if (matchDate == null) {
			return null;
		}
		Calendar cd = Calendar.getInstance();
    	cd.setTime(matchDate);
    	
    	int matchWeekday = cd.get(Calendar.DAY_OF_WEEK);
		int hourOfDay = cd.get(Calendar.HOUR_OF_DAY);
		int minute = cd.get(Calendar.MINUTE);

		Calendar endSaleCalendar = Calendar.getInstance();
		endSaleCalendar.setTime(matchDate);

		endSaleCalendar.set(Calendar.MILLISECOND, 0);
		
		if (matchWeekday == Calendar.MONDAY) {
			if (hourOfDay >= 1 && (hourOfDay < 9 || (hourOfDay == 9 && minute == 0))) {
				endSaleCalendar.set(Calendar.HOUR_OF_DAY, 1);
				endSaleCalendar.set(Calendar.MINUTE, 0);
				endSaleCalendar.set(Calendar.SECOND, 0);
			}
		} else if (matchWeekday == Calendar.TUESDAY || matchWeekday == Calendar.FRIDAY || matchWeekday == Calendar.WEDNESDAY || matchWeekday == Calendar.THURSDAY) {
			if (hourOfDay < 9 || (hourOfDay == 9 && minute == 0)) {
				endSaleCalendar.set(Calendar.HOUR_OF_DAY, 0);
				endSaleCalendar.set(Calendar.MINUTE, 0);
				endSaleCalendar.set(Calendar.SECOND, 0);
			}
		} else if (matchWeekday == Calendar.SATURDAY) {
			if (hourOfDay < 9 || (hourOfDay == 9 && minute == 0)) {
				endSaleCalendar.set(Calendar.HOUR_OF_DAY, 0);
				endSaleCalendar.set(Calendar.MINUTE, 0);
				endSaleCalendar.set(Calendar.SECOND, 0);
			}
		} else {
			if (hourOfDay >= 1 && (hourOfDay < 9 || (hourOfDay == 9 && minute == 0))) {
				endSaleCalendar.set(Calendar.HOUR_OF_DAY, 1);
				endSaleCalendar.set(Calendar.MINUTE, 0);
				endSaleCalendar.set(Calendar.SECOND, 0);
			}
		}
		return endSaleCalendar.getTime();
	}
}


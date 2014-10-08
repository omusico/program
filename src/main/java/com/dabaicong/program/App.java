package com.dabaicong.program;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.dom4j.Element;

import com.dabaicong.jpa.util.HTTPUtil;
import com.dabaicong.jpa.util.LotteryConstant;
import com.dabaicong.jpa.util.LotteryType;
import com.dabaicong.jpa.util.XmlParse;



/**
 * Hello world!
 *
 */
public class App  implements Runnable
{
	/** 大赢家的比分转换    自己规定的比分格式 ---->  出票商的比分格式     */
	private static Map<String, String> bifen = new HashMap<String, String>();
	/** 半全场胜平负转换    自己规定的半全场格式 ---->  出票商的半全场格式    */
	private static Map<String, String> bqcspf = new HashMap<String, String>();
	protected static Map<Integer, String> playTypeMap = new HashMap<Integer, String>();
    public static void main( String[] args )
    {
    	

		bifen.put("10","1:0");    
		bifen.put("20","2:0");
		bifen.put("21","2:1");
		bifen.put("30","3:0");
		bifen.put("31","3:1");
		bifen.put("32","3:2");
		bifen.put("40","4:0");
		bifen.put("41","4:1");
		bifen.put("42","4:2");
		bifen.put("50","5:0");
		bifen.put("51","5:1");
		bifen.put("52","5:2");
		bifen.put("90","9:0");
		bifen.put("00","0:0");
		bifen.put("11","1:1");
		bifen.put("22","2:2");
		bifen.put("33","3:3");
		bifen.put("99","9:9");
		bifen.put("01","0:1");
		bifen.put("02","0:2");
		bifen.put("12","1:2");
		bifen.put("03","0:3");
		bifen.put("13","1:3");
		bifen.put("23","2:3");
		bifen.put("04","0:4");
		bifen.put("14","1:4");
		bifen.put("24","2:4");
		bifen.put("05","0:5");
		bifen.put("15","1:5");
		bifen.put("25","2:5");
		bifen.put("09","0:9");
		
		bqcspf.put("33", "3-3");
		bqcspf.put("31", "3-1");
		bqcspf.put("30", "3-0");
		bqcspf.put("13", "1-3");
		bqcspf.put("11", "1-1");
		bqcspf.put("10", "1-0");
		bqcspf.put("03", "0-3");
		bqcspf.put("01", "0-1");
		bqcspf.put("00", "0-0");
		   playTypeMap.put(LotteryType.JCZQ_BF.value, "CBF");
			playTypeMap.put(LotteryType.JCZQ_BQC.value, "BQC");
			playTypeMap.put(LotteryType.JCZQ_JQS.value, "JQS");
			playTypeMap.put(LotteryType.JCZQ_SPF.value, "RQSPF");
			playTypeMap.put(LotteryType.JCZQ_SPF_WRQ.value, "SPF");
			playTypeMap.put(LotteryType.JCZQ_HHGG.value, "HHGG");
		
		
//    	String beatCode = "01,02,03,04,05";
//    	String winCode = "01,01,01";
//    	System.out.println(caculatePrizeLevel(beatCode,winCode,1));
//    	String notsupport = "3006_01,3006_02,3010_02,";
//    	notsupport=removeString(notsupport, "3006_02,");
//    	System.out.println("======"+notsupport);
//    	List<JSONObject> list = new ArrayList<JSONObject>();
//    	JSONObject jo = new JSONObject();
//    	jo.put("1", 1);
//    	jo.put("2", 2);
//    	jo.put("3", 1);
//    	jo.put("4", 2);
//    	JSONObject jo1 = new JSONObject();
//    	jo1.put("1", 1);
//    	jo1.put("2", 2);
//    	jo1.put("3", 1);
//    	jo1.put("4", 2);
//    	JSONObject jo2 = new JSONObject();
//    	jo2.put("1", 1);
//    	jo2.put("2", 2);
//    	jo2.put("3", 1);
//    	jo2.put("4", 2);
//    	jo2.put("json", jo);
//    	list.add(jo2);
//    	list.add(jo1);
//    	list.add(jo);
//    	JSONArray array =JSONArray.fromObject(list);
//    	System.out.println(array);
    	
//    	JSONArray array = getJczqStatic();
//    	System.out.println(array);
//    	
//    	JSONArray array = getJczqDynamic();
//    	System.out.println(array);
    	//  3006 OK
    	//  3007 比分不ok  ，关于比分的转换
    	String code1 = "300714001-20140925001(20,32,50)|20140925002(21,40)|20140925003(22)|20140925004(03)^";
    	//  3008总进去，不ok，总进球的转换，
    	String code2 = "300813001-20140925001(0,4)|20140925002(0,4)|20140925003(0,4)^";
    	//  3009半场 ,不ok，33,11的转换
    	String code3 = "300913001-20140925001(33,11,03)|20140925002(31,11)|20140925003(31,13)^";
    	//	3010   ok
    	//  混合肯定有问题
    	String code5 = "301116001-20140925001(3)|20140925002(3)|20140925003(3)|20140925006(3)|20140925007(3)|20140925009(3)^";
    	
    	
//    	System.out.println("type:"+code2.substring(0,4));
    	
    	
    	
    	
    	
//		System.out.println(strBuffer.toString());
//    	System.out.println(convert("300612001-20140926018(3,1)|20140926019(3,1)^"));
    	String s = "3:0=8.82|0:2=2.21";
    	String[] s1 = s.split("\\|");
    	System.out.println(s1[0]);
//    	System.out.println(s.split("|")[1]);
    	
    }
 
    // 301114001-20140926003*3010(3)|20140926004*3006(3)|20140925001*3009(33,11,03)|20140925001*3007(20,32,50)|20140925001*3008(0,4)^
    public static String convert(String ticket) {
			String playType = ticket.split("-")[0];  //获得玩法
			String chuan = ticket.split("-")[0].substring(5, 9);    //获得N串N的格式
			String strs[] = ticket.split("-")[1].split("\\|"); 	 //各个单串
	    	StringBuffer strBuffer=new StringBuffer();
	    	int i = 0 ;
			for(String str1:strs){
				str1 = str1.substring(2);
				str1 = str1.replace("(", "=").replace(")", "").replace(",", "/").replace("^", "");
				strBuffer.append(str1);
				if(i!=strs.length-1){
					strBuffer.append(",");
				}
				i++;
			}
		return "HH"+"|"+strBuffer.append("|"+chuan.substring(0,1)+"*"+Integer.parseInt(chuan.substring(1))).toString();
    }
    
    public static JSONArray getJczqDynamic() {
    	String url = "http://119.254.92.197/200.xml";
		//键位matchnum 值是sp对象Map<String, Map<String, String>> lottery_type = new HashMap<String, Map<String, String>>();
		Map<String, JSONObject> checkMap = new HashMap<String, JSONObject>();
		try {
			String matchStr = HTTPUtil.post(url, "");
			XmlParse body = new XmlParse(matchStr, "root", "", "");
			Element el = body.getRootElement();
			Iterator<Element> packages = el.elementIterator("package");
			while (packages.hasNext()) {
				Element packag = packages.next();
				// 日期 20140410
				String date = packag.elementText("date");

				Iterator<Element> matchs = packag.elementIterator("match");
				while (matchs.hasNext()) {
					Element match = matchs.next();
					// 玩法编码 让球胜平负 [01] 总进球数[02] 半全场[03] 比分[04] 胜平负 [05]
					String playCode = match.attributeValue("playCode");
					// 当天赛事场次编号
					String sn = match.attributeValue("sn");
					// 选号方式(购买方式) 02是串关 01是单关 如果是02playCode是00
					String pollCode = match.attributeValue("pollCode");
					JSONObject json = new JSONObject();
					if ("01".equals(pollCode)) {
						JSONObject dansp = checkMap.get(date+sn);
						if (dansp == null) {
							dansp = new JSONObject();
							dansp.put("matchNum", date+sn);
							checkMap.put(date+sn, dansp);
						}

						if ("01".equals(playCode)) {
							Map<String, String> spfMap = new HashMap<String, String>();
							Element winOrNega = match.element("winOrNega");
							// 让球胜平负-负
							String nega = winOrNega.attributeValue("nega");
							spfMap.put(LotteryConstant.JCZQ_SPF_F_VALUE, nega);
							// 让球胜平负-平
							String flat = winOrNega.attributeValue("flat");
							spfMap.put(LotteryConstant.JCZQ_SPF_P_VALUE, flat);
							// 让球胜平负-胜
							String win = winOrNega.attributeValue("win");
							spfMap.put(LotteryConstant.JCZQ_SPF_S_VALUE, win);
							dansp.put(LotteryType.JCZQ_SPF.value+"", spfMap);
						} else if ("02".equals(playCode)) {
							Map<String, String> zjqMap = new HashMap<String, String>();
							Element totalGoal = match.element("totalGoal");
							// 总进球-进0个
							String tg_0 = totalGoal.attributeValue("tg_0");
							zjqMap.put(LotteryConstant.JCZQ_JQS_0_VALUE, tg_0);
							// 总进球-进1个
							String tg_1 = totalGoal.attributeValue("tg_1");
							zjqMap.put(LotteryConstant.JCZQ_JQS_1_VALUE, tg_1);
							// 总进球-进2个
							String tg_2 = totalGoal.attributeValue("tg_2");
							zjqMap.put(LotteryConstant.JCZQ_JQS_2_VALUE, tg_2);
							// 总进球-进3个
							String tg_3 = totalGoal.attributeValue("tg_3");
							zjqMap.put(LotteryConstant.JCZQ_JQS_3_VALUE, tg_3);
							// 总进球-进4个
							String tg_4 = totalGoal.attributeValue("tg_4");
							zjqMap.put(LotteryConstant.JCZQ_JQS_4_VALUE, tg_4);
							// 总进球-进5个
							String tg_5 = totalGoal.attributeValue("tg_5");
							zjqMap.put(LotteryConstant.JCZQ_JQS_5_VALUE, tg_5);
							// 总进球-进6个
							String tg_6 = totalGoal.attributeValue("tg_6");
							zjqMap.put(LotteryConstant.JCZQ_JQS_6_VALUE, tg_6);
							// 总进球-进7+个
							String tg_7 = totalGoal.attributeValue("tg_7");
							zjqMap.put(LotteryConstant.JCZQ_JQS_7_VALUE, tg_7);
							dansp.put(LotteryType.JCZQ_JQS.value+"", zjqMap);
						} else if ("03".equals(playCode)) {
							Map<String, String> bqcMap = new HashMap<String, String>();
							Element halfCourt = match.element("halfCourt");
							// 半场胜平负-负负
							String hc_ff = halfCourt.attributeValue("hc_ff");
							bqcMap.put(LotteryConstant.JCZQ_BQC_FF_VALUE, hc_ff);
							// 半场胜平负-负平
							String hc_fp = halfCourt.attributeValue("hc_fp");
							bqcMap.put(LotteryConstant.JCZQ_BQC_FP_VALUE, hc_fp);
							// 半场胜平负-负胜
							String hc_fs = halfCourt.attributeValue("hc_fs");
							bqcMap.put(LotteryConstant.JCZQ_BQC_FS_VALUE, hc_fs);
							// 半场胜平负-平负
							String hc_pf = halfCourt.attributeValue("hc_pf");
							bqcMap.put(LotteryConstant.JCZQ_BQC_PF_VALUE, hc_pf);
							// 半场胜平负-平平
							String hc_pp = halfCourt.attributeValue("hc_pp");
							bqcMap.put(LotteryConstant.JCZQ_BQC_PP_VALUE, hc_pp);
							// 半场胜平负-平胜
							String hc_ps = halfCourt.attributeValue("hc_ps");
							bqcMap.put(LotteryConstant.JCZQ_BQC_PS_VALUE, hc_ps);
							// 半场胜平负-胜负
							String hc_sf = halfCourt.attributeValue("hc_sf");
							bqcMap.put(LotteryConstant.JCZQ_BQC_SF_VALUE, hc_sf);
							// 半场胜平负-胜平
							String hc_sp = halfCourt.attributeValue("hc_sp");
							bqcMap.put(LotteryConstant.JCZQ_BQC_SP_VALUE, hc_sp);
							// 半场胜平负-胜胜
							String hc_ss = halfCourt.attributeValue("hc_ss");
							bqcMap.put(LotteryConstant.JCZQ_BQC_SS_VALUE, hc_ss);

							dansp.put(LotteryType.JCZQ_BQC.value+"", bqcMap);
						} else if ("05".equals(playCode)) {
							Map<String, String> spfMap2 = new HashMap<String, String>();
							Element spfWinOrNega = match
									.element("spfWinOrNega");
							// 胜平负-负
							String spfNega = spfWinOrNega
									.attributeValue("spfNega");
							spfMap2.put(LotteryConstant.JCZQ_SPF_WRQ_F_VALUE,
									spfNega);
							// 胜平负-平
							String spfFlat = spfWinOrNega
									.attributeValue("spfFlat");
							spfMap2.put(LotteryConstant.JCZQ_SPF_WRQ_P_VALUE,
									spfFlat);
							// 胜平负-胜
							String spfWin = spfWinOrNega
									.attributeValue("spfWin");
							spfMap2.put(LotteryConstant.JCZQ_SPF_WRQ_S_VALUE,
									spfWin);
							dansp.put(LotteryType.JCZQ_SPF_WRQ.value+"", spfMap2);
						}
					}
				}
			}
		} catch (Exception e) {
		}
		return JSONArray.fromObject(checkMap);

	}
    
    
    public static JSONArray getJczqStatic() {
		String url = "http://119.254.92.197/200.xml";
		//  准备返回的jsonarray
		
		Map<String, JSONObject> map = new HashMap<String, JSONObject>();
//		List<JczqStaticSp> retList = new ArrayList<JczqStaticSp>();
		try {
			String matchStr = HTTPUtil.post(url, "");
			XmlParse body = new XmlParse(matchStr, "root", "", "");
			Element el = body.getRootElement();
			// 时间戳 20140409095046
			Iterator<Element> packages = el.elementIterator("package");
			
			while (packages.hasNext()) {
				Element packag = packages.next();
				// 日期 20140410
				String date = packag.elementText("date");
				Iterator<Element> matchs = packag.elementIterator("match");
				while (matchs.hasNext()) {
//					JSONObject json = new JSONObject();
					Element match = matchs.next();
					
					// 当天赛事场次编号
					String sn = match.attributeValue("sn");
					// 选号方式(购买方式) 02是串关 01是单关 如果是02playCode是00
					String pollCode = match.attributeValue("pollCode");
					
					if ("02".equals(pollCode)) {
						JSONObject array = new JSONObject();
						// 串关sp
//						JczqStaticSp jczqStaticSp = new JczqStaticSp();
//						jczqStaticSp.setMatch_num(date + sn);
						// jczqChuanSp.setLottery_type(lottery_type);
						array.put("matchNum", date + sn);
						Map<String, Map<String, String>> lottery_type = new HashMap<String, Map<String, String>>();

						Map<String, String> spfMap = new HashMap<String, String>();
						Element winOrNega = match.element("winOrNega");
						// 让球胜平负-负
						String nega = winOrNega.attributeValue("nega");
						spfMap.put(LotteryConstant.JCZQ_SPF_F_VALUE, nega);
						// 让球胜平负-平
						String flat = winOrNega.attributeValue("flat");
						spfMap.put(LotteryConstant.JCZQ_SPF_P_VALUE, flat);
						// 让球胜平负-胜
						String win = winOrNega.attributeValue("win");
						spfMap.put(LotteryConstant.JCZQ_SPF_S_VALUE, win);
						array.put(LotteryType.JCZQ_SPF.value+"", spfMap);

						Map<String, String> spfMap2 = new HashMap<String, String>();
						Element spfWinOrNega = match.element("spfWinOrNega");
						// 胜平负-负
						String spfNega = spfWinOrNega.attributeValue("spfNega");
						spfMap2.put(LotteryConstant.JCZQ_SPF_WRQ_F_VALUE,
								spfNega);
						// 胜平负-平
						String spfFlat = spfWinOrNega.attributeValue("spfFlat");
						spfMap2.put(LotteryConstant.JCZQ_SPF_WRQ_P_VALUE,
								spfFlat);
						// 胜平负-胜
						String spfWin = spfWinOrNega.attributeValue("spfWin");
						spfMap2.put(LotteryConstant.JCZQ_SPF_WRQ_S_VALUE,
								spfWin);
						array.put(LotteryType.JCZQ_SPF_WRQ.value+"", spfMap2);

						Map<String, String> bfMap = new HashMap<String, String>();
						Element score = match.element("score");
						// 比分-负其它
						String sp_f_qt = score.attributeValue("sp_f_qt");
						bfMap.put(LotteryConstant.JCZQ_BF_ZF_QT_VALUE, sp_f_qt);
						// 比分-平其它
						String sp_p_qt = score.attributeValue("sp_p_qt");
						bfMap.put(LotteryConstant.JCZQ_BF_ZP_QT_VALUE, sp_p_qt);
						// 比分-胜其它
						String sp_s_qt = score.attributeValue("sp_s_qt");
						bfMap.put(LotteryConstant.JCZQ_BF_ZS_QT_VALUE, sp_s_qt);
						// 比分-0:0
						String sp_00 = score.attributeValue("sp_00");
						bfMap.put(LotteryConstant.JCZQ_BF_ZP_0_0_VALUE, sp_00);
						// 比分-0:1
						String sp_01 = score.attributeValue("sp_01");
						bfMap.put(LotteryConstant.JCZQ_BF_ZF_0_1_VALUE, sp_01);
						// sp_02 比分-0:2
						String sp_02 = score.attributeValue("sp_02");
						bfMap.put(LotteryConstant.JCZQ_BF_ZF_0_2_VALUE, sp_02);
						// sp_03 比分-0:3
						String sp_03 = score.attributeValue("sp_03");
						bfMap.put(LotteryConstant.JCZQ_BF_ZF_0_3_VALUE, sp_03);
						// sp_04 比分-0:4
						String sp_04 = score.attributeValue("sp_04");
						bfMap.put(LotteryConstant.JCZQ_BF_ZF_0_4_VALUE, sp_04);
						// sp_05 比分-0:5
						String sp_05 = score.attributeValue("sp_05");
						bfMap.put(LotteryConstant.JCZQ_BF_ZF_0_5_VALUE, sp_05);
						// sp_10 比分-1:0
						String sp_10 = score.attributeValue("sp_10");
						bfMap.put(LotteryConstant.JCZQ_BF_ZS_1_0_VALUE, sp_10);
						// sp_11 比分-1:1
						String sp_11 = score.attributeValue("sp_11");
						bfMap.put(LotteryConstant.JCZQ_BF_ZP_1_1_VALUE, sp_11);
						// sp_12 比分-1:2
						String sp_12 = score.attributeValue("sp_12");
						bfMap.put(LotteryConstant.JCZQ_BF_ZF_1_2_VALUE, sp_12);
						// sp_13 比分-1:3
						String sp_13 = score.attributeValue("sp_13");
						bfMap.put(LotteryConstant.JCZQ_BF_ZF_1_3_VALUE, sp_13);
						// sp_14 比分-1:4
						String sp_14 = score.attributeValue("sp_14");
						bfMap.put(LotteryConstant.JCZQ_BF_ZF_1_4_VALUE, sp_14);
						// sp_15 比分-1:5
						String sp_15 = score.attributeValue("sp_15");
						bfMap.put(LotteryConstant.JCZQ_BF_ZF_1_5_VALUE, sp_15);
						// sp_20 比分-2:0
						String sp_20 = score.attributeValue("sp_20");
						bfMap.put(LotteryConstant.JCZQ_BF_ZS_2_0_VALUE, sp_20);
						// sp_21 比分-2:1
						String sp_21 = score.attributeValue("sp_21");
						bfMap.put(LotteryConstant.JCZQ_BF_ZS_2_1_VALUE, sp_21);
						// sp_22 比分-2:2
						String sp_22 = score.attributeValue("sp_22");
						bfMap.put(LotteryConstant.JCZQ_BF_ZP_2_2_VALUE, sp_22);
						// sp_23 比分-2:3
						String sp_23 = score.attributeValue("sp_23");
						bfMap.put(LotteryConstant.JCZQ_BF_ZF_2_3_VALUE, sp_23);
						// sp_24 比分-2:4
						String sp_24 = score.attributeValue("sp_24");
						bfMap.put(LotteryConstant.JCZQ_BF_ZF_2_4_VALUE, sp_24);
						// sp_25 比分-2:5
						String sp_25 = score.attributeValue("sp_25");
						bfMap.put(LotteryConstant.JCZQ_BF_ZF_2_5_VALUE, sp_25);
						// sp_30 比分-3:0
						String sp_30 = score.attributeValue("sp_30");
						bfMap.put(LotteryConstant.JCZQ_BF_ZS_3_0_VALUE, sp_30);
						// sp_31 比分-3:1
						String sp_31 = score.attributeValue("sp_31");
						bfMap.put(LotteryConstant.JCZQ_BF_ZS_3_1_VALUE, sp_31);
						// sp_32 比分-3:2
						String sp_32 = score.attributeValue("sp_32");
						bfMap.put(LotteryConstant.JCZQ_BF_ZS_3_2_VALUE, sp_32);
						// sp_33 比分-3:3
						String sp_33 = score.attributeValue("sp_33");
						bfMap.put(LotteryConstant.JCZQ_BF_ZP_3_3_VALUE, sp_33);
						// sp_40 比分-4:0
						String sp_40 = score.attributeValue("sp_40");
						bfMap.put(LotteryConstant.JCZQ_BF_ZS_4_0_VALUE, sp_40);
						// sp_41 比分-4:1
						String sp_41 = score.attributeValue("sp_41");
						bfMap.put(LotteryConstant.JCZQ_BF_ZS_4_1_VALUE, sp_41);
						// sp_42 比分-4:2
						String sp_42 = score.attributeValue("sp_42");
						bfMap.put(LotteryConstant.JCZQ_BF_ZS_4_2_VALUE, sp_42);
						// sp_50 比分-5:0
						String sp_50 = score.attributeValue("sp_50");
						bfMap.put(LotteryConstant.JCZQ_BF_ZS_5_0_VALUE, sp_50);
						// sp_51 比分-5:1
						String sp_51 = score.attributeValue("sp_51");
						bfMap.put(LotteryConstant.JCZQ_BF_ZS_5_1_VALUE, sp_51);
						// sp_52 比分-5:2
						String sp_52 = score.attributeValue("sp_52");
						bfMap.put(LotteryConstant.JCZQ_BF_ZS_5_2_VALUE, sp_52);
						array.put(LotteryType.JCZQ_BF.value+"", bfMap);

						Map<String, String> zjqMap = new HashMap<String, String>();
						Element totalGoal = match.element("totalGoal");
						// 总进球-进0个
						String tg_0 = totalGoal.attributeValue("tg_0");
						zjqMap.put(LotteryConstant.JCZQ_JQS_0_VALUE, tg_0);
						// 总进球-进1个
						String tg_1 = totalGoal.attributeValue("tg_1");
						zjqMap.put(LotteryConstant.JCZQ_JQS_1_VALUE, tg_1);
						// 总进球-进2个
						String tg_2 = totalGoal.attributeValue("tg_2");
						zjqMap.put(LotteryConstant.JCZQ_JQS_2_VALUE, tg_2);
						// 总进球-进3个
						String tg_3 = totalGoal.attributeValue("tg_3");
						zjqMap.put(LotteryConstant.JCZQ_JQS_3_VALUE, tg_3);
						// 总进球-进4个
						String tg_4 = totalGoal.attributeValue("tg_4");
						zjqMap.put(LotteryConstant.JCZQ_JQS_4_VALUE, tg_4);
						// 总进球-进5个
						String tg_5 = totalGoal.attributeValue("tg_5");
						zjqMap.put(LotteryConstant.JCZQ_JQS_5_VALUE, tg_5);
						// 总进球-进6个
						String tg_6 = totalGoal.attributeValue("tg_6");
						zjqMap.put(LotteryConstant.JCZQ_JQS_6_VALUE, tg_6);
						// 总进球-进7+个
						String tg_7 = totalGoal.attributeValue("tg_7");
						zjqMap.put(LotteryConstant.JCZQ_JQS_7_VALUE, tg_7);
						array.put(LotteryType.JCZQ_JQS.value+"", zjqMap);

						Map<String, String> bqcMap = new HashMap<String, String>();
						Element halfCourt = match.element("halfCourt");
						// 半场胜平负-负负
						String hc_ff = halfCourt.attributeValue("hc_ff");
						bqcMap.put(LotteryConstant.JCZQ_BQC_FF_VALUE, hc_ff);
						// 半场胜平负-负平
						String hc_fp = halfCourt.attributeValue("hc_fp");
						bqcMap.put(LotteryConstant.JCZQ_BQC_FP_VALUE, hc_fp);
						// 半场胜平负-负胜
						String hc_fs = halfCourt.attributeValue("hc_fs");
						bqcMap.put(LotteryConstant.JCZQ_BQC_FS_VALUE, hc_fs);
						// 半场胜平负-平负
						String hc_pf = halfCourt.attributeValue("hc_pf");
						bqcMap.put(LotteryConstant.JCZQ_BQC_PF_VALUE, hc_pf);
						// 半场胜平负-平平
						String hc_pp = halfCourt.attributeValue("hc_pp");
						bqcMap.put(LotteryConstant.JCZQ_BQC_PP_VALUE, hc_pp);
						// 半场胜平负-平胜
						String hc_ps = halfCourt.attributeValue("hc_ps");
						bqcMap.put(LotteryConstant.JCZQ_BQC_PS_VALUE, hc_ps);
						// 半场胜平负-胜负
						String hc_sf = halfCourt.attributeValue("hc_sf");
						bqcMap.put(LotteryConstant.JCZQ_BQC_SF_VALUE, hc_sf);
						// 半场胜平负-胜平
						String hc_sp = halfCourt.attributeValue("hc_sp");
						bqcMap.put(LotteryConstant.JCZQ_BQC_SP_VALUE, hc_sp);
						// 半场胜平负-胜胜
						String hc_ss = halfCourt.attributeValue("hc_ss");
						bqcMap.put(LotteryConstant.JCZQ_BQC_SS_VALUE, hc_ss);

						array.put(LotteryType.JCZQ_BQC.value+"", bqcMap);
//						json.put("lottery_type", lottery_type);
//						jczqStaticSp.setLottery_type(lottery_type);
//						array.put("ddddd", json);
//						retList.add(jczqStaticSp);
						map.put(date+sn, array);
						System.out.println("======================="+map.size());
					}
				}
				
			}
		} catch (Exception e) {
		}
		
		return JSONArray.fromObject(map);
	}
    
    // 从一个String中移除一段,也就是不支持的玩法
 	public static String removeString (String desc,String str){
 		return desc.replace(str, "");
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

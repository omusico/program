package com.dabaicong.program;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.dabaicong.jpa.util.DyjActionCode;
import com.dabaicong.jpa.util.DyjParam;
import com.dabaicong.jpa.util.DyjResult;
import com.dabaicong.jpa.util.DyjUtil;
import com.dabaicong.jpa.util.LotteryType;
import com.dabaicong.jpa.util.StringUtil;
import com.dabaicong.jpa.util.XmlParse;


public class Test {	
	
	/** 大赢家的比分转换   出票商的比分格式     ---->    自己规定的比分格式    */
	private static Map<String, String> convertMap = new HashMap<String, String>();
	
	private static String orderidbifen = "B201409260000006126";
	private static String orderidjinqiu = "B201409260000006121";
	private static String orderidHH = "B201409260000006114";
	private static String orderidsimple = "B201409290000006294";
	
	
	private static String orderid1 = "B201409260000006135";
	private static String zch_odd = "SPF|120825001=0(3.35),120825002=0(8.88)/0(3.58),120825003=1(3.43)|3*1";
	// HH
	private static String HH = "<?xml version=\"1.0\"?><Project id=\"96192109\" chipmul=\"1\" chipcount=\"2\" chipmoney=\"4\"><bill id=\"20440147025948150258\" billtime=\"2014-09-26 17:54:13\" issue=\"20140926\" playid=\"HH\" maxbonu=\"99999999\" ispass=\"1\" passway=\"2*1\" multi=\"1\"><match id=\"140926005\" playid=\"SPF\">0=7.12</match><match id=\"140926006\" playid=\"CBF\">2:1=4.16|0:3=9.88</match></bill></Project>";
	
	// spf
	private static String cbf = "<?xml version=\"1.0\"?><Project id=\"96192114\" chipmul=\"1\" chipcount=\"4\" chipmoney=\"8\"><bill id=\"20623609368143387410\" billtime=\"2014-09-26 17:54:13\" issue=\"20140926\" playid=\"CBF\" maxbonu=\"99999999\" ispass=\"1\" passway=\"2*1\" multi=\"1\"><match id=\"140926005\">3:0=8.82|0:2=2.21</match><match id=\"140926006\">2:1=9.08|0:3=2.07</match></bill></Project>";
	private static final String zch_returnString = "20120825001(0_3.35)|20120825002(0_8.88,0_3.58)|20120825003(1_3.43)";
	static{
		convertMap.put("1:0","10");
		convertMap.put("2:0","20");
		convertMap.put("2:1","21");
		convertMap.put("3:0","30");
		convertMap.put("3:1","31");
		convertMap.put("3:2","32");
		convertMap.put("4:0","40");
		convertMap.put("4:1","41");
		convertMap.put("4:2","42");
		convertMap.put("5:0","50");
		convertMap.put("5:1","51");
		convertMap.put("5:2","52");
		convertMap.put("9:0","90");
		convertMap.put("0:0","00");
		convertMap.put("1:1","11");
		convertMap.put("2:2","22");
		convertMap.put("3:3","33");
		convertMap.put("9:9","99");
		convertMap.put("0:1","01");
		convertMap.put("0:2","02");
		convertMap.put("1:2","12");
		convertMap.put("0:3","03");
		convertMap.put("1:3","13");
		convertMap.put("2:3","23");
		convertMap.put("0:4","04");
		convertMap.put("1:4","14");
		convertMap.put("2:4","24");
		convertMap.put("0:5","05");
		convertMap.put("1:5","15");
		convertMap.put("2:5","25");
		convertMap.put("0:9","09");    
		convertMap.put("3-3","33");
		convertMap.put("3-1","31");
		convertMap.put("3-0","30");
		convertMap.put("1-3","13");
		convertMap.put("1-1","11");
		convertMap.put("1-0","10");
		convertMap.put("0-3","03");
		convertMap.put("0-1","01");
		convertMap.put("0-0","00");
		convertMap.put("SPF", "3010");
		convertMap.put("RQSPF", "3006");
		convertMap.put("CBF", "3007");
		convertMap.put("BQC", "3009");
		convertMap.put("JQS", "3008");

	}
	
	public static void main(String[] args) {
		DyjUtil dyjUtil = new DyjUtil();
		
		DyjParam reqparam = new DyjParam("", "OrderID=" + orderidsimple,
				DyjActionCode.DL_TO_SELL_TZRESCODE.value());
		try {
			DyjResult result = dyjUtil.getResponse(reqparam);
			// 处理结果
			
			System.out.println(dealResult(result));
		} catch (Exception e) {
			System.out.println("Error");
			e.printStackTrace();
		}
//		System.out.println(dealOdds(zch_odd));
	}
	
	

	private static String dealResult(DyjResult result) throws DocumentException {
		String returnCode = result.getXcode();
		String value = result.getXvalue();
//		System.out.println("大赢家检票返回的相应码是:"+returnCode+",返回的message为:"+value);
		String ticket =  "301112001-20140926005*3010(0)|20140926006*3007(21,03)^";
		String ticket1 = "301012001-20140926005(0)|20140926006(1)^";
		String returnString = null ;

		String lotno = ticket.substring(0, 4);
		if((LotteryType.JCZQ_HHGG.value+"").equals(lotno)){
			returnString = convertMix(value,lotno);
		}else {
			returnString = convert(value,lotno);
		}
		return returnString;
	}
	
	// 非混合格式下的赔率转换
	private static String convert(String context,String lotteryid) throws DocumentException{
		//  math的id和playid
		Document doc = DocumentHelper.parseText(context);
		Element root = doc.getRootElement();
		List<Node> matches = root.selectNodes("/Project/bill/match");
		int i = 0 ;
		StringBuffer sb = new  StringBuffer();
		for(Node match:matches) {
			String text = match.getText();
			String id = "20"+((Attribute)match.selectObject("@id")).getValue();
			sb.append(id);
			sb.append(appendSPString(text,lotteryid));
			if(i!=matches.size()-1){
				sb.append("|");
			}
			i++;
		}
		System.out.println(sb.toString());
		return sb.toString();
	}
	// 大赢家的赔率串转换成自己家的赔率串
	private static String appendSPString(String spString,String lotteryid) {
		StringBuffer returnString = new StringBuffer() ;
		returnString.append("(");
		StringBuffer peilv = new StringBuffer();
		String[] pei = spString.split("\\|");
		for(String arg :pei){
			String[] args = arg.split("=");
			String type= args[0];
			String value = args[1];
//			System.out.println("================"+type+"======"+convertMap.size());
			if(null==convertMap.get(type)){
				peilv.append(type);
			}else {
				peilv.append(convertMap.get(type));
			}
			peilv.append("_"+value+",");
		}
		returnString.append(StringUtil.removeEndCharacter(peilv.toString(), ","));
		returnString.append(")");
		return returnString.toString();
	}
	// 混合格式下的赔率转换
	private static String convertMix(String context,String lotteryid) throws DocumentException{
		//  math的id和playid
		Document doc = DocumentHelper.parseText(context);
		Element root = doc.getRootElement();
		List<Node> matches = root.selectNodes("/Project/bill/match");
		int i = 0 ;
		StringBuffer sb = new  StringBuffer();
		
		for(Node match:matches) {
			String text = match.getText();
			String id = "20"+((Attribute)match.selectObject("@id")).getValue();
			sb.append(id);
			// 只有混合才有这个
			String playid = ((Attribute)match.selectObject("@playid")).getValue();
			sb.append("*"+convertMap.get(playid));
			sb.append(appendSPString(text,lotteryid));
			if(i!=matches.size()-1){
				sb.append("|");
			}
			i++;
		}
		return sb.toString();
	}
}	

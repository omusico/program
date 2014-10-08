package com.dabaicong.program;

import java.util.Calendar;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JingcaiPeiluUtil {
	
	public static void main(String[] args) throws DocumentException {
//		String content = "<Project id=\"TE00000003740629\" chipmul=\"1\" chipcount=\"3\" chipmoney=\"6\"><bill id=\"20024017025926712671\" billtime=\"2014-02-24 18:35:54\" issue=\"20140224\" playid=\"SPF\" maxbonu=\"99999999\" ispass=\"1\" passway=\"2*1\" multi=\"1\"><match id=\"140224001\">3=2.46|1=7.91|0=8.93</match><match id=\"140224002\">3=2.18</match></bill></Project>";
		
//		String content2 = "<Project id=\"TE00000003741631\" chipmul=\"1\" chipcount=\"3\" chipmoney=\"6\"><bill id=\"20096251481602580237\" billtime=\"2014-02-24 19:34:19\" issue=\"20140224\" playid=\"HH\" maxbonu=\"99999999\" ispass=\"1\" passway=\"2*1\" multi=\"1\"><match id=\"140224001\" playid=\"RQSPF\" rq=\"-4\">3=3.02|1=5.29|0=4.43</match><match id=\"140224002\" playid=\"SPF\">3=5.08</match></bill></Project>";
//		System.out.println(getPeiluMix(content2));
		
		
//		String content3 = "eJxNybERgCAQRNF2wHFwb+9ACDBkiLUAGqF4CQz8wU8eETOKQWKmnsBaHuVryOh9Pu0SA2kKqepSiMkfcBpIv9/tx6xcOtSVcJqf3OQF8bIU7Q==";
		
//		System.out.println(new String(ZlibUtil.decompress(Base64.decode(content3))));
		
		String content4 = "RFSF|140224301=11.5_3(4.97)/0(1.13),140224302=34.5_3(2.86)|2*1";
		
		String content5 = "HH|SF>140224301=3(6.56)/0(3.22),RFSF>140224302=26.5_3(9.74)|2*1";
		
		System.out.println(convertLQToDoc(content5));
	}
	private static Logger logger = LoggerFactory.getLogger(JingcaiPeiluUtil.class);
	
	public static String convertPeilu(String betcode,String content,String lotno){
		
		String peilu = "";
		// 关于注码格式的判断，看是不是竞彩
		if(betcode.startsWith("500")&&(!lotno.equals("J00002"))&&(!lotno.equals("J00007"))) {
			return peilu;
		}
		try {
			// if(isJingcaiLQ(lotno)) {
			// 	content = convertLQToDoc(content);
			// }
			// 判断是否是混合
			if(isMix(betcode, lotno)) {
				peilu = getPeiluMix(content);
			}else {
				lotno = getRealLotno(lotno, betcode);
				peilu = getPeilu(content, lotno);
			}
		}catch(Exception e) {
			logger.info("转换赔率异常",e);
			throw new RuntimeException();
		}
		return peilu;
	}

	private static String convertLQToDoc(String peilu) {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("Project");
		Element bill = root.addElement("bill");
		bill.addAttribute("playid", peilu.split("\\|")[0]);
		
		for(String matchStr:peilu.split("\\|")[1].split(",")) {
			Element match = bill.addElement("match");
			if("RFSF".equals(peilu.split("\\|")[0])) {
				match.addAttribute("id", matchStr.split("=")[0]);
				match.addAttribute("rf", matchStr.split("=")[1].split("\\_")[0]);
				match.setText(convertPeiluStr(matchStr.split("=")[1].split("\\_")[1]));
			}else if("DXF".equals(peilu.split("\\|")[0])) {
				match.addAttribute("id", matchStr.split("=")[0]);
				match.addAttribute("zf", matchStr.split("=")[1].split("\\_")[0]);
				match.setText(convertPeiluStr(matchStr.split("=")[1].split("\\_")[1]));
			}else if("HH".equals(peilu.split("\\|")[0])) {
				String type = matchStr.split(">")[0];
				match.addAttribute("id", matchStr.split(">")[1].split("=")[0]);
				match.addAttribute("playid", type);
				if("RFSF".equals(type)) {
					match.addAttribute("rf", matchStr.split(">")[1].split("=")[1].split("\\_")[0]);
					match.setText(convertPeiluStr(matchStr.split("=")[1].split("\\_")[1]));
				}else if("DXF".equals(type)) {
					match.addAttribute("zf", matchStr.split(">")[1].split("=")[1].split("\\_")[0]);
					match.setText(convertPeiluStr(matchStr.split("=")[1].split("\\_")[1]));
				}else {
					match.setText(convertPeiluStr(matchStr.split("=")[1]));
				}
			}else {
				match.addAttribute("id", matchStr.split("=")[0]);
				match.setText(convertPeiluStr(matchStr.split("=")[1]));
			}
		}
		return doc.asXML();
		
	}
	
	
	private static String convertPeiluStr(String peilu) {
		peilu = peilu.replace("/", "|").replace(")", "").replace("(", "=");
		
		StringBuilder peilubuilder = new StringBuilder();
		
		return peilu;
	}
	
	public static String getPeilu(String content,String lotno) throws DocumentException {
		Document doc = DocumentHelper.parseText(content);
		Element root = doc.getRootElement();
		List<Node> matches = root.selectNodes("/Project/bill/match");
		
		StringBuilder peilu = new StringBuilder();
		for(Node match:matches) {
			String id = ((Attribute)match.selectObject("@id")).getValue();
			String day = id.substring(0,6);
			String teamid = id.substring(6);
			
			peilu.append("20"+day).append("*").append(getWeekid(day)).append("*").append(teamid).append("*|");
			peilu.append(appendSP(match, lotno));
		}
		return peilu.toString();
	}
	
	
	
	public static String getPeiluMix(String content) throws DocumentException {
		Document doc = DocumentHelper.parseText(content);
		Element root = doc.getRootElement();
		List<Node> matches = root.selectNodes("/Project/bill/match");
		StringBuilder peilu = new StringBuilder();
		for(Node match:matches) {
			String id = ((Attribute)match.selectObject("@id")).getValue();
			String lotteryid = getLotteryType(((Attribute)match.selectObject("@playid")).getValue());
			String day = id.substring(0,6);
			String teamid = id.substring(6);
			
			peilu.append("20"+day).append("*").append(getWeekid(day)).append("*").append(teamid).append("*|").append(lotteryid).append("|");
			peilu.append(appendSP(match, lotteryid));
		}
		return peilu.toString();
	}



	private static String appendSP(Node match,
			String lotteryid) {
		StringBuilder peilu = new StringBuilder();
		for(String betsp:match.getText().split("\\|")) {
			String score = betsp.split("=")[0].replace(":", "").replace("-", "");
			if("J00008".equals(lotteryid)) {
				score = score.replace("3", "1").replace("0", "2");
			}
			String sp = betsp.split("=")[1];
			if("J00013".equals(lotteryid)) {
				String rq = ((Attribute)match.selectObject("@rq")).getValue();
				peilu.append(score).append("(").append(rq).append(")").append(":").append(sp).append("|");
			}else if("J00006".equals(lotteryid)) {
				String rf = ((Attribute)match.selectObject("@rf")).getValue();
				peilu.append(score).append("(").append(rf).append(")").append(":").append(sp).append("|");
			}else if("J00008".equals(lotteryid)) {
				String zf = ((Attribute)match.selectObject("@zf")).getValue();
				peilu.append(score).append("(").append(zf).append(")").append(":").append(sp).append("|");
			}else {
				peilu.append(score).append(":").append(sp).append("|");
			}
			
		}
		peilu.append("^");
		return peilu.toString();
	}
	
	
	
	private static String getWeekid(String date) {
		Calendar calendar = Calendar.getInstance();//获得一个日历
		int year = Integer.parseInt("20"+date.substring(0, 2));
		int month = Integer.parseInt(date.substring(2, 4));
		int day = Integer.parseInt(date.substring(4, 6));
	    calendar.set(year, month-1, day);//设置当前时间,月份是从0月开始计算
	    int number = calendar.get(Calendar.DAY_OF_WEEK);//星期表示1-7，是从星期日开始，   
	    String [] str = {"","7","1","2","3","4","5","6"};
	    return str[number];
	}
	
	
	
	private static String getOwnJingcaiPlayType(String dyjtype) {
		String type = "";
		if("1*1".equals(dyjtype)) {
			type = "500";
		}else if("2*1".equals(dyjtype)) {
			type = "502";
		}else if("3*1".equals(dyjtype)) {
			type = "503";
		}else if("4*1".equals(dyjtype)) {
			type = "504";
		}else if("5*1".equals(dyjtype)) {
			type = "505";
		}else if("6*1".equals(dyjtype)) {
			type = "506";
		}else if("7*1".equals(dyjtype)) {
			type = "507";
		}else if("8*1".equals(dyjtype)) {
			type = "508";
		}else if("3*3".equals(dyjtype)) {
			type = "526";
		}else if("3*4".equals(dyjtype)) {
			type = "527";
		}else if("4*4".equals(dyjtype)) {
			type = "539";
		}else if("4*5".equals(dyjtype)) {
			type = "540";
		}else if("4*6".equals(dyjtype)) {
			type = "528";
		}else if("4*11".equals(dyjtype)) {
			type = "529";
		}else if("5*5".equals(dyjtype)) {
			type = "544";
		}else if("5*6".equals(dyjtype)) {
			type = "545";
		}else if("5*10".equals(dyjtype)) {
			type = "530";
		}else if("5*16".equals(dyjtype)) {
			type = "541";
		}else if("5*20".equals(dyjtype)) {
			type = "531";
		}else if("5*26".equals(dyjtype)) {
			type = "532";
		}else if("6*6".equals(dyjtype)) {
			type = "549";
		}else if("6*7".equals(dyjtype)) {
			type = "550";
		}else if("6*15".equals(dyjtype)) {
			type = "533";
		}else if("6*20".equals(dyjtype)) {
			type = "542";
		}else if("6*22".equals(dyjtype)) {
			type = "546";
		}else if("6*35".equals(dyjtype)) {
			type = "534";
		}else if("6*42".equals(dyjtype)) {
			type = "543";
		}else if("6*50".equals(dyjtype)) {
			type = "535";
		}else if("6*57".equals(dyjtype)) {
			type = "536";
		}else if("7*7".equals(dyjtype)) {
			type = "553";
		}else if("7*8".equals(dyjtype)) {
			type = "554";
		}else if("7*21".equals(dyjtype)) {
			type = "551";
		}else if("7*35".equals(dyjtype)) {
			type = "547";
		}else if("7*120".equals(dyjtype)) {
			type = "537";
		}else if("8*8".equals(dyjtype)) {
			type = "556";
		}else if("8*9".equals(dyjtype)) {
			type = "557";
		}else if("8*28".equals(dyjtype)) {
			type = "555";
		}else if("8*56".equals(dyjtype)) {
			type = "552";
		}else if("8*70".equals(dyjtype)) {
			type = "548";
		}else if("8*247".equals(dyjtype)) {
			type = "538";
		}
		return type;
	}
	
	
	private static String getLotteryType(String dyjlotno) {
		String dyjType = "";
		if("SPF".equals(dyjlotno)) {
			dyjType = "J00001";
		}else if("CBF".equals(dyjlotno)) {
			dyjType = "J00002";
		}else if("JQS".equals(dyjlotno)) {
			dyjType = "J00003";
		}else if("BQC".equals(dyjlotno)) {
			dyjType = "J00004";
		}else if("RQSPF".equals(dyjlotno)) {
			dyjType = "J00013";
		}else if("SF".equals(dyjlotno)) {
			dyjType = "J00005";
		}else if("RFSF".equals(dyjlotno)) {
			dyjType = "J00006";
		}else if("SFC".equals(dyjlotno)) {
			dyjType = "J00007";
		}else if("DXF".equals(dyjlotno)) {
			dyjType = "J00008";
		}else if("HH".equals(dyjlotno)) {
			dyjType = "J00011";
		}else if("HH".equals(dyjlotno)) {
			dyjType = "J00012";
		}
		
		return dyjType;
	}
	
	
	
	private static boolean isMix(String betcode,String lotno) {
		boolean flag = false;
		if("J00011".equals(lotno)||"J00012".equals(lotno)) {
			String splitlotno = betcode.split("\\@")[1].split("\\^")[0].split("\\|")[3];
			for(String code:betcode.split("\\@")[1].split("\\^")) {
				if(!code.split("\\|")[3].equals(splitlotno)) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}
	
	/*
	private static boolean isJingcaiLQ(String lotno) {
		if ("J00005".equals(lotno) || "J00006".equals(lotno)
				|| "J00007".equals(lotno) || "J00008".equals(lotno)
				|| "J00012".equals(lotno) ) {
			return true;
		}
		return false;
	}
	*/
	
	
	private static String getRealLotno(String lotno,String betcode) {
		if("J00011".equals(lotno)||"J00012".equals(lotno)) {
			if(!isMix(betcode, lotno)) {
				lotno = betcode.split("\\@")[1].split("\\^")[0].split("\\|")[3];
			}
		}
		
		return lotno;
	}
}

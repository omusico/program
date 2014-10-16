package com.dabaicong.program;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.lottery.common.contains.lottery.LotteryType;
import com.lottery.common.util.StringUtil;
import com.lottery.core.domain.JczqRace;
import com.lottery.core.domain.ticket.Ticket;

public class pro {
	/**
     * 用于处理竞彩的信息
     * @param list   包含票的list
     * @return JsonArray  返回的array
     */
    private JSONArray deal( List<Ticket> list,String orderPlaytype) {
		JSONArray array = new JSONArray();
		Map<String, JSONObject> map = new HashMap<String, JSONObject>();
		for(Ticket ticket: list){
			String content = ticket.getContent();
			String[] match = content.split("-")[1].split("\\|");
			JSONObject json = null ;
			// 赔率string
			String odds = ticket.getPeilv();
			// 玩法300812001.hh301112001
			int playtype = ticket.getPlayType();
			for(String m:match){
				//  m,20141009003(01),matchno 003
				// 	m,20141009003*3010(0),matchno 003
				String matchno = m.substring(8, 11);
				if(map.containsKey(matchno))
					json = map.get(matchno);
				else 
					json = new JSONObject();
				JczqRace race = jczqRaceService.getByMatchNum(m.substring(0,11)) ;
				// 比赛名字,在race里,不用考虑累加
				json.put("matchName", race.getHomeTeam()+"VS"+race.getAwayTeam());
				// 比分,在race里,不用考虑累加
				String finalScore = race.getFinalScore();
				if(StringUtils.isBlank(finalScore))
					finalScore ="";
				json.put("score", finalScore);
				// 为了混合准备的
				String type = (json.containsKey("playtype"))?json.getString("playtype"):"";
				type = dealType(m,type,playtype+""); 
				json.put("playtype", type);
				json.put("result", getRaceResult(race));
				String peilv = "";
				if(json.containsKey("peilv")){
					peilv = json.getString("peilv");
				}
//				peilv = dealPeilv(matchno,odds,peilv,playtype+"");
				json.put("peilv", dealPeilv(matchno,odds,peilv,playtype+"",orderPlaytype));
				System.out.println("peilv"+json.getString("peilv"));
				//  这里需要处理下content
				json.put("content", dealContent(ticket.getContent(),matchno));
				map.put(matchno, json);
			}
			
		}
		return JSONArray.fromObject(map);
	}
    private String dealContent(String content,String matchno) {
		// "300713001-20141014004(20,32)|20141014005(32,50)|20141014006(20,32)|20141014007(20,32)^!300714001-20141014004(20,32)|20141014005(32,50)|20141014006(20,32)|20141014007(20,32)^"
    	String returnString ="";
    	String[] match = content.split("-")[1].split("\\|");
    	Set<String> betVale= new HashSet<String>();
    	for(String everyMatch :match){
    		if(everyMatch.contains(matchno)){
    			String type = everyMatch.substring(everyMatch.indexOf('(')+1,everyMatch.indexOf(')'));
    			for(String typeVale:type.split(",")){
    				betVale.add(typeVale);
    			}
    		}
    	}
    	for(String value:betVale){
    		returnString+=(value+",");
    	}
    	return StringUtil.removeEndCharacter(returnString, ",");
	}
	private JSONObject getRaceResult(JczqRace race) {
    	JSONObject result = new JSONObject();
    	if (race == null)
    		return result; 
    	String handicap =race.getHandicap();
    	String firstHalf = race.getFirstHalf();
    	String secondHalf =race.getSecondHalf();
    	String finalScore =race.getFinalScore();
    	if(StringUtil.isBlank(handicap)){
    		handicap="";
    	}
    	if(StringUtil.isBlank(firstHalf)){
    		firstHalf="";
    	}
    	if(StringUtil.isBlank(secondHalf)){
    		secondHalf="";
    	}
    	if(StringUtil.isBlank(finalScore)){
    		finalScore="";
    	}
    	result.put("handicap", ""+handicap);
    	result.put("firstHalf", ""+firstHalf);
    	result.put("secondHalf", ""+secondHalf);
    	result.put("finalScore", ""+finalScore);
    	
		return result;
	}
	// 根据内容获得玩法,并且拼接原来的玩法字符串
    private String dealType(String content,String type,String playtype){
    	//  判断是否是混合过关,这里可能出现好多的重复玩法，注意去重，所以才会有返回值是type
    	if(content.contains("*")&&type.contains(content.substring(12, 16))){
    		return type;
    	}else if(type.contains(playtype.substring(0, 4))) {
    		return type;
    	}
    	if(content.contains("*")){
    		if(!type.equals(""))
    			type+=("_"+content.substring(12, 16));
    		else 
    			type+= content.substring(12, 16);
    	}
    	// 不是混个过关
    	else { 
    		if(!type.equals(""))
    			type+=("_"+playtype.substring(0, 4));
    		else 
    			type+= playtype.substring(0, 4);
    	}
    	return type ;
    }
    // 根据内容和赔率获得该玩法的赔率
    private String dealPeilv(String matchno,String odds,String peilv,String playtype,String ordertype){
    	// odds  20140929002*3010(0_9.22)|20140929003*3007(30_3.11)|20140929004*3009(30_2.63)|20140929005*3007(30_8.75)
    	//       20141014005(32_3.44,50_2.31)|20141014006(20_2.03,32_2.96)|20141014007(20_2.5,32_5.43)
    	//		
    	// 根据场次,还有赔率串获得赔率串的信息
    	String realPlayType =getPlaytypeByMatchno(odds, matchno,playtype);
    	String content = dealOddString(odds,matchno,playtype);
    	String returnString = content;
    	if(StringUtil.isBlank(peilv) ){
    		if(ordertype.startsWith(LotteryType.JCZQ_HHGG.value+""))
	    		peilv = (realPlayType+"("+content+")");
    		else 
    			peilv = content;
    		return peilv;
    	}
    	//  content = 32_3.44,50_2.31
    	String[] singleContent = content.split(",");
    	for(String single:singleContent){
	    	// 得到sp的key和value
	    	String spKey = single.split("_")[0];
	    	String spVale = single.split("_")[1];
	    	// 不是空的，开始比较，原来32_2.17,33_3.65  ,content
	    	//  hhgg :3009(32_3.44,50_2.31)     非混合gg: 32_3.44,50_2.31
	    	String sourcePeilv = getSourcePeilv(peilv,realPlayType,single);
	    	String[] source = sourcePeilv.split(",");
	    	boolean iscontain = true ;
	    	for(String tmp :source ){
	    		String tmpKey = tmp.split("_")[0];
		    	String tmpVale = tmp.split("_")[1];
		    	if(tmpKey.equals(spKey)){
		    		iscontain = false ;
		    		// 更改范围,首先把single从returnString中移除，再把改变后的加进去
		    		String replaceValue = (spKey+"_"+getArrangeString(tmpVale,spVale));
		    		returnString =returnString.replace(single, replaceValue);
		    		break ;
		    	}
	    	}
	    	if(iscontain){
	    		returnString+=(","+single);
	    	}
    	}
    	
    	if(ordertype.startsWith(LotteryType.JCZQ_HHGG.value+"")&&playtype.contains(realPlayType))
    		returnString = (realPlayType+"("+returnString+")");
    	else if(ordertype.startsWith(LotteryType.JCZQ_HHGG.value+"")&&!playtype.contains(realPlayType)){
    		if (peilv.contains(realPlayType)){
    			returnString = (realPlayType+"("+returnString+")");
    			return returnString;
    		}
    		returnString = peilv+("#"+realPlayType+"("+returnString+")");
    	}
    	return returnString;
    	
    }
    private String getSourcePeilv(String peilv,String realtype,String single) {
    	
    	if(peilv.contains("(")&&peilv.contains(realtype)){  // 混合过关
    		return peilv.substring(peilv.indexOf('(')+1,peilv.indexOf(')'));
    	}else if (peilv.contains("(")&&!peilv.contains(realtype)){
    		return single;
    	}
    	return peilv;
	}
	// 竞彩中，针对hhgg，获得赔率中的玩法
    private String getPlaytypeByMatchno(String odds,String matchno,String playtype) {
    	// 如果是混合过关，直接返回前四个
    	if(!playtype.startsWith(LotteryType.JCZQ_HHGG.value+"")){
    		return playtype.substring(0,4);
    	}
    	// hhgg，则需要找到指定场次，按照场次去
    	String [] spString = odds.split("\\|");
		for(int i =0 ; i<spString.length;i++){
			if(spString[i].substring(8,11).equals(matchno))
				return spString[i].substring(spString[i].indexOf('*')+1,spString[i].indexOf('('));
		}
    	return "";
	}
    //  竞彩根据原来的赔率，新的赔率，获得赔率范围
	private String getArrangeString(String src,String sp){
    	// 赔率串包含~
    	if(src.contains("~")){
    		float start = Float.parseFloat(src.split("~")[0]);
    		float end = Float.parseFloat(src.split("~")[1]);
    		float spFloat = Float.parseFloat(sp);
    		if(spFloat>end)
    			end = spFloat;
    		if(spFloat<start)
    			start= spFloat;
    		return start+"~"+end;
    	}else {
    		float start = Float.parseFloat(src);
    		float end = Float.parseFloat(sp);
    		if(start<end)
    			return start+"~"+end;
    		else if (start>end)
    			return end+"~"+start;
    		else 
    			return start+"";
    	}
    }
	private String dealOddString(String odds,String matchno,String playtype) {
		String [] spString = odds.split("\\|");
		for(int i =0 ; i<spString.length;i++){
			if(spString[i].contains("*")&&spString[i].contains(playtype)){
				return spString[i].substring(spString[i].indexOf('(')+1,spString[i].indexOf(')'));
			}
			else if(spString[i].substring(8,11).equals(matchno))
				return spString[i].substring(spString[i].indexOf('(')+1,spString[i].indexOf(')'));
		}
		return "" ;
	}
}

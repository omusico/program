package com.dabaicong.program;

import org.apache.commons.lang.StringUtils;



public class pro {
	
	public static void main(String[] args) {
		String[] separator = StringUtils.splitByWholeSeparator("141_20141126047_2014-11-26 16:10:18_2014-11-26 16:19:48_1_null", "_");
		String startTime = separator[2]; //startTime
		String endTime = separator[3]; //startTime
		String status = separator[4]; //endTime
		System.out.println(startTime);
		System.out.println(endTime);
		System.out.println(status);
	}
}

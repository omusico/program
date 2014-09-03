package com.dabaicong.program;

import java.math.BigDecimal;

public class Caluate {
	public static void main(String[] args) {
		String test = null ;
		BigDecimal a = new BigDecimal(test);
		System.out.println( a.add(new BigDecimal(1000)) );
	}
}

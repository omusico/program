package com.dabaicong.program;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreadTest {
	public static void main(String[] args) throws InterruptedException {
		
		
		
		
		long time = System.currentTimeMillis();
		long reservTime = 1411366863281L;
		Date d = new Date(reservTime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf.format(d));
		
		
		
	}
}

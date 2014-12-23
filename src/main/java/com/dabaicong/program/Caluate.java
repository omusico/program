package com.dabaicong.program;



public class Caluate {
	public static void main(String[] args) {
		String zhuma = "013,013,013,013,013,~,~,~,~,~,~,~,~,~#~,~,~,~,~,013,013,013,013,013,~,~,~,~^"; 
		String s =  zhuma.replace("#", "$").replace("~", "#").replace("^", "");
		System.out.println(s);
	}
}

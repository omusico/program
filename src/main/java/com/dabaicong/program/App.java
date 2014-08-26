package com.dabaicong.program;




/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	convert(50L, 0);
    }
	public static  boolean convert(long num ,long safeAmt) {
		if (num == 0 && safeAmt == 0) {
			System.out.println("1111111111111");
		}
		if (num < 0 || safeAmt < 0) {
			System.out.println("222222222222222");
		}
		return false ;
    }
}

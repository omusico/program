package com.dabaicong.jpa.util;

public class DyjResult {
 
	private String xmsgID = "";
	private String xcode = "";
	private String xmessage = "";
	private String xvalue = "";
	private String xsign = "";
	
	public String getXmsgID() {
		return xmsgID;
	}
	public void setXmsgID(String xmsgID) {
		this.xmsgID = xmsgID;
	}
	public String getXcode() {
		return xcode;
	}
	public void setXcode(String xcode) {
		this.xcode = xcode;
	}
	public String getXmessage() {
		return xmessage;
	}
	public void setXmessage(String xmessage) {
		this.xmessage = xmessage;
	}
	public String getXvalue() {
		return xvalue;
	}
	public void setXvalue(String xvalue) {
		this.xvalue = xvalue;
	}
	public String getXsign() {
		return xsign;
	}
	public void setXsign(String xsign) {
		this.xsign = xsign;
	}	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("xmsgID: ")
			.append(getXmsgID())
			.append("xcode: ")
			.append(getXcode())
			.append("xmessage: ")
			.append(getXmessage())
			.append("xvalue: ")
			.append(getXvalue())
			.append("xsign: ")
			.append(getXsign());
		return builder.toString();
	}
}

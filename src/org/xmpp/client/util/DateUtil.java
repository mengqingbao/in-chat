package org.xmpp.client.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtil {
	
	public static String getDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return format.format(new Date());
	}
	
	public static String toString(Date date){
		if(date==null){
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return format.format(date);
	}
	
	public static Date parseDate(String date){
		if(date==null||date==""){
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			format.parse(date);
		} catch (ParseException e) {
			return null;
		}
		return null;
		
	}

}

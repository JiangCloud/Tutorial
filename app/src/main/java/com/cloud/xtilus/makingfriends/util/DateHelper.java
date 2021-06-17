package com.cloud.xtilus.makingfriends.util;

import android.annotation.SuppressLint;
import java.sql.Date;
import java.text.SimpleDateFormat;

@SuppressLint("SimpleDateFormat") public class DateHelper {
	public final static long DIS_INTERVAL = 300; 
	public static String GetStringFormat(long millis){
		SimpleDateFormat sdf= new SimpleDateFormat("MM月dd HH:mm"); 
		java.util.Date dt = new Date(millis*1000);   		
		return sdf.format(dt);
	}
	
	public static boolean LongInterval(long current,long last){
		return (current-last)>DIS_INTERVAL ? true:false;
	}
	public static String getStringTime(long time){
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTime = sdf.format(date);
		return startTime;
	}
}

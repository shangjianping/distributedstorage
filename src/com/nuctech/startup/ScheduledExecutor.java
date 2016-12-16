package com.nuctech.startup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutor {
	public static void main(String[] args){
		long oneDay = 24 * 60 * 60 *1000;
		long initDelay = getTimeMillis("14:33:00") -System.currentTimeMillis();
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(new RunnableTest(),initDelay, oneDay, TimeUnit.MILLISECONDS);
		//ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		//service.scheduleAtFixedRate(new RunnableTest(),initDelay, oneDay, TimeUnit.MILLISECONDS);
	}
	
	
	private static long getTimeMillis(String time){
		try{
			DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			DateFormat dayFormat= new SimpleDateFormat("yyyy-MM-dd");
			Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " +time);
			return curDate.getTime();
		}catch (Exception  e){
			e.printStackTrace();
		}
		return 0;
	}

}

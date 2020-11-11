package com.gt.wan_gt.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * 时间相关工具
 */
public class DateUtil {

	public synchronized static String getTodayOrTomorrow(long time){
		Calendar calToday = Calendar.getInstance();
		calToday.setTimeInMillis(System.currentTimeMillis());

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);

		int dayDiff = cal.get(Calendar.DAY_OF_MONTH) - calToday.get(Calendar.DAY_OF_MONTH);

		if(dayDiff == 1){
			return "明日";
		}else if(dayDiff == 2){
			return "后天";
		}else if(dayDiff == 0){
			return "今日";
		}
		return cal.get(Calendar.DAY_OF_MONTH)+"日";
	}

	/**
	 * 获得今天0点的毫秒时间
	 */
	public synchronized static long getTodayMillisTime(){
		Calendar cal = Calendar.getInstance();
		cal.clear(Calendar.HOUR_OF_DAY);
		cal.clear(Calendar.HOUR);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		return cal.getTimeInMillis();
	}
	
	/**
	 * 获得明天0点的毫秒时间
	 */
	public synchronized static long getTomorrowMillisTime(){
		Calendar cal = Calendar.getInstance();
		cal.clear(Calendar.HOUR_OF_DAY);
		cal.clear(Calendar.HOUR);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		cal.add(Calendar.DAY_OF_MONTH, 1);

		return cal.getTimeInMillis();
	}
	
	/**
	 * 获得昨天0点的毫秒时间
	 */
	public synchronized static long getYesterdayMillisTime(){
		Calendar cal = Calendar.getInstance();
		cal.clear(Calendar.HOUR_OF_DAY);
		cal.clear(Calendar.HOUR);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		cal.add(Calendar.DAY_OF_MONTH, -1);

		return cal.getTimeInMillis();
	}

	public synchronized static long getDayMillisTime(int day) {
		Calendar cal = Calendar.getInstance();
		cal.clear(Calendar.HOUR_OF_DAY);
		cal.clear(Calendar.HOUR);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		
		cal.add(Calendar.DAY_OF_MONTH, day);
		
		return cal.getTimeInMillis();
	}
	
	/**
	 * 获取秒
	 */
	public synchronized static int getSeconds(long time){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return calendar.get(Calendar.SECOND);
	}

	/**
	 * 获取当前的时间 格式 yyyy-MM-dd HH:mm:ss
	 */
	public synchronized static String getStringDate(long timeMill){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		return format.format(timeMill);
	}
	/**
	 * 获取当前的时间 格式 yyyy-MM-dd HH:mm:ss
	 */
	public synchronized static String getStringDate(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		return format.format(System.currentTimeMillis());
	}
	/**
	 * 获取当前的时间 格式 yyyy-MM-dd
	 */
	public synchronized static String getStringDateNoHour(long timeMill){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		return format.format(timeMill);
	}

	/**
	 * 当前时间 yyyyMMdd
	 */
	public synchronized static String getCurrentDate(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
		return format.format(new Date());
	}
	/**
	 * 将格式化时间转换成毫秒值
	 * 2018-01-01 00:00:00
	 */
	public synchronized static long getTimeMill(String date){
		long time = 0;
		if(TextUtils.isEmpty(date)){
			return time;
		}
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);//24小时制
			time = simpleDateFormat.parse(date).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}
	/**
	 * 获取过去第几天的日期
	 */
	public static String getPastDate(int past,String formatStr) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
		Date today = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat(formatStr,Locale.CHINA);
		return format.format(today);
	}
	/**
	 * 获取过去或者未来 任意天内的日期数组
	 * @param intervals      intervals天内
	 *                       type 0 过去 1 未来
	 */
	public static ArrayList<String> getDayList(int intervals, String format, int type) {
		ArrayList<String> pastDaysList = new ArrayList<>();
		ArrayList<String> futureDaysList = new ArrayList<>();
		for (int i = intervals-1; i >=0; i--) {
			if(type == 0){
				pastDaysList.add(getPastDate(i,format));
			}else{
				futureDaysList.add(getFutureDate(i,format));
			}
		}
		if(type == 0){
			return pastDaysList;
		}else{
			return futureDaysList;
		}
	}

	/**
	 * 获取未来 第 past 天的日期
	 * @param past
	 */
	public static String getFutureDate(int past,String formatStr) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
		Date today = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat(formatStr,Locale.CHINA);
		return format.format(today);
	}

	/**
	 * 时间格式化显示 hh+":"+mm+":"+ss
	 */
	public static String missToFormat(long miss){
		String hh=miss/3600>9?miss/3600+"":"0"+miss/3600;
		String mm=(miss % 3600)/60>9?(miss % 3600)/60+"":"0"+(miss % 3600)/60;
		String ss=(miss % 3600) % 60>9?(miss % 3600) % 60+"":"0"+(miss % 3600) % 60;
		return hh+":"+mm+":"+ss;
	}
}

package com.kasite.core.common.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理
 * 
 * @author chenshun
 * @email 343675979@qq.com
 * @date 2016年12月21日 下午12:53:33
 */
public class DateUtils {
	/** 时间格式(yyyy-MM-dd) */
	public final static String DATE_PATTERN = "yyyy-MM-dd";
	/** 时间格式(yyyy-MM-dd HH:mm:ss) */
	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	/** 时间格式(yyyy-MM-dd HH:mm) */
	public final static String DATE_TIME14_PATTERN = "yyyy-MM-dd HH:mm";
	
	public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static String format(Date date, String pattern) {
        if(date != null){
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }
    
    /**
	 * 将时间格式的字符串类型转换为Date类型
	 * 
	 * @param dateString
	 *            所要转换的字符串对象
	 * @param returntype
	 *            所要转换的返回类型 (eg: yyyyMMdd ; yyyy-MM-dd HH:mm:ss)
	 * @return 返回returntype格式的日期
	 */
	public static Date formatStringtoDate(String dateString, String returntype) {
		Date date = null;
		SimpleDateFormat format = new SimpleDateFormat(returntype);
		try {
			date = format.parse(dateString);
		} catch (Exception e) {
			date = new Date();
		}
		return date;
	}
	
	/**
	 * 将时间类型转换为字符串类型的通用方法
	 * 
	 * @param date
	 *            要转换的时间对象
	 * @param returntype
	 *            所要转换的返回类型 (eg: yyyyMMdd ; yyyy-MM-dd HH:mm:ss)
	 * @return 返回returntype格式的字符串
	 */
	public static String formatDateToString(Date date, String returntype) {
		SimpleDateFormat format = new SimpleDateFormat(returntype);
		return format.format(date);
	}
	
	/**
	 * 将时间类型参数格式化为字符串类型
	 * 
	 * @param date
	 *            需要被格式话的时间对象
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String formatLongDateString(Date date) {
		return formatDateToString(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 将时间类型参数格式化为字符串类型
	 * 
	 * @param date
	 *            需要被格式话的时间对象
	 * @return yyyy-MM-dd
	 */
	public static String formatShortDateString(Date date, String returntype) {
		return formatDateToString(date, returntype);
	}
	
	/**
	 * 得到昨天开始的时间字符串
	 * 
	 * @param date
	 *            日期对象
	 * @return 昨天的日期字符串
	 */
	public static final String getYesterdayStartString(Date date) {
		Calendar yesterday = Calendar.getInstance();
		yesterday.setTime(date);
		yesterday.add(Calendar.DATE, -1);
		yesterday.set(Calendar.HOUR_OF_DAY, 0);  
		yesterday.set(Calendar.MINUTE, 0);  
		yesterday.set(Calendar.SECOND, 0);  
		return DateUtils.formatLongDateString(yesterday.getTime());
	}
	/**
	 * 得到昨天结束的时间字符串
	 * 
	 * @param date
	 *            日期对象
	 * @return 昨天的日期字符串
	 */
	public static final String getYesterdayEndString(Date date) {
		Calendar yesterday = Calendar.getInstance();
		yesterday.setTime(date);
		yesterday.add(Calendar.DATE, -1);
		yesterday.set(Calendar.HOUR_OF_DAY, 23);  
		yesterday.set(Calendar.MINUTE, 59);  
		yesterday.set(Calendar.SECOND, 59);  
		return DateUtils.formatLongDateString(yesterday.getTime());
	}
	/**
	 * 得到昨天开始的时间字符串
	 * 
	 * @param date
	 *            日期对象
	 * @return 昨天的日期字符串
	 */
	public static final String getYesterdayStartString(Date date, String formatStr) {
		Calendar yesterday = Calendar.getInstance();
		yesterday.setTime(date);
		yesterday.add(Calendar.DATE, -1);
		yesterday.set(Calendar.HOUR_OF_DAY, 0);  
		yesterday.set(Calendar.MINUTE, 0);  
		yesterday.set(Calendar.SECOND, 0);  
		return DateUtils.formatDateToString(yesterday.getTime(), formatStr);
	}
	/**
	 * 得到昨天结束的时间字符串
	 * 
	 * @param date
	 *            日期对象
	 * @return 昨天的日期字符串
	 */
	public static final String getYesterdayEndString(Date date, String formatStr) {
		Calendar yesterday = Calendar.getInstance();
		yesterday.setTime(date);
		yesterday.add(Calendar.DATE, -1);
		yesterday.set(Calendar.HOUR_OF_DAY, 23);  
		yesterday.set(Calendar.MINUTE, 59);  
		yesterday.set(Calendar.SECOND, 59);  
		return DateUtils.formatDateToString(yesterday.getTime(), formatStr);
	}
	
	/**
	 * 得到昨天的日期字符串
	 * 
	 * @param date
	 *            日期对象
	 * @return 昨天的日期字符串
	 */
	public static final String getYesterdayString(Date date) {
		Calendar yesterday = Calendar.getInstance();
		yesterday.setTime(date);
		yesterday.add(Calendar.HOUR_OF_DAY, -24);
		return DateUtils.formatShortDateString(yesterday.getTime(), "yyyy-MM-dd");
	}
	
	public static final String getYesterdayString(Date date, String returntype) {
		Calendar yesterday = Calendar.getInstance();
		yesterday.setTime(date);
		yesterday.add(Calendar.HOUR_OF_DAY, -24);
		return DateUtils.formatShortDateString(yesterday.getTime(), returntype);
	}
	
	public static final Date getYesterdayDate(Date date) {
		Calendar yesterday = Calendar.getInstance();
		yesterday.setTime(date);
		yesterday.add(Calendar.HOUR_OF_DAY, -24);
		return yesterday.getTime();
	}
	
	public static final Date getAfterDaysDate(Date date,int afterDays) {
		Calendar yesterday = Calendar.getInstance();
		yesterday.setTime(date);
		yesterday.add(Calendar.DATE, afterDays);
		return yesterday.getTime();
	}
	
	/**
	 * 得到某天开始的时间字符串
	 * 
	 * @param date
	 *            日期对象
	 * @return 昨天的日期字符串
	 */
	public static final String getStartForDayString(Date date) {
		Calendar yesterday = Calendar.getInstance();
		yesterday.setTime(date);
		yesterday.set(Calendar.HOUR_OF_DAY, 0);  
		yesterday.set(Calendar.MINUTE, 0);  
		yesterday.set(Calendar.SECOND, 0);  
		return DateUtils.formatLongDateString(yesterday.getTime());
	}
	/**
	 * 得到某天结束的时间字符串
	 * 
	 * @param date
	 *            日期对象
	 * @return 昨天的日期字符串
	 */
	public static final String getEndForDayString(Date date) {
		Calendar yesterday = Calendar.getInstance();
		yesterday.setTime(date);
		yesterday.set(Calendar.HOUR_OF_DAY, 23);  
		yesterday.set(Calendar.MINUTE, 59);  
		yesterday.set(Calendar.SECOND, 59);  
		return DateUtils.formatLongDateString(yesterday.getTime());
	}
	
	/**
	 * Timestamp时间类型转字符串
	 * 
	 * @param time
	 * @return
	 */
	public static final String getTimestampToStr(Timestamp time) {
		return getTimestampToStr(time, "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * Timestamp时间类型转字符串
	 * 
	 * @param time
	 * @return
	 */
	public static final String getTimestampToStr(Timestamp time, String returnType) {
		DateFormat sdf = new SimpleDateFormat(returnType);  
		if(time == null) {
			return "";
		}
		return sdf.format(time);
	}
}

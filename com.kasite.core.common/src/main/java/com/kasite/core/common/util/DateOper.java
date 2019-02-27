package com.kasite.core.common.util;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.coreframework.util.Time;
import com.kasite.core.common.config.KasiteConfig;


public class DateOper {
	private static final int[] dayArray = new int[]{
		31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
	};
	/**
	 * 时间格式
	 */ 
	public static final String TIME_FORMAT_HHmmss = "HHmmss";
	/**
	 * 标准日期格式
	 */
	public static final String DATE_FORMAT_STRING = "MM/dd/yyyy";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STRING);
	/**
	 * 标准时间格式
	 */
	public static final String DATE_TIME_FORMAT_STRING = "MM/dd/yyyy HH:mm";
	private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(DATE_TIME_FORMAT_STRING);
	/**
	 * ORA标准日期格式
	 */
	public static final String ORA_DATE_FORMAT_STRING = "yyyyMMdd";
	private static final SimpleDateFormat ORA_DATE_FORMAT = new SimpleDateFormat(ORA_DATE_FORMAT_STRING);
	/**
	 * ORA标准时间格式
	 */
	public static final String ORA_DATE_TIME_FORMAT_STRING = "yyyyMMddHHmm";
	private static final SimpleDateFormat ORA_DATE_TIME_FORMAT = new SimpleDateFormat(ORA_DATE_TIME_FORMAT_STRING);
	/**
	 * 时间格式
	 */
	public static final String LONG_DATE_TIME_FORMAT_STRING = "yyyyMMddHHmmss";
	//
	public static final String format6chars = "yyyyMM";
	public static final String format8chars = ORA_DATE_FORMAT_STRING;
	public static final String format12chars = ORA_DATE_TIME_FORMAT_STRING;
	public static final String format14chars = LONG_DATE_TIME_FORMAT_STRING;
	public static final String format16chars = "yyyy-MM-dd HH:mm";
	public static final String format19chars = "yyyy-MM-dd HH:mm:ss";
	public static final String format23chars = "yyyy-MM-dd HH:mm:ss,SSS";

	/**
	 * 把 字符串日期 转换成  Timestamp 对象
	 * @param theDate
	 * @return
	 * @throws ParseException
	 */
	public static Timestamp parse2Timestamp(String theDate) throws ParseException{
		return new Timestamp(parse(theDate).getTime());
	}
	public static synchronized Calendar getCalendar() {
		return GregorianCalendar.getInstance();
	}

	/**
	 * 返回日历的日期和时间对象
	 * @return Date
	 */
	public static Date getDateTime() {
		return getCalendar().getTime();
	}

	/**
	 * 获取年份
	 * @return int
	 */
	public static int getCurrentYear() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 获取阿拉伯数字表示的月份
	 * @return int
	 */
	public static int getCurrentMonth() {
		Calendar calendar = Calendar.getInstance();
		return 1 + calendar.get(Calendar.MONTH);
	}

	/**
	 * 获取阿拉伯数字表示的季度
	 * @return int
	 */
	public static int getCurrentQuarter() {
		Calendar calendar = Calendar.getInstance();
		int monthInt = 1 + calendar.get(Calendar.MONTH);
		int quarterInt = 1;
		switch (monthInt) {
			case 1:
				quarterInt = 1;
				break;
			case 2:
				quarterInt = 1;
				break;
			case 3:
				quarterInt = 1;
				break;
			case 4:
				quarterInt = 2;
				break;
			case 5:
				quarterInt = 2;
				break;
			case 6:
				quarterInt = 2;
				break;
			case 7:
				quarterInt = 3;
				break;
			case 8:
				quarterInt = 3;
				break;
			case 9:
				quarterInt = 3;
				break;
			case 10:
				quarterInt = 4;
				break;
			case 11:
				quarterInt = 4;
				break;
			case 12:
				quarterInt = 4;
				break;
			default:
				quarterInt = 1;
		}

		return quarterInt;
	}

	/**
	 * 获取当前日历中当天是当月的第几天
	 * @return int
	 */
	public static int getCurrentDayOfMonth() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取当前日历中当天是当年的第几天
	 * @return int
	 */
	public static int getCurrentDayOfYear() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 获取当前日历中当前这个星期是当年的第几个星期
	 * @return int
	 */
	public static int getCurrentWeekOfYear() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 获取当前日历中当前这个星期是当月的第几个星期
	 * @return int
	 */
	public static int getCurrentWeekOfMonth() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * 获取当前日历中当天是当周的第几天
	 * @return int
	 */
	public static int getCurrentDayOfWeek() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获取当前日历中当天的小时
	 * @return int
	 */
	public static int getCurrentHour() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取当前日历中当天的分钟
	 * @return int
	 */
	public static int getCurrentMinute() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * 获取当前日历中当天的秒数
	 * @return int
	 */
	public static int getCurrentSecond() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.SECOND);
	}

	/**
	 * 获取当前日历中当天的毫秒数
	 * @return int
	 */
	public static int getCurrentMillisecond() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.MILLISECOND);
	}

	/**
	 * 获取当前日历中的时代标志，公元或公元前
	 * @return int
	 */
	public static int getEra() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.ERA);
	}

	/**
	 * 获取当前时区
	 * @return String
	 */
	public static String getUSTimeZone() {
		String[] zones = new String[]{
			"Hawaii", "Alaskan", "Pacific",
			"Mountain", "Central", "Eastern"
		};

		return zones[10 + getZoneOffset()];
	}

	/**
	 * 获取当前时区与格林制相差的小时数
	 * @return int
	 */
	public static int getZoneOffset() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.ZONE_OFFSET) / (60 * 60 * 1000);
	}

	/**
	 * 获取当前时区白昼储备的偏移量
	 * @return int
	 */
	public static int getDSTOffset() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.DST_OFFSET) / (60 * 60 * 1000);
	}

	/**
	 * 获取上/下午的标志，AM或PM
	 * @return int
	 */
	public static int getAMPM() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.AM_PM);
	}

	/**
	 * 返回日历的yyyy-MM-dd HH:mm:ss,SSS格式
	 * @return
	 */
	public static synchronized String getDateMilliFormat() {
		Calendar cal = Calendar.getInstance();
		return getDateMilliFormat(cal);
	}

	/**
	 * 返回日历的yyyy-MM-dd HH:mm:ss,SSS格式
	 * @param cal
	 * @return String
	 */
	public static String getDateMilliFormat(Calendar cal) {
		return getDateFormat(cal, format23chars);
	}

	/**
	 * 返回日历的yyyy-MM-dd HH:mm:ss,SSS格式
	 * @param date
	 * @return String
	 */
	public static String getDateMilliFormat(Date date) {
		return getDateFormat(date, format23chars);
	}

	/**
	 * 解析日历的yyyy-MM-dd HH:mm:ss,SSS格式
	 * @param strDate
	 * @return Calendar
	 */
	public static Calendar parseCalendarMilliFormat(String strDate) {
		return parseCalendarFormat(strDate, format23chars);
	}

	/**
	 * 解析日历的yyyy-MM-dd HH:mm:ss,SSS格式
	 * @param strDate
	 * @return Date
	 */
	public static Date parseDateMilliFormat(String strDate) {
		return parseDateFormat(strDate, format23chars);
	}

	/**
	 * 返回日历的yyyy-MM-dd HH:mm:ss格式
	 * @return String
	 */
	public static String getDateSecondFormat() {
		Calendar cal = Calendar.getInstance();
		return getDateSecondFormat(cal);
	}

	/**
	 * 返回日历的yyyy-MM-dd HH:mm:ss格式
	 * @param cal
	 * @return String
	 */
	public static String getDateSecondFormat(Calendar cal) {
		return getDateFormat(cal, format19chars);
	}

	/**
	 * 返回日历的yyyy-MM-dd HH:mm:ss格式
	 * @param date
	 * @return String
	 */
	public static String getDateSecondFormat(Date date) {
		return getDateFormat(date, format19chars);
	}

	/**
	 * 解析日历的yyyy-MM-dd HH:mm:ss格式
	 * @param strDate
	 * @return Calendar
	 */
	public static Calendar parseCalendarSecondFormat(String strDate) {
		return parseCalendarFormat(strDate, format19chars);
	}

	/**
	 * 解析日历的yyyy-MM-dd HH:mm:ss格式
	 * @param strDate
	 * @return Date
	 */
	public static Date parseDateSecondFormat(String strDate) {
		return parseDateFormat(strDate, format19chars);
	}

	/**
	 * 返回日历的yyyy-MM-dd HH:mm格式
	 * @return String
	 */
	public static String getDateMinuteFormat() {
		Calendar cal = Calendar.getInstance();
		return getDateMinuteFormat(cal);
	}

	/**
	 * 返回日历的yyyy-MM-dd HH:mm格式
	 * @param cal
	 * @return String
	 */
	public static String getDateMinuteFormat(Calendar cal) {
		return getDateFormat(cal, format16chars);
	}

	/**
	 * 返回日历的yyyy-MM-dd HH:mm格式
	 * @param date
	 * @return String
	 */
	public static String getDateMinuteFormat(Date date) {
		return getDateFormat(date, format16chars);
	}

	/**
	 * 解析日历的yyyy-MM-dd HH:mm格式
	 * @param strDate
	 * @return Calendar
	 */
	public static Calendar parseCalendarMinuteFormat(String strDate) {
		return parseCalendarFormat(strDate, format16chars);
	}

	/**
	 * 解析日历的yyyy-MM-dd_HH-mm-ss格式
	 * @param strDate
	 * @return Date
	 */
	public static Date parseDateMinuteFormat(String strDate) {
		return parseDateFormat(strDate, format16chars);
	}

	public static String getTimeHHmmssFormat() {
		Calendar cal = Calendar.getInstance();
		return getDateFormat(cal, "HHmmss");
	}

	/**
	 * 返回日历的yyyy-MM-dd格式
	 * @return String
	 */
	public static String getDateDayFormat() {
		Calendar cal = Calendar.getInstance();
		return getDateDayFormat(cal);
	}

	/**
	 * 返回日历的指定格式，参数为空则使用yyyyMMdd格式
	 * @param pattern
	 * @return
	 */
	public static String getDateFormat(String pattern) {
		Calendar cal = Calendar.getInstance();
		if (StringUtil.isBlank(pattern)) {
			pattern = "yyyyMMdd";
		}
		return getDateFormat(cal, pattern);
	}

	/**
	 * 返回日历的yyyy-MM-dd格式
	 * @param cal
	 * @return String
	 */
	public static String getDateDayFormat(Calendar cal) {
		String pattern = "yyyy-MM-dd";
		return getDateFormat(cal, pattern);
	}

	/**
	 * 返回日历的yyyy-MM-dd格式
	 * @param date
	 * @return String
	 */
	public static String getDateDayFormat(Date date) {
		String pattern = "yyyy-MM-dd";
		return getDateFormat(date, pattern);
	}

	/**
	 * 解析yyyy-MM-dd的日历
	 * @param strDate
	 * @return Calendar
	 */
	public static Calendar parseCalendarDayFormat(String strDate) {
		String pattern = "yyyy-MM-dd";
		return parseCalendarFormat(strDate, pattern);
	}

	/**
	 * 解析yyyy-MM-dd的日历
	 * @param strDate
	 * @return Date
	 */
	public static Date parseDateDayFormat(String strDate) {
		String pattern = "yyyy-MM-dd";
		return parseDateFormat(strDate, pattern);
	}

	/**
	 * 返回日历的yyyy-MM-dd_HH-mm-ss格式
	 * @return String
	 */
	public static String getDateFileFormat() {
		Calendar cal = Calendar.getInstance();
		return getDateFileFormat(cal);
	}

	/**
	 * 返回日历的yyyy-MM-dd_HH-mm-ss格式
	 * @param cal
	 * @return String
	 */
	public static String getDateFileFormat(Calendar cal) {
		String pattern = "yyyy-MM-dd_HH-mm-ss";
		return getDateFormat(cal, pattern);
	}

	/**
	 * 返回日历的yyyy-MM-dd_HH-mm-ss格式
	 * @param date
	 * @return String
	 */
	public static String getDateFileFormat(Date date) {
		String pattern = "yyyy-MM-dd_HH-mm-ss";
		return getDateFormat(date, pattern);
	}

	/**
	 * 解析日历的yyyy-MM-dd_HH-mm-ss格式
	 * @param strDate
	 * @return Calendar
	 */
	public static Calendar parseCalendarFileFormat(String strDate) {
		String pattern = "yyyy-MM-dd_HH-mm-ss";
		return parseCalendarFormat(strDate, pattern);
	}

	/**
	 * 解析日历的yyyy-MM-dd_HH-mm-ss格式
	 * @param strDate
	 * @return Date
	 */
	public static Date parseDateFileFormat(String strDate) {
		String pattern = "yyyy-MM-dd_HH-mm-ss";
		return parseDateFormat(strDate, pattern);
	}

	/**
	 * 返回yyyy-MM-dd HH:mm:ss格式字符串
	 * @return String
	 */
	public static String getDateW3CFormat() {
		Calendar cal = Calendar.getInstance();
		return getDateW3CFormat(cal);
	}

	/**
	 * 返回yyyy-MM-dd HH:mm:ss格式字符串
	 * @param cal
	 * @return String
	 */
	public static String getDateW3CFormat(Calendar cal) {
		return getDateFormat(cal, format19chars);
	}

	/**
	 * 返回yyyy-MM-dd HH:mm:ss格式字符串
	 * @param date
	 * @return String
	 */
	public static String getDateW3CFormat(Date date) {
		return getDateFormat(date, format19chars);
	}

	/**
	 * 解析yyyy-MM-dd HH:mm:ss格式字符串
	 * @param strDate
	 * @return Calendar
	 */
	public static Calendar parseCalendarW3CFormat(String strDate) {
		return parseCalendarFormat(strDate, format19chars);
	}

	/**
	 * 解析yyyy-MM-dd HH:mm:ss格式字符串
	 * @param strDate
	 * @return Date
	 */
	public static Date parseDateW3CFormat(String strDate) {
		return parseDateFormat(strDate, format19chars);
	}

	/**
	 * 返回yyyy-MM-dd HH:mm:ss格式的字符串
	 * @param cal
	 * @return String
	 */
	public static String getDateFormat(Calendar cal) {
		return getDateFormat(cal, format19chars);
	}

	/**
	 * 返回yyyy-MM-dd HH:mm:ss格式的字符串
	 * @param date
	 * @return String
	 */
	public static String getDateFormat(Date date) {
		return getDateFormat(date, format19chars);
	}

	/**
	 * 按yyyy-MM-dd HH:mm:ss格式解析字符串日期
	 * @param strDate
	 * @return Calendar
	 */
	public static Calendar parseCalendarFormat(String strDate) {
		return parseCalendarFormat(strDate, format19chars);
	}

	/**
	 * 按yyyy-MM-dd HH:mm:ss格式解析字符串日期
	 * @param strDate
	 * @return Date
	 */
	public static Date parseDateFormat(String strDate) {
		return parseDateFormat(strDate, format19chars);
	}

	/**
	 * 返回日期的指定格式
	 * @param cal
	 * @param pattern
	 * @return String
	 */
	public static String getDateFormat(Calendar cal, String pattern) {
		return getDateFormat(cal.getTime(), pattern);
	}

	/**
	 * 返回日期的指定格式
	 * @param date
	 * @param pattern
	 * @return String
	 */
	public static String getDateFormat(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		String str = null;
		sdf.applyPattern(pattern);
		str = sdf.format(date);
		return str;
	}

	/**
	 * 解析字符串日期的指定格式
	 * @param strDate
	 * @param pattern
	 * @return Calendar
	 */
	public static Calendar parseCalendarFormat(String strDate, String pattern) {
		if (StringUtil.isBlank(strDate)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat();
		Calendar cal = null;
		sdf.applyPattern(pattern);
		try {
			sdf.parse(strDate);
			cal = sdf.getCalendar();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cal;
	}

	/**
	 * 解析字符串日期的指定格式
	 * @param strDate
	 * @param pattern
	 * @return Date
	 */
	public static Date parseDateFormat(String strDate, String pattern) {
		if (StringUtil.isBlank(strDate)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat();
		Date date = null;
		sdf.applyPattern(pattern);
		try {
			date = sdf.parse(strDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 当前日历加／减指定天数后，返回新的日期和时间对象
	 * @param days int
	 * @return Date
	 */
	public static Date getAddDays(int days) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}

	/**
	 * 获取月的最后一天
	 * @param month
	 * @return
	 */
	public static int getLastDayOfMonth(int month) {
		if (month < 1 || month > 12) {
			return -1;
		}
		int retn = 0;
		if (month == 2) {
			if (isLeapYear()) {
				retn = 29;
			} else {
				retn = dayArray[month - 1];
			}
		} else {
			retn = dayArray[month - 1];
		}
		return retn;
	}

	/**
	 * 获取月的最后一天
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getLastDayOfMonth(int year, int month) {
		if (month < 1 || month > 12) {
			return -1;
		}
		int retn = 0;
		if (month == 2) {
			if (isLeapYear(year)) {
				retn = 29;
			} else {
				retn = dayArray[month - 1];
			}
		} else {
			retn = dayArray[month - 1];
		}
		return retn;
	}

	/**
	 * 判断今年是否是闰年
	 * @return
	 */
	public static boolean isLeapYear() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		return isLeapYear(year);
	}

	/**
	 * 判断指定年份是否是闰年
	 * @param year
	 * @return
	 */
	public static boolean isLeapYear(int year) {
		/**
		 * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
		 * 3.能被4整除同时能被100整除则不是闰年
		 */
		if ((year % 400) == 0) {
			return true;
		} else if ((year % 4) == 0) {
			if ((year % 100) == 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * 判断指定日期的年份是否是闰年
	 * 
	 * @param date
	 *            指定日期。
	 * @return 是否闰年
	 */
	public static boolean isLeapYear(Date date) {
		/**
		 * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
		 * 3.能被4整除同时能被100整除则不是闰年
		
		 */
		// int year = date.getYear();
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		int year = gc.get(Calendar.YEAR);
		return isLeapYear(year);
	}

	public static boolean isLeapYear(Calendar gc) {
		/**
		 * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
		 * 3.能被4整除同时能被100整除则不是闰年
		 */
		int year = gc.get(Calendar.YEAR);
		return isLeapYear(year);
	}

	/**
	 * 取得指定日期的前一个月
	 * 
	 * @param date
	 *            指定日期。
	 * @return 指定日期的前一个月
	 */
	public static Date getPreviousMonth(Date date) {
		/**
		 * 详细设计： 1.指定日期的月份减1
		 */
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.add(Calendar.MONTH, -1);
		return gc.getTime();
	}

	public static Calendar getPreviousMonth(Calendar gc) {
		/**
		 * 详细设计： 1.指定日期的月份减1
		 */
		gc.add(Calendar.MONTH, -1);
		return gc;
	}

	/**
	 * 得到指定日期的前一个工作日
	 * 
	 * @param date
	 *            指定日期。
	
	 * @return 指定日期的前一个工作日
	 */
	public static Date getPreviousWeekDay(Date date) {
		{
			/**
			 * 详细设计： 1.如果date是星期日，则减3天 2.如果date是星期六，则减2天 3.否则减1天
			 */
			GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
			gc.setTime(date);
			return getPreviousWeekDay(gc);
			// switch ( gc.get( Calendar.DAY_OF_WEEK ) )
			// {
			// case ( Calendar.MONDAY ):
			// gc.add( Calendar.DATE, -3 );
			// break;
			// case ( Calendar.SUNDAY ):
			// gc.add( Calendar.DATE, -2 );
			// break;
			// default:
			// gc.add( Calendar.DATE, -1 );
			// break;
			// }
			// return gc.getTime();
		}
	}

	public static Date getPreviousWeekDay(Calendar gc) {
		/**
		 * 详细设计： 1.如果date是星期日，则减3天 2.如果date是星期六，则减2天 3.否则减1天
		 */
		switch (gc.get(Calendar.DAY_OF_WEEK)) {
			case (Calendar.MONDAY):
				gc.add(Calendar.DATE, -3);
				break;
			case (Calendar.SUNDAY):
				gc.add(Calendar.DATE, -2);
				break;
			default:
				gc.add(Calendar.DATE, -1);
				break;
		}
		return gc.getTime();
	}

	public static Date getPreviousDay(Date date, int days) {
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.add(Calendar.DATE, -days);
		return gc.getTime();
	}

	public static Calendar getPreviousDay(Calendar gc, int days) {
		gc.add(Calendar.DATE, -days);
		return gc;
	}

	/**
	 * 得到指定日期的下一个工作日
	 * 
	 * @param date
	 *            指定日期。
	 * @return 指定日期的下一个工作日
	 */
	public static Date getNextWeekDay(Date date) {
		/**
		 * 详细设计： 1.如果date是星期五，则加3天 2.如果date是星期六，则加2天 3.否则加1天
		 */
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		switch (gc.get(Calendar.DAY_OF_WEEK)) {
			case (Calendar.FRIDAY):
				gc.add(Calendar.DATE, 3);
				break;
			case (Calendar.SATURDAY):
				gc.add(Calendar.DATE, 2);
				break;
			default:
				gc.add(Calendar.DATE, 1);
				break;
		}
		return gc.getTime();
	}

	public static Calendar getNextWeekDay(Calendar gc) {
		/**
		 * 详细设计： 1.如果date是星期五，则加3天 2.如果date是星期六，则加2天 3.否则加1天
		 */
		switch (gc.get(Calendar.DAY_OF_WEEK)) {
			case (Calendar.FRIDAY):
				gc.add(Calendar.DATE, 3);
				break;
			case (Calendar.SATURDAY):
				gc.add(Calendar.DATE, 2);
				break;
			default:
				gc.add(Calendar.DATE, 1);
				break;
		}
		return gc;
	}

	/**
	 * 取得指定日期的下一个月的最后一天
	 * 
	 * @param date
	 *            指定日期。
	 * @return 指定日期的下一个月的最后一天
	 */
	public static Date getLastDayOfNextMonth(Date date) {
		/**
		 * 详细设计： 1.调用getNextMonth设置当前时间 2.以1为基础，调用getLastDayOfMonth
		 */
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.setTime(DateOper.getNextMonth(gc.getTime()));
		gc.setTime(DateOper.getLastDayOfMonth(gc.getTime()));
		return gc.getTime();
	}

	/**
	 * 取得指定日期的下一个星期的最后一天
	 * 
	 * @param date
	 *            指定日期。
	 * @return 指定日期的下一个星期的最后一天
	 */
	public static Date getLastDayOfNextWeek(Date date) {
		/**
		 * 详细设计： 1.调用getNextWeek设置当前时间 2.以1为基础，调用getLastDayOfWeek
		 */
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.setTime(DateOper.getNextWeek(gc.getTime()));
		gc.setTime(DateOper.getLastDayOfWeek(gc.getTime()));
		return gc.getTime();
	}

	/**
	 * 取得指定日期的下一个月的第一天
	 * 
	 * @param date
	 *            指定日期。
	 * @return 指定日期的下一个月的第一天
	 */
	public static Date getFirstDayOfNextMonth(Date date) {
		/**
		 * 详细设计： 1.调用getNextMonth设置当前时间 2.以1为基础，调用getFirstDayOfMonth
		 */
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.setTime(DateOper.getNextMonth(gc.getTime()));
		gc.setTime(DateOper.getFirstDayOfMonth(gc.getTime()));
		return gc.getTime();
	}

	public static Calendar getFirstDayOfNextMonth(Calendar gc) {
		/**
		 * 详细设计： 1.调用getNextMonth设置当前时间 2.以1为基础，调用getFirstDayOfMonth
		 */
		gc.setTime(DateOper.getNextMonth(gc.getTime()));
		gc.setTime(DateOper.getFirstDayOfMonth(gc.getTime()));
		return gc;
	}

	/**
	 * 取得指定日期的下一个星期的第一天
	 * 
	 * @param date
	 *            指定日期。
	 * @return 指定日期的下一个星期的第一天
	 */
	public static Date getFirstDayOfNextWeek(Date date) {
		/**
		 * 详细设计： 1.调用getNextWeek设置当前时间 2.以1为基础，调用getFirstDayOfWeek
		 */
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.setTime(DateOper.getNextWeek(gc.getTime()));
		gc.setTime(DateOper.getFirstDayOfWeek(gc.getTime()));
		return gc.getTime();
	}

	public static Calendar getFirstDayOfNextWeek(Calendar gc) {
		/**
		 * 详细设计： 1.调用getNextWeek设置当前时间 2.以1为基础，调用getFirstDayOfWeek
		 */
		gc.setTime(DateOper.getNextWeek(gc.getTime()));
		gc.setTime(DateOper.getFirstDayOfWeek(gc.getTime()));
		return gc;
	}

	/**
	 * 取得指定日期的下一个月
	 * 
	 * @param date
	 *            指定日期。
	 * @return 指定日期的下一个月
	 */
	public static Date getNextMonth(Date date) {
		/**
		 * 详细设计： 1.指定日期的月份加1
		 */
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.add(Calendar.MONTH, 1);
		return gc.getTime();
	}

	public static Calendar getNextMonth(Calendar gc) {
		/**
		 * 详细设计： 1.指定日期的月份加1
		 */
		gc.add(Calendar.MONTH, 1);
		return gc;
	}

	/**
	 * 取得指定日期的下一天
	 * 
	 * @param date
	 *            指定日期。
	 * @return 指定日期的下一天
	 */
	public static Date getNextDay(Date date) {
		return getNextDay(date, 1);
	}

	public static Date getNextDay(Date date, int day) {
		/**
		 * 详细设计： 1.指定日期加day天
		 */
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.add(Calendar.DATE, day);
		return gc.getTime();
	}

	public static Calendar getNextDay(Calendar gc) {
		return getNextDay(gc, 1);
	}

	public static Calendar getNextDay(Calendar gc, int day) {
		/**
		 * 详细设计： 1.指定日期加day天
		 */
		gc.add(Calendar.DATE, day);
		return gc;
	}

	/**
	 * 取得指定日期的下一个星期
	 * 
	 * @param date
	 *            指定日期。
	 * @return 指定日期的下一个星期
	 */
	public static Date getNextWeek(Date date) {
		/**
		 * 详细设计： 1.指定日期加7天
		 */
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.add(Calendar.DATE, 7);
		return gc.getTime();
	}

	public static Calendar getNextWeek(Calendar gc) {
		/**
		 * 详细设计： 1.指定日期加7天
		 */
		gc.add(Calendar.DATE, 7);
		return gc;
	}

	/**
	 * 取得指定日期的所处星期的最后一天
	 * 
	 * @param date
	 *            指定日期。
	 * @return 指定日期的所处星期的最后一天
	 */
	public static Date getLastDayOfWeek(Date date) {
		/**
		 * 详细设计： 1.如果date是星期日，则加6天 2.如果date是星期一，则加5天 3.如果date是星期二，则加4天
		 * 4.如果date是星期三，则加3天 5.如果date是星期四，则加2天 6.如果date是星期五，则加1天
		 * 7.如果date是星期六，则加0天
		 */
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		switch (gc.get(Calendar.DAY_OF_WEEK)) {
			case (Calendar.SUNDAY):
				gc.add(Calendar.DATE, 6);
				break;
			case (Calendar.MONDAY):
				gc.add(Calendar.DATE, 5);
				break;
			case (Calendar.TUESDAY):
				gc.add(Calendar.DATE, 4);
				break;
			case (Calendar.WEDNESDAY):
				gc.add(Calendar.DATE, 3);
				break;
			case (Calendar.THURSDAY):
				gc.add(Calendar.DATE, 2);
				break;
			case (Calendar.FRIDAY):
				gc.add(Calendar.DATE, 1);
				break;
			case (Calendar.SATURDAY):
				gc.add(Calendar.DATE, 0);
				break;
		}
		return gc.getTime();
	}

	/**
	 * 取得指定日期的所处星期的第一天
	 * 
	 * @param date
	 *            指定日期。
	 * @return 指定日期的所处星期的第一天
	 */
	public static Date getFirstDayOfWeek(Date date) {
		/**
		 * 详细设计：
		 * 1.如果date是星期日，则减0天
		 * 2.如果date是星期一，则减1天
		 * 3.如果date是星期二，则减2天
		 * 4.如果date是星期三，则减3天
		 * 5.如果date是星期四，则减4天
		 * 6.如果date是星期五，则减5天
		 * 7.如果date是星期六，则减6天
		 */
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		switch (gc.get(Calendar.DAY_OF_WEEK)) {
			case (Calendar.SUNDAY):
				gc.add(Calendar.DATE, 0);
				break;
			case (Calendar.MONDAY):
				gc.add(Calendar.DATE, -1);
				break;
			case (Calendar.TUESDAY):
				gc.add(Calendar.DATE, -2);
				break;
			case (Calendar.WEDNESDAY):
				gc.add(Calendar.DATE, -3);
				break;
			case (Calendar.THURSDAY):
				gc.add(Calendar.DATE, -4);
				break;
			case (Calendar.FRIDAY):
				gc.add(Calendar.DATE, -5);
				break;
			case (Calendar.SATURDAY):
				gc.add(Calendar.DATE, -6);
				break;
		}
		return gc.getTime();
	}

	public static Calendar getFirstDayOfWeek(Calendar gc) {
		/**
		 * 详细设计： 1.如果date是星期日，则减0天 2.如果date是星期一，则减1天 3.如果date是星期二，则减2天
		 * 4.如果date是星期三，则减3天 5.如果date是星期四，则减4天 6.如果date是星期五，则减5天
		 * 7.如果date是星期六，则减6天
		 */
		switch (gc.get(Calendar.DAY_OF_WEEK)) {
			case (Calendar.SUNDAY):
				gc.add(Calendar.DATE, 0);
				break;
			case (Calendar.MONDAY):
				gc.add(Calendar.DATE, -1);
				break;
			case (Calendar.TUESDAY):
				gc.add(Calendar.DATE, -2);
				break;
			case (Calendar.WEDNESDAY):
				gc.add(Calendar.DATE, -3);
				break;
			case (Calendar.THURSDAY):
				gc.add(Calendar.DATE, -4);
				break;
			case (Calendar.FRIDAY):
				gc.add(Calendar.DATE, -5);
				break;
			case (Calendar.SATURDAY):
				gc.add(Calendar.DATE, -6);
				break;
		}
		return gc;
	}

	/**
	 * 将日期转换为星期几
	 * @param dateStr
	 * @return
	 */
	public static String getWeek(String dateStr) {
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyy-MM-dd");
		SimpleDateFormat formatter2 = new SimpleDateFormat("E");
		Date date = null;
		try {
			date = formatter1.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formatter2.format(date);
	}

	/**
	 * 取得指定日期的所处月份的最后一天
	 * 
	 * @param date
	 *            指定日期。
	 * @return 指定日期的所处月份的最后一天
	 */
	public static Date getLastDayOfMonth(Date date) {
		/**
		 * 详细设计： 1.如果date在1月，则为31日 2.如果date在2月，则为28日 3.如果date在3月，则为31日
		 * 4.如果date在4月，则为30日 5.如果date在5月，则为31日 6.如果date在6月，则为30日
		 * 7.如果date在7月，则为31日 8.如果date在8月，则为31日 9.如果date在9月，则为30日
		 * 10.如果date在10月，则为31日 11.如果date在11月，则为30日 12.如果date在12月，则为31日
		 * 1.如果date在闰年的2月，则为29日
		 */
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		switch (gc.get(Calendar.MONTH)) {
			case 0:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
			case 1:
				gc.set(Calendar.DAY_OF_MONTH, 28);
				break;
			case 2:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
			case 3:
				gc.set(Calendar.DAY_OF_MONTH, 30);
				break;
			case 4:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
			case 5:
				gc.set(Calendar.DAY_OF_MONTH, 30);
				break;
			case 6:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
			case 7:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
			case 8:
				gc.set(Calendar.DAY_OF_MONTH, 30);
				break;
			case 9:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
			case 10:
				gc.set(Calendar.DAY_OF_MONTH, 30);
				break;
			case 11:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
		}
		// 检查闰年
		if ((gc.get(Calendar.MONTH) == Calendar.FEBRUARY) && (isLeapYear(gc.get(Calendar.YEAR)))) {
			gc.set(Calendar.DAY_OF_MONTH, 29);
		}
		return gc.getTime();
	}

	public static Calendar getLastDayOfMonth(Calendar gc) {
		/**
		 * 详细设计： 1.如果date在1月，则为31日 2.如果date在2月，则为28日 3.如果date在3月，则为31日
		 * 4.如果date在4月，则为30日 5.如果date在5月，则为31日 6.如果date在6月，则为30日
		 * 7.如果date在7月，则为31日 8.如果date在8月，则为31日 9.如果date在9月，则为30日
		 * 10.如果date在10月，则为31日 11.如果date在11月，则为30日 12.如果date在12月，则为31日
		 * 1.如果date在闰年的2月，则为29日
		 */
		switch (gc.get(Calendar.MONTH)) {
			case 0:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
			case 1:
				gc.set(Calendar.DAY_OF_MONTH, 28);
				break;
			case 2:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
			case 3:
				gc.set(Calendar.DAY_OF_MONTH, 30);
				break;
			case 4:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
			case 5:
				gc.set(Calendar.DAY_OF_MONTH, 30);
				break;
			case 6:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
			case 7:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
			case 8:
				gc.set(Calendar.DAY_OF_MONTH, 30);
				break;
			case 9:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
			case 10:
				gc.set(Calendar.DAY_OF_MONTH, 30);
				break;
			case 11:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
		}
		// 检查闰年
		if ((gc.get(Calendar.MONTH) == Calendar.FEBRUARY) && (isLeapYear(gc.get(Calendar.YEAR)))) {
			gc.set(Calendar.DAY_OF_MONTH, 29);
		}
		return gc;
	}

	/**
	 * 取得指定日期的所处月份的第一天
	 * 
	 * @param date
	 *            指定日期。
	 * @return 指定日期的所处月份的第一天
	 */
	public static Date getFirstDayOfMonth(Date date) {
		/**
		 * 详细设计： 1.设置为1号
		 */
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.set(Calendar.DAY_OF_MONTH, 1);
		return gc.getTime();
	}

	public static Calendar getFirstDayOfMonth(Calendar gc) {
		/**
		 * 详细设计： 1.设置为1号
		 */
		gc.set(Calendar.DAY_OF_MONTH, 1);
		return gc;
	}

	/**
	 * 将日期对象转换成为指定ORA日期、时间格式的字符串形式。如果日期对象为空，返回 一个空字符串对象，而不是一个空对象。
	 * 
	 * @param theDate
	 *            将要转换为字符串的日期对象。
	 * @param hasTime
	 *            如果返回的字符串带时间则为true
	 * @return 转换的结果
	 */
	public static String toOraString(Date theDate, boolean hasTime) {
		/**
		 * 详细设计： 1.如果有时间，则设置格式为getOraDateTimeFormat()的返回值
		 * 2.否则设置格式为getOraDateFormat()的返回值 3.调用toString(Date theDate, DateFormat
		 * theDateFormat)
		 */
		DateFormat theFormat;
		if (hasTime) {
			theFormat = getOraDateTimeFormat();
		} else {
			theFormat = getOraDateFormat();
		}
		return toString(theDate, theFormat);
	}

	/**
	 * 将日期对象转换成为指定日期、时间格式的字符串形式。如果日期对象为空，返回 一个空字符串对象，而不是一个空对象。
	 * 
	 * @param theDate
	 *            将要转换为字符串的日期对象。
	 * @param hasTime
	 *            如果返回的字符串带时间则为true
	 * @return 转换的结果
	 */
	public static String toString(Date theDate, boolean hasTime) {
		/**
		 * 详细设计： 1.如果有时间，则设置格式为getDateTimeFormat的返回值 2.否则设置格式为getDateFormat的返回值
		 * 3.调用toString(Date theDate, DateFormat theDateFormat)
		 */
		DateFormat theFormat;
		if (hasTime) {
			theFormat = getDateTimeFormat();
		} else {
			theFormat = getDateFormat();
		}
		return toString(theDate, theFormat);
	}

	/**
	 * 创建一个标准日期格式的克隆
	 * 
	 * @return 标准日期格式的克隆
	 */
	public static DateFormat getDateFormat() {
		SimpleDateFormat theDateFormat = (SimpleDateFormat) DATE_FORMAT.clone();
		theDateFormat.setLenient(false);
		return theDateFormat;
	}

	/**
	 * 创建一个标准时间格式的克隆
	 * 
	 * @return 标准时间格式的克隆
	 */
	public static DateFormat getDateTimeFormat() {
		SimpleDateFormat theDateTimeFormat = (SimpleDateFormat) DATE_TIME_FORMAT.clone();
		theDateTimeFormat.setLenient(false);
		return theDateTimeFormat;
	}

	/**
	 * 创建一个标准ORA日期格式的克隆
	 * 
	 * @return 标准ORA日期格式的克隆
	 */
	public static DateFormat getOraDateFormat() {
		SimpleDateFormat theDateFormat = (SimpleDateFormat) ORA_DATE_FORMAT.clone();
		theDateFormat.setLenient(false);
		return theDateFormat;
	}

	/**
	 * 创建一个标准ORA时间格式的克隆
	 * 
	 * @return 标准ORA时间格式的克隆
	 */
	public static DateFormat getOraDateTimeFormat() {
		SimpleDateFormat theDateTimeFormat = (SimpleDateFormat) ORA_DATE_TIME_FORMAT.clone();
		theDateTimeFormat.setLenient(false);
		return theDateTimeFormat;
	}

	/**
	 * 将一个日期对象转换成为指定日期、时间格式的字符串。 如果日期对象为空，返回一个空字符串，而不是一个空对象。
	 * 
	 * @param theDate
	 *            要转换的日期对象
	 * @param theDateFormat
	 *            返回的日期字符串的格式
	 * @return 转换结果
	 */
	public static String toString(Date theDate, DateFormat theDateFormat) {
		if (theDate == null) {
			return StringPool.BLANK;
		}
		return theDateFormat.format(theDate);
	}

	/**
	 * 判断date1是否在date2之后
	 * @param strDate1
	 * @param strDate2
	 * @return
	 */
	public static boolean afterByDay(String strDate1, String strDate2) {
		Date date1 = parseDateDayFormat(strDate1);
		Date date2 = parseDateDayFormat(strDate2);
		return afterByDay(date1, date2);
	}

	/**
	 * 判断date1是否在date2之后
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean afterByDay(Date date1, Date date2) {
		long millis1 = date1.getTime() / 86400000;
		long millis2 = date2.getTime() / 86400000;

		if (millis1 > millis2) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断date1是否在date2之前
	 * @param strDate1
	 * @param strDate2
	 * @return
	 */
	public static boolean beforeByDay(String strDate1, String strDate2) {
		Date date1 = parseDateDayFormat(strDate1);
		Date date2 = parseDateDayFormat(strDate2);
		return beforeByDay(date1, date2);
	}

	/**
	 * 判断date1是否在date2之前
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean beforeByDay(Date date1, Date date2) {
		long millis1 = date1.getTime() / 86400000;
		long millis2 = date2.getTime() / 86400000;

		if (millis1 < millis2) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否同一天
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean equalsByDay(Date date1, Date date2) {
		long millis1 = date1.getTime() / 86400000;
		long millis2 = date2.getTime() / 86400000;

		if (millis1 == millis2) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 计算年龄
	 * @param date 出生日期
	 * @return
	 */
	public static int getAge(Date date) {
		return getAge(date, getCalendar());
	}

	/**
	 * 计算年龄
	 * @param date 出生日期
	 * @param timeZone 今天的时区
	 * @return
	 */
	public static int getAge(Date date, TimeZone timeZone) {
		return getAge(date, new GregorianCalendar(timeZone));
	}

	/**
	 * 计算年龄
	 * @param date 出生日期
	 * @param today 今天
	 * @return
	 */
	public static int getAge(Date date, Calendar today) {
		Calendar birthday = getCalendar();

		birthday.setTime(date);

		int yearDiff = today.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);

		if (today.get(Calendar.MONTH) < birthday.get(Calendar.MONTH)) {
			yearDiff--;
		} else if (today.get(Calendar.MONTH) == birthday.get(Calendar.MONTH)
				&& today.get(Calendar.DATE) < birthday.get(Calendar.DATE)) {

			yearDiff--;
		}

		return yearDiff;
	}
	public static final String ISO_8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";

	public static int compareTo(Date date1, Date date2) {
		return compareTo(date1, date2, false);
	}

	public static int compareTo(
			Date date1, Date date2, boolean ignoreMilliseconds) {

		// Workaround for bug in JDK 1.5.x. This bug is fixed in JDK 1.5.07. See
		// http://bugs.sun.com/bugdatabase/view_bug.do;:YfiG?bug_id=6207898 for
		// more information.

		if ((date1 != null) && (date2 == null)) {
			return -1;
		} else if ((date1 == null) && (date2 != null)) {
			return 1;
		} else if ((date1 == null) && (date2 == null)) {
			return 0;
		}

		long time1 = date1.getTime();
		long time2 = date2.getTime();

		if (ignoreMilliseconds) {
			time1 = time1 / Time.SECOND;
			time2 = time2 / Time.SECOND;
		}

		if (time1 == time2) {
			return 0;
		} else if (time1 < time2) {
			return -1;
		} else {
			return 1;
		}
	}

	public static boolean equals(Date date1, Date date2) {
		if (compareTo(date1, date2) == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean equals(
			Date date1, Date date2, boolean ignoreMilliseconds) {

		if (!ignoreMilliseconds) {
			return equals(date1, date2);
		}

		long time1 = 0;

		if (date1 != null) {
			time1 = date1.getTime() / Time.SECOND;
		}

		long time2 = 0;

		if (date2 != null) {
			time2 = date2.getTime() / Time.SECOND;
		}

		if (time1 == time2) {
			return true;
		} else {
			return false;
		}
	}

	public static int getDaysBetween(
			Date startDate, Date endDate, TimeZone timeZone) {

		int offset = timeZone.getRawOffset();

		Calendar startCal = new GregorianCalendar(timeZone);

		startCal.setTime(startDate);
		startCal.add(Calendar.MILLISECOND, offset);

		Calendar endCal = new GregorianCalendar(timeZone);

		endCal.setTime(endDate);
		endCal.add(Calendar.MILLISECOND, offset);

		int daysBetween = 0;

		while (beforeByDay(startCal.getTime(), endCal.getTime())) {
			startCal.add(Calendar.DAY_OF_MONTH, 1);

			daysBetween++;
		}

		return daysBetween;
	}

	public static Date newDate() {
		return new Date();
	}

	public static Date newDate(long date) {
		return new Date(date);
	}

	/**
	 * Returns a Date set to the first possible millisecond of the day, just
	 * after midnight. If a null day is passed in, a new Date is created.
	 * midnight (00m 00h 00s)
	 */
	public static Date getStartOfDay(Date day) {
		return getStartOfDay(day, Calendar.getInstance());
	}

	/**
	 * Returns a Date set to the first possible millisecond of the day, just
	 * after midnight. If a null day is passed in, a new Date is created.
	 * midnight (00m 00h 00s)
	 */
	public static Date getStartOfDay(Date day, Calendar cal) {
		if (day == null) {
			day = new Date();
		}
		cal.setTime(day);
		cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
		return cal.getTime();
	}

	/**
	 * Returns a Date set to the last possible millisecond of the day, just
	 * before midnight. If a null day is passed in, a new Date is created.
	 * midnight (00m 00h 00s)
	 */
	public static Date getEndOfDay(Date day) {
		return getEndOfDay(day, Calendar.getInstance());
	}

	public static Date getEndOfDay(Date day, Calendar cal) {
		if (day == null) {
			day = new Date();
		}
		cal.setTime(day);
		cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
		return cal.getTime();
	}

	/**
	 * Returns a Date set to the first possible millisecond of the hour.
	 * If a null day is passed in, a new Date is created.
	 */
	public static Date getStartOfHour(Date day) {
		return getStartOfHour(day, Calendar.getInstance());
	}

	/**
	 * Returns a Date set to the first possible millisecond of the hour.
	 * If a null day is passed in, a new Date is created.
	 */
	public static Date getStartOfHour(Date day, Calendar cal) {
		if (day == null) {
			day = new Date();
		}
		cal.setTime(day);
		cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
		return cal.getTime();
	}

	/**
	 * Returns a Date set to the last possible millisecond of the day, just
	 * before midnight. If a null day is passed in, a new Date is created.
	 * midnight (00m 00h 00s)
	 */
	public static Date getEndOfHour(Date day) {
		return getEndOfHour(day, Calendar.getInstance());
	}

	public static Date getEndOfHour(Date day, Calendar cal) {
		if (day == null || cal == null) {
			return day;
		}

		cal.setTime(day);
		cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
		return cal.getTime();
	}

	/**
	 * Returns a Date set to the first possible millisecond of the minute.
	 * If a null day is passed in, a new Date is created.
	 */
	public static Date getStartOfMinute(Date day) {
		return getStartOfMinute(day, Calendar.getInstance());
	}

	/**
	 * Returns a Date set to the first possible millisecond of the minute.
	 * If a null day is passed in, a new Date is created.
	 */
	public static Date getStartOfMinute(Date day, Calendar cal) {
		if (day == null) {
			day = new Date();
		}
		cal.setTime(day);
		cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
		return cal.getTime();
	}

	/**
	 * Returns a Date set to the last possible millisecond of the minute.
	 * If a null day is passed in, a new Date is created.
	 */
	public static Date getEndOfMinute(Date day) {
		return getEndOfMinute(day, Calendar.getInstance());
	}

	public static Date getEndOfMinute(Date day, Calendar cal) {
		if (day == null || cal == null) {
			return day;
		}

		cal.setTime(day);
		cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
		return cal.getTime();
	}

	/**
	 * Returns a Date set to the first possible millisecond of the month, just
	 * after midnight. If a null day is passed in, a new Date is created.
	 * midnight (00m 00h 00s)
	 */
	public static Date getStartOfMonth(Date day) {
		return getStartOfMonth(day, Calendar.getInstance());
	}

	public static Date getStartOfMonth(Date day, Calendar cal) {
		if (day == null) {
			day = new Date();
		}
		cal.setTime(day);

		// set time to start of day
		cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));

		// set time to first day of month
		cal.set(Calendar.DAY_OF_MONTH, 1);

		return cal.getTime();
	}

	/**
	 * Returns a Date set to the last possible millisecond of the month, just
	 * before midnight. If a null day is passed in, a new Date is created.
	 * midnight (00m 00h 00s)
	 */
	public static Date getEndOfMonth(Date day) {
		return getEndOfMonth(day, Calendar.getInstance());
	}

	public static Date getEndOfMonth(Date day, Calendar cal) {
		if (day == null) {
			day = new Date();
		}
		cal.setTime(day);

		// set time to end of day
		cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));

		// set time to first day of month
		cal.set(Calendar.DAY_OF_MONTH, 1);

		// add one month
		cal.add(Calendar.MONTH, 1);

		// back up one day
		cal.add(Calendar.DAY_OF_MONTH, -1);

		return cal.getTime();
	}

	/**
	 * Returns a Date set just to Noon, to the closest possible millisecond
	 * of the day. If a null day is passed in, a new Date is created.
	 * nnoon (00m 12h 00s)
	 */
	public static Date getNoonOfDay(Date day, Calendar cal) {
		if (day == null) {
			day = new Date();
		}
		cal.setTime(day);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
		return cal.getTime();
	}
	/**
	 * 日期比较(日期格式yyyyMMdd或yyyy-MM-dd)
	 * @param param0 日期一
	 * @param param1 日期一的时段
	 * @param param2 日期二
	 * @param param3 日期二的时段
	 * @return 日期一大于日期二返回1,日期一小于日期二返回-1,相等 返回0  
	 * @throws Exception
	 */
	public static int  compareDate(String param0,String param1,String param2,String param3) throws Exception
	{
		/*
		 * 
		 * 日期一大于日期二  返回 1 
		 * 日期一小于日期二 返回 -1 
		 * 相等 返回0  
		 */
		/*
		String param0 = (String)param.getParaObjectAt(0);//日期一
		String param1 = (String)param.getParaObjectAt(1);//日期一的时段
		String param2 = (String)param.getParaObjectAt(2);//日期二
		String param3 = (String)param.getParaObjectAt(3);//日期二的时段
		*/
		String s="";
		if(param0.length()==8&&param2.length()==8)
		{
			s="yyyyMMdd";
		}
		else
		{
			if(param0.length()==10&&param2.length()==10)
			{
				s="yyyy-MM-dd";
			}
			else
			{
				throw new Exception("日期格式错误，支持格式yyyyMMdd");
				//KasiteConfig.print("日期格式错误，支持格式yyyyMMdd");
				//return -2;
			}
		}
		
	   	Date dateOne=new Date(); 
		  Date dateTwo=new Date();
		 SimpleDateFormat simpledateformat=null; 
		 simpledateformat= new SimpleDateFormat(s);
		
		 dateOne = simpledateformat.parse(param0);//日期一
		 dateTwo = simpledateformat.parse(param2);//日期二
		if(dateOne.compareTo(dateTwo)==0)
		{
			
			 if(Integer.parseInt(param1)>Integer.parseInt(param3))
			 {
				 //KasiteConfig.print("日期1>日期2 (日期一时段大于日期二的时段)");
				 return 1;
			 }
			 else
			 {
				 if(Integer.parseInt(param1)<Integer.parseInt(param3))
				 {
					 //KasiteConfig.print("日期1<日期2 (日期一时段小于日期二的时段)");
					 return -1;
				 }
				 else
				 {
					 //KasiteConfig.print("日期1=日期2");
					 return 0;
				 }
				 
			 }
			
			
		}
		else
		{
		   if(dateOne.compareTo(dateTwo)>0)
		   {
			   //KasiteConfig.print("日期1>日期2");
			   return 1;
		   }
		   else
		   {
			   //KasiteConfig.print("日期1<日期2");
			   return -1;
		   }
		 
		}
			
	}
	public static int getDateDiff(Date s1, Date s2,String s3) {
	        if(s3.equalsIgnoreCase("s"))//秒
	        {
	          
	            long l1 = s1.getTime() - s2.getTime();
	            return (int)(l1 / 1000L);
	            
	        }
	        if(s3.equalsIgnoreCase("mi"))//分钟
	        {
	          
	            long l2 = s1.getTime() - s2.getTime();
	            return  (int)(l2 / 60000L);
	            
	        }
	        if(s3.equalsIgnoreCase("h"))//小时
	        {
	           
	            long l3 = s1.getTime() - s2.getTime();
	            return  (int)(l3 / 0x36ee80L);
	           
	        }
	        if(s3.equalsIgnoreCase("d"))//日
	        {
	            
	            long l4 = s1.getTime() - s2.getTime();
	            return  (int)(l4 / 0x5265c00L);
	            
	        }
	        if(s3.equalsIgnoreCase("m"))//月
	        {
	        	// Calendar   cal  =   Calendar.getInstance();   
	 			 //cal.setTime(s1);
	 			 
	 			 //int month1=cal.get(Calendar.MONTH);
	 			 //cal.setTime(s2);
	 			//int month2=cal.get(Calendar.MONTH);
	  		
	  		   //KasiteConfig.print("month1="+month1);
	  		 //KasiteConfig.print("month2="+month2);
	           // return (int) (month1-month2) + 12 * (month1 -month2);
	        	 int iMonth = 0;      
	             int flag = 0;      
	             try{      
	                 Calendar objCalendarDate1 = Calendar.getInstance();      
	                 objCalendarDate1.setTime(s1);      
	           
	                 Calendar objCalendarDate2 = Calendar.getInstance();      
	                 objCalendarDate2.setTime(s2);      
	           
	                 if (objCalendarDate2.equals(objCalendarDate1))      
	                     return 0;      
	                 if (objCalendarDate1.after(objCalendarDate2)){      
	                     Calendar temp = objCalendarDate1;      
	                     objCalendarDate1 = objCalendarDate2;      
	                     objCalendarDate2 = temp;      
	                 }      
	                 if (objCalendarDate2.get(Calendar.DAY_OF_MONTH) < objCalendarDate1.get(Calendar.DAY_OF_MONTH))      
	                     flag = 1;      
	           
	                 if (objCalendarDate2.get(Calendar.YEAR) > objCalendarDate1.get(Calendar.YEAR))      
	                     iMonth = ((objCalendarDate2.get(Calendar.YEAR) - objCalendarDate1.get(Calendar.YEAR))      
	                             * 12 + objCalendarDate2.get(Calendar.MONTH) - flag)      
	                             - objCalendarDate1.get(Calendar.MONTH);      
	                 else     
	                     iMonth = objCalendarDate2.get(Calendar.MONTH)      
	                             - objCalendarDate1.get(Calendar.MONTH) - flag;      
	           
	             } catch (Exception e){      
	              e.printStackTrace();      
	             }      
	             return iMonth;      

	        }
	        if(s3.equalsIgnoreCase("y"))//年
	        {
	        	 Calendar   cal  =   Calendar.getInstance();   
	 			 cal.setTime(s1);
	 			 int year1=cal.get(Calendar.YEAR);
	 			 cal.setTime(s2);
	 			int year2=cal.get(Calendar.YEAR);
	 			return (int) (year1-year2);
	        }
	        return -14444;
    }

	
	public  static String getWeekType(String theDate) throws Exception
	{
		String s = "yyyyMMdd";
		if (theDate.length() == 10) {
			s = "yyyy-MM-dd";
		} else {
			if (theDate.length() == 8) {
				s = "yyyyMMdd";
			}
			else
			{
				if (theDate.length() == 14) {
					s = "yyyyMMddHHmmss";
				}
				else
				{
				KasiteConfig.print("日期格式错误");
				return "";
				}
			}
		}

		   Date now= new Date();
		   Calendar   cal  =   Calendar.getInstance();   
			
		   cal.setTime(now);
		   int day=cal.get(Calendar.DAY_OF_WEEK);//当天是周几
		   if(day==1)
		   {
			  cal.add(Calendar.DATE, -6);
		   }
		   else
		   {
			   cal.add(Calendar.DATE, -day+1);
		   }
		   Date mondayDate=cal.getTime();//本周周一的日期
		   //KasiteConfig.print(now+"/"+day+"/"+mondayDate);
        SimpleDateFormat simpledateformat3 = new SimpleDateFormat(s);
        
        
        Date date= simpledateformat3.parse(theDate);//传入的日期
       
		 // KasiteConfig.print();
		   
        long l= date.getTime() - mondayDate.getTime();
        int i = (int)(l/ 0x5265c00L);
        if(Math.abs(i)>=14)
        {
        	//BNXmlUtil.setNodeValue(param0,"");
        }
        else
        {
           if(i<0)
           {
              return  "";
           }
           else
           {
        	   if(i<7)
        	   {
        		  return  "本周";
        	   }
        	   else
        	   {
        		  return "下周";
        	   }
           }
       }
        return "";
	}
	public static int  getYearIndex(Date date)
	{
		Calendar   c   =   Calendar.getInstance();   
		
    	c.setTime(date);
    	
    	return c.get(Calendar.YEAR);
	}
	public static int  getMonthIndex(Date date)
	{
		Calendar   c   =   Calendar.getInstance();   
		
    	c.setTime(date);
    	
    	return c.get(Calendar.MONTH);
	}
	public static int  getDayIndex(Date date)
	{
		Calendar   c   =   Calendar.getInstance();   
		
    	c.setTime(date);
    	
    	return c.get(Calendar.DATE);
	}
	public static int  getHour(Date date)
	{
		Calendar   c   =   Calendar.getInstance();   
		
    	c.setTime(date);
    	
    	return c.get(Calendar.HOUR_OF_DAY);
	}
	public static int  getMinute(Date date)
	{
		Calendar   c   =   Calendar.getInstance();   
		
    	c.setTime(date);
    	
    	return c.get(Calendar.MINUTE);
	}
	public static int  getSecond(Date date)
	{
		Calendar   c   =   Calendar.getInstance();   
		
    	c.setTime(date);
    	
    	return c.get(Calendar.SECOND);
	}
	/**指定的日期是第几周
	 * @param theDate
	 * @return
	 */
	public static int getWeekOfMonth(String theDate)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	     
		 int weekID=-1;
	    // Date date1 = simpledateformat.parse(param1);
	    try
	    {
	    	Calendar   c   =   Calendar.getInstance();   
	
	    	c.setTime(sdf.parse(theDate));
		
	    	weekID=c.get(Calendar.WEEK_OF_MONTH);
	    }
	    catch(Exception e)
	    {
	        	KasiteConfig.print("获取指定日期是第几周出错"+e.getMessage());
	    }
	    return weekID;
	}
	/**指定的日期是星期几
	 * @param theDate
	 * @return
	 */
	/*
	public  static int getDayOfWeek(String theDate)
	{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		 int dayID=-1;
	   
	    try
	    {
	    	//Calendar   c   =   Calendar.getInstance();   
	    	GregorianCalendar gregoriancalendar = new GregorianCalendar();
	    	gregoriancalendar.setTime(sdf.parse(theDate));
		
	    	dayID=gregoriancalendar.get(7)-1;
	    }
	    catch(Exception e)
	    {
	        	KasiteConfig.print("获取指定日期是星期几出错"+e.getMessage());
	    }
	    return dayID;
	}
	*/
	public  static int getDayOfWeek(String theDate,String format)
	{
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		 int dayID=-1;
	   
	    try
	    {
	    	//Calendar   c   =   Calendar.getInstance();   
	    	GregorianCalendar gregoriancalendar = new GregorianCalendar();
	    	gregoriancalendar.setTime(sdf.parse(theDate));
		
	    	dayID=gregoriancalendar.get(7)-1;
	    }
	    catch(Exception e)
	    {
	        	KasiteConfig.print("获取指定日期是星期几出错"+e.getMessage());
	    }
	    return dayID;
	}
	public  static int getDayOfWeek(Date theDate)
	{
		
		
		 int dayID=-1;
	   
	    try
	    {
	    	//Calendar   c   =   Calendar.getInstance();   
	    	GregorianCalendar gregoriancalendar = new GregorianCalendar();
	    	gregoriancalendar.setTime(theDate);
		
	    	dayID=gregoriancalendar.get(7)-1;
	    }
	    catch(Exception e)
	    {
	        	KasiteConfig.print("获取指定日期是星期几出错"+e.getMessage());
	    }
	    return dayID;
	}
	public static java.sql.Date getNowDate()
	{
		return new java.sql.Date(new Date().getTime());
	}
	public static java.sql.Timestamp getNowDateTime()
	{
		
		return new java.sql.Timestamp(new Date().getTime());
	}
	
	/**
	 * 获取当前时间+分钟数。
	 * 
	 * @param minutes 可为负数，为负数则当前时间-分钟数
	 * @return
	 */
	public static java.sql.Timestamp getNowDateTimeAndAddMinute(int minutes)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(DateOper.getNowDateTime());
		c.add(Calendar.MINUTE, minutes);
		return new Timestamp(c.getTime().getTime());
	}
	
	public static String getNow(String strFormat) throws ParseException
	{
		 	Date date = new Date();
	        SimpleDateFormat format = new SimpleDateFormat(strFormat);
	        return format.format(date);
	}
	/***
	 * 转换格式
	 * 	"yyyyMMdd";
	 *	"yyyy-MM-dd";
	 *	"yyyyMMddHHmmss";
	 *	"yyyy-MM-dd HH:mm:ss";
	 * @param theDate
	 * @return
	 * @throws ParseException
	 */
	public static Date  parse(String theDate) throws ParseException
	{
		 	if(theDate==null)
		 	{
		 		return null;
		 	}
		 	int len=theDate.length();
		 	String format="";
		 	switch(len)
		 	{
		 	case 8:format="yyyyMMdd";break;
		 	case 10:format="yyyy-MM-dd";break;
		 	case 14:format="yyyyMMddHHmmss";break;
		 	case 19:format="yyyy-MM-dd HH:mm:ss";break;
		 	default:break;
		 	}
		 	SimpleDateFormat sdf=new SimpleDateFormat(format);
		 	return sdf.parse(theDate);
	}
	public static String formatDate(String theDate ,String sourceFormat,String targetFormat) throws ParseException
	{
		
		 SimpleDateFormat sdf = new SimpleDateFormat(sourceFormat);
		 Calendar   c   =   Calendar.getInstance();   
	     c.setTime(sdf.parse(theDate));
	     sdf = new SimpleDateFormat(targetFormat);
	     return sdf.format(c.getTime());

	}
	public static String formatDate(Date date,String strFormat) throws ParseException
	{
		 
	        SimpleDateFormat format = new SimpleDateFormat(strFormat);
	        return format.format(date);
	}
	public static String addMonth(String theDate ,int monthcount,String format) throws ParseException
	{
		 
		 SimpleDateFormat sdf = new SimpleDateFormat(format);
	     
		 Calendar   c   =   Calendar.getInstance();   
			
	     c.setTime(sdf.parse(theDate));
	     c.add(Calendar.MONTH, monthcount);
	     return sdf.format(c.getTime());

	}
	public static Date addMonth(Date theDate ,int monthcount) throws ParseException
	{
		 
		// SimpleDateFormat sdf = new SimpleDateFormat(format);
	     
		 Calendar   c   =   Calendar.getInstance();   
			
	     c.setTime(theDate);
	     c.add(Calendar.MONTH, monthcount);
	     return c.getTime();

	}
	public static String addSecond(String theDate ,int count,String format) throws ParseException
	{
		 
		 SimpleDateFormat sdf = new SimpleDateFormat(format);
	     
		 Calendar   c   =   Calendar.getInstance();   
			
	     c.setTime(sdf.parse(theDate));
	     c.add(Calendar.SECOND, count);
	     return sdf.format(c.getTime());

	}
	public static void main(String[] args) throws Exception {
//		String c = DateOper.addMinute("2013-11-12 17:30", 20,"yyyy-MM-dd HH:mm");
//		KasiteConfig.print(c);
//		String commend = c.substring(11, c.length());
////		String commendtime = DateOper.formatDate(DateOper.parse(c), "hh:mm");
//		KasiteConfig.print(commend);
		
		String amstarttime = "2013-11-12 07:00:00";
		String amendtime = "2013-11-12 12:30:00";
		int i = DateOper.getDateDiff(DateOper.parse(amendtime), DateOper.parse(amstarttime), "mi");
		
	}
	public static String addMinute(String theDate ,int count,String format) throws ParseException
	{
		 
		 SimpleDateFormat sdf = new SimpleDateFormat(format);
		 Calendar   c   =   Calendar.getInstance();   
	     c.setTime(sdf.parse(theDate));
	     c.add(Calendar.MINUTE, count);
	     return sdf.format(c.getTime());

	}
	public static String addDate(String theDate ,int days,String format) throws ParseException
	{
		
		 SimpleDateFormat sdf = new SimpleDateFormat(format);
	     
		 Calendar   c   =   Calendar.getInstance();   
			
	     c.setTime(sdf.parse(theDate));
	     c.add(Calendar.DATE, days);
	     return sdf.format(c.getTime());

	}
	public static String addDate(String theDate ,int days) throws ParseException
	{
		 String format="yyyy-MM-dd";
		 theDate=theDate.trim();
		 if(theDate.length()==19)
		 {
			 format="yyyy-MM-dd HH:mm:ss";
		 }
		 else
		 {
			 if(theDate.length()==14)
			 {
				 format="yyyyMMddHHmmss";
			 }
			 else
			 {
				 if(theDate.length()==8)
				 {
					 format="yyyyMMdd";
				 }
			 }
		 }
		 SimpleDateFormat sdf = new SimpleDateFormat(format);
	     
		 Calendar   c   =   Calendar.getInstance();   
			
	     c.setTime(sdf.parse(theDate));
	     c.add(Calendar.DATE, days);
	     return sdf.format(c.getTime());

	}
	/**
	 * 根据入参判断当前日期相差几天
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Integer diffDay(String startDate, String endDate,String format) {
		Integer day = 0;
		SimpleDateFormat sdi = new SimpleDateFormat(format);
		try {
			Date start = sdi.parse(startDate);
			Date end = sdi.parse(endDate);
			day = (int) ((end.getTime() - start.getTime()) / (3600 * 24 * 1000));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return day;
	}

	/** 定义一个全局变量用来做中转的数据存储*/
	public static String date = "";

	/**
	 * 根据开始时间跟结束时间，返回一个String类型的时间数组
	 * @param startDate
	 * @param endDate
	 * @return
	 */


	public static String[] getDayArr(String startDate,String endDate){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Integer day = DateOper.diffDay(startDate,endDate,"yyyy-MM-dd");
		//定义时间数组
		String [] dayStr = new String[day+1];
		String firstDay = startDate;
		//将初始时间存入数组
		dayStr[0] = firstDay;
		try {
			//将初始化赋值给全局变量date
			date = startDate ;
			for (int i = 0; i < day; i++) {
				Date parse = simpleDateFormat.parse(date);
				//将指定日期的下一天的时间格式化后存入数组
				dayStr[i+1] = simpleDateFormat.format(DateOper.getNextDay(parse));
				//初始化全局变量date
				date = dayStr[i+1];
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dayStr;
	}

}

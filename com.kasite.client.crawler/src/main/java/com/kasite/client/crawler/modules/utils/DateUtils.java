/** 
 *
 * @File name:  FormatDateUtil.java
 * @Description: 日期处理工具   
 * @Create on:  2008-1-1
 * @Author   :  张文星 (wxing)
 * @version 1.0
 * 
 * @ChangeList
 * ---------------------------------------------------
 * Date         Editor              ChangeReasons
 * 2009-11-05   张文星               整合工具类,为理解的直观,更改文件名为DateUtils.java,并且增加了部分传参类型转换的方法 
 * 
 *
 */
package com.kasite.client.crawler.modules.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DateUtils {
	public static Date date = null;
	public static DateFormat dateFormat = null;
	public static SimpleDateFormat format = null;
	public static Calendar calendar = null;

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
		format = new SimpleDateFormat(returntype);
		return format.format(date);
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
		format = new SimpleDateFormat(returntype);
		try {
			date = format.parse(dateString);
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			date = new Date();
		}
		return date;
	}

	/**
	 * 格式化当前系统时间类型为 eg:1009
	 * 
	 * @return
	 * 
	 */
	public static String getFourGroupNum() {
		// format=new SimpleDateFormat("yyyyMMdd");
		String fourNum = formatDateToString(new Date(), "yyyyMMdd").substring(2, 6);
		return fourNum;
	}

	/**
	 * 获得格式为 长时间格式(年-月日 时：分：秒) 系统时间 返回为String型
	 * 
	 * @return yyyy-MM-dd HH:mm:ss
	 * @deprecated
	 */
	public static String getLongDate() {
		// format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowDate = formatDateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
		return nowDate;
	}

	/**
	 * 获得格式为短时间格式( 年-月-日)的系统时间 返回为String型
	 * 
	 * @return yyyy-MM-dd
	 * @deprecated
	 */
	public static String getShortDate() {
		// format = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = formatDateToString(new Date(), "yyyy-MM-dd");
		return nowDate;
	}

	/**
	 * 获得格式为短时间类型( 年-月)的系统时间 返回为String型
	 * 
	 * @return yyyy-MM
	 */
	public static String getYearM() {
		// format = new SimpleDateFormat("yyyyMM");
		String nowDate = formatDateToString(new Date(), "yyyyMM");
		return nowDate;
	}

	/**
	 * 获得格式为短时间类型(HH:mm:ss) 的当前系统时间 返回为String型
	 * 
	 * @return HH:mm:ss
	 */
	public static String getHour() {
		// format = new SimpleDateFormat("HH:mm:ss");
		// format.format(new
		String nowDate = formatDateToString(new Date(), "HH:mm:ss");
		return nowDate;
	}

	/**
	 * 获得毫秒格式的当前系统时间 返回long类型
	 * 
	 * @return eg 123456785
	 */
	public static long getMimtime() {
		return System.currentTimeMillis();
	}

	/**
	 * 将时间类型参数格式化为字符串类型
	 * 
	 * @param date
	 *            需要被格式话的时间对象
	 * @return yyyy-MM-dd
	 */
	public static String formatShortDateString(Date date) {
		// format = new SimpleDateFormat("yyyy-MM-dd");
		return formatDateToString(date, "yyyy-MM-dd");
	}

	/**
	 * 将时间类型参数格式化为字符串类型
	 * 
	 * @param date
	 *            需要被格式话的时间对象
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String formatLongDateString(Date date) {
		// format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatDateToString(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 将字符串yyyy-MM-dd HH:mm:ss）转化为日期对象
	 * 
	 * @param dateString
	 *            字符串类型的日期
	 * @return 返回日期对象
	 */
	public static final Date getDate(String dateString) {
		/*
		 * format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 * 
		 * try { date = format.parse(dateString); } catch (Exception e) {
		 * System.out.println(e.getMessage()); date = new Date(); }
		 */
		return formatStringtoDate(dateString, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到上一年的日期字符串
	 * 
	 * @param date
	 *            日期对象
	 * @return 上一年的日期字符串
	 */
	public static final String getLastYearString(Date date) {
		Calendar lastYear = Calendar.getInstance();
		lastYear.setTime(date);
		lastYear.add(Calendar.YEAR, -1);
		return DateUtils.formatShortDateString(lastYear.getTime());
	}

	/**
	 * 得到上一月的日期字符串
	 * 
	 * @param date
	 *            日期对象
	 * @return 上一月的日期字符串
	 */
	public static final String getLastMonthString(Date date) {
		Calendar lastMonth = Calendar.getInstance();
		lastMonth.setTime(date);
		lastMonth.add(Calendar.MONTH, -1);
		return DateUtils.formatShortDateString(lastMonth.getTime());
	}

	/**
	 * 得到上一周的日期字符串
	 * 
	 * @param date
	 *            日期对象
	 * @return 上一月周日期字符串
	 */
	public static final String getLastWeekString(Date date) {
		Calendar lastWeek = Calendar.getInstance();
		lastWeek.setTime(date);
		lastWeek.add(Calendar.HOUR_OF_DAY, -168);
		return DateUtils.formatShortDateString(lastWeek.getTime());
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
		return DateUtils.formatShortDateString(yesterday.getTime());
	}

	/**
	 * 取得当时的时间戳。年（四位）_月（两位）_日（两位）_时（两位）_分（两位）_秒（两位）_毫秒（三位）
	 * 
	 * @return 当时的时间戳
	 */
	public static final String getNowTimeStamp() {
		Date date = new Date();
		SimpleDateFormat formattxt = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS");
		return formattxt.format(date);
	}

	/**
	 * 返回星期 数字，0周日，1周1，...6周六
	 */

	public static int getWeek() {
		int newWeek = 0;
		String theWeek = "";
		Date date1 = new Date();
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateStringToParse = bartDateFormat.format(date1);
		// String dateStringToParse = thedate;
		try {
			Date date = bartDateFormat.parse(dateStringToParse);
			SimpleDateFormat bartDateFormat2 = new SimpleDateFormat("EEEE");
			theWeek = bartDateFormat2.format(date);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if ("星期日".equals(theWeek)){
			newWeek = 0;
		}else if ("星期一".equals(theWeek)){
			newWeek = 1;
		}else if ("星期二".equals(theWeek)){
			newWeek = 2;
		}else if ("星期三".equals(theWeek)){
			newWeek = 3;
		}else if ("星期四".equals(theWeek)){
			newWeek = 4;
		}else if ("星期五".equals(theWeek)){
			newWeek = 5;
		}else if ("星期六".equals(theWeek)){
			newWeek = 6;
		}

		return newWeek;
	}

	/**
	 * 返回星期中文
	 */

	public static String getWeekCH() {
		int newWeek = 0;
		String theWeek = "";
		Date date1 = new Date();
		format = new SimpleDateFormat("yyyy-MM-dd");
		String dateStringToParse = format.format(date1);
		// String dateStringToParse = thedate;
		try {
			Date date = format.parse(dateStringToParse);
			SimpleDateFormat bartDateFormat2 = new SimpleDateFormat("EEEE");
			theWeek = bartDateFormat2.format(date);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return theWeek;
	}

	/**
	 * 功能描述：返回年份
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回年份
	 */
	public static int getYear(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 功能描述：返回月份
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回月份
	 */
	public static int getMonth(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 功能描述：返回日份
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回日份
	 */
	public static int getDay(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 功能描述：返回小时
	 * 
	 * @param date
	 *            日期
	 * @return 返回小时
	 */
	public static int getHour(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 功能描述：返回分钟
	 * 
	 * @param date
	 *            日期
	 * @return 返回分钟
	 */
	public static int getMinute(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * 返回秒钟
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回秒钟
	 */
	public static int getSecond(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.SECOND);
	}

	/**
	 * 功能描述：返回毫秒
	 * 
	 * @param date
	 *            日期
	 * @return 返回毫秒
	 */
	public static long getMillis(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis();
	}

	/**
	 * 功能描述：日期相加
	 * 
	 * @param date
	 *            Date 日期
	 * @param day
	 *            int 天数
	 * @return 返回相加后的日期
	 */
	public static Date addDate(Date date, int day) {
		calendar = Calendar.getInstance();
		long millis = getMillis(date) + ((long) day) * 24 * 3600 * 1000;
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	/**
	 * 功能描述：日期相减
	 * 
	 * @param date
	 *            Date 日期
	 * @param date1
	 *            Date 日期
	 * @return 返回相减后的日期
	 */
	public static int diffDate(Date date, Date date1) {
		return (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
	}

	public static final String getTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(Calendar.getInstance().getTime());
	}
	
	public static final String getTime(String returnType){
		SimpleDateFormat sdf = new SimpleDateFormat(returnType);
		return sdf.format(Calendar.getInstance().getTime());
	}
	
	public static long getTimeLong(Date date) {
		format = new SimpleDateFormat("yyyyMMddHHmmss");
		return Long.parseLong(format.format(date));
	}
	
	public static String formatNumberToDate(String date) {
		String year = date.substring(0,4);
		String month = date.substring(4,6);
		String day = date.substring(6,8);
		String hour = date.substring(8,10);
		String min = date.substring(10,12);
		String sec = date.substring(12,14);
		return year+"-"+month+"-"+day+" "+hour+":"+min+":"+sec;
	}
	
	/** 
     * 计算两个日期之间相差的天数 
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
	 * @throws ParseException 
     */  
    public static int daysBetween(Date smdate,Date bdate) throws ParseException  
    {  
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    	smdate=sdf.parse(sdf.format(smdate));
    	bdate=sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();  
        cal.setTime(smdate);  
        long time1 = cal.getTimeInMillis();               
        cal.setTime(bdate);  
        long time2 = cal.getTimeInMillis();       
        long betweenDays=(time2-time1)/(1000*3600*24);
          
       return Integer.parseInt(String.valueOf(betweenDays));         
    }  
    /**
      * @Title: daysParseFormat
      * @Description: 把 yyyy-MM-dd HH:mm:ss转换成 yyyy-MM-dd
 
      */
    public static String daysParseFormat(String date)throws Exception{
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    	return sdf.format(sdf.parse(date));
    	
    }
    /** 
     * @Title: JudgeFormat 
     * @Description: 判断是否是时间类型:yyyy-MM-dd
     */ 
    public static boolean judgeFormat(String date){
    String rexp = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
    Pattern pat = Pattern.compile(rexp);    
    Matcher mat = pat.matcher(date);     
    boolean dateType = mat.matches();  
    return dateType;  
    }
    
	/**
	*字符串的日期格式的计算
	*/
    public static int daysBetween(String smdate,String bdate) throws ParseException{
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();  
        cal.setTime(sdf.parse(smdate));  
        long time1 = cal.getTimeInMillis();               
        cal.setTime(sdf.parse(bdate));  
        long time2 = cal.getTimeInMillis();       
        long betweenDays=(time2-time1)/(1000*3600*24);
          
       return Integer.parseInt(String.valueOf(betweenDays));   
    }
	
	public static void main(String[] args) throws Exception{
//		System.out.println("将时间类型装换成String类型: " + new DateUtils().formatDateToString(new Date(), "yyyy-MM-dd 00:00:00.000"));
//		System.out.println("将String类型格式化为Date类型 : " + new DateUtils().formatStringtoDate("2010-08-12 13:23:23", "yyyy-MM-dd"));
//		System.out.println("格式化当前时间的毫秒格式 : " + new DateUtils().getMimtime());
//		System.out.println("将字符串装换为时间格式 : " + new DateUtils().getDate("2010-07-12 12:23:43"));
//		System.out.println("得到现在时间的长时间格式: " + new DateUtils().formatLongDateString(new Date()));
//		System.out.println("得到现在时间的短时间格式: " + new DateUtils().formatShortDateString(new Date()));
//		System.out.println("格式化当前时间为1009 取年2位 和月份 : " + new DateUtils().getFourGroupNum());
//		System.out.println("格式化当前时间的时分秒 : " + new DateUtils().getHour());
//		System.out.println("得到上个月 今天的日期 : " + new DateUtils().getLastMonthString(new Date()));
//		System.out.println("得到上一周今天的日期 : " + new DateUtils().getLastWeekString(new Date()));
//		System.out.println("得到上一年今天的日期 : " + new DateUtils().getLastYearString(new Date()));
//		System.out.println("获得格式为短时间类型( 年-月)的系统时间 : " + new DateUtils().getYearM());
//		System.out.println("得到昨天的日期 : " + new DateUtils().getYesterdayString(new Date()));
//		System.out.println("取得当时的时间戳。年（四位）_月（两位）_日（两位）_时（两位）_分（两位）_秒（两位）_毫秒（三位） : " + new DateUtils().getNowTimeStamp());
//		System.out.println("得到今天星期几,返回数字: " + new DateUtils().getWeek());
//		System.out.println("得到今天星期几,返回中文: " + new DateUtils().getWeekCH());
//		System.out.println("时间加: " + addDate(new Date(), 15).toString());
//		// System.out.println("时间减: "+diffDate(new Date(),new Date()));
//		System.out.println("将时间类型装换成String类型: " + new DateUtils().formatDateToString(new Date(), "yyyyMMddHHmmss"));
//		System.out.println(System.currentTimeMillis());
//		String t = "20151116185159";
//		System.out.println(formatNumberToDate(t));
		//System.out.println(JudgeFormat("2016-02-18"));
	}
}

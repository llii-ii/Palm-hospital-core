package com.kasite.core.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Clob;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.coreframework.util.CharPool;
import com.coreframework.util.Randomizer;
import com.kasite.core.common.constant.KstHosConstant;

/**
 * 字符串公函
 * 
 * @author daiys
 * @since 2012-07-24
 */
@SuppressWarnings("rawtypes")
public class StringUtil {

	/**
	 * 验证手机号正则表达式
	 */
	static Pattern mobilePattern = Pattern.compile("^((14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");

	/**
	 * 手机号去敏感
	 * @param mobile
	 * @return
	 */
	public static String mobileFormat(String mobile) {
		return FormatUtils.mobileFormat(mobile);
	}
	/**
	 * 身份证去敏感
	 * @param idCardNo
	 * @return
	 */
	public static String idCardFormat(String idCardNo) {
		return FormatUtils.idCardFormat(idCardNo);
	}
	
	
	public static String formatXML(String retStr,String encoding) throws Exception {
		String res = null;
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(retStr);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw e;
		}
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(encoding);
        format.setNewLineAfterDeclaration(false);
		format.setExpandEmptyElements(true);
		format.setSuppressDeclaration(true);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		XMLWriter writer = new XMLWriter(outputStream, format);
		writer.write(doc);
		writer.close();
		res = outputStream.toString(encoding);
		return res;
	}
	
	public static String getToString(Object obj) {
		StringBuffer sbf = new StringBuffer();
		if (null != obj) {
			Class clazz = obj.getClass();
			sbf.append("Class = (" + clazz.getName() + ")\t");
			Field[] fs = clazz.getDeclaredFields();
			for (Field field : fs) {
				try {
					// Object o =
					// com.coreframework.util.ReflectionUtils.getFieldValue(obj,
					// field.getName());//.get(obj);
					field.setAccessible(true);
					Object o = field.get(obj);
					if (null != o) {
						sbf.append(field.getName());
						sbf.append(" = ");
						sbf.append(o);
						sbf.append("\t");
					}
					field.setAccessible(false);
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				}

			}
		}
		return sbf.toString();
	}

	/**
	 * 异常字符串处理方法
	 * 
	 * @param e
	 * @return
	 */
	public static String getException(Throwable e) {
		String str = getExceptionInfo(e);
		try {
			if (null != str && str.indexOf("Caused by") != -1) {
				str = str.substring(str.indexOf("Caused by:") + 10);
				str = str.substring(0, str.indexOf("\r\n\t"));
			} else {
				str = e.getMessage();
			}
		} catch (Exception ex) {
		}

		return str;
	}

	public static String getExceptionInfo(Throwable e) {
		if ((e instanceof UndeclaredThrowableException)) {
			UndeclaredThrowableException targetEx = (UndeclaredThrowableException) e;
			Throwable t = targetEx.getUndeclaredThrowable();

			StringWriter sw = new StringWriter();
			PrintWriter w = new PrintWriter(sw);
			t.printStackTrace(w);

			return sw.toString();
		}
		StringWriter sw = new StringWriter();
		PrintWriter w = new PrintWriter(sw);
		e.printStackTrace(w);
		return sw.toString();
	}
	  /**
     * 检查指定的字符串列表是否不为空。
     */
	public static boolean areNotEmpty(String... values) {
		boolean result = true;
		if (values == null || values.length == 0) {
			result = false;
		} else {
			for (String value : values) {
				result &= !isEmpty(value);
			}
		}
		return result;
	}
	// String is null or not
	public static boolean isEmpty(String str) {
		if ("".equals(str) || "null".equals(str) || "NULL".equals(str) || str == null || "undefined".equals(str))
			return true;
		if ("".equals(str.trim()))
			return true;
		return false;
	}

	// List is null or not

	public static boolean isEmpty(List list) {
		return isBlank(list);
	}

	// Map is null or not
	public static boolean isEmpty(Map map) {
		if (map == null || map.size() == 0)
			return true;
		return false;
	}

	public static boolean isEmpty2(Object obj) {
		if (obj == null)
			return true;

		if (obj instanceof String) {
			return isEmpty((String) obj);
		}
		if (obj instanceof Long) {
			return ((Long) obj == 0);
		}
		if (obj instanceof Integer) {
			return ((Integer) obj == 0);
		}
		if (obj instanceof List) {
			return (((List) obj).size() == 0);
		}
		if (obj instanceof Map) {
			return isEmpty((Map) obj);
		}
		return obj == null;
	}

	// to do empty string
	public static String chEmptyStr(String str) {
		if ("".equals(str) || "null".equals(str) || "NULL".equals(str) || str == null)
			return "";
		return str.trim();
	}

	public static String chMap(Map map, String key) {
		String res = "";
		if (map != null) {
			return chEmptyStr(map.get(key) + "");
		}
		return res;
	}

	// chang clob type to String
	public static String clobToStr(Clob clob) throws Exception {
		return (clob != null ? clob.getSubString(1, (int) clob.length()) : "");
	}

	public static int parseInt(Integer i) {
		if (i == null)
			return 0;
		return i;
	}

	public static int parseInt(Integer i, int v) {
		if (i == null)
			return v;
		return i;
	}

	/**
	 * 判断字符串是否为纯数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNum(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches())
			return true;
		return false;
	}

	/**
	 * 判断是否包含中文
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isContainsChinese(String str) {
		String regEx = "[\u4e00-\u9fa5]";
		Pattern pat = Pattern.compile(regEx);
		Matcher matcher = pat.matcher(str);
		boolean flg = false;
		if (matcher.find()) {
			flg = true;
		}
		return flg;
	}

	/**
	 * 判断日期是否合法
	 * 
	 * @param date
	 * @return
	 */
	public static boolean checkDate(String date) {
		boolean res = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);
		try {
			sdf.parse(date);
			res = true;
		} catch (Exception e) {
		}
		return res;
	}

	/**
	 * 从身份证提取生日
	 * 
	 * @param identityID
	 * @return
	 */
	public static Integer[] getBirthYMD(String identityID) {
		int y = 0, m = 0, d = 0;
		if (identityID.length() == 15) {
			y = Integer.parseInt("19" + identityID.substring(6, 8));
			m = Integer.parseInt(identityID.substring(8, 10));
			d = Integer.parseInt(identityID.substring(10, 12));
		}
		if (identityID.length() == 18) {
			y = Integer.parseInt(identityID.substring(6, 10));
			m = Integer.parseInt(identityID.substring(10, 12));
			d = Integer.parseInt(identityID.substring(12, 14));
		}
		Integer[] a = new Integer[3];
		a[0] = y;
		a[1] = m;
		a[2] = d;
		return a;
	}

	/**
	 * 从生日中提取年月日
	 * 
	 * @param birth
	 * @return
	 */
	public static Integer[] getBirthYMDbyBirth(String birth) {
		int y = 0, m = 0, d = 0;
		if (!StringUtil.isEmpty(birth)) {
			y = Integer.parseInt(birth.substring(0, 4));
			m = Integer.parseInt(birth.substring(5, 7));
			d = Integer.parseInt(birth.substring(8, 10));
		}
		Integer[] a = new Integer[3];
		a[0] = y;
		a[1] = m;
		a[2] = d;
		return a;
	}

	public static String getWeek(int weekindex) {
		switch (weekindex) {
		case 0:
			return "星期天";
		case 1:
			return "星期一";
		case 2:
			return "星期二";
		case 3:
			return "星期三";
		case 4:
			return "星期四";
		case 5:
			return "星期五";
		case 6:
			return "星期六";
		default:
			return "";
		}
	}

	public static String getWeekTN(String date) {
		try {
			String a = DateOper.getWeekType(date);
			int b = DateOper.getDayOfWeek(date, "yyyy-MM-dd");
			return "(<font color=red>" + a + "</font>)" + StringUtil.getWeek(b);

		} catch (Exception e) {
		}
		return "";
	}

	/**
	 * 判断字符串是否存在
	 * 
	 * @param str
	 * @param arr
	 *            英文逗号隔开的字符串数组
	 * @return
	 */
	public static boolean strInArr(String str, String[] arr) {
		boolean b = false;
		if (arr != null && arr.length > 0) {
			for (String s : arr) {
				if (str.equals(s)) {
					b = true;
					break;
				}
			}
		}
		return b;
	}

	public static String delHtmlTagaaa(String str1) {
		if (StringUtil.isEmpty(str1))
			return "";
		String str = "";
		str = str1;

		str = str.replaceAll("</?[^>]+>", ""); // 剔出了<html>的标签
		str = str.replace("&nbsp;", "");
		// str = str.replace(".", "");
		// str = str.replace("\"", "‘");
		// str = str.replace("'", "‘");
		str = str.replaceAll("'", "\"");

		return str;

	}

	public static String editHtmlTag(String str) {
		if (StringUtil.isEmpty(str))
			return "";
		str = str.replaceAll("'", "\"");
		return str;
	}

	public static String retrim(String str) {
		if (StringUtil.isEmpty(str))
			return "";
		str = str.trim();
		str = str.replaceAll(" ", "");
		str = str.replaceAll("　", "");

		return str;
	}

	public static String getPermitway(String waystatus) {
		String a = waystatus.substring(0, 1);
		String b = waystatus.substring(1, 2);
		String c = waystatus.substring(2, 3);
		List<String> list = new ArrayList<String>();

		if ("1".equals(a)) {
			list.add("1");
		}
		if ("1".equals(b)) {
			list.add("2");
		}
		if ("1".equals(c)) {
			list.add("3");
		}
		String temp = "";
		if (list.size() > 0) {
			for (String str : list) {
				temp += str + ",";
			}
			temp = temp.substring(0, temp.length() - 1);
		}
		return temp;
	}

	/**
	 * to get standard dept code with four numbers
	 * 
	 * @param sd
	 * @return
	 */
	public static String getStandardDeptId(String sd) {
		if (!StringUtil.isEmpty(sd)) {
			String zero = "";
			for (int i = 0; i < 4 - sd.length(); i++) {
				zero += "0";
			}
			return zero + sd;
		}
		return "";
	}

	/**
	 * to judge where the registerdate is coming date;
	 * 
	 * @param registerdate
	 * @param timeid
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static boolean isAfertime(String registerdate, int timeid) {
		try {
			Date nowdate = new Date();
			String date = DateOper.getNow("yyyy-MM-dd");
			if (registerdate.compareTo(date) > 0)
				return true;
			Integer h = nowdate.getHours();
			int Nowtimeid = 1;
			if (h > 12) {
				Nowtimeid = 2;
			}
			if (registerdate.compareTo(date) == 0 && timeid == 2 && Nowtimeid == 1)
				return true;
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 日期相减得到分钟 date1>date2结果为正数
	 * 
	 * @param date1
	 *            yyyy-MM-dd hh:mm:ss
	 * @param date2
	 *            yyyy-MM-dd hh:mm:ss
	 * @return
	 */
	public static int minusReMin(String date1, String date2) {
		try {
			Calendar d1 = Calendar.getInstance();
			Calendar d2 = Calendar.getInstance();
			d1.setTime(DateOper.parse(date1));
			d2.setTime(DateOper.parse(date2));
			long a1 = d1.getTimeInMillis();
			long a2 = d2.getTimeInMillis();
			long sss = a1 - a2;// Math.abs(a1-a2);
			// int day=(int)((sss)/(3600*24*1000));
			int min = (int) (sss * 60 / 3600000);
			return min;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static String getDateFormat(Object object, String type) {
		SimpleDateFormat sdf = new SimpleDateFormat(type);
		if (object == null) {
			return null;
		} else {
			try {
				if ("".equals(String.valueOf(object))) {
					return null;
				} else {
					return sdf.format(object);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	/**
	 * 将字符串列表以指定分隔符串起来
	 * 
	 * @param splitStr
	 *            分隔符
	 * @param strList
	 *            要串起来的字符串列表
	 * @return 用分隔符串起来的字符串，无记录时返回空字符
	 */
	public static String joinStr(String splitStr, String... strList) {
		if (strList != null && strList.length > 0) {
			StringBuilder builder = new StringBuilder();
			for (String str : strList) {
				builder.append(str);
				builder.append(splitStr);
			}
			if (splitStr != null && !"".equals(splitStr)) {
				builder.delete(builder.length() - splitStr.length(), builder.length());
			}
			return builder.toString();
		} else {
			return "";
		}
	}

	/**
	 * JSON字符串特殊字符处理，比如：“\A1;1300”
	 * 
	 * @param s
	 * @return String
	 */
	public static String JsonCharFilter(String sourceStr) {
		// sourceStr = sourceStr.replace("\\", "\\\\");
		sourceStr = sourceStr.replace("\b", " ");
		sourceStr = sourceStr.replace("\t", " ");
		sourceStr = sourceStr.replace("\n", " ");
		sourceStr = sourceStr.replace("\f", " ");
		sourceStr = sourceStr.replace("\r", " ");
		return sourceStr;
	}

	/***
	 * 在s 后面添加 add 并以 ' , ' 号隔开
	 * 
	 * @param s
	 * @param add
	 * @return
	 */
	public static String add(String s, String add) {
		return add(s, add, StringPool.COMMA);
	}

	public static String add(String s, String add, String delimiter) {
		return add(s, add, delimiter, false);
	}

	public static String add(String s, String add, String delimiter, boolean allowDuplicates) {

		if ((add == null) || (delimiter == null)) {
			return null;
		}

		if (s == null) {
			s = StringPool.BLANK;
		}

		if (allowDuplicates || !contains(s, add, delimiter)) {
			StringBundler sb = new StringBundler();

			sb.append(s);

			if (isBlank(s) || s.endsWith(delimiter)) {
				sb.append(add);
				sb.append(delimiter);
			} else {
				sb.append(delimiter);
				sb.append(add);
				sb.append(delimiter);
			}

			s = sb.toString();
		}

		return s;
	}

	public static String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);

		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0x0100 + (bytes[i] & 0x00FF)).substring(1);

			if (hex.length() < 2) {
				sb.append("0");
			}

			sb.append(hex);
		}

		return sb.toString();
	}

	public static boolean contains(String s, String text) {
		return contains(s, text, StringPool.COMMA);
	}

	public static boolean contains(String s, String text, String delimiter) {
		if ((s == null) || (text == null) || (delimiter == null)) {
			return false;
		}

		if (!s.endsWith(delimiter)) {
			s = s.concat(delimiter);
		}

		String dtd = delimiter.concat(text).concat(delimiter);

		int pos = s.indexOf(dtd);

		if (pos == -1) {
			String td = text.concat(delimiter);

			if (s.startsWith(td)) {
				return true;
			}

			return false;
		}

		return true;
	}

	public static int count(String s, String text) {
		if ((s == null) || (text == null)) {
			return 0;
		}

		int count = 0;

		int pos = s.indexOf(text);

		while (pos != -1) {
			pos = s.indexOf(text, pos + text.length());

			count++;
		}

		return count;
	}

	public static boolean endsWith(String s, char end) {
		return endsWith(s, (new Character(end)).toString());
	}

	public static boolean endsWith(String s, String end) {
		if ((s == null) || (end == null)) {
			return false;
		}

		if (end.length() > s.length()) {
			return false;
		}

		String temp = s.substring(s.length() - end.length(), s.length());

		if (temp.equalsIgnoreCase(end)) {
			return true;
		} else {
			return false;
		}
	}

	public static String insert(String s, String insert, int offset) {
		if (s == null) {
			return null;
		}

		if (insert == null) {
			return s;
		}

		if (offset > s.length()) {
			offset = s.length();
		}

		StringBuilder sb = new StringBuilder(s);

		sb.insert(offset, insert);

		return sb.toString();
	}

	public static String lowerCase(String s) {
		if (s == null) {
			return null;
		} else {
			return s.toLowerCase();
		}
	}

	/**
	 * 首字母小写
	 * 
	 * @param string
	 * @return
	 */
	public static String lowerCaseFirstUpper(String string) {
		char[] charArray = string.toCharArray();

		if ((charArray[0] >= 65) && (charArray[0] <= 90)) {
			charArray[0] = (char) (charArray[0] + 32);
		}

		return new String(charArray);
	}

	public static boolean matches(String s, String pattern) {
		String[] array = pattern.split("\\*");

		for (int i = 0; i < array.length; i++) {
			int pos = s.indexOf(array[i]);

			if (pos == -1) {
				return false;
			}

			s = s.substring(pos + array[i].length());
		}

		return true;
	}

	public static String merge(boolean[] array) {
		return merge(array, StringPool.COMMA);
	}

	public static String merge(boolean[] array, String delimiter) {
		if (array == null) {
			return null;
		}

		StringBundler sb = null;

		if (array.length == 0) {
			sb = new StringBundler();
		} else {
			sb = new StringBundler(2 * array.length - 1);
		}

		for (int i = 0; i < array.length; i++) {
			sb.append(String.valueOf(array[i]).trim());

			if ((i + 1) != array.length) {
				sb.append(delimiter);
			}
		}

		return sb.toString();
	}

	public static String merge(Collection<?> col) {
		return merge(col, StringPool.COMMA);
	}

	public static String merge(Collection<?> col, String delimiter) {
		if (col == null) {
			return null;
		}

		return merge(col.toArray(new Object[col.size()]), delimiter);
	}

	public static String merge(double[] array) {
		return merge(array, StringPool.COMMA);
	}

	public static String merge(double[] array, String delimiter) {
		if (array == null) {
			return null;
		}

		StringBundler sb = null;

		if (array.length == 0) {
			sb = new StringBundler();
		} else {
			sb = new StringBundler(2 * array.length - 1);
		}

		for (int i = 0; i < array.length; i++) {
			sb.append(String.valueOf(array[i]).trim());

			if ((i + 1) != array.length) {
				sb.append(delimiter);
			}
		}

		return sb.toString();
	}

	public static String merge(float[] array) {
		return merge(array, StringPool.COMMA);
	}

	public static String merge(float[] array, String delimiter) {
		if (array == null) {
			return null;
		}

		StringBundler sb = null;

		if (array.length == 0) {
			sb = new StringBundler();
		} else {
			sb = new StringBundler(2 * array.length - 1);
		}

		for (int i = 0; i < array.length; i++) {
			sb.append(String.valueOf(array[i]).trim());

			if ((i + 1) != array.length) {
				sb.append(delimiter);
			}
		}

		return sb.toString();
	}

	public static String merge(int[] array) {
		return merge(array, StringPool.COMMA);
	}

	public static String merge(int[] array, String delimiter) {
		if (array == null) {
			return null;
		}

		StringBundler sb = null;

		if (array.length == 0) {
			sb = new StringBundler();
		} else {
			sb = new StringBundler(2 * array.length - 1);
		}

		for (int i = 0; i < array.length; i++) {
			sb.append(String.valueOf(array[i]).trim());

			if ((i + 1) != array.length) {
				sb.append(delimiter);
			}
		}

		return sb.toString();
	}

	public static String merge(long[] array) {
		return merge(array, StringPool.COMMA);
	}

	public static String merge(long[] array, String delimiter) {
		if (array == null) {
			return null;
		}

		StringBundler sb = null;

		if (array.length == 0) {
			sb = new StringBundler();
		} else {
			sb = new StringBundler(2 * array.length - 1);
		}

		for (int i = 0; i < array.length; i++) {
			sb.append(String.valueOf(array[i]).trim());

			if ((i + 1) != array.length) {
				sb.append(delimiter);
			}
		}

		return sb.toString();
	}

	public static String merge(Object[] array) {
		return merge(array, StringPool.COMMA);
	}

	public static String merge(Object[] array, String delimiter) {
		if (array == null) {
			return null;
		}

		StringBundler sb = null;

		if (array.length == 0) {
			sb = new StringBundler();
		} else {
			sb = new StringBundler(2 * array.length - 1);
		}

		for (int i = 0; i < array.length; i++) {
			sb.append(String.valueOf(array[i]).trim());

			if ((i + 1) != array.length) {
				sb.append(delimiter);
			}
		}

		return sb.toString();
	}

	public static String merge(short[] array) {
		return merge(array, StringPool.COMMA);
	}

	public static String merge(short[] array, String delimiter) {
		if (array == null) {
			return null;
		}

		StringBundler sb = null;

		if (array.length == 0) {
			sb = new StringBundler();
		} else {
			sb = new StringBundler(2 * array.length - 1);
		}

		for (int i = 0; i < array.length; i++) {
			sb.append(String.valueOf(array[i]).trim());

			if ((i + 1) != array.length) {
				sb.append(delimiter);
			}
		}

		return sb.toString();
	}

	public static String randomize(String s) {
		return Randomizer.getInstance().randomize(s);
	}

	public static String remove(String s, String remove) {
		return remove(s, remove, StringPool.COMMA);
	}

	public static String remove(String s, String remove, String delimiter) {
		if ((s == null) || (remove == null) || (delimiter == null)) {
			return null;
		}

		if (isNotBlank(s) && !s.endsWith(delimiter)) {
			s += delimiter;
		}

		String drd = delimiter.concat(remove).concat(delimiter);

		String rd = remove.concat(delimiter);

		while (contains(s, remove, delimiter)) {
			int pos = s.indexOf(drd);

			if (pos == -1) {
				if (s.startsWith(rd)) {
					int x = remove.length() + delimiter.length();
					int y = s.length();

					s = s.substring(x, y);
				}
			} else {
				int x = pos + remove.length() + delimiter.length();
				int y = s.length();

				String temp = s.substring(0, pos);

				s = temp.concat(s.substring(x, y));
			}
		}

		return s;
	}

	public static String replace(String s, char oldSub, char newSub) {
		if (s == null) {
			return null;
		}

		return s.replace(oldSub, newSub);
	}

	public static String replace(String s, char oldSub, String newSub) {
		if ((s == null) || (newSub == null)) {
			return null;
		}

		// The number 5 is arbitrary and is used as extra padding to reduce
		// buffer expansion

		StringBuilder sb = new StringBuilder(s.length() + 5 * newSub.length());

		char[] charArray = s.toCharArray();

		for (char c : charArray) {
			if (c == oldSub) {
				sb.append(newSub);
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	public static String replace(String s, String oldSub, String newSub) {
		return replace(s, oldSub, newSub, 0);
	}

	public static String replace(String s, String oldSub, String newSub, int fromIndex) {

		if ((s == null) || (oldSub == null) || (newSub == null)) {
			return null;
		}

		if (oldSub.equals(StringPool.BLANK)) {
			return s;
		}

		int y = s.indexOf(oldSub, fromIndex);

		if (y >= 0) {
			StringBundler sb = new StringBundler();

			int length = oldSub.length();
			int x = 0;

			while (x <= y) {
				sb.append(s.substring(x, y));
				sb.append(newSub);

				x = y + length;
				y = s.indexOf(oldSub, x);
			}

			sb.append(s.substring(x));

			return sb.toString();
		} else {
			return s;
		}
	}

	public static String replace(String s, String begin, String end, Map<String, String> values) {

		StringBundler sb = replaceToStringBundler(s, begin, end, values);

		return sb.toString();
	}

	public static String replace(String s, String[] oldSubs, String[] newSubs) {
		if ((s == null) || (oldSubs == null) || (newSubs == null)) {
			return null;
		}

		if (oldSubs.length != newSubs.length) {
			return s;
		}

		for (int i = 0; i < oldSubs.length; i++) {
			s = replace(s, oldSubs[i], newSubs[i]);
		}

		return s;
	}

	public static String replace(String s, String[] oldSubs, String[] newSubs, boolean exactMatch) {

		if ((s == null) || (oldSubs == null) || (newSubs == null)) {
			return null;
		}

		if (oldSubs.length != newSubs.length) {
			return s;
		}

		if (!exactMatch) {
			replace(s, oldSubs, newSubs);
		} else {
			for (int i = 0; i < oldSubs.length; i++) {
				s = s.replaceAll("\\b" + oldSubs[i] + "\\b", newSubs[i]);
			}
		}

		return s;
	}

	public static String replaceFirst(String s, char oldSub, char newSub) {
		if (s == null) {
			return null;
		}

		return replaceFirst(s, String.valueOf(oldSub), String.valueOf(newSub));
	}

	public static String replaceFirst(String s, char oldSub, String newSub) {
		if ((s == null) || (newSub == null)) {
			return null;
		}

		return replaceFirst(s, String.valueOf(oldSub), newSub);
	}

	public static String replaceFirst(String s, String oldSub, String newSub) {
		if ((s == null) || (oldSub == null) || (newSub == null)) {
			return null;
		}

		if (oldSub.equals(newSub)) {
			return s;
		}

		int y = s.indexOf(oldSub);

		if (y >= 0) {
			return s.substring(0, y).concat(newSub).concat(s.substring(y + oldSub.length()));
		} else {
			return s;
		}
	}

	public static String replaceFirst(String s, String[] oldSubs, String[] newSubs) {

		if ((s == null) || (oldSubs == null) || (newSubs == null)) {
			return null;
		}

		if (oldSubs.length != newSubs.length) {
			return s;
		}

		for (int i = 0; i < oldSubs.length; i++) {
			s = replaceFirst(s, oldSubs[i], newSubs[i]);
		}

		return s;
	}

	public static String replaceLast(String s, char oldSub, char newSub) {
		if (s == null) {
			return null;
		}

		return replaceLast(s, String.valueOf(oldSub), String.valueOf(newSub));
	}

	public static String replaceLast(String s, char oldSub, String newSub) {
		if ((s == null) || (newSub == null)) {
			return null;
		}

		return replaceLast(s, String.valueOf(oldSub), newSub);
	}

	public static String replaceLast(String s, String oldSub, String newSub) {
		if ((s == null) || (oldSub == null) || (newSub == null)) {
			return null;
		}

		if (oldSub.equals(newSub)) {
			return s;
		}

		int y = s.lastIndexOf(oldSub);

		if (y >= 0) {
			return s.substring(0, y).concat(newSub).concat(s.substring(y + oldSub.length()));
		} else {
			return s;
		}
	}

	public static String replaceLast(String s, String[] oldSubs, String[] newSubs) {

		if ((s == null) || (oldSubs == null) || (newSubs == null)) {
			return null;
		}

		if (oldSubs.length != newSubs.length) {
			return s;
		}

		for (int i = 0; i < oldSubs.length; i++) {
			s = replaceLast(s, oldSubs[i], newSubs[i]);
		}

		return s;
	}

	public static StringBundler replaceToStringBundler(String s, String begin, String end, Map<String, String> values) {

		if ((s == null) || (begin == null) || (end == null) || (values == null) || (values.isEmpty())) {

			return new StringBundler(s);
		}

		StringBundler sb = new StringBundler(values.size() * 2 + 1);

		int pos = 0;

		while (true) {
			int x = s.indexOf(begin, pos);
			int y = s.indexOf(end, x + begin.length());

			if ((x == -1) || (y == -1)) {
				sb.append(s.substring(pos, s.length()));

				break;
			} else {
				sb.append(s.substring(pos, x));

				String oldValue = s.substring(x + begin.length(), y);

				String newValue = values.get(oldValue);

				if (newValue == null) {
					newValue = oldValue;
				}

				sb.append(newValue);

				pos = y + end.length();
			}
		}

		return sb;
	}

	public static StringBundler replaceWithStringBundler(String s, String begin, String end,
			Map<String, StringBundler> values) {

		if ((s == null) || (begin == null) || (end == null) || (values == null) || (values.isEmpty())) {

			return new StringBundler(s);
		}

		int size = values.size() + 1;

		for (StringBundler valueSB : values.values()) {
			size += valueSB.index();
		}

		StringBundler sb = new StringBundler(size);

		int pos = 0;

		while (true) {
			int x = s.indexOf(begin, pos);
			int y = s.indexOf(end, x + begin.length());

			if ((x == -1) || (y == -1)) {
				sb.append(s.substring(pos, s.length()));

				break;
			} else {
				sb.append(s.substring(pos, x));

				String oldValue = s.substring(x + begin.length(), y);

				StringBundler newValue = values.get(oldValue);

				if (newValue == null) {
					sb.append(oldValue);
				} else {
					sb.append(newValue);
				}

				pos = y + end.length();
			}
		}

		return sb;
	}

	public static String reverse(String s) {
		if (s == null) {
			return null;
		}

		char[] charArray = s.toCharArray();
		char[] reverse = new char[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			reverse[i] = charArray[charArray.length - i - 1];
		}

		return new String(reverse);
	}

	public static String safePath(String path) {
		return replace(path, StringPool.DOUBLE_SLASH, StringPool.SLASH);
	}

	public static String shorten(String s) {
		return shorten(s, 20);
	}

	public static String shorten(String s, int length) {
		return shorten(s, length, "...");
	}

	public static String shorten(String s, int length, String suffix) {
		if ((s == null) || (suffix == null)) {
			return null;
		}

		if (s.length() > length) {
			for (int j = length; j >= 0; j--) {
				if (Character.isWhitespace(s.charAt(j))) {
					length = j;

					break;
				}
			}

			String temp = s.substring(0, length);

			s = temp.concat(suffix);
		}

		return s;
	}

	public static String shorten(String s, String suffix) {
		return shorten(s, 20, suffix);
	}

	public static boolean startsWith(String s, char begin) {
		return startsWith(s, (new Character(begin)).toString());
	}

	public static boolean startsWith(String s, String start) {
		if ((s == null) || (start == null)) {
			return false;
		}

		if (start.length() > s.length()) {
			return false;
		}

		String temp = s.substring(0, start.length());

		if (temp.equalsIgnoreCase(start)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Return the number of starting letters that s1 and s2 have in common
	 * before they deviate.
	 *
	 * @return the number of starting letters that s1 and s2 have in common
	 *         before they deviate
	 */
	public static int startsWithWeight(String s1, String s2) {
		if ((s1 == null) || (s2 == null)) {
			return 0;
		}

		char[] charArray1 = s1.toCharArray();
		char[] charArray2 = s2.toCharArray();

		int i = 0;

		for (; (i < charArray1.length) && (i < charArray2.length); i++) {
			if (charArray1[i] != charArray2[i]) {
				break;
			}
		}

		return i;
	}

	public static String strip(String s, char remove) {
		if (s == null) {
			return null;
		}

		int x = s.indexOf(remove);

		if (x < 0) {
			return s;
		}

		int y = 0;

		StringBuilder sb = new StringBuilder(s.length());

		while (x >= 0) {
			sb.append(s.subSequence(y, x));

			y = x + 1;

			x = s.indexOf(remove, y);
		}

		sb.append(s.substring(y));

		return sb.toString();
	}

	public static String stripBetween(String s, String begin, String end) {
		if ((s == null) || (begin == null) || (end == null)) {
			return s;
		}

		StringBuilder sb = new StringBuilder(s.length());

		int pos = 0;

		while (true) {
			int x = s.indexOf(begin, pos);
			int y = s.indexOf(end, x + begin.length());

			if ((x == -1) || (y == -1)) {
				sb.append(s.substring(pos, s.length()));

				break;
			} else {
				sb.append(s.substring(pos, x));

				pos = y + end.length();
			}
		}

		return sb.toString();
	}

	public static String toCharCode(String s) {
		StringBundler sb = new StringBundler(s.length());

		for (int i = 0; i < s.length(); i++) {
			sb.append(s.codePointAt(i));
		}

		return sb.toString();
	}

	public static String toHexString(int i) {
		char[] buffer = new char[8];

		int index = 8;

		do {
			buffer[--index] = _HEX_DIGITS[i & 15];

			i >>>= 4;
		} while (i != 0);

		return new String(buffer, index, 8 - index);
	}

	public static String toHexString(long l) {
		char[] buffer = new char[16];

		int index = 16;

		do {
			buffer[--index] = _HEX_DIGITS[(int) (l & 15)];

			l >>>= 4;
		} while (l != 0);

		return new String(buffer, index, 16 - index);
	}

	public static String toHexString(Object obj) {
		if (obj instanceof Integer) {
			return toHexString(((Integer) obj).intValue());
		} else if (obj instanceof Long) {
			return toHexString(((Long) obj).longValue());
		} else {
			return String.valueOf(obj);
		}
	}

	public static String trim(String s) {
		return trim(s, null);
	}

	public static String trim(String s, char c) {
		return trim(s, new char[] { c });
	}

	public static String trim(String s, char[] exceptions) {
		if (s == null) {
			return null;
		}

		char[] charArray = s.toCharArray();

		int len = charArray.length;

		int x = 0;
		int y = charArray.length;

		for (int i = 0; i < len; i++) {
			char c = charArray[i];

			if (_isTrimable(c, exceptions)) {
				x = i + 1;
			} else {
				break;
			}
		}

		for (int i = len - 1; i >= 0; i--) {
			char c = charArray[i];

			if (_isTrimable(c, exceptions)) {
				y = i;
			} else {
				break;
			}
		}

		if ((x != 0) || (y != len)) {
			return s.substring(x, y);
		} else {
			return s;
		}
	}

	public static String trimLeading(String s) {
		return trimLeading(s, null);
	}

	public static String trimLeading(String s, char c) {
		return trimLeading(s, new char[] { c });
	}

	public static String trimLeading(String s, char[] exceptions) {
		if (s == null) {
			return null;
		}

		char[] charArray = s.toCharArray();

		int len = charArray.length;

		int x = 0;
		int y = charArray.length;

		for (int i = 0; i < len; i++) {
			char c = charArray[i];

			if (_isTrimable(c, exceptions)) {
				x = i + 1;
			} else {
				break;
			}
		}

		if ((x != 0) || (y != len)) {
			return s.substring(x, y);
		} else {
			return s;
		}
	}

	public static String trimTrailing(String s) {
		return trimTrailing(s, null);
	}

	public static String trimTrailing(String s, char c) {
		return trimTrailing(s, new char[] { c });
	}

	public static String trimTrailing(String s, char[] exceptions) {
		if (s == null) {
			return null;
		}

		char[] charArray = s.toCharArray();

		int len = charArray.length;

		int x = 0;
		int y = charArray.length;

		for (int i = len - 1; i >= 0; i--) {
			char c = charArray[i];

			if (_isTrimable(c, exceptions)) {
				y = i;
			} else {
				break;
			}
		}

		if ((x != 0) || (y != len)) {
			return s.substring(x, y);
		} else {
			return s;
		}
	}

	public static String unquote(String s) {
		if (isBlank(s)) {
			return s;
		}

		if ((s.charAt(0) == CharPool.APOSTROPHE) && (s.charAt(s.length() - 1) == CharPool.APOSTROPHE)) {

			return s.substring(1, s.length() - 1);
		} else if ((s.charAt(0) == CharPool.QUOTE) && (s.charAt(s.length() - 1) == CharPool.QUOTE)) {

			return s.substring(1, s.length() - 1);
		}

		return s;
	}

	public static String upperCase(String s) {
		if (s == null) {
			return null;
		} else {
			return s.toUpperCase();
		}
	}

	public static String upperCaseFirstLetter(String s) {
		char[] charArray = s.toCharArray();

		if ((charArray[0] >= 97) && (charArray[0] <= 122)) {
			charArray[0] = (char) (charArray[0] - 32);
		}

		return new String(charArray);
	}

	public static String valueOf(Object obj) {
		return String.valueOf(obj);
	}

	private static boolean _isTrimable(char c, char[] exceptions) {
		if ((exceptions != null) && (exceptions.length > 0)) {
			for (int i = 0; i < exceptions.length; i++) {
				if (c == exceptions[i]) {
					return false;
				}
			}
		}

		return Character.isWhitespace(c);
	}

	/**
	 * 返回字符串的副本，忽略前导空白和尾部空白。
	 * 
	 * @param value
	 * @return
	 */
	public static String asText(String value) {
		if (isBlank(value)) {
			return null;
		}
		return value.trim();
	}

	public static String toCommaText(Set<String> allTag) {
		return toCommaText(allTag, false);
	}

	public static String toCommaText(Set<String> allTag, boolean nospace) {
		String separator = nospace ? StringPool.COMMA : StringPool.COMMA_AND_SPACE;
		StringBuilder sb = new StringBuilder();
		String t;
		for (Iterator it = allTag.iterator(); it.hasNext(); sb.append(t)) {
			t = (String) it.next();
			if (sb.length() > 0) {
				sb.append(separator);
			}
		}

		return sb.toString();
	}

	public static String toSeparatorText(Collection<String> allTag, String sep, boolean nospace) {
		String separator = nospace ? sep : (new StringBuilder()).append(sep).append(StringPool.SPACE).toString();
		StringBuilder sb = new StringBuilder();
		String t;
		for (Iterator it = allTag.iterator(); it.hasNext(); sb.append(t)) {
			t = (String) it.next();
			if (sb.length() > 0) {
				sb.append(separator);
			}
		}

		return sb.toString();
	}

	@SuppressWarnings({ "resource", "unchecked" })
	public static Set fromCommaText(String text) {
		Set set = new LinkedHashSet();
		if (text != null) {
			Scanner scan = (new Scanner(text)).useDelimiter(StringPool.COMMA);
			do {
				if (!scan.hasNext()) {
					break;
				}
				String t = scan.next().trim();
				if (t.length() != 0) {
					set.add(t);
				}
			} while (true);
		}
		return set;
	}

	/*
	 * 把16进制字符串转换成字节数组
	 * 
	 * @param hex
	 * 
	 * @return
	 */
	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	/**
	 * 把字节数组转换为对象
	 * 
	 * @param bytes
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object bytesToObject(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		ObjectInputStream oi = new ObjectInputStream(in);
		Object o = oi.readObject();
		oi.close();
		return o;
	}

	/**
	 * 把可序列化对象转换成字节数组
	 * 
	 * @param s
	 * @return
	 * @throws IOException
	 */
	public static byte[] objectToBytes(Serializable s) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream ot = new ObjectOutputStream(out);
		ot.writeObject(s);
		ot.flush();
		ot.close();
		return out.toByteArray();
	}

	public static String objectToHexString(Serializable s) throws IOException {
		return bytesToHexString(objectToBytes(s));
	}

	public static Object hexStringToObject(String hex) throws IOException, ClassNotFoundException {
		return bytesToObject(hexStringToByte(hex));
	}

	/**
	 * @函数功能: BCD码转为10进制串(阿拉伯数据)
	 * @输入参数: BCD码
	 * @输出结果: 10进制串
	 */
	public static String bcd2Str(byte[] bytes) {
		StringBuilder temp = new StringBuilder(bytes.length * 2);

		for (int i = 0; i < bytes.length; i++) {
			temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
			temp.append((byte) (bytes[i] & 0x0f));
		}
		return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1) : temp.toString();
	}

	/**
	 * @函数功能: 10进制串转为BCD码
	 * @输入参数: 10进制串
	 * @输出结果: BCD码
	 */
	public static byte[] str2Bcd(String asc) {
		int len = asc.length();
		int mod = len % 2;

		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}

		byte abt[] = new byte[len];
		if (len >= 2) {
			len = len / 2;
		}

		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;

		for (int p = 0; p < asc.length() / 2; p++) {
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}

			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}

			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}

	public final static char[] BToA = "0123456789abcdef".toCharArray();

	/**
	 * @函数功能: BCD码转ASC码
	 * @输入参数: BCD串
	 * @输出结果: ASC码
	 */
	public static String BCD2ASC(byte[] bytes) {
		StringBuilder temp = new StringBuilder(bytes.length * 2);

		for (int i = 0; i < bytes.length; i++) {
			int h = ((bytes[i] & 0xf0) >>> 4);
			int l = (bytes[i] & 0x0f);
			temp.append(BToA[h]).append(BToA[l]);
		}
		return temp.toString();
	}

	public static byte[] str2cbcd(String s) {
		if (s.length() % 2 != 0) {
			s = "0" + s;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		char[] cs = s.toCharArray();
		for (int i = 0; i < cs.length; i += 2) {
			int high = cs[i] - 48;
			int low = cs[i + 1] - 48;
			baos.write(high << 4 | low);
		}
		return baos.toByteArray();
	}

	public static String cbcd2string(byte[] b) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			int h = ((b[i] & 0xff) >> 4) + 48;
			sb.append((char) h);
			int l = (b[i] & 0x0f) + 48;
			sb.append((char) l);
		}
		return sb.toString();
	}

	/**
	 * MD5加密字符串，返回加密后的16进制字符串
	 * 
	 * @param origin
	 * @return
	 */
	public static String MD5EncodeToHex(String origin) {
		return bytesToHexString(MD5Encode(origin));
	}

	/**
	 * MD5加密字符串，返回加密后的字节数组
	 * 
	 * @param origin
	 * @return
	 */
	public static byte[] MD5Encode(String origin) {
		return MD5Encode(origin.getBytes());
	}

	/**
	 * MD5加密字节数组，返回加密后的字节数组
	 * 
	 * @param bytes
	 * @return
	 */
	public static byte[] MD5Encode(byte[] bytes) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			return md.digest(bytes);
		} catch (NoSuchAlgorithmException e) {
			return new byte[0];
		}
	}

	/**
	 * 将字符串列表转换为以“,”分隔的字符串
	 * 
	 * @param strList
	 * @return
	 */
	public static String convertToString(List<String> strList) {
		StringBuilder buffer = new StringBuilder();
		for (String str : strList) {
			buffer.append(str).append(StringPool.COMMA);
		}
		return buffer.substring(0, buffer.length() - 2);
	}

	/**
	 * Returns a string with replaced values. This method will replace all text
	 * in the given string, between the beginning and ending delimiter, with new
	 * values found in the given map. For example, if the string contained the
	 * text <code>[$HELLO$]</code>, and the beginning delimiter was
	 * <code>[$]</code>, and the ending delimiter was <code>$]</code>, and the
	 * values map had a key of <code>HELLO</code> that mapped to
	 * <code>WORLD</code>, then the replaced string will contain the text
	 * <code>[$WORLD$]</code>.
	 *
	 * @param s
	 *            the original string
	 * @param begin
	 *            the beginning delimiter
	 * @param end
	 *            the ending delimiter
	 * @param values
	 *            a map of old and new values
	 * @return a string with replaced values
	 */
	public static String replaceValues(String s, String begin, String end, Map<String, String> values) {

		if ((s == null) || (begin == null) || (end == null) || (values == null) || (values.isEmpty())) {

			return s;
		}

		StringBuilder sb = new StringBuilder(s.length());

		int pos = 0;

		while (true) {
			int x = s.indexOf(begin, pos);
			int y = s.indexOf(end, x + begin.length());

			if ((x == -1) || (y == -1)) {
				sb.append(s.substring(pos, s.length()));

				break;
			} else {
				sb.append(s.substring(pos, x + begin.length()));

				String oldValue = s.substring(x + begin.length(), y);

				String newValue = values.get(oldValue);

				if (newValue == null) {
					newValue = oldValue;
				}

				sb.append(newValue);

				pos = y;
			}
		}

		return sb.toString();
	}

	/**
	 * 将以“,”为分隔符字符串转换为字符串列表
	 * 
	 * @param str
	 * @return
	 */
	public static List<String> convertToList(String str) {
		String[] arr = str.split(StringPool.COMMA);
		return Arrays.asList(arr);
	}
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static List<Long> convertToLongList(String str) {
		String[] arr = str.split(StringPool.COMMA);
		List<Long> ll = new ArrayList<>();
		for (String ss : arr) {
			ll.add(Long.parseLong(ss));
		}
		return ll;
	}

	/**
	 * 计算目标数组与源数组的相似程度
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public static int calcMatchDegree(Object[] source, Object[] target) {
		int result = 0;

		if (source == null || target == null) {
			return result;
		}

		Arrays.sort(source);

		for (int i = 0; i < target.length; i++) {
			Object value = target[i];
			if (Arrays.binarySearch(source, value) >= 0) {
				result++;
			}
		}

		return result;
	}

	/**
	 * 判断对象是否为空！(null,"", "null")
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isBlank(Object obj) {
		if (obj == null) {
			return true;
		}
		if (obj instanceof String) {
			return isBlank((String) obj);
		} else {
			return isBlank(obj.toString());
		}
	}
	// public static boolean isEmpty(Object obj) {
	// return isBlank(obj);
	// }
	// public static boolean isNotEmpty(Object obj) {
	// return isNotBlank(obj);
	// }

	/**
	 * 判断对象是否为空！(null,"", "null")
	 * 
	 * @param value
	 * @return
	 */
	private static boolean isBlank(String value) {
		if (value == null || value.length() == 0) {
			return true;
		} else if (StringPool.BLANK.equals(value) || StringPool.NULL.equals(value)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断对象是否非空！(null,"", "null")
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNotBlank(Object obj) {
		return !isBlank(obj);
	}

	public static boolean eq(Object o1, Object o2) {
		return (o1 == null) ? false : (o2 == null) ? true : o1.equals(o2);
	}

	public static String CRLFToHtml(String content) {
		String s = content;
		s = s.replaceAll("\r\n", "<br/>");
		s = s.replaceAll("\t", "　　");
		return s;
	}

	/**
	 * 格式化字符串
	 * 
	 * @param value
	 * @param byteLength
	 *            目标长度
	 * @param lastFill
	 *            true后补空格，false前补空格
	 * @return
	 */
	public static String format(String value, int byteLength, boolean lastFillSpace) {
		return format(value, byteLength, lastFillSpace, StringPool.SPACE);
	}

	/**
	 * 格式化字符串
	 * 
	 * @param value
	 * @param byteLength
	 *            目标长度
	 * @param lastFill
	 *            填充位置 true后补，false前补
	 * @param fillValue
	 *            填充值
	 * @return
	 */
	public static String format(String value, int byteLength, boolean lastFill, String fillValue) {
		String result = value;
		if (value != null) {
			byte[] values = value.getBytes();
			if (values.length > byteLength) {
				values = Arrays.copyOfRange(values, 0, byteLength);
				return new String(values);
			}
		} else {
			result = StringPool.BLANK;
		}

		int count = byteLength - result.getBytes().length;
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < count; i++) {
			buffer.append(fillValue);
		}
		if (lastFill) {
			buffer.insert(0, result);
		} else {
			buffer.append(result);
		}
		return buffer.toString();
	}

	/**
	 * 数据格式化，保留2位小数，带分割符
	 * 
	 * @param value
	 * @return
	 */
	public static String format(Number value) {
		return format(value, 1, 2, true);
	}

	/**
	 * 数据格式化，带分割符
	 * 
	 * @param value
	 * @param decimal-小数位数
	 * @return
	 */
	public static String format(Number value, int decimal) {
		return format(value, 1, decimal, true);
	}

	/**
	 * 数据格式化
	 * 
	 * @param value
	 * @param integer
	 *            保留整数部分位数
	 * @param decimal
	 *            保留小数部分位数
	 * @param useSeparator
	 *            整数部分是否使用分割符
	 * @return
	 */
	public static String format(Number value, int integer, int decimal, boolean useSeparator) {
		String len = "";
		for (int i = 0; i < decimal; i++) {
			len += "0";
		}

		if (value == null && decimal == 0) {
			return "0";
		}

		if (value == null && decimal > 0) {
			return "0." + len;
		}

		String format = "";
		for (int i = 1; i <= integer; i++) {
			format = "0" + format;
			if (useSeparator && i % 3 == 0) {
				format = "," + format;
			}
		}

		if (useSeparator) {
			for (int i = integer + 1; i < 18; i++) {
				format = "#" + format;
				if (i % 3 == 0) {
					format = "," + format;
				}
			}
		} else {
			for (int i = integer + 1; i < 18; i++) {
				format = "#" + format;
			}
		}

		format = "#" + format + len;

		DecimalFormat decimalFormat = new DecimalFormat(format);
		return decimalFormat.format(value);
	}

	/**
	 * 拼接字符串
	 * 
	 * @param stringArray
	 * @return
	 */
	public static String concatString(String... stringArray) {
		StringBundler bundler = new StringBundler(stringArray);
		return bundler.toString();
	}

	/**
	 * 将传入的身份证号码进行校验，并返回一个对应的18位身份证
	 *
	 * @param personIDCode
	 *            身份证号码
	 * @return String 十八位身份证号码
	 * @throws 无效的身份证号
	 */
	public static String getFixedPersonIDCode(String personIDCode) throws Exception {
		if (personIDCode == null) {
			throw new Exception("输入的身份证号无效，请检查");
		}

		if (personIDCode.length() == 18) {
			if (isIdentity(personIDCode)) {
				return personIDCode;
			} else {
				throw new Exception("输入的身份证号无效，请检查");
			}
		} else if (personIDCode.length() == 15) {
			return fixPersonIDCodeWithCheck(personIDCode);
		} else {
			throw new Exception("输入的身份证号无效，请检查");
		}
	}

	/**
	 * 修补15位居民身份证号码为18位，并校验15位身份证有效性
	 *
	 * @param personIDCode
	 *            十五位身份证号码
	 * @return String 十八位身份证号码
	 * @throws 无效的身份证号
	 */
	public static String fixPersonIDCodeWithCheck(String personIDCode) throws Exception {
		if (personIDCode == null || personIDCode.trim().length() != 15) {
			throw new Exception("输入的身份证号不足15位，请检查");
		}

		if (!isIdentity(personIDCode)) {
			throw new Exception("输入的身份证号无效，请检查");
		}

		return fixPersonIDCodeWithoutCheck(personIDCode);
	}

	/**
	 * 修补15位居民身份证号码为18位，不校验身份证有效性
	 *
	 * @param personIDCode
	 *            十五位身份证号码
	 * @return 十八位身份证号码
	 * @throws 身份证号参数不是15位
	 */
	public static String fixPersonIDCodeWithoutCheck(String personIDCode) throws Exception {
		if (personIDCode == null || personIDCode.trim().length() != 15) {
			throw new Exception("输入的身份证号不足15位，请检查");
		}

		String id17 = personIDCode.substring(0, 6) + "19" + personIDCode.substring(6, 15); // 15位身份证补'19'

		char[] code = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' }; // 11个校验码字符
		int[] factor = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 }; // 18个加权因子
		int[] idcd = new int[18];
		int sum; // 根据公式 ∑(ai×Wi) 计算
		int remainder; // 第18位校验码
		for (int i = 0; i < 17; i++) {
			idcd[i] = Integer.parseInt(id17.substring(i, i + 1));
		}
		sum = 0;
		for (int i = 0; i < 17; i++) {
			sum = sum + idcd[i] * factor[i];
		}
		remainder = sum % 11;
		String lastCheckBit = String.valueOf(code[remainder]);
		return id17 + lastCheckBit;
	}

	/**
	 * 判断是否是有效的18位或15位居民身份证号码
	 *
	 * @param identity
	 *            18位或15位居民身份证号码
	 * @return 是否为有效的身份证号码
	 */
	public static boolean isIdentity(String identity) {
		if (identity == null) {
			return false;
		}
		if (identity.length() == 18 || identity.length() == 15) {
			String id15 = null;
			if (identity.length() == 18) {
				id15 = identity.substring(0, 6) + identity.substring(8, 17);
			} else {
				id15 = identity;
			}
			try {
				Long.parseLong(id15); // 校验是否为数字字符串

				String birthday = "19" + id15.substring(6, 12);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				sdf.parse(birthday); // 校验出生日期
				if (identity.length() == 18 && !fixPersonIDCodeWithoutCheck(id15).equals(identity)) {
					return false; // 校验18位身份证
				}
			} catch (Exception e) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 从身份证号中获取出生日期，身份证号可以为15位或18位
	 *
	 * @param identity
	 *            身份证号
	 * @return 出生日期
	 * @throws 身份证号出生日期段有误
	 */
	public static Timestamp getBirthdayFromPersonIDCode(String identity) throws Exception {
		String id = getFixedPersonIDCode(identity);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			Timestamp birthday = new Timestamp(sdf.parse(id.substring(6, 14)).getTime());
			return birthday;
		} catch (ParseException e) {
			throw new Exception("不是有效的身份证号，请检查");
		}
	}

	/**
	 * 从身份证号获取性别
	 *
	 * @param identity
	 *            身份证号
	 * @return 性别代码
	 * @throws Exception
	 *             无效的身份证号码
	 */
	public static String getGenderFromPersonIDCode(String identity) throws Exception {
		String id = getFixedPersonIDCode(identity);
		char sex = id.charAt(16);
		return sex % 2 == 0 ? "2" : "1";
	}

	/**
	 * 将货币转换为大写形式(类内部调用)
	 *
	 * @param val
	 * @return String
	 */
	private static String PositiveIntegerToHanStr(String NumStr) {
		// 输入字符串必须正整数，只允许前导空格(必须右对齐)，不宜有前导零
		String RMBStr = "";
		boolean lastzero = false;
		boolean hasvalue = false; // 亿、万进位前有数值标记
		int len, n;
		len = NumStr.length();
		if (len > 15) {
			return "数值过大!";
		}
		for (int i = len - 1; i >= 0; i--) {
			if (NumStr.charAt(len - i - 1) == ' ') {
				continue;
			}
			n = NumStr.charAt(len - i - 1) - '0';
			if (n < 0 || n > 9) {
				return "输入含非数字字符!";
			}

			if (n != 0) {
				if (lastzero) {
					RMBStr += HanDigiStr[0]; // 若干零后若跟非零值，只显示一个零
				} // 除了亿万前的零不带到后面
				// if( !( n==1 && (i%4)==1 && (lastzero || i==len-1) ) )
				// 如十进位前有零也不发壹音用此行
				if (!(n == 1 && (i % 4) == 1 && i == len - 1)) // 十进位处于第一位不发壹音
				{
					RMBStr += HanDigiStr[n];
				}
				RMBStr += HanDiviStr[i]; // 非零值后加进位，个位为空
				hasvalue = true; // 置万进位前有值标记

			} else {
				if ((i % 8) == 0 || ((i % 8) == 4 && hasvalue)) // 亿万之间必须有非零值方显示万
				{
					RMBStr += HanDiviStr[i]; // “亿”或“万”
				}
			}
			if (i % 8 == 0) {
				hasvalue = false; // 万进位前有值标记逢亿复位
			}
			lastzero = (n == 0) && (i % 4 != 0);
		}

		if (RMBStr.length() == 0) {
			return HanDigiStr[0]; // 输入空字符或"0"，返回"零"
		}
		return RMBStr;
	}

	/**
	 * 将货币转换为大写形式
	 *
	 * @param val
	 *            传入的数据
	 * @return String 返回的人民币大写形式字符串
	 */
	public static String numToRMBStr(double val) {
		String SignStr = "";
		String TailStr = "";
		long fraction, integer;
		int jiao, fen;

		if (val < 0) {
			val = -val;
			SignStr = "负";
		}
		if (val > 99999999999999.999 || val < -99999999999999.999) {
			return "数值位数过大!";
		}
		// 四舍五入到分
		long temp = Math.round(val * 100);
		integer = temp / 100;
		fraction = temp % 100;
		jiao = (int) fraction / 10;
		fen = (int) fraction % 10;
		if (jiao == 0 && fen == 0) {
			TailStr = "整";
		} else {
			TailStr = HanDigiStr[jiao];
			if (jiao != 0) {
				TailStr += "角";
			}
			// 零元后不写零几分
			if (integer == 0 && jiao == 0) {
				TailStr = "";
			}
			if (fen != 0) {
				TailStr += HanDigiStr[fen] + "分";
			}
		}
		// 下一行可用于非正规金融场合，0.03只显示“叁分”而不是“零元叁分”
		// if( !integer ) return SignStr+TailStr;
		return SignStr + PositiveIntegerToHanStr(String.valueOf(integer)) + "元" + TailStr;
	}

	private static final char[] _HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };
	private static String HanDigiStr[] = new String[] { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
	private static String HanDiviStr[] = new String[] { "", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万",
			"拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟" };

	public static String numToCh(int val) {

		String str = String.valueOf(val);

		char[] cs = str.toCharArray();

		String retval = "";
		for (char v : cs) {
			String va = String.valueOf(v);
			retval += HanDigiStr[Integer.parseInt(va)];

		}
		return retval;
	}

	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		}

		if (obj instanceof String) {
			return (obj == null || "null".equals(obj) || obj.toString().trim().length() == 0);
		}
		if (obj instanceof Long) {
			return (obj == null || (Long) obj == 0);
		}
		if (obj instanceof Integer) {
			return (obj == null || (Integer) obj == 0);
		}
		if (obj instanceof List) {
			return (obj == null || ((List) obj).size() == 0);
		}
		return obj == null;
	}

	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	/**
	 * 校验手机号 + 17号段
	 *
	 * @param mobiles
	 * @return
	 * @author 無
	 * @date 2018年5月3日 上午11:00:50
	 */
	public static boolean isMobileNew(String mobiles) {
		String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 校验手机号
	 *
	 * @param mobiles
	 * @return
	 * @author 無
	 * @date 2018年5月3日 上午11:01:03
	 */
	public static boolean isMobile(String mobiles) {
		String regular = "^((14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
		Pattern p = Pattern.compile(regular);
		Matcher m = p.matcher(mobiles);
		return m.matches();

	}

	public static String getException(Exception e) {
		String caused = "Caused by";
		if(null != e) {
			String str = getExceptionInfo(e);
			if (null != str && str.indexOf(caused) != -1) {
				str = str.substring(str.indexOf(caused) + 10);
				str = str.substring(0, str.indexOf("\r\n\t"));
			} else {
				str =( str == null ? e.getMessage() :str.substring(0, str.indexOf("\n\t")));
			}
			return str;
		}
		return caused;
	}

	public static String getJSONValue(Object obj) {
		if (obj == null) {
			return "";
		} else {
			return obj.toString();
		}
	}

	public static String getDecodeValue(String str) {
		if (str == null || str.trim().length() == 0) {
			return "";
		} else {
			try {
				return URLDecoder.decode(str, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return "";
			}
		}
	}

	/**
	 * 去掉数组第一个值
	 * 
	 * @param arr
	 * @return
	 */
	public static String deleteFirstNode(String[] arr) {
		if (arr.length < KstHosConstant.NUMBER_2) {
			return null;
		}
		String[] arrNew = new String[arr.length - 1];
		for (int i = 0; i < arr.length; i++) {
			if (i == 0) {
				continue;
			}
			arrNew[i - 1] = arr[i];
		}
		String ts = "";
		for (String s : arrNew) {
			ts += s + ",";
		}
		return ts.substring(0, ts.length() - 1);
	}

	public static String html2Text(String inputString) {
		// 鍚玥tml鏍囩鐨勫瓧绗︿覆
		String htmlStr = inputString;
		String textStr = "";
		java.util.regex.Pattern pScript;
		java.util.regex.Matcher mScript;
		java.util.regex.Pattern pStyle;
		java.util.regex.Matcher mStyle;
		java.util.regex.Pattern pHtml;
		java.util.regex.Matcher mHtml;
		try {
			// 瀹氫箟script鐨勬鍒欒〃杈惧紡{鎴?script[^>]*?>[\\s\\S]*?<\\/script>
			String regExScript = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
			// 瀹氫箟style鐨勬鍒欒〃杈惧紡{鎴?style[^>]*?>[\\s\\S]*?<\\/style>
			String regExStyle = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
			// 瀹氫箟HTML鏍囩鐨勬鍒欒〃杈惧紡
			String regExHtml = "<[^>]+>";
			pScript = Pattern.compile(regExScript, Pattern.CASE_INSENSITIVE);
			mScript = pScript.matcher(htmlStr);
			// 杩囨护script鏍囩
			htmlStr = mScript.replaceAll("");

			pStyle = Pattern.compile(regExStyle, Pattern.CASE_INSENSITIVE);
			mStyle = pStyle.matcher(htmlStr);
			// 杩囨护style鏍囩
			htmlStr = mStyle.replaceAll("");

			pHtml = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE);
			mHtml = pHtml.matcher(htmlStr);
			// 杩囨护html鏍囩
			htmlStr = mHtml.replaceAll("");

			htmlStr = htmlStr.replaceAll("\\\\n", "");
			htmlStr = htmlStr.replaceAll("\\\\r", "");
			htmlStr = htmlStr.replaceAll("&nbsp;", "");
			textStr = htmlStr;
		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}
		// 杩斿洖鏂囨湰瀛楃涓?
		return textStr;
	}

	public static String unescape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	public static String[] inverseStringArray(String[] array) {
		String t;
		int j = array.length;
		for (int i = 0; i < j; i++) {
			if (j >= i) {
				t = array[i];
				array[i] = array[--j];
				array[j] = t;
			} else {
				break;
			}
		}
		return array;
	}

	public static Map<String, String> getXmlElmentValue(String xml) {
		System.out.print(xml);
		Map<String, String> map = new HashMap<String, String>(16);
		try {
			Document doc = DocumentHelper.parseText(xml);
			Element el = doc.getRootElement();
			return recGetXmlElementValue(el, map);
		} catch (DocumentException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Map<String, String> recGetXmlElementValue(Element ele, Map<String, String> map) {
		List<Element> eleList = ele.elements();
		if (eleList.size() == 0) {
			map.put(toLowerCaseFirstOne(ele.getName()), ele.getTextTrim());
			return map;
		} else {
			for (Iterator<Element> iter = eleList.iterator(); iter.hasNext();) {
				org.dom4j.Element innerEle = iter.next();
				recGetXmlElementValue(innerEle, map);
			}
			return map;
		}
	}

	public static String getString(String param) {
		if (param == null) {
			return "";
		}
		return param;
	}

	/**
	 * 首字母转小写
	 * 
	 * @param s
	 * @author lcy
	 * @return
	 */
	public static String toLowerCaseFirstOne(String s) {
		if (Character.isLowerCase(s.charAt(0))) {
			return s;
		} else {
			return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
		}
	}

	/**
	 * 首字母转大写
	 * 
	 * @param s
	 * @author lcy
	 * @return
	 */
	public static String toUpperCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0))) {
			return s;
		} else {
			return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
		}
	}

	/**
	 * 生成随机数
	 * 
	 * @param min
	 *            最小值
	 * @param max
	 *            最大值
	 * @return
	 */
	public static int getRandomNumber(int min, int max) {
		return (int) (Math.random() * (max - min + 1) + min);
	}

	/**
	 * 获取UUID
	 * 
	 * @return
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 获取异常信息
	 * 
	 * @param e
	 * @return
	 */
	public static String getExceptionStack(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw, true));
		return (sw.toString());
	}
	
	/**
	 * 分转元
	 * @param fen
	 * @return
	 */
	public static String fenChangeYuan(Object fen){
		if(fen == null) {
			return "0";
		}
		BigDecimal yuan = null;
		BigDecimal valueBigDecimal = new BigDecimal(String.valueOf(fen));
		BigDecimal d2 = new BigDecimal("0.01");
		yuan = valueBigDecimal.multiply(d2);
		return yuan.toString();
	}

	/**
	 * 分转元
	 * 
	 * @param fen
	 * @return
	 */
	public static String fenChangeYuan(Integer fen) {
		if(fen == null) {
			return "0";
		}
		double yuan = 0;
		BigDecimal valueBigDecimal = new BigDecimal(fen);
		BigDecimal d2 = new BigDecimal("0.01");
		yuan = valueBigDecimal.multiply(d2).doubleValue();
		return yuan + "";
	}

	/**
	 * 元转分
	 * 
	 * @param fen
	 * @return
	 */
	public static String yuanChangeFen(String yuan) {
		Integer fen = 0;
		BigDecimal valueBigDecimal = new BigDecimal(yuan);
		BigDecimal d2 = new BigDecimal("100");
		fen = valueBigDecimal.multiply(d2).intValue();
		return fen + "";
	}
	
	public static int yuanChangeFenInt(String yuan) {
		Integer fen = 0;
		BigDecimal valueBigDecimal = new BigDecimal(yuan);
		BigDecimal d2 = new BigDecimal("100");
		fen = valueBigDecimal.multiply(d2).intValue();
		return fen ;
	}

	/**
	 * InputStream转换成String 注意:流关闭需要自行处理
	 * 
	 * @param in
	 * @param encoding
	 *            编码
	 * @return String
	 * @throws Exception
	 */
	public static String inputStreamTOString(InputStream in, String encoding) throws IOException {
		int bufferSize = 4096;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[bufferSize];
		int count = -1;
		while ((count = in.read(data, 0, bufferSize)) != -1) {
			outStream.write(data, 0, count);
		}
		data = null;
		byte[] outByte = outStream.toByteArray();
		outStream.close();
		return new String(outByte, encoding);
	}

	public static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	public static String getFileEndWitsh(String headerField) {
		String fileEndWitsh = "";
		if (headerField.equalsIgnoreCase(KstHosConstant.HEADERFIELD_GIF)) {
			fileEndWitsh = ".gif";
		} else if (headerField.equalsIgnoreCase(KstHosConstant.HEADERFIELD_IMG)) {
			fileEndWitsh = ".img";
		} else if (headerField.equalsIgnoreCase(KstHosConstant.HEADERFIELD_JPG)) {
			fileEndWitsh = ".jpg";
		} else if (headerField.equalsIgnoreCase(KstHosConstant.HEADERFIELD_JPEG)) {
			fileEndWitsh = ".jpeg";
		} else if (headerField.equalsIgnoreCase(KstHosConstant.HEADERFIELD_JPE)) {
			fileEndWitsh = ".jpe";
		}
		return fileEndWitsh;
	}

	/**
	 * str为empty则返回null否则返回整形
	 * 
	 */
	public static Integer intEmptyToNull(String str) {
		return isEmpty(str) ? null : Integer.valueOf(str.trim());
	}

	/**
	 * str为empty则返回null否则返回去空字符串
	 * 
	 */
	public static String emptyToNull(String str) {
		return isEmpty(str) ? null : str.trim();
	}

	public static double parsDouble(double s1, int d) {
		double s2 = Double.parseDouble(String.valueOf(d));
		java.math.BigDecimal value = new java.math.BigDecimal(s1 / s2);
		double result = value.setScale(2, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
		return result;
	}
	
	/**
	 * 姓名脱敏,首字改为*
	 * @param name
	 */
	public static String nameDesensitization(String name) {
		return "*"+name.substring(1);
	}
	
	/**
	 * 身份证出生日期脱敏
	 * @param idCardNo
	 * @return
	 */
	public static String idCardNoDesensitization(String idCardNo) {
		if(isNotBlank(idCardNo)) {
			return idCardNo.replaceAll("(?<=.{6}).(?=.{4})","*");
		}
		return null;
	}
	
	/**
	 * 手机号中间4位脱敏
	 * @param mobile
	 * @return
	 */
	public static String mobileDesensitization(String mobile) {
		if(isNotBlank(mobile)) {
			return mobile.replaceAll("(?<=.{3}).(?=.{4})","*");
		}
		return null;
	}
	
	/**
	 * 金额-字符串转Integer
	 * 
	 * @param price
	 * @return
	 */
	public static Integer strToInt(String price) {
		if(isEmpty(price)) {
			return null;
		}
		return new Integer(price);
	}
	
	/**
	 * 金额-Integer转字符串
	 * 
	 * @param price
	 * @return
	 */
	public static String intToStr(Integer price) {
		if(price == null) {
			return null;
		}
		return String.valueOf(price);
	}
}

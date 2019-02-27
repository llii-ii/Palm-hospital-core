package com.kasite.core.common.util;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.PageVo;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.util.wxmsg.IDSeed;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.hos.util.RelativeXmlNode;
import com.yihu.wsgw.api.InterfaceMessage;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

/**
 * 通用工具类
 *
 * @author 無
 * @version V1.0
 * @date 2018年4月24日 下午3:25:08
 */
public class CommonUtil {
	private static String todaydatetime;
	static{
		try {
			todaydatetime = DateOper.getNow("MMddHHmms");
			//获取当前分钟  如果分钟 和 当前内存中的一样 说明需要重新获取 
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	private static long startId = 0;
	private static synchronized long getAddNum() throws ParseException {
		String now = DateOper.getNow("MMddHHmms");
		//避免每分钟并发比较大的时候重复  判断  startId > 1000000 才重置
		if(!todaydatetime.equals(now) || startId > 1000000) {
			todaydatetime = now;
			startId = 0;
		}
		startId = startId + 1;
		return startId;
	}
	/**
	 * 获取一个数字型随机的ID 一天内不重复 
	 * @return
	 * @throws ParseException
	 */
	public static synchronized String getOrderNum() throws ParseException {
		//00112000101
		StringBuffer sbf = new StringBuffer(KasiteConfig.getConfigId());
		sbf.append(DateOper.getNow("HHmmss"));
		sbf.append(String.format("%03d", getAddNum()));
		return sbf.toString();
	}
	
	/**
	 * 取一个UUID 加上时间戳 YYYYMMDD
	 * */
	public static String getUUIDYMD() {
		try {
			String m = KasiteConfig.getConfigId(false);
			StringBuffer sbf = new StringBuffer();
			if(null != m) {
				sbf.append(m);
			}
			sbf.append(DateOper.getNow("MMDDHH"));
			sbf.append(IDSeed.next());
			return sbf.toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (IDSeed.next());
	}

	/**
	 * 获取uuid值
	 */
	public static String getUUID() {
		return (IDSeed.next());
	}
	/**
	 * 获取uuid值
	 */
	public static String getGUID() {
		return UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
	}
	/**
	 * 返回前端XML
	 * 
	 * @throws ParamException
	 */
	public static String getRetVal(InterfaceMessage interfaceMsg,String transActionCode, Integer code, String msg) {
		return getRetVal(interfaceMsg,transActionCode, code, msg, null);
	}
	
	/**
	 * 返回前端XML
	 * 
	 * @throws ParamException
	 */
	public static String getRetVal(InterfaceMessage msg,String transActionCode, RetCode ret) {
		return getRetVal(msg,transActionCode, ret.getCode(), ret.getMessage(), null);
	}
	/**
	 * 返回前端XML
	 * 
	 * @throws ParamException
	 */
	public static String getRetVal(InterfaceMessage msg,String transActionCode, RetCode ret,String message) {
		return getRetVal(msg,transActionCode, ret.getCode(), message, null);
	}
	/**
	 * 返回前端XML
	 * 
	 * @throws ParamException
	 */
	public static String getRetVal(InterfaceMessage msg,String transActionCode, RetCode.Success ret,CommonResp<?> resp) {
		//TODO 调整出参控制 未将出参加上
		return getRetVal(msg,transActionCode, ret.getCode(), "", null);
	}
	/**
	 * 返回前端XML
	 * 
	 * @throws ParamException
	 */
	public static String getRetVal(InterfaceMessage msg,String transActionCode, RetCode ret, Document doc) {
		return getRetVal(msg,transActionCode, ret.getCode(), ret.getMessage(), doc);
	}

	/**
	 * 返回前端XML
	 * 
	 * @throws ParamException
	 */
	public static String getRetVal(InterfaceMessage interfaceMsg,String transActionCode, Integer code, String msg, Document doc) {

		if (doc != null) {
			Element el = doc.getRootElement();
			XMLUtil.addElement(el, KstHosConstant.TRANSACTIONCODE, transActionCode);
			XMLUtil.addElement(el, KstHosConstant.RESPCODE, code);
			XMLUtil.addElement(el, KstHosConstant.RESPMESSAGE, msg);
		} else {
			doc = DocumentHelper.createDocument();
			Element resp = doc.addElement(KstHosConstant.RESP);
			XMLUtil.addElement(resp, KstHosConstant.TRANSACTIONCODE, transActionCode);
			XMLUtil.addElement(resp, KstHosConstant.RESPCODE, code);
			XMLUtil.addElement(resp, KstHosConstant.RESPMESSAGE, msg);
		}
		return doc.asXML();
	}

	/**
	 * 根据查询到的list结果集 生成返回前端的XML字符串
	 * 
	 * @param transActionCode
	 *            交易代码
	 * @param totalcolumns
	 *            返回全部的节点名称多个逗号分隔
	 * @param data1Columns
	 *            返回Data1节点名称多个逗号分隔
	 * @param list
	 *            数据对象
	 * @param pageVo
	 *            分页对象
	 * @return
	 * @throws ParamException
	 */
	public static String getRetVal(InterfaceMessage msg,String transActionCode, String totalcolumns, String data1Columns, List<?> list,
			PageVo pageVo) throws ParamException {
		Document doc = DocumentHelper.createDocument();
		Element resp = doc.addElement(KstHosConstant.RESP);
		XMLUtil.addElement(resp, KstHosConstant.TRANSACTIONCODE, transActionCode);
		XMLUtil.addElement(resp, KstHosConstant.RESPCODE, RetCode.Success.RET_10000.getCode());
		XMLUtil.addElement(resp, KstHosConstant.RESPMESSAGE, RetCode.Success.RET_10000.getMessage());
		String[] cols = totalcolumns.split(",");
		List<String> cols1 = Arrays.asList(data1Columns.split(","));
		if (pageVo != null) {
			Element page = DocumentHelper.createElement(KstHosConstant.PAGE);
			XMLUtil.addElement(page, KstHosConstant.PINDEX, pageVo.getPIndex());
			XMLUtil.addElement(page, KstHosConstant.PSIZE, pageVo.getPSize());
			XMLUtil.addElement(page, KstHosConstant.PCOUNT, pageVo.getPCount());
			resp.add(page);
		}
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Element data = DocumentHelper.createElement(KstHosConstant.DATA);
				Element data1 = data.addElement(KstHosConstant.DATA_1);
				Object obj = list.get(i);
				Field[] ff = obj.getClass().getDeclaredFields();
				for (int j = 0; j < cols.length; j++) {
					String keyName = cols[j];
					boolean bb = false;
					for (int k = 0; k < ff.length; k++) {
						if (ff[k].isAnnotationPresent(RelativeXmlNode.class)) {
							RelativeXmlNode rxn = ff[k].getAnnotation(RelativeXmlNode.class);
							if (keyName.equalsIgnoreCase(rxn.XmlFiled())
									&& rxn.BeanFiled().equalsIgnoreCase(ff[k].getName())) {
								bb = true;
								if (cols1.contains(keyName)) {
									XMLUtil.addElement(data1, rxn.XmlFiled(),
											ReflectionUtils.getFieldValue(obj, ff[k].getName()));
								} else {
									XMLUtil.addElement(data, rxn.XmlFiled(),
											ReflectionUtils.getFieldValue(obj, ff[k].getName()));
								}
								break;
							}
						} else {
							if (keyName.equalsIgnoreCase(ff[k].getName())) {
								bb = true;
								if (cols1.contains(keyName)) {
									XMLUtil.addElement(data1, keyName,
											ReflectionUtils.getFieldValue(obj, ff[k].getName()));
								} else {
									XMLUtil.addElement(data, keyName,
											ReflectionUtils.getFieldValue(obj, ff[k].getName()));
								}
								break;
							}
						}
					}
					if (!bb) {
						XMLUtil.addElement(data, keyName, "");
					}
				}
				resp.add(data);
			}
		}
		return doc.asXML();
	}

	/**
	 * 根据查询到的list接口集 生成返回前端的XML字符串
	 * 
	 * @param transActionCode
	 *            交易代码
	 * @param columns
	 *            返回节点名称多个逗号分隔
	 * @param list
	 *            数据对象
	 * @param pageVo
	 *            分页对象
	 * @return
	 * @throws ParamException
	 */
	public static String getRetVal(InterfaceMessage msg,String transActionCode, String columns, Object obj) throws ParamException {
		Document doc = DocumentHelper.createDocument();
		Element resp = doc.addElement(KstHosConstant.RESP);
		XMLUtil.addElement(resp, KstHosConstant.TRANSACTIONCODE, transActionCode);
		XMLUtil.addElement(resp, KstHosConstant.RESPCODE, RetCode.Success.RET_10000.getCode());
		XMLUtil.addElement(resp, KstHosConstant.RESPMESSAGE, RetCode.Success.RET_10000.getMessage());
		String[] cols = columns.split(",");
		// ReflectionHelper
		Element data = DocumentHelper.createElement(KstHosConstant.DATA);
		if (obj != null) {
			Field[] ff = obj.getClass().getDeclaredFields();
			for (int j = 0; j < cols.length; j++) {
				String keyName = cols[j];
				boolean bb = false;
				for (int k = 0; k < ff.length; k++) {
					if (ff[k].isAnnotationPresent(RelativeXmlNode.class)) {
						RelativeXmlNode rxn = ff[k].getAnnotation(RelativeXmlNode.class);
						if (keyName.equalsIgnoreCase(rxn.XmlFiled())
								&& rxn.BeanFiled().equalsIgnoreCase(ff[k].getName())) {
							bb = true;
							XMLUtil.addElement(data, rxn.XmlFiled(),
									ReflectionUtils.getFieldValue(obj, ff[k].getName()));
							break;
						}
					} else {
						if (keyName.equalsIgnoreCase(ff[k].getName())) {
							bb = true;
							XMLUtil.addElement(data, keyName, ReflectionUtils.getFieldValue(obj, ff[k].getName()));
							break;
						}
					}
				}
				if (!bb) {
					XMLUtil.addElement(data, keyName, "");
				}
			}
		}
		resp.add(data);
		return doc.asXML();
	}

	/**
	 * 根据查询到的list结果集 生成返回前端的XML字符串
	 * 
	 * @param transActionCode
	 *            交易代码
	 * @param columns
	 *            返回节点名称多个逗号分隔
	 * @param list
	 *            数据对象
	 * @param pageVo
	 *            分页对象
	 * @return
	 * @throws ParamException
	 */
	public static String getRetVal(InterfaceMessage msg,String transActionCode, String columns, List<?> list, PageVo pageVo)
			throws ParamException {
		Document doc = DocumentHelper.createDocument();
		Element resp = doc.addElement(KstHosConstant.RESP);
		XMLUtil.addElement(resp, KstHosConstant.TRANSACTIONCODE, transActionCode);
		XMLUtil.addElement(resp, KstHosConstant.RESPCODE, RetCode.Success.RET_10000.getCode());
		XMLUtil.addElement(resp, KstHosConstant.RESPMESSAGE, RetCode.Success.RET_10000.getMessage());
		String[] cols = columns.split(",");
		if (pageVo != null) {
			Element page = DocumentHelper.createElement(KstHosConstant.PAGE);
			XMLUtil.addElement(page, KstHosConstant.PINDEX, pageVo.getPIndex());
			XMLUtil.addElement(page, KstHosConstant.PSIZE, pageVo.getPSize());
			XMLUtil.addElement(page, KstHosConstant.PCOUNT, pageVo.getPCount());
			resp.add(page);
		}
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Element data = DocumentHelper.createElement(KstHosConstant.DATA);
				Object obj = list.get(i);
				Field[] ff = obj.getClass().getDeclaredFields();
				for (int j = 0; j < cols.length; j++) {
					String keyName = cols[j];
					boolean bb = false;
					for (int k = 0; k < ff.length; k++) {
						if (ff[k].isAnnotationPresent(RelativeXmlNode.class)) {
							RelativeXmlNode rxn = ff[k].getAnnotation(RelativeXmlNode.class);
							if (keyName.equalsIgnoreCase(rxn.XmlFiled())
									&& rxn.BeanFiled().equalsIgnoreCase(ff[k].getName())) {
								bb = true;
								XMLUtil.addElement(data, rxn.XmlFiled(),
										ReflectionUtils.getFieldValue(obj, ff[k].getName()));
								break;
							}
						} else {
							if (keyName.equalsIgnoreCase(ff[k].getName())) {
								bb = true;
								XMLUtil.addElement(data, keyName, ReflectionUtils.getFieldValue(obj, ff[k].getName()));
								break;
							}
						}
					}
					if (!bb) {
						XMLUtil.addElement(data, keyName, "");
					}
				}
				resp.add(data);
			}
		}
		return doc.asXML();
	}

	/**
	 * 返回前端XML
	 *
	 * @param outType
	 * @param code
	 * @param message
	 * @return
	 * @author 無
	 * @date 2018年4月24日 下午3:25:41
	 */
	public static String getRetVal(InterfaceMessage msg,int outType, int code, String message) {
		if (outType == 0) {
			JSONObject json = new JSONObject();
			json.put("RespCode", code);
			json.put("RespMessage", message);
			return json.toString();
		} else {
			return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<Resp><RespCode>" + code + "</RespCode><RespMessage>"
					+ message + "</RespMessage></Resp>";
		}
	}
	
	public static String getRetVal(int outType, int code, String message) {
		return getRetVal(null, outType, code, message);
	}

	/**
	 * 返回前端XML
	 * 
	 * @throws ParamException
	 */
	public static String getRetValUseResp(String transActionCode, Integer code, String msg,
			Map<String, String> mapParam) {
		Document doc = DocumentHelper.createDocument();
		Element resp = doc.addElement(KstHosConstant.RESP);
		XMLUtil.addElement(resp, KstHosConstant.TRANSACTIONCODE, transActionCode);
		XMLUtil.addElement(resp, KstHosConstant.RESPCODE, code);
		XMLUtil.addElement(resp, KstHosConstant.RESPMESSAGE, msg);
		if (mapParam != null && mapParam.size() > 0) {
			Iterator<Map.Entry<String, String>> it = mapParam.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				XMLUtil.addElement(resp, entry.getKey(), entry.getValue());
			}
		}
		return doc.asXML();
	}

	/**
	 * 返回前端XML
	 * 
	 * @throws ParamException
	 */
	public static String getRetValUseRespV2(String transActionCode, Integer code, String msg,
			Map<String, String> mapParam) {
		Document doc = DocumentHelper.createDocument();
		Element resp = doc.addElement(KstHosConstant.RESP);
		XMLUtil.addElement(resp, KstHosConstant.TRANSACTIONCODE, transActionCode);
		XMLUtil.addElement(resp, KstHosConstant.RESPCODE, code);
		XMLUtil.addElement(resp, KstHosConstant.RESPMESSAGE, msg);
		Element data = resp.addElement(KstHosConstant.DATA);

		if (mapParam != null && mapParam.size() > 0) {
			Iterator<Map.Entry<String, String>> it = mapParam.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				XMLUtil.addElement(data, entry.getKey(), entry.getValue());
			}
		}
		return doc.asXML();
	}
	
	public static String getRetVal(InterfaceMessage msg,Integer respcode, String respMsg, JSONObject dataJson) {
		JSONObject json = new JSONObject();
		json.put("respCode", respcode);
		json.put("respMessage", respMsg);
		json.put("data", dataJson);
		return json.toString();
	}
	
	public static String getRetVal(Integer respcode, String respMsg, JSONObject dataJson) {
		return getRetVal(null,respcode, respMsg, dataJson);
	}

	public static String getRetVal(InterfaceMessage msg,Integer respcode, String respMsg, JSONArray jsonArray) {
		JSONObject json = new JSONObject();
		json.put("respCode", respcode);
		json.put("respMessage", respMsg);
		json.put("data", jsonArray);
		return json.toString();
	}

	public static String getRetVal(InterfaceMessage msg,Integer respcode, String respMsg, JSONArray jsonArray, JSONObject page) {
		JSONObject json = new JSONObject();
		json.put("respCode", respcode);
		json.put("respMessage", respMsg);
		json.put("data", jsonArray);
		json.put("page", page);
		return json.toString();
	}

	/**
	 * 删除文件夹及子文件
	 */
	public static boolean deleteAllFilesOfDir(File path) {
		if (!path.exists()) {
			return true;
		}
		if (path.isFile()) {
			return path.delete();
		}
		File[] files = path.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (!deleteAllFilesOfDir(files[i])) {
				return false;
			}
		}
		return path.delete();
	}

	/**
	 * 包个CDDATA
	 * 
	 * @param param
	 * @return
	 */
	public static String addCdData(String param) {
		return KstHosConstant.CDDATA_START + param + KstHosConstant.CDDATA_END;
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double div(String v1, String v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 两数相乘。
	 * 
	 * @return 两个参数的积
	 */
	public static double mul(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).doubleValue();
	}
	
	public static void main(String[] args) { 
//		String reqParam = "<Req><TransactionCode></TransactionCode><Data><CardType>{CardType}</CardType><CardNo>{CardNo}</CardNo></Data></Req>";
//		Map<String, String> map = new HashMap<String, String>(); 
//		map.put("CardType", "1");
//		map.put("CardNo", "A12345");
//		reqParam = CommonUtil.formateReqParam(reqParam, map);
//		System.out.println(reqParam);
		
		System.out.println(getUUIDYMD());
	}
	
	/**
	 * lsq 格式化入参
	 * 
	 * @param reqParam
	 * @param paramMap
	 * @return
	 */
	public static String formateReqParam(String reqParam, Map<String, String> paramMap) {
		StringBuffer strBuffer = new StringBuffer(reqParam);
		String regular = "(\\{[^\\}]*\\})";
		Pattern pattern = Pattern.compile(regular);
		Matcher m = pattern.matcher(strBuffer.toString());
		while (m.find()) {
			String regxStrMode = m.group();
			String regxStr = regxStrMode.substring(1, regxStrMode.length() - 1);
			if (paramMap.get(regxStr) != null && !StringUtil.isEmpty(paramMap.get(regxStr))) {
				strBuffer.replace(strBuffer.indexOf(regxStrMode) , strBuffer.indexOf(regxStrMode) + regxStrMode.length(), paramMap.get(regxStr));
			} else {
				strBuffer.replace(strBuffer.indexOf(regxStrMode) , strBuffer.indexOf(regxStrMode) + regxStrMode.length() , "");
			}
		}
		return strBuffer.toString();
	}
	
	/**
	 * 获取返回结果的RespCode
	 *
	 * @param respXml
	 * @return
	 * @throws AbsHosException
	 * @author 無
	 * @date 2018年4月24日 下午3:26:03
	 */
	public static Integer getRespCode(String respXml) throws AbsHosException {
		if (StringUtil.isEmpty(respXml)) {
			return RetCode.Common.ERROR_XMLERROR.getCode();
		}
		Document doc = XMLUtil.parseXml(respXml);
		Element root = doc.getRootElement();
		return XMLUtil.getInt(root, KstHosConstant.RESPCODE, false);
	}

	/**
	 * 获取返回结果的RespMessage
	 *
	 * @param respXml
	 * @return
	 * @throws AbsHosException
	 * @author 無
	 * @date 2018年4月24日 下午3:26:45
	 */
	public static String getRespMessage(String respXml) throws AbsHosException {
		if (StringUtil.isEmpty(respXml)) {
			return RetCode.Common.ERROR_XMLERROR.getMessage();
		}
		Document doc = XMLUtil.parseXml(respXml);
		Element root = doc.getRootElement();
		return XMLUtil.getString(root, KstHosConstant.RESPMESSAGE, false);
	}

	/**
	 * 将字符串转换为Double类型值
	 * 
	 * @param str
	 * @return
	 */
	public static Double getDouble(String str) {
		Double result = 0.0;
		try {
			if (str != null && !"".equals(str)) {
				result = Double.parseDouble(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获得页面列表数据
	 * 
	 * @param <T>
	 * @param dataList
	 *            列表数据
	 * @param pIndex
	 *            页码（第一页从0开始）
	 * @param pSize
	 *            每页大小
	 * @return
	 */
	public static <T> List<T> getPageList(List<T> dataList, PageVo page) {
		int pIndex = 0;
		int pSize = 0;
		List<T> list = null;
		if (dataList != null && dataList.size() > 0) {
			if (page != null) {
				pIndex = page.getPIndex();
				pSize = page.getPSize();
				page.setPCount(dataList.size());
			}

			if (pSize <= 0) {
				list = dataList;
			} else {
				if (dataList.size() > (pIndex + 1) * pSize) {
					// 当前页数最后一条数据在列表数据内
					list = dataList.subList(pIndex * pSize, (pIndex + 1) * pSize);
				} else if (dataList.size() > pIndex * pSize) {
					// 当前页数第一条数据在列表数据内，最后一条数据在列表数据外
					list = dataList.subList(pIndex * pSize, dataList.size());
				}
			}
		}
		return list;
	}

	/**
	 * 判断字符串是否为数字
	 * 
	 * @param str
	 *            字符串
	 * @return
	 */
	public static boolean isNumeric(String str) {
		boolean result = false;
		if (str != null && !"".equals(str)) {
			String regular = "[0-9]*";
			Pattern pattern = Pattern.compile(regular);
			result = pattern.matcher(str).matches();
		}
		return result;
	}

	/**
	 * 获得数字
	 * 
	 * @param str
	 * @return
	 */
	public static int getNumber(String str) {
		int result = -1;
		if (str != null && !"".equals(str) && !KstHosConstant.SYMBOL_RAIL.equals(str)) {
			str = str.replaceAll("[a-zA-Z]", "");
			if (CommonUtil.isNumeric(str)) {
				result = Integer.parseInt(str);
			}
		}
		return result;
	}

	/**
	 * 获得0-9,a-z,A-Z范围的随机数
	 * 
	 * @param length
	 *            随机数长度
	 * @return String
	 */
	public static String getRandomChar(int length) {
		char[] chr = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
				'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
				'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
				'Z' };

		Random random = new Random();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			buffer.append(chr[random.nextInt(62)]);
		}
		return buffer.toString();
	}

	/**
	 * 获取对象属性字符串（HosId,HosName）
	 * 
	 * @param o
	 * @return
	 */
	public static String getFiledName(Object o) {
		Field[] fields = o.getClass().getDeclaredFields();
		StringBuffer fieldNames = new StringBuffer(2048);
		for (int i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName();
			String first = fieldName.substring(0, 1).toUpperCase();
			String rest = fieldName.substring(1, fieldName.length());
			fieldName = new StringBuffer(first).append(rest).toString();
			fieldNames.append(fieldNames).append(fieldName).append(",");
		}
		if (StringUtil.isEmpty(fieldNames)) {
			fieldNames.append(fieldNames.substring(0, fieldNames.length() - 1));
		}
		return fieldNames.toString();
	}

	/**
	 * 获取性别值
	 *
	 * @param sex
	 * @return
	 * @author 無
	 * @date 2018年4月24日 下午3:27:25
	 */
	public static String getSex(String sex) {
		String newSex = "3";
		if (KstHosConstant.MAN.equals(sex)) {
			newSex = "1";
		} else if (KstHosConstant.WOMAN.equals(sex)) {
			newSex = "2";
		}
		return newSex;
	}

	/**
	 * 返回前端XML
	 *
	 * @param o
	 * @return
	 * @author 無
	 * @date 2018年4月24日 下午3:28:06
	 */
	public static String getRespParam(Object o) {
		StringBuffer sb = new StringBuffer();
		sb.append("<" + KstHosConstant.RESP + ">");
		sb.append(KstHosConstant.TRANSACTIONCODE + ",");
		sb.append(KstHosConstant.RESPCODE + ",");
		sb.append(KstHosConstant.RESPMESSAGE);
		sb.append(getFiledName(o));
		sb.append("</" + KstHosConstant.RESP + ">");
		return sb.toString();
	}

	/**
	 * json转XML
	 *
	 * @param json
	 * @return
	 * @author 無
	 * @date 2018年4月24日 下午3:28:39
	 */
	public static String json2XML(String json) {
		if (StringUtil.isEmpty(json)) {
			return "";
		} else {
			net.sf.json.JSONObject jobj = net.sf.json.JSONObject.fromObject(json);
			String xml = new XMLSerializer().write(jobj).replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "")
					.replace("<o>", "").replace("</o>", "");
			return xml;
		}
	}

	/**
	 * 字符串的日期格式的计算
	 * 
	 * @param smdate
	 *            较小日期,不填默认今天; bdate 较大日期,不填默认今天
	 * 
	 * @param formate
	 *            格式化,不填默认yyyy-MM-dd
	 */
	public static int daysBetween(String smdate, String bdate, String formate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(StringUtils.isBlank(formate) ? "yyyy-MM-dd" : formate);
		Calendar cal = Calendar.getInstance();

		cal.setTime(StringUtils.isBlank(smdate) ? new Date() : sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(StringUtils.isBlank(bdate) ? new Date() : sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long betweenDays = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(betweenDays));
	}
	
	
	public static String getMsgContent(String modeContent, Element data) {
		if (data == null) {
			return modeContent;
		}
		Iterator<?> iter = data.elementIterator();
		while (iter.hasNext()) {
			Element el = (Element) iter.next();
			String key = el.getName();
			String value = el.getTextTrim();
			String newKey = "<" + key + ">";
			if (modeContent.indexOf(newKey) != -1) {
				modeContent = modeContent.replaceAll(newKey, XMLUtil.ElementValue(value));
			}
		}
		if (modeContent.indexOf("<URL>") != -1) {//如果url没有传值
			modeContent = modeContent.replaceAll("<URL>", "");
		}
		return modeContent;
	}
	
	
	public static String replaceStr(String content, String str1, String str2) {
		if (content.indexOf(str1) != -1) {
			content = content.replaceAll(str1, str2);
		}
		return content;
	}
	
	
//	public static String getAuthInfo(String clientId) {
//		JSONObject authInfo = new JSONObject();
//		authInfo.put("ClientVersion", 1);
//		authInfo.put("ClientId", clientId);
//		authInfo.put("Sign", "Sign");
//		authInfo.put("SessionKey", "SessionKey");
//		return authInfo.toString();
//	}
}

package com.kasite.core.common.util;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.coreframework.util.StringUtil;
import com.kasite.core.common.exception.RRException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

/**
 * 数据处理工具类
 *
 * @author 無
 * @version V1.0
 * @date 2018年4月24日 下午3:29:48
 */
public class DataUtils {
	
	private static final String UNKNOWN = "unknown";
	 /**
     * String 转 org.dom4j.Document
     * @param xml
     * @return
     * @throws DocumentException
     */
    public static Document strToDocument(String xml) throws DocumentException {
        return DocumentHelper.parseText(xml);
    }

    /**
     * org.dom4j.Document 转  com.alibaba.fastjson.JSONObject
     * @param xml
     * @return
     * @throws DocumentException
     */
    public static com.alibaba.fastjson.JSONObject documentToJSONObject(String xml) throws DocumentException {
        return elementToJSONObject(strToDocument(xml).getRootElement());
    }

    /**
     * org.dom4j.Element 转  com.alibaba.fastjson.JSONObject
     * @param node
     * @return
     */
    @SuppressWarnings("unchecked")
	public static com.alibaba.fastjson.JSONObject elementToJSONObject(Element node) {
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        // 当前节点的名称、文本内容和属性
        List<Attribute> listAttr = node.attributes();// 当前节点的所有属性的list
        for (Attribute attr : listAttr) {// 遍历当前节点的所有属性
            result.put(attr.getName(), attr.getValue());
        }
        // 递归遍历当前节点所有的子节点
        List<Element> listElement = node.elements();// 所有一级子节点的list
        if (!listElement.isEmpty()) {
            for (Element e : listElement) {// 遍历所有一级子节点
                if (e.attributes().isEmpty() && e.elements().isEmpty()) // 判断一级节点是否有属性和子节点
                    result.put(e.getName(), e.getTextTrim());// 沒有则将当前节点作为上级节点的属性对待
                else {
                    if (!result.containsKey(e.getName())) // 判断父节点是否存在该一级节点名称的属性
                        result.put(e.getName(), new JSONArray());// 没有则创建
                    ((JSONArray) result.get(e.getName())).add(elementToJSONObject(e));// 将该一级节点放入该节点名称的属性对应的值中
                }
            }
        }
        return result;
    }

	public static JSONObject xml2JSONObject(String xml) {
		if (StringUtil.isBlank(xml)) {
			return new JSONObject();
		} else {
			return (JSONObject) new XMLSerializer().read(xml);
		}
	}
	
	public static String xml2JSON(String xml) {
		if (StringUtil.isBlank(xml)) {
			return "";
		} else {
			return new XMLSerializer().read(xml).toString();
		}
	}

	public static String json2XML(String json) {
		if (StringUtil.isBlank(json)) {
			return "";
		} else {
			try {
				net.sf.json.JSONObject jobj = net.sf.json.JSONObject.fromObject(json);
				String xml = new XMLSerializer().write(jobj).replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "")
						.replace("<o>", "").replace("</o>", "");
				return xml;
			}catch (Exception e) {
				throw new RRException("不是合法的JSON字符串无法解析："+json);
			}
		}
	}

	/**
	 * json转换成xml
	 * 
	 * @param json
	 *            json字符串
	 * @param encoding
	 *            编码
	 * @return
	 */
	public static String json2XML(String json, String encoding) {
		if (StringUtil.isBlank(json)) {
			return "";
		} else {
			if (StringUtils.isBlank(encoding)) {
				encoding = null;
			}
			net.sf.json.JSONObject jobj = net.sf.json.JSONObject.fromObject(json);
			String xml = new XMLSerializer().write(jobj, encoding)
					.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "").replace("<o>", "").replace("</o>", "");
			return xml;
		}
	}

	public static String jsonToXml(String jsonStr) {
		JSONObject json = JSONObject.fromObject(jsonStr);
		StringBuffer reqXml = new StringBuffer();
		reqXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		reqXml.append(getXml(json));
		String rt = reqXml.toString();
		return rt;
	}

	private static String getXml(JSONObject json) {
		StringBuffer sb = new StringBuffer();
		if (json != null && !json.isNullObject()) {
			Iterator<?> iter = json.keys();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				if (json.get(key) instanceof JSONObject) {
					sb.append("<").append(key).append(">");
					sb.append(getXml(json.getJSONObject(key)));
					sb.append("</").append(key).append(">");
				} else if (json.get(key) instanceof JSONArray) {
					JSONArray array = json.getJSONArray(key);
					if (array != null && array.size() > 0) {
						for (int i = 0; i < array.size(); i++) {
							sb.append("<").append(key).append(">");
							sb.append(getXml(array.getJSONObject(i)));
							sb.append("</").append(key).append(">");
						}
					}
				} else {
					String content = json.getString(key);
					// if(StringUtils.isNotBlank(content)){
					// content = "<![CDATA["+content+"]]>";//防止有特殊字符
					// }
					sb.append("<").append(key).append(">").append(content).append("</").append(key).append(">");
				}
			}
		}
		return sb.toString();
	}

	public static String jsonToXml2(String jsonStr) {
		JSONObject json = JSONObject.fromObject(jsonStr);
		StringBuffer reqXml = new StringBuffer();
		reqXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		reqXml.append(getXml2(json));
		String rt = reqXml.toString();
		return rt;
	}

	private static String getXml2(JSONObject json) {
		StringBuffer sb = new StringBuffer();
		if (json != null && !json.isNullObject()) {
			Iterator<?> iter = json.keys();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				if (json.get(key) instanceof JSONObject) {
					sb.append("<").append(key).append(">");
					sb.append(getXml2(json.getJSONObject(key)));
					sb.append("</").append(key).append(">");
				} else if (json.get(key) instanceof JSONArray) {
					JSONArray array = json.getJSONArray(key);
					if (array != null && array.size() > 0) {
						for (int i = 0; i < array.size(); i++) {
							sb.append("<").append(key).append(">");
							sb.append(getXml2(array.getJSONObject(i)));
							sb.append("</").append(key).append(">");
						}
					}
				} else {
					String content = json.getString(key);
					sb.append("<").append(key).append(">").append(content).append("</").append(key).append(">");
				}
			}
		}
		return sb.toString();
	}
	/**
	 * 获取ip
	 * @param request
	 * @return
	 */
	public static String getIp(HttpServletRequest request) {
		if (request == null) {
			return "";
		}
		String ip = request.getHeader("X-Requested-For");
		if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	public static void main(String[] args) throws Exception {

		// String productPrice = "21.05151";
		//
		// BigDecimal a = new BigDecimal(productPrice);
		// BigDecimal b = new BigDecimal(100);
		// productPrice = a.multiply(b).setScale(0,
		// BigDecimal.ROUND_HALF_UP).toString();
		// KasiteConfig.print("2015"+System.currentTimeMillis());
		// String finalmoney = "0";
		// BigDecimal a = new BigDecimal(0.01);
		// BigDecimal b = new BigDecimal(100);
		// finalmoney = a.multiply(b).setScale(0,
		// BigDecimal.ROUND_HALF_UP).toString();

		// KasiteConfig.print(finalmoney);

		// 32
		// ytGH 1435031721375 9935031740125

		// KasiteConfig.print(getNonceStr(15));
		// YYGH-1435031721375-
		// KasiteConfig.print( DateOper.formatDate(new Date(1435031721375l),
		// "yyyy-MM-dd hh:mm:ss"));
		// String id = "YYGH-"+new Date().getTime()+"-"+getNonceStr(13);

	}

}

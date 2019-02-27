package com.kasite.core.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;

import com.coreframework.util.StringPool;
import com.coreframework.util.StringUtil;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.PageVo;
import com.kasite.core.common.resp.ApiKey;
import com.yihu.hos.exception.AbsHosException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

public class XMLUtil {

	/**
	 * 把xml字符串转换成 Document对象。
	 * 
	 * @param xml
	 *            需要转换的xml字符串
	 * @return 返回Document对象
	 * @throws RRException
	 *             如果转换成Document对象异常的话抛出异常。
	 */
	public static Document parseXml(String xml) throws AbsHosException {
		try {
			xml = xml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
			if(null == xml || "".equals(xml)) {
				return null;
			}
			// 这是优先选择. 如果不允许 DTDs (doctypes) ,几乎可以阻止所有的XML实体攻击
	        String FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
	        SAXReader reader=new SAXReader();
	        reader.setFeature(FEATURE, true);
			InputStream in = new ByteArrayInputStream(xml.getBytes("UTF-8"));
			return reader.read(in);
//			return DocumentHelper.parseText(xml);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RRException("传入的 xml 不是标准的xml字符串，请检查字符串是否合法。");
		}  catch (SAXException e) {
			e.printStackTrace();
			throw new ParamException("解析的字符串无法转换成XML对象。"+e.getMessage());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new ParamException("解析的字符串无法转换成XML对象。"+e.getMessage());
		}
	}
	/**
	 * 转换成xml字符串
	 * 
	 * @param xmlDoc
	 *            需要解析的xml对象
	 * @throws AbsHosException 
	 */
	public static String toXML_UTF_8(org.dom4j.Document xmlDoc) throws AbsHosException {
		return toXML(xmlDoc, "UTF-8", true);
	}
	/**
	 * 转换成xml字符串
	 * 
	 * @param xmlDoc
	 *            需要解析的xml对象
	 * @throws AbsHosException 
	 */
	public static String toXML_GBK(org.dom4j.Document xmlDoc) throws AbsHosException {
		return toXML(xmlDoc, "GBK", true);
	}
	/**
	 * 转换成xml字符串
	 * 
	 * @param xmlDoc
	 *            需要解析的xml对象
	 * @param encoding
	 *            编码格式：UTF-8、GBK
	 * @param iscom
	 *            是否为紧凑型格式
	 * @return 修正完成后的xml字符串
	 * @throws AbsHosException 
	 */
	public static String toXML(org.dom4j.Document xmlDoc, String encoding,
			boolean iscom) throws AbsHosException {
		ByteArrayOutputStream byteRep = new ByteArrayOutputStream();
		OutputFormat format = null;
		if (iscom) {
			format = OutputFormat.createCompactFormat();// 紧凑型格式
		} else {
			format = OutputFormat.createPrettyPrint();// 缩减型格式
		}
		format.setEncoding(encoding);// 设置编码
		format.setTrimText(false);// 设置text中是否要删除其中多余的空格
		XMLWriter xw;
		try {
			xw = new XMLWriter(byteRep, format);
			xw.write(xmlDoc);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RRException(e,"传入的编码格式错误，请传入正确的编码。");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RRException(e,"文档转换成xml字符串时出错。"+xmlDoc.asXML());
		}
		return byteRep.toString();
	}/**
	 * 对节点Element 添加节点。
	 * @param e 需要添加的节点
	 * @param name 添加的节点的名称
	 * @param value 添加的内容
	 * <br/>
	 * Demo:
	 * 	  < Root > aaa < /Root >
	 * <br/>
	 *  call-->  addChildElement(root, "A", "a");
	 *  <br/>
	 *  result--> < Root >< A >a< /A >< /Root >
	 */
	public static void addElement(Element e,ApiKey name,Object value){
		addElement(e, name.getName(), value);
	}
	/**
	 * 对节点Element 添加节点。
	 * @param e 需要添加的节点
	 * @param name 添加的节点的名称
	 * @param value 添加的内容
	 * <br/>
	 * Demo:
	 * 	  < Root > aaa < /Root >
	 * <br/>
	 *  call-->  addChildElement(root, "A", "a");
	 *  <br/>
	 *  result--> < Root >< A >a< /A >< /Root >
	 */
	public static void addElement(Element e,String name,Object value){
		if(isBlank(value)){
			e.addElement(name).addText("");
		}else{
			e.addElement(name).addText(value.toString());
		}
	}
	/**
	 * 判断对象是否为空！(null,"", "null")
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
	 * @param obj
	 * @return
	 */
	public static boolean isNotBlank(Object obj) {
		return !isBlank(obj);
	}
	/**
	 * 判断对象是否为空！(null,"", "null")
	 * @param obj
	 * @return
	 */
	public static boolean isBlank(Object obj) {
		if (obj == null) {
			return true;
		}
		if (obj instanceof String) {
			return isBlank((String) obj);
		}else {
			return isBlank(obj.toString());
		}
	}
	public static void addElement(Element e, String name, Integer value) {
		Element current = e.addElement(name);
		if (value != null) {
			current.setText(Integer.toString(value));
		}
	}

	/**
	 * 添加CDATA 类型节点
	 * @param e
	 * @param name
	 * @param value
	 */
	public static void addCDATAElement(Element e, String name, String value) {
		Element current = e.addElement(name);
		if (value != null) {
			current.addCDATA(value.trim());
		}
	}
	/**
	 * 添加CDATA 类型节点
	 * @param e
	 * @param name
	 * @param value
	 */
	public static void addCDATAElement(Element e, String name, Integer value) {
		Element current = e.addElement(name);
		if (value != null) {
			current.addCDATA(value.toString());
		}
	}
	/**获取节点中的整数
	 * @throws AbsHosException */
	public static  Integer  getInt(Element e,String name,Integer defaultVal) throws AbsHosException{
		if(e != null) {
			Element  current=e.element(name);
			if(current==null||current.getText()==null || "".equals(current.getText().trim()) || current.getText().length()<=0){
				return defaultVal;
			}
			Integer i = defaultVal;
			try{
				i =Integer.parseInt(current.getTextTrim());
			}catch (NumberFormatException e1) {
				
			}
			return i;
		}
		return defaultVal;
	}
	/**获取节点中的整数
	 * @throws AbsHosException */
	public static  Integer  getInt(Element e,String name,boolean isMust,Integer defval) throws AbsHosException{
		Element current = e.element(name);
		if (current == null || current.getText() == null || "".equals(current.getText().trim())
				|| current.getText().length() <= 0) {
			if (isMust) {
				throw new RRException("在 $" + e.asXML() + "$中获取节点：" + name + " 的值为空。");
			}
			return defval;
		}
		Integer i = defval;
		try {
			i = Integer.parseInt(current.getTextTrim());
		} catch (NumberFormatException e1) {
			if (isMust) {
				throw new RRException("在 $" + e.asXML() + "$中获取节点：" + name + " 的值不是整形。");
			}
		}
		return i;
	}
	/**获取节点中的整数
	 * @throws AbsHosException */
	public static  int  getInt(Element e,String name,boolean isMust) throws AbsHosException{
		Element  current=e.element(name);
		
		
		if(current==null||current.getText()==null || "".equals(current.getText().trim()) || current.getText().length()<=0)
		{
			if(isMust){
				throw new RRException("在 $"+ e.asXML() +"$中获取节点："+name +" 的值为空。");
			}
			return 0;
		}
		Integer i = 0;
		try{
			i =Integer.parseInt(current.getTextTrim());
		}catch (NumberFormatException e1) {
			i = 0;
			if(isMust){
				throw new RRException("在 $"+ e.asXML() +"$中获取节点："+name +" 的值不是整形。");
			}
		}
		return i;
	}
	/**获取节点中的整数
	 * @throws AbsHosException */
	public static  int  getInt(Element e,String name,int def) throws AbsHosException
	{
		Element  current=e.element(name);
		
		
		if(current==null||current.getText()==null || "".equals(current.getText().trim()) || current.getText().length()<=0)
		{
			return def;
		}
		Integer i = 0;
		try{
			i =Integer.parseInt(current.getTextTrim());
		}catch (NumberFormatException e1) {
			i = def;
		}
		return i;
	}
	/**获取节点中的整数
	 * @throws AbsHosException */
	public static long  getLong(Element e,String name,boolean isMust) throws AbsHosException{
		Element  current = e.element(name);
		if(current==null || StringUtil.isBlank(current.getTextTrim())){
			if(isMust){
				throw new RRException("在 $"+ e.asXML() +"$中获取节点："+name +" 的值为空。");
			}
		}else {
			try{
				return Long.parseLong(current.getTextTrim());
			}catch (NumberFormatException e1) {
				if(isMust){
					throw new RRException("在 $"+ e.asXML() +"$中获取节点："+name +" 的值不是整形。");
				}
			}
		}
		return 0L;
	}
	/**获取节点中的字符串
	 * @throws AbsHosException */
	public static  String  getString(Element e,String name,boolean isMust) throws AbsHosException{
		return getString(e, name, isMust,null,0);
	}
	/**
	 * 获取节点中的字符串
	 * @param e 需要获取的节点
	 * @param name 节点名称
	 * @param isMust 是否必须存在
	 * @param length 存在的情况下长度必须小于这个数字
	 * @return
	 * @throws AbsHosException
	 */
	public static  String  getString(Element e,String name,boolean isMust,int length) throws AbsHosException{
		return getString(e, name, isMust,null,length);
	}/**
	 * 获取节点中的字符串
	 * @param e
	 * @param name
	 * @param isMust	是否必填 true 必填  false非必填
	 * @param defVal	默认值
	 * @return
	 * @throws AbsHosException
	 */
	public static  String  getString(Element e,String name,boolean isMust,String defVal) throws AbsHosException{
		return getString(e, name, isMust,defVal,0);
	}
	/**
	 * 获取节点中的字符串
	 * @param e
	 * @param name
	 * @param isMust	是否必填 true 必填  false非必填
	 * @param defVal	默认值
	 * @return
	 * @throws AbsHosException
	 */
	public static  String  getString(Element e,String name,boolean isMust,String defVal,int length) throws AbsHosException{
		Element  current=e.element(name);
		if(current==null||current.getText()==null || StringUtil.isBlank(current.getText().trim()))
		{
			if(isMust){
				throw new RRException("在 $"+ e.asXML() +"$中获取节点："+name +" 的值为空。");
			}
			return defVal;
		}
		String retVal = current.getTextTrim();
		if(length>0 && retVal.length() > length) {
			throw new RRException(MessageFormat.format("在节点：{0} 的长度不能超过:{1} 。",name,length))  ;
		}
		return retVal;
	}
	
	public static String ElementStringValue(String str) {
		if (str != null) {
			return str;
		} else {
			return "";
		}
	}

	public static Integer ElementIntegerValue(Integer str) {
		if (str != null) {
			return str;
		} else {
			return 0;
		}
	}
	
	public static String ElementValue(String str) {
		if (str != null && !str.equalsIgnoreCase("null")) {
			return str;
		} else {
			return "";
		}
	}

	public static String xml2JSON(String xml) 
	{
		if(StringUtil.isBlank(xml)){
			return "";
		}else{
			return new XMLSerializer().read(xml).toString();
		}
	}
	
	public static String json2XML(String json)
	{
		if(StringUtil.isBlank(json)){
			return "";
		}else{
			net.sf.json.JSONObject jobj = net.sf.json.JSONObject.fromObject(json);
			String xml = new XMLSerializer().write(jobj).replace(
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "").replace(
					"<o>", "").replace("</o>", "");
			return xml;
		}
	}
	public static String jsonToXml(String jsonStr){
		JSONObject json = JSONObject.fromObject(jsonStr);
		StringBuffer reqXml =  new StringBuffer();
		reqXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		reqXml.append(GetXml(json));
		String rt = reqXml.toString();
		return rt;
	}
	
	private static String GetXml(JSONObject json){
		StringBuffer sb = new StringBuffer();
		if(json!=null && !json.isNullObject()){
			Iterator<?> iter =  json.keys();
			while(iter.hasNext()){
				String key  = (String) iter.next();
				if(json.get(key) instanceof JSONObject){
					sb.append("<").append(key).append(">");
					sb.append(GetXml(json.getJSONObject(key)));
					sb.append("</").append(key).append(">");
				}else if(json.get(key) instanceof JSONArray){
					JSONArray array = json.getJSONArray(key);
					if(array!=null && array.size()>0){
						for(int i=0;i<array.size();i++){
							sb.append("<").append(key).append(">");
							sb.append(GetXml(array.getJSONObject(i)));
							sb.append("</").append(key).append(">");
						}
					}
				}else{
					sb.append("<").append(key).append(">").append(json.getString(key)).append("</").append(key).append(">");
				}
			}
		}
		return sb.toString();
	}
	
	public static void main(String[] args) throws Exception {
		
//		Document doc = parseXml("<Root></Root>");
//		Element root = doc.getRootElement();
//		addElement(root, "A", "阿萨德飞");
//		addElement(root, "A", 2);
//		KasiteConfig.print("添加子节点：\r\n"+toXML(doc, "GBK", true));
//		addCDATAElement(root, "B", "所发生的");
//		KasiteConfig.print("添加 CDATA 子节点：\r\n"+toXML(doc, "UTF-8", true));
		
//		String xml = "<Resp><TransactionCode></TransactionCode><RespCode>10000</RespCode><RespMessage>医生查询信息成功</RespMessage><Data><DeptCode>286</DeptCode><DoctorName>陈久毅</DoctorName><Spec>在骨科方面有较高的造诣，率先于我科开展人工髋、膝关节置换手术，擅长人工髋、膝关节置换，关节外科，中西医结合治疗骨折、骨肿瘤、先天性畸形、各种软组织损伤，对四肢骨骨折闭合复位独树一帜。</Spec><DoctorTitle>主任医师</DoctorTitle><DoctorCode>164</DoctorCode><DeptName>贵州省骨伤医院</DeptName></Data><Data><DeptCode>286</DeptCode><DoctorName>田民</DoctorName><Spec>骨与关节疾病、创伤骨科、脊柱外科。</Spec><DoctorTitle>主任医师</DoctorTitle><DoctorCode>266</DoctorCode><DeptName>贵州省骨伤医院</DeptName></Data><Data><DeptCode>286</DeptCode><DoctorName>王松</DoctorName><Spec>善长运用AO技术、中西医结合治疗各种骨折；各种关节疾患、脊柱骨病的治疗；小儿骨科和关节镜微创技术，在创伤矫形、关节外科、脊柱外科有较深的认识并作出了一些成绩。</Spec><DoctorTitle>主任医师</DoctorTitle><DoctorCode>169</DoctorCode><DeptName>贵州省骨伤医院</DeptName></Data><Data><DeptCode>286</DeptCode><DoctorName>张开伟</DoctorName><Spec>擅长人工关节置换、关节镜及其它骨科微创手术。</Spec><DoctorTitle>主任医师</DoctorTitle><DoctorCode>174</DoctorCode><DeptName>贵州省骨伤医院</DeptName></Data><Data><DeptCode>286</DeptCode><DoctorName>祝乾清</DoctorName><Spec>擅长手法骨折复位、骨科微创手术。</Spec><DoctorTitle>主任医师</DoctorTitle><DoctorCode>168</DoctorCode><DeptName>贵州省骨伤医院</DeptName></Data><Data><DeptCode>286</DeptCode><DoctorName>方明智</DoctorName><Spec>骨与关节疾病、创伤骨科、脊柱外科。</Spec><DoctorTitle>副主任医师</DoctorTitle><DoctorCode>176</DoctorCode><DeptName>贵州省骨伤医院</DeptName></Data><Data><DeptCode>286</DeptCode><DoctorName>李贵华</DoctorName><Spec>创伤康复、颈肩腰腿痛、骨性关节炎及骨质疏松症的中西医治疗。</Spec><DoctorTitle>副主任医师</DoctorTitle><DoctorCode>172</DoctorCode><DeptName>贵州省骨伤医院</DeptName></Data><Data><DeptCode>286</DeptCode><DoctorName>李玉雄</DoctorName><Spec>在股骨头坏死、骨髓炎、骨结核、外伤性感染等病的中西医结合治疗颇具特色。</Spec><DoctorTitle>副主任医师</DoctorTitle><DoctorCode>171</DoctorCode><DeptName>贵州省骨伤医院</DeptName></Data><Data><DeptCode>286</DeptCode><DoctorName>沈俊</DoctorName><Spec>擅长于治疗各种创伤、颈椎病、股骨头缺血坏死、骨质疏松症、骨关节炎等骨科疾病以及小儿骨科领域的疾患。</Spec><DoctorTitle>副主任医师</DoctorTitle><DoctorCode>173</DoctorCode><DeptName>贵州省骨伤医院</DeptName></Data><Data><DeptCode>286</DeptCode><DoctorName>周建鸿</DoctorName><Spec>擅长脊柱关节疾病、颈椎前后路手术、腰椎间盘突出症、腰椎管狭窄以及脊柱肿瘤的手术治疗。</Spec><DoctorTitle>副主任医师</DoctorTitle><DoctorCode>4545</DoctorCode><DeptName>贵州省骨伤医院</DeptName></Data></Resp>";
//		KasiteConfig.print(formatXML(parseXml(xml)));
//		String ss = "[{\"hospNum\":\"4000XXXXX\",\"medicalNum\":\"15XXXXX\",\"billNum\":\"417XXXX\",\"settleSerialNum\":\"2018XXXXXX00000000XXX\",\"userId\":\"322XXXX\",\"name\":\"邓XXXX\",\"personType\":\"\",\"medicalType\":\"12\",\"cenTradeNum\":\"20171201165532-200000042-9664\",\"medicalCost\":\"25XXXX\",\"commercialInsuranceCost\":\"0.00\",\"sbFundPay\":\"0.00\",\"personalAmount\":\"0.00\",\"accumCompAmount\":\"0.00\",\"insuranceHospitalAccumulatePayment\":\"0.00\",\"residualAmount\":\"0.00\",\"indemnitySign\":\"0\",\"nonImmediateReason\":\"\",\"waiverReason\":\"\",\"waiverSign\":\"\",\"calculateInformation\":\"\",\"settleDate\":\"2018XXXX000000\",\"remark\":\"\",\"promptInformation\":\"\",\"policyFileName\":\"\",\"recipeList\":[{\"recipeNum\":\"231212XXXX\",\"recipeSerialNum\":\"100XXXXX\",\"money\":\"40.0000\",\"commercialInvoice\":\"\",\"commercialLevel\":\"\",\"commercialSelfpercent\":\"\",\"commercialSelfCareAmount\":\"\",\"commercialSelfAmount\":\"\",\"commercialLimitedPrice\":\"\",\"remark\":\"\",\"promptInformation\":\"\"}]}]";
//		Document doc = XMLUtil.parseJSONObject2Xml(com.alibaba.fastjson.JSONObject.parseObject(ss));
//		String ss = "[{'o':'112233','sss':'ccc','qq':[{'q':'3522'},{'q':'3522454'}],'QQQ':{'sss':'sss','ddd':'dddd'}},{'o':'112233','sss':'ccc','qq':[{'q':'3522'},{'q':'3522454'}],'QQQ':{'sss':'sss','ddd':'dddd'}}]";
//		Document doc = XMLUtil.parseJSONArray2Xml(com.alibaba.fastjson.JSONArray.parseArray(ss),"composite=Data_1,recipeList=Data_2",null);
//		KasiteConfig.print(doc.asXML());
		
		
	}
	public static String formatXML(Document doc) throws Exception {
        StringWriter out=null;
        try{
            OutputFormat formate=OutputFormat.createPrettyPrint();
            out=new StringWriter();
            XMLWriter writer=new XMLWriter(out,formate);
            writer.write(doc);
        } catch (IOException e){
           e.printStackTrace();
        } finally{
            out.close(); 
        }
        return out.toString();
	}
	
	/**
	 * 是否包含XML非法字符
	 * @param str
	 * @return
	 */
	public static boolean isContainIllegalCharacters(String str) {
		if(StringUtil.isBlank(str)) {
			return false;
		}else if(str.contains("&") || str.contains("<") || str.contains(">")) {
			return true;
		}
		return false;
	}
	
	  
    public static String getNode(String source,String start,String end){  
    	if(source==null) {
    		return "";
    	}
    	String rgex = start+"(.*?)"+end;
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式  
        Matcher m = pattern.matcher(source);  
        while(m.find()){  
            return m.group(1);  
        }  
        return "";  
    }  
    /**
	 * 将JSONObject 对象转换为Kasite标准的XML返回格式
	 * @Description: 
	 * @param json
	 * @param nodes			格式：regList=Data_1,OrderDetail=Data_2,     说明：将JSONObject中的regList节点转换为Data_1，OrderDetail节点转换为Data_2。
	 * @param callBack		回调：可进行值的转换，例如：JSONObject中 Sex=0,表示未知性别。转换成XML时，需要将Sex=0的值转换为 Sex=3，则可以实现该接口进行转换
	 * @return
	 */
	public static Document parseJSONObject2Xml(com.alibaba.fastjson.JSONObject json,String nodes,IXMLCallBack callBack) {
		return parseJSONObject2Xml(KstHosConstant.DEFAULTTRAN, 10000, "成功", null, json,nodes,callBack);
	}
	/**
	 * 将JSONObject 对象转换为Kasite标准的XML返回格式
	 * @Description: 
	 * @param transactionCode
	 * @param code
	 * @param message
	 * @param page
	 * @param json
	 * @param nodes			格式：regList=Data_1,OrderDetail=Data_2,     说明：将JSONObject中的regList节点转换为Data_1，OrderDetail节点转换为Data_2。
	 * @param callBack		回调：可进行值的转换，例如：JSONObject中 Sex=0,表示未知性别。转换成XML时，需要将Sex=0的值转换为 Sex=3，则可以实现该接口进行转换
	 * @return
	 */
	public static Document parseJSONObject2Xml(String transactionCode,int code,String message,PageVo page,com.alibaba.fastjson.JSONObject json,String nodes,IXMLCallBack callBack) {
		Document doc = DocumentHelper.createDocument();
		Element resp = doc.addElement(KstHosConstant.RESP);
		XMLUtil.addElement(resp, KstHosConstant.TRANSACTIONCODE, transactionCode);
		XMLUtil.addElement(resp, KstHosConstant.RESPCODE, code);
		XMLUtil.addElement(resp, KstHosConstant.RESPMESSAGE, message);
		if(page!=null) {
			Element pageElement = resp.addElement(KstHosConstant.PAGE);
			XMLUtil.addElement(pageElement, KstHosConstant.PINDEX, page.getPIndex());
			XMLUtil.addElement(pageElement, KstHosConstant.PSIZE, page.getPSize());
			XMLUtil.addElement(pageElement, KstHosConstant.PCOUNT, page.getPCount());
		}
		Element data = resp.element(KstHosConstant.DATA);
		parseJSONObject2Xml(data,json,nodes,callBack);
		return doc;
	}
	/**
	 *  将JSONObject 对象转换为Kasite标准的XML返回格式
	 * @Description: 
	 * @param data
	 * @param json
	 * @param nodes			格式：regList=Data_1,OrderDetail=Data_2,     说明：将JSONObject中的regList节点转换为Data_1，OrderDetail节点转换为Data_2。
	 * @param callBack		回调：可进行值的转换，例如：JSONObject中 Sex=0,表示未知性别。转换成XML时，需要将Sex=0的值转换为 Sex=3，则可以实现该接口进行转换
	 */
	private static void parseJSONObject2Xml(Element data,com.alibaba.fastjson.JSONObject json,String nodes,IXMLCallBack callBack) {
		if(nodes!=null && !nodes.endsWith(",")) {
			nodes = nodes+",";
		}
		for(Entry<String, Object> entry : json.entrySet()) {
			String key = entry.getKey();
			Object obj = entry.getValue();
			if(obj instanceof com.alibaba.fastjson.JSONObject) {
				//递归
				String node = getNode(nodes, key+"=", ",");
				if(StringUtil.isBlank(node)) {
					continue;
				}
				Element el = data.addElement(node);
				parseJSONObject2Xml(el, (com.alibaba.fastjson.JSONObject)obj,nodes,callBack);
			}else if(obj instanceof com.alibaba.fastjson.JSONArray) {
				String node = getNode(nodes, key+"=", ",");
				if(StringUtil.isBlank(node)) {
					continue;
				}
				com.alibaba.fastjson.JSONArray array = (com.alibaba.fastjson.JSONArray)obj;
				for (int i=0;i<array.size();i++) {
					Element el = data.addElement(node);
					parseJSONObject2Xml(el, array.getJSONObject(i),nodes,callBack);
				}
			}else {
				//直接添加值
				if(callBack!=null) {
					String value = callBack.parseXmlValue(key, obj.toString());
					if(isContainIllegalCharacters(value)) {
						addCDATAElement(data, key, value);
					}else {
						addElement(data, key, value);
					}
				}else {
					if(isContainIllegalCharacters(obj.toString())) {
						addCDATAElement(data, key, obj.toString());
					}else {
						addElement(data, key, obj.toString());
					}
				}
			}
		}
	}
	/**
	 * 将JSONArray 对象转换为Kasite标准的XML返回格式
	 * @Description: 
	 * @param array
	 * @param nodes			格式：regList=Data_1,OrderDetail=Data_2,     说明：将JSONArray中的regList节点转换为Data_1，OrderDetail节点转换为Data_2。
	 * @param callBack		回调：可进行值的转换，例如：JSONArray中 Sex=0,表示未知性别。转换成XML时，需要将Sex=0的值转换为 Sex=3，则可以实现该接口进行转换
	 * @return
	 */
	public static Document parseJSONArray2Xml(com.alibaba.fastjson.JSONArray array,String nodes,IXMLCallBack callBack) {
		return parseJSONArray2Xml(KstHosConstant.DEFAULTTRAN, 10000, "成功", null, array,nodes,callBack);
	}
	/**
	 * 将JSONArray 对象转换为Kasite标准的XML返回格式
	 * @Description: 
	 * @param code
	 * @param message
	 * @param array
	 * @param nodes			格式：regList=Data_1,OrderDetail=Data_2,     说明：将JSONArray中的regList节点转换为Data_1，OrderDetail节点转换为Data_2。
	 * @param callBack		回调：可进行值的转换，例如：JSONArray中 Sex=0,表示未知性别。转换成XML时，需要将Sex=0的值转换为 Sex=3，则可以实现该接口进行转换
	 * @return
	 */
	public static Document parseJSONArray2Xml(int code,String message,com.alibaba.fastjson.JSONArray array,String nodes,IXMLCallBack callBack) {
		return parseJSONArray2Xml(KstHosConstant.DEFAULTTRAN, code, message, null, array,nodes,callBack);
	}
	/**
	 * 将JSONArray 对象转换为Kasite标准的XML返回格式
	 * @Description: 
	 * @param code
	 * @param message
	 * @param page			分页对象
	 * @param array
	 * @param nodes			将JSONArray 对象转换为Kasite标准的XML返回格式格式：regList=Data_1,OrderDetail=Data_2,     说明：将JSONArray中的regList节点转换为Data_1，OrderDetail节点转换为Data_2。
	 * @param callBack		回调：可进行值的转换，例如：JSONArray中 Sex=0,表示未知性别。转换成XML时，需要将Sex=0的值转换为 Sex=3，则可以实现该接口进行转换
	 * @return
	 */
	public static Document parseJSONArray2Xml(int code,String message,PageVo page,com.alibaba.fastjson.JSONArray array,String nodes,IXMLCallBack callBack) {
		return parseJSONArray2Xml(KstHosConstant.DEFAULTTRAN, code, message, page, array,nodes,callBack);
	}
	/**
	 * 将JSONArray 对象转换为Kasite标准的XML返回格式
	 * @Description: 
	 * @param transactionCode
	 * @param code
	 * @param message		
	 * @param page			分页对象
	 * @param array
	 * @param nodes			将JSONArray 对象转换为Kasite标准的XML返回格式格式：regList=Data_1,OrderDetail=Data_2,     说明：将JSONArray中的regList节点转换为Data_1，OrderDetail节点转换为Data_2。
	 * @param callBack		回调：可进行值的转换，例如：JSONArray中 Sex=0,表示未知性别。转换成XML时，需要将Sex=0的值转换为 Sex=3，则可以实现该接口进行转换
	 * @return
	 */
	public static Document parseJSONArray2Xml(String transactionCode,int code,String message,PageVo page,com.alibaba.fastjson.JSONArray array,String nodes,IXMLCallBack callBack) {
		Document doc = DocumentHelper.createDocument();
		Element resp = doc.addElement(KstHosConstant.RESP);
		XMLUtil.addElement(resp, KstHosConstant.TRANSACTIONCODE, transactionCode);
		XMLUtil.addElement(resp, KstHosConstant.RESPCODE, code);
		XMLUtil.addElement(resp, KstHosConstant.RESPMESSAGE, message);
		if(page!=null) {
			Element pageElement = resp.addElement(KstHosConstant.PAGE);
			XMLUtil.addElement(pageElement, KstHosConstant.PINDEX, page.getPIndex());
			XMLUtil.addElement(pageElement, KstHosConstant.PSIZE, page.getPSize());
			XMLUtil.addElement(pageElement, KstHosConstant.PCOUNT, page.getPCount());
		}
		for (int i=0;i<array.size();i++) {
			Element data = resp.addElement(KstHosConstant.DATA);
			com.alibaba.fastjson.JSONObject json = array.getJSONObject(i);
			parseJSONObject2Xml(data, json,nodes,callBack);
		}
		return doc;
	}
	
	/**
	 * 将Data节点转换为List对象
	 * @Description: 
	 * @param root	
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> parse2BeanList(Element root,Class<T> clazz)throws Exception {
		Iterator<?> it = root.elementIterator(KstHosConstant.DATA);
		List<T> list = new ArrayList<T>();
		while(it!=null && it.hasNext()) {
			Element data = (Element) it.next();
			T t = XMLUtil.parse2Bean(data, clazz);
			list.add(t);
		}
		return list;
	}
	/**
	 * XML转换成对象。
	 * 支持java.util.List泛型，对象的属性名称和XML节点名称除首字母大小写可以不同外，其他必须一致
	 * （会先将XML节点首字母转成小写获取，没有获取到时再直接使用XML节点获取）
	 * 
	 * @Description: 
	 * @param data
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> T parse2Bean(Element data, Class<T> clazz) throws Exception {
		T bean = clazz.newInstance();
		Iterator<?> it = data.elementIterator();
		while (it.hasNext()) {
			Element el = (Element) it.next();
			String name = el.getName();
			if (el.isTextOnly()) {
				ReflectionUtils.setFieldValueByFieldType(bean, el.getName(), el.getTextTrim());
			} else {
				Field field = null;
				try {
					field = clazz.getDeclaredField(firstCharLowerCase(name));
				}catch (NoSuchFieldException e) {
					field = clazz.getDeclaredField(firstCharUppercase(name));
				}
				if (field != null) {
					field.setAccessible(true);
					Class<?> fieldClazz = field.getType();
					if (fieldClazz.isAssignableFrom(List.class)) {
						// 如果是List泛型
						Type fGtype = field.getGenericType();
						if (fGtype != null) {
							if (fGtype instanceof ParameterizedType) {
								// 如果是泛型参数的类型 ,获取泛型参数类型
								ParameterizedType pt = (ParameterizedType) fGtype;
								Class<?> childClazz = (Class<?>) pt.getActualTypeArguments()[0];
								Iterator<?> childIt = data.elementIterator(name);
								List<Object> childList = new ArrayList<Object>();
								while (childIt.hasNext()) {
									Element childEl = (Element) childIt.next();
									Object obj = parse2Bean(childEl, childClazz);
									childList.add(obj);
								}
								//将解析的list 赋值给对象赋值
								field.set(bean, childList);
							}
						}
					}else {
						//非List类型，直接使用获取到的Type类型,进行递归解析
						Object obj = parse2Bean(el, fieldClazz);
						field.set(bean, obj);
					}
				}

			}
		}
		return bean;
	}
	private static String firstCharUppercase(String name) {
		if(Character.isLowerCase(name.charAt(0))) {
			return name.substring(0,1).toUpperCase()+name.substring(1);
		}else {
			return name;
		}
	}
	private static String firstCharLowerCase(String name) {
		if(Character.isUpperCase(name.charAt(0))) {
			return name.substring(0,1).toLowerCase()+name.substring(1);
		}else {
			return name;
		}
	}
	/**
	 * 对象转换为XML
	 * @Description: 
	 * @param data
	 * @param bean
	 * @param nodes
	 * @param uppercase	首字母转大写  true是  false 否
	 * @throws Exception
	 */
	public static void parse2Xml(Element data,Object bean,String nodes,boolean uppercase) throws Exception {
		Class<?> clazz = bean.getClass();
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				String name = field.getName();
				String nodeName = name;
				if(uppercase) {
					nodeName = firstCharUppercase(name);
				}
				Object obj = ReflectionUtils.getFieldValue(bean, name);
				if(String.class.isAssignableFrom(field.getType())) {
					if(obj!=null) {
						if(isContainIllegalCharacters(obj.toString())) {
							addCDATAElement(data, nodeName, obj.toString());
						}else {
							addElement(data, nodeName, obj);
						}
					}else {
						addElement(data, nodeName, "");
					}
				}else if(Integer.class.isAssignableFrom(field.getType())
						 || Long.class.isAssignableFrom(field.getType())
						 || Float.class.isAssignableFrom(field.getType())
						 || Double.class.isAssignableFrom(field.getType())) {
					addElement(data, nodeName, obj);
				}else if(java.sql.Date.class.isAssignableFrom(field.getType())) {
					if(obj!=null) {
						addElement(data, nodeName, DateOper.formatDate(((java.sql.Date)obj), "yyyy-MM-dd"));
					}else {
						addElement(data, nodeName, "");
					}
				}else if(java.sql.Timestamp.class.isAssignableFrom(field.getType())) {
					if(obj!=null) {
						addElement(data, nodeName, DateOper.formatDate(((java.sql.Timestamp)obj), "yyyy-MM-dd HH:mm:ss"));
					}else {
						addElement(data, nodeName, "");
					}
				}else if(Date.class.isAssignableFrom(field.getType())) {
					if(obj!=null) {
						addElement(data, nodeName, DateOper.formatDate(((Date)obj), "yyyy-MM-dd HH:mm:ss"));
					}else {
						addElement(data, nodeName, "");
					}
				}else {
					if(nodes!=null && !nodes.endsWith(",")) {
						nodes = nodes+",";
					}
					//对象内部嵌套对象时
					String node = getNode(nodes, name+"=", ",");
					if(StringUtil.isBlank(node)) {
						continue;
					}
					Element n = data.addElement(node);
					parse2Xml(n, obj, nodes,uppercase);
				}
			}
		}
	}
}

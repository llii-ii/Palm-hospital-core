package com.kasite.client.crawler.modules.upload.datacloud;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.json.JSONArray;
import com.common.json.JSONObject;
import com.coreframework.util.JsonUtil;
import com.coreframework.util.StringUtil;
import com.kasite.client.crawler.config.Convent;
import com.kasite.client.crawler.config.data.Data1_5Bus;
import com.kasite.client.crawler.config.data.DictBus1_5;
import com.kasite.client.crawler.config.data.vo.Data15PkVo;
import com.kasite.client.crawler.modules.upload.datacloud.vo.DataVo;
import com.kasite.client.crawler.modules.utils.FileOper;
import com.kasite.core.common.util.DateOper;

import oracle.sql.BLOB;
import oracle.sql.CLOB;

public class DataCloudFileUtil {
	private static final Logger logger = LoggerFactory.getLogger(DataCloudFileUtil.class);

	/**
	 * 结构化文档
	 * @param jsonObjt
	 * @param name
	 * @param type
	 * @param isZkPackage
	 */
	private static void write(JSONObject jsonObjt,String name,DataType type,boolean isZkPackage) {
		String dataFileDir = Convent.getDataDir();
		String fileName = UUID.randomUUID().toString().replaceAll("-", "");
		StringBuffer filePath = new StringBuffer(dataFileDir);
		filePath.append(File.separator)
		.append(name)
		.append(Convent.TEMPDATAFILENAME)
		.append(File.separator);
		if(isZkPackage) {
			fileName = "events";
		}else {
			filePath.append(type);
		}
		File f = new File(filePath.toString());
		if(!f.exists()) {
			f.mkdirs();
		}
		filePath.append(File.separator)
		.append(fileName)
		.append(Convent.DATAFILESE);
		//写入文件。替换掉空值符号“-”
		String str = jsonObjt.toString().replace("\"-\"", "\"\"");
		FileOper.write(filePath.toString(),str);
	}
	
	/**
	 * 写入文件-非结构化文档
	 * 
	 * @param jsonObjt json文件的内容
	 * @param dirName UUID主目录名
	 * @param strDate  String文档内容
	 * @param byteDate  byte文档内容
	 * @param fileName 文档文件名
	 * @param documentType 1=结构化文档、2=BLOB ZIP byte XML、3=文件地址  jpg、pdf 4=String XML、
	 * 5=ZIP Base64 XML 和 String XML 混合、6=BLOB byte XML text/xml、7=CLOB Text String text/plain
	 * 8=CLOB XML String text/xml
	 */
	private static void writeUnstructured(String jsonObjt, String dirName, String content, byte[] byteDate,
			String fileName, Integer documentType) {
		String dataFileDir = Convent.getDataDir();
		StringBuffer filePath = new StringBuffer(dataFileDir);
		filePath.append(File.separator).append(dirName).append(Convent.TEMPDATAFILENAME).append(File.separator);
		String jsonPath = filePath.toString();
		filePath.append(DataType.documents);
		// 创建documents目录
		File f = new File(filePath.toString());
		if (!f.exists()) {
			f.mkdirs();
		}
		filePath.append(File.separator).append(fileName);
		/** documents目录下写入document文件 */
		try {
			/** 1=结构化文档、2=ZIP byte XML、3=文件地址  jpg、pdf 4=String XML、5=Base64 XML 和 String XML 混合 、6=String text/html */
			if (documentType == 2 || documentType == 6) {
				//byte[]
				OutputStream os = new FileOutputStream(filePath.toString());
				os.write(byteDate);
				os.flush();
				os.close();
			} else if (documentType == 4 || documentType == 7 || documentType == 8) {
				//文本
				FileOper.write(filePath.toString(), content);
			} else if (documentType == 5) {
				//Base64文本
				Files.write(Paths.get(filePath.toString()), Base64.getDecoder().decode(content),StandardOpenOption.CREATE);
			} else if (documentType == 3) {
				//下载文件
				if(Convent.getIsPrint()) {
					logger.info("url="+content);
				}
				downloadNet(content, filePath.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/** 写入document.json文件 */
		StringBuffer xmlPathsb = new StringBuffer(jsonPath);
		xmlPathsb.append(DataType.documents).append(Convent.DATAFILESE);
		FileOper.write(xmlPathsb.toString(), jsonObjt);
	}

	/**
	 * 下载网络文件
	 * @param urlString
	 * @throws Exception
	 */
	public static void downloadNet(String urlString, String filePath) throws Exception {
		int bytesum = 0;
		int byteread = 0;
		try {
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			InputStream inStream = conn.getInputStream();
			FileOutputStream fs = new FileOutputStream(filePath);
			byte[] buffer = new byte[1204];
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				fs.write(buffer, 0, byteread);
			}
			fs.close();
			logger.info("文件下载完成！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 质控包数据上传
	 * @param json
	 * @param name
	 * @throws Exception
	 */
	public static void writeControlPackage(JSONObject json,String name,boolean isZkPackage) throws Exception {
		JsonUtil.getJsonString(json, "inner_version",true);
		JsonUtil.getJsonString(json, "org_code",true);
		JsonUtil.getJsonString(json, "create_date",true);
		JsonUtil.getJsonString(json, "code",true);
		//写入文件夹
		write(json, name, DataType.origin,isZkPackage);
	}
	
	/**
	 * documents.json文件路径
	 * @param dirName
	 * @return
	 */
	public static String getJsonFilePath(String dirName){
		String dataFileDir = Convent.getDataDir();
		StringBuffer filePath = new StringBuffer(dataFileDir);
		filePath.append(File.separator).append(dirName).append(Convent.TEMPDATAFILENAME).append(File.separator);
		filePath.append(DataType.documents).append(Convent.DATAFILESE);
		return filePath.toString();
	}
	
	/**
	 * 非结构化文档写入文件夹
	 * @param json 头部
	 * @param obj 主表数据
	 * @param tableName 主表名
	 * @param otherTableName 副表名
	 * @param documentType 1=结构化文档、2=BLOB ZIP byte XML、3=文件地址  jpg、pdf 4=String XML、
	 * 5=Base64 XML 和 String XML 混合、6=BLOB XML byte text/xml、7=CLOB Text String text/plain、
	 * 8=CLOB XML String text/xml
	 * @param dirName 非结构化目录
	 * @throws Exception 
	 */
	public static void writeDocument(JSONObject json,JSONObject obj,String tableName ,String otherTableName,Integer documentType, String dirName) throws Exception {
		//获取已存在的documents.json的路径
		String jsonPath = getJsonFilePath(dirName);
//		System.out.println("documents.json.path="+jsonPath);
		//获取已存在的documents.json的内容
		String result = FileOper.read(jsonPath);
//		System.out.println("documents.json.result="+result);
		
		JSONObject headJson = new JSONObject();
		JSONArray filesArr = new JSONArray();
		JSONObject filesObj = new JSONObject();
		JSONArray contentArr = new JSONArray();
		JSONObject contentObj;
		
		//documents.json是否存在、获取file数组
		if(StringUtil.isNotBlank(result)){
			headJson = new JSONObject(result);
			filesArr = headJson.getJSONArray("files");
		}else{
			String inner_version = JsonUtil.getJsonString(json, "inner_version",true);
			String patient_id = JsonUtil.getJsonString(json, "patient_id",true);
			String event_no = JsonUtil.getJsonString(json, "event_no",true);
			String org_code = JsonUtil.getJsonString(json, "org_code",true);
			String event_time = JsonUtil.getJsonString(json, "event_time",true);
			String create_date = JsonUtil.getJsonString(json, "create_date",true);
			headJson.put("inner_version", inner_version);
			headJson.put("patient_id", patient_id);
			headJson.put("event_no", event_no);
			headJson.put("event_type",  "2");
			headJson.put("org_code", org_code);
			headJson.put("event_time", event_time);
			headJson.put("create_date", create_date);
			//false覆盖数据  true更新数据
			//headJson.put("re_upload_flg", "false");
		}
		//cdaDocId
		String cdaDocId = JsonUtil.getJsonString(json, "code",true);
		
		filesObj.put("cda_doc_id", cdaDocId);
		filesObj.put("content", contentArr);
		filesArr.put(filesObj);
		//新文件加入文件数组
		headJson.put("files", filesArr);
		
		JSONArray data = json.getJSONArray("data");
		if (null != data && data.length() > 0) {
			for (int i = 0; i < data.length(); i++) {
				json = data.getJSONObject(i);
				contentObj = new JSONObject();
				try {
					// 文件名
					String fileName = UUID.randomUUID().toString().replaceAll("-", "");
					String mimeType = "";
					String content = null;
					byte[] zipDate = null;
					// 获取文档类型
					if (documentType == 2) {
						fileName = fileName + Convent.XML_ZIP_FILESE;
						mimeType = "application/zip";
						BLOB blob = (BLOB) json.get("data");
						zipDate = blob.getBytes((long) 1, (int) blob.length());
					} else if (documentType == 5) {
						content = json.getString("data");
						if (content.startsWith("<")) {
							fileName = fileName + Convent.XML_FILESE;
							mimeType = "text/xml";
							documentType = 4;
						} else {
							fileName = fileName + Convent.XML_ZIP_FILESE;
							mimeType = "application/zip";
						}
					} else if (documentType == 4) {
						fileName = fileName + Convent.XML_FILESE;
						mimeType = "text/xml";
						content = json.getString("data");
					} else if (documentType == 3) {
						content = json.getString("data");
						if (content.indexOf(".jpg") != -1) {
							fileName = fileName + Convent.JPG_FILESE;
							mimeType = "application/jpeg";
						} else if (content.indexOf(".pdf") != -1) {
							fileName = fileName + Convent.PDF_FILESE;
							mimeType = "application/pdf";
						} else {

						}
					}else if (documentType == 6) {
						fileName = fileName + Convent.XML_FILESE;
						mimeType = "text/xml";
						BLOB blob = (BLOB) json.get("data");
						zipDate = blob.getBytes((long) 1, (int) blob.length());
					}else if (documentType == 7) {
						fileName = fileName + Convent.TEXT_FILESE;
						mimeType = "text/plain";
						CLOB clob = (CLOB) json.get("data");
						content = ClobToString(clob); 
					}else if (documentType == 8) {
						fileName = fileName + Convent.XML_FILESE;
						mimeType = "text/xml";
						CLOB clob = (CLOB) json.get("data");
						content = ClobToString(clob); 
					}
					// 身份证加入头部
					if (StringUtil.isNotBlank(JsonUtil.getJsonString(json, "hdsa00_01_017"))) {
						headJson.put("demographic_id", JsonUtil.getJsonString(json, "hdsa00_01_017"));
					}
					// 文件
					contentObj.put("emr_id", JsonUtil.getJsonString(json, "emr_id"));
					contentObj.put("emr_name", JsonUtil.getJsonString(json, "emr_name"));
					contentObj.put("mime_type", mimeType);
					contentObj.put("name", fileName);
					contentArr.put(contentObj);
					String strData = headJson.toString();
					/* 替换掉“-” */
					strData = strData.replace("\"-\"", "\"\"");
					/** 写入非结构化文档 */
					// 非结构化的数据集 不能和标准数据集混在一起  必须单独一个文件夹
					writeUnstructured(strData, dirName, content, zipDate, fileName, documentType);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 非结构化文档写入文件夹
	 * @param json 头部
	 * @param obj 主表数据
	 * @param tableName 主表名
	 * @param otherTableName 副表名
	 * @param documentType 1=结构化文档、2=ZIP byte XML、3=文件地址  jpg、pdf 4=String XML、5=Base64 XML 和 String XML 混合
	 * @throws Exception 
	 */
	/*public static void writeDocument(JSONObject json,JSONObject obj,String tableName ,String otherTableName,Integer documentType) throws Exception {
		
		String inner_version = JsonUtil.getJsonString(json, "inner_version",true);
		String patient_id = JsonUtil.getJsonString(json, "patient_id",true);
		String event_no = JsonUtil.getJsonString(json, "event_no",true);
		String org_code = JsonUtil.getJsonString(json, "org_code",true);
		String event_time = JsonUtil.getJsonString(json, "event_time",true);
		String create_date = JsonUtil.getJsonString(json, "create_date",true);
		String cdaDocId = JsonUtil.getJsonString(json, "code",true);
		
		//规则获取 
		Map<String,Data15PkVo> ruleMap = Rule1_5Bus.getInstall().getData15Map("RULE");
		//获取副表规则
		Data15PkVo vo = ruleMap.get(otherTableName+"_event_no");
		//获取规则配置中副表的名称
		String subName = vo.getName();
//		//获取规则配置中副表的术语名 cda_doc_id
//		String cdaDocId = vo.getPrivateDictName();
		
		//添加头部信息
		JSONObject headJson = new JSONObject();
		headJson.put("inner_version", inner_version);
		headJson.put("patient_id", patient_id);
		headJson.put("event_no", event_no);
		headJson.put("event_type",  "2");
		headJson.put("org_code", org_code);
		headJson.put("event_time", event_time);
		headJson.put("create_date", create_date);
		//false覆盖数据  true更新数据
		headJson.put("re_upload_flg", "true");
		//加入患者基本信息
//		JSONObject patient = new JSONObject();
//		JSONArray patientArr = new JSONArray();
//		JSONObject patientObj = new JSONObject();
		
		//加入文件信息
		JSONArray filesArr = new JSONArray();
		JSONObject filesObj = new JSONObject();
		JSONArray contentArr = new JSONArray();
		JSONObject contentObj = new JSONObject();
		
		JSONArray data = json.getJSONArray("data");
		if(null != data && data.length() > 0) {
			for (int i = 0; i < data.length(); i++) {
				JSONObject standardJson = headJson;
				json = data.getJSONObject(i);
				//加入患者基本信息
				//姓名
				patientObj.put("HDSD00_01_002", JsonUtil.getJsonString(json, "hdsa00_01_009"));
				patientObj.put("HDSA00_01_009", JsonUtil.getJsonString(json, "hdsa00_01_009"));
				//性别
				patientObj.put("HDSA00_01_011", JsonUtil.getJsonString(json, "hdsa00_01_011"));
				//出身日期
				String birth = "";
				String birthDate = JsonUtil.getJsonString(json, "hdsa00_01_012");
				try {
					if (StringUtil.isNotBlank(birthDate) && birthDate.length() >= 10) {
						if (birthDate.length() > 19) {
							birthDate = birthDate.substring(0, 19);
						}
						Date date = DateOper.parse(birthDate);
						SimpleDateFormat df2 = new SimpleDateFormat(Convent.TIMEFORMAT);
						birth = df2.format(date);
					} else {
						birth = "";
					}
				} catch (Exception e) {
					e.printStackTrace();
					birth = null;
				}
				patientObj.put("HDSA00_01_012", birth);
				//身份证
				String idCard = JsonUtil.getJsonString(json, "hdsa00_01_017");
				if(StringUtil.isBlank(idCard) || "-".equals(idCard)){
					idCard = "";
				}
				patientObj.put("HDSA00_01_017", idCard);
				patientArr.put(patientObj);
				patient.put("HDSA00_01", patientArr);
				standardJson.put("data_sets", patient);
				
				//身份证加入头部
				standardJson.put("demographic_id", JsonUtil.getJsonString(json, "hdsa00_01_017"));

				//文件
				String emrId = JsonUtil.getJsonString(json, "emr_id");
				if(StringUtil.isBlank(emrId) || "-".equals(emrId)){
					emrId = "";
				}
				contentObj.put("emr_id", emrId);
				String emrName = JsonUtil.getJsonString(json, "emr_name");
				if(StringUtil.isBlank(emrName) || "-".equals(emrName)){
					emrName = subName;
				}
				contentObj.put("emr_id", emrId);
				contentObj.put("emr_name", emrName);
				//替换
				contentObj.put("mime_type", "{mimeType}");
				contentObj.put("name", "{fileName}");
				contentArr.put(contentObj);
				filesObj.put("cda_doc_id", cdaDocId);
				filesObj.put("content", contentArr);
				filesArr.put(filesObj);
				standardJson.put("files", filesArr);
				String strData = standardJson.toString();
				//文件名
				String fileName = UUID.randomUUID().toString().replaceAll("-", "");
				//获取大数据
				String mimeType ="";
				String content = null;
				byte[] zipDate = null;
				try {
					*//**1=结构化文档、2=ZIP byte XML、3=文件地址  jpg、pdf 4=String XML、5=Base64 XML 和 String XML 混合 *//*
					if (documentType == 2) {
						fileName = fileName + Convent.XML_ZIP_FILESE;
						mimeType ="application/zip";
						BLOB blob = (BLOB) json.get("data");
						zipDate = blob.getBytes((long) 1, (int) blob.length());
					}else if (documentType == 5) {
						content = json.getString("data");
						if(content.startsWith("<")){
							fileName = fileName + Convent.XML_FILESE;
							mimeType ="text/xml";
							documentType = 4;
						}else{
							fileName = fileName + Convent.XML_ZIP_FILESE;
							mimeType ="application/zip";
						}
					}else if (documentType == 4) {
						fileName = fileName + Convent.XML_FILESE;
						mimeType ="text/xml";
						content = json.getString("data");
					}else if (documentType == 3) {
						content = json.getString("data");
						if(Convent.getIsPrint()) {
							logger.info("content="+content);
						}
						if(content.indexOf(".jpg")!=-1){
							fileName = fileName + Convent.JPG_FILESE;
							mimeType ="application/jpeg";
						}else if(content.indexOf(".pdf")!=-1){
							fileName = fileName + Convent.PDF_FILESE;
							mimeType ="application/pdf";
						}else {
							
						}
					}
					strData=strData.replace("{fileName}", fileName);
					strData=strData.replace("{mimeType}", mimeType);
					替换掉“-”
					strData=strData.replace("\"-\"", "\"\"");
					*//**写入非结构化文档*//*	
					//非结构化的数据集 不能和标准数据集混在一起  必须单独一个文件夹
					String newDirName = UUID.randomUUID().toString();
					writeUnstructured(strData, newDirName, content, zipDate, fileName, documentType);
					//结束上传数据
					DataCloudFileUtil.writEnd(newDirName,false,2);
				} catch (Exception e) {
					e.printStackTrace(); 
				}
			}
		}
	}*/
	
	/**
	 * 文件写入文件夹
	 * @param json
	 * @param name
	 * @param 是否质控包
	 * @throws Exception 
	 */
	public static void write(JSONObject json,String name,boolean isZkPackage) throws Exception {
		
		String inner_version = JsonUtil.getJsonString(json, "inner_version",true);
		String patient_id = JsonUtil.getJsonString(json, "patient_id",true);
		String event_no = JsonUtil.getJsonString(json, "event_no",true);
		String org_code = JsonUtil.getJsonString(json, "org_code",true);
		String event_time = JsonUtil.getJsonString(json, "event_time",true);
		String create_date = JsonUtil.getJsonString(json, "create_date",true);
		String code = JsonUtil.getJsonString(json, "code",true);
		
		DataVo standardVo = new DataVo(code, create_date, event_time, patient_id);
		standardVo.setOrg_code(org_code);
		standardVo.setEvent_no(event_no);
		standardVo.setInner_version(inner_version);
		
		JSONObject standardJson = new JSONObject();
		standardJson.put("inner_version", inner_version);
		standardJson.put("patient_id", patient_id);
		standardJson.put("event_no", event_no);
		standardJson.put("org_code", org_code);
		standardJson.put("event_time", event_time);
		standardJson.put("create_date", create_date);
		standardJson.put("code", code);
		
		JSONArray data = json.getJSONArray("data");
		JSONArray standardData = new JSONArray();
		if(null != data && data.length() > 0) {
			Map<String, Data15PkVo> map = Data1_5Bus.getInstall().getData15Map(code);
			if(null == map) {
				logger.error("未找到对应的表结构描述："+code);
				return;
			}
			for (int i = 0; i < data.length(); i++) {
				JSONObject jsonObj = (JSONObject) data.get(i);
				JSONObject standardDataJson = new JSONObject();
				for (Map.Entry<String, Data15PkVo> entity : map.entrySet()) {
					Data15PkVo vo = entity.getValue();
					String privateName = vo.getPrivateName();
					String value = JsonUtil.getJsonString(jsonObj, privateName.toLowerCase());
					/**病人ID 特殊处理  跟头部patient_id保持一致*/
					if("JDSA00_01_001".equals(privateName)){
						value = patient_id;
					}
					if(null != value) {
						value = value.trim();
					}
					/**空值处理*/
					if("-".equals(value) || "_".equals(value) || "null".equals(value) || "##".equals(value) || "—".equals(value)) {
						value = "";
					}
					/**费用清单 数量<=0的排除 */
					if("HDSD00_70".equals(code) || "HDSD00_67".equals(code)){
						if("JDSD00_70_006".equals(privateName) || "JDSD00_67_006".equals(privateName)){
							if(StringUtil.isNotBlank(value)){
								try {
									String num = value;
									if("0".equals(value) || "0.0".equals(value) || "0.00".equals(value) || value.indexOf("-")!=-1 || Float.parseFloat(num)<=0){
										standardDataJson = null;
										break;
									}
								} catch (Exception e) {
									standardDataJson = null;
									logger.error(e.getMessage());
									break;
								}
							}
						}
					}
					/**兼容旧医院  人口学建档时间 为空时 赋值为event_time*/
					if("HDSA00_01".equals(code) && "JDSA00_01_020".equals(privateName)){
						if(StringUtil.isNotBlank(value)){
							value = event_time;
						}
					}
					Integer isMust = vo.getIsNotNum();// 0 表示不能为空 1表示可以为空
					String des = vo.getPrivateDes();
//					Integer isKey = vo.getIsKey();//0.表示非主键 1.表示主键
					String privateType = vo.getPrivateType();
					String dataType = vo.getDataType();
					String dictName = vo.getPrivateDictName();
					//数据校验规范-- 如果数据是必填的医院返回没有值 则从标准中剔除这个 如果是字典值 并且不在字典表里的数据都直接剔除此字段。不保存到标准集合中
					if (StringUtil.isNotBlank(value)) {
						try {
							// 日期类型转义 D、DT 统一类型  与文档描述不同
							if (StringUtil.isNotBlank(dataType)) {
								if (dataType.trim().equalsIgnoreCase("D")) {
									if (StringUtil.isNotBlank(value)) {
										if (value.length() > 19) {
											value = value.substring(0, 19);
										}
										Date date = DateOper.parse(value);
										SimpleDateFormat df2 = new SimpleDateFormat(Convent.TIMEFORMAT);
										value = df2.format(date);
									} else {
										value = "";
									}
								}
								if (dataType.trim().equalsIgnoreCase("DT")) {
									if (StringUtil.isNotBlank(value)) {
										if (value.length() > 19) {
											value = value.substring(0, 19);
										}
										Date date = DateOper.parse(value);
										SimpleDateFormat df2 = new SimpleDateFormat(Convent.TIMEFORMAT);
										value = df2.format(date);
									} else {
										value = "";
									}
								}
								if (dataType.trim().equalsIgnoreCase("T")) {
									if (StringUtil.isNotBlank(value)) {
										Date date = DateOper.parse(value);
										SimpleDateFormat df2 = new SimpleDateFormat(Convent.TIME_T_FORMAT);
										value = df2.format(date);
									} else {
										value = "";
									}
								}
							}
						} catch (Exception e) {
							writeLogDateIsError(standardVo, code, privateName, value, des);
							value = null;
						}
						
						if ((null != privateType && privateType.equalsIgnoreCase("number") || dataType.equalsIgnoreCase("N"))) {
							try {
								String num = value;
								Float.parseFloat(num);
							} catch (Exception e) {
								writeLogNumberIsError(standardVo, code, privateName, value, des);
								value = null;
							}
						}
						// 如果是字典值则 做字典值校验
						if (null != dictName && StringUtil.isNotBlank(dictName.trim())) {
							dictName = dictName.trim();
							/**视图中已适配的情况：判断是否是有效的字典code*/
							String dictValue = DictBus1_5.getInstall().getValue(code, privateName, dictName, value);
//							System.out.println(value+"="+dictValue);
							if (StringUtil.isNotBlank(dictValue)) {
								/**字典code有效*/
								standardDataJson.put(privateName, value);
							} else {
								/**未适配：根据value适配字典、获得字典code*/
								/**1、全匹配  或者有 【其他】选项*/
								String dictCode = DictBus1_5.getInstall().getValue2(code, privateName, dictName, value);
								if (StringUtil.isNotBlank(dictCode)) {
									standardDataJson.put(privateName, dictCode);
								}else{
									/** 记录无完全适配的值*/
									writeLogIsDictError(standardVo, code, privateName, dictName, value, des);
									try {
										/**2、模糊匹配 、取相似度最高*/
										dictCode = DictBus1_5.getInstall().getMaxSimilarityDictCode(code, privateName, dictName, value);
									} catch (Exception e) {
										dictCode = null;
										e.printStackTrace();
									}
									//System.out.println(value+"="+dictCode);
									if (StringUtil.isNotBlank(dictCode)) {
										standardDataJson.put(privateName, dictCode);
									} else {
										standardDataJson.put(privateName, "");
										writeLogIsDictError(standardVo, code, privateName, dictName, value, des);
									}
								}
							}
						}else {
							if (StringUtil.isNotBlank(value)) {
								standardDataJson.put(privateName, value);
							}
						}
					}else{
						/**字典项 空值*/
						if (null != dictName && StringUtil.isNotBlank(dictName.trim())) {
							/**取【其他】字典*/
							String dictCode = DictBus1_5.getInstall().getValueOther(dictName);
							if(StringUtil.isNotBlank(dictCode)){
								standardDataJson.put(privateName, dictCode);
							}else {
								standardDataJson.put(privateName, "");
							}
							/**如果这个对象是必传的但是没传需要做说明*/
						}else if(null != isMust && isMust.equals(0)) {
							standardDataJson.put(privateName, "");
							writeLogIsMust(standardVo, code, privateName, des);
						}
					}
				}
				//该条记录写入到标准的结果集合中
				if(null != standardDataJson){
					standardData.put(standardDataJson);
				}
			}
			standardJson.put("data", standardData);
			
			//写入文件夹
			write(json, name, DataType.origin,isZkPackage);
			write(standardJson, name, DataType.standard,isZkPackage);
		}
	}
	private static void writeerror(String logstr) {
		if(Convent.getIsCheck()) {
			logger.error(logstr);
		}
	}
	private  static void writeLogNumberIsError(DataVo dataVo,String table,String privateName,String value,String des) {
		String logstr = ("|数字类型错误|表名|"+ table +"|字段|"+ privateName +"|字段名|"+ des + "|字典||值|"+ value+"|事件号|"+ dataVo.getEvent_no());
		writeerror(logstr);
	}
	private  static void writeLogDateIsError(DataVo dataVo,String table,String privateName,String value,String des) {
		String logstr = ("|日期类型错误|表名|"+ table +"|字段|"+ privateName +"|字段名|"+ des + "|字典||值|"+ value+"|事件号|"+ dataVo.getEvent_no());
		writeerror(logstr);
	}
	private  static void writeLogIsMust(DataVo dataVo,String table,String privateName,String des) {
		String logstr =("|必传字段为空|表名|"+ table +"|字段|"+ privateName +"|字段名|"+ des +"|字典||值|"+"|事件号|"+ dataVo.getEvent_no());
		writeerror(logstr);
	}
	private static void writeLogIsDictError(DataVo dataVo,String table,String privateName,String dictName,String value,String des) {
		String logstr =("|值域超出范围|表名|"+ table +"|字段|"+ privateName +"|字段名|"+ des +"|字典|"+ dictName +"|值|"+ value+"|事件号|"+ dataVo.getEvent_no());
		writeerror(logstr);
	}
	
	/**
	 * 写入完成
	 * @param name
	 * @param isQcPackages 是否质控包
	 * @param packType 包类型 1=结构化 2=非结构化
	 */
	public static void writEnd(String name,boolean isQcPackages,Integer packType) {
		String dataFileDir = Convent.getDataDir();
		StringBuffer filePath = new StringBuffer(dataFileDir);
		filePath.append(File.separator)
		.append(name)
		.append(Convent.TEMPDATAFILENAME);
		File f = new File(filePath.toString());
		if(f.exists()) {
			String fileName = UUID.randomUUID().toString().replaceAll("-", "");
			if(isQcPackages) {//如果是质控包上传需要打上特殊标记传不同的接口
				fileName = fileName+Convent.ZKPACKAGE;
			}
			if(null!=packType && packType==2) {//如果是非结构化包上传需要打上特殊标记传不同的接口
				fileName = fileName+Convent.FJGPACKAGE;
			}
			StringBuffer newfilePath = new StringBuffer(dataFileDir);
			newfilePath.append(File.separator)
			.append(fileName);
			//重命名文件
			FileOper.moveFile(filePath.toString(), newfilePath.toString());
		}
		
	}
	
	// 将字Clob转成String类型  
    public static String ClobToString(Clob sc) throws SQLException, IOException {  
        String reString = "";  
        Reader is = sc.getCharacterStream();
        BufferedReader br = new BufferedReader(is);  
        String s = br.readLine();  
        StringBuffer sb = new StringBuffer();  
        while (s != null) {  
            sb.append(s);  
            s = br.readLine();  
        }  
        reString = sb.toString();  
        return reString;  
    }  
    
	public static void main(String[] args) throws ParseException {
//		String value ="2018-01-01 01:01:01";
//		if (StringUtil.isNotBlank(value)) {
//			if (value.length() > 19) {
//				value = value.substring(0, 19);
//			}
//			Date date = DateOper.parse(value);
//			SimpleDateFormat df2 = new SimpleDateFormat(Convent.TIMEFORMAT);
//			value = df2.format(date);
//			System.out.println(value);
//		}
		
		
		System.out.println("徐青照              ".trim()+"|");
//		DataCloudFileUtil.write(new JSONObject(), "123123", DataType.origin);
//		System.exit(0);
	}
}

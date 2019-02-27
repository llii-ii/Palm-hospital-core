package test.com.kasite.client.crawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Reader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Clob;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.commons.lang.time.DateFormatUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.coreframework.db.DB;
import com.coreframework.db.Sql;
import com.kasite.client.crawler.config.Convent;
import com.kasite.client.crawler.config.MyDatabaseEnum;
import com.kasite.client.crawler.config.data.DictBus1_5;
import com.kasite.client.crawler.modules.utils.FileOper;
import com.kasite.client.crawler.modules.utils.MD5;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;

public class Test {

	public static void main(String[] args) throws Exception {
		
//		String str = "{\"inner_version\":\"5b56ea0b673b\",\"code\":\"HDSA00_01\",\"data\":[{\"HDSB01_01_001\":\"\",\"HDSA00_01_009\":\"徐丽丽\",\"HDSD00_02_019\":\"99\",\"HDSA00_11_051\":\"江西省\",\"JDSA00_01_008\":\"江西省德兴市\",\"JDSA00_01_007\":\"334500\",\"JDSA00_01_006\":\"江西省德兴市\",\"JDSA00_01_005\":\"334200\",\"HDSA00_01_048\":\"5\",\"JDSA00_01_004\":\"361124\",\"HDSA00_01_003\":\"492270268\",\"JDSA00_01_003\":\"\",\"JDSA00_01_002\":\"江西省德兴市\",\"JDSA00_01_001\":\"00078138\",\"HDSA00_01_049\":\"3\",\"HDSD00_20_017\":\"\",\"JDSA00_01_020\":\"\",\"JDSA00_01_019\":\"\",\"HDSD00_02_005\":\"\",\"JDSA00_01_018\":\"\",\"HDSA00_01_013\":\"156\",\"HDSA00_01_035\":\"334500\",\"JDSA00_01_017\":\"\",\"HDSA00_01_012\":\"1986-07-05T00:00:00Z\",\"JDSA00_01_016\":\"\",\"HDSA00_01_015\":\"90\",\"JDSA00_01_015\":\"江西省德兴市\",\"HDSA00_01_014\":\"01\",\"JDSA00_01_014\":\"334200\",\"HDSA00_01_017\":\"\",\"HDSA00_01_039\":\"90\",\"JDSA00_01_013\":\"1\",\"HDSA00_01_016\":\"01\",\"HDSA00_01_038\":\"Y\",\"JDSA00_01_012\":\"江西省德兴市\",\"JDSA00_01_011\":\"334200\",\"HDSA00_01_050\":\"9\",\"HDSA00_01_011\":\"2\"},{\"HDSB01_01_001\":\"\",\"HDSA00_01_009\":\"胡梅贿\",\"HDSD00_02_019\":\"99\",\"JDSA00_01_008\":\"江西省德兴市\",\"JDSA00_01_007\":\"334500\",\"JDSA00_01_006\":\"江西省德兴市\",\"JDSA00_01_005\":\"334200\",\"HDSA00_01_048\":\"5\",\"JDSA00_01_004\":\"361124\",\"HDSA00_01_003\":\"492270268\",\"JDSA00_01_003\":\"\",\"JDSA00_01_001\":\"00078138\",\"HDSA00_01_049\":\"3\",\"HDSD00_20_017\":\"\",\"JDSA00_01_020\":\"\",\"JDSA00_01_019\":\"\",\"HDSD00_02_005\":\"\",\"JDSA00_01_018\":\"\",\"HDSA00_01_013\":\"156\",\"HDSA00_01_035\":\"334200\",\"JDSA00_01_017\":\"\",\"HDSA00_01_012\":\"1952-08-14T00:00:00Z\",\"JDSA00_01_016\":\"\",\"HDSA00_01_015\":\"90\",\"JDSA00_01_015\":\"江西省德兴市\",\"HDSA00_01_014\":\"01\",\"JDSA00_01_014\":\"334500\",\"HDSA00_01_017\":\"\",\"HDSA00_01_039\":\"90\",\"JDSA00_01_013\":\"1\",\"HDSA00_01_016\":\"01\",\"HDSA00_01_038\":\"Y\",\"JDSA00_01_012\":\"江西省德兴市\",\"JDSA00_01_011\":\"334200\",\"HDSA00_01_050\":\"9\",\"HDSA00_01_011\":\"2\"}],\"event_no\":\"00078138\",\"patient_id\":\"00078138\",\"create_date\":\"2018-08-27T11:03:52Z\",\"org_code\":\"492270268\",\"event_time\":\"2018-08-25T09:24:00Z\"}";
//		String str ="操作完上面几步后，即使你原来的字体里面没有显示Lucida Console这个字体，现在应该也能看到了。选择它！如果原来就有，可以选上它先试试，不行在执行上述步骤（这里补充：至少我本机需要CHCP 65001下，有朋友说不要）；";
//		FileOper.write("D:\\K_S_T\\CrawlerServer\\test3.json", str);
//		FileOper.write("C:\\Users\\無\\Desktop\\非结构化\\test2.json", str);
		
		
//		FileOutputStream outSTr = new FileOutputStream(new File("D:\\K_S_T\\CrawlerServer\\test5.json"));
//		BufferedOutputStream Buff = new BufferedOutputStream(outSTr);
//        Buff.write(str.getBytes("UTF-8"));
//        Buff.flush();
//        Buff.close();
        
//		Buff = new BufferedOutputStream(outSTr);
//		Buff.write(str.getBytes("UTF-8"));
//		Buff.flush();
//		Buff.close();
		
//		SpringApplication.run(ServerConsole.class, args);
		
//
//		Map<String,Data15PkVo> ruleMap = Data1_5Bus.getInstall().getData15Map("HDSA00_01");
//		for (String key : ruleMap.keySet()) {
//			Data15PkVo vo = ruleMap.get(key);
//			System.out.println(vo.getPrivateName());
//		}
		
		//遍历规则		
//		Map<String,Data15PkVo> ruleMap = Data1_5Bus.getInstall().getData15Map("RULE");
//		for (String key : ruleMap.keySet()) {
//			Data15PkVo vo = ruleMap.get(key);
//			System.out.println(vo.getPrivateName());
//		}
		
//		test();
//		testDict();
//		fileWrite();
		
//		OkHttpClient client = new OkHttpClient();
//		MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
//		RequestBody body = RequestBody.create(mediaType, "api=rhip.doctor.post&param=%7B%22doctor_json_data%22%3A%7B%22idCardNo%22%3A%22342221333122223426%22%2C%22code%22%3A%22CWS2%22%2C%22name%22%3A%22CW%22%7D%2C%22model%22%3A%7B%22orgCode%22%3A%22492240421%22%2C%22deptName%22%3A%22%E6%9C%AA%E5%88%86%E9%85%8D%22%7D%7D&appKey=u5GEgEuoyc&timestamp=2018-07-09T17%3A28%3A16%2B0800&v=1.0&sign=8cfc9cc7ad2c163ed51ec29dbd450e08");
//		Request request = new Request.Builder()
//		  .url("http://27.154.233.186:9999/api")
//		  .post(body)
//		  .addHeader("Content-Type", "application/x-www-form-urlencoded")
//		  .addHeader("Cache-Control", "no-cache")
//		  .addHeader("Postman-Token", "f9aede5b-7b74-4bfb-b7a9-75414dc059d1")
//		  .build();

//		Response response = client.newCall(request).execute();
//		System.out.println(response.body().string());
//		System.out.println(java.net.URLEncoder.encode("未分配", "UTF-8"));
//		if ("1".equals("1")) {
//			return;
//		}
		
//		JSONObject doctorJsonData = JSONObject
//				.fromObject("{\"code\":\"8974\",\"sex\":\"0\",\"idCardNo\":\"\",\"name\":\"王艺婷\",\"status\":\"1\"}");
//		JSONObject model = JSONObject.fromObject("{\"deptName\":\"未分配\",\"orgCode\":\"492270292\"}");
//		com.common.json.JSONObject param = new com.common.json.JSONObject();
//		try {
//			param.put("doctor_json_data", doctorJsonData);
//			param.put("model", model);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
//		System.out.println(java.net.URLEncoder.encode(param.toString(), "UTF-8"));

//		 uploadBasicInfo(param);

//		System.out.println("Convent.getPublicKey()="+Convent.getPublicKey());
		
		com.common.json.JSONObject param = new com.common.json.JSONObject("{\"doctor_json_data\":{\"code\":\"8974\",\"sex\":\"0\",\"idCardNo\":\"1243243254324324\",\"name\":\"王艺婷\",\"status\":\"1\"},\"model\":{\"deptName\":\"未分配\",\"orgCode\":\"492270292\"}}");
		
//		SoapResponseVo signResult = HttpRequestBus.create(Convent.getBasicUploadUrl().replace("api", "sign"), RequestType.post)
//				.contentType("application/x-www-form-urlencoded")
//				.addHttpParam("api", URLEncoder.encode("rhip.doctor.post", "UTF-8"))
//				.addHttpParam("param",URLEncoder.encode(param.toString(), "UTF-8"))
//				.addHttpParam("timestamp", URLEncoder.encode("2018-09-03T14:59:34+0800", "UTF-8"))
//				.addHttpParam("v", URLEncoder.encode("1.0", "UTF-8"))
//				.addHttpParam("appKey", URLEncoder.encode("u5GEgEuoyc", "UTF-8"))
//        		.send();
//		
//		System.out.println("signResult="+signResult.getResult());
//		String sign ="";
//		if(signResult.getCode() == HttpStatus.SC_OK){
//			if(StringUtil.isNotBlank(signResult.getResult()) && signResult.getResult().indexOf(",")!=-1){
//				sign = signResult.getResult().split(",")[1];
//				System.out.println(sign);
//			}
//		}
		
		uploadBasicInfo("rhip.doctor.post", param);

		// testDict();

		//查询字典 
		// Map<String, String> map = DictBus1_5.getInstall().getDictMap("CV07.10.005");
		// for (String k : map.keySet()) {
		// System.out.println(k +"="+map.get(k));
		// }
	}

	/**
	 * sign 签名生成 （ md5(secret + params拼接字符串 + secret) ）
	 *
	 * @return
	 */
	public static String signParam(TreeMap<String, String> paramMap, String secret) {
		Iterator<Map.Entry<String, String>> iterator = paramMap.entrySet().iterator();
		StringBuilder builder = new StringBuilder();
		builder.append(secret);
		while (iterator.hasNext()) {
			Map.Entry<String, String> next = iterator.next();
			String key = next.getKey();
			String value = next.getValue();
			builder.append(key);
			builder.append(value);
		}

		builder.append(secret);
		try {
			return MD5.hash(builder.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// 测试字典项 适配
	public static void testDict() {
		String value = "亚急性和慢性类鼻疽";
		// String value ="A02.100x002";
		String dictName = "ICD-10-CM";
		String code = "table";
		String privateName = "privateName";
		if (StringUtil.isNotBlank(dictName)) {
			/** 判断value是否有效的字典code */
			String dictValue = DictBus1_5.getInstall().getValue(code, privateName, dictName, value);
			if (StringUtil.isNotBlank(dictValue)) {
				/** 字典code有效 */
				System.out.println(value + "=" + dictValue);
			} else {
				String dictCode = DictBus1_5.getInstall().getValue2(code, privateName, dictName, value);
				if (StringUtil.isNotBlank(dictCode)) {
					System.out.println(value + "==" + dictCode);
				}else{
					/** 根据value适配字典的value，获得字典code */
					dictCode = DictBus1_5.getInstall().getMaxSimilarityDictCode(code, privateName, dictName, value);
					if (StringUtil.isNotBlank(dictCode)) {
						System.out.println(value + "===" + dictCode);
					}
				}
			}
		}
	}
	
	/**
	 * 上传医疗基础信息
	 *
	 * @return
	 * @author 無
	 * @throws Exception 
	 * @date 2018年6月26日 下午3:52:50
	 */
	public static SoapResponseVo uploadBasicInfo(String apiName,com.common.json.JSONObject param) throws Exception{
		String appKey = "u5GEgEuoyc";
		String secret = "MNhQXnCSaaRc8kcS";
		String timestamp = DateFormatUtils.format(new Date(), "yyyy-MM-dd'T'HH:mm:ssZ");
		String v = "1.0";
		//签名
//		timestamp = "2018-09-03T17:37:31+0800";
		TreeMap<String, String> paramMap = new TreeMap<>();
		paramMap.put("api", apiName);
		paramMap.put("appKey", appKey);
		paramMap.put("param", param.toString());
		paramMap.put("timestamp", timestamp);
		paramMap.put("v", v);
		String sign = MD5.signParam(paramMap, secret);
		//请求
		SoapResponseVo result = HttpRequestBus.create("http://27.154.233.186:9999/api", RequestType.post)
					.contentType("application/x-www-form-urlencoded")
					.addHttpParam("api", URLEncoder.encode(apiName, "UTF-8"))
					.addHttpParam("param",URLEncoder.encode(param.toString(), "UTF-8"))
					.addHttpParam("timestamp", URLEncoder.encode(timestamp, "UTF-8"))
					.addHttpParam("v", URLEncoder.encode(v, "UTF-8"))
					.addHttpParam("appKey", URLEncoder.encode(appKey, "UTF-8"))
					.addHttpParam("sign", URLEncoder.encode(sign, "UTF-8"))
	        		.send();
		System.out.println("url:http://27.154.233.186:9999/api");
		System.out.println("secret:"+secret);
		System.out.println("api:"+apiName);
		System.out.println("param:"+param);
		System.out.println("appKey:"+appKey);
		System.out.println("timestamp:"+timestamp);	
		System.out.println("v:"+v);	
		System.out.println("sign:"+sign);
		System.out.println("result:"+result.getResult());
		return result;
	}
	
//	public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/x-www-form-urlencoded");
	public static SoapResponseVo uploadBasicInfo(com.common.json.JSONObject param) throws Exception {
		String apiName = "rhip.doctor.post";
		String appKey = "u5GEgEuoyc";
		String timestamp = DateFormatUtils.format(new Date(), "yyyy-MM-dd'T'HH:mm:ssZ");
		
		// 签名
		TreeMap<String, String> paramMap = new TreeMap<>();
		paramMap.put("api", apiName);
		paramMap.put("param", param.toString());
		paramMap.put("timestamp", timestamp);
		paramMap.put("v", "1.0");
		paramMap.put("appKey", appKey);
		String sign = MD5.signParam(paramMap, "MNhQXnCSaaRc8kcS");
		
//	    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
	    
//	    FormBody.Builder formBody = new FormBody.Builder()
//	    		.add("api",apiName)
//			    .add("param",param.toString())
//			    .add("timestamp",timestamp)
//			    .add("v","1.0")
//			    .add("appKey",appKey);
//	    Request request = new Request.Builder()//创建Request 对象。
//	            .url("http://27.154.233.186:9999/api")
//	            .post(formBody.build())//传递请求体
//	            .addHeader("Content-Type", "application/x-www-form-urlencoded")
//	            .header("api",apiName)
//			    .header("param",param.toString())
//			    .header("timestamp",timestamp)
//			    .header("v","1.0")
//			    .header("appKey",appKey)
//	            .build();    
	    
	    String postBody = "api="+URLEncoder.encode(apiName, "UTF-8")
	    		+ "&param="+URLEncoder.encode(param.toString(), "UTF-8")
	    		+ "&appKey="+URLEncoder.encode("u5GEgEuoyc", "UTF-8")
	    		+ "&timestamp="+URLEncoder.encode(timestamp, "UTF-8")
	    		+ "&v="+URLEncoder.encode("1.0", "UTF-8")
	    		+ "&sign="+URLEncoder.encode(sign, "UTF-8");
	    System.out.println(postBody);
	    //OKHTTP
//        Request request = new Request.Builder()
//            .url("http://27.154.233.186:9999/api")
//            .post(RequestBody.create(MEDIA_TYPE_MARKDOWN,postBody))
//            .build();
//	    
//	    Response response = client.newCall(request).execute();
//	    if (!response.isSuccessful()) 
//	    	throw new IOException("Unexpected code " + response);
//	    
//	    System.out.println("result="+response.body().string());
	    
		
		SoapResponseVo result = HttpRequestBus.create("http://27.154.233.186:9999/api", RequestType.post)
				.contentType("application/x-www-form-urlencoded")
				//BODY
//				.setParam(postBody)
				//addHttpParam
				.addHttpParam("api", URLEncoder.encode(apiName, "UTF-8"))
				.addHttpParam("param",URLEncoder.encode(param.toString(), "UTF-8"))
				.addHttpParam("timestamp", URLEncoder.encode(timestamp, "UTF-8"))
				.addHttpParam("v", URLEncoder.encode("1.0", "UTF-8"))
				.addHttpParam("appKey", URLEncoder.encode(appKey, "UTF-8"))
				.addHttpParam("sign", URLEncoder.encode(sign, "UTF-8"))
				.send();
//		System.out.println("url="+Convent.getBasicUploadUrl());
		System.out.println("api="+apiName);
		System.out.println("param="+param);
		System.out.println("appKey="+appKey);
		System.out.println("timestamp="+timestamp);	
		System.out.println("v=1.0");	
		System.out.println("secret=MNhQXnCSaaRc8kcS");	
		System.out.println("sign="+sign);
		System.out.println("result="+result.getResult());
		System.out.println(URLDecoder.decode(result.getResult(),"UTF-8"));
		return null;
	}
	
	/**
	 * 大字段测试
	 * @throws Exception
	 */
	public static void test() throws Exception{
		com.common.json.JSONObject queryJson = DB.me().queryForJson(MyDatabaseEnum.hisdb, new Sql("select t.* from TEST t"));
		com.common.json.JSONArray arr = queryJson.getJSONArray("result");
		System.out.println(arr);
		com.common.json.JSONObject json = arr.getJSONObject(0);
		Clob clob = (Clob)json.get("name");
		Reader rd = clob.getCharacterStream();
        char [] str = new char[12];
        StringBuilder sb = new StringBuilder();
        while(rd.read(str) != -1) {
        	sb.append(new String(str));
        }
        System.out.println(sb);
        Document document = DocumentHelper.parseText(sb.toString()); 
        
        XMLWriter writer=new XMLWriter(new FileOutputStream("D:/test.xml"),OutputFormat.createPrettyPrint());
        writer.setEscapeText(false);//字符是否转义,默认true
        writer.write(document);
        writer.close();
	}
	
	public static void fileWrite(){
		String dirName = UUID.randomUUID().toString();
		String dataFileDir = "file/data";
		String fileName = UUID.randomUUID().toString().replaceAll("-", "");
		StringBuffer filePath = new StringBuffer(dataFileDir);
		filePath.append(File.separator)
		.append(dirName)
		.append(Convent.TEMPDATAFILENAME)
		.append(File.separator);
		
		String xmlPath=filePath.toString();
		filePath.append("documents");
//		//创建documents目录
		File f = new File(filePath.toString());
		if(!f.exists()) {
			f.mkdirs();
		}
		filePath.append(File.separator).append(fileName).append(Convent.XML_FILESE);
		String sb = "{\"demographic_id\":\"362321193909018911\",\"patient_id\":\"ee5f7a3f-7220-4a17-8874-6de00b446db3\",\"event_no\":\"261742\",\"event_type\":\"2\",\"org_code\":\"49229004X\",\"event_time\":\"2018-05-08 11:05:10\",\"create_date\":\"2018-05-08 11:05:10\",\"inner_version\":\"59083976eebd\",\"datasets\":{\"HDSA00_01\":[{\"HDSD00_01_002\":\"吴金庭\",\"HDSA00_01_011\":\"1\",\"HDSA00_01_012\":\"1939-09-01 00:00:00\",\"HDSA00_01_017\":\"362321193909018911\"}]},\"files\":[{\"cda_doc_id\":\"HSDC01.06\",\"content\":[{\"emr_id\":\"\\\\1001\\\\201805\\\\20180508\\\\E8F053C1-C965-43B6-853E-8FEC4D4A5D87\\\\E8F053C1-C965-43B6-853E-8FEC4D4A5D87.pdf\",\"emr_name\":\"心电图报告\"\"mime_type\":\"application/x-pdf\",\"name\":\"{name}\"}]}]}";
		//documents目录下写入document文件
		System.out.println(filePath.toString());
		FileOper.write(filePath.toString(), sb, "");
		
		//写入json文件
		StringBuffer xmlPathsb = new StringBuffer(xmlPath);
		xmlPathsb.append("documents").append(Convent.DATAFILESE);
		System.out.println(xmlPathsb.toString());
		FileOper.write(xmlPathsb.toString(), sb, "");
		
	}
}

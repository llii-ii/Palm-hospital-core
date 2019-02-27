package com.kasite.client.crawler.modules.patient.service;


import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClient.FailureListener;
import org.elasticsearch.client.RestClientBuilder;

import com.kasite.client.crawler.modules.utils.FileOper;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;

public class LowLevelTest {
	static final String INDEX = "test";
	static RestClient client = create();

	public static RestClient create() {
		/*
		 * 如果想要自动发现集群中的其它es服务器，需要添加 elasticsearch-rest-client-sniffer.jar
		 * 黑名单和自动重试都是RestClient实现的，与HttpClinet无关
		 * RestClient里面有个blacklist（黑名单），黑名单有截止时间。如果全部加入了黑名单，就随机选择一个使用。
		 * socket异常，或者code==502、503、504，RestClient会换host重试（同host是否重试，由httpClient决定）,直到超过maxRetryTimeoutMillis或所有可用路由重试完毕。参见RestClinet的retryIfPossible方法。
		 * 其它类型的异常，比如code==400、404之类的，就不会重试。
		 * 返回值的code<300认为成功，其它的performRequest()都会抛出异常
		 * 
		 */
		return RestClient.builder(
//				new HttpHost("172.18.20.75", 9200, "http")
				new HttpHost("127.0.0.1", 9200, "http")
//				,new HttpHost("127.0.0.2", 9200, "http")
//				,new HttpHost("172.18.21.75", 1111, "http")
				)
				.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
					@Override
					public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
						/*
						 *  设置socket超时时间,connectionRequestTimeout是从连接池获取连接的超时时间
						 *  根据源码可知，如果没有设置以下参数，restClient会采用默认参数，而不是httpclient默认的-1
						 */
						return requestConfigBuilder
								.setConnectTimeout(5000) //ip不可到达时，这个超时才有意义。如果ip可到达，只是port不可用，大概1s就能知道，这个时间没意义
								.setSocketTimeout(30000)
								.setConnectionRequestTimeout(1000);
					}
				}).setMaxRetryTimeoutMillis(60000) // 请求重试的最大时间，超过这个时间，就不再重试了。而不是指整个请求的超时时间（请求超过这个时间是有可能的）
				.setFailureListener(new FailureListener(){
					public void onFailure(HttpHost host) {
						System.out.println(host.toHostString()+"请求失败");
			        }
				})
				.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
					/*
					 * 这里可以设置几乎所有的HttpAsyncClient参数，包括连接池之类的.
					 * 参见RestClientBuilder.createHttpClient().
					 * 开启连接的地方见DefaultConnectingIOReactor
					 * 因为用的是NIO，connectionTime实际上是代码判断，SocketChannel没办法配置（soTimeout可以）。
					 * DefaultIOReactorConfig的配置属于连接池，RequestConfig属于HttpClinet。
					 * 所以RequestConfig会覆盖DefaultIOReactorConfig的配置
					 */
					@Override
					public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
						return httpClientBuilder
								.setDefaultIOReactorConfig(IOReactorConfig.custom()
										.setConnectTimeout(3000)//这个会被覆盖，没有用
										// 设置异步读取http返回值的线程数为2(执行AbstractIOReactor的线程)，默认是cpu的核心数
										.setIoThreadCount(2).build());
					}
				}).build();
	}

	private static void write(String fileName,String content) throws URISyntaxException {
		File f = new File(LowLevelTest.class.getResource("entity.txt").toURI());
		String f1 = f.getAbsolutePath();
		String f2 = f1.substring(0, f1.lastIndexOf("/"))+"/"+fileName;
		System.out.println(f2);
		FileOper.write(f2, content);
	}
	
	private static String read(String fileName) throws IOException, URISyntaxException {
		byte[] data = Files.readAllBytes(new File(LowLevelTest.class.getResource(fileName).toURI()).toPath());
		return new String(data, StandardCharsets.UTF_8);
	}

	private static void bulk() throws Exception {
		/*
		 * bulk.txt最后要有空行(\n) 如果都是一个文档的，endpoint就可以用{index}/{type}/_bulk
		 * 单个用PUT，批量要用POST
		 * bulk都是用\n作为分行的，但也支持\r\n
		 * 从BulkRequest.add()可以知道，delete方法只要1行，其它的都要2行(index、create、update)
		 */
		Header headers = new BasicHeader("Content-Type", "application/x-ndjson");
		Map<String, String> params = new HashMap<>();
		params.put("pretty", "true");// 可选
		params.put("type", "logtable");
		String txt = read("bulk.txt");
		HttpEntity entity = new NStringEntity(txt, ContentType.APPLICATION_JSON);
		try {
			Response resp = client.performRequest("POST", "_bulk", params, entity, headers);
			System.out.println("bulk response:  "+EntityUtils.toString(resp.getEntity()));
			Thread.sleep(2000);
		} catch (ResponseException e) {
			System.out.println("--"+e.getResponse());
			throw e;
		}

	}

	private static String simpleMsg(Throwable e){
		String msg="##############"+e.getClass().getName()+":"+e.getMessage();
		Throwable cause=e.getCause();
		if(cause!=null){
			msg+=",cause by "+cause.getClass().getName()+":"+cause.getMessage();
		}
		return msg;
	}
	
	public static String initIDC10(String fileName) throws IOException, URISyntaxException {
		String basepath=Thread.currentThread().getContextClassLoader().getResource("").toString();
		File file = new File(basepath+fileName);
		System.out.println(file.exists());
		byte[] data = Files.readAllBytes(new File(basepath+"/"+fileName).toPath());
		return new String(data, StandardCharsets.UTF_8);
	}
	
	public static void dict() throws IOException, Exception {
		String txt = read("dict.txt");
		String[] dicts = txt.split("\r\n");
		StringBuffer sbf = new StringBuffer();
		String type = "";
		for (String string : dicts) {
			String[] ss2 = string.split("\t");
			if(ss2.length == 1) {
				sbf.append("\r\n");
				type = ss2[0].trim();
				continue;
			}
			String s1 = ss2[0].trim();
			String s2 = ss2[1].trim();
			sbf.append("\r\n");
			sbf.append("<dic type=\""+type+"\" code=\""+s1+"\" value=\""+s2+"\"/>");
		}
		
		System.out.println(sbf);
	}
	
	public static void createESTableSql(String tableName) throws Exception, URISyntaxException {
		String txt = read("entity.txt");
		String[] dicts = txt.split("\n");
		StringBuffer sbf = new StringBuffer();
		for (String sss : dicts) {
			String[] ss = sss.split("\t");
			String type = "String";
			if(ss.length == 4) {
				type = ss[3];
			}
			sbf.append(" \t\t,\""+ss[1]+"\": {");
			sbf.append("\r\n");
			if(type.equals("Currency")) {
				sbf.append("\t\t\t\"type\": \"integer\"\r\n");
			}else if(type.equals("Date")) {
				sbf.append("\t\t\t\"format\": \"YYYY-MM-dd\",");
				sbf.append("\t\t\t\"type\": \"date\"\r\n");
				sbf.append("");
			}else if(type.equals("DateTime")) {
				sbf.append("\t\t\t\"format\": \"YYYY-MM-dd HH:mm:ss\",\r\n");
				sbf.append("\t\t\t\"type\": \"date\"\r\n");
			}else {
				sbf.append("\t\t\t\"type\": \"keyword\"\r\n");
			}
			sbf.append("\t\t}");
			sbf.append("\r\n");
		}
		
		String mode = read("dbmode");
		mode = mode.replaceAll("<TABLE>", sbf.toString());
		System.out.println(mode);
		write(tableName, mode);
		
	}

	public static void entity3() throws Exception, URISyntaxException {
		String txt = read("entity3.txt");
		String[] dicts = txt.split("\n");
		StringBuffer sbf = new StringBuffer();
		for (String sss : dicts) {
			String[] ss = sss.split("\t");
			String dis = ss[0];
			String pname = ss[1];
			String is = ss[2];
			String type = "String";
			if(ss.length > 3) {
				type = ss[3];
			}
			boolean isNotNull = false;
			if("是".equals(is)) {
				isNotNull = true;
			}
			if("Currency".equals(type)) {
				sbf.append("@CheckCurrency(message=\""+dis+" "+ pname +" 数据格式不正确\", groups = {AddGroup.class} , isNotNull ="+ isNotNull+")");
				sbf.append("\r\n");
			}
			if("DateTime".equals(type)) {
				sbf.append("@CheckDate(format=\"YYYY-MM-dd HH:mm:ss\", message=\""+dis+" "+ pname +" 数据格式不正确\", groups = {AddGroup.class}, isNotNull ="+ isNotNull+")");
				sbf.append("\r\n");
			}
			if("Date".equals(type)) {
				sbf.append("@CheckDate(message=\""+dis+" "+ pname +" 数据格式不正确\", groups = {AddGroup.class} , isNotNull ="+ isNotNull+")");
				sbf.append("\r\n");
			}
			if("字典".equals(type)) {
				sbf.append("@CheckDict(inf=CheckDictBuser.class,type=\""+pname+"\",isNotNull = "+isNotNull+",message=\""+dis +" “"+pname+"” 字典值不合法\",groups=AddGroup.class)\r\n");
			}
			sbf.append("/**"+dis+" "+ pname +" **/");
			sbf.append("\r\n");
			if("Currency".equals(type)) {
				sbf.append("private Integer "+ pname +";");
				sbf.append("\r\n");
			}else if("Date".equals(type)||"DateTime".equals(type) || "字典".equals(type)) {
				sbf.append("private String "+ pname +";");
				sbf.append("\r\n");
			}else{
				if(isNotNull) {
					sbf.append("@NotBlank(message=\""+dis+" "+ pname +" 不能为空\", groups = {AddGroup.class})");
					sbf.append("\r\n");
				}
				sbf.append("private String "+ pname +";");
				sbf.append("\r\n");
			}
		}
//		for (int i = 0; i < dicts.length; i++) {
//			sbf.append("@NotBlank(message=\""+dicts[i]+" "+dicts[i+1] +" 不能为空\", groups = {AddGroup.class})");
//			sbf.append("\r\n");
//			sbf.append("/**"+dicts[i]+"*/\r\n");
//			sbf.append("private String "+dicts[i+1]+";");
//			sbf.append("\r\n");
//		}
		System.out.println(sbf);
	}
	
	public static void entity2() throws Exception, URISyntaxException {
		String txt = read("entity.txt");
		String[] dicts = txt.split("\n");
		StringBuffer sbf = new StringBuffer();
		for (String sss : dicts) {
			String[] ss = sss.split("\t");
			String dis = ss[0];
			String pname = ss[1];
			String is = ss[2];
			String type = "String";
			if(ss.length > 3) {
				type = ss[3];
			}
			boolean isNotNull = false;
			if("是".equals(is)) {
				isNotNull = true;
			}
			if("Currency".equals(type)) {
				sbf.append("@CheckCurrency(message=\""+dis+" "+ pname +" 数据格式不正确\", groups = {AddGroup.class} , isNotNull ="+ isNotNull+")");
				sbf.append("\r\n");
			}
			if("DateTime".equals(type)) {
				sbf.append("@CheckDate(format=\"YYYY-MM-dd HH:mm:ss\", message=\""+dis+" "+ pname +" 数据格式不正确\", groups = {AddGroup.class}, isNotNull ="+ isNotNull+")");
				sbf.append("\r\n");
			}
			if("Date".equals(type)) {
				sbf.append("@CheckDate(message=\""+dis+" "+ pname +" 数据格式不正确\", groups = {AddGroup.class} , isNotNull ="+ isNotNull+")");
				sbf.append("\r\n");
			}
			if("字典".equals(type)) {
				sbf.append("@CheckDict(inf=CheckDictBuser.class,type=\""+pname+"\",isNotNull = "+isNotNull+",message=\""+dis +" “"+pname+"” 字典值不合法\",groups=AddGroup.class)\r\n");
			}
			sbf.append("/**"+dis+" "+ pname +" **/");
			sbf.append("\r\n");
			if("Currency".equals(type)) {
				sbf.append("private Integer "+ pname +";");
				sbf.append("\r\n");
			}else if("Date".equals(type)||"DateTime".equals(type) || "字典".equals(type)) {
				sbf.append("private String "+ pname +";");
				sbf.append("\r\n");
			}else{
				if(isNotNull) {
					sbf.append("@NotBlank(message=\""+dis+" "+ pname +" 不能为空\", groups = {AddGroup.class})");
					sbf.append("\r\n");
				}
				sbf.append("private String "+ pname +";");
				sbf.append("\r\n");
			}
		}
//		for (int i = 0; i < dicts.length; i++) {
//			sbf.append("@NotBlank(message=\""+dicts[i]+" "+dicts[i+1] +" 不能为空\", groups = {AddGroup.class})");
//			sbf.append("\r\n");
//			sbf.append("/**"+dicts[i]+"*/\r\n");
//			sbf.append("private String "+dicts[i+1]+";");
//			sbf.append("\r\n");
//		}
		System.out.println(sbf);
	}
	
	public static void entity() throws Exception, URISyntaxException {
		String txt = read("xmlformat.txt");
		String[] dicts = txt.split("\n");
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i < dicts.length; i+=2) {
			sbf.append("<"+dicts[i+1]+">"+dicts[i] +"</"+dicts[i+1]+">");
			sbf.append("\r\n");
		}
		System.out.println(sbf);
	}
	/**
	 * 创建表索引对象
	 * @throws Exception
	 * @throws URISyntaxException
	 */
	public static void createESTable(String tablename) throws Exception, URISyntaxException {
		String txt = read(tablename);
		HttpRequestBus.create("http://localhost:9200/"+tablename, RequestType.put).contentType("application/json").setParam(txt).send();
	}
//	curl -XDELETE http://localhost:9200/*
	public static void deleteESTable(String tableName) throws Exception, URISyntaxException {
		HttpRequestBus.create("http://localhost:9200/"+tableName, RequestType.delete).contentType("application/json").send();
	}
	
	public static void main(String[] args) throws InterruptedException, Exception, URISyntaxException{
//		System.out.println("----------start-----------");
//		dict();
//		entity3();
//		createESTable(ElasticIndex.pingan.name());	
		
//		createESTableSql(ElasticIndex.hospitalizationbill.name());

//		deleteESTable(ElasticIndex.hospitalizationbill.name());
//		createESTable(ElasticIndex.hospitalizationbill.name());	

//		deleteESTable(ElasticIndex.hospitalizationperscriptiondetail.name());
//		createESTable(ElasticIndex.hospitalizationperscriptiondetail.name());	
		
//		deleteESTable(ElasticIndex.hospitalizationfreesummary.name());
//		createESTable(ElasticIndex.hospitalizationfreesummary.name());		
		
//		deleteESTable(ElasticIndex.hospitalizationdiagnosis.name());
//		createESTable(ElasticIndex.hospitalizationdiagnosis.name());		
		
//		deleteESTable(ElasticIndex.hospitalization.name());
//		createESTable(ElasticIndex.hospitalization.name());		
		
//		deleteESTable(ElasticIndex.cliniccase.name());
//		createESTable(ElasticIndex.cliniccase.name());		
//
//		deleteESTable(ElasticIndex.clinicbill.name());
//		createESTable(ElasticIndex.clinicbill.name());	
//		
//		deleteESTable(ElasticIndex.clinicprescriptiondetail.name());
//		createESTable(ElasticIndex.clinicprescriptiondetail.name());
//		
//		deleteESTable(ElasticIndex.clinicfreesummary.name());
//		createESTable(ElasticIndex.clinicfreesummary.name());
//		
//		deleteESTable(ElasticIndex.clinicdiagnosis.name());
//		createESTable(ElasticIndex.clinicdiagnosis.name());
//		
//
//		deleteESTable(ElasticIndex.clinicregisetrinfo.name());
//		createESTable(ElasticIndex.clinicregisetrinfo.name());
//
//		deleteESTable(ElasticIndex.persion.name());
//		createESTable(ElasticIndex.persion.name());
		
		
//		System.out.println(initIDC10("IDC10.txt"));
//		for(int i=0;i<1;i++){
//			long begin=System.currentTimeMillis();
//			try {
//				bulk();
//				System.out.println("插入成功");
//				break;
//			} catch (Throwable e) {
//				System.out.println("----------insert error-----------,耗时"+(System.currentTimeMillis()-begin));
//				System.out.println(simpleMsg(e));
//			}
//		}
//		for(int i=0;i<5;i++){
//			long begin=System.currentTimeMillis();
//			try {
//				String txt = read("query.txt");
//				//params是url中的参数，即query部分
//				Map<String, String> params = Collections.singletonMap("pretty", "false");// 可选
//				HttpEntity entity = new NStringEntity(txt, ContentType.APPLICATION_JSON);
//				//_search也要用POST
//				Response resp = client.performRequest("POST", INDEX + "/_search", params, entity);
//				System.out.println(EntityUtils.toString(resp.getEntity()));
////				break;
//			} catch (Exception e) {
//				System.out.println("----------query error,将手工重试，耗时："+(System.currentTimeMillis()-begin));
//				System.out.println(simpleMsg(e));
//			}
//		}
		System.exit(0);
	}

}

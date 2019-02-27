package com.kasite.core.httpclient.http;


import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.dom4j.DocumentException;

public class HttpRequest{
	public final static String headerContentType = "Content-Type";

	public final static String Content_Type_post = "application/x-www-form-urlencoded";
	public final static String Content_Type_post_file = "application/octet-stream";
	public final static String Content_Type_post_UploadFile = "multipart/form-data";
	public final static String Content_Type_soap1_1 = "text/xml";
	public final static String Content_Type_soap1_2 = "application/soap+xml";
	public final static String Content_Type_httpPatch = "application/json";
	public final static String Content_Type_JSON = "application/json";
	
	public final static int ERRORRETCODE_NULL = -24444;// 调用接口返回空
	public final static int ERRORRETCODE_EXCEPTION = -34444;// 调用接口返回异常
	public final static int ERRORRETCODE_ConnectException = -10200;//接口无法正常访问
	public final static int ERRORRETCODE_SocketTimeoutException = -408;//接口超时异常代码
	
//	public static SoapResponseVo post(YYHisMethodInf method, String clientKey,
//			HttpRequestConfigVo config, String url, String param)
//			throws Exception {
//		return post(method, clientKey, config, url, param, false);
//	}
//
//	public static String doPostTest(YYHisMethodInf method, String url,
//			String param) throws Exception {
//		SoapResponseVo vo = post(method, "test_post_" + url,
//				new HttpRequestConfigVo(), url, param);
//		return vo.getResult();
//	}
//
//	public static String get(YYHisMethodInf method, String url, String param)
//			throws Exception {
//		SoapResponseVo vo = get(method, "test_get_" + url,
//				new HttpRequestConfigVo(), url, param, false);// (method,
//																// "test_get_"+url,
//																// new
//																// HttpRequestConfigVo(),
//																// url, param);
//		return vo.getResult();
//	}
//
//	public static String doPostSoap1_2Test(YYHisMethodInf method, String url,
//			String param) throws Exception {
//		SoapResponseVo vo = doPostSoap1_2(method, "test_soap12_" + url,
//				new HttpRequestConfigVo(), url, param);
//		return vo.getResult();
//	}
//
//	public static String doPostSoap1_1Test(YYHisMethodInf method, String url,
//			String param) throws Exception {
//		SoapResponseVo vo = doPostSoap1_1(method, "test_soap11_" + url,
//				new HttpRequestConfigVo(), url, param, false);// (method,
//																// "test_"+url,
//																// new
//																// HttpRequestConfigVo(),
//																// url, param);
//		return vo.getResult();
//	}
//
//	public static SoapResponseVo doPostSoap1_2(YYHisMethodInf method,
//			String clientKey, HttpRequestConfigVo config, String url,
//			String param) throws Exception {
//		return doPostSoap1_2(method, clientKey, config, url, param, false);
//	}
//
//	public static SoapResponseVo get(YYHisMethodInf method, String clientKey,
//			HttpRequestConfigVo config, String url, String param,
//			boolean isSaveLog) throws Exception {
//		StringBuffer logSbf = new StringBuffer();
//		SoapResponseVo vo = new SoapResponseVo();
//		boolean isLongConnection = true;
//		CloseableHttpClient closeableHttpClient = null;
//		HttpClientManager manager = null;
//		HttpGet get = null;
////		
////		int timeout = HttpClientManager.DEFAULT_HTTP_TIMEOUT;
////		if (null != config && config.getHttp_timeOut() > 0) {
////			timeout = config.getHttp_timeOut();
////		}
//		// 发送get请求
//		try {
//			if(null == config){
//				config = new HttpRequestConfigVo();
//			}
//			manager = HttpClientManager.getInstance();
//			if (null != config && config.isLongConnection() == false) {
//				isLongConnection = false;
//			}
//			
//			closeableHttpClient = manager.createHttpClient(clientKey,config, url);
//			// 用get方法发送http请求
//			String reqparam = URLEncoder.encode(param, encode);
//			get = new HttpGet(url + reqparam);
//			manager.config(get, config.getHttp_timeOut(),config.getConnectTimeOut(),config.getSocketTime());
//			logSbf.append("执行get请求, uri:" + get.getURI());
//			logSbf.append("\r\n执行get请求, 请求报文:" + reqparam);
//			addHeader(get, config, logSbf);
//
//			CloseableHttpResponse httpResponse = closeableHttpClient
//					.execute(get);
//			if (null != httpResponse) {
//				int statusCode = httpResponse.getStatusLine().getStatusCode();
//				if(statusCode != 200){
//					vo.setCode(-statusCode);
//				}else{
//					vo.setCode(statusCode);
//				}
//				logSbf.append("\r\n响应状态码:" + statusCode);
//				// response实体
//				HttpEntity entity = httpResponse.getEntity();
//				if (null != entity) {
//					String response = EntityUtils.toString(entity);
//					logSbf.append("\r\n响应内容:" + response);
//					vo.setResult(response);
//				}
//			} else {
//				vo.setCode(ERRORRETCODE_NULL);
//			}
//			
//		}catch( java.net.ConnectException e){
//			//连接异常
//			vo.setException(e);
//			vo.setResult("接口无法正常通信，请检查接口状态。ConnectException");
//			vo.setCode(ERRORRETCODE_ConnectException);
//			try {
//				manager.close(clientKey);
//			} catch (Exception e2) {
//				e2.printStackTrace();
//				logSbf.append("\r\n关闭连接失败:" + getExceptionStack(e2));
//			}
//		}catch( java.net.SocketTimeoutException e){
//			//医院接口请求超时
//			vo.setException(e);
//			vo.setResult("接口请求超时。SocketTimeoutException");
//			vo.setCode(ERRORRETCODE_SocketTimeoutException);
//			try {
//				manager.close(clientKey);
//			} catch (Exception e2) {
//				e2.printStackTrace();
//				logSbf.append("\r\n关闭连接失败:" + getExceptionStack(e2));
//			}
//		}catch (Exception e) {
//			logSbf.append("\r\n请求失败:" + getExceptionStack(e));
//			vo.setCode(ERRORRETCODE_EXCEPTION);
//			vo.setResult(getExceptionStack(e));
//			try {
//				manager.close(clientKey);
//			} catch (Exception e2) {
//				e2.printStackTrace();
//				logSbf.append("\r\n关闭连接失败:" + getExceptionStack(e2));
//			}
//			vo.setException(e);
//		} finally {
//			if (!isLongConnection) {
//				try {
//					if (!isLongConnection) {
//						manager.close(clientKey);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					logSbf.append("\r\n关闭连接失败:" + getExceptionStack(e));
//				}
//			}
//			if (isSaveLog) {
//				logSbf.append("执行get请求, uri: " + get.getURI());
//				com.coreframework.log.Logger.get().info(
//						"CallHisMethod",
//						new LogBody().set("method", method.name())
//								.set("param", "请求参数：" + param)
//								.set("Info", logSbf.toString()));
//			}
//			vo.setCalllog(logSbf.toString());
//		}
//		return vo;
//	}
//
//	/**
//	 * 使用SOAP1.2发送消息
//	 * 
//	 * @param postUrl
//	 * @param soapXml
//	 * @param soapAction
//	 * @return
//	 * @throws Exception
//	 */
//	public static SoapResponseVo doPostSoap1_2(YYHisMethodInf method,
//			String clientKey, HttpRequestConfigVo config, String postUrl,
//			String soapXml, boolean isSaveLog) throws Exception {
//		SoapResponseVo vo = new SoapResponseVo();
//		String retStr = "";
//		StringBuffer logSbf = new StringBuffer();
//		CloseableHttpClient closeableHttpClient = null;
//		boolean isLongConnection = true;
//		HttpClientManager manager =null;
//		// 发送post请求
//		try {
//			manager = HttpClientManager.getInstance();
//			if(null == config){
//				config = new HttpRequestConfigVo();
//			}
//			if (null != config && config.isLongConnection() == false) {
//				isLongConnection = false;
//			}
//			HttpPost httpPost = new HttpPost(postUrl);
//			closeableHttpClient = manager.createHttpClient(clientKey, config, postUrl);
//			// 设置请求和传输超时时间
//			manager.config(httpPost, config.getHttp_timeOut(),config.getConnectTimeOut(),config.getSocketTime());
//			logSbf.append("执行PostSoap1_2请求, uri: " + httpPost.getURI());
//			httpPost.setHeader(headerContentType, Content_Type_soap1_2);
//			logSbf.append("\r\n执行PostSoap1_2请求, 请求头新增: " + headerContentType + ":"
//					+ Content_Type_soap1_2);
//			StringEntity data = new StringEntity(soapXml,
//					Charset.forName(encode));
//			httpPost.setEntity(data);
//			logSbf.append("\r\n执行PostSoap1_2，请求报文:" + data.toString());
//			logSbf.append("\r\n执行PostSoap1_2请求, 请求内容: " + soapXml.toString());
//			addHeader(httpPost, config, logSbf);
//			soapXml = addHeader2Param(httpPost, soapXml, logSbf);
//			CloseableHttpResponse response = closeableHttpClient
//					.execute(httpPost);
//			if (response != null) {
//				// http 响应状态
//				int statuscode = response.getStatusLine().getStatusCode();
//				if(statuscode != 200){
//					vo.setCode(-statuscode);
//				}else{
//					vo.setCode(statuscode);
//				}
//				logSbf.append("\r\n响应状态码:" + statuscode);
//				HttpEntity httpEntity = response.getEntity();
//				if (null != httpEntity) {
//					retStr = EntityUtils.toString(httpEntity, encode);
//					logSbf.append("\r\n响应内容:" + retStr);
//				}
//			} else {
//				vo.setCode(ERRORRETCODE_NULL);
//			}
//			vo.setResult(retStr);
//		}catch( java.net.ConnectException e){
//			//连接异常
//			vo.setException(e);
//			vo.setResult("接口无法正常通信，请检查接口状态。ConnectException");
//			vo.setCode(ERRORRETCODE_ConnectException);
//			try {
//				manager.close(clientKey);
//			} catch (Exception e2) {
//				e2.printStackTrace();
//				logSbf.append("\r\n关闭连接失败:" + getExceptionStack(e2));
//			}
//			
//		}catch( java.net.SocketTimeoutException e){
//			//医院接口请求超时
//			vo.setException(e);
//			vo.setResult("接口请求超时。SocketTimeoutException");
//			vo.setCode(ERRORRETCODE_SocketTimeoutException);
//			try {
//				manager.close(clientKey);
//			} catch (Exception e2) {
//				e2.printStackTrace();
//				logSbf.append("\r\n关闭连接失败:" + getExceptionStack(e2));
//			}
//		} catch (Exception e) {
//			vo.setException(e);
//			logSbf.append("\r\n请求失败:" + getExceptionStack(e));
//			vo.setCode(ERRORRETCODE_EXCEPTION);
//			vo.setResult(getExceptionStack(e));
//			try {
//				manager.close(clientKey);
//			} catch (Exception e2) {
//				e2.printStackTrace();
//				logSbf.append("\r\n关闭连接失败:" + getExceptionStack(e2));
//			}
//		} finally {
//			try {
//				if (!isLongConnection) {
//					manager.close(clientKey);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				logSbf.append("\r\n关闭连接失败:" + getExceptionStack(e));
//			}
//			if (isSaveLog) {
//				com.coreframework.log.Logger.get().info(
//						"CallHisMethod",
//						new LogBody().set("method", method.name())
//								.set("param", "请求参数：" + soapXml)
//								.set("Info", logSbf.toString()));
//			}
//			vo.setCalllog(logSbf.toString());
//		}
//		return vo;
//	}
//
//	/**
//	 * 异常字符串处理方法
//	 * 
//	 * @param e
//	 * @return
//	 */
//	public static String getExceptionStack(Exception e) {
//		StringWriter sw = new StringWriter();
//		e.printStackTrace(new PrintWriter(sw, true));
//		return sw.toString();
//	}
//
//	/**
//	 * 普通put请求
//	 * 
//	 * @param method
//	 * @param url
//	 * @param param
//	 * @param isSaveLog
//	 * @return
//	 * @throws Exception 
//	 */
//	public static SoapResponseVo put(YYHisMethodInf method, String clientKey,
//			HttpRequestConfigVo config, String url, String param,
//			boolean isSaveLog) throws Exception {
//		SoapResponseVo vo = new SoapResponseVo();
//		StringBuffer logSbf = new StringBuffer();
//		// 创建默认的httpClient实例
//		CloseableHttpClient httpClient = null;
//		HttpClientManager manager = null;
//		boolean isLongConnection = true;
//		// 发送put请求
//		try {
//			manager = HttpClientManager.getInstance();
//			if(null == config){
//				config = new HttpRequestConfigVo();
//			}
//			if (null != config && config.isLongConnection() == false) {
//				isLongConnection = false;
//			}
//			httpClient = manager.createHttpClient(clientKey, config, url);
//			// 用get方法发送http请求
//			HttpPut postMethod = new HttpPut(url);
//			logSbf.append("执行put请求, uri: " + postMethod.getURI());
//			logSbf.append("\r\n执行put请求, 请求报文: " + param);
//			HttpEntity reqentity = new StringEntity(param, encode);
//			manager.config(postMethod, config.getHttp_timeOut(),config.getConnectTimeOut(),config.getSocketTime());
//			// response实体
//			postMethod.setEntity(reqentity);
//
//			addHeader(postMethod, config, logSbf);
//			param = addHeader2Param(postMethod, param, logSbf);
//			CloseableHttpResponse status = httpClient.execute(postMethod);
//			// response实体
//			if (null != status) {
//				int statusCode = status.getStatusLine().getStatusCode();
//				logSbf.append("\r\n执行put请求, 响应状态码:" + statusCode);
//				if(statusCode != 200){
//					vo.setCode(-statusCode);
//				}else{
//					vo.setCode(statusCode);
//				}
//
//				String response = EntityUtils.toString(status.getEntity(),
//						encode);
//				logSbf.append("\r\n执行put请求, 响应内容:" + response);
//				vo.setResult(response);
//			} else {
//				vo.setCode(ERRORRETCODE_NULL);
//			}
//		}catch( java.net.ConnectException e){
//			//连接异常
//			vo.setException(e);
//			vo.setResult("接口无法正常通信，请检查接口状态。ConnectException");
//			vo.setCode(ERRORRETCODE_ConnectException);
//			try {
//				manager.close(clientKey);
//			} catch (Exception e2) {
//				e2.printStackTrace();
//				logSbf.append("\r\n关闭连接失败:" + getExceptionStack(e2));
//			}
//			
//		}catch( java.net.SocketTimeoutException e){
//			//医院接口请求超时
//			vo.setException(e);
//			vo.setResult("接口请求超时。SocketTimeoutException");
//			vo.setCode(ERRORRETCODE_SocketTimeoutException);
//			try {
//				manager.close(clientKey);
//			} catch (Exception e2) {
//				e2.printStackTrace();
//				logSbf.append("\r\n关闭连接失败:" + getExceptionStack(e2));
//			}
//		} catch (Exception e) {
//			vo.setException(e);
//			logSbf.append("\r\n执行put请求,请求失败:" + getExceptionStack(e));
//			vo.setCode(ERRORRETCODE_EXCEPTION);
//			vo.setResult(getExceptionStack(e));
//			try {
//				manager.close(clientKey);
//			} catch (Exception e2) {
//				e2.printStackTrace();
//				logSbf.append("\r\n关闭连接失败:" + getExceptionStack(e2));
//			}
//		} finally {
//			try {
//				if (!isLongConnection) {
//					manager.close(clientKey);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				logSbf.append("\r\n关闭连接失败:" + getExceptionStack(e));
//			}
//			if (isSaveLog) {
//				com.coreframework.log.Logger.get().info(
//						"CallHisMethod",
//						new LogBody().set("method", method.name())
//								.set("param", "请求参数：" + param)
//								.set("Info", logSbf.toString()));
//			}
//			vo.setCalllog(logSbf.toString());
//		}
//		return vo;
//	}
//
//	/**
//	 * 普通post请求
//	 * 
//	 * @param method
//	 * @param url
//	 * @param param
//	 * @param isSaveLog
//	 * @return
//	 * @throws Exception 
//	 */
//	public static SoapResponseVo post(YYHisMethodInf method, String clientKey,
//			HttpRequestConfigVo config, String url, String param,
//			boolean isSaveLog) throws Exception {
//		SoapResponseVo vo = new SoapResponseVo();
//		StringBuffer logSbf = new StringBuffer();
//		// 创建默认的httpClient实例
//		CloseableHttpClient httpClient = null;
//		HttpClientManager manager = null;
//		boolean isLongConnection = true;
//		// 发送get请求
//		try {
//			manager = HttpClientManager.getInstance();
//			if(null == config){
//				config = new HttpRequestConfigVo();
//			}
//			if (null != config && config.isLongConnection() == false) {
//				isLongConnection = false;
//			}
//			httpClient = manager.createHttpClient(clientKey,config, url);
//			// 用get方法发送http请求
//			HttpPost postMethod = new HttpPost(url);
//			logSbf.append("执行post请求, uri:" + postMethod.getURI());
//			logSbf.append("\r\n执行post请求, param:" + param);
//			postMethod.addHeader("Content-Type", "application/x-www-form-urlencoded;");
//			HttpEntity reqentity = new StringEntity(param, encode);
//			manager.config(postMethod, config.getHttp_timeOut(),config.getConnectTimeOut(),config.getSocketTime());
//			// response实体
//			postMethod.setEntity(reqentity);
//			param = addHeader2Param(postMethod, param, logSbf);
//			CloseableHttpResponse status = httpClient.execute(postMethod);
//			// response实体
//			if (null != status) {
//				String response = EntityUtils.toString(status.getEntity());
//				int statusCode = status.getStatusLine().getStatusCode();
//				logSbf.append("\r\n响应状态码:" + statusCode);
//				logSbf.append("\r\n响应内容:" + response);
//				if(statusCode != 200){
//					vo.setCode(-statusCode);
//				}else{
//					vo.setCode(statusCode);
//				}
//				vo.setResult(response);
//				return vo;
//			} else {
//				vo.setCode(ERRORRETCODE_NULL);
//			}
//		}catch( java.net.ConnectException e){
//			//连接异常
//			vo.setException(e);
//			vo.setResult("接口无法正常通信，请检查接口状态。ConnectException");
//			vo.setCode(ERRORRETCODE_ConnectException);
//			try {
//				manager.close(clientKey);
//			} catch (Exception e2) {
//				e2.printStackTrace();
//				logSbf.append("\r\n关闭连接失败:" + getExceptionStack(e2));
//			}
//			
//		}catch( java.net.SocketTimeoutException e){
//			//医院接口请求超时
//			vo.setException(e);
//			vo.setResult("接口请求超时。SocketTimeoutException");
//			vo.setCode(ERRORRETCODE_SocketTimeoutException);
//			try {
//				manager.close(clientKey);
//			} catch (Exception e2) {
//				e2.printStackTrace();
//				logSbf.append("\r\n关闭连接失败:" + getExceptionStack(e2));
//			}
//		} catch (Exception e) {
//			vo.setException(e);
//			e.printStackTrace();
//			String calllog = "\r\n请求失败:" + getExceptionStack(e);
//			logSbf.append(calllog);
//			vo.setCode(-1);
//			vo.setCalllog(logSbf.toString());
//
//			try {
//				manager.close(clientKey);
//			} catch (Exception e2) {
//				e2.printStackTrace();
//				logSbf.append("\r\n关闭连接失败:" + getExceptionStack(e2));
//			}
//		} finally {
//			try {
//				if (!isLongConnection) {
//					manager.close(clientKey);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				logSbf.append("\r\n关闭连接失败:" + getExceptionStack(e));
//			}
//			if (isSaveLog) {
//				com.coreframework.log.Logger.get().info(
//						"CallHisMethod",
//						new LogBody().set("method", method.name())
//								.set("param", "请求参数：" + param)
//								.set("Info", logSbf.toString()));
//			}
//			vo.setCalllog(logSbf.toString());
//		}
//		return vo;
//	}
//	
//	public static void addHeader(HttpRequestBase request,
//			HttpRequestConfigVo config, StringBuffer logSbf) {
//		try {
//			String stroeJsonStr = config.getHeaderJsonObjStrConfig();
//			if(null != stroeJsonStr){
//				String s1 = stroeJsonStr.substring(0, 1);
//				if("\"".equals(s1)){
//					stroeJsonStr = stroeJsonStr.substring(1, stroeJsonStr.length()-1);
//				}
//				JSONTokener tk = new JSONTokener(stroeJsonStr);
//				Object json = tk.nextValue();
//				if(json instanceof JSONObject){
//				    JSONObject storeJson = (JSONObject)json;
//					Set<String> keys = storeJson.keySet();
//					for (String key : keys) {
//						request.setHeader(key, storeJson.getString(key));
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
//		if (null != config && null != config.getHeadMap()) {
//			Map<String, String> hmap = config.getHeadMap();
//			for (Map.Entry<String, String> entry : hmap.entrySet()) {
//				String key = entry.getKey();
//				String value = entry.getValue();
//				request.setHeader(key, value);
//				if(null != logSbf){
//					logSbf.append("\r\n执行PostSoap1_1请求, 请求头新增: " + key + ":"
//							+ value);
//				}
//			}
//		}
//	}
	/**
	 * //自定义内容的 Header 设置
	 */
	public static String addHeader2Param(HttpEntityEnclosingRequestBase request,
			String soapXml, StringBuffer logSbf) throws DocumentException {
		return IParseParamHeaderImpl.getInstance().parseParam2Header(request, soapXml);
	}

//	/** 
//	 * 使用SOAP1.1发送消息
//	 * 
//	 * @param postUrl
//	 * @param soapXml
//	 * @param soapAction
//	 * @return
//	 * @throws Exception
//	 */
//	public static SoapResponseVo doPostSoap1_1(YYHisMethodInf method,
//			String clientKey, HttpRequestConfigVo config, String postUrl,
//			String soapXml, boolean isSaveLog) throws Exception {
//		SoapResponseVo vo = new SoapResponseVo();
//		String retStr = "";
//		StringBuffer logSbf = new StringBuffer();
//		CloseableHttpClient closeableHttpClient = null;
//		HttpClientManager manager = null;
//		boolean isLongConnection = true;
//		// 发送get请求
//		try {
//			manager = HttpClientManager.getInstance();
//			if(null == config){
//				config = new HttpRequestConfigVo();
//			}
//			if (null != config && config.isLongConnection() == false) {
//				isLongConnection = false;
//			}
//			HttpPost httpPost = new HttpPost(postUrl);
//			closeableHttpClient = manager.createHttpClient(clientKey, config, postUrl);
//			// 设置请求和传输超时时间
//			manager.config(httpPost, config.getHttp_timeOut(),config.getConnectTimeOut(),config.getSocketTime());
//			logSbf.append("执行PostSoap1_1请求, uri: " + httpPost.getURI());
//			httpPost.setHeader(headerContentType, Content_Type_soap1_1);
//			logSbf.append("\r\n执行PostSoap1_1请求, 请求头新增: " + headerContentType + ":"
//					+ Content_Type_soap1_1);
//			addHeader(httpPost, config, logSbf);
//			soapXml = addHeader2Param(httpPost, soapXml, logSbf);
//			StringEntity data = new StringEntity(soapXml,
//					Charset.forName(encode));
//			httpPost.setEntity(data);
//			logSbf.append("\r\n执行PostSoap1_1请求, 请求报文: " + data.toString());
//			logSbf.append("\r\n执行PostSoap1_1请求, 请求内容: " + soapXml.toString());
//			CloseableHttpResponse response = closeableHttpClient
//					.execute(httpPost);
//			if (response != null) {
//				// http 响应状态
//				int statuscode = response.getStatusLine().getStatusCode();
//				if(statuscode != 200){
//					vo.setCode(-statuscode);
//				}else{
//					vo.setCode(statuscode);
//				}
//				logSbf.append("\r\n响应状态码:" + statuscode);
//				HttpEntity httpEntity = response.getEntity();
//				if (null != httpEntity) {
//					retStr = EntityUtils.toString(httpEntity, encode);
//					logSbf.append("\r\n响应内容:" + retStr);
//				}
//			} else {
//				vo.setCode(ERRORRETCODE_NULL);
//			}
//			vo.setResult(retStr);
//		}catch( java.net.ConnectException e){
//			//连接异常
//			vo.setException(e);
//			vo.setResult("接口无法正常通信，请检查接口状态。ConnectException");
//			vo.setCode(ERRORRETCODE_ConnectException);
//			try {
//				manager.close(clientKey);
//			} catch (Exception e2) {
//				e2.printStackTrace();
//				logSbf.append("\r\n关闭连接失败:" + getExceptionStack(e2));
//			}
//		}catch( java.net.SocketTimeoutException e){
//			//医院接口请求超时
//			vo.setException(e);
//			vo.setResult("接口请求超时。SocketTimeoutException");
//			vo.setCode(ERRORRETCODE_SocketTimeoutException);
//			try {
//				manager.close(clientKey);
//			} catch (Exception e2) {
//				e2.printStackTrace();
//				logSbf.append("\r\n关闭连接失败:" + getExceptionStack(e2));
//			}
//		} catch (Exception e) {
//			vo.setException(e);
//			String str = getExceptionStack(e);
//			logSbf.append("\r\n请求失败:" + str);
//			vo.setCode(ERRORRETCODE_EXCEPTION);
//			vo.setResult(str);
//			try {
//				manager.close(clientKey);
//			} catch (Exception e2) {
//				e2.printStackTrace();
//				logSbf.append("\r\n关闭连接失败" + getExceptionStack(e2));
//			}
//		} finally {
//			try {
//				if (!isLongConnection) {
//					manager.close(clientKey);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				logSbf.append("\r\n关闭连接失败" + getExceptionStack(e));
//			}
//			if (isSaveLog) {
//				com.coreframework.log.Logger.get().info(
//						"CallHisMethod",
//						new LogBody().set("method", method.name())
//								.set("param", "请求参数：" + soapXml)
//								.set("Info", logSbf.toString()));
//			}
//			vo.setCalllog(logSbf.toString());
//		}
//		return vo;
//	}
	/**
	 * 格式化 SOAP 返回值
	 * */
	public static String formateSoapResp(String soapXml) {
		if (StringUtils.isNotBlank(soapXml)) {
			soapXml = StringEscapeUtils.unescapeXml(soapXml);
			if (StringUtils.isNotBlank(soapXml) && soapXml.contains("ns1:out")) {
				int begin = soapXml.indexOf("<ns1:out>");
				int end = soapXml.indexOf("</ns1:out>");
				soapXml = soapXml.substring(begin + 9, end);
			}
			if (StringUtils.isNotBlank(soapXml) && soapXml.contains("<![CDATA[")) {
				int begin = soapXml.indexOf("<![CDATA[");
				int end = soapXml.indexOf("]]>");
				soapXml = soapXml.substring(begin + 9, end);
			}
			if(StringUtils.isNotBlank(soapXml) && soapXml.contains("<Resp>")){
				int begin = soapXml.indexOf("<Resp>");
				int end = soapXml.indexOf("</Resp>");
				soapXml = soapXml.substring(begin, end+7);
			}
			if(StringUtils.isNotBlank(soapXml) && soapXml.contains("<response")){
				int begin = soapXml.indexOf("<response");
				int end = soapXml.indexOf("</response>");
				soapXml = soapXml.substring(begin, end+11);
			}
		}
		return soapXml;
	}
	public enum ResultFormat{
		ret,
		ns1out,
		cdata,
		
		
	}
	
	/**
	 * 格式化 SOAP 返回值
	 * */
	public static String formateSoapResp(String soapXml,ResultFormat formatEnum) {
		if (StringUtils.isNotBlank(soapXml)) {
			soapXml = StringEscapeUtils.unescapeXml(soapXml);
			switch (formatEnum) {
			case ret:{
				int begin = soapXml.indexOf("<return>");
				int end = soapXml.indexOf("</return>");
				soapXml = soapXml.substring(begin + 8, end);
				break;
			}
			case cdata:{
				int begin = soapXml.indexOf("<![CDATA[");
				int end = soapXml.indexOf("]]>");
				soapXml = soapXml.substring(begin + 9, end);
				break;
			}
			case ns1out:{
				int begin = soapXml.indexOf("<ns1:out>");
				int end = soapXml.indexOf("</ns1:out>");
				soapXml = soapXml.substring(begin + 9, end);
				break;
			}
			default:
				break;
			}
		}
		return soapXml;
	}
}
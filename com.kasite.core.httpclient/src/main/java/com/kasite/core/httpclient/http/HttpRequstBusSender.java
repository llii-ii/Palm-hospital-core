package com.kasite.core.httpclient.http;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.kasite.core.httpclient.http.IRequestExceptionHandler.ReqInfo;



public class HttpRequstBusSender {
	static Map<String, String> map = new HashMap<String,String>();
	//编码类型
	private String encode = "UTF-8";
	/**org.apache.http.entity.mime.content.ContentBody
	 * 不设置超时时长，一定等待对方的返回。
	 */
	private boolean isNotTimeOut=false;
	private String url;
	private String clientKey;
	private static List<IRequestExceptionHandler> handlerList = new ArrayList<>();
	//请求配置参数
	private HttpRequestConfigVo config;
	//请求类型 get/post/soap1/soap2/put ...
	private RequestType type;
	//get请求的参数
	private HttpParam getParam;
	
	private String contentType;
	
	private String filePath;
	
	private File file;
	
	private String fileDispositionName;
	
	private boolean isLongConnection = false;
	/**
	 * 新增接口异常告警处理类
	 */
	public void addHandler(IRequestExceptionHandler handler) {
		if(null == handlerList) {
			handlerList = new ArrayList<>();
		}
		handlerList.add(handler);
	}
	
	public HttpRequstBusSender httpRequestConfigVo(HttpRequestConfigVo config) {
		this.config = config;
		return this;
	}
	public HttpRequstBusSender uuid(String uuid) {
		return this;
	}
	public HttpRequstBusSender contentType(String contentType) {
		this.contentType = contentType;
		return this;
	}
	public HttpRequstBusSender setFilePath(String filePath) {
		this.filePath = filePath;
		return this;
	}
	public HttpRequstBusSender setFile(String name,File file) {
		this.file = file;
		this.fileDispositionName = name;
		return this;
	}

	/***
	 * 设置编码类型：默认utf-8编码字符。
	 * @param encode
	 * @return
	 */
	public HttpRequstBusSender encode(String encode) {
		this.encode = encode;
		return this;
	}
	/***
	 * 设置请求报文，
	 * 默认为post方法将param内容字符串 通过UTF-8 的编译方式进行编译后发放到请求body中进行发送
	 * 可以将soap的通过此参数设置
	 * @param param
	 * @return
	 */
	public HttpRequstBusSender setParam(String param) {
		if(this.getParam == null){
			this.getParam = new HttpParam();
		}
		IParseParamUrl pu = IParseParamUrlImpl.getInstance().parse(param);
		String ul = pu.url();
		if(null != ul && !"".equals(ul)){
			String p = pu.param();
			String url = pu.url();
			this.url = url;
			param = p;
		}
		EncodeVo encodeImpl = IParseParamEncodeImpl.getInstance().parse(param);
		String encode = encodeImpl.getEncode();
		if(null != encode && !"".equals(encode)){
			String retval = encodeImpl.getRetVal();
			this.encode = encode;
			param = retval;
		}
		this.getParam.setParam(param);
		return this;
	}
	/***
	 * 是否设置连接超时。如果此次连接不设置超时则没有超时参数设置。
	 * @param isNotTimeOut
	 * @return
	 */
	public HttpRequstBusSender isNotTimeOut(boolean isNotTimeOut) {
		this.isNotTimeOut = isNotTimeOut;
		return this;
	}
	/***
	 * 是否使用长连接
	 * @param isLongConn
	 * @return
	 */
	public HttpRequstBusSender isLongConnection(boolean isLongConn) {
		this.isLongConnection = isLongConn;
		return this;
	}
	/***
	 * 最大连接数：本地连接池最大连接数 默认 500
	 * @param isLongConn
	 * @return
	 */
	public HttpRequstBusSender http_maxTotal(int http_maxTotal) {
		if(null == config){
			config = new HttpRequestConfigVo();
		}
		config.setHttp_maxTotal(http_maxTotal);
		return this;
	}
	/***
	 * 目标主机的最大连接数： 默认 100
	 * @param isLongConn
	 * @return
	 */
	public HttpRequstBusSender http_maxPerRoute(int http_maxPerRoute) {
		if(null == config){
			config = new HttpRequestConfigVo();
		}
		config.setHttp_maxPerRoute(http_maxPerRoute);
		return this;
	}
	/**
	 * 连接超时控制：秒内等待本地连接池连接 默认 从连接池获取连接 超时异常
	 * @param http_timeOut
	 * @return
	 */
	public HttpRequstBusSender http_timeOut(int http_timeOut) {
		if(null == config){
			config = new HttpRequestConfigVo();
		}
		config.setHttp_timeOut(http_timeOut);
		return this;
	}
	/**
	 * 异常重试次数设置 小于2 的不生效
	 * @param http_executionCount
	 * @return
	 */
	public HttpRequstBusSender http_executionCount(int http_executionCount) {
		if(null == config){
			config = new HttpRequestConfigVo();
		}
		config.setHttp_executionCount(http_executionCount);
		return this;
	}
	/**
	 * 连接超时connetionTimeout：指客户端和服务器建立连接的timeout， 
	 * 就是http请求的三个阶段，一：建立连接；二：数据传送；三，断开连接。超时后会ConnectionTimeOutException
	 * @param connectTimeOut
	 * @return
	 */
	public HttpRequstBusSender connectTimeOut(int connectTimeOut) {
		if(null == config){
			config = new HttpRequestConfigVo();
		}
		config.setConnectTimeOut(connectTimeOut);;
		return this;
	}
	/**
	 * socketTimeout：指客户端从服务器读取数据的timeout，超出后会抛出SocketTimeOutException
	 * @param socketTime
	 * @return
	 */
	public HttpRequstBusSender socketTime(int socketTime) {
		if(null == config){
			config = new HttpRequestConfigVo();
		}
		config.setSocketTime(socketTime);
		return this;
	}
	public HttpRequstBusSender url(String url) {
		this.url = url;
		return this;
	}
	public HttpRequstBusSender clientKey(String clientKey) {
		this.clientKey = clientKey;
		return this;
	}
	public HttpRequstBusSender requestType(RequestType type) {
		this.type = type;
		return this;
	}
	//新增参数
	public HttpRequstBusSender addHttpParam(String name,String value){
		if(this.getParam == null){
			this.getParam = new HttpParam();
		}
		this.getParam.addParam(name, value);
		return this;
	}
	//新增头部参数
	public HttpRequstBusSender setHeaderHttpParam(String name,String value){
		if(this.getParam == null){
			this.getParam = new HttpParam();
		}
		this.getParam.addHeaderParam(name, value);
		return this;
	}
	
	private HttpRequestBase newhttpPatchBody() throws Exception{
		HttpPatch httpPatch = new HttpPatch(url);
		String headerContentType = HttpRequest.Content_Type_httpPatch+";charset="+encode;
		if(StringUtils.isNotBlank(contentType)){
			headerContentType = contentType;
		}
		httpPatch.setHeader(HttpRequest.headerContentType, headerContentType);
        httpPatch.setHeader("Accept", "application/json");  
		if(null != getParam){
			String param = getParam.toBodyString();
			if(StringUtils.isNotBlank(param)){
				HttpEntity entity = new StringEntity(HttpRequest.addHeader2Param(httpPatch, param, null), ContentType.create(HttpRequest.Content_Type_httpPatch, encode));;
				httpPatch.setEntity(entity);
			}
		}
		return httpPatch;
	}
	private HttpRequestBase newSoap2Body() throws Exception{
		HttpPost post = new HttpPost(url);
		String headerContentType = HttpRequest.Content_Type_soap1_2+";charset="+encode;
		if(StringUtils.isNotBlank(contentType)){
			headerContentType = contentType;
		}
		post.setHeader(HttpRequest.headerContentType, headerContentType);
		if(null != getParam){
			String param = getParam.toBodyString();
			if(StringUtils.isNotBlank(param)){
				HttpEntity entity = new StringEntity(HttpRequest.addHeader2Param(post, param, null), encode);;
				post.setEntity(entity);
			}
		}
		return post;
	}
	private HttpRequestBase newSoap1Body() throws Exception{
		HttpPost post = new HttpPost(url);
		
		String headerContentType = HttpRequest.Content_Type_soap1_1+";charset="+encode;
		if(StringUtils.isNotBlank(contentType)){
			headerContentType = contentType;
		}
		post.setHeader(HttpRequest.headerContentType, headerContentType);
		if(null != getParam){
			String param = getParam.toBodyString();
			if(StringUtils.isNotBlank(param)){
				HttpEntity entity = new StringEntity(HttpRequest.addHeader2Param(post, param, null), encode);;
				post.setEntity(entity);
			}
		}
		return post;
	}
	private HttpRequestBase newDeleteUrl() throws Exception{
		HttpDelete post = new HttpDelete(url);
		String headerContentType = HttpRequest.Content_Type_post+";charset="+encode;
		if(StringUtils.isNotBlank(contentType)){
			headerContentType = contentType;
		}
		post.setHeader(HttpRequest.headerContentType, headerContentType);
		return post;
	}
//	private HttpRequestBase newFileUploadPostUrl() throws Exception{
//		HttpPost post = new HttpPost(url);
//		String headerContentType = HttpRequest.Content_Type_post_file+";charset="+encode;
//		if(StringUtils.isNotBlank(contentType)){
//			headerContentType = contentType;
//		}
//		post.setHeader(HttpRequest.headerContentType, headerContentType);
//		if(null != getParam){
//			String param = getParam.toBodyString();
//			if(StringUtils.isNotBlank(param)){
//				HttpEntity entity = new StringEntity(HttpRequest.addHeader2Param(post, param, null), encode);;
//				post.setEntity(entity);
//			}
//		}
//		return post;
//	}
	/**
	 * 上传文件的 mimeType 默认为：application/zip
	 * 需要修改自行调整
	 * @param mimeType
	 * @return
	 */
	public HttpRequstBusSender setMimeType(String mimeType) {
		this.mimeType = mimeType;
		return this;
	}
	private String mimeType = "application/zip";
	
	private HttpRequestBase newUploadFilePostUrl() throws Exception{
		HttpPost post = new HttpPost(url);
		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
		if(StringUtils.isNotBlank(filePath)) {
			file = new File(filePath);
		}else if(null != file){
			filePath = file.getPath();
		}
		FileBody body = new FileBody(file,mimeType);
		if(StringUtils.isBlank(fileDispositionName)) {
			fileDispositionName = "file";
		}
		multipartEntityBuilder.addPart(fileDispositionName, new FileBody(file));
		if(null != getParam && null != getParam.getParamMap()) {
			Map<String, String> map = getParam.getParamMap();
			for (Map.Entry<String, String> entity : map.entrySet()) {
				String key = entity.getKey(); 
				String value = entity.getValue();
				multipartEntityBuilder.addPart(key, new StringBody(value));
			}
		}
		
		
		HttpEntity httpEntity = multipartEntityBuilder.build();
	    post.setEntity(httpEntity);
		return post;
	}
	
	
	
	private HttpRequestBase newPostUrl() throws Exception{
		HttpPost post = new HttpPost(url);
		String headerContentType = HttpRequest.Content_Type_post+";charset="+encode;
		if(StringUtils.isNotBlank(contentType)){
			headerContentType = contentType;
		}
		post.setHeader(HttpRequest.headerContentType, headerContentType);
		if(null != getParam){
			String param = getParam.toBodyString();
			if(StringUtils.isNotBlank(param)){
				HttpEntity entity = new StringEntity(HttpRequest.addHeader2Param(post, param, null), encode);;
				post.setEntity(entity);
			}
		}
		return post;
	}
	private HttpRequestBase newPut() throws Exception{
		HttpPut post = new HttpPut(url);
		String headerContentType = HttpRequest.Content_Type_post+";charset="+encode;
		if(StringUtils.isNotBlank(contentType)){
			headerContentType = contentType;
		}
		post.setHeader(HttpRequest.headerContentType, headerContentType);
		if(null != getParam){
			String param = getParam.toBodyString();
			if(StringUtils.isNotBlank(param)){
				HttpEntity entity = new StringEntity(HttpRequest.addHeader2Param(post, param, null), encode);;
				post.setEntity(entity);
			}
		}
		return post;
	}
	private HttpRequestBase newDownFilePostUrl() throws Exception{
		HttpPost post = new HttpPost(url);
		if(null != getParam){
			String param = getParam.toBodyString();
			if(StringUtils.isNotBlank(param)){
				HttpEntity entity = new StringEntity(HttpRequest.addHeader2Param(post, param, null), encode);;
				post.setEntity(entity);
			}
		}
		return post;
	}
	
	private HttpRequestBase newGetUrl(){
		if(null != getParam){
			String urlparam = getParam.toGetString();
			if(StringUtils.isNotBlank(urlparam)){
				url += urlparam;
			}
		}
		HttpGet get = new HttpGet(url);
		return get;
	}
	
	
    /**
     * @param url 请求地址
     * @param map 请求的参数
     * @param filePath 文件路径
     * @param body_data 上传的文件二进制内容
     * @param charset 字符集
     * @return
     */
    public SoapResponseVo doPostSubmitBody(Map<String, String> map,
            String filePath, byte[] body_data, String charset) {
    		SoapResponseVo respVo = new SoapResponseVo();
        // 设置三个常用字符串常量：换行、前缀、分界线（NEWLINE、PREFIX、BOUNDARY）；
        final String NEWLINE = "\r\n"; // 换行，或者说是回车
        final String PREFIX = "--"; // 固定的前缀
        final String BOUNDARY = "#59083976eebd#"; // 分界线，就是上面提到的boundary，可以是任意字符串，建议写长一点，这里简单的写了一个#
        HttpURLConnection httpConn = null;
        BufferedInputStream bis = null;
        DataOutputStream dos = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 实例化URL对象。调用URL有参构造方法，参数是一个url地址；
            URL urlObj = new URL(url);
            // 调用URL对象的openConnection()方法，创建HttpURLConnection对象；
            httpConn = (HttpURLConnection) urlObj.openConnection();
            // 调用HttpURLConnection对象setDoOutput(true)、setDoInput(true)、setRequestMethod("POST")；
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            httpConn.setRequestMethod("POST");
            // 设置Http请求头信息；（Accept、Connection、Accept-Encoding、Cache-Control、Content-Type、User-Agent），不重要的就不解释了，直接参考抓包的结果设置即可
            httpConn.setUseCaches(false);
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("Accept", "*/*");
            httpConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            httpConn.setRequestProperty("Cache-Control", "no-cache");
            // 这个比较重要，按照上面分析的拼装出Content-Type头的内容
            httpConn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);
            // 这个参数可以参考浏览器中抓出来的内容写，用chrome或者Fiddler抓吧看看就行
            httpConn.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30)");
            // 调用HttpURLConnection对象的connect()方法，建立与服务器的真实连接；
            httpConn.connect();

            // 调用HttpURLConnection对象的getOutputStream()方法构建输出流对象；
            dos = new DataOutputStream(httpConn.getOutputStream());
            // 获取表单中上传控件之外的控件数据，写入到输出流对象（根据上面分析的抓包的内容格式拼凑字符串）；
            if (map != null && !map.isEmpty()) { // 这时请求中的普通参数，键值对类型的，相当于上面分析的请求中的username，可能有多个
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey(); // 键，相当于上面分析的请求中的username
                    String value = map.get(key); // 值，相当于上面分析的请求中的sdafdsa
                    dos.writeBytes(PREFIX + BOUNDARY + NEWLINE); // 像请求体中写分割线，就是前缀+分界线+换行
                    dos.writeBytes("Content-Disposition: form-data; "
                            + "name=\"" + key + "\"" + NEWLINE); // 拼接参数名，格式就是Content-Disposition: form-data; name="key" 其中key就是当前循环的键值对的键，别忘了最后的换行 
                    dos.writeBytes(NEWLINE); // 空行，一定不能少，键和值之间有一个固定的空行
                    dos.writeBytes(URLEncoder.encode(value.toString(), charset)); // 将值写入
                    // 或者写成：dos.write(value.toString().getBytes(charset));
                    dos.writeBytes(NEWLINE); // 换行
                } // 所有循环完毕，就把所有的键值对都写入了
            }

            // 获取表单中上传附件的数据，写入到输出流对象（根据上面分析的抓包的内容格式拼凑字符串）；
            if (body_data != null && body_data.length > 0) {
                dos.writeBytes(PREFIX + BOUNDARY + NEWLINE);// 像请求体中写分割线，就是前缀+分界线+换行
                String fileName = filePath.substring(filePath
                        .lastIndexOf(File.separatorChar) + 1); // 通过文件路径截取出来文件的名称，也可以作文参数直接传过来
                // 格式是:Content-Disposition: form-data; name="请求参数名"; filename="文件名"
                // 我这里吧请求的参数名写成了uploadFile，是死的，实际应用要根据自己的情况修改
                // 不要忘了换行
                dos.writeBytes("Content-Disposition: form-data; " + "name=\""
                        + "uploadFile" + "\"" + "; filename=\"" + fileName
                        + "\"" + NEWLINE);
                // 换行，重要！！不要忘了
                dos.writeBytes(NEWLINE);
                dos.write(body_data); // 上传文件的内容
                dos.writeBytes(NEWLINE); // 最后换行
            }
            dos.writeBytes(PREFIX + BOUNDARY + PREFIX + NEWLINE); // 最后的分割线，与前面的有点不一样是前缀+分界线+前缀+换行，最后多了一个前缀
            dos.flush();

            // 调用HttpURLConnection对象的getInputStream()方法构建输入流对象；
            byte[] buffer = new byte[8 * 1024];
            int c = 0;
            // 调用HttpURLConnection对象的getResponseCode()获取客户端与服务器端的连接状态码。如果是200，则执行以下操作，否则返回null；
            int statusCode = (httpConn.getResponseCode());
            respVo.setCode(statusCode);
            if(statusCode == 200) {
            	 	bis = new BufferedInputStream(httpConn.getInputStream());
                 while ((c = bis.read(buffer)) != -1) {
                     baos.write(buffer, 0, c);
                     baos.flush();
                 }
            }
            String result =  new String(baos.toByteArray(), charset);
            respVo.setResult(result);
            // 将输入流转成字节数组，返回给客户端。
            return respVo;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dos != null)
                    dos.close();
                if (bis != null)
                    bis.close();
                if (baos != null)
                    baos.close();
                httpConn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

	public void upLoadFile(File file) throws Exception{
		
		HttpRequestBase requestBodyMessage = null;
		CloseableHttpClient httpClient = null;
		HttpClientManager manager = null;
		FileOutputStream output = null;
		InputStream is = null;
		try {
			if(null == config) {
				config = new HttpRequestConfigVo();
			}
			manager = HttpClientManager.getInstance();
			httpClient = manager.createHttpClient(clientKey,config, url);
			
			if(null != getParam){
				if(StringUtils.isNotBlank(url) && type.equals(RequestType.get)){
					int leng = url.length();
					if(url.lastIndexOf("?") < 0){
						url = url+"?SYSTEMTIMEMILLIS="+System.currentTimeMillis();
					}else if(url.lastIndexOf("?") == (leng - 1)){
						url = url+"SYSTEMTIMEMILLIS="+System.currentTimeMillis();
					}
				}
			}
			switch (type) {
				case get:{
					requestBodyMessage = newGetUrl();
					break;
				}
				case post:{
					requestBodyMessage = newPostUrl();
					break;
				}
				default:
					break;
			}
			requestBodyMessage.addHeader("Content-type", HttpRequest.Content_Type_post_file);
			CloseableHttpResponse status = httpClient.execute(requestBodyMessage);
			// response实体
			if (null != status) {
				int code = status.getStatusLine().getStatusCode();
				if(code == 200) {
					File storeFile = file;
					output = new FileOutputStream(storeFile);
					  // 得到网络资源的字节数组,并写入文件
					is = status.getEntity().getContent();
					byte[] buf = new byte[4*4096];
			        int read;
			        while ((read = is.read(buf)) != -1) {
			        		output.write(buf, 0, read);
			        		output.flush();
			        }
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally {
			manager.close(clientKey);
			try {
				if(null != output)
					output.close();
			}catch (Exception e) {
			}
			try {
				if(null != is)
					is.close();
			}catch (Exception e) {
			}
	        
		}
		
		
		
		
	}
	
	public void downLoad(File file) throws Exception {
		// 创建默认的httpClient实例
		HttpRequestBase requestBodyMessage = null;
		CloseableHttpClient httpClient = null;
		HttpClientManager manager = null;
		FileOutputStream output = null;
		InputStream is = null;
		try {
			if(null == config) {
				config = new HttpRequestConfigVo();
			}
			manager = HttpClientManager.getInstance();
			httpClient = manager.createHttpClient(clientKey,config, url);
			
			if(null != getParam){
				if(StringUtils.isNotBlank(url) && type.equals(RequestType.get)){
					int leng = url.length();
					if(url.lastIndexOf("?") < 0){
						url = url+"?SYSTEMTIMEMILLIS="+System.currentTimeMillis();
					}else if(url.lastIndexOf("?") == (leng - 1)){
						url = url+"SYSTEMTIMEMILLIS="+System.currentTimeMillis();
					}
				}
			}
			switch (type) {
				case get:{
					requestBodyMessage = newGetUrl();
					break;
				}
				case post:{
					requestBodyMessage = newPostUrl();
					break;
				}
				default:
					break;
			}
			requestBodyMessage.addHeader("Content-type", HttpRequest.Content_Type_post_file);
			CloseableHttpResponse status = httpClient.execute(requestBodyMessage);
			// response实体
			if (null != status) {
				int code = status.getStatusLine().getStatusCode();
				if(code == 200) {
					File storeFile = file;
					output = new FileOutputStream(storeFile);
					  // 得到网络资源的字节数组,并写入文件
					is = status.getEntity().getContent();
					byte[] buf = new byte[4*4096];
			        int read;
			        while ((read = is.read(buf)) != -1) {
			        		output.write(buf, 0, read);
			        		output.flush();
			        }
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally {
			manager.close(clientKey);
			try {
				if(null != output)
					output.close();
			}catch (Exception e) {
			}
			try {
				if(null != is)
					is.close();
			}catch (Exception e) {
			}
	        
		}
		
	}
	
	
	/**
	 * 往集群发送消息
	 * @return
	 */
	public SoapResponseVo sendCluster() {
		String[] urls = url.split(",");
		return sendCluster(urls);
	}
	/**
	 * 往集群发送消息
	 * @param sendUrlKey 集群主键 每一个集群都要设置独立的一个 用于识别当前可用的节点
	 * @param urls
	 * @return
	 */
	public SoapResponseVo sendCluster(String[] urls) {
		//随机从集群url中取一个，如果失败则再循环中挑选一个
		String sendUrlKey = clientKey;
		String mainPKeyUrl = map.get(sendUrlKey);
		if(StringUtils.isBlank(mainPKeyUrl)) {
			mainPKeyUrl = urls[0];
			map.put(sendUrlKey, mainPKeyUrl);
		}
		SoapResponseVo vo = url(mainPKeyUrl).send();
		int code = vo.getCode();
		if(HttpRequest.ERRORRETCODE_ConnectException == code) {
			if(urls.length > 1) {
				for (String url : urls) {
					vo = url(url).send();
					if(vo.getCode() == 200) {
						map.put(sendUrlKey, url);
						return vo;
					}
				}
			}
		}
		return vo;
	}
	
	public SoapResponseVo send() {
		ReqInfo info = new ReqInfo();
		if(null == clientKey ){
			clientKey = url;
		}
		info.setClientKey(clientKey);
		info.setUrl(url);
		info.setType(type);
		boolean isStopReq = false;
//		LogBody body = new LogBody().set("clientkey", clientKey);
		Exception ex = null;
		if(config == null){
			config = new HttpRequestConfigVo();
		}
		SoapResponseVo vo = new SoapResponseVo();
		HttpRequestBase requestBodyMessage = null;
		// 创建默认的httpClient实例
		CloseableHttpClient httpClient = null;
		HttpClientManager manager = null;
		try {
			manager = HttpClientManager.getInstance();
			if (null != config && config.isLongConnection() == false) {
				isLongConnection = false;
			}
			//判断是否要阻断访问
			if(null != handlerList) {
				for (IRequestExceptionHandler handler : handlerList) {
					if(handler.isStopRequest(info)) {
						isStopReq = true;
						throw new Exception("请求发生大量异常，系统默认关闭该接口地址的请求："	+ url);
					}
				}
			}
			
			
			if(null != getParam){
				if(StringUtils.isNotBlank(url) && type.equals(RequestType.get)){
					int leng = url.length();
					if(url.lastIndexOf("?") < 0){
						url = url+"?SYSTEMTIMEMILLIS="+System.currentTimeMillis();
					}else if(url.lastIndexOf("?") == (leng - 1)){
						url = url+"SYSTEMTIMEMILLIS="+System.currentTimeMillis();
					}
				}
			}
			
			httpClient = manager.createHttpClient(clientKey,config, url);
			switch (type) {
			case get:{
				requestBodyMessage = newGetUrl();
				break;
			}
			case delete:{
				requestBodyMessage = newDeleteUrl();
				break;
			}
			case post:{
				requestBodyMessage = newPostUrl();
				break;
			}
			case put:{
				requestBodyMessage = newPut();
				break;
			}
			case soap1:{
				requestBodyMessage = newSoap1Body();
				break;
			}
			case soap2:{
				requestBodyMessage = newSoap2Body();
				break;
			}
			case HttpPatch:{
				requestBodyMessage = newhttpPatchBody();
				break;
			}
			case fileUploadPost:{
				requestBodyMessage = newUploadFilePostUrl();
				break;
			}
			default:
				break;
			}
			//设置头部
			if(null != getParam && null != requestBodyMessage){
				for (Map.Entry<String, String> entity : getParam.getHeaderParams().entrySet()) {
					requestBodyMessage.setHeader(entity.getKey(), entity.getValue());
				}
			}
			
			//如果请求不设置超时
			if(!isNotTimeOut){
				//设置超时限制
				manager.config(requestBodyMessage, config.getHttp_timeOut(), config.getConnectTimeOut(), config.getSocketTime());
			}
			
//			body.set("url", url)
//			.set("config",config.toString())
//			.set("requesttype", type)
//			.set("uuid", uuid);
			
			
			CloseableHttpResponse status = httpClient.execute(requestBodyMessage);
			// response实体
			if (null != status) {
				String response = EntityUtils.toString(status.getEntity(),encode);
				int statusCode = status.getStatusLine().getStatusCode();
				if(statusCode != 200){
					vo.setCode(-statusCode);
				}else{
					vo.setCode(statusCode);
				}
				vo.setResult(response);
				return vo;
			} else {
				vo.setCode(HttpRequest.ERRORRETCODE_NULL);
			}
		}catch( java.net.ConnectException e){
			ex = e;
			//连接异常
			vo.setResult("接口无法正常通信，请检查接口状态。ConnectException"+e.getMessage());
			vo.setCode(HttpRequest.ERRORRETCODE_ConnectException);
			if(null != handlerList) {
				for (IRequestExceptionHandler handler : handlerList) {
					handler.httpRequestException(e, info);
				}
			}
		}catch( java.net.SocketTimeoutException e){
			ex = e;
			//医院接口请求超时
			vo.setResult("接口请求超时。SocketTimeoutException"+e.getMessage());
			vo.setCode(HttpRequest.ERRORRETCODE_SocketTimeoutException);
			if(null != handlerList) {
				for (IRequestExceptionHandler handler : handlerList) {
					handler.httpRequestException(e, info);
				}
			}
		}catch(	org.apache.http.conn.ConnectionPoolTimeoutException e){
			ex = e;
			//从连接池中获取连接异常。
			vo.setResult("从连接池中获取连接异常。ConnectionPoolTimeoutException："+e.getMessage());
			vo.setCode(HttpRequest.ERRORRETCODE_ConnectException);
			if(null != handlerList) {
				for (IRequestExceptionHandler handler : handlerList) {
					handler.httpRequestException(e, info);
				}
			}
		}catch(java.net.SocketException e){
			ex = e;
			//通信异常 请求未发送到对方
			vo.setResult("通信异常无法将请求发往对方。SocketException："+e.getMessage());
			vo.setCode(HttpRequest.ERRORRETCODE_ConnectException);
			if(null != handlerList) {
				for (IRequestExceptionHandler handler : handlerList) {
					handler.httpRequestException(e, info);
				}
			}
		}catch(org.apache.http.conn.ConnectTimeoutException e){
			ex = e;
			//创建连接超时
			vo.setResult("创建通信连接超时异常。ConnectTimeoutException："+e.getMessage());
			vo.setCode(HttpRequest.ERRORRETCODE_ConnectException);
			if(null != handlerList) {
				for (IRequestExceptionHandler handler : handlerList) {
					handler.httpRequestException(e, info);
				}
			}
		}catch(org.apache.http.NoHttpResponseException e){
			ex = e;
			//对方无返回报文异常
			vo.setResult("未获取到对方到返回值异常。NoHttpResponseException："+e.getMessage());
			vo.setCode(HttpRequest.ERRORRETCODE_EXCEPTION);
			if(null != handlerList) {
				for (IRequestExceptionHandler handler : handlerList) {
					handler.httpRequestException(e, info);
				}
			}
		} catch (Exception e) {
			ex = e;
			vo.setResult("未定义的异常："+e.getMessage());
			vo.setCode(HttpRequest.ERRORRETCODE_EXCEPTION);
			if(null != handlerList && !isStopReq) {
				for (IRequestExceptionHandler handler : handlerList) {
					handler.otherException(e, info);
				}
			}
		} finally {
			if(null != ex){
				try {
					manager.close(clientKey);
				} catch (Exception e) {
					e.printStackTrace();
				}
				vo.setException(ex);
				ex.printStackTrace();
			} 
			if(null != requestBodyMessage) {
				requestBodyMessage.releaseConnection();
			}
		}
		return vo;
	}
	
	@Override
	public String toString() {
		StringBuffer sbf = new StringBuffer();
		sbf.append("发送HTTP请求: url = ");
		sbf.append(url);
		sbf.append("\t");
		sbf.append("type = ");
		sbf.append(type);
		sbf.append("\t");

		sbf.append("isLongConnection = ");
		sbf.append(isLongConnection);
		sbf.append("\t");
		
		sbf.append("Config = ");
		sbf.append(config.toString());
		sbf.append("\t");
		
		
		return super.toString();
	}
	
	/**
	 * 异常字符串处理方法
	 * 
	 * @param e
	 * @return
	 */
	public static String getExceptionStack(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw, true));
		return sw.toString();
	}
}

package test.com.kasite.client.crawler.micro;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringEscapeUtils;

import com.alibaba.fastjson.JSONObject;

public class SaveCallLogBuser {
	private String url;
	private static SaveCallLogBuser install;
	private SaveCallLogBuser() {

	}
	public void setUrl(String url) {
		this.url = url;
	}

	public static SaveCallLogBuser getInstall() {
		if(null == install) {
			synchronized (obj) {
				install = new SaveCallLogBuser();
			}
		}
		return install;
	}
	  private static HttpClient client;
	  private final static Object obj = new Object();
	  public static HttpClient getClient() {
		  if(null == client) {
			  synchronized (obj) {
				  client = new HttpClient();
				// url的连接等待超时时间设置
					client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
					// 读取数据超时时间设置
					client.getHttpConnectionManager().getParams().setSoTimeout(10000);
			  }
		  }
		  return client;
	  }
	  
	  public static String httpPost(String url,String param) throws Exception{
		    String returnStr = "";
			HttpClient client = getClient();
			PostMethod postMethod = new PostMethod(url);
			try {
				Thread.sleep(100);
//				postMethod.setRequestHeader("Connection", "close");  
				postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
				postMethod.addParameter("reqParam", param);
				// 执行postMethod
				int statusCode = client.executeMethod(postMethod);
				// 判断回复响应状态是否正常
				if (statusCode == HttpStatus.SC_OK) {
					returnStr = new String(postMethod.getResponseBodyAsString());
					//格式化返回值
					returnStr = StringEscapeUtils.unescapeXml(returnStr);
				}else {
					returnStr = new String(postMethod.getResponseBodyAsString());
					throw new Exception("请求日志接口异常返回状态码：httpstatus = "+ statusCode +" 返回结果 = "+returnStr);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				client = null;
				throw ex;
			} finally {
				// 释放连接
				if (postMethod != null) {
					postMethod.releaseConnection();
					try {
						if(null != client) {
							client.getHttpConnectionManager().closeIdleConnections(10);
						}
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
			}
		return returnStr;
	}
	public void saveWsgwLog(WsgwLogVo vo) throws Exception {
		String param = JSONObject.toJSONString(vo);
		httpPost(url, param);
	}
}

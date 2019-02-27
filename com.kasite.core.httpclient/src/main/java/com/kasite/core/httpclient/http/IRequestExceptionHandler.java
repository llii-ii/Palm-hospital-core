package com.kasite.core.httpclient.http;
/**
 * 当接口请求通信发送异常的时候 处理异常回调的监听告知需要处理异常
 * 这里如果实现该接口需要做好异常判断，处理异常的方法不要再抛出异常了
 * 另外这会影响接口返回：异常情况下接口同步执行此方法。方法内部可以针对接口是否进行断路处理，即如果设置了关闭该接口则下次再调用此
 * 接口则直接返回接口异常不继续进行网络访问。
 * @author daiyanshui
 *
 */
public interface IRequestExceptionHandler {
	/**
	 * 返回状态下一次请求发送  是否需要继续发往服务端
	 * @param info
	 * @return true 则关闭此请求，返回false 则不关闭。如果有实现该接口请默认返回false
	 */
	boolean isStopRequest(ReqInfo info);
	
	/**
	 * 请求地址不可达的时候抛出异常（请求未发送到服务端）
	 * @param e
	 * @param info
	 */
	void httpRequestException(java.net.ConnectException e,ReqInfo info) ;
	
	/**
	 * 表示服务器响应的超时，即服务器已经收到了请求但是没有给客户端进行有效的返回；（请求已发送到服务端）
	 * @param e
	 * @param info
	 */
	void httpRequestException(java.net.SocketTimeoutException e,ReqInfo info) ;
	
	/**
	 * 从连接池中获取连接异常，一般是我们自己的请求端（客户端连接池设置太小或者大量请求堵塞）（请求未发送到服务端）
	 * @param e
	 * @param info
	 */
	void httpRequestException(org.apache.http.conn.ConnectionPoolTimeoutException e,ReqInfo info) ;
	
	/**
	 * 通信异常无法将请求发往对方,这类异常问题一般是通信的中间发生网络无法访问3次握手未完成（客户端连接池设置太小或者大量请求堵塞）（请求未发送到服务端）
	 * @param e
	 * @param info
	 */
	void httpRequestException(java.net.SocketException e,ReqInfo info) ;
	/**
	 * 指服务器请求超时，指在请求的时候客户端无法连接上服务端（请求未发送到服务端）
	 * 一般 ConnectTimeoutException 异常可以尝试再次发送请求到服务端。
	 * @param e
	 * @param info
	 */
	void httpRequestException(org.apache.http.conn.ConnectTimeoutException e,ReqInfo info) ;
	
	/**
	 * 当服务器端由于负载过大等情况发生时，可能会导致在收到请求后无法处理
	 * (比如没有足够的线程资源)，会直接丢弃链接而不进行处理。此时客户端就回报错：
	 * NoHttpResponseException。 建议出现这种情况时，可以选择重试
	 * @param e
	 * @param info
	 */
	void httpRequestException(org.apache.http.NoHttpResponseException e,ReqInfo info) ;
	
	/**
	 * 其它未定义的异常
	 * @param e
	 * @param info
	 */
	void otherException(Exception e,ReqInfo info) ;
	
	public class ReqInfo{
		private String clientKey;
		private String url;
		private RequestType type;
		public String getClientKey() {
			return clientKey;
		}
		public void setClientKey(String clientKey) {
			this.clientKey = clientKey;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public RequestType getType() {
			return type;
		}
		public void setType(RequestType type) {
			this.type = type;
		}
		
	}
}

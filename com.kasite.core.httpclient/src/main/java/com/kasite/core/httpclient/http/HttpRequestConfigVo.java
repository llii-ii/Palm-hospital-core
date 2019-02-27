package com.kasite.core.httpclient.http;



public class HttpRequestConfigVo {
	//默认值
	public final static Integer DEFAULT_HTTP_MAXTOTAL = 200;//200;//默认 最大连接数 200 个连接
	public final static Integer DEFAULT_HTTP_MAXPERROUTE = 40;//40;//默认 每个路由基础的连接 40
	public final static Integer DEFAULT_HTTP_MAXROUTE = 100;//100;//默认 目标主机的最大连接数 100
	public final static Integer DEFAULT_HTTP_TIMEOUT = 30000; //30 秒内等待本地连接池连接 默认 从连接池获取连接 超时异常
	public final static Integer DEFAULT_HTTP_EXECUTIONCOUNT = 2; //默认重试2次
	public final static Integer DEFAULT_HTTP_CONNECTTIMEOUT = 2*60000; //2分钟 发送数据接口连接超时30秒  请求客服端并传输数据超时异常
	public final static Integer DEFAULT_HTTP_SOCKETTIMEOUT = 2*60000; //2分钟 从服务端获取数据 socketTimeout接口连接超时30秒  从服务端返回数据超时异常

	public HttpRequestConfigVo init(){
		this.http_executionCount = DEFAULT_HTTP_EXECUTIONCOUNT;
		this.http_maxPerRoute = DEFAULT_HTTP_MAXPERROUTE;
		this.http_maxRoute = DEFAULT_HTTP_MAXROUTE;
		this.http_maxTotal = DEFAULT_HTTP_MAXTOTAL;
		this.http_timeOut = DEFAULT_HTTP_TIMEOUT;
		this.connectTimeOut = DEFAULT_HTTP_CONNECTTIMEOUT;
		this.socketTime = DEFAULT_HTTP_SOCKETTIMEOUT;
		this.isLongConnection = true;
		return this;
	}
	
	public HttpRequestConfigVo(){
		init();
	}
	/**连接超时connetionTimeout：指客户端和服务器建立连接的timeout， 
		就是http请求的三个阶段，一：建立连接；二：数据传送；三，断开连接。超时后会ConnectionTimeOutException
	*/
	private int connectTimeOut;
	/**socketTimeout：指客户端从服务器读取数据的timeout，超出后会抛出SocketTimeOutException*/
	private int socketTime;
	/**是否为长连接 默认是true*/
	private boolean isLongConnection;
	/**最大连接数 */
	private int http_maxTotal;
	/**每个路由基础的连接 */
	private int http_maxPerRoute;
	/**目标主机的最大连接数 */
	private int http_maxRoute;
	/**请求的超时 */
	private int http_timeOut;
	/**请求重试次数 */
	private int http_executionCount;
	/**请求header入参配置:JsonObject 的方式进行传入，如果不是json对象则无法解析到头部里*/
	private String headerJsonObjStrConfig;
	
	public String getHeaderJsonObjStrConfig() {
		return headerJsonObjStrConfig;
	}
	public void setHeaderJsonObjStrConfig(String headerJsonObjStrConfig) {
		this.headerJsonObjStrConfig = headerJsonObjStrConfig;
	}
	public int getConnectTimeOut() {
		if(connectTimeOut < 30000){
			return DEFAULT_HTTP_CONNECTTIMEOUT;
		}
		return connectTimeOut;
	}
	public void setConnectTimeOut(int connectTimeOut) {
		this.connectTimeOut = connectTimeOut;
	}
	public int getSocketTime() {
		if(socketTime < 30000){
			return DEFAULT_HTTP_SOCKETTIMEOUT;
		}
		return socketTime;
	}
	public void setSocketTime(int socketTime) {
		this.socketTime = socketTime;
	}
	//如果小于200 则默认值 500
	public int getHttp_maxTotal() {
		if(http_maxTotal < 200){
			return DEFAULT_HTTP_MAXTOTAL;
		}
		return http_maxTotal;
	}
	public void setHttp_maxTotal(int http_maxTotal) {
		this.http_maxTotal = http_maxTotal;
	}
	public int getHttp_maxPerRoute() {
		return http_maxPerRoute;
	}
	public void setHttp_maxPerRoute(int http_maxPerRoute) {
		this.http_maxPerRoute = http_maxPerRoute;
	}
	public int getHttp_maxRoute() {
		return http_maxRoute;
	}
	public void setHttp_maxRoute(int http_maxRoute) {
		this.http_maxRoute = http_maxRoute;
	}
	public int getHttp_timeOut() {
		if(http_timeOut<8000){
			return DEFAULT_HTTP_TIMEOUT;
		}
		return http_timeOut;
	}
	public void setHttp_timeOut(int http_timeOut) {
		this.http_timeOut = http_timeOut;
	}
	public int getHttp_executionCount() {
		if(this.http_executionCount < 3){
			return DEFAULT_HTTP_EXECUTIONCOUNT;
		}
		return http_executionCount;
	}
	public void setHttp_executionCount(int http_executionCount) {
		this.http_executionCount = http_executionCount;
	}
	public boolean isLongConnection() {
		return isLongConnection;
	}
	public void setLongConnection(boolean isLongConnection) {
		this.isLongConnection = isLongConnection;
	}
	
	@Override
	public String toString() {
		
		StringBuffer sbf = new StringBuffer();

		sbf.append("connectTimeOut=").append(this.connectTimeOut).append("\t");

		sbf.append("http_timeOut=").append(this.http_timeOut).append("\t");

		sbf.append("socketTime=").append(this.socketTime).append("\t");
		
		sbf.append("headerConfig=").append(this.headerJsonObjStrConfig).append("\t");

		sbf.append("http_maxPerRoute=").append(this.http_maxPerRoute).append("\t");

		sbf.append("http_maxRoute=").append(this.http_maxRoute).append("\t");

		sbf.append("http_maxTotal=").append(this.http_maxTotal).append("\t");

		sbf.append("http_executionCount=").append(this.http_executionCount).append("\t");
		
		
		return sbf.toString();
	}
}

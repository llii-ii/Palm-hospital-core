/**
 * 
 */
package com.kasite.core.common.log;
import java.text.SimpleDateFormat;
import java.util.Date;
/**日志头(日志的公共信息)
 * @author chenwu
 *
 */
public class LogHeader  implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 日志产生的服务器IP
	 */
	private String ip;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * 类名
	 */
	private String className;
	/**
	 * 行号
	 */
	private int lineNumber;
	/**
	 * 方法
	 */
	private String methodName;
	/**
	 * 应用部署的路径
	 */
	private String path;
	/**
	 * 应用名称(你的war包名称)
	 */
	private String appName;
	//private String classFileName;
	//public String getClassFileName() {
	//	return classFileName;
	//}
	//public void setClassFileName(String classFileName) {
	//	this.classFileName = classFileName;
	//}
	@Override
	/**
	 * 格式[当前时间|ip|应用名称|类名|行号|方法名]
	 */
	public String toString()
	{
		try
		{
			
			StringBuffer  sb=new StringBuffer();
			Date date = new Date();
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
	    	sb.append(format.format(date));
			sb.append("\t");
			sb.append(this.ip);
			sb.append("\t");
			sb.append(this.className==null?"":this.className);
			sb.append("\t");
			sb.append(this.lineNumber);
			sb.append("\t");
			sb.append(this.methodName==null?"":this.methodName);
			sb.append("\t");
			return sb.toString();	
			
			
		}
		catch(Exception e)
		{
			return e.getMessage();
		}
	}
	
}

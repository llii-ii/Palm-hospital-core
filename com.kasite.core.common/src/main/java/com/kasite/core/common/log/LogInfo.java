/**
 * 
 */
package com.kasite.core.common.log;


/**完整的日志信息
 * @author chenw
 *
 */
public class LogInfo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 *  日志头
	 */
	private LogHeader header;
	private long currTimes;
	
	public long getCurrTimes() {
		return currTimes;
	}
	public void setCurrTimes(long currTimes) {
		this.currTimes = currTimes;
	}
	public LogHeader getHeader() {
		return header;
	}
	public void setHeader(LogHeader header) {
		this.header = header;
	}
	public LogLevel getLevel() {
		return level;
	}
	public void setLevel(LogLevel level) {
		this.level = level;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	private String sessionKey;
	private String userId;
	/**
	 * 日志等级
	 */
	private LogLevel  level;
	/**
	 * 模块名称
	 */
	private String moduleName;
	/**
	 * 内容
	 */
	private String content;
	@Override
	public String toString()
	{
		try
		{
			
			StringBuffer  sb=new StringBuffer();
			sb.append(this.sessionKey)
			.append(this.userId);
			sb.append(this.header);
			sb.append(this.level);
			sb.append("\t");
			sb.append(this.content);
			return sb.toString();	
			
			
		}
		catch(Exception e)
		{
			return e.getMessage();
		}
	}
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}

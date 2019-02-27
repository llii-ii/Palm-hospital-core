package com.yihu.hos;

import java.io.Serializable;

import org.dom4j.Element;
/**
 * 接口配置实体类
 * @author Administrator
 *
 */
public class DModule implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2072491344257408196L;
	/**
	 * 对应配置文件
	 */
	private Element element;
	/**
	 * 内部调用实例服务对象的Key 暂时用的是ghthospitalid
	 */
	private String key;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	/**
	 * 接口应用名字
	 */
	private String cname;
	/**
	 * 具体实现类
	 */
	private String clazz;
	/**
	 * 是否开启测试
	 */
	private boolean isDebug;
	/**
	 * 开发的时候远程调试使用。
	 */
	private String testid;
	
	private String interfaceClass;
	
	public String getTestid() {
		return testid;
	}
	public void setTestid(String testid) {
		this.testid = testid;
	}
	public boolean isDebug() {
		return isDebug;
	}
	public String getInterfaceClass() {
		return interfaceClass;
	}
	public void setInterfaceClass(String interfaceClass) {
		this.interfaceClass = interfaceClass;
	}
	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	public DModule(String key, String cname, String clazz,String interfaceClass) {
		super();
		this.key = key;
		this.cname = cname;
		this.clazz = clazz;
		this.interfaceClass = interfaceClass;
	}
	public Element getElement() {
		return element;
	}
	public void setElement(Element element) {
		this.element = element;
	}

	
}

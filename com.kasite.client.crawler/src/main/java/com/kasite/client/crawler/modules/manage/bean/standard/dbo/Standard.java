package com.kasite.client.crawler.modules.manage.bean.standard.dbo;

import java.util.Date;

/**
 * 数据标准集数据库对象
 * 
 * @author cjy
 * @version V1.0
 * @date 2018年6月27日 上午11:47:33
 */
public class Standard {
	
	private String id;
	private String name;
	private String url;
	private String standard_describe;
	private String version;
	private String del_flag;
	private String standard_type;
	private String currently;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDel_flag() {
		return del_flag;
	}
	public void setDel_flag(String del_flag) {
		this.del_flag = del_flag;
	}
	public String getStandard_describe() {
		return standard_describe;
	}
	public void setStandard_describe(String standard_describe) {
		this.standard_describe = standard_describe;
	}
	public String getStandard_type() {
		return standard_type;
	}
	public void setStandard_type(String standard_type) {
		this.standard_type = standard_type;
	}
	public String getCurrently() {
		return currently;
	}
	public void setCurrently(String currently) {
		this.currently = currently;
	}
	

}

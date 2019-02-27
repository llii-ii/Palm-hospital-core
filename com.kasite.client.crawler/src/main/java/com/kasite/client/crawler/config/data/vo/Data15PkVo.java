package com.kasite.client.crawler.config.data.vo;

public class Data15PkVo {
	/**1内部标识*/
	private String privateName;
	/**9列类型*/
	private String privateType;
	/**0.表示非主键 1.表示主键*/
	private Integer isKey;
	/**0 表示不能为空 1表示可以为空*/
	private Integer isNotNum;
	/**3数据元名称*/
	private String privateDes;
	/**术语范围值*/
	private String privateDictName;
	/**列名 表名*/
	private String headIndex;
	/**数据类型*/
	private String dataType;
	/**4数据元定义*/
	private String name;
	/**10 sql语句*/
	private String sql;
	
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getPrivateName() {
		return privateName;
	}
	public void setPrivateName(String privateName) {
		this.privateName = privateName;
	}
	public String getPrivateType() {
		return privateType;
	}
	public void setPrivateType(String privateType) {
		this.privateType = privateType;
	}
	public Integer getIsKey() {
		return isKey;
	}
	public void setIsKey(Integer isKey) {
		this.isKey = isKey;
	}
	/***
	 * 0 表示不可为空
	 * 1 表示可以为空
	 * @return
	 */
	public Integer getIsNotNum() {
		return isNotNum;
	}
	public void setIsNotNum(Integer isNotNum) {
		this.isNotNum = isNotNum;
	}
	public String getPrivateDes() {
		return privateDes;
	}
	public void setPrivateDes(String privateDes) {
		this.privateDes = privateDes;
	}
	public String getPrivateDictName() {
		return privateDictName;
	}
	public void setPrivateDictName(String privateDictName) {
		this.privateDictName = privateDictName;
	}
	public String getHeadIndex() {
		return headIndex;
	}
	public void setHeadIndex(String headIndex) {
		this.headIndex = headIndex;
	}
	
	
}

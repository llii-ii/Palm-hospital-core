package com.kasite.core.common.sys.runcmd.entity;


public class VersionEntity {
	/**版本号*/
	private String number;
	/**文件哈希 */
	private String build_hash;
	/**文件当前版本生成时间*/
	private String build_timestamp;
	/**支持的jdk的版本*/
	private String jdk_version;
	/**支持的mysql的版本*/
	private String mysql_version;
	/**支持的elasticsearch的版本*/
	private String elasticsearch_version; 
	/**是否进行强更*/
	private boolean forced_upgrade;
	/**文件url下载地址*/
	private String url;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isForced_upgrade() {
		return forced_upgrade;
	}
	public void setForced_upgrade(boolean forced_upgrade) {
		this.forced_upgrade = forced_upgrade;
	}
	public String getJdk_version() {
		return jdk_version;
	}
	public void setJdk_version(String jdk_version) {
		this.jdk_version = jdk_version;
	}
	public String getMysql_version() {
		return mysql_version;
	}
	public void setMysql_version(String mysql_version) {
		this.mysql_version = mysql_version;
	}
	public String getElasticsearch_version() {
		return elasticsearch_version;
	}
	public void setElasticsearch_version(String elasticsearch_version) {
		this.elasticsearch_version = elasticsearch_version;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getBuild_hash() {
		return build_hash;
	}
	public void setBuild_hash(String build_hash) {
		this.build_hash = build_hash;
	}
	public String getBuild_timestamp() {
		return build_timestamp;
	}
	public void setBuild_timestamp(String build_timestamp) {
		this.build_timestamp = build_timestamp;
	}
	
	
}

package com.kasite.core.common.resp;

/**
 * 
 * @className: ParseConfig
 * @author: lcz
 * @date: 2018年7月19日 下午9:35:51
 */
public class ParseConfig {
	
	/**
	 * 首字符大写
	 */
	private static final boolean UPPERCASE = true;
	
	private boolean uppercase;
	/**
	 * 所需字段 忽略大小写 
	 * 当所需字段和排除字段同时存在时，所需字段优先
	 * 格式：data=id,reportTitle,name,data_1|data_1=itemCode,itemName,unit
	 */
	private String columns;
	/**
	 * 排除字段  忽略大小写   
	 * 当所需字段和排除字段同时存在时，所需字段优先
	 * 格式：data=id,reportTitle,name,data_1|data_1=itemCode,itemName,unit
	 */
	private String excludeColumns;
	
	
	ParseConfig(){
		this.uppercase = UPPERCASE;
	}
	ParseConfig(boolean uppercase){
		this.uppercase = uppercase;
	}
	
	
	public String getColumns() {
		return columns;
	}
	public void setColumns(String columns) {
		this.columns = columns;
	}
	public String getExcludeColumns() {
		return excludeColumns;
	}
	public void setExcludeColumns(String excludeColumns) {
		this.excludeColumns = excludeColumns;
	}
	public boolean isUppercase() {
		return uppercase;
	}
	public ParseConfig uppercase(boolean uppercase) {
		this.uppercase = uppercase;
		return this;
	}
	
	
	
}

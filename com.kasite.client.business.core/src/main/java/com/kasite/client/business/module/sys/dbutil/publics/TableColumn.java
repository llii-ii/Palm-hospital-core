package com.kasite.client.business.module.sys.dbutil.publics;

/**
 * 表结构对应的详细
 * 
 * @author zhaoy
 * @version 1.0
 * 2019-01-28
 */
@SuppressWarnings("serial")
public class TableColumn {
	/** 字段 */
	private String columnsFiled;

	/** 数据类型 */
	private String columnsType;

	/** 是否允许为空 */
	private String columnsNull;

	/** 主键 */
	private String columnsKey;

	/** 默认值 */
	private String columnsDefault;

	/** 附加 */
	private String columnsExtra;

	public String getColumnsFiled() {
		return columnsFiled;
	}

	public void setColumnsFiled(String columnsFiled) {
		this.columnsFiled = columnsFiled;
	}

	public String getColumnsType() {
		return columnsType;
	}

	public void setColumnsType(String columnsType) {
		this.columnsType = columnsType;
	}
	
	public String getColumnsNull() {
		return columnsNull;
	}

	public void setColumnsNull(String columnsNull) {
		this.columnsNull = columnsNull;
	}

	public String getColumnsKey() {
		return columnsKey;
	}

	public void setColumnsKey(String columnsKey) {
		this.columnsKey = columnsKey;
	}

	public String getColumnsDefault() {
		return columnsDefault;
	}

	public void setColumnsDefault(String columnsDefault) {
		this.columnsDefault = columnsDefault;
	}

	public String getColumnsExtra() {
		return columnsExtra;
	}

	public void setColumnsExtra(String columnsExtra) {
		this.columnsExtra = columnsExtra;
	}
}

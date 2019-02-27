package com.kasite.core.serviceinterface.module.job.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 
 * @className: IJobMapper
 * @author: lcz
 * @date: 2018年8月1日 下午3:18:34
 */
public interface IJobMapper {
	@Select("SELECT COUNT(TABLE_NAME) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME=#{tableName} AND TABLE_SCHEMA=#{tableSchema}")
	int getTable(@Param("tableName")String tableName,@Param("tableSchema")String tableSchema);
	
	@Select("CREATE TABLE ${backTable} as select * from ${dataTable} where ${createTime} < DATE_SUB(CURDATE(),INTERVAL ${month} MONTH)")
	void createAndBackData(@Param("backTable")String backTable,@Param("dataTable")String dataTable,@Param("createTime")String createTime,@Param("month")int month);
	
	@Select("INSERT INTO ${backTable} SELECT * FROM ${dataTable} WHERE ${createTime} < DATE_SUB(CURDATE(),INTERVAL ${month} MONTH)")
	void backData(@Param("backTable")String backTable,@Param("dataTable")String dataTable,@Param("createTime")String createTime,@Param("month")int month);
	
	@Select("DELETE FROM ${dataTable} WHERE ${createTime} < DATE_SUB(CURDATE(),INTERVAL ${month} MONTH)")
	void deleteData(@Param("dataTable")String dataTable,@Param("createTime")String createTime,@Param("month")int month);
}

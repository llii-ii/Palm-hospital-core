package com.kasite.core.common.mappers;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;

import com.kasite.core.common.mappers.providers.BatchInsertProvider;

import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * 批量新增通用Mapper
 * @className: BatchInsertMapper
 * @author: lcz
 * @date: 2018年9月10日 下午9:54:34
 */
@RegisterMapper
public interface BatchInsertMapper<T> {
	
	/**
	 * 批量新增，支持UID主键,该方法为全量新增，即：对象的值为空时，也会把数据库中的字段值设置为null,不会使用数据库中的默认设置
	 * @Description: 
	 * @param recordList
	 * @return
	 */
    @InsertProvider(type = BatchInsertProvider.class, method = "dynamicSQL")
    int batchInsert(List<T> recordList);
	
}

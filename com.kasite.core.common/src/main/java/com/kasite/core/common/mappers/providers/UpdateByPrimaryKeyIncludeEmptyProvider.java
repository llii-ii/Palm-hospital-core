package com.kasite.core.common.mappers.providers;

import org.apache.ibatis.mapping.MappedStatement;

import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

/**
 * 
 * @className: UpdateExcludeNullProvider
 * @author: lcz
 * @date: 2018年9月3日 下午12:00:58
 */
public class UpdateByPrimaryKeyIncludeEmptyProvider extends MapperTemplate{
	
	 	public UpdateByPrimaryKeyIncludeEmptyProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
	        super(mapperClass, mapperHelper);
	    }

	    /**
	     * 通过主键更新非null的字段，更新字段为空串时，也会更新
	     *
	     * @param ms
	     */
	    public String updateByPrimaryKeyIncludeEmpty(MappedStatement ms) {
	        Class<?> entityClass = getEntityClass(ms);
	        StringBuilder sql = new StringBuilder();
	        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
	        sql.append(SqlHelper.updateSetColumns(entityClass, null, true, false));
	        sql.append(SqlHelper.wherePKColumns(entityClass, true));
	        return sql.toString();
	    }

}

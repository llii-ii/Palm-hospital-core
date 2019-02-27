package com.kasite.core.common.mappers.providers;

import java.util.Set;

import org.apache.ibatis.mapping.MappedStatement;

import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

/**
 * 
 * @className: BatchInsertProvider
 * @author: lcz
 * @date: 2018年9月10日 下午9:59:30
 */
public class BatchInsertProvider extends MapperTemplate{
	
	 public BatchInsertProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
	        super(mapperClass, mapperHelper);
	    }

	 	/**
	     * 批量插入
	     *
	     * @param ms
	     */
	    public String batchInsert(MappedStatement ms) {
	    	 final Class<?> entityClass = getEntityClass(ms);
		        //开始拼sql
		        StringBuilder sql = new StringBuilder();
		        sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
		        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
		        //获取全部列
		        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
		        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
		        for (EntityColumn column : columnList) {
		            if (!column.isInsertable()) {
		                continue;
		            }
		            if (column.isIdentity() && column.getColumnHolder("record")==null) {
		                continue;
		            }
	                sql.append(column.getColumn() + ",");
		        }
		        sql.append("</trim>");
		        sql.append(" VALUES ");
		        sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
		        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
		        
		        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
		        for (EntityColumn column : columnList) {
		        	 if (!column.isInsertable()) {
		                continue;
		            }
		            if (column.isIdentity() && column.getColumnHolder("record")==null) {
		                continue;
		            }
	                sql.append(column.getColumnHolder("record") + ",");
		        }
		        sql.append("</trim>");
		        sql.append("</foreach>");
		        return sql.toString();
	    }
}
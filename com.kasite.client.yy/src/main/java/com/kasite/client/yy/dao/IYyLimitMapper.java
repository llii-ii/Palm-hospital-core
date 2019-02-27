package com.kasite.client.yy.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.client.yy.bean.dbo.YyLimit;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @className: IYyLimit
 * @author: lcz
 * @date: 2018年7月21日 下午1:52:45
 */
public interface IYyLimitMapper extends Mapper<YyLimit>{
	
	@Select("${queryLimitSql}")
	int queryCount(@Param("queryLimitSql")String queryLimitSql);
}

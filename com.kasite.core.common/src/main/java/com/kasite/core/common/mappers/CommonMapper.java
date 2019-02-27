package com.kasite.core.common.mappers;

import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * 通用Mybatis扩展,
 * 注意：该接口属于通用Mapper接口，请勿将该类所在路径配置成Mybatis  Mapper扫描的路径，否则将导致系统启动异常。
 * @className: BaseMapper
 * @author: lcz
 * @date: 2018年9月3日 下午12:09:12
 */
@RegisterMapper
public interface CommonMapper<T> extends 
			BatchInsertMapper<T>,
			UpdateByPrimaryKeyIncludeEmptyMapper<T>,
			Mapper<T> {

}
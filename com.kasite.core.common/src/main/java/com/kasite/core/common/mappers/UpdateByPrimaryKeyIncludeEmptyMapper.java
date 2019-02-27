package com.kasite.core.common.mappers;

import org.apache.ibatis.annotations.UpdateProvider;

import com.kasite.core.common.mappers.providers.UpdateByPrimaryKeyIncludeEmptyProvider;

import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * 通用mybatis的扩展，新增不过滤空串的更新方法，
 * 注意：该接口属于通用Mapper接口，请勿将该类所在路径配置成Mybatis  Mapper扫描的路径，否则将导致系统启动异常。
 * @className: UpdateByPrimaryKeyExcludeNullMapper
 * @author: lcz
 * @date: 2018年9月3日 上午11:59:41
 */
@RegisterMapper
public interface UpdateByPrimaryKeyIncludeEmptyMapper<T> {
	/**
     * 根据主键更新属性不为null的值
     *
     * @param record
     * @return
     */
    @UpdateProvider(type = UpdateByPrimaryKeyIncludeEmptyProvider.class, method = "dynamicSQL")
    int updateByPrimaryKeyIncludeEmpty(T record);
}

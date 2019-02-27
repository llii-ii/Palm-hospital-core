package com.kasite.core.common.sys.service;

import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.sys.service.pojo.SysUserEntity;
import com.kasite.core.common.sys.service.pojo.SysUserTokenEntity;

/**
 * shiro相关接口
 * @author daiys
 * @email 343675979@qq.com
 */
public interface ShiroService {
    /**
     * 获取用户权限列表
     */
    Set<String> getUserPermissions(long userId) throws Exception;
    Set<String> getUserPermissions(long userId,boolean isAdmin) throws Exception;
    JSONObject getUserPermsForJSONObject(long userId,boolean isAdmin) throws Exception;

    SysUserTokenEntity queryByToken(String token) throws Exception;

    /**
     * 根据用户ID，查询用户
     * @param userId
     */
    SysUserEntity queryUser(Long userId) throws Exception;    /**
     * 根据用户ID，查询用户
     * @param userId
     */
    SysUserEntity getUser(String userName) throws Exception;
    
    /**
     * 获取当前用户登录的User Token 一般在记日志的地方使用，其它地方不要使用，有需要强制判断的时候最好还是直接从数据库读取
     * @param userId
     * @return
     * @throws Exception
     */
    SysUserTokenEntity getSysUserToken(Long userId) throws Exception;

	SysUserEntity getUser();
    
    
}

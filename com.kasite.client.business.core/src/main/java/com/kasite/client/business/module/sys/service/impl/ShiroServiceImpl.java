package com.kasite.client.business.module.sys.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.kasite.client.business.config.ShiroUtils;
import com.kasite.client.business.module.sys.dao.SysMenuDao;
import com.kasite.client.business.module.sys.dao.SysUserDao;
import com.kasite.core.common.sys.service.RedisUtil;
import com.kasite.core.common.sys.service.ShiroService;
import com.kasite.core.common.sys.service.SysUserService;
import com.kasite.core.common.sys.service.SysUserTokenService;
import com.kasite.core.common.sys.service.pojo.SysUserEntity;
import com.kasite.core.common.sys.service.pojo.SysUserTokenEntity;
import com.kasite.core.common.util.StringUtil;



@Service("shiroService")
public class ShiroServiceImpl implements ShiroService {

	@Autowired
	private SysUserTokenService sysUserTokenService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysMenuDao sysMenuDao;
	@Autowired
	private SysUserDao sysUserDao;
	
	
    @Override
    public Set<String> getUserPermissions(long userId) throws Exception {
    	List<String> permsList = null;
    	Integer num = sysUserDao.isAdmin(userId);
    	if(num!=null && num>0) {
        	//管理员，查询所有权限
    		permsList = sysMenuDao.queryAllPerms();
        }else {
        	permsList = sysUserDao.queryUserAllPerms(userId);
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for(String perms : permsList){
            if(StringUtil.isBlank(perms)){
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
    	return permsSet;
    }
    @Override
    public Set<String> getUserPermissions(long userId,boolean isAdmin) throws Exception {
        List<String> permsList = null;
        if(isAdmin) {
        	//管理员，查询所有权限
        	permsList = sysMenuDao.queryAllPerms();
        }else {
        	permsList = sysUserDao.queryUserAllPerms(userId);
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for(String perms : permsList){
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
       return permsSet;
    }
    
    
    @Override
	public JSONObject getUserPermsForJSONObject(long userId, boolean isAdmin) throws Exception {
    	List<String> permsList = null;
        if(isAdmin) {
        	//管理员，查询所有权限
        	permsList = sysMenuDao.queryAllPerms();
        }else {
        	permsList = sysUserDao.queryUserAllPerms(userId);
        }
        JSONObject json = new JSONObject();
        for(String perms : permsList){
        	String[] ps = perms.trim().split(",");
        	for (String per : ps) {
        		json.put(per, 1);
			}
        }
        return json;
	}
	@Override
    public SysUserTokenEntity queryByToken(String token) throws Exception {
        return sysUserTokenService.queryByToken(token);
    }

    @Override
    public SysUserEntity queryUser(Long userId) throws Exception {
    	SysUserEntity entity = RedisUtil.create().get(userId);
    	if(null == entity) {
    		entity = sysUserService.queryObject(userId);
    		RedisUtil.create().setK(userId, entity);
    	}
        return entity;
    }

	@Override
	public SysUserTokenEntity getSysUserToken(Long userId) throws Exception {
		SysUserTokenEntity entity = RedisUtil.create().getUserToken(userId);
    	if(null == entity) {
    		entity = sysUserTokenService.queryByUserId(userId);
    		RedisUtil.create().setUserToken(userId, entity);
    	}
        return entity;
	}
	
	@Override
	public SysUserEntity getUser() {
		try {
			return ShiroUtils.getUserEntity();
		}catch (Exception e) {
			return null;
		}
	}

	@Override
	public SysUserEntity getUser(String userName) throws Exception {
		return sysUserService.queryByUserName(userName);
	}
}

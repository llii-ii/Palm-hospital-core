package com.kasite.core.common.sys.service;

import java.util.List;
import java.util.Map;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.sys.req.ReqAddUser;
import com.kasite.core.common.sys.req.ReqDeleteUser;
import com.kasite.core.common.sys.req.ReqGetUserInfo;
import com.kasite.core.common.sys.req.ReqQueryUserKey;
import com.kasite.core.common.sys.req.ReqQueryUserList;
import com.kasite.core.common.sys.req.ReqUpdateUser;
import com.kasite.core.common.sys.req.ReqUpdateUserKey;
import com.kasite.core.common.sys.req.ReqUpdateUserPassword;
import com.kasite.core.common.sys.resp.RespQueryUserKey;
import com.kasite.core.common.sys.resp.RespQueryUserList;
import com.kasite.core.common.sys.service.pojo.SysUserEntity;
import com.kasite.core.common.sys.service.pojo.SysUserRoleEntity;


/**
 * 系统用户
 */
public interface SysUserService {

	void updateUserClientIdAndConfigKey(long userId,SysUserEntity user);
	
	boolean isAdmin(long userId) throws Exception;
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */ 
	List<String> queryUserAllPerms(Long userId) throws Exception;
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryUserAllMenuId(Long userId) throws Exception;

	/**
	 * 根据用户名，查询系统用户
	 */
	SysUserEntity queryByUserName(String username) throws Exception;
	/**
	 * 根据手机号查询当前用户权限
	 * @param mobile
	 * @throws Exception
	 */
	boolean queryUserExist(String mobile) throws Exception;
	/**
	 * 根据用户ID，查询用户角色ID
	 */
	SysUserRoleEntity queryUserRoleById(Long userId) throws Exception;
	/**
	 * 根据用户ID，查询用户
	 * @param userId
	 * @return
	 */
	SysUserEntity queryObject(Long userId) throws Exception;
	
	/**
	 * 查询用户列表
	 */
	List<SysUserEntity> queryList(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询总数
	 */
	int queryTotal(Map<String, Object> map) throws Exception;
	
	/**
	 * 保存用户
	 */
	void save(SysUserEntity user) throws Exception;
	
	/**
	 * 修改用户
	 */
	void update(SysUserEntity user) throws Exception;
	
	/**
	 * 删除用户
	 */
	void deleteBatch(Long[] userIds) throws Exception;
	
	/**
	 * 修改密码
	 * @param userId       用户ID
	 * @param password     原密码
	 * @param newPassword  新密码
	 */
	int updatePassword(Long userId, String password, String newPassword) throws Exception;
	
	CommonResp<RespQueryUserList> queryUserList(CommonReq<ReqQueryUserList> req) throws Exception;
	
	CommonResp<RespQueryUserList> getUserInfo(CommonReq<ReqGetUserInfo> req) throws Exception;
	
	CommonResp<RespMap> deleteUser(CommonReq<ReqDeleteUser> req) throws Exception;
	
	CommonResp<RespMap> addUser(CommonReq<ReqAddUser> req) throws Exception;
	
	CommonResp<RespMap> updateUser(CommonReq<ReqUpdateUser> req) throws Exception;
	CommonResp<RespMap> updateUserPassword(CommonReq<ReqUpdateUserPassword> req) throws Exception;
	
	/**
	 * 校验二级支付密码
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> validateUserPayKey(CommonReq<ReqQueryUserKey> commReq) throws Exception;
	
	/**
	 * 查询二级支付密码
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryUserKey> queryUserPayKey(CommonReq<ReqQueryUserKey> commReq) throws Exception;
	
	/**
	 * 设置二级支付密码
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> updateUserForPayKey(CommonReq<ReqUpdateUserKey> commReq) throws Exception;
	
}

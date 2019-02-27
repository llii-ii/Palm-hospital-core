package com.kasite.client.business.module.sys.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.kasite.client.business.module.sys.dao.SysUserDao;
import com.kasite.client.business.module.sys.dao.SysUserPayKeyDao;
import com.kasite.client.business.module.sys.dao.SysUserRoleDao;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
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
import com.kasite.core.common.sys.service.SysUserService;
import com.kasite.core.common.sys.service.pojo.SysUserEntity;
import com.kasite.core.common.sys.service.pojo.SysUserPayKeyEntity;
import com.kasite.core.common.sys.service.pojo.SysUserRoleEntity;
import com.kasite.core.common.util.BeanCopyUtils;
import com.kasite.core.common.util.StringUtil;

import tk.mybatis.mapper.entity.Example;


/**
 * 系统用户
 */
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserDao sysUserDao;
	
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	@Autowired
	private SysUserPayKeyDao sysUserPayKeyDao;
	
	@Override
	public boolean isAdmin(long userId) throws Exception {
		Integer num = sysUserDao.isAdmin(userId);
		return (num!=null && num>0);
	}

	@Override
	public List<String> queryUserAllPerms(Long userId) throws Exception {
		
		return sysUserDao.queryUserAllPerms(userId);
	}

	@Override
	public List<Long> queryUserAllMenuId(Long userId) throws Exception {
		return sysUserDao.queryUserAllMenuId(userId);
	}

	@Override
	public SysUserEntity queryByUserName(String openId) throws Exception {
		SysUserEntity record = new SysUserEntity();
		record.setUsername(openId);
		return sysUserDao.selectOne(record);
	}

	@Override
	public boolean queryUserExist(String mobile) throws Exception {
		return false;
	}

	@Override
	public SysUserRoleEntity queryUserRoleById(Long userId) throws Exception {
		SysUserRoleEntity record = new SysUserRoleEntity();
		record.setUserId(userId);
		return sysUserRoleDao.selectOne(record);
	}
	
	@Override
	public SysUserEntity queryObject(Long userId) throws Exception {
		return sysUserDao.selectByPrimaryKey(userId);
	}

	@Override
	public List<SysUserEntity> queryList(Map<String, Object> map) throws Exception {
//		return sysUserDao.queryList_Map(map);
		
		return null;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) throws Exception {
//		return sysUserDao.queryTotal_Map(map);
		return 0;
	}

	@Override
	public void save(SysUserEntity user) throws Exception {
		sysUserDao.insertSelective(user);
	}

	@Override
	public void update(SysUserEntity user) throws Exception {
//		if(StringUtils.isBlank(user.getPassword())){
//			user.setPassword(null);
//		}else{
//			user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
//		}
//		long userid = user.getUser_id();
//		List<Long> roles = user.getRoleIdList();
		sysUserDao.updateByPrimaryKeySelective(user);
//		//保存用户与角色关系
//		sysUserRoleService.saveOrUpdate(userid, roles);
	}

	@Override
	public void deleteBatch(Long[] userId) throws Exception {
//		for (Long long1 : userId) {
//			SysUserEntity t = new SysUserEntity();
//			t.setUser_id(long1);
//			t.setStatus(0);
//			update(t);
//		}
	}

	@Override
	public int updatePassword(Long userId, String password, String newPassword) throws Exception {
//		return sysUserDao.updatePassword(userId, password, newPassword);
		return 0;
	}

	@Override
	public CommonResp<RespQueryUserList> queryUserList(CommonReq<ReqQueryUserList> req) throws Exception {
		ReqQueryUserList userReq = req.getParam();
		SysUserEntity user = new SysUserEntity();
		BeanCopyUtils.copyProperties(userReq, user, null);
		List<SysUserEntity> list = null;
		if(userReq.getPage()!=null && userReq.getPage().getPIndex()!=null && userReq.getPage().getPSize()>0) {
			PageHelper.startPage(userReq.getPage().getPIndex()+1, userReq.getPage().getPSize());
			list = sysUserDao.queryUserList(user,1);
			userReq.getPage().initPCount(list);
		}else {
			PageHelper.startPage(1,200);
			list = sysUserDao.queryUserList(user,1);
		}
		List<RespQueryUserList> respList = new ArrayList<RespQueryUserList>();
		for (SysUserEntity uu : list) {
			RespQueryUserList resp = new RespQueryUserList();
			BeanCopyUtils.copyProperties(uu, resp, null);
			respList.add(resp);
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList, userReq.getPage());
	}

	@Transactional
	@Override
	public CommonResp<RespMap> deleteUser(CommonReq<ReqDeleteUser> req) throws Exception {
		ReqDeleteUser delReq = req.getParam();
		if(delReq.getId()==null || delReq.getId()<=0) {
			return new CommonResp<RespMap>(req, RetCode.Common.ERROR_PARAM, "参数Id不能为空。");
		}
		SysUserRoleEntity entity = new SysUserRoleEntity();
		entity.setUserId(delReq.getId());
		sysUserRoleDao.delete(entity);
		sysUserDao.deleteByPrimaryKey(delReq.getId());
		
		return new CommonResp<RespMap>(req, RetCode.Success.RET_10000, "成功");
	}

	@Transactional
	@Override
	public CommonResp<RespMap> addUser(CommonReq<ReqAddUser> req) throws Exception {
		ReqAddUser addReq = req.getParam();
		if(addReq.getRoleId()==null || addReq.getRoleId()<=0) {
			return new CommonResp<RespMap>(req, RetCode.Common.ERROR_PARAM, "参数RoleId不能为空。");
		}
		if(StringUtil.isBlank(addReq.getUsername())) {
			return new CommonResp<RespMap>(req, RetCode.Common.ERROR_PARAM, "参数Username不能为空。");
		}
		if(StringUtil.isBlank(addReq.getPassword())) {
			return new CommonResp<RespMap>(req, RetCode.Common.ERROR_PARAM, "参数Password不能为空。");
		}
		SysUserEntity query = new SysUserEntity();
		query.setUsername(addReq.getUsername());
		int count = sysUserDao.selectCount(query);
		if(count>0) {
			return new CommonResp<RespMap>(req, RetCode.Common.ERROR_PARAM, "用户名："+addReq.getUsername()+"，已经存在，请重新输入。");
		}
		String salt = RandomStringUtils.randomAlphanumeric(20);
		SysUserEntity user = new SysUserEntity();
		user.setChannelId(addReq.getClientId());
		user.setChannelName(addReq.getClientId());
		user.setClientId(addReq.getClientId());
		user.setMobile(addReq.getMobile());
		user.setOperatorId(addReq.getOpenId());
		user.setOperatorName(addReq.getOperatorName());
		user.setOrgId(KasiteConfig.getOrgCode());
		user.setOrgName(KasiteConfig.getOrgName());
		user.setPassword(new Sha256Hash(addReq.getPassword(), salt).toHex());
		user.setRealName(addReq.getRealName());
		user.setSalt(salt);
		user.setStatus(1);
		user.setUsername(addReq.getUsername());
		sysUserDao.insertSelective(user);
		
		SysUserRoleEntity entity = new SysUserRoleEntity();
		entity.setRoleId(addReq.getRoleId());
		entity.setUserId(user.getId());
		sysUserRoleDao.insertSelective(entity);
		
		return new CommonResp<RespMap>(req, RetCode.Success.RET_10000, "成功");
	}

	
	@Transactional
	@Override
	public CommonResp<RespMap> updateUser(CommonReq<ReqUpdateUser> req) throws Exception {
		ReqUpdateUser upReq = req.getParam();
		if(upReq.getId()==null || upReq.getId()<=0) {
			return new CommonResp<RespMap>(req, RetCode.Common.ERROR_PARAM, "参数Id不能为空");
		}
		SysUserEntity user = new SysUserEntity();
		user.setId(upReq.getId());
		user.setMobile(upReq.getMobile());
		user.setOperatorId(upReq.getOpenId());
		user.setOperatorName(upReq.getOperatorName());
		if(StringUtil.isNotBlank(upReq.getPassword())) {
			String salt = RandomStringUtils.randomAlphanumeric(20);
			user.setSalt(salt);
			user.setPassword(new Sha256Hash(upReq.getPassword(), salt).toHex());
		}
		user.setRealName(upReq.getRealName());
		user.setStatus(upReq.getStatus());
		user.setUsername(upReq.getUsername());
		sysUserDao.updateByPrimaryKeySelective(user);
		if(upReq.getRoleId()!=null && upReq.getRoleId()>0) {
			SysUserRoleEntity entity = new SysUserRoleEntity();
			entity.setUserId(upReq.getId());
			int count = sysUserRoleDao.selectCount(entity);
			if(count<=0) {
				entity.setRoleId(upReq.getRoleId());
				sysUserRoleDao.insertSelective(entity);
			}else {
				entity.setRoleId(upReq.getRoleId());
				Example example = new Example(SysUserRoleEntity.class);
				example.createCriteria().andEqualTo("userId", upReq.getId());
				sysUserRoleDao.updateByExampleSelective(entity, example);
			}
		}
		return new CommonResp<RespMap>(req, RetCode.Success.RET_10000, "成功");
	}
	
	

	public CommonResp<RespMap> updateUserPassword(CommonReq<ReqUpdateUserPassword> req) throws Exception {
		ReqUpdateUserPassword upReq = req.getParam();
		SysUserEntity user = new SysUserEntity();
		user.setUsername(upReq.getUsername());
		SysUserEntity entity = sysUserDao.selectOne(user);
		if(entity==null) {
			return new CommonResp<RespMap>(req, RetCode.Common.ERROR_PARAM, "没有找到:"+upReq.getUsername()+"对应的用户信息。");
		}
		//加密获得旧密码
		String oldPass = new Sha256Hash(upReq.getOldPassword(), entity.getSalt()).toHex();
		if(!oldPass.equals(entity.getPassword())) {
			return new CommonResp<RespMap>(req, RetCode.Common.ERROR_PARAM, "修改失败，旧密码错误");
		}
		user.setId(entity.getId());
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setSalt(salt);
		user.setPassword(new Sha256Hash(upReq.getNewPassword(), salt).toHex());
		user.setOperatorId(upReq.getOpenId());
		user.setOperatorName(upReq.getOperatorName());
		sysUserDao.updateByPrimaryKeySelective(user);
		sysUserPayKeyDao.deleteByPrimaryKey(upReq.getOpenId()); //删除用户的支付密码信息
		return new CommonResp<RespMap>(req, RetCode.Success.RET_10000, "成功");
	}

	public CommonResp<RespQueryUserList> getUserInfo(CommonReq<ReqGetUserInfo> req) throws Exception {
		ReqGetUserInfo getReq = req.getParam();
		SysUserEntity user = new SysUserEntity();
		BeanCopyUtils.copyProperties(getReq, user, null);
		List<SysUserEntity> list = sysUserDao.queryUserList(user,1);
		if(list!=null && list.size()>1) {
			return new CommonResp<RespQueryUserList>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_SYSTEM, "查询用户信息异常：count="+list.size());
		}
		
		RespQueryUserList resp = new RespQueryUserList();
		BeanCopyUtils.copyProperties(list.get(0), resp, null);
		
		return new CommonResp<RespQueryUserList>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	@Override
	public void updateUserClientIdAndConfigKey(long userId, SysUserEntity user ) {
		user.setId(userId);
		sysUserDao.updateByPrimaryKeySelective(user);
	}
	
	@Override
	public CommonResp<RespMap> validateUserPayKey(CommonReq<ReqQueryUserKey> commReq) throws Exception {
		ReqQueryUserKey req = commReq.getParam();
		String payKey = req.getPayKey();
		String userId = req.getOpenId();
		SysUserPayKeyEntity userPaykey = sysUserPayKeyDao.selectByPrimaryKey(userId);
		if(!userPaykey.getPayKey().equals(new Sha256Hash(payKey, userId).toHex())) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PAY_PASSWORD);
		}
		return new CommonResp<RespMap>(commReq, RetCode.Success.RET_10000, "成功");
	}

	@Override
	public CommonResp<RespQueryUserKey> queryUserPayKey(CommonReq<ReqQueryUserKey> commReq) throws Exception {
		ReqQueryUserKey req = commReq.getParam();
		String userId = req.getUserId();
		SysUserPayKeyEntity userPaykey = sysUserPayKeyDao.selectByPrimaryKey(userId);
		if(userPaykey == null) {
			return new CommonResp<RespQueryUserKey>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		}
		RespQueryUserKey resp = new RespQueryUserKey();
		resp.setUserId(userId);
		resp.setPayKey(userPaykey.getPayKey());
		return new CommonResp<RespQueryUserKey>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	@Override
	public CommonResp<RespMap> updateUserForPayKey(CommonReq<ReqUpdateUserKey> commReq) throws Exception {
		ReqUpdateUserKey req = commReq.getParam();
		String userId = req.getUserId();
		/*String loginKey = req.getLoginKey();
		SysUserEntity user = this.queryByUserName(userId);
		//登录密码错误
		if(user == null || !user.getPassword().equals(new Sha256Hash(loginKey, user.getSalt()).toHex())) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_LOGIN_PASSWORD);
		}*/
		String payKey = req.getPayKey();
		payKey = new Sha256Hash(payKey, userId).toHex();  //二级密码加密
		
		SysUserPayKeyEntity userPaykey = new SysUserPayKeyEntity();
		userPaykey.setUserId(userId);
		userPaykey.setPayKey(payKey);
		sysUserPayKeyDao.insert(userPaykey);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}
	
	public static void main(String[] args) {
		String salt = RandomStringUtils.randomAlphanumeric(20);
		KasiteConfig.print(salt);
		KasiteConfig.print(new Sha256Hash("96E79218965EB72C92A549DD5A330112", salt).toHex());
	}
}

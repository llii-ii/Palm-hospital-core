package com.kasite.client.business.module.sys.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.kasite.client.business.module.sys.dao.SysMenuDao;
import com.kasite.client.business.module.sys.dao.SysRoleDao;
import com.kasite.client.business.module.sys.dao.SysRoleMenuDao;
import com.kasite.client.business.module.sys.dao.SysUserRoleDao;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.sys.req.ReqAddRole;
import com.kasite.core.common.sys.req.ReqDelRole;
import com.kasite.core.common.sys.req.ReqGetRole;
import com.kasite.core.common.sys.req.ReqQueryMenusByRoleId;
import com.kasite.core.common.sys.req.ReqQueryRoleList;
import com.kasite.core.common.sys.req.ReqUpdateRole;
import com.kasite.core.common.sys.resp.RespQueryMenusByRoleId;
import com.kasite.core.common.sys.resp.RespQueryRoleList;
import com.kasite.core.common.sys.service.SysRoleService;
import com.kasite.core.common.sys.service.pojo.SysMenuEntity;
import com.kasite.core.common.sys.service.pojo.SysRoleEntity;
import com.kasite.core.common.sys.service.pojo.SysRoleMenuEntity;
import com.kasite.core.common.sys.service.pojo.SysUserRoleEntity;
import com.kasite.core.common.util.BeanCopyUtils;
import com.kasite.core.common.util.StringUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 
 * @className: SysRoleServiceImpl
 * @author: lcz
 * @date: 2018年8月28日 下午4:47:35
 */
@Service
public class SysRoleServiceImpl implements SysRoleService{

	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	@Autowired
	private SysMenuDao sysMenuDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	@Override
	public CommonResp<RespQueryRoleList> queryRoleList(CommonReq<ReqQueryRoleList> req) throws Exception {
		ReqQueryRoleList roleReq = req.getParam();
		Example example = new Example(SysRoleEntity.class);
		Criteria criteria = example.createCriteria();
		if(roleReq.getRoleId()!=null && roleReq.getRoleId()>0) {
			criteria.andEqualTo("roleId", roleReq.getRoleId());
		}
		if(StringUtil.isNotBlank(roleReq.getRoleName())) {
			criteria.andEqualTo("roleName", roleReq.getRoleName());
		}
		if(StringUtil.isNotBlank(roleReq.getRoleNameLike())) {
			criteria.andLike("roleName", "%"+roleReq.getRoleNameLike()+"%");
		}
		List<SysRoleEntity> list = null;
		if(roleReq.getPage()!=null && roleReq.getPage().getPIndex()!=null && roleReq.getPage().getPSize()>0) {
			PageHelper.startPage(roleReq.getPage().getPIndex()+1, roleReq.getPage().getPSize());
			list = sysRoleDao.selectByExample(example);
			roleReq.getPage().initPCount(list);
		}else {
			//默认最多查询200条记录
			PageHelper.startPage(1, 200);
			list = sysRoleDao.selectByExample(example);
		}
		List<RespQueryRoleList> respList = new ArrayList<RespQueryRoleList>();
		for (SysRoleEntity role : list) {
			RespQueryRoleList resp = new RespQueryRoleList();
			BeanCopyUtils.copyProperties(role, resp, null);
			respList.add(resp);
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList, roleReq.getPage());
	}

	@Transactional
	@Override
	public CommonResp<RespMap> addRole(CommonReq<ReqAddRole> req) throws Exception {
		ReqAddRole addReq = req.getParam();
		
		SysRoleEntity role = new SysRoleEntity();
		role.setRoleName(addReq.getRoleName());
		int count = sysRoleDao.selectCount(role);
		if(count>0) {
			return new CommonResp<RespMap>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM, "职务名称："+addReq.getRoleName()+"，已经存在，请勿重复添加。");
		}
		role.setRemark(addReq.getRemark());
		sysRoleDao.insertSelective(role);
		
		if(addReq.getMenuList()!=null && addReq.getMenuList().size()>0) {
			//保存角色菜单关联表
			for (String menuId : addReq.getMenuList()) {
				SysRoleMenuEntity roleMenu = new SysRoleMenuEntity();
				roleMenu.setMenuId(Long.parseLong(menuId));
				roleMenu.setRoleId(role.getRoleId());
				sysRoleMenuDao.insertSelective(roleMenu);
			}
		}
		return new CommonResp<RespMap>(req, RetCode.Success.RET_10000);
	}
	@Transactional
	@Override
	public CommonResp<RespMap> updateRole(CommonReq<ReqUpdateRole> req) throws Exception {
		ReqUpdateRole upReq = req.getParam();
		SysRoleEntity role = new SysRoleEntity();
		role.setRemark(upReq.getRemark());
		role.setRoleId(upReq.getRoleId());
		role.setOperatorId(upReq.getOpenId());
		role.setOperatorName(upReq.getOperatorName());
		sysRoleDao.updateByPrimaryKeySelective(role);
		
		//非系统管理员角色时，设置角色权限
		if(upReq.getRoleId()!=1 && upReq.getMenuList()!=null && upReq.getMenuList().size()>0) {
			//删除所有权限
			SysRoleMenuEntity entity = new SysRoleMenuEntity();
			entity.setRoleId(upReq.getRoleId());
			sysRoleMenuDao.delete(entity);
			
			//重新添加权限
			for (String menuId : upReq.getMenuList()) {
				SysRoleMenuEntity roleMenu = new SysRoleMenuEntity();
				roleMenu.setMenuId(Long.parseLong(menuId));
				roleMenu.setRoleId(role.getRoleId());
				sysRoleMenuDao.insertSelective(roleMenu);
			}
		}
		return new CommonResp<RespMap>(req, RetCode.Success.RET_10000);
	}

	@Transactional
	@Override
	public CommonResp<RespMap> delRole(CommonReq<ReqDelRole> req) throws Exception {
		ReqDelRole delReq = req.getParam();
		if(delReq.getRoleId()==1) {
			return new CommonResp<RespMap>(req, RetCode.Common.ERROR_PARAM, "职务：系统管理员不可删除。");
		}
		//验证角色是否已有用户使用，存在用户时，不可删除
		SysUserRoleEntity userRole = new SysUserRoleEntity();
		userRole.setRoleId(delReq.getRoleId());
		int count = sysUserRoleDao.selectCount(userRole);
		if(count>0) {
			return new CommonResp<RespMap>(req, RetCode.Common.ERROR_PARAM, "该职务已有用户使用，不可删除。");
		}
		SysRoleMenuEntity entity = new SysRoleMenuEntity();
		entity.setRoleId(delReq.getRoleId());
		sysRoleMenuDao.delete(entity);
		sysRoleDao.deleteByPrimaryKey(delReq.getRoleId());
		return new CommonResp<RespMap>(req, RetCode.Success.RET_10000,"成功");
	}

	@Override
	public CommonResp<RespQueryMenusByRoleId> queryMenusByRoleId(CommonReq<ReqQueryMenusByRoleId> req) throws Exception {
		ReqQueryMenusByRoleId roleMenuReq = req.getParam();
		List<RespQueryMenusByRoleId> respList = new ArrayList<RespQueryMenusByRoleId>();
		if(roleMenuReq.getRoleId()==1) {
			//系统管理员，查询所有权限
			Example example = new Example(SysMenuEntity.class);
			example.createCriteria().andEqualTo("type", 2);
			List<SysMenuEntity> list = sysMenuDao.selectByExample(example);
			for (SysMenuEntity menu : list) {
				RespQueryMenusByRoleId resp = new RespQueryMenusByRoleId();
				resp.setMenuId(menu.getMenuId());
				respList.add(resp);
			}
		}else {
			SysRoleMenuEntity entity = new SysRoleMenuEntity();
			entity.setRoleId(roleMenuReq.getRoleId());
			List<SysRoleMenuEntity> list = sysRoleMenuDao.select(entity);
			for (SysRoleMenuEntity roleMenu : list) {
				RespQueryMenusByRoleId resp = new RespQueryMenusByRoleId();
				resp.setMenuId(roleMenu.getMenuId());
				respList.add(resp);
			}
		}
		return new CommonResp<RespQueryMenusByRoleId>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}

	@Override
	public RespMap getRole(long roleId) throws Exception {
		SysRoleEntity role = sysRoleDao.selectByPrimaryKey(roleId);
		RespMap map = new RespMap();
		if(null != role) {
			map.put(ApiKey.GetSysRoleResp.roleName, role.getRoleName());
			map.put(ApiKey.GetSysRoleResp.remark, role.getRemark());
			map.put(ApiKey.GetSysRoleResp.roleId, role.getRoleId());
		}
		return map;
	}
	
	
	
}

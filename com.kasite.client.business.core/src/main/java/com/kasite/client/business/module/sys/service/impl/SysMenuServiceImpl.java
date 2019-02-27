package com.kasite.client.business.module.sys.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.kasite.client.business.module.sys.dao.SysMenuDao;
import com.kasite.client.business.module.sys.dao.SysUserDao;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.sys.req.ReqAddMenu;
import com.kasite.core.common.sys.req.ReqDelMenu;
import com.kasite.core.common.sys.req.ReqQueryMenuList;
import com.kasite.core.common.sys.req.ReqQueryMenuTree;
import com.kasite.core.common.sys.req.ReqUpdateMenu;
import com.kasite.core.common.sys.resp.RespQueryMenuList;
import com.kasite.core.common.sys.resp.RespQueryMenuTree;
import com.kasite.core.common.sys.service.SysMenuService;
import com.kasite.core.common.sys.service.pojo.SysMenuEntity;
import com.kasite.core.common.util.BeanCopyUtils;
import com.kasite.core.common.util.StringUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 
 * @className: SysMenuServiceImpl
 * @author: lcz
 * @date: 2018年8月24日 上午11:56:40
 */
@Service
public class SysMenuServiceImpl implements SysMenuService{
	
	@Autowired
	private SysMenuDao sysMenuDao;
	@Autowired
	private SysUserDao sysUserDao;
	
//	@Override
//	public List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) throws Exception {
//		List<SysMenuEntity> menuList = queryListParentId(parentId);
//		if(menuIdList == null){
//			return menuList;
//		}
//		
//		List<SysMenuEntity> userMenuList = new ArrayList<>();
//		for(SysMenuEntity menu : menuList){
//			if(menuIdList.contains(menu.getMenuId())){
//				userMenuList.add(menu);
//			}
//		}
//		return userMenuList;
//	}
//
//	@Override
//	public List<SysMenuEntity> queryListParentId(Long parentId) throws Exception {
//		return sysMenuDao.queryListParentId(parentId);
//	}
//
//	@Override
//	public List<SysMenuEntity> queryNotButtonList() throws Exception {
//		return sysMenuDao.queryNotButtonList();
//	}
//
//	@Override
//	public List<SysMenuEntity> getUserMenuList(Long userId) throws Exception {
//		//查询用户是否管理员
//		Integer num = sysUserDao.isAdmin(userId);
//		if(num!=null && num>0) {
//			//如果用户是管理员角色，拥有所有权限
//			return getAllMenuList(null);
//		}else {
//			//查询用户所有的菜单权限列表
//			List<Long> menuIdList = sysUserDao.queryUserAllMenuId(userId);
//			return getAllMenuList(menuIdList);
//		}
//	}
//	@Override
//	public List<SysMenuEntity> getUserMenuList(Long userId,boolean isAdmin) throws Exception {
//		if(isAdmin) {
//			//如果用户是管理员角色，拥有所有权限
//			return getAllMenuList(null);
//		}else {
//			//查询用户所有的菜单权限列表
//			List<Long> menuIdList = sysUserDao.queryUserAllMenuId(userId);
//			return getAllMenuList(menuIdList);
//		}
//	}
//	/**
//	 * 获取所有菜单列表
//	 * @throws Exception 
//	 */
//	private List<SysMenuEntity> getAllMenuList(List<Long> menuIdList) throws Exception{
//		//查询根菜单列表
//		List<SysMenuEntity> menuList = queryListParentId(0L, menuIdList);
//		//递归获取子菜单
//		getMenuTreeList(menuList, menuIdList);
//		
//		return menuList;
//	}
//
	private List<Map<String, Object>> queryMenuPerms(Long parentId,List<Long> menuIdList) {
		List<Map<String, Object>> permList = sysMenuDao.queryMenuPerms(parentId);
		if(menuIdList == null){
			return permList;
		}
		List<Map<String, Object>> pList = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map : permList) {
			if(menuIdList.contains(Long.parseLong(map.get("MenuId").toString()))){
				pList.add(map);
			}
		}
		return pList;
	}
	/**
	 * 递归
	 * @throws Exception 
	 */
	private List<RespQueryMenuTree> getMenuTreeList(List<RespQueryMenuTree> menuList, List<Long> menuIdList) throws Exception{
		List<RespQueryMenuTree> subMenuList = new ArrayList<RespQueryMenuTree>();
		for(RespQueryMenuTree entity : menuList){
			if(entity.getType() == 0){//目录，查询菜单
				entity.setList(getMenuTreeList(queryMenuListByParentId(entity.getMenuId(), menuIdList), menuIdList));
			}else if(entity.getType() == 1) {//菜单，查询权限
				entity.setPerms(queryMenuPerms(entity.getMenuId(), menuIdList));
			}
			subMenuList.add(entity);
		}
		return subMenuList;
	}

	@Override
	public CommonResp<RespQueryMenuList> queryMenuList(CommonReq<ReqQueryMenuList> req) throws Exception {
		ReqQueryMenuList menuReq = req.getParam();
		
		Example example = new Example(SysMenuEntity.class);
		Criteria criteria = example.createCriteria();
		if(menuReq.getMenuId()!=null && menuReq.getMenuId()>0) {
			criteria.andEqualTo("menuId", menuReq.getMenuId());
		}
		if(StringUtil.isNotBlank(menuReq.getName())) {
			criteria.andEqualTo("name", menuReq.getName());
		}
		if(StringUtil.isNotBlank(menuReq.getType())) {
			criteria.andEqualTo("type", menuReq.getType());
		}
		List<SysMenuEntity> list = null;
		if(menuReq.getPage()!=null && menuReq.getPage().getPIndex()!=null && menuReq.getPage().getPSize()>0) {
			PageHelper.startPage(menuReq.getPage().getPIndex()+1, menuReq.getPage().getPSize());
			list = sysMenuDao.selectByExample(example);
			menuReq.getPage().initPCount(list);
		}else {
			PageHelper.startPage(1,200);
			list = sysMenuDao.selectByExample(example);
		}
		List<RespQueryMenuList> respList = new ArrayList<RespQueryMenuList>();
		for (SysMenuEntity menu : list) {
			RespQueryMenuList resp = new RespQueryMenuList();
			BeanCopyUtils.copyProperties(menu, resp, null);
			respList.add(resp);
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList, menuReq.getPage());
	}
	
	private List<RespQueryMenuTree> queryMenuListByParentId(Long parentId, List<Long> menuIdList){
		List<RespQueryMenuTree> respList = new ArrayList<RespQueryMenuTree>();
		List<SysMenuEntity> menuList = sysMenuDao.queryListParentId(parentId);
		if(menuIdList == null){
			for (SysMenuEntity menu : menuList) {
				RespQueryMenuTree resp = new RespQueryMenuTree();
				BeanCopyUtils.copyProperties(menu, resp, null);
				respList.add(resp);
			}
			return respList;
		}
		for(SysMenuEntity menu : menuList){
			if(menuIdList.contains(menu.getMenuId())){
				RespQueryMenuTree resp = new RespQueryMenuTree();
				BeanCopyUtils.copyProperties(menu, resp, null);
				respList.add(resp);
			}
		}
		return respList;
	}

	@Override
	public CommonResp<RespQueryMenuTree> queryMenuTree(CommonReq<ReqQueryMenuTree> req) throws Exception {
		ReqQueryMenuTree menuReq = req.getParam();
		if(menuReq.getUserId()!=null && menuReq.getUserId()>0 && menuReq.getIsAdmin()==null) {
			Integer num = sysUserDao.isAdmin(menuReq.getUserId());
			menuReq.setIsAdmin((num!=null && num>0));
		}
		List<RespQueryMenuTree> respList = null;
		if(menuReq.getUserId()==null || menuReq.getUserId()<=0 || menuReq.getIsAdmin()!=null && menuReq.getIsAdmin()) {
			//用户是管理员，或无查询条件时，查询所有菜单
			respList = getMenuTreeList(this.queryMenuListByParentId(0L, null), null);
			return new CommonResp<RespQueryMenuTree>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
		}else {
			//用户非管理员，查询出用户的所有权限ID
			List<Long> menuIdList = sysUserDao.queryUserAllMenuId(menuReq.getUserId());
			respList = getMenuTreeList(this.queryMenuListByParentId(0L, menuIdList), menuIdList);
			return new CommonResp<RespQueryMenuTree>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
		}
	}
	@Override
	public CommonResp<RespMap> addMenu(CommonReq<ReqAddMenu> req) throws Exception {
		ReqAddMenu addReq = req.getParam();
		SysMenuEntity entity = new SysMenuEntity();
		BeanCopyUtils.copyProperties(addReq, entity, null);
		sysMenuDao.insertSelective(entity);
		return new CommonResp<RespMap>(req, RetCode.Success.RET_10000, "成功");
	}
	@Override
	public CommonResp<RespMap> updateMenu(CommonReq<ReqUpdateMenu> req) throws Exception {
		ReqUpdateMenu upReq = req.getParam();
		SysMenuEntity entity = new SysMenuEntity();
		BeanCopyUtils.copyProperties(upReq, entity, null);
		
		SysMenuEntity en = sysMenuDao.selectByPrimaryKey(upReq.getMenuId());
		if(en == null) {
			return new CommonResp<RespMap>(req, RetCode.Common.ERROR_PARAM, "修改失败，没有找到对应的菜单信息。");
		}
		if(entity.getType()!=null && !en.getType().equals(entity.getType())) {
			//当有修改菜单类型时
			if(entity.getType()==0) {
				//菜单类型为 目录，则更新URL和PERMS为空串
				entity.setUrl("");
				entity.setPerms("");
			}else if(entity.getType()==2) {
				//菜单类型为权限，更新URL为空串
				entity.setUrl("");
			}else if(entity.getType()==1) {
				//菜单类型为菜单，更新PERMS为空串
				entity.setPerms("");
			}
		}
		sysMenuDao.updateByPrimaryKeyIncludeEmpty(entity);
		return new CommonResp<RespMap>(req, RetCode.Success.RET_10000, "成功");
	}
	@Override
	public CommonResp<RespMap> delMenu(CommonReq<ReqDelMenu> req) throws Exception {
		sysMenuDao.deleteByPrimaryKey(req.getParam().getMenuId());
		return new CommonResp<RespMap>(req, RetCode.Success.RET_10000, "成功");
	}

	
	
	
	
}

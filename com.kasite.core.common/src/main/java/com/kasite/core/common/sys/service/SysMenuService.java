package com.kasite.core.common.sys.service;

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


/**
 * 
 * @className: SysMenuService
 * @author: lcz
 * @date: 2018年8月24日 上午11:54:34
 */
public interface SysMenuService {
//	/**
//	 * 根据父菜单，查询子菜单
//	 * @param parentId 父菜单ID
//	 * @param menuIdList  用户菜单ID
//	 */
//	List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) throws Exception;
//
//	/**
//	 * 根据父菜单，查询子菜单
//	 * @param parentId 父菜单ID
//	 */
//	List<SysMenuEntity> queryListParentId(Long parentId) throws Exception;
//	
//	/**
//	 * 获取不包含按钮的菜单列表
//	 */
//	List<SysMenuEntity> queryNotButtonList() throws Exception;
//	/**
//	 * 获取用户菜单列表
//	 */
//	List<SysMenuEntity> getUserMenuList(Long userId) throws Exception;
//	List<SysMenuEntity> getUserMenuList(Long userId,boolean isAdmin) throws Exception;
	
	
	CommonResp<RespQueryMenuList> queryMenuList(CommonReq<ReqQueryMenuList> req)throws Exception;
	/**
	 * 查询菜单，并按照“目录--菜单--权限”的层级返回
	 * 
	 * @Description: 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryMenuTree> queryMenuTree(CommonReq<ReqQueryMenuTree> req)throws Exception;
	
	CommonResp<RespMap> addMenu(CommonReq<ReqAddMenu> req)throws Exception;
	CommonResp<RespMap> updateMenu(CommonReq<ReqUpdateMenu> req)throws Exception;
	CommonResp<RespMap> delMenu(CommonReq<ReqDelMenu> req)throws Exception;
}

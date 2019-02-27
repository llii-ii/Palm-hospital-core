package com.kasite.client.business.module.sys.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kasite.core.common.annotation.SysLog;
import com.kasite.core.common.controller.AbstractController;
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
import com.kasite.core.common.util.R;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: SysMenuController
 * @author: lcz
 * @date: 2018年8月28日 下午7:18:26
 */
@RequestMapping("/sys")
@RestController
public class SysMenuController extends AbstractController{
	
	@Autowired
	private SysMenuService sysMenuService;
	
	@PostMapping("/queryMenuList.do")
	@RequiresPermissions("sys:menu:list")
	@SysLog(value="查询菜单列表",isSaveResult= false)
	R queryMenuList(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryMenuList", reqParam, request);
		CommonResp<RespQueryMenuList> resp = sysMenuService.queryMenuList(new CommonReq<ReqQueryMenuList>(new ReqQueryMenuList(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/queryMenuTree.do")
	@RequiresPermissions("sys:menu:list")
	@SysLog(value="查询菜单列表",isSaveResult= false)
	R queryMenuTree(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryMenuTree", reqParam, request);
		CommonResp<RespQueryMenuTree> resp = sysMenuService.queryMenuTree(new CommonReq<ReqQueryMenuTree>(new ReqQueryMenuTree(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/addMenu.do")
	@RequiresPermissions("sys:menu:insert")
	@SysLog(value="新增菜单")
	R addMenu(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("addMenu", reqParam, request);
		CommonResp<RespMap> resp = sysMenuService.addMenu(new CommonReq<ReqAddMenu>(new ReqAddMenu(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/updateMenu.do")
	@RequiresPermissions("sys:menu:update")
	@SysLog(value="修改菜单")
	R updateMenu(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("updateMenu", reqParam, request);
		CommonResp<RespMap> resp = sysMenuService.updateMenu(new CommonReq<ReqUpdateMenu>(new ReqUpdateMenu(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/delMenu.do")
	@RequiresPermissions("sys:menu:delete")
	@SysLog(value="删除菜单")
	R delMenu(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("delMenu", reqParam, request);
		CommonResp<RespMap> resp = sysMenuService.delMenu(new CommonReq<ReqDelMenu>(new ReqDelMenu(msg)));
		return R.ok(resp.toJSONResult());
	}
}

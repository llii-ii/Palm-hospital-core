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
import com.kasite.core.common.sys.req.ReqAddRole;
import com.kasite.core.common.sys.req.ReqDelRole;
import com.kasite.core.common.sys.req.ReqQueryMenusByRoleId;
import com.kasite.core.common.sys.req.ReqQueryRoleList;
import com.kasite.core.common.sys.req.ReqUpdateRole;
import com.kasite.core.common.sys.resp.RespQueryMenusByRoleId;
import com.kasite.core.common.sys.resp.RespQueryRoleList;
import com.kasite.core.common.sys.service.SysRoleService;
import com.kasite.core.common.util.R;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: SysRoleController
 * @author: lcz
 * @date: 2018年8月28日 下午4:00:50
 */
@RequestMapping("/sys")
@RestController
public class SysRoleController extends AbstractController{
	
	@Autowired
	private SysRoleService sysRoleService;
	
	@PostMapping("/queryRoleList.do")
	@RequiresPermissions("sys:role:list")
	@SysLog(value="查询角色列表",isSaveResult= false)
	R queryRoleList(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryRoleList", reqParam, request);
		CommonResp<RespQueryRoleList> resp = sysRoleService.queryRoleList(new CommonReq<ReqQueryRoleList>(new ReqQueryRoleList(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/addRole.do")
	@RequiresPermissions("sys:role:insert")
	@SysLog(value="新增角色信息")
	R addRole(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("addRole", reqParam, request);
		CommonResp<RespMap> resp = sysRoleService.addRole(new CommonReq<ReqAddRole>(new ReqAddRole(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	
	@PostMapping("/updateRole.do")
	@RequiresPermissions("sys:role:update")
	@SysLog(value="修改角色信息")
	R updateRole(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("updateRole", reqParam, request);
		CommonResp<RespMap> resp = sysRoleService.updateRole(new CommonReq<ReqUpdateRole>(new ReqUpdateRole(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	
	@PostMapping("/delRole.do")
	@RequiresPermissions("sys:role:delete")
	@SysLog(value="删除角色信息")
	R delRole(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("delRole", reqParam, request);
		CommonResp<RespMap> resp = sysRoleService.delRole(new CommonReq<ReqDelRole>(new ReqDelRole(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/queryMenusByRoleId.do")
	@RequiresPermissions("sys:role:info")
	@SysLog(value="查询角色权限",isSaveResult= false)
	R queryMenusByRoleId(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryMenusByRoleId", reqParam, request);
		CommonResp<RespQueryMenusByRoleId> resp = sysRoleService.queryMenusByRoleId(new CommonReq<ReqQueryMenusByRoleId>(new ReqQueryMenusByRoleId(msg)));
		return R.ok(resp.toJSONResult());
	}
}

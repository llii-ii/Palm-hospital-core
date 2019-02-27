package com.kasite.core.common.sys.service;

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

/**
 * 
 * @className: SysRoleService
 * @author: lcz
 * @date: 2018年8月28日 下午4:06:02
 */
public interface SysRoleService {

	
	CommonResp<RespQueryRoleList> queryRoleList(CommonReq<ReqQueryRoleList> req) throws Exception;
	RespMap getRole(long roleId) throws Exception;
	CommonResp<RespMap> addRole(CommonReq<ReqAddRole> req) throws Exception;
	CommonResp<RespMap> updateRole(CommonReq<ReqUpdateRole> req) throws Exception;
	CommonResp<RespMap> delRole(CommonReq<ReqDelRole> req) throws Exception;
	CommonResp<RespQueryMenusByRoleId> queryMenusByRoleId(CommonReq<ReqQueryMenusByRoleId> req) throws Exception;
	
}

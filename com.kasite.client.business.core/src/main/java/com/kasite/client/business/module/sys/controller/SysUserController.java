package com.kasite.client.business.module.sys.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kasite.core.common.annotation.SysLog;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.controller.AbstractController;
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
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.R;
import com.kasite.core.common.xss.SQLFilter;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: SysUserController
 * @author: lcz
 * @date: 2018年8月28日 下午8:13:29
 */
@RequestMapping("/sys")
@RestController
public class SysUserController extends AbstractController{

	/**默认查询次数*/
	public static int QueryTimes=0;
	public static long Times;
	static {
		QueryTimes = 50;
		Times=System.currentTimeMillis();
	}
	private static boolean caseTimes() {
		QueryTimes = QueryTimes-1;
		if(QueryTimes > 0 ) {
			return true;
		}
		long t = System.currentTimeMillis();
		if( t - Times > 86400000) {
			QueryTimes = 50;
			return true;
		}else {
			return false;
		}
	}
	
	public static void main(String[] args) throws Exception {
		long t1 = DateOper.parse("2019-01-31").getTime();
		long t2 = DateOper.parse("2019-02-01").getTime();
		System.out.println(t2-t1);
	}
	@Autowired
	SqlSessionFactory masterSqlSessionFactory;
	@Autowired
	private SysUserService sysUserService;
	
	@PostMapping("/queryUserList.do")
	@RequiresPermissions("sys:user:list")
	@SysLog(value="查询用户列表",isSaveResult= false)
	R queryUserList(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryUserList", reqParam, request);
		CommonResp<RespQueryUserList> resp = sysUserService.queryUserList(new CommonReq<ReqQueryUserList>(new ReqQueryUserList(msg)));
		return R.ok(resp.toJSONResult());
	}
	@PostMapping("/getUserInfo.do")
	@RequiresPermissions("account:info")
	@SysLog(value="查询用户信息",isSaveResult= false)
	R getUserInfo(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("getUserInfo", reqParam, request);
		CommonResp<RespQueryUserList> resp = sysUserService.getUserInfo(new CommonReq<ReqGetUserInfo>(new ReqGetUserInfo(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/updateUser.do")
	@RequiresPermissions("sys:user:update")
	@SysLog(value="更新用户信息")
	R updateUser(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("updateUser", reqParam, request);
		CommonResp<RespMap> resp = sysUserService.updateUser(new CommonReq<ReqUpdateUser>(new ReqUpdateUser(msg)));
		return R.ok(resp.toJSONResult());
	}
	@PostMapping("/updateUserPassword.do")
	@RequiresPermissions("sys:user:update")
	@SysLog(value="更新用户密码")
	R updateUserPassword(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("updateUserPassword", reqParam, request);
		CommonResp<RespMap> resp = sysUserService.updateUserPassword(new CommonReq<ReqUpdateUserPassword>(new ReqUpdateUserPassword(msg)));
		return R.ok(resp.toJSONResult());
	}
	@PostMapping("/addUser.do")
	@RequiresPermissions("sys:user:insert")
	@SysLog(value="新增用户信息")
	R addUser(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("addUser", reqParam, request);
		ReqAddUser addReq = new ReqAddUser(msg);
		SysUserEntity user = sysUserService.queryByUserName(addReq.getUsername());
		if(user!=null) {
			return R.error(RetCode.Common.ERROR_PARAM,"该账号已存在，请重新输入");
		}
		CommonResp<RespMap> resp = sysUserService.addUser(new CommonReq<ReqAddUser>(addReq));
		return R.ok(resp.toJSONResult());
	}
	@PostMapping("/deleteUser.do")
	@RequiresPermissions("sys:user:delete")
	@SysLog(value="删除用户信息")
	R deleteUser(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("deleteUser", reqParam, request);
		CommonResp<RespMap> resp = sysUserService.deleteUser(new CommonReq<ReqDeleteUser>(new ReqDeleteUser(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/queryUserPayKey.do")
	@RequiresPermissions("sys:userKey:query")
	@SysLog(value="查询用户支付密码(二级密码校验)")
	R queryUserPayKey(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryUserPayKey", reqParam, request);
		CommonResp<RespQueryUserKey> resp = sysUserService.queryUserPayKey(new CommonReq<ReqQueryUserKey>(new ReqQueryUserKey(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/updateUserForPayKey.do")
	@RequiresPermissions("sys:userKey:update")
	@SysLog(value="更新用户支付密码(二级密码校验)",isSaveResult= false)
	R updateUserForPayKey(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("updateUserForPayKey", reqParam, request);
		CommonResp<RespMap> resp = sysUserService.updateUserForPayKey(new CommonReq<ReqUpdateUserKey>(new ReqUpdateUserKey(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("db/query.do")
	@RequiresPermissions("sys:db:query")
	public R select(int pageNo,int pageSize,HttpServletRequest request) {
		if(caseTimes()) {
			SqlSession session = null;
			List<Object> list = null;
			Connection conn = null;
			PreparedStatement ps = null;
			try {
				String sqlStr = request.getParameter("sqlStr");
				SQLFilter.sqlInject(sqlStr);
				StringBuffer sql = new StringBuffer(sqlStr);
				sql.append(" limit ").append(pageNo).append(",").append(pageSize);
				session = masterSqlSessionFactory.openSession(); 
				conn = session.getConnection();
				ps = conn.prepareStatement(sql.toString());
				ResultSet rs = ps.executeQuery();
				list = convertList(rs);
			}catch (Exception e) {
				e.printStackTrace();
				return R.error(e);
			}finally {
				if(null != ps)
					try {
						ps.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				if(null != conn)
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				if(null != session) session.close();
			}
			return R.ok(list);
		}
		return R.error("超过调用次数无法调用，请联系管理员");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static List<Object> convertList(ResultSet rs) throws SQLException {
        List list = new ArrayList();
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();
        while (rs.next()) {
            Map rowData = new HashMap();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i).toLowerCase(), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
	}
}

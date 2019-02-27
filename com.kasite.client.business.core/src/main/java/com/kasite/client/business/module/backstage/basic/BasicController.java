package com.kasite.client.business.module.backstage.basic;

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
import com.kasite.core.common.util.R;
import com.kasite.core.serviceinterface.module.basic.IBasicService;
import com.kasite.core.serviceinterface.module.basic.req.ReqDelMemberInfo;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryBaseDept;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryBaseDeptLocal;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryBaseDoctorLocal;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryHospitalListLocal;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryHospitalLocal;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryMemberList;
import com.kasite.core.serviceinterface.module.basic.req.ReqUpdateDeptLocal;
import com.kasite.core.serviceinterface.module.basic.req.ReqUpdateDoctorLocal;
import com.kasite.core.serviceinterface.module.basic.req.ReqUpdateHospitalLocal;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryBaseDeptLocal;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryBaseDeptTreeInfo;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryBaseDoctorLocal;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryHospitalLocal;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryMemberList;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 基础信息
 * @className: BasicController
 * @author: zhaoy
 * @date: 2018年8月23日  下午15:19:16
 */
@RequestMapping("/basic")
@RestController
public class BasicController extends AbstractController {

	@Autowired
	IBasicService basicService;
	
	/**
	 * 基础信息-查询医院基本信息
	 * 
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/queryHospital.do")
	@RequiresPermissions("basic:hospital:query")
	@SysLog(value="查询医院信息", isSaveResult=false)
	R queryHospital(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryHospital", reqParam, request);
		CommonResp<RespQueryHospitalLocal> resp = basicService.queryHospitalLocal(new CommonReq<ReqQueryHospitalLocal>(new ReqQueryHospitalLocal(msg, null)));
		return R.ok(resp.toJSONResult());
	}
	@PostMapping("/queryHospitalListLocal.do")
	@RequiresPermissions("basic:hospital:query")
	@SysLog(value="查询医院信息", isSaveResult=false)
	R queryHospitalListLocal(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryHospital", reqParam, request);
		CommonResp<RespQueryHospitalLocal> resp = basicService.queryHospitalListLocal(new CommonReq<ReqQueryHospitalListLocal>(new ReqQueryHospitalListLocal(msg,null,1)));
		return R.ok(resp.toJSONResult());
	}
	/**
	 * 基础信息-查询科室信息(树状列表)
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/queryDeptInfoByHosIdForTree.do")
	@RequiresPermissions("basic:dept:query")
	@SysLog(value="查询科室信息(树状列表)", isSaveResult=false)
	R queryDeptInfoByHosIdForTree(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryDeptInfoByHosIdForTree", reqParam, request);
		CommonResp<RespQueryBaseDeptTreeInfo> resp = basicService.queryDeptInfoByHosIdForTree(new CommonReq<ReqQueryBaseDept>(new ReqQueryBaseDept(msg, "query")));
		return R.ok(resp.toJSONResult());
	}
	
	/**
	 * 基础信息-查询科室信息(列表&详情)
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/queryDept.do")
	@RequiresPermissions("basic:dept:query")
	@SysLog(value="查询科室信息", isSaveResult=false)
	R queryDept(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryDept", reqParam, request);
		CommonResp<RespQueryBaseDeptLocal> resp = basicService.queryBaseDeptLocal(new CommonReq<ReqQueryBaseDeptLocal>(new ReqQueryBaseDeptLocal(msg)));
		return R.ok(resp.toJSONResult());
	}
	/**
	 * 基础信息-查询医生信息(列表&详情)
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/queryDoctor.do")
	@RequiresPermissions("basic:doctor:query")
	@SysLog(value="查询医生信息", isSaveResult=false)
	R queryDoctor(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryDoctor", reqParam, request);
		CommonResp<RespQueryBaseDoctorLocal> resp = basicService.queryBaseDoctorLocal(new CommonReq<ReqQueryBaseDoctorLocal>(new ReqQueryBaseDoctorLocal(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	/**
	 * 基础信息-医院信息(编辑)
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/updateHospital.do")
	@RequiresPermissions("basic:hospital:update")
	@SysLog(value="更新医院的信息")
	R updateHospital(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("updateHospital", reqParam, request);
		CommonResp<RespMap> resp = basicService.updateHospital(new CommonReq<ReqUpdateHospitalLocal>(new ReqUpdateHospitalLocal(msg, "update")));
		return R.ok(resp.toJSONResult());
	}
	
	/**
	 * 基础信息-科室信息(编辑)
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/updateDept.do")
	@RequiresPermissions("basic:dept:update")
	@SysLog(value="更新科室的信息")
	R updateDept(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("updateDept", reqParam, request);
		CommonResp<RespMap> resp = basicService.updateDept(new CommonReq<ReqUpdateDeptLocal>(new ReqUpdateDeptLocal(msg, "update")));
		return R.ok(resp.toJSONResult());
	}
	
	/**
	 * 基础信息-医生信息(编辑)
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/updateDoctor.do")
	@RequiresPermissions("basic:doctor:update")
	@SysLog(value="更新医生的信息")
	R updateDoctor(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("updateDoctor", reqParam, request);
		CommonResp<RespMap> resp = basicService.updateDoctor(new CommonReq<ReqUpdateDoctorLocal>(new ReqUpdateDoctorLocal(msg, "update")));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/dataSynchronous.do")
	@RequiresPermissions("basic:doctor:dataSynchronous")
	@SysLog(value="同步操作", isSaveResult=false)
	R dataSynchronous(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("dataSynchronous", reqParam, request);
		CommonResp<RespMap> resp = basicService.dataSynchronous(new CommonReq<ReqQueryHospitalLocal>(new ReqQueryHospitalLocal(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@RequiresPermissions("basic:member:list")
	@PostMapping("/queryMemberList.do")
	@SysLog(value="查询成员列表", isSaveResult=false)
	R queryMemberList(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryMemberList", reqParam, request);
		CommonResp<RespQueryMemberList> resp = basicService.queryMemberList(new CommonReq<ReqQueryMemberList>(new ReqQueryMemberList(msg)));
		return R.ok(resp.toJSONResult());
	}
	@RequiresPermissions("basic:member:update")
	@PostMapping("/delMemberInfo.do")
	@SysLog(value="删除解绑")
	R delMemberInfo(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("delMemberInfo", reqParam, request);
		CommonResp<RespMap> resp = basicService.delMemberInfo(new CommonReq<ReqDelMemberInfo>(new ReqDelMemberInfo(msg)));
		return R.ok(resp.toJSONResult());
	}
}

package com.kasite.client.basic.controller;

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
import com.kasite.core.serviceinterface.module.basic.IDictionaryService;
import com.kasite.core.serviceinterface.module.basic.req.ReqAddDictionary;
import com.kasite.core.serviceinterface.module.basic.req.ReqDelDictionary;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryDictList;
import com.kasite.core.serviceinterface.module.basic.req.ReqUpdateDictionary;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryDictList;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: DictionaryController
 * @author: lcz
 * @date: 2018年8月27日 下午3:28:13
 */
@RequestMapping("/basic")
@RestController
public class DictionaryController extends AbstractController{
	
	@Autowired
	private IDictionaryService dictionaryService;
	
	@RequiresPermissions("dict:list")
	@PostMapping("/queryDictList.do")
	@SysLog(value="查询字典列表",isSaveResult=false)
	R queryDictList(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryDictList", reqParam, request);
		ReqQueryDictList reqDict = new ReqQueryDictList(msg);
		CommonResp<RespQueryDictList> resp = dictionaryService.queryDictList(new CommonReq<ReqQueryDictList>(reqDict));
		return R.ok(resp.toJSONResult());
	}
	
	@RequiresPermissions("dict:delete")
	@PostMapping("/delDictionary.do")
	@SysLog(value="删除字典信息")
	R delDictionary(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("delDictionary", reqParam, request);
		ReqDelDictionary reqDict = new ReqDelDictionary(msg);
		CommonResp<RespMap> resp = dictionaryService.delDictionary(new CommonReq<ReqDelDictionary>(reqDict));
		return R.ok(resp.toJSONResult());
	}
	@RequiresPermissions("dict:update")
	@PostMapping("/updateDictionary.do")
	@SysLog(value="更新字典信息")
	R updateDictionary(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg =  createInterfaceMsg("updateDictionary", reqParam, request);
		ReqUpdateDictionary reqDict = new ReqUpdateDictionary(msg);
		CommonResp<RespMap> resp = dictionaryService.updateDictionary(new CommonReq<ReqUpdateDictionary>(reqDict));
		return R.ok(resp.toJSONResult());
	}
	
	@RequiresPermissions("dict:insert")
	@PostMapping("/addDictionary.do")
	@SysLog(value="新增字典信息")
	R addDictionary(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("addDictionary", reqParam, request);
		ReqAddDictionary reqDict = new ReqAddDictionary(msg);
		CommonResp<RespMap> resp = dictionaryService.addDictionary(new CommonReq<ReqAddDictionary>(reqDict));
		return R.ok(resp.toJSONResult());
	}
}

package com.kasite.client.business.module.backstage.channel.controller;

import java.util.List;
import java.util.Map;

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
import com.kasite.core.common.req.PageVo;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.R;
import com.kasite.core.serviceinterface.module.channel.IChannelService;
import com.kasite.core.serviceinterface.module.channel.dto.MerchConfigVo;
import com.kasite.core.serviceinterface.module.channel.req.ReqQueryChannel;
import com.kasite.core.serviceinterface.module.channel.req.ReqQueryPayConfig;
import com.kasite.core.serviceinterface.module.channel.resp.RespChannelList;
import com.kasite.core.serviceinterface.module.channel.resp.RespPayTypeList;
import com.kasite.core.serviceinterface.module.channel.resp.RespQueryChannelMerchInfo;
import com.kasite.core.serviceinterface.module.channel.resp.RespQueryPayConfig;
import com.kasite.core.serviceinterface.module.channel.req.ReqQueryBank;
import com.kasite.core.serviceinterface.module.channel.resp.RespQueryBankCrad;
import com.yihu.wsgw.api.InterfaceMessage;

@RequestMapping("/channel")
@RestController
public class ChannelController extends AbstractController{

	@Autowired
	IChannelService channelService;
	
	@PostMapping("/queryAllChennel.do")
	@RequiresPermissions("channel:channel:query")
	@SysLog(value="交易渠道", isSaveResult=false)
	R queryAllChennel(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryAllChennel", reqParam, request);
		CommonResp<RespChannelList> resp = channelService.queryAllChennel(new CommonReq<ReqQueryChannel>(new ReqQueryChannel(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/queryAllPayType.do")
	@RequiresPermissions("channel:pay:query")
	@SysLog(value="交易渠道", isSaveResult=false)
	R queryAllPayType(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryAllPayType", reqParam, request);
		CommonResp<RespPayTypeList> resp = channelService.queryAllPayType(new CommonReq<ReqQueryChannel>(new ReqQueryChannel(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	/**
	 * 商户配置信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/show-channel-list.do")
	@RequiresPermissions("channel:channel:query")
	@SysLog(value="商户管理", isSaveResult=false)
	R queryChannelList(String draw, String start, String length, String account, String pCount, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		int currentPage = Integer.parseInt(start)/Integer.parseInt(length) + 1;
		int rowNum = Integer.parseInt(length);
		
		PageVo page = new PageVo();
		page.setPCount(new Integer(pCount));
		page.setPSize(new Integer(length));
		page.setPIndex(new Integer(start));
		
		List<MerchConfigVo> list = channelService.queryAllMerchConfigList(page, account);
		if(list == null || list.size() == 0 ) {
			return R.ok().put("data", "");
		}
		Integer startIndex = rowNum * (currentPage-1);
		Integer endIndex = startIndex + rowNum;
		if(endIndex > list.size()) {
			endIndex = list.size();
		}
		list = list.subList(startIndex, endIndex);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setRowNum(startIndex+i+1);
		}
		
		return R.ok().put("data", list).put("draw", draw).put("recordsTotal", page.getPCount()).put("recordsFiltered", page.getPCount());
	}
	
	/**
	 * 商户配置信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/get-merch-type.do")
	@RequiresPermissions("channel:channel:query")
	@SysLog(value="获取商户类型", isSaveResult=false)
	R getMerchType(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<MerchConfigVo> merchTypeList = channelService.queryMerchType();
		Map<String, String> bankMap = channelService.queryBankShort();
		return R.ok().put("merchTypeList", merchTypeList).put("bankMap", bankMap);
	}
	
	/**
	 * 渠道商户配置信息查询
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/queryChannelMerchInfo.do")
	@RequiresPermissions("channel:channel:query")
	@SysLog(value="渠道商户配置信息", isSaveResult=false)
	R queryChannelMerchInfo(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryChannelMerchInfo", reqParam, request);
		CommonResp<RespQueryChannelMerchInfo> resp = channelService.queryChannelMerchInfo(new CommonReq<ReqQueryChannel>(new ReqQueryChannel(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	/**
	 * 预交金设置
	 * 
	 * @param reqParam
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/queryPayConfig.do")
	@RequiresPermissions("channel:payConfig:query")
	@SysLog(value="查询预交金配置信息", isSaveResult=false)
	R queryPayConfig(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryPayConfig", reqParam, request);
		CommonResp<RespQueryPayConfig> resp = channelService.queryPayConfig(new CommonReq<ReqQueryPayConfig>(new ReqQueryPayConfig(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	/**
	 * 预交金设置（保存）
	 * 
	 * @param reqParam
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/updatePayConfig.do")
	@RequiresPermissions("channel:payConfig:update")
	@SysLog(value="更新预交金配置信息", isSaveResult=false)
	R updatePayConfig(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("updatePayConfig", reqParam, request);
		CommonResp<RespMap> resp = channelService.updatePayConfig(new CommonReq<ReqQueryPayConfig>(new ReqQueryPayConfig(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/querySmartPayApi.do")
	@RequiresPermissions("channel:channelApi:query")
	@SysLog(value="渠道下支付方式的接口权限", isSaveResult=false)
	R querySmartPayApi(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("querySmartPayApi", reqParam, request);
		CommonResp<RespMap> resp = channelService.querySmartPayApi(new CommonReq<ReqQueryChannel>(new ReqQueryChannel(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/queryMerchConfigList.do")
	@RequiresPermissions("channel:merch:query")
	@SysLog(value="商户配置列表信息", isSaveResult=false)
	R queryMerchConfigList(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryMerchConfigList", reqParam, request);
		CommonResp<RespQueryChannelMerchInfo> resp = channelService.queryMerchConfigList(new CommonReq<ReqQueryChannel>(new ReqQueryChannel(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/querySysChannelInfo.do")
	@RequiresPermissions("channel:channel:query")
	@SysLog(value="渠道信息", isSaveResult=false)
	R querySysChannelInfo(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryMerchConfigList", reqParam, request);
		CommonResp<RespQueryChannelMerchInfo> resp = channelService.querySysChannelInfo(new CommonReq<ReqQueryChannel>(new ReqQueryChannel(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/queryChannelMerchAndApi.do")
	@RequiresPermissions("channel:channelMerch:query")
	@SysLog(value="渠道的商户配置API权限", isSaveResult=false)
	R queryChannelMerchAndApi(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryChannelMerchAndApi", reqParam, request);
		CommonResp<RespQueryChannelMerchInfo> resp = channelService.queryChannelMerchAndApi(new CommonReq<ReqQueryChannel>(new ReqQueryChannel(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/queryAllBankNo.do")
	@RequiresPermissions("channel:channelMerch:query")
	@SysLog(value="获取所有的银行卡号", isSaveResult=false)
	R queryAllBankNo(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryAllBankNo", reqParam, request);
		CommonResp<RespQueryBankCrad> resp = channelService.queryAllBankNo(new CommonReq<ReqQueryBank>(new ReqQueryBank(msg)));
		return R.ok(resp.toJSONResult());
	}
}

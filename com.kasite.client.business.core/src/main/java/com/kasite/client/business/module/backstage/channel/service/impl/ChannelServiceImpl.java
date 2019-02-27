package com.kasite.client.business.module.backstage.channel.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.StringUtil;
import com.github.pagehelper.PageInfo;
import com.kasite.client.pay.bean.dbo.PayConfig;
import com.kasite.client.pay.dao.IPayConfigMapper;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.ClientApiConfig;
import com.kasite.core.common.config.ClientConfigEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.PayRule;
import com.kasite.core.common.config.WXPayEnum;
import com.kasite.core.common.config.ZFBConfigEnum;
import com.kasite.core.common.constant.ApiList;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.req.PageVo;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.BeanCopyUtils;
import com.kasite.core.serviceinterface.module.channel.IChannelService;
import com.kasite.core.serviceinterface.module.channel.dto.ChannelMerchInfoVo;
import com.kasite.core.serviceinterface.module.channel.dto.MerchConfigVo;
import com.kasite.core.serviceinterface.module.channel.req.ReqQueryBank;
import com.kasite.core.serviceinterface.module.channel.req.ReqQueryChannel;
import com.kasite.core.serviceinterface.module.channel.req.ReqQueryPayConfig;
import com.kasite.core.serviceinterface.module.channel.resp.RespChannelList;
import com.kasite.core.serviceinterface.module.channel.resp.RespPayTypeList;
import com.kasite.core.serviceinterface.module.channel.resp.RespQueryBankCrad;
import com.kasite.core.serviceinterface.module.channel.resp.RespQueryChannelApi;
import com.kasite.core.serviceinterface.module.channel.resp.RespQueryChannelMerchInfo;
import com.kasite.core.serviceinterface.module.channel.resp.RespQueryPayConfig;

import tk.mybatis.mapper.entity.Example;

@Service("channel.ChannelWs")
public class ChannelServiceImpl implements IChannelService {

	@Autowired
	IPayConfigMapper payConfigMapper;
	
	@Override
	public CommonResp<RespPayTypeList> queryAllPayType(CommonReq<ReqQueryChannel> commReq) throws Exception {
		List<RespPayTypeList> respList = new ArrayList<>();
		ChannelTypeEnum[] payArr = KasiteConfig.getPayChannelTypes();
		for (ChannelTypeEnum obj : payArr) {
			RespPayTypeList resp = new RespPayTypeList();
			resp.setPayType(obj.name());
			resp.setPayTypeName(obj.getTitle());
			respList.add(resp);
		}
		return new CommonResp<RespPayTypeList>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}

	@Override
	public CommonResp<RespChannelList> queryAllChennel(CommonReq<ReqQueryChannel> commReq) throws Exception {
		List<RespChannelList> respList = new ArrayList<>();
		Set<String[]> channelSet = KasiteConfig.getClientIds();
		Map<String, RespChannelList> map = new TreeMap<>();
		for (String[] channel : channelSet) {
			RespChannelList resp = new RespChannelList();
			String key = channel[0];
			String value = channel[1];
			String isOpen = channel[2];
			if("false".equals(isOpen)) {
				continue;
			}
			
			resp.setChannelId(key);
			resp.setChannelName(value);
			map.put(key, resp);
		}
		if(map != null) {
			for (String key: map.keySet()) {
				respList.add(map.get(key));
			}
		}
		return new CommonResp<RespChannelList>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}
	
	@Override
	public List<MerchConfigVo> queryAllMerchConfigList(PageVo page, String account) throws Exception {
		List<MerchConfigVo> respList = new LinkedList<>();
		Map<String, ChannelTypeEnum> configkeyMap = KasiteConfig.getAllConfigKey();
		int serialId = 1;
		for (String configKey : configkeyMap.keySet()) {
			if(StringUtil.isNotBlank(account) && !configKey.equals(account)) {
				continue;
			}
			MerchConfigVo vo = new MerchConfigVo();
			ChannelTypeEnum payInfo = KasiteConfig.getPayTypeByConfigKey(configKey);
			if(payInfo != null) {
				String payMethodName = payInfo.getTitle();
				if(ChannelTypeEnum.zfb.equals(payInfo)) {
					vo.setSerialId(serialId);
					vo.setConfigkey(configKey);
					vo.setMerchId(KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_appId, configKey));
//					vo.setMerchPayConfig();
					vo.setBankNo(KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_num, configKey));
					vo.setBankShortName(KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_abs_name, configKey));
					vo.setMerchType(payInfo.name());
					vo.setMerchTypeName(payMethodName);
				}else if(ChannelTypeEnum.wechat.equals(payInfo)) {
					vo.setSerialId(serialId);
					vo.setConfigkey(configKey);
					vo.setMerchId(KasiteConfig.getWxPay(WXPayEnum.wx_mch_id, configKey));
					vo.setBankNo(KasiteConfig.getWxPay(WXPayEnum.bank_num, configKey));
					vo.setBankShortName(KasiteConfig.getWxPay(WXPayEnum.bank_abs_name, configKey));
//					vo.setMerchPayConfig();
					vo.setMerchType(payInfo.name());
					vo.setMerchTypeName(payMethodName);
				}else {
					continue;
				}
			}
			respList.add(vo);
			serialId++;
		}
		
		page.setPCount(configkeyMap.size());
		return respList;
	}
	
	@Override
	public CommonResp<RespQueryChannelMerchInfo> queryChannelMerchInfo(CommonReq<ReqQueryChannel> commReq) throws Exception {
		Map<String, List<ChannelMerchInfoVo>> retMap = new TreeMap<>();
		Set<String[]> channelSet = KasiteConfig.getClientIds();
		for (String[] channel : channelSet) {
			List<ChannelMerchInfoVo> list = new ArrayList<>();
			String channelId = channel[0];
			String channelName = channel[1];
			if(StringUtil.isNotBlank(KasiteConfig.getClientConfig(ClientConfigEnum.WxPayConfigKey, channelId))) {
				String isEnable = KasiteConfig.getClientConfig(ClientConfigEnum.isOpen, channelId);
				String wxConfigKey = KasiteConfig.getClientConfig(ClientConfigEnum.WxPayConfigKey, channelId);
				if(StringUtil.isNotBlank(wxConfigKey)) {
					String[] wxArr = wxConfigKey.split(",");
					for (String key : wxArr) {
						ChannelMerchInfoVo vo = new ChannelMerchInfoVo();
						vo.setChannelId(channelId);
						vo.setChannelName(channelName);
						vo.setIsEnable(isEnable.equals("true")?KstHosConstant.IS_ENABLE_1:KstHosConstant.IS_ENABLE_0);
						ChannelTypeEnum payInfo = KasiteConfig.getPayTypeByConfigKey(key);
						if(payInfo != null) {
							vo.setMerchType(payInfo.name());
							vo.setMerchTypeName(payInfo.getTitle());
							String bankNo = KasiteConfig.getWxPay(WXPayEnum.bank_num, key);
							String bankName = KasiteConfig.getWxPay(WXPayEnum.bank_name, key);
							vo.setBankNo(bankNo);
							vo.setBankName(bankName);
						}
						vo.setMerchAccount(KasiteConfig.getWxPay(WXPayEnum.wx_mch_id, key));
						vo.setMerchCode(key);
						list.add(vo);
					}
				}
				
			}
			if(StringUtil.isNotBlank(KasiteConfig.getClientConfig(ClientConfigEnum.ZfbConfigKey, channelId))) {
				String isEnable = KasiteConfig.getClientConfig(ClientConfigEnum.isOpen, channelId);
				String zfbConfigKey = KasiteConfig.getClientConfig(ClientConfigEnum.ZfbConfigKey, channelId);
				if(StringUtil.isNotBlank(zfbConfigKey)) {
					String[] zfbArr = zfbConfigKey.split(",");
					for (String key : zfbArr) {
						ChannelMerchInfoVo vo = new ChannelMerchInfoVo();
						vo.setChannelId(channelId);
						vo.setChannelName(channelName);
						vo.setIsEnable(isEnable.equals("true")?KstHosConstant.IS_ENABLE_1:KstHosConstant.IS_ENABLE_0);
						ChannelTypeEnum payInfo = KasiteConfig.getPayTypeByConfigKey(key);
						if(payInfo != null) {
							vo.setMerchType(payInfo.name());
							vo.setMerchTypeName(payInfo.getTitle());
							String bankNo = KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_num, key);
							String bankName = KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_name, key);
							vo.setBankNo(bankNo);
							vo.setBankName(bankName);
						}
						vo.setMerchAccount(KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_appId, key));
						vo.setMerchCode(key);
						list.add(vo);
					}
				}
			}
			if(StringUtil.isBlank(KasiteConfig.getClientConfig(ClientConfigEnum.WxPayConfigKey, channelId)) &&
					StringUtil.isBlank(KasiteConfig.getClientConfig(ClientConfigEnum.ZfbConfigKey, channelId))) {
				ChannelMerchInfoVo vo = new ChannelMerchInfoVo();
				vo.setChannelId(channelId);
				vo.setChannelName(channelName);
				String isEnable = KasiteConfig.getClientConfig(ClientConfigEnum.isOpen, channelId);
				vo.setIsEnable(isEnable.equals("true")?KstHosConstant.IS_ENABLE_1:KstHosConstant.IS_ENABLE_0);
				list.add(vo);
			}
			retMap.put(channelName, list);
		}
		List<RespQueryChannelMerchInfo> respList = new ArrayList<>();
		if(retMap != null) {
			for (String channelName : retMap.keySet()) {
				List<ChannelMerchInfoVo> list = retMap.get(channelName);
				for (int i = 0; i < list.size(); i++) {
					ChannelMerchInfoVo vo = list.get(i);
					RespQueryChannelMerchInfo resp = new RespQueryChannelMerchInfo();
					BeanCopyUtils.copyProperties(vo, resp, null);
					respList.add(resp);
				}
			}
		}
		return new CommonResp<RespQueryChannelMerchInfo>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}
	
	public CommonResp<RespQueryChannelMerchInfo> queryChannelMerchInfo1(CommonReq<ReqQueryChannel> commReq) throws Exception {
		List<ChannelMerchInfoVo> retList = new ArrayList<>();
		Set<String[]> channelSet = KasiteConfig.getClientIds();
		for (String[] channel : channelSet) {
			String channelId = channel[0];
			String channelName = channel[1];
			if(StringUtil.isNotBlank(KasiteConfig.getClientConfig(ClientConfigEnum.WxPayConfigKey, channelId))) {
				String isEnable = KasiteConfig.getClientConfig(ClientConfigEnum.isOpen, channelId);
				String wxConfigKey = KasiteConfig.getClientConfig(ClientConfigEnum.WxPayConfigKey, channelId);
				if(StringUtil.isNotBlank(wxConfigKey)) {
					String[] wxArr = wxConfigKey.split(",");
					for (String key : wxArr) {
						ChannelMerchInfoVo vo = new ChannelMerchInfoVo();
						vo.setChannelId(channelId);
						vo.setChannelName(channelName);
						vo.setIsEnable(isEnable.equals("true")?KstHosConstant.IS_ENABLE_1:KstHosConstant.IS_ENABLE_0);
						ChannelTypeEnum payInfo = KasiteConfig.getPayTypeByConfigKey(key);
						if(payInfo != null) {
							vo.setMerchType(payInfo.name());
							vo.setMerchTypeName(payInfo.getTitle());
							String bankNo = KasiteConfig.getWxPay(WXPayEnum.bank_num, key);
							String bankName = KasiteConfig.getWxPay(WXPayEnum.bank_name, key);
							vo.setBankNo(bankNo);
							vo.setBankName(bankName);
						}
						vo.setMerchAccount(KasiteConfig.getWxPay(WXPayEnum.wx_mch_id, key));
						vo.setMerchCode(key);
						retList.add(vo);
					}
				}
				
			}
			if(StringUtil.isNotBlank(KasiteConfig.getClientConfig(ClientConfigEnum.ZfbConfigKey, channelId))) {
				String isEnable = KasiteConfig.getClientConfig(ClientConfigEnum.isOpen, channelId);
				String zfbConfigKey = KasiteConfig.getClientConfig(ClientConfigEnum.ZfbConfigKey, channelId);
				if(StringUtil.isNotBlank(zfbConfigKey)) {
					String[] zfbArr = zfbConfigKey.split(",");
					for (String key : zfbArr) {
						ChannelMerchInfoVo vo = new ChannelMerchInfoVo();
						vo.setChannelId(channelId);
						vo.setChannelName(channelName);
						vo.setIsEnable(isEnable.equals("true")?KstHosConstant.IS_ENABLE_1:KstHosConstant.IS_ENABLE_0);
						ChannelTypeEnum payInfo = KasiteConfig.getPayTypeByConfigKey(key);
						if(payInfo != null) {
							vo.setMerchType(payInfo.name());
							vo.setMerchTypeName(payInfo.getTitle());
							String bankNo = KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_num, key);
							String bankName = KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_name, key);
							vo.setBankNo(bankNo);
							vo.setBankName(bankName);
						}
						vo.setMerchAccount(KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_appId, key));
						vo.setMerchCode(key);
						retList.add(vo);
					}
				}
			}
			if(StringUtil.isBlank(KasiteConfig.getClientConfig(ClientConfigEnum.WxPayConfigKey, channelId)) &&
					StringUtil.isBlank(KasiteConfig.getClientConfig(ClientConfigEnum.ZfbConfigKey, channelId))) {
				ChannelMerchInfoVo vo = new ChannelMerchInfoVo();
				vo.setChannelId(channelId);
				vo.setChannelName(channelName);
				String isEnable = KasiteConfig.getClientConfig(ClientConfigEnum.isOpen, channelId);
				vo.setIsEnable(isEnable.equals("true")?KstHosConstant.IS_ENABLE_1:KstHosConstant.IS_ENABLE_0);
				retList.add(vo);
			}
		}
		ReqQueryChannel req = commReq.getParam();
		PageInfo<ChannelMerchInfoVo> page = new PageInfo<>(retList);
		Long total = page.getTotal();
		if(req.getPage() != null) {
			req.getPage().setPCount(total.intValue());
		}
		List<RespQueryChannelMerchInfo> respList = new ArrayList<>();
		if(req.getPage() != null && req.getPage().getPSize() > 0) {
			int pIndex = req.getPage().getPIndex();
			int pSize = req.getPage().getPSize();
			int startIndex = (pIndex-1)*pSize;
			int endIndex = pIndex*pSize;
			if(startIndex > total) {
				req.getPage().setPIndex(1);
				startIndex = 0;
				endIndex = 1*pSize;
			}else if(endIndex > total) {
				endIndex = total.intValue();
			}
			for (int i=startIndex; i<endIndex; i++) {
				ChannelMerchInfoVo vo = retList.get(i);
				RespQueryChannelMerchInfo resp = new RespQueryChannelMerchInfo();
				BeanCopyUtils.copyProperties(vo, resp, null);
				respList.add(resp);
			}
		}
		return new CommonResp<RespQueryChannelMerchInfo>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList, req.getPage());
	}

	@Override
	public List<MerchConfigVo> queryMerchType() throws Exception {
		List<MerchConfigVo> respList = new ArrayList<>();
		ChannelTypeEnum[] payArr = KasiteConfig.getPayChannelTypes();
		if(payArr ==  null || payArr.length < 1) {
			return null;
		}
		for (ChannelTypeEnum obj : payArr) {
			MerchConfigVo resp = new MerchConfigVo();
			resp.setMerchType(obj.name());
			resp.setMerchTypeName(obj.getTitle());
			respList.add(resp);
		}
		return respList;
	}

	@Override
	public Map<String, String> queryBankShort() throws Exception {
		Map<String, String> map = new HashMap<>();
		Map<String, ChannelTypeEnum> configkeyList = KasiteConfig.getAllConfigKey();
		for (String configkey : configkeyList.keySet()) {
			String bankNo = null;
			String bankName = null;
			ChannelTypeEnum payInfo = KasiteConfig.getPayTypeByConfigKey(configkey);
			if(payInfo == null) {
				continue;
			}
			if(ChannelTypeEnum.zfb.equals(payInfo)) {
				bankNo = KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_num, configkey);
				bankName = KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_abs_name, configkey);
			}else if(ChannelTypeEnum.wechat.equals(payInfo)) {
				bankNo = KasiteConfig.getWxPay(WXPayEnum.bank_num, configkey);
				bankName = KasiteConfig.getWxPay(WXPayEnum.bank_abs_name, configkey);
			}
			map.put(bankNo, bankName);
		}
		return map;
	}

	@Override
	public CommonResp<RespQueryPayConfig> queryPayConfig(CommonReq<ReqQueryPayConfig> commReq) throws Exception {
		RespQueryPayConfig resp = new RespQueryPayConfig();
		PayConfig vo = payConfigMapper.selectOne(null);
		BeanCopyUtils.copyProperties(vo, resp, null);
		return new CommonResp<RespQueryPayConfig>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	@Override
	public CommonResp<RespMap> updatePayConfig(CommonReq<ReqQueryPayConfig> commReq) throws Exception {
		ReqQueryPayConfig req = commReq.getParam();
		PayConfig vo = new PayConfig();
		vo.setOpPayMoneyLimit(req.getOpPayMoneyLimit());
		vo.setIhPayMoneyLimit(req.getIhPayMoneyLimit());
		Example example = new Example(PayConfig.class);
		example.createCriteria().andCondition("1=1");
		payConfigMapper.updateByExampleSelective(vo, example);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public CommonResp<RespQueryChannelMerchInfo> queryMerchConfigList(CommonReq<ReqQueryChannel> commReq)
			throws Exception {
		List<RespQueryChannelMerchInfo> respList = new ArrayList<>();
		Map<String, ChannelTypeEnum> configkeyList = KasiteConfig.getAllConfigKey();
		for (String configkey : configkeyList.keySet()) {
			RespQueryChannelMerchInfo resp = new RespQueryChannelMerchInfo();
			String merchId = null;
			String bankNo = null;
			String bankName = null;
			String bankShortName = null;
			ChannelTypeEnum payInfo = KasiteConfig.getPayTypeByConfigKey(configkey);
			if(payInfo == null) {
				continue;
			}
			if(ChannelTypeEnum.zfb.equals(payInfo)) {
				merchId = KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_appId, configkey);
				bankNo = KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_num, configkey);
				bankName = KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_name, configkey);
				bankShortName = KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_abs_name, configkey);
			}else if(ChannelTypeEnum.wechat.equals(payInfo)) {
				merchId = KasiteConfig.getWxPay(WXPayEnum.wx_mch_id, configkey);
				bankNo = KasiteConfig.getWxPay(WXPayEnum.bank_num, configkey);
				bankName = KasiteConfig.getWxPay(WXPayEnum.bank_name, configkey);
				bankShortName = KasiteConfig.getWxPay(WXPayEnum.bank_abs_name, configkey);
			}
			resp.setMerchAccount(merchId);
			resp.setMerchCode(configkey);
			resp.setMerchType(payInfo.name());
			resp.setMerchTypeName(payInfo.getTitle());
			resp.setBankNo(bankNo);
			resp.setBankName(bankName);
			resp.setBankShortName(bankShortName);
			
			respList.add(resp);
		}
		return new CommonResp<RespQueryChannelMerchInfo>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}

	@Override
	public CommonResp<RespQueryChannelMerchInfo> querySysChannelInfo(CommonReq<ReqQueryChannel> commReq)
			throws Exception {
		ReqQueryChannel req = commReq.getParam();
		String channelId = req.getChannelId();
		String channelName = KasiteConfig.getChannelById(channelId);
		RespQueryChannelMerchInfo resp = new RespQueryChannelMerchInfo();
		resp.setChannelId(channelId);
		resp.setChannelName(channelName);
		resp.setStatus(1);
		return new CommonResp<RespQueryChannelMerchInfo>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	@Override
	public CommonResp<RespMap> querySmartPayApi(CommonReq<ReqQueryChannel> commReq) throws Exception {
		ReqQueryChannel req = commReq.getParam();
		String channelId = req.getChannelId();
		Set<String> ipWhiteList = KasiteConfig.getClientIpWhite(channelId);  //ip的白名单
		ClientApiConfig apiConfig = KasiteConfig.getClientApiConfig(channelId);  //关闭的api权限列表
		Set<String> closeApiInfoSet = null;
		if(apiConfig != null) {
			closeApiInfoSet = apiConfig.getCloseApi();
		}
		JSONObject apiInfoJson = ApiList.me().apiJson();  //所有的api权限列表
		List<RespQueryChannelApi> apiList = new ArrayList<>();
		if(apiInfoJson != null && !apiInfoJson.isEmpty()) {
			for (String key : apiInfoJson.keySet()) {
				if(key.indexOf("pay.PayWs.") > -1) {
					RespQueryChannelApi api = new RespQueryChannelApi();
					api.setApi(key);
					api.setApiname(apiInfoJson.getString(key));
					//过滤存在于api权限列表黑名单中的key
					if(closeApiInfoSet != null && closeApiInfoSet.contains(key)) {
						api.setStatus(0);  //关闭
					}else {
						api.setStatus(1);  //开启
					}
					apiList.add(api);
				}
			}
		}
		RespMap respMap = new RespMap();
		respMap.put(ApiKey.QueryChannel.ApiList, apiList);
		respMap.put(ApiKey.QueryChannel.IpWhiteList, ipWhiteList);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respMap);
	}
	
	@Override
	public CommonResp<RespQueryChannelMerchInfo> queryChannelMerchAndApi(CommonReq<ReqQueryChannel> commReq)
			throws Exception {
		List<RespQueryChannelMerchInfo> respList = new ArrayList<>();
		ReqQueryChannel req = commReq.getParam();
		String channelId = req.getChannelId();
		String channelName = KasiteConfig.getChannelById(channelId);
		Set<String> configKeySet = KasiteConfig.getConfigKeyByChannelId(channelId);
		for (String configKey : configKeySet) {
			RespQueryChannelMerchInfo resp = new RespQueryChannelMerchInfo();
			String merchId = null;
			String bankNo = null;
			String bankName = null;
			String bankShortName = null;
			ChannelTypeEnum payInfo = KasiteConfig.getPayTypeByConfigKey(configKey);
			if(payInfo == null) {
				continue;
			}
			if(ChannelTypeEnum.zfb.equals(payInfo)) {
				merchId = KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_appId, configKey);
				bankNo = KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_num, configKey);
				bankName = KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_name, configKey);
				bankShortName = KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_abs_name, configKey);
			}else if(ChannelTypeEnum.wechat.equals(payInfo)) {
				merchId = KasiteConfig.getWxPay(WXPayEnum.wx_mch_id, configKey);
				bankNo = KasiteConfig.getWxPay(WXPayEnum.bank_num, configKey);
				bankName = KasiteConfig.getWxPay(WXPayEnum.bank_name, configKey);
				bankShortName = KasiteConfig.getWxPay(WXPayEnum.bank_abs_name, configKey);
			}
			PayRule payRule = KasiteConfig.getPayRule(configKey);
			if(payRule == null) {
				continue;
			}
			resp.setChannelId(channelId);
			resp.setChannelName(channelName);
			resp.setMerchAccount(merchId);
			resp.setMerchCode(configKey);
			resp.setMerchType(payInfo.name());
			resp.setMerchTypeName(payInfo.getTitle());
			resp.setBankNo(bankNo);
			resp.setBankName(bankName);
			resp.setBankShortName(bankShortName);
			resp.setSinglePriceLimit(payRule.getSingleLimitStart() + "~" + payRule.getSingleLimitEnd());
			resp.setPayTimeLimit(payRule.getPayTimeStart() + "~" + payRule.getPayTimeEnd());
			resp.setRefundTimeLimit(payRule.getRefundTimeStart() + "~" + payRule.getRefundTimeEnd());
			resp.setIsCredit("true".equals(payRule.getCreditCardsAccepted())?1:0);
			resp.setStatus(1);
			resp.setIsEnable(1);
			
			Set<String> ipWhiteSet = KasiteConfig.getClientIpWhite(channelId);  //ip的白名单
			JSONObject ipWhiteJson = new JSONObject();
			ipWhiteJson.put("IpWhiteSet", ipWhiteSet);
			resp.setIpWhiteJson(ipWhiteJson);
			ClientApiConfig apiConfig = KasiteConfig.getClientApiConfig(channelId);  //关闭的api权限列表
			Set<String> closeApiInfoSet = null;
			if(apiConfig != null) {
				closeApiInfoSet = apiConfig.getCloseApi();
			}
			JSONObject apiInfoJson = ApiList.me().apiJson();  //所有的api权限列表
			List<RespQueryChannelApi> apiList = new ArrayList<>();
			if(apiInfoJson != null && !apiInfoJson.isEmpty()) {
				for (String key : apiInfoJson.keySet()) {
					if(key.indexOf("pay.PayWs.") > -1) {
						RespQueryChannelApi api = new RespQueryChannelApi();
						api.setApi(key);
						api.setApiname(apiInfoJson.getString(key));
						//过滤存在于api权限列表黑名单中的key
						if(closeApiInfoSet != null && closeApiInfoSet.contains(key)) {
							api.setStatus(0);  //关闭
						}else {
							api.setStatus(1);  //开启
						}
						apiList.add(api);
					}else {
						continue;
					}
				}
			}
			resp.setApiList(apiList);
			respList.add(resp);
		}
		return new CommonResp<RespQueryChannelMerchInfo>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}
	
	@Override
	public CommonResp<RespQueryBankCrad> queryAllBankNo(CommonReq<ReqQueryBank> commReq) throws Exception {
		List<RespQueryBankCrad> respList = new ArrayList<>();
		Map<String, ChannelTypeEnum> configkeyList = KasiteConfig.getAllConfigKey();
		Map<String, RespQueryBankCrad> map = new HashMap<>();  //临时存储,去重
		for (String configKey : configkeyList.keySet()) {
			RespQueryBankCrad resp = new RespQueryBankCrad();
			String bankNo = null;
			String bankName = null;
			ChannelTypeEnum payInfo = KasiteConfig.getPayTypeByConfigKey(configKey);
			if(payInfo == null) {
				continue;
			}
			if(ChannelTypeEnum.zfb.equals(payInfo)) {
				bankNo = KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_num, configKey);
				bankName = KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_name, configKey);
			}else if(ChannelTypeEnum.wechat.equals(payInfo)) {
				bankNo = KasiteConfig.getWxPay(WXPayEnum.bank_num, configKey);
				bankName = KasiteConfig.getWxPay(WXPayEnum.bank_name, configKey);
			}
			resp.setBankNo(bankNo);
			resp.setBankName(bankName);
			
			map.put(bankNo, resp);
		}
		
		for (String bankNo : map.keySet()) {
			respList.add(map.get(bankNo));
		}
		
		return new CommonResp<RespQueryBankCrad>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}

}

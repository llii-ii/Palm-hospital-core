package com.kasite.core.serviceinterface.common.controller;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.coreframework.util.DateOper;
import com.coreframework.util.JsonUtil;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.WXConfigEnum;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.controller.AbstractController;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.SpringContextUtil;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.serviceinterface.module.basic.IBatService;
import com.kasite.core.serviceinterface.module.basic.cache.BatUserCache;
import com.kasite.core.serviceinterface.module.basic.cache.IBatUserLocalCache;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryBatUser;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryBatUser;
import com.yihu.hos.util.JSONUtil;
import com.yihu.wsgw.api.InterfaceMessage;

public abstract class AbsAddUserController extends AbstractController{
	
	public String addUser(String openId,String accountId,AuthInfoVo vo,BatUserCache json) throws Exception {
		try {
			//判断本地用户是否存在，不存在新增
			IBatUserLocalCache userLocalCache =  SpringContextUtil.getBean(IBatUserLocalCache.class);
			if(null != userLocalCache) {
				userLocalCache.put(openId, json);
			}
			String api = ApiModule.Bat.AddBatUser.getName();
			Document document = DocumentHelper.createDocument();
			Element req = document.addElement(KstHosConstant.REQ);
			XMLUtil.addElement(req, KstHosConstant.TRANSACTIONCODE, "15102");
			Element data = req.addElement(KstHosConstant.DATA);
			XMLUtil.addElement(data, "OpenId", json.getOpenId());
			XMLUtil.addElement(data, "NickName",json.getNickName());
			XMLUtil.addElement(data, "AccountId",accountId);
			XMLUtil.addElement(data, "Sex", json.getSex());
			XMLUtil.addElement(data, "City", json.getCity());
			XMLUtil.addElement(data, "Country", json.getCountry());
			XMLUtil.addElement(data, "Province", json.getProvince());
			XMLUtil.addElement(data, "Language", json.getLanguage());
			XMLUtil.addElement(data, "HeadImgUrl", json.getHeadImgUrl());
			XMLUtil.addElement(data, "Subscribe", json.getSubscribe());
			XMLUtil.addElement(data, "SubscribeTime", json.getSubscribeTime());
			XMLUtil.addElement(data, "UnionId", json.getUnionId());
			XMLUtil.addElement(data, "Remark", json.getRemark());
			XMLUtil.addElement(data, "GroupId", json.getGroupId());
			XMLUtil.addElement(data, "SyncTime", DateOper.getNow("yyyy-MM-dd HH:mm:ss"));
			XMLUtil.addElement(data, "SysId", json.getSysId());
			XMLUtil.addElement(data, "State", "1");

			InterfaceMessage msg = new InterfaceMessage();
			msg.setApiName(api);
			msg.setParam(document.asXML());
			msg.setParamType(KstHosConstant.INTYPE);
			msg.setOutType(KstHosConstant.OUTTYPE);
			msg.setAuthInfo(vo.toString());
			msg.setSeq(DateOper.getNow("yyyyMMddHHmmssSSS"));
			msg.setVersion(KstHosConstant.V);
			IBatService batService = SpringContextUtil.getBean(IBatService.class);
			if(null != batService) {
				return batService.AddBatUser(msg);
			}else {
				LogUtil.info(logger, "未实现用户管理，不进行保存用户信息");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(logger, "保存关注用户失败",e);
			throw e;
		}
	}


	protected void addWeChatUser(String openid,AuthInfoVo vo,net.sf.json.JSONObject userJs) throws Exception {
		IBatUserLocalCache userLocalCache = SpringContextUtil.getBean(IBatUserLocalCache.class);
		IBatService batService = SpringContextUtil.getBean(IBatService.class);
		boolean isInDB = true;//默认用户是有保存到系统中的
		if(null != batService) {
			InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(this.getClass(),ApiModule.Bat.QueryBatUser.getName(),null,vo);
			CommonResp<RespQueryBatUser> resp = batService.queryBatUser(new CommonReq<ReqQueryBatUser>(new ReqQueryBatUser(msg, openid, null, null, null, null, null, null, null, null, null, null, null, null, null, null)));
			if(resp.getCode().equals(RetCode.Basic.ERROR_NOTFINDMEMBER.getCode().toString())) {
				isInDB = false;//数据库中没有找到该用户
			}
		}
		if((null != userLocalCache && userLocalCache.get(openid)==null) || !isInDB) {
			BatUserCache cache = new BatUserCache();
			cache = new BatUserCache();
			cache.setOpenId(JSONUtil.getJsonString(userJs, "openid"));
			cache.setNickName(JsonUtil.getJsonString(userJs, "nickname"));
			cache.setSex(JSONUtil.getJsonInt(userJs, "sex", false, 3));
			cache.setCity(JSONUtil.getJsonString(userJs, "city"));
			cache.setCountry(JSONUtil.getJsonString(userJs, "country"));
			cache.setProvince(JSONUtil.getJsonString(userJs, "province"));
			cache.setLanguage(JSONUtil.getJsonString(userJs, "language"));
			cache.setHeadImgUrl(JSONUtil.getJsonString(userJs, "headimgurl"));
			cache.setSubscribe(JSONUtil.getJsonInt(userJs, "subscribe", false));
			cache.setSubscribeTime(JSONUtil.getJsonInt(userJs, "subscribe_time", false));
			cache.setUnionId(JSONUtil.getJsonString(userJs, "unionid", false));
			cache.setRemark(JSONUtil.getJsonString(userJs, "remark"));
			cache.setGroupId(JSONUtil.getJsonInt(userJs, "groupid", false));
			cache.setConfigKey(vo.getConfigKey());
			userLocalCache.put(openid, cache);
			addUser(openid, KasiteConfig.getWxConfig(WXConfigEnum.wx_app_id,vo.getConfigKey()), vo, cache);
		}
	}
	
}

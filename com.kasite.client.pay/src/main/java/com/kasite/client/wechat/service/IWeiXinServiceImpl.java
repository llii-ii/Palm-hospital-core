package com.kasite.client.wechat.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.MsgCenterConfig;
import com.kasite.core.common.config.MsgCenterConfigVo;
import com.kasite.core.common.config.WXConfigEnum;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.HttpRequstBusSender;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;
import com.kasite.core.serviceinterface.module.wechat.IWeiXinService;
import com.kasite.core.serviceinterface.module.wechat.req.ReqGetShortUrl;
import com.kasite.core.serviceinterface.module.wechat.req.ReqQueryAllTemplateList;
import com.kasite.core.serviceinterface.module.wechat.req.ReqSendCustomMessage;
import com.kasite.core.serviceinterface.module.wechat.req.ReqSendTemplateMessage;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author cyh
 * @version 1.0 2017-11-17 下午2:29:38
 */
@Service
public class IWeiXinServiceImpl implements IWeiXinService {

	@Override
	public CommonResp<RespMap> sendCustomMessage(CommonReq<ReqSendCustomMessage> commReq) throws Exception {
		ReqSendCustomMessage req = commReq.getParam();
		JSONObject resJs = WeiXinService.sendCustomMessageByWxKey(req.getWxKey(), req.getContent());
		if(resJs!=null && resJs.has("errcode") && "0".equals(resJs.getString("errcode"))) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		}
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_UNKNOWN,"发送客服消息失败："+(resJs!=null?resJs.toString():""));
	}

	@Override
	public CommonResp<RespMap> sendTemplateMessage(CommonReq<ReqSendTemplateMessage> commReq) throws Exception {
		ReqSendTemplateMessage req = commReq.getParam();
		JSONObject resJs = WeiXinService.sendTemplateMessageByWxKey(req.getWxKey(), req.getContent());
		if(resJs!=null && resJs.has("errcode") && "0".equals(resJs.getString("errcode"))) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		}
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_UNKNOWN,"发送客服消息失败："+(resJs!=null?resJs.toString():""));
	}

	@Override
	public CommonResp<RespMap> getWechatShorturl(CommonReq<ReqGetShortUrl> commReq) throws Exception {
		ReqGetShortUrl req = commReq.getParam();
		String wxKey = req.getWxKey();
		String long_url = req.getContent();
		String shortUrl = WeiXinService.getShortUrl(wxKey, long_url);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,new RespMap().put(ApiKey.getWechatShorturl.short_url,shortUrl));
	}
	@Autowired
	MsgCenterConfig msgConfig;
	@Override
	public CommonResp<RespMap> QueryWxTemplateList(CommonReq<ReqQueryAllTemplateList> commReq) throws Exception {
		JSONObject resJs = new JSONObject();
		ReqQueryAllTemplateList req = commReq.getParam();
		String wxKey = req.getWxKey().split(",")[0];
		String modeId = req.getModeId();
		MsgCenterConfigVo auth = msgConfig.getAuth();
		String queryWxTemplateListUrl = "";
		if(auth!=null&&"1".equals(auth.getState())){
			String appid = KasiteConfig.getWxConfig(WXConfigEnum.wx_app_id, wxKey);
			queryWxTemplateListUrl = auth.getQueryWxTemplateListUrl();
			queryWxTemplateListUrl = queryWxTemplateListUrl.replace("{appId}", appid);
			HttpRequstBusSender sender = HttpRequestBus.create(queryWxTemplateListUrl, RequestType.post);
			SoapResponseVo response = sender.send();
			int httpStausCode = response.getCode();
			if(response==null || httpStausCode!=200) {
			}
			else{
				resJs = JSONObject.fromObject(response.getResult());
			}
		}
		else{
			resJs = WeiXinService.QueryWxTemplateList(wxKey);
		}
		JSONArray jsonArray = resJs.getJSONArray("template_list");
		List<RespMap> list = new ArrayList<RespMap>();
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String  template_id = jsonObject.get("template_id").toString();
			if(modeId!=null&&!"".equals(modeId)&&!template_id.equals(modeId)){
				continue;
			}
			String  title = jsonObject.get("title").toString();
			String  primary_industry = jsonObject.get("primary_industry").toString();
			String  deputy_industry = jsonObject.get("deputy_industry").toString();
			String  content = jsonObject.get("content").toString();
			content = content.replaceAll("\n", "</br>");
			String  example = jsonObject.get("example").toString();
			example = example.replaceAll("\n", "</br>");
			RespMap resp = new RespMap();
			resp.put(ApiKey.MsgCenterResp.template_id,template_id);
			resp.put(ApiKey.MsgCenterResp.title,title);
			resp.put(ApiKey.MsgCenterResp.primary_industry,primary_industry);
			resp.put(ApiKey.MsgCenterResp.deputy_industry,deputy_industry);
			resp.put(ApiKey.MsgCenterResp.content,content);
			resp.put(ApiKey.MsgCenterResp.example,example);
			list.add(resp);
		}
		CommonResp<RespMap> commonResp = new CommonResp<RespMap>(commReq,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,list);
		return commonResp;
	
	}


}

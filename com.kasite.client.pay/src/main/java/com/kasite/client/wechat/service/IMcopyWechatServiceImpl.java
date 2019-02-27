package com.kasite.client.wechat.service;

import org.springframework.stereotype.Service;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.wechat.constants.WeiXinConstant;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqExpressOrder;
import com.kasite.core.serviceinterface.module.wechat.IMcopyWechatService;
import com.kasite.core.serviceinterface.module.wechat.req.ReqMcopyWechat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class IMcopyWechatServiceImpl implements IMcopyWechatService{
	
	/**
	 * 获取微信票据，通过wxKey获取accessToken后发送
	 */
	@Override
	public CommonResp<RespMap> getWechatTicket(CommonReq<ReqMcopyWechat> commReq) throws Exception {
		ReqMcopyWechat req = commReq.getParam();
		String ticket = WeiXinService.getTicket(req.getConfigKey(), req.getType());
		if (StringUtil.isBlank(ticket)) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}else {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,ticket);	
		}
	}

	/**
	 * 获取微信临时素材
	 */
	@Override
	public CommonResp<RespMap> getWechatMedia(CommonReq<ReqMcopyWechat> commReq,String url) throws Exception {
		ReqMcopyWechat req = commReq.getParam();
		WeiXinService.getMedia(req.getConfigKey(), req.getMediaId(), url);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	
	/**
	 * 微信消息回调
	 */
	@Override
	public CommonResp<RespMap> sendMessage(CommonReq<ReqExpressOrder> commReq) throws Exception {
		try {
			ReqExpressOrder req = commReq.getParam();	
	        JSONObject json =new JSONObject();
	        json.put("touser", req.getOpenId());
	        json.put("msgtype", WeiXinConstant.RESP_MESSAGE_TYPE_NEWS);
	        JSONObject news =new JSONObject();
	        JSONArray articles =new JSONArray();
	        JSONObject list =new JSONObject();
	        String description = "";
	        String msgUrl = req.getMsgUrl();
	        int num = msgUrl.indexOf("?");
	        msgUrl = msgUrl.substring(0, num);
	        if("2".equals(req.getPayState())) {
                msgUrl = msgUrl.replace("pay-success", "orderList");
	        	//msgUrl = msgUrl.replace("payment", "orderList").replace("pay", "mCopy");
				list.put("title","病历复印申请通知"); //标题
				description += "您好，您已完成病历复印申请。\n";
	        	description += "姓名："+req.getName()+"\n";
	        	description += "病历号："+req.getPatientId()+"\n";
	        	description += "申请时间："+req.getApplyTime()+"\n";
	        	description += "付款金额："+req.getTotalMoney()+"元";
			}else if ("4".equals(req.getOrderState())) {
	        	list.put("title","病历复印取消通知"); //标题
	        	description += "您好，您已取消病历复印申请。\n";
	        	description += "姓名："+req.getName()+"\n";
	        	description += "病历号："+req.getPatientId()+"\n";
	        	if ("4".equals(req.getPayState())) {
	            	description += "退款时间："+req.getUpdateTime()+"\n";
	            	description += "退款金额："+req.getTotalMoney()+"元";
				}
	        	msgUrl = msgUrl.replace("detail", "orderList");
			} 
	        list.put("description",description); //描述
	        list.put("url",msgUrl); //点击图文链接跳转的地址
	        list.put("picurl",""); //图文链接的图片
	        articles.add(list);
	        news.put("articles", articles);
	        JSONObject text =new JSONObject();
	        json.put("text", text);
	        json.put("news", news);
	        if(!"0".equals(req.getPayState())) {
				WeiXinService.sendCustomMessage(commReq.getParam().getConfigKey(),json.toString());
	        }
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_SENDMSG);
		}
	}
}

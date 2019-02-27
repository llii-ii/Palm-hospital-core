package com.kasite.server.admin;

import java.util.ArrayList;
import java.util.List;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.Form;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.kasite.server.admin.dingtalk.DingTalkUtil;
import com.kasite.server.admin.dingtalk.Env;
import com.taobao.api.ApiException;

public class dd {

	public static void main(String[] args) throws Exception {
		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");

		OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
		request.setUseridList("020045516328961");
		request.setAgentId(Env.AGENTID);
		request.setToAllUser(true);

		OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
//		msg.setMsgtype("text");
//		msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
//		msg.getText().setContent("test123");
//		request.setMsg(msg);

//		msg.setMsgtype("image");
//		msg.setImage(new OapiMessageCorpconversationAsyncsendV2Request.Image());
//		msg.getImage().setMediaId("@lADOdvRYes0CbM0CbA");
//		request.setMsg(msg);
//
//		msg.setMsgtype("file");
//		msg.setFile(new OapiMessageCorpconversationAsyncsendV2Request.File());
//		msg.getFile().setMediaId("@lADOdvRYes0CbM0CbA");
//		request.setMsg(msg);
//
//		msg.setMsgtype("link");
//		msg.setLink(new OapiMessageCorpconversationAsyncsendV2Request.Link());
//		msg.getLink().setTitle("test");
//		msg.getLink().setText("test");
//		msg.getLink().setMessageUrl("test");
//		msg.getLink().setPicUrl("test");
//		request.setMsg(msg);
//
//		msg.setMsgtype("markdown");
//		msg.setMarkdown(new OapiMessageCorpconversationAsyncsendV2Request.Markdown());
//		msg.getMarkdown().setText("##### text");
//		msg.getMarkdown().setTitle("### Title");
//		request.setMsg(msg);
//
		msg.setOa(new OapiMessageCorpconversationAsyncsendV2Request.OA());
		msg.getOa().setHead(new OapiMessageCorpconversationAsyncsendV2Request.Head());
		msg.getOa().getHead().setText("head");
		msg.getOa().getHead().setBgcolor("FFE61A1A");
		msg.getOa().setBody(new OapiMessageCorpconversationAsyncsendV2Request.Body());
		
		List<Form> list = new ArrayList<Form>();
		
		Form from = new Form();
		from.setKey("紧急通知：");
		from.setValue("要下雨了!");
		list.add(from);
		
		msg.getOa().getBody().setForm(list);
		msg.getOa().getBody().setAuthor("来自spring admin的消息");
		msg.setMsgtype("oa");
		request.setMsg(msg);
//
//		msg.setActionCard(new OapiMessageCorpconversationAsyncsendV2Request.ActionCard());
//		msg.getActionCard().setTitle("xxx123411111");
//		msg.getActionCard().setMarkdown("### 测试123111");
//		msg.getActionCard().setSingleTitle("测试测试");
//		msg.getActionCard().setSingleUrl("https://www.baidu.com");
//		msg.setMsgtype("action_card");
//		request.setMsg(msg);

		OapiMessageCorpconversationAsyncsendV2Response response = client.execute(request,DingTalkUtil.getAccessToken(Env.AppKey, Env.AppSecret));
		System.out.println("返回结果="+response.getBody());
	}
	
	
}

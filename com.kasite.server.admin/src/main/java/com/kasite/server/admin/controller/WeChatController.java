/*
 * Copyright 2014-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kasite.server.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.coreframework.util.StringUtil;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;
import com.kasite.server.admin.dingtalk.DingTalkUtil;
import com.kasite.server.admin.util.PropertyUtil;
import com.kasite.server.admin.wechat.SendMessageService;
import com.kasite.server.admin.wechat.WeiXinUtil;

import net.sf.json.JSONObject;

/**
 * 企业微信Controller
 * 
 * @author 無
 *
 */
@RestController
@ResponseBody
public class WeChatController {
	
	private static Logger log = LoggerFactory.getLogger(WeChatController.class);
	
	/**
	 *  发送文本卡片消息:根据医院ID获取消息接收者
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(path = "/weChat/push/textCardMessage")
	public String SendTextCardMessage(HttpServletRequest request, HttpServletResponse response) {
		String hosid = request.getParameter("hosid");
		String title = request.getParameter("title");
		String desc = request.getParameter("desc");
		String url = request.getParameter("url");
		String touser = request.getParameter("touser");
		String toparty = request.getParameter("toparty");
		return SendMessageService.SendTextCardMessage(title, desc, url, touser, toparty,hosid);
	}
	
	/**
	 * 钉钉获取消息发送结果
	 * @param taskId
	 * @return
	 */
	@RequestMapping(path = "/weChat/getSendResult/{taskId}")
	public String GetSendResult(@PathVariable String taskId) {
		return DingTalkUtil.getSendResult(Long.parseLong(taskId));
	}
	
	/**
	 *  发送钉钉OA消息:根据医院ID获取消息接收者
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(path = "/dingTalk/push/OAMessage")
	public static String SendOAMessage(HttpServletRequest request, HttpServletResponse response) {
		//获取参数
		String hosid = request.getParameter("hosid");
		String messageUrl = request.getParameter("url");
		String touser = request.getParameter("touser");
		String toparty = request.getParameter("toparty");
		String author = request.getParameter("author");
		net.sf.json.JSONObject formJson = new net.sf.json.JSONObject();
		Boolean toAllUser = false;
		try {
			formJson = JSONObject.fromObject(request.getParameter("formJson"));
			toAllUser = Boolean.parseBoolean(request.getParameter("toAllUser"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String imageUrl = request.getParameter("imageUrl");
		String content = request.getParameter("content");
		String num = request.getParameter("num");
		String unit = request.getParameter("unit");
		String title = request.getParameter("title");
		
		/*String hosid = "10000";
		String touser = "";
		String toparty = "";
		Boolean toAllUser = false;
		String author = "spring admin";
		Map<String, String> formMap = new HashMap<String, String>();
		formMap.put("1", "新年你是不是很《想家》？");
		formMap.put("2", "父母盼望《你快回来》，");
		formMap.put("3", "愿你有一双《隐形的翅膀》，");
		formMap.put("4", "让《一切随风》、");
		formMap.put("5", "《从开始到现在》，");
		formMap.put("6", "向着家乡《自由飞翔》。");
		formMap.put("7", "祝你《新年快乐》！");
		String imageUrl = "https://image2.cnpp.cn/upload/images/20181207/11410766737_640x427.jpg";
		String content = "";
		String num = "  么么哒！！！";
		String unit = "哈哈";
		String messageUrl = "";
		String title ="祝大家新年好！";*/
		
		JSONObject result = new JSONObject();
		result.put("RespCode", "-10000");
		result.put("RespMessage", "系统异常");	
		//获取推送人
		if (StringUtil.isBlank(hosid) && StringUtil.isBlank(touser)) {
			result.put("RespMessage", "医院ID:hosid、成员ID:touser不能同时为空");
			return result.toString();
		}
		if (StringUtil.isBlank(touser)) {
			touser = PropertyUtil.getProperty(hosid);
			if (StringUtil.isBlank(touser)) {
				result.put("RespMessage", "医院ID【" + hosid + "】下还未配置任何成员");
				return result.toString();
			}
		}
		//医院告警时 加上默认推送人
		if(StringUtil.isNotBlank(touser) && StringUtil.isNotBlank(hosid)) {
			String deaulf=PropertyUtil.getProperty("10000");
			log.info("默认接收人:"+deaulf);
			if(StringUtil.isNotBlank(deaulf)) {
				String[] deaulfArr= deaulf.split("\\|");
				for (int i = 0; i < deaulfArr.length; i++) {
					if(touser.indexOf(deaulfArr[i])==-1) {
						touser +="|"+deaulfArr[i];
					}
				}
			}
		}
		if (StringUtil.isNotBlank(touser)) {
			String touserName="";
			try {
				touserName = DingTalkUtil.getUserNamePL(touser.toLowerCase());
			} catch (Exception e) {
				touserName = touser;
			}
			formJson.put("接收人", touserName);
		}
		//发送
		touser = touser.replace("|", ",");
		String resp = DingTalkUtil.sendDingDingOAMessage(touser, toparty, toAllUser, author, formJson, imageUrl, content, num, unit,messageUrl,title);
		if (StringUtil.isNotBlank(resp)) {
			com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(resp);
			if (null != jsonObject) {
				int errCode = jsonObject.getIntValue("errcode");
				String errMsg = jsonObject.getString("errmsg");
				if (0 != errCode) {
					result.put("RespCode", errCode);
					result.put("RespMessage", "消息发送失败:" + errMsg);
					log.error("消息发送失败 errcode:{} errmsg:{}", errCode, errMsg);
				} else {
					result.put("RespCode", 10000);
					result.put("TaskId", jsonObject.getString("task_id"));
					result.put("RespMessage", "消息发送成功");
				}
			}
		}
		return result.toString();
	}

	/**
	 *  发送文本卡片消息:测试
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(path = "/weChat/push/test")
	public String test(HttpServletRequest request, HttpServletResponse response) {
		String hosid="10000";
		String title="系统通知";
        String description="<div class=\"gray\">2018年12月10日</div> "
        		+ "<div class=\"normal\">恭喜你抽中纸巾一包，领奖码：3523</div>"
        		+ "<div class=\"highlight\">请于2018年12月31日前联系黄嘤嘤领取</div>"
        		+ "<div class=\"highlight\">本消息由spring boot admin发送</div>";
        String url="https://shop.zhe800.com/products/ze150924150248000588?jump_source=1&qd_key=qyOwt6Jn";
        String touser = "fage";
        String toparty = "";
		return SendMessageService.SendTextCardMessage(title, description, url, touser, toparty,hosid);
	}

	/**
	 * 发送文本卡片消息TO消息接收者
	 * 
	 * @param title  标题 必填
	 * @param desc   描述 必填
	 * @param url    点击跳转地址
	 * @param touser 消息接收者 必填
	 * @return
	 */
	@RequestMapping(path = "/weChat/pushByUser/{title}/{desc}/{url}/{touser}")
	public String SendTextCardMessage(@PathVariable String title, @PathVariable String desc, @PathVariable String url, @PathVariable String touser) {
		return SendMessageService.SendTextCardMessage(title, desc, url, touser, "",null);
	}
	
	/**
	 * 发送文本卡片消息TO消息接收者
	 * 
	 * @param title  标题 必填
	 * @param desc   描述 必填
	 * @param touser 消息接收者 必填
	 * @return
	 */
	@RequestMapping(path = "/weChat/pushByUser/{title}/{desc}/{touser}")
	public String SendTextCardMessage(@PathVariable String title, @PathVariable String desc, @PathVariable String touser) {
		return SendMessageService.SendTextCardMessage(title, desc, "", touser, "",null);
	}
	
	/**
	 * Get发送文本卡片消息:根据医院ID获取消息接收者
	 * 
	 * @param title  标题 必填
	 * @param desc   描述 必填
	 * @param url    点击跳转地址
	 * @param touser 消息接收者 必填
	 * @return
	 */
	@RequestMapping(path = "/weChat/pushByHosId/{title}/{desc}/{url}/{hosid}")
	public String SendTextCardMessageByHosId(@PathVariable String title, @PathVariable String desc, @PathVariable String url, @PathVariable String hosid) {
		return SendMessageService.SendTextCardMessage(title, desc, url, "", "",hosid);
	}
	
	/**
	 * 发送文本卡片消息:根据医院ID获取消息接收者
	 * 
	 * @param title  标题 必填
	 * @param desc   描述 必填
	 * @param touser 消息接收者 必填
	 * @return
	 */
	@RequestMapping(path = "/weChat/pushByHosId/{title}/{desc}/{hosid}")
	public String SendTextCardMessageByHosId(@PathVariable String title, @PathVariable String desc, @PathVariable String hosid) {
		return SendMessageService.SendTextCardMessage(title, desc, "", "", "",hosid);
	}
	
	/**
	 * 获取医院消息接收人
	 * 
	 * @param hosid
	 * @param touser
	 * @return
	 */
	@RequestMapping(path = "/weChat/getUser/{hosid}")
	public String GetUser(@PathVariable String hosid) {
		return "医院ID【"+hosid+"】当前配置:"+PropertyUtil.getProperty(hosid);
	}
	
	/**
	 * 添加医院消息接收人
	 * 
	 * @param hosid
	 * @param touser
	 * @return
	 */
	@RequestMapping(path = "/weChat/addUser/{hosid}/{touser}")
	public String AddUser(@PathVariable String hosid, @PathVariable String touser) {
		return update(hosid, touser, false);
	}

	/**
	 * 删除医院消息接收人
	 * 
	 * @param hosid
	 * @param touser
	 * @return
	 */
	@RequestMapping(path = "/weChat/delUser/{hosid}/{touser}")
	public String DelUser(@PathVariable String hosid, @PathVariable String touser) {
		return update(hosid, touser, true);
	}

	private String update(String hosid, String touser,boolean isDel) {
		JSONObject result = new JSONObject();
		result.put("RespCode", -10000);
		if (StringUtil.isBlank(hosid)) {
			result.put("RespMessage", "医院ID:hosid不能为空");
			return result.toString();
		}
		if (StringUtil.isBlank(touser)) {
			result.put("RespMessage", "消息接收者:touser不能为空");
			return result.toString();
		}
		return PropertyUtil.updatedProps(hosid, touser, isDel);
	}
	
	@RequestMapping(path = "/weChat/loadProps")
	public String GetPro() {
		return PropertyUtil.loadProps();
	}
	
	@RequestMapping(path = "/weChat/getToken")
	public String token() {
		WeiXinUtil.init();
		return WeiXinUtil.accessToken.getToken();
	}
	
	public static void main(String[] args) throws Exception {
		//(hosid、touser不可同时为空)business工程的sprint admin中配置的orgCode（根据orgCode读取配置中的接收者）
		String hosid = "10001";
		//(hosid、touser不可同时为空)接收者的用户userid列表，最大列表长度：20
		String touser = "";
		//(可为空)接收者的部门id列表，最大列表长度：20,  接收者是部门id下(包括子部门下)的所有用户
		String toparty = "";
		//(可为空)是否发送给企业全部用户 默认否
		Boolean toAllUser = false;
		//(必须)消息体的标题
		String title ="祝大家新年好！";
		//(可为空)消息体中的图片
		String imageUrl = "https://image2.cnpp.cn/upload/images/20181207/11410766737_640x427.jpg";
		//(可为空)消息体的内容，最多显示3行
		String content = "开门红";
		//(可为空)消息点击链接地址
		String messageUrl = "http://www.baidu.com";
		//(必须)消息体的表单 键值对
		net.sf.json.JSONObject formJson = new net.sf.json.JSONObject();
		formJson.put("告警时间", "2019-02-08 12:33:52");
		formJson.put("医院ID", "10001");
		formJson.put("应用ID", "3e1206dc83ae");
		formJson.put("异常信息", "TimeOut");
		formJson.put("持续时间", "2m");
		//(可为空)单行富文本信息-数目
		String num = "200";
		//(可为空)单行富文本信息-单位
		String unit = "元";
		//(必须)自定义的作者名字
		String author = "spring admin";
		
		SoapResponseVo result = null;
		String posturl = null;
		try {
			posturl = "https://springadmin.kasitesoft.com/dingTalk/push/OAMessage";
//			posturl = "http://127.0.0.1:9999/dingTalk/push/OAMessage";
			result = HttpRequestBus.create(posturl, RequestType.post)
					.addHttpParam("hosid", hosid)
					.addHttpParam("touser", touser)
					.addHttpParam("toparty", toparty)
					.addHttpParam("title", title)
					.addHttpParam("imageUrl", imageUrl)
					.addHttpParam("content", content)
					.addHttpParam("url", messageUrl)
					.addHttpParam("formJson", formJson.toString())
					.addHttpParam("toAllUser", toAllUser+"")
					.addHttpParam("num", num)
					.addHttpParam("unit", unit)
					.addHttpParam("author", author)
					.send();
			System.out.println(result.getResult());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

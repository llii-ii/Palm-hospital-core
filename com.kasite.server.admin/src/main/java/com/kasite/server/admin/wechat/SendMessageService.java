package com.kasite.server.admin.wechat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coreframework.util.StringUtil;
import com.kasite.server.admin.util.PropertyUtil;
import com.kasite.server.admin.wechat.pbo.Textcard;
import com.kasite.server.admin.wechat.pbo.TextcardMessage;

import net.sf.json.JSONObject;

/**
 * 企业微信消息推送
 * 
 * @author 無
 *
 */
public class SendMessageService {

	private static final Logger logger = LoggerFactory.getLogger(SendMessageService.class);
	
	/**
	 * 	企微：卡片消息的展现形式非常灵活，支持使用br标签或者空格来进行换行处理，也支持使用div标签来使用不同的字体颜色， 目前内置了3种文字颜色：灰色(gray)、高亮(highlight)、默认黑色(normal)，将其作为div标签的class属性即可
		参数:	
		agent_id – 必填，企业应用的id，整型。可在应用的设置页面查看。
		user_ids – 成员ID列表。
		title – 必填，成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。
		description – 必填，描述，不超过512个字节，超过会自动截断
		url – 必填，点击后跳转的链接。
		btntxt – 按钮文字。 默认为“详情”， 不超过4个文字，超过自动截断。
		party_ids – 部门ID列表。
		tag_ids – 标签ID列表
	 * @param title  标题 必填
	 * @param desc   描述 必填
	 * @param url    点击跳转地址
	 * @param touser 消息接收者ID列表 必填
	 * @param toparty 部门ID列表 暂时未启用
	 * @param hosid 医院ID
	 * @return
	 */
	public static String SendTextCardMessage(String title, String description, String url, String touser,
			String toparty,String hosid) {
		//参数校验
		JSONObject result = new JSONObject();
		result.put("RespCode", -10000);
		if (StringUtil.isBlank(hosid) && StringUtil.isBlank(touser)) {
			result.put("RespMessage", "医院ID:hosid、成员ID:touser不能同时为空");
			return result.toString();
		}
		if (StringUtil.isBlank(title)) {
			result.put("RespMessage", "标题:title不能为空");
			return result.toString();
		}
		if (StringUtil.isBlank(description)) {
			result.put("RespMessage", "描述:description不能为空");
			return result.toString();
		}
//		if (StringUtil.isBlank(url)) {
//			result.put("RespMessage", "链接地址:url不能为空");
//			return result.toString();
//		}
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
			logger.info("默认接收人:"+deaulf);
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
			description += "<div class=\"normal\">" + "接收人:" + touser + "</div>";
		}
		
		logger.info("准备推送文本卡片消息给："+touser);
		if (StringUtil.isBlank(url)) {
			url = "https://springadmin.kasitesoft.com";
		}
		
		// 1.创建文本卡片消息对象
		TextcardMessage message = new TextcardMessage();
		// 1.1非必需
		/**
		 * touser 否 成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送
		 * toparty 否 部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数 totag 否
		 * 标签ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
		 */
		message.setTouser(touser);
		message.setToparty(toparty);
		// message.setTotag(totag);
		// message.setSafe(0);

		// 1.2必需
		message.setMsgtype("textcard");
		message.setAgentid(WeiXinUtil.agentId);

		Textcard textcard = new Textcard();
		textcard.setTitle(title);
		textcard.setDescription(description);
		textcard.setUrl(url);
//		textcard.setBtnTxt("详情");
		message.setTextcard(textcard);

		// 2.获取access_token
//		String accessToken = WeiXinUtil.getAccessToken(WeiXinUtil.corpId, WeiXinUtil.agentSecret).getToken();
//		logger.info("accessToken:" + accessToken);

		// 3.发送消息
		return WeiXinUtil.sendMessage(message);
	}

	
}
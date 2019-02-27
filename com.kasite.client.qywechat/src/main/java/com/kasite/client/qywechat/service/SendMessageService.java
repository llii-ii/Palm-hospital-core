package com.kasite.client.qywechat.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.kasite.client.qywechat.bean.message.vo.Article;
import com.kasite.client.qywechat.bean.message.vo.BaseMessage;
import com.kasite.client.qywechat.bean.message.vo.News;
import com.kasite.client.qywechat.bean.message.vo.NewsMessage;
import com.kasite.client.qywechat.bean.message.vo.Textcard;
import com.kasite.client.qywechat.bean.message.vo.TextcardMessage;
import com.kasite.client.qywechat.constant.QyWeChatConstant;
import com.kasite.client.qywechat.util.WeiXinUtil;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.QyWeChatConfig;
import com.kasite.core.common.constant.RetCode;

import net.sf.json.JSONObject;

/**
 * 企业微信消息推送
 * 
 * @author 無
 *
 */
@Component
public class SendMessageService {

	private static final Logger log = LoggerFactory.getLogger(SendMessageService.class);

	/**
	 * 推送消息
	 * 
	 * @param message 消息类
	 * @param tag     失败是否重试1次
	 * @param wxkey   configkey
	 * @return
	 * @throws Exception
	 */
	public static String sendMessage(BaseMessage message, Boolean tag, String wxkey) throws Exception {
		JSONObject result = new JSONObject();
		Gson gson = new Gson();
		String jsonMessage = gson.toJson(message);
		log.info("即将发送消息:" + jsonMessage);
		JSONObject jsonObject = WeiXinUtil.httpRequest(QyWeChatConstant.SENDMESSAGE_URL, "POST", jsonMessage, wxkey,
				false, tag);
		if (null != jsonObject) {
			log.info("返回结果:" + jsonObject.toString());
			if (QyWeChatConstant.SUCCESS_CODE != jsonObject.getInt(QyWeChatConstant.ERR_CODE)) {
				result.put("RespCode", jsonObject.getInt("errcode"));
				result.put("RespMessage", "消息发送失败:" + jsonObject.getString("errmsg"));
				log.error("消息发送失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
			} else {
				result.put("RespCode", RetCode.Success.RET_10000.getCode());
				result.put("RespMessage", "消息发送成功:" + jsonObject.getString("errmsg"));
			}
		}
		return result.toString();
	}

	/**
	 * 推送消息
	 * 
	 * @param message 消息类
	 * @param wxkey   configkey
	 * @return
	 * @throws Exception
	 */
	public static String sendMessage(BaseMessage message, String wxkey) throws Exception {
		return SendMessageService.sendMessage(message, null, wxkey);
	}

	/**
	 * 推送图文消息
	 * 
	 * @param title       标题，不超过128个字节，超过会自动截断
	 * @param description 描述，不超过512个字节，超过会自动截断
	 * @param url         点击后跳转的链接。
	 * @param picurl      图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图 1068*455，小图150*150。
	 * @param touser      成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送
	 * @param toparty     部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
	 * @param wxkey       configkey
	 * @return
	 * @throws Exception
	 */
	public String SendNewsMessageService(String title, String description, String url, String picurl, String touser,
			String toparty, String wxkey) throws Exception {
		log.info("准备推送图文消息touser=" + touser + ",toparty=" + toparty);
		// 1.创建图文消息对象
		NewsMessage message = new NewsMessage();
		// 1.1非必需
		// 不区分大小写
		message.setTouser(touser);
		message.setToparty(toparty);
		// message.setTotag(totag);
		// message.setSafe(0);
		// 消息类型，此时固定为：news
		message.setMsgtype("news");
		// 企业应用的id，整型。企业内部开发，可在应用的设置页面查看；第三方服务商，可通过接口 获取企业授权信息 获取该参数值
		message.setAgentid(Integer.parseInt(KasiteConfig.getQyWeChatConfig(QyWeChatConfig.agentid, wxkey)));
		// 图文消息，一个图文消息支持1到8条图文
		Article article1 = new Article();
		article1.setPicurl(picurl);
		article1.setTitle(title);
		article1.setDescription(description);
		article1.setUrl(url);
		List<Article> articles = new ArrayList<Article>();
		articles.add(article1);
		News news = new News();
		news.setArticles(articles);
		message.setNews(news);
		// 3.发送消息：调用业务类，发送消息
		return sendMessage(message, wxkey);
	}

	/**
	 * 推送卡片消息的展现形式非常灵活，支持使用br标签或者空格来进行换行处理，也支持使用div标签来使用不同的字体颜色，
	 * 目前内置了3种文字颜色：灰色(gray)、高亮(highlight)、默认黑色(normal)，将其作为div标签的class属性即可 参数:
	 * agent_id – 必填，企业应用的id，整型。可在应用的设置页面查看。 user_ids – 成员ID列表。 title –
	 * 必填，成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。 description – 必填，描述，不超过512个字节，超过会自动截断
	 * url – 必填，点击后跳转的链接。 btntxt – 按钮文字。 默认为“详情”， 不超过4个文字，超过自动截断。 party_ids –
	 * 部门ID列表。 tag_ids – 标签ID列表
	 * 
	 * @param title   标题 必填
	 * @param desc    描述 必填
	 * @param url     点击跳转地址
	 * @param touser  消息接收者ID列表 必填
	 * @param toparty 部门ID列表 暂时未启用
	 * @return
	 * @throws Exception
	 */
	public String SendTextCardMessage(String title, String description, String url, String touser, String toparty,
			String wxkey) throws Exception {
		log.info("准备推送文本卡片消息touser=" + touser + ",toparty=" + toparty);
		// 创建文本卡片消息对象
		TextcardMessage message = new TextcardMessage();
		/**
		 * touser 否 成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送
		 * toparty 否 部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数 totag 否
		 * 标签ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
		 */
		message.setTouser(touser);
		message.setToparty(toparty);
		// message.setTotag(totag);
		// message.setSafe(0);
		message.setMsgtype("textcard");
		message.setAgentid(Integer.parseInt(KasiteConfig.getQyWeChatConfig(QyWeChatConfig.agentid, wxkey)));

		Textcard textcard = new Textcard();
		textcard.setTitle(title);
		textcard.setDescription(description);
		textcard.setUrl(url);
//		textcard.setBtnTxt("详情");
		message.setTextcard(textcard);

		// 3.发送消息
		return sendMessage(message, wxkey);
	}
}
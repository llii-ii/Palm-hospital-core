package com.kasite.client.qywechat.service;

import com.google.gson.Gson;
import com.kasite.client.qywechat.bean.chat.req.ReqChat;
import com.kasite.client.qywechat.bean.chat.vo.Chat;
import com.kasite.client.qywechat.constant.QyWeChatConstant;
import com.kasite.client.qywechat.util.WeiXinUtil;

import net.sf.json.JSONObject;

/**
 * 群聊业务类
 * 
 * @author 無
 *
 */
public class AppChatService {

	public static void main(String[] args) throws Exception {
		String Chatid = "BAM0H5PNC4A6NVEV";
		String wxkey = "1000002";

		Chat chat = new Chat();
		chat.setChatid(Chatid);
		chat.setOwner("HeiTaoEr");
		chat.setName("年会");
		String[] userlist = { "HeiTaoEr", "TEST2", "WuFaDong" };
		chat.setUserlist(userlist);
		AppChatService.createChat(chat, wxkey);

		AppChatService.sendTextChat(wxkey, Chatid, "欢迎参加年会活动！");

//		ReqChat reqchat = new ReqChat();
//		reqchat.setChatid(Chatid);
//		reqchat.setOwner("HeiTaoEr");
//		reqchat.setName("new年会");
//		String[] adduserlist = {"XIAOLI","WuFaDong"};
//		reqchat.setAdd_user_list(adduserlist);
//		String[] deluserlist = {"TEST2"};
//		reqchat.setDel_user_list(deluserlist);
//		AppChatService.updateChat(wxkey, reqchat);

//		AppChatService.getChat(wxkey, Chatid);
	}

	/**
	 * 创建群聊会话
	 * 
	 * @param chat
	 * @param wxkey
	 * @return
	 * @throws Exception
	 */
	public static JSONObject createChat(Chat chat, String wxkey) throws Exception {
		Gson gson = new Gson();
		String json = gson.toJson(chat);
		System.out.println("json:" + json);
		return WeiXinUtil.httpRequest(QyWeChatConstant.CREATE_CHAT_URL, "POST", json, wxkey, false);
	}

	/**
	 * 修改群聊会话
	 * 
	 * @param wxkey
	 * @param reqChat
	 * @return
	 * @throws Exception
	 */
	public static JSONObject updateChat(String wxkey, ReqChat reqChat) throws Exception {
		Gson gson = new Gson();
		String json = gson.toJson(reqChat);
		System.out.println("json:" + json);
		return WeiXinUtil.httpRequest(QyWeChatConstant.UPDATE_CHAT_URL, "POST", json, wxkey, false);
	}

	/**
	 * 获取群聊会话
	 * 
	 * @param wxkey
	 * @param departmentId
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getChat(String wxkey, String chatId) throws Exception {
		String get_chat_url = QyWeChatConstant.GET_CHAT_URL.replace("CHATID", chatId);
		return WeiXinUtil.httpRequest(get_chat_url, "GET", null, wxkey, false);
	}

	/**
	 * 群聊-发送文本消息 当群聊创建时，我们需要发送一条文本消息，这样群聊会话才会显示出来
	 * 
	 * @param wxkey
	 * @param reqChat
	 * @return
	 * @throws Exception
	 */
	public static JSONObject sendTextChat(String wxkey, String chatId, String content) throws Exception {
		String json = "{" + "\"chatid\": \"" + chatId + "\"," + "\"msgtype\":\"text\"," + "\"text\":{"
				+ "\"content\" : \"" + content + "\"" + "}," + "\"safe\":0" + "};";
		System.out.println("json:" + json);
		return WeiXinUtil.httpRequest(QyWeChatConstant.SEND_CHAT_URL, "POST", json, wxkey, false);
	}
}
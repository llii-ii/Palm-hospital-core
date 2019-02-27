package com.kasite.client.qywechat.bean.chat.req;

/**
 * 群聊会话Req
 * 
 * @author 無
 *
 */
public class ReqChat {
	// 群聊id
	private String chatid;
	// 新的群聊名。若不需更新，请忽略此参数。最多50个utf8字符，超过将截断
	private String name;
	// 新群主的id。若不需更新，请忽略此参数
	private String owner;
	// 添加成员的id列表
	private String[] add_user_list;
	// 踢出成员的id列表
	private String[] del_user_list;

	public ReqChat() {
	};

	public ReqChat(String chatid, String name, String owner, String[] add_user_list, String[] del_user_list) {
		super();
		this.chatid = chatid;
		this.name = name;
		this.owner = owner;
		this.del_user_list = del_user_list;
		this.add_user_list = add_user_list;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String[] getAdd_user_list() {
		return add_user_list;
	}

	public void setAdd_user_list(String[] add_user_list) {
		this.add_user_list = add_user_list;
	}

	public String[] getDel_user_list() {
		return del_user_list;
	}

	public void setDel_user_list(String[] del_user_list) {
		this.del_user_list = del_user_list;
	}

	public String getChatid() {
		return chatid;
	}

	public void setChatid(String chatid) {
		this.chatid = chatid;
	}

}

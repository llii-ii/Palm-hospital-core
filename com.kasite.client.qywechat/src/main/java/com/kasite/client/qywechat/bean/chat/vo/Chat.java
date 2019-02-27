package com.kasite.client.qywechat.bean.chat.vo;

/**
 * 群聊会话
 * 
 * @author 無
 *
 */
public class Chat {
	// 群聊名，最多50个utf8字符，超过将截断
	private String name;
	// 指定群主的id。如果不指定，系统会随机从userlist中选一人作为群主
	private String owner;
	// 群成员id列表。至少2人，至多500人
	private String[] userlist;
	// 群聊的唯一标志，不能与已有的群重复；字符串类型，最长32个字符。只允许字符0-9及字母a-zA-Z。如果不填，系统会随机生成群id
	private String chatid;

	public Chat() {
	};

	public Chat(String name, String owner, String[] userlist, String chatid) {
		super();
		this.name = name;
		this.owner = owner;
		this.userlist = userlist;
		this.chatid = chatid;
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

	public String[] getUserlist() {
		return userlist;
	}

	public void setUserlist(String[] userlist) {
		this.userlist = userlist;
	}

	public String getChatid() {
		return chatid;
	}

	public void setChatid(String chatid) {
		this.chatid = chatid;
	}

}

package com.kasite.client.qywechat.bean.message.vo;

/**
 * 文本
 * 
 * @author 無
 *
 */
public class Text {
	// 是 消息内容，最长不超过2048个字节
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
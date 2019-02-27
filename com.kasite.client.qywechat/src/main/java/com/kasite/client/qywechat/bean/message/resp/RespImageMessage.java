package com.kasite.client.qywechat.bean.message.resp;

/**
 * @desc : 图片消息
 * 
 * @author: shirayner
 * @date : 2017-8-17 下午1:53:28
 */
public class RespImageMessage extends RespBaseMessage {

	private RespMedia Image;

	public RespMedia getImage() {
		return Image;
	}

	public void setImage(RespMedia image) {
		Image = image;
	}
}
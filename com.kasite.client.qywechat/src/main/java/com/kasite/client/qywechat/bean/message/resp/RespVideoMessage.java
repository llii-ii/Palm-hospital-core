package com.kasite.client.qywechat.bean.message.resp;

/**
 * @desc : 视频消息
 * 
 * @author: shirayner
 * @date : 2017-8-17 下午1:57:42
 */
public class RespVideoMessage extends RespBaseMessage {
	// 视频
	private RespMedia Voice;

	public RespMedia getVoice() {
		return Voice;
	}

	public void setVoice(RespMedia voice) {
		Voice = voice;
	}

}
package com.kasite.client.qywechat.bean.message.resp;

/**
 * @desc : 语音消息
 * 
 * @author: shirayner
 * @date : 2017-8-17 下午1:57:42
 */
public class RespVoiceMessage extends RespBaseMessage {
	// 语音
	private RespMedia Voice;

	public RespMedia getVoice() {
		return Voice;
	}

	public void setVoice(RespMedia voice) {
		Voice = voice;
	}

}

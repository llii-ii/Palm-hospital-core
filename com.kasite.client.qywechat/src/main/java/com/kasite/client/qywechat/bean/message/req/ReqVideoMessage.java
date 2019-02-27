package com.kasite.client.qywechat.bean.message.req;

/**
 * 视频请求类
 * 
 * @author 無
 *
 */
public class ReqVideoMessage extends ReqBaseMessage {
	// 视频
	private ReqVideo Video;

	public ReqVideo getVideo() {
		return Video;
	}

	public void setVideo(ReqVideo video) {
		Video = video;
	}
}
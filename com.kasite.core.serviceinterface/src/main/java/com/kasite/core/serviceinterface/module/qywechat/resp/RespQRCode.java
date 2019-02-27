package com.kasite.core.serviceinterface.module.qywechat.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 二维码RESP
 * 
 * @author 無
 *
 */
public class RespQRCode extends AbsResp {
	private String qrPicUrl;

	public String getQrPicUrl() {
		return qrPicUrl;
	}

	public void setQrPicUrl(String qrPicUrl) {
		this.qrPicUrl = qrPicUrl;
	}

}

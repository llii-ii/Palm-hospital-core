package com.kasite.core.serviceinterface.module.basic.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespQueryTitleInfo
 * @author: lcz
 * @date: 2018年7月23日 下午5:53:34
 */
public class RespQueryTitleInfo extends AbsResp{
	
	private String doctorTitle;
	private String doctorTitleCode;
	
	public String getDoctorTitle() {
		return doctorTitle;
	}
	public void setDoctorTitle(String doctorTitle) {
		this.doctorTitle = doctorTitle;
	}
	public String getDoctorTitleCode() {
		return doctorTitleCode;
	}
	public void setDoctorTitleCode(String doctorTitleCode) {
		this.doctorTitleCode = doctorTitleCode;
	}
	
	
	
}

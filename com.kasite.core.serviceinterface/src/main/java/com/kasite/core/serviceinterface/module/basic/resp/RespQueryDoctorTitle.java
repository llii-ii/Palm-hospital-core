package com.kasite.core.serviceinterface.module.basic.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * @author mhd
 * @version 1.0
 * 2017-6-21 下午3:12:34
 */
public class RespQueryDoctorTitle extends AbsResp {
	/**
	 * 医生职称
	 */
	private String doctorTitle;
	/**
	 * 医生职称编码
	 */
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

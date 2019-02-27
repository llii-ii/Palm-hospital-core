package com.kasite.client.crawler.modules.upload.job.service;

import java.io.File;

public class UploadFileVo {
	private File file;
	private String password;
	private String md5;
	private int code;
	//是否是质控包
	private boolean isZkPackage = false;
	/**
	 * 是否RSA加密密文 true 是 、 fasle 否
	 */
	private boolean isRSA = false;
	/**
	 * 1 结构化档案 2 非结构化档案 3 影像档案
	 */
	private String packType = "1";

	public boolean isRSA() {
		return isRSA;
	}
	public void setRSA(boolean isRSA) {
		this.isRSA = isRSA;
	}
	public String getPackType() {
		return packType;
	}
	public void setPackType(String packType) {
		this.packType = packType;
	}
	public boolean isZkPackage() {
		return isZkPackage;
	}
	public void setZkPackage(boolean isZkPackage) {
		this.isZkPackage = isZkPackage;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}

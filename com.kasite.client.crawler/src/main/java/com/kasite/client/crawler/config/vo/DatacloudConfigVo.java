package com.kasite.client.crawler.config.vo;

import org.springframework.beans.factory.annotation.Value;

public class DatacloudConfigVo {
	private String file_data;
	private String file_rule;
	private String file_dict;
	private String api_publickey;
	private String api_upload;
	private String api_zkUpload;
	private String basic_Upload;
    private String medical_Url;
	private String group_org_code;
	private String group_appSecret;
	private String group_inner_version;
	private String group_appKey;
	private String group_publicKey;
	private String fileupload_dir_zip;
	private String fileupload_dir_data;
	private String fileupload_dir_log;
	private Integer fileupload_threads;
	private Integer fileupload_tempdates;
	private Integer pageSize;
	private String mydict;
	private boolean isCheck;
	private boolean isUpload;
	private boolean isPrint;
	private boolean isStartReport;
	private boolean isStartJob;
	private boolean isZK;
	
	public boolean isZK() {
		return isZK;
	}
	public void setZK(boolean isZK) {
		this.isZK = isZK;
	}
	public boolean isPrint() {
		return isPrint;
	}
	public void setPrint(boolean isPrint) {
		this.isPrint = isPrint;
	}
	public boolean isStartReport() {
		return isStartReport;
	}
	public void setStartReport(boolean isStartReport) {
		this.isStartReport = isStartReport;
	}
	public boolean isStartJob() {
		return isStartJob;
	}
	public void setStartJob(boolean isStartJob) {
		this.isStartJob = isStartJob;
	}
	public String getFile_rule() {
		return file_rule;
	}
	public void setFile_rule(String file_rule) {
		this.file_rule = file_rule;
	}
	public String getBasic_Upload() {
		return basic_Upload;
	}
	public void setBasic_Upload(String basic_Upload) {
		this.basic_Upload = basic_Upload;
	}
	public void setApi_zkUpload(String api_zkUpload) {
		this.api_zkUpload = api_zkUpload;
	}
	public String getApi_zkUpload() {
		return api_zkUpload;
	}
	public boolean isUpload() {
		return isUpload;
	}
	public void setUpload(boolean isUpload) {
		this.isUpload = isUpload;
	}
	public String getGroup_publicKey() {
		return group_publicKey;
	}
	public void setGroup_publicKey(String group_publicKey) {
		this.group_publicKey = group_publicKey;
	}
	public boolean isCheck() {
		return isCheck;
	}
	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	public String getMydict() {
		return mydict;
	}
	public void setMydict(String mydict) {
		this.mydict = mydict;
	}
	
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getFile_data() {
		return file_data;
	}
	public void setFile_data(String file_data) {
		this.file_data = file_data;
	}
	public String getFile_dict() {
		return file_dict;
	}
	public void setFile_dict(String file_dict) {
		this.file_dict = file_dict;
	}
	public String getApi_publickey() {
		return api_publickey;
	}
	public void setApi_publickey(String api_publickey) {
		this.api_publickey = api_publickey;
	}
	public String getApi_upload() {
		return api_upload;
	}
	public void setApi_upload(String api_upload) {
		this.api_upload = api_upload;
	}
	public String getGroup_org_code() {
		return group_org_code;
	}
	public void setGroup_org_code(String group_org_code) {
		this.group_org_code = group_org_code;
	}
	public String getGroup_appSecret() {
		return group_appSecret;
	}
	public void setGroup_appSecret(String group_appSecret) {
		this.group_appSecret = group_appSecret;
	}
	public String getGroup_inner_version() {
		return group_inner_version;
	}
	public void setGroup_inner_version(String group_inner_version) {
		this.group_inner_version = group_inner_version;
	}
	public String getGroup_appKey() {
		return group_appKey;
	}
	public void setGroup_appKey(String group_appKey) {
		this.group_appKey = group_appKey;
	}
	public String getFileupload_dir_zip() {
		return fileupload_dir_zip;
	}
	public void setFileupload_dir_zip(String fileupload_dir_zip) {
		this.fileupload_dir_zip = fileupload_dir_zip;
	}
	public String getFileupload_dir_data() {
		return fileupload_dir_data;
	}
	public void setFileupload_dir_data(String fileupload_dir_data) {
		this.fileupload_dir_data = fileupload_dir_data;
	}
	public String getFileupload_dir_log() {
		return fileupload_dir_log;
	}
	public void setFileupload_dir_log(String fileupload_dir_log) {
		this.fileupload_dir_log = fileupload_dir_log;
	}
	public Integer getFileupload_threads() {
		return fileupload_threads;
	}
	public void setFileupload_threads(Integer fileupload_threads) {
		this.fileupload_threads = fileupload_threads;
	}
	public Integer getFileupload_tempdates() {
		return fileupload_tempdates;
	}
	public void setFileupload_tempdates(Integer fileupload_tempdates) {
		this.fileupload_tempdates = fileupload_tempdates;
	}
	
	public String getMedical_Url() {
		return medical_Url;
	}
	public void setMedical_Url(String medical_Url) {
		this.medical_Url = medical_Url;
	}
	public DatacloudConfigVo(String file_data, String file_dict, String api_publickey, String api_upload,
			String group_org_code, String group_appSecret, String group_inner_version, String group_appKey,
			String fileupload_dir_zip, String fileupload_dir_data, String fileupload_dir_log,
			Integer fileupload_threads, Integer fileupload_tempdates,Integer pageSize) {
		super();
		this.pageSize = pageSize;
		this.file_data = file_data;
		this.file_dict = file_dict;
		this.api_publickey = api_publickey;
		this.api_upload = api_upload;
		this.group_org_code = group_org_code;
		this.group_appSecret = group_appSecret;
		this.group_inner_version = group_inner_version;
		this.group_appKey = group_appKey;
		this.fileupload_dir_zip = fileupload_dir_zip;
		this.fileupload_dir_data = fileupload_dir_data;
		this.fileupload_dir_log = fileupload_dir_log;
		this.fileupload_threads = fileupload_threads;
		this.fileupload_tempdates = fileupload_tempdates;
	}
	public DatacloudConfigVo() {
		super();
	}
	
}
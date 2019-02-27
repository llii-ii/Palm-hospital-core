package com.kasite.client.crawler.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.coreframework.util.StringUtil;
import com.kasite.client.crawler.config.vo.DatacloudConfigVo;
@Configuration
public class DataCloudConfig {
    @Value("${datacloud.file.execute:#{null}}")
    private String execute;
    @Value("${datacloud.file.data:#{null}}")
    private String file_data;
    @Value("${datacloud.file.rule:#{null}}")
    private String file_rule;
    @Value("${datacloud.file.dict:#{null}}")
	private String file_dict;
    @Value("${datacloud.api.publickey:#{null}}")
	private String api_publickey;
    @Value("${datacloud.api.upload:#{null}}")
	private String api_upload;
    @Value("${datacloud.api.zkUpload:#{null}}")
	private String api_zkUpload;
    @Value("${datacloud.api.basicUpload:#{null}}")
    private String basic_Upload;
    @Value("${datacloud.api.medicalUrl:#{null}}")
    private String medical_Url;
    @Value("${datacloud.group.org_code:#{null}}")
	private String group_org_code;
    @Value("${datacloud.group.appSecret:#{null}}")
	private String group_appSecret;
    @Value("${datacloud.group.inner_version:#{null}}")
	private String group_inner_version;
    @Value("${datacloud.group.appKey:#{null}}")
	private String group_appKey;
    @Value("${datacloud.fileupload.filepath.zip:#{null}}")
	private String fileupload_dir_zip;
    @Value("${datacloud.fileupload.filepath.data:#{null}}")
	private String fileupload_dir_data;
    @Value("${datacloud.fileupload.filepath.log:#{null}}")
	private String fileupload_dir_log;
    @Value("${datacloud.fileupload.filepath.threads:#{null}}")
	private Integer fileupload_threads;
    @Value("${datacloud.fileupload.filepath.tempdates:#{null}}")
	private Integer fileupload_tempdates;
    @Value("${datacloud.fileupload.filepath.pageSize:#{null}}")
	private Integer pageSize;
    @Value("${datacloud.file.mydict:#{null}}")
	private String mydict;
    @Value("${datacloud.isCheck:#{null}}")
    private boolean isCheck;
    @Value("${datacloud.group.publicKey:#{null}}")
    private String publicKey;
    @Value("${datacloud.fileupload.filepath.isUpload:#{null}}")
    private boolean isUpload;
    @Value("${datacloud.isPrint:#{null}}")
    private boolean isPrint;
    @Value("${datacloud.isStartReport:#{null}}")
    private boolean isStartReport;
    @Value("${datacloud.isStartJob:#{null}}")
    private boolean isStartJob;
    @Value("${datacloud.isZK:#{null}}")
    private boolean isZK;
    
    @Bean
  	public DatacloudConfigVo getDatacloudConfigVo(){
    	
    		if(StringUtil.isNotBlank(fileupload_dir_data)) {
    			if(fileupload_dir_data.indexOf("user.dir") >= 0) {
    				fileupload_dir_data = fileupload_dir_data.replace("user.dir", System.getProperty("user.dir"));
    				File file = new File(fileupload_dir_data);
    				if(!file.exists()) {
    					file.mkdirs();
    				}
    			}
    		}
		if(StringUtil.isNotBlank(fileupload_dir_zip)) {
			if(fileupload_dir_zip.indexOf("user.dir") >= 0) {
				fileupload_dir_zip = fileupload_dir_zip.replace("user.dir", System.getProperty("user.dir"));
				File file = new File(fileupload_dir_zip);
				if(!file.exists()) {
					file.mkdirs();
				}
			}
    		}
		if(StringUtil.isNotBlank(fileupload_dir_log)) {
			if(fileupload_dir_log.indexOf("user.dir") >= 0) {
				fileupload_dir_log = fileupload_dir_log.replace("user.dir", System.getProperty("user.dir"));
				File file = new File(fileupload_dir_log);
				if(!file.exists()) {
					file.mkdirs();
				}
			}
    		}
    	
    	DatacloudConfigVo vo = new DatacloudConfigVo(file_data, file_dict, api_publickey, api_upload, group_org_code, group_appSecret, group_inner_version, group_appKey, fileupload_dir_zip, fileupload_dir_data, fileupload_dir_log, fileupload_threads, fileupload_tempdates,pageSize);
  		vo.setMydict(mydict);
  		vo.setCheck(isCheck);
  		vo.setGroup_publicKey(publicKey);
  		vo.setUpload(isUpload);
  		vo.setApi_zkUpload(api_zkUpload);
  		vo.setBasic_Upload(basic_Upload);
  		vo.setMedical_Url(medical_Url);
  		vo.setFile_rule(file_rule);
  		vo.setPrint(isPrint);
  		vo.setStartReport(isStartReport);
  		vo.setStartJob(isStartJob);
  		vo.setZK(isZK);
    	return vo;
  	}
    
    @Bean
    public XMDataCloudConfig getXMDataCloudConfig() {
	    	XMDataCloudConfig vo = new XMDataCloudConfig();
	    	vo.setAppKey(group_appKey);
	    	vo.setAppSecret(group_appSecret);
	    	vo.setInner_version(group_inner_version);
	    	vo.setOrg_code(group_org_code);
	    	return vo;
    }
    public String getExecute() {
		return execute;
	}
    
}

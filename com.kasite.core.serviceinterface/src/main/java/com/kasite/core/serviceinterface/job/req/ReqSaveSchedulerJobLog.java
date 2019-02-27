package com.kasite.core.serviceinterface.job.req;

import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqSaveSchedulerJobLog extends AbsReq{
	
	private Long jobId;
	private String beanName;
	private String methodName;
	private String params;
	private Integer status;
	private String error;
	private Integer times;
	
	
	
	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public ReqSaveSchedulerJobLog(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.jobId =  getDataJs().getLong("JobId");
			this.beanName = getDataJs().getString("BeanName");
			this.methodName = getDataJs().getString("MethodName");
			this.params = getDataJs().getString("Params");
			this.status = getDataJs().getInteger("Status");
			this.error = getDataJs().getString("Error");
			this.times =  getDataJs().getInteger("Times");
		}else {
			this.jobId =  XMLUtil.getLong(getData(), "JobId", false);
			this.beanName = XMLUtil.getString(getData(), "BeanName", false);
			this.methodName = XMLUtil.getString(getData(), "MethodName", false);
			this.params = XMLUtil.getString(getData(), "Params", false);
			this.status = XMLUtil.getInt(getData(), "Status", false,null);
			this.error = XMLUtil.getString(getData(), "Error",false);
			this.times =  XMLUtil.getInt(getData(),"Times",false,null);
		}
	}

	public ReqSaveSchedulerJobLog(InterfaceMessage msg, Long jobId, String beanName, String methodName,
			String params, Integer status, String error, Integer times) throws AbsHosException {
		super(msg);
		this.jobId = jobId;
		this.beanName = beanName;
		this.methodName = methodName;
		this.params = params;
		this.status = status;
		this.error = error;
		this.times = times;
	}
	
	
}

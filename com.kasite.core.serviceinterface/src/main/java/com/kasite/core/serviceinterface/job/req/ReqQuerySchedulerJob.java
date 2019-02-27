package com.kasite.core.serviceinterface.job.req;

import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqQuerySchedulerJob extends AbsReq{

	private Long jobId;
	private String beanName;
	private String beanNameLike;
	private String methodName;
	private String methodNameLike;
	private Integer status;
	private String cronExpression;
	private String params;
	private String remark;
	
	
	public String getBeanNameLike() {
		return beanNameLike;
	}
	public void setBeanNameLike(String beanNameLike) {
		this.beanNameLike = beanNameLike;
	}
	public String getMethodNameLike() {
		return methodNameLike;
	}
	public void setMethodNameLike(String methodNameLike) {
		this.methodNameLike = methodNameLike;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
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




	public ReqQuerySchedulerJob(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.beanName = getDataJs().getString("BeanName");
			this.beanNameLike = getDataJs().getString("BeanNameLike");
			this.methodName = getDataJs().getString("MethodName");
			this.methodNameLike = getDataJs().getString("MethodNameLike");
			this.params = getDataJs().getString("Params");
			this.cronExpression = getDataJs().getString("CronExpression");
			this.remark = getDataJs().getString("Remark");
			this.status = getDataJs().getInteger("Status");
		}else {
			this.beanName = XMLUtil.getString(getData(), "BeanName", false);
			this.beanNameLike = XMLUtil.getString(getData(), "BeanNameLike", false);
			this.methodName = XMLUtil.getString(getData(), "MethodName", false);
			this.methodNameLike = XMLUtil.getString(getData(), "MethodNameLike", false);
			this.params = XMLUtil.getString(getData(), "Params", false);
			this.cronExpression = XMLUtil.getString(getData(), "CronExpression", false);
			this.remark = XMLUtil.getString(getData(), "Remark", false);
			this.status = XMLUtil.getInt(getData(), "Status", false,null);
		}
	}
	public ReqQuerySchedulerJob(InterfaceMessage msg, Long jobId, String beanName, String methodName, Integer status,
			String cronExpression, String params, String remark) throws AbsHosException {
		super(msg);
		this.jobId = jobId;
		this.beanName = beanName;
		this.methodName = methodName;
		this.status = status;
		this.cronExpression = cronExpression;
		this.params = params;
		this.remark = remark;
	}
	
	
	
}

package com.kasite.core.serviceinterface.job.req;

import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqUpdateSchedulerJob extends AbsReq{

	private Long jobId;
	private String beanName;
	private String methodName;
	private String params;
	private String cronExpression;
	private Integer status;
	private String remark;
	private String operatorId;
	private String operatorName;
	
	
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


	public String getCronExpression() {
		return cronExpression;
	}


	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getOperatorId() {
		return operatorId;
	}


	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}


	public String getOperatorName() {
		return operatorName;
	}


	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}


	public ReqUpdateSchedulerJob(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.jobId = getDataJs().getLong("JobId");
			this.beanName = getDataJs().getString("BeanName");
			this.methodName = getDataJs().getString("MethodName");
			this.params = getDataJs().getString("Params");
			this.cronExpression = getDataJs().getString("CronExpression");
			this.remark = getDataJs().getString("Remark");
			this.status = getDataJs().getInteger("Status");
		}else {
			this.jobId =  XMLUtil.getLong(getData(), "JobId", false);
			this.beanName = XMLUtil.getString(getData(), "BeanName", false);
			this.methodName = XMLUtil.getString(getData(), "MethodName", false);
			this.params = XMLUtil.getString(getData(), "Params", false);
			this.cronExpression = XMLUtil.getString(getData(), "CronExpression", false);
			this.remark = XMLUtil.getString(getData(), "Remark", false);
			this.status = XMLUtil.getInt(getData(), "Status", false, null);
		}
		if(this.jobId==null || this.jobId<=0) {
			throw new RRException(RetCode.Common.ERROR_PARAM,"参数JobId不能为空");
		}
	}


	public ReqUpdateSchedulerJob(InterfaceMessage msg, Long jobId, String beanName, String methodName, String params,
			String cronExpression, Integer status, String remark, String operatorId, String operatorName)
			throws AbsHosException {
		super(msg);
		this.jobId = jobId;
		this.beanName = beanName;
		this.methodName = methodName;
		this.params = params;
		this.cronExpression = cronExpression;
		this.status = status;
		this.remark = remark;
		this.operatorId = operatorId;
		this.operatorName = operatorName;
		if(this.jobId==null || this.jobId<=0) {
			throw new RRException(RetCode.Common.ERROR_PARAM,"参数JobId不能为空");
		}
	}
	
	
}

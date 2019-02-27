package com.kasite.core.serviceinterface.job.req;

import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqQuerySchedulerJobLog extends AbsReq{
	
	private Long jobId;
	private String beanName;
	private String beanNameLike;
	private String methodName;
	private String methodNameLike;
	private Integer status;
	private String params;
	private String startTime;
	private String endTime;
	private Integer gteTimes;
	private Integer lteTimes;
	
	
	public Integer getGteTimes() {
		return gteTimes;
	}
	public void setGteTimes(Integer gteTimes) {
		this.gteTimes = gteTimes;
	}
	public Integer getLteTimes() {
		return lteTimes;
	}
	public void setLteTimes(Integer lteTimes) {
		this.lteTimes = lteTimes;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
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
	public String getBeanNameLike() {
		return beanNameLike;
	}
	public void setBeanNameLike(String beanNameLike) {
		this.beanNameLike = beanNameLike;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getMethodNameLike() {
		return methodNameLike;
	}
	public void setMethodNameLike(String methodNameLike) {
		this.methodNameLike = methodNameLike;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}







	public ReqQuerySchedulerJobLog(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.beanName = getDataJs().getString("BeanName");
			this.beanNameLike = getDataJs().getString("BeanNameLike");
			this.methodName = getDataJs().getString("MethodName");
			this.methodNameLike = getDataJs().getString("MethodNameLike");
			this.params = getDataJs().getString("Params");
			this.status = getDataJs().getInteger("Status");
			this.startTime = getDataJs().getString("StartTime");
			this.endTime = getDataJs().getString("EndTime");
			this.gteTimes = getDataJs().getInteger("GteTimes");
			this.lteTimes = getDataJs().getInteger("LteTimes");
		}else {
			this.beanName = XMLUtil.getString(getData(), "BeanName", false);
			this.beanNameLike = XMLUtil.getString(getData(), "BeanNameLike", false);
			this.methodName = XMLUtil.getString(getData(), "MethodName", false);
			this.methodNameLike = XMLUtil.getString(getData(), "MethodNameLike", false);
			this.params = XMLUtil.getString(getData(), "Params", false);
			this.status = XMLUtil.getInt(getData(), "Status", false,null);
			this.startTime = XMLUtil.getString(getData(), "StartTime", false);
			this.endTime = XMLUtil.getString(getData(), "EndTime", false);
			this.gteTimes = XMLUtil.getInt(getData(), "GteTimes", false,null);
			this.lteTimes = XMLUtil.getInt(getData(), "LteTimes", false,null);
		}
	}
	public ReqQuerySchedulerJobLog(InterfaceMessage msg, Long jobId, String beanName, String beanNameLike,
			String methodName, String methodNameLike, Integer status, String params, String startTime, String endTime,
			Integer gteTimes, Integer lteTimes) throws AbsHosException {
		super(msg);
		this.jobId = jobId;
		this.beanName = beanName;
		this.beanNameLike = beanNameLike;
		this.methodName = methodName;
		this.methodNameLike = methodNameLike;
		this.status = status;
		this.params = params;
		this.startTime = startTime;
		this.endTime = endTime;
		this.gteTimes = gteTimes;
		this.lteTimes = lteTimes;
	}

	
	
}

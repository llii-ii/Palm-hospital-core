package com.kasite.core.serviceinterface.job.req;

import java.util.List;

import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqResumeSchedulerJob extends AbsReq{

	private Long jobId;
	private List<Long> jobIds;


	public Long getJobId() {
		return jobId;
	}


	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}


	public List<Long> getJobIds() {
		return jobIds;
	}


	public void setJobIds(List<Long> jobIds) {
		this.jobIds = jobIds;
	}


	public ReqResumeSchedulerJob(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.jobId = getDataJs().getLong("JobId");
			String jIds = getDataJs().getString("JobIds");
			if(StringUtil.isNotBlank(jIds)) {
				this.jobIds = StringUtil.convertToLongList(jIds);
			}
		}else {
			this.jobId =  XMLUtil.getLong(getData(), "JobId", false);
			String jIds = XMLUtil.getString(getData(), "JobIds", false);
			if(StringUtil.isNotBlank(jIds)) {
				this.jobIds = StringUtil.convertToLongList(jIds);
			}
		}
		if((this.jobId==null || this.jobId<=0) && (jobIds==null || jobIds.size()<=0)) {
			throw new RRException(RetCode.Common.ERROR_PARAM,"参数JobId不能为空");
		}
	}


	public ReqResumeSchedulerJob(InterfaceMessage msg, Long jobId, List<Long> jobIds) throws AbsHosException {
		super(msg);
		this.jobId = jobId;
		this.jobIds = jobIds;
	}
	
}

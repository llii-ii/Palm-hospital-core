package com.kasite.core.serviceinterface.module.survey.req;

import java.sql.Timestamp;

import com.coreframework.util.StringUtil;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 更新问题跳转请求实体类(后台管理)
 * @author zhaoy
 * @date 2018-09-03
 */
public class ReqQuestionFlow extends AbsReq{

	public ReqQuestionFlow(InterfaceMessage msg, String type) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.flowIds = getDataJs().getString("FlowIds");
			this.flowId = getDataJs().getInteger("FlowId");
			this.itemIds = getDataJs().getString("ItemIds");
			this.itemId = getDataJs().getInteger("ItemId");
			this.nextQuestId = getDataJs().getInteger("NextQuestId");
			this.nextQuestIds = getDataJs().getString("NextQuestIds");
			this.operatorId = getDataJs().getString("OperatorID");
			this.operatorName = getDataJs().getString("OperatorName");
			this.operTime = getDataJs().getTimestamp("OperTime");
			this.status = getDataJs().getInteger("Status");
			if("add".equals(type)) {
				if(StringUtil.isBlank(this.itemIds) && StringUtil.isBlank(this.itemId)) {
					throw new RRException(RetCode.Common.ERROR_PARAM,"ItemId参数不能为空!");
				}
				if(StringUtil.isBlank(nextQuestIds) && this.nextQuestId == null) {
					throw new RRException(RetCode.Common.ERROR_PARAM,"NextQuestId参数不能为空!");
				}
			}else if("update".equals(type)) {
				
			}else if("delete".equals(type)) {
				if(StringUtil.isBlank(this.itemIds) && StringUtil.isBlank(this.itemId)) {
					throw new RRException(RetCode.Common.ERROR_PARAM,"ItemId参数不能为空!");
				}
			}
		}
	}

	private String flowIds;
	
	private Integer flowId;
	
	private String itemIds;
	
	private Integer itemId;
	
	private String nextQuestIds;
	
	private Integer nextQuestId;
	
	private String operatorId;
	
	private String operatorName;
	
	private Timestamp operTime;
	
	private Integer status;

	public String getFlowIds() {
		return flowIds;
	}

	public void setFlowIds(String flowIds) {
		this.flowIds = flowIds;
	}

	public Integer getFlowId() {
		return flowId;
	}

	public void setFlowId(Integer flowId) {
		this.flowId = flowId;
	}

	public String getItemIds() {
		return itemIds;
	}

	public void setItemIds(String itemIds) {
		this.itemIds = itemIds;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getNextQuestIds() {
		return nextQuestIds;
	}

	public void setNextQuestIds(String nextQuestIds) {
		this.nextQuestIds = nextQuestIds;
	}

	public Integer getNextQuestId() {
		return nextQuestId;
	}

	public void setNextQuestId(Integer nextQuestId) {
		this.nextQuestId = nextQuestId;
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

	public Timestamp getOperTime() {
		return operTime;
	}

	public void setOperTime(Timestamp operTime) {
		this.operTime = operTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}

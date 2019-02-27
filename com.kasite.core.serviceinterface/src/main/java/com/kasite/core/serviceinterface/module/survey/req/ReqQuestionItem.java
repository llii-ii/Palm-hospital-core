package com.kasite.core.serviceinterface.module.survey.req;

import java.sql.Timestamp;

import com.coreframework.util.StringUtil;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 更新问题类型请求实体类(后台管理)
 * @author zhaoy
 * @date 2018-09-03
 */
public class ReqQuestionItem extends AbsReq{

	public ReqQuestionItem(InterfaceMessage msg, String type) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.itemIds = getDataJs().getString("ItemIds");
			this.itemIds1 = getDataJs().getString("ItemIds1");
			this.itemIds2 = getDataJs().getString("ItemIds2");
			this.itemId = getDataJs().getInteger("ItemId");
			this.itemId1 = getDataJs().getInteger("ItemId1");
			this.itemId2 = getDataJs().getInteger("ItemId2");
			this.questId = getDataJs().getInteger("QuestId");
			this.itemCont = getDataJs().getString("ItemCont");
			this.sortNum = getDataJs().getInteger("SortNum");
			this.operatorId = super.checkOperatorId(null);
			this.operatorName = super.checkOperatorName(null);
			this.operTime = getDataJs().getTimestamp("OperTime");
			this.status = getDataJs().getInteger("Status");
			this.originalId = getDataJs().getInteger("OriginalId");
			this.itemOption = getDataJs().getString("ItemOption");
			this.ifAddBlank = getDataJs().getString("IfAddBlank");
			this.ifAllowNull = getDataJs().getString("IfAllowNull");
			if("add".equals(type)) {
				if(this.itemCont == null) {
					throw new RRException(RetCode.Common.ERROR_PARAM,"操作人ID参数不能为空!");
				}
				if(this.questId == null) {
					throw new RRException(RetCode.Common.ERROR_PARAM,"问题ID参数不能为空!");
				}
			}else if("update".equals(type)) {
				if(this.itemId == null && StringUtil.isBlank(this.itemIds)) {
					throw new RRException(RetCode.Common.ERROR_PARAM,"问题选项ID参数不能为空!");
				}
			}else if("exchange".equals(type)) {
				if(this.itemId1 == null && StringUtil.isBlank(this.itemIds1)) {
					throw new RRException(RetCode.Common.ERROR_PARAM,"问题选项ID1参数不能为空!");
				}
				if(this.itemId2 == null && StringUtil.isBlank(this.itemIds2)) {
					throw new RRException(RetCode.Common.ERROR_PARAM,"问题选项ID2参数不能为空!");
				}
			}
		}
	}

	private String itemIds;
	
	private String itemIds1;
	
	private String itemIds2;
	
	private Integer itemId;
	
	private Integer itemId1;
	
	private Integer itemId2;
	
	private Integer questId;
	
	private String itemCont;
	
	private Integer sortNum;
	
	private String operatorId;
	
	private String operatorName;
	
	private Timestamp operTime;
	
	private Integer status;
	
	private Integer originalId;
	
	private String itemOption;
	
	private String ifAddBlank;
	
	private String ifAllowNull;

	public String getItemIds() {
		return itemIds;
	}

	public void setItemIds(String itemIds) {
		this.itemIds = itemIds;
	}

	public String getItemIds1() {
		return itemIds1;
	}

	public void setItemIds1(String itemIds1) {
		this.itemIds1 = itemIds1;
	}

	public String getItemIds2() {
		return itemIds2;
	}

	public void setItemIds2(String itemIds2) {
		this.itemIds2 = itemIds2;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getItemId1() {
		return itemId1;
	}

	public void setItemId1(Integer itemId1) {
		this.itemId1 = itemId1;
	}

	public Integer getItemId2() {
		return itemId2;
	}

	public void setItemId2(Integer itemId2) {
		this.itemId2 = itemId2;
	}

	public Integer getQuestId() {
		return questId;
	}

	public void setQuestId(Integer questId) {
		this.questId = questId;
	}

	public String getItemCont() {
		return itemCont;
	}

	public void setItemCont(String itemCont) {
		this.itemCont = itemCont;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
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

	public Integer getOriginalId() {
		return originalId;
	}

	public void setOriginalId(Integer originalId) {
		this.originalId = originalId;
	}

	public String getItemOption() {
		return itemOption;
	}

	public void setItemOption(String itemOption) {
		this.itemOption = itemOption;
	}

	public String getIfAddBlank() {
		return ifAddBlank;
	}

	public void setIfAddBlank(String ifAddBlank) {
		this.ifAddBlank = ifAddBlank;
	}

	public String getIfAllowNull() {
		return ifAllowNull;
	}

	public void setIfAllowNull(String ifAllowNull) {
		this.ifAllowNull = ifAllowNull;
	}
	
}

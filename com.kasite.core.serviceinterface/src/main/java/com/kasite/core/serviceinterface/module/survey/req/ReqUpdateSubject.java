package com.kasite.core.serviceinterface.module.survey.req;

import java.sql.Timestamp;

import com.coreframework.util.StringUtil;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 调查问卷更新请求实体类(后台管理)
 * @author zhaoy
 * @date 2018-09-03
 *
 */
public class ReqUpdateSubject extends AbsReq {

	public ReqUpdateSubject(InterfaceMessage msg, String type) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.subjectId = getDataJs().getInteger("SubjectId");
			this.subjectTitle = getDataJs().getString("SubjectTitle");
			this.objType = getDataJs().getInteger("ObjType");
			this.periodNum = getDataJs().getInteger("PeriodNum");
			this.periodInterVal = getDataJs().getInteger("PeriodInterVal");
			this.startTime = getDataJs().getTimestamp("StartTime");
			this.endTime = getDataJs().getTimestamp("EndTime");
			this.remark = getDataJs().getString("Remark");
			this.beginIntro = getDataJs().getString("BeginIntro");
			this.endingIntro = getDataJs().getString("EndingIntro");
			this.operatorId = this.getOperatorId();
			this.operatorName = this.getOperatorName();
			this.operTime = getDataJs().getTimestamp("OperTime");
			this.status = getDataJs().getInteger("Status");
			this.subType = getDataJs().getInteger("SubType");
			this.parentSubjectId = getDataJs().getInteger("ParentSubjectId");
			this.recommend = getDataJs().getInteger("Recommend");
			this.groupId = getDataJs().getInteger("GroupId"); 
			this.periodOrder = getDataJs().getInteger("PeriodOrder"); 
			this.orgId = super.getHosId();
			this.orgName = getDataJs().getString("OrgName");
			this.createTime = getDataJs().getTimestamp("CreateTime");
			this.quantity = getDataJs().getInteger("Quantity");  
			this.provinceId = getDataJs().getInteger("ProvinceId");  
			this.cityId = getDataJs().getInteger("CityId");  
			this.surveyType = getDataJs().getInteger("SurveyType");
			this.tempSampleId = getDataJs().getInteger("TempSampleId");
			this.cycleTag = getDataJs().getInteger("CycleTag");
			this.orgType = getDataJs().getInteger("OrgType");
			this.otherOrg = getDataJs().getString("OtherOrg");
			this.collection = getDataJs().getInteger("Collection");
			this.overTime = getDataJs().getTimestamp("OverTime");
			this.overType = getDataJs().getInteger("OverType");
			this.replyType = getDataJs().getInteger("ReplyType");
			this.qRCode = getDataJs().getString("QRCode");
			this.verifyTime = getDataJs().getTimestamp("VerifyTime");
			this.contactPerson = getDataJs().getString("ContactPerson");
			this.contactPhone = getDataJs().getString("ContactPhone");
			this.ditchID = getDataJs().getInteger("DitchID");
			this.applyTime = getDataJs().getTimestamp("ApplyTime");
			this.checkMan = getDataJs().getString("CheckMan");
			this.checkManId = getDataJs().getInteger("CheckManId");
			
			if("updateBegin".equals(type)) {
				if(this.subjectId == null) {
					throw new RRException(RetCode.Common.ERROR_PARAM,"SubjectId参数不能为空!");
				}
			}else if("update".equals(type)) {
				if(this.subjectId == null) {
					throw new RRException(RetCode.Common.ERROR_PARAM,"SubjectId参数不能为空!");
				}
				if(this.overType == null) {
					throw new RRException(RetCode.Common.ERROR_PARAM,"结束类型参数不能为空!");
				}
				if(this.replyType == null) {
					throw new RRException(RetCode.Common.ERROR_PARAM,"回复类型参数不能为空!");
				}
			}else if("add".equals(type)) {
				if(StringUtil.isBlank(this.subjectTitle)) {
					throw new RRException(RetCode.Common.ERROR_PARAM,"问卷标题不能为空!");
				}
				if(this.objType == null) {
					throw new RRException(RetCode.Common.ERROR_PARAM,"ObjType参数不能为空!");
				}
				if(this.provinceId == null) {
					throw new RRException(RetCode.Common.ERROR_PARAM,"省份不能为空!");
				}
				if(this.cityId == null) {
					throw new RRException(RetCode.Common.ERROR_PARAM,"城市不能为空!");
				}
				if(this.orgId == null) {
					throw new RRException(RetCode.Common.ERROR_PARAM,"医院ID不能为空!");
				}
				if(StringUtil.isBlank(this.orgName)) {
					this.orgName = KasiteConfig.getOrgName();  //配置文件的默认医院
				}
				if(this.surveyType == null) {
					throw new RRException(RetCode.Common.ERROR_PARAM,"调研类型不能为空!");
				}
			}
		}
	}
	private Integer subjectId;
	private String subjectTitle;
	/**
	 * 调查对象
	 */
	private Integer objType;
	/**
	 * 总期数
	 */
	private Integer periodNum;
	/**
	 * 每期频率(每X月一期)
	 */
	private Integer periodInterVal;
	/**
	 * 启用时间
	 */
	private Timestamp startTime;
	/**
	 * 结束时间
	 */
	private Timestamp endTime;
	/**
	 * 备注说明
	 */
	private String remark;
	/**
	 * 开场白
	 */
	private String beginIntro;
	/**
	 * 结束语
	 */
	private String endingIntro;
	/**
	 * 操作者ID
	 */
	private String operatorId;
	/**
	 * 操作者姓名
	 */
	private String operatorName;
	/**
	 * 操作时间
	 */
	private Timestamp operTime;
	/**
	 * 状态(0:无效，1:有效，2:调查中，3:已结束 4：待审核 5：审核不通过)
	 */
	private Integer status;
	/**
	 * 题库类型
	 */
	private Integer subType;
	/**
	 * 源主题ID
	 */
	private Integer parentSubjectId;
	/**
	 * 推荐
	 */
	private Integer recommend;
	/**
	 * 主题分组ID
	 */
	private Integer groupId;
	/**
	 * 第X期
	 */
	private Integer periodOrder;
	/**
	 * 所属机构ID
	 */
	private String orgId;
	/**
	 * 所属机构名称
	 */
	private String orgName;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	/**
	 * 需要问卷量
	 */
	private Integer quantity;
	/**
	 * 省份ID
	 */
	private Integer provinceId;
	/**
	 * 城市ID
	 */
	private Integer cityId;
	/**
	 * 调查类型
	 */
	private Integer surveyType;
	/**
	 * 
	 */
	private Integer tempSampleId;
	/**
	 * 
	 */
	private Integer cycleTag;
	/***
	 * 机构类型
	 */
	private Integer orgType;
	/**
	 * 其他机构类型
	 */
	private String otherOrg;
	/**
	 * 收集量
	 */
	private Integer collection;
	/**
	 * 结束时间
	 */
	private Timestamp overTime;
	/**
	 * 结束类型 1 收集多少份结束 2 到什么时间结束 3无限制
	 */
	private Integer overType;
	/**
	 * 回复类型设置 1 一个手机或电脑只能回复一次 2每个IP只能回复一次 3无限制
	 */
	private Integer replyType;
	/**
	 * 二维码编号
	 */
	private String qRCode;
	/**
	 * 审核时间
	 */
	private Timestamp verifyTime;
	/**
	 * 业务联系人
	 */
	private String contactPerson;
	/***
	 * 联系电话
	 */
	private String contactPhone;
	/**
	 * 渠道ID 1boss 2 无边界
	 */
	private Integer ditchID;
	/**
	 * 申请时间
	 */
	private Timestamp applyTime;
	/**
	 * 审核人
	 */
	private String checkMan;
	/**
	 * 审核人ID
	 */
	private Integer checkManId;
	public Integer getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectTitle() {
		return subjectTitle;
	}
	public void setSubjectTitle(String subjectTitle) {
		this.subjectTitle = subjectTitle;
	}
	public Integer getObjType() {
		return objType;
	}
	public void setObjType(Integer objType) {
		this.objType = objType;
	}
	public Integer getPeriodNum() {
		return periodNum;
	}
	public void setPeriodNum(Integer periodNum) {
		this.periodNum = periodNum;
	}
	public Integer getPeriodInterVal() {
		return periodInterVal;
	}
	public void setPeriodInterVal(Integer periodInterVal) {
		this.periodInterVal = periodInterVal;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getBeginIntro() {
		return beginIntro;
	}
	public void setBeginIntro(String beginIntro) {
		this.beginIntro = beginIntro;
	}
	public String getEndingIntro() {
		return endingIntro;
	}
	public void setEndingIntro(String endingIntro) {
		this.endingIntro = endingIntro;
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
	public Integer getSubType() {
		return subType;
	}
	public void setSubType(Integer subType) {
		this.subType = subType;
	}
	public Integer getParentSubjectId() {
		return parentSubjectId;
	}
	public void setParentSubjectId(Integer parentSubjectId) {
		this.parentSubjectId = parentSubjectId;
	}
	public Integer getRecommend() {
		return recommend;
	}
	public void setRecommend(Integer recommend) {
		this.recommend = recommend;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Integer getPeriodOrder() {
		return periodOrder;
	}
	public void setPeriodOrder(Integer periodOrder) {
		this.periodOrder = periodOrder;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public Integer getSurveyType() {
		return surveyType;
	}
	public void setSurveyType(Integer surveyType) {
		this.surveyType = surveyType;
	}
	public Integer getTempSampleId() {
		return tempSampleId;
	}
	public void setTempSampleId(Integer tempSampleId) {
		this.tempSampleId = tempSampleId;
	}
	public Integer getCycleTag() {
		return cycleTag;
	}
	public void setCycleTag(Integer cycleTag) {
		this.cycleTag = cycleTag;
	}
	public Integer getOrgType() {
		return orgType;
	}
	public void setOrgType(Integer orgType) {
		this.orgType = orgType;
	}
	public String getOtherOrg() {
		return otherOrg;
	}
	public void setOtherOrg(String otherOrg) {
		this.otherOrg = otherOrg;
	}
	public Integer getCollection() {
		return collection;
	}
	public void setCollection(Integer collection) {
		this.collection = collection;
	}
	public Timestamp getOverTime() {
		return overTime;
	}
	public void setOverTime(Timestamp overTime) {
		this.overTime = overTime;
	}
	public Integer getOverType() {
		return overType;
	}
	public void setOverType(Integer overType) {
		this.overType = overType;
	}
	public Integer getReplyType() {
		return replyType;
	}
	public void setReplyType(Integer replyType) {
		this.replyType = replyType;
	}
	public String getqRCode() {
		return qRCode;
	}
	public void setqRCode(String qRCode) {
		this.qRCode = qRCode;
	}
	public Timestamp getVerifyTime() {
		return verifyTime;
	}
	public void setVerifyTime(Timestamp verifyTime) {
		this.verifyTime = verifyTime;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public Integer getDitchID() {
		return ditchID;
	}
	public void setDitchID(Integer ditchID) {
		this.ditchID = ditchID;
	}
	public Timestamp getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(Timestamp applyTime) {
		this.applyTime = applyTime;
	}
	public String getCheckMan() {
		return checkMan;
	}
	public void setCheckMan(String checkMan) {
		this.checkMan = checkMan;
	}
	public Integer getCheckManId() {
		return checkManId;
	}
	public void setCheckManId(Integer checkManId) {
		this.checkManId = checkManId;
	}
	
}

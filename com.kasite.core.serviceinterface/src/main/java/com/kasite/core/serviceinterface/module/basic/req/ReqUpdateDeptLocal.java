package com.kasite.core.serviceinterface.module.basic.req;

import com.coreframework.util.StringUtil;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 更新科室信息的请求入参
 * @author zhaoy
 *
 */
public class ReqUpdateDeptLocal extends AbsReq {

	public ReqUpdateDeptLocal(InterfaceMessage msg, String type) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.hosId = super.getHosId();
			this.deptName = getDataJs().getString("DeptName");
			this.deptCode = getDataJs().getString("DeptCode");
			this.parentDeptCode = getDataJs().getString("ParentDeptCode");
			this.deptAddr = getDataJs().getString("DeptAddr");
			this.deptBrief = getDataJs().getString("DeptBrief");
			this.attachBuilding = getDataJs().getString("AttachBuilding");
			this.floorNum = getDataJs().getString("FloorNum");
			this.isShow = getDataJs().getInteger("IsShow");
			this.remark = getDataJs().getString("Remark");
			this.photoUrl = getDataJs().getString("PhotoUrl");
			this.deptId = getDataJs().getString("DeptId");
			this.orderCol = getDataJs().getInteger("OrderCol");
			this.deptType = getDataJs().getInteger("DeptType");
			this.deptTel = getDataJs().getString("DeptTel");
			
			if("update".equals(type)) {
				if(StringUtil.isBlank(this.deptCode)) {
					throw new RRException(RetCode.Common.ERROR_PARAM,"科室CODE参数不能为空!");
				}
			}
		}
	}
	
	/**医院编码*/
	public String hosId;
	
	/**科室名称*/
	public String deptName;	
	
	/**科室编码*/
	public String deptCode;	
	
	/**上级科室编码*/
	public String parentDeptCode;	
	
	/**科室位置*/
	public String deptAddr;	
	
	/**科室简介*/
	public String deptBrief;
	
	/**归属楼*/
	public String attachBuilding;	
	
	/**所在楼层*/
	public String floorNum;
	
	/**是否显示*/
	public Integer isShow;	
	
	/**备注*/
	public String remark;	
	
	/**科室图片地址*/
	public String photoUrl;
	
	/**健康之路*/
	private String deptId;
	
	/**排序字段*/
	public Integer orderCol;
	
	/**科室类型*/
	public Integer deptType;
	
	/**科室电话*/
	public String deptTel;

	public String getHosId() {
		return hosId;
	}

	public void setHosId(String hosId) {
		this.hosId = hosId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getParentDeptCode() {
		return parentDeptCode;
	}

	public void setParentDeptCode(String parentDeptCode) {
		this.parentDeptCode = parentDeptCode;
	}

	public String getDeptAddr() {
		return deptAddr;
	}

	public void setDeptAddr(String deptAddr) {
		this.deptAddr = deptAddr;
	}

	public String getDeptBrief() {
		return deptBrief;
	}

	public void setDeptBrief(String deptBrief) {
		this.deptBrief = deptBrief;
	}

	public String getAttachBuilding() {
		return attachBuilding;
	}

	public void setAttachBuilding(String attachBuilding) {
		this.attachBuilding = attachBuilding;
	}

	public String getFloorNum() {
		return floorNum;
	}

	public void setFloorNum(String floorNum) {
		this.floorNum = floorNum;
	}

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public Integer getOrderCol() {
		return orderCol;
	}

	public void setOrderCol(Integer orderCol) {
		this.orderCol = orderCol;
	}


	public Integer getDeptType() {
		return deptType;
	}

	public void setDeptType(Integer deptType) {
		this.deptType = deptType;
	}

	public String getDeptTel() {
		return deptTel;
	}

	public void setDeptTel(String deptTel) {
		this.deptTel = deptTel;
	}
	
}

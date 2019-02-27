package com.kasite.core.serviceinterface.module.report.resp;

import java.util.List;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespGetReportInfo
 * @author: zwl
 * @date: 2018年7月25日 下午4:59:01
 */
public class RespGetTjReportItemInfo extends AbsResp{

	private String itemTitle;//项目名称
	private String deptName;//科室
	private String docName;//审核医生
	private String deptAdvice;//科室小结
	private String itemSummary;//综述
	private String checkDate;//审核日期
	private String itemType;//类型 2 检查  1检验
	private List<RespGetTjReportItemDetail> data_2;
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getItemTitle() {
		return itemTitle;
	}
	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getDeptAdvice() {
		return deptAdvice;
	}
	public void setDeptAdvice(String deptAdvice) {
		this.deptAdvice = deptAdvice;
	}
	public String getItemSummary() {
		return itemSummary;
	}
	public void setItemSummary(String itemSummary) {
		this.itemSummary = itemSummary;
	}
	public String getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}
	public List<RespGetTjReportItemDetail> getData_2() {
		return data_2;
	}
	public void setData_2(List<RespGetTjReportItemDetail> data_2) {
		this.data_2 = data_2;
	}
	
	
}

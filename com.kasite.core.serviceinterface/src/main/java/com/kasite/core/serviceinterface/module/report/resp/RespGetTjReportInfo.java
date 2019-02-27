package com.kasite.core.serviceinterface.module.report.resp;

import java.util.List;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespGetReportInfo
 * @author: ZWL
 * @date: 2018年7月25日 下午4:59:01
 * 体检项目分三层返回 第一层：包含针对整个体检套餐的综述和建议 第二层：如一般项目  第三层：如一般项目下的身高
 */
public class RespGetTjReportInfo extends AbsResp{

	private String reportTitle;//体检名称（体检类型）
	private String name;//姓名
	private String sex;
	private String age;
	private String tel;
	private String idCardNo;
	private String djDate;//登记日期
	private String tjDate;//体检日期
	private String summary;//综述
	private String advice;//建议
	private List<RespGetTjReportItemInfo> data_1;
	public String getReportTitle() {
		return reportTitle;
	}
	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public String getDjDate() {
		return djDate;
	}
	public void setDjDate(String djDate) {
		this.djDate = djDate;
	}
	public String getTjDate() {
		return tjDate;
	}
	public void setTjDate(String tjDate) {
		this.tjDate = tjDate;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getAdvice() {
		return advice;
	}
	public void setAdvise(String advice) {
		this.advice = advice;
	}
	public List<RespGetTjReportItemInfo> getData_1() {
		return data_1;
	}
	public void setData_1(List<RespGetTjReportItemInfo> data_1) {
		this.data_1 = data_1;
	}
	
}

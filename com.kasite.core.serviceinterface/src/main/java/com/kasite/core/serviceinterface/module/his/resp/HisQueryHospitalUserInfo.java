/**
 * 
 */
package com.kasite.core.serviceinterface.module.his.resp;

import javax.validation.constraints.NotBlank;

import com.kasite.core.common.resp.AbsResp;
import com.kasite.core.common.validator.group.AddGroup;

/**
 * @author mhd
 * @version 1.0
 * 2017-7-12 下午2:54:21
 */
public class HisQueryHospitalUserInfo  extends AbsResp{

	/**
	 * 身份证号
	 */
//	@NotBlank(message="身份证 idCardId 不能为空", groups = {AddGroup.class})
	private String idCardId;
	/**
	 * 联系电话
	 */
//	@NotBlank(message="手机号 mobile 不能为空", groups = {AddGroup.class})
	private String mobile;
	/**
	 * 住院号
	 */
	@NotBlank(message="住院号 hospitalNo 不能为空", groups = {AddGroup.class})
	private String hospitalNo;
	/**
	 * 姓名
	 */
	@NotBlank(message="姓名 name 不能为空", groups = {AddGroup.class})
	private String name;
	/**
	 * 入院科室代码
	 */
	private String deptCode;
	/**
	 * 入院科室名称
	 */
	private String deptName;
	/**
	 * 床号
	 */
	private String bedNo;
	/**
	 * 入院日期
	 */
	private String inHospitalDate;
	/**
	 * 入院天数
	 */
	private Integer inHospitalDays;
	/**
	 * 住院总费用
	 */
	private Integer inHospitalTotalFee;
	/**
	 * 住院余额
	 */
	//@CheckCurrency(message="余额 balance 不能为空", groups = {AddGroup.class})
	private Integer balance;
	/**
	 * 性别 1男 2女 0未知
	 */
	private Integer sex;
	/**
	 * 年龄
	 */
	private Integer age;
	/**
	 * 用户信息返回
	 */
	private String hisMemberId;
	/**
	 * 住院流水号
	 */
	private String hospitalTransNo;
	/**
	 * 住院状态                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
	 */
	private String state;
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getHospitalTransNo() {
		return hospitalTransNo;
	}
	public void setHospitalTransNo(String hospitalTransNo) {
		this.hospitalTransNo = hospitalTransNo;
	}
	public String getHisMemberId() {
		return hisMemberId;
	}
	public void setHisMemberId(String hisMemberId) {
		this.hisMemberId = hisMemberId;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getIdCardId() {
		return idCardId;
	}
	public void setIdCardId(String idCardId) {
		this.idCardId = idCardId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getHospitalNo() {
		return hospitalNo;
	}
	public void setHospitalNo(String hospitalNo) {
		this.hospitalNo = hospitalNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getInHospitalDate() {
		return inHospitalDate;
	}
	public void setInHospitalDate(String inHospitalDate) {
		this.inHospitalDate = inHospitalDate;
	}
	public Integer getInHospitalDays() {
		return inHospitalDays;
	}
	public void setInHospitalDays(Integer inHospitalDays) {
		this.inHospitalDays = inHospitalDays;
	}
	
	public Integer getBalance() {
		return balance;
	}
	public void setBalance(Integer balance) {
		this.balance = balance;
	}
	public Integer getInHospitalTotalFee() {
		return inHospitalTotalFee;
	}
	public void setInHospitalTotalFee(Integer inHospitalTotalFee) {
		this.inHospitalTotalFee = inHospitalTotalFee;
	}
	
	
	
	 

}

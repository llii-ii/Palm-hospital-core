//package com.kasite.core.serviceinterface.module.order.dbo;
//
//import javax.persistence.Id;
//import javax.persistence.Table;
//
///**
// * @author linjianfa
// * @Description:  订单扩展表
// * @version: V1.0  
// * 2017-7-5 下午7:55:03
// */
//@Table(name="O_ORDER_EXTENSION")
//public class OrderExtension {
//	@Id
//	private String orderId;
//	
//	private String doctorCode;
//	
//	private String doctorName;
//	
//	private String deptName;
//
//	private String deptCode;
// 	/**扩展字段1*/
// 	private String extendField1;
// 	/**扩展字段1*/
// 	private String extendField2;
// 	/**扩展字段1*/
// 	private String extendField3;
//
//	public String getOrderId() {
//		return orderId;
//	}
//
//	public void setOrderId(String orderId) {
//		this.orderId = orderId;
//	}
//
//	public String getDeptCode() {
//		return deptCode;
//	}
//
//	public void setDeptCode(String deptCode) {
//		this.deptCode = deptCode;
//	}
//
//	public String getDoctorCode() {
//		return doctorCode;
//	}
//
//	public void setDoctorCode(String doctorCode) {
//		this.doctorCode = doctorCode;
//	}
//
//	public String getDoctorName() {
//		return doctorName;
//	}
//
//	public void setDoctorName(String doctorName) {
//		this.doctorName = doctorName;
//	}
//
//	public String getDeptName() {
//		return deptName;
//	}
//
//	public void setDeptName(String deptName) {
//		this.deptName = deptName;
//	}
//
//	public String getExtendField1() {
//		return extendField1;
//	}
//
//	public void setExtendField1(String extendField1) {
//		this.extendField1 = extendField1;
//	}
//
//	public String getExtendField2() {
//		return extendField2;
//	}
//
//	public void setExtendField2(String extendField2) {
//		this.extendField2 = extendField2;
//	}
//
//	public String getExtendField3() {
//		return extendField3;
//	}
//
//	public void setExtendField3(String extendField3) {
//		this.extendField3 = extendField3;
//	}
//	
//	
//}
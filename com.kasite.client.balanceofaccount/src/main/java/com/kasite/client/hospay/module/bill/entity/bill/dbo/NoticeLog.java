package com.kasite.client.hospay.module.bill.entity.bill.dbo;

import java.sql.Timestamp;

/**
 * @author cc
 * TODO
 */
public class NoticeLog {
	/**主键*/
	private String NOTICE_ID;
	/**商户订单号*/
	private String OUT_TRADE_NO;
	/**支付渠道订单号*/
	private String TRANSACTION_ID;
	/**支付渠道类型*/
	private Integer CHANEL_TYPE;
	/**操作类型*/
	private Integer OP_TYPE;
	/**消息*/
	private String MESSAGE;
	/**操作时间*/
	private Timestamp OP_TIME;
	/**返回状态码*/
	private String RETURN_CODE;
	/**返回状态码*/
	private String RESULT_CODE;
	/**流程最终结果*/
	private String FINAL_RESULT;

	
	public String getRETURN_CODE() {
		return RETURN_CODE;
	}
	public void setRETURN_CODE(String rETURN_CODE) {
		RETURN_CODE = rETURN_CODE;
	}
	public String getRESULT_CODE() {
		return RESULT_CODE;
	}
	public void setRESULT_CODE(String rESULT_CODE) {
		RESULT_CODE = rESULT_CODE;
	}
	public Integer getCHANEL_TYPE() {
		return CHANEL_TYPE;
	}
	public void setCHANEL_TYPE(Integer cHANEL_TYPE) {
		CHANEL_TYPE = cHANEL_TYPE;
	}
	
	public String getNOTICE_ID() {
		return NOTICE_ID;
	}
	public void setNOTICE_ID(String nOTICE_ID) {
		NOTICE_ID = nOTICE_ID;
	}
	public String getOUT_TRADE_NO() {
		return OUT_TRADE_NO;
	}
	public void setOUT_TRADE_NO(String oUT_TRADE_NO) {
		OUT_TRADE_NO = oUT_TRADE_NO;
	}
	public String getTRANSACTION_ID() {
		return TRANSACTION_ID;
	}
	public void setTRANSACTION_ID(String tRANSACTION_ID) {
		TRANSACTION_ID = tRANSACTION_ID;
	}
	public Integer getOP_TYPE() {
		return OP_TYPE;
	}
	public void setOP_TYPE(Integer oP_TYPE) {
		OP_TYPE = oP_TYPE;
	}
	public String getMESSAGE() {
		return MESSAGE;
	}
	public void setMESSAGE(String mESSAGE) {
		MESSAGE = mESSAGE;
	}

	public Timestamp getOP_TIME() {
		return OP_TIME;
	}

	public void setOP_TIME(Timestamp OP_TIME) {
		this.OP_TIME = OP_TIME;
	}

	public String getFINAL_RESULT() {
		return FINAL_RESULT;
	}
	public void setFINAL_RESULT(String fINAL_RESULT) {
		FINAL_RESULT = fINAL_RESULT;
	}

}

package com.kasite.core.common.service;

import com.kasite.core.common.exception.RRException;

/**
 * 业务订单类型
 * 系统支持的业务订单类型（产品编号）
 * @author daiyanshui
 ********************** 订单类型 *******************
	/** 挂号订单 
	public static final String ORDERTYPE_0 = "0";
	/** 西药 
	public static final String ORDERTYPE_001 = "001";
	/** 中成药 
	public static final String ORDERTYPE_002 = "002";
	/** 草药 
	public static final String ORDERTYPE_003 = "003";
	/** 非药品类型列表（检查） 
	public static final String ORDERTYPE_004 = "004";
	/** 就诊卡充值 
	public static final String ORDERTYPE_005 = "005";
	/** 门诊 充值 
	public static final String ORDERTYPE_006 = "006";
	/** 住院 充值 
	public static final String ORDERTYPE_007 = "007";
	/** 诊间数据 
	public static final String ORDERTYPE_008 = "008";
	/** 当日挂号 
	public static final String ORDERTYPE_009 = "009";
	/** 病历复印
	public static final String ORDERTYPE_009 = "010";
	/** 所有订单 
	public static final String ORDERTYPE_999 = "999";
 8*/
public enum BusinessTypeEnum {
	/** 挂号订单 */
	ORDERTYPE_0("0", "挂号订单"),
	/** 西药 */
	ORDERTYPE_001("001", "西药"),
	/** 中成药 */
	ORDERTYPE_002("002", "中成药"),
	/** 草药 */
	ORDERTYPE_003("003", "草药"),
	/** 非药品类型列表（检查） */
	ORDERTYPE_004("004", "非药品类型列表（检查）"),
	/** 就诊卡充值 */
	ORDERTYPE_005("005", "就诊卡充值"),
	/** 门诊 充值 */
	ORDERTYPE_006("006", "门诊充值"),
	/** 住院 充值 */
	ORDERTYPE_007("007", "住院充值"),
	/** 诊间数据 */
	ORDERTYPE_008("008", "诊间数据"),
	/** 当日挂号 */
	ORDERTYPE_009("009", "当日挂号"),
	/** 病历复印 */
	ORDERTYPE_010("010", "病历复印"),
	/** 诊间订单合并支付 */
	ORDERTYPE_011("011", "诊间订单合并"),
	/** 医技预约 */
	ORDERTYPE_012("012", "医技预约"),
	/**
	 * 所有订单（当运维的时候需要将指定渠道的所有线上支付都关闭）
	 */
	ORDERTYPE_999("999", "所有订单"),
	;
	private String code;
	private String value;
	BusinessTypeEnum(String code, String value){
		this.code = code;
		this.value = value;
	}
	public String getCode() {
		return this.code;
	}
	public String getValue() {
		return this.value;
	}
	public static BusinessTypeEnum valuesOfCode(String serviceId) {
		BusinessTypeEnum[] bts = BusinessTypeEnum.values();
		for (BusinessTypeEnum b : bts) {
			if(b.getCode().equals(serviceId)) {
				return b;
			}
		}
		throw new RRException("未找到业务对应的订单类型：serviceId="+serviceId);
	}
	
	
}

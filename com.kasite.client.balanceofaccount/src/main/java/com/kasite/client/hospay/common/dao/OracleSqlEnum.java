package com.kasite.client.hospay.common.dao;

/**
 * @author cc
 */
public enum OracleSqlEnum{
	// *********************************************************
	/**
	 * 删除指定日期内的账单数据（去重）
	 */
	oracleDeleteHisBillByTransDate,
	/**
	 * 汇总全流程每日账单明细
	 */
	oracleSummaryQLCBalance,
	/**
	 * 查询p_Bill中比p_QlcBalance多出的订单数据
	 */
	oracleQueryChannelBillDiffQlcBill,
	/**
	 * 删除指定日期内的全流程账单明细数据（去重）
	 */
	oracleDeleteQLCBalance,
	/**
	 *	查询出来的异常账单由于会缺少一些展示字段,所以这边需要到o_order表中获取那些展示字段
	 */
	oracleQueryOrderData,
	// **********************************************************
	oracleUpdateOrderExeState,
	/**
	 * 查询当前退款订单是否存在p_bill表
	 */
	oracleQueryOrderExistBill,
	/**
	 * 查询当前退款订单是否存在p_His_bill表
	 */
	oracleQueryOrderExistHisBill,
	/**
	 * 生成三方汇总账单明细
	 */
	oracleSummaryThreePartyBalance,
	/**
	 * 查询当前异常订单是否为空
	 */
	oracleQueryExceptionBillWhetherNull,
	/**
	 * 删除指定日期内的三方汇总账单明细数据（去重）
	 */
	oracleDeleteThreePartyBalance,
	/**
	 * 获取三方每日汇总账单明细数据
	 */
	oracleQueryThreePartyDetailBills,
	/**
	 * 给三方汇总账单明细新增业务执行
	 */
	oracleAddPendingOrder,

	// **********************************************************
	/**
	 * 获取三方汇总账单明细数据（用于生成每日汇总账单数据）
	 */
	oracleQueryThreePartyBalance,
	/**
	 * 查询每日汇总账单数据总数
	 */
	oracleQueryEveryDayBillCount,
	/**
	 * 生成每日汇总账单数据
	 */
	oracleSummaryEveryDayBalance,
	/**
	 * 生成每日分类汇总账单数据
	 */
	oracleClassifySummaryBill,
	/**
	 * 删除指定日期内的每日账单数据（去重）
	 */
	oracleDeleteEveryDayBalance,
	/**
	 * 删除指定日期内的分类汇总数据（去重）
	 */
	oracleDeleteClassifySummaryBalance,
	/**
	 * 获取每日汇总账单数据
	 */
	oracleQueryEveryDayBills,
	/**
	 * 查询分类汇总账单数据
	 */
	oracleQueryClassifySummaryBills,
	/**
	 * 查看是否存在异常订单
	 */
	oracleQueryErrorNum,
	//*************************************************
	/**
	 * 查询是否存在待执行业务订单
	 */
	oracleQueryExistBizOrder,
	/**
	 * 更新三方汇总账单明细表里某笔订单的执行状态
	 */
	oracleUpdateThreePartyBalanceExeState,
	/**
	 * 查询某笔订单的交易订单的创建时间
	 */
	oracleQueryOrderCreateDate,
	/**
	 * 查询当前日期内是否还存在异常账单总数（用于汇总每日账单）
	 */
	oracleQueryVeryDayExitsErrorBill,
	/**
	 * 查询当前日期内存在的异常订单明细（用于三方汇总订单去重）
	 */
	oracleQueryExitsErrorBills,
	/**
	 * 查询当前存在已执行业务的异常订单是否在表中
	 */
	oracleQueryOrderExist,
	;
}

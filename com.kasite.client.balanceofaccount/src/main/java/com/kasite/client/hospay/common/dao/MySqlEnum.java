package com.kasite.client.hospay.common.dao;


/**
 * @author cc
 */
public enum MySqlEnum{
	// *********************************************************
	/**
	 * 删除指定日期内的账单数据（去重）
	 */
	mysqlDeleteHisBillByTransDate,
	/**
	 * 汇总全流程每日账单明细
	 */
	mysqlSummaryQLCBalance,
	/**
	 * 查询p_Bill中比p_QlcBalance多出的订单数据
	 */
	mysqlQueryChannelBillDiffQlcBill,
	/**
	 * 删除指定日期内的全流程账单明细数据（去重）
	 */
	mysqlDeleteQLCBalance,
	/**
	 *	查询出来的异常账单由于会缺少一些展示字段,所以这边需要到o_order表中获取那些展示字段
	 */
	mysqlQueryOrderData,
	// **********************************************************
	mysqlUpdateOrderExeState,
	/**
	 * 查询当前退款订单是否存在p_bill表
	 */
	mysqlQueryOrderExistBill,
	/**
	 * 查询当前退款订单是否存在p_His_bill表
	 */
	mysqlQueryOrderExistHisBill,
	/**
	 * 生成三方汇总账单明细
	 */
	mysqlSummaryThreePartyBalance,
	/**
	 * 查询当前异常订单是否为空
	 */
	mysqlQueryExceptionBillWhetherNull,
	/**
	 * 删除指定日期内的三方汇总账单明细数据（去重）
	 */
	mysqlDeleteThreePartyBalance,
	/**
	 * 获取三方每日汇总账单明细数据
	 */
	mysqlQueryThreePartyDetailBills,
	/**
	 * 给三方汇总账单明细增加业务执行
	 */
	mysqlAddPendingOrder,

	// **********************************************************
	/**
	 * 获取三方汇总账单明细数据（用于生成每日汇总账单数据）
	 */
	mysqlQueryThreePartyBalance,
	/**
	 * 查询每日汇总账单数据总数
	 */
	mysqlQueryEveryDayBillCount,

	/**
	 * 生成每日汇总账单数据
	 */
	mysqlSummaryEveryDayBalance,
	/**
	 * 生成每日分类汇总账单数据
	 */
	mysqlClassifySummaryBill,
	/**
	 * 删除指定日期内的每日账单数据（去重）
	 */
	mysqlDeleteEveryDayBalance,
	/**
	 * 删除指定日期内的分类汇总数据（去重）
	 */
	mysqlDeleteClassifySummaryBalance,
	/**
	 * 获取每日汇总账单数据
	 */
	mysqlQueryEveryDayBills,
	/**
	 * 查询分类汇总账单数据
	 */
	mysqlQueryClassifySummaryBills,
	/**
	 * 查看是否存在异常订单
	 */
	mysqlQueryErrorNum,

	//*******************************************************
	/**
	 * 查询是否存在待执行业务订单
	 */
	mysqlQueryExistBizOrder,
	/**
	 * 更新三方汇总账单明细表里某笔订单的执行状态
	 */
	mysqlUpdateThreePartyBalanceExeState,
	/**
	 * 查询某笔订单的交易订单的创建时间
	 */
	mysqlQueryOrderCreateDate,
	/**
	 * 查询当前日期内是否还存在异常账单总数（用于汇总每日账单）
	 */
	mysqlQueryVeryDayExitsErrorBill,
	/**
	 * 查询当前日期内存在的异常订单明细（用于三方汇总订单去重）
	 */
	mysqlQueryExitsErrorBills,
	/**
	 * 查询当前存在已执行业务的异常订单是否在表中
	 */
	mysqlQueryOrderExist,
	;
}

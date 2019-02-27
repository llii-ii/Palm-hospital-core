package com.kasite.core.elastic.log.enums;

/**
 * 
 * @className: MethodEnums
 * @author: lcz
 * @date: 2018年5月28日 下午8:32:40
 */
public enum MethodEnums {
	/**查询医院**/
	queryHospital,
	/**查询科室**/
	queryBaseDept,
	/**查询医生**/
	queryBaseDoctor,
	/**查询门诊科室**/
	queryClinicDept,
	/**查询门诊医生**/
	queryClinicDoctor,
	/**查询门诊排班**/
	queryClinicSchedule,
	/**查询号源**/
	queryNumbers,
	/**锁号**/
	lockOrder,
	/**解锁**/
	unLock,
	/**挂号**/
	bookService,
	/**退号**/
	yyCancel,
	/**查询预约记录**/
	queryRegInfo,
	/**查询排队信息**/
	queryQueueInfo,
	/**排队签到**/
	signQueue,
	/**查询报告单**/
	getReportList,
	/**查询报告单详情**/
	getReportInfo,
	/**更新订单**/
	updateOrder,
	/**查询订单**/
	orderList,
	/**查询订单详情**/
	orderDetail,
	/**查询就诊卡信息**/
	queryClinicCard,
	/**新增虚拟卡**/
	addClinicCard,
	/**查询住院号**/
	queryHospitalNo,
	/**查询余额**/
	queryCardBalance,
	/**查询用户信息**/
	queryUserInfo,
	/**住院用户信息查询**/
	queryHospitalUserInfo,
	/**住院费用日清单查询**/
	queryInHospitalCostListByDate,
	
}

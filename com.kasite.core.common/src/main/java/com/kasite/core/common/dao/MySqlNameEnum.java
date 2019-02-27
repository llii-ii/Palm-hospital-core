///**
// * 
// */
//package com.kasite.core.common.dao;
//
//import com.coreframework.db.SqlNameEnum;
//
///**
// * SQL语句ID
// * 
// * @author linjf
// */
//public enum MySqlNameEnum implements SqlNameEnum{
//	/**
//	 * 更新患者状态 1为正常 -1为无效
//	 */
//	update_MemberState,
//	/**
//	 * 查询患者信息
//	 */
//	query_MemberInfo,
//	/**
//	 * 查询用户列表
//	 */
//	query_MemberList,
//	/**
//	 * 查询科室
//	 */
//	query_Dept,
//	/**
//	 * 查询用户或者卡信息
//	 */
//	query_MemOrCard,
//	/**
//	 * 添加用户
//	 */
//	add_Member,
//	/**
//	 * 删除用户
//	 */
//	del_Member,
//	/**
//	 * 加卡
//	 */
//	bind_Card,
//	/**
//	 * 更新状态
//	 */
//	update_Member,
//	/**
//	 * 查询用户
//	 */
//	query_Member,
//	/**
//	 * 查询验证码
//	 */
//	query_ProvingCode,
//	
//	/**
//	 * 新增文章
//	 */
//	addArticle,
//	/**
//	 * 修改文章
//	 */
//	editArticle,
//	/**
//	 * 查询文章总数
//	 */
//	queryArticleTotal,
//	/**
//	 * 分类 查询文章
//	 */
//	queryArticleByClass,
//	/**
//	 * 查询文章列表
//	 */
//	queryArticleInfoForList,
//	/**
//	 * 查询文章列表按照置顶排序
//	 */
//	queryArticle,
//	/**
//	 * 查询文章详细信息
//	 */
//	queryNewDetail,
//	/**
//	 * 删除文章
//	 */
//	delArticleInfo,
//	/**
//	 * 查询医生职称
//	 */
//	queryDoctorTitle,
//	
//	/**
//	 * 查询数据字典
//	 */
//	queryDictionary,
//	/**
//	 * 删除字典数据
//	 */
//	delDictionary,
//
//	/**
//	 * 查询就诊人数
//	 */
//	getDataCollection4,
//	
//	/**
//	 * 查询消息模版
//	 */
//	M_QueryMsgTemp,
//	
//	/**
//	 * 查询消息推送记录
//	 */
//	M_QueryMsgPush,
//	
//	/**
//	 * 查询关注用户数量
//	 */
//	getBatUserCount,
//	/**
//	 * 更新用户
//	 */
//	updateBatUserAll,
//	/**
//	 * 更新用户简单
//	 */
//	updateBatUserSingle,
//	deleteBatUserGroup,
//	/**
//	 * 查询公众号回复设置
//	 */
//	queryReplayList,
//	
//	/**
//	 * 查询科室是否存在
//	 */
//	checkDeptExsit,
//	/**
//	 * 查询医生是否存在
//	 */
//	checkDoctorExsit,
//	/**
//	 * 更新科室
//	 */
//	updateDept,
//	/**
//	 * 更新医生
//	 */
//	updateDoctor, 
//	/**
//	 * 查询报表数据汇总
//	 */
//	QUERY_RFREPORTDATA,
//	/**
//	 * 更新报表数据
//	 */
//	UPDATE_RFREPORTDATA,
//	/**
//	 * 查询云报表数据汇总
//	 */
//	QUERY_CLOUDRFREPORTDATA,
//	/**
//	 * 修改云报表数据汇总
//	 */
//	UPDATE_CLOUDRFREPORTDATA,
//	/**
//	 * 查询本地医生
//	 */
//	queryDoctor,
//	/**
//	 * 查询渠道
//	 */
//	querySysChannel,
//	
//	queryMemberByCardNoTypeAndChannel,
//	
//	
//	
//	/***************** 候诊队列Queue 相关sql *********************/
//	queryQueueInfo,
//	updateQueueInfo,
//	queryQueueInfoListByToday,
//	updateQueueSendStatus,
//	
//	/***************** 预约挂号 相关sql *********************/
//	YY_QueryHistoryDoctor,
//	YY_GetLockOrder,
//	YY_QueryClinicDoctor,
//	queryWaterForQueue,
//	
//	/***************** Job 相关sql *********************/
//	queryTable,
//	queryAndSaveTable1,
//	queryAndSaveTable2,
//	delTable,
//	
//	/******************支付**************************/
//	/**
//	 * 删除账单
//	 */
//	del_BillByOrderNo,
//	/**
//	 * 查询退费订单
//	 */
//	query_RetryRefundOrder,
//	/**
//	 * 根据日期查询账单
//	 */
//	query_BillByTransDate,
//	/**
//	 * 查询支付宝当面付没有回调的订单
//	 */
//	query_UnknownOrder,
//	/**
//	 * 根据渠道，日期删除账单
//	 */
//	delete_billByChannelIdAndDate,
//	/**
//	 * 根据渠道，交易类型，日期删除账单
//	 */
//	delete_billByChannelIdAndDateAndTradetype,
//	/**
//	 * 新增支付通知日志
//	 */
//	insert_P_NOTICE_LOG,
//}

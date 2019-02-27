package com.kasite.client.business.module.backstage.bill.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.core.common.mappers.CommonMapper;
import com.kasite.core.serviceinterface.module.channel.dto.ChannelVo;
import com.kasite.core.serviceinterface.module.order.dto.OrderReportVo;
import com.kasite.core.serviceinterface.module.order.dto.PayOrderDetailVo;
import com.kasite.core.serviceinterface.module.pay.dbo.BillChannelRF;
import com.kasite.core.serviceinterface.module.pay.dbo.BillCheck;
import com.kasite.core.serviceinterface.module.pay.dbo.BillMerchRF;
import com.kasite.core.serviceinterface.module.pay.dto.BillByMonthVo;
import com.kasite.core.serviceinterface.module.pay.dto.BillCheckCountVo;
import com.kasite.core.serviceinterface.module.pay.dto.BillCheckVo;
import com.kasite.core.serviceinterface.module.pay.dto.BillDetailVo;
import com.kasite.core.serviceinterface.module.pay.dto.ExceptionBillCountVo;
import com.kasite.core.serviceinterface.module.pay.dto.QueryBillCheck;

public interface BillCheckDao extends CommonMapper<BillCheck> {
	
	/**
	 * 查询对账统计列表数据
	 * 
	 * @param startDate
	 * @param endDate
	 * @param channelId
	 * @param payType
	 */
	@Select({"<script>",
		"SELECT BC.CHECKSTATE AS checkState, COUNT(1) AS count FROM P_BILL_CHECK BC ",
		"<where>",
			"<if test=\"startDate!=null and startDate!='' and endDate!=null and endDate!=''\">",
				" AND BC.TRANSDATE BETWEEN str_to_date(DATE_FORMAT(#{startDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s') ",
				" AND DATE_ADD(str_to_date(DATE_FORMAT(#{endDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s'),INTERVAL 1 DAY) " ,
			"</if>" ,
			"<if test=\"channelId!=null and channelId!=''\">",
				"AND BC.CHANNELID = #{channelId}" ,
			"</if>" ,
			"<if test=\"payMethod!=null and payMethod!=''\">",
				"AND BC.PAYMETHOD = #{payMethod}" ,
			"</if>" ,
			"<if test=\"configKey!=null and configKey!=''\">",
				"AND BC.CONFIGKEY = #{configKey}" ,
			"</if>" ,
			"<if test=\"serviceIdList != null and serviceIdList.size>0\">",
				" AND BC.SERVICEID IN ",
				"<foreach collection=\"serviceIdList\" open=\"(\" separator=\",\" close=\")\" item=\"serviceId\">",
					"<![CDATA[(#{serviceId}) ]]>",
				"</foreach>",	
			"</if>",
		"</where>",
		" GROUP BY BC.CHECKSTATE",
		"</script>"})
	List<BillCheckCountVo> findBillCheckCount(@Param("startDate")String startDate,@Param("endDate")String endDate, @Param("channelId")String channelId,  
			@Param("payMethod")String payMethod, @Param("serviceIdList")List<String> serviceIdList, @Param("configKey")String configKey);

	/**
	 * 查询对账明细列表数据(日对账)
	 * 
	 * @param queryBean 查询实体类
	 * @param initCheckState 初始化的对账结果
	 */
	@Select({"<script>",
		"SELECT BC.id,BC.ORDERID,BC.orderNo,BC.createDate,BC.hisOrderNo,BC.merchNo,BC.billType,BC.hisPrice,BC.payMethod,BC.payMethodName,",
			"BC.transDate,BC.merchPrice,BC.checkState,BC.dealState,BC.dealDate,OM.MEMBERNAME AS nickName,IFNULL(HB.CASEHISTORY, O.CARDNO) AS caseHistory ,BC.serviceId ",
		"FROM P_BILL_CHECK BC ",
		"LEFT JOIN O_ORDER O ON O.ORDERID = BC.ORDERID ",
		"LEFT JOIN O_ORDER_MEMBER OM ON OM.ORDERID = BC.ORDERID ",
		"LEFT JOIN P_HIS_BILL HB ON HB.ORDERID = BC.ORDERID AND HB.HISORDERTYPE = BC.BILLTYPE ",
		"<where>",
			"<if test=\"queryBean.startDate!=null and queryBean.startDate!='' and queryBean.endDate!=null and queryBean.endDate!=''\">",
				" AND BC.TRANSDATE BETWEEN str_to_date(DATE_FORMAT(#{queryBean.startDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s') ",
				" AND DATE_ADD(str_to_date(DATE_FORMAT(#{queryBean.endDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s'),INTERVAL 1 DAY) " ,
			"</if>" ,
			"<if test=\"queryBean.checkState != 999\">",
				" AND BC.CHECKSTATE = ${queryBean.checkState}" ,
			"</if>" ,
			"<if test=\"queryBean.checkState==999 and initCheckState!=null\">",
				"<if test=\"initCheckState==0\">",
					" AND BC.CHECKSTATE IN(1,-1)" ,
				"</if>" ,
				"<if test=\"initCheckState==1\">",
					" AND BC.CHECKSTATE = 0" ,
				"</if>" ,
			"</if>" ,
			"<if test=\"queryBean.orderId!=null and queryBean.orderId!=''\">",
				" AND BC.ORDERID = #{queryBean.orderId}" ,
			"</if>" ,
			"<if test=\"queryBean.hisOrderNo!=null and queryBean.hisOrderNo!=''\">",
				" AND BC.HISORDERNO = #{queryBean.hisOrderNo}" ,
			"</if>" ,
			"<if test=\"queryBean.channelId!=null and queryBean.channelId!=''\">",
				" AND BC.CHANNELID = #{queryBean.channelId}" ,
			"</if>" ,
			"<if test=\"queryBean.payMethod!=null and queryBean.payMethod!=''\">",
				" AND BC.PAYMETHOD = #{queryBean.payMethod}" ,
			"</if>" ,
			"<if test=\"queryBean.merchNo!=null and queryBean.merchNo!=''\">",
				" AND BC.MERCHNO = #{queryBean.merchNo}" ,
			"</if>" ,
			"<if test=\"queryBean.configKey!=null and queryBean.configKey!=''\">",
				"AND BC.CONFIGKEY = #{queryBean.configKey}" ,
			"</if>" ,
			"<if test=\"queryBean.serviceIdList != null and queryBean.serviceIdList.size>0\">",
				" AND BC.SERVICEID IN ",
				"<foreach collection=\"queryBean.serviceIdList\" open=\"(\" separator=\",\" close=\")\" item=\"serviceId\">",
					"<![CDATA[(#{serviceId}) ]]>",
				"</foreach>",	
			"</if>",
		"</where>",
		"<if test=\"queryBean.orderRule==0\">",
			" ORDER BY BC.TRANSDATE ASC",
		"</if>" ,
		"<if test=\"queryBean.orderRule==1\">",
			" ORDER BY BC.TRANSDATE DESC",
		"</if>" ,
		"<if test=\"queryBean.orderRule==2\">",
			" ORDER BY BC.CREATEDATE ASC",
		"</if>" ,
		"<if test=\"queryBean.orderRule==3\">",
			" ORDER BY BC.CREATEDATE DESC",
		"</if>" ,
		"<if test=\"queryBean.orderRule==4\">",
			" ORDER BY BC.DEALDATE ASC",
		"</if>" ,
		"<if test=\"queryBean.orderRule==5\">",
			" ORDER BY BC.DEALDATE DESC",
		"</if>" ,
		"</script>"})
	List<BillCheckVo> findBillList(@Param("queryBean")QueryBillCheck queryBean, @Param("initCheckState")Integer initCheckState);
	
	/**
	 * 异常账单统计
	 * 
	 * @param startDate
	 * @param endDate
	 * @param payMethod
	 * @return
	 */
	@Select({"<script>",
		"SELECT BC.dealState, COUNT(BC.ID) count FROM P_BILL_CHECK BC ",
		"<where>",
			"<if test=\"startDate!=null and startDate!='' and endDate!=null and endDate!=''\">",
				" AND BC.TRANSDATE BETWEEN str_to_date(DATE_FORMAT(#{startDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s') ",
				" AND DATE_ADD(str_to_date(DATE_FORMAT(#{endDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s'),INTERVAL 1 DAY) " ,
			"</if>" ,
			"<if test=\"payMethod!=null and payMethod!=''\">",
				"AND BC.PAYMETHOD = #{payMethod}" ,
			"</if>" ,
			"<if test=\"configKey!=null and configKey!=''\">",
				"AND BC.CONFIGKEY = #{configKey}" ,
			"</if>" ,
			"<if test=\"channelId!=null and channelId!=''\">",
				" AND BC.CHANNELID = #{channelId}" ,
			"</if>" ,
			"<if test=\"serviceIdList != null and serviceIdList.size>0\">",
				" AND BC.SERVICEID IN ",
				"<foreach collection=\"serviceIdList\" open=\"(\" separator=\",\" close=\")\" item=\"serviceId\">",
					"<![CDATA[(#{serviceId}) ]]>",
				"</foreach>",	
			"</if>",
			"AND (BC.DEALSTATE = 1 OR BC.CHECKSTATE IN(1,-1))" ,
		"</where>",
		" GROUP BY BC.DEALSTATE",
		"</script>"})
	List<ExceptionBillCountVo> findExceptionBillCount(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("payMethod")String payMethod,
			 @Param("channelId")String channelId, @Param("serviceIdList")List<String> serviceIdList, @Param("configKey")String configKey);
	
	/**
	 * 查询异常账单明细列表
	 * 
	 * @param queryBean
	 * @return
	 */
	@Select({"<script>",
		"SELECT BC.id,BC.orderNo,BC.orderId,BC.createDate,BC.updateDate,BC.hisOrderNo,BC.merchNo,BC.billType,BC.hisPrice,BC.payMethod,BC.payMethodName,",
			"BC.transDate,BC.merchPrice,BC.checkState,BC.dealState,BC.dealDate,OM.MEMBERNAME AS nickName,IFNULL(HB.CASEHISTORY, O.CARDNO) AS caseHistory,BC.serviceId ",
		"FROM P_BILL_CHECK BC ",
		"LEFT JOIN O_ORDER O ON O.ORDERID = BC.ORDERID ",
		"LEFT JOIN O_ORDER_MEMBER OM ON OM.ORDERID = BC.ORDERID ",
		"LEFT JOIN P_HIS_BILL HB ON HB.ORDERID = BC.ORDERID AND HB.HISORDERTYPE = BC.BILLTYPE",
		"<where>",
			"<if test=\"queryBean.startDate!=null and queryBean.startDate!='' and queryBean.endDate!=null and queryBean.endDate!=''\">",
				" AND BC.TRANSDATE BETWEEN str_to_date(DATE_FORMAT(#{queryBean.startDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s') ",
				" AND DATE_ADD(str_to_date(DATE_FORMAT(#{queryBean.endDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s'),INTERVAL 1 DAY) " ,
			"</if>" ,
			"<if test=\"queryBean.orderId!=null and queryBean.orderId!=''\">",
				" AND BC.ORDERID = #{queryBean.orderId}" ,
			"</if>" ,
			"<if test=\"queryBean.configKey!=null and queryBean.configKey!=''\">",
				"AND BC.CONFIGKEY = #{queryBean.configKey}" ,
			"</if>" ,
			"<if test=\"queryBean.hisOrderNo!=null and queryBean.hisOrderNo!=''\">",
				" AND BC.HISORDERNO = #{queryBean.hisOrderNo}" ,
			"</if>" ,
			"<if test=\"queryBean.merchNo!=null and queryBean.merchNo!=''\">",
				" AND BC.MERCHNO = #{queryBean.merchNo}" ,
			"</if>" ,
			"<if test=\"queryBean.channelId!=null and queryBean.channelId!=''\">",
				" AND BC.CHANNELID = #{queryBean.channelId}" ,
			"</if>" ,
			"<if test=\"queryBean.payMethod!=null and queryBean.payMethod!=''\">",
				" AND BC.PAYMETHOD = #{queryBean.payMethod}" ,
			"</if>" ,
			"<if test=\"queryBean.dealWay != 0\">",
				" AND BC.DEALWAY = #{queryBean.dealWay}" ,
			"</if>" ,
			"<if test=\"queryBean.dealState != null and queryBean.dealState == 0\">",
				" AND BC.DEALSTATE = 0 AND BC.CHECKSTATE IN(1,-1)" ,
			"</if>" ,
			"<if test=\"queryBean.dealState != null and queryBean.dealState == 1\">",
				" AND BC.DEALSTATE = 1" ,
			"</if>" ,
			"<if test=\"queryBean.serviceIdList != null and queryBean.serviceIdList.size>0\">",
				" AND BC.SERVICEID IN ",
				"<foreach collection=\"queryBean.serviceIdList\" open=\"(\" separator=\",\" close=\")\" item=\"serviceId\">",
					"<![CDATA[(#{serviceId}) ]]>",
				"</foreach>",	
			"</if>",
		"</where>",
		"<if test=\"queryBean.orderRule==0\">",
			" ORDER BY BC.TRANSDATE ASC",
		"</if>" ,
		"<if test=\"queryBean.orderRule==1\">",
			" ORDER BY BC.TRANSDATE DESC",
		"</if>" ,
		"<if test=\"queryBean.orderRule==2\">",
			" ORDER BY BC.CREATEDATE ASC",
		"</if>" ,
		"<if test=\"queryBean.orderRule==3\">",
			" ORDER BY BC.CREATEDATE DESC",
		"</if>" ,
		"<if test=\"queryBean.orderRule==4\">",
			" ORDER BY BC.DEALDATE ASC",
		"</if>" ,
		"<if test=\"queryBean.orderRule==5\">",
			" ORDER BY BC.DEALDATE DESC",
		"</if>" ,
		"</script>"})
	List<BillCheckVo> findBillListForException(@Param("queryBean")QueryBillCheck queryBean);
	
	/**
	 * 查询对账明细列表数据(月对账)
	 * 
	 * @param queryYear 年份
	 */
	@Select({"<script>",
		"SELECT COUNT(BC.HISORDERNO) AS hisCount,COUNT(BC.MERCHNO) AS merchCount,SUM(BC.CHECKSTATE) AS checkState,MONTH (BC.TRANSDATE) AS transMonth ",
		"FROM P_BILL_CHECK BC ",
		"<where>",
			"<if test=\"queryYear!=null and queryYear!=''\">",
			" AND YEAR (BC.TRANSDATE) = #{queryYear}" ,
			"</if>" ,
		"</where>",
		" GROUP BY MONTH (BC.TRANSDATE) ORDER BY MONTH (BC.TRANSDATE) ASC",
		"</script>"})
	List<BillByMonthVo> findBillListForMonth(@Param("queryYear")String queryYear);
	
	/**
	 * 查询对账明细列表数据(月对账)
	 * 
	 * @param queryYear 年份
	 */
	@Select({"<script>",
		"SELECT SUM(BC.HISPRICE) AS hisMoney, SUM(BC.MERCHPRICE) AS merchMoney",
		"FROM P_BILL_CHECK BC ",
		"<where>",
			"AND YEAR (BC.TRANSDATE) = #{queryYear} AND MONTH (BC.TRANSDATE) = #{queryMonth} AND BC.BILLTYPE = #{billType}",
		"</where>",
		"</script>"})
	BillByMonthVo findSumMoneyForMonth(@Param("queryYear")String queryYear, @Param("queryMonth")String queryMonth, @Param("billType")Integer billType);
	
	@Select({"<script>",
		"SELECT BC.id,BC.orderNo,BC.orderId,BC.CREATEDATE AS billDate,BC.dealWay,BC.checkState,BC.dealState,BC.dealBy,BC.dealRemark,",
			"BC.dealDate,BC.channelId,BC.channelName,BC.hisOrderNo,BC.hisPrice,BC.payMethod,BC.payMethodName,BC.merchNo,",
			"BC.merchPrice, BC.hisbizDate,BC.payDate, BC.billType, BC.updateDate ",
		"FROM P_BILL_CHECK BC WHERE BC.ID = #{billId}",
		"</script>"})
	BillDetailVo findBillDetailById(String billId);
	
	@Select("SELECT O.orderId, O.serviceId FROM O_ORDER O WHERE O.ORDERID = #{orderId}")
	BillDetailVo findPayOrderById(@Param("orderId")String orderId);
	
	@Select("SELECT RO.orderId, O.serviceId FROM O_REFUNDORDER RO LEFT JOIN O_ORDER O ON O.ORDERID = RO.ORDERID WHERE RO.REFUNDORDERID = #{refundOrderId}")
	BillDetailVo findRefundOrderById(@Param("refundOrderId")String refundOrderId);
	
	@Select({"<script>",
		"SELECT BC.id,BC.orderNo,BC.hisOrderNo,BC.merchNo,BC.configkey,BC.transDate,BC.payMethod,BC.payMethodName,BC.channelId,BC.channelName,BC.billType,",
		"BC.hisPrice,BC.merchPrice,BC.checkState,BC.dealWay,BC.dealBy,BC.dealRemark,BC.dealState,BC.dealDate,BC.createBy,BC.createDate,BC.updateBy,BC.updateDate",
		" FROM P_BILL_CHECK BC",
		"<where>",
			"<if test=\"isHisSingle == true\">",
				" AND BC.HISORDERNO IS NULL" ,
			"</if>",
			"<if test=\"isMerchSingle == true\">",
				" AND BC.MERCHNO IS NULL" ,
			"</if>",
			" AND BC.ORDERNO = #{orderNo} AND BC.BILLTYPE = ${orderType}",
		"</where>",
		"</script>"})
	BillCheckVo findBillCheckByOrderNo(@Param("orderNo")String orderNo, @Param("isHisSingle")boolean isHisSingle, @Param("isMerchSingle")boolean isMerchSingle, @Param("orderType")Integer orderType);
	
	@Select({"<script>",
		"SELECT BC.payMethod, BC.payMethodName, BC.configkey, SUM(BC.HISPRICE) AS hisBillSum, SUM(BC.MERCHPRICE) AS merchBillSum FROM P_BILL_CHECK BC",
		"<where>",
			"<if test=\"startDate != null and startDate != '' and endDate != null and endDate != '' \">",
				" AND BC.TRANSDATE BETWEEN #{startDate} AND #{endDate}" ,
			"</if>",
			"<if test=\"orderType != null\">",
				" AND BC.BILLTYPE = ${orderType}" ,
			"</if>",
		"</where>",
		" GROUP BY BC.PAYMETHOD, BC.PAYMETHODNAME, BC.CONFIGKEY",
		"</script>"})
	List<BillMerchRF> findMoneySumByOrderType(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("orderType")Integer orderType);
	
	@Select({"<script>",
		"SELECT COUNT(1) FROM P_BILL_CHECK BC",
		"<where>",
			"<if test=\"startDate != null and startDate != '' and endDate != null and endDate != '' \">",
				" AND BC.TRANSDATE BETWEEN #{startDate} AND #{endDate}" ,
			"</if>",
			"<if test=\"configkey != null and configkey != '' \">",
				" AND BC.CONFIGKEY = #{configkey}" ,
			"</if>",
			"<if test=\"channelId != null and channelId != '' \">",
				" AND BC.CHANNELID = #{channelId}" ,
			"</if>",
			" AND BC.CHECKSTATE != 0",
		"</where>",
		"</script>"})
	Integer findMoneyIsWrong(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("configkey")String configkey, @Param("channelId")String channelId);
	
	@Select({"<script>",
		"SELECT COUNT(1) FROM P_BILL_CHECK BC",
		"<where>",
			"<if test=\"startDate != null and startDate != '' and endDate != null and endDate != '' \">",
				" AND BC.TRANSDATE BETWEEN #{startDate} AND #{endDate}" ,
			"</if>",
			"<if test=\"channelId != null and channelId != '' \">",
				" AND BC.CHANNELID = #{channelId}" ,
			"</if>",
			" AND BC.HISORDERNO IS NOT NULL",
		"</where>",
		"</script>"})
	int findHisBillCount(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("channelId")String channelId);
	
	@Select({"<script>",
		"SELECT COUNT(1) FROM P_BILL_CHECK BC",
		"<where>",
			"<if test=\"startDate != null and startDate != '' and endDate != null and endDate != '' \">",
				" AND BC.TRANSDATE BETWEEN #{startDate} AND #{endDate}" ,
			"</if>",
			"<if test=\"channelId != null and channelId != '' \">",
				" AND BC.CHANNELID = #{channelId}" ,
			"</if>",
			" AND BC.CHECKSTATE = 0",
		"</where>",
		"</script>"})
	int findBillCheck0Count(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("channelId")String channelId);
	
	@Select({"<script>",
		"SELECT COUNT(1) FROM P_BILL_CHECK BC",
		"<where>",
			"<if test=\"startDate != null and startDate != '' and endDate != null and endDate != '' \">",
				" AND BC.TRANSDATE BETWEEN #{startDate} AND #{endDate}" ,
			"</if>",
			"<if test=\"channelId != null and channelId != '' \">",
				" AND BC.CHANNELID = #{channelId}" ,
			"</if>",
			" AND BC.MERCHNO IS NULL",
		"</where>",
		"</script>"})
	int findHisSingleBillCount(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("channelId")String channelId);
	
	@Select({"<script>",
		"SELECT COUNT(1) FROM P_BILL_CHECK BC",
		"<where>",
			"<if test=\"startDate != null and startDate != '' and endDate != null and endDate != '' \">",
				" AND BC.TRANSDATE BETWEEN #{startDate} AND #{endDate}" ,
			"</if>",
			"<if test=\"channelId != null and channelId != '' \">",
				" AND BC.CHANNELID = #{channelId}" ,
			"</if>",
			" AND BC.HISORDERNO IS NULL",
		"</where>",
		"</script>"})
	int findChannelSingleBillCount(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("channelId")String channelId);
	
	@Select({"<script>",
		"SELECT COUNT(1) FROM P_BILL_CHECK BC",
		"<where>",
			"<if test=\"startDate != null and startDate != '' and endDate != null and endDate != '' \">",
				" AND BC.TRANSDATE BETWEEN #{startDate} AND #{endDate}" ,
			"</if>",
			"<if test=\"channelId != null and channelId != '' \">",
				" AND BC.CHANNELID = #{channelId}" ,
			"</if>",
			" AND BC.MERCHNO IS NOT NULL AND BC.HISORDERNO IS NOT NULL AND BC.CHECKSTATE IN(1,-1)",
		"</where>",
		"</script>"})
	int findDiffPriceBillCount(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("channelId")String channelId);
	
	@Select({"<script>",
		"SELECT BC.channelId, SUM(BC.HISPRICE) AS hisBillSum, SUM(BC.MERCHPRICE) AS merchBillSum FROM P_BILL_CHECK BC",
		"<where>",
			"<if test=\"startDate != null and startDate != '' and endDate != null and endDate != '' \">",
				" AND BC.TRANSDATE BETWEEN #{startDate} AND #{endDate}" ,
			"</if>",
			"<if test=\"orderType != null \">",
				" AND BC.BILLTYPE = #{orderType}" ,
			"</if>",
			"<if test=\"channelId != null and channelId != '' \">",
				" AND BC.CHANNELID = #{channelId}" ,
			"</if>",
		"</where>",
		"</script>"})
	BillChannelRF findMoneySumByChannelId(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("channelId")String channelId, @Param("orderType")Integer orderType);
	
	@Select({"<script>",
		"SELECT COUNT(1) FROM P_BILL_CHECK BC",
		"<where>",
			"<if test=\"startDate != null and startDate != '' and endDate != null and endDate != '' \">",
				" AND BC.TRANSDATE BETWEEN #{startDate} AND #{endDate}" ,
			"</if>",
			"<if test=\"channelId != null and channelId != '' \">",
				" AND BC.CHANNELID = #{channelId}" ,
			"</if>",
			" AND BC.MERCHNO IS NOT NULL",
		"</where>",
		"</script>"})
	int findChannelBillCount(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("channelId")String channelId);
	
	@Select({"<script>",
		"SELECT BC.channelId, BC.channelName FROM P_BILL_CHECK BC",
		"<where>",
			"<if test=\"startDate != null and startDate != '' and endDate != null and endDate != '' \">",
				" AND BC.TRANSDATE BETWEEN #{startDate} AND #{endDate}" ,
			"</if>",
		"</where>",
		" GROUP BY BC.CHANNELID, BC.CHANNELNAME",
		"</script>"})
	List<ChannelVo> findChannelList(@Param("startDate")String startDate, @Param("endDate")String endDate);
	
	/***
	 * 查询出对账里面所有单边账的账单
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Select({"<script>",
		"SELECT BC.* FROM P_BILL_CHECK BC",
		"<where>",
			"<if test=\"startDate != null and startDate != '' and endDate != null and endDate != '' \">",
				" AND BC.TRANSDATE BETWEEN #{startDate} AND #{endDate}" ,
			"</if>",
			" AND (BC.MERCHNO IS NULL OR BC.HISORDERNO IS NULL)",
		"</where>",
		"</script>"})
	List<BillCheckVo> findExceptBillCheck(@Param("startDate")String startDate, @Param("endDate")String endDate);
	
	@Select("SELECT O.ORDERID, IFNULL(O.PRESCNO, BO.OUTBIZORDERID) AS HISSERIALNO, PO.PRICE AS PAYMONEY FROM O_ORDER O "
			+ "LEFT JOIN O_PAYORDER PO ON PO.ORDERID = O.ORDERID "
			+ "LEFT JOIN O_BIZORDER BO ON BO.ORDERID = O.ORDERID "
			+ "WHERE O.ORDERID = #{orderId}")
	PayOrderDetailVo findHisOrderByOrderId(String orderId);
	
	@Select({"<script>",
		"SELECT COUNT(1) FROM P_BILL_CHECK BC",
		"<where>",
			" AND BC.TRANSDATE BETWEEN #{startDate} AND #{endDate} AND BC.CHANNELID = #{channelId}",
			" AND BC.CONFIGKEY = #{configKey} AND BC.CHECKSTATE IN(1,-1)",
		"</where>",
		"</script>"})
	int findExceptBillCount(@Param("startDate")String startDate, @Param("endDate")String endDate, 
			@Param("channelId")String channelId, @Param("configKey")String configKey);
	
	@Select({"<script>",
		"SELECT COUNT(1) FROM P_BILL_CHECK BC",
		"<where>",
			" AND BC.TRANSDATE BETWEEN #{startDate} AND #{endDate} AND BC.CHANNELID = #{channelId}",
			" AND BC.CONFIGKEY = #{configKey} AND BC.DEALSTATE = 1",
		"</where>",
		"</script>"})
	int findDealBillCount(@Param("startDate")String startDate, @Param("endDate")String endDate,
			@Param("channelId")String channelId, @Param("configKey")String configKey);
	
	@Select("SELECT DISTINCT BC.channelId, BC.configkey FROM P_BILL_CHECK BC WHERE BC.ORDERID = #{orderId} AND BC.CONFIGKEY IS NOT NULL")
	BillCheckVo findBillCheckVoByOrderId(String orderId);
	
	/**
	 * 交易日报-统计待冲正笔数
	 * 
	 * @param startDate
	 * @param endDate
	 * @param payType
	 * @param channelId
	 * @param serviceId
	 * @return
	 */
	@Select({"<script>",
		"SELECT O.serviceId, COUNT(BC.ID) AS reverseCount, SUM(BC.MERCHPRICE) AS reverseMoney FROM P_BILL_CHECK BC ",
		"LEFT JOIN O_ORDER O ON O.ORDERID = BC.ORDERNO ",
		"LEFT JOIN O_PAYORDER PO ON PO.ORDERID = BC.ORDERNO ",
		"<where>",
			"<if test=\"startDate!=null and startDate!='' and endDate!=null and endDate!=''\">",
				" AND BC.CREATEDATE BETWEEN str_to_date(DATE_FORMAT(#{startDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s') ",
				" AND DATE_ADD(str_to_date(DATE_FORMAT(#{endDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s'),INTERVAL 1 DAY) " ,
			"</if>" ,
			"<if test=\"payType != null and payType != ''\">",
				"<![CDATA[ AND BC.PAYMETHOD = #{payType} ]]> ",
			"</if>",
			"<if test=\"channelId != null and channelId != ''\">",
				"<![CDATA[ AND PO.CHANNELID = #{channelId} ]]> ",
			"</if>",
			"<if test=\"serviceId != null and serviceId != ''\">",
				"<![CDATA[ AND O.SERVICEID = #{serviceId} ]]> ",
			"</if>",
			" AND BC.CHECKSTATE = 1 AND HISORDERNO IS NULL AND BC.BILLTYPE = 1 ",
		"</where>",
	"</script>"})
	OrderReportVo findReverseCount(@Param("startDate")String startDate, @Param("endDate")String endDate, 
				@Param("payType")String payType, @Param("channelId")String channelId, @Param("serviceId")String serviceId);
}

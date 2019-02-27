package com.kasite.client.order.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.client.order.bean.dbo.Order;
import com.kasite.client.order.bean.dbo.RefundOrder;
import com.kasite.client.order.bean.dto.QueryOrderViewParam;
import com.kasite.core.serviceinterface.module.order.dbo.OrderView;
import com.kasite.core.serviceinterface.module.order.dto.OrderCountMoneyVo;
import com.kasite.core.serviceinterface.module.order.dto.OrderQuery;
import com.kasite.core.serviceinterface.module.order.dto.OrderReportVo;
import com.kasite.core.serviceinterface.module.order.dto.OrderTransLogVo;
import com.kasite.core.serviceinterface.module.order.dto.OrderVo;
import com.kasite.core.serviceinterface.module.order.dto.PayOrderDetailVo;
import com.kasite.core.serviceinterface.module.order.dto.RefundOrderDetailVo;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @className: IOrderMapper
 * @author: lcz
 * @date: 2018年7月20日 下午4:56:00
 */
public interface IOrderMapper extends Mapper<Order>{
	@Select({"<script>",
		"SELECT a.MERCHANTTYPE, a.HOSID, a.ORDERID, a.SERVICEID,a.ORDERNUM, a.PRICENAME",
		", IFNULL(a.TOTALPRICE, 0) AS TOTALPRICE",
		", IFNULL(a.PRICE, 0) AS PRICE, c.REFUNDPRICE",
		", a.ORDERMEMO, a.CARDNO, a.ISONLINEPAY, a.PRESCNO, a.OPERATORID",
		", a.OPERATORNAME, a.CARDTYPE, a.BEGINDATE,b.TRANSACTIONNO, a.CHANNELID, b.CHANNELID AS PAYCHANNELID",
		", CASE ",
		"	WHEN c.REFUNDSTATE = 4 THEN 4",
		"	WHEN c.REFUNDSTATE = 3 THEN 3",
		"	WHEN b.PAYSTATE = 2 THEN 2",
		"	WHEN b.PAYSTATE = 1 THEN 1",
		"	ELSE 0",
		"END AS PAYSTATE, IFNULL(d.BIZSTATE, 0) AS BIZSTATE",
		", IFNULL(f.OVERSTATE, 0) AS OVERSTATE, a.EQPTTYPE AS EQPTTYPE,b.CONFIGKEY AS CONFIGKEY,",
		"e.OPENID,e.MEMBERID,e.MEMBERNAME,e.MOBILE,e.IDCARDNO,e.SEX,e.BIRTHDATE,e.ADDRESS,e.ISCHILDREN",
		"FROM (",
		"	SELECT MERCHANTTYPE, ORDERID, SERVICEID,ORDERNUM, PRICENAME, TOTALPRICE",
		"		, PRICE, OPERATORID, OPERATORNAME, CHANNELID, CHANNELNAME",
		"		, HOSID, BEGINDATE, ENDDATE, CARDNO, CARDTYPE",
		"		, PRESCNO, ORDERMEMO, EQPTTYPE, ISONLINEPAY",
		"	FROM O_ORDER",
		"	WHERE ORDERID=#{orderId} AND BEGINDATE >= now() - INTERVAL #{orderLimitDate} DAY",
		") a",
		"LEFT JOIN (",
		"	SELECT r.ORDERID, SUM(r.REFUNDPRICE) AS REFUNDPRICE",
		"		, IF(MIN(r.PAYSTATE) != 7, MIN(r.PAYSTATE), 0) AS REFUNDSTATE",
		"	FROM O_REFUNDORDER r",
		"	GROUP BY r.ORDERID",
		") c ON a.ORDERID = c.ORDERID",
		"LEFT JOIN O_PAYORDER b ON a.ORDERID = b.ORDERID",
		"LEFT JOIN O_BIZORDER d ON a.ORDERID = d.ORDERID",
		"LEFT JOIN O_OVERORDER f ON a.ORDERID = f.ORDERID",
		"LEFT JOIN O_ORDER_MEMBER e ON a.ORDERID = e.ORDERID",
	"</script>"})
	OrderView getOrderViewByOrderId(@Param("orderId")String orderId,@Param("orderLimitDate")Integer orderLimitDate);
	
	@Select({"<script>",
		"SELECT * FROM (",
		"SELECT a.MERCHANTTYPE, a.HOSID, a.ORDERID, a.SERVICEID,a.ORDERNUM, a.PRICENAME",
		", IFNULL(a.TOTALPRICE, 0) AS TOTALPRICE",
		", IFNULL(a.PRICE, 0) AS PRICE, c.REFUNDPRICE",
		", a.ORDERMEMO, a.CARDNO, a.ISONLINEPAY, a.PRESCNO, a.OPERATORID",
		", a.OPERATORNAME, a.CARDTYPE, a.BEGINDATE, b.TRANSACTIONNO, a.CHANNELID, b.CHANNELID AS PAYCHANNELID",
		", CASE ",
		"	WHEN c.REFUNDSTATE = 4 THEN 4",//退费完成 前端 退款完成 表示退费完成 判断支付状态： O_REFUNDORDER 表中退款状态  3:退费申请中 4:退费完成7:退费失败 
		"	WHEN c.REFUNDSTATE = 3 THEN 3",//退费中
		"	WHEN b.PAYSTATE = 2 THEN 2",// 支付状态 支付完成 前端显示支付完成  O_PAYORDER 表中定义的：PAYSTATE 支付状态  1支付中  2支付完成
		"	WHEN b.PAYSTATE = 1 THEN 1",// 支付中 显示支付中
		"	ELSE 0",//默认 不存在：O_REFUNDORDER  O_PAYORDER 记录的时候 默认是 0 支付状态：待支付
		"END AS PAYSTATE, IFNULL(d.BIZSTATE, 0) AS BIZSTATE",//业务状态：  1:订单业务完成 2:订单业务取消 0:未执行业务
		", IFNULL(f.OVERSTATE, 0) AS OVERSTATE, a.EQPTTYPE,",
		"a.OPENID,a.MEMBERID,e.MEMBERNAME,e.MOBILE,e.IDCARDNO,e.SEX,e.BIRTHDATE,e.ADDRESS,e.ISCHILDREN,e.HISMEMBERID",
		"FROM (",
		"	SELECT MERCHANTTYPE, ORDERID, SERVICEID, ORDERNUM, PRICENAME, TOTALPRICE",
		"		, PRICE, OPERATORID , OPERATORNAME, CHANNELID, CHANNELNAME",
		"		, HOSID, BEGINDATE, ENDDATE, CARDNO, CARDTYPE",
		"		, PRESCNO, ORDERMEMO, EQPTTYPE, ISONLINEPAY,MEMBERID,OPENID",
		"	FROM O_ORDER ",
		"	WHERE BEGINDATE >= now() - INTERVAL #{queryOrderViewParam.orderLimitDate} DAY ",
		") a",
		"LEFT JOIN (",
		"	SELECT r.ORDERID, SUM(r.REFUNDPRICE) AS REFUNDPRICE",
		"		, IF(MIN(r.PAYSTATE) != 7, MIN(r.PAYSTATE), 0) AS REFUNDSTATE",
		"	FROM O_REFUNDORDER r",
		"	GROUP BY r.ORDERID",
		") c ON a.ORDERID = c.ORDERID",
		"LEFT JOIN O_PAYORDER b ON a.ORDERID = b.ORDERID",
		"LEFT JOIN O_BIZORDER d ON a.ORDERID = d.ORDERID",
		"LEFT JOIN O_OVERORDER f ON a.ORDERID = f.ORDERID",
		"LEFT JOIN O_ORDER_MEMBER e ON a.ORDERID = e.ORDERID",
		") f ",
		"<where>",
		//支付状态
		"<if test=\"queryOrderViewParam.payState!=null\">",
		"	AND  f.PAYSTATE= #{queryOrderViewParam.payState}" ,
		"</if>" ,
//		"<if test=\"queryOrderViewParam.operatorId!=null and queryOrderViewParam.operatorId!=''\">",
//		"	AND  f.OPERATORID= #{queryOrderViewParam.operatorId}" ,
//		"</if>" ,
		//开始时间（订单生成时间）
		"<if test=\"queryOrderViewParam.beginDate!=null and queryOrderViewParam.beginDate!=''\">",
		"	<![CDATA[ AND  f.BEGINDATE  >=  str_to_date(#{queryOrderViewParam.beginDate},'%Y-%m-%d') ]]> " ,
		"</if>" ,
		//结束日期
		"<if test=\"queryOrderViewParam.endDate!=null and queryOrderViewParam.endDate!=''\">",
		"	<![CDATA[ AND  f.BEGINDATE  < str_to_date(#{queryOrderViewParam.endDate},'%Y-%m-%d') ]]> " ,
		"</if>" ,
		//渠道ID
		"<if test=\"queryOrderViewParam.channelId!=null and queryOrderViewParam.channelId!=''\">",
		"	AND  f.CHANNELID =#{queryOrderViewParam.channelId}" ,
		"</if>" ,
		//卡号+卡类型
		"<if test=\"queryOrderViewParam.cardNo!=null and queryOrderViewParam.cardNo!='' and queryOrderViewParam.cardType!=null\">",
		"	AND  f.CARDNO =#{queryOrderViewParam.cardNo} AND f.CARDTYPE=#{queryOrderViewParam.cardType}" ,
		"</if>" ,
		//交易流水号
		"<if test=\"queryOrderViewParam.transactionNo!=null and queryOrderViewParam.transactionNo!=''\">",
		"	AND  b.TRANSACTIONNO =#{queryOrderViewParam.transactionNo}" ,
		"</if>" ,
		//订单业务执行状态
		"<if test=\"queryOrderViewParam.bizState!=null and queryOrderViewParam.bizState!='' \">",
		"	<choose>",
		"		<when test='queryOrderViewParam.bizState==\"!=0\" ' >",
		"	 	<![CDATA[	 AND f.BIZSTATE <> 0 ]]>" ,
		"		</when>", 
		"		 <otherwise>", 
		"			AND f.BIZSTATE =#{queryOrderViewParam.bizState}" ,
		"		 </otherwise>", 
		"	</choose>", 
		"</if>" ,
		//订单完成状态
		"<if test=\"queryOrderViewParam.overState!=null and queryOrderViewParam.overState!=''\">",
		"	AND  f.OVERSTATE =#{queryOrderViewParam.overState}" ,
		"</if>" ,
		//业务订单类型
		"<if test=\"queryOrderViewParam.serviceId!=null and queryOrderViewParam.serviceId!=''\">",
		"	AND  f.SERVICEID =#{queryOrderViewParam.serviceId}" ,
		"</if>" ,
		//订单ID
		"<if test=\"queryOrderViewParam.orderId!=null and queryOrderViewParam.orderId!=''\">",
		"	AND  f.ORDERID =#{queryOrderViewParam.orderId}" ,
		"</if>" ,
		//HIS订单流水 或者 处方当号 （HIS内部唯一）
		"<if test=\"queryOrderViewParam.prescNo!=null and queryOrderViewParam.prescNo!=''\">",
		"	AND  f.PRESCNO =#{queryOrderViewParam.prescNo}" ,
		"</if>" ,
		//成员卡号列表 尽量少用这个查询，尽量用memberid查询
		"<if test=\"queryOrderViewParam.memberList != null and queryOrderViewParam.memberList.size>0\">",
			"AND <foreach collection=\"queryOrderViewParam.memberList\" item=\"member\" open=\"(\" separator=\" OR \" close=\")\" > ",
					"<if test=\"member.cardNo!=null and member.cardNo!=''\">",
					" f.CARDNO =#{member.cardNo} AND f.CARDTYPE=#{member.cardType} ",
					"</if>" ,
					//成员id
					"<if test=\"member.memberId!=null and member.memberId!=''\">",
					" f.MEMBERID=#{member.memberId} ",
					"</if>" ,
				"</foreach>",
		 "</if>",
		//用户微信openId
		"<if test=\"queryOrderViewParam.openId!=null and queryOrderViewParam.openId!=''\">",
		"	AND  f.OPENID=#{queryOrderViewParam.openId}" ,
		"</if>" ,
		//业务类型列表
		"<if test=\"queryOrderViewParam.serviceIds != null and queryOrderViewParam.serviceIds.size>0\">",
			"AND f.SERVICEID IN ",
				"<foreach collection=\"queryOrderViewParam.serviceIds\" item=\"serviceId\" open=\"(\" separator=\" , \" close=\")\" > ",
					"#{serviceId}",
				"</foreach>",
		 "</if>",
		"</where>",
		"ORDER BY f.BEGINDATE DESC",
	"</script>"})
	List<OrderView> queryOrderView(@Param("queryOrderViewParam")QueryOrderViewParam queryOrderViewParam);
	
	@Select({"<script>",
		"SELECT O.ORDERID,O.CHANNELID FROM O_ORDER O ",
		"<where>",
			"AND NOT EXISTS (select 1 FROM O_PAYORDER P WHERE P.ORDERID = O.ORDERID)",
			"AND NOT EXISTS (select 1 from O_BIZORDER B WHERE B.ORDERID = O.ORDERID)",
			"AND NOT EXISTS (select 1 from O_OVERORDER OV WHERE OV.ORDERID = O.ORDERID)",
				"<![CDATA[ AND O.ENDDATE < NOW() ]]>",
		"</where>",
	"</script>"})
	List<Order> queryTimeOutOrder();
	
	
	@Select("SELECT SUM(REFUNDPRICE) FROM O_REFUNDORDER WHERE ORDERID=#{orderId} AND PAYSTATE = 4 ")
	Integer getTotalRrefundPrice(@Param("orderId")String orderId);
	
	@Select("SELECT COUNT(DISTINCT  a.CARDNO) AS CARDNOCOUNT,a.CHANNELID "
			+ "FROM O_ORDER a LEFT JOIN O_PAYORDER b ON a.ORDERID = b.ORDERID "
			+ "WHERE b.PAYSTATE=2 AND a.BEGINDATE>now() - INTERVAL 1 DAY GROUP BY a.CHANNELID")
	List<Map<String, Object>> getOrderDataCollection();
	
	/**
	 * 查询订单列表信息(智付后台-交易管理)
	 * 
	 * @param query
	 * @return
	 */
	@Select({"<script>",
		"SELECT PO.orderId, IFNULL(RO.REFUNDORDERID,'') AS refundOrderId, O.BEGINDATE AS transTime,IFNULL(PO.TRANSACTIONNO,'') AS channelSerialNo, ",
		"O.serviceId,IFNULL(O.PRESCNO, BO.OUTBIZORDERID) AS hisSerialNo, O.PRICE AS orderMoney,IFNULL(O.CARDNO,'') AS cardNo, PO.PRICE AS transMoney,",
		"PO.PAYSTATE AS orderState,PO.configKey, IFNULL(BO.BIZSTATE, OUTBO.OUTBIZTYPE) AS bizState, ",
		"OM.MEMBERNAME AS nickName,OM.MOBILE AS nickMobile ",
		"FROM O_PAYORDER PO ",
		"LEFT JOIN O_ORDER O ON PO.ORDERID = O.ORDERID ",
		"LEFT JOIN O_BIZORDER BO ON BO.ORDERID = PO.ORDERID ",
		"LEFT JOIN O_ORDER_OUT_BIZ OUTBO ON OUTBO.ORDERID = PO.ORDERID ",
		"LEFT JOIN O_REFUNDORDER RO ON RO.ORDERID = PO.ORDERID ",
		"LEFT JOIN O_ORDER_MEMBER OM ON OM.ORDERID = PO.ORDERID ",
		"<where>",
			"<if test=\"queryBean.startDate!=null and queryBean.startDate!='' and queryBean.endDate!=null and queryBean.endDate!=''\">",
				"<if test=\"queryBean.orderState == 999 or queryBean.orderState == 1 or queryBean.orderState == 2\">",
					" AND PO.BEGINDATE BETWEEN str_to_date(DATE_FORMAT(#{queryBean.startDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s') ",
					" AND DATE_ADD(str_to_date(DATE_FORMAT(#{queryBean.endDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s'),INTERVAL 1 DAY) ",
				"</if>",
				"<if test=\"queryBean.orderState == 3 or queryBean.orderState == 4 or queryBean.orderState == 7\">",
					" AND RO.BEGINDATE BETWEEN str_to_date(DATE_FORMAT(#{queryBean.startDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s') ",
					" AND DATE_ADD(str_to_date(DATE_FORMAT(#{queryBean.endDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s'),INTERVAL 1 DAY) ",
				"</if>",
			"</if>" ,
			"<if test=\"queryBean.orderId != null and queryBean.orderId != ''\">",
				"<![CDATA[ AND PO.ORDERID = #{queryBean.orderId} ]]>",
			"</if>",
			"<if test=\"queryBean.configKey != null and queryBean.configKey != ''\">",
				"<![CDATA[ AND PO.CONFIGKEY = #{queryBean.configKey} ]]>",
			"</if>",
			"<if test=\"queryBean.channelSerialNo != null and queryBean.channelSerialNo != ''\">",
				"<![CDATA[ AND PO.TRANSACTIONNO = #{queryBean.channelSerialNo} ]]>",
			"</if>",
			"<if test=\"queryBean.hisSerialNo != null and queryBean.hisSerialNo != ''\">",
				"<![CDATA[ AND BO.OUTBIZORDERID = #{queryBean.hisSerialNo} ]]>",
			"</if>",
			"<if test=\"queryBean.cardNo != null and queryBean.cardNo != ''\">",
				"<![CDATA[ AND O.CARDNO LIKE #{queryBean.cardNo} ]]>",
			"</if>",
			"<if test=\"queryBean.nickName != null and queryBean.nickName != ''\">",
				"<![CDATA[ AND OM.MEMBERNAME LIKE #{queryBean.nickName} ]]>",
			"</if>",
			"<if test=\"queryBean.nickMobile != null and queryBean.nickMobile != ''\">",
				"<![CDATA[ AND OM.MOBILE LIKE #{queryBean.nickMobile} ]]>",
			"</if>",
			"<if test=\"queryBean.serviceIdList != null and queryBean.serviceIdList.size>0\">",
				" AND O.SERVICEID IN ",
				"<foreach collection=\"queryBean.serviceIdList\" open=\"(\" separator=\",\" close=\")\" item=\"serviceId\">",
					"<![CDATA[(#{serviceId}) ]]>",
				"</foreach>",	
			"</if>",
			"<if test=\"queryBean.orderState != null and queryBean.orderState != 999\">",
				"<choose>",
					"<when test=\"queryBean.orderState == 1\">",
						" AND (BO.BIZSTATE IS NULL OR BO.BIZSTATE = 2) AND OUTBO.ORDERID IS NULL AND RO.PAYSTATE IS NULL",
					"</when>",
					"<when test=\"queryBean.orderState == 2\">",
						" AND (BO.BIZSTATE = 1 OR OUTBO.OUTBIZTYPE = 1) AND RO.PAYSTATE IS NULL",
					"</when>",
					"<otherwise>",
						"<![CDATA[ AND RO.PAYSTATE = #{queryBean.orderState} ]]>",
					"</otherwise>",
				"</choose>",
			"</if>",
			"<if test=\"queryBean.channelIdList != null and queryBean.channelIdList.size>0\">",
				" AND PO.CHANNELID IN ",
				"<foreach collection=\"queryBean.channelIdList\" open=\"(\" separator=\",\" close=\")\" item=\"channelId\">",
					"<![CDATA[(#{channelId}) ]]>",
				"</foreach>",	
			"</if>",
			" AND PO.PAYSTATE = 2",
		"</where>",
		" ORDER BY PO.BEGINDATE DESC",
	"</script>"})
	List<OrderVo> findAllOrderList(@Param("queryBean")OrderQuery query);
	
	/**
	 * 根据退款流水号查询退款订单信息(智付后台-交易管理)
	 * 
	 * @param refundOrderId
	 * @return
	 */
	@Select({"<script>",
		"SELECT RO.orderId, RO.channelId, IFNULL(RO.REFUNDORDERID,'') AS refundOrderId, O.serviceId, RO.BEGINDATE AS transTime, IFNULL(RO.REFUNDNO,'') AS channelSerialNo, ",
		"IFNULL(RO.OUTREFUNDORDERID,IFNULL(O.PRESCNO,BO.OUTBIZORDERID)) AS hisSerialNo,IFNULL(O.CARDNO,'') AS cardNo, O.PRICE AS orderMoney, RO.REFUNDPRICE AS transMoney, RO.PAYSTATE AS orderState ",
		"FROM O_REFUNDORDER RO ",
		"LEFT JOIN O_ORDER O ON RO.ORDERID = O.ORDERID ",
		"LEFT JOIN O_BIZORDER BO ON BO.ORDERID = RO.ORDERID ",
		"<where>",
			"<if test=\"refundOrderId != null and refundOrderId != ''\">",
				"<![CDATA[ AND RO.refundOrderId = #{refundOrderId} ]]>",
			"</if>",
		"</where>",
	"</script>"})
	OrderVo findRefundOrderByRefundOrderId(@Param("refundOrderId")String refundOrderId);
	
	/**
	 * 统计支付金额(支付成功状态)
	 * 
	 * @param query
	 * @return
	 */
	@Select({"<script>",
		"SELECT SUM(PO.PRICE) AS totalPayMoney, SUM(O.PRICE) AS totalOrderMoney FROM O_PAYORDER PO ",
		"LEFT JOIN O_ORDER O ON PO.ORDERID = O.ORDERID ",
		"LEFT JOIN O_BIZORDER BO ON BO.ORDERID = PO.ORDERID ",
		"LEFT JOIN O_ORDER_OUT_BIZ OUTBIZ ON OUTBIZ.ORDERID = PO.ORDERID",
		"LEFT JOIN O_ORDER_MEMBER OM ON OM.ORDERID = PO.ORDERID ",
		"<where>",
			"<if test=\"queryBean.startDate!=null and queryBean.startDate!='' and queryBean.endDate!=null and queryBean.endDate!=''\">",
				" AND PO.BEGINDATE BETWEEN str_to_date(DATE_FORMAT(#{queryBean.startDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s') ",
				" AND DATE_ADD(str_to_date(DATE_FORMAT(#{queryBean.endDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s'),INTERVAL 1 DAY) " ,
			"</if>" ,
			"<if test=\"queryBean.orderId != null and queryBean.orderId != ''\">",
				"<![CDATA[ AND PO.ORDERID = #{queryBean.orderId} ]]>",
			"</if>",
			"<if test=\"queryBean.configKey != null and queryBean.configKey != ''\">",
				"<![CDATA[ AND PO.CONFIGKEY = #{queryBean.configKey} ]]>",
			"</if>",
			"<if test=\"queryBean.channelSerialNo != null and queryBean.channelSerialNo != ''\">",
				"<![CDATA[ AND PO.TRANSACTIONNO = #{queryBean.channelSerialNo} ]]>",
			"</if>",
			"<if test=\"queryBean.hisSerialNo != null and queryBean.hisSerialNo != ''\">",
				"<![CDATA[ AND BO.OUTBIZORDERID = #{queryBean.hisSerialNo} ]]>",
			"</if>",
			"<if test=\"queryBean.cardNo != null and queryBean.cardNo != ''\">",
				"<![CDATA[ AND O.CARDNO LIKE #{queryBean.cardNo} ]]>",
			"</if>",
			"<if test=\"queryBean.nickName != null and queryBean.nickName != ''\">",
				"<![CDATA[ AND OM.MEMBERNAME LIKE #{queryBean.nickName} ]]>",
			"</if>",
			"<if test=\"queryBean.nickMobile != null and queryBean.nickMobile != ''\">",
				"<![CDATA[ AND OM.MOBILE LIKE #{queryBean.nickMobile} ]]>",
			"</if>",
			"<if test=\"queryBean.serviceIdList != null and queryBean.serviceIdList.size>0\">",
				" AND O.SERVICEID IN ",
				"<foreach collection=\"queryBean.serviceIdList\" open=\"(\" separator=\",\" close=\")\" item=\"serviceId\">",
					"<![CDATA[(#{serviceId}) ]]>",
				"</foreach>",	
			"</if>",
			"<if test=\"queryBean.orderState != null and queryBean.orderState != 999\">",
				"<choose>",
					"<when test=\"queryBean.orderState == 1\">",
						" AND (BO.BIZSTATE IS NULL OR BO.BIZSTATE = 2)  AND OUTBIZ.ORDERID IS NULL",
					"</when>",
					"<when test=\"queryBean.orderState == 2\">",
						" AND (BO.BIZSTATE = 1 OR OUTBIZ.OUTBIZTYPE = 1)",
					"</when>",
				"</choose>",
			"</if>",
			"<if test=\"queryBean.channelIdList != null and queryBean.channelIdList.size>0\">",
				" AND PO.CHANNELID IN ",
				"<foreach collection=\"queryBean.channelIdList\" open=\"(\" separator=\",\" close=\")\" item=\"channelId\">",
					"<![CDATA[#{channelId} ]]>",
				"</foreach>",	
			"</if>",
			" AND PO.PAYSTATE = 2",
		"</where>",
	"</script>"})
	OrderCountMoneyVo findTotalPayMoney(@Param("queryBean")OrderQuery query);
	
	/**
	 * 统计退款金额(退款成功状态)
	 * 
	 * @param query
	 * @return
	 */
	@Select({"<script>",
		"SELECT SUM(RO.REFUNDPRICE) AS totalRefundMoney FROM O_REFUNDORDER RO ",
		"LEFT JOIN O_ORDER O ON RO.ORDERID = O.ORDERID ",
		"LEFT JOIN O_PAYORDER PO ON PO.ORDERID = RO.ORDERID ",
		"LEFT JOIN O_BIZORDER BO ON BO.ORDERID = RO.ORDERID ",
		"LEFT JOIN O_ORDER_OUT_BIZ OUTBIZ ON OUTBIZ.ORDERID = RO.ORDERID",
		"LEFT JOIN O_ORDER_MEMBER OM ON OM.ORDERID = RO.ORDERID ",
		"<where>",
			"<if test=\"queryBean.startDate!=null and queryBean.startDate!='' and queryBean.endDate!=null and queryBean.endDate!=''\">",
				" AND RO.BEGINDATE BETWEEN str_to_date(DATE_FORMAT(#{queryBean.startDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s') ",
				" AND DATE_ADD(str_to_date(DATE_FORMAT(#{queryBean.endDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s'),INTERVAL 1 DAY) " ,
			"</if>" ,
			"<if test=\"queryBean.orderId != null and queryBean.orderId != ''\">",
				"<![CDATA[ AND PO.ORDERID = #{queryBean.orderId} ]]>",
			"</if>",
			"<if test=\"queryBean.configKey != null and queryBean.configKey != ''\">",
				"<![CDATA[ AND PO.CONFIGKEY = #{queryBean.configKey} ]]>",
			"</if>",
			"<if test=\"queryBean.channelSerialNo != null and queryBean.channelSerialNo != ''\">",
				"<![CDATA[ AND PO.TRANSACTIONNO = #{queryBean.channelSerialNo} ]]>",
			"</if>",
			"<if test=\"queryBean.hisSerialNo != null and queryBean.hisSerialNo != ''\">",
				"<![CDATA[ AND BO.OUTBIZORDERID = #{queryBean.hisSerialNo} ]]>",
			"</if>",
			"<if test=\"queryBean.cardNo != null and queryBean.cardNo != ''\">",
				"<![CDATA[ AND O.CARDNO LIKE #{queryBean.cardNo} ]]>",
			"</if>",
			"<if test=\"queryBean.nickName != null and queryBean.nickName != ''\">",
				"<![CDATA[ AND OM.MEMBERNAME LIKE #{queryBean.nickName} ]]>",
			"</if>",
			"<if test=\"queryBean.nickMobile != null and queryBean.nickMobile != ''\">",
				"<![CDATA[ AND OM.MOBILE LIKE #{queryBean.nickMobile} ]]>",
			"</if>",
			"<if test=\"queryBean.serviceIdList != null and queryBean.serviceIdList.size>0\">",
				" AND O.SERVICEID IN ",
				"<foreach collection=\"queryBean.serviceIdList\" open=\"(\" separator=\",\" close=\")\" item=\"serviceId\">",
					"<![CDATA[(#{serviceId}) ]]>",
				"</foreach>",	
			"</if>",
			"<if test=\"queryBean.channelIdList != null and queryBean.channelIdList.size>0\">",
				" AND PO.CHANNELID IN ",
				"<foreach collection=\"queryBean.channelIdList\" open=\"(\" separator=\",\" close=\")\" item=\"channelId\">",
					"<![CDATA[ #{channelId} ]]>",
				"</foreach>",	
			"</if>",
			"<if test=\"queryBean.orderState != null and queryBean.orderState != 999\">",
				"<![CDATA[ AND RO.PAYSTATE = #{queryBean.orderState} ]]>",
			"</if>",
			"<if test=\"queryBean.orderState == null or queryBean.orderState == 999\">",
				"AND RO.PAYSTATE = 4",
			"</if>",
			" AND PO.PAYSTATE = 2",
		"</where>",
	"</script>"})
	OrderCountMoneyVo findTotalRefundMoney(@Param("queryBean")OrderQuery query);
	
	/**
	 * 交易日报-支付订单报表信息
	 * 
	 * @param startDate
	 * @param endDate
	 * @param payType
	 * @param channelId
	 * @return
	 */
	@Select({"<script>",
		"SELECT O.serviceId, COUNT(B.BILLID) AS transCount, SUM(B.TRANSACTIONS) AS transMoney FROM O_PAYORDER PO ",
		"LEFT JOIN O_ORDER O ON O.ORDERID = PO.ORDERID ",
		"LEFT JOIN P_BILL B ON B.ORDERID = PO.ORDERID ",
		"<where>",
			"<if test=\"startDate!=null and startDate!='' and endDate!=null and endDate!=''\">",
				" AND B.TRANSDATE BETWEEN str_to_date(DATE_FORMAT(#{startDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s') ",
				" AND DATE_ADD(str_to_date(DATE_FORMAT(#{endDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s'),INTERVAL 1 DAY) " ,
			"</if>" ,
			"<if test=\"configKeyList != null and configKeyList.size>0\">",
				"AND PO.CONFIGKEY IN ",
					"<foreach collection=\"configKeyList\" item=\"configKey\" open=\"(\" separator=\" , \" close=\")\" > ",
						"<![CDATA[ #{configKey} ]]>",
					"</foreach>",
			 "</if>",
			"<if test=\"channelId != null and channelId != ''\">",
				"<![CDATA[ AND PO.CHANNELID = #{channelId} ]]> ",
			"</if>",
			" AND B.ORDERTYPE = 1 AND PO.PAYSTATE = 2 ",
		"</where>",
		" GROUP BY O.SERVICEID",
	"</script>"})
	List<OrderReportVo> findPayOrderReport(@Param("startDate")String startDate, @Param("endDate")String endDate, 
			@Param("configKeyList")List<String> configKeyList, @Param("channelId")String channelId);
	
	/**
	 * 交易日报-退款订单报表信息
	 * 
	 * @param startDate
	 * @param endDate
	 * @param configKey
	 * @param channelId
	 * @return
	 */
	@Select({"<script>",
		"SELECT O.serviceId, COUNT(B.BILLID) AS transCount, SUM(B.REFUNDPRICE) AS transMoney FROM P_BILL B ",
		"LEFT JOIN O_ORDER O ON O.ORDERID = B.ORDERID ",
		"LEFT JOIN O_PAYORDER PO ON PO.ORDERID = B.ORDERID ",
		"<where>",
			"<if test=\"startDate!=null and startDate!='' and endDate!=null and endDate!=''\">",
				" AND B.TRANSDATE BETWEEN str_to_date(DATE_FORMAT(#{startDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s') ",
				" AND DATE_ADD(str_to_date(DATE_FORMAT(#{endDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s'),INTERVAL 1 DAY) " ,
			"</if>" ,
			"<if test=\"configKey != null and configKey != ''\">",
				"<![CDATA[ AND PO.CONFIGKEY = #{configKey} ]]> ",
			"</if>",
			"<if test=\"channelId != null and channelId != ''\">",
				"<![CDATA[ AND PO.CHANNELID = #{channelId} ]]> ",
			"</if>",
			" AND B.ORDERTYPE = 2 ",
		"</where>",
		" GROUP BY O.SERVICEID",
	"</script>"})
	List<OrderReportVo> findRefundOrderReport(@Param("startDate")String startDate, @Param("endDate")String endDate, 
			@Param("configKey")String configKey, @Param("channelId")String channelId);
	
	/**
	 * 交易管理-已支付订单详情
	 * 
	 * @param orderId
	 * @return
	 */
	@Select({"<script>",
		"SELECT PO.orderId,O.cardNo,O.BEGINDATE AS orderDate,PO.BEGINDATE AS payDate,PO.TRANSACTIONNO AS channelSerialNo, ",
		"'' AS hisSerialNo,PO.configKey,PO.channelId,O.PRICE AS orderMoney,PO.PRICE AS payMoney,PO.payState, ",
		"O.serviceId,O.PRICENAME AS orderType, RO.PAYSTATE AS refundState,OM.MEMBERNAME AS nickName,OM.MOBILE AS nickMobile ",
		"FROM O_PAYORDER PO ",
		"LEFT JOIN O_ORDER O ON PO.ORDERID = O.ORDERID ",
		"LEFT JOIN O_REFUNDORDER RO ON RO.ORDERID = PO.ORDERID ",
		"LEFT JOIN O_ORDER_MEMBER OM ON OM.ORDERID = PO.ORDERID ",
		"WHERE 1=1 AND PO.ORDERID = #{orderId}",
	"</script>"})
	PayOrderDetailVo findPayOrder(@Param("orderId")String orderId);
	
	/**
	 * 交易管理-业务已完成订单详情
	 * 
	 * @param orderId
	 * @return
	 */
	@Select({"<script>",
		"SELECT O.orderId,O.BEGINDATE AS orderDate,PO.BEGINDATE AS payDate,IFNULL(BO.BEGINDATE,IFNULL(OUTBIZ.CREATEDATE,PO.BEGINDATE)) AS bizDate,PO.TRANSACTIONNO AS channelSerialNo, ",
		"IFNULL(O.PRESCNO,BO.OUTBIZORDERID) AS hisSerialNo,O.cardNo,PO.configKey,PO.channelId,O.PRICE AS orderMoney,PO.PRICE AS payMoney,PO.payState, ",
		"O.serviceId,O.PRICENAME AS orderType,OM.MEMBERNAME AS nickName,OM.MOBILE AS nickMobile ",
		"FROM O_ORDER O ",
		"LEFT JOIN O_BIZORDER BO ON BO.ORDERID = O.ORDERID",
		"LEFT JOIN O_ORDER_OUT_BIZ OUTBIZ ON OUTBIZ.ORDERID = O.ORDERID",
		"LEFT JOIN O_PAYORDER PO ON PO.ORDERID = O.ORDERID ",
		"LEFT JOIN O_ORDER_MEMBER OM ON OM.ORDERID = O.ORDERID ",
		"WHERE 1=1 AND O.ORDERID = #{orderId}",
	"</script>"})
	PayOrderDetailVo findBizOrder(@Param("orderId")String orderId);
	
	/**
	 * 交易管理-退款订单详情
	 * 
	 * @param refundOrderId
	 * @return
	 */
	@Select({"<script>",
		"SELECT RO.refundOrderId,RO.orderId,RO.BEGINDATE AS refundBeginDate,RO.ENDDATE AS refundEndDate,RO.PAYSTATE AS refundState,RO.operatorName, ",
		"O.BEGINDATE AS orderDate,PO.BEGINDATE AS payDate,IFNULL(BO.BEGINDATE,OUTBIZ.CREATEDATE) AS bizDate,PO.TRANSACTIONNO AS channelSerialNo,IFNULL(RO.OUTREFUNDORDERID,IFNULL(O.PRESCNO,BO.OUTBIZORDERID)) AS hisSerialNo,RO.refundNo, ",
		"RO.OUTREFUNDORDERID AS hisRefundSerialNo,PO.configkey, PO.channelId, RO.CHANNELID AS refundChannelId, RO.refundPrice,O.PRICE AS orderMoney,O.PRICENAME AS orderType,",
		"O.cardNo,O.serviceId,PO.PRICE AS payMoney,OM.MEMBERNAME AS nickName,OM.MOBILE AS nickMobile",
		"FROM O_REFUNDORDER RO ",
		"LEFT JOIN O_ORDER O ON O.ORDERID = RO.ORDERID ",
		"LEFT JOIN O_BIZORDER BO ON BO.ORDERID = RO.ORDERID",
		"LEFT JOIN O_ORDER_OUT_BIZ OUTBIZ ON OUTBIZ.ORDERID = RO.ORDERID",
		"LEFT JOIN O_PAYORDER PO ON PO.ORDERID = RO.ORDERID ",
		"LEFT JOIN O_ORDER_MEMBER OM ON OM.ORDERID = RO.ORDERID ",
		"WHERE 1=1  AND RO.REFUNDORDERID = #{refundOrderId} LIMIT 1",
	"</script>"})
	RefundOrderDetailVo findRefundOrder(@Param("refundOrderId")String refundOrderId);
	
	@Select("SELECT RO.orderId, RO.refundOrderId, RO.refundNo, RO.refundPrice FROM O_REFUNDORDER RO WHERE RO.PAYSTATE=4 AND RO.REFUNDORDERID = #{refundOrderId}")
	RefundOrderDetailVo findRefundOrderByOrderNo(@Param("refundOrderId")String refundOrderId);
	
	/**
	 * 查询可退订单列表
	 * @param cardNo
	 * @param cardType
	 * @param hisMemberId
	 * @return
	 */
	@Select({"<script>",
		"SELECT O.*,P.TRANSACTIONNO,P.CONFIGKEY FROM O_ORDER O",
		"	LEFT JOIN O_PAYORDER P ON P.ORDERID = O.ORDERID",
		"	LEFT JOIN O_ORDER_MEMBER M ON M.ORDERID = O.ORDERID",
		"WHERE P.PAYSTATE = 2 ",
		"	AND NOT EXISTS ( SELECT 1 FROM O_REFUNDORDER R WHERE R.ORDERID = O.ORDERID  AND (R.PAYSTATE=4 OR R.PAYSTATE=3))",
		"	AND NOT EXISTS ( SELECT 1 FROM O_OVERORDER OV WHERE OV.ORDERID = O.ORDERID )",
		"	AND P.BEGINDATE > DATE_SUB(CURDATE(), INTERVAL #{limitDate} DAY)",
		"<if test=\"cardNo != null and cardNo != ''\">",
			"<![CDATA[  AND CARDNO= #{cardNo} ]]> ",
		"</if>",
		"<if test=\"cardType != null and cardType != ''\">",
			"<![CDATA[  AND CARDTYPE= #{cardType} ]]> ",
		"</if>",
		"<if test=\"hisMemberId != null and hisMemberId != ''\">",
			"<![CDATA[  AND M.HISMEMBERID= #{hisMemberId} ]]> ",
		"</if>",
		"<if test=\"serviceId != null and serviceId != ''\">",
			"<![CDATA[  AND O.SERVICEID= #{serviceId} ]]> ",
		"</if>",
		"<if test=\"transactionNo != null and transactionNo != ''\">",
			"<![CDATA[  AND P.TRANSACTIONNO= #{transactionNo} ]]> ",
		"</if>",
		"<if test=\"channelId != null and channelId != ''\">",
			"<![CDATA[  AND O.CHANNELID= #{channelId} ]]> ",
		"</if>",
		"ORDER BY P.ENDDATE DESC",
	"</script>"})
	List<OrderView> queryRefundableOrderList(Map<String,Object> map);

	/**
	 * 查询下载His账单需要的订单信息(本地)
	 * 
	 * @param orderId
	 * @param transationNo
	 * @return
	 */
	@Select({"<script>",
		"SELECT PO.orderId, O.serviceId, PO.PRICE AS payMoney, PO.transactionNo, O.TOTALPRICE AS orderMoney, ",
		  "O.channelId, O.channelName, O.orderMemo, O.priceName, O.channelId, O.channelName FROM O_PAYORDER PO",
			"LEFT JOIN O_ORDER O ON PO.ORDERID = O.ORDERID ",
			"<where>",
				"<if test=\"orderId != null and orderId != ''\">",
					"<![CDATA[  AND PO.ORDERID = #{orderId} ]]> ",
				"</if>",
				"<if test=\"transationNo != null and transationNo != ''\">",
					"<![CDATA[  AND PO.TRANSACTIONNO = #{transationNo} ]]> ",
				"</if>",
				"AND PO.PAYSTATE = 2",
			"</where>",
			" LIMIT 1",
	"</script>"})
	OrderVo findLocalOrder(@Param("orderId")String orderId, @Param("transationNo")String transationNo);
	
	/**
	 * 查询下载His账单需要的退款订单信息(本地)
	 * 
	 * @param transationNo
	 * @return
	 */
	@Select({"<script>",
		"SELECT RO.orderId, RO.refundOrderId, RO.refundPrice, RO.refundNo, RO.BEGINDATE AS refundDate, RO.outRefundorderId, PO.transactionNo,",
		" PO.PRICE AS payMoney,O.TOTALPRICE AS orderMoney, O.serviceId, O.orderMemo, O.priceName, O.channelId, O.channelName FROM O_REFUNDORDER RO ",
		"LEFT JOIN O_PAYORDER PO ON PO.ORDERID = RO.ORDERID ",
		"LEFT JOIN O_ORDER O ON RO.ORDERID = O.ORDERID ",
		"<where>",
			"<if test=\"orderId != null and orderId != ''\">",
				"<![CDATA[  AND RO.ORDERID = #{orderId} ]]> ",
			"</if>",
			"<if test=\"transationNo != null and transationNo != ''\">",
				"<![CDATA[  AND PO.TRANSACTIONNO = #{transationNo} ]]> ",
			"</if>",
			"<if test=\"refundOrderId != null and refundOrderId != ''\">",
				"<![CDATA[  AND RO.REFUNDORDERID = #{refundOrderId} ]]> ",
			"</if>",
			"AND RO.PAYSTATE = 4",
		"</where>",
	"</script>"})
	List<OrderVo> findLocalRefundOrder(@Param("orderId")String orderId, @Param("transationNo")String transationNo, @Param("refundOrderId")String refundOrderId);
	
	/**
	 * 查询收集的第三方订单信息
	 * 
	 * @param orderId
	 * @param transationNo
	 * @return
	 */
	@Select({"<script>",
		"SELECT DISTINCT OC.orderId,OC.PAYPRICE payMoney,OC.transactionNo,OC.TOTALPRICE AS orderMoney,OC.orderMemo,OC.priceName,OC.channelId,",
		  " OC.channelName, OC.BIZDATETIME AS hisbizDate,OC.PAYDATETIME AS payDate, OC.serviceId FROM O_ORDER_CHECK OC",
		   "<where>",
			    "<if test=\"orderId != null and orderId != ''\">",
					"<![CDATA[  AND OC.ORDERID = #{orderId} ]]> ",
				"</if>",
				"<if test=\"transationNo != null and transationNo != ''\">",
					"<![CDATA[  AND OC.TRANSACTIONNO= #{transationNo} ]]> ",
				"</if>",
			"</where>",
	"</script>"})
	List<OrderVo> findOrderCheck(@Param("orderId")String orderId, @Param("transationNo")String transationNo);
	
	/**
	 * 查询收集的第三方退款订单信息
	 * 
	 * @param orderId
	 * @param transationNo
	 * @return
	 */
	@Select({"<script>",
		"SELECT OC.orderId,OC.PAYPRICE payMoney,OC.transactionNo,OC.TOTALPRICE AS orderMoney,OC.orderMemo,OC.priceName,OC.channelId,",
		  " OC.refundOrderId,OC.refundPrice,OC.refundNo,OC.BIZDATETIME AS hisbizDate,OC.PAYDATETIME AS payDate FROM O_ORDER_CHECK OC",
		   "<where>",
			    "<if test=\"orderId != null and orderId != ''\">",
					"<![CDATA[  AND OC.ORDERID = #{orderId} ]]> ",
				"</if>",
				"<if test=\"transationNo != null and transationNo != ''\">",
					"<![CDATA[  AND OC.TRANSACTIONNO= #{transationNo} ]]> ",
				"</if>",
				"<if test=\"refundOrderId != null and refundOrderId != ''\">",
					"<![CDATA[  AND OC.REFUNDORDERID= #{refundOrderId} ]]> ",
				"</if>",
				" AND OC.ORDERTYPE = 2",
			"</where>",
	"</script>"})
	List<OrderVo> findRefundOrderCheck(@Param("orderId")String orderId, @Param("transationNo")String transationNo, @Param("refundOrderId")String refundOrderId);
	
	/**
	 * 本地全流程订单交易日志信息
	 * 
	 * @param orderId
	 * @return
	 */
	@Select({"<script>",
		"SELECT O.orderId, O.channelId, O.channelName, O.BEGINDATE AS transTime, O.PRICE AS orderMoney, PO.BEGINDATE AS payTime,",
		  " PO.PRICE AS payMoney, PO.configKey, IFNULL(BO.BEGINDATE, OUTBO.CREATEDATE) AS bizTime FROM O_ORDER O",
			"LEFT JOIN O_PAYORDER PO ON PO.ORDERID = O.ORDERID ",
			"LEFT JOIN O_BIZORDER BO ON BO.ORDERID = O.ORDERID ",
			"LEFT JOIN O_ORDER_OUT_BIZ OUTBO ON OUTBO.ORDERID = O.ORDERID ",
			"WHERE 1=1 AND (OUTBIZ.OUTBIZTYPE = 1 OR BO.BIZSTATE = 1 ) AND O.ORDERID = #{orderId} LIMIT 1",
	"</script>"})
	OrderTransLogVo findLocalTransLogInfo(@Param("orderId")String orderId);
	
	/**
	 * 第三方全流程订单交易日志信息
	 * 
	 * @param orderId
	 * @return
	 */
	@Select({"<script>",
		"SELECT O.orderId, O.channelId, O.channelName, O.ORDERDATETIME AS transTime, O.PRICE AS orderMoney, O.PAYDATETIME AS payTime,",
		  " O.PAYPRICE AS payMoney, O.configKey, O.BIZDATETIME AS bizTime FROM O_ORDER_CHECK O",
			"WHERE O.ORDERID = #{orderId} LIMIT 1",
	"</script>"})
	OrderTransLogVo findOutTransLogInfo(@Param("orderId")String orderId);
	
	/**
	 * 第三方全流程订单交易日志信息
	 * 
	 * @param orderId
	 * @return
	 */
	@Select({"<script>",
		"SELECT O.refundOrderId,O.refundNo,O.refundPrice,O.REFUNDDATETIME AS refundTime, O.refundChannelName FROM O_ORDER_CHECK O",
		"WHERE O.ORDERID = #{orderId} AND ORDERTYPE = 2",
	"</script>"})
	List<RefundOrder> findOutRefundOrderInfo(@Param("orderId")String orderId);
	
	@Select({"<script>",
		"SELECT DISTINCT O.CARDNO, O.serviceId, O.channelId, PO.BEGINDATE AS payDate, PO.PRICE AS payMoney, OM.MEMBERNAME AS nickName FROM O_ORDER O ",
		"LEFT JOIN O_PAYORDER PO ON PO.ORDERID = O.ORDERID ",
		"LEFT JOIN O_ORDER_MEMBER OM ON OM.ORDERID = O.ORDERID ",
		"WHERE O.ORDERID = #{orderId} LIMIT 1 ",
	"</script>"})
	OrderVo findCardNoForOrder(String orderId);
	
	@Select("SELECT DISTINCT OC.CARDNO, OC.serviceId, OC.channelId, OC.PAYDATETIME AS payDate, OC.PAYPRICE AS payMoney, '' AS nickName FROM O_ORDER_CHECK OC WHERE OC.ORDERID = #{orderId} LIMIT 1")
	OrderVo findCardNoForOrderCheck(String orderId);
	
}

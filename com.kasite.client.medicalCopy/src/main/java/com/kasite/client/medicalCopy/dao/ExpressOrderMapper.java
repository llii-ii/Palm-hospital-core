package com.kasite.client.medicalCopy.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.kasite.core.serviceinterface.module.medicalCopy.dbo.CopyContent;
import com.kasite.core.serviceinterface.module.medicalCopy.dbo.ExpressOrder;
import com.kasite.core.serviceinterface.module.medicalCopy.dto.ExpressOrderVo;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqExpressOrder;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespExpressOrder;
import com.kasite.core.serviceinterface.module.order.dbo.OrderView;

import tk.mybatis.mapper.common.Mapper;

public interface ExpressOrderMapper extends Mapper<ExpressOrder>{
	
	@Select("SELECT * FROM  TB_EXPRESS_ORDER WHERE caseIdAll LIKE CONCAT(CONCAT('%', #{caseId}), '%') ORDER BY applyTime DESC" )
	List<ExpressOrder> selectExpressOrderByCaseId(String caseId);

	
	@Select({"<script>",
		"SELECT *,mc.id AS mcId,CASE SUBSTR(eo.patientId,LENGTH(eo.patientId),1)+0 WHEN 0 THEN 10 ELSE SUBSTR(eo.patientId,LENGTH(eo.patientId),1)+0 END AS orderByNum ",
		"FROM TB_EXPRESS_ORDER eo ",
		"INNER JOIN TB_MCOPY_USER mu ON eo.patientId = mu.id ",
		"INNER JOIN TB_ORDER_CASE oc ON oc.orderId = eo.id ",
		"INNER JOIN TB_MCOPY_CASE mc ON mc.id = oc.caseId ",
		"<where>",
			"<if test=\"expressOrder.name!=null and expressOrder.name!=''\">",
				"AND mu.name like CONCAT('%',#{expressOrder.name},'%') ",
			"</if>",
			"<if test=\"expressOrder.id!=null and expressOrder.id!=''\">",
				"AND eo.id like CONCAT('%',#{expressOrder.id},'%') ",
			"</if>",
			"<if test=\"expressOrder.orderState!=null and expressOrder.orderState!=''\">",
				"AND eo.orderState = #{expressOrder.orderState} ",
			"</if>",
			
			"<if test=\"expressOrder.startTime!=null and expressOrder.startTime!='' \">",
				"AND eo.applyTime &gt;= #{expressOrder.startTime} ",
			"</if>",
			"<if test=\"expressOrder.endTime!=null and expressOrder.endTime!=''\">",
				"AND eo.applyTime &lt;= DATE_ADD(#{expressOrder.endTime},INTERVAL 1 DAY) ",
			"</if>",
			
			
//			"<if test=\"expressOrder.startTime!=null and expressOrder.startTime!='' and expressOrder.endTime!=null and expressOrder.endTime!=''\">",
//				"AND eo.applyTime between #{expressOrder.startTime} and DATE_ADD(#{expressOrder.endTime},INTERVAL 1 DAY) ",
//			"</if>",
//			"<if test=\"expressOrder.startTime!=null and expressOrder.startTime!=''\">",
//				"AND eo.applyTime between #{expressOrder.startTime} and DATE_ADD(#{expressOrder.startTime},INTERVAL 1 DAY) ",
//			"</if>",
			"<if test=\"expressOrder.patientId!=null and expressOrder.patientId!=''\">",
				"AND eo.patientId = #{expressOrder.patientId} ",
			"</if>",
			"<if test=\"expressOrder.outHosDate!=null and expressOrder.outHosDate!=''\">",
				"AND to_days(mc.outHosDate) = to_days(#{expressOrder.outHosDate})",
			"</if>",
//			"<if test=\"expressOrder.payState!=null and expressOrder.payState!=''\">",
//				"AND eo.payState = #{expressOrder.payState} ",
//			"</if>",
			"<if test=\"expressOrder.mcId!=null and expressOrder.mcId!=''\">",
				"AND mc.id = #{expressOrder.mcId} ",
			"</if>",
			"<if test=\"expressOrder.wxOrderId!=null and expressOrder.wxOrderId!=''\">",
				"AND eo.wxOrderId = #{expressOrder.wxOrderId} ",
			"</if>",
		"</where>",
		"ORDER BY YEAR(mc.outHosDate) DESC,MONTH(mc.outHosDate) DESC,orderByNum ASC,DAY(mc.outHosDate) DESC",
		"</script>"
	})
	List<ExpressOrderVo> selectExpressOrder(@Param("expressOrder")ReqExpressOrder expressOrder);
	
	
	
	@Select({"<script>",
		"SELECT * FROM TB_EXPRESS_ORDER eo ",
		"INNER JOIN TB_MCOPY_USER mu ON eo.patientId = mu.id ",
		"<where>",
			"<if test=\"expressOrder.applyOpenId!=null and expressOrder.applyOpenId!=''\">",
				"AND eo.applyOpenId = #{expressOrder.applyOpenId} ",
			"</if>",
			"<if test=\"expressOrder.orderState!=null and expressOrder.orderState!=''\">",
				"AND eo.orderState = #{expressOrder.orderState} ",
			"</if>",	
//			"<if test=\"expressOrder.startTime!=null and expressOrder.startTime!='' and expressOrder.endTime!='' and expressOrder.endTime!=''\">",
//				"AND eo.applyTime between #{expressOrder.startTime} and DATE_ADD(#{expressOrder.endTime},INTERVAL 1 DAY) ",
//			"</if>",
			"<if test=\"expressOrder.startTime!=null and expressOrder.startTime!=''\">",
				"AND eo.applyTime between #{expressOrder.startTime} and DATE_ADD(#{expressOrder.startTime},INTERVAL 1 DAY) ",
			"</if>",
			"<if test=\"expressOrder.channelId!=null and expressOrder.channelId!=''\">",
			"AND eo.channelId = #{expressOrder.channelId} ",
			"</if>",
			"<if test=\"expressOrder.receiveType!=null and expressOrder.receiveType!=''\">",
			"AND eo.receiveType = #{expressOrder.receiveType} ",
			"</if>",
			"<if test=\"expressOrder.applyRelationType!=null and expressOrder.applyRelationType!=''\">",
			"AND eo.applyRelationType = #{expressOrder.applyRelationType} ",
			"</if>",
			"<if test=\"expressOrder.fuzzyQuery!=null and expressOrder.fuzzyQuery!=''\">",
			"AND eo.id like '%#{expressOrder.fuzzyQuery}%' OR eo.name like '%#{expressOrder.fuzzyQuery}%' OR eo.patient like '%#{expressOrder.fuzzyQuery}%'",
			"</if>",
//			"<if test=\"expressOrder.payState!=null and expressOrder.payState!=''\">",
//				"AND eo.payState = #{expressOrder.payState} ",
//			"</if>",
		"</where>",
		"ORDER BY eo.applyTime DESC",
		"</script>"
	})
	List<ExpressOrderVo> selectOrderByWX(@Param("expressOrder")ReqExpressOrder expressOrder);
	
	
	
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
		"	WHERE SERVICEID='010' AND ORDERID=#{orderId} ",
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
	OrderView getPayState(@Param("orderId")String orderId);
	
	/**
	 * 统计订单充值及退款金额
	 * 
	 * @param orderIds
	 */
	@Select({"<script>",
		"SELECT IFNULL(sum(t2.PRICE),0) PAYPRICE,IFNULL(sum(t3.TOTALPRICE),0) REFUNDPRICE",
		"FROM O_ORDER t1",
		"LEFT JOIN O_PAYORDER t2 ON t1.ORDERID = t2.ORDERID AND t2.PAYSTATE = 2",
		"LEFT JOIN O_REFUNDORDER t3 ON t1.ORDERID = t3.ORDERID AND t3.PAYSTATE = 4",
		"<where>",
			"<if test=\"orderIds != null and orderIds.length>0\">",
				" t1.ORDERID IN ",
				"<foreach collection=\"orderIds\" open=\"(\" separator=\",\" close=\")\" item=\"orderId\">",
					"<![CDATA[(#{orderId}) ]]>",
				"</foreach>",	
			"</if>",
		"</where>",
		"</script>"})
	Map<String, Object> countPriceByOrderIds(@Param("orderIds")String[] orderIds);
}

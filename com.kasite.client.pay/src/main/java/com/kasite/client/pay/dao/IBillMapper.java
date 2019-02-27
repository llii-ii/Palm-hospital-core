package com.kasite.client.pay.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.client.pay.bean.dbo.Bill;
import com.kasite.core.serviceinterface.module.his.resp.HisBill;
import com.kasite.core.serviceinterface.module.pay.dto.BillCountByDateVo;
import com.kasite.core.serviceinterface.module.pay.dto.BillVo;
import com.kasite.core.serviceinterface.module.pay.dto.ChannelOfConfigKeyVo;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @className: IBillMapper
 * @author: lcz
 * @date: 2018年7月26日 下午8:05:02
 */
public interface IBillMapper extends Mapper<Bill>{
	
	@Select({"<script>",
		"SELECT @a FROM P_BILL A LEFT JOIN O_ORDER B ON A.ORDERID=B.ORDERID",
		"<where>",
			"<if test=\"serviceId!=null and serviceId!=''\">",
				"  AND B.SERVICEID=#{serviceId} ",
			"</if>",
			"<if test=\"transDate!=null and transDate!=''\">",
			"  AND DATE_FORMAT(A.TRANSDATE,'%Y%M%D')=#{transDate} ",
			"</if>",
		"</where>",
	"</script>"})
	List<Bill> findBillList(@Param("serviceId")String serviceId,@Param("transDate")String transDate);
	
	@Select({"<script>",
		"SELECT HB.* FROM P_HIS_BILL HB LEFT JOIN O_ORDER O ON O.ORDERID = HB.ORDERID",
		"<where>",
			"<if test=\"startDate!=null and startDate!='' and endDate!=null and endDate!=''\">",
				" AND HB.TRANSDATE BETWEEN #{startDate} AND #{endDate}",
			"</if>" ,
			"<if test=\"channelId!=null and channelId!=''\">",
			"  AND O.CHANNELID=#{channelId} ",
			"</if>",
		"</where>",
	"</script>"})
	List<HisBill> queryHisBillList(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("channelId")String channelId);
	
	@Select("SELECT HB.orderId FROM P_HIS_BILL HB WHERE HB.ORDERID = #{orderId} AND HB.HISORDERTYPE = ${orderType}")
	HisBill findHisBillByOrderId(@Param("orderId")String orderId,@Param("orderType")Integer orderType);
	
	/**
	 * 统计支付金额
	 * 
	 * @param startDate
	 * @param endDate
	 * @param configkey
	 * @return
	 */
	@Select({"<script>",
		"SELECT SUM(B.transactions) AS merchPayPrice FROM P_BILL B ",
		"<where>",
			"<if test=\"startDate!=null and startDate!='' and endDate!=null and endDate!=''\">",
				" AND B.TRANSDATE BETWEEN #{startDate} AND #{endDate}",
			"</if>" ,
			" AND B.CONFIGKEY = #{configkey}",
			" AND B.ORDERTYPE = 1",
		"</where>",
	"</script>"})
	Long findCountPayMoney(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("configkey")String configkey);
	
	/**
	 * 统计退款金额
	 * 
	 * @param startDate
	 * @param endDate
	 * @param configkey
	 * @return
	 */
	@Select({"<script>",
		"SELECT SUM(B.refundprice) AS merchRefundPrice FROM P_BILL B ",
		"<where>",
			"<if test=\"startDate!=null and startDate!='' and endDate!=null and endDate!=''\">",
				" AND B.TRANSDATE BETWEEN #{startDate} AND #{endDate}",
			"</if>" ,
			" AND B.CONFIGKEY = #{configkey}",
			" AND B.ORDERTYPE = 2",
		"</where>",
	"</script>"})
	Long findCountRefundMoney(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("configkey")String configkey);
	
	/**
	 * 获取所有的configkey
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Select({"<script>",
		"SELECT DISTINCT B.configkey FROM P_BILL B ",
		"<where>",
			"<if test=\"startDate!=null and startDate!='' and endDate!=null and endDate!=''\">",
				" AND B.TRANSDATE BETWEEN #{startDate} AND #{endDate}",
			"</if>" ,
		"</where>",
	"</script>"})
	List<String> findAllConfigkeyByDate(@Param("startDate")String startDate, @Param("endDate")String endDate);
	
	/**
	 * 获取支付和退款订单的日统计金额
	 * 
	 * @param date
	 * @param configkeyList
	 * @return
	 */
	@Select({"<script>",
		"SELECT B.configkey, (SUM(CASE ORDERTYPE WHEN 1 THEN TRANSACTIONS ELSE  0 END) - SUM(CASE ORDERTYPE WHEN 2 THEN REFUNDPRICE ELSE 0 END)) diffPrice FROM P_BILL B ",
		"<where>",
			" AND DATE_FORMAT(TRANSDATE,'%Y%m%d') = DATE_FORMAT(#{date},'%Y%m%d')",
			"<if test=\"configkeyList != null and configkeyList.size>0\">",
				" AND B.CONFIGKEY IN ",
				"<foreach collection=\"configkeyList\" open=\"(\" separator=\",\" close=\")\" item=\"configkey\">",
					"<![CDATA[(#{configkey}) ]]>",
				"</foreach>",	
			"</if>",
		"</where>",
		"GROUP BY B.CONFIGKEY",
	"</script>"})
	List<BillVo> findPayWayCountMoney(@Param("date")String date, @Param("configkeyList")List<String> configkeyList);
	
	/**
	 * 获取支付和退款订单的日统计交易数
	 * 
	 * @param date
	 * @param configkeyList
	 * @return
	 */
	@Select({"<script>",
		"SELECT B.configkey, COUNT(TRANSACTIONS) billCount FROM P_BILL B ",
		"<where>",
			" AND DATE_FORMAT(TRANSDATE,'%Y%m%d') = DATE_FORMAT(#{date},'%Y%m%d') AND ORDERTYPE = 1",
			"<if test=\"configkeyList != null and configkeyList.size>0\">",
				" AND B.CONFIGKEY IN ",
				"<foreach collection=\"configkeyList\" open=\"(\" separator=\",\" close=\")\" item=\"configkey\">",
					"<![CDATA[(#{configkey}) ]]>",
				"</foreach>",	
			"</if>",
		"</where>",
		"GROUP BY B.CONFIGKEY",
	"</script>"})
	List<BillVo> findPayWayCount(@Param("date")String date, @Param("configkeyList")List<String> configkeyList);
	
	@Select("SELECT PO.CONFIGKEY FROM O_PAYORDER PO WHERE PO.ORDERID = #{orderId}")
	String findConfigkeyByOrderId(String orderId);
	
	@Select("SELECT O.SERVICEID FROM O_ORDER O WHERE O.ORDERID = #{orderId}")
	String findServiceIdByOrderId(String orderId);
	
	@Select("SELECT DISTINCT PO.TRANSACTIONNO FROM O_PAYORDER PO WHERE PO.ORDERID = #{orderId}")
	String findTransactionNoByOrderId(String orderId);

	@Select("SELECT DISTINCT OC.TRANSACTIONNO FROM O_ORDER_CHECK OC WHERE OC.ORDERID = #{orderId}")
	String findTransactionNoForOrderCheck(String orderId);
	
	@Select("SELECT R.REFUNDORDERID FROM O_REFUNDORDER R WHERE R.ORDERID = #{orderId} AND R.REFUNDNO = #{refundId}")
	String findRefundOrderId(String refundId,String orderId);
	
	@Select("SELECT DISTINCT A.CHANNELID FROM O_ORDER A WHERE A.ORDERID = #{orderId} LIMIT 1")
	String findChannelByOrderId(String orderId);
	
	@Select("SELECT DISTINCT A.CHANNELID FROM O_ORDER_CHECK A WHERE A.ORDERID = #{orderId} LIMIT 1")
	String findChannelForOrderCheck(String orderId);
	
	@Select({"<script>",
		"SELECT IFNULL(B.ORDERID,HB.ORDERID) AS orderId,B.merchNo,B.refundMerchNo,IFNULL(B.REFUNDORDERID,HB.REFUNDORDERID) AS refundOrderId,",
			"HB.HISORDERTYPE AS orderType,B.TRANSDATE AS payTransDate,IFNULL(B.TRANSDATE,HB.TRANSDATE) AS transDate,HB.channelId,HB.channelName,B.configkey,B.TRANSACTIONS AS merchPayPrice,",
			"B.REFUNDPRICE AS merchRefundPrice,HB.PAYMONEY AS hisPayPrice,HB.REFUNDMONEY AS hisRefundMoney,HB.TOTALMONEY AS hisTotalMoney,",
			"HB.hisOrderId,HB.hisBizState,HB.TRANSDATE AS hisBizDate",
		"FROM",
			"P_HIS_BILL HB",
		"LEFT JOIN P_BILL B ON B.ORDERID = HB.ORDERID AND B.ORDERTYPE = HB.HISORDERTYPE",
		"WHERE HB.TRANSDATE BETWEEN #{startDate} AND #{endDate} AND HB.HISORDERTYPE = 1",
	"</script>"})
	List<BillVo> findPayHisBillList(@Param("startDate")String startDate, @Param("endDate")String endDate);
	
	@Select({"<script>",
		"SELECT B.orderId,B.merchNo,B.refundMerchNo,B.refundOrderId,B.orderType,B.TRANSDATE AS payTransDate,B.transDate,B.channelId,B.channelName,B.configkey,",
			"B.REFUNDPRICE AS merchRefundPrice,B.TRANSACTIONS AS merchPayPrice,HB.hisOrderId,HB.PAYMONEY AS hisPayPrice,",
			"HB.TOTALMONEY AS hisTotalMoney,HB.REFUNDMONEY AS hisRefundMoney, HB.HISBIZSTATE AS hisBizState,HB.TRANSDATE AS hisBizDate ",
		"FROM",
			"P_BILL B",
		"LEFT JOIN P_HIS_BILL HB ON B.ORDERID = HB.ORDERID AND B.ORDERTYPE = HB.HISORDERTYPE",
		"WHERE B.TRANSDATE BETWEEN #{startDate} AND #{endDate} AND B.ORDERTYPE = 1",
	"</script>"})
	List<BillVo> findPayBillList(@Param("startDate")String startDate, @Param("endDate")String endDate);
	
	@Select({"<script>",
		"SELECT IFNULL(B.ORDERID,HB.ORDERID) AS orderId,B.merchNo,B.refundMerchNo,IFNULL(B.REFUNDORDERID,HB.REFUNDORDERID) AS refundOrderId,",
			"HB.HISORDERTYPE AS orderType,B.TRANSDATE AS payTransDate,IFNULL(B.TRANSDATE,HB.TRANSDATE) AS transDate,HB.channelId,HB.channelName,B.configkey,B.TRANSACTIONS AS merchPayPrice,",
			"B.REFUNDPRICE AS merchRefundPrice,HB.PAYMONEY AS hisPayPrice,HB.REFUNDMONEY AS hisRefundMoney,HB.TOTALMONEY AS hisTotalMoney,",
			"HB.hisOrderId,HB.hisBizState,HB.TRANSDATE AS hisBizDate",
		"FROM",
			"P_HIS_BILL HB",
		"LEFT JOIN P_BILL B ON B.REFUNDORDERID = HB.REFUNDORDERID AND B.ORDERTYPE = HB.HISORDERTYPE",
		"WHERE HB.TRANSDATE BETWEEN #{startDate} AND #{endDate} AND HB.HISORDERTYPE = 2",
	"</script>"})
	List<BillVo> findRefundHisBillList(@Param("startDate")String startDate, @Param("endDate")String endDate);
	
	@Select({"<script>",
		"SELECT B.orderId,B.merchNo,B.refundMerchNo,B.refundOrderId,B.orderType,B.TRANSDATE AS payTransDate,B.transDate,B.channelId,B.channelName,B.configkey,",
			"B.REFUNDPRICE AS merchRefundPrice,B.TRANSACTIONS AS merchPayPrice,HB.hisOrderId,HB.PAYMONEY AS hisPayPrice,",
			"HB.TOTALMONEY AS hisTotalMoney,HB.REFUNDMONEY AS hisRefundMoney, HB.HISBIZSTATE AS hisBizState,HB.TRANSDATE AS hisBizDate ",
		"FROM",
			"P_BILL B",
		"LEFT JOIN P_HIS_BILL HB ON B.REFUNDORDERID = HB.REFUNDORDERID AND B.ORDERTYPE = HB.HISORDERTYPE ",
		"WHERE B.TRANSDATE BETWEEN #{startDate} AND #{endDate} AND B.ORDERTYPE = 2",
	"</script>"})
	List<BillVo> findRefundBillList(@Param("startDate")String startDate, @Param("endDate")String endDate);
	
	/**
	 * 渠道下的商户账单金额统计
	 * 
	 * @param startDate
	 * @param endDate
	 * @param orderType
	 * @return
	 */
	@Select({"<script>",
		"SELECT BC.channelId, BC.channelName, BC.configkey, SUM(IFNULL(BC.MERCHPRICE,0)) AS paideMoney, SUM(IFNULL(BC.HISPRICE,0)) AS payableMoney, ",
		"BC.serviceId, SUM((IFNULL(BC.MERCHPRICE,0))-(IFNULL(BC.HISPRICE,0))) AS diffMoney FROM P_BILL_CHECK BC ",
		"WHERE BC.TRANSDATE BETWEEN #{startDate} AND #{endDate} AND BC.BILLTYPE = #{orderType} ",
		"GROUP BY BC.CHANNELID, BC.CHANNELNAME, BC.CONFIGKEY, BC.SERVICEID",
	"</script>"})
	List<ChannelOfConfigKeyVo> findMoneyChannelOfConfigkey(@Param("startDate")String startDate, 
			@Param("endDate")String endDate, @Param("orderType")Integer orderType);
	
	/**
	 * 根据渠道和商户号查询是否账平
	 * 
	 * @param startDate
	 * @param endDate
	 * @param orderType
	 * @return
	 */
	@Select({"<script>",
		"SELECT DISTINCT BC.CHECKSTATE FROM P_BILL_CHECK BC ",
		"WHERE BC.TRANSDATE BETWEEN #{startDate} AND #{endDate} ",
		"AND BC.SERVICEID = #{serviceId} AND BC.CHANNELID = #{channelId} AND BC.CONFIGKEY = #{configKey} ",
	"</script>"})
	List<Integer> findBillCheck0(@Param("startDate")String startDate, @Param("endDate")String endDate, 
			@Param("serviceId")String serviceId, @Param("channelId")String channelId, @Param("configKey")String configKey);
	
	@Select({"<script>",
		"SELECT channelId, SUM(TRANSACTIONS) AS moneySum, COUNT(BILLID) AS count FROM P_BILL B",
		" WHERE B.TRANSDATE BETWEEN str_to_date(DATE_FORMAT(#{queryDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s') ",
		" AND DATE_ADD(str_to_date(DATE_FORMAT(#{queryDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s'),INTERVAL 1 DAY) AND ORDERTYPE=1 ",
		" GROUP BY B.CHANNELID",
	"</script>"})
	List<BillCountByDateVo> findBillPayCountByDate(@Param("queryDate")String queryDate);
	
	@Select({"<script>",
		"SELECT channelId, SUM(REFUNDPRICE) AS moneySum, COUNT(BILLID) AS count FROM P_BILL B",
		" WHERE B.TRANSDATE BETWEEN str_to_date(DATE_FORMAT(#{queryDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s') ",
		" AND DATE_ADD(str_to_date(DATE_FORMAT(#{queryDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s'),INTERVAL 1 DAY) AND ORDERTYPE=2 ",
		" GROUP BY B.CHANNELID",
	"</script>"})
	List<BillCountByDateVo> findBillCountRefundByDate(@Param("queryDate")String queryDate);
}

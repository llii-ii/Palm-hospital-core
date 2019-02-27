package com.kasite.client.business.module.backstage.bill.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.core.common.mappers.CommonMapper;
import com.kasite.core.serviceinterface.module.pay.dbo.BillChannelRF;
import com.kasite.core.serviceinterface.module.pay.dto.BillRFVo;

public interface BillChannelRFDao extends CommonMapper<BillChannelRF> {
	
	/**
	 * 查询对账明细列表数据(日对账)
	 * 
	 * @param startDate
	 * @param endDate
	 */
	@Select({"<script>",
		"SELECT BCR.DATE AS billDate,SUM(BCR.HISBILLCOUNT) AS hisBillCount,SUM(BCR.CHANNELBILLCOUNT) AS channelBillCount,SUM(BCR.CHECKCOUNT) AS checkCount, ",
			"SUM(BCR.HISSINGLEBILLCOUNT) AS hisSingleBillCount,SUM(BCR.CHANNELSINGLEBILLCOUNT) AS channelSingleBillCount,",
			"SUM(BCR.DIFFERPIRCECOUNT) AS differPirceCount,SUM(BCR.HISBILLSUM) AS hisBillMoneySum,SUM(BCR.MERCHBILLSUM) AS merchBillMoneySum ",
		"FROM P_BILL_CHANNEL_RF BCR ",
		"<where>",
			"<if test=\"startDate!=null and startDate!='' and endDate!=null and endDate!=''\">",
				" AND BCR.DATE BETWEEN #{startDate} AND #{endDate}",
			"</if>" ,
		"</where>",
		" GROUP BY BCR.DATE ORDER BY BCR.DATE DESC",
		"</script>"})
	List<BillRFVo> findBillRFListForDate(@Param("startDate")String startDate, @Param("endDate")String endDate);
	
	/**
	 * 查询对账明细列表数据(月对账)
	 * 
	 * @param startDate
	 * @param endDate
	 */
	@Select({"<script>",
		"SELECT DATE_FORMAT(BCR.DATE,'%Y-%m') AS billDate,SUM(BCR.HISBILLCOUNT) AS hisBillCount,SUM(BCR.CHANNELBILLCOUNT) AS channelBillCount,SUM(BCR.CHECKCOUNT) AS checkCount, ",
			"SUM(BCR.HISSINGLEBILLCOUNT) AS hisSingleBillCount,SUM(BCR.CHANNELSINGLEBILLCOUNT) AS channelSingleBillCount,",
			"SUM(BCR.DIFFERPIRCECOUNT) AS differPirceCount,SUM(BCR.HISBILLSUM) AS hisBillMoneySum,SUM(BCR.MERCHBILLSUM) AS merchBillMoneySum ",
			"FROM P_BILL_CHANNEL_RF BCR ",
		"<where>",
			"<if test=\"startDate!=null and startDate!='' and endDate!=null and endDate!=''\">",
				" AND DATE_FORMAT(BCR.DATE,'%Y-%m') BETWEEN #{startDate} AND #{endDate} " ,
			"</if>" ,
		"</where>",
		" GROUP BY DATE_FORMAT(BCR.DATE, '%Y-%m'), MONTH (BCR.DATE) ORDER BY MONTH (BCR.DATE) DESC",
		"</script>"})
	List<BillRFVo> findBillRFListForMonth(@Param("startDate")String startDate, @Param("endDate")String endDate);
	
	/**
	 * 是否处置后账平(日)
	 * @param queryDate
	 * @return
	 */
	@Select("SELECT COUNT(BCR.DATE) FROM P_BILL_CHANNEL_RF BCR WHERE BCR.DATE = #{queryDate} AND BCR.DEALSTATE = 1 ")
	int findBillCountIsDealOkForDate(@Param("queryDate")String queryDate);
	
	/**
	 * 是否处置后账平(月)
	 * @param queryDate
	 * @return
	 */
	@Select("SELECT COUNT(BCR.DATE) FROM P_BILL_CHANNEL_RF BCR WHERE DATE_FORMAT(BCR.DATE,'%Y-%m') = #{queryDate} AND BCR.DEALSTATE = 1")
	int findBillCountIsDealOkForMonth(@Param("queryDate")String queryDate);
	
	/**
	 * 是否账平(日)
	 * @param queryDate
	 * @return
	 */
	@Select("SELECT COUNT(BCR.DATE) FROM P_BILL_CHANNEL_RF BCR WHERE BCR.DATE = #{queryDate} AND BCR.CHECKSTATE = 0")
	int findBillCountIsOkForDate(@Param("queryDate")String queryDate);
	
	/**
	 * 是否账平(月)
	 * @param queryDate
	 * @return
	 */
	@Select("SELECT COUNT(BCR.DATE) FROM P_BILL_CHANNEL_RF BCR WHERE DATE_FORMAT(BCR.DATE,'%Y-%m') = #{queryDate} AND BCR.CHECKSTATE = 0")
	int findBillCountIsOkForMonth(@Param("queryDate")String queryDate);
	
	@Select("SELECT BCR.DATE FROM P_BILL_CHANNEL_RF BCR ORDER BY BCR.DATE DESC LIMIT 1")
	String findCurrentDate();
}

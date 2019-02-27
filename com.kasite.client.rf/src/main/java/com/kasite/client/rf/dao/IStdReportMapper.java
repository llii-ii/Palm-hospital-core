package com.kasite.client.rf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.client.rf.bean.dto.StdReporDate;
import com.kasite.core.serviceinterface.module.rf.dto.MapVo;
import com.kasite.core.serviceinterface.module.rf.dto.StdReportVo;

import tk.mybatis.mapper.common.Mapper;

public interface IStdReportMapper extends Mapper<StdReporDate> {

	/**
	 * 昨日交易笔数
	 * 
	 * @param type
	 * @param queryDate
	 * @return
	 */
	@Select({"<script>",
		"SELECT SUM(DATAVALUE) TOTAL FROM RF_STD_REPORTDATE ",
		"<where>",
			"<if test=\"type==0\">",
				"<![CDATA[ AND TO_DAYS( #{queryDate} )-TO_DAYS(SUMDATE)=1 ]]> AND DATAPARENTTYPE=1",
			"</if>",
			"<if test=\"type==1\">",
				"<![CDATA[ AND TO_DAYS(#{queryDate})-TO_DAYS(SUMDATE)=1 ]]> AND DATAPARENTTYPE=2",
			"</if>",
		"</where>",
	"</script>"})
	Double findTotalYesterday(@Param("type")Integer type, @Param("queryDate")String queryDate);
	
	/**
	 * 昨日交易总金额
	 * 
	 * @param type
	 * @param queryDate
	 * @return
	 */
	@Select({"<script>",
		"SELECT SUM(DATAVALUE) TOTAL FROM RF_STD_REPORTDATE ",
		"<where>",
			"<if test=\"type == 1\">",
				"<![CDATA[ AND TO_DAYS(#{queryDate})-TO_DAYS(SUMDATE)=1 ]]> AND DATATYPE=1",
			"</if>",
			"<if test=\"type == 2\">",
				"<![CDATA[ AND TO_DAYS(#{queryDate})-TO_DAYS(SUMDATE)=1 ]]> AND DATATYPE=2",
			"</if>",
		"</where>",
		"AND DATAPARENTTYPE = 1",
	"</script>"})
	Double findMoneyYesterday(@Param("type")Integer type, @Param("queryDate")String queryDate);
	
	/**
	 * 交易笔数(日)
	 * 
	 * @param type
	 * @param queryDate
	 * @return
	 */
	@Select({"<script>",
		"SELECT DATE_FORMAT(SUMDATE, '%Y%m%d') queryDate, SUM(DATAVALUE) total FROM RF_REPORTDATE ",
		"<where>",
			" AND DATE_FORMAT(SUMDATE, '%Y%m%d') &lt;= DATE_FORMAT(#{queryDate}, '%Y%m%d') AND DATE_SUB(#{queryDate}, INTERVAL 7 DAY) &lt;= SUMDATE",
			"<if test='type!=\"0\"'>",
				" AND DATATYPE = 5 <![CDATA[ AND CHANNELID = #{type} ]]>",
			"</if>",
		"</where>",
		" GROUP BY DATE_FORMAT( SUMDATE, '%Y%m%d' ) ORDER BY DATE_FORMAT( SUMDATE, '%Y%m%d' ) ASC",
	"</script>"})
	List<StdReportVo> findTrendForDate(@Param("type")String type, @Param("queryDate")String queryDate);
	
	/**
	 * 交易金额(日)
	 * 
	 * @param type
	 * @param queryDate
	 * @return
	 */
	@Select({"<script>",
		"SELECT DATE_FORMAT(SUMDATE, '%Y%m%d') queryDate, SUM(DATAVALUE) total FROM RF_REPORTDATE ",
		"<where>",
			" AND DATE_FORMAT(SUMDATE, '%Y%m%d') &lt;= DATE_FORMAT(#{queryDate}, '%Y%m%d') AND DATE_SUB(#{queryDate}, INTERVAL 7 DAY) &lt;= SUMDATE",
			"<if test='type!=\"0\"'>",
				" AND DATATYPE = 6 <![CDATA[ AND CHANNELID = #{type} ]]>",
			"</if>",
		"</where>",
		" GROUP BY DATE_FORMAT( SUMDATE, '%Y%m%d' ) ORDER BY DATE_FORMAT( SUMDATE, '%Y%m%d' ) ASC",
	"</script>"})
	List<StdReportVo> findMoneyYesterdayForDate(@Param("type")String type, @Param("queryDate")String queryDate);
	
	/**
	 * 交易笔数(月)
	 * 
	 * @param type
	 * @param queryDate
	 * @return
	 */
	@Select({"<script>",
		"SELECT DATE_FORMAT(SUMDATE, '%Y%m') queryDate, SUM(DATAVALUE) total FROM RF_REPORTDATE ",
		"<where>",
			" AND DATE_FORMAT(SUMDATE, '%Y%m') &lt;= DATE_FORMAT(#{queryDate}, '%Y%m') AND DATE_SUB(#{queryDate}, INTERVAL 7 MONTH) &lt;= SUMDATE",
			"<if test='type!=\"0\"'>",
				" AND DATATYPE = 5 <![CDATA[ AND CHANNELID = #{type} ]]>",
			"</if>",
		"</where>",
		" GROUP BY DATE_FORMAT( SUMDATE, '%Y%m' ) ORDER BY DATE_FORMAT( SUMDATE, '%Y%m' ) ASC",
	"</script>"})
	List<StdReportVo> findTrendForMonth(@Param("type")String type, @Param("queryDate")String queryDate);
	
	/**
	 * 交易金额(月)
	 * 
	 * @param type
	 * @param queryDate
	 * @return
	 */
	@Select({"<script>",
		"SELECT DATE_FORMAT(SUMDATE, '%Y%m') queryDate, SUM(DATAVALUE) total FROM RF_REPORTDATE ",
		"<where>",
			" AND DATE_FORMAT(SUMDATE, '%Y%m') &lt;= DATE_FORMAT(#{queryDate}, '%Y%m') AND DATE_SUB(#{queryDate}, INTERVAL 7 MONTH) &lt;= SUMDATE",
			" AND DATATYPE = 6",
			"<if test='type!=\"0\"'>",
				" <![CDATA[ AND CHANNELID = #{type} ]]>",
			"</if>",
		"</where>",
		" GROUP BY DATE_FORMAT( SUMDATE, '%Y%m' ) ORDER BY DATE_FORMAT( SUMDATE, '%Y%m' ) ASC",
	"</script>"})
	List<StdReportVo> findMoneyYesterdayForMonth(@Param("type")String type, @Param("queryDate")String queryDate);
	
	@Select("SELECT rr.CHANNELNAME name, SUM(DATAVALUE) value FROM RF_REPORTDATE rr WHERE DATATYPE = 5 AND SUMDATE>DATE_SUB(CURDATE(), INTERVAL 1 YEAR) GROUP BY rr.CHANNELNAME ")
	List<MapVo> findChannelCountYear();
	
	@Select("SELECT DATATYPE name, SUM(DATAVALUE) value FROM RF_STD_REPORTDATE WHERE DATAPARENTTYPE = 2 GROUP BY DATATYPE ")
	List<MapVo> findPayCountYear();
	
}

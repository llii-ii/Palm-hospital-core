package com.kasite.client.rf.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.kasite.client.rf.bean.dto.ReportDate;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @className: IReportDateMapper
 * @author: lcz
 * @date: 2018年7月31日 下午3:00:57
 */
public interface IReportDateMapper extends Mapper<ReportDate>{
	
//	@Update("UPDATE RF_REPORTDATE SET DATAVALUE=DATAVALUE+#{dataValue} where CHANNELID=#{channelId} and  DATATYPE=#{dataType} and SUMDATE=#{sumDate} ")
//	int updateReportDate(@Param("dataValue")String dataValue,@Param("channelId")String channelId,@Param("dataType")int dataType,@Param("sumDate")String sumDate);
	
	@Update("INSERT INTO RF_REPORTDATE (CHANNELID,CHANNELNAME,DATATYPE,DATAVALUE,SUMDATE) "
			+ "VALUES (#{reportDate.channelId},#{reportDate.channelName},#{reportDate.dataType},#{reportDate.dataValue},#{reportDate.sumDate}) "
			+ "ON DUPLICATE KEY UPDATE DATAVALUE=DATAVALUE+#{reportDate.dataValue}")
	int insertOrUpdateReportDate(@Param("reportDate")ReportDate reportDate);
	  
	  @Select({"<script>",
		  "SELECT DATATYPE,SUM(DATAVALUE) AS DATAVALUE FROM RF_REPORTDATE ",
		  "<where>",
		  	"<if test=\"sumDate!=null and sumDate!=''\">",
		  		" AND SUMDATE = #{sumDate}",
		  	"</if>",
		  "</where>",
		  "GROUP BY DATATYPE",
		  "</script>"})
	  List<Map<String,Object>> getDCSummary(@Param("sumDate")String sumDate);
	  
	  @Select({"<script>",
		  "SELECT DATE_FORMAT(SUMDATE,'%Y-%m-%d') AS OPERTIME,",
          " SUM(IF(DATATYPE=1, DATAVALUE,0)) AS TYPE1, ",
          " SUM(IF(DATATYPE=2, DATAVALUE,0)) AS TYPE2, ",
          " SUM(IF(DATATYPE=3, DATAVALUE,0)) AS TYPE3, ",
          " SUM(IF(DATATYPE=4, DATAVALUE,0)) AS TYPE4, ",
          " SUM(IF(DATATYPE=5, DATAVALUE,0)) AS TYPE5, ",
          " SUM(IF(DATATYPE=6, DATAVALUE,0)) AS TYPE6 ",
          " FROM RF_REPORTDATE WHERE ",
          "<![CDATA[ SUMDATE >= #{startDate} and SUMDATE <= #{endDate} ]]>",
          " GROUP BY DATE_FORMAT(SUMDATE,'%Y-%m-%d')",
          " ORDER BY DATE_FORMAT(SUMDATE,'%Y-%m-%d') DESC",
		  "</script>"})
	  List<Map<String,Object>> getDataCollectionGrid(@Param("startDate")String startDate,@Param("endDate")String endDate);
	  
	  @Select({"<script>",
         " SELECT SUMDATE AS OPERTIME,",
         " IF(DATATYPE = 6,SUM(DATAVALUE)/100,SUM(DATAVALUE)) AS DATAVALUE ",
         " FROM RF_REPORTDATE WHERE ",
         " SUMDATE BETWEEN #{startDate} AND #{endDate} ",
         " AND DATATYPE = #{dataType} ",
         " GROUP BY SUMDATE  ORDER BY SUMDATE ",
      "</script>"})
      List<Map<String,Object>> getDCLineAll(Map<String,Object> map);
	  
      @Select({"<script>",
          " SELECT SUMDATE AS OPERTIME,CHANNELID ",
          " FROM RF_REPORTDATE WHERE ",
          " SUMDATE BETWEEN #{startDate} AND #{endDate} ",
          " AND DATATYPE = #{dataType}",
          " GROUP BY SUMDATE,CHANNELID ",
      "</script>"})
      List<Map<String,Object>> getDCLineChannelList(Map<String,Object> map);
      
      @Select({"<script>",
          " SELECT SUMDATE AS OPERTIME,",
          "    ${selectColumn}",
          " FROM RF_REPORTDATE WHERE ",
          " SUMDATE BETWEEN #{startDate} AND #{endDate} ",
          " AND DATATYPE = #{dataType}",
          " GROUP BY SUMDATE ",
          " ORDER BY SUMDATE ",
      "</script>"})
      List<Map<String,Object>> getDCLineForChannel(Map<String,Object> map);

      @Select({"<script>",
          " SELECT CHANNELID,  SUM(DATAVALUE) AS DATAVALUE ",
          " FROM RF_REPORTDATE WHERE ",
          " SUMDATE BETWEEN #{startDate} AND #{endDate} ",
          " AND DATATYPE=#{dataType}  GROUP BY CHANNELID",
          "</script>"})
        List<Map<String,Object>> getDCbar(Map<String,Object> map);

}

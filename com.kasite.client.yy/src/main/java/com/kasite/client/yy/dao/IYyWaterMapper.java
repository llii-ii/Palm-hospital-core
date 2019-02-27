package com.kasite.client.yy.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.client.yy.bean.dbo.YyHistoryDoctor;
import com.kasite.client.yy.bean.dbo.YyWater;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @className: IWaterMapper
 * @author: lcz
 * @date: 2018年7月21日 上午10:22:29
 */
public interface IYyWaterMapper extends Mapper<YyWater>{
	
	@Select({"<script>",
		"	SELECT DISTINCT DOCTORCODE,HOSID,DEPTCODE FROM ( SELECT ",
		"		yy.DOCTORCODE,yy.HOSID,yy.DEPTCODE,yy.CREATETIME ",
		"		FROM YY_WATER yy ",
//		"		INNER JOIN B_PATIENT bb on  yy.CLINICCARD = bb.CARDNO and bb.STATE=1 ",
//		"		INNER JOIN BAT_USER u on bb.MEMBERID = u.MEMBERID AND u.STATE=1 ",
		"		WHERE yy.operatorid = #{openId} ORDER BY yy.CREATETIME DESC) T LIMIT 0,10 ",
	"</script>"})
	List<YyHistoryDoctor> queryHistoryDoctor(@Param("openId")String openId);
	
	
	@Select({"<script>",
		"	SELECT DISTINCT DOCTORCODE,HOSID,DEPTCODE FROM ( SELECT ",
		"		yy.DOCTORCODE,yy.HOSID,yy.DEPTCODE,yy.CREATETIME ",
		"		FROM YY_WATER yy ",
		"		WHERE yy.operatorid = #{openId} ",
		"<if test=\"hosId!=null and hosId!=''\">",
			" AND yy.hosId=#{hosId}",
		"</if>",
		"ORDER BY yy.CREATETIME DESC) T LIMIT 0,10 ",
	"</script>"})
	List<YyHistoryDoctor> queryHistoryDoctorForMap(Map<String, String> map);
}

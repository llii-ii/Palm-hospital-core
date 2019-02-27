package com.kasite.client.basic.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.kasite.core.common.mappers.CommonMapper;
import com.kasite.core.serviceinterface.module.basic.dbo.Doctor;

/**
 * 
 * @className: IDoctorMapper
 * @author: lcz
 * @date: 2018年7月19日 上午11:25:21
 */
public interface IDoctorMapper extends CommonMapper<Doctor>{
	
	@Select({"<script>",
		"SELECT DISTINCT TITLE, TITLECODE FROM B_DOCTOR ",
		"<where>",
			"<if test=\"hosId!=null and hosId!=''\">",
				" AND HOSID=#{hosId}",
			"</if>",
			"<if test=\"deptCode!=null and deptCode!=''\">",
				" AND DEPTCODE=#{deptCode}",
			"</if>",
		"</where>",
		"</script>"})
	@Results({
		@Result(property = "DoctorTitleCode", column = "TITLECODE"),
		@Result(property = "DoctorTitle", column = "TITLE")
	})
	List<Map<String,String>> getDocTitleList(@Param("hosId")String hosId,@Param("deptCode")String deptCode);
}

package com.kasite.client.basic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.core.serviceinterface.module.basic.dbo.Dept;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @className: IDeptMapper
 * @author: lcz
 * @date: 2018年7月20日 下午4:56:00
 */
public interface IDeptMapper extends Mapper<Dept>{

	@Select({"<script>",
		"SELECT B.* FROM B_DEPT B ",
		"<where>",
			"<if test=\"dept.hosId!=null and dept.hosId!=''\">",
				"AND B.HOSID=#{dept.hosId} ",
			"</if>",
			"<if test=\"dept.deptCode!=null and dept.deptCode!=''\">",
				"AND B.DEPTCODE=#{dept.deptCode} ",
			"</if>",
			"<if test=\"dept.deptName!=null and dept.deptName!=''\">",
				"AND B.DEPTNAME LIKE '%${dept.deptName}%' ",
			"</if>",
			"<if test=\"dept.parentDeptCode!=null and dept.parentDeptCode!='' and dept.parentDeptCode!='-3'\">",
				"AND B.PARENTDEPTCODE=#{dept.parentDeptCode} ",
			"</if>",
			"<if test=\"dept.depttype!=null and dept.depttype!=''\">",
				"AND B.DEPTTYPE=#{dept.depttype} ",
			"</if>",
		"</where>",
		" ORDER BY ORDERCOL ASC",
		"</script>"
	})
	public List<Dept> queryDeptList(@Param("dept")Dept dept);
	
}

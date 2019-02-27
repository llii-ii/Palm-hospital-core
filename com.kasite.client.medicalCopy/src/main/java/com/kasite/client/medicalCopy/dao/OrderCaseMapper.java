package com.kasite.client.medicalCopy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.kasite.core.serviceinterface.module.medicalCopy.dbo.OrderCase;

import tk.mybatis.mapper.common.Mapper;

public interface OrderCaseMapper extends Mapper<OrderCase>{

	@Select("SELECT * FROM TB_ORDER_CASE WHERE caseId = #{caseId} ORDER BY createTime DESC")
	public List<OrderCase> isCopyBy30Day(String caseId);
}

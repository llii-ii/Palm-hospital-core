package com.kasite.client.business.module.backstage.bill.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.core.common.mappers.CommonMapper;
import com.kasite.core.serviceinterface.module.pay.dbo.BankMoneyCheck;
import com.kasite.core.serviceinterface.module.pay.dto.BankCheckCountVo;

public interface BankCheckDao extends CommonMapper<BankMoneyCheck> {

	@Select({"<script>",
		"SELECT BMC.isCheck, BMC.checkState, COUNT(BMC.DATE) AS count FROM P_BANK_MONEY_CHECK BMC",
		"<where>",
			"<if test=\"startDate!=null and startDate!='' and endDate!=null and endDate!=''\">",
				" AND BMC.DATE BETWEEN str_to_date(DATE_FORMAT(#{startDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s') ",
				" AND DATE_ADD(str_to_date(DATE_FORMAT(#{endDate},'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s'),INTERVAL 1 DAY) " ,
			"</if>" ,
		"</where>",
		" GROUP BY BMC.ISCHECK,BMC.CHECKSTATE",
		"</script>"})
	List<BankCheckCountVo> findBankCheckListForDate(@Param("startDate")String startDate, @Param("endDate")String endDate);
	
}

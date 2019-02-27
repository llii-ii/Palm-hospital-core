package com.kasite.client.order.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.client.order.bean.dbo.SelfRefundRecord;

import tk.mybatis.mapper.common.Mapper;

public interface ISelfRefundRecordMapper extends Mapper<SelfRefundRecord>{

	@Select({"<script>",
		  "SELECT * FROM O_SELFREFUND_RECORD T WHERE T.CARDNO = #{cardNo}  AND (T.STATE = 0 OR T.STATE = 1 )",
		  "AND T.CARDTYPE = #{cardType}",
		  "<if test=\"hisMemberId != null and hisMemberId !='' \">",
			"<![CDATA[ AND T.HISMEMBERID = #{hisMemberId}  ]]>",
		  "</if>",
		"</script>"})
	List<SelfRefundRecord> queryUnSuccessSelfRefundRecord(@Param("cardNo")String cardNo,@Param("cardType")String cardType,
			@Param("hisMemberId")String hisMemberId);
	
	
}

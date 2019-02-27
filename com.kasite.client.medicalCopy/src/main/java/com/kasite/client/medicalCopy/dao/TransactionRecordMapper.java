package com.kasite.client.medicalCopy.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.kasite.core.serviceinterface.module.medicalCopy.dbo.TransactionRecord;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqTransactionRecord;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespTransactionRecord;

import tk.mybatis.mapper.common.Mapper;

public interface TransactionRecordMapper extends Mapper<TransactionRecord>{

	@Select({"<script>",
		"SELECT *,eo.createTime AS eoTime FROM TB_TRANSACTION_RECORD tr ",
		"INNER JOIN TB_EXPRESS_ORDER eo ON tr.orderId = eo.id ",
		"<where>",
			"<if test=\"transactionRecord.orderId!=null and transactionRecord.orderId!=''\">",
				"AND tr.orderId like CONCAT('%',#{transactionRecord.orderId},'%') ",
			"</if>",
			"<if test=\"transactionRecord.orderType!=null and transactionRecord.orderType!=''\">",
				"AND tr.orderType = #{transactionRecord.orderType} ",
			"</if>",
			"<if test=\"transactionRecord.payChannel!=null and transactionRecord.payChannel!=''\">",
				"AND tr.payChannel = #{transactionRecord.payChannel} ",
			"</if>",
			"<if test=\"transactionRecord.accountResult!=null and transactionRecord.accountResult!=''\">",
				"AND tr.accountResult = #{transactionRecord.accountResult} ",
			"</if>",			
			"<if test=\"transactionRecord.startTime!=null and transactionRecord.startTime!='' and transactionRecord.endTime!='' and transactionRecord.endTime!=''\">",
				"AND eo.createTime between #{transactionRecord.startTime} and DATE_ADD(#{transactionRecord.endTime},INTERVAL 1 DAY) ",
			"</if>",
		"</where>",
		"ORDER BY tr.orderId DESC",
		"</script>"
	})
	List<RespTransactionRecord> selectTransactionRecord(@Param("transactionRecord")ReqTransactionRecord transactionRecord);


	@Update({"<script>",
		"UPDATE TB_TRANSACTION_RECORD SET ",
			"<if test=\"transactionRecord.shouldRefunds!=null and transactionRecord.shouldRefunds!=''\">",
				" shouldRefunds = #{transactionRecord.shouldRefunds},",
			"</if>",
			"<if test=\"transactionRecord.actualRefunds!=null and transactionRecord.actualRefunds!=''\">",
				" actualRefunds = #{transactionRecord.actualRefunds},",
			"</if>",
			"<if test=\"transactionRecord.accountResult!=null and transactionRecord.accountResult!=''\">",
				" accountResult = #{transactionRecord.accountResult},",
			"</if>",
			"<if test=\"transactionRecord.receiptsType!=null and transactionRecord.receiptsType!=''\">",
				" receiptsType = #{transactionRecord.receiptsType},",
			"</if>",
		" orderId = #{transactionRecord.orderId} WHERE orderId = #{transactionRecord.orderId}",
		"</script>"
	})
	int updateByOrderId(@Param("transactionRecord")ReqTransactionRecord transactionRecord);
	
	@Select({"<script>",
		"SELECT",
		"<if test=\"transactionRecord.type!=null and transactionRecord.type eq 1\">",
		"DATE_FORMAT( createTime, '%Y-%m-%d' ) eoTime,",
		"</if>",
		"<if test=\"transactionRecord.type!=null and transactionRecord.type eq 2\">",
		"payChannel,",
		"</if>",
		"count( * ) orderNum,SUM( actualReceipts ) totalMoney,SUM( actualRefunds ) totalRefundMoney",
		"<where>",			
			"<if test=\"transactionRecord.startTime!=null and transactionRecord.startTime!='' and transactionRecord.endTime!='' and transactionRecord.endTime!=''\">",
				"AND createTime between #{transactionRecord.startTime} and DATE_ADD(#{transactionRecord.endTime},INTERVAL 1 DAY) ",
			"</if>",
		"</where>",
		"GROUP BY",
		"<if test=\"transactionRecord.type!=null and transactionRecord.type eq 1\">",
		" eoTime",
		"</if>",
		"<if test=\"transactionRecord.type!=null and transactionRecord.type eq 2\">",
		" payChannel",
		"</if>",
		"</script>"
	})
	List<RespTransactionRecord> countTransactionRecord(@Param("transactionRecord")ReqTransactionRecord transactionRecord);
}

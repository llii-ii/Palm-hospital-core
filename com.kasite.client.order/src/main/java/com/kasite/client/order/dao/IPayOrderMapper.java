package com.kasite.client.order.dao;

import org.apache.ibatis.annotations.Insert;
import com.kasite.client.order.bean.dbo.PayOrder;
import tk.mybatis.mapper.common.Mapper;
public interface IPayOrderMapper extends Mapper<PayOrder>{

	@Insert({
		"<script>",
		"INSERT INTO O_PAYORDER (orderId,operatorId,operatorName,beginDate,channelId,remark,price,configKey,payState) ",
		"VALUES(#{orderId},#{operatorId},#{operatorName},#{beginDate},#{channelId},#{remark},#{price},#{configKey},#{payState})",
		"ON DUPLICATE KEY UPDATE operatorId =#{operatorId},operatorName =#{operatorName},endDate=#{endDate}",
	"</script>"
	})
	public void insertByOrderIsPayment(PayOrder payOrder);
	
	@Insert({
		"<script>",
		"INSERT INTO O_PAYORDER (orderId,operatorId,operatorName,beginDate,endDate,channelId,remark,price,configKey,payState,accNo,transactionNo) ",
		"VALUES(#{orderId},#{operatorId},#{operatorName},#{beginDate},#{endDate},#{channelId},#{remark},#{price},#{configKey},#{payState},#{accNo},#{transactionNo})",
		"ON DUPLICATE KEY UPDATE configKey =#{configKey},payState =#{payState},endDate=#{endDate},transactionNo=#{transactionNo},accNo=#{accNo}",
		"<if test=\"remark != null and remark!=''\">,remark=#{remark}</if>",
	"</script>"
	})
	public void insertByPayForCompletion(PayOrder payOrder);
}

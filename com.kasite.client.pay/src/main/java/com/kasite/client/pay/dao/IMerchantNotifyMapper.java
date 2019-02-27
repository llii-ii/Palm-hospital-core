package com.kasite.client.pay.dao;


import org.apache.ibatis.annotations.Insert;

import com.kasite.core.serviceinterface.module.pay.dbo.MerchantNotify;

import tk.mybatis.mapper.common.Mapper;


/**
 * @author linjf
 * MerchantNotify 数据库操作类
 */
public interface IMerchantNotifyMapper extends Mapper<MerchantNotify>{


	@Insert({
		"<script>",
		"INSERT IGNORE INTO P_MERCHANT_NOTIFY ",
		" ( id,orderId,orderType,refundOrderId,transactionNo,price,createTime,updateTime,retryNum,state,clientId,configKey) ",
		" VALUES",
		"(#{id},#{orderId},#{orderType},#{refundOrderId},#{transactionNo},#{price},#{createTime},#{updateTime},#{retryNum},#{state},#{clientId},#{configKey} )",
	"</script>"
	})
	public int insertIgnore(MerchantNotify merchantNotify);
	
}

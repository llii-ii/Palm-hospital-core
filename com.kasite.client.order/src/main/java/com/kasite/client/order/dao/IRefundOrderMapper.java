package com.kasite.client.order.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.client.order.bean.dbo.RefundOrder;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @className: IOrderMapper
 * @author: lcz
 * @date: 2018年7月20日 下午4:56:00
 */
public interface IRefundOrderMapper extends Mapper<RefundOrder>{

	//"SELECT SUM(REFUNDPRICE) FROM O_REFUNDORDER WHERE ORDERID=? AND (PAYSTATE = 4 OR PAYSTATE = 3)"
	
	@Select({"<script>",
		  "SELECT SUM(REFUNDPRICE) FROM O_REFUNDORDER WHERE orderId=#{orderId} AND (PAYSTATE = 4 OR PAYSTATE = 3)",
		"</script>"})
	Integer getTotalRrefundPrice(@Param("orderId")String orderId);
		
}

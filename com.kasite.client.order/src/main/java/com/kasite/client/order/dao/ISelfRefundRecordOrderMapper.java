package com.kasite.client.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.client.order.bean.dbo.SelfRefundRecordOrder;
import com.kasite.client.order.bean.dto.RefundOrderVo;

import tk.mybatis.mapper.common.Mapper;

/**
 * @author linjf
 * TODO
 */
public interface ISelfRefundRecordOrderMapper extends Mapper<SelfRefundRecordOrder>{

	@Select({"<script>",
		" SELECT S.ORDERID,R.REFUNDORDERID,IFNULL(R.PAYSTATE,7) AS PAYSTATE,S.REMARK AS FAILREASON,IFNULL(R.REFUNDPRICE,0) AS REFUNDPRICE,",
		" R.REMARK,R.BEGINDATE,R.EndDate,O.ORDERNUM,P.CHANNELID AS PAYCHANNELID,P.CONFIGKEY ",
		" FROM O_SELFREFUND_RECORD_ORDER S ",
		" LEFT JOIN O_REFUNDORDER  R ON R.REFUNDORDERID=S.REFUNDORDERID ",
		" LEFT JOIN O_ORDER  O ON O.ORDERID  =S.ORDERID ",
		" LEFT JOIN O_PAYORDER P ON P.ORDERID = S.ORDERID ",
		" WHERE S.SELFREFUNDRECORDID=#{selfRefundRecordId}",
		"</script>"})
	List<RefundOrderVo> querySelfRefundOrderInfoList(@Param("selfRefundRecordId")Long selfRefundRecordId);
	
}

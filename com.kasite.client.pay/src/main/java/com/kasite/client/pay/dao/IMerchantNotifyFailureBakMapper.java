package com.kasite.client.pay.dao;

import org.apache.ibatis.annotations.Insert;

import com.kasite.core.serviceinterface.module.pay.dbo.MerchantNotifyFailureBak;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author linjf
 * TODO
 */
public interface IMerchantNotifyFailureBakMapper extends Mapper<MerchantNotifyFailureBak>{

	@Insert({
		"<script>",
		"INSERT INTO P_MERCHANT_FAILURE_BAK (id,merchantNotifyId,createTime,updateTime) VALUES(#{id},#{merchantNotifyId},#{createTime},#{updateTime})",
		"ON DUPLICATE KEY UPDATE updateTime =#{updateTime}",
	"</script>"
	})
	public void insertOnDuplicateKey(MerchantNotifyFailureBak merchantNotifyFailureBak);
}

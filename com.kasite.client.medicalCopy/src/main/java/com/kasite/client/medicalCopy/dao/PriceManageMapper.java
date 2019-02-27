package com.kasite.client.medicalCopy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.core.serviceinterface.module.medicalCopy.dbo.PriceManage;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqPriceManage;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespPriceManage;

import tk.mybatis.mapper.common.Mapper;

public interface PriceManageMapper extends Mapper<PriceManage>{
	@Select({"<script>",
		"SELECT * FROM TB_PRICE_MANAGE pm",
		"<where>",
			"<if test=\"priceManage.name!=null and priceManage.name!=''\">",
				"AND pm.name like CONCAT('%',#{priceManage.name},'%') ",
			"</if>",
			"<if test=\"priceManage.priceType!=null and priceManage.priceType!=''\">",
				"AND pm.priceType = #{priceManage.priceType} ",
			"</if>",
//			"<if test=\"priceManage.startTime!=null and priceManage.startTime!='' and priceManage.endTime!='' and priceManage.endTime!=''\">",
//				"AND pm.updateTime between #{priceManage.startTime} and #{priceManage.endTime} ",
//			"</if>",
			"<if test=\"priceManage.startTime!=null and priceManage.startTime!=''\">",
				"AND pm.updateTime between #{priceManage.startTime} and DATE_ADD(#{priceManage.startTime},INTERVAL 1 DAY)  ",
			"</if>",
		"</where>",
		"</script>"
	})
	List<PriceManage> selectPriceManage(@Param("priceManage")ReqPriceManage priceManage);
}

package com.kasite.client.medicalCopy.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.core.serviceinterface.module.medicalCopy.dbo.CopyContent;
import com.kasite.core.serviceinterface.module.pay.dto.BillCheckCountVo;

import tk.mybatis.mapper.common.Mapper;

public interface CopyContentMapper extends Mapper<CopyContent>{
	
	/**
	 * 根据查询复印内容列表
	 * 
	 * @param startDate
	 * @param endDate
	 * @param channelId
	 * @param payType
	 */
	@Select({"<script>",
		"SELECT * FROM TB_COPY_CONTENT",
		"<where>",
			"<if test=\"contentIds != null and contentIds.size>0\">",
				" ID IN ",
				"<foreach collection=\"contentIds\" open=\"(\" separator=\",\" close=\")\" item=\"contentId\">",
					"<![CDATA[(#{contentId}) ]]>",
				"</foreach>",	
			"</if>",
		"</where>",
		"</script>"})
	List<CopyContent> getCopyContentByCopyContentIds(@Param("contentIds")String[] contentIds);

}

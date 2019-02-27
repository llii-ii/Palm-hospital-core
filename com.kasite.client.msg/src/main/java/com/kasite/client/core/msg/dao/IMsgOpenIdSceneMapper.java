package com.kasite.client.core.msg.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.client.core.msg.bean.dbo.MsgOpenIdScene;
import com.kasite.client.core.msg.bean.dbo.MsgScene;
import com.kasite.core.serviceinterface.module.msg.req.ReqMsgOpenIdSceneList;
import com.kasite.core.serviceinterface.module.msg.req.ReqMsgSceneList;
import com.kasite.core.serviceinterface.module.msg.resp.RespMsgScene;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @className: IMsgSceneMapper
 * @author: zwl
 * @date: 2018年11月13日 下午10:29:51
 */
public interface IMsgOpenIdSceneMapper extends Mapper<MsgOpenIdScene>{
	@Select({"<script>",
		"SELECT count(1) FROM M_MSGSCENE_OPENID M ",
		"<where>",
			"<if test=\"openId != null and openId != ''\">",
				"<![CDATA[ AND openId = #{openId} ]]>",
			"</if>",
			"<if test=\"modeType != null and modeType != ''\">",
				"<![CDATA[ AND modeType = #{modeType} ]]>",
			"</if>",
			"<if test=\"modeType != null and modeType != ''\">",
				"<![CDATA[ AND modeType = #{modeType} ]]>",
			"</if>",
			"<if test=\"hosId != null and hosId != ''\">",
				"<![CDATA[ AND hosId = #{hosId} ]]>",
			"</if>",
			"<![CDATA[ AND state = 1 ]]>",
		"</where>",
	"</script>"})
	int queryMsgOpenIdScene(@Param("openId")String openId,@Param("modeType")String modeType,@Param("hosId")String hosId);
	
}

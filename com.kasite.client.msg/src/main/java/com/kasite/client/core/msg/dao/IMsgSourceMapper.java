package com.kasite.client.core.msg.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.client.core.msg.bean.dbo.MsgScene;
import com.kasite.client.core.msg.bean.dbo.MsgSource;
import com.kasite.core.serviceinterface.module.msg.req.ReqMsgSceneList;
import com.kasite.core.serviceinterface.module.msg.req.ReqMsgSourceList;
import com.kasite.core.serviceinterface.module.msg.resp.RespMsgScene;
import com.kasite.core.serviceinterface.module.msg.resp.RespMsgSource;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @className: IMsgSceneMapper
 * @author: zwl
 * @date: 2018年11月13日 下午10:29:51
 */
public interface IMsgSourceMapper extends Mapper<MsgSource>{
	@Select({"<script>",
		"SELECT * FROM M_MSGSOURCE M ",
		"<where>",
			"<if test=\"msgSceneList.sourceId != null and msgSceneList.sourceId != ''\">",
				"<![CDATA[ AND sourceId = #{msgSceneList.sourceId} ]]>",
			"</if>",
			"<if test=\"msgSceneList.sourceName != null and msgSceneList.sourceName != ''\">",
				"<![CDATA[ AND sourceName = #{msgSceneList.sourceName} ]]>",
			"</if>",
			"<if test=\"msgSceneList.state != null and msgSceneList.state != ''\">",
				"<![CDATA[ AND state = #{msgSceneList.state} ]]>",
			"</if>",
			"<if test=\"msgSceneList.orgId != null and msgSceneList.orgId != ''\">",
				"<![CDATA[ AND hosId = #{msgSceneList.orgId} ]]>",
			"</if>",
		"</where>",
	"</script>"})
	List<RespMsgSource> queryMsgScene(@Param("msgSceneList")ReqMsgSourceList msgSceneList);
}

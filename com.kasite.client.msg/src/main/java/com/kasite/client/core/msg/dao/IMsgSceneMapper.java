package com.kasite.client.core.msg.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
public interface IMsgSceneMapper extends Mapper<MsgScene>{
	@Select({"<script>",
		"SELECT * FROM M_MSGSCENE M ",
		"<where>",
			"<if test=\"msgSceneList.sceneId != null and msgSceneList.sceneId != ''\">",
				"<![CDATA[ AND sceneid = #{msgSceneList.sceneId} ]]>",
			"</if>",
			"<if test=\"msgSceneList.sceneName != null and msgSceneList.sceneName != ''\">",
				"<![CDATA[ AND sceneName = #{msgSceneList.sceneName} ]]>",
			"</if>",
			"<if test=\"msgSceneList.modeType != null and msgSceneList.modeType != ''\">",
				"<![CDATA[ AND modeType = #{msgSceneList.modeType} ]]>",
			"</if>",
			"<if test=\"msgSceneList.state != null and msgSceneList.state != ''\">",
				"<![CDATA[ AND state = #{msgSceneList.state} ]]>",
			"</if>",
			"<if test=\"msgSceneList.orgId != null and msgSceneList.orgId != ''\">",
				"<![CDATA[ AND hosId = #{msgSceneList.orgId} ]]>",
			"</if>",
		"</where>",
	"</script>"})
	List<RespMsgScene> queryMsgScene(@Param("msgSceneList")ReqMsgSceneList msgSceneList);
	@Select({"<script>",
		"SELECT * FROM M_MSGSCENE M ",
		"<where>",
			"<if test=\"modeType != null and modeType != ''\">",
				"<![CDATA[ AND modeType = #{modeType} ]]>",
			"</if>",
			"<if test=\"state != null and state != ''\">",
				"<![CDATA[ AND state = #{state} ]]>",
			"</if>",
		"</where>",
	"</script>"})
	List<RespMsgScene> queryMsgSceneByModeType(@Param("modeType")String modeType,@Param("state")int state);
	@Select({"<script>",
		"SELECT * FROM M_MSGSCENE M ",
		"<where>",
			"<if test=\"msgSceneList.sceneId != null and msgSceneList.sceneId != ''\">",
				"<![CDATA[ AND sceneid = #{msgSceneList.sceneId} ]]>",
			"</if>",
			"<if test=\"msgSceneList.sceneName != null and msgSceneList.sceneName != ''\">",
				"<![CDATA[ AND sceneName = #{msgSceneList.sceneName} ]]>",
			"</if>",
			"<if test=\"msgSceneList.modeType != null and msgSceneList.modeType != ''\">",
				"<![CDATA[ AND modeType = #{msgSceneList.modeType} ]]>",
			"</if>",
			"<if test=\"msgSceneList.orgId != null and msgSceneList.orgId != ''\">",
				"<![CDATA[ AND hosId = #{msgSceneList.orgId} ]]>",
			"</if>",
		"</where>",
	"</script>"})
	List<RespMsgScene> queryMsgSceneByMsgOpenIdSceneList(@Param("msgSceneList")ReqMsgOpenIdSceneList msgSceneList);
}

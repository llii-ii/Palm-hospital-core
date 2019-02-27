package com.kasite.client.core.msg.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.client.core.msg.bean.dbo.MsgTemp;
import com.kasite.core.serviceinterface.module.msg.req.ReqMsgTempList;
import com.kasite.core.serviceinterface.module.msg.resp.RespMsgTemp;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @className: IMsgTempMapper
 * @author: lcz
 * @date: 2018年7月24日 下午10:29:51
 */
public interface IMsgTempMapper extends Mapper<MsgTemp>{
	@Select({"<script>",
		"SELECT * FROM M_MSGTEMP M ",
		"<where>",
			"<if test=\"msgTempList.modeId != null and msgTempList.modeId != ''\">",
				"<![CDATA[ AND modeId = #{msgTempList.modeId} ]]>",
			"</if>",
			"<if test=\"msgTempList.useChannel != null and msgTempList.useChannel != ''\">",
				"<![CDATA[ AND useChannel = #{msgTempList.useChannel} ]]>",
			"</if>",
			"<if test=\"msgTempList.modeType != null and msgTempList.modeType != ''\">",
				"<![CDATA[ AND modeType = #{msgTempList.modeType} ]]>",
			"</if>",
			"<if test=\"msgTempList.state != null and msgTempList.state != ''\">",
				"<![CDATA[ AND state = #{msgTempList.state} ]]>",
			"</if>",
			"<if test=\"msgTempList.msgType != null and msgTempList.msgType != ''\">",
				"<![CDATA[ AND msgType = #{msgTempList.msgType} ]]>",
			"</if>",
			"<if test=\"msgTempList.templateId != null and msgTempList.templateId != ''\">",
				"<![CDATA[ AND templateId = #{msgTempList.templateId} ]]>",
			"</if>",
			"<if test=\"msgTempList.msgTempName != null and msgTempList.msgTempName != ''\">",
				"<![CDATA[ AND msgTempName = #{msgTempList.msgTempName} ]]>",
			"</if>",
			"<if test=\"msgTempList.userHos != null and msgTempList.userHos != ''\">",
				"<![CDATA[ AND userHos = #{msgTempList.userHos} ]]>",
			"</if>",
		"</where>",
		" ORDER BY BEGIN DESC",
	"</script>"})
	List<RespMsgTemp> queryMsgTemp(@Param("msgTempList")ReqMsgTempList msgTempList);
}

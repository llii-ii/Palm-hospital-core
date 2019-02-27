package com.kasite.client.core.msg.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.client.core.msg.bean.dbo.MsgScene;
import com.kasite.client.core.msg.bean.dbo.MsgSource;
import com.kasite.client.core.msg.bean.dbo.MsgUserOpenId;
import com.kasite.core.serviceinterface.module.basic.dbo.Member;
import com.kasite.core.serviceinterface.module.msg.req.ReqMsgSceneList;
import com.kasite.core.serviceinterface.module.msg.req.ReqMsgSourceList;
import com.kasite.core.serviceinterface.module.msg.req.ReqMsgUserOpenIdList;
import com.kasite.core.serviceinterface.module.msg.req.ReqQueryOpenId;
import com.kasite.core.serviceinterface.module.msg.resp.RespMsgScene;
import com.kasite.core.serviceinterface.module.msg.resp.RespMsgSource;
import com.kasite.core.serviceinterface.module.msg.resp.RespMsgUserOpenId;
import com.kasite.core.serviceinterface.module.msg.resp.RespQueryOpenId;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @className: IMsgSceneMapper
 * @author: zwl
 * @date: 2018年11月13日 下午10:29:51
 */
public interface IMsgUserOpenIdMapper extends Mapper<MsgUserOpenId>{
	@Select({"<script>",
		"SELECT * FROM M_CARDNO_OPENID M ",
		"<where>",
			"<if test=\"msgUserList.cardType != null and msgUserList.cardType != ''\">",
				"<![CDATA[ AND cardType = #{msgUserList.cardType} ]]>",
			"</if>",
			"<if test=\"msgUserList.cardNo != null and msgUserList.cardNo != ''\">",
				"<![CDATA[ AND cardNo = #{msgUserList.cardNo} ]]>",
			"</if>",
			"<if test=\"msgUserList.OpenId != null and msgUserList.OpenId != ''\">",
				"<![CDATA[ AND OpenId = #{msgUserList.OpenId} ]]>",
			"</if>",
			"<if test=\"msgUserList.OpenType != null and msgUserList.OpenType != ''\">",
				"<![CDATA[ AND OpenType = #{msgUserList.OpenType} ]]>",
			"</if>",
			"<if test=\"msgUserList.State != null and msgUserList.State != ''\">",
				"<![CDATA[ AND State = #{msgUserList.State} ]]>",
			"</if>",
		"</where>",
	"</script>"})
	List<RespMsgUserOpenId> queryMsgUserOpenId(@Param("msgUserList")ReqMsgUserOpenIdList msgUserList);
	@Select({"<script>",
		"SELECT * FROM M_CARDNO_OPENID M ",
		"<where>",
			"<if test=\"msgUserList.cardType != null and msgUserList.cardType != ''\">",
				"<![CDATA[ AND cardType = #{msgUserList.cardType} ]]>",
			"</if>",
			"<if test=\"msgUserList.cardNo != null and msgUserList.cardNo != ''\">",
				"<![CDATA[ AND cardNo = #{msgUserList.cardNo} ]]>",
			"</if>",
			"<if test=\"msgUserList.OpenId != null and msgUserList.OpenId != ''\">",
				"<![CDATA[ AND OpenId = #{msgUserList.OpenId} ]]>",
			"</if>",
			"<if test=\"msgUserList.OpenType != null and msgUserList.OpenType != ''\">",
				"<![CDATA[ AND OpenType = #{msgUserList.OpenType} ]]>",
			"</if>",
			"<if test=\"msgUserList.State != null and msgUserList.State != ''\">",
				"<![CDATA[ AND State = #{msgUserList.State} ]]>",
			"</if>",
			"<if test=\"msgUserList.orgId != null and msgUserList.orgId != ''\">",
				"<![CDATA[ AND hosId = #{msgUserList.orgId} ]]>",
			"</if>",
		"</where>",
		"limit #{pIndex},#{pSize}",
	"</script>"})
	List<RespMsgUserOpenId> queryMsgUserOpenIdPage(@Param("msgUserList")ReqMsgUserOpenIdList msgUserList,@Param("pIndex")int pIndex,@Param("pSize")int pSize);
	@Select({"<script>",
		"SELECT count(1) FROM M_CARDNO_OPENID M ",
		"<where>",
			"<if test=\"msgUserList.cardType != null and msgUserList.cardType != ''\">",
				"<![CDATA[ AND cardType = #{msgUserList.cardType} ]]>",
			"</if>",
			"<if test=\"msgUserList.cardNo != null and msgUserList.cardNo != ''\">",
				"<![CDATA[ AND cardNo = #{msgUserList.cardNo} ]]>",
			"</if>",
			"<if test=\"msgUserList.OpenId != null and msgUserList.OpenId != ''\">",
				"<![CDATA[ AND OpenId = #{msgUserList.OpenId} ]]>",
			"</if>",
			"<if test=\"msgUserList.OpenType != null and msgUserList.OpenType != ''\">",
				"<![CDATA[ AND OpenType = #{msgUserList.OpenType} ]]>",
			"</if>",
			"<if test=\"msgUserList.State != null and msgUserList.State != ''\">",
				"<![CDATA[ AND State = #{msgUserList.State} ]]>",
			"</if>",
			"<if test=\"msgUserList.orgId != null and msgUserList.orgId != ''\">",
				"<![CDATA[ AND hosId = #{msgUserList.orgId} ]]>",
			"</if>",
			
		"</where>",
	"</script>"})
	int queryMsgUserOpenIdCount(@Param("msgUserList")ReqMsgUserOpenIdList msgUserList);
	@Select({"<script>",
		"SELECT OpenId FROM M_CARDNO_OPENID M ",
		"<where>",
			"<if test=\"cardType != null and cardType != ''\">",
				"<![CDATA[ AND cardType = #{cardType} ]]>",
			"</if>",
			"<if test=\"openType != null and openType != ''\">",
				"<![CDATA[ AND openType = #{openType} ]]>",
			"</if>",
			"<if test=\"cardNo != null and cardNo != ''\">",
				"<![CDATA[ AND (cardNo = #{cardNo} or patId= #{cardNo})]]>",
			"</if>",
			"<if test=\"State != null and State != ''\">",
				"<![CDATA[ AND State = #{State} ]]>",
			"</if>",
			"<if test=\"hosId != null and hosId != ''\">",
				"<![CDATA[ AND hosId = #{hosId} ]]>",
			"</if>",
		"</where>",
	"</script>"})
	List<String> queryOpenId(@Param("cardNo")String cardNo,@Param("cardType")int cardType,@Param("State")int State,@Param("openType")int openType,@Param("hosId")String hosId);

	
	@Select({"<script>",
		" SELECT ",
			" UM.OPENID ,U.CHANNELID",
		" FROM B_USER_MEMBER UM ",
		" INNER JOIN B_MEMBER M ON UM.MEMBERID =  M.MEMBERID ",
		" INNER JOIN BAT_USER U ON U.OPENID = UM.OPENID ",
		" LEFT JOIN B_USER_MEMBER_CARD UMC ON UM.OPENID = UMC.OPENID AND UM.MEMBERID=UMC.MEMBERID ",
		" LEFT JOIN B_CARDBAG C ON UMC.CARDID = C.CARDID ",
		" <where> ",
			"<if test=\"member.cardType!=null and member.cardType>0\">",
				" AND C.CARDTYPE=#{member.cardType} ",
			"</if>",
			"<if test=\"member.cardNo!=null and member.cardNo!=''\">",
				" AND C.CARDNO=#{member.cardNo} ",
			"</if>",
			"<if test=\"member.channelId!=null and member.channelId!=''\">",
				" AND U.CHANNELID=#{member.channelId} ",
			"</if>",
			"<if test=\"member.memberName!=null and member.memberName!=''\">",
				" AND M.MEMBERNAME=#{member.memberName} ",
			"</if>",
			"<if test=\"member.idCardNo!=null and member.idCardNo!=''\">",
				" AND M.IDCARDNO=#{member.idCardNo} ",
			"</if>",
			"<if test=\"member.mobile!=null and member.mobile!=''\">",
				" AND M.MOBILE=#{member.mobile} ",
			"</if>",
			"</where>",
	"</script>"})
	public List<RespQueryOpenId> queryOpenIds(@Param("member")ReqQueryOpenId member);
}

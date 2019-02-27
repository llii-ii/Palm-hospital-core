package com.kasite.client.basic.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.kasite.core.serviceinterface.module.basic.dbo.UserMemberCard;

import tk.mybatis.mapper.common.Mapper;

public interface IUserMemberCardMapper extends Mapper<UserMemberCard>{
	
	@Select({"<script>",
		"SELECT UMC.*,C.CARDTYPE,C.CARDNO,C.CARDTYPENAME ",
		"FROM B_USER_MEMBER_CARD UMC ",
		"LEFT JOIN B_CARDBAG C ON UMC.CARDID = C.CARDID ",
		"<where>",
			"<if test=\"memberId!=null and memberId!=''\">",
				" AND UMC.MEMBERID=#{memberId} ",
			"</if>",
			"<if test=\"openId!=null and openId!=''\">",
				" AND UMC.OPENID=#{openId} ",
			"</if>",
			"<if test=\"isDefault!=null and isDefault!=''\">",
				" AND UMC.ISDEFAULT=#{isDefault} ",
			"</if>",
			"<if test=\"cardType!=null and cardType!=''\">",
				" AND C.CARDTYPE=#{cardType} ",
			"</if>",
			"<if test=\"cardNo!=null and cardNo!=''\">",
				" AND C.CARDNO=#{cardNo} ",
			"</if>",
			"<if test=\"notEqualCardNo!=null and notEqualCardNo!=''\">",
				"<![CDATA[ AND C.CARDNO<>#{notEqualCardNo}  ]]>",
			"</if>",
		"</where>",
		"</script>"})
	List<UserMemberCard> queryUserMemberCardList(Map<String,Object> map) throws Exception;
	
	@Select({"<script>",
		"SELECT COUNT(1) FROM B_USER_MEMBER_CARD UMC ",
		"LEFT JOIN B_CARDBAG C ON UMC.CARDID = C.CARDID ",
		"<where>",
			"<if test=\"memberId!=null and memberId!=''\">",
				" AND UMC.MEMBERID=#{memberId} ",
			"</if>",
			"<if test=\"openId!=null and openId!=''\">",
				" AND UMC.OPENID=#{openId} ",
			"</if>",
			"<if test=\"cardType!=null and cardType!=''\">",
				" AND C.CARDTYPE=#{cardType} ",
			"</if>",
		"</where>",
		"</script>"})
	int queryUserMemberCardCount(@Param("openId")String openId,@Param("memberId")String memberId,@Param("cardType")String cardType) throws Exception;
	
	/**
	 * 将用户、成员、卡信息关联关系移到历史表
	 * @param openId
	 * @param memberId
	 * @param cardId
	 * @throws Exception
	 */
	@Update({"<script>",
		"INSERT INTO B_USER_MEMBER_CARD_OLD SELECT * FROM B_USER_MEMBER_CARD WHERE OPENID=#{openId} AND MEMBERID=#{memberId}",
		"<if test=\"cardId!=null and cardId!=''\">",
			" AND CARDID=#{cardId} ",
		"</if>",
	"</script>"})
	void moveUserMemberCardToOld(@Param("openId")String openId,@Param("memberId")String memberId,@Param("cardId")Long cardId) throws Exception;
	/**
	 * 删除用户、成员、卡信息关联关系
	 * @param openId
	 * @param memberId
	 * @param cardId
	 * @throws Exception
	 */
	@Update({"<script>",
		"DELETE FROM B_USER_MEMBER_CARD WHERE OPENID=#{openId} AND MEMBERID=#{memberId}",
		"<if test=\"cardId!=null and cardId!=''\">",
			" AND CARDID=#{cardId} ",
		"</if>",
	"</script>"})
	void delUserMemberCard(@Param("openId")String openId,@Param("memberId")String memberId,@Param("cardId")Long cardId) throws Exception;
	@Update({"<script>",
		"UPDATE B_USER_MEMBER_CARD UMC,B_CARDBAG C SET UMC.ISDEFAULT=#{setIsDefault},UMC.OPERATORID =#{operatorId},UMC.OPERATORNAME=#{operatorname} WHERE ",
		" UMC.CARDID=C.CARDID AND UMC.OPENID=#{openId} AND UMC.MEMBERID=#{memberId} ",
		"<if test=\"cardId!=null and cardId!=''\">",
			" AND UMC.CARDID=#{cardId} ",
		"</if>",
		"<if test=\"isDefault!=null and isDefault!=''\">",
			" AND UMC.ISDEFAULT=#{isDefault} ",
		"</if>",
		"<if test=\"cardType!=null and cardType!=''\">",
			" AND C.CARDTYPE=#{cardType} ",
		"</if>",
		"<if test=\"cardNo!=null and cardNo!=''\">",
			" AND C.CARDNO=#{cardNo} ",
		"</if>",
		"<if test=\"notEqualCardNo!=null and notEqualCardNo!=''\">",
			"<![CDATA[ AND C.CARDNO<>#{notEqualCardNo}  ]]>",
		"</if>",
	"</script>"})
	int setDefaultCard(Map<String,Object> map)throws Exception;
}

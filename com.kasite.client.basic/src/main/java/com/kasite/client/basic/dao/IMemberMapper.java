package com.kasite.client.basic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.core.serviceinterface.module.basic.dbo.Member;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @className: IMemberMapper
 * @author: lcz
 * @date: 2018年7月18日 下午5:01:50
 */
public interface IMemberMapper extends Mapper<Member>{
	
//	@Select({"<script>",
//		"SELECT ",
//		"	m.MEMBERID,m.MEMBERNAME,m.MOBILE,m.IDCARDNO,m.SEX,m.BIRTHDATE,m.ADDRESS,	",
//		"	m.CERTTYPE,m.CERTNUM,m.GUARDIANNAME,m.GUARDIANSEX,m.GUARDIANCERTTYPE,		",
//		"	m.GUARDIANCERTNUM,m.ISCHILDREN,m.CREATETIME,m.NATION,p.UPDATETIME,			",
//		"	p.OPERATORID,p.OPERATORNAME,p.CARDNO,p.CARDTYPE,p.CARDTYPENAME,p.MCARDNO,	",
//		"	p.BIRTHNUMBER,p.HOSID,p.STATE,p.HISMEMBERID,p.ISDEFAULT,p.ACCOUNTID,		",
//		"	p.HOSPITALNO,u.CHANNELID,u.OPENID,m.ISDEFAULTMEMBER 						",
//		"FROM BAT_USER u ",
//		"INNER JOIN B_USER_MEMBER um ON u.OPENID=um.OPENID ",
//		"INNER JOIN  B_MEMBER m ON m.MEMBERID=um.MEMBERID ",
//		"INNER JOIN B_PATIENT p ON m.MEMBERID=p.MEMBERID ",
//		"WHERE um.MEMBERID = #{memberId} and u.OPENID = #{openId} and p.cardType=#{cardType} ",
//		"<choose>",
//		"	<when test='state!=0' >",
//		"	 	AND p.state=#{state} " ,
//		"	</when>", 
//		"	<otherwise>", 
//		"		AND p.state=1 " ,
//		"	</otherwise>", 
//		"</choose>", 
//		"</script>"
//	})
//	public Member getMemberInfoById(@Param("memberId")String memberId,@Param("openId")String openId,@Param("cardType")String cardType,@Param("state")int state);
//	@Select({"<script>",
//		"	SELECT 																		",
//		"	m.MEMBERID,m.MEMBERNAME,m.MOBILE,m.IDCARDNO,m.SEX,m.BIRTHDATE,m.ADDRESS,	",
//		"	m.CERTTYPE,m.CERTNUM,m.GUARDIANNAME,m.GUARDIANSEX,m.GUARDIANCERTTYPE,		",
//		"	m.GUARDIANCERTNUM,m.ISCHILDREN,m.CREATETIME,m.NATION,p.UPDATETIME,			",
//		"	p.OPERATORID,p.OPERATORNAME,p.CARDNO,p.CARDTYPE,p.CARDTYPENAME,p.MCARDNO,	",
//		"	p.BIRTHNUMBER,p.HOSID,p.STATE,p.HISMEMBERID,p.ISDEFAULT,p.ACCOUNTID,		",
//		"	p.HOSPITALNO,u.CHANNELID,u.OPENID,m.ISDEFAULTMEMBER 						",
//		"	FROM BAT_USER u  															",
//		"	INNER JOIN B_USER_MEMBER um ON u.OPENID=um.OPENID 					 		",
//		"	INNER JOIN  B_MEMBER m ON m.MEMBERID=um.MEMBERID 							",
//		"	INNER JOIN B_PATIENT p ON m.MEMBERID=p.MEMBERID 							",
//		"	<where>																		",
//		"	<if test=\"member.memberId!=null and member.memberId!=''\">					",
//		" 		AND um.MEMBERID=#{member.memberId}										",
//		"	</if>																		",
//		"	<choose>",
//		"		<when test=\"member.state!=null and member.state!=0\" >",
//		"			AND p.STATE=#{member.state} ",
//		"		</when>", 
//		"		<otherwise>", 
//		"			AND p.STATE=1 " ,
//		"		</otherwise>", 
//		"	</choose>", 
//			"<if test=\"member.cardType!=null and member.cardType>0\">",
//				"AND p.CARDTYPE=#{member.cardType} ",
//			"</if>",
//			"<if test=\"member.cardNo!=null and member.cardNo!=''\">",
//				"AND p.CARDNO=#{member.cardNo} ",
//			"</if>",
//			"<if test=\"member.channelId!=null and member.channelId!=''\">",
//				"AND u.CHANNELID=#{member.channelId} ",
//			"</if>",
//			"<if test=\"member.openId!=null and member.openId!=''\">",
//				"AND u.OPENID=#{member.openId} ",
//			"</if>",
//			"<if test=\"member.hospitalNo!=null and member.hospitalNo!=''\">",
//				"AND p.HOSPITALNO=#{member.hospitalNo} ",
//			"</if>",
//			"<if test=\"member.memberName!=null and member.memberName!=''\">",
//				"AND m.MEMBERNAME=#{member.memberName} ",
//			"</if>",
//			"<if test=\"member.memberNameLike!=null and member.memberNameLike!=''\">",
//				"AND m.MEMBERNAME like CONCAT('%',#{member.memberNameLike},'%') ",
//			"</if>",
//			"<if test=\"member.idCardNo!=null and member.idCardNo!=''\">",
//				"AND m.IDCARDNO=#{member.idCardNo} ",
//			"</if>",
//			"<if test=\"member.mcardNo!=null and member.mcardNo!=''\">",
//				"AND p.MCARDNO=#{member.mcardNo} ",
//			"</if>",
//			"<if test=\"member.hosId!=null and member.hosId!=''\">",
//				"AND p.HOSID=#{member.hosId} ",
//			"</if>",
//			"<if test=\"member.hisMemberId!=null and member.hisMemberId!=''\">",
//				"AND p.HISMEMBERID=#{member.hisMemberId} ",
//			"</if>",
//			"<if test=\"member.accountId!=null and member.accountId!=''\">",
//				"AND p.ACCOUNTID=#{member.accountId} ",
//			"</if>",
//			"<if test=\"member.mobile!=null and member.mobile!=''\">",
//				"AND m.MOBILE=#{member.mobile} ",
//			"</if>",
//			"<if test=\"member.isDefault!=null and member.isDefault!=''\">",
//				"AND p.ISDEFAULT=#{member.isDefault} ",
//			"</if>",
//			"<if test=\"member.isDefaultMember!=null and member.isDefaultMember!=''\">",
//				"AND m.ISDEFAULTMEMBER=#{member.isDefaultMember} ",
//			"</if>",
//			"<if test=\"member.hisMemberId!=null and member.hisMemberId!=''\">",
//				"AND p.hisMemberId=#{member.hisMemberId} ",
//			"</if>",
//		"</where>",
//		" ORDER BY p.ISDEFAULT DESC",
//		"</script>"
//	})
	@Select({"<script>",
		" SELECT ",
			" UM.MEMBERID,M.MEMBERNAME,M.MOBILE,M.IDCARDNO,M.SEX,M.BIRTHDATE,M.ADDRESS, ",
			" M.CERTTYPE,M.CERTNUM,M.GUARDIANNAME,M.GUARDIANSEX,M.GUARDIANCERTTYPE, ",	
			" M.GUARDIANCERTNUM,M.ISCHILDREN,M.CREATETIME,M.NATION, ",
			" CASE WHEN UMC.UPDATETIME IS NOT NULL THEN UMC.UPDATETIME ELSE UM.UPDATETIME END UPDATETIME, ",
			" CASE WHEN UMC.OPERATORID IS NOT NULL THEN UMC.OPERATORID ELSE UM.OPERATORID END OPERATORID, ",
			" CASE WHEN UMC.OPERATORNAME IS NOT NULL THEN UMC.OPERATORNAME ELSE UM.OPERATORNAME END OPERATORNAME, ",			
			" C.CARDNO,C.CARDTYPE,C.CARDTYPENAME, ",
			" UMC.HOSID,UMC.HISMEMBERID,UMC.ISDEFAULT, ",
			" CASE WHEN C.CARDTYPE='14' THEN C.CARDNO ELSE NULL END HOSPITALNO, ",
			" U.CHANNELID,U.OPENID,UM.ISDEFAULTMEMBER ",
		" FROM B_USER_MEMBER UM ",
		" INNER JOIN B_MEMBER M ON UM.MEMBERID =  M.MEMBERID ",
		" INNER JOIN BAT_USER U ON U.OPENID = UM.OPENID ",
		" LEFT JOIN B_USER_MEMBER_CARD UMC ON UM.OPENID = UMC.OPENID AND UM.MEMBERID=UMC.MEMBERID ",
		" LEFT JOIN B_CARDBAG C ON UMC.CARDID = C.CARDID ",
		" <where> ",
			"<if test=\"member.memberId!=null and member.memberId!=''\">",
				" AND UM.MEMBERID=#{member.memberId} ",
			"</if>",
			"<if test=\"member.cardType!=null and member.cardType>0\">",
				" AND C.CARDTYPE=#{member.cardType} ",
			"</if>",
			"<if test=\"member.cardNo!=null and member.cardNo!=''\">",
				" AND C.CARDNO=#{member.cardNo} ",
			"</if>",
			"<if test=\"member.channelId!=null and member.channelId!=''\">",
				" AND U.CHANNELID=#{member.channelId} ",
			"</if>",
			"<if test=\"member.openId!=null and member.openId!=''\">",
				" AND UM.OPENID=#{member.openId} ",
			"</if>",
			"<if test=\"member.hospitalNo!=null and member.hospitalNo!=''\">",
				" AND C.CARDNO=#{member.hospitalNo} ",
			"</if>",
			"<if test=\"member.memberName!=null and member.memberName!=''\">",
				" AND M.MEMBERNAME=#{member.memberName} ",
			"</if>",
			"<if test=\"member.memberNameLike!=null and member.memberNameLike!=''\">",
				" AND M.MEMBERNAME like CONCAT('%',#{member.memberNameLike},'%') ",
			"</if>",
			"<if test=\"member.idCardNo!=null and member.idCardNo!=''\">",
				" AND M.IDCARDNO=#{member.idCardNo} ",
			"</if>",
			"<if test=\"member.hosId!=null and member.hosId!=''\">",
				" AND UMC.HOSID=#{member.hosId} ",
			"</if>",
			"<if test=\"member.hisMemberId!=null and member.hisMemberId!=''\">",
				" AND UMC.HISMEMBERID=#{member.hisMemberId} ",
			"</if>",
			"<if test=\"member.mobile!=null and member.mobile!=''\">",
				" AND M.MOBILE=#{member.mobile} ",
			"</if>",
			"<if test=\"member.isDefault!=null and member.isDefault!=''\">",
				" AND UMC.ISDEFAULT=#{member.isDefault} ",
			"</if>",
			"<if test=\"member.isDefaultMember!=null and member.isDefaultMember!=''\">",
				" AND UM.ISDEFAULTMEMBER=#{member.isDefaultMember} ",
			"</if>",
		"</where>",
		" ORDER BY UM.ISDEFAULTMEMBER DESC,UMC.ISDEFAULT DESC",
	"</script>"})
	public List<Member> queryMemberList(@Param("member")Member member);
	
	/**
	 * 仅查询就诊人信息，不查询卡信息
	 * @param member
	 * @return
	 */
	@Select({"<script>",
		" SELECT ",
		 	" DISTINCT UM.MEMBERID,M.MEMBERNAME,M.MOBILE,M.IDCARDNO,M.SEX,M.BIRTHDATE,M.ADDRESS, ",	
		 	" M.CERTTYPE,M.CERTNUM,M.GUARDIANNAME,M.GUARDIANSEX,M.GUARDIANCERTTYPE, ",		
		 	" M.GUARDIANCERTNUM,M.ISCHILDREN,M.CREATETIME,M.NATION,UM.UPDATETIME, ",
		 	" UM.OPERATORID,UM.OPERATORNAME, ",
		 	" U.CHANNELID,U.OPENID,UM.ISDEFAULTMEMBER ",	
		" FROM B_USER_MEMBER UM ",
		" INNER JOIN B_MEMBER M ON UM.MEMBERID =  M.MEMBERID ",
		" INNER JOIN BAT_USER U ON U.OPENID = UM.OPENID ",
		" LEFT JOIN B_USER_MEMBER_CARD UMC ON UM.OPENID = UMC.OPENID AND UM.MEMBERID=UMC.MEMBERID ",
		" LEFT JOIN B_CARDBAG C ON UMC.CARDID = C.CARDID ",
		" <where> ",
			"<if test=\"member.memberId!=null and member.memberId!=''\">",
				" AND UM.MEMBERID=#{member.memberId} ",
			"</if>",
			"<if test=\"member.channelId!=null and member.channelId!=''\">",
				" AND U.CHANNELID=#{member.channelId} ",
			"</if>",
			"<if test=\"member.openId!=null and member.openId!=''\">",
				" AND UM.OPENID=#{member.openId} ",
			"</if>",
			"<if test=\"member.memberName!=null and member.memberName!=''\">",
				" AND M.MEMBERNAME=#{member.memberName} ",
			"</if>",
			"<if test=\"member.memberNameLike!=null and member.memberNameLike!=''\">",
				" AND M.MEMBERNAME like CONCAT('%',#{member.memberNameLike},'%') ",
			"</if>",
			"<if test=\"member.idCardNo!=null and member.idCardNo!=''\">",
				" AND M.IDCARDNO=#{member.idCardNo} ",
			"</if>",
			"<if test=\"member.mobile!=null and member.mobile!=''\">",
				" AND M.MOBILE=#{member.mobile} ",
			"</if>",
			"<if test=\"member.isDefaultMember!=null and member.isDefaultMember!=''\">",
				" AND UM.ISDEFAULTMEMBER=#{member.isDefaultMember} ",
			"</if>",
			"<if test=\"member.cardType!=null and member.cardType>0 and member.cardNo!=null and member.cardNo!='' \">",
				" AND C.CARDTYPE=#{member.cardType} AND C.CARDNO=#{member.cardNo} ",
			"</if>",
		"</where>",
		" ORDER BY UM.ISDEFAULTMEMBER DESC",
	"</script>"})
	public List<Member> queryUserMemberList(@Param("member")Member member);
	
	
	@Select({"<script>",
		" SELECT ",
			" UM.MEMBERID,M.MEMBERNAME,M.MOBILE,M.IDCARDNO,M.SEX,M.BIRTHDATE,M.ADDRESS, ",
			" M.CERTTYPE,M.CERTNUM,M.GUARDIANNAME,M.GUARDIANSEX,M.GUARDIANCERTTYPE, ",	
			" M.GUARDIANCERTNUM,M.ISCHILDREN,M.CREATETIME,M.NATION, ",
			" CASE WHEN UMC.UPDATETIME IS NOT NULL THEN UMC.UPDATETIME ELSE UM.UPDATETIME END UPDATETIME, ",
			" CASE WHEN UMC.OPERATORID IS NOT NULL THEN UMC.OPERATORID ELSE UM.OPERATORID END OPERATORID, ",
			" CASE WHEN UMC.OPERATORNAME IS NOT NULL THEN UMC.OPERATORNAME ELSE UM.OPERATORNAME END OPERATORNAME, ",			
			" C.CARDNO,C.CARDTYPE,C.CARDTYPENAME, ",
			" UMC.HOSID,UMC.HISMEMBERID,UMC.ISDEFAULT, ",
			" CASE WHEN C.CARDTYPE='14' THEN C.CARDNO ELSE NULL END HOSPITALNO, ",
			" U.CHANNELID,U.OPENID,UM.ISDEFAULTMEMBER ",
		" FROM B_USER_MEMBER UM ",
		" INNER JOIN B_MEMBER M ON UM.MEMBERID =  M.MEMBERID ",
		" INNER JOIN BAT_USER U ON U.OPENID = UM.OPENID ",
		" LEFT JOIN B_USER_MEMBER_CARD UMC ON UM.OPENID = UMC.OPENID AND UM.MEMBERID=UMC.MEMBERID ",
		" LEFT JOIN B_CARDBAG C ON UMC.CARDID = C.CARDID ",
		" <where> ",
			"<if test=\"member.memberId!=null and member.memberId!=''\">",
				" AND UM.MEMBERID=#{member.memberId} ",
			"</if>",
			"<if test=\"member.cardType!=null and member.cardType>0\">",
				" AND C.CARDTYPE=#{member.cardType} ",
			"</if>",
			"<if test=\"member.cardNo!=null and member.cardNo!=''\">",
				" AND C.CARDNO=#{member.cardNo} ",
			"</if>",
			"<if test=\"member.channelId!=null and member.channelId!=''\">",
				" AND U.CHANNELID=#{member.channelId} ",
			"</if>",
			"<if test=\"member.openId!=null and member.openId!=''\">",
				" AND UM.OPENID=#{member.openId} ",
			"</if>",
			"<if test=\"member.hospitalNo!=null and member.hospitalNo!=''\">",
				" AND C.CARDNO=#{member.hospitalNo} ",
			"</if>",
			"<if test=\"member.memberName!=null and member.memberName!=''\">",
				" AND M.MEMBERNAME=#{member.memberName} ",
			"</if>",
			"<if test=\"member.memberNameLike!=null and member.memberNameLike!=''\">",
				" AND M.MEMBERNAME like CONCAT('%',#{member.memberNameLike},'%') ",
			"</if>",
			"<if test=\"member.idCardNo!=null and member.idCardNo!=''\">",
				" AND M.IDCARDNO=#{member.idCardNo} ",
			"</if>",
			"<if test=\"member.hosId!=null and member.hosId!=''\">",
				" AND UMC.HOSID=#{member.hosId} ",
			"</if>",
			"<if test=\"member.hisMemberId!=null and member.hisMemberId!=''\">",
				" AND UMC.HISMEMBERID=#{member.hisMemberId} ",
			"</if>",
			"<if test=\"member.mobile!=null and member.mobile!=''\">",
				" AND M.MOBILE=#{member.mobile} ",
			"</if>",
			"<if test=\"member.isDefault!=null and member.isDefault!=''\">",
				" AND UMC.ISDEFAULT=#{member.isDefault} ",
			"</if>",
			"<if test=\"member.isDefaultMember!=null and member.isDefaultMember!=''\">",
				" AND UM.ISDEFAULTMEMBER=#{member.isDefaultMember} ",
			"</if>",
		"</where>",
	"</script>"})
	public Member queryMemberInfo(@Param("member")Member member);
	
//	@Select({"<script>",
//		" SELECT ",
//			" DISTINCT m.MEMBERID,m.MEMBERNAME,m.MOBILE,m.IDCARDNO,m.SEX,m.BIRTHDATE,m.ADDRESS, ",
//			" m.CERTTYPE,m.CERTNUM,m.GUARDIANNAME,m.GUARDIANSEX,m.GUARDIANCERTTYPE, ",
//			" m.GUARDIANCERTNUM,m.ISCHILDREN,m.CREATETIME,m.NATION,m.UPDATETIME, ",
//			" m.OPERATORID,m.OPERATORNAME,u.CHANNELID,u.OPENID,m.ISDEFAULTMEMBER ",
//		" FROM BAT_USER u ",
//		" INNER JOIN B_USER_MEMBER um ON u.OPENID=um.OPENID ",
//		" INNER JOIN  B_MEMBER m ON m.MEMBERID=um.MEMBERID ",
//		" INNER JOIN B_PATIENT p ON m.MEMBERID=p.MEMBERID ",
//		"<where>",
//			"<if test=\"member.memberId!=null and member.memberId!=''\"> ",
//				" AND um.MEMBERID=#{member.memberId} ",
//			"</if> ",
//			"<choose>",
//				"<when test=\"member.state!=null and member.state!=0\" >",
//					" AND p.STATE=#{member.state} ",
//				"</when>", 
//				"<otherwise>", 
//					" AND p.STATE=1 " ,
//				"</otherwise>", 
//			"</choose>", 
//			"<if test=\"member.cardType!=null and member.cardType>0\">",
//				"AND p.CARDTYPE=#{member.cardType} ",
//			"</if>",
//			"<if test=\"member.cardNo!=null and member.cardNo!=''\">",
//				"AND p.CARDNO=#{member.cardNo} ",
//			"</if>",
//			"<if test=\"member.channelId!=null and member.channelId!=''\">",
//				"AND u.CHANNELID=#{member.channelId} ",
//			"</if>",
//			"<if test=\"member.openId!=null and member.openId!=''\">",
//				"AND u.OPENID=#{member.openId} ",
//			"</if>",
//			"<if test=\"member.hospitalNo!=null and member.hospitalNo!=''\">",
//				"AND p.HOSPITALNO=#{member.hospitalNo} ",
//			"</if>",
//			"<if test=\"member.memberName!=null and member.memberName!=''\">",
//				"AND m.MEMBERNAME=#{member.memberName} ",
//			"</if>",
//			"<if test=\"member.memberNameLike!=null and member.memberNameLike!=''\">",
//				"AND m.MEMBERNAME like CONCAT('%',#{member.memberNameLike},'%') ",
//			"</if>",
//			"<if test=\"member.idCardNo!=null and member.idCardNo!=''\">",
//				"AND m.IDCARDNO=#{member.idCardNo} ",
//			"</if>",
//			"<if test=\"member.mcardNo!=null and member.mcardNo!=''\">",
//				"AND p.MCARDNO=#{member.mcardNo} ",
//			"</if>",
//			"<if test=\"member.hosId!=null and member.hosId!=''\">",
//				"AND p.HOSID=#{member.hosId} ",
//			"</if>",
//			"<if test=\"member.hisMemberId!=null and member.hisMemberId!=''\">",
//				"AND p.HISMEMBERID=#{member.hisMemberId} ",
//			"</if>",
//			"<if test=\"member.accountId!=null and member.accountId!=''\">",
//				"AND p.ACCOUNTID=#{member.accountId} ",
//			"</if>",
//			"<if test=\"member.mobile!=null and member.mobile!=''\">",
//				"AND m.MOBILE=#{member.mobile} ",
//			"</if>",
//			"<if test=\"member.isDefault!=null and member.isDefault!=''\">",
//				"AND p.ISDEFAULT=#{member.isDefault} ",
//			"</if>",
//			"<if test=\"member.isDefaultMember!=null and member.isDefaultMember!=''\">",
//				"AND m.ISDEFAULTMEMBER=#{member.isDefaultMember} ",
//			"</if>",
//			"<if test=\"member.hisMemberId!=null and member.hisMemberId!=''\">",
//				"AND p.hisMemberId=#{member.hisMemberId} ",
//			"</if>",
//		"</where>",
//		" ORDER BY m.ISDEFAULTMEMBER DESC",
//	"</script>"})
//	public List<Member> queryMemberBaseList(@Param("member")Member member);
//	@Select({"<script>",
//		" SELECT COUNT(MEMBERID) FROM (SELECT m.MEMBERID FROM BAT_USER u ",
//		" INNER JOIN B_USER_MEMBER um ON u.OPENID=um.OPENID ",
//		" INNER JOIN  B_MEMBER m ON m.MEMBERID=um.MEMBERID ",
//		" INNER JOIN B_PATIENT p ON m.MEMBERID=p.MEMBERID ",
//		" WHERE u.OPENID = #{openId} AND p.STATE=1 ",
//		" GROUP BY m.MEMBERID ) t",
//		"</script>"})
//	public Integer getMemberCount(@Param("openId")String openId) ;
	
}

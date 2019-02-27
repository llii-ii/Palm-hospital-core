package com.kasite.client.qywechat.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.core.serviceinterface.module.qywechat.dbo.MemberCreditsInfo;
import com.kasite.core.serviceinterface.module.qywechat.dto.MemberCreditsVo;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqMember;

import tk.mybatis.mapper.common.Mapper;

/**
 * 成员积分DAO对象
 * 
 * @author 無
 *
 */
public interface MemberCreditsInfoDao extends Mapper<MemberCreditsInfo> {

	@Select({"<script>",
		"SELECT m.MEMBERID,m.MEMBERNAME,IFNULL(SUM(mci.CREDITS),0) AS allCredits FROM QY_MEMBER m ",
		"LEFT JOIN QY_MEMBER_CREDITS_INFO mci ON mci.MEMBERID = m.MEMBERID ",
		"<where>",
		"<if test=\"req.membername!=null and req.membername!='' \">",
			"AND m.MEMBERNAME LIKE concat('%',#{req.membername},'%') ",
		"</if>",
		"<if test=\"req.memberid!=null and req.memberid!='' \">",
			"AND m.MEMBERID = #{req.memberid} ",
		"</if>",
		"</where>",
		"GROUP BY m.MEMBERID,m.MEMBERNAME ",
		"</script>"
	})
	List<MemberCreditsVo> queryMemberCreditsList(@Param("req")ReqMember req) throws SQLException;
	
}

package com.kasite.client.qywechat.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.core.serviceinterface.module.qywechat.dbo.ToMember;
import com.kasite.core.serviceinterface.module.qywechat.dto.ToMemberVo;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqToMemberQuery;

import tk.mybatis.mapper.common.Mapper;

/**
 * 参与人DAO对象
 * 
 * @author 無
 *
 */
public interface ToMemberDao extends Mapper<ToMember>{
	
	@Select({"<script>",
		"SELECT ID FROM QY_TO_MEMBER S1 WHERE ",
		"S1.MEMBERTYPE = #{type} AND ",
		"((S1.MEMBERID = #{memberid}  AND ISDEPT=0) ",
		"<if test=\"deptIds!=null and deptIds!='' \">",
			"OR (S1.MEMBERID IN (${deptIds}) AND ISDEPT=1) ",
		"</if>",
		") ",
		"</script>"
	})
	List<ToMember> applyMeetingPower(@Param("memberid")String memberid,@Param("deptIds")String deptIds,@Param("type")Integer type) throws SQLException;
	
	
	@Select({"<script>",
		"SELECT * FROM QY_TO_MEMBER tm ",
		"LEFT JOIN QY_MEMBER m ON tm.MEMBERID = m.MEMBERID ",
		"<where>",
		"<if test=\"req.memberType!=null and req.memberType!='' \">",
			"AND tm.MEMBERTYPE = #{req.memberType} ",
		"</if>",
		"<if test=\"req.uid!=null and req.uid!='' \">",
			"AND tm.UID = #{req.uid} ",
		"</if>",
		"<if test=\"req.membername!=null and req.membername!='' \">",
			"AND tm.MEMBERNAME LIKE concat('%',#{req.membername},'%') ",
		"</if>",
		"</where>",
		"</script>"
	})
	List<ToMemberVo> queryToMemberAndDept(@Param("req")ReqToMemberQuery req) throws SQLException;

	
}

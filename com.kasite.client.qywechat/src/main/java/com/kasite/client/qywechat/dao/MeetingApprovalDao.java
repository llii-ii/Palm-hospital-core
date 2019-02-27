package com.kasite.client.qywechat.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.core.serviceinterface.module.qywechat.dbo.MeetingApproval;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqMeetingApproval;

import tk.mybatis.mapper.common.Mapper;

/**
 * 会议-事项审批DAO对象
 * @author 無
 *
 */
public interface MeetingApprovalDao extends Mapper<MeetingApproval>{

	@Select({"<script>",
		"SELECT * FROM QY_MEETING_APPROVAL ",
		"<where>",
			"<if test=\"req.id!=null and req.id!=-1 \">",
				"AND ID = #{req.id} ",
			"</if>",
			"<if test=\"req.status!=null and req.status!=-1 \">",
				"AND STATUS = #{req.status} ",
			"</if>",
			"<if test=\"req.approvalid!=null and req.approvalid!='' \">",
				"AND APPROVALID LIKE concat('%',#{req.approvalid},'%') ",
			"</if>",
			"<if test=\"req.startTime!=null and req.startTime!='' \">",
				"AND INSERTTIME &gt;= #{req.startTime} ",
			"</if>",
			"<if test=\"req.endTime!=null and req.endTime!='' \">",
				"AND INSERTTIME &lt;= DATE_ADD(#{req.endTime},INTERVAL 1 DAY) ",
			"</if>",
			"<if test=\"req.membername!=null and req.membername!='' \">",
				"AND MEMBERNAME LIKE concat('%',#{req.membername},'%') ",
			"</if>",
			"<if test=\"req.meetingid!=null and req.meetingid!='' \">",
				"AND MEETINGID = #{req.meetingid} ",
			"</if>",
			"<if test=\"req.memberid!=null and req.memberid!='' \">",
				"AND MEMBERID = #{req.memberid} ",
			"</if>",
		"</where>",
		"ORDER BY INSERTTIME ASC",
		"</script>"
	})
	List<MeetingApproval> queryMeetingApproval(@Param("req")ReqMeetingApproval req) throws SQLException;
	
}

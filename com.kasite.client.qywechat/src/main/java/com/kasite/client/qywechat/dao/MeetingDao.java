package com.kasite.client.qywechat.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.core.serviceinterface.module.qywechat.dbo.Meeting;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqMeeting;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqMeetingAdd;

import tk.mybatis.mapper.common.Mapper;

/**
 * 会议DAO对象
 * @author 無
 *
 */
public interface MeetingDao extends Mapper<Meeting>{

	@Select({"<script>",
		"SELECT * FROM QY_MEETING ",
		"<where>",
			"<if test=\"req.id!=null and req.id!=-1 \">",
				"AND ID = #{req.id} ",
			"</if>",
			"<if test=\"req.meetingid!=null and req.meetingid!='' \">",
				"AND MEETINGID = #{req.meetingid} ",
			"</if>",
			"<if test=\"req.status!=null and req.status!=-1 \">",
				"AND STATUS = #{req.status} ",
			"</if>",
			"<if test=\"req.meetingdate!=null and req.meetingdate!='' \">",
				"AND MEETINGDATE = #{req.meetingdate} ",
			"</if>",
			"<if test=\"req.timestart!=null and req.timestart!='' \">",
				"AND TIMESTART &gt;= #{req.timestart} ",
//				"<![CDATA[ AND TIMESTART >= #{req.timestart} ]]>",
			"</if>",
			"<if test=\"req.timeend!=null and req.timeend!='' \">",
				"AND TIMEEND &lt;= #{req.timeend} ",
//				"<![CDATA[ AND TIMEEND <= #{req.timeend} ]]>",
			"</if>",
			"<if test=\"req.title!=null and req.title!='' \">",
				"AND TITLE LIKE concat('%',#{req.title},'%') ",
			"</if>",
		"</where>",
		"ORDER BY INSERTTIME DESC",
		"</script>"
	})
	List<Meeting> queryMeetingList(@Param("req")ReqMeeting req) throws SQLException;
	
	
	
	@Select({"<script>",
		"SELECT * FROM QY_MEETING WHERE MEETINGID IN ",
		"(SELECT UID FROM QY_TO_MEMBER S1 WHERE ",
		"S1.MEMBERTYPE = 0 AND ",
		"((S1.MEMBERID = #{req.openId}  AND ISDEPT=0) ",
		"<if test=\"req.deptIds!=null and req.deptIds!='' \">",
			"OR (S1.MEMBERID IN (${req.deptIds}) AND ISDEPT=1) ",
		"</if>",
		")) ",
		"<if test=\"req.title!=null and req.title!='' \">",
			"AND TITLE LIKE concat('%',#{req.title},'%') ",
		"</if>",
		"<if test=\"req.type == 1 \">",
			"AND (MEETINGDATE &gt; #{req.meetingdate} OR (MEETINGDATE = #{req.meetingdate} AND TIMESTART &gt;= #{req.timestart})) ",
		"</if>",
		"<if test=\"req.type == 2 \">",
			"AND (MEETINGDATE &lt; #{req.meetingdate} OR (MEETINGDATE = #{req.meetingdate} AND TIMEEND &lt;= #{req.timeend})) ",
		"</if>",
		"<if test=\"req.type == 3 \">",
			"<if test=\"req.operatorId!=null and req.operatorId!='' \">",
				"AND OPERATORID = #{req.operatorId} ",
			"</if>",
		"</if>",
		"<if test=\"req.type == 4 \">",
			"AND MEETINGDATE = #{req.meetingdate} AND TIMEEND &gt;= #{req.timeend} AND TIMESTART &lt;= #{req.timestart} ",
		"</if>",
		"</script>"
	})
	List<Meeting> qwQueryMeetingList(@Param("req")ReqMeeting req) throws SQLException;


	@Select({"<script>",
		"SELECT ID FROM QY_MEETING ",
		"<where>",
		"<if test=\"req.meetingdate!=null and req.meetingdate!='' \">",
			"AND MEETINGDATE = #{req.meetingdate} ",
		"</if>",
		"<if test=\"req.timestart!=null and req.timestart!='' \">",
			"AND TIMESTART &lt;= #{req.timestart} ",
		"</if>",
		"<if test=\"req.timeend!=null and req.timeend!='' \">",
			"AND TIMEEND &gt;= #{req.timeend} ",
		"</if>",
		"<if test=\"req.addressid!=null and req.addressid!='' \">",
			"AND ADDRESSID = #{req.addressid} ",
		"</if>",
		"</where>",
		"</script>"
	})
	List<Meeting> roomIsUse(@Param("req")ReqMeetingAdd req) throws SQLException;
	
}

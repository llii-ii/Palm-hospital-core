package com.kasite.client.qywechat.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.core.serviceinterface.module.qywechat.dbo.MeetingSign;
import com.kasite.core.serviceinterface.module.qywechat.dto.MeetingSignVo;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqMeetingSign;

import tk.mybatis.mapper.common.Mapper;

/**
 * 会议-签到、签退DAO对象
 * 
 * @author 無
 *
 */
public interface MeetingSignDao extends Mapper<MeetingSign> {

	@Select({"<script>",
		"SELECT * FROM (",
		"SELECT S1.UID,S1.ISDEPT,S1.MEMBERID,S1.MEMBERNAME,S6.DEPTNAME,",
		"CASE S2.MEMBERTYPE WHEN 2 THEN '参加'  WHEN 3 THEN '请假' ELSE '待定' END AS MEMBERTYPE,S3.REASON,",
		"CASE S4.SIGNTYPE WHEN 0 THEN '已签到' ELSE '未签到' END AS signInType,S4.INSERTTIME AS signInTime ,",
		"CASE S5.SIGNTYPE WHEN 1 THEN '已签退' ELSE '未签退' END AS signBackType,S5.INSERTTIME AS signBackTime,S5.PLACE ",
		"FROM QY_TO_MEMBER S1 ",
		"LEFT JOIN QY_MEMBER S6 ON S1.MEMBERID=S6.MEMBERID ",
		"LEFT JOIN QY_TO_MEMBER S2 ON S1.UID=S2.UID AND S1.MEMBERID = S2.MEMBERID  AND S2.MEMBERTYPE IN(2,3) ",
		"LEFT JOIN QY_MEETING_APPROVAL S3 ON S1.UID=S3.MEETINGID AND S1.MEMBERID = S3.MEMBERID AND STATUS=1 ",
		"LEFT JOIN QY_MEETING_SIGN S4 ON S1.UID=S4.UID AND S1.MEMBERID = S4.MEMBERID AND S4.SIGNTYPE=0 ",
		"LEFT JOIN QY_MEETING_SIGN S5 ON S1.UID=S5.UID AND S1.MEMBERID = S5.MEMBERID AND S5.SIGNTYPE=1 ",
		"WHERE S1.MEMBERTYPE = 0 ",
		"<if test=\"req.memberType!=null and req.memberType!='' \">",
			"AND S2.MEMBERTYPE = #{req.memberType} ",
		"</if>",		
		"<if test=\"req.membername!=null and req.membername!='' \">",
			"AND S1.MEMBERNAME LIKE concat('%',#{req.membername},'%') ",
		"</if>",
		"AND S1.UID=#{req.uid} ) AS S ",
		"<where>",
		"<if test=\"req.signIn!=null and req.signIn!='' \">",
			"AND S.signInType = #{req.signIn} ",
		"</if>",
		"<if test=\"req.signBack!=null and req.signBack!='' \">",
			"AND S.signBackType = #{req.signBack} ",
		"</if>",
		"</where>",
		"</script>"
	})
	List<MeetingSignVo> queryMeetingSign(@Param("req")ReqMeetingSign req) throws SQLException;
	
	
	
	@Select({"<script>",
		"SELECT S1.MEMBERID,S1.MEMBERNAME,S1.DEPTNAME,",
		"CASE S2.MEMBERTYPE WHEN 2 THEN '参加'  WHEN 3 THEN '请假' ELSE '待定' END AS MEMBERTYPE,S3.REASON,",
		"CASE S4.SIGNTYPE WHEN 0 THEN '已签到' ELSE '未签到' END AS signInType,S4.INSERTTIME AS signInTime ,",
		"CASE S5.SIGNTYPE WHEN 1 THEN '已签退' ELSE '未签退' END AS signBackType,S5.INSERTTIME AS signBackTime ",
		"FROM QY_MEMBER S1 ",
		"LEFT JOIN QY_TO_MEMBER S2 ON #{req.uid}=S2.UID AND S1.MEMBERID = S2.MEMBERID  AND S2.MEMBERTYPE IN(2,3) ",
		"LEFT JOIN QY_MEETING_SIGN S4 ON #{req.uid}=S4.UID AND S1.MEMBERID = S4.MEMBERID AND S4.SIGNTYPE=0 ",		
		"LEFT JOIN QY_MEETING_SIGN S5 ON #{req.uid}=S5.UID AND S1.MEMBERID = S5.MEMBERID AND S5.SIGNTYPE=1 ",		
		"LEFT JOIN QY_MEETING_APPROVAL S3 ON #{req.uid}=S3.MEETINGID AND S1.MEMBERID = S3.MEMBERID AND STATUS=1 ",		
		"WHERE S1.MEMBERID = #{req.memberid}",	
		"</script>"
	})
	List<MeetingSignVo> meetingSignDetail(@Param("req")ReqMeetingSign req) throws SQLException;
	
}

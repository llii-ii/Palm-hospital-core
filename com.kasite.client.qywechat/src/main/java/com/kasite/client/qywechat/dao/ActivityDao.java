package com.kasite.client.qywechat.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.core.serviceinterface.module.qywechat.dbo.Activity;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqActivity;

import tk.mybatis.mapper.common.Mapper;

/**
 * 活动DAO对象
 * 
 * @author 無
 *
 */
public interface ActivityDao extends Mapper<Activity> {

	@Select({"<script>",
		"SELECT * FROM QY_ACTIVITY WHERE STATUS !=10 ",
			"<if test=\"req.id!=null and req.id!=-1 \">",
				"AND ID = #{req.id} ",
			"</if>",
			"<if test=\"req.activityid!=null and req.activityid!='' \">",
				"AND ACTIVITYID = #{req.activityid} ",
			"</if>",
			"<if test=\"req.status!=null and req.status!=-1 \">",
				"AND STATUS = #{req.status} ",
			"</if>",
			"<if test=\"req.starttime!=null and req.starttime!='' \">",
				"AND STARTTIME &gt;= #{req.starttime} ",
			"</if>",
			"<if test=\"req.endtime!=null and req.endtime!='' \">",
				"AND ENDTIME &lt;= #{req.endtime} ",
			"</if>",
			"<if test=\"req.title!=null and req.title!='' \">",
				"AND TITLE LIKE concat('%',#{req.title},'%') ",
			"</if>",
		"ORDER BY INSERTTIME ASC",
		"</script>"
	})
	List<Activity> queryActivityList(@Param("req")ReqActivity req) throws SQLException;
	
	

	@Select({"<script>",
		"SELECT * FROM QY_ACTIVITY WHERE (STATUS !=10 AND ACTIVITYID IN ",
		"(SELECT DISTINCT UID FROM QY_TO_MEMBER TM ",
		"WHERE TM.MEMBERTYPE = 0 AND ",
		"((TM.MEMBERID = #{req.openId}  AND ISDEPT=0) ",
		"<if test=\"req.deptIds!=null and req.deptIds!='' \">",
			"OR (TM.MEMBERID IN (#{req.deptIds}) AND ISDEPT=1) ",
		"</if>",
		"))) ",
		"OR (STATUS !=10 AND OPERATORID = #{req.openId})",
		"<if test=\"req.title!=null and req.title!='' \">",
			"AND TITLE LIKE concat('%',#{req.title},'%') ",
		"</if>",
		"ORDER BY INSERTTIME DESC",
		"</script>"
	})
	List<Activity> qwQueryActivity(@Param("req")ReqActivity req) throws SQLException;
}

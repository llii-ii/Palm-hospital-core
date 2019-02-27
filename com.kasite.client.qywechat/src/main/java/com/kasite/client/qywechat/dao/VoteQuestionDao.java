package com.kasite.client.qywechat.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.core.serviceinterface.module.qywechat.dbo.VoteQuestion;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqUID;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqVoteQuestionQuery;
import com.kasite.core.serviceinterface.module.qywechat.resp.RespVoteQuestion;
import com.kasite.core.serviceinterface.module.qywechat.resp.RespVoteQuestionAnalyze;

import tk.mybatis.mapper.common.Mapper;

/**
 * 投票、问卷DAO对象
 * 
 * @author 無
 *
 */
public interface VoteQuestionDao extends Mapper<VoteQuestion> {
	@Select({"<script>"
			+"SELECT * FROM QY_VOTE_QUESTION WHERE STATUS!=3 " 
			+"<if test='req.themeType!=null'> AND THEMETYPE=#{req.themeType} </if>"
			+"<if test='req.status!=null ' > AND STATUS=#{req.status} </if>"
			+"<if test='req.title!=null and req.title!=&quot;&quot;' > AND TITLE LIKE concat('%',#{req.title},'%')  </if>"
			+"<if test='req.startTimeFrom!=null and req.startTimeFrom!=&quot;&quot;' > "
			+"	<![CDATA[ AND INSERTTIME >= str_to_date(#{req.startTimeFrom},'%Y-%m-%d') ]]> " 
			+ "</if>"
			+"<if test='req.startTimeTo!=null and req.startTimeTo!=&quot;&quot;' > "
			+"	<![CDATA[ AND INSERTTIME <= str_to_date(#{req.startTimeTo},'%Y-%m-%d') ]]> " 
			+ "</if>"
			+"<if test='req.endTimeFrom!=null and req.endTimeFrom!=&quot;&quot;' > "
			+"	<![CDATA[ AND ENDTIME >= str_to_date(#{req.endTimeFrom},'%Y-%m-%d') ]]> " 
			+ "</if>"
			+"<if test='req.endTimeTo!=null and req.endTimeTo!=&quot;&quot;' > "
			+"	<![CDATA[ AND ENDTIME <= str_to_date(#{req.endTimeTo},'%Y-%m-%d') ]]> " 
			+ "</if>"
			+" ORDER BY INSERTTIME DESC"
			+"</script>"
	})
	List<RespVoteQuestion> queryVoteQuestionList(@Param("req")ReqVoteQuestionQuery req) throws SQLException;
	
	@Select({"<script> " +
			"SELECT DISTINCT S1.ID,S1.THEMEID,S1.QUESTNAME,S1.QUESTTYPE,S1.SORTNUM,S2.ITEMVALUE,S2.SORTNUM AS SORTNUM2, " + 
			"S3.ANSWER,ifnull(S6.NUM,0) AS NUM, ROUND(ROUND(1/S5.COUNT,4)*100*ifnull(S6.NUM,0),2) AS LV " + 
			"FROM QY_QUESTION S1 LEFT JOIN QY_QUESTION_ITEM S2 ON S1.THEMEID=S2.THEMEID AND S1.ID=S2.QUESTID " + 
			"LEFT JOIN QY_QUESTION_ANSWER S3 ON S1.THEMEID=S3.THEMEID AND S1.ID=S3.QUESTID " + 
			"AND CASE WHEN S1.QUESTTYPE=0 OR S1.QUESTTYPE=1 THEN S2.ID=S3.ANSWER " + 
			"WHEN S1.QUESTTYPE=2 OR S1.QUESTTYPE=3 THEN 1=1 END " + 
			"LEFT JOIN (select S4.QUESTID,COUNT(S4.OPERATORID) AS COUNT FROM QY_QUESTION_ANSWER S4 GROUP BY S4.QUESTID) S5 ON S1.ID=S5.QUESTID " + 
			"LEFT JOIN (select QUESTID,ANSWER,COUNT(OPERATORID) AS NUM FROM QY_QUESTION_ANSWER GROUP BY QUESTID,ANSWER) S6 ON S1.ID=S6.QUESTID AND S3.ANSWER=S6.ANSWER " + 
			"<where>" +
			"<if test='req.id!=null' > AND S1.THEMEID=#{req.id} </if>" +
			"</where>" +
			"ORDER BY S1.ID,S1.SORTNUM ASC,S2.SORTNUM ASC,S3.ANSWER ASC "+
			"</script>"
	})
	List<RespVoteQuestionAnalyze> getVoteQuestionAnalyzes(@Param("req")ReqUID req) throws SQLException;
	
	
	
	@Select({"<script>" + 
			"SELECT S1.*,IFNULL(S2.VOTED,0) AS VOTED FROM QY_VOTE_QUESTION S1 "
			+ "LEFT JOIN (select THEMEID,COUNT(*) AS VOTED from QY_QUESTION_ANSWER WHERE OPERATORID=#{req.openId} GROUP BY THEMEID) S2 ON S1.UID=S2.THEMEID "
			+ "WHERE S1.STATUS!=3 AND S1.STATUS!=0 AND S1.THEMETYPE=#{req.themeType} AND S1.UID IN( " + 
			"SELECT distinct S3.UID FROM QY_TO_MEMBER S3 " + 
			"WHERE ((S3.MEMBERID = #{req.openId}  AND S3.ISDEPT=0) " +
			"<if test=\"deptIds!=null and deptIds!='' \">" +
				"OR (S3.MEMBERID IN "+
				 "<foreach collection='deptIds' item='deptId' open='(' separator=',' close=')'>"+
                 "#{deptId}"+
                 "</foreach>"+
				 "AND S3.ISDEPT=1)) " +
			"</if>" +
			"AND S3.MEMBERTYPE = 0 ) " +
			"<if test='req.title!=null and req.title!=&quot;&quot;' > AND TITLE LIKE concat('%',#{req.title},'%')  </if>" +
			"ORDER BY S1.STATUS ASC,S1.INSERTTIME DESC " + 
			"</script>"
	})		
	List<RespVoteQuestion> queryVoteQuestionListForQY(@Param("req")ReqVoteQuestionQuery req,@Param("deptIds")String[] deptIds) throws SQLException;

	
	@Select({"<script>" + 
			"SELECT S1.*,IFNULL(S2.VOTED,0) AS VOTED FROM QY_VOTE_QUESTION S1 "
			+ "LEFT JOIN (select THEMEID,COUNT(*) AS VOTED from QY_QUESTION_ANSWER WHERE OPERATORID=#{req.openId} GROUP BY THEMEID) S2 ON S1.UID=S2.THEMEID "
			+ "WHERE S1.STATUS!=3 AND S1.THEMETYPE=#{req.themeType} AND S1.OPERATORID=#{req.openId}" +
			"<if test='req.title!=null and req.title!=&quot;&quot;' > AND TITLE LIKE concat('%',#{req.title},'%')  </if>" +
			"ORDER BY S1.STATUS ASC,S1.INSERTTIME DESC " + 
			"</script>"
	})		
	List<RespVoteQuestion> queryVoteQuestionListForQYByPower(@Param("req")ReqVoteQuestionQuery req) throws SQLException;
}

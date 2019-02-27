package com.kasite.client.survey.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.client.survey.bean.dbo.SampleAnswer;
import com.kasite.client.survey.bean.dto.SampleAnswerVo;
import com.kasite.client.survey.bean.dto.SampleInfoVo;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @className: ISampleAnswerMapper
 * @author: lcz
 * @date: 2018年7月26日 下午1:57:51
 */
public interface ISampleAnswerMapper extends Mapper<SampleAnswer>{

	@Select("SELECT COUNT(*) FROM SV_SAMPLEANSWER A,SV_SAMPLE B WHERE A.SAMPLEID = B.SAMPLEID AND B.STATUS=2 AND A.QUESTID = #{questId}")
	Integer querySingleQuestionTotal(Integer questId);
	
	@Select("SELECT COUNT(DISTINCT A.SAMPLEID) FROM SV_SAMPLEANSWER A,SV_SAMPLE B WHERE A.SAMPLEID = B.SAMPLEID AND B.STATUS=2 AND A.QUESTID = #{questId}")
	Integer queryCountAnswerByQuestId(Integer questId);
	
	@Select("SELECT COUNT(*) FROM SV_SAMPLEANSWER A,SV_SAMPLE B WHERE A.SAMPLEID = B.SAMPLEID AND B.STATUS=2 AND A.ANSWER = #{itemId}")
	Integer queryCountAnswerByItemIdEquals(String itemId);
	
	@Select("SELECT COUNT(*) FROM SV_SAMPLEANSWER A,SV_SAMPLE B WHERE A.SAMPLEID = B.SAMPLEID AND B.STATUS=2 AND A.ANSWER LIKE #{itemId}")
	Integer queryCountAnswerByItemIdLike(String itemId);
	
	@Select("SELECT A.ANSWER FROM SV_SAMPLEANSWER A,SV_SAMPLE B WHERE A.SAMPLEID = B.SAMPLEID AND B.STATUS=2 AND A.QUESTID = #{questId}")
	List<SampleAnswer> queryAnswerByQuestId(Integer questId);
	
	@Select("SELECT COUNT(DISTINCT A.SAMPLEID) FROM SV_SAMPLEANSWER A,SV_SAMPLE B WHERE A.SAMPLEID = B.SAMPLEID AND B.STATUS=2 AND A.QUESTID IN(SELECT QUESTID FROM SV_QUESTION WHERE MATRIXQUESTID = #{matrixQuestId})")
	Integer queryCountAnswerByMatrixQuestId(Integer matrixQuestId);
	
	@Select("SELECT A.answer FROM SV_SAMPLEANSWER A, SV_SAMPLE B WHERE A.SAMPLEID = B.SAMPLEID AND  B.STATUS=2 AND A.QUESTID = #{questId} AND A.SAMPLEID = #{sampleId}")
	List<SampleAnswer> queryAnswer(@Param("questId")Integer questId, @Param("sampleId")String sampleId);
	
	@Select("SELECT A.answer FROM SV_SAMPLEANSWER A, SV_SAMPLE B WHERE A.SAMPLEID = B.SAMPLEID AND  B.STATUS=2 AND A.QUESTID = #{questId} AND A.SAMPLEID = #{sampleId}")
	List<SampleAnswerVo> queryAnswers(@Param("questId")Integer questId, @Param("sampleId")String sampleId);
	
	@Select({"<script>",
		"SELECT B.itemId, A.otherAnswer,B.ITEMCONT AS itemCount,B.ifAddBlank FROM SV_SAMPLEANSWER A,SV_QUESTIONITEM B,SV_SAMPLE C ",
		"<where>",
			" AND A.QUESTID = B.QUESTID AND A.SAMPLEID=#{sampleId} AND A.SAMPLEID=C.SAMPLEID AND C.STATUS=2 AND A.QUESTID=#{questId}",
			"<if test=\"questType == 1\">",
				" AND B.ITEMID=#{itemId}",
			"</if>" ,
			"<if test=\"questType != 1\">",
				" AND LOCATE(B.ITEMID, #{itemId}) &gt; 0",
			"</if>" ,
		"</where>",
		" ORDER BY B.SORTNUM",
		"</script>"})
	List<SampleInfoVo> querySampleInfoById(@Param("questId")Integer questId, @Param("sampleId")String sampleId, 
			@Param("itemId")String itemId, @Param("questType")int questType);
}

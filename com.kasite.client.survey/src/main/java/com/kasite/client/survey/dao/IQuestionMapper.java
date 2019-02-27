package com.kasite.client.survey.dao;

import org.apache.ibatis.annotations.Select;

import com.kasite.client.survey.bean.dbo.Question;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @className: IQuestionMapper
 * @author: lcz
 * @date: 2018年7月26日 上午10:08:41
 */
public interface IQuestionMapper extends Mapper<Question>{
	
	@Select("SELECT SEQ_NEXTVAL(#{seqName}) FROM DUAL")
	int getSequence(String seqName);
	
	@Select("SELECT MAX(SortNum)+1 FROM  SV_QUESTIONITEM WHERE QuestId=#{questionId}")
	Integer selectMaxSort(Integer questionId);
	
}

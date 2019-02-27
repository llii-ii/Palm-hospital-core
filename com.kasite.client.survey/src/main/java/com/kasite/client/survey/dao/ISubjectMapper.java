package com.kasite.client.survey.dao;

import org.apache.ibatis.annotations.Select;

import com.kasite.client.survey.bean.dbo.Subject;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @className: ISubjectMapper
 * @author: lcz
 * @date: 2018年7月25日 下午8:15:19
 */
public interface ISubjectMapper extends Mapper<Subject>{
	
	@Select("SELECT MAX(SortNum)+1 FROM  SV_QUESTION WHERE SUBJECTID=#{subjectId}")
	Integer selectMaxSort(Integer subjectId);
	
	@Select("SELECT SEQ_NEXTVAL(#{seqName}) FROM DUAL")
	int getSequence(String seqName);
}

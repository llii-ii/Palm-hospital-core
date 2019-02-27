package com.kasite.client.survey.dao;

import org.apache.ibatis.annotations.Select;

import com.kasite.client.survey.bean.dbo.Sample;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @className: ISampleMapper
 * @author: lcz
 * @date: 2018年7月26日 上午11:50:34
 */
public interface ISampleMapper extends Mapper<Sample>{
	
	@Select("select SEQ_NEXTVAL(#{seqName}) from dual")
	int getSequence(String seqName);
	
}

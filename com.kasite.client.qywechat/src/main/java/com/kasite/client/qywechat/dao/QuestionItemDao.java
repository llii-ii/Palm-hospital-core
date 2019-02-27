package com.kasite.client.qywechat.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.core.serviceinterface.module.qywechat.dbo.QuestionItem;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqUID;

import tk.mybatis.mapper.common.Mapper;

/**
 * 问题DAO对象
 * 
 * @author 無
 *
 */
public interface QuestionItemDao extends Mapper<QuestionItem> {

	@Select({"<script>"
			+"SELECT * FROM QY_QUESTION_ITEM " 
			+"<where>"
			+"<if test='req.id!=null and req.tag==null'> AND QUESTID=#{req.id} </if>"
			+"<if test='req.id!=null and req.tag==&quot;uid&quot;'> AND THEMEID=#{req.id} </if>"
			+"</where>"
			+" AND STATUS = 0 "
			+" ORDER BY SORTNUM ASC"
			+"</script>"
	})
	List<QuestionItem> queryQuestionItemList(@Param("req")ReqUID req) throws SQLException;
}

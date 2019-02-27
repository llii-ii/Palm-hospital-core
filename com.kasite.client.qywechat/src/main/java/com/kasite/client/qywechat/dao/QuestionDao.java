package com.kasite.client.qywechat.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.core.serviceinterface.module.qywechat.dbo.Question;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqQuestionAdd;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqUID;

import tk.mybatis.mapper.common.Mapper;

/**
 * 问题DAO对象
 * 
 * @author 無
 *
 */
public interface QuestionDao extends Mapper<Question> {

	@Select({"<script>"
			+"SELECT * FROM QY_QUESTION " 
			+"<where>"
			+"<if test='req.id!=null' > AND THEMEID=#{req.id} </if>"
			+"</where>"
			+" AND STATUS = 0 "
			+" ORDER BY SORTNUM ASC"
			+"</script>"
	})
	List<Question> queryQuestionList(@Param("req")ReqUID req) throws SQLException;
	
	@Select({"<script>"
			+"SELECT MAX(SORTNUM) FROM QY_QUESTION " 
			+"<where>"
			+"<if test='req.themeid!=null' > AND THEMEID=#{req.themeid} </if>"
			+"</where>"
			+" AND STATUS = 0 "
			+"</script>"
	})
	Integer getMaxNo(@Param("req")ReqQuestionAdd req) throws SQLException;
}

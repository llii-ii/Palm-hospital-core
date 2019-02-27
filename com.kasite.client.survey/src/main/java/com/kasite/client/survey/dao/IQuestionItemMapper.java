package com.kasite.client.survey.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.kasite.client.survey.bean.dbo.QuestionItem;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @className: IQuestionItem
 * @author: lcz
 * @date: 2018年7月26日 上午10:23:13
 */
public interface IQuestionItemMapper extends Mapper<QuestionItem>{
	
	@Select({"<script>",
		"SELECT q.*,f.NEXTQUESTID FROM SV_QUESTIONITEM q ",
		" LEFT JOIN SV_QUESTIONFLOW f ON q.ITEMID = f.ITEMID ",
		"LEFT JOIN SV_QUESTION u ON q.QUESTID=u.QUESTID ",
		"WHERE u.SUBJECTID=#{subjectId} ORDER BY SORTNUM ASC",
		"</script>"})
	List<QuestionItem> queryQuestionItem(int subjectId);
	
	@Select("SELECT SEQ_NEXTVAL(#{seqName}) FROM DUAL")
	int getSequence(String seqName);
}

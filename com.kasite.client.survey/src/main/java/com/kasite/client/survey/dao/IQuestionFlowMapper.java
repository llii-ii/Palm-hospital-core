package com.kasite.client.survey.dao;

import org.apache.ibatis.annotations.Select;

import com.kasite.client.survey.bean.dbo.QuestionFlow;

import tk.mybatis.mapper.common.Mapper;

public interface IQuestionFlowMapper extends Mapper<QuestionFlow> {

	@Select("SELECT SEQ_NEXTVAL(#{seqName}) FROM DUAL")
	int getSequence(String seqName);
	
	@Select({"<script>",
		"DELETE FROM SV_QUESTIONFLOW ",
		"WHERE ITEMID IN(SELECT ItemId FROM SV_QUESTIONITEM WHERE QUESTID = #{questId}) ",
		"</script>"
	})
	Integer deleteChildFlowItem(Integer questId);
	
	@Select({"<script>",
		"DELETE FROM SV_QUESTIONFLOW WHERE NEXTQUESTID = #{questId}",
		"</script>"
	})
	Integer deleteParentFlowItem(Integer questId);
}

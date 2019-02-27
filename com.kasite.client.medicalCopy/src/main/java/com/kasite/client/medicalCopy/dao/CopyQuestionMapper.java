package com.kasite.client.medicalCopy.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.core.serviceinterface.module.medicalCopy.dbo.CopyQuestion;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqCopyQuestion;

import tk.mybatis.mapper.common.Mapper;

public interface CopyQuestionMapper extends Mapper<CopyQuestion>{
	
	@Select({"<script>",
		"SELECT * FROM TB_COPY_QUESTION",
		"<where>",
			" state!=0",
			"<if test=\"copyQuestion.state!=null and copyQuestion.state!=''\">",
				" AND state = #{copyQuestion.state} ",
			"</if>",
		"</where>",
		"</script>"
	})
	List<CopyQuestion> selectCopyQuestion(@Param("copyQuestion")ReqCopyQuestion reqCopyQuestion);
}

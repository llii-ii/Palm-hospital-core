package com.kasite.core.serviceinterface.module.survey;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.module.survey.req.ReqCommitAnswer;
import com.kasite.core.serviceinterface.module.survey.req.ReqQuerySampleInfo;
import com.kasite.core.serviceinterface.module.survey.req.ReqQuerySubject;
import com.kasite.core.serviceinterface.module.survey.req.ReqQuestion;
import com.kasite.core.serviceinterface.module.survey.req.ReqQuestionFlow;
import com.kasite.core.serviceinterface.module.survey.req.ReqQuestionItem;
import com.kasite.core.serviceinterface.module.survey.req.ReqUpdateSubject;
import com.kasite.core.serviceinterface.module.survey.resp.RespQueryQuestion;
import com.kasite.core.serviceinterface.module.survey.resp.RespQuerySampleInfo;
import com.kasite.core.serviceinterface.module.survey.resp.RespQuerySubject;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO
 */
public interface SurveyService {

	/**
	 * 根据id查询问卷
	 * @param msg
	 * @return
	 */
	public String QuerySubjectById(InterfaceMessage msg) throws Exception;
	/**
	 * 根据id查询问卷
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQuerySubject> querySubjectById(CommonReq<ReqQuerySubject> commReq) throws Exception;
	
	/**
	 * 根据机构id查询主题列表（外用）
	 * @param msg
	 * @return
	 */
	public String QuerySubjectsByOrgId(InterfaceMessage msg) throws Exception;

	/**
	 * 根据机构id查询主题列表（外用）
	 * @Description: 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> querySubjectsByOrgId(CommonReq<ReqQuerySubject> commReq) throws Exception;
	
	/**
	 * 提交答案
	 * @param msg
	 * @return
	 */
	public String CommitAnswer(InterfaceMessage msg) throws Exception;
	/**
	 * 提交答案
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> commitAnswer(CommonReq<ReqCommitAnswer> commReq) throws Exception;
	
	/**
	 * 查询满意度调查问卷列表信息(管理后台)
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespQuerySubject> querySubjectlistByOrgId(CommonReq<ReqQuerySubject> commReq) throws Exception;
	
	/**
	 * 满意度调查信息更新(管理后台)
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespMap> updateSubjectBegin(CommonReq<ReqUpdateSubject> commReq) throws Exception;
	
	/**
	 * 获取判断题数量(管理后台)
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespMap> judgeQuestSumBySubjectid(CommonReq<ReqQuestion> commReq) throws Exception;
	
	/**
	 * 添加问卷问题类型(管理后台)
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespMap> addDetailQuestion(CommonReq<ReqQuestion> commReq) throws Exception;
	
	/**
	 * 添加问题(管理后台)
	 * @param reqQuestionVo
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespMap> addQuestion(CommonReq<ReqQuestion> commReq) throws Exception;
	
	/**
	 * 更新问题(管理后台)
	 * @param commReq
	 * @throws Exception
	 */
	public CommonResp<RespMap> updateQuestion(CommonReq<ReqQuestion> commReq) throws Exception;
	
	/**
	 * 删除问题(管理后台)
	 * 
	 * @param commReq
	 * @throws Exception
	 */
	public CommonResp<RespMap> deleteQuestion(CommonReq<ReqQuestion> commReq) throws Exception;
	
	/**
	 * 删除矩阵问题(管理后台)
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespMap> matdeleteQuestion(CommonReq<ReqQuestion> commReq) throws Exception;
	
	/**
	 * 修改问题选项(管理后台)
	 * @param reqQuestionItemVo
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespMap> updateQuestionItem(CommonReq<ReqQuestionItem> commReq) throws Exception;
	
	/**
	 * 新增问题选项(管理后台)
	 * @param reqQuestionItemVo
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespMap> addQuestionItem(CommonReq<ReqQuestionItem> commReq) throws Exception;
	
	/**
	 * 删除问题选项(管理后台)
	 * @param reqQuestionItemVo
	 * @throws Exception
	 */
	public CommonResp<RespMap> deleteQuestionItem(CommonReq<ReqQuestionItem> commReq)throws Exception;
	
	/**
	 * 批量删除问题选项(管理后台)
	 * @param itemIdArr
	 * @throws Exception
	 */
	public CommonResp<RespMap> deleteQuestionItems(CommonReq<ReqQuestionItem> commReq) throws Exception;
	
	/**
	 * 添加问题跳转(管理后台)
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespMap> addQuestionFlow(CommonReq<ReqQuestionFlow> commReq) throws Exception;
	
	/**
	 * 批量添加问题跳转(管理后台)
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespMap> addQuestionFlows(CommonReq<ReqQuestionFlow> commReq) throws Exception;
	
	/**
	 * 批量删除问题跳转(管理后台)
	 * @param itemIdArr
	 * @throws Exception
	 */
	public CommonResp<RespMap> delAllQuestionFlow(CommonReq<ReqQuestionFlow> commReq) throws Exception;
	
	/**
	 * 删除问题跳转(管理后台)
	 * @param itemIdArr
	 * @throws Exception
	 */
	public CommonResp<RespMap> deleteQuestionFlow(CommonReq<ReqQuestionFlow> commReq) throws Exception;
	
	/**
	 * 新建问卷(管理后台)
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespMap> addSubject(CommonReq<ReqUpdateSubject> commReq) throws Exception;
	
	/**
	 * 修改问卷发布信息(管理后台)
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespMap> updateSubject(CommonReq<ReqUpdateSubject> commReq) throws Exception;
	
	public CommonResp<RespMap> updateSubjectForNet(CommonReq<ReqUpdateSubject> commReq) throws Exception;
	
	/**
	 * 调换问卷问题排列顺序(管理后台)
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespMap> exchangeQuestionSort(CommonReq<ReqQuestion> commReq) throws Exception;
	
	/**
	 * 调换问卷问题选项排列顺序(管理后台)
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespMap> exchangeQuestionItemSort(CommonReq<ReqQuestionItem> commReq) throws Exception;
	
	/**
	 * 修改矩阵问题选项排序 ItemIds1 选项的集合 ItemIds2 选项的集合（比如 满意 选项 不满意选项 逗号分隔 ）(管理后台)
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespMap> exchangeMatQuestionItemSorts(CommonReq<ReqQuestionItem> commReq) throws Exception;
	
	/**
	 * 满意度调查问卷统计
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespQuerySubject> allStatisticaBySubjectId(CommonReq<ReqQuerySubject> commReq) throws Exception;
	
	/**
	 * 查询问卷答案模版
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespQuerySampleInfo> querySampleBySubjectid(CommonReq<ReqQuerySampleInfo> commReq) throws Exception;
	
	/**
	 * 查询问卷答案模版数量
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespMap> querySampleCount(CommonReq<ReqQuerySampleInfo> commReq) throws Exception;
	
	/**
	 * 判断是否有问卷
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespQuerySubject> querySubjectForNet(CommonReq<ReqQuerySubject> commReq) throws Exception;
	
	/**
	 * 查询答案详情
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespQueryQuestion> personStatisticaBySampleId(CommonReq<ReqQuerySampleInfo> commReq) throws Exception;
	
}

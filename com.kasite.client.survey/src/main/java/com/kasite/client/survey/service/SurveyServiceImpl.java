package com.kasite.client.survey.service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coreframework.util.DateOper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kasite.client.survey.bean.dbo.Question;
import com.kasite.client.survey.bean.dbo.QuestionFlow;
import com.kasite.client.survey.bean.dbo.QuestionItem;
import com.kasite.client.survey.bean.dbo.Sample;
import com.kasite.client.survey.bean.dbo.SampleAnswer;
import com.kasite.client.survey.bean.dbo.SampleTrack;
import com.kasite.client.survey.bean.dbo.Subject;
import com.kasite.client.survey.bean.dto.SampleAnswerVo;
import com.kasite.client.survey.bean.dto.SampleInfoVo;
import com.kasite.client.survey.constant.Constant;
import com.kasite.client.survey.dao.IQuestionFlowMapper;
import com.kasite.client.survey.dao.IQuestionItemMapper;
import com.kasite.client.survey.dao.IQuestionMapper;
import com.kasite.client.survey.dao.ISampleAnswerMapper;
import com.kasite.client.survey.dao.ISampleMapper;
import com.kasite.client.survey.dao.ISampleTrackMapper;
import com.kasite.client.survey.dao.ISubjectMapper;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.req.PageVo;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.BeanCopyUtils;
import com.kasite.core.common.util.DateUtils;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryAnswer;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryMatriItem;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryMatriStamp;
import com.kasite.core.serviceinterface.module.survey.SurveyService;
import com.kasite.core.serviceinterface.module.survey.req.ReqAnswers;
import com.kasite.core.serviceinterface.module.survey.req.ReqCommitAnswer;
import com.kasite.core.serviceinterface.module.survey.req.ReqQuerySampleInfo;
import com.kasite.core.serviceinterface.module.survey.req.ReqQuerySubject;
import com.kasite.core.serviceinterface.module.survey.req.ReqQuestionFlow;
import com.kasite.core.serviceinterface.module.survey.req.ReqQuestionItem;
import com.kasite.core.serviceinterface.module.survey.req.ReqQuestion;
import com.kasite.core.serviceinterface.module.survey.req.ReqUpdateSubject;
import com.kasite.core.serviceinterface.module.survey.resp.RespMartrQueryQuestion;
import com.kasite.core.serviceinterface.module.survey.resp.RespQueryQuestion;
import com.kasite.core.serviceinterface.module.survey.resp.RespQuerySampleInfo;
import com.kasite.core.serviceinterface.module.survey.resp.RespQuerySubject;
import com.kasite.core.serviceinterface.module.survey.resp.RespQuestionItem;
import com.yihu.wsgw.api.InterfaceMessage;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * @author linjf TODO
 */
@Service("survey.SurveyWs")
public class SurveyServiceImpl implements SurveyService {
	private final static Log log = LogFactory.getLog(Constant.LOG4J_SURVEY);

	/** 1:单选 */
	final static int QUESTTYPE_1 = 1;
	/** 2:多选 */
	final static int QUESTTYPE_2 = 2;
	/** 3:填空 */
	final static int QUESTTYPE_3 = 3;
	/** 4:矩阵单选题 */
	final static int QUESTTYPE_4 = 4;
	/** 5:矩阵多选题 */
	final static int QUESTTYPE_5 = 5;
	/** 1 收集多少份结束 */
	final static int OVERTYPE_1 = 1;
	/** 2 到什么时间结束 */
	final static int OVERTYPE_2 = 2;
	/** 3无限制 */
	final static int OVERTYPE_3 = 3;
	/** 1 一个手机或电脑只能回复一次 */
	final static int REPLYTYOE_1 = 1;
	/** 2每个IP只能回复一次 */
	final static int REPLYTYOE_2 = 2;
	/** 3无限制 */
	final static int REPLYTYOE_3 = 3;
	/** 4网络问卷 */
	final static int SURVEYTYPE_4 = 4;

	@Autowired
	ISubjectMapper subjectMapper;
	@Autowired
	IQuestionMapper questionMapper;
	@Autowired
	IQuestionItemMapper questionItemMapper;
	@Autowired
	IQuestionFlowMapper questionFlowMapper;
	@Autowired
	ISampleMapper sampleMapper;
	@Autowired
	ISampleAnswerMapper sampleAnswerMapper;
	@Autowired
	ISampleTrackMapper sampleTrackMapper;

	/**
	 * @method
	 * @author wyq
	 * @date 2014-12-25上午10:29:17
	 * @information 根据机构id 查询满意度列表
	 * @return 返回描述
	 */
	@Override
	public String QuerySubjectsByOrgId(InterfaceMessage msg) throws Exception {
		return this.querySubjectsByOrgId(new CommonReq<ReqQuerySubject>(new ReqQuerySubject(msg, true, false))).toResult();
	}

	/**
	 * @information 根据满意度id来查询详细内容
	 * @return 返回描述
	 */
	@Override
	public String QuerySubjectById(InterfaceMessage msg) throws Exception {
		return this.querySubjectById(new CommonReq<ReqQuerySubject>(new ReqQuerySubject(msg, false, true))).toResult();
	}

	/**
	 * 提交答案
	 * 
	 * @throws Exception
	 */
	@Override
	public String CommitAnswer(InterfaceMessage msg) throws Exception {
		return this.commitAnswer(new CommonReq<ReqCommitAnswer>(new ReqCommitAnswer(msg))).toResult();
	}

	@Override
	public CommonResp<RespQuerySubject> querySubjectById(CommonReq<ReqQuerySubject> commReq) throws Exception {

		ReqQuerySubject req = commReq.getParam();
		Subject subject = subjectMapper.selectByPrimaryKey(req.getSubjectId());

		if (subject == null) {
			return new CommonResp<RespQuerySubject>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_SQLEXECERROR);
		}
		List<RespQueryQuestion> list = new ArrayList<RespQueryQuestion>();
		RespQuerySubject resp = new RespQuerySubject();
		resp.setSubjectId(subject.getSubjectId());
		resp.setSubjectTitle(subject.getSubjectTitle());
		resp.setBeginIntro(subject.getBeginIntro());
		resp.setEndingIntro(subject.getEndingIntro());
		resp.setObjtype(subject.getObjType());
		resp.setRemark(subject.getRemark());
		resp.setQuantity(subject.getQuantity());
		resp.setSubjectTitle(subject.getSubjectTitle());
		resp.setSurveyType(subject.getSurveyType());
		resp.setReplytype(subject.getReplyType());
		if (subject.getOverTime() != null) {
			resp.setOvertime(DateOper.formatDate(subject.getOverTime(), "yyyy-MM-dd HH:mm:ss"));
		}
		resp.setOvertype(subject.getOverType());
		resp.setStatus(subject.getStatus());

		Example example = new Example(Question.class);
		example.setOrderByClause("sortNum ASC");
		example.createCriteria().andEqualTo("subjectId", subject.getSubjectId());
		List<Question> qsList = questionMapper.selectByExample(example);
		List<QuestionItem> itemList = null;
		if (null != qsList && qsList.size() > 0) {
			itemList = questionItemMapper.queryQuestionItem(subject.getSubjectId());
		}
		List<RespQuestionItem> arrayquetitem = null;
		List<RespQuestionItem> matrisQustionItem = null;
		RespMartrQueryQuestion matrQuestion = null;
		List<RespMartrQueryQuestion> matrisarray = null;

		for (Question vo : qsList) {
			/** 排除矩形的二级题目 */
			if (!(null == vo.getMatrixQuestId() || vo.getMatrixQuestId() == 0)) {
				continue;
			}
			/** 题目 */
			RespQueryQuestion question = new RespQueryQuestion();
			question.setQuestId(vo.getQuestId());
			question.setContentType(vo.getContentType());
			question.setQuestType(vo.getQuestType());
			question.setSortNum(vo.getSortNum());
			question.setQuestion(vo.getQuestion());
			question.setSubType(vo.getSubType());
			question.setParentQuestId(vo.getParentQuestId());
			question.setRecommend(vo.getRecommend());
			question.setMatrixQuestId(vo.getMatrixQuestId());
			question.setMustquest(vo.getMustQuest());
			question.setMinoption(vo.getMinOption());
			question.setMaxoption(vo.getMaxOption());
			/** 答案 */
			arrayquetitem = new ArrayList<RespQuestionItem>();
			for (QuestionItem tempitem : itemList) {
				if (tempitem.getQuestId().equals(vo.getQuestId())) {
					RespQuestionItem questionItem = new RespQuestionItem();
					questionItem.setItemId(StringUtil.getJSONValue(tempitem.getItemId()));
					questionItem.setQuestId(StringUtil.getJSONValue(tempitem.getQuestId()));
					questionItem.setItemCont(StringUtil.getJSONValue(tempitem.getItemCont()));
					questionItem.setSortNum(StringUtil.getJSONValue(tempitem.getSortNum()));
					questionItem.setIfAddblank(StringUtil.getJSONValue(tempitem.getIfAddBlank()));
					questionItem.setIfAllowNull(StringUtil.getJSONValue(tempitem.getIfAllowNull()));
					
					Example flowExample = new Example(QuestionFlow.class);
					flowExample.createCriteria().andEqualTo("status", 1).andEqualTo("itemId", tempitem.getItemId());
					QuestionFlow svQuestionFlow = questionFlowMapper.selectOneByExample(flowExample);
					if (svQuestionFlow != null) {
						questionItem.setJumpQuest(StringUtil.getJSONValue(svQuestionFlow.getNextQuestId()));
					} else {
						questionItem.setJumpQuest("");
					}
					arrayquetitem.add(questionItem);
				}
			}
			question.setSvQuestionItems(arrayquetitem);

			/** 是否有矩阵题目 */
			List<Question> matrisvQuestions = new ArrayList<Question>();
			for (Question q : qsList) {
				if (q.getMatrixQuestId().equals(vo.getQuestId())) {
					matrisvQuestions.add(q);
				}
			}
			if (null != matrisvQuestions && matrisvQuestions.size() > 0) {
				matrisarray = new ArrayList<RespMartrQueryQuestion>();
				/** 矩阵二级题目 */
				for (Question tempMatri : matrisvQuestions) {
					matrQuestion = new RespMartrQueryQuestion();
					matrQuestion.setQuestId(StringUtil.getJSONValue(tempMatri.getQuestId()));
					matrQuestion.setQuestion(StringUtil.getJSONValue(tempMatri.getQuestion()));
					matrQuestion.setContentType(StringUtil.getJSONValue(tempMatri.getContentType()));
					matrQuestion.setQuestType(StringUtil.getJSONValue(tempMatri.getQuestType()));
					matrQuestion.setSortNum(StringUtil.getJSONValue(tempMatri.getSortNum()));
					matrQuestion.setSubType(StringUtil.getJSONValue(tempMatri.getSubType()));
					matrQuestion.setParentQuestId(StringUtil.getJSONValue(tempMatri.getParentQuestId()));
					matrQuestion.setRecommend(StringUtil.getJSONValue(tempMatri.getRecommend()));
					matrQuestion.setMatrixQuestId(StringUtil.getJSONValue(tempMatri.getMatrixQuestId()));
					matrQuestion.setMustquest(StringUtil.getJSONValue(vo.getMustQuest()));
					matrQuestion.setMinoption(StringUtil.getJSONValue(vo.getMinOption()));
					matrQuestion.setMaxoption(StringUtil.getJSONValue(vo.getMaxOption()));
					/** 矩阵二级题目答案 */
					matrisQustionItem = new ArrayList<RespQuestionItem>();
					for (QuestionItem tempitem : itemList) {
						if (tempitem.getQuestId().equals(tempMatri.getQuestId())) {
							RespQuestionItem questionItem = new RespQuestionItem();
							questionItem.setItemId(StringUtil.getJSONValue(tempitem.getItemId()));
							questionItem.setQuestId(StringUtil.getJSONValue(tempitem.getQuestId()));
							questionItem.setItemCont(StringUtil.getJSONValue(tempitem.getItemCont()));
							questionItem.setSortNum(StringUtil.getJSONValue(tempitem.getSortNum()));
							questionItem.setIfAddblank(StringUtil.getJSONValue(tempitem.getIfAddBlank()));
							questionItem.setIfAllowNull(StringUtil.getJSONValue(tempitem.getIfAllowNull()));
							matrisQustionItem.add(questionItem);
						}
					}
					matrQuestion.setMatrixQuestItems(matrisQustionItem);
					matrisarray.add(matrQuestion);
				}
				question.setChildrenMatrixQuestion(matrisarray);
			}
			list.add(question);
		}
		resp.setResult(list);
		List<RespQuerySubject> respList = new ArrayList<RespQuerySubject>();
		respList.add(resp);
		return new CommonResp<RespQuerySubject>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}

	@Override
	public CommonResp<RespMap> querySubjectsByOrgId(CommonReq<ReqQuerySubject> commReq) throws Exception {

		ReqQuerySubject req = commReq.getParam();
		int surveyType = 8888;
		if (StringUtil.isNotBlank(req.getSurveyType())) {
			surveyType = Integer.parseInt(req.getSurveyType());
		}
		Example example = new Example(Subject.class);
		example.setOrderByClause("CREATETIME DESC");
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("status", 2);
		criteria.andEqualTo("orgId", req.getOrgId());
		if (surveyType != 8888) {
			criteria.andEqualTo("surveyType", surveyType);
		}
		if (StringUtils.isNotBlank(req.getObjType())) {
			criteria.andEqualTo("objType", req.getObjType());
		} else {
			criteria.andNotEqualTo("objType", 2);
		}
		List<Subject> list = subjectMapper.selectByExample(example);
		List<RespMap> respList = new ArrayList<RespMap>();
		for (Subject temp : list) {
			RespMap resp = new RespMap();
			resp.put(ApiKey.querySubjectsByOrgId.SubjectId, StringUtil.getJSONValue(temp.getSubjectId()));
			resp.put(ApiKey.querySubjectsByOrgId.SubjectTitle, StringUtil.getJSONValue(temp.getSubjectTitle()));
			resp.put(ApiKey.querySubjectsByOrgId.Overtype, StringUtil.getJSONValue(temp.getOverType()));
			if (StringUtil.isNotBlank(temp.getOverTime())) {
				resp.put(ApiKey.querySubjectsByOrgId.Overtime, DateOper.formatDate(temp.getOverTime(), "yyyy-MM-dd HH:mm:ss"));
			}
			resp.put(ApiKey.querySubjectsByOrgId.Status, StringUtil.getJSONValue(temp.getStatus()));
			respList.add(resp);
		}
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}

	@Override
	public CommonResp<RespMap> commitAnswer(CommonReq<ReqCommitAnswer> commReq) throws Exception {
		ReqCommitAnswer req = commReq.getParam();
		int subjectId = req.getSubjectId();
		String operatorId = req.getOperatorId();
		String operatorName = req.getOperatorName();
		String ip = req.getIp();
		String phoneOrPc = req.getPhoneOrPc();

		Subject subject = subjectMapper.selectByPrimaryKey(subjectId);
		if (subject == null) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Survery.ERROR_INFO_NOT_FIND);
		}
		// 判断 调查类型surveyType
		if (subject.getSurveyType() != SURVEYTYPE_4) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Survery.ERROR_CHOOSE_SUBJECT);
		}
		// overType结束类型 1 收集多少份结束 2 到什么时间结束 3无限制
		if (subject.getOverType() == OVERTYPE_1) {
			Example example = new Example(Sample.class);
			example.createCriteria().andEqualTo("subjectId", subjectId).andEqualTo("status", 2);
			int count = sampleMapper.selectCountByExample(example);
			if (count > subject.getQuantity()) {
				return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Survery.ERROR_FULL_SUBJECT);
			}
		}
		if (subject.getOverType() == OVERTYPE_2) {
			if (subject.getOverTime().getTime() < DateOper.getNowDateTime().getTime()) {
				// update SV_SUBJECT set Status=3 ,EndTime=now(),overtime=now() where
				// SubjectId=?
				Example example = new Example(Subject.class);
				example.createCriteria().andEqualTo("subjectId", subjectId);
				Subject up = new Subject();
				up.setStatus(3);
				up.setEndTime(DateOper.getNowDateTime());
				up.setOverTime(DateOper.getNowDateTime());
				up.setOperatorId(req.getOperatorId());
				up.setOperatorName(req.getOperatorName());
				subjectMapper.updateByExampleSelective(up, example);
				return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Survery.ERROR_OVER_COLLECT);
			}
		}
		// replyTyoe回复类型设置 1 一个手机或电脑只能回复一次 2每个IP只能回复一次 3无限制
		if (subject.getReplyType() == REPLYTYOE_1) {
			// select COUNT(1) from SV_SAMPLE where PhoneOrPc = ? and SubjectId=?
			Example example = new Example(Sample.class);
			example.createCriteria().andEqualTo("phoneOrPc", phoneOrPc).andEqualTo("subjectId", subjectId);
			int count = sampleMapper.selectCountByExample(example);
			if (count > 0) {
				return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Survery.ERROR_PHONE_REPLY_ONCE);
			}
		}
		if (subject.getReplyType() == REPLYTYOE_2) {
			// SELECT COUNT(1) FROM SV_SAMPLE where SubjectId=? and IP=?
			Example example = new Example(Sample.class);
			example.createCriteria().andEqualTo("ip", ip).andEqualTo("subjectId", subjectId);
			int count = sampleMapper.selectCountByExample(example);
			if (count > 0) {
				return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Survery.ERROR_IP_REPLY_ONCE);
			}
		}
		String userName = req.getUserName();
		int sex = StringUtil.isEmpty(req.getSex()) ? 3 : req.getSex();
		int age = StringUtil.isEmpty(req.getAge()) ? 0 : req.getAge();
		String moblie = req.getMoblie();
		// 给Sample对象赋值
		Sample sample = new Sample();
		sample.setStatus(1);
		sample.setSubjectId(subjectId);
		sample.setUserName(userName);
		sample.setSampleId(sampleMapper.getSequence("SURVERY_SAMPLEID_SEQ"));
		sample.setSex(sex);
		sample.setAge(age);
		sample.setMoblie(moblie);
		sample.setOperatorId(operatorId);
		sample.setOperatorName(operatorName);
		sample.setUpdateTime(DateOper.getNowDateTime());
		sample.setTrackflag(0);
		sample.setCareUser(0);
		sample.setHospId(subject.getOrgId());
		sample.setHospName(subject.getOrgName());
		// replyTyoe回复类型设置 1 一个手机或电脑只能回复一次 2每个IP只能回复一次 3无限制
		if (subject.getReplyType() == REPLYTYOE_1) {
			sample.setPhoneOrPc(phoneOrPc);
		}
		if (subject.getReplyType() == REPLYTYOE_2) {
			sample.setIp(ip);
		}
		sampleMapper.insertSelective(sample);

		int sampleId = sample.getSampleId();

		List<?> list = req.getAnswers();
		ReqAnswers answers = null;
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			answers = (ReqAnswers) list.get(i);
			String as = answers.getAnswer();
			if (StringUtil.isNotEmpty(as)) {
				SampleAnswer sampleAnswer = new SampleAnswer();
				sampleAnswer.setAnswerId(sampleMapper.getSequence("SURVERY_ANSWERID_SEQ"));
				sampleAnswer.setSampleId(sampleId);
				sampleAnswer.setQuestId(answers.getQuestId());

				Question obj = questionMapper.selectByPrimaryKey(answers.getQuestId());
				// questType题型(1:单选,2:多选,3:填空,4:矩阵单选题,5:矩阵多选题)
				if (obj.getQuestType() == 1) {
					sampleAnswer.setAnswer("," + as + ",");
					QuestionItem item = questionItemMapper.selectByPrimaryKey(new Integer(as));
					if (StringUtil.isNotEmpty(item.getIfAddBlank())) {
						if ("1".equals(item.getIfAddBlank().trim())) {
							sampleAnswer.setOtherAnswer(answers.getBlank());
						}
					}

				} else if (obj.getQuestType() == 2 || obj.getQuestType() == 4 || obj.getQuestType() == 5) {
					sampleAnswer.setAnswer("," + as + ",");
				} else {
					sampleAnswer.setAnswer(as);
				}
				sampleAnswer.setOperatorId(operatorId);
				sampleAnswer.setOperatorName(operatorName);

				sampleAnswerMapper.insertSelective(sampleAnswer);
				count++;
			}
		}

		if (count > 0) {
			SampleTrack track = new SampleTrack();
			track.setTrackId(sampleMapper.getSequence("SURVERY_TRACKID_SEQ"));
			track.setOperatorId(operatorId);
			track.setOperatorName(operatorName);
			track.setSampleId(sampleId);
			track.setRemark("");
			track.setVisited(1);
			track.setTrackFlag(0);
			track.setCareUser(0);
			track.setCallres(1);
			Sample updateSam = new Sample();
			updateSam.setStatus(2);
			updateSam.setSampleId(sampleId);
			sampleTrackMapper.insertSelective(track);
			sampleMapper.updateByPrimaryKeySelective(updateSam);
			if (subject.getOverType() == 1) {
				// SELECT COUNT(1) FROM SV_SAMPLE where SubjectId=? and Status=2
				Example example = new Example(Sample.class);
				example.createCriteria().andEqualTo("status", 2).andEqualTo("subjectId", subjectId);
				int totalcount = sampleMapper.selectCountByExample(example);
				// quantity 需要问卷量
				if (totalcount >= subject.getQuantity()) {
					// 结束问卷调查，更新SV_SUBJECT数据
					// update SV_SUBJECT set Status=3 ,EndTime=now(),overtime=now() where
					// SubjectId=?
					Subject upSubject = new Subject();
					upSubject.setSubjectId(subjectId);
					upSubject.setStatus(3);
					upSubject.setOverTime(DateOper.getNowDateTime());
					upSubject.setEndTime(DateOper.getNowDateTime());
					subjectMapper.updateByPrimaryKeySelective(upSubject);
				}
			}
		}
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public CommonResp<RespQuerySubject> querySubjectlistByOrgId(CommonReq<ReqQuerySubject> commReq) throws Exception {
		ReqQuerySubject req = commReq.getParam();
		Example example = new Example(Subject.class);
		example.setOrderByClause("CREATETIME DESC");
		example.createCriteria().andNotEqualTo("status", 0).andEqualTo("orgId", req.getOrgId()).andEqualTo("surveyType", 4);
		
		PageVo pageVo = req.getPage();
		PageInfo<Subject> pageInfo = null;
		if(pageVo != null && pageVo.getPSize() > 0) {
			PageHelper.startPage(pageVo.getPIndex(), pageVo.getPSize());
		}
		List<Subject> subjectList = subjectMapper.selectByExample(example);
		List<RespQuerySubject> respList = new ArrayList<RespQuerySubject>();
		if(subjectList != null) {
			pageInfo = new PageInfo<>(subjectList);
			Long total = pageInfo.getTotal();
			pageVo.setPCount(total.intValue());
			for (Subject subject : pageInfo.getList()) {
				RespQuerySubject resp = new RespQuerySubject();
				resp.setSubjectId(subject.getSubjectId());
				resp.setSubjectTitle(subject.getSubjectTitle());
				resp.setBeginIntro(subject.getBeginIntro());
				resp.setEndingIntro(subject.getEndingIntro());
				resp.setObjtype(subject.getObjType());
				resp.setRemark(subject.getRemark());
				resp.setQuantity(subject.getQuantity());
				resp.setSurveyType(subject.getSurveyType());
				resp.setReplytype(subject.getReplyType());
				if (subject.getOverTime() != null) {
					resp.setOvertime(DateOper.formatDate(subject.getOverTime(), "yyyy-MM-dd HH:mm:ss"));
				}
				if (subject.getCreateTime() != null) {
					resp.setCreateTime(DateOper.formatDate(subject.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
				}
				resp.setOvertype(subject.getOverType());
				resp.setStatus(subject.getStatus());
				Example sampExample = new Example(Sample.class);
				sampExample.createCriteria().andEqualTo("status",2).andEqualTo("subjectId", subject.getSubjectId());
				Integer countSample = sampleMapper.selectCountByExample(sampExample);
				resp.setCountsample(countSample);
				respList.add(resp);
			}
		}
		return new CommonResp<RespQuerySubject>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList, pageVo);
	}
	
	@Override
	public CommonResp<RespMap> updateSubjectForNet(CommonReq<ReqUpdateSubject> commReq) throws Exception {
		ReqUpdateSubject req = commReq.getParam();
		Integer quantity = req.getQuantity();
		String overTime = DateUtils.getTimestampToStr(req.getOverTime());
		Subject subject = new Subject();
		subject.setSubjectId(req.getSubjectId());
		subject.setSurveyType(req.getSurveyType());
		subject.setOverType(req.getOverType());
		subject.setReplyType(req.getReplyType());
		if ((new Integer(1)).equals(req.getOverType())) {
			if (quantity > 0) {
				subject.setQuantity(quantity);
			}else {
				return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM);
			}
		}else if ((new Integer(2)).equals(req.getOverType())) {
			if (StringUtil.isNotEmpty(overTime)) {
				if (overTime.length() >= 14 && overTime.length() < 17) {
					overTime = overTime + ":00";
				}
				subject.setOverTime(new Timestamp(DateOper.parse(overTime)
						.getTime()));
			} else {
				return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM);
			}
		}
		subjectMapper.updateByPrimaryKeySelective(subject);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}
		
	@Override
	public CommonResp<RespMap> updateSubjectBegin(CommonReq<ReqUpdateSubject> commReq) throws Exception {
		ReqUpdateSubject req = commReq.getParam();
		Integer status = req.getStatus();
		
		Subject subject = new Subject();
		subject.setSubjectId(req.getSubjectId());
		subject.setSubjectTitle(req.getSubjectTitle());
		subject.setObjType(req.getObjType());
		subject.setOperatorId(req.getOperatorId());
		subject.setOperatorName(req.getOperatorName());
		subject.setProvinceId(req.getProvinceId());
		subject.setCityId(req.getCityId());
		subject.setOrgId(req.getOrgId());
		subject.setQuantity(req.getQuantity());
		subject.setOrgName(req.getOrgName());
		subject.setRemark(req.getRemark());
		subject.setBeginIntro(req.getBeginIntro());
		subject.setEndingIntro(req.getEndingIntro());
		subject.setSurveyType(req.getSurveyType());
		subject.setContactPerson(req.getContactPerson());
		subject.setContactPhone(req.getContactPhone());
		if (StringUtil.isNotEmpty(status) && status == 3) {
			subject.setOverTime(DateOper.getNowDateTime());
		}
		subject.setStatus(status);
		if (StringUtil.isNotEmpty(req.getContactPerson())
				&& StringUtil.isNotEmpty(req.getContactPhone())) {
			subject.setApplyTime(DateOper.getNowDateTime());
		}
		subjectMapper.updateByPrimaryKeySelective(subject);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}
	
	@Override
	public CommonResp<RespMap> judgeQuestSumBySubjectid(CommonReq<ReqQuestion> commReq) throws Exception {
		ReqQuestion req = commReq.getParam();
		Example example = new Example(Question.class);
		example.createCriteria().andEqualTo("matrixQuestId", 0).andEqualTo("subjectId", req.getSubjectId());
		int count = questionMapper.selectCountByExample(example);
		RespMap resp = new RespMap();
		resp.put(ApiKey.judgeQuestSumBySubjectid.Count, StringUtil.getJSONValue(count));
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}
	
	@Transactional
	@Override
	public CommonResp<RespMap> addDetailQuestion(CommonReq<ReqQuestion> commReq) throws Exception {
		ReqQuestion req = commReq.getParam();
		Integer questType = req.getQuestType();
		Integer subjectId = req.getSubjectId();
		Question question = new Question();
		question.setQuestId(questionMapper.getSequence("SURVERY_QUESTIONID_SEQ"));
		question.setQuestion("请在此输入问题标题");
		question.setSubjectId(subjectId);
		question.setContentType(req.getContentType());
		question.setQuestType(questType);
		question.setObjType(req.getObjType());
		question.setMatrixQuestId(0);
		question.setStatus(1);
		question.setSubType(2);
		question.setParentQuestId(0);
		question.setRecommend(2);
		question.setOperatorId(req.getOperatorId());
		question.setOperatorName(req.getOperatorName());
		if (subjectId > 0) {
			Integer sort = getSubjectMaxSort(subjectId);
			question.setSortNum(sort);
		}
		if (questType == 1) {
			// 添加默认问题选项
			createQuetionItem(question, "很满意");
			createQuetionItem(question, "满意");
			createQuetionItem(question, "一般");
			questionMapper.insertSelective(question);
		}else if (questType == 2) {
			// 添加默认问题选项
			createQuetionItem(question, "很满意");
			createQuetionItem(question, "满意");
			questionMapper.insertSelective(question);
		}else if (questType == 3) {
			questionMapper.insertSelective(question);
		}else if (questType == 4 || questType == 5) {
			questionMapper.insertSelective(question);
			Question question1 = new Question();
			question1.setQuestion("矩阵");
			question1.setSubjectId(subjectId);
			question1.setContentType(req.getContentType());
			question1.setQuestId(questionMapper.getSequence("SURVERY_QUESTIONID_SEQ"));
			question1.setQuestType(questType);
			question1.setObjType(req.getObjType());
			question1.setMatrixQuestId(question.getQuestId());
			question1.setStatus(1);
			question1.setSubType(2);
			question1.setParentQuestId(0);
			question1.setRecommend(2);
			question1.setOperatorId(req.getOperatorId());
			question1.setOperatorName(req.getOperatorName());
			if (subjectId > 0) {
				Integer sort = getSubjectMaxSort(subjectId);
				question1.setSortNum(sort);
			}
			questionMapper.insertSelective(question1);
			// 添加默认问题选项
			createQuetionItem(question1, "很满意");
			createQuetionItem(question1, "满意");
			
			Question question2 = new Question();
			question2.setQuestion("矩阵");
			question2.setSubjectId(subjectId);
			question2.setQuestId(questionMapper.getSequence("SURVERY_QUESTIONID_SEQ"));
			question2.setContentType(req.getContentType());
			question2.setQuestType(questType);
			question2.setObjType(req.getObjType());
			question2.setMatrixQuestId(question.getQuestId());
			question2.setStatus(1);
			question2.setSubType(2);
			question2.setParentQuestId(0);
			question2.setRecommend(2);
			question2.setOperatorId(req.getOperatorId());
			question2.setOperatorName(req.getOperatorName());
			if (subjectId > 0) {
				Integer sort = getSubjectMaxSort(subjectId);
				question2.setSortNum(sort);
			}
			questionMapper.insertSelective(question2);
			// 添加默认问题选项
			createQuetionItem(question2, "很满意");
			createQuetionItem(question2, "满意");
		}
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public CommonResp<RespMap> addQuestion(CommonReq<ReqQuestion> commReq) throws Exception {
		ReqQuestion req = commReq.getParam();
		Question question = new Question();
		question.setQuestId(questionMapper.getSequence("SURVERY_QUESTIONID_SEQ"));
		question.setQuestion(req.getQuestion());
		question.setContentType(req.getContentType());
		question.setObjType(req.getObjType());
		question.setQuestType(req.getQuestType());
		question.setSubjectId(req.getSubjectId());
		question.setMatrixQuestId(req.getMatrixQuestId());
		question.setStatus(1);
		question.setSubType(2);
		question.setParentQuestId(0);
		question.setRecommend(2);
		question.setOperatorId(req.getOperatorId());
		question.setOperatorName(req.getOperatorName());
		if (req.getQuestType() != 4 && req.getQuestType() != 5) {
			question.setMatrixQuestId(0);
		}
		if (req.getSubjectId() > 0) {
			Integer sort = getQuestionMaxSort(question.getQuestId());
			question.setSortNum(sort);
		}
		questionMapper.insertSelective(question);
		Integer questId = question.getQuestId();
		RespMap resp = new RespMap();
		resp.put(ApiKey.querySubjectsByOrgId.QuestId, StringUtil.getJSONValue(questId));
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	@Override
	public CommonResp<RespMap> updateQuestion(CommonReq<ReqQuestion> commReq) throws Exception {
		ReqQuestion req = commReq.getParam();
		Question question = new Question();
		question.setQuestId(req.getQuestId());
		question.setQuestion(req.getQuestion());
		question.setObjType(req.getObjType());
		question.setContentType(req.getContentType());
		question.setQuestType(req.getQuestType());
		question.setOperatorName(req.getOperatorName());
		question.setOperatorId(req.getOperatorId());
		question.setMatrixQuestId(req.getMatrixQuestId());
		question.setMustQuest(req.getMustQuest());
		question.setMinOption(req.getMinOption());
		question.setMaxOption(req.getMaxOption());
		question.setParentQuestId(0);
		questionMapper.updateByPrimaryKeySelective(question);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Transactional
	@Override
	public CommonResp<RespMap> deleteQuestion(CommonReq<ReqQuestion> commReq) throws Exception {
		ReqQuestion req = commReq.getParam();
		Integer questId = req.getQuestId();
		//1.先查询出所有的问题答案选项
		Example exampleQI = new Example(QuestionItem.class);
		exampleQI.createCriteria().andEqualTo("questId", questId);
		List<QuestionItem> itemList = questionItemMapper.selectByExample(exampleQI);
		List<Integer> itemIdArr = new ArrayList<>();
		if(itemList != null && itemList.size() > 0) {
			for (QuestionItem questionItem : itemList) {
				itemIdArr.add(questionItem.getItemId());
			}
		}
		//2.删除问题答案选项的跳转,即删除从表SV_QUESTIONFLOW
		Example exampleQF = new Example(QuestionFlow.class);
		exampleQF.createCriteria().andIn("itemId", itemIdArr);
		questionFlowMapper.deleteByExample(exampleQF);
		//3.删除问题时,把问题选项跳转 以及被跳转删除,即删除从表SV_QUESTIONFLOW
		exampleQF = new Example(QuestionFlow.class);
		exampleQF.createCriteria().andNotEqualTo("nextQuestId", questId);
		questionFlowMapper.deleteByExample(exampleQF);
		//4.删除问题选项,即删除从表SV_QUESTIONITEM
		exampleQI = new Example(QuestionItem.class);
		exampleQI.createCriteria().andIn("itemId", itemIdArr);
		questionItemMapper.deleteByExample(exampleQI);
		//5.删除主表SV_QUESTION
		questionMapper.deleteByPrimaryKey(questId);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}
	
	@Transactional
	@Override
	public CommonResp<RespMap> matdeleteQuestion(CommonReq<ReqQuestion> commReq) throws Exception {
		ReqQuestion req = commReq.getParam();
		Integer questId = req.getQuestId();
		//1.先查询出改问题下的所有矩阵题目
		Example exampleQ = new Example(Question.class);
		exampleQ.createCriteria().andNotEqualTo("matrixQuestId", questId);
		List<Question> questionList = questionMapper.selectByExample(exampleQ);
		List<Integer> questIdArr = new ArrayList<>();
		if(questionList != null) {
			for (Question question : questionList) {
				questIdArr.add(question.getQuestId());
			}
		}
		//2.删除问题答案选项的跳转,即删除从表SV_QUESTIONFLOW
		Example exampleQF = new Example(QuestionFlow.class);
		exampleQF.createCriteria().andNotEqualTo("nextQuestId", questId);
		questionFlowMapper.deleteByExample(exampleQF);
		//3.删除问题
		questionMapper.deleteByPrimaryKey(questId);
		exampleQ = new Example(Question.class);
		exampleQ.createCriteria().andNotEqualTo("matrixQuestId", questId);
		questionMapper.deleteByExample(exampleQ);
		//4.删除矩阵问题答案选项的跳转,即删除从表SV_QUESTIONFLOW
		exampleQF = new Example(QuestionFlow.class);
		exampleQF.createCriteria().andIn("nextQuestId", questIdArr);
		questionFlowMapper.deleteByExample(exampleQF);
		//5.删除矩阵问题答案选项,即删除从表SV_QUESTIONITEM
		Example exampleQI = new Example(QuestionItem.class);
		exampleQI.createCriteria().andIn("questId", questIdArr);
		questionItemMapper.deleteByExample(exampleQI);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public CommonResp<RespMap> updateQuestionItem(CommonReq<ReqQuestionItem> commReq) throws Exception {
		ReqQuestionItem req = commReq.getParam();
		QuestionItem questionItem = new QuestionItem();
		questionItem.setItemId(req.getItemId());
		questionItem.setQuestId(req.getQuestId());
		questionItem.setItemCont(req.getItemCont());
		questionItem.setOperatorId(req.getOperatorId());
		questionItem.setOperatorName(req.getOperatorName());
//		questionItem.setOperTime(DateOper.getNowDateTime());
		questionItem.setIfAddBlank(req.getIfAddBlank());
		questionItem.setIfAllowNull(req.getIfAllowNull());
		questionItemMapper.updateByPrimaryKeySelective(questionItem);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public CommonResp<RespMap> addQuestionItem(CommonReq<ReqQuestionItem> commReq) throws Exception {
		ReqQuestionItem req = commReq.getParam();
		QuestionItem questionItem = new QuestionItem();
		questionItem.setItemId(questionItemMapper.getSequence("SURVERY_ITEMID_SEQ"));
		questionItem.setQuestId(req.getQuestId());
		questionItem.setItemCont(req.getItemCont());
		questionItem.setOperatorId(req.getOperatorId());
		questionItem.setOperatorName(req.getOperatorName());
//		questionItem.setOperTime(DateOper.getNowDateTime());
		questionItem.setIfAddBlank(req.getIfAddBlank());
		questionItem.setIfAllowNull(req.getIfAllowNull());
		questionItem.setStatus(1);
		questionItem.setSortNum(getQuestionMaxSort(req.getQuestId()));
		
		questionItemMapper.insertSelective(questionItem);
		RespMap resp = new RespMap();
		resp.put(ApiKey.querySubjectsByOrgId.ItemId, StringUtil.getJSONValue(questionItem.getItemId()));
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}
	
	@Transactional
	@Override
	public CommonResp<RespMap> deleteQuestionItem(CommonReq<ReqQuestionItem> commReq) throws Exception {
		ReqQuestionItem req = commReq.getParam();
		Integer itemId = req.getItemId();
		//1.先删除从表，把问题选项跳转 以及被跳转删除,即删除从表SV_QUESTIONFLOW
		Example exampleQF = new Example(QuestionFlow.class);
		exampleQF.createCriteria().andNotEqualTo("itemId", itemId);
		questionFlowMapper.deleteByExample(exampleQF);
		//2.再删主表，删除问题选项,即删除从表SV_QUESTIONITEM
		questionItemMapper.deleteByPrimaryKey(itemId);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Transactional
	@Override
	public CommonResp<RespMap> deleteQuestionItems(CommonReq<ReqQuestionItem> commReq) throws Exception {
		ReqQuestionItem req = commReq.getParam();
		String itemIds = req.getItemIds();
		//1.先删除从表，把问题选项跳转 以及被跳转删除,即删除从表SV_QUESTIONFLOW
		Example exampleQF = new Example(QuestionFlow.class);
		exampleQF.createCriteria().andIn("itemId", Arrays.asList(itemIds.split(",")));
		questionFlowMapper.deleteByExample(exampleQF);
		//2.再删主表，删除问题选项,即删除从表SV_QUESTIONITEM
		Example exampleQI = new Example(QuestionItem.class);
		exampleQI.createCriteria().andIn("itemId", Arrays.asList(itemIds.split(",")));
		questionItemMapper.deleteByExample(exampleQI);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public CommonResp<RespMap> addQuestionFlow(CommonReq<ReqQuestionFlow> commReq) throws Exception {
		ReqQuestionFlow req = commReq.getParam();
		Integer itemId = req.getItemId();
		Integer nextQuestId = req.getNextQuestId();
		QuestionFlow questionFlow = new QuestionFlow();
		questionFlow.setFlowId(questionFlowMapper.getSequence("SURVERY_FLOWID_SEQ"));
		questionFlow.setItemId(itemId);
		questionFlow.setNextQuestId(nextQuestId);
		questionFlow.setOperatorId(req.getOperatorId());
		questionFlow.setOperatorName(req.getOperatorName());
		questionFlow.setOperTime(DateOper.getNowDateTime());
		questionFlow.setStatus(1);
		questionFlowMapper.insertSelective(questionFlow);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}
	
	@Transactional
	@Override
	public CommonResp<RespMap> addQuestionFlows(CommonReq<ReqQuestionFlow> commReq) throws Exception {
		ReqQuestionFlow req = commReq.getParam();
		String itemIds = req.getItemIds();
		String nextQuestIds = req.getNextQuestIds();
		String[] itemidArry = itemIds.split(",");
		String[] nextQuestArry = nextQuestIds.split(",");
		if (nextQuestArry.length != itemidArry.length) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.NULLERROR);
		}
		for (int i = 0; i < nextQuestArry.length; i++) {
			QuestionFlow questionFlow = new QuestionFlow();
			questionFlow.setFlowId(questionFlowMapper.getSequence("SURVERY_FLOWID_SEQ"));
			questionFlow.setItemId(Integer.valueOf(itemidArry[i]));
			questionFlow.setNextQuestId(Integer.valueOf(nextQuestArry[i]));
			questionFlow.setOperatorId(req.getOperatorId());
			questionFlow.setOperatorName(req.getOperatorName());
			questionFlow.setOperTime(DateOper.getNowDateTime());
			questionFlow.setStatus(1);
			questionFlowMapper.insertSelective(questionFlow);
		}
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public CommonResp<RespMap> delAllQuestionFlow(CommonReq<ReqQuestionFlow> commReq) throws Exception {
		ReqQuestionFlow req = commReq.getParam();
		String itemIds = req.getItemIds();
		String[] itemidArry = itemIds.split(",");
		Example exampleQF = new Example(QuestionFlow.class);
		exampleQF.createCriteria().andIn("itemId", Arrays.asList(itemidArry));
		questionFlowMapper.deleteByExample(exampleQF);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}
	
	@Override
	public CommonResp<RespMap> deleteQuestionFlow(CommonReq<ReqQuestionFlow> commReq) throws Exception {
		ReqQuestionFlow req = commReq.getParam();
		Integer itemId = req.getItemId();
		Example exampleQF = new Example(QuestionFlow.class);
		exampleQF.createCriteria().andNotEqualTo("itemId", itemId);
		questionFlowMapper.deleteByExample(exampleQF);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}
	
	@Override
	public CommonResp<RespMap> addSubject(CommonReq<ReqUpdateSubject> commReq) throws Exception {
		ReqUpdateSubject req = commReq.getParam();
		Subject subject = new Subject();
//		BeanCopyUtils.copyProperties(req, subject, null);
		int subjectId = subjectMapper.getSequence("SURVERY_SUBJECTID_SEQ");
		subject.setSubjectId(subjectId);
		subject.setSubjectTitle(req.getSubjectTitle());
		subject.setBeginIntro("欢迎参加,此次调查旨在收集建议，帮助医院改善服务效率和质量，希望您抽点时间积极配合我们的调查工作，本次调查采用匿名方式，我们将严格保密您的信息，谢谢您的参与！");
		subject.setEndingIntro("非常感谢您对我们回访调查工作的配合和支持，祝您健康！再见！");
		subject.setObjType(req.getObjType());
		subject.setOperatorId(req.getOperatorId());
		subject.setOperatorName(req.getOperatorName());
		subject.setProvinceId(req.getProvinceId());
		subject.setCityId(req.getCityId());
		subject.setOrgId(req.getOrgId());
		subject.setOrgType(req.getOrgType());
		subject.setOtherOrg(req.getOtherOrg());
		subject.setOrgName(req.getOrgName());
		subject.setRemark(req.getRemark());
		subject.setSurveyType(req.getSurveyType());
		subject.setQuantity(req.getQuantity());
		subject.setStatus(1);
		// 默认无限制
		subject.setOverType(3);
		subject.setReplyType(3);
		subject.setCreateTime(DateOper.getNowDateTime());
		subject.setCycleTag(1);
		// 渠道ID（BOSS 1 无边界 2）
		subject.setDitchID(2);
		subjectMapper.insertSelective(subject);
		 
		RespMap resp = new RespMap();
		resp.put(ApiKey.querySubjectsByOrgId.SubjectId, StringUtil.getJSONValue(subjectId));
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}
	
	@Override
	public CommonResp<RespMap> updateSubject(CommonReq<ReqUpdateSubject> commReq) throws Exception {
		ReqUpdateSubject req = commReq.getParam();
		Subject subject = new Subject();
		subject.setSubjectId(req.getSubjectId());
		subject.setSubjectTitle(req.getSubjectTitle());
		subject.setObjType(req.getObjType());
		subject.setOperatorId(req.getOperatorId());
		subject.setOperatorName(req.getOperatorName());
		subject.setProvinceId(req.getProvinceId());
		subject.setCityId(req.getCityId());
		subject.setOrgId(req.getOrgId());
		subject.setQuantity(req.getQuantity());
		subject.setOrgName(req.getOrgName());
		subject.setRemark(req.getRemark());
		subject.setBeginIntro(req.getBeginIntro());
		subject.setEndingIntro(req.getEndingIntro());
		subject.setSurveyType(req.getSurveyType());
		subject.setContactPerson(req.getContactPerson());
		subject.setContactPhone(req.getContactPhone());
		if (StringUtil.isNotEmpty(req.getStatus()) && req.getStatus() == 3) {
			subject.setOverTime(DateOper.getNowDateTime());
		}
		subject.setStatus(req.getStatus());
		// surveytype=1条件是否要加？&& vo.getSurveytype() == 1
		if (StringUtil.isNotEmpty(req.getContactPerson())
				&& StringUtil.isNotEmpty(req.getContactPhone())) {
			subject.setApplyTime(DateOper.getNowDateTime());
		}
		subjectMapper.updateByPrimaryKeySelective(subject);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}
	
	@Transactional
	@Override
	public CommonResp<RespMap> exchangeQuestionSort(CommonReq<ReqQuestion> commReq) throws Exception {
		ReqQuestion req = commReq.getParam();
		Integer questId1 = req.getQuestId1();
		Integer questId2 = req.getQuestId2();
		//1.先删除相关问题跳转关系
		questionFlowMapper.deleteChildFlowItem(questId1);
		questionFlowMapper.deleteParentFlowItem(questId1);
		questionFlowMapper.deleteChildFlowItem(questId2);
		questionFlowMapper.deleteParentFlowItem(questId2);
		//2.查询问题信息
		Question bean1 = questionMapper.selectByPrimaryKey(questId1);
		Question bean2 = questionMapper.selectByPrimaryKey(questId2);
		//3.替换顺序
		Integer sort1 = bean1.getSortNum();
		Integer sort2 = bean2.getSortNum();
		bean1.setSortNum(sort2);
		bean1.setOperatorId(req.getOperatorId());
		bean1.setOperatorName(req.getOperatorName());
		bean2.setSortNum(sort1);
		bean2.setOperatorId(req.getOperatorId());
		bean2.setOperatorName(req.getOperatorName());
		questionMapper.updateByPrimaryKey(bean1);
		questionMapper.updateByPrimaryKey(bean2);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}
	
	@Override
	public CommonResp<RespMap> exchangeQuestionItemSort(CommonReq<ReqQuestionItem> commReq) throws Exception {
		ReqQuestionItem req = commReq.getParam();
		Integer itemId1 = req.getItemId1();
		Integer itemId2 = req.getItemId2();
		QuestionItem bean1 = questionItemMapper.selectByPrimaryKey(itemId1);
		QuestionItem bean2 = questionItemMapper.selectByPrimaryKey(itemId2);
		Integer sort1 = bean1.getSortNum();
		Integer sort2 = bean2.getSortNum();
		bean1.setSortNum(sort2);
		bean1.setOperatorId(req.getOperatorId());
		bean1.setOperatorName(req.getOperatorName());
		bean2.setSortNum(sort1);
		bean2.setOperatorId(req.getOperatorId());
		bean2.setOperatorName(req.getOperatorName());
		questionItemMapper.updateByPrimaryKey(bean1);
		questionItemMapper.updateByPrimaryKey(bean2);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}
	
	@Transactional
	@Override
	public CommonResp<RespMap> exchangeMatQuestionItemSorts(CommonReq<ReqQuestionItem> commReq) throws Exception {
		ReqQuestionItem req = commReq.getParam();
		String itemIds1 = req.getItemIds1();
		String itemIds2 = req.getItemIds2();
		String[] itemid1Arry = itemIds1.split(",");
		String[] itemid2Arry = itemIds2.split(",");
		if (itemid1Arry.length != itemid2Arry.length) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.NULLERROR);
		}
		for (int i = 0; i < itemid1Arry.length; i++) {
			QuestionItem bean1 = questionItemMapper.selectByPrimaryKey(itemid1Arry[i]);
			QuestionItem bean2 = questionItemMapper.selectByPrimaryKey(itemid2Arry[i]);
			Integer sort1 = bean1.getSortNum();
			Integer sort2 = bean2.getSortNum();
			bean1.setSortNum(sort2);
			bean1.setOperatorId(req.getOperatorId());
			bean1.setOperatorName(req.getOperatorName());
			bean2.setSortNum(sort1);
			bean2.setOperatorId(req.getOperatorId());
			bean2.setOperatorName(req.getOperatorName());
			questionItemMapper.updateByPrimaryKey(bean1);
			questionItemMapper.updateByPrimaryKey(bean2);
		}
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}
	
	@Override
	public CommonResp<RespQuerySubject> allStatisticaBySubjectId(CommonReq<ReqQuerySubject> commReq) throws Exception {
		ReqQuerySubject req = commReq.getParam();
		RespQuerySubject resp = new RespQuerySubject();
		//导出下载时用到
		if(req.getQuestionId() != null) {
			resp.setQuestionId(req.getQuestionId());
		}
		String subjectId = req.getSubjectId();
		if (StringUtil.isBlank(subjectId)) {
			return new CommonResp<RespQuerySubject>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.NULLERROR);
		}
		//1.先查询满意度调查问卷基本信息
		Subject subject = subjectMapper.selectByPrimaryKey(subjectId);
		if (subject == null) {
			return new CommonResp<RespQuerySubject>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
		//2.统计问卷答案信息
		Example sampleExample = new Example(Sample.class);
		sampleExample.createCriteria().andEqualTo("status", 2).andEqualTo("subjectId", subjectId);
		Integer subjectIdInteger = sampleMapper.selectCountByExample(sampleExample);
		resp.setSubjectTotal(StringUtil.getJSONValue(subjectIdInteger));
		resp.setSubjectTitle(StringUtil.getJSONValue(subject.getSubjectTitle()));
		//3.此调查问卷的问题列表信息(按顺序排序)
		Example questionExample = new Example(Question.class);
		questionExample.createCriteria().andEqualTo("status", 1).andEqualTo("matrixQuestId", 0).andEqualTo("subjectId", subjectId);
		questionExample.setOrderByClause("sortNum ASC");
		List<Question> questionList = questionMapper.selectByExample(questionExample);
		List<RespQueryQuestion> respQueryQuestionList = new ArrayList<>();
		for (int i = 0; i < questionList.size(); i++) {
			Integer questId = questionList.get(i).getQuestId();
			RespQueryQuestion respQueryQuestion = new RespQueryQuestion();
			respQueryQuestion.setQuestion(questionList.get(i).getQuestion());
			respQueryQuestion.setQuestId(questId);
			respQueryQuestion.setQuestType(questionList.get(i).getQuestType());
			if (questionList.get(i).getQuestType() < 3) {
				Integer singlequestiontotal = 0;
				singlequestiontotal = sampleAnswerMapper.querySingleQuestionTotal(questId);
				
				Integer totalSamples = sampleAnswerMapper.queryCountAnswerByQuestId(questId);
				respQueryQuestion.setTotalSamp(totalSamples);
				
				Example questionItemExample = new Example(QuestionItem.class);
				questionItemExample.createCriteria().andEqualTo("questId", questId);
				questionItemExample.setOrderByClause("sortNum ASC");
				List<QuestionItem> questionItemslist = questionItemMapper.selectByExample(questionItemExample);
				List<RespQuestionItem> svQuestionItems = new ArrayList<>();
				for (int j = 0; j < questionItemslist.size(); j++) {
					Integer itemId = questionItemslist.get(j).getItemId();
					RespQuestionItem respQuestionItem = new RespQuestionItem();
					respQuestionItem.setItemId(StringUtil.getJSONValue(itemId));
					respQuestionItem.setItemCont(StringUtil.getJSONValue(questionItemslist.get(j).getItemCont()));
					respQuestionItem.setIfAddblank(StringUtil.getJSONValue(questionItemslist.get(j).getIfAddBlank()));
					if (new Integer(1).equals(questionList.get(i).getQuestType())) {
						Integer single = 0;
						single = sampleAnswerMapper.queryCountAnswerByItemIdEquals("," + itemId + ",");
						respQuestionItem.setSum(StringUtil.getJSONValue(single));
						
						List<RespQueryAnswer> OtherAnswerList = new ArrayList<>();
						QuestionItem temp = questionItemslist.get(j);
						if(StringUtil.isNotEmpty(temp.getIfAddBlank())) {
							if ("1".equals(temp.getIfAddBlank().trim())) {
								Example sampleAnswerExample = new Example(SampleAnswer.class);
								sampleAnswerExample.createCriteria().andEqualTo("answer", "," + itemId + ",");
								List<SampleAnswer> sampleAnswers = sampleAnswerMapper.selectByExample(sampleAnswerExample);
								for (int k = 0; k < sampleAnswers.size(); k++) {
									RespQueryAnswer otherAnswer = new RespQueryAnswer();
									if (sampleAnswers.get(k).getOtherAnswer() != null){
										otherAnswer.setoAnswer(StringUtil.getJSONValue(new String(sampleAnswers.get(k).getOtherAnswer().getBytes("UTF-8"),"UTF-8")));
									}
									OtherAnswerList.add(otherAnswer);
								}
							}
						}
						respQuestionItem.setOtherAnswers(OtherAnswerList);
						DecimalFormat df = new DecimalFormat();
						df.setMaximumFractionDigits(2);
						df.setMinimumFractionDigits(2);
						if (singlequestiontotal != 0) {
							respQuestionItem.setPercent(StringUtil.getJSONValue(df.format(single* 100.00 / singlequestiontotal)));
						} else {
							respQuestionItem.setPercent("0");
						}
					}else if (new Integer(2).equals(questionList.get(i).getQuestType())) {
						Integer single = 0;
						single = sampleAnswerMapper.queryCountAnswerByItemIdLike("%," + itemId + ",%");
						respQuestionItem.setSum(StringUtil.getJSONValue(single));
						
						DecimalFormat df = new DecimalFormat();
						df.setMaximumFractionDigits(2);
						df.setMinimumFractionDigits(2);
						if (singlequestiontotal != 0) {
							respQuestionItem.setPercent(StringUtil.getJSONValue(df.format(single* 100.00 / singlequestiontotal)));
						} else {
							respQuestionItem.setPercent("0");
						}
					}
					svQuestionItems.add(respQuestionItem);
				}
				// 冒泡降序
				int in = 0, out = 0;
				for (out = 0; out < svQuestionItems.size(); out++) {
					for (in = svQuestionItems.size() - 1; in > out; in--) {
						if (Integer.valueOf(svQuestionItems.get(in).getSum().toString()) 
								> Integer.valueOf(svQuestionItems.get(in - 1).getSum().toString())) {
							RespQuestionItem obj = svQuestionItems.get(in);
							svQuestionItems.set(in,svQuestionItems.get(in - 1));
							svQuestionItems.set(in - 1, obj);
						}
					}
				}
				respQueryQuestion.setSvQuestionItems(svQuestionItems);
			}else if (new Integer(3).equals(questionList.get(i).getQuestType())){
				List<RespQuestionItem> svQuestionItems = new ArrayList<>();
				List<SampleAnswer> sampleAnswerList = sampleAnswerMapper.queryAnswerByQuestId(questId);
				Integer totalSamples = sampleAnswerMapper.queryCountAnswerByQuestId(questId);
				respQueryQuestion.setTotalSamp(totalSamples);
				for (int k = 0; k < sampleAnswerList.size(); k++) {
					RespQuestionItem respQuestionItem = new RespQuestionItem();
					String answer = sampleAnswerList.get(k).getAnswer();
					respQuestionItem.setAnswer(StringUtil.getJSONValue(new String(answer.getBytes("utf-8"),"utf-8")));
					svQuestionItems.add(respQuestionItem);
				}
				respQueryQuestion.setSvQuestionItems(svQuestionItems);
				
			}else if (new Integer(4).equals(questionList.get(i).getQuestType())
					|| new Integer(5).equals(questionList.get(i).getQuestType())) {
				Example matrixQuestionExample = new Example(Question.class);
				matrixQuestionExample.createCriteria().andEqualTo("status", 1).andEqualTo("matrixQuestId", questId);
				matrixQuestionExample.setOrderByClause("sortNum ASC");
				List<Question> matrixQuestionList = questionMapper.selectByExample(matrixQuestionExample);
				
				Example matrixQuestionItemExample = new Example(QuestionItem.class);
				matrixQuestionItemExample.createCriteria().andEqualTo("questId", matrixQuestionList.get(0).getQuestId());
				matrixQuestionItemExample.setOrderByClause("sortNum ASC");
				List<QuestionItem> matrixsQuestionItemsList = questionItemMapper.selectByExample(matrixQuestionItemExample);

				List<RespQuestionItem> categories = new ArrayList<>();
				for (int j = 0; j < matrixsQuestionItemsList.size(); j++) {
					RespQuestionItem QT = new RespQuestionItem();
					QT.setItemCont(matrixsQuestionItemsList.get(j).getItemCont());
					categories.add(QT);
				}
				respQueryQuestion.setCategories(categories);
				
				List<RespQueryMatriStamp> matrisList = new ArrayList<>();
				for (int k = 0; k < matrixQuestionList.size(); k++) {
					RespQueryMatriStamp matriStamp = new RespQueryMatriStamp();
					matriStamp.setQuestion(StringUtil.getJSONValue(matrixQuestionList.get(k).getQuestion()));
					
					Example questionItemExample = new Example(QuestionItem.class);
					questionItemExample.createCriteria().andEqualTo("questId", matrixQuestionList.get(k).getQuestId());
					questionItemExample.setOrderByClause("sortNum ASC");
					List<QuestionItem> questionItemsList = questionItemMapper.selectByExample(questionItemExample);
					List<RespQueryMatriItem> matrisItemList = new ArrayList<>();
					for (int l = 0; l < questionItemsList.size(); l++) {
						RespQueryMatriItem matriItem = new RespQueryMatriItem();
						Integer single = 0;
						if (new Integer(4).equals(questionList.get(i).getQuestType())) {
							single = sampleAnswerMapper.queryCountAnswerByItemIdEquals("," + questionItemsList.get(l) + ",");
						} else {
							single = sampleAnswerMapper.queryCountAnswerByItemIdLike("%," + questionItemsList.get(l) + ",%");
						}
						
						matriItem.setSum(StringUtil.getJSONValue(single));
						matrisItemList.add(matriItem);
					}
					matriStamp.setData(matrisItemList);
					matrisList.add(matriStamp);
				}
				
				Integer totalSamples = sampleAnswerMapper.queryCountAnswerByMatrixQuestId(questionList.get(i).getQuestId());
				respQueryQuestion.setTotalSamp(totalSamples);
				respQueryQuestion.setSeries(matrisList);
			}
			respQueryQuestionList.add(respQueryQuestion);
		}
		resp.setResult(respQueryQuestionList);
		return new CommonResp<RespQuerySubject>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}
	
	@Override
	public CommonResp<RespQuerySampleInfo> querySampleBySubjectid(CommonReq<ReqQuerySampleInfo> commReq) throws Exception {
		ReqQuerySampleInfo req = commReq.getParam();
		Integer subjectId = req.getSubjectId();
		Integer status = req.getStatus();
		if (StringUtil.isEmpty(subjectId)) {
			return new CommonResp<RespQuerySampleInfo>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
		List<RespQuerySampleInfo> respList = new ArrayList<>();
		PageInfo<Sample> page = null;
		if(req.getPage() != null && req.getPage().getPSize() > 0) {
			PageHelper.startPage(req.getPage().getPIndex(), req.getPage().getPSize());
		}
		Example sampleExample = new Example(Sample.class);
		sampleExample.createCriteria().andEqualTo("status", status).andEqualTo("subjectId", subjectId);
		List<Sample> sampleList = sampleMapper.selectByExample(sampleExample);
		if(sampleList != null) {
			page = new PageInfo<>(sampleList);
			Long total = page.getTotal();
			if(req.getPage() != null) {
				req.getPage().setPCount(total.intValue());
			}
			for (Sample sample : sampleList) {
				RespQuerySampleInfo resp = new RespQuerySampleInfo();
				BeanCopyUtils.copyProperties(sample, resp, null);
				resp.setOpertime(sample.getUpdateTime());
				respList.add(resp);
			}
		}else {
			return new CommonResp<RespQuerySampleInfo>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
		return new CommonResp<RespQuerySampleInfo>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList, req.getPage());
	}
	
	@Override
	public CommonResp<RespMap> querySampleCount(CommonReq<ReqQuerySampleInfo> commReq) throws Exception {
		ReqQuerySampleInfo req = commReq.getParam();
		Integer subjectId = req.getSubjectId();
		Integer status = req.getStatus();
		if (StringUtil.isEmpty(subjectId)) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
		Example sampleExample = new Example(Sample.class);
		Criteria criteria = sampleExample.createCriteria();
		if (StringUtil.isNotEmpty(status)) {
			criteria.andEqualTo("status", status).andEqualTo("subjectId", subjectId);
		}else {
			criteria.andEqualTo("subjectId", subjectId);
		}
		Integer sampleCount = sampleMapper.selectCountByExample(sampleExample);
		RespMap resp = new RespMap();
		resp.add(ApiKey.querySubjectsByOrgId.SampleCount, sampleCount);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}
	
	@Override
	public CommonResp<RespQuerySubject> querySubjectForNet(CommonReq<ReqQuerySubject> commReq) throws Exception {
		ReqQuerySubject req = commReq.getParam();
		String orgId = req.getOrgId();
		PageInfo<Subject> page = null;
		if(req.getPage() != null && req.getPage().getPSize() > 0) {
			PageHelper.startPage(req.getPage().getPIndex(), req.getPage().getPSize());
		}
		Example subjectExample = new Example(Subject.class);
		subjectExample.createCriteria().andNotEqualTo("status", 0).andEqualTo("surveyType", 4).andEqualTo("orgId", orgId);
		subjectExample.setOrderByClause(" subjectId DESC");
		List<Subject> subjectList = subjectMapper.selectByExample(subjectExample);
		List<RespQuerySubject> respList = new ArrayList<>();
		if(subjectList != null) {
			page = new PageInfo<>(subjectList);
			Long total = page.getTotal();
			if(req.getPage() != null) {
				req.getPage().setPCount(total.intValue());
			}
			for (Subject subject : subjectList) {
				RespQuerySubject resp = new RespQuerySubject();
				BeanCopyUtils.copyProperties(subject, resp, null);
				respList.add(resp);
			}
		}	
		return new CommonResp<RespQuerySubject>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList, req.getPage());
	}
	
	@Override
	public CommonResp<RespQueryQuestion> personStatisticaBySampleId(CommonReq<ReqQuerySampleInfo> commReq)
			throws Exception {
		ReqQuerySampleInfo req = commReq.getParam();
		String sampleId = req.getSampleId();
		Example sampleExample = new Example(Sample.class);
		sampleExample.createCriteria().andEqualTo("status", 2).andEqualTo("sampleId", sampleId);
		Sample sample = sampleMapper.selectOneByExample(sampleExample);
		RespQuerySampleInfo resp = new RespQuerySampleInfo();
		resp.setSampleName(sample.getUserName());
		
		Example questionExample = new Example(Question.class);
		questionExample.createCriteria().andEqualTo("status", 1).andEqualTo("matrixQuestId", 0).andEqualTo("subjectId", sample.getSubjectId());
		questionExample.setOrderByClause(" sortNum ASC");
		List<Question> svQuestions = questionMapper.selectByExample(questionExample);
		List<RespQueryQuestion> questionList = new ArrayList<>();
		for (int i = 0; i < svQuestions.size(); i++) {
			Question question = svQuestions.get(i);
			RespQueryQuestion queryQuestion = new RespQueryQuestion();
			queryQuestion.setQuestId(question.getQuestId());
			queryQuestion.setQuestType(question.getQuestType());
			queryQuestion.setQuestion(question.getQuestion());
			queryQuestion.setParentQuestId(question.getParentQuestId());
			List<RespQuestionItem> respQuestItemList = new ArrayList<>();
			if (new Integer(1).equals(svQuestions.get(i).getQuestType())) {  // 查询单选题答案
				List<SampleAnswer> answers = sampleAnswerMapper.queryAnswer(svQuestions.get(i).getQuestId(), sampleId);
				List<SampleInfoVo> sampleInfoList = sampleAnswerMapper.querySampleInfoById(svQuestions.get(i).getQuestId(), sampleId, 
						removeComma(answers==null || answers.size()<1 ? "" : answers.get(0).getAnswer()), 1);
				for (SampleInfoVo sampleInfoVo : sampleInfoList) {
					RespQuestionItem respQuestItem = new RespQuestionItem();
					respQuestItem.setItemId(StringUtil.getJSONValue(sampleInfoVo.getItemId()));
					respQuestItem.setOtherAnswer(StringUtil.getJSONValue(sampleInfoVo.getOtherAnswer()));
					respQuestItem.setItemCont(StringUtil.getJSONValue(sampleInfoVo.getItemCount()));
					respQuestItem.setIfAddblank(StringUtil.getJSONValue(sampleInfoVo.getIfAddBlank()));
					
					respQuestItemList.add(respQuestItem);
				}
			}else if (new Integer(2).equals(svQuestions.get(i).getQuestType())) {  // 查询多选题答案
				List<SampleAnswer> answers = sampleAnswerMapper.queryAnswer(svQuestions.get(i).getQuestId(), sampleId);
				List<SampleInfoVo> sampleInfoList = sampleAnswerMapper.querySampleInfoById(svQuestions.get(i).getQuestId(), sampleId, 
						answers==null || answers.size()<1 ? "":answers.get(0).getAnswer(), 2);
				for (SampleInfoVo sampleInfoVo : sampleInfoList) {
					RespQuestionItem respQuestItem = new RespQuestionItem();
					respQuestItem.setItemId(StringUtil.getJSONValue(sampleInfoVo.getItemId()));
					respQuestItem.setItemCont(StringUtil.getJSONValue(sampleInfoVo.getItemCount()));
					respQuestItem.setOtherAnswer(StringUtil.getJSONValue(sampleInfoVo.getOtherAnswer()));
					
					respQuestItemList.add(respQuestItem);
				}
			}else if (new Integer(3).equals(svQuestions.get(i).getQuestType())) {   // 查询填空题答案
				List<SampleAnswerVo> answers = sampleAnswerMapper.queryAnswers(svQuestions.get(i).getQuestId(), sampleId);
				for (SampleAnswerVo answerVo : answers) {
					RespQuestionItem respQuestItem = new RespQuestionItem();
//					respQuestItem.setItemId(StringUtil.getJSONValue(answerVo.getItemId()));
					respQuestItem.setAnswer(StringUtil.getJSONValue(answerVo.getAnswer()));
					
					respQuestItemList.add(respQuestItem);
				}
			}else if (new Integer(4).equals(svQuestions.get(i).getQuestType()) 
					|| new Integer(5).equals(svQuestions.get(i).getQuestType())) {  // 查询矩阵题答案
				Example matriQuestionsExample = new Example(Question.class);
				matriQuestionsExample.createCriteria().andEqualTo("status", 1).andEqualTo("matrixQuestId", svQuestions.get(i).getQuestId());
				matriQuestionsExample.setOrderByClause(" sortNum ASC");
				List<Question> matriQuestionList = questionMapper.selectByExample(matriQuestionsExample);
				List<RespMartrQueryQuestion> matriQuestions = new ArrayList<>();
				for (Question matriQuestion : matriQuestionList) {
					RespMartrQueryQuestion martriQuset = new RespMartrQueryQuestion();
					martriQuset.setQuestId(StringUtil.getJSONValue(matriQuestion.getQuestId()));
					martriQuset.setQuestType(StringUtil.getJSONValue(matriQuestion.getQuestType()));
					martriQuset.setQuestion(matriQuestion.getQuestion());
					List<RespQuestionItem> matriQuestionItems = new ArrayList<>();
					List<SampleAnswer> answers = sampleAnswerMapper.queryAnswer(svQuestions.get(i).getQuestId(), sampleId);
					List<SampleInfoVo> sampleInfoList = sampleAnswerMapper.querySampleInfoById(matriQuestion.getQuestId(), sampleId, 
							answers==null || answers.size()<1 ? "":answers.get(0).getAnswer(), svQuestions.get(i).getQuestType());
					for (SampleInfoVo sampleInfoVo : sampleInfoList) {
						RespQuestionItem respQuestItem = new RespQuestionItem();
						respQuestItem.setItemId(StringUtil.getJSONValue(sampleInfoVo.getItemId()));
						respQuestItem.setItemCont(sampleInfoVo.getItemCount());
						
						matriQuestionItems.add(respQuestItem);
					}
					martriQuset.setMatrixQuestItems(matriQuestionItems);
					matriQuestions.add(martriQuset);
				}
				queryQuestion.setChildrenMatrixQuestion(matriQuestions);
			}
			queryQuestion.setSvQuestionItems(respQuestItemList);
			questionList.add(queryQuestion);
		}
		
		return new CommonResp<RespQueryQuestion>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, questionList);
	}
	
	private String removeComma(String parma) {
		if (parma.endsWith(",")) {
			parma = parma.substring(0, parma.length() - 1);
		}
		if (parma.startsWith(",")) {
			parma = parma.substring(1);
		}
		return parma;
	}
	
	private Integer getSubjectMaxSort(Integer subjectId)throws Exception{
		Integer sort = null;
		Integer result = subjectMapper.selectMaxSort(subjectId);
		if (result == null){
			sort = 1;
		}else {
			sort = result;
		}
		return sort;
	}
	
	private Integer getQuestionMaxSort(Integer questionId)throws Exception{
		Integer sort = null;
		Integer result = questionMapper.selectMaxSort(questionId);
		if (result == null){
			sort = 1;
		}else {
			sort = result;
		}
		return sort;
	}
	
	private Integer createQuetionItem(Question question, String itemCont)throws Exception{
		QuestionItem questionItem = new QuestionItem();
		questionItem.setQuestId(question.getQuestId());
		questionItem.setItemCont(itemCont);
		questionItem.setItemId(questionItemMapper.getSequence("SURVERY_ITEMID_SEQ"));
		questionItem.setOperatorName(question.getOperatorName());
		questionItem.setOperatorId(question.getOperatorId());
		questionItem.setStatus(1);
		Integer max = getQuestionMaxSort(questionItem.getQuestId());
		questionItem.setSortNum(max);
		
		return questionItemMapper.insertSelective(questionItem);
	}
}
package com.kasite.client.qywechat.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kasite.client.qywechat.bean.chat.req.ReqChat;
import com.kasite.client.qywechat.bean.chat.vo.Chat;
import com.kasite.client.qywechat.constant.QyWeChatConstant;
import com.kasite.client.qywechat.dao.ActivityDao;
import com.kasite.client.qywechat.dao.AttachmentDao;
import com.kasite.client.qywechat.dao.MeetingApprovalDao;
import com.kasite.client.qywechat.dao.MeetingDao;
import com.kasite.client.qywechat.dao.MeetingEquipmentDao;
import com.kasite.client.qywechat.dao.MeetingRoomDao;
import com.kasite.client.qywechat.dao.MeetingSignDao;
import com.kasite.client.qywechat.dao.MemberCreditsInfoDao;
import com.kasite.client.qywechat.dao.MemberDao;
import com.kasite.client.qywechat.dao.MsgPushDao;
import com.kasite.client.qywechat.dao.QuestionAnswerDao;
import com.kasite.client.qywechat.dao.QuestionDao;
import com.kasite.client.qywechat.dao.QuestionItemDao;
import com.kasite.client.qywechat.dao.ToMemberDao;
import com.kasite.client.qywechat.dao.VoteQuestionDao;
import com.kasite.client.qywechat.dao.WarnValueDao;
import com.kasite.client.qywechat.util.WeiXinUtil;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.req.PageVo;
import com.kasite.core.common.resp.AbsResp;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.util.BeanCopyUtils;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.DateUtils;
import com.kasite.core.common.util.MatrixToImageWriter;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.serviceinterface.module.qywechat.IQyWeChatService;
import com.kasite.core.serviceinterface.module.qywechat.dbo.Activity;
import com.kasite.core.serviceinterface.module.qywechat.dbo.Attachment;
import com.kasite.core.serviceinterface.module.qywechat.dbo.Meeting;
import com.kasite.core.serviceinterface.module.qywechat.dbo.MeetingApproval;
import com.kasite.core.serviceinterface.module.qywechat.dbo.MeetingEquipment;
import com.kasite.core.serviceinterface.module.qywechat.dbo.MeetingRoom;
import com.kasite.core.serviceinterface.module.qywechat.dbo.MeetingSign;
import com.kasite.core.serviceinterface.module.qywechat.dbo.Member;
import com.kasite.core.serviceinterface.module.qywechat.dbo.MemberCreditsInfo;
import com.kasite.core.serviceinterface.module.qywechat.dbo.MsgPush;
import com.kasite.core.serviceinterface.module.qywechat.dbo.Question;
import com.kasite.core.serviceinterface.module.qywechat.dbo.QuestionAnswer;
import com.kasite.core.serviceinterface.module.qywechat.dbo.QuestionItem;
import com.kasite.core.serviceinterface.module.qywechat.dbo.ToMember;
import com.kasite.core.serviceinterface.module.qywechat.dbo.VoteQuestion;
import com.kasite.core.serviceinterface.module.qywechat.dbo.WarnValue;
import com.kasite.core.serviceinterface.module.qywechat.dto.MeetingSignVo;
import com.kasite.core.serviceinterface.module.qywechat.dto.MemberCreditsVo;
import com.kasite.core.serviceinterface.module.qywechat.dto.ToMemberVo;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqActivity;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqActivityAdd;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqActivityMeetingPush;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqAddVoteQuestion;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqDepartmentQuery;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqGetVoteQuestionInfoById;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqMeeting;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqMeetingAdd;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqMeetingApproval;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqMeetingApprovalAdd;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqMeetingApprovalUpdate;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqMeetingEquipment;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqMeetingRoom;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqMeetingSign;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqMember;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqMemberCreditsInfo;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqQuestionAdd;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqQuestionAnswerAdd;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqQuestionAnswerQuery;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqQuestionItemAdd;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqQuestionUpdate;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqQyWxConfigInfoGet;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqToMember;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqToMemberAdd;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqToMemberQuery;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqUID;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqUpdateVoteQuestion;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqUserQuery;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqVoteQuestionQuery;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqWarnValue;
import com.kasite.core.serviceinterface.module.qywechat.resp.RespActivity;
import com.kasite.core.serviceinterface.module.qywechat.resp.RespMeeting;
import com.kasite.core.serviceinterface.module.qywechat.resp.RespMeetingApproval;
import com.kasite.core.serviceinterface.module.qywechat.resp.RespMeetingEquipment;
import com.kasite.core.serviceinterface.module.qywechat.resp.RespMeetingRoom;
import com.kasite.core.serviceinterface.module.qywechat.resp.RespMeetingSign;
import com.kasite.core.serviceinterface.module.qywechat.resp.RespMember;
import com.kasite.core.serviceinterface.module.qywechat.resp.RespMemberCreditsInfo;
import com.kasite.core.serviceinterface.module.qywechat.resp.RespQRCode;
import com.kasite.core.serviceinterface.module.qywechat.resp.RespToMember;
import com.kasite.core.serviceinterface.module.qywechat.resp.RespUID;
import com.kasite.core.serviceinterface.module.qywechat.resp.RespUser;
import com.kasite.core.serviceinterface.module.qywechat.resp.RespVoteQuestion;
import com.kasite.core.serviceinterface.module.qywechat.resp.RespVoteQuestionAnalyze;
import com.kasite.core.serviceinterface.module.qywechat.resp.RespWarnValue;
import com.yihu.hos.util.JSONUtil;
import com.yihu.wsgw.api.InterfaceMessage;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 企业微信接口实现类
 * 
 * @author 無
 *
 */
@Service("qyWeChat.QyWeChatApi")
public class QyWeChatService implements IQyWeChatService {

	@Autowired
	private WarnValueDao warnValueDao;

	@Autowired
	private VoteQuestionDao voteQuestionDao;

//	@Autowired
//	private FaultRepairDao faultRepairDao;

	@Autowired
	private MeetingDao meetingDao;

	@Autowired
	private MeetingEquipmentDao meetingEquipmentDao;

	@Autowired
	private MeetingRoomDao meetingRoomDao;

	@Autowired
	private ToMemberDao toMemberDao;

	@Autowired
	private MemberDao memberDao;

	@Autowired
	private MemberCreditsInfoDao memberCreditsInfoDao;

	@Autowired
	private MeetingApprovalDao meetingApprovalDao;

	@Autowired
	private MeetingSignDao meetingSignDao;

	@Autowired
	private AttachmentDao attachmentDao;

	@Autowired
	private QuestionDao questionDao;

	@Autowired
	private QuestionItemDao questionItemDao;

	@Autowired
	private ActivityDao activityDao;

	@Autowired
	private QuestionAnswerDao questionAnswerDao;

	@Autowired
	MsgPushDao msgPushDao;

	/**
	 * 获取危急值信息（供LIS调用）
	 */
	@Override
	public String GetWarnValue(InterfaceMessage msg) throws Exception {
		return getWarnValue(new CommonReq<ReqWarnValue>(new ReqWarnValue(msg))).toResult();
	}

	@Override
	public CommonResp<RespWarnValue> getWarnValue(CommonReq<ReqWarnValue> req) throws Exception {

		ReqWarnValue reqWarnValue = req.getParam();
		System.out.println("===============================");
		System.out.println(reqWarnValue);
		WarnValue record = new WarnValue();
		record.setLisOrderid(reqWarnValue.getLisOrderid());
		record.setWard(reqWarnValue.getWard());
		record.setBedNo(reqWarnValue.getBedNo());
		record.setInHospitalNo(reqWarnValue.getInHospitalNo());
		record.setName(reqWarnValue.getName());
		record.setCheckIteam(reqWarnValue.getCheckIteam());
		record.setReference(reqWarnValue.getReference());
		record.setRemark(reqWarnValue.getRemark());
		record.setDoctorCode(reqWarnValue.getDoctorCode());
		record.setStatus(reqWarnValue.getStatus());
		record.setInsertTime(new Timestamp(System.currentTimeMillis()).toString());
		record.setUpdateTime(new Timestamp(System.currentTimeMillis()).toString());
		warnValueDao.insertSelective(record);
		return new CommonResp<RespWarnValue>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	/**
	 * 获取投票、问卷列表
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String QueryVoteQuestionList(InterfaceMessage msg) throws Exception {
		return QueryVoteQuestionList(new CommonReq<ReqVoteQuestionQuery>(new ReqVoteQuestionQuery(msg))).toResult();
	}

	@Override
	public CommonResp<RespVoteQuestion> QueryVoteQuestionList(CommonReq<ReqVoteQuestionQuery> req) throws Exception {
		ReqVoteQuestionQuery reqVo = req.getParam();
		// 设置分页
		int pIndex = 0;
		int pSize = 10;
		if (reqVo.getPage() != null) {
			pIndex = reqVo.getPage().getPIndex();
			pSize = reqVo.getPage().getPSize();
		}
		PageVo pageVo = new PageVo();
		pageVo.setPIndex(pIndex);
		pageVo.setPSize(pSize);
		PageHelper.startPage(pIndex + 1, pSize);

		// 查询
		List<RespVoteQuestion> list;
		// 企微查询
		if ("QY".equals(reqVo.getTag())) {
			// 发起者
			if ("true".equals(reqVo.getPower())) {
				list = voteQuestionDao.queryVoteQuestionListForQYByPower(reqVo);
				
				String[] deptIds = GetDeptIdsByUserId(reqVo.getConfigKey(), reqVo.getOpenId());
				List<RespVoteQuestion> list1 = voteQuestionDao.queryVoteQuestionListForQY(reqVo, deptIds);
				for (int i = 0; i < list1.size(); i++) {
					if (!list.contains(list1.get(i))) {
						list.add(list1.get(i));
					}
				}
			} else {
				// 根据userid获取所在科室
				String[] deptIds = GetDeptIdsByUserId(reqVo.getConfigKey(), reqVo.getOpenId());
				list = voteQuestionDao.queryVoteQuestionListForQY(reqVo, deptIds);
			}
		} else {
			list = voteQuestionDao.queryVoteQuestionList(reqVo);
		}
		// 获取参与人数
		Map<String, String> map;
		for (int i = 0; i < list.size(); i++) {
			map = getToMemberString(list.get(i).getUid(), "0");
			if (null != map && map.size() > 0) {
				if (map.get("d_memberIds") != null) {
					
					//list.get(i).setCount(map.get("d_memberIds").split(",").length);
					list.get(i).setCount(getPartUserNum(list.get(i).getUid()));
				} else {
					list.get(i).setCount(0);
				}
			}
			String endTime = DateUtils.format(
					DateUtils.formatStringtoDate(list.get(i).getEndTime(), DateUtils.DATE_TIME_PATTERN),
					"yyyy-MM-dd HH:mm");
			list.get(i).setEndTime(endTime);
		}

		PageInfo<RespVoteQuestion> page = new PageInfo<RespVoteQuestion>(list);
		// 获取总记录数
		pageVo.setPCount(Integer.parseInt(page.getTotal() + ""));
		return new CommonResp<RespVoteQuestion>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, list,
				pageVo);
		// return new CommonResp<RespVoteQuestion>(req, KstHosConstant.DEFAULTTRAN,
		// RetCode.Success.RET_10000,list);
	}

	/**
	 * 根据ID获取投票、问卷详情
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String GetVoteQuestionInfoById(InterfaceMessage msg) throws Exception {
		return GetVoteQuestionInfoById(new CommonReq<ReqGetVoteQuestionInfoById>(new ReqGetVoteQuestionInfoById(msg)))
				.toResult();
	}

	@Override
	public CommonResp<RespVoteQuestion> GetVoteQuestionInfoById(CommonReq<ReqGetVoteQuestionInfoById> req)
			throws Exception {
		ReqGetVoteQuestionInfoById reqVo = req.getParam();
		// 投票、问卷
		RespVoteQuestion resp = GetVoteQuestionInfoById(reqVo.getId());
		return new CommonResp<RespVoteQuestion>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	public RespVoteQuestion GetVoteQuestionInfoById(Long id) {
		// 投票、问卷
		RespVoteQuestion resp = new RespVoteQuestion();
		VoteQuestion record = new VoteQuestion();
		record.setId(id);
		VoteQuestion voteQuestion = voteQuestionDao.selectOne(record);
		if (voteQuestion != null) {
			BeanCopyUtils.copyProperties(voteQuestion, resp, null);
			String endTime = DateUtils.format(
					DateUtils.formatStringtoDate(resp.getEndTime(), DateUtils.DATE_TIME_PATTERN), "yyyy-MM-dd HH:mm");
			resp.setEndTime(endTime);
		}
		// 获取附件
		Map<String, String> map = getAttachment(resp.getUid());
		if (null != map && map.size() > 0) {
			resp.setAttachmentUrl(map.get("attachmentUrl"));
			resp.setAttachmentName(map.get("attachmentName"));
		}
		// 获取参与人
		Map<String, String> map2 = getToMemberString(resp.getUid(), "0");
		if (null != map2 && map2.size() > 0) {
			resp.setMemberIds(map2.get("d_memberIds"));
			resp.setMemberNames(map2.get("memberNames"));
			if (!StringUtil.isEmpty(map2.get("count"))) {
				resp.setCount(Integer.parseInt(map2.get("count")));
			} else {
				resp.setCount(0);
			}
		}
		return resp;
	}

	/**
	 * 获取投票、文件二维码地址
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String GetVoteQuestionQRCode(InterfaceMessage msg) throws Exception {
		return GetVoteQuestionQRCode(new CommonReq<ReqUID>(new ReqUID(msg))).toResult();
	}

	@Override
	public CommonResp<RespQRCode> GetVoteQuestionQRCode(CommonReq<ReqUID> req) throws Exception {
		ReqUID reqVo = req.getParam();

		VoteQuestion record = new VoteQuestion();
		record.setId(Long.parseLong(reqVo.getId()));
		VoteQuestion voteQuestion = voteQuestionDao.selectOne(record);

		if (voteQuestion == null) {
			return new CommonResp<RespQRCode>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_SYSTEM);
		}

		String mainUrl = KasiteConfig.getKasiteHosWebAppUrl();
		StringBuffer msgUrl = new StringBuffer();
		String toUrl = "/business/qywechat/wechat/html/ask/view.html?themeid=" + reqVo.getId();
		msgUrl.append(mainUrl).append("/qywechat/").append(reqVo.getQyClientId()).append("/")
				.append(reqVo.getQyConfigKey()).append("/gotoOauth.do?toUrl=")
				.append(URLEncoder.encode(mainUrl + toUrl, "utf-8"));
		// 生成二维码图片
		String qrPicUrl = MatrixToImageWriter.CreateQyQRCode(voteQuestion.getUid(), msgUrl.toString());
		RespQRCode resp = new RespQRCode();
		resp.setQrPicUrl(mainUrl + "/" + qrPicUrl);
		return new CommonResp<RespQRCode>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	/**
	 * 根据用户ID获取所在部门IDs
	 * 
	 * @param configKey
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	private String[] GetDeptIdsByUserId(String configKey, String userid) throws Exception {
		String[] deptIds = null;
		try {
			Member record = new Member();
			record.setMemberid(userid);
			Member member = memberDao.selectOne(record);
			if (null != member && StringUtil.isNotBlank(member.getDeptid())) {
				deptIds = member.getDeptid().split(",");
			}
		} catch (Exception e) {
			RespUser respUser = UserService.getUser(configKey, userid);
			deptIds = respUser.getDepartment();
		}
		return deptIds;
	}

	/**
	 * 根据用户ID获取姓名
	 * 
	 * @param configKey
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	private String GetNameByUserId(String configKey, String userid) throws Exception {
		String name = null;
		try {
			Member record = new Member();
			record.setMemberid(userid);
			Member member = memberDao.selectOne(record);
			if (null != member && StringUtil.isNotBlank(member.getMembername())) {
				name = member.getMembername();
			}
		} catch (Exception e) {
			RespUser respUser = UserService.getUser(configKey, userid);
			name = respUser.getName();
		}
		return name;
	}

	/**
	 * 投票、问卷消息推送
	 * 
	 * @param id
	 * @param qyConfigKey
	 * @param qyClientId
	 * @throws Exception
	 */
	private void PushMsgVoteQuestion(Long id, String qyConfigKey, String qyClientId) throws Exception {
		RespVoteQuestion resp = GetVoteQuestionInfoById(id);
		String toUrl = "";
		if (resp.getThemeType() == 0) {
			// 投票
			toUrl = "/business/qywechat/wechat/html/vote/submit.html?themeid=" + resp.getId();
		} else {
			// 问卷
			toUrl = "/business/qywechat/wechat/html/ask/submit.html?themeid=" + resp.getId();
		}
		PushMsg(qyConfigKey, qyClientId, resp.getMemberIds(), toUrl, resp.getUid(), resp.getTitle(), "阅读全文",
				resp.getAttachmentUrl(), 2);
	}

	/**
	 * 消息推送
	 * 
	 * @param qyConfigKey
	 * @param qyClientId
	 * @param memberIds
	 * @param toUrl
	 * @param uid
	 * @param title
	 * @param content       文本卡片消息格式参考如下： String content="<div
	 *                      class=\"gray\">2018年12月10日</div> " + "<div
	 *                      class=\"normal\">恭喜你抽中纸巾一包，领奖码：3523</div>" + "<div
	 *                      class=\"highlight\">请于2018年12月31日前联系黄嘤嘤领取</div>" + "<div
	 *                      class=\"highlight\">本消息由spring boot admin发送</div>";
	 * @param attachmentUrl
	 * @param msgType
	 * @throws Exception
	 */
	private void PushMsg(String qyConfigKey, String qyClientId, String memberIds, String toUrl, String uid,
			String title, String content, String attachmentUrl, int msgType) throws Exception {
		if (!StringUtil.isEmpty(memberIds)) {
			String[] memberIdStr = memberIds.split(",");
			String users = "";
			String depts = "";
			for (String member : memberIdStr) {
				if (StringUtil.isNotEmpty(member)) {
					if (member.startsWith("d_")) {
						depts = member + "|" + depts;
					} else {
						users = member + "|" + users;
					}
				}
			}
			if (StringUtil.isNotEmpty(users)) {
				users = users.substring(0, users.length() - 1);
			}
			if (StringUtil.isNotEmpty(depts)) {
				depts = depts.substring(0, depts.length() - 1);
				depts = depts.replace("d_", "");
			}
			// 保存消息
			MsgPush msg = new MsgPush();
			msg.setMsgPush(users, depts, uid, title, content, toUrl, qyConfigKey, qyClientId, attachmentUrl, msgType);
			msgPushDao.insertSelective(msg);
		}
	}

	/**
	 * 查询问题列表
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String GetQuestionList(InterfaceMessage msg) throws Exception {
		return GetQuestionList(new CommonReq<ReqUID>(new ReqUID(msg))).toResult();
	}

	@Override
	public CommonResp<Question> GetQuestionList(CommonReq<ReqUID> req) throws Exception {
		ReqUID reqVo = req.getParam();
		// 设置分页
//		int pIndex = 0;
//		int pSize = 10;
//		if (reqVo.getPage() != null) { 
//			pIndex = reqVo.getPage().getPIndex();
//			pSize = reqVo.getPage().getPSize();
//		}
//		PageVo pageVo = new PageVo();
//		pageVo.setPIndex(pIndex);
//		pageVo.setPSize(pSize);
//		PageHelper.startPage(pIndex + 1, pSize);

		List<Question> list = questionDao.queryQuestionList(reqVo);

//		PageInfo<Question> page = new PageInfo<Question>(list);
//		// 获取总记录数
//		pageVo.setPCount(Integer.parseInt(page.getTotal() + ""));
//		return new CommonResp<Question>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,list, pageVo);
		return new CommonResp<Question>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, list);
	}

	/**
	 * 根据ID获取问题
	 */
	@Override
	public String GetQuestionById(InterfaceMessage msg) throws Exception {
		return GetQuestionById(new CommonReq<ReqUID>(new ReqUID(msg))).toResult();
	}

	@Override
	public CommonResp<Question> GetQuestionById(CommonReq<ReqUID> req) throws Exception {
		ReqUID reqVo = req.getParam();
		Question question = new Question();

		if (!StringUtil.isEmpty(reqVo.getTag()) && reqVo.getTag().equals("uid")) {
			Example example = new Example(Question.class);
			Criteria criteria = example.createCriteria();
			criteria.andEqualTo("themeid", reqVo.getId());
			question = questionDao.selectOneByExample(example);
		} else {
			question = questionDao.selectByPrimaryKey(reqVo.getId());
		}

		return new CommonResp<Question>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, question);
	}

	/**
	 * 查询问题选项列表
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String GetQuestionItemList(InterfaceMessage msg) throws Exception {
		return GetQuestionItemList(new CommonReq<ReqUID>(new ReqUID(msg))).toResult();
	}

	@Override
	public CommonResp<QuestionItem> GetQuestionItemList(CommonReq<ReqUID> req) throws Exception {
		ReqUID reqVo = req.getParam();
		List<QuestionItem> list = questionItemDao.queryQuestionItemList(reqVo);
		return new CommonResp<QuestionItem>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, list);
	}

	/**
	 * 添加问题
	 */
	@Override
	public String AddQuestion(InterfaceMessage msg) throws Exception {
		return AddQuestion(new CommonReq<ReqQuestionAdd>(new ReqQuestionAdd(msg))).toResult();
	}

	@Override
	public CommonResp<Question> AddQuestion(CommonReq<ReqQuestionAdd> req) throws Exception {
		ReqQuestionAdd reqVo = req.getParam();
		Question record = new Question();
		record.setThemeid(reqVo.getThemeid());
		record.setQuestname(reqVo.getQuestname());
		record.setQuesttype(reqVo.getQuesttype());
		// 获取当前最大序号
		Integer no = questionDao.getMaxNo(reqVo);
		if (null == no) {
			no = 0;
		}
		record.setSortnum(no + 1);
		record.setStatus(0);
		record.setIsmust(reqVo.getIsmust());
		record.setOperatorid(reqVo.getOperatorid());
		record.setOperatorname(reqVo.getOperatorName());
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		record.setInserttime(dateFormat.format(date));
		record.setUpdatetime(dateFormat.format(date));
		int c = questionDao.insertSelective(record);
		System.out.println("添加问题受影响条数=" + c + "条");

		// 添加问题选项
		if (c > 0 && StringUtil.isNotEmpty(reqVo.getItemArray())) {
			AddQuestionItem(reqVo.getThemeid(), Integer.parseInt(record.getId() + ""), reqVo.getItemArray(),
					reqVo.getOperatorid(), reqVo.getOperatorName());
		}

		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	/**
	 * 根据ID删除问题、批量删除问题
	 */
	@Override
	public String DeleteQuestionById(InterfaceMessage msg) throws Exception {
		return DeleteQuestionById(new CommonReq<ReqUID>(new ReqUID(msg))).toResult();
	}

	@Override
	public CommonResp<Question> DeleteQuestionById(CommonReq<ReqUID> req) throws Exception {
		ReqUID reqVo = req.getParam();
		String[] array = reqVo.getId().split(",");
		int a = 0;
		for (int i = 0; i < array.length; i++) {
			a += questionDao.deleteByPrimaryKey(array[i]);
		}
		System.out.println("受影响条数：" + a + "条");
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	/**
	 * 修改问题
	 */
	@Override
	public String UpdateQuestion(InterfaceMessage msg) throws Exception {
		return UpdateQuestion(new CommonReq<ReqQuestionUpdate>(new ReqQuestionUpdate(msg))).toResult();
	}

	@Override
	public CommonResp<Question> UpdateQuestion(CommonReq<ReqQuestionUpdate> req) throws Exception {
		ReqQuestionUpdate reqVo = req.getParam();
		Question record = new Question();
		BeanCopyUtils.copyProperties(reqVo, record, null);
		int a = questionDao.updateByPrimaryKeySelective(record);
		System.out.println("受影响条数：" + a + "条");

		// 添加问题选项
		if (StringUtil.isNotEmpty(reqVo.getItemArray())) {
			AddQuestionItem(reqVo.getThemeid(), Integer.parseInt(record.getId() + ""), reqVo.getItemArray(),
					reqVo.getOperatorid(), reqVo.getOperatorName());
		} else {
			// 问答、打分
			if (reqVo.getQuesttype() == 2 || reqVo.getQuesttype() == 3) {
				// 清除问题选项
				Example example = new Example(QuestionItem.class);
				Criteria criteria = example.createCriteria();
				criteria.andEqualTo("questid", record.getId());
				criteria.andEqualTo("status", "0");
				questionItemDao.deleteByExample(example);
			}
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	/**
	 * 添加问题选项
	 */
	@Override
	public String AddQuestionItem(InterfaceMessage msg) throws Exception {
		return AddQuestionItem(new CommonReq<ReqQuestionItemAdd>(new ReqQuestionItemAdd(msg))).toResult();
	}

	@Override
	public CommonResp<QuestionItem> AddQuestionItem(CommonReq<ReqQuestionItemAdd> req) throws Exception {
		ReqQuestionItemAdd reqVo = req.getParam();
		// reqVo.getItemvalue()=["1@A", "2@B", "3@C", "4@D"]
		AddQuestionItem(reqVo.getThemeid(), reqVo.getQuestid(), reqVo.getItemvalue(), reqVo.getOperatorid(),
				reqVo.getOperatorname());
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	public int AddQuestionItem(String themeid, int questid, String itemvalue, String operatorid, String operatorName) {
		String[] itemvalueStr = itemvalue.split(",");
		// 先删除
		Example example = new Example(QuestionItem.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("questid", questid);
		criteria.andEqualTo("status", "0");
		questionItemDao.deleteByExample(example);

		// 后添加
		int c = 0;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String insertTime = dateFormat.format(new Date());
		for (int i = 0; i < itemvalueStr.length; i++) {
			if (StringUtil.isNotEmpty(itemvalueStr[i])) {
				QuestionItem record = new QuestionItem();
				record.setThemeid(themeid);
				record.setQuestid(questid);
				record.setItemvalue(itemvalueStr[i].split("@")[1]);
				record.setSortnum(Integer.parseInt(itemvalueStr[i].split("@")[0]));
				record.setStatus(0);
				record.setOperatorid(operatorid);
				record.setOperatorname(operatorName);
				record.setInserttime(insertTime);
				record.setUpdatetime(insertTime);
				c += questionItemDao.insertSelective(record);
			}
		}
		System.out.println("添加问题选项受影响条数=" + c + "条");
		return c;
	}

	/**
	 * 根据问题选项ID删除问题选项
	 */
	@Override
	public String DeleteQuestionItemById(InterfaceMessage msg) throws Exception {
		return DeleteQuestionItemById(new CommonReq<ReqUID>(new ReqUID(msg))).toResult();
	}

	@Override
	public CommonResp<QuestionItem> DeleteQuestionItemById(CommonReq<ReqUID> req) throws Exception {
		ReqUID reqVo = req.getParam();
		int a = questionItemDao.deleteByPrimaryKey(reqVo.getId());
		System.out.println("受影响条数：" + a + "条");
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	/**
	 * 获取附件
	 * 
	 * @param uid
	 * @return
	 */
	public Map<String, String> getAttachment(String uid) {
		Map<String, String> map = new HashMap<String, String>(16);
		// 附件
		Example example = new Example(Attachment.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("uid", uid);
		criteria.andEqualTo("status", "0");
		List<Attachment> attachmentList = attachmentDao.selectByExample(example);
		String attachmentUrl = "";
		String attachmentName = "";
		for (int i = 0; i < attachmentList.size(); i++) {
			Attachment a = attachmentList.get(i);
			if (i == 0) {
				attachmentUrl = a.getUrl();
				attachmentName = a.getName();
			} else {
				attachmentUrl += "," + a.getUrl();
				attachmentName += "," + a.getName();
			}
		}
		map.put("attachmentUrl", attachmentUrl);
		map.put("attachmentName", attachmentName);
		return map;
	}

	/**
	 * 获取参与人串
	 * 
	 * @param uid
	 * @param membertype
	 * @return
	 */
	public Map<String, String> getToMemberString(String uid, String membertype) {
		Map<String, String> map = new HashMap<String, String>(16);
		Example example = new Example(ToMember.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("uid", uid);
		criteria.andEqualTo("membertype", membertype);
		List<ToMember> toMemberList = toMemberDao.selectByExample(example);
		String d_memberIds = "";
		String memberIds = "";
		String memberNames = "";
		for (int i = 0; i < toMemberList.size(); i++) {
			ToMember m = toMemberList.get(i);
			String d_memberId = "";
			// 部门
			if (m.getIsDept() == 1) {
				d_memberId = "d_" + m.getMemberid();
			} else {
				d_memberId = m.getMemberid();
			}
			if (i == 0) {
				d_memberIds = d_memberId;
				memberIds = m.getMemberid();
				memberNames = m.getMembername();
			} else {
				d_memberIds += "," + d_memberId;
				memberIds += "," + m.getMemberid();
				memberNames += "," + m.getMembername();
			}
		}
		map.put("d_memberIds", d_memberIds);
		map.put("memberIds", memberIds);
		map.put("memberNames", memberNames);
		map.put("count", toMemberList.size() + "");
		return map;
	}

	/**
	 * 修改投票、问卷
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String UpdateVoteQuestion(InterfaceMessage msg) throws Exception {
		return UpdateVoteQuestion(new CommonReq<ReqUpdateVoteQuestion>(new ReqUpdateVoteQuestion(msg))).toResult();
	}

	@Override
	public CommonResp<RespVoteQuestion> UpdateVoteQuestion(CommonReq<ReqUpdateVoteQuestion> req) throws Exception {
		ReqUpdateVoteQuestion reqVo = req.getParam();
		// 修改、修改截止时间、发布
		if (StringUtil.isEmpty(reqVo.getUid()) && StringUtil.isEmpty(reqVo.getEndTime())
				&& StringUtil.isEmpty(reqVo.getStatus())) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM);
		}

		VoteQuestion record = new VoteQuestion();
		BeanCopyUtils.copyProperties(reqVo, record, null);
		int c = voteQuestionDao.updateByPrimaryKeySelective(record);

		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (StringUtil.isNotEmpty(reqVo.getAttachmentUrl())) {

			Example example = new Example(Attachment.class);
			Criteria criteria = example.createCriteria();
			criteria.andEqualTo("uid", reqVo.getUid());
			criteria.andEqualTo("status", "0");

			Attachment attachment = new Attachment();
			attachment.setUid(reqVo.getUid());
			attachment.setUrl(reqVo.getAttachmentUrl().replace("\\", "/"));
			attachment.setName(reqVo.getAttachmentName());
			attachment.setStatus(0);
			attachment.setInserttime(dateFormat.format(date));
			int a = attachmentDao.updateByExampleSelective(attachment, example);
			// 更新失败！不存在 新增
			if (a == 0) {
				a = attachmentDao.insert(attachment);
			}
			System.out.println("修改附件成功=" + a);
		}
		if (StringUtil.isNotEmpty(reqVo.getMemberIds())) {
			ReqToMemberAdd reqMemberAdd = new ReqToMemberAdd(reqVo.getMsg());
			reqMemberAdd.setUid(reqVo.getUid());
			reqMemberAdd.setMemberType(0);
			AddToMember(new CommonReq<ReqToMemberAdd>(reqMemberAdd));
		}

		// 发布 -> 推送消息
		if (null != reqVo.getStatus() && 1 == reqVo.getStatus()) {
			// 前后端取到的ConfigKey、ClientId不同
			String qyConfigKey = reqVo.getQyConfigKey();
			String qyClientId = reqVo.getQyClientId();
			if (StringUtil.isBlank(qyConfigKey)) {
				qyConfigKey = reqVo.getConfigKey();
				qyClientId = reqVo.getClientId();
			}
			PushMsgVoteQuestion(record.getId(), qyConfigKey, qyClientId);
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, "受影响条数:" + c);
	}

	/**
	 * 添加投票、问卷
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String AddVoteQuestion(InterfaceMessage msg) throws Exception {
		return AddVoteQuestion(new CommonReq<ReqAddVoteQuestion>(new ReqAddVoteQuestion(msg))).toResult();
	}

	@Override
	public CommonResp<RespVoteQuestion> AddVoteQuestion(CommonReq<ReqAddVoteQuestion> req) throws Exception {
		ReqAddVoteQuestion reqVo = req.getParam();

		VoteQuestion record = new VoteQuestion();
		// uid前端传入
//		String uid = CommonUtil.getUUID();
		String uid = reqVo.getUid();
		record.setUid(uid);
		record.setBeginIntro(reqVo.getBeginIntro());
		record.setEndingIntro(reqVo.getEndingIntro());
		record.setEndTime(reqVo.getEndTime());
		record.setOperatorId(reqVo.getOperatorId());
		record.setOperatorName(reqVo.getOperatorName());
		record.setRemark(reqVo.getRemark());
		record.setStartTime(reqVo.getStartTime());
		record.setStatus(reqVo.getStatus());
		record.setThemeType(reqVo.getThemeType());
		record.setTitle(reqVo.getTitle());

		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		record.setInsertTime(dateFormat.format(date));
		record.setUpdateTime(dateFormat.format(date));

		int id = voteQuestionDao.insertSelective(record);
		System.out.println("投票、问卷添加成功ID=" + record.getId());
		if (id > 0 && StringUtil.isNotEmpty(reqVo.getAttachmentUrl())) {
			Attachment attachment = new Attachment();
			attachment.setUid(uid);
			attachment.setUrl(reqVo.getAttachmentUrl().replace("\\", "/"));
			attachment.setName(reqVo.getAttachmentName());
			attachment.setStatus(0);
			attachment.setInserttime(dateFormat.format(date));
			id = attachmentDao.insertSelective(attachment);
			System.out.println("附件添加成功ID=" + attachment.getId());
		}
		// 添加参与人
		if (id > 0 && StringUtil.isNotEmpty(reqVo.getMemberIds())) {
			ReqToMemberAdd reqMemberAdd = new ReqToMemberAdd(reqVo.getMsg());
			reqMemberAdd.setUid(uid);
			reqMemberAdd.setMemberType(0);
			AddToMember(new CommonReq<ReqToMemberAdd>(reqMemberAdd));
			// 发布 -> 推送消息
			if (null != reqVo.getStatus() && 1 == reqVo.getStatus()) {
				PushMsgVoteQuestion(record.getId(), reqVo.getQyConfigKey(), reqVo.getQyClientId());
			}
		}
		if (id > 0) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, record.getId() + "");
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.CALL_SQL_ERROR);
	}

	/**
	 * 复制投票、问卷
	 */
	@Override
	public String CopyVoteQuestion(InterfaceMessage msg) throws Exception {
		return CopyVoteQuestion(new CommonReq<ReqUID>(new ReqUID(msg))).toResult();
	}

	@Override
	public CommonResp<RespUID> CopyVoteQuestion(CommonReq<ReqUID> req) throws Exception {
		ReqUID reqVo = req.getParam();
		// 根据ID获取投票问卷
		VoteQuestion record = voteQuestionDao.selectByPrimaryKey(reqVo.getId());
		String oldUid = record.getUid();
		// 复制投票问卷
		String newUid = CommonUtil.getUUID();
		record.setId(null);
		record.setUid(newUid);
		record.setStatus(0);
		record.setTitle(record.getTitle() + "(复制)");
		record.setOperatorId(reqVo.getOperatorName());
		record.setOperatorName(reqVo.getOperatorName());
		record.setInsertTime(null);
		record.setUpdateTime(null);
		voteQuestionDao.insertSelective(record);

		if (null != record.getId()) {
			// 复制参与人
			Example example = new Example(ToMember.class);
			Criteria criteria = example.createCriteria();
			criteria.andEqualTo("uid", oldUid);
			criteria.andEqualTo("membertype", 0);
			List<ToMember> toMemberList = toMemberDao.selectByExample(example);
			for (ToMember toMember : toMemberList) {
				toMember.setId(null);
				toMember.setUid(newUid);
				toMember.setInserttime(null);
				toMemberDao.insertSelective(toMember);
			}
			// 复制附件
			example = new Example(Attachment.class);
			criteria = example.createCriteria();
			criteria.andEqualTo("uid", oldUid);
			criteria.andEqualTo("status", "0");
			List<Attachment> attachmentList = attachmentDao.selectByExample(example);
			for (Attachment attachment : attachmentList) {
				attachment.setId(null);
				attachment.setUid(newUid);
				attachment.setInserttime(null);
				attachmentDao.insertSelective(attachment);
			}
			// 复制问题
			reqVo.setId(oldUid);
			List<Question> list = questionDao.queryQuestionList(reqVo);
			for (Question question : list) {
				int oldQuestId = Integer.parseInt(question.getId() + "");
				question.setId(null);
				question.setThemeid(newUid);
				question.setOperatorid(reqVo.getOperatorName());
				question.setOperatorname(reqVo.getOperatorName());
				question.setInserttime(null);
				question.setUpdatetime(null);
				questionDao.insertSelective(question);
				int newQuestId = Integer.parseInt(question.getId() + "");
				// 复制问题选项
				reqVo.setId(oldQuestId + "");
				List<QuestionItem> itemList = questionItemDao.queryQuestionItemList(reqVo);
				for (QuestionItem item : itemList) {
					item.setId(null);
					item.setThemeid(newUid);
					item.setQuestid(newQuestId);
					item.setOperatorid(reqVo.getOperatorName());
					item.setOperatorname(reqVo.getOperatorName());
					item.setInserttime(null);
					questionItemDao.insertSelective(item);
				}
			}
			RespUID respUID = new RespUID();
			respUID.setId(record.getId() + "");
			return new CommonResp<RespUID>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respUID);
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_SYSTEM, "复制失败！");
	}

	/**
	 * 复制投票、问卷
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String DeleteVoteQuestion(InterfaceMessage msg) throws Exception {
		return DeleteVoteQuestion(new CommonReq<ReqUID>(new ReqUID(msg))).toResult();
	}

	@Override
	public CommonResp<AbsResp> DeleteVoteQuestion(CommonReq<ReqUID> req) throws Exception {
		ReqUID reqVo = req.getParam();

		Example example = new Example(ToMember.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("id", reqVo.getId());

		VoteQuestion record = new VoteQuestion();
		record.setStatus(3);
		int a = voteQuestionDao.updateByExampleSelective(record, example);
		if (a > 0) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		} else {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_SYSTEM, "删除失败！");
		}
	}

	/**
	 * 投票、文件分析
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String GetVoteQuestionAnalyze(InterfaceMessage msg) throws Exception {
		return GetVoteQuestionAnalyze(new CommonReq<ReqUID>(new ReqUID(msg))).toResult();
	}

	@Override
	public CommonResp<RespVoteQuestionAnalyze> GetVoteQuestionAnalyze(CommonReq<ReqUID> req) throws Exception {
		ReqUID reqVo = req.getParam();
		List<RespVoteQuestionAnalyze> list = voteQuestionDao.getVoteQuestionAnalyzes(reqVo);

		return new CommonResp<RespVoteQuestionAnalyze>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,
				list);
	}

	/**
	 * 提交问卷答案
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String AddQuestionAnswer(InterfaceMessage msg) throws Exception {
		return AddQuestionAnswer(new CommonReq<ReqQuestionAnswerAdd>(new ReqQuestionAnswerAdd(msg))).toResult();
	}

	@Override
	public CommonResp<QuestionAnswer> AddQuestionAnswer(CommonReq<ReqQuestionAnswerAdd> req) throws Exception {
		ReqQuestionAnswerAdd reqVo = req.getParam();
		// 判断是否已回答过
		Example example = new Example(QuestionAnswer.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("themeid", reqVo.getThemeid());
		criteria.andEqualTo("operatorid", reqVo.getOpenId());
		List<QuestionAnswer> list = questionAnswerDao.selectByExample(example);
		if (null != list && list.size() > 0) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_SYSTEM, "请勿重复提交");
		}

		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String[] answers = reqVo.getAnswerArr().split(",");
		int c = 0;
		for (String a : answers) {
			String[] answer = a.split("@");
			if (!StringUtil.isEmpty(answer[1])) {
				QuestionAnswer record = new QuestionAnswer();
				record.setThemeid(reqVo.getThemeid());
				record.setQuestid(Integer.parseInt(answer[0]));
				record.setAnswer(answer[1]);
				record.setOperatorid(reqVo.getOpenId());
				record.setOperatorname(reqVo.getOpenId());
				record.setInserttime(dateFormat.format(date));
				record.setUpdatetime(dateFormat.format(date));
				c += questionAnswerDao.insertSelective(record);
			}
		}

		System.out.println("添加问题答案受影响条数=" + c + "条");

		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	/**
	 * 会议管理模块 - 申请会议
	 */
	@Override
	public String ApplyMeeting(InterfaceMessage msg) throws Exception {
		return applyMeeting(new CommonReq<ReqMeetingAdd>(new ReqMeetingAdd(msg))).toResult();
	}

	@Override
	public CommonResp<RespMeeting> applyMeeting(CommonReq<ReqMeetingAdd> req) throws Exception {
		ReqMeetingAdd reqMeetingAdd = req.getParam();
		
		List<Meeting> list = meetingDao.roomIsUse(reqMeetingAdd);
		if(list != null && !list.isEmpty()) {
			String sign = reqMeetingAdd.getAddress() + "已被占用，请重新选择会议时间或者其他会议室！";
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_SYSTEM, sign);
		}
		Meeting meeting = new Meeting();
		BeanCopyUtils.copyProperties(reqMeetingAdd, meeting, null);
		meeting.setInserttime(new Timestamp(System.currentTimeMillis()).toString());
		meeting.setUpdtetime(new Timestamp(System.currentTimeMillis()).toString());
		meeting.setOperatorid(reqMeetingAdd.getOpenId());
		Member member = new Member();
		member.setMemberid(reqMeetingAdd.getOpenId());
		List<Member> members = memberDao.select(member);
		if (members != null && !members.isEmpty()) {
			meeting.setOperatorname(members.get(0).getMembername());
		} else {
			meeting.setOperatorname(reqMeetingAdd.getOperatorName());
		}
		String uid = CommonUtil.getGUID();
		meeting.setMeetingid(uid);

		// 附件添加
		if (StringUtil.isNotBlank(reqMeetingAdd.getUrl())) {
			Attachment attachment = new Attachment();
			attachment.setUid(uid);
			attachment.setUrl(reqMeetingAdd.getUrl().replace("\\", "/"));
			attachment.setName(reqMeetingAdd.getFileName());
			attachment.setAttachmenttype(reqMeetingAdd.getAttachmenttype());
			attachment.setStatus(0);
			attachment.setInserttime(new Timestamp(System.currentTimeMillis()).toString());
			int insertA = attachmentDao.insert(attachment);
			if (insertA <= 0) {
				return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.CALL_SQL_ERROR);
			}
		}
		int insert = meetingDao.insert(meeting);
		if (insert > 0) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, uid);
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.CALL_SQL_ERROR);
	}

	/**
	 * 会议管理模块 - 查询会议列表
	 */
	@Override
	public String QueryMeeting(InterfaceMessage msg) throws Exception {
		return queryMeeting(new CommonReq<ReqMeeting>(new ReqMeeting(msg))).toResult();
	}

	@Override
	public CommonResp<RespMeeting> queryMeeting(CommonReq<ReqMeeting> req) throws Exception {
		ReqMeeting reqVo = req.getParam();
		List<Meeting> meetings = meetingDao.queryMeetingList(reqVo);
		List<RespMeeting> respMeetings = new ArrayList<>();
		if (meetings != null && !meetings.isEmpty()) {
			for (int i = 0; i < meetings.size(); i++) {
				RespMeeting resp = new RespMeeting();
				BeanCopyUtils.copyProperties(meetings.get(i), resp, null);

				// 查询详情时 返回二维码
				if (null != reqVo.getId() && reqVo.getId() != -1) {
					String mainUrl = KasiteConfig.getKasiteHosWebAppUrl();

					StringBuffer msgUrl = new StringBuffer();
					String toUrl = "/business/qywechat/wechat/html/meeting/signIn.html?uid=" + resp.getMeetingid();
					msgUrl.append(mainUrl).append("/qywechat/").append(reqVo.getQyClientId()).append("/")
							.append(reqVo.getQyConfigKey()).append("/gotoOauth.do?toUrl=")
							.append(URLEncoder.encode(mainUrl + toUrl, "utf-8"));
					String qrPicUrl = MatrixToImageWriter.CreateQyQRCode(resp.getMeetingid() + "_signIn",
							msgUrl.toString());
					resp.setQrPicSignInUrl(mainUrl + "/" + qrPicUrl);

					toUrl = "/business/qywechat/wechat/html/meeting/signOut.html?uid=" + resp.getMeetingid();
					msgUrl.setLength(0);
					msgUrl.append(mainUrl).append("/qywechat/").append(reqVo.getQyClientId()).append("/")
							.append(reqVo.getQyConfigKey()).append("/gotoOauth.do?toUrl=")
							.append(URLEncoder.encode(mainUrl + toUrl, "utf-8"));
					qrPicUrl = MatrixToImageWriter.CreateQyQRCode(resp.getMeetingid() + "_signOut", msgUrl.toString());
					resp.setQrPicSignOutUrl(mainUrl + "/" + qrPicUrl);
				}

				// 获得有效附件
				Attachment attachment = new Attachment();
				attachment.setUid(meetings.get(i).getMeetingid());
				attachment.setStatus(0);
				attachment.setAttachmenttype(1);
				List<Attachment> attachments = attachmentDao.select(attachment);
				if (attachments != null && !attachments.isEmpty()) {
					resp.setFileUrl(attachments.get(0).getUrl());
					resp.setFileName(attachments.get(0).getName());
				}

				respMeetings.add(resp);
			}
			// 获得当前登录人姓名
			respMeetings.get(0).setLoginMemberName(GetNameByUserId(reqVo.getConfigKey(), reqVo.getOpenId()));
		}
		return new CommonResp<RespMeeting>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respMeetings);
	}

	/**
	 * 会议管理模块 - 编辑会议信息
	 */
	@Override
	public String UpdateMeeting(InterfaceMessage msg) throws Exception {
		return updateMeeting(new CommonReq<ReqMeeting>(new ReqMeeting(msg))).toResult();
	}

	@Override
	public CommonResp<RespMeeting> updateMeeting(CommonReq<ReqMeeting> req) throws Exception {
		ReqMeeting reqMeeting = req.getParam();
		if (reqMeeting.getId() == null || reqMeeting.getId() == 0) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM);
		}
		if (reqMeeting.getMeetingid() == null || reqMeeting.getMeetingid() == "") {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM);
		}
		Meeting meeting = new Meeting();
		BeanCopyUtils.copyProperties(reqMeeting, meeting, null);
		int update = meetingDao.updateByPrimaryKeySelective(meeting);

		if (StringUtil.isNotEmpty(reqMeeting.getUrl())) {

			Example example = new Example(Attachment.class);
			Criteria criteria = example.createCriteria();
			criteria.andEqualTo("uid", reqMeeting.getMeetingid());
			criteria.andEqualTo("attachmenttype", reqMeeting.getAttachmenttype());
			criteria.andEqualTo("status", "0");

			Attachment attachment = new Attachment();
			attachment.setUid(reqMeeting.getMeetingid());
			attachment.setUrl(reqMeeting.getUrl().replace("\\", "/"));
			attachment.setName(reqMeeting.getFileName());
			attachment.setStatus(0);
			attachment.setAttachmenttype(reqMeeting.getAttachmenttype());
			attachment.setInserttime(new Timestamp(System.currentTimeMillis()).toString());
			int a = attachmentDao.updateByExampleSelective(attachment, example);
			// 更新失败！不存在 新增
			if (a == 0) {
				a = attachmentDao.insert(attachment);
			}
			System.out.println("修改附件成功=" + a);
		}
		if (update > 0) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.CALL_SQL_ERROR);
	}

	/**
	 * 会议管理模块 - 设备管理 - 新增设备
	 */
	@Override
	public String AddMeetingEquipment(InterfaceMessage msg) throws Exception {
		return addMeetingEquipment(new CommonReq<ReqMeetingEquipment>(new ReqMeetingEquipment(msg))).toResult();
	}

	@Override
	public CommonResp<RespMeetingEquipment> addMeetingEquipment(CommonReq<ReqMeetingEquipment> req) throws Exception {
		ReqMeetingEquipment reqEquipment = req.getParam();
		MeetingEquipment equipment = new MeetingEquipment();
		equipment.setName(reqEquipment.getName());
		List<MeetingEquipment> equipments = meetingEquipmentDao.select(equipment);
		if (equipments != null && !equipments.isEmpty()) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM);
		} else {
			equipment.setStatus(0);
			equipment.setInserttime(new Timestamp(System.currentTimeMillis()).toString());
			equipment.setUpdatetime(new Timestamp(System.currentTimeMillis()).toString());
			int insert = meetingEquipmentDao.insertSelective(equipment);
			if (insert > 0) {
				return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
			}
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.CALL_SQL_ERROR);
		}
	}

	/**
	 * 会议管理模块 - 设备管理 - 设备查询显示
	 */
	@Override
	public String QueryMeetingEquipment(InterfaceMessage msg) throws Exception {
		return queryMeetingEquipment(new CommonReq<ReqMeetingEquipment>(new ReqMeetingEquipment(msg))).toResult();
	}

	@Override
	public CommonResp<RespMeetingEquipment> queryMeetingEquipment(CommonReq<ReqMeetingEquipment> req) throws Exception {
		ReqMeetingEquipment reqMeetingEquipment = req.getParam();
		MeetingEquipment meetingEquipment = new MeetingEquipment();
		meetingEquipment.setStatus(reqMeetingEquipment.getStatus());
		if (reqMeetingEquipment.getId() != 0) {
			meetingEquipment.setId(reqMeetingEquipment.getId());
		}
		List<MeetingEquipment> equipments = meetingEquipmentDao.select(meetingEquipment);
		List<RespMeetingEquipment> resp = new ArrayList<>();
		if (equipments != null && !equipments.isEmpty()) {
			for (MeetingEquipment item : equipments) {
				RespMeetingEquipment respMeetingEquipment = new RespMeetingEquipment();
				BeanCopyUtils.copyProperties(item, respMeetingEquipment, null);
				resp.add(respMeetingEquipment);
			}
		} else {
			return new CommonResp<RespMeetingEquipment>(req, KstHosConstant.DEFAULTTRAN,
					RetCode.Common.ERROR_NOTRESULT);
		}
		return new CommonResp<RespMeetingEquipment>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	/**
	 * 会议管理模块 - 设备管理 - 修改设备信息
	 */
	@Override
	public String UpdateMeetingEquipment(InterfaceMessage msg) throws Exception {
		return updateMeetingEquipment(new CommonReq<ReqMeetingEquipment>(new ReqMeetingEquipment(msg))).toResult();
	}

	@Override
	public CommonResp<RespMeetingEquipment> updateMeetingEquipment(CommonReq<ReqMeetingEquipment> req)
			throws Exception {
		ReqMeetingEquipment reqME = req.getParam();
		if (reqME.getId() == null || reqME.getId() == 0) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM);
		}
		MeetingEquipment meetingEquipment = new MeetingEquipment();

		if (StringUtil.isNotBlank(reqME.getName())) {
			meetingEquipment.setName(reqME.getName());
			List<MeetingEquipment> equipments = meetingEquipmentDao.select(meetingEquipment);
			if (equipments != null && !equipments.isEmpty()) {
				return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM);
			}
		}

		BeanCopyUtils.copyProperties(reqME, meetingEquipment, null);
		int update = meetingEquipmentDao.updateByPrimaryKeySelective(meetingEquipment);
		if (update > 0) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.CALL_SQL_ERROR);

	}

	/**
	 * 会议管理模块 - 会议室管理 - 新增会议室
	 */
	@Override
	public String AddMeetingRoom(InterfaceMessage msg) throws Exception {
		return addMeetingRoom(new CommonReq<ReqMeetingRoom>(new ReqMeetingRoom(msg))).toResult();
	}

	@Override
	public CommonResp<RespMeetingRoom> addMeetingRoom(CommonReq<ReqMeetingRoom> req) throws Exception {
		ReqMeetingRoom reqMeetingRoom = req.getParam();
		MeetingRoom record = new MeetingRoom();
		BeanCopyUtils.copyProperties(reqMeetingRoom, record, null);
		record.setInserttime(new Timestamp(System.currentTimeMillis()).toString());
		record.setUpdatetime(new Timestamp(System.currentTimeMillis()).toString());
		int insert = meetingRoomDao.insertSelective(record);
		if (insert > 0) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.CALL_SQL_ERROR);
	}

	/**
	 * 会议管理模块 - 会议室管理 - 查询会议室列表
	 */
	@Override
	public String QueryMeetingRoom(InterfaceMessage msg) throws Exception {
		return queryMeetingRoom(new CommonReq<ReqMeetingRoom>(new ReqMeetingRoom(msg))).toResult();
	}

	@Override
	public CommonResp<RespMeetingRoom> queryMeetingRoom(CommonReq<ReqMeetingRoom> req) throws Exception {
		String equipmentNames = "";
		ReqMeetingRoom reqMeetingRoom = req.getParam();
		MeetingRoom room = new MeetingRoom();
		room.setStatus(reqMeetingRoom.getStatus());
		if (reqMeetingRoom.getId() != 0) {
			room.setId(reqMeetingRoom.getId());
		}
		List<MeetingRoom> meetingRooms = meetingRoomDao.select(room);
		List<RespMeetingRoom> resp = new ArrayList<>();
		if (meetingRooms != null && !meetingRooms.isEmpty()) {
			for (MeetingRoom item : meetingRooms) {
				RespMeetingRoom respMeetingRoom = new RespMeetingRoom();
				BeanCopyUtils.copyProperties(item, respMeetingRoom, null);
				if (respMeetingRoom.getEquipmentid() != "" || respMeetingRoom.getEquipmentid() != null) {
					String[] ids = respMeetingRoom.getEquipmentid().split(",");
					for (int i = 0; i < ids.length; i++) {
						String name = meetingEquipmentDao.selectByPrimaryKey(ids[i]).getName();
						equipmentNames = equipmentNames + name + ",";
					}
					equipmentNames = equipmentNames.substring(0, equipmentNames.length() - 1);
					respMeetingRoom.setEquipmentid(equipmentNames);
					equipmentNames = "";
				}
				resp.add(respMeetingRoom);
			}
		} else {
			return new CommonResp<RespMeetingRoom>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
		return new CommonResp<RespMeetingRoom>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	/**
	 * 会议管理模块 - 会议室管理 - 编辑会议室
	 */
	@Override
	public String UpdateMeetingRoom(InterfaceMessage msg) throws Exception {
		return updateMeetingRoom(new CommonReq<ReqMeetingRoom>(new ReqMeetingRoom(msg))).toResult();
	}

	@Override
	public CommonResp<RespMeetingRoom> updateMeetingRoom(CommonReq<ReqMeetingRoom> req) throws Exception {
		ReqMeetingRoom reqVo = req.getParam();
		if (reqVo.getId() == null || reqVo.getId() == 0) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM);
		}
		MeetingRoom record = new MeetingRoom();
		BeanCopyUtils.copyProperties(reqVo, record, null);
		record.setUpdatetime(new Timestamp(System.currentTimeMillis()).toString());
		int update = meetingRoomDao.updateByPrimaryKeySelective(record);
		if (update > 0) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.CALL_SQL_ERROR);
	}

	/**
	 * 会议管理模块 - 学分管理 - 查询成员学分列表
	 */
	@Override
	public String QueryMemberCredits(InterfaceMessage msg) throws Exception {
		return queryMemberCredits(new CommonReq<ReqMember>(new ReqMember(msg))).toResult();
	}

	@Override
	public CommonResp<RespMember> queryMemberCredits(CommonReq<ReqMember> req) throws Exception {
		List<MemberCreditsVo> vos = memberCreditsInfoDao.queryMemberCreditsList(req.getParam());
		List<RespMember> resp = new ArrayList<>();
		if (vos != null && !vos.isEmpty()) {
			for (MemberCreditsVo item : vos) {
				RespMember respMemberCredits = new RespMember();
				BeanCopyUtils.copyProperties(item, respMemberCredits, null);
				resp.add(respMemberCredits);
			}
		} else {
			return new CommonResp<RespMember>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
		return new CommonResp<RespMember>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	/**
	 * 会议管理模块 - 学分管理 - 变更成员学分
	 */
	@Override
	public String UpdateMemberCredits(InterfaceMessage msg) throws Exception {
		return updateMemberCredits(new CommonReq<ReqMemberCreditsInfo>(new ReqMemberCreditsInfo(msg))).toResult();
	}

	@Override
	public CommonResp<RespMemberCreditsInfo> updateMemberCredits(CommonReq<ReqMemberCreditsInfo> req) throws Exception {
		ReqMemberCreditsInfo reqMemberCreditsInfo = req.getParam();
		MemberCreditsInfo memberCreditsInfo = new MemberCreditsInfo();
		memberCreditsInfo.setInserttime(new Timestamp(System.currentTimeMillis()).toString());
		memberCreditsInfo.setUpdatetime(new Timestamp(System.currentTimeMillis()).toString());
		memberCreditsInfo.setMeetingid("0");
		if ("oneUpdate".equals(reqMemberCreditsInfo.getUpdateType())) {
			memberCreditsInfo.setMeetingtitle(reqMemberCreditsInfo.getOperatorName() + "后台操作，个人学分变更");
			memberCreditsInfo.setCredits(reqMemberCreditsInfo.getCredits());
			memberCreditsInfo.setMemberid(reqMemberCreditsInfo.getMemberid());
			int insert = memberCreditsInfoDao.insertSelective(memberCreditsInfo);
			if (insert <= 0) {
				return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.CALL_SQL_ERROR);
			}
		} else if ("oneClear".equals(reqMemberCreditsInfo.getUpdateType())) {
			memberCreditsInfo.setMeetingtitle(reqMemberCreditsInfo.getOperatorName() + "后台操作，个人学分清零");
			memberCreditsInfo.setCredits(reqMemberCreditsInfo.getCredits());
			memberCreditsInfo.setMemberid(reqMemberCreditsInfo.getMemberid());
			int insert = memberCreditsInfoDao.insertSelective(memberCreditsInfo);
			if (insert <= 0) {
				return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.CALL_SQL_ERROR);
			}
		} else if ("allClear".equals(reqMemberCreditsInfo.getUpdateType())) {
			memberCreditsInfo.setMeetingtitle(reqMemberCreditsInfo.getOperatorName() + "后台操作，全员学分清零");
			ReqMember reqMember = new ReqMember(req.getMsg());
			List<MemberCreditsVo> memberCreditsVos = memberCreditsInfoDao.queryMemberCreditsList(reqMember);
			for (int i = 0; i < memberCreditsVos.size(); i++) {
				memberCreditsInfo.setId(null);
				memberCreditsInfo.setMemberid(memberCreditsVos.get(i).getMemberid());
				memberCreditsInfo.setCredits(0 - memberCreditsVos.get(i).getAllCredits());
				if (memberCreditsInfo.getCredits() != 0) {
					int num = memberCreditsInfoDao.insertSelective(memberCreditsInfo);
					if (num <= 0) {
						return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.CALL_SQL_ERROR);
					}
				}
			}
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	/**
	 * 会议管理模块 - 学分管理 - 查询学分明细
	 */
	@Override
	public String QueryMemberCreditsInfo(InterfaceMessage msg) throws Exception {
		return queryMemberCreditsInfo(new CommonReq<ReqMemberCreditsInfo>(new ReqMemberCreditsInfo(msg))).toResult();
	}

	@Override
	public CommonResp<RespMemberCreditsInfo> queryMemberCreditsInfo(CommonReq<ReqMemberCreditsInfo> req)
			throws Exception {
		ReqMemberCreditsInfo reqMemberCreditsInfo = req.getParam();
		MemberCreditsInfo memberCreditsInfo = new MemberCreditsInfo();
		memberCreditsInfo.setMemberid(reqMemberCreditsInfo.getMemberid());
		List<MemberCreditsInfo> infos = memberCreditsInfoDao.select(memberCreditsInfo);
		List<RespMemberCreditsInfo> resp = new ArrayList<>();
		if (infos != null && !infos.isEmpty()) {
			for (MemberCreditsInfo item : infos) {
				RespMemberCreditsInfo info = new RespMemberCreditsInfo();
				BeanCopyUtils.copyProperties(item, info, null);
				resp.add(info);
			}
		} else {
			return new CommonResp<RespMemberCreditsInfo>(req, KstHosConstant.DEFAULTTRAN,
					RetCode.Common.ERROR_NOTRESULT);
		}
		return new CommonResp<RespMemberCreditsInfo>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	/**
	 * 会议管理模块 - 事项审批 - 查询事项列表
	 */
	@Override
	public String QueryMeetingApproval(InterfaceMessage msg) throws Exception {
		return queryMeetingApproval(new CommonReq<ReqMeetingApproval>(new ReqMeetingApproval(msg))).toResult();
	}

	@Override
	public CommonResp<RespMeetingApproval> queryMeetingApproval(CommonReq<ReqMeetingApproval> req) throws Exception {
		List<MeetingApproval> approvals = meetingApprovalDao.queryMeetingApproval(req.getParam());
		List<RespMeetingApproval> resp = new ArrayList<>();
//		if (approvals != null && !approvals.isEmpty()) {
			for (int i = 0; i < approvals.size(); i++) {
				RespMeetingApproval respma = new RespMeetingApproval();
				BeanCopyUtils.copyProperties(approvals.get(i), respma, null);
				Meeting meeting = new Meeting();
				meeting.setMeetingid(respma.getMeetingid());
				String operatorId = meetingDao.select(meeting).get(0).getOperatorid();
				if(operatorId.equals(req.getParam().getOpenId())) {
					respma.setMeetingPublisher("0");
				}else {
					respma.setMeetingPublisher("1");
				}
				resp.add(respma);
			}
//		} else {
//			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
//		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);

	}

	/**
	 * 会议管理模块 - 事项审批 - 修改事项审批
	 */
	@Override
	public String UpdateMeetingApproval(InterfaceMessage msg) throws Exception {
		return updateMeetingApproval(new CommonReq<ReqMeetingApprovalUpdate>(new ReqMeetingApprovalUpdate(msg)))
				.toResult();
	}

	@Override
	public CommonResp<RespMeetingApproval> updateMeetingApproval(CommonReq<ReqMeetingApprovalUpdate> req)
			throws Exception {
		ReqMeetingApprovalUpdate reqVo = req.getParam();
		if (reqVo.getId() == null || reqVo.getId() == 0) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM);
		}
		MeetingApproval record = new MeetingApproval();
		BeanCopyUtils.copyProperties(reqVo, record, null);
		record.setOperatorid(reqVo.getOpenId());
		
		Member member = new Member();
		member.setMemberid(reqVo.getOpenId());
		List<Member> members = memberDao.select(member);
		if(members != null && !members.isEmpty()) {
			record.setOperatorname(members.get(0).getMembername());
		}else {
			record.setOperatorname(reqVo.getOperatorName());
		}
		record.setUpdatetime(new Timestamp(System.currentTimeMillis()).toString());
		int update = meetingApprovalDao.updateByPrimaryKeySelective(record);
		if (update > 0) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.CALL_SQL_ERROR);
	}

	/**
	 * 会议管理模块 - 事项审批 - 添加事项审批
	 */
	@Override
	public String AddMeetingApproval(InterfaceMessage msg) throws Exception {
		return addMeetingApproval(new CommonReq<ReqMeetingApprovalAdd>(new ReqMeetingApprovalAdd(msg))).toResult();
	}

	@Override
	public CommonResp<RespMeetingApproval> addMeetingApproval(CommonReq<ReqMeetingApprovalAdd> req) throws Exception {
		String uid = CommonUtil.getGUID();
		ReqMeetingApprovalAdd add = req.getParam();
		String memberId = add.getMemberid();
		Member member = new Member();
		member.setMemberid(memberId);
		List<Member> members = memberDao.select(member);
		MeetingApproval record = new MeetingApproval();
		BeanCopyUtils.copyProperties(add, record, null);
		record.setApprovalid(uid);
		record.setMembername(members.get(0).getMembername());
		record.setDeptname(members.get(0).getDeptname());
		record.setInserttime(new Timestamp(System.currentTimeMillis()).toString());
		record.setUpdatetime(new Timestamp(System.currentTimeMillis()).toString());
		int insert = meetingApprovalDao.insertSelective(record);
		if (insert > 0) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, uid);
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.CALL_SQL_ERROR);
	}

	/**
	 * 会议管理模块 - 会议安排 - 统计查看
	 */
	@Override
	public String QueryMeetingSign(InterfaceMessage msg) throws Exception {
		return queryMeetingSign(new CommonReq<ReqMeetingSign>(new ReqMeetingSign(msg))).toResult();
	}

	@Override
	public CommonResp<RespMeetingSign> queryMeetingSign(CommonReq<ReqMeetingSign> req) throws Exception {
		int signInNum = 0;
		int signBackNum = 0;
		int leaveNum = 0;
		ReqMeetingSign reqMeetingSign = req.getParam();
		List<MeetingSignVo> vos = meetingSignDao.queryMeetingSign(reqMeetingSign);
		List<RespMeetingSign> resp = new ArrayList<>();
		if (vos != null && !vos.isEmpty()) {
			for (MeetingSignVo item : vos) {
				if (item.getIsDept().equals("0")) {
					if (item.getSignInType().equals("已签到")) {
						signInNum++;
					}
					if (item.getSignBackType().equals("已签退")) {
						signBackNum++;
					}
					if (StringUtil.isNotBlank(item.getReason())) {
						leaveNum++;
					}
					RespMeetingSign info = new RespMeetingSign();
					BeanCopyUtils.copyProperties(item, info, null);
					if (!resp.contains(info)) {
						resp.add(info);
					}
				} else if (item.getIsDept().equals("1")) {
					JSONObject jsonObject = UserService.getDepartmentUserDetails(reqMeetingSign.getConfigKey(),
							item.getMemberid(), "1");
					String result = jsonObject.getString("userlist");
					JSONArray jsonArray = JSONArray.fromObject(result);
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject user = (JSONObject) jsonArray.get(i);
						reqMeetingSign.setMemberid(user.getString("userid"));
						List<MeetingSignVo> vo = meetingSignDao.meetingSignDetail(reqMeetingSign);
						RespMeetingSign info = new RespMeetingSign();
						if (vo != null && !vo.isEmpty()) {
							MeetingSignVo vos0 = vo.get(0);
							if (vos0.getSignInType().equals("已签到")) {
								signInNum++;
							}
							if (vos0.getSignBackType().equals("已签退")) {
								signBackNum++;
							}
							if (StringUtil.isNotBlank(vos0.getReason())) {
								leaveNum++;
							}
							BeanCopyUtils.copyProperties(vos0, info, null);
							if (!resp.contains(info)) {
								resp.add(info);
							}
						}
					}
				}
			}
			resp.get(0).setSignInNum(signInNum);
			resp.get(0).setSignBackNum(signBackNum);
			resp.get(0).setLeaveNum(leaveNum);
		}

		// 获得成员的头像
		for (int i = 0; i < resp.size(); i++) {
			Member member = new Member();
			member.setMemberid(resp.get(i).getMemberid());
			List<Member> members = memberDao.select(member);
			if (members != null && !members.isEmpty()) {
				resp.get(i).setMemberAvatar(members.get(0).getMemberavatar());
			}
		}

		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	/**
	 * 微信端 - 会议管理模块 - 添加签到情况
	 */
	@Override
	public String AddMeetingSign(InterfaceMessage msg) throws Exception {
		return addMeetingSign(new CommonReq<ReqMeetingSign>(new ReqMeetingSign(msg))).toResult();
	}

	@Override
	public CommonResp<RespMeetingSign> addMeetingSign(CommonReq<ReqMeetingSign> req) throws Exception {
		ReqMeetingSign reqVo = req.getParam();
		String meetingId = reqVo.getUid();
		if (StringUtil.isBlank(meetingId)) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM);
		}
		MeetingSign meetingSign = new MeetingSign();
		BeanCopyUtils.copyProperties(reqVo, meetingSign, null);
		meetingSign.setInserttime(new Timestamp(System.currentTimeMillis()).toString());
		meetingSign.setMemberid(reqVo.getOpenId());
		meetingSign.setMembername(reqVo.getOperatorName());
		meetingSign.setPlace(reqVo.getPlace());
		int insert = meetingSignDao.insertSelective(meetingSign);
		if (insert > 0) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.CALL_SQL_ERROR);
	}

	/**
	 * 微信端 - 会议管理模块 - 会议查询
	 */
	@Override
	public String QwQueryMeeting(InterfaceMessage msg) throws Exception {
		return qwQueryMeeting(new CommonReq<ReqMeeting>(new ReqMeeting(msg))).toResult();
	}

	@Override
	public CommonResp<RespMeeting> qwQueryMeeting(CommonReq<ReqMeeting> req) throws Exception {
		ReqMeeting reqVo = req.getParam();

		// 根据userid获取所在科室
		String[] deptIdArr = GetDeptIdsByUserId(reqVo.getConfigKey(), reqVo.getOpenId());
		String deptIds = "";
		if (null != deptIdArr) {
			deptIds = StringUtils.join(deptIdArr, ",");
		}

		reqVo.setDeptIds(deptIds);
		// 会议列表显示
		List<Meeting> meetings = meetingDao.qwQueryMeetingList(reqVo);
		List<RespMeeting> respMeetings = new ArrayList<>();
		if (meetings != null && !meetings.isEmpty()) {
			for (int i = 0; i < meetings.size(); i++) {
				RespMeeting resp = new RespMeeting();
				BeanCopyUtils.copyProperties(meetings.get(i), resp, null);
				respMeetings.add(resp);
			}
			// 获得当前登录人姓名
			//respMeetings.get(0).setLoginMemberName(GetNameByUserId(reqVo.getConfigKey(), reqVo.getOpenId()));
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respMeetings);
	}

	/**
	 * 微信端 - 发起权限查询(可拓展)
	 */
	@Override
	public String QwQueryPower(InterfaceMessage msg) throws Exception {
		return qwQueryPower(new CommonReq<ReqToMember>(new ReqToMember(msg))).toResult();
	}

	@Override
	public CommonResp<RespToMember> qwQueryPower(CommonReq<ReqToMember> req) throws Exception {
		ReqToMember reqVo = req.getParam();

		// 根据userid获取所在科室
		String[] deptIdArr = GetDeptIdsByUserId(reqVo.getConfigKey(), reqVo.getOpenId());
		String deptIds = "";
		if (null != deptIdArr) {
			deptIds = StringUtils.join(deptIdArr, ",");
		}

		// 获取权限
		List<ToMember> power = toMemberDao.applyMeetingPower(reqVo.getOpenId(), deptIds, reqVo.getMembertype());
		List<RespToMember> resps = new ArrayList<>();
		if (power != null && !power.isEmpty()) {
			for (int i = 0; i < power.size(); i++) {
				RespToMember resp = new RespToMember();
				BeanCopyUtils.copyProperties(power.get(i), resp, null);
				resps.add(resp);
			}
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resps);
		} else {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
	}

	/**
	 * 活动管理模块 - 新建活动
	 */
	@Override
	public String AddActivity(InterfaceMessage msg) throws Exception {
		return addActivity(new CommonReq<ReqActivityAdd>(new ReqActivityAdd(msg))).toResult();
	}

	@Override
	public CommonResp<RespActivity> addActivity(CommonReq<ReqActivityAdd> req) throws Exception {
		ReqActivityAdd reqActivityAdd = req.getParam();
		Activity activity = new Activity();
		BeanCopyUtils.copyProperties(reqActivityAdd, activity, null);
		activity.setInserttime(new Timestamp(System.currentTimeMillis()).toString());
		activity.setUpdatetime(new Timestamp(System.currentTimeMillis()).toString());
		activity.setOperatorid(reqActivityAdd.getOpenId());
		Member member = new Member();
		member.setMemberid(reqActivityAdd.getOpenId());
		List<Member> members = memberDao.select(member);
		if (members != null && !members.isEmpty()) {
			activity.setOperatorname(members.get(0).getMembername());
		} else {
			activity.setOperatorname(reqActivityAdd.getOperatorName());
		}
		String uid = CommonUtil.getGUID();
		activity.setActivityid(uid);

		// 附件添加
		if (StringUtil.isNotBlank(reqActivityAdd.getAttachmentUrl())) {
			Attachment attachment = new Attachment();
			attachment.setUid(uid);
			attachment.setUrl(reqActivityAdd.getAttachmentUrl().replace("\\", "/"));
			attachment.setName(reqActivityAdd.getAttachmentName());
			attachment.setAttachmenttype(1);
			attachment.setStatus(0);
			attachment.setInserttime(new Timestamp(System.currentTimeMillis()).toString());
			int insertA = attachmentDao.insert(attachment);
			if (insertA <= 0) {
				return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.CALL_SQL_ERROR);
			}
		}

		// 附件图片
		if (StringUtil.isNotBlank(reqActivityAdd.getImgUrl())) {
			Attachment attachment = new Attachment();
			attachment.setUid(uid);
			attachment.setUrl(reqActivityAdd.getImgUrl().replace("\\", "/"));
			attachment.setName(reqActivityAdd.getImgName());
			attachment.setAttachmenttype(0);
			attachment.setStatus(0);
			attachment.setInserttime(new Timestamp(System.currentTimeMillis()).toString());
			int insertA = attachmentDao.insert(attachment);
			if (insertA <= 0) {
				return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.CALL_SQL_ERROR);
			}
		}

		int insert = activityDao.insert(activity);
		if (insert > 0) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, uid);
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.CALL_SQL_ERROR);
	}

	/**
	 * 活动管理模块 - 活动列表查询
	 */
	@Override
	public String QueryActivity(InterfaceMessage msg) throws Exception {
		return queryActivity(new CommonReq<ReqActivity>(new ReqActivity(msg))).toResult();
	}

	@Override
	public CommonResp<RespActivity> queryActivity(CommonReq<ReqActivity> req) throws Exception {
		ReqActivity reqActivity = req.getParam();
		List<Activity> activities = activityDao.queryActivityList(reqActivity);
		ToMember toMember = new ToMember();
		List<RespActivity> respActivities = new ArrayList<>();
		if (activities != null && !activities.isEmpty()) {
			for (int i = 0; i < activities.size(); i++) {
				RespActivity resp = new RespActivity();
				BeanCopyUtils.copyProperties(activities.get(i), resp, null);
				toMember.setUid(activities.get(i).getActivityid());
				// 阅读数
				toMember.setMembertype(10);
				int readNum = toMemberDao.select(toMember).size();
				resp.setReadNumber(readNum);
				// 参加人数
				toMember.setMembertype(2);
				int partNum = toMemberDao.select(toMember).size();
				resp.setPartNumber(partNum);
				respActivities.add(resp);
			}
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respActivities);
	}

	/**
	 * 活动管理模块 - 根据id获得活动信息
	 */
	@Override
	public String QueryActivityById(InterfaceMessage msg) throws Exception {
		return queryActivityById(new CommonReq<ReqActivity>(new ReqActivity(msg))).toResult();
	}

	@Override
	public CommonResp<RespActivity> queryActivityById(CommonReq<ReqActivity> req) throws Exception {
		ReqActivity reqActivity = req.getParam();
		RespActivity resp = new RespActivity();
		if (reqActivity.getId() == 0) {
			Activity record = new Activity();
			record.setActivityid(reqActivity.getActivityid());
			BeanCopyUtils.copyProperties(activityDao.select(record).get(0), resp, null);
		} else {
			Activity activitie = activityDao.selectByPrimaryKey(reqActivity.getId());
			BeanCopyUtils.copyProperties(activitie, resp, null);
		}

		// 查询详情时 返回二维码
		String mainUrl = KasiteConfig.getKasiteHosWebAppUrl();
		StringBuffer msgUrl = new StringBuffer();
		String toUrl = "/business/qywechat/wechat/html/activity/activityDetail.html?activityId=" + resp.getActivityid();
		msgUrl.append(mainUrl).append("/qywechat/").append(reqActivity.getQyClientId()).append("/")
				.append(reqActivity.getQyConfigKey()).append("/gotoOauth.do?toUrl=")
				.append(URLEncoder.encode(mainUrl + toUrl, "utf-8"));

		// 生成二维码图片
		String qrPicUrl = MatrixToImageWriter.CreateQyQRCode(resp.getActivityid(), msgUrl.toString());
		resp.setQrPicUrl(mainUrl + "/" + qrPicUrl);

		Attachment attachment = new Attachment();
		attachment.setUid(resp.getActivityid());
		attachment.setStatus(0);
		// 获得活动图片
		attachment.setAttachmenttype(0);
		List<Attachment> img = attachmentDao.select(attachment);
		if (img != null && !img.isEmpty()) {
			resp.setImgUrl(img.get(0).getUrl());
			resp.setImgName(img.get(0).getName());
		}
		// 获得活动附件
		attachment.setAttachmenttype(1);
		List<Attachment> attachments = attachmentDao.select(attachment);
		if (attachments != null && !attachments.isEmpty()) {
			resp.setAttachmentUrl(attachments.get(0).getUrl());
			resp.setAttachmentName(attachments.get(0).getName());
		}
		// 获得当前登录人姓名
		resp.setLoginMemberName(GetNameByUserId(reqActivity.getConfigKey(), reqActivity.getOpenId()));
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	/**
	 * 活动管理模块 - 修改活动信息
	 */
	@Override
	public String UpdateActivity(InterfaceMessage msg) throws Exception {
		return updateActivity(new CommonReq<ReqActivity>(new ReqActivity(msg))).toResult();
	}

	@Override
	public CommonResp<RespActivity> updateActivity(CommonReq<ReqActivity> req) throws Exception {
		ReqActivity reqActivity = req.getParam();
		if (reqActivity.getId() == null || reqActivity.getId() == 0) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM);
		}
		if (StringUtil.isBlank(reqActivity.getActivityid())) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM);
		}

		Activity activity = new Activity();
		BeanCopyUtils.copyProperties(reqActivity, activity, null);
		int update = activityDao.updateByPrimaryKeySelective(activity);

		Attachment attachment = new Attachment();
		attachment.setUid(reqActivity.getActivityid());
		attachment.setStatus(0);
		attachment.setInserttime(new Timestamp(System.currentTimeMillis()).toString());

		if (StringUtil.isNotEmpty(reqActivity.getImgUrl())) {
			Example example = new Example(Attachment.class);
			Criteria criteria = example.createCriteria();
			criteria.andEqualTo("uid", reqActivity.getActivityid());
			criteria.andEqualTo("status", "0");
			criteria.andEqualTo("attachmenttype", 0);

			attachment.setUrl(reqActivity.getImgUrl().replace("\\", "/"));
			attachment.setName(reqActivity.getImgName());
			attachment.setAttachmenttype(0);
			int a = attachmentDao.updateByExampleSelective(attachment, example);
			// 更新失败！不存在 新增
			if (a == 0) {
				a = attachmentDao.insert(attachment);
			}
			System.out.println("修改活动图片成功=" + a);
			if (a <= 0) {
				return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.CALL_SQL_ERROR);
			}
		}
		if (StringUtil.isNotEmpty(reqActivity.getAttachmentUrl())) {
			Example example = new Example(Attachment.class);
			Criteria criteria = example.createCriteria();
			criteria.andEqualTo("uid", reqActivity.getActivityid());
			criteria.andEqualTo("status", "0");
			criteria.andEqualTo("attachmenttype", 1);
			attachment.setUrl(reqActivity.getAttachmentUrl().replace("\\", "/"));
			attachment.setName(reqActivity.getAttachmentName());
			attachment.setAttachmenttype(1);
			int a = attachmentDao.updateByExampleSelective(attachment, example);
			// 更新失败！不存在 新增
			if (a == 0) {
				a = attachmentDao.insert(attachment);
			}
			System.out.println("修改活动附件成功=" + a);
			if (a <= 0) {
				return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.CALL_SQL_ERROR);
			}
		}
		if (update > 0) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.CALL_SQL_ERROR);
	}

	/**
	 * 活动管理模块 - 活动参加人员统计
	 */
	@Override
	public String ActivityStatistics(InterfaceMessage msg) throws Exception {
		return activityStatistics(new CommonReq<ReqToMemberQuery>(new ReqToMemberQuery(msg))).toResult();
	}

	@Override
	public CommonResp<RespToMember> activityStatistics(CommonReq<ReqToMemberQuery> req) throws Exception {
		ReqToMemberQuery reqToMember = req.getParam();
		List<ToMemberVo> vos = toMemberDao.queryToMemberAndDept(reqToMember);
		List<RespToMember> resp = new ArrayList<>();
		if (null != vos && vos.size() > 0) {
			for (ToMemberVo item : vos) {
				RespToMember respMember = new RespToMember();
				BeanCopyUtils.copyProperties(item, respMember, null);
				resp.add(respMember);
			}
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	/**
	 * 活动管理模块 - 活动列表查询 - 微信端
	 */
	@Override
	public String QwQueryActivity(InterfaceMessage msg) throws Exception {
		return qwQueryActivity(new CommonReq<ReqActivity>(new ReqActivity(msg))).toResult();
	}

	@Override
	public CommonResp<RespActivity> qwQueryActivity(CommonReq<ReqActivity> req) throws Exception {
		ReqActivity reqActivity = req.getParam();

		// 根据userid获取所在科室
		String[] deptIdArr = GetDeptIdsByUserId(reqActivity.getConfigKey(), reqActivity.getOpenId());
		String deptIds = "";
		if (null != deptIdArr) {
			deptIds = StringUtils.join(deptIdArr, ",");
		}

		reqActivity.setDeptIds(deptIds);
		ToMember toMember = new ToMember();
		// 活动列表显示
		List<Activity> activities = activityDao.qwQueryActivity(reqActivity);
		List<RespActivity> respActivitys = new ArrayList<>();
		if (activities != null && !activities.isEmpty()) {
			for (int i = 0; i < activities.size(); i++) {
				RespActivity resp = new RespActivity();
				BeanCopyUtils.copyProperties(activities.get(i), resp, null);
				// 参加人数
				toMember.setUid(resp.getActivityid());
				toMember.setMembertype(2);
				int partNum = toMemberDao.select(toMember).size();
				resp.setPartNumber(partNum);
				respActivitys.add(resp);
			}
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respActivitys);
	}

	/**
	 * 活动管理模块 - 进入群聊
	 */
	@Override
	public String QwActivityGroupChat(InterfaceMessage msg) throws Exception {
		return qwActivityGroupChat(new CommonReq<ReqActivity>(new ReqActivity(msg))).toResult();
	}

	@Override
	public CommonResp<RespActivity> qwActivityGroupChat(CommonReq<ReqActivity> req) throws Exception {
		ReqActivity reqActivity = req.getParam();
		String activityId = reqActivity.getActivityid();
		if (StringUtil.isBlank(activityId)) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.NULLERROR, "查无此活动，请联系管理员");
		}

		// 42001 token失效
		// 86003 聊天不存在
		// 获取群聊
		JSONObject jsonObject = AppChatService.getChat(reqActivity.getConfigKey(), activityId);
		// 存在该群聊
		if (jsonObject.getInt(QyWeChatConstant.ERR_CODE) == 0) {
			JSONObject chatJson = (JSONObject) jsonObject.get("chat_info");
			JSONArray chat_info = chatJson.getJSONArray("userlist");
			if (chat_info.size() == QyWeChatConstant.CHAT_INFO_SIZE) {
				//return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.NULLERROR, "群聊人数已达上限");
				req.getParam().setActivityid(activityId + "_0");
				qwActivityGroupChat(req);
			} else if (chat_info.size() < QyWeChatConstant.CHAT_INFO_SIZE) {
				int isexit = jsonObject.get("chat_info").toString().indexOf(reqActivity.getOpenId());
				// 不存在该用户则加入
				if (isexit == -1) {
					ReqChat reqchat = new ReqChat();
					reqchat.setChatid(activityId);
					String[] adduserlist = { reqActivity.getOpenId() };
					reqchat.setAdd_user_list(adduserlist);
					AppChatService.updateChat(reqActivity.getConfigKey(), reqchat);
					Member member = new Member();
					member.setMemberid(reqActivity.getOpenId());
					List<Member> members = memberDao.select(member);
					if (members != null && !members.isEmpty()) {
						AppChatService.sendTextChat(reqActivity.getConfigKey(), reqchat.getChatid(),
								"欢迎" + members.get(0).getMembername() + "加入");
					} else {
						AppChatService.sendTextChat(reqActivity.getConfigKey(), reqchat.getChatid(), "欢迎新成员加入");
					}
				} else {// 存在返回
					return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.NULLERROR, "已在群聊中");
				}
			}
		} else if (jsonObject.getInt(QyWeChatConstant.ERR_CODE) == QyWeChatConstant.CHAT_NO_EXIST_CODE) {
			// 获得活动创建人id
			Activity activity = new Activity();
			activity.setActivityid(activityId);
			List<Activity> activities = activityDao.select(activity);
			String operId = activities.get(0).getOperatorid();
			// 获得该活动目前的参与人
			ToMember toMember = new ToMember();
			toMember.setUid(activityId);
			toMember.setMembertype(2);
			int number = toMemberDao.selectCount(toMember);

			if (number < 1) {
				return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.NULLERROR, "该活动暂无开放的群聊");
			} else if (number == 1) {
				if (operId.equals(reqActivity.getOpenId())) {
					return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.NULLERROR,
							"您为本次活动发起人，待有其他人参与，届时会将您设置为管理员创建群聊");
				} else {
					// 创建一个群聊
					Chat chat = new Chat();
					chat.setChatid(activityId);
					chat.setOwner(operId);
					chat.setName(activities.get(0).getTitle() + "-活动群");
					String[] userlist = { operId, reqActivity.getOpenId() };
					chat.setUserlist(userlist);
					AppChatService.createChat(chat, reqActivity.getConfigKey());
					AppChatService.sendTextChat(reqActivity.getConfigKey(), chat.getChatid(),
							"欢迎参加" + activities.get(0).getTitle());
				}
			} else if (number > 1) {
				// 创建一个群聊
				Chat chat = new Chat();
				chat.setChatid(activityId);
				chat.setOwner(operId);
				chat.setName(activities.get(0).getTitle() + "-活动群");
				String[] userlist = { operId, reqActivity.getOpenId() };
				chat.setUserlist(userlist);
				AppChatService.createChat(chat, reqActivity.getConfigKey());
				AppChatService.sendTextChat(reqActivity.getConfigKey(), chat.getChatid(),
						"欢迎参加" + activities.get(0).getTitle());
			}
		} else if (jsonObject.getInt(QyWeChatConstant.ERR_CODE) == QyWeChatConstant.TOKEN_EXPIRED_CODE) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.NULLERROR, "token失效请刷新重试");
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	/**
	 * 活动会议 - 消息推送
	 */
	@Override
	public String ActivityMeetingPush(InterfaceMessage msg) throws Exception {
		return activityMeetingPush(new CommonReq<ReqActivityMeetingPush>(new ReqActivityMeetingPush(msg))).toResult();
	}

	@Override
	public CommonResp<RespActivity> activityMeetingPush(CommonReq<ReqActivityMeetingPush> req) throws Exception {
		ReqActivityMeetingPush push = req.getParam();
		String qyConfigKey = push.getQyConfigKey();
		String qyClientId = push.getQyClientId();
		String uid = push.getUid();
		// 活动
		if (push.getType().equals("0")) {
			Activity activity = new Activity();
			activity.setActivityid(uid);
			activity = activityDao.select(activity).get(0);
			// 获取附件
			Attachment attachment = new Attachment();
			attachment.setUid(uid);
			attachment.setStatus(0);
			attachment.setAttachmenttype(0);
			List<Attachment> attachments = attachmentDao.select(attachment);
			String url = "";
			if (attachments != null && !attachments.isEmpty()) {
				url = attachments.get(0).getUrl();
			}
			String toUrl = "/business/qywechat/wechat/html/activity/activityDetail.html?acId=" + activity.getId();
			PushMsg(qyConfigKey, qyClientId, push.getMemberIds(), toUrl, uid, activity.getTitle(), "阅读全文", url, 2);
		} else if (push.getType().equals("1")) {
			// 会议
			Meeting meeting = new Meeting();
			meeting.setMeetingid(uid);
			meeting = meetingDao.select(meeting).get(0);
			// ispush == 1 才推送
			if (meeting.getIspush() == 1) {
				String toUrl = "/business/qywechat/wechat/html/meeting/detail.html?meetingId=" + meeting.getId();
				String content = "<div class=\"normal\">发起人：" + meeting.getOperatorname() + "</div>"
						+"<div class=\"normal\">会议学分：" + meeting.getCredits() + "分</div>"
						+ "<div class=\"normal\">会议时间：" + meeting.getMeetingdate() + " " + meeting.getTimestart() + "~"
						+ meeting.getTimeend() + "</div>" + "<div class=\"normal\">会议地点：" + meeting.getAddress()
						+ "</div>";
				PushMsg(qyConfigKey, qyClientId, push.getMemberIds(), toUrl, uid, "会议通知：" + meeting.getTitle(), content,
						"", 1);
			}
		} else if (push.getType().equals("2")) {
			MeetingApproval approval = meetingApprovalDao.selectByPrimaryKey(uid);
			String result = "";
			// 会议
			Meeting meeting = new Meeting();
			meeting.setMeetingid(approval.getMeetingid());
			meeting = meetingDao.select(meeting).get(0);
			if ("1".equals(push.getLeaveStatus())) {
				result = "审核通过！";
			}else if ("2".equals(push.getLeaveStatus())) {
				result = "请假被驳回！";
			}
			String toUrl = "/business/qywechat/wechat/html/meeting/approvalDetail.html?approvalId=" + approval.getApprovalid();
			PushMsg(qyConfigKey, qyClientId, push.getMemberIds(), toUrl, approval.getApprovalid(), meeting.getTitle()+"-请假审批结果", result, "", 1);
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	/**
	 * 查询参与人
	 */
	@Override
	public String QueryToMember(InterfaceMessage msg) throws Exception {
		return QueryToMember(new CommonReq<ReqToMemberQuery>(new ReqToMemberQuery(msg))).toResult();
	}

	/**
	 * 查询参与人
	 */
	@Override
	public CommonResp<RespToMember> QueryToMember(CommonReq<ReqToMemberQuery> req) throws Exception {
		ReqToMemberQuery reqVo = req.getParam();

		Example example = new Example(ToMember.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("uid", reqVo.getUid());
		criteria.andEqualTo("membertype", reqVo.getMemberType());
		if(StringUtil.isNotEmpty(reqVo.getMemberId())) {
			criteria.andEqualTo("memberid", reqVo.getMemberId());
		}
		List<ToMember> toMemberList = toMemberDao.selectByExample(example);
		List<RespToMember> resp = new ArrayList<>();
		if (null != toMemberList && toMemberList.size() > 0) {
			for (ToMember item : toMemberList) {
				RespToMember respMember = new RespToMember();
				BeanCopyUtils.copyProperties(item, respMember, null);
				Member member = new Member();
				member.setMemberid(item.getMemberid());
				List<Member> members = memberDao.select(member);
				if (members != null && !members.isEmpty()) {
					respMember.setMemberAvatar(members.get(0).getMemberavatar());
				}
				resp.add(respMember);
			}
		}
		return new CommonResp<RespToMember>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	/**
	 * 添加参与人
	 */
	@Override
	public String AddToMember(InterfaceMessage msg) throws Exception {
		return AddToMember(new CommonReq<ReqToMemberAdd>(new ReqToMemberAdd(msg))).toResult();
	}

	/**
	 * 添加参与人
	 */
	@Override
	public CommonResp<RespToMember> AddToMember(CommonReq<ReqToMemberAdd> req) throws Exception {
		ReqToMemberAdd reqVo = req.getParam();

		String[] memberIdStr = reqVo.getMemberId().split(",");
		String[] memberNameStr = reqVo.getMemberName().split(",");
		if (memberIdStr.length != memberNameStr.length) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM, "参数个数不匹配!");
		}
		if (StringUtil.isEmpty(reqVo.getUid())) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM, "参数UID不能为空");
		}
		if (null == reqVo.getMemberType()) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM, "参数MemberType不能为空");
		}

		// 除了2=参加（报名） 3=请假 外,批量增加的都是先删除后重新添加
		if (reqVo.getMemberType() != 2 && reqVo.getMemberType() != 3) {
			Example example = new Example(ToMember.class);
			Criteria criteria = example.createCriteria();
			criteria.andEqualTo("uid", reqVo.getUid());
			criteria.andEqualTo("membertype", reqVo.getMemberType());
			toMemberDao.deleteByExample(example);
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String insertTime = dateFormat.format(new Date());
		for (int i = 0; i < memberIdStr.length; i++) {
			if (StringUtil.isNotEmpty(memberIdStr[i])) {
				ToMember record = new ToMember();
				record.setUid(reqVo.getUid());
				// 部门
				if (memberIdStr[i].startsWith("d_")) {
					record.setIsDept(1);
					record.setMemberid(memberIdStr[i].replace("d_", ""));
				} else {
					record.setIsDept(0);
					record.setMemberid(memberIdStr[i]);
				}
				record.setMembername(memberNameStr[i]);
				record.setMembertype(reqVo.getMemberType());
				List<ToMember> toMembers = toMemberDao.select(record);
				if (toMembers == null || toMembers.isEmpty()) {
					record.setInserttime(insertTime);
					toMemberDao.insertSelective(record);
					System.out.println("成员添加成功ID=" + record.getId());
				}
			}
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	
	/**
	 * 删除参与人员
	 */
	@Override
	public String DeleteToMember(InterfaceMessage msg) throws Exception {
		return deleteToMember(new CommonReq<ReqToMember>(new ReqToMember(msg))).toResult();
	}

	@Override
	public CommonResp<RespToMember> deleteToMember(CommonReq<ReqToMember> req) throws Exception {
		ReqToMember reqVo = req.getParam();
		Example example = new Example(ToMember.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("uid", reqVo.getUid());
		criteria.andEqualTo("memberid", reqVo.getMemberid());
		criteria.andEqualTo("membertype", reqVo.getMembertype());
		int delete = toMemberDao.deleteByExample(example);
		if (delete > 0) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		}else {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.CALL_SQL_ERROR);
		}
	}
	
	
	
	public static net.sf.json.JSONArray getDeptUser(String deptId, String wxkey, String deptName, String fetchChild)
			throws Exception {
		JSONObject deptObject = DepartmentService.getDepartmentList(deptId, wxkey);
		net.sf.json.JSONArray deptArray = deptObject.getJSONArray("department");

		JSONObject userObject = UserService.getDepartmentUserDetails(wxkey, deptId, fetchChild);
		net.sf.json.JSONArray userarray = userObject.getJSONArray("userlist");

		// 合并科室、人员
		for (int k = 0; k < userarray.size(); k++) {
			JSONObject ob = userarray.getJSONObject(k);
			net.sf.json.JSONArray array = ob.getJSONArray("department");
			for (int i = 0; i < array.size(); i++) {
				JSONObject user = new JSONObject();
				user.put("id", ob.getString("userid"));
//		    	if(StringUtil.isNotEmpty(JSONUtil.getJsonString(ob, "alias"))) {
//		    		 user.put("name", ob.getString("name")+"("+ob.getString("alias")+")");
//		    	}else {
				user.put("name", ob.getString("name"));
//				}
				user.put("pId", array.get(i));
				if (StringUtil.isBlank(JSONUtil.getJsonString(ob, "avatar"))) {
					user.put("icon", "../js/webCom1.0/img/user.png");
				} else {
					user.put("icon", ob.getString("avatar"));
				}

				if (!deptId.equals("1")) {
					if (array.getString(i).equals(deptId)) {
						deptArray.add(user);
					}
				} else {
					deptArray.add(user);
				}
			}
		}

		// 模糊查询
		if (StringUtil.isNotEmpty(deptName)) {
			net.sf.json.JSONArray resultarray = new net.sf.json.JSONArray();
			for (int i = 0; i < deptArray.size(); i++) {
				JSONObject ob = deptArray.getJSONObject(i);
				// 添加匹配项
				if (ob.getString("name").indexOf(deptName) != -1) {
					boolean tag = true;
					if (resultarray.size() > 0) {
						// 去重
						for (int j = 0; j < resultarray.size(); j++) {
							if (resultarray.getJSONObject(j).getString("id").equals(ob.getString("id"))) {
								tag = false;
								break;
							}
						}
					}
					if (tag) {
						resultarray.add(ob);
					}
				}
			}
			// 获取子部门、成员
			net.sf.json.JSONArray resultarray2 = resultarray;
			net.sf.json.JSONArray deptuserArray = new JSONArray();
			for (int i = 0; i < resultarray2.size(); i++) {
				JSONObject ob = resultarray2.getJSONObject(i);
				System.out.println("获取子部门、成员=" + i + ":" + resultarray2.size() + ":" + resultarray.getJSONObject(i));
				// 是部门 获取子部门、成员
				if (StringUtil.isNotEmpty(JSONUtil.getJsonString(ob, "order"))) {
					net.sf.json.JSONArray deptuser = getDeptUser(ob.getString("id"), wxkey, "", "1");
					for (int j = 0; j < deptuser.size(); j++) {
						// 自身不加人
						JSONObject object = deptuser.getJSONObject(j);
						if (!object.getString("id").equals(ob.getString("id"))) {
							deptuserArray.add(object);
							// 是部门 暂支持查询二级部门
							if (StringUtil.isNotEmpty(JSONUtil.getJsonString(object, "order"))) {
								net.sf.json.JSONArray deptuser2 = getDeptUser(object.getString("id"), wxkey, "", "1");
								for (int k = 0; k < deptuser2.size(); k++) {
									// 自身不加人
									JSONObject object2 = deptuser2.getJSONObject(k);
									if (!object2.getString("id").equals(object.getString("id"))) {
										deptuserArray.add(object2);
									}
								}
							}
						}
					}
				} else {
					// 人员 独立于部门之外展示
					System.out.println(resultarray.getJSONObject(i));
					resultarray.getJSONObject(i).put("pId", -1);
				}
			}
			// 将获取的子部门、成员加入结果集
			for (int j = 0; j < deptuserArray.size(); j++) {
				resultarray.add(deptuserArray.get(j));
			}
			deptArray = resultarray;
		}
		return deptArray;
	}

	/**
	 * 企微部门管理 - 获取部门列表
	 */
	@Override
	public String QueryDepartment(InterfaceMessage msg) throws Exception {
		CommonReq<ReqDepartmentQuery> req = new CommonReq<ReqDepartmentQuery>(new ReqDepartmentQuery(msg));
		ReqDepartmentQuery reqVo = req.getParam();

		net.sf.json.JSONArray resultarray = getDeptUser(reqVo.getId(), reqVo.getWxkey(), reqVo.getDeptName(), "1");
		for (int i = 0; i < resultarray.size(); i++) {
			JSONObject object = resultarray.getJSONObject(i);
			// 部门的parentid换成pId
			if (StringUtil.isNotEmpty(JSONUtil.getJsonString(object, "parentid"))) {
				object.put("pId", object.getString("parentid"));
//				object.put("icon", "../js/webCom1.0/img/dept.png");
			}
			// 顶级部门增加图标\展开
			if (JSONUtil.getJsonString(object, "id").equals("1")) {
				object.put("icon", "../js/webCom1.0/img/top.jpg");
				object.put("open", true);
			}

		}
		String result = resultarray.toString().replace("parentid", "pId");
		System.out.println("结果==" + result);
		return new CommonResp<ReqDepartmentQuery>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, result)
				.toResult();
	}

	/**
	 * 企微成员管理 - 获取部门成员
	 */
	@Override
	public String QueryUser(InterfaceMessage msg) throws Exception {
		CommonReq<ReqUserQuery> req = new CommonReq<ReqUserQuery>(new ReqUserQuery(msg));
		ReqUserQuery reqVo = req.getParam();
		JSONObject jsonObject = UserService.getDepartmentUserDetails(reqVo.getWxkey(), reqVo.getDepartment(), "1");
		String result = jsonObject.getString("userlist");
		return new CommonResp<ReqUserQuery>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, result)
				.toResult();
	}

	/**
	 * 获取企业微信配置
	 */
	@Override
	public String GetQyWxConfigInfo(InterfaceMessage msg) throws Exception {
		CommonReq<ReqQyWxConfigInfoGet> req = new CommonReq<ReqQyWxConfigInfoGet>(new ReqQyWxConfigInfoGet(msg));
		ReqQyWxConfigInfoGet reqVo = req.getParam();
		Map<String, Object> map = WeiXinUtil.getWxConfig(reqVo.getUrl(), reqVo.getConfigKey());
		JSONObject jsonObject = JSONObject.fromObject(map);
		System.out.println(jsonObject);
		return new CommonResp<ReqQyWxConfigInfoGet>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,
				jsonObject.toString()).toResult();
	}

	
	/**
	 * 获得参与人数(查询数据库)
	 * */
	public int getPartUserNum(String uid) {
		//参与人数
		List<String> ids = new ArrayList<>();
		
		Example example = new Example(ToMember.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("uid", uid);
		criteria.andEqualTo("membertype", 0);
		List<ToMember> toMemberList = toMemberDao.selectByExample(example);
		
		if(toMemberList != null && !toMemberList.isEmpty()) {
			for (int i = 0; i < toMemberList.size(); i++) {
				ToMember user = toMemberList.get(i);
				if (user.getIsDept() == 0) {
					//个人
					String memberId = user.getMemberid();
					if (!ids.contains(memberId)) {
						ids.add(memberId);
					}
				}else if (user.getIsDept() == 1) {
					//部门
					String deptId = user.getMemberid();
					
					List<Member> members = memberDao.selectAll();//获得所有成员
					for (int j = 0; j < members.size(); j++) {
						String deptids = members.get(j).getDeptid();
						if (deptids.indexOf(deptId) > 0) {
							String memberId = members.get(j).getMemberid();
							if (!ids.contains(memberId)) {
								ids.add(memberId);
							}
						}
					}
				}
			}
		}
		return ids.size();
	}

	@Override
	public String JudgeAnswerOrNot(InterfaceMessage msg) throws Exception {
		return judgeAnswerOrNot(new CommonReq<ReqQuestionAnswerQuery>(new ReqQuestionAnswerQuery(msg))).toResult();
	}

	@Override
	public CommonResp<QuestionAnswer> judgeAnswerOrNot(CommonReq<ReqQuestionAnswerQuery> req) throws Exception {
		ReqQuestionAnswerQuery reqVo = req.getParam();
		// 判断是否已回答过
		Example example = new Example(QuestionAnswer.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("themeid", reqVo.getThemeid());
		criteria.andEqualTo("operatorid", reqVo.getOpenId());
		List<QuestionAnswer> list = questionAnswerDao.selectByExample(example);
		if (null != list && list.size() > 0) {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_SYSTEM, "请勿重复提交");
		}else {
			return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, list);
		}
	}
}

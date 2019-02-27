package com.kasite.core.serviceinterface.module.qywechat;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.AbsResp;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.serviceinterface.module.qywechat.dbo.Question;
import com.kasite.core.serviceinterface.module.qywechat.dbo.QuestionAnswer;
import com.kasite.core.serviceinterface.module.qywechat.dbo.QuestionItem;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqActivity;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqActivityAdd;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqActivityMeetingPush;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqAddVoteQuestion;
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
import com.kasite.core.serviceinterface.module.qywechat.req.ReqToMember;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqToMemberAdd;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqToMemberQuery;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqUID;
import com.kasite.core.serviceinterface.module.qywechat.req.ReqUpdateVoteQuestion;
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
import com.kasite.core.serviceinterface.module.qywechat.resp.RespVoteQuestion;
import com.kasite.core.serviceinterface.module.qywechat.resp.RespVoteQuestionAnalyze;
import com.kasite.core.serviceinterface.module.qywechat.resp.RespWarnValue;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 企业微信接口
 * 
 * @author 無
 *
 */
public interface IQyWeChatService {

	/**
	 * 会议管理模块 - 申请会议
	 */
	public String ApplyMeeting(InterfaceMessage msg) throws Exception;

	CommonResp<RespMeeting> applyMeeting(CommonReq<ReqMeetingAdd> req) throws Exception;

	/**
	 * 会议管理模块 - 查询会议列表
	 */
	public String QueryMeeting(InterfaceMessage msg) throws Exception;

	CommonResp<RespMeeting> queryMeeting(CommonReq<ReqMeeting> req) throws Exception;

	/**
	 * 会议管理模块 - 编辑会议信息
	 */
	public String UpdateMeeting(InterfaceMessage msg) throws Exception;

	CommonResp<RespMeeting> updateMeeting(CommonReq<ReqMeeting> req) throws Exception;

	/**
	 * 会议管理模块 - 设备管理 - 新增设备
	 */
	public String AddMeetingEquipment(InterfaceMessage msg) throws Exception;

	CommonResp<RespMeetingEquipment> addMeetingEquipment(CommonReq<ReqMeetingEquipment> req) throws Exception;

	/**
	 * 会议管理模块 - 设备管理 - 设备查询显示
	 */
	public String QueryMeetingEquipment(InterfaceMessage msg) throws Exception;

	CommonResp<RespMeetingEquipment> queryMeetingEquipment(CommonReq<ReqMeetingEquipment> req) throws Exception;

	/**
	 * 会议管理模块 - 设备管理 - 修改设备信息
	 */
	public String UpdateMeetingEquipment(InterfaceMessage msg) throws Exception;

	CommonResp<RespMeetingEquipment> updateMeetingEquipment(CommonReq<ReqMeetingEquipment> req) throws Exception;

	/**
	 * 会议管理模块 - 会议室管理 - 新增会议室
	 */
	public String AddMeetingRoom(InterfaceMessage msg) throws Exception;

	CommonResp<RespMeetingRoom> addMeetingRoom(CommonReq<ReqMeetingRoom> req) throws Exception;

	/**
	 * 会议管理模块 - 会议室管理 - 查询会议室列表
	 */
	public String QueryMeetingRoom(InterfaceMessage msg) throws Exception;

	CommonResp<RespMeetingRoom> queryMeetingRoom(CommonReq<ReqMeetingRoom> req) throws Exception;

	/**
	 * 会议管理模块 - 会议室管理 - 编辑会议室
	 */
	public String UpdateMeetingRoom(InterfaceMessage msg) throws Exception;

	CommonResp<RespMeetingRoom> updateMeetingRoom(CommonReq<ReqMeetingRoom> req) throws Exception;

	/**
	 * 会议管理模块 - 学分管理 - 查询成员学分列表
	 */
	public String QueryMemberCredits(InterfaceMessage msg) throws Exception;

	CommonResp<RespMember> queryMemberCredits(CommonReq<ReqMember> req) throws Exception;

	/**
	 * 会议管理模块 - 学分管理 - 变更成员学分
	 */
	public String UpdateMemberCredits(InterfaceMessage msg) throws Exception;

	CommonResp<RespMemberCreditsInfo> updateMemberCredits(CommonReq<ReqMemberCreditsInfo> req) throws Exception;

	/**
	 * 会议管理模块 - 学分管理 - 查询学分明细
	 */
	public String QueryMemberCreditsInfo(InterfaceMessage msg) throws Exception;

	CommonResp<RespMemberCreditsInfo> queryMemberCreditsInfo(CommonReq<ReqMemberCreditsInfo> req) throws Exception;

	/**
	 * 会议管理模块 - 事项审批 - 查询事项列表
	 */
	public String QueryMeetingApproval(InterfaceMessage msg) throws Exception;

	CommonResp<RespMeetingApproval> queryMeetingApproval(CommonReq<ReqMeetingApproval> req) throws Exception;

	/**
	 * 会议管理模块 - 事项审批 - 修改事项审批
	 */
	public String UpdateMeetingApproval(InterfaceMessage msg) throws Exception;

	CommonResp<RespMeetingApproval> updateMeetingApproval(CommonReq<ReqMeetingApprovalUpdate> req) throws Exception;

	/**
	 * 会议管理模块 - 事项审批 - 添加事项审批
	 */
	public String AddMeetingApproval(InterfaceMessage msg) throws Exception;

	CommonResp<RespMeetingApproval> addMeetingApproval(CommonReq<ReqMeetingApprovalAdd> req) throws Exception;

	/**
	 * 会议管理模块 - 会议安排 - 统计查看
	 */
	public String QueryMeetingSign(InterfaceMessage msg) throws Exception;

	CommonResp<RespMeetingSign> queryMeetingSign(CommonReq<ReqMeetingSign> req) throws Exception;

	/**
	 * 微信端 - 会议管理模块 - 添加签到情况
	 */
	public String AddMeetingSign(InterfaceMessage msg) throws Exception;

	CommonResp<RespMeetingSign> addMeetingSign(CommonReq<ReqMeetingSign> req) throws Exception;

	/**
	 * 微信端 - 会议管理模块 - 会议查询
	 */
	public String QwQueryMeeting(InterfaceMessage msg) throws Exception;

	CommonResp<RespMeeting> qwQueryMeeting(CommonReq<ReqMeeting> req) throws Exception;

	/**
	 * 微信端 - 发起权限查询(可拓展)
	 */
	public String QwQueryPower(InterfaceMessage msg) throws Exception;

	CommonResp<RespToMember> qwQueryPower(CommonReq<ReqToMember> req) throws Exception;

	/**
	 * 活动管理模块 - 新建活动
	 */
	public String AddActivity(InterfaceMessage msg) throws Exception;

	CommonResp<RespActivity> addActivity(CommonReq<ReqActivityAdd> req) throws Exception;

	/**
	 * 活动管理模块 - 活动列表查询
	 */
	public String QueryActivity(InterfaceMessage msg) throws Exception;

	CommonResp<RespActivity> queryActivity(CommonReq<ReqActivity> req) throws Exception;

	/**
	 * 活动管理模块 - 根据id获得活动信息
	 */
	public String QueryActivityById(InterfaceMessage msg) throws Exception;

	CommonResp<RespActivity> queryActivityById(CommonReq<ReqActivity> req) throws Exception;

	/**
	 * 活动管理模块 - 修改活动信息
	 */
	public String UpdateActivity(InterfaceMessage msg) throws Exception;

	CommonResp<RespActivity> updateActivity(CommonReq<ReqActivity> req) throws Exception;

	/**
	 * 活动管理模块 - 活动参加人员统计
	 */
	public String ActivityStatistics(InterfaceMessage msg) throws Exception;

	CommonResp<RespToMember> activityStatistics(CommonReq<ReqToMemberQuery> req) throws Exception;

	/**
	 * 活动管理模块 - 活动列表查询 - 微信端
	 */
	public String QwQueryActivity(InterfaceMessage msg) throws Exception;

	CommonResp<RespActivity> qwQueryActivity(CommonReq<ReqActivity> req) throws Exception;

	/**
	 * 活动管理模块 - 进入群聊
	 */
	public String QwActivityGroupChat(InterfaceMessage msg) throws Exception;

	CommonResp<RespActivity> qwActivityGroupChat(CommonReq<ReqActivity> req) throws Exception;

	/**
	 * 活动会议 - 消息推送
	 */
	public String ActivityMeetingPush(InterfaceMessage msg) throws Exception;

	CommonResp<RespActivity> activityMeetingPush(CommonReq<ReqActivityMeetingPush> req) throws Exception;

	/**
	 * 获取危急值信息（供LIS调用）
	 * 
	 * @param
	 * @return
	 */
	public String GetWarnValue(InterfaceMessage msg) throws Exception;

	CommonResp<RespWarnValue> getWarnValue(CommonReq<ReqWarnValue> req) throws Exception;

	/**
	 * 获取投票、问卷列表
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String QueryVoteQuestionList(InterfaceMessage msg) throws Exception;

	CommonResp<RespVoteQuestion> QueryVoteQuestionList(CommonReq<ReqVoteQuestionQuery> req) throws Exception;

	/**
	 * 根据ID获取投票、问卷详情
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String GetVoteQuestionInfoById(InterfaceMessage msg) throws Exception;

	CommonResp<RespVoteQuestion> GetVoteQuestionInfoById(CommonReq<ReqGetVoteQuestionInfoById> req) throws Exception;

	/**
	 * 修改投票、问卷
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String UpdateVoteQuestion(InterfaceMessage msg) throws Exception;

	CommonResp<RespVoteQuestion> UpdateVoteQuestion(CommonReq<ReqUpdateVoteQuestion> req) throws Exception;

	/**
	 * 添加投票、问卷
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String AddVoteQuestion(InterfaceMessage msg) throws Exception;

	CommonResp<RespVoteQuestion> AddVoteQuestion(CommonReq<ReqAddVoteQuestion> req) throws Exception;

	/**
	 * 复制投票、问卷
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String CopyVoteQuestion(InterfaceMessage msg) throws Exception;

	CommonResp<RespUID> CopyVoteQuestion(CommonReq<ReqUID> req) throws Exception;

	/**
	 * 删除投票、问卷
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String DeleteVoteQuestion(InterfaceMessage msg) throws Exception;

	CommonResp<AbsResp> DeleteVoteQuestion(CommonReq<ReqUID> req) throws Exception;

	/**
	 * 投票、文件分析
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String GetVoteQuestionAnalyze(InterfaceMessage msg) throws Exception;

	public CommonResp<RespVoteQuestionAnalyze> GetVoteQuestionAnalyze(CommonReq<ReqUID> req) throws Exception;

	/**
	 * 添加问题
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String AddQuestion(InterfaceMessage msg) throws Exception;

	CommonResp<Question> AddQuestion(CommonReq<ReqQuestionAdd> req) throws Exception;

	/**
	 * 查询问题列表
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String GetQuestionList(InterfaceMessage msg) throws Exception;

	CommonResp<Question> GetQuestionList(CommonReq<ReqUID> req) throws Exception;

	/**
	 * 根据ID获取问题
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String GetQuestionById(InterfaceMessage msg) throws Exception;

	public CommonResp<Question> GetQuestionById(CommonReq<ReqUID> req) throws Exception;

	/**
	 * 根据ID删除问题、批量删除问题
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String DeleteQuestionById(InterfaceMessage msg) throws Exception;

	public CommonResp<Question> DeleteQuestionById(CommonReq<ReqUID> req) throws Exception;

	/**
	 * 修改问题
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String UpdateQuestion(InterfaceMessage msg) throws Exception;

	public CommonResp<Question> UpdateQuestion(CommonReq<ReqQuestionUpdate> req) throws Exception;

	/**
	 * 添加问题选项
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String AddQuestionItem(InterfaceMessage msg) throws Exception;

	CommonResp<QuestionItem> AddQuestionItem(CommonReq<ReqQuestionItemAdd> req) throws Exception;

	/**
	 * 查询问题选项列表
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String GetQuestionItemList(InterfaceMessage msg) throws Exception;

	CommonResp<QuestionItem> GetQuestionItemList(CommonReq<ReqUID> req) throws Exception;

	/**
	 * 根据问题id删除问题选项
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String DeleteQuestionItemById(InterfaceMessage msg) throws Exception;

	public CommonResp<QuestionItem> DeleteQuestionItemById(CommonReq<ReqUID> req) throws Exception;

	/**
	 * 添加问题答案
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String AddQuestionAnswer(InterfaceMessage msg) throws Exception;

	CommonResp<QuestionAnswer> AddQuestionAnswer(CommonReq<ReqQuestionAnswerAdd> req) throws Exception;

	/**
	 * 获取参与人员列表
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String QueryToMember(InterfaceMessage msg) throws Exception;

	CommonResp<RespToMember> QueryToMember(CommonReq<ReqToMemberQuery> req) throws Exception;

	/**
	 * 添加参与人员
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String AddToMember(InterfaceMessage msg) throws Exception;

	CommonResp<RespToMember> AddToMember(CommonReq<ReqToMemberAdd> req) throws Exception;
	
	/**
	 * 删除参与人员
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String DeleteToMember(InterfaceMessage msg) throws Exception;
	CommonResp<RespToMember> deleteToMember(CommonReq<ReqToMember> req) throws Exception;
	
	

	/**
	 * 企微部门管理 - 获取部门列表
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String QueryDepartment(InterfaceMessage msg) throws Exception;

	/**
	 * 企微成员管理 - 获取部门成员
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String QueryUser(InterfaceMessage msg) throws Exception;

	public String GetQyWxConfigInfo(InterfaceMessage msg) throws Exception;

	/**
	 * 获取投票、问卷二维码地址
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String GetVoteQuestionQRCode(InterfaceMessage msg) throws Exception;

	public CommonResp<RespQRCode> GetVoteQuestionQRCode(CommonReq<ReqUID> req) throws Exception;

	/**
	 * 判断投票、问卷是否已经回答
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String JudgeAnswerOrNot(InterfaceMessage msg) throws Exception;

	public CommonResp<QuestionAnswer> judgeAnswerOrNot(CommonReq<ReqQuestionAnswerQuery> req) throws Exception;
}

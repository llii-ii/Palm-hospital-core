package com.kasite.core.serviceinterface.module.msg;

import java.util.Map;

import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.module.msg.req.ReqAddMsgScene;
import com.kasite.core.serviceinterface.module.msg.req.ReqAddMsgSource;
import com.kasite.core.serviceinterface.module.msg.req.ReqAddMsgTemp;
import com.kasite.core.serviceinterface.module.msg.req.ReqAddMsgUserOpenId;
import com.kasite.core.serviceinterface.module.msg.req.ReqDeleteMsgTemp;
import com.kasite.core.serviceinterface.module.msg.req.ReqEditMsgScene;
import com.kasite.core.serviceinterface.module.msg.req.ReqEditMsgSource;
import com.kasite.core.serviceinterface.module.msg.req.ReqEditMsgTemp;
import com.kasite.core.serviceinterface.module.msg.req.ReqEditMsgUserOpenId;
import com.kasite.core.serviceinterface.module.msg.req.ReqMsgCenterMainCount;
import com.kasite.core.serviceinterface.module.msg.req.ReqMsgOpenIdSceneList;
import com.kasite.core.serviceinterface.module.msg.req.ReqMsgQueue;
import com.kasite.core.serviceinterface.module.msg.req.ReqMsgQueueList;
import com.kasite.core.serviceinterface.module.msg.req.ReqMsgSceneList;
import com.kasite.core.serviceinterface.module.msg.req.ReqMsgSourceList;
import com.kasite.core.serviceinterface.module.msg.req.ReqMsgTempList;
import com.kasite.core.serviceinterface.module.msg.req.ReqMsgUserOpenIdList;
import com.kasite.core.serviceinterface.module.msg.req.ReqQueryAutoReplayByFollow;
import com.kasite.core.serviceinterface.module.msg.req.ReqQueryOpenId;
import com.kasite.core.serviceinterface.module.msg.req.ReqReadMsg;
import com.kasite.core.serviceinterface.module.msg.req.ReqReplayMgr;
import com.kasite.core.serviceinterface.module.msg.req.ReqSendSms;
import com.kasite.core.serviceinterface.module.msg.resp.MsgCenterPageHelper;
import com.kasite.core.serviceinterface.module.msg.resp.RespMsgQueueList;
import com.kasite.core.serviceinterface.module.msg.resp.RespMsgScene;
import com.kasite.core.serviceinterface.module.msg.resp.RespMsgSource;
import com.kasite.core.serviceinterface.module.msg.resp.RespMsgTemp;
import com.kasite.core.serviceinterface.module.msg.resp.RespMsgUserOpenId;
import com.kasite.core.serviceinterface.module.msg.resp.RespQueryAutoReplayArbitrarily;
import com.kasite.core.serviceinterface.module.msg.resp.RespQueryAutoReplayByFollow;
import com.kasite.core.serviceinterface.module.msg.resp.RespQueryOpenId;
import com.kasite.core.serviceinterface.module.wechat.req.ReqQueryAllTemplateList;
import com.kasite.core.serviceinterface.module.yy.req.ReqPushRecipeIn;
import com.yihu.wsgw.api.InterfaceMessage;

import net.sf.json.JSONObject;

/**
 * @author linjf 2017年11月14日 17:52:25
 * TODO 消息接口类
 */
public interface IMsgService extends MsgCommonService{
	
	enum Type{
		DEFAULT,
		;
	}
	
	
	Type accept();
	/**
	 * 推送处方
	 * @param msg
	 * @return
	 */
	String PushRecipe(InterfaceMessage msg) throws Exception;

	/**
	 * 推送处方消息
	 * @param msg
	 * @param list
	 * @return
	 */
	CommonResp<RespMap> pushRecipe(CommonReq<ReqPushRecipeIn> req) throws Exception;
	/**
	 * 关注消息自动回复
	 * @param msg
	 * @return
	 */
	public String QueryAutoReplayByFollow(InterfaceMessage msg) throws Exception;
	
	/**
	 * 关注消息自动回复
	 * @Description: 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryAutoReplayByFollow> queryAutoReplayByFollow(CommonReq<ReqQueryAutoReplayByFollow> commReq)throws Exception;
	/**
	 * 任意或关键字消息自动回复
	 * @param msg
	 * @return
	 */
	public String QueryAutoReplayArbitrarily(InterfaceMessage msg) throws Exception;
	/**
	 * 任意或关键字消息自动回复
	 * @Description: 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryAutoReplayArbitrarily> queryAutoReplayArbitrarily(CommonReq<ReqReplayMgr> commReq)throws Exception;
	/**
	 * 短信推送
	 * @param msg
	 * @return
	 */
	public String SendSms(InterfaceMessage msg) throws Exception;
	/**
	 * 短信推送
	 * @Description: 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> sendSms(CommonReq<ReqSendSms> commReq)throws Exception;
	
	/**
	 * 短信推送 阿里云短信验证码
	 * 
	 * 手机号必填，其它选填
	 * 如果有发送短信验证码需要返回短信文案，
	 * 
	 * @Description: 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> sendAliCodeSms(CommonReq<ReqSendSms> commReq)throws Exception;
	
	/**
	 * 图文消息回复
	 */
	String response(AuthInfoVo vo,String wechatConfigKey,String openId,String gId)throws Exception;
	
	
	
	/**
	 * 消息中心首页统计
	 */
	CommonResp<Map<String, Object>> MsgCenterMainCount(CommonReq<ReqMsgCenterMainCount> commReq)throws Exception;
	public String MsgCenterMainCount(InterfaceMessage msg) throws Exception;
	/**
	 * 消息中心插入消息队列
	 */
	CommonResp<RespMap> AddMsgQueue(CommonReq<ReqMsgQueue> commReq)throws Exception;
	public String AddMsgQueue(InterfaceMessage msg) throws Exception;
	/**
	 * 消息中心查询消息队列
	 */
	CommonResp<RespMsgQueueList> MsgQueueList(CommonReq<ReqMsgQueueList> commReq)throws Exception;
	public String MsgQueueList(InterfaceMessage msg) throws Exception;
	/**
	 * 消息中心查询消息队列
	 */
	CommonResp<MsgCenterPageHelper> MsgQueueListPage(CommonReq<ReqMsgQueueList> commReq)throws Exception;
	public String MsgQueueListPage(InterfaceMessage msg) throws Exception;
	/**
	 * 查询微信模板消息列表
	 */
	CommonResp<RespMap> QueryWxTemplateList(CommonReq<ReqQueryAllTemplateList> commReq)throws Exception;
	public String QueryWxTemplateList(InterfaceMessage msg) throws Exception;
	/**
	 * 查询消息场景列表
	 */
	CommonResp<RespMsgScene> MsgSceneList(CommonReq<ReqMsgSceneList> commReq)throws Exception;
	public String MsgSceneList(InterfaceMessage msg) throws Exception;
	/**
	 * 新增消息场景
	 */
	CommonResp<RespMap> AddMsgScene(CommonReq<ReqAddMsgScene> commReq)throws Exception;
	public String AddMsgScene(InterfaceMessage msg) throws Exception;
	/**
	 * 编辑消息场景
	 */
	CommonResp<RespMap> EditMsgScene(CommonReq<ReqEditMsgScene> commReq)throws Exception;
	public String EditMsgScene(InterfaceMessage msg) throws Exception;
	/**
	 * 新增消息模板
	 */
	CommonResp<RespMap> AddMsgTemp(CommonReq<ReqAddMsgTemp> commReq)throws Exception;
	public String AddMsgTemp(InterfaceMessage msg) throws Exception;
	/**
	 * 删除消息模板
	 */
	CommonResp<RespMap> DeleteMsgTemp(CommonReq<ReqDeleteMsgTemp> commReq)throws Exception;
	public String DeleteMsgTemp(InterfaceMessage msg) throws Exception;
	/**
	 * 编辑消息模板
	 */
	CommonResp<RespMap> EditMsgTemp(CommonReq<ReqEditMsgTemp> commReq)throws Exception;
	public String EditMsgTemp(InterfaceMessage msg) throws Exception;
	/**
	 * 查询消息模板
	 */
	CommonResp<RespMsgTemp> MsgTempList(CommonReq<ReqMsgTempList> commReq)throws Exception;
	public String MsgTempList(InterfaceMessage msg) throws Exception;
	/**
	 * 新增消息来源
	 */
	CommonResp<RespMap> AddMsgSource(CommonReq<ReqAddMsgSource> commReq)throws Exception;
	public String AddMsgSource(InterfaceMessage msg) throws Exception;
	/**
	 * 编辑消息来源
	 */
	CommonResp<RespMap> EditMsgSource(CommonReq<ReqEditMsgSource> commReq)throws Exception;
	public String EditMsgSource(InterfaceMessage msg) throws Exception;
	/**
	 * 查询消息来源
	 */
	CommonResp<RespMsgSource> MsgSourceList(CommonReq<ReqMsgSourceList> commReq)throws Exception;
	public String MsgSourceList(InterfaceMessage msg) throws Exception;
	/**
	 * 新增消息用户映射
	 */
	CommonResp<RespMap> AddMsgUserOpenId(CommonReq<ReqAddMsgUserOpenId> commReq)throws Exception;
	public String AddMsgUserOpenId(InterfaceMessage msg) throws Exception;
	/**
	 * 编辑消息用户映射
	 */
	CommonResp<RespMap> EditMsgUserOpenId(CommonReq<ReqEditMsgUserOpenId> commReq)throws Exception;
	public String EditMsgUserOpenId(InterfaceMessage msg) throws Exception;
	/**
	 * 查询消息用户映射
	 */
	CommonResp<MsgCenterPageHelper> MsgUserOpenIdList(CommonReq<ReqMsgUserOpenIdList> commReq)throws Exception;
	public String MsgUserOpenIdList(InterfaceMessage msg) throws Exception;
	/**
	 * 查询用户订阅消息场景列表
	 */
	CommonResp<RespMsgScene> MsgOpenIdSceneList(CommonReq<ReqMsgOpenIdSceneList> commReq)throws Exception;
	public String MsgOpenIdSceneList(InterfaceMessage msg) throws Exception;
	/**
	 * 用户订阅消息场景
	 */
	CommonResp<RespMap> AddMsgOpenIdScene(CommonReq<ReqMsgOpenIdSceneList> commReq)throws Exception;
	public String AddMsgOpenIdScene(InterfaceMessage msg) throws Exception;
	/**
	 * 用户取消订阅消息场景
	 */
	CommonResp<RespMap> DeleteMsgOpenIdScene(CommonReq<ReqMsgOpenIdSceneList> commReq)throws Exception;
	public String DeleteMsgOpenIdScene(InterfaceMessage msg) throws Exception;
	/**
	 * 根据卡号获取openId
	 */
	CommonResp<RespQueryOpenId> QueryOpenId(CommonReq<ReqQueryOpenId> commReq)throws Exception;
	public String QueryOpenId(InterfaceMessage msg) throws Exception;
	/**
	 * 消息已读更新
	 */
	CommonResp<RespMap> ReadMsg(CommonReq<ReqReadMsg> commReq)throws Exception;
	public String ReadMsg(InterfaceMessage msg) throws Exception;
}

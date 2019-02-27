package com.kasite.core.serviceinterface.module.msg;


import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.module.msg.req.ReqMaintenanceMsg;
import com.kasite.core.serviceinterface.module.msg.req.ReqSendMsg;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 消息接口
 *
 * @author 無
 * @version V1.0
 * @date 2018年4月24日 下午3:11:38
 */
public interface MsgCommonService {

	/**
	 * 消息推送 通过该接口推送各类消息
	 * 
	 * @param msg
	 * @return 
	 */
	public String sendMsg(InterfaceMessage msg) throws Exception;
	
	/**
	 * 运维告警推送
	 * 
	 * @param msg
	 * @return
	 */
	public String SendMaintenancenMsg(InterfaceMessage msg) throws Exception;
	/**
	 * 运维告警推送
	 * @Description: 
	 * @param reqSendMsg
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespMap> sendMaintenancenMsg(CommonReq<ReqMaintenanceMsg> reqSendMsg) throws Exception;
	
	
	/**
	 * 发送消息内部调用
	 * @Description: 
	 * @param reqSendMsg
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespMap> sendMsg(CommonReq<ReqSendMsg> reqSendMsg) throws Exception;
	
}

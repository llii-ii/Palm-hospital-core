package com.kasite.core.serviceinterface.module.queue;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.module.queue.req.ReqGetQueueInfo;
import com.kasite.core.serviceinterface.module.queue.req.ReqQueryLocalQueue;
import com.kasite.core.serviceinterface.module.queue.req.ReqSetReMindNo;
import com.kasite.core.serviceinterface.module.queue.resp.RespGetQueueInfo;
import com.kasite.core.serviceinterface.module.queue.resp.RespQueryLocalQueue;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO
 */
public interface IQueueService {

	/**
	 * 获取排队信息
	 * 
	 * @param msg
	 * @return
	 */
	public String GetQueueInfo(InterfaceMessage msg) throws Exception;
	/**
	 * 获取排队信息
	 * @Description: 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespGetQueueInfo> getQueueInfo(CommonReq<ReqGetQueueInfo> commReq) throws Exception;

	/**
	 * 设置提醒号序
	 * 
	 * @param msg
	 * @return
	 */
	public String SetReMindNo(InterfaceMessage msg) throws Exception;
	/**
	 * 设置提醒号序
	 * @Description: 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> setReMindNo(CommonReq<ReqSetReMindNo> commReq) throws Exception;

	
	CommonResp<RespQueryLocalQueue> queryLocalQueue(CommonReq<ReqQueryLocalQueue> commReq) throws Exception;
}

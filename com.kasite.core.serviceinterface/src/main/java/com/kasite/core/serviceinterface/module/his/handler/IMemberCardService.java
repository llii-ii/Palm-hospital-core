package com.kasite.core.serviceinterface.module.his.handler;

import java.util.Map;

import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.service.ICallHis;
import com.yihu.wsgw.api.InterfaceMessage;

public interface IMemberCardService extends ICallHis{
	
	/**
	 * 绑定成员卡信息
	 * @param msg
	 * @param map
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> hisBindMemberCard(InterfaceMessage msg,Map<String, String> map) throws Exception;
	
	/**
	 * 解绑成员卡信息
	 * @param msg
	 * @param map
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> hisUnbindMemberCard(InterfaceMessage msg,Map<String, String> map) throws Exception;
	
	
}

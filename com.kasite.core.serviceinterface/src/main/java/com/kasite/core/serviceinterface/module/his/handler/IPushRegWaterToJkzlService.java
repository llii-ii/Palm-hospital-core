package com.kasite.core.serviceinterface.module.his.handler;

import java.util.Map;

import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.service.ICallHis;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: IPushRegWaterToJkzlService
 * @author: lcz
 */
public interface IPushRegWaterToJkzlService extends ICallHis{
	
	
	CommonResp<RespMap> pushRegWaterToJkzl(InterfaceMessage msg,Map<String, Object> map) throws Exception;
}

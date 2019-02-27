package com.kasite.core.serviceinterface.module.his.handler;

import java.util.Map;

import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.service.ICallHis;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryEntityCardInfoResp;
import com.yihu.wsgw.api.InterfaceMessage;


/**
 * @author linjf
 * 直接卡面付（无需全流程生成二维码链接地址）
 */
public interface IDirectCardPayService extends ICallHis{

	/**
	 * 查询实体卡信息
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	CommonResp<HisQueryEntityCardInfoResp> queryEntityCardInfo(InterfaceMessage msg,Map<String, String> paramMap) throws Exception ;
	
}

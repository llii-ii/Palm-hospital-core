package com.kasite.core.serviceinterface.module.his.handler;

import java.util.Map;

import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.service.ICallHis;
import com.kasite.core.serviceinterface.module.his.resp.HisAddMember;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 新增用户的时候都会调用这个接口
 * @author daiyanshui
 */
public interface IAddMemberService extends ICallHis{

	CommonResp<HisAddMember> addMemberService(InterfaceMessage msg,Map<String, String> paramMap)throws Exception;
	
}

package com.kasite.core.serviceinterface.module.his.handler;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.service.ICallHis;
import com.kasite.core.serviceinterface.module.his.req.ReqHisMemberAutoUnbind;
import com.kasite.core.serviceinterface.module.his.resp.HisMemberAutoUnbind;

/**
 * 自动解绑--就诊卡 会无效（回收）卡号 发给另外一个用户
 * @className: IMemberAutoUnbindService
 * @author: lcz
 * @date: 2018年9月14日 下午2:43:58
 */
public interface IMemberAutoUnbindService extends ICallHis{
	
	
	CommonResp<HisMemberAutoUnbind> memberAutoUnbind(CommonReq<ReqHisMemberAutoUnbind> req) throws Exception;
}

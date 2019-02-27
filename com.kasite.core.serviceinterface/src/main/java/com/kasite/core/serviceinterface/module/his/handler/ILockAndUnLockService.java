package com.kasite.core.serviceinterface.module.his.handler;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.service.ICallHis;
import com.kasite.core.serviceinterface.module.his.req.ReqHisLock;
import com.kasite.core.serviceinterface.module.his.req.ReqHisUnLock;
import com.kasite.core.serviceinterface.module.his.resp.HisLockOrder;
import com.kasite.core.serviceinterface.module.his.resp.HisUnlock;

/**
 * 锁号和解锁功能
 * @author daiyanshui
 */
public interface ILockAndUnLockService extends ICallHis{
	/**
	 * 实现HIS接口的锁号逻辑
	 * 必须返回HIS内的锁号ID
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	CommonResp<HisLockOrder> lockOrder(CommonReq<ReqHisLock> req) throws Exception;
	
	/**
	 * 实现HIS接口的解锁逻辑
	 * 必须返回解锁成功或失败
	 * @param req
	 * @return
	 */
	CommonResp<HisUnlock> unLockOrder(CommonReq<ReqHisUnLock> req) throws Exception;
	
}

package com.kasite.core.serviceinterface.module.pay;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.module.pay.req.ReqAddMerchantNotifyFailure;
import com.kasite.core.serviceinterface.module.pay.req.ReqGetMerchantNotifyById;
import com.kasite.core.serviceinterface.module.pay.req.ReqMerchantNotifyForceRetry;
import com.kasite.core.serviceinterface.module.pay.req.ReqQueryFrontPayLimit;
import com.kasite.core.serviceinterface.module.pay.req.ReqUpdateMerchantNotifyById;
import com.kasite.core.serviceinterface.module.pay.resp.RespGetMerchantNotifyById;
import com.kasite.core.serviceinterface.module.pay.resp.RespQueryFrontPayLimit;
import com.yihu.hos.service.ICommonService;
import com.yihu.wsgw.api.InterfaceMessage;
/**
 * @author linjf
 * TODO
 */
public interface IPayMerchantService extends ICommonService{

	/**
	 * 根据主键获取商户通知记录
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespGetMerchantNotifyById> getMerchantNotifyById(CommonReq<ReqGetMerchantNotifyById> commReq) throws Exception;
	
	/**
	 * 根据主键更新商户通知记录
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> updateMerchantNotifyById(CommonReq<ReqUpdateMerchantNotifyById> commReq) throws Exception;
	
	/**
	 * 新增商户通知失败的记录
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> addMerchantNotifyFailure(CommonReq<ReqAddMerchantNotifyFailure> commReq) throws Exception;
	
	/**
	 * 将失败的商户通知记录强制重试
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> merchantNotifyForceRetry(CommonReq<ReqMerchantNotifyForceRetry> commReq) throws Exception;
	
	/**
	 * 查询前端支付限制
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	
	String QueryFrontPayLimit(InterfaceMessage msg) throws Exception;
	
	CommonResp<RespQueryFrontPayLimit> queryFrontPayLimit(CommonReq<ReqQueryFrontPayLimit> commReq) throws Exception;
	
}

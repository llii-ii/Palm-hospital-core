package com.kasite.core.serviceinterface.module.pay;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.module.his.resp.RespQueryHisBill;
import com.kasite.core.serviceinterface.module.pay.req.ReqGetBill;
import com.kasite.core.serviceinterface.module.pay.resp.RespGetBill;
import com.yihu.hos.service.ICommonService;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO
 */
public interface IBillService extends ICommonService{

	/**
	 * 查询His的原始账单
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	public String QueryHisOrderBill(InterfaceMessage msg) throws Exception;
	
	/**
	 * 查询商户账单
	 * @param msg
	 * @return
	 */
	public String GetBill(InterfaceMessage msg) throws Exception;
	
	/**
	 * 查询商户原始账单
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespGetBill> getBill(CommonReq<ReqGetBill> commReq) throws Exception;
	
	/**
	 * 查询His的原始账单
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryHisBill> queryHisOrderBill(CommonReq<ReqGetBill> commReq) throws Exception;
	
	/**
	 * 查询所有的原始账单(His&Merch)
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> queryAllBill(CommonReq<ReqGetBill> commReq) throws Exception;
}

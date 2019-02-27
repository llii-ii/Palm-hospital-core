package com.kasite.client.pay.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kasite.client.pay.bean.dbo.PayConfig;
import com.kasite.client.pay.dao.IMerchantNotifyFailureMapper;
import com.kasite.client.pay.dao.IMerchantNotifyMapper;
import com.kasite.client.pay.dao.IPayConfigMapper;
import com.kasite.client.pay.util.PayBackCallUtil;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.BeanCopyUtils;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.serviceinterface.module.pay.IPayMerchantService;
import com.kasite.core.serviceinterface.module.pay.dbo.MerchantNotify;
import com.kasite.core.serviceinterface.module.pay.dbo.MerchantNotifyFailure;
import com.kasite.core.serviceinterface.module.pay.req.ReqAddMerchantNotifyFailure;
import com.kasite.core.serviceinterface.module.pay.req.ReqGetMerchantNotifyById;
import com.kasite.core.serviceinterface.module.pay.req.ReqMerchantNotifyForceRetry;
import com.kasite.core.serviceinterface.module.pay.req.ReqQueryFrontPayLimit;
import com.kasite.core.serviceinterface.module.pay.req.ReqUpdateMerchantNotifyById;
import com.kasite.core.serviceinterface.module.pay.resp.RespGetMerchantNotifyById;
import com.kasite.core.serviceinterface.module.pay.resp.RespQueryFrontPayLimit;
import com.yihu.hos.service.CommonService;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO
 */
@Service("pay.PayMerchant")
public class PayMerchantServiceImpl  extends CommonService implements IPayMerchantService {
	
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY);
	
	@Autowired
	IMerchantNotifyMapper merchantNotifyMapper;

	@Autowired
	IMerchantNotifyFailureMapper merchantNotifyFailureMapper; 
	
	@Autowired
	IPayConfigMapper payConfigMapper;
	
	@Autowired
	private PayBackCallUtil payBackCallUtil; 
	
	/**
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	@Override
	public CommonResp<RespGetMerchantNotifyById> getMerchantNotifyById(CommonReq<ReqGetMerchantNotifyById> commReq) throws Exception {
		ReqGetMerchantNotifyById reqGetMerchantNotifyById = commReq.getParam();
		MerchantNotify merchantNotify  = merchantNotifyMapper.selectByPrimaryKey(reqGetMerchantNotifyById.getId());
		if(merchantNotify!=null ) {
			RespGetMerchantNotifyById resp = BeanCopyUtils.copyProperties(merchantNotify, new RespGetMerchantNotifyById(), null);
			return new CommonResp<RespGetMerchantNotifyById>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,resp);
		}else {
			return new CommonResp<RespGetMerchantNotifyById>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Pay.ERROR_MERCHANTORDER,"主键不存在！");
		}
		
	}

	/**
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	@Override
	public CommonResp<RespMap> updateMerchantNotifyById(CommonReq<ReqUpdateMerchantNotifyById> commReq)
			throws Exception {
		ReqUpdateMerchantNotifyById req = commReq.getParam();
		MerchantNotify merchantNotify = new MerchantNotify();
		merchantNotify.setId(req.getId());
		merchantNotify.setUpdateTime(DateOper.getNowDateTime());
		merchantNotify.setState(req.getState());
		merchantNotify.setRetryNum(req.getRetryNum());
		int ret = merchantNotifyMapper.updateByPrimaryKeySelective(merchantNotify);
		if( ret>0 ) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		}else {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.CALL_SQL_ERROR);
		}
	}

	/**
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	@Override
	public CommonResp<RespMap> addMerchantNotifyFailure(CommonReq<ReqAddMerchantNotifyFailure> commReq)
			throws Exception {
		ReqAddMerchantNotifyFailure req = commReq.getParam();
		MerchantNotifyFailure merchantNotifyFailure = new MerchantNotifyFailure();
		merchantNotifyFailure.setMerchantNotifyId(req.getMerchantNotifyId());
		merchantNotifyFailure.setCreateTime(DateOper.getNowDateTime());
		merchantNotifyFailureMapper.insertSelective(merchantNotifyFailure);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	/**
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	@Override
	public CommonResp<RespMap> merchantNotifyForceRetry(CommonReq<ReqMerchantNotifyForceRetry> commReq)
			throws Exception {
		ReqMerchantNotifyForceRetry reqMerchantNotifyForceRetry = commReq.getParam();
		payBackCallUtil.payNotifyForceRetry(reqMerchantNotifyForceRetry.getMsg(), reqMerchantNotifyForceRetry.getOrderId());;
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	/**
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String QueryFrontPayLimit(InterfaceMessage msg) throws Exception {
//		PayConfig queryPayConfig = new PayConfig();
//		PayConfig conf = payConfigMapper.selectOne(queryPayConfig);
//		if( conf != null ) {
//			return CommonUtil.getRetVal(msg,KstHosConstant.DEFAULTTRAN,
//					"OpMinPayMoney,OpMaxPayMoney,IhMinPayMoney,IhMaxPayMoney,IsEnablePay", conf);
//		}else {
//			return CommonUtil.getRetVal(msg,KstHosConstant.DEFAULTTRAN,RetCode.Common.ERROR_SQLEXECERROR);
//		}
		return this.queryFrontPayLimit(new CommonReq<ReqQueryFrontPayLimit>(new ReqQueryFrontPayLimit(msg))).toResult();
	}
	
	@Override
	public CommonResp<RespQueryFrontPayLimit> queryFrontPayLimit(CommonReq<ReqQueryFrontPayLimit> commReq) throws Exception {
		PayConfig queryPayConfig = new PayConfig();
		PayConfig conf = payConfigMapper.selectOne(queryPayConfig);
		RespQueryFrontPayLimit resp = new RespQueryFrontPayLimit();
		BeanCopyUtils.copyProperties(conf, resp, null);
		return new CommonResp<RespQueryFrontPayLimit>(commReq, RetCode.Success.RET_10000, resp);
	}

}

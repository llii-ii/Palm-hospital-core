package com.kasite.client.dragonpay.service;

import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.serviceinterface.module.pay.IPaymentGateWayService;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqClose;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqQueryOrder;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqQueryRefundOrder;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqRefund;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqRevoke;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqSweepCodePay;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqUniteOrder;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqWapUniteOrder;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespClose;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespDownloadBill;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespQueryOrder;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespQueryRefundOrder;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespRefund;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespRevoke;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespSweepCodePay;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespUniteOrder;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespWapUniteOrder;

/**
 * @author linjf
 * 建行龙支付接口
 */
public class DragonPayGateWayServiceImple implements IPaymentGateWayService {
	
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY);
	
	/**
	 * @return
	 */
	@Override
	public String mchType() {
		return ChannelTypeEnum.dragonpay.toString();
	}

	/**
	 * @param authInfoVo
	 * @param pgwReqUniteOrder
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespUniteOrder uniteOrder(AuthInfoVo authInfoVo, PgwReqUniteOrder pgwReqUniteOrder) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param authInfoVo
	 * @param pgwReqRefund
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespRefund refund(AuthInfoVo authInfoVo, PgwReqRefund pgwReqRefund) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param authInfoVo
	 * @param pgwReqSweepCodePay
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespSweepCodePay sweepCodePay(AuthInfoVo authInfoVo, PgwReqSweepCodePay pgwReqSweepCodePay)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param authInfoVo
	 * @param pgwReqRevoke
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespRevoke revoke(AuthInfoVo authInfoVo, PgwReqRevoke pgwReqRevoke) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param authInfoVo
	 * @param pgwReqClose
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespClose close(AuthInfoVo authInfoVo, PgwReqClose pgwReqClose) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param authInfoVo
	 * @param pgwReqQueryOrder
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespQueryOrder queryOrder(AuthInfoVo authInfoVo, PgwReqQueryOrder pgwReqQueryOrder) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param authInfoVo
	 * @param pgwReqQueryRefundOrder
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespQueryRefundOrder queryRefundOrder(AuthInfoVo authInfoVo,
			PgwReqQueryRefundOrder pgwReqQueryRefundOrder) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param authInfoVo
	 * @param pgwReqUniteOrder
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespWapUniteOrder wapUniteOrder(AuthInfoVo authInfoVo, PgwReqWapUniteOrder pgwReqUniteOrder)
			throws Exception {
//		Map<String,String> paramMap = new LinkedHashMap<String,String>();
//		paramMap.put("MERCHANTID", KasiteConfig.getDragonPay(DragonPayEnum.merchantId, authInfoVo.getConfigKey()));
//		paramMap.put("POSID", KasiteConfig.getDragonPay(DragonPayEnum.posId, authInfoVo.getConfigKey()));
//		paramMap.put("BRANCHID", KasiteConfig.getDragonPay(DragonPayEnum.branchId, authInfoVo.getConfigKey()));
//		paramMap.put("ORDERID", "19991101234");//定单号,最长30位
//		paramMap.put("PAYMENT", "0.01");//付款金额,由商户提供，按实际金额给出
//		paramMap.put("CURCODE", "01");//币种缺省为01－人民币
//		paramMap.put("TXCODE", "520100");//	交易码	CHAR(6)	Y	由建行统一分配为520100
//		paramMap.put("REMARK1", "");//备注1
//		paramMap.put("REMARK2", "");//备注2
//		//签名域
//		Map<String,String> signParamMap = new LinkedHashMap<String,String>();
//		signParamMap.put("TYPE", "1");//接口类型	CHAR(1)	Y	0- 非钓鱼接口
//		signParamMap.put("PUB", "30819d300d06092a864886f70d0108");//公钥后30位	CHAR(30)	Y	仅作为源串参加MD5摘要，不作为参数传递
//		signParamMap.put("GATEWAY", "");//网关类型	VARCHAR(100)	Y	详见下方的GATEWAY设置说明
//		signParamMap.put("CLIENTIP", "");//	客户端IP	CHAR(40)	N	客户在商户系统中的IP
//		signParamMap.put("REGINFO", "");//客户注册信息 CHAR(256)	N	客户在商户系统中注册的信息，中文需使用escape编码
//		
				

		
		return null;
	}

	/**
	 * @param authInfoVo
	 * @param billDate
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespDownloadBill downloadBill(AuthInfoVo authInfoVo, Date billDate) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}

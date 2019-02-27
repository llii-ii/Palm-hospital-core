package com.kasite.client.unionpay.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.kasite.client.unionpay.constants.UnionPayConstant;
import com.kasite.client.unionpay.sdk.AcpService;
import com.kasite.client.unionpay.sdk.SDKUtil;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.UnionPayEnum;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.serviceinterface.module.pay.IPaymentGateWayService;
import com.kasite.core.serviceinterface.module.pay.bo.MchBill;
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

@Service("unionPay")
public class UnionPayGateWayServiceImpl implements IPaymentGateWayService {

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY);

	/**
	 * @param authInfoVo
	 * @param pgwReqUniteOrder
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespUniteOrder uniteOrder(AuthInfoVo authInfoVo, PgwReqUniteOrder pgwReqUniteOrder) throws Exception {
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
		Map<String, String> data = new HashMap<String, String>();
		PgwRespRefund pgwRespRefund = new PgwRespRefund();
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		data.put("version", UnionPayConstant.VERSION);               //版本号
		data.put("encoding", UnionPayConstant.ENCODING);             //字符集编码 可以使用UTF-8,GBK两种方式
		data.put("signMethod", UnionPayConstant.SIGNMETHOD); //签名方法
		data.put("txnType", UnionPayConstant.TXNTYPE_04);                           //交易类型 04-退货		
		data.put("txnSubType", UnionPayConstant.TXNSUBTYPE_00);                        //交易子类型  默认00		
		data.put("bizType", UnionPayConstant.BIZTYPE_000000);          		 	//填写000000
		data.put("channelType",UnionPayConstant.CHANNELTYPE_08);                       //渠道类型，07-PC，08-手机		
		
		/***商户接入参数***/
		data.put("merId", KasiteConfig.getUnionPay(UnionPayEnum.merId, pgwReqRefund.getPayConfigKey()));                //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		data.put("accessType",KasiteConfig.getUnionPay(UnionPayEnum.accessType, pgwReqRefund.getPayConfigKey()));                         //接入类型，商户接入固定填0，不需修改		
		data.put("orderId", pgwReqRefund.getRefundOrderId());          //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费		
		data.put("txnTime", DateOper.getNow("yyyyMMddHHmmss"));      //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效		
		data.put("currencyCode", UnionPayConstant.CURRENCYCODE_156);                     //交易币种（境内商户一般是156 人民币）		
		data.put("txnAmt", pgwReqRefund.getRefundPrice().toString());                          //****退货金额，单位分，不要带小数点。退货金额小于等于原消费金额，当小于的时候可以多次退货至退货累计金额等于原消费金额		
		data.put("backUrl", KasiteConfig.getRefundCallBackUrl(ChannelTypeEnum.unionpay, pgwReqRefund.getPayConfigKey(), authInfoVo.getClientId(),
				authInfoVo.getSign(), authInfoVo.getSessionKey(),pgwReqRefund.getOrderId()));               //后台通知地址，后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 退货交易 商户通知,其他说明同消费交易的后台通知
		
		/***要调通交易以下字段必须修改***/
		if( !StringUtil.isEmpty(pgwReqRefund.getTransactionNo()) ) {
			data.put("origQryId", pgwReqRefund.getTransactionNo()); //如果使用银联的订单号，可直接退费
		}else {
			data.put("origOrderId", pgwReqRefund.getOrderId()); //如果使用全流程订单，则必须提供订单的支付时间
			data.put("origTxnTime", pgwReqRefund.getPayTime());      	
		}
		// 请求方保留域，
        // 透传字段，查询、通知、对账文件中均会原样出现，如有需要请启用并修改自己希望透传的数据。
        // 出现部分特殊字符时可能影响解析，请按下面建议的方式填写：
        // 1. 如果能确定内容不会出现&={}[]"'等符号时，可以直接填写数据，建议的方法如下。
//		data.put("reqReserved", "透传信息1|透传信息2|透传信息3");
        // 2. 内容可能出现&={}[]"'符号时：
        // 1) 如果需要对账文件里能显示，可将字符替换成全角＆＝｛｝【】“‘字符（自己写代码，此处不演示）；
        // 2) 如果对账文件没有显示要求，可做一下base64（如下）。
        //    注意控制数据长度，实际传输的数据长度不能超过1024位。
        //    查询、通知等接口解析时使用new String(Base64.decodeBase64(reqReserved), DemoBase.encoding);解base64后再对数据做后续解析。
//		data.put("reqReserved", Base64.encodeBase64String("任意格式的信息都可以".toString().getBytes(DemoBase.encoding)));
		
		/**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->**/
		Map<String, String> reqData  = AcpService.sign(data,UnionPayConstant.ENCODING,pgwReqRefund.getPayConfigKey());		//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		String url = UnionPayConstant.BACKTRANSURL;								//交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
		Map<String, String> rspData = AcpService.post(pgwReqRefund.getOrderId(),ApiModule.UnionPay.backTransReq_refund,url,authInfoVo.getConfigKey(),
				reqData,UnionPayConstant.ENCODING);//这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
		// 返回的参数
		/**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
		//应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
		if(!rspData.isEmpty()){
			if(AcpService.validate(rspData, UnionPayConstant.ENCODING,pgwReqRefund.getPayConfigKey())){
				LogUtil.info(log, new LogBody(authInfoVo).set("验证签名结果","成功！"));
				String respCode = rspData.get("respCode") ;
				String respMsg = rspData.get("respMsg") ;
				if(("00").equals(respCode)){
					//交易已受理(不代表交易已成功），等待接收后台通知更新订单状态,也可以主动发起 查询交易确定交易状态。
					LogUtil.info(authInfoVo,log, "YL-INFO[" + "申请银联退款成功|refundOrderId:" + pgwReqRefund.getRefundOrderId() + "]");
					pgwRespRefund.setRespCode(RetCode.Success.RET_10000);
					pgwRespRefund.setRespMsg( "申请退款成功！");
					pgwRespRefund.setRefundId(rspData.get("queryId"));
					// 交易金额 txnAmt N1..12 无 R-需要返回
					//retJson.put("RefundFee", rspData.get("txnAmt"));
					// 订单发送时间 txnTime YYYYMMDDhhmmss 无 R-需要返回
					//retJson.put("RefundTxnTime", rspData.get("txnTime"));
				}else if(("03").equals(respCode)||
						 ("04").equals(respCode)||
						 ("05").equals(respCode)){
					//后续需发起交易状态查询交易确定交易状态
					pgwRespRefund.setRespCode(RetCode.Common.ERROR_SYSTEM);
					pgwRespRefund.setRespMsg(respMsg);
				}else{
					//其他应答码为失败请排查原因
					pgwRespRefund.setRespCode(RetCode.Common.ERROR_SYSTEM);
					pgwRespRefund.setRespMsg(respMsg);
				}
			}else{
				LogUtil.info(log, new LogBody(authInfoVo).set("验证签名结果","失败！"));
				pgwRespRefund.setRespCode(RetCode.Common.ERROR_SYSTEM);
				pgwRespRefund.setRespMsg("验证签名结果:失败！");
			}
		}else{
			//未返回正确的http状态
			LogUtil.info(log, new LogBody(authInfoVo).set("银联扫码付请求结果失败","未获取到返回报文或返回http状态码非200"));
			pgwRespRefund.setRespCode(RetCode.Common.ERROR_SYSTEM);
			pgwRespRefund.setRespMsg( "银联扫码付请求结果失败：未获取到返回报文或返回http状态码非200");
		}
		return pgwRespRefund;	
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
		PgwRespSweepCodePay pgwRespSweepCodePay = new PgwRespSweepCodePay();
		String configKey = authInfoVo.getConfigKey();
		// 终端号 termId O ANS8
		// 原则是可以通过交易上送的终端编号准确定位商户每一个门店内每一台收银设备，建议按“门店编号+收银机编号”或“设备编号”组成8位终端编号在交易中上送。商户需将终端编号与门店对应关系反馈给银联。
		// 不能输入英文例如SMQ01就会返回报文错误
		// String termId = "00000011";

		Map<String, String> contentData = new HashMap<String, String>();

		/*** 银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改 ***/
		// 版本号 全渠道默认值
		contentData.put("version", UnionPayConstant.VERSION);
		// 字符集编码 可以使用UTF-8,GBK两种方式
		contentData.put("encoding", UnionPayConstant.ENCODING);
		// 签名方法
		contentData.put("signMethod", UnionPayConstant.SIGNMETHOD);
		// 交易类型 01:消费
		contentData.put("txnType",UnionPayConstant.TXNTYPE_01);
		// 交易子类 06：二维码消费
		contentData.put("txnSubType",UnionPayConstant.TXNSUBTYPE_06);
		// 产品类型,填写000000
		contentData.put("bizType", UnionPayConstant.BIZTYPE_000000);
		// 渠道类型 08手机
		contentData.put("channelType", UnionPayConstant.CHANNELTYPE_08);

		/*** 商户接入参数 ***/
		// 商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		// 商户号
		contentData.put("merId", KasiteConfig.getUnionPay(UnionPayEnum.merId, configKey));
		// 接入类型，商户接入填0 ，不需修改（0：直连商户， 1： 收单机构 2：平台商户）TODO
		contentData.put("accessType", KasiteConfig.getUnionPay(UnionPayEnum.accessType, configKey));
		// 商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
		contentData.put("orderId", pgwReqSweepCodePay.getOrderId());
		// 订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		contentData.put("txnTime", DateOper.getNow("yyyyMMddHHmmss"));
		// 交易金额 单位为分，不能带小数点
		contentData.put("txnAmt", pgwReqSweepCodePay.getTotalPrice().toString());
		// 境内商户固定 156 人民币
		contentData.put("currencyCode", UnionPayConstant.CURRENCYCODE_156);
		// C2B码,1-20位数字
		contentData.put("qrNo", pgwReqSweepCodePay.getAuthCode());
		//contentData.put("termId", reqQrCodeConsume.getTermId()); // 终端号

		// 请求方保留域，
		// 透传字段，查询、通知、对账文件中均会原样出现，如有需要请启用并修改自己希望透传的数据。
		// 出现部分特殊字符时可能影响解析，请按下面建议的方式填写：
		// 1. 如果能确定内容不会出现&={}[]"'等符号时，可以直接填写数据，建议的方法如下。
		// contentData.put("reqReserved", "透传信息1|透传信息2|透传信息3");
		// 2. 内容可能出现&={}[]"'符号时：
		// 1) 如果需要对账文件里能显示，可将字符替换成全角＆＝｛｝【】“‘字符（自己写代码，此处不演示）；
		// 2) 如果对账文件没有显示要求，可做一下base64（如下）。
		// 注意控制数据长度，实际传输的数据长度不能超过1024位。
		// 查询、通知等接口解析时使用new String(Base64.decodeBase64(reqReserved),
		// DemoBase.encoding);解base64后再对数据做后续解析。
		// contentData.put("reqReserved",
		// Base64.encodeBase64String("任意格式的信息都可以".toString().getBytes(DemoBase.encoding)));

		// 后台通知地址（需设置为外网能访问 http
		// https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，【支付失败的交易银联不会发送后台通知】
		// 后台通知参数详见open.unionpay.com帮助中心 下载 产品接口规范 网关支付产品接口规范 消费交易 商户通知
		// 注意:1.需设置为外网能访问，否则收不到通知 2.http https均可 3.收单后台通知后需要10秒内返回http200或302状态码
		// 4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200或302，那么银联会间隔一段时间再次发送。总共发送5次，银联后续间隔1、2、4、5
		// 分钟后会再次通知。
		// 5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d
		// 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
		contentData.put("backUrl",KasiteConfig.getPayCallBackUrl(ChannelTypeEnum.unionpay, configKey, authInfoVo.getClientId(), 
				authInfoVo.getSign(), authInfoVo.getSessionKey(),pgwReqSweepCodePay.getOrderId()));

		/** 对请求参数进行签名并发送http post请求，接收同步应答报文 **/
		Map<String, String> reqData = AcpService.sign(contentData, UnionPayConstant.ENCODING,configKey); // 报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。

		// 交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
		// String requestAppUrl =
		// "https://gateway.test.95516.com/gateway/api/backTransReq.do";
		String requestAppUrl = UnionPayConstant.BACKTRANSURL;
		Map<String, String> rspData = AcpService.post(pgwReqSweepCodePay.getOrderId(),ApiModule.UnionPay.backTransReq_qrCodeConsumePassive,requestAppUrl,configKey,reqData,UnionPayConstant.ENCODING);   
		//发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

		/** 对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考-------------> **/
		// 应答码规范参考open.unionpay.com帮助中心 下载 产品接口规范 《平台接入接口规范-第5部分-附录》
		if(!rspData.isEmpty()){
			if(AcpService.validate(rspData, UnionPayConstant.ENCODING,configKey)){
				LogUtil.info(log, new LogBody(authInfoVo).set("验证签名结果","成功！"));
				String respCode = rspData.get("respCode") ;
				if(("00").equals(respCode)){
					//成功,获取tn号
					//String tn = resmap.get("tn");
					pgwRespSweepCodePay.setRespCode(RetCode.Success.RET_10000);
					pgwRespSweepCodePay.setRespMsg(RetCode.Success.RET_10000.getMessage());
					// 银联查询流水号 queryId M AN20..21 消费交易的流水号，供后续查询用
					pgwRespSweepCodePay.setTransactionNo(rspData.get("queryId"));
					// 订单发送时间
					pgwRespSweepCodePay.setPayTime(rspData.get("txnTime"));
					// 交易金额
					//retJson.put("TotalFee", rspData.get("txnAmt"));
					/** 需要其他返回字段 待加 */
				}else{
					// 其他应答码表示失败
					// 银联的返回码
					//TODO 需要用户输入验证码、或者二维码不正确、二维码多次扫是什么情况、需要完善测试！
					pgwRespSweepCodePay.setRespCode(unionPayRespCodeCodeParseRetCode(respCode));
					pgwRespSweepCodePay.setRespMsg("调用银联二维码消费（被扫）接口失败,银联应答码respCode:"
					+ respCode + ",银联应答信息respMsg:" + rspData.get("respMsg"));
				}
			}else{
				LogUtil.info(log, new LogBody(authInfoVo).set("验证签名结果","失败！"));
				pgwRespSweepCodePay.setRespCode(RetCode.Common.ERROR_SYSTEM);
				pgwRespSweepCodePay.setRespMsg("验证签名结果:失败！");
			}
		}else{
			//未返回正确的http状态
			LogUtil.info(log, new LogBody(authInfoVo).set("银联扫码付请求结果失败","未获取到返回报文或返回http状态码非200"));
			pgwRespSweepCodePay.setRespCode(RetCode.Common.ERROR_SYSTEM);
			pgwRespSweepCodePay.setRespMsg("银联扫码付请求结果失败:未获取到返回报文或返回http状态码非200");
		}
		return pgwRespSweepCodePay;
	}

	/***
	 * 银联的冲正，相当于微信支付宝的撤销接口，专门用于撤销异常订单的。
	 * 
	 * 银联的冲正API:冲正必须与原始消费在同一天（准确讲是昨日23:00至本日23:00之间）。
	 * 
	 * 冲正交易，仅用于超时无应答等异常场景，只有发生支付系统超时或者支付结果未知时可调用冲正，
	 * 
	 * 其他正常支付的订单如果需要实现相通功能，请调用消费撤销或者退货。
	 * 
	 * 经测试支付成功的订单调用银联的冲正API也会进行退款。
	 * outTradeNo全流程订单号
	 * 
	 * txnTime1订单发起时间
	 *
	 * @param authInfoVo
	 * @param pgwReqRevoke
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespRevoke revoke(AuthInfoVo authInfoVo, PgwReqRevoke pgwReqRevoke) throws Exception {
		String configKey = authInfoVo.getConfigKey();
		Map<String, String> data = new HashMap<String, String>();
		PgwRespRevoke pgwRespRevoke = new PgwRespRevoke();
		/*** 银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改 ***/
		// 版本号
		data.put("version", UnionPayConstant.VERSION);
		// 字符集编码 可以使用UTF-8,GBK两种方式
		data.put("encoding", UnionPayConstant.ENCODING);
		// 签名方法
		data.put("signMethod", KasiteConfig.getUnionPay(UnionPayEnum.accessType, configKey));
		// 交易类型 99-冲正
		data.put("txnType", UnionPayConstant.TXNTYPE_99);
		// 交易子类型
		data.put("txnSubType", UnionPayConstant.TXNSUBTYPE_01);
		// 填写000000
		data.put("bizType", UnionPayConstant.BIZTYPE_000000);
		// 渠道类型，07-PC，08-手机
		data.put("channelType", UnionPayConstant.CHANNELTYPE_08);

		/*** 商户接入参数 ***/
		// 商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		data.put("merId", KasiteConfig.getUnionPay(UnionPayEnum.merId, configKey));
		// 接入类型，商户接入固定填0，不需修改
		data.put("accessType", KasiteConfig.getUnionPay(UnionPayEnum.accessType, configKey));
		// 商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
		data.put("orderId", pgwReqRevoke.getOrderId());
		// 订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		data.put("txnTime", pgwReqRevoke.getPayTime());

		// 请求方保留域，
		// 透传字段，查询、通知、对账文件中均会原样出现，如有需要请启用并修改自己希望透传的数据。
		// 出现部分特殊字符时可能影响解析，请按下面建议的方式填写：
		// 1. 如果能确定内容不会出现&={}[]"'等符号时，可以直接填写数据，建议的方法如下。
		// data.put("reqReserved", "透传信息1|透传信息2|透传信息3");
		// 2. 内容可能出现&={}[]"'符号时：
		// 1) 如果需要对账文件里能显示，可将字符替换成全角＆＝｛｝【】“‘字符（自己写代码，此处不演示）；
		// 2) 如果对账文件没有显示要求，可做一下base64（如下）。
		// 注意控制数据长度，实际传输的数据长度不能超过1024位。
		// 查询、通知等接口解析时使用new String(Base64.decodeBase64(reqReserved),
		// DemoBase.encoding);解base64后再对数据做后续解析。
		// data.put("reqReserved",
		// Base64.encodeBase64String("任意格式的信息都可以".toString().getBytes(DemoBase.encoding)));

		/** 请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文-------------> **/
		// 报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		Map<String, String> reqData = AcpService.sign(data, UnionPayConstant.ENCODING,configKey);
		// 交易请求url从配置文件读取对应属性文件acp_sdk.properties中的acpsdk.backTransUrl
		String url = UnionPayConstant.BACKTRANSURL;
		// 这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
		Map<String, String> rspData =AcpService.post(pgwReqRevoke.getOrderId(),ApiModule.UnionPay.backTransReq_correction,url,configKey,reqData, UnionPayConstant.ENCODING);

		/** 对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考-------------> **/

		// 应答码规范参考open.unionpay.com帮助中心 下载 产品接口规范 《平台接入接口规范-第5部分-附录》
		// 返回的参数
		if (!rspData.isEmpty()) {

			if (AcpService.validate(rspData, UnionPayConstant.ENCODING,configKey)) {
				LogUtil.info(log, new LogBody(authInfoVo).set("验证签名结果","成功！"));
				String respCode = rspData.get("respCode");

				if (("00").equals(respCode)) {
					pgwRespRevoke.setRespCode(RetCode.Success.RET_10000);
					pgwRespRevoke.setRespMsg(RetCode.Success.RET_10000.getMessage());
				} else {
					// 撤单失败
					pgwRespRevoke.setRespCode(RetCode.Common.ERROR_SYSTEM);
					pgwRespRevoke.setRespMsg( "调用银联冲正接口失败,银联返回码respCode:" + rspData.get("respCode")
					+ ",银联返回信息respMsg:" + rspData.get("respMsg"));
				}
			} else {
				// 验证签名失败
				pgwRespRevoke.setRespCode(RetCode.Common.ERROR_SYSTEM);
				pgwRespRevoke.setRespMsg("调用银联冲正接口,验证签名失败");
			}

		} else {
			// 未返回正确的http状态，未获取到返回报文或返回http状态码非200
			pgwRespRevoke.setRespCode(RetCode.Common.ERROR_SYSTEM);
			pgwRespRevoke.setRespMsg("调用银联冲正接口,未获取到返回报文或返回http状态码非200");
		}
		return pgwRespRevoke;

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
		String configKey = authInfoVo.getConfigKey();
		Map<String, String> data = new HashMap<String, String>();
		PgwRespQueryOrder pgwRespQueryOrder = new PgwRespQueryOrder();
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		data.put("version", UnionPayConstant.VERSION);                 //版本号
		data.put("encoding", UnionPayConstant.ENCODING);          //字符集编码 可以使用UTF-8,GBK两种方式
		data.put("signMethod", UnionPayConstant.SIGNMETHOD); //签名方法
		data.put("txnType", UnionPayConstant.TXNTYPE_00);                             //交易类型 00-默认
		data.put("bizType", UnionPayConstant.BIZTYPE_000201);                         //业务类型 
		
		/***商户接入参数***/
		data.put("merId", KasiteConfig.getUnionPay(UnionPayEnum.merId, configKey));                  			   //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		data.put("accessType", KasiteConfig.getUnionPay(UnionPayEnum.accessType, configKey));                           //接入类型，商户接入固定填0，不需修改
		
		if(!StringUtil.isEmpty(pgwReqQueryOrder.getTransactionNo())) {//如果银联流水号存在，则直接使用银联流水号查询
			// 交易子类型
			data.put("txnSubType", UnionPayConstant.TXNSUBTYPE_02);
			// 查询流水号 queryId C AN20..21 交易子类为02流水号查询时必填
			data.put("queryId", pgwReqQueryOrder.getTransactionNo());
		}else {
			/***要调通交易以下字段必须修改***/
			data.put("txnSubType",UnionPayConstant.TXNSUBTYPE_00);
			data.put("orderId", pgwReqQueryOrder.getOrderId());                 			//****商户订单号，每次发交易测试需修改为被查询的交易的订单号
			data.put("txnTime", pgwReqQueryOrder.getPayTime());                			//****订单发送时间，每次发交易测试需修改为被查询的交易的订单发送时间
		}

		/**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->**/
		
		Map<String, String> reqData = AcpService.sign(data,UnionPayConstant.ENCODING,configKey);			//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		String url = UnionPayConstant.SINGLEQUERYURL;						//交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.singleQueryUrl
		Map<String, String> rspData = AcpService.post(pgwReqQueryOrder.getOrderId(),ApiModule.UnionPay.queryTrans_queryTransStatus,url,configKey,
				reqData,UnionPayConstant.ENCODING); //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
		/**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
		//应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
		if(!rspData.isEmpty()){
			if(AcpService.validate(rspData, UnionPayConstant.ENCODING,configKey)){
				LogUtil.info(log, new LogBody(authInfoVo).set("验证签名结果","成功！"));
				if(("00").equals(rspData.get("respCode"))){//如果查询交易成功
					String origRespCode = rspData.get("origRespCode");
					if(("00").equals(origRespCode)){
						//交易成功，更新商户订单状态
						// 银联查询流水号 queryId M AN20..21 消费交易的流水号，供后续查询用
						pgwRespQueryOrder.setRespCode(RetCode.Success.RET_10000);
						pgwRespQueryOrder.setRespMsg(RetCode.Success.RET_10000.getMessage());
						pgwRespQueryOrder.setOrderState(KstHosConstant.I2);
						pgwRespQueryOrder.setOrderId(rspData.get("orderId"));
						// 订单发送时间
						pgwRespQueryOrder.setPayTime(rspData.get("txnTime"));
						// 交易金额
						pgwRespQueryOrder.setPrice(new Integer(rspData.get("txnAmt")));
						pgwRespQueryOrder.setTransactionNo(rspData.get("queryId"));
					}else if(("03").equals(origRespCode)||
							 ("04").equals(origRespCode)||
							 ("05").equals(origRespCode)){
						//订单处理中或交易状态未明，需稍后发起交易状态查询交易 【如果最终尚未确定交易是否成功请以对账文件为准】
						pgwRespQueryOrder.setRespCode(RetCode.Success.RET_10000);
						pgwRespQueryOrder.setRespMsg(RetCode.Success.RET_10000.getMessage());
						//状态未明=支付中
						pgwRespQueryOrder.setOrderState(KstHosConstant.I1);
					}else{
						//其他应答码为交易失败
						pgwRespQueryOrder.setRespCode(RetCode.Success.RET_10000);
						pgwRespQueryOrder.setRespMsg(rspData.get("respMsg"));
						//交易失败=未支付
						pgwRespQueryOrder.setOrderState(KstHosConstant.I0);
					}
				}else if(("34").equals(rspData.get("respCode"))){
					//订单不存在，可认为交易状态未明，需要稍后发起交易状态查询，或依据对账结果为准
					pgwRespQueryOrder.setRespCode(RetCode.Pay.ERROR_MERCHANTORDER);
					pgwRespQueryOrder.setRespMsg(rspData.get("respMsg"));
				}else{//查询交易本身失败，如应答码10/11检查查询报文是否正确
					pgwRespQueryOrder.setRespCode(RetCode.Common.ERROR_SYSTEM);
					pgwRespQueryOrder.setRespMsg(rspData.get("respMsg"));
				}
			}else{
				LogUtil.info(log, new LogBody(authInfoVo).set("验证签名结果","失败！"));
				pgwRespQueryOrder.setRespCode(RetCode.Common.ERROR_SYSTEM);
				pgwRespQueryOrder.setRespMsg( "验证签名结果失败！");
			}
		}else{
			//未返回正确的http状态
			LogUtil.info(log, new LogBody(authInfoVo).set("银联扫码付请求结果失败","未获取到返回报文或返回http状态码非200"));
			pgwRespQueryOrder.setRespCode(RetCode.Common.ERROR_SYSTEM);
			pgwRespQueryOrder.setRespMsg("未获取到返回报文或返回http状态码非200");
		}
		return pgwRespQueryOrder;
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
		String configKey = authInfoVo.getConfigKey();
		Map<String, String> data = new HashMap<String, String>();
		PgwRespQueryRefundOrder pgwRespQueryRefundOrder = new PgwRespQueryRefundOrder();
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		data.put("version", UnionPayConstant.VERSION);                 //版本号
		data.put("encoding", UnionPayConstant.ENCODING);          //字符集编码 可以使用UTF-8,GBK两种方式
		data.put("signMethod", UnionPayConstant.SIGNMETHOD); //签名方法
		data.put("txnType", UnionPayConstant.TXNTYPE_00);                             //交易类型 00-默认
		data.put("bizType", UnionPayConstant.BIZTYPE_000201);                         //业务类型 
		
		/***商户接入参数***/
		data.put("merId", KasiteConfig.getUnionPay(UnionPayEnum.merId, configKey));                  			   //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		data.put("accessType", KasiteConfig.getUnionPay(UnionPayEnum.accessType, configKey));                           //接入类型，商户接入固定填0，不需修改
		
		if(!StringUtil.isEmpty(pgwReqQueryRefundOrder.getRefundId())) {//如果银联流水号存在，则直接使用银联流水号查询
			// 交易子类型
			data.put("txnSubType", UnionPayConstant.TXNSUBTYPE_02);
			// 查询流水号 queryId C AN20..21 交易子类为02流水号查询时必填
			data.put("queryId", pgwReqQueryRefundOrder.getRefundId());
		}else {
			/***要调通交易以下字段必须修改***/
			data.put("txnSubType",UnionPayConstant.TXNSUBTYPE_00);
			data.put("orderId", pgwReqQueryRefundOrder.getOrderId());                 			//****商户订单号，每次发交易测试需修改为被查询的交易的订单号
			data.put("txnTime", pgwReqQueryRefundOrder.getPayTime());                			//****订单发送时间，每次发交易测试需修改为被查询的交易的订单发送时间
		}

		/**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->**/
		
		Map<String, String> reqData = AcpService.sign(data,UnionPayConstant.ENCODING,configKey);			//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		String url = UnionPayConstant.SINGLEQUERYURL;						//交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.singleQueryUrl
		Map<String, String> rspData = AcpService.post(pgwReqQueryRefundOrder.getOrderId(),ApiModule.UnionPay.queryTrans_queryTransStatus,url,configKey,
				reqData,UnionPayConstant.ENCODING); //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
		/**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
		//应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
		if(!rspData.isEmpty()){
			if(AcpService.validate(rspData, UnionPayConstant.ENCODING,configKey)){
				LogUtil.info(log, new LogBody(authInfoVo).set("验证签名结果","成功！"));
				if(("00").equals(rspData.get("respCode"))){//如果查询交易成功
					String origRespCode = rspData.get("origRespCode");
					if(("00").equals(origRespCode)){
						//交易成功，更新商户订单状态
						// 银联查询流水号 queryId M AN20..21 消费交易的流水号，供后续查询用
						pgwRespQueryRefundOrder.setRespCode(RetCode.Success.RET_10000);
						pgwRespQueryRefundOrder.setRespMsg(RetCode.Success.RET_10000.getMessage());
						pgwRespQueryRefundOrder.setRefundStatus(KstHosConstant.I4);
						pgwRespQueryRefundOrder.setRefundId(rspData.get("queryId"));
						pgwRespQueryRefundOrder.setRefundOrderId(rspData.get("orderId"));
						pgwRespQueryRefundOrder.setRefundPrice(new Integer(rspData.get("txnAmt")));
						pgwRespQueryRefundOrder.setRefundTime(rspData.get("txnTime"));
					}else if(("03").equals(origRespCode)||
							 ("04").equals(origRespCode)||
							 ("05").equals(origRespCode)){
						//订单处理中或交易状态未明，需稍后发起交易状态查询交易 【如果最终尚未确定交易是否成功请以对账文件为准】
						pgwRespQueryRefundOrder.setRespCode(RetCode.Success.RET_10000);
						pgwRespQueryRefundOrder.setRespMsg(RetCode.Success.RET_10000.getMessage());
						//状态未明=支付中
						pgwRespQueryRefundOrder.setRefundStatus(KstHosConstant.I3);
					}else{
						//其他应答码为交易失败
						pgwRespQueryRefundOrder.setRespCode(RetCode.Success.RET_10000);
						pgwRespQueryRefundOrder.setRespMsg(rspData.get("respMsg"));
						//交易失败=退款失败
						pgwRespQueryRefundOrder.setRefundStatus(KstHosConstant.I7);
					}
				}else if(("34").equals(rspData.get("respCode"))){
					//订单不存在，可认为交易状态未明，需要稍后发起交易状态查询，或依据对账结果为准
					pgwRespQueryRefundOrder.setRespCode(RetCode.Pay.ERROR_MERCHANTORDER);
					pgwRespQueryRefundOrder.setRespMsg(rspData.get("respMsg"));
				}else{//查询交易本身失败，如应答码10/11检查查询报文是否正确
					pgwRespQueryRefundOrder.setRespCode(RetCode.Common.ERROR_SYSTEM);
					pgwRespQueryRefundOrder.setRespMsg(rspData.get("respMsg"));
				}
			}else{
				LogUtil.info(log, new LogBody(authInfoVo).set("验证签名结果","失败！"));
				pgwRespQueryRefundOrder.setRespCode(RetCode.Common.ERROR_SYSTEM);
				pgwRespQueryRefundOrder.setRespMsg( "验证签名结果失败！");
			}
		}else{
			//未返回正确的http状态
			LogUtil.info(log, new LogBody(authInfoVo).set("银联扫码付请求结果失败","未获取到返回报文或返回http状态码非200"));
			pgwRespQueryRefundOrder.setRespCode(RetCode.Common.ERROR_SYSTEM);
			pgwRespQueryRefundOrder.setRespMsg("未获取到返回报文或返回http状态码非200");
		}
		return pgwRespQueryRefundOrder;
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
		return null;
	}
	
	public static RetCode unionPayRespCodeCodeParseRetCode(String respCode) {
		if( StringUtil.isEmpty(respCode) ) {
			return RetCode.Common.ERROR_SYSTEM;
		}
		RetCode retCode = null;
		switch (respCode) {
		case UnionPayConstant.QRCODECONSUMEPASSIVE_88:
			//respMsg=无此二维码
			retCode = RetCode.Pay.ERROR_SWEEPCODEPAY_AUTHCODEINVALID;
			break;
		case UnionPayConstant.QRCODECONSUMEPASSIVE_33:
			//respMsg=交易金额超限
			//TODO 待测试，验证！！
			retCode = RetCode.Pay.ERROR_SWEEPCODEPAY_USERPAYING;
			break;
		case UnionPayConstant.QRCODECONSUMEPASSIVE_01:
			//respMsg=交易失败，重复二维码会出现。
			retCode = RetCode.Pay.ERROR_SWEEPCODEPAY_AUTHCODEINVALID;
		break;default:
			retCode = RetCode.Common.ERROR_SYSTEM;
			break;
		}
		return retCode;
	}

	/**
	 * @param configKey
	 * @param bill
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespDownloadBill downloadBill(AuthInfoVo authInfoVo, Date billDate) throws Exception {
		PgwRespDownloadBill pgwRespDownloadBill = new PgwRespDownloadBill();
		List<MchBill> mchBillList = new ArrayList<MchBill>();
		String configKey = authInfoVo.getConfigKey();
		// 账单参数，请根据以下地址对照
		// https://open.unionpay.com/upload/download/%E5%B9%B3%E5%8F%B0%E6%8E%A5%E5%8F%A3%E8%A7%84%E8%8C%83-%E7%AC%AC3%E9%83%A8%E5%88%86-%E6%96%87%E4%BB%B6%E6%8E%A5%E5%8F%A3V2.2.pdf
		String ylBillDate = DateOper.formatDate(billDate, "MMdd");
		// String ylBillDate = "0119";
		Map<String, String> data = new HashMap<String, String>();

		/*** 银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改 ***/
		data.put("version", UnionPayConstant.VERSION); // 版本号 全渠道默认值
		data.put("encoding", UnionPayConstant.ENCODING); // 字符集编码 可以使用UTF-8,GBK两种方式
		data.put("signMethod", UnionPayConstant.SIGNMETHOD); // 签名方法
		data.put("txnType", UnionPayConstant.TXNTYPE_76); // 交易类型 76-对账文件下载
		data.put("txnSubType", UnionPayConstant.TXNSUBTYPE_01); // 交易子类型 01-对账文件下载
		data.put("bizType", UnionPayConstant.BIZTYPE_000000); // 业务类型，固定

		/*** 商户接入参数 ***/
		data.put("accessType", KasiteConfig.getUnionPay(UnionPayEnum.accessType, configKey)); // 接入类型，商户接入填0，不需修改
		data.put("merId", KasiteConfig.getUnionPay(UnionPayEnum.merId, configKey)); // 商户代码，请替换正式商户号测试，如使用的是自助化平台注册的777开头的商户号，该商户号没有权限测文件下载接口的，请使用测试参数里写的文件下载的商户号和日期测。如需777商户号的真实交易的对账文件，请使用自助化平台下载文件。
		data.put("settleDate", ylBillDate); // 清算日期，如果使用正式商户号测试则要修改成自己想要获取对账文件的日期，
											// 测试环境如果使用700000000000001商户号则固定填写0119
		data.put("txnTime", DateOper.getNow("yyyyMMddHHmmss")); // 订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		data.put("fileType", KasiteConfig.getUnionPay(UnionPayEnum.fileType, configKey)); // 文件类型，一般商户填写00即可

		/** 请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文-------------> **/

		Map<String, String> reqData = AcpService.sign(data, UnionPayConstant.ENCODING, configKey); // 报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		String url = UnionPayConstant.fileTransUrl; // 获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.fileTransUrl
		Map<String, String> rspData = AcpService.post(null,ApiModule.UnionPay.filedownload, url, configKey, reqData,
				UnionPayConstant.ENCODING);

		// 先清空之前的账单文件
		String tmpdir = System.getProperty("java.io.tmpdir");
		String tempFileName = "tempYLBill";
		String destination = null;
		if (tmpdir.endsWith("/") || tmpdir.endsWith("\\")) {
			destination = tmpdir + tempFileName + File.separator;
		} else {
			destination = tmpdir + File.separator + tempFileName + File.separator;
		}
		File desFile = new File(destination);
		if (desFile.exists()) {
			KasiteConfig.print("清空临时文件:" + CommonUtil.deleteAllFilesOfDir(desFile));
		} else {
			KasiteConfig.print("创建临时文件:" + desFile.mkdirs());
			desFile.mkdirs();
		}
		/** 对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考-------------> **/

		// 应答码规范参考open.unionpay.com帮助中心 下载 产品接口规范 《平台接入接口规范-第5部分-附录》
		if (!rspData.isEmpty()) {
			if (AcpService.validate(rspData, UnionPayConstant.ENCODING, configKey)) {
				LogUtil.info(log, "验证签名成功");
				String respCode = rspData.get("respCode");
				if ("00".equals(respCode)) {
					// 交易成功，解析返回报文中的fileContent并落地
					String zipFilePath = AcpService.deCodeFileContent(rspData, destination, UnionPayConstant.ENCODING);
					// 对落地的zip文件解压缩并解析
					List<String> fileList = SDKUtil.unzip(zipFilePath, destination);
					for (String file : fileList) {
						if (file.indexOf("ZM_") != -1) {
							List<Map<Integer, String>> zmDataList = SDKUtil.parseZMFile(file);
							// KasiteConfig.print(SDKUtil.getFileContentTable(zmDataList,file));
							for (int i = 0; i < zmDataList.size(); i++) {
								Map<Integer, String> dataMapTmp = zmDataList.get(i);

								MchBill mchBill = new MchBill();
								if (StringUtil.isEmpty(dataMapTmp.get(13))) {// 14.原始交易系统跟踪号，不存在，则为支付交易
									mchBill.setMchTradeNo(dataMapTmp.get(9));// queryId
									mchBill.setOrderId(dataMapTmp.get(11));// orderId
									mchBill.setOrderType(KstHosConstant.BILL_ORDER_TYPE_1);
									mchBill.setPayPrice(new Integer(dataMapTmp.get(6)));// txnAmet
								} else {// 存在则为退货（退费）交易
									mchBill.setOrderType(KstHosConstant.BILL_ORDER_TYPE_2);
									mchBill.setRefundMchTradeNo(dataMapTmp.get(9));// queryId
									mchBill.setRefundOrderId(dataMapTmp.get(11));// orderId
									mchBill.setMchTradeNo(dataMapTmp.get(26));// origQryId
									mchBill.setRefundPrice(new Integer(dataMapTmp.get(6)));// txnAmet
									mchBill.setOrderId(dataMapTmp.get(40));//
								}
								String transDateStr = DateOper.formatDate(billDate, "yyyy") + dataMapTmp.get(4);// txnTime
																												// MMDDhhmmss
								mchBill.setTransDate(DateOper.parse2Timestamp(transDateStr));
								mchBill.setConfigKey(configKey);
								// 调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP
								mchBill.setTradeType(dataMapTmp.get(17));// payType
								mchBillList.add(mchBill);
							}
						}
						// else if(file.indexOf("ZME_")!=-1){
						// //此处逻辑待测试完善
						// ZMF_为商户查错交易明细流水文件
						// SDKUtil.parseZMEFile(file);
						// }
					}
					pgwRespDownloadBill.setRespCode(RetCode.Success.RET_10000);
					pgwRespDownloadBill.setRespMsg(RetCode.Success.RET_10000.getMessage());
					pgwRespDownloadBill.setMchBillList(mchBillList);
					// TODO
				} else {
					// 其他应答码为失败请排查原因
					log.error("账单日期:" + billDate + ",应答码失败：" + respCode);
					pgwRespDownloadBill.setRespCode(RetCode.Common.ERROR_SYSTEM);
					pgwRespDownloadBill.setRespMsg("应答码失败：" + respCode);
				}
			} else {
				log.error("账单日期:" + billDate + ",验证签名失败，请检查配置。");
				// TODO 检查验证签名失败的原因
				pgwRespDownloadBill.setRespCode(RetCode.Common.ERROR_SYSTEM);
				pgwRespDownloadBill.setRespMsg("验证签名结果失败！");
			}
		} else {
			// 未返回正确的http状态
			log.error("账单日期:" + billDate + ",未获取到返回报文或返回http状态码非200。");
			pgwRespDownloadBill.setRespCode(RetCode.Common.ERROR_SYSTEM);
			pgwRespDownloadBill.setRespMsg("未获取到返回报文或返回http状态码非200。");
		}
		return pgwRespDownloadBill;
	}

	/**
	 * @return
	 */
	@Override
	public String mchType() {
		return ChannelTypeEnum.unionpay.name();
	}

}

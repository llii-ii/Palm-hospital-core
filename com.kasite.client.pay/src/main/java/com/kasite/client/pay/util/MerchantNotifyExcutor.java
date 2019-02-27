package com.kasite.client.pay.util;

import java.text.ParseException;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.kasite.client.pay.dao.IMerchantNotifyMapper;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.SpringContextUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;
import com.kasite.core.serviceinterface.module.order.IOrderService;
import com.kasite.core.serviceinterface.module.order.OrderCallbackService;
import com.kasite.core.serviceinterface.module.order.req.ReqCancelForCompletion;
import com.kasite.core.serviceinterface.module.pay.IPayMerchantService;
import com.kasite.core.serviceinterface.module.pay.dbo.MerchantNotify;
import com.kasite.core.serviceinterface.module.pay.req.ReqUpdateMerchantNotifyById;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * 异步通知执行器
 */
public class MerchantNotifyExcutor extends Thread{
	
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_ORDER);

	private MerchantNotify merchantNotify;
	
	private InterfaceMessage msg;
	
	private IMerchantNotifyMapper merchantNotifyMapper;
	
	public MerchantNotifyExcutor(InterfaceMessage msg,MerchantNotify merchantNotify,IMerchantNotifyMapper merchantNotifyMapper) {
		this.merchantNotify = merchantNotify;
		this.msg = msg;
		this.merchantNotifyMapper = merchantNotifyMapper;
	}
	
	/**
	 * 
	 */
	@Override
	public void run() {
		try {
			
			String configKey = merchantNotify.getConfigKey();
			String hopNotifyUrl = KasiteConfig.getDiyVal("div_hopNotifyUrl_"+configKey);
			if(StringUtil.isEmpty(hopNotifyUrl)) {//是否存在自定义的异步通知配置
				internalNotify();//标准版逻辑
			}else {
				divHopNotify(hopNotifyUrl);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e);
		}
	}
	/**
	 * 标准版逻辑
	 * 内部通知-调用订单支付完成接口，并且调用HIS接口
	 * @throws Exception
	 */
	private void internalNotify() throws Exception {
		//支付通知
		if(merchantNotify.getOrderType().intValue()==1) {
			OrderCallbackService orderCallbackService = SpringContextUtil.getBean(OrderCallbackService.class);
			if(orderCallbackService != null) {
				orderCallbackService.orderCallback(msg, merchantNotify);
			}else {
				throw new RRException("接口回调异常。");
			}
		}else if(merchantNotify.getOrderType().intValue() == 2) {
			//退款通知 TODO 执行回调业务
			IOrderService orderService = (IOrderService) SpringContextUtil.getBean(IOrderService.class);
			IPayMerchantService payMerchantService = (IPayMerchantService) SpringContextUtil.getBean(IPayMerchantService.class);
			AuthInfoVo authInfoVo  = KasiteConfig.getAuthInfo(msg.getAuthInfo());
			if(orderService!=null ) {
				ReqCancelForCompletion reqPayForCompletion = new ReqCancelForCompletion(msg, merchantNotify.getOrderId(), merchantNotify.getRefundOrderId(),
						merchantNotify.getRefundNo(),authInfoVo.getSign(),authInfoVo.getSessionKey(),merchantNotify.getRefundPrice(),merchantNotify.getPrice());
				orderService.cancelForCompletion(new CommonReq<ReqCancelForCompletion>(reqPayForCompletion));
				ReqUpdateMerchantNotifyById reqUpdateMerchantNotifyById = new ReqUpdateMerchantNotifyById (msg,merchantNotify.getId(),1,null);
				payMerchantService.updateMerchantNotifyById(new CommonReq<ReqUpdateMerchantNotifyById>(reqUpdateMerchantNotifyById));
			}
		}
	}
	
	
	/**
	 * 自定义的通知
	 * 逻辑：不走内部通知逻辑，直接调用旧的hos-hop的payForCompletion接口
	 * 泉州儿童在用2019年1月30日 14:29:19
	 * 非标准版逻辑，建议泉州儿童重构后，删除该逻辑
	 * @throws ParseException 
	 */
	private void divHopNotify(String hopNotifyUrl) throws ParseException {
		merchantNotify.setState(0);//置为正在处理中
		merchantNotify.setRetryNum(merchantNotify.getRetryNum()+1);
		merchantNotify.setUpdateTime(DateOper.getNowDateTime());
		merchantNotifyMapper.updateByPrimaryKeySelective(merchantNotify);
		String sequenceNo = DateOper.getNow("yyyyMMddHHmmss");
		Document reqDoc = DocumentHelper.createDocument();
		Element reqEl = reqDoc.addElement(KstHosConstant.REQ);
		XMLUtil.addElement(reqEl, KstHosConstant.TRANSACTIONCODE, "6007");
		Element dataEl = reqEl.addElement("Service");
		XMLUtil.addElement(dataEl, "OrderID", merchantNotify.getOrderId());
		XMLUtil.addElement(dataEl, "UpdateKey", merchantNotify.getTransactionNo());
		XMLUtil.addElement(dataEl, "Price", merchantNotify.getPrice());
		String authInfo = "{ClientVersion:1,ClientId:'"+merchantNotify.getClientId()+"',Sign:'Sign',SessionKey:'SessionKey'}";
		String reqParam = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://com.yihu.wsgw/ServiceGateWay\">\r\n" + 
				"   <soapenv:Header/>\r\n" + 
				"   <soapenv:Body>\r\n" + 
				"      <ser:service>\r\n" + 
				"         <ser:authInfo><![CDATA["+authInfo+"]]></ser:authInfo>\r\n" + 
				"         <ser:sequenceNo>"+sequenceNo+"</ser:sequenceNo>\r\n" + 
				"         <ser:api>order.orderApi.PayForCompletion</ser:api>\r\n" + 
				"         <ser:param><![cdata[ "+reqDoc.asXML()+"]]></ser:param>\r\n" + 
				"         <ser:paramType>1</ser:paramType>\r\n" + 
				"         <ser:outType>1</ser:outType>\r\n" + 
				"         <ser:v>1</ser:v>\r\n" + 
				"      </ser:service>\r\n" + 
				"   </soapenv:Body>\r\n" + 
				"</soapenv:Envelope>";
		SoapResponseVo soapResponseVo = HttpRequestBus.create(hopNotifyUrl, RequestType.soap2).setParam(reqParam).send();
		if( HttpStatus.SC_OK == soapResponseVo.getCode()) {
			merchantNotify.setState(1);//置为处理成功
			merchantNotify.setUpdateTime(DateOper.getNowDateTime());
			merchantNotifyMapper.updateByPrimaryKeySelective(merchantNotify);
		}else {
			merchantNotify.setState(-1);//置为处理失败
			merchantNotify.setUpdateTime(DateOper.getNowDateTime());
			merchantNotifyMapper.updateByPrimaryKeySelective(merchantNotify);
		}
	}
}

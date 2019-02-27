package com.kasite.client.hospay.module.bill.service;

import java.io.IOException;
import java.text.ParseException;

import com.alipay.api.AlipayApiException;
import org.jdom.JDOMException;

import com.alibaba.fastjson.JSONObject;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.ThreePartyBalance;
import com.kasite.client.hospay.module.bill.entity.bill.dto.QueryBillParam;

/**
 * @author cc
 * TODO 退费相关业务接口
 */
public interface RefundService {

     /**
      * 退费接口实现类,兼容微信/支付宝
      * @param bill
      * @return
      * @throws Exception
      */
     JSONObject refund(ThreePartyBalance bill) throws Exception;

     /**
      * 查询微信/支付宝订单详情
      * @param queryBillParam
      * @return
      * @throws JDOMException
      * @throws IOException
      * @throws ParseException
      * @throws AlipayApiException
      */
     JSONObject queryOrder(QueryBillParam queryBillParam) throws JDOMException, IOException, ParseException, AlipayApiException;
}

package com.kasite.client.hospay.module.bill.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.kasite.client.hospay.common.constant.Constant;
import com.kasite.client.hospay.module.bill.entity.bill.bo.HisBill;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.ThreePartyBalance;
import com.kasite.client.hospay.module.bill.entity.bill.dto.QueryBillParam;
import com.kasite.client.hospay.module.bill.service.RefundService;
import com.kasite.client.hospay.module.bill.util.BillUtils;
import com.kasite.core.common.util.R;

import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.List;

/**
 * @author cc
 * TODO 退费控制器
 */
@RequestMapping("/billRefundController")
@RestController
public class BillRefundController {
     private final static Logger logger = LoggerFactory.getLogger(BillRefundController.class);

     @Autowired
     RefundService refundService;

     @Autowired
     BillUtils billUtils;

     @PostMapping(value = "/queryHisOrderState.do")
     public R queryHisOrderState(QueryBillParam param){
          /* 调用HIS订单验证接口,验证当前订单状态是否满足退费*/
          logger.info("开始验证HIS订单的状态!!!");
          List<HisBill> list;
          try {
               list = billUtils.getHisInstance().queryHisOrderState(param.getOrderId(), param.getStartDate(), param.getEndDate());
          } catch (Exception e) {
               e.printStackTrace();
               return R.error(e.getMessage());

          }
          logger.info("验证HIS订单的状态结束!!!");
          if (list != null && list.isEmpty()) {
               logger.info("未在HIS系统查询到当前订单,可进行退费操作!");
               return R.ok("未在HIS系统查询到当前订单,可进行退费操作!");
          }
          logger.info("当前账单已存在HIS系统,切勿执行退费业务!!!");
          return R.error().put("RespCode", -10000).put("RespMessage","当前账单已存在HIS系统或调用接口异常,切勿执行退费业务!");
     }

     @PostMapping("/refund.do")
     public JSONObject refund(QueryBillParam param) {
          /*
           * 1.先提供订单号调用渠道查询订单的接口,获取订单详情
           * 2.处理后拼接到ThreePartyBalance对象当中调用refund接口进行退费
           * */
          try {
               JSONObject result = refundService.queryOrder(param);
               /* 如果查询订单成功，则开始赋值进行退费*/
               if (Constant.RET_10000.equals(result.get(Constant.RESPCODE))){
                    logger.info("查询订单详情成功,订单号是:{}",param.getOrderId());
                    ThreePartyBalance partyBalance = new ThreePartyBalance();
                    partyBalance.setOrderId(result.getString("orderId"));
                    partyBalance.setChannelOrderId(result.getString("channelOrderId"));
                    partyBalance.setChannelId(param.getChannelId());
                    partyBalance.setReceivableMoney(result.getString("price"));
                    partyBalance.setRefundMoney(result.getString("price"));
                    try {
                         JSONObject refund = refundService.refund(partyBalance);
                         //判断当前调用退费API是否成功
                         if (!Constant.RET_10000.equals(refund.get(Constant.RESPCODE))){
                              logger.info("调用退费API失败,Code是:{},Msg是:{}", refund.get("RespCode"), refund.get("RespMessage"));
                              return refund;
                         }
                         logger.info("调用退费API成功,Code是:{},Msg是:{}", refund.get("RespCode"), refund.get("RespMessage"));
                         return refund;
                    } catch (Exception e) {
                         e.printStackTrace();
                         logger.info("调用退费API异常:{}",e.getMessage());
                         return billUtils.returnMsg(Constant.FAIL_10000,e.getMessage());
                    }
               }else if (Constant.RET_10001.equals(result.get(Constant.RESPCODE))) {
            	   logger.info("查询订单详情成功,但该订单已无法进行退费,原因:{},订单号是:{}",result.getString("RespMessage"),param.getOrderId());
            	   return result;
               }
               logger.info("查询订单详情失败,订单号是:{},提示信息是:{}",param.getOrderId(),result.get("RespMessage"));
               return result;
          } catch (JDOMException | IOException | ParseException | AlipayApiException e) {
               e.printStackTrace();
               return billUtils.returnMsg(Constant.FAIL_10000,e.getMessage());
          }
     }

     public static void main(String[] args) throws UnsupportedEncodingException {
          String s = "https%3A%2F%2Fdiagnosis-guide.wecity.qq.com%2F3rd%2Fregistered-callback%3Fsignature%3Ddfa0a846e18d75617a2d8fe82dd8208b%26timestamp%3D1547438104141%26partnerId%3D100000038%26id%3D8b3257eb-efda-44a1-9372-f0eedd4b0dd0%26dept%3D%25E7%25A5%259E%25E7%25BB%258F%25E5%2586%2585%25E7%25A7%2591%26doctor%3D";
          System.out.println(URLDecoder.decode(s, "utf-8"));
     }
}

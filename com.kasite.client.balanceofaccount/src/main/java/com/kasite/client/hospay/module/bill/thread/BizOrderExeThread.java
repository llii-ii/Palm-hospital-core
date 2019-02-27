package com.kasite.client.hospay.module.bill.thread;

import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.StringUtil;
import com.kasite.client.hospay.common.constant.Constant;
import com.kasite.client.hospay.module.bill.dao.ThreePartyBalanceDao;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.ThreePartyBalance;
import com.kasite.client.hospay.module.bill.service.BillService;
import com.kasite.client.hospay.module.bill.service.RefundService;
import com.kasite.core.common.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author cc
 * TODO 异常订单业务执行线程
 */
public class BizOrderExeThread extends Thread{

     private final Logger logger = LoggerFactory.getLogger(BizOrderExeThread.class);
     /** 进入方法执行的标记 */
     private static boolean flag = true;

     private BillService billService;

     private RefundService refundService;

     private ThreePartyBalanceDao threePartyBalanceDao;

     public BizOrderExeThread() {
          this.billService = SpringContextUtil.getBean(BillService.class);
          this.refundService =  SpringContextUtil.getBean(RefundService.class);
          this.threePartyBalanceDao = SpringContextUtil.getBean(ThreePartyBalanceDao.class);
     }


     @Override
     public void run() {
          if (flag) {
               flag = false;
               try {
                    JSONObject result = null;
                    logger.info("查询需要执行业务的订单!!!");
                    List<ThreePartyBalance> partyBills = threePartyBalanceDao.queryExistBizOrder();
                    logger.info("需要执行业务的订单总数为:{}",partyBills.size());
                    if (!partyBills.isEmpty()) {
                         for (ThreePartyBalance bill : partyBills) {
                              // 1.更新业务订单执行状态为正在执行
                              threePartyBalanceDao.updateThreePartyBalanceExeState(Constant.BEINGPAIDSTATE, bill.getOrderId(), bill.getHisOrderId(), bill.getChannelOrderId(), null,null,null);
                              logger.info("修改订单号:{} 状态为正在执行中！！！", bill.getOrderId());
                              /* 判断当前订单的业务类型 为1时才进行冲正(需要自己实现) 为2时才进行退费 为3时进行财务调账(针对短款订单)*/
                              if (Constant.BIZTYPEONE.equals(bill.getBizType())) {
                                   //需要自行实现，建议是让掌医端开放接口，不要直接在对账平台实现，少耦合
                                   logger.info("目前暂未实现冲正接口,建议是让掌医端开放接口，不要直接在对账平台实现，少耦合");
                                   return;
                              } else if (Constant.BIZTYPETWO.equals(bill.getBizType())) {
                                   // 2.如果渠道不为空则调用退费API
                                   if (!StringUtil.isNotBlank(bill.getChannelId())) {
                                        logger.error("订单号为:{} 不存在渠道ID无法进行退费操作,请核实！！！", bill.getOrderId());
                                   }
                                   logger.info("开始进行退费,订单号是:{},渠道号是:{}", bill.getOrderId(), bill.getChannelId());
                                   try {
                                        result = refundService.refund(bill);
                                        /* 根据Code判断退费结果,如果不为10000则提示，业务暂停*/
                                        if (!Constant.RET_10000.equals(result.get("RespCode"))) {
                                             logger.info("调用退费API失败,Code是:{},Msg是:{}", result.get("RespCode"), result.get("RespMessage"));
                                             return;
                                        }
                                   } catch (Exception e) {
                                        e.printStackTrace();
                                        logger.info("调用退费API异常:{}", e.getMessage());
                                        return;
                                   }
                                   logger.info("退费成功,开始更新订单号为:{} 的订单状态", bill.getOrderId());
                                   //3.更新该订单的退费金额，保持数据的一致且在退款成功后续更新业务订单状态为退款成功
                                   String refundPrice;
                                   if (Constant.QLCCHANNELORDERTYPE.equals(bill.getQlcOrderState())){
                                        refundPrice = bill.getAlreadyReceivedMoney()+"";
                                   }else {
                                        refundPrice = bill.getRefundMoney()+"";
                                   }
                                   logger.info("开始更新退款订单状态!,订单号:{},订单类型:{}",bill.getOrderId(),bill.getQlcOrderState());
                                   threePartyBalanceDao.updateThreePartyBalanceExeState(Constant.SUCCESSSTATE, bill.getOrderId(), bill.getHisOrderId(), bill.getChannelOrderId(), refundPrice,result.get("RefundId").toString(),bill.getQlcOrderState());
                                   logger.info("更新退款订单状态结束!");
                              } else if (Constant.BIZTYPETHREE.equals(bill.getBizType())) {
                                   logger.info("开始保存财务调账,订单号:{}", bill.getOrderId());
                                   /* 判断当前订单的业务类型 为3的订单属于财务调账,直接更新执行状态即可*/
                                   threePartyBalanceDao.updateThreePartyBalanceExeState(Constant.SUCCESSSTATE, bill.getOrderId(), bill.getHisOrderId(), bill.getChannelOrderId(), null,null,bill.getQlcOrderState());
                                   logger.info("保存财务调账成功!!!");
                                   /* 由于并没有执行service层的业务,所以更新完这边直接赋值RespCode为10000即可*/
                                   result = new JSONObject();
                                   result.put("RespCode", Constant.RET_10000);
                              }
                              //4.当业务执行成功后需要通知系统，需要重新汇总每日汇总账单
                              if (Constant.RET_10000.equals(result.get("RespCode"))) {
                                   logger.info("开始重新汇总每日汇总账单！！！");
                                   billService.basedBizChangesEveryDayBalance(bill.getBizType(), bill.getOrderId(), bill.getHisOrderId(), bill.getChannelOrderId());
                                   logger.info("重新汇总每日汇总账单结束！！！");
                              }
                         }
                    }
               } finally {
            	    logger.info("异步线程执行业务结束!!!");
                    flag = true;
               }
          }
     }
}

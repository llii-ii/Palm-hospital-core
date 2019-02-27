package com.kasite.client.hospay.module.bill.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.ArithUtil;
import com.coreframework.util.StringUtil;
import com.github.pagehelper.PageInfo;
import com.kasite.client.hospay.module.bill.dao.EveryDayBalanceDao;
import com.kasite.client.hospay.module.bill.entity.bill.bo.RequestHandlerParam;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.ClassifySummaryBalance;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.EveryDayBalance;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.ThreePartyBalance;
import com.kasite.client.hospay.module.bill.entity.bill.dto.QueryBillParam;
import com.kasite.client.hospay.module.bill.service.BillService;
import com.kasite.client.hospay.module.bill.util.BillUtils;
import com.kasite.client.hospay.module.bill.util.XmlParamUtils;
import com.kasite.core.common.req.PageVo;

import com.kasite.core.common.util.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author cc 对账控制类
 * TODO 由于数据库一些表设计的原因,根据条件筛选出来的结果有些属性会为null，但是又需要做一些函数判断，所以一律在javaBean的get,set方法中做处理
 */
@RequestMapping(value = "/billController")
@RestController
public class BillController {
     private final static Logger logger = LoggerFactory.getLogger(BillController.class);

     @Autowired
     BillService billService;

     @Autowired
     EveryDayBalanceDao everyDayBalanceDao;

     @Autowired
     RequestHandlerParam request;

     @Autowired
     BillUtils billUtils;

     /**
      * 获取每日汇总账单数据
      *
      * @return
      */
     @GetMapping(value = "/queryEveryDayBills.do", produces = "text/html;charset=UTF-8")
     public String queryEveryDayBills(QueryBillParam queryBillParam) {
          PageVo page = new PageVo();
          page.setPCount(new Integer(queryBillParam.getCount()));
          page.setPIndex(new Integer(queryBillParam.getStart()));
          page.setPSize(new Integer(queryBillParam.getLength()));
          PageInfo<EveryDayBalance> everyDayBills = null;
          try {
               everyDayBills = billService.queryEveryDayBills(queryBillParam,page);
          } catch (SQLException e) {
               e.printStackTrace();
          }

          JSONObject jsonobject = new JSONObject();
          jsonobject.put("recordsTotal", everyDayBills.getTotal());
          jsonobject.put("recordsFiltered", everyDayBills.getTotal());
          jsonobject.put("draw", queryBillParam.getDraw());
          /* 是否开启汇总时间限制*/
          jsonobject.put("isDefault", request.isDefault);
          JSONArray array = new JSONArray();
          if (everyDayBills.getList().size() > 0) {
               for (EveryDayBalance bill : everyDayBills.getList()) {
                    JSONObject data = new JSONObject();
                    data.put("billDate", bill.getBillDate());
                    data.put("hisBills", StringUtil.isBlank(bill.getHisBills())?"":bill.getHisBills());
                    data.put("qlcBills", StringUtil.isBlank(bill.getQlcBills())?"":bill.getQlcBills());
                    data.put("channelBills", StringUtil.isBlank(bill.getChannelBills())?"":bill.getChannelBills());
                    data.put("checkNum", bill.getCheckNum());
                    data.put("abnormalNum", bill.getAbnormalNum());
                    data.put("overPlusErrNum", StringUtil.isBlank(bill.getOverPlusErrNum())?"":bill.getOverPlusErrNum());
                    data.put("receivableMoney", ArithUtil.div(bill.getReceivableMoney(),"100",2));
                    data.put("alreadyReceivedMoney",ArithUtil.div(bill.getAlreadyReceivedMoney(),"100",2));
                    data.put("refundMoney", ArithUtil.div(bill.getRefundMoney(),"100",2));
                    data.put("alreadyRefundMoney",ArithUtil.div(bill.getAlreadyRefundMoney(),"100",2));
                    data.put("checkResult", bill.getCheckResult());
                    array.add(data);
               }
          }
          jsonobject.put("data", array);
          return jsonobject.toString();
     }

     /**
      * 查询每日账单趋势图
      * @return String
      */
     @PostMapping(value="/queryEveryDayTrendMap.do")
     public String queryEveryDayTrendMap(QueryBillParam queryBillParam){
          List<EveryDayBalance> list;
          logger.info("开始查询每日账单趋势图");
          list = everyDayBalanceDao.queryEveryDayBills(queryBillParam);
          logger.info("查询每日账单趋势图结束！！！");
          JSONObject jsonobject = new JSONObject();
          JSONArray arrayBillDate = new JSONArray();
          JSONArray arrayHisBillCount = new JSONArray();
          if( list!=null && list.size()>0 ){
               for(EveryDayBalance bill : list){
                    arrayBillDate.add(bill.getBillDate());
                    arrayHisBillCount.add(bill.getCheckNum());
               }
          }
          jsonobject.put("billDates", arrayBillDate);
          jsonobject.put("billCount", arrayHisBillCount);
          return jsonobject.toString();
     }


     /**
      * 获得三方汇总账单明细数据

      * @param queryBillParam
      * @return String
      * @throws ParseException
      */
     @GetMapping(value = "/queryThreePartyBillDetail.do", produces = "text/html;charset=UTF-8")
     public String queryThreePartyBillDetail(QueryBillParam queryBillParam) throws ParseException{
          PageVo page = new PageVo();
          page.setPCount(new Integer(queryBillParam.getCount()));
          page.setPIndex(new Integer(queryBillParam.getStart()));
          page.setPSize(new Integer(queryBillParam.getLength()));
          PageInfo<ThreePartyBalance> threePartyBalancePageInfo = null;
          try {
               threePartyBalancePageInfo = billService.queryThreePartyBillDetail(queryBillParam,page);
          } catch (SQLException e) {
               e.printStackTrace();
          }
          
          JSONObject jsonobject = new JSONObject();
          jsonobject.put("recordsTotal", threePartyBalancePageInfo.getTotal());
          jsonobject.put("recordsFiltered", threePartyBalancePageInfo.getTotal());
          jsonobject.put("draw", queryBillParam.getDraw());
          JSONArray array = new JSONArray();
          if (threePartyBalancePageInfo.getList().size() > 0) {
               for (ThreePartyBalance bill : threePartyBalancePageInfo.getList()) {
                    JSONObject data = new JSONObject();
                    data.put("orderId", StringUtil.isBlank(bill.getOrderId())?"":bill.getOrderId());
                    data.put("hisOrderId",StringUtil.isBlank(bill.getHisOrderId())?"":bill.getHisOrderId());
                    data.put("channelOrderId",StringUtil.isBlank(bill.getChannelOrderId())?"":bill.getChannelOrderId());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    data.put("createDate", simpleDateFormat.format(bill.getCreateDate()));
                    data.put("lastDate", StringUtil.isBlank(bill.getLastDate())?"暂无":simpleDateFormat.format(bill.getLastDate()));
                    data.put("priceName", StringUtil.isBlank(bill.getPriceName())?"":bill.getPriceName());
                    data.put("receivableMoney", ArithUtil.div(bill.getReceivableMoney(),"100",2));
                    data.put("alreadyReceivedMoney",ArithUtil.div(bill.getAlreadyReceivedMoney(),"100",2));
                    data.put("refundMoney", ArithUtil.div(bill.getRefundMoney(),"100",2));
                    data.put("alreadyRefundMoney",ArithUtil.div(bill.getAlreadyRefundMoney(),"100",2));
                    data.put("channelOrderState", StringUtil.isBlank(bill.getChannelOrderState())?"":bill.getChannelOrderState());
                    data.put("channelId", StringUtil.isBlank(bill.getChannelId())?"":bill.getChannelId());
                    data.put("errorState","1".equals(bill.getExeState())||"2".equals(bill.getExeState())?"1":bill.getErrorState());
                    //根据联合查询的结果exeState得出当前订单执行业务后，具体的订单状态，然后做返回
                    data.put("checkState", "2".equals(bill.getExeState())?bill.getExeState():bill.getCheckState());
                    //业务类型的返回需要根据exeState的值来返回
                    data.put("bizType",billUtils.changeType(bill.getBizType(),bill.getExeState()));

                    //暂未使用到,但还是获取
                    data.put("cardNo", StringUtil.isBlank(bill.getCardNo())?"":bill.getCardNo());
                    data.put("serviceId", StringUtil.isBlank(bill.getServiceId())?"":bill.getServiceId());
                    data.put("reserveId", StringUtil.isBlank(bill.getReserveId())?"":bill.getReserveId());
                    data.put("orderMemo", StringUtil.isBlank(bill.getOrderMemo())?"":bill.getOrderMemo());
                    data.put("operator", StringUtil.isBlank(bill.getOperator())?"":bill.getOperator());
                    data.put("operatorName", StringUtil.isBlank(bill.getOperatorName())?"":bill.getOperatorName());
                    data.put("payUpdateKey", StringUtil.isBlank(bill.getPayUpdateKey())?"":bill.getPayUpdateKey());
                    data.put("qlcOrderState", StringUtil.isBlank(bill.getQlcOrderState())?"":bill.getQlcOrderState());
                    data.put("hisOrderState", StringUtil.isBlank(bill.getHisOrderState())?"":bill.getHisOrderState());
                    /* 用于三方汇总账单的操作按钮*/
                    data.put("operationButton", request.operationButton);
                    /* 操作人列是否展示json*/
                    data.put("operationJson", request.operationJson);

                    array.add(data);
               }
          }
          jsonobject.put("data", array);
          return jsonobject.toString();
     }
     
     /**
      * 获取分类汇总账单数据
      * @param queryBillParam
      * @return String
      */
     @GetMapping(value = "/queryClassifySummaryBills.do",produces = "text/html;charset=UTF-8")
     public String queryClassifySummaryBills(QueryBillParam queryBillParam) {
          PageVo page = new PageVo();
          page.setPCount(new Integer(queryBillParam.getCount()));
          page.setPIndex(new Integer(queryBillParam.getStart()));
          page.setPSize(new Integer(queryBillParam.getLength()));
          PageInfo<ClassifySummaryBalance> classifySummaries = null;
          try {
               classifySummaries = billService.queryClassifySummaryBills(queryBillParam,page);
          } catch (SQLException e) {
               e.printStackTrace();
          }

          JSONObject jsonobject = new JSONObject();
          jsonobject.put("recordsTotal", classifySummaries.getTotal());
          jsonobject.put("recordsFiltered", classifySummaries.getTotal());
          jsonobject.put("draw", queryBillParam.getDraw());
          JSONArray array = new JSONArray();
          if (classifySummaries.getList().size() > 0) {
               for (ClassifySummaryBalance summary : classifySummaries.getList()) {
                    JSONObject data = new JSONObject();
                    data.put("billDate", summary.getBillDate());
                    data.put("channelId", StringUtil.isBlank(summary.getChannelId())?"":summary.getChannelId());
                    data.put("serviceName", StringUtil.isBlank(summary.getServiceName())?"":summary.getServiceName());
                    data.put("hisBills", StringUtil.isBlank(summary.getHisBills())?"":summary.getHisBills());
                    data.put("qlcBills", StringUtil.isBlank(summary.getQlcBills())?"":summary.getQlcBills());
                    data.put("channelBills", StringUtil.isBlank(summary.getChannelBills())?"":summary.getChannelBills());
                    data.put("receivableMoney", ArithUtil.div(summary.getReceivableMoney(),"100",2));
                    data.put("alreadyReceivedMoney",ArithUtil.div(summary.getAlreadyReceivedMoney(),"100",2));
                    data.put("refundMoney", ArithUtil.div(summary.getRefundMoney(),"100",2));
                    data.put("alreadyRefundMoney",ArithUtil.div(summary.getAlreadyRefundMoney(),"100",2));
                    array.add(data);

               }
          }
          jsonobject.put("data", array);
          return jsonobject.toString();
     }



     @PostMapping(value = "/addPendingOrder.do", produces = "application/json;charset=UTF-8")
     public JSONObject addPendingOrder(@RequestParam String bizType, @RequestParam String orderId, @RequestParam String hisOrderId, @RequestParam String channelOrderId){
          String num = billService.addPendingOrder(bizType,orderId,hisOrderId,channelOrderId);
          return XmlParamUtils.documentToJSONObject(num);
     }

     @PostMapping(value = "/basedBizChangesEveryDayBalance.do", produces = "application/json;charset=UTF-8")
     public String basedBizChangesEveryDayBalance(@RequestParam String bizType,@RequestParam String orderId,@RequestParam String hisOrderId,@RequestParam String channelOrderId){
          return billService.basedBizChangesEveryDayBalance(bizType,orderId,hisOrderId,channelOrderId);
     }

     @PostMapping(value = "/getParamState.do", produces = "application/json;charset=UTF-8")
     public R getParamState(){
          return R.ok().put("everyDay", request.everyDayBillIsShow)
                  .put("everyDayDetail", request.everyDayBillDetailIsShow)
                  .put("classifySummary", request.classifySummaryBillIsShow)
                  .put("refund", request.alwaysRefundIsShow);
     }


}

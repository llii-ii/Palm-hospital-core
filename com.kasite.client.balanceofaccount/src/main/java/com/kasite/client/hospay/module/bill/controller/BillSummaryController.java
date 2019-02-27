package com.kasite.client.hospay.module.bill.controller;

import com.alibaba.fastjson.JSONObject;
import com.kasite.client.hospay.module.bill.entity.bill.dto.BillParam;
import com.kasite.client.hospay.module.bill.service.BillSummaryService;
import com.kasite.client.hospay.module.bill.util.XmlParamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cc
 * TODO 账单汇总控制器
 */
@RequestMapping(value = "/billSummaryController")
@RestController
public class BillSummaryController {

     private final static Logger logger = LoggerFactory.getLogger(BillController.class);

     @Autowired
     BillSummaryService billSummary;


     /**
      * 生成全流程每日汇总账单明细
      * 将HTTP请求参数注入到javaBean中，根据属性的前缀进行映射
      */
     @PostMapping(value = "/summary.do", produces = "application/json;charset=UTF-8")
     public JSONObject summaryQLCBalance(BillParam billParam){
          String resp = billSummary.summaryQLCBalance(billParam);
          return XmlParamUtils.documentToJSONObject(resp);
     }

     /**
      * 汇总三方每日账单明细
      * 将HTTP请求参数注入到javaBean中，根据属性的前缀进行映射
      */
     @PostMapping(value = "/three.do", produces = "application/json;charset=UTF-8")
     public JSONObject summaryThreePartyBalance(BillParam billParam) {
          String resp =  billSummary.summaryThreePartyBalance(billParam);
          return XmlParamUtils.documentToJSONObject(resp);
     }

     /**
      * 每日账单汇总
      * 将HTTP请求参数注入到javaBean中，根据属性的前缀进行映射
      */
     @PostMapping(value = "/every.do", produces = "application/json;charset=UTF-8")
     public JSONObject summaryEveryDayBalance(BillParam billParam) {
          String resp = billSummary.summaryEveryDayBalance(billParam);
          return XmlParamUtils.documentToJSONObject(resp);

     }

}

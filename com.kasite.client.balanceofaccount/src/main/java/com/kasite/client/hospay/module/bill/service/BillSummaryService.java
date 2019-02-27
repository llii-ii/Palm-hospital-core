package com.kasite.client.hospay.module.bill.service;

import com.kasite.client.hospay.module.bill.entity.bill.dto.BillParam;

/**
 * @author cc
 * TODO 账单汇总服务接口（账单汇总的接口都在这边编写）
 */
public interface BillSummaryService {
     /**
      * 汇总全流程每日账单明细
      * @param billParam
      * @return
      */
     String summaryQLCBalance(BillParam billParam);

     /**
      * 汇总三方每日账单明细
      * @param billParam
      * @return
      */
     String summaryThreePartyBalance(BillParam billParam);

     /**
      * 汇总每日订单（根据三方订单明细数据汇总）
      * @param billParam
      * @return
      */
     String summaryEveryDayBalance(BillParam billParam);
}

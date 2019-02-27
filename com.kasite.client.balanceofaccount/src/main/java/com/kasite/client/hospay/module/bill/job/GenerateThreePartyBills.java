package com.kasite.client.hospay.module.bill.job;

import com.kasite.client.hospay.module.bill.entity.bill.dto.BillParam;
import com.kasite.client.hospay.module.bill.service.BillSummaryService;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.util.DateOper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;

/**
 * @author cc
 */
@Component
public class GenerateThreePartyBills {
     private final static Logger logger = LoggerFactory.getLogger(GenerateThreePartyBills.class);
     private static boolean flag = true;
     @Autowired
     BillSummaryService billSummary;

     @Autowired
     KasiteConfigMap kasiteConfigMap;

     /**
      * 每日10.35点开始生成三方汇总账单明细数据
      */
     @Scheduled(cron = "0 35 10 * * ? ")
     public void generateThreePartyBill() {
          try {
               if (flag && kasiteConfigMap.isStartJob(this.getClass())) {
                    flag = false;
                    String startDate = DateOper.addDate(DateOper.getNow("yyyy-MM-dd"), -1);
                    String endDate = DateOper.addDate(DateOper.getNow("yyyy-MM-dd"), -1);
                    BillParam billParam = new BillParam();
                    billParam.setStartDate(startDate);
                    billParam.setEndDate(endDate);
                    logger.info("开始下载三方汇总账单明细数据");
                    billSummary.summaryThreePartyBalance(billParam);
                    logger.info("下载三方汇总账单明细数据结束");
               }
          } catch (ParseException e) {
               e.printStackTrace();
               logger.info("获取格式化日期异常:{}", e.getMessage());
          } finally {
               flag = true;
          }

     }


}

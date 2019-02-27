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
 * 生成全流程账单明细数据
 */
@Component
public class GenerateQlcBills {
     private final static Logger logger = LoggerFactory.getLogger(GenerateQlcBills.class);
     private static boolean flag = true;
     @Autowired
     BillSummaryService billSummary;

     @Autowired
     KasiteConfigMap kasiteConfigMap;

     /**
      * 由于全流程汇总时使用到了P_BILL表的数据进行校准,所以全流程汇总的作业应该晚于微信账单的下载
      */
     @Scheduled(cron = "0 20 10 * * ?")
     public void generateQLCBill() {
          try {
               if (flag && kasiteConfigMap.isStartJob(this.getClass())) {
                    flag = false;
                    String startDate = DateOper.addDate(DateOper.getNow("yyyy-MM-dd"), -1);
                    String endDate = DateOper.addDate(DateOper.getNow("yyyy-MM-dd"), -1);
                    BillParam billParam = new BillParam();
                    billParam.setStartDate(startDate);
                    billParam.setEndDate(endDate);
                    logger.info("开始下载全流程账单");
                    billSummary.summaryQLCBalance(billParam);
                    logger.info("下载全流程账单结束");
               }
          } catch (ParseException e) {
               e.printStackTrace();
               logger.info("获取格式化日期异常:{}", e.getMessage());
          }finally {
               flag = true;
          }

     }


}

package com.kasite.client.hospay.module.bill.job;


import com.kasite.client.hospay.module.bill.service.DownloadBillService;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.util.DateOper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;

/**
 * 下载HIS账单
 *
 * @author cc
 */
@Component
public class DownloadHisBills {

     private final static Logger logger = LoggerFactory.getLogger(DownloadHisBills.class);
     private static boolean flag = true;
     @Autowired
     DownloadBillService downloadBillService;

     @Autowired
     KasiteConfigMap kasiteConfigMap;

     /**
      * 每日凌晨3点下载HIS账单
      */
     @Scheduled(cron = "0 0 3 * * ? ")
     public void downloadHisBill() {
          try {
               if (flag && kasiteConfigMap.isStartJob(this.getClass())) {
                    flag = false;
                    String startDate = DateOper.addDate(DateOper.getNow("yyyy-MM-dd"), -1);
                    String endDate = DateOper.addDate(DateOper.getNow("yyyy-MM-dd"), -1);

                    logger.info("开始下载HIS账单");
                    downloadBillService.downloadHisBill(startDate+" 00:00:00",endDate+" 23:59:59");
                    logger.info("下载HIS账单结束");
               }
          } catch (ParseException e) {
               e.printStackTrace();
               logger.info("获取格式化日期异常:{}", e.getMessage());
          } finally {
               flag = true;
          }
     }

     public static void main(String[] args) {
          DownloadHisBills downloadHisBill = new DownloadHisBills();
          downloadHisBill.downloadHisBill();
     }
}

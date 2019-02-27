package com.kasite.client.hospay.module.bill.service.impl;

import com.kasite.client.hospay.common.constant.Constant;
import com.kasite.client.hospay.module.bill.dao.DownloadBillExcelDao;
import com.kasite.client.hospay.module.bill.dao.PayDao;
import com.kasite.client.hospay.module.bill.dao.ThreePartyBalanceDao;
import com.kasite.client.hospay.module.bill.entity.bill.bo.*;
import com.kasite.client.hospay.module.bill.entity.bill.dto.BillParam;
import com.kasite.client.hospay.module.bill.service.DownloadBillService;
import com.kasite.client.hospay.module.bill.util.BillUtils;
import com.kasite.client.hospay.module.bill.util.CommonUtils;
import com.kasite.core.common.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author cc
 * TODO 下载账单业务实现类（跟账单下载业务有关的实现都在写在这边）
 */
@Service
public class DownloadBillServiceImpl implements DownloadBillService {

     private final static Logger logger = LoggerFactory.getLogger(DownloadBillServiceImpl.class);

     @Autowired
     BillUtils billUtils;

     @Autowired
     CommonUtils commonUtils;

     @Autowired
     ThreePartyBalanceDao threePartyBalanceDao;

     @Autowired
     DownloadBillExcelDao downloadBillExcelDao;

     @Autowired
     PayDao payDao;

     @Autowired
     RequestHandlerParam requestHandlerParam;

     @SuppressWarnings("unchecked")
	 @Override
     public String downloadHisBill(String startDate, String endDate){
          try {
               logger.info("开始调用HIS账单下载接口,当前HIS的具体实现类是:{},实现方式为:{}",billUtils.getHisInstance(),requestHandlerParam.hisType);
               List<HisBill> hisBills = null;
               if (Constant.SOAP.equals(requestHandlerParam.hisType)) {
                    hisBills = billUtils.getHisInstance().queryHisOrderBillListSoap(startDate,endDate);
               }else if (Constant.HL7.equals(requestHandlerParam.hisType)){
                    hisBills = billUtils.getHisInstance().queryHisOrderBillListHL7(startDate,endDate);
               }

               logger.info("HIS账单去重开始");
               payDao.deleteOldHisBillByTransDate(startDate, endDate);
               logger.info("HIS账单去重结束！！！");

               if (hisBills != null && !hisBills.isEmpty()) {
                    logger.info("调用HIS账单下载接口结束,总条数是:{}",hisBills.size());
                    /* 3.为了防止查询数据量太大，导致mybatis拼接的SQL过长数据库连接过久，所以这边统一采取分页插入*/
                    Boolean flag = true;
                    int i = 0;
                    int pageSize = 500;
                    while (flag) {
                         i = i + 1;
                         Map<String, Object> resultMap = commonUtils.getPagingResultMap(hisBills, i, pageSize);
                         flag = (Boolean) resultMap.get("flag");
                         List<HisBill> newHisBillList = (List<HisBill>) resultMap.get("result");
                         logger.info("分页查询成功,当前页数是:{},分页后实际获取的条数是:{}", i, newHisBillList.size());
                         if (!newHisBillList.isEmpty()) {
                              logger.info("开始保存HIS账单数据,当前页数是:{},总条数是:{}", i, newHisBillList.size());
                              payDao.insertHisBill(newHisBillList);
                              logger.info("保存HIS账单数据结束！！！");
                         }
                    }
                    return CommonUtil.getRetVal(null, "1002", Constant.RET_INT10000, "下载HIS账单成功");
               }
               return CommonUtil.getRetVal(null,"1002", Constant.RET_INT10000,"下载HIS账单数据为空");
          } catch (Exception e) {
               e.printStackTrace();
               logger.error(e.getMessage());
               return CommonUtil.getRetVal(null,"1002", Constant.FAIL_INT10000,e.getMessage());
          }

     }



     @Override
     public List<ThreeEveryDayBillsExcel> downloadEveryDayBillExcel(String startDate, String endDate, String checkState) throws SQLException {
          List<ThreeEveryDayBillsExcel> everyDayBills = downloadBillExcelDao.downloadEveryDayBillsExcel(startDate, endDate, checkState);
          if (everyDayBills.size() == 0) {
               logger.info("获取每日汇总账单数据为零");
          }
          logger.info("开始返回数据！！！");
          return everyDayBills;
     }

     @Override
     public List<ThreePartyBillsExcel> downloadThreePartyBillExcel(BillParam billParam) throws SQLException {
          List<ThreePartyBillsExcel> everyDayBills = downloadBillExcelDao.downloadThreePartyBillsExcel(billParam);
          if (everyDayBills.size() == 0) {
               logger.info("获取每日汇总账单数据为零");
          }
          logger.info("开始返回数据！！！");
          return everyDayBills;
     }

     @Override
     public List<ThreeClassifySummaryExcel> downloadClassifySummaryBillExcel(BillParam billParam) throws SQLException {
          List<ThreeClassifySummaryExcel> threeClassifySummaryExcels = downloadBillExcelDao.downloadClassifySummaryBillExcel(billParam);
          if (threeClassifySummaryExcels.size() == 0) {
               logger.info("获取每日汇总账单数据为零！！");
          }
          logger.info("开始返回数据！！！");
          return threeClassifySummaryExcels;
     }
}

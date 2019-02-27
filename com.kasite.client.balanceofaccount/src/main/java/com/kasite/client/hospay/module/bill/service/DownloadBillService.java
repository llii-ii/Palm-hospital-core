package com.kasite.client.hospay.module.bill.service;

import java.sql.SQLException;
import java.util.List;

import com.kasite.client.hospay.module.bill.entity.bill.bo.ThreeClassifySummaryExcel;
import com.kasite.client.hospay.module.bill.entity.bill.bo.ThreeEveryDayBillsExcel;
import com.kasite.client.hospay.module.bill.entity.bill.bo.ThreePartyBillsExcel;
import com.kasite.client.hospay.module.bill.entity.bill.dto.BillParam;
import com.kasite.core.common.exception.ParamException;

/**
 * @author cc
 * TODO 账单下载接口（下载账单报表的接口都可以在这边编写）
 */
public interface DownloadBillService {


     /**
      * 下载HIS账单
      * @param startDate
      * @param endDate
      * @return
      * @throws ParamException
      */
     String downloadHisBill(String startDate, String endDate) throws ParamException;

     
     /**
      * 下载每日汇总账单报表
      * @param startDate
      * @param endDate
      * @param checkState
      * @return
      * @throws SQLException
      */
     List<ThreeEveryDayBillsExcel> downloadEveryDayBillExcel(String startDate, String endDate, String checkState) throws SQLException;

     /**
      * 下载三方汇总账单明细报表
      * @param billParam
      * @return
      * @throws SQLException
      */
     List<ThreePartyBillsExcel> downloadThreePartyBillExcel(BillParam billParam) throws SQLException;

     /**
      * 下载分类汇总账单报表
      * @param billParam
      * @return
      * @throws SQLException
      */
     List<ThreeClassifySummaryExcel> downloadClassifySummaryBillExcel(BillParam billParam) throws SQLException;

}

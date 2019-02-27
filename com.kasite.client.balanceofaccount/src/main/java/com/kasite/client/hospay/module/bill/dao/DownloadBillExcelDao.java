package com.kasite.client.hospay.module.bill.dao;

import com.kasite.client.hospay.module.bill.entity.bill.bo.ThreeClassifySummaryExcel;
import com.kasite.client.hospay.module.bill.entity.bill.bo.ThreeEveryDayBillsExcel;
import com.kasite.client.hospay.module.bill.entity.bill.bo.ThreePartyBillsExcel;
import com.kasite.client.hospay.module.bill.entity.bill.dto.BillParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cc
 * TODO 账单下载相关SQL方法说明
 */
public interface DownloadBillExcelDao {
     /**
      * 下载每日汇总账单报表
      * @param startDate
      * @param endDate
      * @param checkState
      * @return
      */
     List<ThreeEveryDayBillsExcel> downloadEveryDayBillsExcel(@Param("startDate") String startDate, @Param("endDate")  String endDate, @Param("checkState")  String checkState);

     /**
      * 下载三方汇总账单明细报表
      * @param billParam
      * @return
      */
     List<ThreePartyBillsExcel> downloadThreePartyBillsExcel(@Param("billParam") BillParam billParam);

     /**
      * 下载分类汇总账单报表
      * @param billParam
      * @return
      */
     List<ThreeClassifySummaryExcel> downloadClassifySummaryBillExcel(@Param("billParam") BillParam billParam);
}

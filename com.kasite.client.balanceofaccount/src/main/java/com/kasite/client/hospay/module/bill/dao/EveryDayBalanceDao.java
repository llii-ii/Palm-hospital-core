package com.kasite.client.hospay.module.bill.dao;

import com.kasite.client.hospay.module.bill.entity.bill.dbo.ClassifySummaryBalance;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.EveryDayBalance;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.ThreePartyBalance;
import com.kasite.client.hospay.module.bill.entity.bill.dto.QueryBillParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cc
 * TODO 每日汇总账单SQL方法说明
 */
public interface EveryDayBalanceDao {
     /**
      * 查询三方汇总账单明细表指定日期内的数据
      * @param startDate
      * @param endDate
      * @return
      */
     List<ThreePartyBalance> queryThreePartyBalance(@Param("startDate") String startDate, @Param("endDate") String endDate);

     /**
      * 查询每日汇总账单表里的明细总数
      * @param startDate
      * @param endDate
      * @return
      */
     Integer queryEveryDayBillCount(@Param("startDate") String startDate, @Param("endDate") String endDate);

     /**
      * 生成每日汇总账单数据
      * @param startDate
      * @param endDate
      * @return
      */
     List<EveryDayBalance> summaryEveryDayBill(@Param("startDate") String startDate, @Param("endDate") String endDate);

     /**
      * 每日汇总账单数据去重
      * @param startDate
      * @param endDate
      */
     void deleteEveryDayBalance(@Param("startDate") String startDate, @Param("endDate") String endDate);

     /**
      * 查询当前日期内是否还存在异常账单（用于汇总每日账单）
      * @param startDate
      * @param endDate
      * @return
      *
      */
     Integer queryVeryDayExitsErrorBill(@Param("startDate") String startDate, @Param("endDate") String endDate);

     /**
      * 保存每日汇总账单数据
      * @param everyDayBalance
      */
     void insertEveryDayBill(@Param ("everyDayBalance") EveryDayBalance everyDayBalance);

     /**
      * 生成每日分类汇总账单明细数据 根据渠道ID和服务ID进行分组
      * @param startDate
      * @param endDate
      * @return
      */
     List<ClassifySummaryBalance> classifySummaryBill(@Param("startDate") String startDate, @Param("endDate") String endDate);

     /**
      * 删除分类汇总账单数据（去重）
      * @param startDate
      * @param endDate
      */
     void deleteClassifySummaryBalance(@Param("startDate") String startDate, @Param("endDate") String endDate);

     /**
      * 生成指定日期内的分类汇总数据
      * @param startDate
      * @param endDate
      * @return
      */
     ClassifySummaryBalance summaryClassifyBills(@Param("startDate") String startDate, @Param("endDate") String endDate);

     /**
      * 保存每日分类汇总账单数据
      * @param classifySummaryBalance
      */
     void insertClassifySummaryBill(@Param("classifySummaryBalance") ClassifySummaryBalance classifySummaryBalance);

     /**
      * 查询每日汇总账单数据
      * @param queryBillParam
      * @return
      */
     List<EveryDayBalance> queryEveryDayBills(@Param ("queryBillParam") QueryBillParam queryBillParam);

     /**
      * 查询每日汇总分类账单数据
      * @param queryBillParam
      * @return
      */
     List<ClassifySummaryBalance> queryClassifySummaryBills(@Param ("queryBillParam") QueryBillParam queryBillParam);

}

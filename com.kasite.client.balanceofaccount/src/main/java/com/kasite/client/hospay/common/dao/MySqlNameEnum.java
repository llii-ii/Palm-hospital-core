package com.kasite.client.hospay.common.dao;

/**
 * @author cc
 */
public enum MySqlNameEnum{


     
     //*********************************************************
     /**
      * 删除指定日期内的账单数据（去重）
      */
     deleteHisBillByTransDate,
     /**
      * 汇总全流程每日账单明细
      */
     summaryQLCBalance,
     /**
      * 删除指定日期内的全流程账单明细数据（去重）
      */
     deleteQLCBalance,

     //**********************************************************
     /**
      * 生成三方汇总账单明细
      */
     summaryThreePartyBalance,
     /**
      * 删除指定日期内的三方汇总账单明细数据（去重）
      */
     deleteThreePartyBalance,
     /**
      * 获取三方每日汇总账单明细数据
      */
     queryThreePartyDetailBills,

     //**********************************************************
     /**
      * 获取三方汇总账单明细数据（用于生成每日汇总账单数据）
      */
     queryThreePartyBalance,
     /**
      * 生成每日汇总账单数据
      */
     summaryEveryDayBalance,
     /**
      * 删除指定日期内的每日账单数据（去重）
      */
     deleteEveryDayBalance,
     /**
      * 获取每日汇总账单数据
      */
     queryEveryDayBills,

     ;


}

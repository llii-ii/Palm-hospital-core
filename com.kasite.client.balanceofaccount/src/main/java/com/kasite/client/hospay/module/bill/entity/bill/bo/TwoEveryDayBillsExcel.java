package com.kasite.client.hospay.module.bill.entity.bill.bo;


import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

/**
 * @author cc
 * 每日汇总账单报表
 */

@ExcelTarget(value = "TwoEveryDayBillsExcel")
public class TwoEveryDayBillsExcel {

     /** 账单日期*/
     @Excel(name = "对账日期", orderNum = "1",isImportField = "billDate",width = 15)
     private String billDate;
     /** 全流程账单笔数*/
     @Excel(name = "全流程账单笔数", orderNum = "3",isImportField = "qlcBills",isStatistics = true,width = 15)
     private String qlcBills;
     /** 渠道（微信/支付宝）账单笔数*/
     @Excel(name = "渠道账单笔数", orderNum = "4",isImportField = "channelBills",isStatistics = true,width = 15)
     private String channelBills;
     /** 已核对笔数*/
     @Excel(name = "已核对笔数", orderNum = "5",isImportField = "checkNum",width = 15)
     private String checkNum;
     /** 异常笔数*/
     @Excel(name = "异常笔数", orderNum = "6",isImportField = "abnormalNum",isStatistics = true,width = 15)
     private String abnormalNum;
     /** 剩余异常笔数*/
     @Excel(name = "剩余异常笔数", orderNum = "7",isImportField = "overPlusErrNum",isStatistics = true,width = 15)
     private String overPlusErrNum;
     /** 应收金额*/
     @Excel(name = "应收款（元）", orderNum = "8",isImportField = "receivableMoney",isStatistics = true,width = 15)
     private String receivableMoney;
     /** 已收金额*/
     @Excel(name = "已收款（元）", orderNum = "9", isImportField = "alreadyReceivedMoney",isStatistics = true,width = 15)
     private String alreadyReceivedMoney;

     /** 应收金额*/
     @Excel(name = "应退金额（元）", orderNum = "10",isImportField = "refundMoney",isStatistics = true,width = 15)
     private String refundMoney;
     /** 已收金额*/
     @Excel(name = "实退金额（元）", orderNum = "11", isImportField = "alreadyRefundMoney",isStatistics = true,width = 15)
     private String alreadyRefundMoney;

     /** 核对结果*/
     @Excel(name = "对账结果", replace ={"账不平_-1","账平_1","账平(处置后)_2"},orderNum = "12",isImportField = "checkResult",width = 15)
     private String checkResult;

     public TwoEveryDayBillsExcel(String billDate, String qlcBills, String channelBills, String checkNum, String abnormalNum, String overPlusErrNum,String receivableMoney, String alreadyReceivedMoney, String refundMoney, String alreadyRefundMoney, String checkResult) {
          this.billDate = billDate;
          this.qlcBills = qlcBills;
          this.channelBills = channelBills;
          this.checkNum = checkNum;
          this.abnormalNum = abnormalNum;
          this.overPlusErrNum = overPlusErrNum;
          this.receivableMoney = receivableMoney;
          this.alreadyReceivedMoney = alreadyReceivedMoney;
          this.refundMoney = refundMoney;
          this.alreadyRefundMoney = alreadyRefundMoney;
          this.checkResult = checkResult;
     }

     public String getBillDate() {
          return billDate;
     }

     public void setBillDate(String billDate) {
          this.billDate = billDate;
     }

     public String getQlcBills() {
          return qlcBills;
     }

     public void setQlcBills(String qlcBills) {
          this.qlcBills = qlcBills;
     }

     public String getChannelBills() {
          return channelBills;
     }

     public void setChannelBills(String channelBills) {
          this.channelBills = channelBills;
     }

     public String getCheckNum() {
          return checkNum;
     }

     public void setCheckNum(String checkNum) {
          this.checkNum = checkNum;
     }

     public String getAbnormalNum() {
          return abnormalNum;
     }

     public void setAbnormalNum(String abnormalNum) {
          this.abnormalNum = abnormalNum;
     }

     public String getOverPlusErrNum() {
          return overPlusErrNum;
     }

     public void setOverPlusErrNum(String overPlusErrNum) {
          this.overPlusErrNum = overPlusErrNum;
     }

     public String getReceivableMoney() {
          return receivableMoney;
     }

     public void setReceivableMoney(String receivableMoney) {
          this.receivableMoney = receivableMoney;
     }

     public String getAlreadyReceivedMoney() {
          return alreadyReceivedMoney;
     }

     public void setAlreadyReceivedMoney(String alreadyReceivedMoney) {
          this.alreadyReceivedMoney = alreadyReceivedMoney;
     }

     public String getRefundMoney() {
          return refundMoney;
     }

     public void setRefundMoney(String refundMoney) {
          this.refundMoney = refundMoney;
     }

     public String getAlreadyRefundMoney() {
          return alreadyRefundMoney;
     }

     public void setAlreadyRefundMoney(String alreadyRefundMoney) {
          this.alreadyRefundMoney = alreadyRefundMoney;
     }


     public String getCheckResult() {
          return checkResult;
     }

     public void setCheckResult(String checkResult) {
          this.checkResult = checkResult;
     }
}

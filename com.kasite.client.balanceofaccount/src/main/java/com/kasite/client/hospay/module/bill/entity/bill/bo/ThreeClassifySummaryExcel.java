package com.kasite.client.hospay.module.bill.entity.bill.bo;

import com.coreframework.util.ArithUtil;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;


/**
 * @author cc
 * TODO 分类汇总实体类
 */
@ExcelTarget(value = "ThreeClassifySummaryExcel")
public class ThreeClassifySummaryExcel {


     /** 账单日期*/
     @Excel(name = "对账日期", orderNum = "1",isImportField = "billDate",width = 15)
     private String billDate;
     /** 渠道ID*/
     @Excel(name = "渠道ID", replace ={"微信_100123","支付宝_100125"},orderNum = "2",isImportField = "channelId",width = 15)
     private String channelId;
     /** 服务名称*/
     @Excel(name = "服务名称", replace ={"挂号支付_0","当日挂号支付_006","医嘱支付_999","当面付_008","住院支付_007","病历复印_010"},orderNum = "3",isImportField = "serviceName",width = 15)
     private String serviceName;
     /** HIS账单笔数*/
     @Excel(name = "HIS账单笔数", orderNum = "4",isImportField = "hisBills",isStatistics = true,width = 15)
     private String hisBills;
     /** 全流程账单笔数*/
     @Excel(name = "全流程账单笔数", orderNum = "5",isImportField = "qlcBills",isStatistics = true,width = 15)
     private String qlcBills;
     /** 渠道（微信/支付宝）账单笔数*/
     @Excel(name = "渠道账单笔数", orderNum = "6",isImportField = "channelBills",isStatistics = true,width = 15)
     private String channelBills;
     /** 应收金额*/
     @Excel(name = "应收款（元）", orderNum = "7",isImportField = "receivableMoney",isStatistics = true,width = 15)
     private String receivableMoney;
     /** 已收金额*/
     @Excel(name = "已收款（元）", orderNum = "8", isImportField = "alreadyReceivedMoney",isStatistics = true,width = 15)
     private String alreadyReceivedMoney;

     /** 应收金额*/
     @Excel(name = "应退金额（元）", orderNum = "9",isImportField = "refundMoney",isStatistics = true,width = 15)
     private String refundMoney;
     /** 已收金额*/
     @Excel(name = "实退金额（元）", orderNum = "10", isImportField = "alreadyRefundMoney",isStatistics = true,width = 15)
     private String alreadyRefundMoney;

     public ThreeClassifySummaryExcel(String billDate, String channelId, String serviceId, String serviceName, String hisBills, String qlcBills, String channelBills, String receivableMoney, String alreadyReceivedMoney, String refundMoney, String alreadyRefundMoney) {
          this.billDate = billDate;
          this.channelId = channelId;
          this.serviceName = serviceName;
          this.hisBills = hisBills;
          this.qlcBills = qlcBills;
          this.channelBills = channelBills;
          this.receivableMoney = receivableMoney;
          this.alreadyReceivedMoney = alreadyReceivedMoney;
          this.refundMoney = refundMoney;
          this.alreadyRefundMoney = alreadyRefundMoney;
     }

     public String getBillDate() {
          return billDate;
     }

     public void setBillDate(String billDate) {
          this.billDate = billDate;
     }

     public String getChannelId() {
          return channelId;
     }

     public void setChannelId(String channelId) {
          this.channelId = channelId;
     }

     public String getServiceName() {
          return serviceName;
     }

     public void setServiceName(String serviceName) {
          this.serviceName = serviceName;
     }

     public String getHisBills() {
          return hisBills;
     }

     public void setHisBills(String hisBills) {
          this.hisBills = hisBills;
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

     public String getReceivableMoney() {
          return ArithUtil.div(receivableMoney, "100", 2);
     }

     public void setReceivableMoney(String receivableMoney) {
          this.receivableMoney = receivableMoney;
     }

     public String getAlreadyReceivedMoney() {
          return ArithUtil.div(alreadyReceivedMoney, "100", 2);
     }

     public void setAlreadyReceivedMoney(String alreadyReceivedMoney) {
          this.alreadyReceivedMoney = alreadyReceivedMoney;
     }

     public String getRefundMoney() {
          return ArithUtil.div(refundMoney, "100", 2);
     }

     public void setRefundMoney(String refundMoney) {
          this.refundMoney = refundMoney;
     }

     public String getAlreadyRefundMoney() {
          return ArithUtil.div(alreadyRefundMoney, "100", 2);
     }

     public void setAlreadyRefundMoney(String alreadyRefundMoney) {
          this.alreadyRefundMoney = alreadyRefundMoney;
     }
}

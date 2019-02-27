package com.kasite.client.hospay.module.bill.entity.bill.dbo;

import java.sql.Timestamp;

/**
 * @author cc
 * TODO 分类汇总实体类
 */
public class ClassifySummaryBalance {

     /** 账单日期*/
     private String billDate;
     /** 渠道ID*/
     private String channelId;
     /** 服务ID*/
     private String serviceId;
     /** 服务名称*/
     private String serviceName;
     /** HIS账单笔数 */
     private String hisBills;
     /** 全流程账单笔数 */
     private String qlcBills;
     /** 渠道（微信/支付宝）账单笔数 */
     private String channelBills;
     /** 应收费金额 */
     private String receivableMoney;
     /** 实收费金额 */
     private String alreadyReceivedMoney;
     /** 应退金额 */
     private String refundMoney;
     /** 实退金额 */
     private String alreadyRefundMoney;
     /** 创建时间*/
     private Timestamp createDate;
     /** 父服务id*/
     private String parenServiceId;

     public String getParenServiceId() {
          return parenServiceId;
     }

     public void setParenServiceId(String parenServiceId) {
          this.parenServiceId = parenServiceId;
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

     public String getServiceId() {
          return serviceId;
     }

     public void setServiceId(String serviceId) {
          this.serviceId = serviceId;
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
          return receivableMoney==null?"0":receivableMoney;
     }

     public void setReceivableMoney(String receivableMoney) {
          this.receivableMoney = (receivableMoney==null?"0":receivableMoney);
     }

     public String getAlreadyReceivedMoney() {
          return alreadyReceivedMoney==null?"0":alreadyReceivedMoney;
     }

     public void setAlreadyReceivedMoney(String alreadyReceivedMoney) {
          this.alreadyReceivedMoney = (alreadyReceivedMoney==null?"0":alreadyReceivedMoney);
     }

     public String getRefundMoney() {
          return refundMoney==null?"0":refundMoney;
     }

     public void setRefundMoney(String refundMoney) {
          this.refundMoney = refundMoney==null?"0":refundMoney;
     }

     public String getAlreadyRefundMoney() {
          return alreadyRefundMoney==null?"0":alreadyRefundMoney;
     }

     public void setAlreadyRefundMoney(String alreadyRefundMoney) {
          this.alreadyRefundMoney = (alreadyRefundMoney==null?"0":alreadyRefundMoney);
     }
     public Timestamp getCreateDate() {
          return createDate;
     }

     public void setCreateDate(Timestamp createDate) {
          this.createDate = createDate;
     }
}

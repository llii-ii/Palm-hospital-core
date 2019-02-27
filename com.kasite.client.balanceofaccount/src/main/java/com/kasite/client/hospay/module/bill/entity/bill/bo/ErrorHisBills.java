package com.kasite.client.hospay.module.bill.entity.bill.bo;

import java.sql.Timestamp;

/**
 * @author cc
 * TODO 与HIS账单有差异的订单实体类
 */
public class ErrorHisBills {
     /**
      *全流程订单号
      */
     private String orderId;
     /**
      * HIS订单号
      */
     private String hisOrderId;
     /**
      * 订单类型 1.支付订单 2.退款订单
      */
     private String orderType;
     /**
      * 交易流水号
      */
     private String payUpdateKey;
     /**
      * 订单创建时间
      */
     private Timestamp createDate;
     /**
      * 服务ID
      */
     private String serviceId;
     /**
      * 保留字段
      */
     private String reserveId;
     /**
      * 交易名称
      */
     private String priceName;
     /**
      * 应收金额
      */
     private String receivableMoney;
     /**
      * 订单描述
      */
     private String orderMemo;
     /**
      * 卡号
      */
     private String cardNo;
     /**
      * 操作ID
      */
     private String operator;
     /**
      * 操作人名称
      */
     private String operatorName;
     /**
      * 渠道号
      */
     private String channelId;


     public String getOrderId() {
          return orderId;
     }

     public void setOrderId(String orderId) {
          this.orderId = orderId;
     }

     public String getHisOrderId() {
          return hisOrderId;
     }

     public void setHisOrderId(String hisOrderId) {
          this.hisOrderId = hisOrderId;
     }

     public String getOrderType() {
          return orderType;
     }

     public void setOrderType(String orderType) {
          this.orderType = orderType;
     }

     public String getPayUpdateKey() {
          return payUpdateKey;
     }

     public void setPayUpdateKey(String payUpdateKey) {
          this.payUpdateKey = payUpdateKey;
     }

     public Timestamp getCreateDate() {
          return createDate;
     }

     public void setCreateDate(Timestamp createDate) {
          this.createDate = createDate;
     }

     public String getServiceId() {
          return serviceId;
     }

     public void setServiceId(String serviceId) {
          this.serviceId = serviceId;
     }

     public String getReserveId() {
          return reserveId;
     }

     public void setReserveId(String reserveId) {
          this.reserveId = reserveId;
     }

     public String getPriceName() {
          return priceName;
     }

     public void setPriceName(String priceName) {
          this.priceName = priceName;
     }

     public String getReceivableMoney() {
          return receivableMoney;
     }

     public void setReceivableMoney(String receivableMoney) {
          this.receivableMoney = receivableMoney;
     }

     public String getOrderMemo() {
          return orderMemo;
     }

     public void setOrderMemo(String orderMemo) {
          this.orderMemo = orderMemo;
     }

     public String getCardNo() {
          return cardNo;
     }

     public void setCardNo(String cardNo) {
          this.cardNo = cardNo;
     }

     public String getOperator() {
          return operator;
     }

     public void setOperator(String operator) {
          this.operator = operator;
     }

     public String getOperatorName() {
          return operatorName;
     }

     public void setOperatorName(String operatorName) {
          this.operatorName = operatorName;
     }

     public String getChannelId() {
          return channelId;
     }

     public void setChannelId(String channelId) {
          this.channelId = channelId;
     }
}

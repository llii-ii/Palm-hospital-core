package com.kasite.client.hospay.module.bill.entity.bill.dto;

/**
 * @author cc
 * TODO Bill控制器请求入参
 */
public class BillParam {

     /** 开始时间*/
     private String startDate;
     /** 结束时间*/
     private String endDate;
     /** 渠道订单类型（全流程，微信/支付宝，his）*/
     private String channelOrderType;
     /** 对应订单号*/
     private String orderId;
     /** 订单类型*/
     private String orderType;
     /** 检验状态*/
     private String checkState;
     /** 执行状态*/
     private String exeState;
     /** 结果集状态码,因为后续的订单会涉及到业务处理，所以这边需要新增这个code来更新每日汇总状态中的checkResult*/
     private String resultCode;
     /** 渠道号(微信/支付宝)*/
     private String channelId;
     /** 服务ID*/
     private String serviceId;
     /** 异常状态*/
     private String errorState;

     public String getErrorState() {
          return errorState;
     }

     public void setErrorState(String errorState) {
          this.errorState = errorState;
     }

     public String getServiceId() {
          return serviceId;
     }

     public void setServiceId(String serviceId) {
          this.serviceId = serviceId;
     }

     public String getChannelOrderType() {
          return channelOrderType;
     }

     public void setChannelOrderType(String channelOrderType) {
          this.channelOrderType = channelOrderType;
     }

     public String getOrderId() {
          return orderId;
     }

     public void setOrderId(String orderId) {
          this.orderId = orderId;
     }

     public String getOrderType() {
          return orderType;
     }

     public void setOrderType(String orderType) {
          this.orderType = orderType;
     }

     public String getCheckState() {
          return checkState;
     }

     public void setCheckState(String checkState) {
          this.checkState = checkState;
     }

     public String getExeState() {
          return exeState;
     }

     public void setExeState(String exeState) {
          this.exeState = exeState;
     }

     public String getStartDate() {
          return startDate;
     }

     public void setStartDate(String startDate) {
          this.startDate = startDate+" 00:00:00";
     }

     public String getEndDate() {
          return endDate;
     }

     public void setEndDate(String endDate) {
          this.endDate = endDate+" 23:59:59";
     }

     public String getResultCode() {
          return resultCode;
     }

     public void setResultCode(String resultCode) {
          this.resultCode = resultCode;
     }

     public String getChannelId() {
          return channelId;
     }

     public void setChannelId(String channelId) {
          this.channelId = channelId;
     }
}

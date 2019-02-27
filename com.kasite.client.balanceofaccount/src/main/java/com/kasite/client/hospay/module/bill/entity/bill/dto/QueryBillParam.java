package com.kasite.client.hospay.module.bill.entity.bill.dto;

/**
 * @author cc
 * 查询账单数据入参
 */
public class QueryBillParam {
     /** pIndex*/
     private String start;
     /** pSize*/
     private String length;
     /** pCount*/
     private String count;
     /** 开始时间*/
     private String startDate;
     /** 结束时间*/
     private String endDate;
     /** 渠道号(微信/支付宝)*/
     private String channelId;
     /** 渠道订单类型（全流程，微信/支付宝，his）*/
     private String channelOrderType;
     /** 服务ID*/
     private String serviceId;
     /** 对应订单号*/
     private String orderId;
     /** 订单类型*/
     private String orderType;
     /** 检验状态*/
     private String checkState;
     /** 异常状态*/
     private String errorState;
     /** 执行状态状态*/
     private String exeState;
     /** dataTable自带函数请求次数*/
     private String draw;

     public String getServiceId() {
          return serviceId;
     }

     public void setServiceId(String serviceId) {
          this.serviceId = serviceId;
     }

     public String getErrorState() {
          return errorState;
     }

     public void setErrorState(String errorState) {
          this.errorState = errorState;
     }

     public String getStart() {
          return start;
     }

     public void setStart(String start) {
          this.start = start;
     }

     public String getLength() {
          return length;
     }

     public void setLength(String length) {
          this.length = length;
     }

     public String getCount() {
          return count;
     }

     public void setCount(String count) {
          this.count = count;
     }

     public String getStartDate() {
          return startDate+" 00:00:00";
     }

     public void setStartDate(String startDate) {
          this.startDate = startDate;
     }

     public String getEndDate() {
          return endDate+" 23:59:59";
     }

     public void setEndDate(String endDate) {
          this.endDate = endDate;
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

     public String getChannelId() {
          return channelId;
     }

     public void setChannelId(String channelId) {
          this.channelId = channelId;
     }

     public String getOrderType() {
          return orderType;
     }

     public void setOrderType(String orderType) {
          this.orderType = orderType;
     }

     public String getOrderId() {
          return orderId;
     }

     public void setOrderId(String orderId) {
          this.orderId = orderId;
     }
     

	public String getChannelOrderType() {
		return channelOrderType;
	}

	public void setChannelOrderType(String channelOrderType) {
		this.channelOrderType = channelOrderType;
	}

	public String getDraw() {
          return draw;
     }

     public void setDraw(String draw) {
          this.draw = draw;
     }
}

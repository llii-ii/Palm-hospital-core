package com.kasite.client.hospay.module.bill.entity.bill.bo;

import com.coreframework.util.ArithUtil;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;


/**
 * @author cc
 */
@ExcelTarget(value = "ThreePartyBillsExcel")
public class ThreePartyBillsExcel {

     /** 订单类型 */
     @Excel(name = "订单类型", replace = {"支付订单_1","退款订单_2"},orderNum = "1",isImportField = "qlcOrderState",width = 15)
     private String qlcOrderState;

     /** 全流程订单ID */
     @Excel(name = "全流程订单号", orderNum = "2",isImportField = "orderId",width = 40)
     private String orderId;

     /** HIS订单ID */
     @Excel(name = "His订单号", orderNum = "3", isImportField = "hisOrderId",width = 40)
     private String hisOrderId;

     /** 渠道订单ID */
     @Excel(name = "渠道订单号", orderNum = "4", isImportField = "channelOrderId",width = 40)
     private String channelOrderId;

     /** 订单创建时间*/
     @Excel(name = "订单创建时间", orderNum = "5",isImportField = "createDate",width = 35)
     private String createDate;

     /** 订单最后操作时间 */
     @Excel(name = "订单最后操作时间", orderNum = "6",isImportField = "lastDate",width = 35)
     private String lastDate;

     /** 收费项目名*/
     @Excel(name = "服务内容", orderNum = "7",isImportField = "priceName",width = 15)
     private String priceName;

     /** 操作人姓名*/
     @Excel(name = "操作人", orderNum = "8",isImportField = "operatorName",width = 15)
     private String operatorName;

     /** 应收费金额*/
     @Excel(name = "应收金额（元）", orderNum = "9", isImportField = "receivableMoney",isStatistics = true)
     private String receivableMoney;

     /** 已收费金额*/
     @Excel(name = "实收金额（元）", orderNum = "10",isImportField = "alreadyReceivedMoney",isStatistics = true)
     private String alreadyReceivedMoney;

     /** 应退金额*/
     @Excel(name = "应退金额（元）", orderNum = "11", isImportField = "refundMoney",isStatistics = true)
     private String refundMoney;

     /** 实退金额*/
     @Excel(name = "实退金额（元）", orderNum = "12",isImportField = "alreadyRefundMoney",isStatistics = true)
     private String alreadyRefundMoney;

     /** 渠道交易订单状态 如：微信/支付宝 1 交易成功 2 退款成功*/
     @Excel(name = "收款状态", replace = {"已收款_1","已退款_2"},orderNum = "13",isImportField = "channelOrderState")
     private String channelOrderState;

     /** 渠道号*/
     @Excel(name = "渠道", replace = {"微信_100123","支付宝_100125"},orderNum = "14",isImportField = "channelId")
     private String channelId;

     /** 订单异常状态 -1异常 1正常*/
     private String errorState;

     /** 业务结果 0暂无 1已冲正 2已退费 */
     @Excel(name = "业务结果", replace = {"暂无_0","已冲正_1","已退费_2","正在冲正中_3","正在退费中_4","待冲正_5","待退费_6","待调账_7","正在调账中_8","已调账_9","已收款_10"},orderNum = "15",isImportField = "bizType")
     private String bizType;

     /** 对账结果  -1短款 0账平 1长款 2账平(处置后) */
     @Excel(name = "对账结果",  replace = {"短款_-1","账平_0","长款_1","账平(处置后)_2"},orderNum = "16",isImportField = "checkState")
     private String checkState;

     /** 业务执行状态  0 待执行 1 正在执行 2 执行完成*/
     private String exeState;
     
     private String cardNo;

     public String getCardNo() {
          return cardNo;
     }

     public void setCardNo(String cardNo) {
          this.cardNo = cardNo;
     }

     public String getBizType() {
          return bizType;
     }

     public void setBizType(String bizType) {
          this.bizType = bizType;
     }

     public String getLastDate() {
          return lastDate;
     }

     public void setLastDate(String lastDate) {
          this.lastDate = lastDate;
     }

     public String getExeState() {
          return exeState;
     }

     public void setExeState(String exeState) {
          this.exeState = exeState;
     }

     public String getQlcOrderState() {
          return qlcOrderState;
     }

     public void setQlcOrderState(String qlcOrderState) {
          this.qlcOrderState = qlcOrderState;
     }

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

     public String getChannelOrderId() {
          return channelOrderId;
     }

     public void setChannelOrderId(String channelOrderId) {
          this.channelOrderId = channelOrderId;
     }

     public String getCreateDate() {
          return createDate;
     }

     public void setCreateDate(String createDate) {
          this.createDate = createDate;
     }

     public String getPriceName() {
          return priceName;
     }

     public void setPriceName(String priceName) {
          this.priceName = priceName;
     }

     public String getOperatorName() {
          return operatorName;
     }

     public void setOperatorName(String operatorName) {
          this.operatorName = operatorName;
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


     public String getChannelOrderState() {
          return channelOrderState;
     }

     public void setChannelOrderState(String channelOrderState) {
          this.channelOrderState = channelOrderState;
     }

     public String getChannelId() {
          return channelId;
     }

     public void setChannelId(String channelId) {
          this.channelId = channelId;
     }

     public String getErrorState() {
          return errorState;
     }

     public void setErrorState(String errorState) {
          this.errorState = errorState;
     }

     public String getCheckState() {
          return checkState;
     }

     public void setCheckState(String checkState) {
          this.checkState = checkState;
     }
}

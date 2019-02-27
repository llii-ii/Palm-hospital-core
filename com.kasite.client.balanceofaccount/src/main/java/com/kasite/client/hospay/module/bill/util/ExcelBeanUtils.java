package com.kasite.client.hospay.module.bill.util;

import com.kasite.client.hospay.common.constant.Constant;
import com.kasite.client.hospay.module.bill.entity.bill.bo.RequestHandlerParam;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.EveryDayBalance;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.ThreePartyBalance;
import org.jeecgframework.poi.excel.entity.params.ExcelExportEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cc
 * TODO excel转换封装类
 */
@Component
public class ExcelBeanUtils {

     @Autowired
     RequestHandlerParam requestHandlerParam;

     /**
      * 多方汇总Excel导出的实体
      * @return
      */
     public List<ExcelExportEntity> threePartyBalanceExcelChange(){
          List<ExcelExportEntity> threePartyEntity = new ArrayList<>();
          //构造对象等同于@Excel
          ExcelExportEntity excellently = new ExcelExportEntity("订单类型", "qlcOrderState",15);
          excellently.setReplace(new String[]{"支付订单_1","退款订单_2"});
          threePartyEntity.add(excellently);
          threePartyEntity.add(new ExcelExportEntity("全流程订单号","orderId",35));
          /* 根据severalParties节点的值判断当前为几方汇总,去除多余的Excel节点*/
          if (Constant.THREEPARTY.equals(requestHandlerParam.severalParties)){
               threePartyEntity.add(new ExcelExportEntity("His订单号","hisOrderId",35));
          }
          threePartyEntity.add(new ExcelExportEntity("渠道订单号","channelOrderId",35));

          ExcelExportEntity createDate = new ExcelExportEntity("订单创建时间", "createDate",35);
          createDate.setDatabaseFormat("yyyy-MM-dd HH:mm:ss.0");
          createDate.setFormat("yyyy-MM-dd HH:mm:ss");
          threePartyEntity.add(createDate);

          ExcelExportEntity lastDate = new ExcelExportEntity("订单最后操作时间", "lastDate",35);
          lastDate.setDatabaseFormat("yyyy-MM-dd HH:mm:ss.0");
          lastDate.setFormat("yyyy-MM-dd HH:mm:ss");
          threePartyEntity.add(lastDate);

          threePartyEntity.add(new ExcelExportEntity("服务内容","priceName",15));

          ExcelExportEntity receivableMoney = new ExcelExportEntity("应收金额（元）", "receivableMoney");
          receivableMoney.setStatistics(true);
          threePartyEntity.add(receivableMoney);

          ExcelExportEntity alreadyReceivedMoney = new ExcelExportEntity("实收金额（元）", "alreadyReceivedMoney");
          alreadyReceivedMoney.setStatistics(true);
          threePartyEntity.add(alreadyReceivedMoney);

          ExcelExportEntity refundMoney = new ExcelExportEntity("应退金额（元）", "refundMoney");
          refundMoney.setStatistics(true);
          threePartyEntity.add(refundMoney);

          ExcelExportEntity alreadyRefundMoney = new ExcelExportEntity("实退金额（元）", "alreadyRefundMoney");
          alreadyRefundMoney.setStatistics(true);
          threePartyEntity.add(alreadyRefundMoney);

          ExcelExportEntity channelOrderState = new ExcelExportEntity("收款状态", "channelOrderState");
          channelOrderState.setReplace(new String[]{"已收款_1","已退款_2"});
          threePartyEntity.add(channelOrderState);

          ExcelExportEntity channelId = new ExcelExportEntity("交易渠道", "channelId",15);
          channelId.setReplace(new String[]{"微信_100123","支付宝_100125"});
          threePartyEntity.add(channelId);

          ExcelExportEntity bizType = new ExcelExportEntity("业务结果", "bizType");
          bizType.setReplace(new String[]{"暂无_0","已冲正_1","已退费_2","正在冲正中_3","正在退费中_4","待冲正_5","待退费_6","待调账_7","正在调账中_8","已调账_9","已收款_10"});
          threePartyEntity.add(bizType);

          ExcelExportEntity checkState = new ExcelExportEntity("对账结果", "checkState");
          checkState.setReplace(new String[]{"短款_-1","账平_0","长款_1","账平(处置后)_2"});
          threePartyEntity.add(checkState);
          return threePartyEntity;
     }

     /**
      * 每日汇总Excel导出的实体
      * @return
      */
     public List<ExcelExportEntity> everDayBalanceExcelChange(){
          List<ExcelExportEntity> everDayEntity = new ArrayList<>();
          //构造对象等同于@Excel
          everDayEntity.add(new ExcelExportEntity("对账日期", "billDate", 15));

          /* 根据severalParties节点的值判断当前为几方汇总,去除多余的Excel节点*/
          if (Constant.THREEPARTY.equals(requestHandlerParam.severalParties)){
               ExcelExportEntity hisBills = new ExcelExportEntity("HIS账单笔数","hisBills",15);
               hisBills.setStatistics(true);
               everDayEntity.add(hisBills);
          }

          ExcelExportEntity qlcBills = new ExcelExportEntity("全流程账单笔数","qlcBills",15);
          qlcBills.setStatistics(true);
          everDayEntity.add(qlcBills);

          ExcelExportEntity channelBills = new ExcelExportEntity("渠道账单笔数","channelBills",15);
          channelBills.setStatistics(true);
          everDayEntity.add(channelBills);

          ExcelExportEntity checkNum = new ExcelExportEntity("已核对笔数","checkNum",15);
          checkNum.setStatistics(true);
          everDayEntity.add(checkNum);

          ExcelExportEntity abnormalNum = new ExcelExportEntity("异常笔数","abnormalNum",15);
          abnormalNum.setStatistics(true);
          everDayEntity.add(abnormalNum);

          ExcelExportEntity receivableMoney = new ExcelExportEntity("应收金额（元）", "receivableMoney",15);
          receivableMoney.setStatistics(true);
          everDayEntity.add(receivableMoney);

          ExcelExportEntity alreadyReceivedMoney = new ExcelExportEntity("实收金额（元）", "alreadyReceivedMoney",15);
          alreadyReceivedMoney.setStatistics(true);
          everDayEntity.add(alreadyReceivedMoney);

          ExcelExportEntity refundMoney = new ExcelExportEntity("应退金额（元）", "refundMoney",15);
          refundMoney.setStatistics(true);
          everDayEntity.add(refundMoney);

          ExcelExportEntity alreadyRefundMoney = new ExcelExportEntity("实退金额（元）", "alreadyRefundMoney",15);
          alreadyRefundMoney.setStatistics(true);
          everDayEntity.add(alreadyRefundMoney);

          ExcelExportEntity checkResult = new ExcelExportEntity("对账结果","checkResult",15);
          checkResult.setReplace(new String[]{"账不平_-1","账平_1","账平(处置后)_2"});
          everDayEntity.add(checkResult);

          return everDayEntity;
     }

     /**
      * 分类汇总Excel导出的实体
      * @return
      */
     public List<ExcelExportEntity> classifySummaryBalanceExcelChange(){
          List<ExcelExportEntity> classifyEntity = new ArrayList<>();
          //构造对象等同于@Excel
          classifyEntity.add(new ExcelExportEntity("对账日期", "billDate", 16));

          ExcelExportEntity channelId = new ExcelExportEntity("交易渠道", "channelId",15);
          channelId.setReplace(new String[]{"微信_100123","支付宝_100125"});
          classifyEntity.add(channelId);

          ExcelExportEntity serviceName = new ExcelExportEntity("服务名称", "serviceName");
          serviceName.setReplace(new String[]{"挂号支付_0","当日挂号支付_006","医嘱支付_999","当面付_008","住院支付_007"});
          classifyEntity.add(serviceName);

          /* 根据severalParties节点的值判断当前为几方汇总,去除多余的Excel节点*/
          if (Constant.THREEPARTY.equals(requestHandlerParam.severalParties)){
               ExcelExportEntity hisBills = new ExcelExportEntity("HIS账单笔数","hisBills",15);
               hisBills.setStatistics(true);
               classifyEntity.add(hisBills);
          }

          ExcelExportEntity qlcBills = new ExcelExportEntity("全流程账单笔数","qlcBills",15);
          qlcBills.setStatistics(true);
          classifyEntity.add(qlcBills);

          ExcelExportEntity channelBills = new ExcelExportEntity("渠道账单笔数","channelBills",15);
          channelBills.setStatistics(true);
          classifyEntity.add(channelBills);

          ExcelExportEntity checkNum = new ExcelExportEntity("已核对笔数","checkNum",15);
          checkNum.setStatistics(true);
          classifyEntity.add(checkNum);

          ExcelExportEntity abnormalNum = new ExcelExportEntity("异常笔数","abnormalNum",15);
          abnormalNum.setStatistics(true);
          classifyEntity.add(abnormalNum);

          ExcelExportEntity receivableMoney = new ExcelExportEntity("应收金额（元）", "receivableMoney",15);
          receivableMoney.setStatistics(true);
          classifyEntity.add(receivableMoney);

          ExcelExportEntity alreadyReceivedMoney = new ExcelExportEntity("实收金额（元）", "alreadyReceivedMoney",15);
          alreadyReceivedMoney.setStatistics(true);
          classifyEntity.add(alreadyReceivedMoney);

          ExcelExportEntity refundMoney = new ExcelExportEntity("应退金额（元）", "refundMoney",15);
          refundMoney.setStatistics(true);
          classifyEntity.add(refundMoney);

          ExcelExportEntity alreadyRefundMoney = new ExcelExportEntity("实退金额（元）", "alreadyRefundMoney",15);
          alreadyRefundMoney.setStatistics(true);
          classifyEntity.add(alreadyRefundMoney);

          classifyEntity.add(new ExcelExportEntity("创建时间","createDate",15));

          return classifyEntity;
     }

}

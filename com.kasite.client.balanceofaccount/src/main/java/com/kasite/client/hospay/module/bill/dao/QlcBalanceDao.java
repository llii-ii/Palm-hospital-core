package com.kasite.client.hospay.module.bill.dao;

import com.kasite.client.hospay.module.bill.entity.bill.dbo.Order;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.QLCBalance;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cc
 * TODO 全流程表相关SQL方法说明
 */
public interface QlcBalanceDao {
     /**
      * 汇总全流程账单明细数据
      * @param startDate
      * @param endDate
      * @return
      * TODO 查询的时间建议以payOrder和cancelOrder表中的实际时间为主
      */
     List<QLCBalance> summaryQlcBalance(@Param("startDate") String startDate, @Param("endDate") String endDate);

     /**
      * 查看p_bill比p_qlcBalance表多出的订单数据
      * @param startDate
      * @param endDate
      * @return
      * TODO 由于这边汇总的全流程账单只获取orderState为2和4的账单,如果这家医院存在个性化的修改，很容易遗漏掉一些数据，所以这边需要提供p_bill表的数据进行校准
      */
     List<QLCBalance> queryChannelBillDiffQlcBill(@Param("startDate") String startDate, @Param("endDate") String endDate);

     /**
      * 查询order表里的订单数据
      * @param balance
      * @return
      * TODO 查询出来的异常账单由于会缺少一些展示字段,所以这边需要到o_order表中获取那些展示字段
      */
     Order queryOrderData(@Param("balance") QLCBalance balance);

     /**
      * 全流程表去重
      * @param startDate
      * @param endDate
      */
     void deleteQLCBalance(@Param("startDate") String startDate, @Param("endDate") String endDate);

     /**
      * 保存全流程账单明细数据(由于数据条数太多,要多次读写数据库,在效率上太低,所以使用批量插入)
      * @param balanceList
      */
     void insertQLCBalance(List<QLCBalance> balanceList);
}

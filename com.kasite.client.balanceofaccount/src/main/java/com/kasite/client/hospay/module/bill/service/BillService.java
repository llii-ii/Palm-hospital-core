package com.kasite.client.hospay.module.bill.service;

import com.github.pagehelper.PageInfo;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.ClassifySummaryBalance;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.EveryDayBalance;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.ThreePartyBalance;
import com.kasite.client.hospay.module.bill.entity.bill.dto.QueryBillParam;
import com.kasite.core.common.req.PageVo;

import java.sql.SQLException;

/**
 * @author cc
 * TODO 账单服务接口（账单增删改查的接口都在这边编写）
 */
public interface BillService {
     /**
      * 查询每日汇总账单数据
      * @param queryBillParam
      * @param pageVo
      * @return
      * @throws SQLException
      */
     PageInfo<EveryDayBalance> queryEveryDayBills(QueryBillParam queryBillParam, PageVo pageVo) throws SQLException;

     /**
      * 查询分类汇总账单数据
      * @param queryBillParam
      * @param pageVo
      * @return
      * @throws SQLException
      */
     PageInfo<ClassifySummaryBalance> queryClassifySummaryBills(QueryBillParam queryBillParam, PageVo pageVo) throws SQLException;

     /**
      * 查询三方每日汇总账单明细数据
      * @param queryBillParam
      * @param page
      * @return
      * @throws SQLException
      */
     PageInfo<ThreePartyBalance> queryThreePartyBillDetail(QueryBillParam queryBillParam, PageVo page) throws SQLException;

     /**
      * 根据前端的传递的bizType给当前订单增加业务执行
      * @param bizType
      * @param orderId
      * @param hisOrderId
      * @param channelOrderId
      * @return
      */
     String addPendingOrder(String bizType, String orderId, String hisOrderId, String channelOrderId);

     /**
      * 根据业务执行的结果更改每日汇总账单状态
      * @param bizType
      * @param orderId
      * @param hisOrderId
      * @param channelOrderId
      * @return
      */
     String basedBizChangesEveryDayBalance(String bizType, String orderId, String hisOrderId, String channelOrderId);
}

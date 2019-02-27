package com.kasite.client.hospay.module.bill.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kasite.client.hospay.common.constant.Constant;
import com.kasite.client.hospay.module.bill.dao.EveryDayBalanceDao;
import com.kasite.client.hospay.module.bill.dao.ThreePartyBalanceDao;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.ClassifySummaryBalance;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.EveryDayBalance;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.ThreePartyBalance;
import com.kasite.client.hospay.module.bill.entity.bill.dto.BillParam;
import com.kasite.client.hospay.module.bill.entity.bill.dto.QueryBillParam;
import com.kasite.client.hospay.module.bill.service.BillService;
import com.kasite.client.hospay.module.bill.service.BillSummaryService;
import com.kasite.client.hospay.module.bill.thread.BizOrderExeThread;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.PageVo;
import com.kasite.core.common.util.CommonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.SQLException;
import java.util.List;

/**
 * @author cc TODO 账单业务实现类
 */
@Service
public class BillServiceImpl implements BillService {

     @Autowired
     BillSummaryService billSummaryService;

     @Autowired
     ThreePartyBalanceDao threePartyBalanceDao;

     @Autowired
     EveryDayBalanceDao everyDayBalanceDao;

     public final static Logger logger = LoggerFactory.getLogger(BillServiceImpl.class);

     @Override
     public PageInfo<EveryDayBalance> queryEveryDayBills(QueryBillParam queryBillParam, PageVo page) throws SQLException {
          PageHelper.startPage((page.getPIndex() / page.getPSize()) + 1, page.getPSize());
          List<EveryDayBalance> everyDayBills = everyDayBalanceDao.queryEveryDayBills(queryBillParam);
          if (everyDayBills.size() == 0) {
               logger.info("获取每日汇总账单数据为零");
          }
          logger.info("开始返回每日汇总账单数据！！！");
          //用PageInfo对结果进行包装,对获取到数据后进行分页
          PageInfo<EveryDayBalance> pageInfo = new PageInfo<>(everyDayBills);
          return pageInfo;
     }

     @Override
     public PageInfo<ClassifySummaryBalance> queryClassifySummaryBills(QueryBillParam queryBillParam, PageVo page) throws SQLException {
          PageHelper.startPage((page.getPIndex() / page.getPSize()) + 1, page.getPSize());
          List<ClassifySummaryBalance> classifySummaries = everyDayBalanceDao.queryClassifySummaryBills(queryBillParam);
          if (classifySummaries.size() == 0) {
               logger.info("获取分类汇总账单数据为零");
          }
          logger.info("开始返回分类汇总账单数据！！！");
          //用PageInfo对结果进行包装,对获取到数据后进行分页
          PageInfo<ClassifySummaryBalance> pageInfo = new PageInfo<>(classifySummaries);
          return pageInfo;
     }

     @Override
     public PageInfo<ThreePartyBalance> queryThreePartyBillDetail(QueryBillParam queryBillParam, PageVo page)
             throws SQLException {
          /* 设置分页参数*/
          PageHelper.startPage((page.getPIndex() / page.getPSize()) + 1, page.getPSize());
          List<ThreePartyBalance> threePartyBills = threePartyBalanceDao.queryThreePartyBillDetail(queryBillParam);
          if (threePartyBills.size() == 0) {
               logger.info("获取三方汇总账单明细数据为零");
          }
          logger.info("开始返回三方汇总账单明细数据！！！");
          //用PageInfo对结果进行包装,对获取到数据后进行分页
          PageInfo<ThreePartyBalance> pageInfo = new PageInfo<>(threePartyBills);
          return pageInfo;
     }

     @Override
     public String addPendingOrder(String bizType, String orderId, String hisOrderId, String channelOrderId) {
          logger.info("开始查询该订单是否存在业务订单状态");
          Integer exist = threePartyBalanceDao.queryOrderExist(orderId, hisOrderId, channelOrderId);
          logger.info("查询结束!!!");
          if (exist != 0) {
               return CommonUtil.getRetVal(null, Constant.ADDPENDINGORDER, Constant.FAIL_INT10000, "该订单已存在业务执行状态,请等待业务执行完成再进行操作!!!");
          }
          logger.info("开始为三方订单明细数据增加业务执行");
          threePartyBalanceDao.addPendingOrder(bizType, orderId, hisOrderId, channelOrderId);
          logger.info("新增成功,订单号是:{}", orderId);

          logger.info("开启异步线程执行业务!!!");
          BizOrderExeThread bizOrderExeThread = new BizOrderExeThread();
          bizOrderExeThread.setName("Biz-Order-Exe-thread-orderId=" + orderId);
          KstHosConstant.cachedThreadPool.execute(bizOrderExeThread);

          return CommonUtil.getRetVal(null, Constant.ADDPENDINGORDER, Constant.RET_INT10000, "新增业务状态成功!!!");
     }

     @Override
     public String basedBizChangesEveryDayBalance(String bizType, String orderId, String hisOrderId,
                                                  String channelOrderId) {
//          Integer errorNum;
          String startDate;
          String endDate;
          // 1.先查询出当前订单创建时间
          String createDate = threePartyBalanceDao.queryOrderCreateDate(bizType, orderId, hisOrderId, channelOrderId);
          startDate = createDate.substring(0, 10);
          endDate = createDate.substring(0, 10);
          // 2.根据订单创建时间来查询当前日期是否还存在异常订单
//          errorNum = threePartyBalanceDao.queryErrorNum(startDate + " 00:00:00", endDate + " 23:59:59");
          // 3.更新为处理的异常订单数（剩余异常订单）

          // 4.如果当前日期已不存在异常订单，则触发汇总业务
//          if (errorNum == 0) {
          try {
               BillParam billParam = new BillParam();
               billParam.setStartDate(startDate);
               billParam.setEndDate(endDate);
               billParam.setResultCode("2");
               logger.info("开始调用统计汇总每日账单接口");
               billSummaryService.summaryEveryDayBalance(billParam);
               logger.info("调用统计每日账单接口结束！！！");
          } catch (Exception e) {
               e.printStackTrace();
               logger.error("SQL名称:{},重新汇总每日账单数据异常:{}", "againEveryDayBalance", e.getMessage());
          }
//          }
          return CommonUtil.getRetVal(null, Constant.BASEDBIZCHANGESEVERYDAYBALANCE, Constant.RET_INT10000, Constant.RET_SUCCESSMSG);
     }
}

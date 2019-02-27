package com.kasite.client.hospay.module.bill.service.impl;

import com.coreframework.util.StringUtil;
import com.kasite.client.hospay.common.constant.Constant;
import com.kasite.client.hospay.module.bill.dao.EveryDayBalanceDao;
import com.kasite.client.hospay.module.bill.dao.QlcBalanceDao;
import com.kasite.client.hospay.module.bill.dao.ThreePartyBalanceDao;
import com.kasite.client.hospay.module.bill.entity.bill.bo.ErrorHisBills;
import com.kasite.client.hospay.module.bill.entity.bill.bo.RequestHandlerParam;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.ClassifySummaryBalance;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.EveryDayBalance;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.Order;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.QLCBalance;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.ThreePartyBalance;
import com.kasite.client.hospay.module.bill.entity.bill.dto.BillParam;
import com.kasite.client.hospay.module.bill.service.BillSummaryService;
import com.kasite.client.hospay.module.bill.util.BillUtils;
import com.kasite.client.hospay.module.bill.util.CommonUtils;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.DateOper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author cc TODO 账单汇总实现类
 */
@SuppressWarnings("unchecked")
@Service
public class BillSummaryServiceImpl implements BillSummaryService {

     public final static Logger logger = LoggerFactory.getLogger(BillSummaryServiceImpl.class);

     @Autowired
     BillUtils billUtils;

     @Autowired
     CommonUtils commonUtils;

     @Autowired
     QlcBalanceDao qlcBalanceDao;

     @Autowired
     ThreePartyBalanceDao threePartyBalanceDao;

     @Autowired
     EveryDayBalanceDao everyDayBalanceDao;

     @Autowired
     RequestHandlerParam requestHandlerParam;

     
     @Override
     public String summaryQLCBalance(BillParam billParam) {
          String startDate = billParam.getStartDate();
          String endDate = billParam.getEndDate();
          List<QLCBalance> qlcBalances = qlcBalanceDao.summaryQlcBalance(startDate, endDate);
          if (qlcBalances.isEmpty()) {
               logger.info("获取全流程账单数据总数为零");
               return CommonUtil.getRetVal(null, Constant.SUMMARYQLCBILL, Constant.FAIL_INT10000, "获取全流程账单数据总数为零");
          }
          logger.info("获取全流程账单成功,总条数是:{}",qlcBalances.size());
          /* 多数据源的获取,目前暂未使用先注释*/
//			List<QLCBalance> qlcFacePayBalances = BillDao.summaryQLCFacePayBalance(startDate, endDate);
//			if (qlcBalances.size() == null) {
//				logger.info("获取全流程当面付账单数据总数为零");
//				return CommonUtil.getRetVal(Constant.SUMMARYQLCBILL, Constant.FAIL_INT10000,"获取全流程当面付账单数据总数为零");
//			}
//			logger.info("获取全流程当面付账单成功!!!");

          /* 2.再插入数据时先进行去重的操作,防止出现重复数据 */
          logger.info("全流程汇总表去重操作开始");
          qlcBalanceDao.deleteQLCBalance(startDate, endDate);
          logger.info("全流程汇总表去重操作结束！！！");

          /* 3.为了防止查询数据量太大，导致mybatis拼接的SQL过长数据库连接过久，所以这边统一采取分页插入*/
          Boolean flag = true;
          int i = 0;
          int pageSize = 500;
          while (flag){
               i = i+1;
               logger.info("开始进行分页查询,当前页数是:{},每页固定获取条数是:{}",i,pageSize);
               Map<String, Object> resultMap = commonUtils.getPagingResultMap(qlcBalances, i, pageSize);
               flag = (Boolean) resultMap.get("flag");
               List<QLCBalance> newQlcBalanceList = (List<QLCBalance>) resultMap.get("result");
               logger.info("分页查询成功,当前页数是:{},分页后实际获取的条数是:{}",i,newQlcBalanceList.size());
               if (!newQlcBalanceList.isEmpty()){
                    logger.info("开始保存全流程汇总账单,当前页数是:{},总条数是:{}",i,newQlcBalanceList.size());
                    qlcBalanceDao.insertQLCBalance(newQlcBalanceList);
                    logger.info("全流程汇总账单保存结束！！！");
               }
          }


          /* 多数据源的获取,目前暂未使用先注释*/
//			logger.info("开始保存全流程当面付汇总账单,总条数是:{}", qlcFacePayBalances.size());
//			BillDao.insertQLCBalance(qlcFacePayBalances);
//			logger.info("全流程当面付汇总账单保存结束！！！");

          /* 4.由于这边汇总的全流程账单只获取orderState为2和4的账单,如果这家医院存在个性化的修改，很容易遗漏掉一些数据，所以这边需要提供p_bill表的数据进行校准
           * 注意：两方账单比较特殊,在汇总全流程时不需要与p_bill进行校准，且三方汇总时已p_bill表为主*/
          if (Constant.THREEPARTY.equals(requestHandlerParam.severalParties)){
               logger.info("开始查询是否存在全流程未保存的异常账单");
               List<QLCBalance> moreBalances = qlcBalanceDao.queryChannelBillDiffQlcBill(startDate, endDate);
               logger.info("跟P_Bill比对后全流程无数据的异常账单总条数是:{}", moreBalances.size());
               /* 创建一个新的List<QLCBalance>的集合用于存储改造后的新对象*/
               List<QLCBalance> changeBalances = new ArrayList<>();

               if (moreBalances.size() > 0) {
                    /* 查询出来的异常账单由于会缺少一些展示字段,所以这边需要到o_order表中获取那些展示字段*/
                    logger.info("开始替换全流程缺失的账单数据");
                    for (QLCBalance balance : moreBalances) {
                         logger.info("全流程汇总缺失的账单是:{}", balance.getOrderId());
                         Order order = qlcBalanceDao.queryOrderData(balance);
                         /* 由于有些系统订单流程不标准，所以这边查询出来的结果可能会为空,为空则直接添加当前缺失订单到集合当中无需做转换*/
                         if (StringUtil.isNotBlank(order)) {
                              QLCBalance qlcBalance = BillUtils.changeQLCBalance(order, balance);
                              changeBalances.add(qlcBalance);
                         } else {
                              changeBalances.add(balance);
                         }
                    }
                    logger.info("替换全流程缺失的账单数据结束!!! 替换的总数是:{}", changeBalances.size());

                    logger.info("开始保存全流程未保存的缺失账单");
                    qlcBalanceDao.insertQLCBalance(changeBalances);
                    logger.info("保存全流程缺失的账单结束！！！");
               }
          }
          return CommonUtil.getRetVal(null, Constant.SUMMARYQLCBILL, Constant.RET_INT10000, "汇总全流程账单成功");
     }

     @Override
     public String summaryThreePartyBalance(BillParam billParam) {
          String startDate = billParam.getStartDate();
          String endDate = billParam.getEndDate();
          List<ThreePartyBalance> threePartyBills = null;
          /* 根据此节点 【severalParties】判断当前属于几方对账,然后执行相应的汇总逻辑*/
          if (Constant.THREEPARTY.equals(requestHandlerParam.severalParties)){
               logger.info("当前为三方汇总！！！");
               threePartyBills = threePartyBalanceDao.summaryThreePartyBalance(startDate, endDate);
          }else if (Constant.TWOPARTY.equals(requestHandlerParam.severalParties)){
               logger.info("当前为两方汇总！！！");
               threePartyBills = threePartyBalanceDao.summaryTwoPartyBalance(startDate, endDate);
          }
          if (threePartyBills.size() == 0) {
               logger.info("获取汇总账单明细数据总数为零",requestHandlerParam.severalParties);
               return CommonUtil.getRetVal(null, Constant.SUMMARYTHREEPARTYSINGLEBILL,
                       Constant.FAIL_INT10000, "获取汇总账单明细数据总数为零");
          }
          logger.info("查询汇总账单明细数据成功!!!");

          /*
           * 再插入数据时先进行去重的操作,防止出现重复数据
           * 1.由于原来的异常订单可能已经添加过业务执行状态了，通过controller重新调用再去重时需要跳过这些订单
           * 2.去重后将当前日期内已存在业务的订单查询出来,插入时也跳过这些订单
           */
          logger.info("{}方汇总表去重操作开始",requestHandlerParam.severalParties);
          threePartyBalanceDao.deleteThreePartyBalance(startDate, endDate);
          logger.info("{}方汇总表去重操作结束！！！",requestHandlerParam.severalParties);

          logger.info("执行汇总逻辑前查询当前时间内是否已有存在业务的异常订单,开始时间：{},结束时间：{}",startDate,endDate);
          List<ThreePartyBalance> exitsErrorBills = threePartyBalanceDao.queryExitsErrorBills(startDate, endDate);
          logger.info("查询已存在业务的异常订单结束,总条数是:{}", exitsErrorBills.size());

          Integer num;

          logger.info("开始汇总逻辑处理！！！");
          List<ThreePartyBalance> handleBills = new ArrayList<>();
          for (ThreePartyBalance partyBills : threePartyBills) {
               /* exitsErrorBills.size()==0 说明不存在已有执行业务的异常订单，则可全部插入*/
               if (exitsErrorBills.size() == 0) {
                    handleBills.add(billUtils.branch(partyBills));
               } else {
                    num = 0;
                    /* 存在已有执行业务的异常订单，需过滤 */
                    for (ThreePartyBalance errorBill : exitsErrorBills) {
                         if (partyBills.getOrderId().equals(errorBill.getOrderId()) && partyBills.getChannelOrderId().equals(errorBill.getChannelOrderId()) && partyBills.getHisOrderId().equals(errorBill.getHisOrderId())) {
                              num = num+1;
                         }
                    }
                    /* 通过num的值来操作当前订单是否要保存到数据表中*/
                    if (num == 0) {
                         handleBills.add(billUtils.branch(partyBills));
                    }
               }
          }
          logger.info("汇总逻辑处理结束！！！");

          /* 3.为了防止查询数据量太大，导致mybatis拼接的SQL过长数据库连接过久，所以这边统一采取分页插入*/
          Boolean flag = true;
          int i = 0;
          int pageSize = 500;
          while (flag){
               i = i+1;
               logger.info("开始进行分页查询,当前页数是:{},每页固定获取条数是:{}",i,pageSize);
               Map<String, Object> resultMap = commonUtils.getPagingResultMap(handleBills, i, pageSize);
               flag = (Boolean) resultMap.get("flag");
               List<ThreePartyBalance> newThreePartyBalanceList = (List<ThreePartyBalance>) resultMap.get("result");
               logger.info("分页查询成功,当前页数是:{},分页后每页获取条数是:{}",i,newThreePartyBalanceList.size());
               if (!newThreePartyBalanceList.isEmpty()){
                    logger.info("开始保存{}方汇总账单明细数据,当前页数是:{},总条数是:{}",requestHandlerParam.severalParties,i,newThreePartyBalanceList.size());
                    threePartyBalanceDao.insertThreePartyBill(newThreePartyBalanceList);
                    logger.info("保存{}方汇总账单明细数据结束！！！",requestHandlerParam.severalParties);
               }
          }
          /*
           * 保存完三方汇总账单明细数据后需要再跟HIS的账单数据进行比对（防止HIS没有做订单唯一性的校验）,将HIS多余全流程账单的数据填充进来
           * 只有当配置参数【severalParties】为三方汇总账单时需要走以下流程
           */
          if (Constant.THREEPARTY.equals(requestHandlerParam.severalParties)){
               logger.info("开始查询是否存在与HIS账单有差额的订单！！！");
               List<ErrorHisBills> errorBills = threePartyBalanceDao.queryHisBillNotExitThreePartyBalance(startDate, endDate);
               logger.info("查询结束,有差额的订单笔数是:{}",errorBills.size());
               /* 判断当前订单条目跟HIS订单条目是否存在差额
                * 1.存在：判断订单的类型如是支付订单即为短款，退费订单即为长款*/
               if (!errorBills.isEmpty()){
                    for (ErrorHisBills bill : errorBills) {
                         /* 1.根据返回的orderId,orderType查询到三方汇总账单明细数据表中相应正常的那笔订单*/
                         ThreePartyBalance errorThreePartyBalance = threePartyBalanceDao.queryErrorThreePartyBalance(bill);
                         /* 判断当前对象在三方明细数据表是否存在数据
                          *  1.存在：做更新,将汇总后异常的订单更新为正常
                          *  2.不存在：做插入，保存当前异常订单*/
                         if (StringUtil.isNotBlank(errorThreePartyBalance)){
                              logger.info("参数设置开始!!!");
                              errorThreePartyBalance = billUtils.changePartyBillData(errorThreePartyBalance,bill,true);
                              logger.info("参数设置结束!!!");
                              logger.info("开始更新异常的三方汇总账单明细数据,订单号是：{}",bill.getOrderId());
                              threePartyBalanceDao.updateThreePartyBillData(errorThreePartyBalance);
                              logger.info("更新异常的三方汇总账单明细数据结束！！！");
                         }else{
                              logger.info("参数设置开始!!!");
                              /* errorThreePartyBalance对象为空，需要重新new一个*/
                              errorThreePartyBalance = billUtils.changePartyBillData(new ThreePartyBalance(),bill,false);
                              logger.info("参数设置结束!!!");
                              logger.info("开始保存异常的三方汇总账单明细数据,订单号是：{}",bill.getOrderId());
                              threePartyBalanceDao.insertThreeParty(errorThreePartyBalance);
                              logger.info("保存异常的三方汇总账单明细数据结束！！！");
                         }
                    }
               }
          }
          return CommonUtil.getRetVal(null, Constant.SUMMARYTHREEPARTYSINGLEBILL, Constant.RET_INT10000,"汇总"+requestHandlerParam.severalParties+"方每日账单数据成功");
     }

     /*
      * 1.判断当前传入的日期差决定要执行的业务次数
      *
      * @param billParam
      */
     @Override
     public String summaryEveryDayBalance(BillParam billParam) {
          String startDate = billParam.getStartDate();
          String endDate = billParam.getEndDate();
          /* 根据传入的初始日期和结束日期计算相差时间，来决定需要处理几次每日账单汇总*/
          String[] dayArr = DateOper.getDayArr(startDate, endDate);
          for (int i = 0; i < dayArr.length; i++) {
               startDate = dayArr[i];
               /* 由于这边的日期时间直接由当前数组的位数赋值,所以需要将endDate的日期格式替换一下 */
               endDate = dayArr[i].replace("00:00:00", "23:59:59");
               List<ThreePartyBalance> threePartyBills = everyDayBalanceDao.queryThreePartyBalance(startDate, endDate);
               if (threePartyBills.size() == 0) {
            	   logger.info("获取日期为:{}的每日汇总账单数据总数为零",startDate);
            	   /* 判断当前传入的日期数组长度,如果为1则说明只有一天直接Return就好了,反之继续 */
            	   if (dayArr.length == 1) {
                       return CommonUtil.getRetVal(null, Constant.SUMMARYTHREEPARTYBILL, Constant.FAIL_INT10000,
                               "获取每日汇总账单数据总数为零");
            	   }else {
            		   continue;
            	   }
                    
               }
               logger.info("获取每日汇总账单数据成功！！！");
               //异常订单
               Integer errNum = 0;
               //剩余异常订单
               Integer overPlusErrNum = 0;
               for (ThreePartyBalance bills : threePartyBills) {
                    /* 统计异常订单 */
                    if (Constant.ERRORSTATE.equals(bills.getErrorState())) {
                         errNum = errNum+1;
                         /* 判断当前异常订单的执行状态bu为2时，说明当前异常订单还未处理，则加1*/
                         if (!Constant.EXEORDERSTATE.equals(bills.getExeState())){
                              overPlusErrNum = overPlusErrNum+1;
                         }
                    }
               }


               /* 查询每日账单数据总数 */
               Integer count = everyDayBalanceDao.queryEveryDayBillCount(startDate, endDate);
               if (count == 0) {
                    logger.info("获取每日汇总账单数据总数为零,暂无数据!总数是{}", count);
                    /* 判读当前数据为空的日期是否是数组的最后一位，如不是就应该继续往下走而不return结束方法 */
                    if (i == dayArr.length - 1) {
                         return CommonUtil.getRetVal(null, Constant.SUMMARYTHREEPARTYBILL,
                                 RetCode.Basic.ERROR_CANNOTEXIST.getCode(), "日期为:" + startDate + "的获取每日汇总账单数据总数为零,暂无数据!");
                    }
               }
               List<EveryDayBalance> everyDayBills = everyDayBalanceDao.summaryEveryDayBill(startDate, endDate);
               /* 再插入数据时先进行去重的操作,防止出现重复数据 */
               logger.info("全流程每日汇总表去重操作开始");
               everyDayBalanceDao.deleteEveryDayBalance(startDate, endDate);
               logger.info("全流程每日汇总表去重操作结束!!!");

               logger.info("保存每日汇总账单数据开始");
               for (EveryDayBalance everyDayBill : everyDayBills) {
                    /* 查询下当前日期内是否还存在异常订单 */
                    logger.info("开始查询下当前日期内是否还存在异常订单");
                    Integer num = everyDayBalanceDao.queryVeryDayExitsErrorBill(startDate, endDate);
                    logger.info("查询结束,异常订单的条数是:{}", num);
                    if (errNum != 0 && !"2".equals(billParam.getResultCode()) && num != 0) {
                         everyDayBill.setCheckResult("-1");
                         everyDayBill.setAbnormalNum(String.valueOf(errNum));
                         everyDayBill.setOverPlusErrNum(String.valueOf(overPlusErrNum));
                    } else if (errNum != 0 && !"2".equals(billParam.getResultCode()) && num == 0) {
                        everyDayBill.setCheckResult("2");
                        everyDayBill.setAbnormalNum(String.valueOf(errNum));
                        everyDayBill.setOverPlusErrNum(String.valueOf(overPlusErrNum));
                    } else if (errNum == 0 && !"2".equals(billParam.getResultCode()) && num == 0) {
                         everyDayBill.setCheckResult("1");
                         everyDayBill.setAbnormalNum("0");
                         everyDayBill.setOverPlusErrNum(String.valueOf(overPlusErrNum));
                    } else if ("2".equals(billParam.getResultCode()) && errNum != 0 && num != 0) {
                        everyDayBill.setCheckResult("-1");
                        everyDayBill.setAbnormalNum(String.valueOf(errNum));
                        everyDayBill.setOverPlusErrNum(String.valueOf(overPlusErrNum));
                    } else if ("2".equals(billParam.getResultCode()) && errNum != 0 && num == 0) {
                         everyDayBill.setCheckResult("2");
                         everyDayBill.setAbnormalNum(String.valueOf(errNum));
                         everyDayBill.setOverPlusErrNum(String.valueOf(overPlusErrNum));
                    }
                    everyDayBill.setCheckNum(String.valueOf(count));
                    everyDayBill.setCreateDate(DateOper.getNowDateTime());
                    logger.info("开始保存每日汇总账单数据!!!");
                    everyDayBalanceDao.insertEveryDayBill(everyDayBill);
                    logger.info("保存每日汇总账单数据结束!!!");

               }
               logger.info("保存每日账单数据结束,保存的账单日期是:{},总数是:{}", startDate, count);

               /*
                * 保存完每日汇总账单后需要触发分类汇总的流程
                */
               logger.info("开始进行分类汇总的业务！！！");
               List<ClassifySummaryBalance> classifySummaries = everyDayBalanceDao.classifySummaryBill(startDate, endDate);
               if (StringUtil.isNotBlank(classifySummaries)) {
                    /* 再插入数据时先进行去重的操作,防止出现重复数据 */
                    logger.info("分类汇总账单表去重操作开始");
                    everyDayBalanceDao.deleteClassifySummaryBalance(startDate, endDate);
                    logger.info("分类汇总账单表去重操作结束!!!");

                    for (ClassifySummaryBalance summary : classifySummaries) {
                         /* 保存生成当前汇总订单记录的时间*/
                         summary.setCreateDate(DateOper.getNowDateTime());
                         logger.info("开始插入分类汇总账单！！！");
                         everyDayBalanceDao.insertClassifySummaryBill(summary);
                         logger.info("插入日期为" + summary.getBillDate() + "的分类汇总账单结束！！！服务名称是:{}", summary.getServiceName());
                    }
                    logger.info("分类汇总结束！！！");
               }
          }
          return CommonUtil.getRetVal(null, Constant.SUMMARYTHREEPARTYBILL, Constant.RET_INT10000,
                  "保存每日汇总账单数据成功");
     }
}

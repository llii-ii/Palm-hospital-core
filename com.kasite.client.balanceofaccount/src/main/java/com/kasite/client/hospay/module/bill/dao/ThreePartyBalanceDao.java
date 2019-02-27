package com.kasite.client.hospay.module.bill.dao;

import com.kasite.client.hospay.module.bill.entity.bill.bo.ErrorHisBills;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.ThreePartyBalance;
import com.kasite.client.hospay.module.bill.entity.bill.dto.QueryBillParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cc
 * TODO 三方汇总账单表相关SQL方法说明
 */
public interface ThreePartyBalanceDao {
     /**
      * 汇总三方账单明细数据
      * @param startDate
      * @param endDate
      * @return
      */
     List<ThreePartyBalance> summaryThreePartyBalance(@Param("startDate") String startDate, @Param("endDate") String endDate);

     /**
      * 汇总两方账单明细数据
      * @param startDate
      * @param endDate
      * @return
      */
     List<ThreePartyBalance> summaryTwoPartyBalance(@Param("startDate") String startDate, @Param("endDate") String endDate);

     /**
      * 删除指定日期内的三方汇总账单明细数据（去重）
      * @param startDate
      * @param endDate
      */
     void deleteThreePartyBalance(@Param("startDate") String startDate, @Param("endDate") String endDate);

     /**
      * 查询已存在业务状态的异常订单
      * @param startDate
      * @param endDate
      * @return
      */
     List<ThreePartyBalance> queryExitsErrorBills(@Param("startDate") String startDate, @Param("endDate") String endDate);

     /**
      * 查询当前退费订单是否存在于P_Bill表中
      * @param orderId
      * @param orderType
      * @return
      */
     boolean queryOrderExistBill(@Param("orderId") String orderId, @Param("orderType") String orderType);

     /**
      * 查询当前退费订单是否存在于P_His_Bill表中
      * @param orderId
      * @param orderType
      * @return
      */
     boolean queryOrderExistHisBill(@Param("orderId") String orderId, @Param("orderType") String orderType);

     /**
      * 查看当前异常订单是否已添加过,添加过则不处理
      * @param threePartyBalance
      * @return
      * TODO 再将异常的三方汇总订单明细数据到异常数据表,并防止重复
      */
     boolean queryExceptionBillWhetherNull(@Param("threePartyBalance")ThreePartyBalance threePartyBalance);

     /**
      * 在统计三方对账时如果出现异常订单,就保存到p_exceptionBill表中进行留档
      * @param threePartyBalance
      */
     void insertExceptionBill(@Param("threePartyBalance") ThreePartyBalance threePartyBalance);

     /**
      * 保存三方汇总账单明细数据(由于数据条数太多,要多次读写数据库,在效率上太低,所以使用批量插入)
      * @param threePartyBills
      */
     void insertThreePartyBill(List<ThreePartyBalance> threePartyBills);

     /**
      * 更新三方订单明细数据执行状态
      * @param exeState
      * @param orderId
      * @param hisOrderId
      * @param channelOrderId
      * @param price
      * @param refundId
      * @param orderType
      */
     void updateThreePartyBalanceExeState(@Param("exeState") String exeState,@Param("orderId") String orderId,@Param("hisOrderId") String hisOrderId,@Param("channelOrderId") String channelOrderId,@Param("price") String price,@Param("refundId") String refundId,@Param("orderType") String orderType);

     /**
      * 查询需要执行业务的订单
      * @return
      */
     List<ThreePartyBalance> queryExistBizOrder();

     /**
      * 查询三方汇总账单明细数据
      * @param queryBillParam
      * @return
      */
     List<ThreePartyBalance> queryThreePartyBillDetail(@Param ("queryBillParam") QueryBillParam queryBillParam);

     /**
      * 查询订单是否已存在待执行的业务
      * @param orderId
      * @param hisOrderId
      * @param channelOrderId
      * @return
      */
     Integer queryOrderExist(@Param("orderId") String orderId, @Param("hisOrderId") String hisOrderId, @Param("channelOrderId") String channelOrderId);

     /**
      * 根据前端的传递的bizType给当前订单新增业务状态
      * @param bizType
      * @param orderId
      * @param hisOrderId
      * @param channelOrderId
      */
     void addPendingOrder(@Param("bizType") String bizType, @Param("orderId") String orderId,@Param("hisOrderId")  String hisOrderId, @Param("channelOrderId")  String channelOrderId);

     /**
      * 查询订单创建时间
      * @param bizType
      * @param orderId
      * @param hisOrderId
      * @param channelOrderId
      * @return
      */
     String queryOrderCreateDate(@Param("bizType") String bizType, @Param("orderId") String orderId,@Param("hisOrderId")  String hisOrderId, @Param("channelOrderId")  String channelOrderId);

     /**
      * 根据日期查询每日异常账单数据，并与每日汇总拼凑
      * @param startDate
      * @param endDate
      * @return
      */
     Integer queryErrorNum(@Param("startDate") String startDate, @Param("endDate") String endDate);


     /**
      * 查看当前汇总完的三方汇总账单明细数据是否还有缺失
      * 1.由于HIS有可能未对同一笔订单进行校验，所以多次调用就会在HIS端生成
      *   orderId一致但是HisOrderID不一致的情况,这种订单如是支付订单即为短款，退费订单即为长款
      * @param startDate
      * @param endDate
      * @return
      */
     List<ErrorHisBills> queryHisBillNotExitThreePartyBalance(@Param("startDate") String startDate, @Param("endDate") String endDate);

     /**
      * 查询当前异常订单在三方汇总订单数据表中相应正常的那笔订单
      * @param bill
      * @return
      */
     ThreePartyBalance queryErrorThreePartyBalance(@Param("bill") ErrorHisBills bill);

     /**
      * 保存异常的三方汇总账单明细数据
      * @param errorThreePartyBalance
      */
     void insertThreeParty(@Param("errorThreePartyBalance") ThreePartyBalance errorThreePartyBalance);

     /**
      * 更新三方汇总账单明细数据
      * @param errorThreePartyBalance
      */
     void updateThreePartyBillData(@Param("errorThreePartyBalance")ThreePartyBalance errorThreePartyBalance);
}

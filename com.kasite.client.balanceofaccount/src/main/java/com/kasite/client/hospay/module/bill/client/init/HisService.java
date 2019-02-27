package com.kasite.client.hospay.module.bill.client.init;

import com.kasite.client.hospay.module.bill.entity.bill.bo.HisBill;

import java.util.List;

/**
 * @author cc
 * TODO HIS接口定义
 */
public interface HisService {
     /**
      * HIS账单接口soap实现
      * @param startDate
      * @param endDate
      * @throws Exception
      * @return
      */
     List<HisBill> queryHisOrderBillListSoap(String startDate, String endDate) throws Exception;

     /**
      * HIS账单接口HL7实现
      * @param startDate
      * @param endDate
      * @return
      */
     List<HisBill> queryHisOrderBillListHL7(String startDate, String endDate) throws Exception;

     /**
      * 查询HIS订单的时时状态做时时退款,建议只有查询不到订单状态或者订单状态失败时才做退费
      * @param orderId
      * @param startDate
      * @param endDate
      * @throws Exception
      * @return
      */
     List<HisBill> queryHisOrderState(String orderId,String startDate, String endDate) throws Exception;

}

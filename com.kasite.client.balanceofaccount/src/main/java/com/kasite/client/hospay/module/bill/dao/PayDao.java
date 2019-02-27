package com.kasite.client.hospay.module.bill.dao;

import com.kasite.client.hospay.module.bill.entity.bill.bo.HisBill;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.NoticeLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cc
 * TODO 存放除跟几张账单表有关的SQL方法说明
 */
public interface PayDao {
     /**
      * 将退费记录的日志记录在P_NOTICE_LOG中，有助于后续查看
      * @param noticeLog
      */
     void insertNoticeLog(@Param("noticeLog") NoticeLog noticeLog);

     /**
      *  根据日期删除过往的HIS账单,防止相同数据插入多次(去重)
      * @param startDate
      * @param endDate
      */
     void deleteOldHisBillByTransDate(@Param("startDate") String startDate, @Param("endDate") String endDate);


     /**
      * 保存HIS账单数据
      * @param hisBills
      */
     void insertHisBill(List<HisBill> hisBills);


}

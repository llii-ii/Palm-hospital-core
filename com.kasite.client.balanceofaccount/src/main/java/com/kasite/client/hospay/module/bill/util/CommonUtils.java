package com.kasite.client.hospay.module.bill.util;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author cc
 */
@Component
public class CommonUtils {
     /**
      * 获取uuid值
      */
     public static String getUUID() {
          return (UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
     }

     /**
      *  根据传入的index和pageSize获取当前集合中的分页数据
      *  * @currPageNo  页面传入的页号，从一开始  
      *  * @pageSize 每页记录数  
      */
     public <T> Map<String, Object> getPagingResultMap(List<T> list, Integer currPageNo, Integer pageSize) {
          Map<String, Object> retMap = new HashMap<>(16);

          /* 总行数*/
          int totalRowNum = list.size();
          /* 行数对应的总页数*/
          int totalPageNum = (totalRowNum - 1) / pageSize + 1;

          /* 判断当前传入的list集合是否为空以及页数是否大于总页数,存在则继续处理，反之*/
          if (currPageNo > totalPageNum || list.isEmpty()) {
               retMap.put("result", Collections.emptyList());
               retMap.put("pageNo", 0);
               retMap.put("pageRowNum", 0);
               retMap.put("totalRowNum", 0);
               retMap.put("totalPageNum", 0);
               //标记当前集合是否已经查询结束
               retMap.put("flag",false);
               return retMap;
          }


          int realPageNo = currPageNo;
          if (currPageNo > totalPageNum) {
               realPageNo = totalPageNum;
          } else if (currPageNo < 1) {
               realPageNo = 1;
          }

          int fromIdx = (realPageNo - 1) * pageSize;
          int toIdx = realPageNo * pageSize > totalRowNum ? totalRowNum : realPageNo * pageSize;

          if (realPageNo == totalPageNum && totalPageNum * pageSize > totalRowNum) {
               toIdx = totalRowNum;
          }

          List<T> result = list.subList(fromIdx, toIdx);

          retMap.put("result", result);
          retMap.put("pageNo", realPageNo);
          retMap.put("pageRowNum", result.size());
          retMap.put("totalRowNum", totalRowNum);
          retMap.put("totalPageNum", totalPageNum);
          /* 判断当前页总数是否大于pageSize，如大于才需要返回true继续走分页*/
          if (pageSize > totalRowNum) {
        	  retMap.put("flag",false);
          }else {
        	  retMap.put("flag",true);
          }
          return retMap;
     }

     /**
      * 由于每个HIS产商的接口入参时间格式不同，所以这边可根据需要进行修改
      * @param date
      * @return
      */
     public String dateChange(String date,String format){
          SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
          Date d = null;
          try {
               d = simpleDateFormat.parse(date);
          } catch (ParseException e) {
               e.printStackTrace();
          }
          return simpleDateFormat.format(d);
     }

}

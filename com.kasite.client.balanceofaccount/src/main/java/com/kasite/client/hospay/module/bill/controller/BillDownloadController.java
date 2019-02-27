package com.kasite.client.hospay.module.bill.controller;

import com.alibaba.fastjson.JSONObject;
import com.kasite.client.hospay.common.constant.Constant;
import com.kasite.client.hospay.module.bill.entity.bill.bo.*;
import com.kasite.client.hospay.module.bill.entity.bill.dto.BillParam;
import com.kasite.client.hospay.module.bill.service.DownloadBillService;
import com.kasite.client.hospay.module.bill.util.BillUtils;
import com.kasite.client.hospay.module.bill.util.ExcelBeanUtils;
import com.kasite.client.hospay.module.bill.util.XmlParamUtils;
import com.kasite.core.common.exception.ParamException;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

/**
 * @author cc
 * TODO excel下载使用的EasyPoi
 * TODO 官方地址 http://easypoi.mydoc.io/#text_202975
 */
@RestController
@RequestMapping(value = "/billDownload")
public class BillDownloadController {
     private final static Logger logger = LoggerFactory.getLogger(BillDownloadController.class);

     @Autowired
     DownloadBillService downloadBillService;

     @Autowired
     RequestHandlerParam requestHandlerParam;

     @Autowired
     ExcelBeanUtils excelBeanUtils;

     @Autowired
     BillUtils billUtils;


     /**
      * 下载HIS账单（每日） 1.根据日期调用HIS账单接口（所有账单，支付和退款）
      *
      * @return JSONObject
      * @HisResponse orderID, HisOrderID, Price, HisOrderType or HisOrderState
      */
     @PostMapping("/downloadHisBill.do")
     public JSONObject downloadHisBill(BillParam billParam) {

          String resp = downloadBillService.downloadHisBill(billParam.getStartDate(), billParam.getEndDate());

          return XmlParamUtils.documentToJSONObject(resp);
     }

     /**
      * 每日汇总账单excel下载
      *
      * @param fileName
      * @param billParam
      * @param checkState
      */
     @GetMapping(value = "/downloadEveryDayBillsExcel.do", produces = "text/html;charset=UTF-8")
     public void downloadEveryDayBillsExcel(@RequestParam String fileName, BillParam billParam, @RequestParam String checkState, HttpServletResponse response) {
          // 告诉浏览器用什么软件可以打开此文件
          response.setHeader("content-Type", "application/vnd.ms-excel");
          // 下载文件的默认名称
          response.setHeader("Content-Disposition","attachment;filename= downloadEveryDayBillsExcel.xls");
          List<ThreeEveryDayBillsExcel> billsList = null;
          List<TwoEveryDayBillsExcel> twoBills;
          try {
               billsList = downloadBillService.downloadEveryDayBillExcel(billParam.getStartDate(), billParam.getEndDate(), checkState);
          } catch (SQLException e) {
               e.printStackTrace();
               logger.info("查询每日汇总账单excel数据SQL异常:{}", e.getMessage());
          }
          logger.info("开始分类汇总账单明细");
          Workbook workbook = null;
          // 根据几方对账的参数来初始化需要导出的Excel实体
          if (Constant.THREEPARTY.equals(requestHandlerParam.severalParties)){
               workbook = ExcelExportUtil.exportExcel(new ExportParams(fileName, fileName), ThreeEveryDayBillsExcel.class, billsList);
          }else if (Constant.TWOPARTY.equals(requestHandlerParam.severalParties)){
               twoBills = new ArrayList<>();
               TwoEveryDayBillsExcel twoEveryDayBillsExcel;
               for (ThreeEveryDayBillsExcel summaryExcel : billsList) {
                    twoEveryDayBillsExcel = new TwoEveryDayBillsExcel(summaryExcel.getBillDate(),summaryExcel.getQlcBills(),summaryExcel.getChannelBills(),summaryExcel.getCheckNum(),summaryExcel.getAbnormalNum(),summaryExcel.getOverPlusErrNum(),summaryExcel.getReceivableMoney(),summaryExcel.getAlreadyReceivedMoney(),summaryExcel.getRefundMoney(),summaryExcel.getAlreadyRefundMoney(),summaryExcel.getCheckResult());
                    twoBills.add(twoEveryDayBillsExcel);
               }
               workbook = ExcelExportUtil.exportExcel(new ExportParams(fileName, fileName), TwoEveryDayBillsExcel.class, twoBills);
          }
          try {
               workbook.write(response.getOutputStream());
               logger.info("导出每日汇总账单结束");
          } catch (IOException e) {
               e.printStackTrace();
               logger.info("写入每日汇总账单excel数据异常:{}", e.getMessage());
          }
     }

     /**
      * 账单明细账单excel下载
      * @param fileName
      * @param billParam
      * @param response
      * @throws ParseException
      */
     @GetMapping(value = "/downloadPartyBillsExcel.do", produces = "text/html;charset=UTF-8")
     public void downloadPartyBillsExcel(@RequestParam String fileName, BillParam billParam, HttpServletResponse response)
             throws ParseException {
          // 告诉浏览器用什么软件可以打开此文件
          response.setHeader("content-Type", "application/vnd.ms-excel");
          // 下载文件的默认名称
          response.setHeader("Content-Disposition","attachment;filename= downloadThreePartyBillsExcel.xls");
          List<ThreePartyBillsExcel> list = null;
          List<ThreePartyBillsExcel> billsList = new ArrayList<>();
          List<TwoPartyBillsExcel> twoBills;
          try {
               list = downloadBillService.downloadThreePartyBillExcel(billParam);
          } catch (SQLException e) {
               e.printStackTrace();
               logger.info("查询三方汇总账单明细excel数据SQL异常:{}", e.getMessage());
          }
          // 根据返回值设置中文提示
          for (ThreePartyBillsExcel bills : list) {

               bills.setCheckState("2".equals(bills.getExeState())?bills.getExeState():bills.getCheckState());
               //业务类型的返回需要根据exeState的值来返回
               bills.setBizType(billUtils.changeType(bills.getBizType(),bills.getExeState()));
               billsList.add(bills);
          }
          // 生成excel数据
          logger.info("开始导出三方汇总账单明细");
          Workbook workbook = null;
          // 根据几方对账的参数来初始化需要导出的Excel实体
          if (Constant.THREEPARTY.equals(requestHandlerParam.severalParties)){
               workbook = ExcelExportUtil.exportExcel(new ExportParams(fileName, fileName), ThreePartyBillsExcel.class, billsList);
          }else if (Constant.TWOPARTY.equals(requestHandlerParam.severalParties)){
               twoBills = new ArrayList<>();
               TwoPartyBillsExcel twoPartyBillsExcel;
               for (ThreePartyBillsExcel summaryExcel : billsList) {
                    String checkState = "2".equals(summaryExcel.getExeState())?summaryExcel.getExeState(): summaryExcel.getCheckState();
                    //业务类型的返回需要根据exeState的值来返回
                    String bizType = billUtils.changeType(summaryExcel.getBizType(),summaryExcel.getExeState());
                    twoPartyBillsExcel = new TwoPartyBillsExcel(summaryExcel.getQlcOrderState(),summaryExcel.getOrderId(),summaryExcel.getChannelOrderId(),summaryExcel.getCreateDate(),summaryExcel.getLastDate(),summaryExcel.getPriceName(),summaryExcel.getOperatorName(),summaryExcel.getReceivableMoney(),summaryExcel.getAlreadyReceivedMoney(),summaryExcel.getRefundMoney(),summaryExcel.getAlreadyRefundMoney(),summaryExcel.getChannelOrderState(),summaryExcel.getChannelId(),checkState,bizType);
                    twoBills.add(twoPartyBillsExcel);
               }
               workbook = ExcelExportUtil.exportExcel(new ExportParams(fileName, fileName), TwoPartyBillsExcel.class, twoBills);
          }
          try {
               workbook.write(response.getOutputStream());
               logger.info("导出三方汇总账单明细结束");
          } catch (IOException e) {
               e.printStackTrace();
               logger.info("写入三方汇总账单明细excel数据异常:{}", e.getMessage());
          }
     }

     /**
      * 分类汇总账单下载
      * @param fileName
      * @param billParam
      * @param response
      */
     @GetMapping(value = "/downloadClassifySummaryBillsExcel.do", produces = "text/html;charset=UTF-8")
     public void downloadClassifySummaryBillsExcel(@RequestParam String fileName, BillParam billParam, HttpServletResponse response) {
          /* 告诉浏览器用什么软件可以打开此文件 */
          response.setHeader("content-Type", "application/vnd.ms-excel");
          /* 下载文件的默认名称 */
          response.setHeader("Content-Disposition", "attachment;filename= downloadClassifySummaryBillsExcel.xls");
          List<ThreeClassifySummaryExcel> billsList = null;
          List<TwoClassifySummaryExcel> twoBills;
          try {
               billsList = downloadBillService.downloadClassifySummaryBillExcel(billParam);
          } catch (SQLException e) {
               e.printStackTrace();
               logger.info("查询分类汇总账单excel数据SQL异常:{}", e.getMessage());
          }
          // 生成excel数据
          logger.info("开始分类汇总账单明细");
          Workbook workbook = null;
          // 根据几方对账的参数来初始化需要导出的Excel实体
          if (Constant.THREEPARTY.equals(requestHandlerParam.severalParties)){
               workbook = ExcelExportUtil.exportExcel(new ExportParams(fileName, fileName), ThreeClassifySummaryExcel.class, billsList);
          }else if (Constant.TWOPARTY.equals(requestHandlerParam.severalParties)){
               twoBills = new ArrayList<>();
               TwoClassifySummaryExcel twoClassifySummaryExcel;
               for (ThreeClassifySummaryExcel summaryExcel : billsList) {
                    twoClassifySummaryExcel = new TwoClassifySummaryExcel(summaryExcel.getBillDate(),summaryExcel.getChannelId(), summaryExcel.getServiceName(), summaryExcel.getChannelBills(),summaryExcel.getQlcBills(),summaryExcel.getReceivableMoney(),summaryExcel.getAlreadyReceivedMoney(),summaryExcel.getRefundMoney(),summaryExcel.getAlreadyRefundMoney());
                    twoBills.add(twoClassifySummaryExcel);
               }
               workbook = ExcelExportUtil.exportExcel(new ExportParams(fileName, fileName), TwoClassifySummaryExcel.class, twoBills);
          }

          try {
               if (workbook != null) {
                    workbook.write(response.getOutputStream());
               }
               logger.info("导出分类汇总账单结束");
          } catch (IOException e) {
               e.printStackTrace();
               logger.info("写入分类汇总账单excel数据异常:{}", e.getMessage());
          }
     }
}

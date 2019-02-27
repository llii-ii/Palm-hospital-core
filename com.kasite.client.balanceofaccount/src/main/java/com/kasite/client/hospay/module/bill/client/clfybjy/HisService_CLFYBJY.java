package com.kasite.client.hospay.module.bill.client.clfybjy;

import com.coreframework.util.ArithUtil;
import com.kasite.client.hospay.common.constant.Constant;
import com.kasite.client.hospay.module.bill.client.init.HisService;
import com.kasite.client.hospay.module.bill.client.stub.HisClient;
import com.kasite.client.hospay.module.bill.client.stub.HisClientHL7;
import com.kasite.client.hospay.module.bill.client.stub.SoapInvocationClient;
import com.kasite.client.hospay.module.bill.entity.bill.bo.HisBill;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kasite.client.hospay.module.bill.client.stub.HisClientHL7.getNowDateStr;

/**
 * @author cc
 * TODO 长乐妇幼保健院HIS接口实现类
 */
@Component
public class HisService_CLFYBJY implements HisService{

     private final static Logger logger = LoggerFactory.getLogger(HisService_CLFYBJY.class);

     @Autowired
     HisClient hisClient;

     @Autowired
     SoapInvocationClient soapInvocationClient;

     @Autowired
     HisClientHL7 hisClientHL7;

     @Override
     public List<HisBill> queryHisOrderBillListSoap(String startDate, String endDate) {
          return null;
     }

     /**
      * 查询HIS接口获取账单列表(每日)
      * 金额计算使用此函数(ArithUtil.mul(XMLUtil.getString(exams.get(i), "PayMoney",false),"100"))
      * @return
      * TODO startDate与endDate格式也许无法满足HIS接口的标准,可使用CommonUtils.dateChange("date","format")进行转换
      * TODO 当前时间格式是yyyy-MM-dd HH:mm:ss
      */
     @Override
     public List<HisBill> queryHisOrderBillListHL7(String startDate, String endDate) throws Exception {

          List<String> reqResultMap = new ArrayList<String>();
          Map<String, String> paramMap = new HashMap<>(16);
          paramMap.clear();
          /** 处理入参*/
          paramMap.put("endDate",endDate.replace("-", ""));
          try {
               paramMap.put("startDate", DateOper.addDate(startDate, -1).replace("-", ""));
          } catch (ParseException e1) {
               e1.printStackTrace();
          }
          paramMap.put("type", "1");
          String nowDateStr = getNowDateStr();
          String reqParam = "MSH|^~\\&|APP|健康之路|HIS|接收设备名称|" + nowDateStr + "||QBP^Q13^QBP_Q13|QRY_Reconciliation-"
                  + nowDateStr.replace(".", "") + "|P|2.7\n"
                  + "QPD|Q13^QRY_Reconciliation^HL7|QR0001|{startDate}|{endDate}|{type}|\n"
                  + "RDF|12|单据号^NM^50~姓名^ST^50~缴费金额^NM^50~支付方式^ST^20~出票单位^ST^50~出票开户银行^ST^50~票据流水号^NM^50~操作日期^DMT^50~操作时间^DMT^50~操作员^ST^50~病人余额^NM^50~机构名称^ST^50\n"
                  + "RCP|I|页数^PG&每页记录数";

          /** 参数格式化*/
          String reqParam1 = CommonUtil.formateReqParam(reqParam, paramMap);
          reqResultMap.add(reqParam1);
          paramMap.put("type", "3");
          reqParam1 =CommonUtil.formateReqParam(reqParam, paramMap);
          reqResultMap.add(reqParam1);
          logger.info("调用HIS账单接口入参:{}", "门诊："+reqResultMap.get(0)+"住院："+reqResultMap.get(1));

          /** 调用多次HIS接口统一由进行封装调用*/
          List<List<String[]>> allBills = new ArrayList<>();
          for (int i = 0; i < reqResultMap.size(); i++) {
               List<String[]> list = hisClientHL7.queryAllBills(reqResultMap.get(i));
               allBills.add(list);
          }

          logger.info("调用HIS账单接口结果集:{}", allBills.size());
          List<HisBill> list = new ArrayList<>();

          HisBill hisBill;
          //先循环结果集
          for (List<String[]> bill : allBills) {
               for (String[] allBill : bill) {
                    hisBill = new HisBill();
                    if (allBill.length >= 13) {
                         hisBill.setOrderId(allBill[13]);
                    }
                    hisBill.setRefundOrderId("");
                    hisBill.setMerchOrderNo(allBill[7]);
                    hisBill.setHisOrderId(allBill[1]);
                    hisBill.setPayMoney(ArithUtil.mul(allBill[3], "100"));
                    hisBill.setTotalMoney(ArithUtil.mul(allBill[3], "100"));
                    hisBill.setRefundMoney(ArithUtil.mul(allBill[3], "100"));
                    if (allBill.length >= 13) {
                         hisBill.setPriceName(allBill[12]);
                         hisBill.setOrderMemo(allBill[12]);
                    }

                    hisBill.setHisOrderType(Constant.CHARGEORDERSTATE);
                    hisBill.setHisBizState(Constant.CHARGEORDERSTATE);
                    /** 这个需要根据项目的情况填写,目前默认微信*/
                    hisBill.setChannelId(Constant.WXCHANNELID);
                    /** 当前对账版本此字段置空，如后期上线统一支付平台此字段必须赋值，可通过订单号去订单表里进行查询*/
                    hisBill.setChannelName("");
                    try {
                         hisBill.setTransDate(Timestamp.valueOf(DateOper.formatDate(allBill[8] + " " + allBill[9],
                                 "yyyyMMdd HH:mm:ss", "yyyy-MM-dd HH:mm:ss")));
                    } catch (ParseException e) {
                         e.printStackTrace();
                    }
                    list.add(hisBill);
               }
          }
          return list;
     }

     @Override
     public List<HisBill> queryHisOrderState(String orderId, String startDate, String endDate) {
          return null;
     }


     public static void main(String[] args) throws Exception {
          //**********************SOAP调用demo************************
//          String soapActionHeader = "http://tempuri.org/IHospitalService/DoDHSP";
//          String soapParam = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
//                  "   <soapenv:Header/>\n" +
//                  "   <soapenv:Body>\n" +
//                  "      <tem:DoDHSP>\n" +
//                  "         <!--Optional:-->\n" +
//                  "         <tem:request><![CDATA[<request><hospital_id>fykqyy</hospital_id><password>698D51A19D8A121CE581499D7B701668</password><check>66F2FA0EA</check><fun_id>003_012</fun_id><transaction_id>000001</transaction_id><openid>02</openid><input><businessID>086591000A2017091815151900747</businessID><businessType>1</businessType></input></request>]]></tem:request>\n" +
//                  "      </tem:DoDHSP>\n" +
//                  "   </soapenv:Body>\n" +
//                  "</soapenv:Envelope>";
//          SoapInvocationClient hisService = new SoapInvocationClient();
//          System.out.println(hisService.soapInvocation(soapActionHeader, soapParam));

          HisService_CLFYBJY hisService = new HisService_CLFYBJY();
          List<HisBill> list = hisService.queryHisOrderBillListHL7("2018-11-09 00:00:00", "2018-11-09 23:59:59");
          for (HisBill hisBill : list) {
               System.out.println(hisBill.getOrderId());
               System.out.println(hisBill.getHisOrderId());
          }

          //************************HL7调用demo**************************
//          String startDate = "20180928";
//          String endDate = "20180928";
//          String type = "1";
//          HisServiceImpl hisService1 = new HisServiceImpl();
//          System.out.println(hisService1.queryHisOrderBillListHL7(startDate, endDate, type));

     }
}

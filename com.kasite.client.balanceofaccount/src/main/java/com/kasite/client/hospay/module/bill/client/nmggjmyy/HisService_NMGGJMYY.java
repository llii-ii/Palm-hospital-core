package com.kasite.client.hospay.module.bill.client.nmggjmyy;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kasite.client.hospay.module.bill.client.stub.HisClientHL7.getNowDateStr;

/**
 * @author cc
 */
@Component
public class HisService_NMGGJMYY implements HisService{

     private final static Logger logger = LoggerFactory.getLogger(HisService_NMGGJMYY.class);

     @Autowired
     HisService_NMGGJMYY hisClient;

     @Autowired
     SoapInvocationClient soapInvocationClient;

     @Autowired
     HisClientHL7 hisClientHL7;

     /**
      * 查询HIS接口获取账单列表(每日)
      * 金额计算使用此函数(ArithUtil.mul(XMLUtil.getString(exams.get(i), "PayMoney",false),"100"))
      * @return
      * TODO startDate与endDate格式也许无法满足HIS接口的标准,可使用CommonUtils.dateChange("date","format")进行转换
      * TODO 当前时间格式是yyyy-MM-dd HH:mm:ss
      */
     @Override
     public List<HisBill> queryHisOrderBillListSoap(String startDate, String endDate) throws Exception {

          logger.info("开始拼接调用HIS账单接口的入参!!!");

          /**
           * TODO 以下的入参都按照实施的实际情况进行修改，无需按照当前的标准
           */

//          String api = "QueryHisOrderBillList";
//
//          String code = "JK6003";
          /**
           * 这一块是调用HIS的入参 [HisOrderType 1支付订单,2退款订单,0或空则获取所有]
           */
//          String param = "<Req>" +
//                  "  <oracode>00001</oracode>" +
//                  "  <oraauthcode>zs0057905231</oraauthcode>" +
//                  "  <TransactionCode>"+code+"</TransactionCode>" +
//                  "  <PIndex>0</PIndex>" +
//                  "  <PSize>100000</PSize>" +
//                  "  <OrderId>12efd8eced414bd7abac8f98290bff8f</OrderId>" +
//                  "  <RefundOrderId/>" +
//                  "  <ChanncelOrderId/>" +
//                  "  <HisOrderId/>" +
//                  "  <BeginDate>"+startDate+"</BeginDate>" +
//                  "  <EndDate>"+endDate+"</EndDate>" +
//                  "  <HisOrderType>1</HisOrderType>" +
//                  "</Req>";
          String param = "<Req>" + 
          		"<TransactionCode>QueryHisOrderBillList</TransactionCode>"+ 
          		"<Data>"+ 
          		"<BeginDate>"+startDate.substring(0, 10)+"</BeginDate>"+ 
          		"<EndDate>"+endDate.substring(0, 10)+"</EndDate>"+ 
          		"<Page>"+ 
          		"<PIndex>0</PIndex>"+ 
          		"<PSize>100000</PSize>"+ 
          		"</Page>"+ 
          		"</Data>"+ 
          		"</Req>";

          /** 用于接口识别，可使用soapUi进行获取,如果无则传空字符串*/
//          String soapAction = "urn:CallInterface";
          String soapAction = "urn:getService";

          /** 用于接口方法调用，可使用soapUi进行获取，请将参数填写完整后直接调用soapInvocation（）即可，请勿修改soapInvocation（）方法*/
//          String soapParam = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:zys=\"http://www.zysoft.com.cn/\">"+
//                  "<soapenv:Header/>" +
//                  "<soapenv:Body>" +
//                  "<zys:CallInterface>" +
//                  "<zys:msgHeader><![CDATA[<root><serverName>"+api+"</serverName><format>xml</format><callOperator></callOperator><certificate>vhHQTHAF+yOo7CJfOKgPDO+Fvny7d92J</certificate></root>]]></zys:msgHeader>" +
//                  "<zys:msgBody><![CDATA[<root><tradeCode>"+code+"</tradeCode><requestXml>"+param+"</requestXml></root>]]></zys:msgBody>" +
//                  "</zys:CallInterface>" +
//                  "</soapenv:Body>" +
//                  "</soapenv:Envelope>";
          String soapParam = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://WebService.HisService\">"+ 
          		"<soapenv:Header/>"+ 
          		"<soapenv:Body>"+ 
          		"<web:getService>"+ 
          		"<web:inValue>"+ 
          		"<![CDATA["+param+"]]></web:inValue>"+ 
          		"      </web:getService>"+ 
          		"   </soapenv:Body>"+ 
          		"</soapenv:Envelope>";

          logger.info("调用HIS账单接口的入参拼接完成,SoapAction:{}\r\n,SoapParam:{}",soapAction,soapParam);

          /** 调用HIS下载账单接口*/
          SoapInvocationClient soapInvocationClient = new SoapInvocationClient();
          String result = soapInvocationClient.soapInvocation(soapAction,soapParam);
          List<HisBill> list = new ArrayList<>();

          try {
               HisBill hisBill;
//               System.out.println("回i  "+result);
//               System.out.println(result.substring(result.indexOf("<Resp>"), result.indexOf("</ns:return>")));
               Document xml = XMLUtil.parseXml(result);
               Element root = xml.getRootElement();
               List<Element> exams = root.selectNodes("//Data");
               logger.info("总记录数:{}", exams.size());
               for (int i = 0; i < exams.size(); i++) {
                    hisBill = new HisBill();
                    hisBill.setOrderId(XMLUtil.getString(exams.get(i), "OrderId",false));
                    hisBill.setRefundOrderId(XMLUtil.getString(exams.get(i), "RefundOrderId",false));
                    hisBill.setMerchOrderNo(XMLUtil.getString(exams.get(i), "WxOrderNo",false));
                    hisBill.setHisOrderId(XMLUtil.getString(exams.get(i), "HisOrderId", false));
                    hisBill.setPayMoney((ArithUtil.mul(XMLUtil.getString(exams.get(i), "PayMoney",false),"100")));
                    hisBill.setTotalMoney((ArithUtil.mul(XMLUtil.getString(exams.get(i), "TotalMoney",false),"100")));
                    hisBill.setRefundMoney((ArithUtil.mul(XMLUtil.getString(exams.get(i), "RefundMoney",false),"100")));
                    hisBill.setPriceName(XMLUtil.getString(exams.get(i), "PriceName",false));
                    hisBill.setOrderMemo(XMLUtil.getString(exams.get(i), "OrderMemo",false));
                    //HIS订单类型1支付2退费*/
                    hisBill.setHisOrderType(XMLUtil.getString(exams.get(i), "HisOrderState", false));
                    //HIS订单业务状态0未消费1已消费-1未知*/
                    hisBill.setHisBizState(XMLUtil.getString(exams.get(i), "HisBizState", false));
                    //未知   his未返回
                    hisBill.setChannelId(XMLUtil.getString(exams.get(i), "ChannelId", false));
                    /** 当前对账版本此字段置空，如后期上线统一支付平台此字段必须赋值，可通过订单号去订单表里进行查询*/
                    hisBill.setChannelName("");
                    String hisOrderState= XMLUtil.getString(exams.get(i), "HisOrderState", false);
                    String refundDate = XMLUtil.getString(exams.get(i), "RefundDate", false);
                    String payDate=XMLUtil.getString(exams.get(i), "PayDate", false);
                    //转换String的时间为TimeStamp类型
                    hisBill.setTransDate(Timestamp.valueOf(hisOrderState.equals("2")?refundDate:payDate));
                    list.add(hisBill);
               }
          } catch (AbsHosException e) {
               e.printStackTrace();
               logger.error("XML解析异常:{}", e.getMessage());
          }
          return list;
     }


     /**
      * 获取HIS账单
      * @param startDate
      * @param endDate
      * @return
      */
     @Override
     public List<HisBill> queryHisOrderBillListHL7(String startDate, String endDate) {

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
//          SoapInvocationClient hisServiceImpl = new SoapInvocationClient();
//          System.out.println(hisServiceImpl.soapInvocation(soapActionHeader, soapParam));

          HisService_NMGGJMYY hisServiceImpl = new HisService_NMGGJMYY();
          List<HisBill> list = hisServiceImpl.queryHisOrderBillListSoap("2018-11-09 00:00:00", "2018-11-09 23:59:59");
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


	@Override
	public List<HisBill> queryHisOrderState(String orderId, String startDate, String endDate) {
		// TODO Auto-generated method stub
		return null;
	}
}

package com.kasite.client.hospay.module.bill.client.xmhcyy;

import com.kasite.client.hospay.module.bill.client.init.HisService;
import com.kasite.client.hospay.module.bill.client.stub.HisClient;
import com.kasite.client.hospay.module.bill.client.stub.HisClientHL7;
import com.kasite.client.hospay.module.bill.client.stub.SoapInvocationClient;
import com.kasite.client.hospay.module.bill.entity.bill.bo.HisBill;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
/**
 * @author cc
 * TODO 厦门海沧医院调用HIS账单接口实现类
 */
@Component
public class HisService_XMHCYY implements HisService{

     private final static Logger logger = LoggerFactory.getLogger(HisService_XMHCYY.class);

     @Autowired
     HisClient hisClient;

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
     public List<HisBill> queryHisOrderBillListSoap(String startDate,String endDate) throws Exception {

          logger.info("开始拼接调用HIS账单接口的入参!!!");

          /**
           * TODO 以下的入参都按照实施的实际情况进行修改，无需按照当前的标准
           */

          String api = "QueryHisOrderBillList";

          String code = "JK6003";
          /**
           * 这一块是调用HIS的入参 [HisOrderType 1支付订单,2退款订单,0或空则获取所有]
           */
          String param = "<Req>" +
                  "  <oracode>00001</oracode>" +
                  "  <oraauthcode>zs0057905231</oraauthcode>" +
                  "  <TransactionCode>"+code+"</TransactionCode>" +
                  "  <PIndex>0</PIndex>" +
                  "  <PSize>100000</PSize>" +
                  "  <OrderId></OrderId>" +
                  "  <RefundOrderId/>" +
                  "  <ChanncelOrderId/>" +
                  "  <HisOrderId/>" +
                  "  <BeginDate>"+startDate+"</BeginDate>" +
                  "  <EndDate>"+endDate+"</EndDate>" +
                  "  <HisOrderType>1</HisOrderType>" +
                  "</Req>";

          /** 用于接口识别，可使用soapUi进行获取,如果无则传空字符串*/
          String soapAction = "urn:CallInterface";

          /** 用于接口方法调用，可使用soapUi进行获取，请将参数填写完整后直接调用soapInvocation（）即可，请勿修改soapInvocation（）方法*/
          String soapParam = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:zys=\"http://www.zysoft.com.cn/\">\n" +
                  "   <soapenv:Header/>\n" +
                  "   <soapenv:Body>\n" +
                  "      <zys:CallInterface>\n" +
                  "         <!--Optional:-->\n" +
                  "         <zys:msgHeader><![CDATA[<root><serverName>"+api+"</serverName><format>xml</format><callOperator></callOperator><certificate>vhHQTHAF+yOo7CJfOKgPDO+Fvny7d92J</certificate></root>]]></zys:msgHeader>\n" +
                  "         <!--Optional:-->\n" +
                  "         <zys:msgBody><![CDATA[<root><tradeCode>"+code+"</tradeCode><requestXml>"+param+"</requestXml></root>]]></zys:msgBody>\n" +
                  "      </zys:CallInterface>\n" +
                  "   </soapenv:Body>\n" +
                  "</soapenv:Envelope>";

          logger.info("调用HIS账单接口的入参拼接完成,SoapAction:{}\r\n,SoapParam:{}",soapAction,soapParam);

          /** 调用HIS下载账单接口*/
          SoapInvocationClient soapInvocationClient = new SoapInvocationClient();
          String result = soapInvocationClient.soapInvocation(soapAction,soapParam);
          List<HisBill> list = new ArrayList<>();

          try {
               HisBill hisBill;
               Document xml = XMLUtil.parseXml(result);
               Element root = xml.getRootElement();
               List<Element> exams = root.selectNodes("//Resp//Data");
               logger.info("总记录数:{}", exams.size());
               for (int i = 0; i < exams.size(); i++) {
                    hisBill = new HisBill();
                    hisBill.setOrderId(XMLUtil.getString(exams.get(i), "OrderId",false));
                    hisBill.setRefundOrderId(XMLUtil.getString(exams.get(i), "RefundOrderId",false));
                    hisBill.setHisOrderId(XMLUtil.getString(exams.get(i), "HisOrderId", false));
                    hisBill.setMerchOrderNo(XMLUtil.getString(exams.get(i), "MerchOrderNo",false));
                    hisBill.setPayMoney(XMLUtil.getString(exams.get(i), "PayMoney",false));
                    hisBill.setTotalMoney(XMLUtil.getString(exams.get(i), "TotalMoney",false));
                    hisBill.setRefundMoney(XMLUtil.getString(exams.get(i), "RefundMoney",false));
                    hisBill.setPriceName(XMLUtil.getString(exams.get(i), "PriceName",false));
                    hisBill.setOrderMemo(XMLUtil.getString(exams.get(i), "OrderMemo",false));
                    hisBill.setHisOrderType(XMLUtil.getString(exams.get(i), "HisOrderType", false));
                    hisBill.setHisBizState(XMLUtil.getString(exams.get(i), "HisBizState", false));
                    hisBill.setChannelId(XMLUtil.getString(exams.get(i), "ChannelId", false));
                    /** 当前对账版本此字段置空，如后期上线统一支付平台此字段必须赋值，可通过订单号去订单表里进行查询*/
                    hisBill.setChannelName("");
                    //转换String的时间为TimeStamp类型
                    hisBill.setTransDate(Timestamp.valueOf(XMLUtil.getString(exams.get(i), "TransDate", false)));
                    list.add(hisBill);
               }
          } catch (AbsHosException e) {
               logger.error("XML解析异常:{}", e.getMessage());
               e.printStackTrace();

          }
          return list;
     }

     @Override
     public List<HisBill> queryHisOrderBillListHL7(String startDate, String endDate) {
          return null;
     }

     @Override
     public List<HisBill> queryHisOrderState(String orderId, String startDate, String endDate) throws Exception {
          logger.info("开始拼接调用HIS订单状态查询接口的入参!!!");

          /**
           * TODO 以下的入参都按照实施的实际情况进行修改，无需按照当前的标准
           */

          String api = "QueryHisOrderBillList";

          String code = "JK6003";
          /**
           * 这一块是调用HIS的入参 [HisOrderType 1支付订单,2退款订单,0或空则获取所有]
           */
          String param = "<Req>" +
                  "  <oracode>00001</oracode>" +
                  "  <oraauthcode>zs0057905231</oraauthcode>" +
                  "  <TransactionCode>"+code+"</TransactionCode>" +
                  "  <PIndex>0</PIndex>" +
                  "  <PSize>100000</PSize>" +
                  "  <OrderId>"+orderId+"</OrderId>" +
                  "  <RefundOrderId/>" +
                  "  <ChanncelOrderId/>" +
                  "  <HisOrderId/>" +
                  "  <BeginDate>"+startDate+"</BeginDate>" +
                  "  <EndDate>"+endDate+"</EndDate>" +
                  "  <HisOrderType>1</HisOrderType>" +
                  "</Req>";

          /** 用于接口识别，可使用soapUi进行获取,如果无则传空字符串*/
          String soapAction = "urn:CallInterface";

          /** 用于接口方法调用，可使用soapUi进行获取，请将参数填写完整后直接调用soapInvocation（）即可，请勿修改soapInvocation（）方法*/
          String soapParam = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:zys=\"http://www.zysoft.com.cn/\">\n" +
                  "   <soapenv:Header/>\n" +
                  "   <soapenv:Body>\n" +
                  "      <zys:CallInterface>\n" +
                  "         <!--Optional:-->\n" +
                  "         <zys:msgHeader><![CDATA[<root><serverName>"+api+"</serverName><format>xml</format><callOperator></callOperator><certificate>vhHQTHAF+yOo7CJfOKgPDO+Fvny7d92J</certificate></root>]]></zys:msgHeader>\n" +
                  "         <!--Optional:-->\n" +
                  "         <zys:msgBody><![CDATA[<root><tradeCode>"+code+"</tradeCode><requestXml>"+param+"</requestXml></root>]]></zys:msgBody>\n" +
                  "      </zys:CallInterface>\n" +
                  "   </soapenv:Body>\n" +
                  "</soapenv:Envelope>";

          logger.info("调用HIS订单状态查询接口的入参拼接完成,SoapAction:{}\r\n,SoapParam:{}",soapAction,soapParam);

          /** 调用HIS订单状态查询接口*/
          SoapInvocationClient soapInvocationClient = new SoapInvocationClient();
          String result = soapInvocationClient.soapInvocation(soapAction,soapParam);
          List<HisBill> list = new ArrayList<>();

          try {
               HisBill hisBill;
               Document xml = XMLUtil.parseXml(result);
               Element root = xml.getRootElement();
               List<Element> exams = root.selectNodes("//Resp//Data");
               logger.info("总记录数:{}", exams.size());
               for (int i = 0; i < exams.size(); i++) {
                    hisBill = new HisBill();
                    hisBill.setMerchOrderNo(XMLUtil.getString(exams.get(i), "MerchOrderNo",false));
                    hisBill.setPayMoney(XMLUtil.getString(exams.get(i), "PayMoney",false));
                    hisBill.setOrderId(XMLUtil.getString(exams.get(i), "OrderId",false));
                    hisBill.setRefundOrderId(XMLUtil.getString(exams.get(i), "RefundOrderId",false));
                    hisBill.setHisOrderId(XMLUtil.getString(exams.get(i), "HisOrderId", false));
                    hisBill.setTotalMoney(XMLUtil.getString(exams.get(i), "TotalMoney",false));
                    hisBill.setRefundMoney(XMLUtil.getString(exams.get(i), "RefundMoney",false));
                    hisBill.setPriceName(XMLUtil.getString(exams.get(i), "PriceName",false));
                    hisBill.setOrderMemo(XMLUtil.getString(exams.get(i), "OrderMemo",false));
                    hisBill.setHisOrderType(XMLUtil.getString(exams.get(i), "HisOrderType", false));
                    hisBill.setHisBizState(XMLUtil.getString(exams.get(i), "HisBizState", false));
                    hisBill.setChannelId(XMLUtil.getString(exams.get(i), "ChannelId", false));
                    /** 当前对账版本此字段置空，如后期上线统一支付平台此字段必须赋值，可通过订单号去订单表里进行查询*/
                    hisBill.setChannelName("");
                    //转换String的时间为TimeStamp类型
                    hisBill.setTransDate(Timestamp.valueOf(XMLUtil.getString(exams.get(i), "TransDate", false)));
                    list.add(hisBill);
               }
          } catch (AbsHosException e) {
               logger.error("XML解析异常:{}", e.getMessage());
               e.printStackTrace();
          }
          return list;
     }

}

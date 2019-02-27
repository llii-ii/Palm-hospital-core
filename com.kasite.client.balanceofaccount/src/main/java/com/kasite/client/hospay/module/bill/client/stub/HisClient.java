package com.kasite.client.hospay.module.bill.client.stub;

import com.kasite.client.hospay.common.constant.Constant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.httpclient.http.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author cc
 * TODO 调用HIS账单接口
 */
@Component
public class HisClient {

     private final static Logger logger = LoggerFactory.getLogger(HisClient.class);

     /**
      * 在初始化spring容器时为静态变量赋值
      */
     public static String HisUrl;

     @Value("${HisConfig.url}")
     public String url;

     @PostConstruct
     public void init(){
          HisUrl = url;
     }

     public String service(String params) {

          logger.info("调用HIS入参:{}",params);
          String result;
          String param = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:tem=\"http://tempuri.org/\">\n" +
                  "   <soap:Header/>\n" +
                  "   <soap:Body>\n" +
                  "      <tem:QueryHisOrderBillList>\n" +
                  "         <!--Optional:-->\n" +
                  "         <tem:inXML>" +
                  "           <![CDATA["+params+"]]>" +
                  "         </tem:inXML>\n" +
                  "      </tem:QueryHisOrderBillList>\n" +
                  "   </soap:Body>\n" +
                  "</soap:Envelope>";
          SoapResponseVo vo = HttpRequestBus.create(HisUrl, RequestType.soap1).setParam(param).send();
          if (vo.getCode() == Constant.HTTPCODE) {
               String r = vo.getResult();
               result = formatResp(r);
               logger.info("调用成功CODE:{}",vo.getCode());
               return result;
          }
          logger.info("调用HIS接口异常:{}",vo.getResult());
          return CommonUtil.getRetVal(null,Constant.DOWNLOADHISBILLS, RetCode.Common.ERROR_CALLHIS.getCode(), RetCode.Common.ERROR_CALLHIS.getMessage());
     }

     public static String formatResp(String param){
          if (StringUtils.isNotBlank(param)) {
               param = StringEscapeUtils.unescapeXml(param);
               if (StringUtils.isNotBlank(param) && param.contains("QueryHisOrderBillListResult")) {
                    int begin = param.indexOf("<QueryHisOrderBillListResult>");
                    int end = param.indexOf("</QueryHisOrderBillListResult>");
                    param = param.substring(begin+29, end);
               }
          }
          return param;
     }
}

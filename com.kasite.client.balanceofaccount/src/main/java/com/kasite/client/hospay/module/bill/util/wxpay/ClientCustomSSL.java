package com.kasite.client.hospay.module.bill.util.wxpay;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.Map;

import javax.net.ssl.SSLContext;

import com.kasite.client.hospay.module.bill.entity.bill.bo.RequestHandlerParam;
import com.kasite.core.common.util.StringUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author cc
 * 微信SSL 退款交互处理
 */
@Component
public class ClientCustomSSL {

     private static final Logger log = LoggerFactory.getLogger(ClientCustomSSL.class);

     private final static int  RETRY_COUNT = 3;

     @Autowired
     RequestHandlerParam requestHandlerParam;

     public String call(String arrayToXml) throws Exception {
          String refundUrl = requestHandlerParam.refundUrl;
          //商户号，密码
          String merchantId = requestHandlerParam.merchantId;
          if( !StringUtil.isEmpty(requestHandlerParam.parentAppId) ) {
               merchantId = requestHandlerParam.parentMerchantId;
          }

          KeyStore keyStore = KeyStore.getInstance("PKCS12");
          FileInputStream inStream = new FileInputStream(new File(requestHandlerParam.certPath));
          try {
               keyStore.load(inStream, merchantId.toCharArray());
          } catch (Exception e) {
               e.printStackTrace();
               log.error(e.getLocalizedMessage());
          } finally {
               inStream.close();
          }
          SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, merchantId.toCharArray()).build();
          // Allow TLSv1 protocol only
          SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
          CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
          String xmlStr="";
          int n = 0;
          try {
               long flag = System.currentTimeMillis();
               log.info("\r\n"+"["+flag+"]微信退款处理发送：\r\n" + arrayToXml);
               //可能报超时异常，3次机会
               while(n++<RETRY_COUNT){
                    try {
                         HttpPost httpPost = new HttpPost(refundUrl);
                         httpPost.setEntity(new StringEntity(arrayToXml, "UTF-8"));
                         CloseableHttpResponse response = httpclient.execute(httpPost);
                         xmlStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                         response.close();
                         break;
                    }catch (IOException e) {
                         e.printStackTrace();
                         log.info("\r\n休息一秒");
                         //休息一秒
                         Thread.sleep(1000);
                    }
               }
               log.info("\r\n"+"["+flag+"]微信退款处理返回：\r\n" + xmlStr);
          } catch (Exception e) {
               e.printStackTrace();
               log.error(e.getLocalizedMessage());
          } finally {
               httpclient.close();
          }

          return  xmlStr;
     }



     /**
      * 调用微信退费
      * @param configMap
      * @param arrayToXml
      * @return
      * @throws Exception
      */
     public static String call(Map configMap, String arrayToXml) throws Exception {
          String certPath = configMap.get("certpath").toString();
          String refundUrl = configMap.get("refundurl").toString();
          //商户号，密码
          String merchantId = configMap.get("merchantid").toString();

          KeyStore keyStore = KeyStore.getInstance("PKCS12");
          FileInputStream inStream = new FileInputStream(new File(certPath));
          try {
               keyStore.load(inStream, merchantId.toCharArray());
          } finally {
               inStream.close();
          }
          SSLContext sslcontext = SSLContexts.custom()
                  .loadKeyMaterial(keyStore, merchantId.toCharArray()).build();
          // Allow TLSv1 protocol only
          SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                  sslcontext, new String[] { "TLSv1" }, null,
                  SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
          CloseableHttpClient httpclient = HttpClients.custom()
                  .setSSLSocketFactory(sslsf).build();
          String xmlStr="";
          try {
               HttpPost httpPost = new HttpPost(refundUrl);
               httpPost.setEntity(new StringEntity(arrayToXml, "UTF-8"));
               System.out.println(httpPost.getURI());
               System.out.println("微信退款处理发送：" + arrayToXml);
               CloseableHttpResponse response = httpclient.execute(httpPost);
               xmlStr = EntityUtils.toString(response.getEntity(), "UTF-8");
               System.out.println("微信退款处理返回：" + xmlStr);
               response.close();
          } finally {
               httpclient.close();
          }
          return  xmlStr;
     }
}

package com.kasite.client.hospay.module.bill.util;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cc
 */
public class HttpPostUtils {


     public static String httpPost(String url, Map<String,Object> parameterMap, Map<String,String> requestHeaderMap, NameValuePair[] data) {
          String returnStr = "";
          HttpClient client = new HttpClient();
          PostMethod postMethod = new PostMethod(url);
          try {
               if(parameterMap!=null && parameterMap.size()>0){
                    for(String key : parameterMap.keySet()){
                         postMethod.getParams().setParameter(key, parameterMap.get(key));
                    }
               }
               if(requestHeaderMap!=null && requestHeaderMap.size()>0){
                    for(String key : requestHeaderMap.keySet()){
                         postMethod.setRequestHeader(key, requestHeaderMap.get(key));
                    }
               }

               // url的连接等待超时时间设置
               client.getHttpConnectionManager().getParams().setConnectionTimeout(60000);

               // 读取数据超时时间设置
               client.getHttpConnectionManager().getParams().setSoTimeout(40000);

               // 将表单的值放入postMethod中
               postMethod.setRequestBody(data);

               // 执行postMethod
               int statusCode = client.executeMethod(postMethod);

               for (Header header : postMethod.getResponseHeaders()) {
                    System.out.println(header.getName() + " : " + header.getValue());
               }
               // 判断回复响应状态是否正常
               if (statusCode == HttpStatus.SC_OK) {
                    returnStr = postMethod.getResponseBodyAsString();
                    System.out.println("===returnStr==>"+returnStr);
               }
          } catch (Exception ex) {
               ex.printStackTrace();
          } finally {
               // 释放连接
               try {
                    postMethod.releaseConnection();
               } catch (Exception e) {
                    e.getStackTrace();
               }
          }
          return returnStr;
     }

     private static String httpPost(String url, NameValuePair[] data) {
          Map<String,Object> parameterMap = new HashMap<String, Object>(16);
          parameterMap.put(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
          return httpPost(url, parameterMap, null, data);
     }


     public static String httpPost(String url,Map<String, String> map) {
          List<NameValuePair> params=new ArrayList<NameValuePair>();
          if(map!=null && map.size()>0){
               for(String key : map.keySet()){
                    String value = map.get(key);
                    NameValuePair nameValuePair = new NameValuePair(key,value);
                    params.add(nameValuePair);
               }
          }
          NameValuePair[] nameValuePairs= (NameValuePair[])params.toArray(new NameValuePair[params.size()]);
          return httpPost(url, nameValuePairs);

     }

     /**
      * 上传表单数据
      * @param url 文件地址
      * @param stringMap 字符串表单域
      * @param fileMap 文件
      * @return
      */
     public static String httpClientSubmitForm(String url,Map<String, String> stringMap,Map<String, File> fileMap){
          String result="";
          String responseStr = "";
          PostMethod postMethod = null;
          HttpClient httpClient = null;
          try{
               if(StringUtils.isNotBlank(url)){
                    postMethod = new PostMethod(url);
                    httpClient = new HttpClient();
                    postMethod.getParams().setParameter("http.protocol.content-charset", "utf-8");

                    List<Part> partList = new ArrayList<Part>();
                    //构造表单数据
                    if(stringMap!=null && stringMap.size()>0){
                         for(String key : stringMap.keySet()){
                              String value = stringMap.get(key);
                              if(value!=null && !"".equals(value)){
                                   StringPart part = new StringPart( key,value);
                                   partList.add(part);
                              }
                         }
                    }
                    if(fileMap!=null && fileMap.size()>0){
                         for(String key : fileMap.keySet()){
                              File value = fileMap.get(key);
                              if(value!=null && value.exists()){
                                   FilePart part = new FilePart( key,value);
                                   partList.add(part);
                              }
                         }
                    }
                    Part[] parts= (Part[])partList.toArray(new Part[partList.size()]);
                    MultipartRequestEntity mrp =  new MultipartRequestEntity(parts , postMethod.getParams());
                    postMethod.setRequestEntity(mrp);


                    int statusCode;
                    //执行postMethod
                    statusCode = httpClient.executeMethod(postMethod);
                    //获得返回的内容
                    responseStr = postMethod.getResponseBodyAsString();
                    if (HttpStatus.SC_OK == statusCode) {
                         result = responseStr;
                    }
               }

          }catch(Exception e){
               e.printStackTrace();
               result = "";
          }finally{
               if(postMethod!=null){
                    postMethod.releaseConnection();//释放连接
               }
               //删除本地文件
               if(fileMap!=null && fileMap.size()>0){
                    for(String key : fileMap.keySet()){
                         File value = fileMap.get(key);
                         if(value!=null && value.exists()){
                              value.delete();
                         }
                    }
               }
          }
          System.out.println("--result-->"+result);
          return result;
     }
}

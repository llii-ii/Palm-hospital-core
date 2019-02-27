package com.yihu.wsgw.api;

import org.apache.commons.httpclient.methods.PostMethod;

public class UTF8PostMethod
  extends PostMethod
{
  public UTF8PostMethod(String url)
  {
    super(url);
  }
  
  public String getRequestCharSet()
  {
    return "UTF-8";
  }
}

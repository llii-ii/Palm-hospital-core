package com.coreframework.vo;

import java.io.Serializable;

public class ReturnValue
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private int code;
  private String message;
  
  public ReturnValue(int code, String message)
  {
    this.code = code;
    this.message = message;
  }
  
  public ReturnValue() {}
  
  public int getCode()
  {
    return this.code;
  }
  
  public void setCode(int code)
  {
    this.code = code;
  }
  
  public String getMessage()
  {
    return this.message;
  }
  
  public void setMessage(String message)
  {
    this.message = message;
  }
}

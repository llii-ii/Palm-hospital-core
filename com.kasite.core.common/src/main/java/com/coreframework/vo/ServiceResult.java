package com.coreframework.vo;

public class ServiceResult<T>
  extends ReturnValue
{
  private static final long serialVersionUID = 1L;
  private T result;
  
  public ServiceResult(int code, String message)
  {
    super(code, message);
  }
  
  public ServiceResult() {}
  
  public T getResult()
  {
    return (T)this.result;
  }
  
  public void setResult(T result)
  {
    this.result = result;
  }
}

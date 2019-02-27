package com.kasite.core.common.dao.vo;

import java.util.List;


/**
 * 返回对象  分页查询或查询出列表时继承该对象
 * @created 2015-05-31
 * @author lcz
 *
 */
public class RespList extends AbsRespObject{
	
	private Integer Code;
	private String Message;
	private Integer Total;
	private List<?> Result;
	
	
	
	public List<?> getResult() {
		return Result;
	}
	public void setResult(List<?> result) {
		Result = result;
	}
	public Integer getTotal() {
		return Total;
	}
	public void setTotal(Integer total) {
		Total = total;
	}
	public Integer getCode() {
		return Code;
	}
	public String getMessage() {
		return Message;
	}
	public void setCode(Integer code) {
		Code = code;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public RespList(){
		
	}
	public RespList(RetCode ret){
		this.Code = ret.getCode();
		this.Message = ret.getMessage();
	}
	public RespList(Integer code,String message){
		this.Code = code;
		this.Message = message;
	}
	
	public RespList(Integer code,String message,List<?> result){
		this.Code = code;
		this.Message = message;
		this.Result = result;
	}
	
}

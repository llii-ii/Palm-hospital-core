/**
 * 
 */
package com.kasite.core.common.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.req.AbsReq;

/**
 * @author Administrator
 *
 */
public class LogBody {

	public static LogBody  me(AuthInfoVo vo)
	{
		return new LogBody(vo);
	}
	
	private JSONObject json;
	private AuthInfoVo vo;
	
	public AuthInfoVo getVo() {
		return vo;
	}

	public LogBody(AuthInfoVo vo)
	{
		json=new JSONObject();
		if(null != vo) {
			this.vo = vo;
			if(null != vo) {
				json.put("authInfo", vo.toString());
			}
		}
	}
	
	public LogBody(AbsReq req)
	{
		json=new JSONObject();
		if(null != req && null != req.getAuthInfo()) {
			AuthInfoVo vo = req.getAuthInfo();
			this.vo = vo;
			if(null != vo) {
				json.put("authInfo", vo.toString());
			}
		}
		
	}
	public LogBody set(Throwable ex)
	{
		if(ex instanceof InvocationTargetException)
		{   
             ex=((InvocationTargetException)ex).getTargetException();   
		}
		StringWriter sw = new StringWriter();
        PrintWriter w = new PrintWriter(sw);
        ex.printStackTrace(w);
		json.put("exception", sw.toString());
		return this;
	}
	public LogBody set(String key,Object value)
	{
		json.put(key, value);
		return this;
	}
	public LogBody set(JSONObject json)
	{
		json.putAll(json);
//		this.json=json;
		return this;
	}
	@Override
	public String toString()
	{
		return json.toString();
	}
}

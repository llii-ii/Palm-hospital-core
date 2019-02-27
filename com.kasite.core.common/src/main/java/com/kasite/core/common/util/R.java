package com.kasite.core.common.util;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpStatus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.PageVo;
import com.kasite.core.common.resp.RespMap;
import com.yihu.hos.IRetCode;

/**
 * 返回数据
 * 
 * @author admin

 * @date 2016年10月27日 下午9:59:27
 */
public class R extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	public R() {
		put("code", 0);
		put("RespCode", 10000);
	}
	
	public static R error() {
		return error(500, "未知异常，请联系管理员");
	}
	
	public static R error(String msg) {
		return error(500, msg);
	}
	
	public static R error(int code, String msg) {
		R r = new R();
		r.put("code", code);
		r.put("msg", msg);
		r.put("RespCode", code);
		r.put("RespMessage", msg);
		return r;
	}
	public static R error(IRetCode code, String msg) {
		R r = new R();
		r.put("code", code.getCode());
		r.put("msg", msg);
		r.put("RespCode", code.getCode());
		r.put("RespMessage", msg);
		return r;
	}
	public static R error(IRetCode code) {
		R r = new R();
		r.put("code", code.getCode());
		r.put("msg", code.getMessage());
		r.put("RespCode", code.getCode());
		r.put("RespMessage", code.getMessage());
		return r;
	}
	public static R ok(String msg) {
		R r = new R();
		r.put("msg", msg);
		r.put("RespMessage", msg);
		return r;
	}
	/**
	 * 返回list 结果集的时候 返回的结果集合节点为result
	 * @param list
	 * @return
	 */
	public static R ok(List<?> list){
		return R.ok().put("result", list);
	}
	/**
	 * 返回list 结果集的时候 返回的结果集合节点为result
	 * @param list
	 * @return
	 */
	public static R ok(Page<?> page){
		R r = ok();
		JSONObject pageJs = new JSONObject();
		pageJs.put(KstHosConstant.PINDEX, page.getPageNum());
		pageJs.put(KstHosConstant.PSIZE, page.getPageSize());
		pageJs.put(KstHosConstant.PCOUNT, page.getTotal());
		r.put(KstHosConstant.PAGE, pageJs);
		
		if(page.getResult()!=null && page.getResult().size()>0) {
			JSONArray dataArray = new JSONArray();
			for (Object obj : page.getResult()) {
				if(obj instanceof RespMap) {
					RespMap respMap = (RespMap) obj;
					JSONObject dataJs = (JSONObject) JSONObject.toJSON(respMap.getMap());
					dataArray.add(foreachJSON(KstHosConstant.DATA, dataJs));
				}else {
					JSONObject dataJs = (JSONObject) JSONObject.toJSON(obj);
					dataArray.add(foreachJSON(KstHosConstant.DATA, dataJs));
				}
			}
			r.put(KstHosConstant.DATA, dataArray);
		}
		return r;
	}
	public static R ok(List<?> list, PageVo pageVo){
		return R.ok(list).put("page", pageVo);
	}
	
	/**
	 * 遍历JSONObject对象  
	 * 验证哪些接点需要，且节点 首字母是否需要转换为大写
	 * @Description: 
	 * @param json
	 * @return
	 */
	private static JSONObject foreachJSON(String parentKey,JSONObject json) {
		JSONObject newJs = new JSONObject();
		for (Entry<String, Object> entry : json.entrySet()) {
			String key = firstCharToUppercase(entry.getKey());
			if(entry.getValue() instanceof JSONObject) {
				JSONObject resJs = foreachJSON(key,((JSONObject)entry.getValue()));
				newJs.put(key, resJs);
			}else if(entry.getValue() instanceof JSONArray) {
				JSONArray newArray = new JSONArray();
				JSONArray array = (JSONArray)entry.getValue();
				if(array.size()>0 && array.get(0) instanceof JSONObject) {
					for (int i=0;i<array.size();i++) {
						JSONObject js = array.getJSONObject(i);
						JSONObject resJs = foreachJSON(key,js);
						newArray.add(resJs);
					}
					newJs.put(key, newArray);
				}else {
					newJs.put(key, array);
				}
			}else {
				newJs.put(key, entry.getValue()==null?"":entry.getValue());
			}
		}
		return newJs;
	}
	private static String firstCharToUppercase(String key) {
		return key.substring(0,1).toUpperCase()+key.substring(1);
	}
	 
	/**
	 * 返回list 结果集的时候 返回的结果集合节点为result
	 * @param list
	 * @return
	 */
	public static R ok(Set<?> set){
		return R.ok().put("result", set);
	}
	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}
	
	public static R ok() {
		return new R();
	}

	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}
	
	public static R error(int code, Throwable e) {
		R r = new R();
		r.put("code", code);
		r.put("msg", "系统发生异常，请联系管理员.");
		r.put("RespCode", code);
		r.put("RespMessage", "系统发生异常，请联系管理员.");
	 	String exception = StringUtil.getException(e);
	    	if(StringUtil.isNotBlank(exception) && exception.length() > 200){
	    		exception = exception.substring(0, 200);
	    	}
		r.put("ex", exception);
		return r;
	}
	public static R error(Throwable e) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, e);
	}
	
	
	@Override
	public String toString() {
		JSONObject json = (JSONObject) JSON.toJSON(this);
		return json.toJSONString();
	}
}

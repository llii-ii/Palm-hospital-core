package com.kasite.core.common.resp;

import java.util.Map.Entry;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.StringUtil;
import com.kasite.core.common.constant.KstHosConstant;

/**
 * 
 * @className: JsonRespHandler
 * @author: lcz
 * @date: 2018年7月19日 下午7:19:17
 */
public class JsonRespHandler extends BaseRespHandler{


	@Override
	public JSONObject parseJSON() {
		JSONObject respJs = new JSONObject();
		respJs.put(KstHosConstant.TRANSACTIONCODE, data.getTransactionCode());
		respJs.put(KstHosConstant.RESPCODE,  data.getCode());
		respJs.put(KstHosConstant.RESPMESSAGE,  data.getMessage());
		if(data!=null) {
			if(data.getPIndex()!=null && data.getPSize()!=null && data.getPSize()>0) {
				JSONObject pageJs = new JSONObject();
				pageJs.put(KstHosConstant.PINDEX, data.getPIndex());
				pageJs.put(KstHosConstant.PSIZE, data.getPSize());
				pageJs.put(KstHosConstant.PCOUNT, data.getPCount());
				respJs.put(KstHosConstant.PAGE, pageJs);
			}
			if(data.getList()!=null && data.getList().size()>0) {
				JSONArray dataArray = new JSONArray();
				for (Object obj : data.getList()) {
					if(obj instanceof RespMap) {
						RespMap respMap = (RespMap) obj;
						JSONObject dataJs = (JSONObject) JSONObject.toJSON(respMap.getMap());
						dataArray.add(foreachJSON(KstHosConstant.DATA, dataJs));
					}else {
						JSONObject dataJs = (JSONObject) JSONObject.toJSON(obj);
						dataArray.add(foreachJSON(KstHosConstant.DATA, dataJs));
					}
				}
				respJs.put(KstHosConstant.DATA, dataArray);
			}
		}
		return respJs;
	}

	@Override
	public String parseString() {
		
		return this.parseJSON().toString();
	}
	/**
	 * 遍历JSONObject对象  
	 * 验证哪些接点需要，且节点 首字母是否需要转换为大写
	 * @Description: 
	 * @param json
	 * @return
	 */
	private JSONObject foreachJSON(String parentKey,JSONObject json) {
		JSONObject newJs = new JSONObject();
		for (Entry<String, Object> entry : json.entrySet()) {
			String key = firstCharToUppercase(entry.getKey());
			boolean bool = filterColumns(parentKey, key);
			if(!bool) {
				continue;
			}
			if(cfg.isUppercase()) {
				key = firstCharToUppercase(key);
			}
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
	private String firstCharToUppercase(String key) {
		return key.substring(0,1).toUpperCase()+key.substring(1);
	}
	
	/**
	 * 返回字段过滤
	 * @Description: 
	 * @return
	 */
	public boolean filterColumns(String parentKey,String name) {
		if(StringUtil.isNotBlank(cfg.getColumns())) {
			String[] filter = cfg.getColumns().split("\\|");
			for (String ff : filter) {
				String[] arrays = ff.split("=");
				if(parentKey.equalsIgnoreCase(arrays[0])) {
					String[] columns = arrays[1].split(",");
					for (String column : columns) {
						if(name.equalsIgnoreCase(column)) {
							return true;
						}
					}
					return false;
				}
			}
		}
		
		if(StringUtil.isNotBlank(cfg.getExcludeColumns())) {
			String[] filter = cfg.getExcludeColumns().split("\\|");
			for (String ff : filter) {
				String[] arrays = ff.split("=");
				if(parentKey.equalsIgnoreCase(arrays[0])) {
					String[] columns = arrays[1].split(",");
					for (String column : columns) {
						if(name.equalsIgnoreCase(column)) {
							return false;
						}
					}
					return true;
				}
			}
		}
		return true;
	}
}

package com.kasite.core.common.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.kasite.core.common.xss.SQLFilter;

/**
 * 查询参数
 *
 * @author chenshun
 * @email 343675979@qq.com
 * @date 2017-03-14 23:15
 */
public class Query extends LinkedHashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	//当前页码
    private int page;
    //每页条数
    private int limit;
    
    private int offset;

    public Query(Map<String, Object> params) throws Exception{
        this.putAll(params);

        Object pageobj = params.get("page");
        if(null != pageobj){
        	//分页参数
            this.page = Integer.parseInt(params.get("page").toString());
        }else{
        	throw new Exception("分页参数不能为空： page");
        }
        Object limitobj = params.get("limit");
        if(null != limitobj){
        	 this.limit = Integer.parseInt(params.get("limit").toString());
        }else{
        	throw new Exception("分页参数不能为空： limit");
        }
        if(null != pageobj && null != limitobj){
        	offset =  (page - 1) * limit;
	        this.put("offset",offset);
	        this.put("page", page);
	        this.put("limit", limit);
        }

        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String sidx = (String)params.get("sidx");
        String order = (String)params.get("order");
//        if(StringUtils.isNotBlank(sidx)){
//            this.put("sidx", SQLFilter.sqlInject(sidx));
//        }
//        if(StringUtils.isNotBlank(order)){
//            this.put("order", SQLFilter.sqlInject(order));
//        }
        //查询order by拼接
        if(StringUtils.isNotBlank(sidx) && StringUtils.isNotBlank(order)){
        	String sidxInject = SQLFilter.sqlInject(sidx);
        	String orderInject = SQLFilter.sqlInject(order);
        	this.put("sidx", sidxInject);
        	this.put("order", orderInject);
            this.put("sidx_order", " order by " + sidxInject + " " + orderInject);
        }else if(StringUtils.isNotBlank(sidx)){
        	 this.put("sidx", SQLFilter.sqlInject(sidx));
        }else if(StringUtils.isNotBlank(order)){
            this.put("order", SQLFilter.sqlInject(order));
        }
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }


	public int getOffset() {
		return offset;
	}


	public void setOffset(int offset) {
		this.offset = offset;
	}
}

package com.kasite.core.common.resp;

/**
 * 
 * @className: AbsRespHandler
 * @author: lcz
 * @date: 2018年7月20日 上午10:54:01
 */
public abstract class AbsRespHandler implements IRespHandler{
	
	/**
	 * 需要转换的数据
	 */
	protected CommonResp<?> data;
	/**
	 * 转换规则配置
	 */
	protected  ParseConfig cfg;
	
	
	public CommonResp<?> getData() {
		return data;
	}
	public void setData(CommonResp<?> data) {
		this.data = data;
	}
	public ParseConfig getCfg() {
		return cfg;
	}
	public void setCfg(ParseConfig cfg) {
		this.cfg = cfg;
	}
	
}

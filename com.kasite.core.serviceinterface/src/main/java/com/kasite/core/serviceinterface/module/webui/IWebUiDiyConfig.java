package com.kasite.core.serviceinterface.module.webui;

import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.service.ICallHis;

/**
 * 前端页面个性化属性配置
 * 每次 token 失效后会重新从服务端获取。
 * @author daiyanshui
 *
 */
public interface IWebUiDiyConfig extends ICallHis{
	/**
	 * 获取医院前端个性化配置
	 * @return
	 */
	JSONObject getDiyConfig();
	
}

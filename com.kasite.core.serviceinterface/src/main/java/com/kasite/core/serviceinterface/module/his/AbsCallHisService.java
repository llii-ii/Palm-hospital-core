package com.kasite.core.serviceinterface.module.his;

import com.kasite.core.common.config.KasiteConfig;

/**
 * 所有调用HIS的接口部分抽象封装方法
 * @author daiyanshui
 *
 */
public abstract class AbsCallHisService {

	/**
	 * 获取个性化UI配置
	 * @return
	 */
	public com.alibaba.fastjson.JSONObject getWebUiDiy() {
		return KasiteConfig.getWebUiDiy();
	}
	
}

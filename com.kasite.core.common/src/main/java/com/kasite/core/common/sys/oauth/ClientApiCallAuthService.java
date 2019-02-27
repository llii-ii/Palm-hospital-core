package com.kasite.core.common.sys.oauth;

import com.kasite.core.common.annotation.ClientIdAuthApi;
import com.kasite.core.common.bean.bo.AuthInfoVo;

/**
 * 渠道接口控制鉴权
 * 通过 加注解：ClientIdAuthApi 方法的【系统（定时任务等）基本调用除外】 都经过此接口进行控制
 * @author daiyanshui
 *
 */
public interface ClientApiCallAuthService {
	/**
	 * 校验接口是否可以调用如果不行则返回false 则返回true
	 */
	boolean isOk(AuthInfoVo vo,ClientIdAuthApi ann);
}

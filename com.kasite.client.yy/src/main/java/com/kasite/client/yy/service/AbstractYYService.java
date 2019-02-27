/**
 * 
 */
package com.kasite.client.yy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.service.ICallHis;
import com.kasite.core.common.util.ExpiryMap;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.ICallHisService;
import com.kasite.core.serviceinterface.module.his.handler.ILockAndUnLockService;
import com.kasite.core.serviceinterface.module.yy.IYYService;
/**
 * @author lsq version 1.0 2017-6-20下午5:43:41
 */
public abstract class AbstractYYService implements IYYService {
	
	protected static Logger logger = LoggerFactory.getLogger(AbstractYYService.class);
	
	/**
	 * 预约挂号缓存,默认缓存15分钟
	 */
	protected static ExpiryMap<String, CommonResp<?>> yyCacheMap = new ExpiryMap<>(15*60*1000);
	
	/**
	 * 获取指定机构下的接口实现类
	 * @param vo
	 * @return
	 */
	protected ICallHisService getCallHisService(AuthInfoVo vo) {
		ICallHisService service = HandlerBuilder.get().getCallHisService(vo);
		if(null == service) {
			throw new RRException("未发现对应医院的接口实现类："+ vo.getClientVersion());
		}
		
		return service;
	}
	
	/**
	 * 如果机构有实现锁号，则返回锁号接口
	 * @param vo
	 * @return
	 */
	protected ILockAndUnLockService getLockAndUnLockService(AuthInfoVo vo) {
		return HandlerBuilder.get().getCallHisService(vo, ILockAndUnLockService.class);//(vo);
	}
	
	protected <T extends ICallHis> T getCallHisService(AuthInfoVo vo,Class<T> clazz) {
		return HandlerBuilder.get().getCallHisService(vo,clazz);
	}
}

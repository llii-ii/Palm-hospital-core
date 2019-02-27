package com.kasite.client.business.module.sys.check;
/**
 * 接口异常回调通知
 * @author daiyanshui
 *
 */
public interface CheckInterfaceHandler {
	/**
	 * 接口判断后回调通知
	 * @param vo
	 */
	void notify(InterfaceStatusVo vo);
}

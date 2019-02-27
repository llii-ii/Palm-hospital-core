package com.kasite.core.common.sys.handler;

import com.alibaba.fastjson.JSONObject;

/**
 * 日志告警通知 系统发送异常的时候触发此告警通知
 * @author daiyanshui
 */
public interface IWarnNotifyHandler {
	/**
	 * 通知的内容  通知后返回的url
	 * @param json {title:'标题',url:'链接地址',remark:'备注'}
	 */
	void notify(JSONObject json);
}

package com.kasite.server.verification.module.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.coreframework.remoting.standard.DateOper;
import com.kasite.core.common.annotation.ApiCallLog;
import com.kasite.core.common.annotation.GetwayLoginUser;
import com.kasite.core.common.util.R;
import com.kasite.core.common.validator.Assert;
import com.kasite.core.httpclient.http.StringUtils;
import com.kasite.server.verification.common.controller.AbstractController;
import com.kasite.server.verification.module.app.entity.AppEntity;
import com.kasite.server.verification.module.app.entity.AppNotify;
import com.kasite.server.verification.module.app.service.AppNotifyService;
import com.kasite.server.verification.module.app.service.AppOnLineService;

@RestController
@RequestMapping("/gateway/message/")
public class AppMessageCenter extends AbstractController {

	@Autowired
	private AppNotifyService appNotifyService;
	@Autowired
	private AppOnLineService appOnLineService;
	/**
	 * 获取 PublicKey 公钥
	 * @throws Exception 
	 */
	@ApiCallLog("获取消息状态码")
	@RequestMapping(value = "queryMessageList", method = RequestMethod.POST)
	public R getStatus(@GetwayLoginUser AppEntity app,String lastReadTime) throws Exception {
		String appId = app.getAppId();
		Assert.isBlank2(appId, "appId");
		try {
			appOnLineService.appOnLine(appId);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("更新在线状态异常。",e);
		}
		String now = DateOper.getNow("yyyy-MM-dd HH:mm:ss");
		logger.info(lastReadTime);
		if(StringUtils.isBlank(lastReadTime)) {
			lastReadTime = now;
		}
		List<AppNotify> list = appNotifyService.queryAppNotifyList(appId,lastReadTime);
		if(null != list) {
			for (AppNotify appNotify : list) {
				appNotifyService.deleteAppNotify(appNotify.getId());
			}
			return R.ok(list).put("lastReadTime", now);
		}else {
			return R.error(600, "not message");
		}
	}
}

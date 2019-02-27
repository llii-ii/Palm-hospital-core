package com.kasite.server.verification.module.app.service;

import java.util.List;

import com.kasite.server.verification.module.app.entity.CenterWxConfig;

public interface CenterWxConfigService {
	public List<CenterWxConfig> queryWxConfigList(String ids) throws Exception;
}

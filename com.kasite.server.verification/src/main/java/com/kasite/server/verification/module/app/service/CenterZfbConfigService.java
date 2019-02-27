package com.kasite.server.verification.module.app.service;

import java.util.List;

import com.kasite.server.verification.module.app.entity.CenterZfbConfig;

public interface CenterZfbConfigService {
	public List<CenterZfbConfig> queryZfbConfigList(String ids) throws Exception;
}

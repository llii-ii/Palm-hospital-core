package com.kasite.server.verification.module.app.service;

import java.util.List;

import com.kasite.server.verification.module.app.entity.CenterWxPay;

public interface CenterWxPayService {
	public List<CenterWxPay> queryWxPayList(String ids) throws Exception;
}

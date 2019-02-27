package com.kasite.client.crawler.modules.patient.service;

import com.kasite.client.crawler.modules.patient.vo.ReqQueryPatientListVo;

/**
 * 同步用户信息到ES数据库
 * @author daiyanshui
 *
 */
public interface ISyncPatintInfoData2ESDBService {
	
	public void sync(ReqQueryPatientListVo vo) throws Exception;

}

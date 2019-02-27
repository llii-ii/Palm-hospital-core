package com.kasite.core.serviceinterface.module.basic;

import java.util.List;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.req.PageVo;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.module.basic.dbo.Dictionary;
import com.kasite.core.serviceinterface.module.basic.req.ReqAddDictionary;
import com.kasite.core.serviceinterface.module.basic.req.ReqDelDictionary;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryDictList;
import com.kasite.core.serviceinterface.module.basic.req.ReqUpdateDictionary;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryDictList;

/**
 * 
 * @className: IDictionaryService
 * @author: lcz
 * @date: 2018年8月27日 下午3:36:43
 */
public interface IDictionaryService {
	
	List<Dictionary> queryDictList(PageVo page, Dictionary dict) throws Exception;
	
	CommonResp<RespQueryDictList> queryDictList(CommonReq<ReqQueryDictList> req) throws Exception;
	
	CommonResp<RespMap> delDictionary(CommonReq<ReqDelDictionary> req) throws Exception;
	
	CommonResp<RespMap> updateDictionary(CommonReq<ReqUpdateDictionary> req) throws Exception;
	
	CommonResp<RespMap> addDictionary(CommonReq<ReqAddDictionary> req) throws Exception;
	
	
}

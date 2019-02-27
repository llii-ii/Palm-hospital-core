package com.kasite.core.serviceinterface.module.basic;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.module.basic.req.ReqAddBatUser;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryBatUser;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryBatUser;
import com.yihu.wsgw.api.InterfaceMessage;
/**
 * @author lq
 * @version 1.0
 * 2017-7-24 下午2:29:38
 */
public interface IBatService{
	
	/**
	 * 添加用户
	 * @param msg
	 * @return
	 */
	public String AddBatUser(InterfaceMessage msg)  throws Exception;
	/**
	 * 添加用户
	 * @Description: 
	 * @param paramReq
	 * @return
	 */
	CommonResp<RespMap> addBatUser(CommonReq<ReqAddBatUser> paramReq)  throws Exception;
	
	/**
	 * 查询用户
	 * @param msg
	 * @return
	 */
	public String QueryBatUser(InterfaceMessage msg) throws Exception;
	/**
	 * 查询用户
	 * @Description: 
	 * @param paramReq
	 * @return
	 */
	CommonResp<RespQueryBatUser> queryBatUser(CommonReq<ReqQueryBatUser> paramReq) throws Exception;
}

package com.kasite.core.serviceinterface.module.healthTools;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.module.healthTools.req.ReqCheckListInterpretationAdd;
import com.kasite.core.serviceinterface.module.healthTools.req.ReqCheckListInterpretationById;
import com.kasite.core.serviceinterface.module.healthTools.req.ReqCheckListInterpretationQuery;
import com.kasite.core.serviceinterface.module.healthTools.req.ReqCheckListInterpretationUpdate;
import com.kasite.core.serviceinterface.module.healthTools.resp.RespCheckListInterpretation;
import com.yihu.wsgw.api.InterfaceMessage;

public interface HealthToolsService {

	/**
	 * 查询检查单解读列表
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String QueryCheckListList(InterfaceMessage msg) throws Exception;

	CommonResp<RespCheckListInterpretation> queryCheckListList(CommonReq<ReqCheckListInterpretationQuery> req)
			throws Exception;

	/**
	 * 添加检查单解读
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String AddCheckList(InterfaceMessage msg) throws Exception;

	CommonResp<RespMap> addCheckList(CommonReq<ReqCheckListInterpretationAdd> req) throws Exception;

	/**
	 * 根据ID查询检查单解读
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String QueryCheckListById(InterfaceMessage msg) throws Exception;

	CommonResp<RespCheckListInterpretation> queryCheckListById(CommonReq<ReqCheckListInterpretationById> req)
			throws Exception;

	/**
	 * 修改检查单解读
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String UpdateCheckList(InterfaceMessage msg) throws Exception;

	CommonResp<RespMap> updateCheckList(CommonReq<ReqCheckListInterpretationUpdate> req) throws Exception;

}

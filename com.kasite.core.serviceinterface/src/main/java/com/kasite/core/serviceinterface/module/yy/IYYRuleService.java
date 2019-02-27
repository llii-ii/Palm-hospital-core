package com.kasite.core.serviceinterface.module.yy;

import java.util.List;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.req.ReqString;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.serviceinterface.module.yy.req.ReqUpdateRuleAndLimitVo;
import com.kasite.core.serviceinterface.module.yy.resp.RespQueryYYRule;

public interface IYYRuleService {
	/**
	 * 查询预约规则
	 * @Description: 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryYYRule> queryYYRule(CommonReq<ReqString> commReq) throws Exception;
	
	/**
	 * 更新预约规则(管理后台)
	 * 
	 * @param rulelimitVo
	 * @return
	 * @throws Exception
	 */
	public int updateYYRule(ReqUpdateRuleAndLimitVo rulelimitVo) throws Exception;
	

	/**
	 * 查询预约规则(管理后台)
	 * 
	 * @param ruleId
	 * @param state
	 * @return
	 * @throws Exception
	 */
	public List<RespQueryYYRule> queryYYRuleList(String ruleId, Integer state) throws Exception;
}

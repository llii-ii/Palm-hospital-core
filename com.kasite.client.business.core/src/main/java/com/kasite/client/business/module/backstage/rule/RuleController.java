package com.kasite.client.business.module.backstage.rule;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kasite.core.common.controller.AbstractController;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.serviceinterface.module.yy.IYYRuleService;
import com.kasite.core.serviceinterface.module.yy.IYYService;
import com.kasite.core.serviceinterface.module.yy.req.ReqUpdateRuleAndLimitVo;
import com.kasite.core.serviceinterface.module.yy.req.ReqYyLimit;
import com.kasite.core.serviceinterface.module.yy.resp.RespQueryYYLimit;
import com.kasite.core.serviceinterface.module.yy.resp.RespQueryYYRule;

/**
 * 预约规则(此模块暂时停用，保留)
 * 
 * @author zhaoy
 *
 */
@RestController
public class RuleController extends AbstractController {

	@Autowired
	IYYService iYYService;
	
	@Autowired
	IYYRuleService yyRuleService;
	
	@PostMapping("/yyRule/queryRule.do")
	R queryRule(String ruleId, Integer state, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<RespQueryYYRule> respList = yyRuleService.queryYYRuleList(ruleId, state);
		return R.ok(respList);
	}
	
	@PostMapping("/yyRule/queryLimit.do")
	R queryLimit(String ruleId, Integer state, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<RespQueryYYLimit> respList = iYYService.queryYYLimit(ruleId, state);
		return R.ok(respList);
	}
	
	@PostMapping("/yyRule/updateRuleAndLimit.do")
	R updateRuleAndLimit(ReqUpdateRuleAndLimitVo rulelimitVo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(StringUtil.isBlank(rulelimitVo.getRuleId())){
			return R.error("参数错误,规则主键ID不能为空!");
		}
		int result = yyRuleService.updateYYRule(rulelimitVo);
		if(result <= 0) {
			return R.error("更新预约规则信息失败！");
		}
		for(int i=0; i<rulelimitVo.getLimits().size(); i++){
			ReqYyLimit reqYyLimit = rulelimitVo.getLimits().get(i);
			if(StringUtil.isBlank(reqYyLimit.getLimitId())){
				return R.error("参数错误,预约限制规则主键ID不能为空!");
			}
			result = iYYService.updateYYLimit(reqYyLimit);
		}
		return R.ok();
	}
}

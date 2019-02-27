package com.kasite.client.yy.service;

import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kasite.client.yy.bean.dbo.YyRule;
import com.kasite.client.yy.dao.IYyRuleMapper;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.req.ReqString;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.util.BeanCopyUtils;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.serviceinterface.module.yy.IYYRuleService;
import com.kasite.core.serviceinterface.module.yy.req.ReqUpdateRuleAndLimitVo;
import com.kasite.core.serviceinterface.module.yy.resp.RespQueryYYRule;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class YYRuleServiceImpl implements IYYRuleService{
	
	@Autowired
	IYyRuleMapper ruleMapper;
	
	@Override
	public CommonResp<RespQueryYYRule> queryYYRule(CommonReq<ReqString> commReq) throws Exception {
		ReqString reqStr = commReq.getParam();
		Example example = new Example(YyRule.class);
		Criteria criteria = example.createCriteria();
		if(StringUtil.isNotBlank(reqStr.getParam())) {
			criteria.andEqualTo("hosId", reqStr.getParam());
		}
		criteria.andEqualTo("state", 1);
		YyRule rule = ruleMapper.selectOneByExample(example);
		if(rule == null) {
			return new CommonResp<RespQueryYYRule>(commReq, RetCode.YY.ERROR_NOTYYRULE, "没有找到预约规则配置信息,请先配置预约规则");
		}
		RespQueryYYRule resp = new RespQueryYYRule();
		BeanCopyUtils.copyProperties(rule, resp, null);
		return new CommonResp<RespQueryYYRule>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	
	@Override
	public List<RespQueryYYRule> queryYYRuleList(String ruleId, Integer state) throws Exception {
		Example example = new Example(YyRule.class);
		if(state != null) {
			example.createCriteria().andEqualTo("state", state);
		}
		if(StringUtil.isNotBlank(ruleId)) {
			example.createCriteria().andEqualTo("ruleId", state);
		}
		List<RespQueryYYRule> respList = new Vector<RespQueryYYRule>();
		List<YyRule> yyRuleList = ruleMapper.selectByExample(example);
		if(yyRuleList != null) {
			for (YyRule yyRule : yyRuleList) {
				RespQueryYYRule resp = new RespQueryYYRule();
				BeanCopyUtils.copyProperties(yyRule, resp, null);
				respList.add(resp);
			}
		}
		return respList;
	}
	
	@Override
	public int updateYYRule(ReqUpdateRuleAndLimitVo rulelimitVo) throws Exception {
		YyRule rule = new YyRule();
		if(StringUtil.isNotBlank(rulelimitVo.getAmTakeNum())){
			rule.setAmTakeNum(rulelimitVo.getAmTakeNum());
		}
		if(StringUtil.isNotBlank(rulelimitVo.getBreachDay())){
			rule.setBreachDay(rulelimitVo.getBreachDay());
		}
		if(StringUtil.isNotBlank(rulelimitVo.getBreachTimes())){
			rule.setBreachTimes(rulelimitVo.getBreachTimes());
		}
		if(StringUtil.isNotBlank(rulelimitVo.getEndDay())){
			rule.setEndDay(rulelimitVo.getEndDay());
		}
		if(StringUtil.isNotBlank(rulelimitVo.getEndTime())){
			rule.setEndTime(rulelimitVo.getEndTime());
		}
		if(StringUtil.isNotBlank(rulelimitVo.getPmTakeNum())){
			rule.setPmTakeNum(rulelimitVo.getPmTakeNum());
		}
		if(StringUtil.isNotBlank(rulelimitVo.getStartTime())){
			rule.setStartTime(rulelimitVo.getStartTime());
		}
		if(StringUtil.isNotBlank(rulelimitVo.getStartDay())){
			rule.setStartDay(rulelimitVo.getStartDay());
		}
		if(StringUtil.isNotBlank(rulelimitVo.getState()) && rulelimitVo.getState()!=0){
			rule.setState(rulelimitVo.getState());
		}
		if(StringUtil.isNotBlank(rulelimitVo.getDrawPoint())){
			rule.setDrawPoint(rulelimitVo.getDrawPoint());
		}
		if(StringUtil.isNotBlank(rulelimitVo.getNumberDes())){
			rule.setNumberDes(rulelimitVo.getNumberDes());
		}
		return ruleMapper.updateByPrimaryKeySelective(rule);
	}
}

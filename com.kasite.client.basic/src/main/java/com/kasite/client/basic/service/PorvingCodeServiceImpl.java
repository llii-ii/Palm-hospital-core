package com.kasite.client.basic.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coreframework.util.DateOper;
import com.kasite.client.basic.dao.IProvingCodeMapper;
import com.kasite.core.common.config.GoogleAuthenticator;
import com.kasite.core.common.config.GoogleAuthenticatorConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.serviceinterface.module.basic.IPorvingCodeService;
import com.kasite.core.serviceinterface.module.basic.dbo.ProvingCode;
import com.kasite.core.serviceinterface.module.basic.req.ReqCheckPorvingCode;
import com.kasite.core.serviceinterface.module.basic.req.ReqSaveProvingCode;
import com.kasite.core.serviceinterface.module.basic.resp.RespGetprovingCode;

import tk.mybatis.mapper.entity.Example;

@Service
public class PorvingCodeServiceImpl implements IPorvingCodeService{
	
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_BASIC);
	
	@Autowired
	GoogleAuthenticatorConfig config;
	@Autowired
	private IProvingCodeMapper provingCodeMapper;
	
	@Override
	public CommonResp<RespGetprovingCode> saveProvingCode(CommonReq<ReqSaveProvingCode> reqCommon)throws Exception{
			ReqSaveProvingCode req = reqCommon.getParam();
			// 验证码发送成功之后保存验证码
			ProvingCode provingCode = new ProvingCode();
			provingCode.setMobile(req.getMobile());
			provingCode.setProvingCode(req.getCode());
			String now = DateOper.getNow("yyyy-MM-dd HH:mm:ss");
			// 设置有效时间120秒
			String validTime = DateOper.addSecond(now, 120, "yyyy-MM-dd HH:mm:ss");
			provingCode.setValidTime(java.sql.Timestamp.valueOf(validTime));
			provingCode.setpCId(CommonUtil.getGUID());
			// 存储验证码到本地
			provingCodeMapper.insertSelective(provingCode);
			RespGetprovingCode resp = new RespGetprovingCode(provingCode.getpCId());
			return new CommonResp<RespGetprovingCode>(reqCommon,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,resp);
		
	}
	@Override
	public CommonResp<RespMap> checkPorvingCode(CommonReq<ReqCheckPorvingCode> reqComm) throws Exception{
		ReqCheckPorvingCode req = reqComm.getParam();
		//判断验证码是否和 google的验证码一致，如果一致的话就直接返回成功。否则 走短信验证
		String secret = config.getAuth().getAppSecret();
		Boolean isTrue = GoogleAuthenticator.authcode(req.getProvingCode(), secret);
		if(null != isTrue && isTrue) {
			LogUtil.info(log, "通过 GoogleAuthCode 进行验证。code = "+ req.getProvingCode());
			return new CommonResp<RespMap>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		}
		Example example = new Example(ProvingCode.class);
		example.createCriteria()
		.andEqualTo("pCId", req.getpCId())
		.andEqualTo("provingCode", req.getProvingCode())
		.andEqualTo("mobile", req.getMobile())
		.andGreaterThanOrEqualTo("validTime", DateOper.getNowDateTime());
		int count = provingCodeMapper.selectCountByExample(example);
		if(count>0) {
			return new CommonResp<RespMap>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		}else {
			return new CommonResp<RespMap>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Basic.ERROR_CHECKPORVINGCODE,"验证码错误");
		}
	}
	
	
}

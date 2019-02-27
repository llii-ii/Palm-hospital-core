package com.kasite.core.serviceinterface.common.aspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kasite.core.common.annotation.ClientIdAuthApi;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.sys.oauth.ClientApiCallAuthService;
import com.kasite.core.common.sys.service.ShiroService;
import com.kasite.core.common.util.SpringContextUtil;
import com.yihu.wsgw.api.InterfaceMessage;

@Aspect
@Component
public class ClientIdAuthApiAspect {
    final static Logger log = LoggerFactory.getLogger(ClientIdAuthApiAspect.class);

	@Autowired
	protected ShiroService shiroService;
	
    ThreadLocal<Long> beginTime = new ThreadLocal<>();

    @Pointcut("@annotation(clientIdAuthApi)")
    public void serviceStatistics(ClientIdAuthApi clientIdAuthApi) {
    }

    @Before("serviceStatistics(clientIdAuthApi)")
    public void doBefore(JoinPoint joinPoint, ClientIdAuthApi clientIdAuthApi) {
        // 记录请求到达时间
        beginTime.set(System.currentTimeMillis());
        // 获取当前登录用户的信息
        Object[] args = joinPoint.getArgs();
        InterfaceMessage msgObj = null;
        CommonReq<?> commreqObj = null;
        AuthInfoVo vo = null;
        
        for (Object object : args) {
			if(object instanceof CommonReq<?>) {
				commreqObj = (CommonReq<?>) object;
			}
			if(object instanceof InterfaceMessage) {
				msgObj = (InterfaceMessage) object;
			}
		}
        
        if(null != commreqObj || null != msgObj) {
        	if(null != commreqObj) {
        		vo = commreqObj.getParam().getAuthInfo();
        	}else {
        		vo = KasiteConfig.getAuthInfo(msgObj.getAuthInfo());
        	}
        	String clientId = vo.getClientId();
        	//如果不是系统级调用的都需要经过鉴权控制
        	if(!KstHosConstant.SYSOPERATORID.equals(clientId)) {
        		ClientApiCallAuthService service = SpringContextUtil.getBean(ClientApiCallAuthService.class);
        		if(null != service) {
        			boolean isOk = service.isOk(vo, clientIdAuthApi);
        			if(!isOk) {
        				throw new RRException(RetCode.Common.OAUTH_ERROR_NOPROMIIME);
        			}
        		}
        	}else {
        		return;
        	}
        }else {
        	//如果当前登录的用户为空则抛出异常
        	throw new RRException(RetCode.Common.OAUTH_ERROR_NOPROMIIME);
        }
        log.info("调用API api:{}", clientIdAuthApi.api());
    }

    @After("serviceStatistics(clientIdAuthApi)")
    public void doAfter(ClientIdAuthApi clientIdAuthApi) {
        log.info("调用耗时 time:{}, api:{}", System.currentTimeMillis() - beginTime.get(), clientIdAuthApi.api());
    }

}
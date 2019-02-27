/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.kasite.client.zfb.executor;

import com.kasite.core.common.bean.bo.AuthInfoVo;

/**
 * 业务执行接口
 * 
 * @author taixu.zqq
 * @version $Id: ActionExecutor.java, v 0.1 2014年7月24日 下午3:57:01 taixu.zqq Exp $
 */
public interface ActionExecutor {
    
    /**
     * 业务执行方法
     * @return
     * @throws Exception
     */
    public String execute(AuthInfoVo authInfo) throws Exception;

}

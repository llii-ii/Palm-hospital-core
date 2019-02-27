package com.kasite.core.serviceinterface.module.pay.req;

import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.StringUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO
 */
public class ReqGetMerchantNotifyById extends AbsReq {

	private Long id;
	
	
	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqGetMerchantNotifyById(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		
	}



	/**
	 * @param msg
	 * @param id
	 * @throws AbsHosException
	 */
	public ReqGetMerchantNotifyById(InterfaceMessage msg, Long id) throws AbsHosException {
		super(msg);
		if( StringUtil.isEmpty(id)) {
			throw new ParamException("主键ID不能为空！");
		}
		this.id = id;
	}

	
	
}

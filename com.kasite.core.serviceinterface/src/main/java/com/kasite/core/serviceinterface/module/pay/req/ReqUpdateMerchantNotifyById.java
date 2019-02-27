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
public class ReqUpdateMerchantNotifyById extends AbsReq {

	private Long id;
	
	private Integer state;
	
	private Integer retryNum;
	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public Integer getState() {
		return state;
	}



	public void setState(Integer state) {
		this.state = state;
	}



	public Integer getRetryNum() {
		return retryNum;
	}



	public void setRetryNum(Integer retryNum) {
		this.retryNum = retryNum;
	}



	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqUpdateMerchantNotifyById(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		
	}



	/**
	 * @param msg
	 * @param id
	 * @throws AbsHosException
	 */
	public ReqUpdateMerchantNotifyById(InterfaceMessage msg, Long id,Integer state,Integer retryNum) throws AbsHosException {
		super(msg);
		if( StringUtil.isEmpty(id)) {
			throw new ParamException("主键ID不能为空！");
		}
		this.id = id;
		this.state = state;
		this.retryNum = retryNum;
	}

	
	
}

package com.kasite.core.common.dao.vo;

import com.kasite.core.common.exception.ParamException;

/**
 * 接口入参抽象类
 * @created 2016-05-27
 * @author lcz
 *
 */
public abstract class AbsReqParam implements java.io.Serializable,IReqParam{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6169459967671387962L;


	@Override
	public void paramValid(ReqAuthInfo auth) throws ParamException{
		
//		if(auth==null || StringUtil.isBlank(auth.getClientId())){
//			throw new ParamException("渠道ID不能为空.");
//		}
		
	}
	
	
	
	
	
	
	
}

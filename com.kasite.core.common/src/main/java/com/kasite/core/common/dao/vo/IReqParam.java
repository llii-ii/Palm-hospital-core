package com.kasite.core.common.dao.vo;

import javax.servlet.http.HttpServletRequest;

import com.kasite.core.common.exception.ParamException;




/**
 * 入参处理接口（入参验证等）
 * @created 2016-05-27
 * @author lcz
 *
 */
public interface IReqParam {
	
	/**
	 * 入参验证
	 * @throws ParamException
	 */
	public void paramValid(ReqAuthInfo auth) throws ParamException;
	/**
	 * 
	 * 初始化入参
	 * @author Archer Zheng
	 * @created 2016-12-13 下午3:03:08
	 * @param auth
	 * @throws ParamException
	 */
	public void paramInit(HttpServletRequest request) throws ParamException;
	
	
}

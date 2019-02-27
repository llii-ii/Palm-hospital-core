package com.yihu.hos.service;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.kasite.core.common.exception.RRException;
import com.yihu.hos.DModule;
import com.yihu.hos.IRetCode;
import com.yihu.hos.constant.IConstant;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.hos.exception.CommonServiceException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 公共的返回值管理类
 * @author Administrator
 *
 */
public class CommonServiceReturnMessage {
	
	/**
	 * 调用医院端接口异常返回
	 * @param reqParam 请求参数
	 * @param module 该对象的配置信息
	 * @return Code = -1445
	 */
	protected String getReqHosClientErrorMessage(DModule module,String message) {
		String m = getHosClientErrorMsg(module);
		return getRetVal(CommonServiceRetCode.Common.CallHosClientError,m+"\t"+message);
	}
	/**
	 * 返回医院接口异常
	 * @param module 当前配置信息
	 * @throws RRException 抛出异常信息
	 */
	protected void getHosClientErrorMessage(DModule module,IRetCode c) throws AbsHosException {
		throw new CommonServiceException(c,getHosClientErrorMsg(module));
	}
	/**
	 * 返回医院接口异常
	 * @param module 当前配置信息
	 * @return 异常信息
	 */
	protected String getHosClientErrorMsg(DModule module){
		return	module.getCname() +"  医院接口:" + module.getClazz() + " 调用失败. 请联系技术人员.";
	}
	
	/**
	 * 返回错误代码和错误信息
	 */
	public static String getRetVal(IRetCode code,String otherMsg) {
		Document resDoc = DocumentHelper.createDocument();
		Element Resp = resDoc.addElement(IConstant.RESP);
		Resp.addElement(IConstant.RESPCODE).addText(code.getCode() + "");
		Resp.addElement(IConstant.RESPMESSAGE).addCDATA(code.getMessage() +"\t"+otherMsg);
		return resDoc.asXML();
	}
	
	/**
	 * 调用医院端接口异常返回
	 * @param reqParam 请求参数
	 * @param module 该对象的配置信息
	 * @param retStr 返回参数
	 * @return Code = -1446
	 */
	protected String getReqHosClientErrorMessage(DModule module,String reqParam,String retStr) {
		String msg = module.getCname() +"  医院接口:" 
				+ module.getClazz() + " 返回数据内容不正确. 请联系技术人员.返回内容:"+retStr;
		return getRetVal(CommonServiceRetCode.Common.CallHosClientError,msg);
	}
	
	/**
	 * 参数异常输出
	 * @param e 异常信息
	 * @param msg 请求参数
	 * @param module 该对象的配置信息
	 * * @return Code = -1445
	 */
	protected String getReqParamException(DModule module,AbsHosException e,InterfaceMessage msg) {
		String m =  module.getCname() +
		"  医院接口:" + module.getClazz() + 
		" 调用失败. 请求参数出错:"+ msg.getParam()+
		" 异常信息:" + e.getMessage();
		return getRetVal(CommonServiceRetCode.Common.CallHosClientError,m);
	}
	/**
	 * 业务异常输出
	 * @param e 异常信息
	 * @param msg 请求参数
	  * @param module 该对象的配置信息
	  * * @return Code = -1445
	 */
	protected String getReqRRException(DModule module,AbsHosException e,InterfaceMessage msg) {
		String m =  module.getCname() +
				"  医院接口:" + module.getClazz() + 
				" 调用失败. 请求参数出错:"+ msg.getParam()+
				" 异常信息:" + e.getMessage();
		return getRetVal(e.getCode(),m);
	}
	
	/**
	 * 没有该接口
	 * @param module 对象配置文件
	 * @return 返回异常信息
	 */
	protected String getNotOveMethod(DModule module){
		return "医院: "+module.getCname() + " 的该接口:"+CommonServiceRetCode.Common.Init.getMessage();
	}
	
}

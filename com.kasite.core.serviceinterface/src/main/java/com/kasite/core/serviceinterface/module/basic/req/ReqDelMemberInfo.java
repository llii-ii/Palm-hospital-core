/**
 * 
 */
package com.kasite.core.serviceinterface.module.basic.req;

import org.dom4j.Element;

import com.coreframework.util.StringUtil;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author lcy
 * 删除用户入参类
 * @version 1.0 
 * 2017-7-4上午9:04:43
 */
public class ReqDelMemberInfo extends AbsReq{
	private String memberId;

	public ReqDelMemberInfo(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.memberId = getDataJs().getString("MemberId");
		}else {
			Element ser = root.element(KstHosConstant.DATA);
			this.memberId=XMLUtil.getString(ser, "MemberId", false);
		}
		if(StringUtil.isBlank(this.memberId)) {
			throw new RRException(RetCode.Common.ERROR_PARAM,"参数成员ID不能为空。");
		}
	}

	
	
	/**
	 * @Title: ReqDelMemberInfo
	 * @Description: 
	 * @param msg
	 * @param memberId
	 * @throws AbsHosException
	 */
	public ReqDelMemberInfo(InterfaceMessage msg, String memberId) throws AbsHosException {
		super(msg);
		this.memberId = memberId;
	}



	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
}

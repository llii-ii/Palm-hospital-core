package com.kasite.core.serviceinterface.module.qywechat.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * Req获取部门下的用户
 * 
 * @author 無
 *
 */
public class ReqUserQuery extends AbsReq {

	public ReqUserQuery(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.department = XMLUtil.getString(ser, "department", true);
		this.wxkey = XMLUtil.getString(ser, "wxkey", true);
	}

	/**
	 * 部门ID
	 */
	private String department;
	private String wxkey;

	public String getWxkey() {
		return wxkey;
	}

	public void setWxkey(String wxkey) {
		this.wxkey = wxkey;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

}
package com.kasite.core.serviceinterface.module.qywechat.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * Req部门
 * 
 * @author 無
 *
 */
public class ReqDepartmentQuery extends AbsReq {
	public ReqDepartmentQuery(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.id = XMLUtil.getString(dataEl, "id", false);
		this.wxkey = XMLUtil.getString(dataEl, "wxkey", true);
		this.deptName = XMLUtil.getString(dataEl, "deptName", false);
	}

	/**
	 * 部门id。获取指定部门及其下的子部门。 如果不填，默认获取全量组织架构
	 */
	private String id;
	private String wxkey;
	private String deptName;

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getWxkey() {
		return wxkey;
	}

	public void setWxkey(String wxkey) {
		this.wxkey = wxkey;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
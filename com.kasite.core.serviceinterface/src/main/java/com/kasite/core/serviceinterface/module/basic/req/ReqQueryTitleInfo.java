package com.kasite.core.serviceinterface.module.basic.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linf 2017年11月14日 17:26:52 
 * TODO 查询科室头衔请求对象
 */
public class ReqQueryTitleInfo  extends AbsReq{
	/**
	 * 医院唯一编码
	 */
	private String hosId; 
	/**
	 * 科室代码
	 */
	private String deptCode; 
	public ReqQueryTitleInfo(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.hosId=XMLUtil.getString(ser, "HosId", true);
		this.deptCode = XMLUtil.getString(ser, "DeptCode", false);
	}
	public String getHosId() {
		return hosId;
	}
	public void setHosId(String hosId) {
		this.hosId = hosId;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

}

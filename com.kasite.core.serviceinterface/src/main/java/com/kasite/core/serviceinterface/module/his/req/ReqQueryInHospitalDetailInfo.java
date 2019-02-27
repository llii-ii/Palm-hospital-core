package com.kasite.core.serviceinterface.module.his.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO
 */
public class ReqQueryInHospitalDetailInfo extends AbsReq{
	private String expenseType;
	public String getExpenseType() {
		return expenseType;
	}
	public void setExpenseType(String expenseType) {
		this.expenseType = expenseType;
	}
	public ReqQueryInHospitalDetailInfo(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);

		if (ser == null) {
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		Element service = root.element(KstHosConstant.DATA);
		this.expenseType = XMLUtil.getString(service, "ExpenseType", false);
	}

}

/**
 * 
 */
package com.kasite.core.serviceinterface.module.yy.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**查询历史医生入参
 * @author Administrator
 *
 */
public class ReqQueryHistoryDoctor  extends AbsReq{
	/**医院唯一编号*/
	private String hosId;
	/**用户openid*/
	private String opId;

	
	public ReqQueryHistoryDoctor(InterfaceMessage reqXml) throws AbsHosException {
		super(reqXml);
		Element ser = root.element(KstHosConstant.DATA);
		if(ser==null){
			throw new ParamException("传入参数中[DATA]节点不能为空。");
		}
		this.hosId = XMLUtil.getString(ser, "HosId", false);
		this.opId = XMLUtil.getString(ser, "OpId", false,super.getOpenId());
	}
	public String getHosId() {
		return hosId;
	}
	public void setHosId(String hosId) {
		this.hosId = hosId;
	}
	public String getOpId() {
		return opId;
	}
	public void setOpId(String opId) {
		this.opId = opId;
	}
}

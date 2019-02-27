/**
 * 
 */
package com.kasite.core.serviceinterface.module.basic.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 住院用户信息查询 入参
 * @author mhd
 * @version 1.0
 * 2017-7-12 下午2:37:41
 */
public class ReqQueryHospitalUserInfo extends AbsReq {

	/**
	 * 住院号
	 */
	private String hospitalNo;
	private String memberId;
	/** 
	 * 查询住院费用总额  有些医院在查询住院用户信息的时候没有返回，但是有单独的住院费用总额查询接口
	 * 这里在显示hospitalCost.html页面的时候需要显示总额 或者在其它的地方有需要显示的时候传入 则触发再次查询
	 * */
	private String queryHospitalCost;
	
	public String getQueryHospitalCost() {
		return queryHospitalCost;
	}
	public void setQueryHospitalCost(String queryHospitalCost) {
		this.queryHospitalCost = queryHospitalCost;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getHospitalNo() {
		return hospitalNo;
	}

	public void setHospitalNo(String hospitalNo) {
		this.hospitalNo = hospitalNo;
	}

	public ReqQueryHospitalUserInfo(InterfaceMessage msg)
			throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		if(ser==null){
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		this.hospitalNo=XMLUtil.getString(ser, "HospitalNo", false);
		this.memberId=XMLUtil.getString(ser, "MemberId", false);
		this.queryHospitalCost=XMLUtil.getString(ser, "QueryHospitalCost", false);
	}

}

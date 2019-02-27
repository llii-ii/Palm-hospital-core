package com.kasite.core.serviceinterface.module.basic.req;


import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 文章入参类
 * 
 * @author caiyouhong
 * @version 1.0
 * @time 2017-7-20 下午5:59:42
 **/
public class ReqQueryArticle extends AbsReq {
	private String hosID;
	private String typeClass;
	private String status;
	private String id;

	public ReqQueryArticle(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.hosID = XMLUtil.getString(ser, "HosId", false);
		this.typeClass = XMLUtil.getString(ser, "TypeClass", false);
		this.status = XMLUtil.getString(ser, "Status", false);
		this.id = XMLUtil.getString(ser, "ID", false);
	}
	
	public ReqQueryArticle(InterfaceMessage msg, String type) throws AbsHosException {
		super(msg);
		
	}

	public String getHosID() {
		return hosID;
	}

	public void setHosID(String hosID) {
		this.hosID = hosID;
	}

	public String getTypeClass() {
		return typeClass;
	}

	public void setTypeClass(String typeClass) {
		this.typeClass = typeClass;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}

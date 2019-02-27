package com.kasite.core.serviceinterface.module.report.req;
import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;
/**
 * @author zwl
 * TODO
 */
public class ReqGetTjReportInfo extends AbsReq {
	private String reportId;
	/**
	 * @return the reportId
	 */
	public String getReportId() {
		return reportId;
	}


	/**
	 * @param reportId the reportId to set
	 */
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public ReqGetTjReportInfo(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		if(ser==null){
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		this.reportId = XMLUtil.getString(ser, "ReportId", true);
	}
}

package com.kasite.core.serviceinterface.module.report;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.serviceinterface.module.report.req.ReqGetReportInfo;
import com.kasite.core.serviceinterface.module.report.req.ReqGetReportList;
import com.kasite.core.serviceinterface.module.report.req.ReqGetTjReportInfo;
import com.kasite.core.serviceinterface.module.report.resp.RespGetReportInfo;
import com.kasite.core.serviceinterface.module.report.resp.RespGetReportList;
import com.kasite.core.serviceinterface.module.report.resp.RespGetTjReportInfo;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO
 */
public interface IReportService{

	/**
	 * 获取报告单
	 * @param msg
	 * @return
	 */
	public String GetReportList(InterfaceMessage msg) throws Exception;
	CommonResp<RespGetReportList> getReportList(CommonReq<ReqGetReportList> commReq) throws Exception;
	/**
	 * 获取报告单信息
	 * @param msg
	 * @return
	 */
	public String GetReportInfo(InterfaceMessage msg) throws Exception;
	CommonResp<RespGetReportInfo> getReportInfo(CommonReq<ReqGetReportInfo> commReq) throws Exception;
	/**
	 * 获取体检报告单信息
	 * @param msg
	 * @return
	 */
	public String GetTjReportInfo(InterfaceMessage msg) throws Exception;
	CommonResp<RespGetTjReportInfo> GetTjReportInfo(CommonReq<ReqGetTjReportInfo> commReq) throws Exception;
}

/**
 * 
 */
package com.kasite.core.serviceinterface.module.yy.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.handler.IPushService;
import com.kasite.core.serviceinterface.module.his.resp.HisStopClinicList;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * 医院推送停诊信息入参
 */
public class ReqStopSchedule extends AbsReq {
	
	private CommonResp<HisStopClinicList> pushStop;
	
	public ReqStopSchedule(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		IPushService service = HandlerBuilder.get().getCallHisService(getAuthInfo(), IPushService.class);
		if(null != service) {
			this.pushStop = service.parseStopSchedule(msg);
		}else {
			Element ser = root.element(KstHosConstant.DATA);
			if(ser==null){
				throw new ParamException("传入参数中[Data]节点不能为空。");
			}
			HisStopClinicList req = new HisStopClinicList();
			String scheduleId = XMLUtil.getString(ser, "ScheduleId", false);	
			String deptCode = XMLUtil.getString(ser, "DeptCode", false);
			String doctorCode = XMLUtil.getString(ser, "DoctorCode", false);
			String regDate = XMLUtil.getString(ser, "RegDate", false);
			Integer isHalt = XMLUtil.getInt(ser, "IsHalt", true);		
			String remark = XMLUtil.getString(ser, "Remark", false);	
			Integer timeSlice = XMLUtil.getInt(ser, "TimeSlice", false);	
			String sourceCode = XMLUtil.getString(ser, "sourceCode", false);	
			if(StringUtil.isBlank(scheduleId) && 
					StringUtil.isBlank(deptCode) && 
					StringUtil.isBlank(doctorCode) && 
					StringUtil.isBlank(regDate) && 
					StringUtil.isBlank(timeSlice) && 
					StringUtil.isBlank(sourceCode)
					) {
				throw new RRException("参数不能都为空：ScheduleId  DeptCode  DoctorCode  RegDate  TimeSlice  sourceCode");
			}
			req.setSourceCode(sourceCode);
			req.setDeptCode(deptCode);
			req.setScheduleId(scheduleId);
			req.setDoctorCode(doctorCode);
			req.setRegDate(regDate);
			req.setIsHalt(isHalt);
			req.setRemark(remark);
			req.setTimeSlice(timeSlice);
			pushStop = new CommonResp<>(msg,null,RetCode.Success.RET_10000, req);
		}
	}

	public CommonResp<HisStopClinicList> getPushStop() {
		return pushStop;
	}

	public void setPushStop(CommonResp<HisStopClinicList> pushStop) {
		this.pushStop = pushStop;
	}
	
	
	
}

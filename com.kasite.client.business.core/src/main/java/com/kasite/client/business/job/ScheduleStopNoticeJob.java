package com.kasite.client.business.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kasite.client.yy.dao.IYyWaterMapper;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.resp.HisStopClinicList;
import com.kasite.core.serviceinterface.module.msg.IMsgService;
import com.kasite.core.serviceinterface.module.order.IOrderService;
import com.kasite.core.serviceinterface.module.yy.IYYService;
import com.kasite.core.serviceinterface.module.yy.req.ReqStop;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 排班停诊通知
 * @author lcz
 *
 */
@Component
public class ScheduleStopNoticeJob {
	
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);
	@Autowired
	KasiteConfigMap config;
	@Autowired
	IYyWaterMapper yyWaterMapper;
	@Autowired
	IMsgService msgService;
	@Autowired
	IOrderService orderService;
	@Autowired
	IYYService yyService;
	
	public void execute(){
		try {
			if(config.isStartJob(this.getClass())) {
				InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(ScheduleStopNoticeJob.class,"queryStopClinicList", 
						null, String.valueOf(UUID.randomUUID()),null, null);
				String hosid = KasiteConfig.getOrgCode();
				Map<String, String> map = new HashMap<String, String>();
				map.put("hosId", hosid);
				map.put("workDateStart", DateOper.getNow("yyyy-MM-dd"));
				CommonResp<HisStopClinicList> commResp = HandlerBuilder.get().getCallHisService(hosid).queryStopClinicList(msg, map);
				if(null != commResp && KstHosConstant.SUCCESSCODE.equals(commResp.getCode())) {
					List<HisStopClinicList> list = commResp.getData();
					ReqStop t = new ReqStop(msg);
					t.setList(list);
					CommonReq<ReqStop> req = new CommonReq<ReqStop>(t);
					yyService.pushStop(req);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e);
		}
	}
	
//	public static void main(String[] args) {
//		String content = "{\"channelType\":1,\"msgType\": 3, \"parmContent\": {\"touser\":\"<OpenId>\","
//				+ " \"template_id\":\"4hdsaCIhb1dz4yJthpifgpBH_Cer0dzCMuH5T7CI1zM\","
//				+ "\"data\":{"
//				+ "\"first\":{\"value\":\"很抱歉，接到医院通知，您预约的医生临时停诊，具体如下：\"},"
//				+ "\"keyword1\":{\"value\":\"<UserName>\"},"
//				+ "\"keyword2\":{\"value\":\"<HosName>\"},"
//				+ "\"keyword3\":{\"value\":\"<DeptName>\"},"
//				+ "\"keyword4\":{\"value\":\"<DoctorName>\"},"
//				+ "\"keyword5\":{\"value\":\"<Time>\"},"
//				+ "\"remark\":{\"value\":"+"\"医院已经取消您的预约，请您重新挂号\""+"}"
//				+ "}}}";
//		System.out.println(content);
//	}
}

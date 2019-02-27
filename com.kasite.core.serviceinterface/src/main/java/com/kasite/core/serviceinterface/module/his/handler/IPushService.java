package com.kasite.core.serviceinterface.module.his.handler;

import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.service.ICallHis;
import com.kasite.core.serviceinterface.module.his.resp.HisRecipeClinicList;
import com.kasite.core.serviceinterface.module.his.resp.HisStopClinicList;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 医院推送的消息解析方法
 * @author daiyanshui
 */
public interface IPushService extends ICallHis{

	/**
	 * HIS推送的排班停诊信息
	 * 解析排班停诊消息进行消息推送
	 * @param param
	 * @return
	 */
	CommonResp<HisStopClinicList> parseStopSchedule(InterfaceMessage msg);
	/**
	 * HIS推送的处方开药信息
	 * 解析处方开药消息进行消息推送
	 * @param param
	 * @return
	 */
	CommonResp<HisRecipeClinicList> parseRecipe(InterfaceMessage msg);
	
	
	
}

package com.kasite.core.serviceinterface.module.his.handler;

import java.util.Map;

import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.service.ICallHis;
import com.kasite.core.serviceinterface.module.his.resp.HisCheckEntityCard;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryHospitalUserInfo;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * mini付需要实现的HIS接口
 * @author daiyanshui
 *
 */
public interface IMiniPayService extends ICallHis{

	/**
	 * 校验实体卡
	 * 传入卡号 CardNo 实体卡读取的卡信息
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	CommonResp<HisCheckEntityCard> CheckEntityCard(InterfaceMessage msg,Map<String, String> paramMap) throws Exception ;
	
	/**
	 * 通过配置信息获取 对应的商户号 如果返回为空则默认使用 默认配置
	 * @param msg
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> GetConfigKeyByGuidInfo(InterfaceMessage msg,Map<String, String> paramMap) throws Exception ;
	
	
	/**
	 * 根据住院号查询住院患者信息 mini付场景使用该接口
	 * 参数只传入住院号
	 * @param msg
	 * @param map
	 * @return
	 */
	CommonResp<HisQueryHospitalUserInfo> queryHospitalUserInfoByHospiatlNo(InterfaceMessage msg,Map<String, String> map) throws Exception;
	
	
}

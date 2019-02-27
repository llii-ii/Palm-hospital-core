package com.kasite.core.serviceinterface.module.channel;

import java.util.List;
import java.util.Map;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.req.PageVo;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.module.channel.dto.MerchConfigVo;
import com.kasite.core.serviceinterface.module.channel.req.ReqQueryChannel;
import com.kasite.core.serviceinterface.module.channel.req.ReqQueryPayConfig;
import com.kasite.core.serviceinterface.module.channel.resp.RespChannelList;
import com.kasite.core.serviceinterface.module.channel.resp.RespPayTypeList;
import com.kasite.core.serviceinterface.module.channel.resp.RespQueryChannelMerchInfo;
import com.kasite.core.serviceinterface.module.channel.resp.RespQueryPayConfig;
import com.kasite.core.serviceinterface.module.channel.req.ReqQueryBank;
import com.kasite.core.serviceinterface.module.channel.resp.RespQueryBankCrad;

public interface IChannelService {

	/**
	 * 获取已配置的支付方式
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespPayTypeList> queryAllPayType(CommonReq<ReqQueryChannel> commReq) throws Exception;
	
	/**
	 * 获取已配置的交易渠道
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespChannelList> queryAllChennel(CommonReq<ReqQueryChannel> commReq) throws Exception;
	
	/**
	 * 获取商户配置信息
	 * 
	 * @param page
	 * @param account
	 * @return
	 * @throws Exception
	 */
	List<MerchConfigVo> queryAllMerchConfigList(PageVo page, String account) throws Exception;
	
	/**
	 * 获取渠道商户配置信息(详情)
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryChannelMerchInfo> queryChannelMerchInfo(CommonReq<ReqQueryChannel> commReq) throws Exception;
	
	/**
	 * 获取商户类型
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	List<MerchConfigVo> queryMerchType() throws Exception;
	
	/**
	 * 获取银行名称
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	Map<String, String> queryBankShort() throws Exception;
	
	/**
	 * 查询支付配置信息
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryPayConfig> queryPayConfig(CommonReq<ReqQueryPayConfig> commReq) throws Exception;
	
	/**
	 * 查询支付配置信息
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> updatePayConfig(CommonReq<ReqQueryPayConfig> commReq) throws Exception;
	
	/**
	 * 查询渠道商户配置信息(列表)
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryChannelMerchInfo> queryMerchConfigList(CommonReq<ReqQueryChannel> commReq) throws Exception;
	
	/**
	 * 查询渠道信息
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryChannelMerchInfo> querySysChannelInfo(CommonReq<ReqQueryChannel> commReq) throws Exception;
	
	/**
	 * 查询渠道API权限配置
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> querySmartPayApi(CommonReq<ReqQueryChannel> commReq) throws Exception;
	
	/**
	 * 渠道的商户配置API权限
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryChannelMerchInfo> queryChannelMerchAndApi(CommonReq<ReqQueryChannel> commReq) throws Exception;
	
	
	/**
	 * 获取所有的银行卡号
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryBankCrad> queryAllBankNo(CommonReq<ReqQueryBank> commReq) throws Exception;
	
}

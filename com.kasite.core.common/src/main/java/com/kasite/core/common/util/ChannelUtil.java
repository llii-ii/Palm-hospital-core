//package com.kasite.core.common.util;
//
//import com.kasite.core.common.constant.KstHosConstant;
//
///**
// * 渠道工具类
// *
// * @author 無
// * @version V1.0
// * @date 2018年4月24日 下午3:24:50
// */
//public class ChannelUtil {
//
//	/**
//	 * 未知渠道名称
//	 */
//	public static String UNKNOW_CHANNELNAME = "";
//
//	/**
//	 * 根据渠道ID，查询有效的渠道名称
//	 * 
//	 * @param channelId
//	 *            渠道ID
//	 * @return 渠道名称
//	 */
//	public static String getChannelName(String channelId) {
//		
//		if(StringUtil.isBlank(channelId)) {
//			return UNKNOW_CHANNELNAME;
//		}
//		if (KstHosConstant.WX_CHANNEL_ID.equals(channelId)) {
//			return KstHosConstant.WX_CHANNEL_NAME;
//		} else if (KstHosConstant.ZFB_CHANNEL_ID.equals(channelId)) {
//			return KstHosConstant.ZFB_CHANNEL_NAME;
//		} else if(KstHosConstant.JKZL_CHANNEL_ID.equals(channelId)){
//			return KstHosConstant.JKZL_CHANNEL_NAME;
//		}
//		return UNKNOW_CHANNELNAME;
//	}
//}

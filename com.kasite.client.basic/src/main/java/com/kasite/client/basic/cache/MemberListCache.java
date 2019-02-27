package com.kasite.client.basic.cache;
import com.coreframework.util.StringUtil;
import com.kasite.core.common.util.ExpiryMap;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryMemberList;

public class MemberListCache {
	/***
	 * 默认缓存1小时数据 ，如果超过1小时数据从内存中释放
	 */
	private static long defaultExpiryTime = 1 * 60 * 60 * 1000;
	private static ExpiryMap<String, RespQueryMemberList> map = new ExpiryMap<>(defaultExpiryTime);
	private static MemberListCache me;
	private static long version = 0;
	private final static Object obj = new Object();
	private MemberListCache() {
	}
	public RespQueryMemberList getValue(String memberId,String cardNo,String cardType,String openId) {
		if(StringUtil.isNotBlank(memberId) && StringUtil.isNotBlank(cardType)  && StringUtil.isNotBlank(openId)) {
			return map.get(version+"_"+memberId+"_"+cardType+"_"+openId);
		}else if(StringUtil.isNotBlank(cardNo) && StringUtil.isNotBlank(cardType) && StringUtil.isNotBlank(openId)) {
			return map.get(version+"_"+cardNo+"_"+cardType+"_"+openId);
		}else {
			return null;
		}
	}
	public void add2MemberId(String memberId,String openId,String cardType,RespQueryMemberList value) {
		if(StringUtil.isNotBlank(memberId) && StringUtil.isNotBlank(cardType)  && StringUtil.isNotBlank(openId)) {
			map.put(version+"_"+memberId+"_"+cardType+"_"+openId, value);
		}
	}
	public void add2CardNo(String cardNo,String cardType,String openId,RespQueryMemberList value) {
		if(StringUtil.isNotBlank(cardNo) && StringUtil.isNotBlank(cardType)  && StringUtil.isNotBlank(openId)) {
			map.put(version+"_"+cardNo+"_"+cardType+"_"+openId, value);
		}
	}
	/**
	 * 清楚单个缓存
	 * @param memberId
	 * @param openId
	 */
	public void clearMemberCardNo(String cardNo,String cardType,String openId) {
		map.remove(version+"_"+cardNo+"_"+cardType+"_"+openId);
	}
	/**
	 * 清楚单个缓存
	 * @param memberId
	 * @param openId
	 */
	public void clearMemberMemberId(String memberId,String cardType,String openId) {
		map.remove(version+"_"+memberId+"_"+cardType+"_"+openId);
	}
	/**
	 * 清理缓存
	 */
	public void addVersion() {
		version = version +1;
	}
	/**
	 * 单例
	 * @return
	 */
	public static MemberListCache me() {
		if(null == me) {
			synchronized (obj) {
				me = new MemberListCache();
			}
		}
		return me;
	}
}

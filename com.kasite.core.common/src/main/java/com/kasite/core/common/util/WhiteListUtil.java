package com.kasite.core.common.util;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kasite.core.common.config.KasiteConfig;

/**
 * 白名单帮助类
 * @author lcz
 *
 */
@Component
public class WhiteListUtil {
	
	
	private boolean openWhiteList = true;
	
	private Map<String, Object> whiteMap = new HashMap<>();
	@Autowired
	private KasiteConfig kasiteConfig;
	
	@PostConstruct
	public void init() {
		if(StringUtil.isNotBlank(kasiteConfig.getOpenWhiteList())) {
			openWhiteList = Boolean.valueOf(kasiteConfig.getOpenWhiteList());
		}
	}
	/**
	 * 新增用户为白名单用户
	 * @param openId
	 */
	public void add(String openId) {
		whiteMap.put(openId, 1);
	}
	/**
	 * 移除单个白名单用户
	 * @param openId
	 */
	public void remove(String openId) {
		whiteMap.remove(openId);
	}
	/**
	 * 移除所有白名单用户
	 */
	public void removeAll() {
		whiteMap.clear();
	}
	/**
	 * 是否白名单用户
	 * @param openId
	 * @return
	 */
	public boolean isWhiteUser(String openId) {
		return whiteMap.containsKey(openId);
	}
	/**
	 * 是否开启白名单 true 开启
	 * @return
	 */
	public boolean isOpenWhiteList() {
		return this.openWhiteList;
	}
	
	/**
	 * 开启或关闭白名单，关闭白名单时，默认会清空所有白名单用户列表
	 * @param operType	1开启 其他关闭
	 */
	public void openOrCloseWhileList(int operType) {
		if(operType==1) {
			//开启
			openWhileList();
		}else if(operType==2){
			//关闭，但不清空白名单用户
			this.openWhiteList = false;
		}else {
			//关闭且清空白名单用户
			closeAndRemoveAllWhileList();
		}
	}
	/**
	 * 开启白名单
	 */
	public void openWhileList() {
		//开启
		this.openWhiteList = true;
	}
	
	/**
	 * 关闭并移除所有白名单用户
	 */
	public void closeAndRemoveAllWhileList() {
		//关闭
		this.openWhiteList = false;
		
		whiteMap.clear();
	}
}

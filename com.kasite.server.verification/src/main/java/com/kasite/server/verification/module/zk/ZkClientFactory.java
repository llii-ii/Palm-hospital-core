package com.kasite.server.verification.module.zk;

import java.util.concurrent.ConcurrentHashMap;

import com.kasite.core.common.util.StringUtil;

/**
 * @author Administrator
 *
 */
public class ZkClientFactory {

	private ConcurrentHashMap <String,ZkClientWrapper> zkClientWrapperMap;
	private static final ZkClientFactory  instance=new ZkClientFactory();
	private static final Object  lock=new Object();
	private final static String DEFAULT_ZK_URL="127.0.0.1:2181";
	private static String defaultURL=DEFAULT_ZK_URL;
	public static ZkClientFactory  getInstance()
	{
		return instance;
	}
	
	/**
	 * 设置zk的默认路径，并返回真正的zk地址
	 * @param zkService
	 * @return
	 */
	public static String setDefaultURL(String zkService) {
		if(zkService==null){
			return null;
		}
		zkService=zkService.trim();
		defaultURL=zkService;
		return zkService;
	}

	private ZkClientFactory()
	{
		zkClientWrapperMap=new ConcurrentHashMap <String,ZkClientWrapper>();
	}
	public ZkClientWrapper getZkClient(String zkUrl,int connectionTimeout) throws Exception
	{
		
		synchronized (lock) {
			if(StringUtil.isNotBlank(zkUrl)) {
				ZkClientWrapper wrapper= this.zkClientWrapperMap.get(zkUrl);
				if(wrapper==null)
				{
					wrapper=new  ZkClientWrapper(zkUrl,connectionTimeout);
					this.zkClientWrapperMap.put(zkUrl, wrapper);
					System.out.println("构造ZkClient："+zkUrl);
				}
				return this.zkClientWrapperMap.get(zkUrl);
			}
			return null;
		}
		
	}
	public ZkClientWrapper getZkClient(String zkUrl) throws Exception
	{
		return getZkClient( zkUrl,20000);
	}
	
}

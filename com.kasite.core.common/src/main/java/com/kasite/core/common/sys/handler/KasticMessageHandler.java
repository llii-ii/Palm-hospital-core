package com.kasite.core.common.sys.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kasite.core.common.sys.verification.VerificationBuser;

public class KasticMessageHandler implements Runnable{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/**消息状态  处理从中心获取的所有消息的列表*/
	private int status;
	public KasticMessageHandler(int status,String gid) {
		this.status = status;
	}
	@Override
	public void run() {
		
		try {
			switch (status) {
			case 0:{
				//正常心跳消息，不处理。
				break;
			}
			//获取API配置信息
			case 1:{
				//获取本 appId 的可以调用的 appid 列表 
				/*
				[
					{ 
						orgCode:123123, appId:123123, apis:["abc.abc.abc","bcd.b.c","c.d.x"], whiteIp:['123.123.123.12','123.123.123.12','http://www.baidu.com']
					}
				]
				*/
				break;
			}
			case 2:{
				//上报内存／线程／堆栈等信息
				
				break;
			}
			case 3:{
				//获取 appId 对应的 access_token 权限
				
				break;
			}
			case 4:{
				//获取当前实例的appId 的 微信 token 和 支付宝 token
				
				
				break;
			}
			case 5:{
				//初始化本地配置
				VerificationBuser.create().init();	
				break;
			}
			case 98:{
				//重新获取token
				
				break;
			}
			//系统初始化重启
			case 99:{
				
				break;
			}
			default:
				break;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("消息处理异常：",e);
		}
	}

}

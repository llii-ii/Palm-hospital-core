package com.kasite.client.crawler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.coreframework.remoting.Server;
import com.kasite.client.crawler.config.vo.RpcConfigVo;

@Configuration
public class RpcConfig {

    @Value("${rpc.appId:#{null}}")
    private String appId;
    @Value("${rpc.appName:#{null}}")
    private String appName;
    @Value("${rpc.port:#{null}}")
    private Integer port;
    @Value("${rpc.startRpc:#{null}}")
    private boolean startRpc;
	
    public void shutdown(){
		try {
			if(startRpc){
				Server.getInstance(port).dispose();
				System.exit(0);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    @Bean
	public RpcConfigVo getRpcConfigVo(){
    		RpcConfigVo vo = new RpcConfigVo();
    		vo.setAppId(appId);
    		vo.setAppName(appName);
    		vo.setPort(port);
    		vo.setStartRpc(startRpc);
    		return vo;
	}
    
	
}

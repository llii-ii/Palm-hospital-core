package com.kasite.server.admin.wechat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kasite.server.admin.dingtalk.DingDingNotifier;

import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;

@Configuration
public class WeChatNotifierConfiguration {

	@Bean
	public WeChatNotifier weChatNotifier(InstanceRepository repository) {
		return new WeChatNotifier(repository);
	}
	
	@Bean
	public DingDingNotifier dingDingNotifier(InstanceRepository repository) {
		return new DingDingNotifier(repository);
	}

}

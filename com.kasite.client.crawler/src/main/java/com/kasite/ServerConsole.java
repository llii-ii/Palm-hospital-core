package com.kasite;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.kasite.client.crawler.config.MyDatabaseEnum;


@SpringBootApplication
@EnableScheduling
public class ServerConsole extends SpringBootServletInitializer {

	public static void main(String[] args) throws Exception {
		//2分钟超时
		System.setProperty("DB_TIMEOUT","240000");
		//强制回收连接时间2分钟  貌似没什么卵效果
		System.setProperty(MyDatabaseEnum.hisdb.name()+".abandonedTimeout", "240");
		SpringApplication.run(ServerConsole.class, args);
	} 

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ServerConsole.class);
	}

}

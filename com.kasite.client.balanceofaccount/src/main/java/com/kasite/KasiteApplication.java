package com.kasite;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;



/**
 * @author cc
 */
@SpringBootApplication
@EnableScheduling
@MapperScan("com.kasite.client.hospay.module.bill.dao")
public class KasiteApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(KasiteApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(KasiteApplication.class);
	}

}

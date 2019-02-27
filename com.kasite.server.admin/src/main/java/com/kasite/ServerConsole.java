package com.kasite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
@EnableAdminServer
@SpringBootApplication
public class ServerConsole {
	public static void main(String[] args) {
		SpringApplication.run(ServerConsole.class, args);
	}
	
	@Configuration
	public static class SecuritySecureConfig extends WebSecurityConfigurerAdapter {
		private final String adminContextPath;

		public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
			this.adminContextPath = adminServerProperties.getContextPath();
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
			successHandler.setTargetUrlParameter("redirectTo");

			http.authorizeRequests().antMatchers(adminContextPath + "/assets/**",adminContextPath + "/dingTalk/**",adminContextPath + "/weChat/**",adminContextPath + "/actuator/**",adminContextPath + "/instances/**").permitAll()
					.antMatchers(adminContextPath + "/login",adminContextPath + "/dingTalk/**",adminContextPath + "/weChat/**",adminContextPath + "/actuator/**",adminContextPath + "/instances/**").permitAll().anyRequest().authenticated().and().formLogin()
					.loginPage(adminContextPath + "/login").successHandler(successHandler).and().logout()
					.logoutUrl(adminContextPath + "/logout").and().httpBasic().and().csrf().disable();
		}
	}

}
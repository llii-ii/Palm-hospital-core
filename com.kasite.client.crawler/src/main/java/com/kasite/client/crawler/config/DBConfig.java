package com.kasite.client.crawler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBConfig {
    @Value("${datasource.alias:#{null}}")
    private String alias;
    @Value("${datasource.dbtype:#{null}}")
    private String dbtype;
    @Value("${datasource.ip:#{null}}")
    private String ip;
    @Value("${datasource.username:#{null}}")
    private String username;
    @Value("${datasource.password:#{null}}")
    private String password;
    @Value("${datasource.port:#{null}}")
    private Integer port;
    @Value("${datasource.databaseName:#{null}}")
    private String databaseName;
    @Value("${datasource.testsql:#{null}}")
    private String testsql;
//    @Bean("db")
//    @Primary
//	public DB dbName(){
//    		DB.me().addDataSourceOracle(alias, databaseName, ip, port, username, password);
//    		return DB.me();
////    		return DB.me().addDataSource(alias, ds, ip, dbName);
//	}
    public String getTestsql() {
		return testsql;
	}
}

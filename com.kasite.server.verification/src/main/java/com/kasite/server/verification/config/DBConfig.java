//package com.kasite.server.verification.config;
//
//import java.sql.SQLException;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//import com.coreframework.db.DB;
//import com.coreframework.db.Sql;
//import com.kasite.server.verification.common.dao.MyDatabaseEnum;
//
//@Configuration
//public class DBConfig {
//    @Value("${datasource.alias:#{null}}")
//    private String alias;
//    @Value("${datasource.dbtype:#{null}}")
//    private String dbtype;
//    @Value("${datasource.ip:#{null}}")
//    private String ip;
//    @Value("${datasource.username:#{null}}")
//    private String username;
//    @Value("${datasource.password:#{null}}")
//    private String password;
//    @Value("${datasource.port:#{null}}")
//    private Integer port;
//    @Value("${datasource.databaseName:#{null}}")
//    private String databaseName;
//    @Value("${datasource.testsql:#{null}}")
//    private String testsql;
//    @Bean("db")
//    @Primary
//	public DB dbName(){
//    		switch (dbtype) {
//			case "mysql":
//				DB.me().addDataSourceMySQL(alias, databaseName, ip, port, username, password);
//				break;
//			case "oracle":{
//				DB.me().addDataSourceOracle(alias, databaseName, ip, port, username, password);
//				break;
//			}
//			default:
//				break;
//			}
//    		return DB.me();
//	}
//    public String getTestsql() {
//		return testsql;
//	}
//    @Bean("testDbResult")
//    public String testDB(DB db){
//    	   	try {
//   			int i = db.queryForInteger(MyDatabaseEnum.valueOf(alias), new Sql(getTestsql()));
//   			System.out.println(i);
//   		} catch (SQLException e) {
//   			// TODO Auto-generated catch block
//   			e.printStackTrace();
//   		}
//    		return "";
//    }
//}

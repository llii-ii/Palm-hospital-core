//package com.kasite.client.crawler.config;
//
//
//import java.sql.SQLException;
//
//import org.apache.commons.dbcp.BasicDataSource;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.common.json.JSONObject;
//import com.coreframework.db.DB;
//import com.coreframework.db.Sql;
//import com.kasite.core.common.config.DBType;
//import com.kasite.core.common.util.StringUtil;
//
///**
// * 数据源3_lis
// */
//@Configuration
//public class DruidConfig3 {
//
//	private static final String SQLSERVER_DriverClass = "net.sourceforge.jtds.jdbc.Driver";
//	private static final String ORACLE_DriverClass = "oracle.jdbc.driver.OracleDriver";
//	private static final String MYSQL_DriverClass = "com.mysql.jdbc.Driver";
//	
//	private static Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);
//    @Value("${kasite.lis.alias:#{null}}")
//    private String alias;
//    @Value("${kasite.lis.ip:#{null}}")
//    private String ip;
//    @Value("${kasite.lis.port:#{null}}")
//    private String port;
//    @Value("${kasite.lis.databaseName:#{null}}")
//    private String databaseName;
//    @Value("${kasite.lis.url:#{null}}")
//    private String dbUrl;
//    @Value("${kasite.lis.username: #{null}}")
//    private String username;
//    @Value("${kasite.lis.password:#{null}}")
//    private String password;
//    @Value("${kasite.lis.driverClassName:#{null}}")
//    private String driverClassName;
//    @Value("${kasite.zk.initialSize:#{null}}")
//    private Integer initialSize;
//    @Value("${kasite.lis.minIdle:#{null}}")
//    private Integer minIdle;
//    @Value("${kasite.lis.maxActive:#{null}}")
//    private Integer maxActive;
//    @Value("${kasite.lis.maxWait:#{null}}")
//    private Integer maxWait;
//    @Value("${kasite.lis.timeBetweenEvictionRunsMillis:#{null}}")
//    private Integer timeBetweenEvictionRunsMillis;
//    @Value("${kasite.lis.minEvictableIdleTimeMillis:#{null}}")
//    private Integer minEvictableIdleTimeMillis;
//    @Value("${kasite.lis.validationQuery:#{null}}")
//    private String validationQuery;
//    @Value("${kasite.lis.testWhileIdle:#{null}}")
//    private Boolean testWhileIdle;
//    @Value("${kasite.lis.testOnBorrow:#{null}}")
//    private Boolean testOnBorrow;
//    @Value("${kasite.lis.testOnReturn:#{null}}")
//    private Boolean testOnReturn;
//    @Value("${kasite.lis.poolPreparedStatements:#{null}}")
//    private Boolean poolPreparedStatements;
//    @Value("${kasite.lis.maxPoolPreparedStatementPerConnectionSize:#{null}}")
//    private Integer maxPoolPreparedStatementPerConnectionSize;
//    @Value("${kasite.lis.filters:#{null}}")
//    private String filters;
//    @Value("{kasite.lis.connectionProperties:#{null}}")
//    private String connectionProperties;
//    @Value("${kasite.lis.showSql:#{null}}")
//    private Boolean showSql;
//    @Value("${kasite.lis.dbType:#{null}}")
//    private String dbType;
//    /**
//     * 获取数据库类型： mysql／oracle／sqlserver
//     * @return
//     */
//    public DBType getDbType() {
//		return DBType.valueOf(dbType);
//	}
//	@Bean(name="baseDaoEnum")
//	public MyDatabaseEnum dbName(){
//		return MyDatabaseEnum.valueOf(alias);
//	}
//    @Bean(name="lis")
//    public DB dataSource(){
//    	if(getDbType().equals(DBType.mysql)) {
//    		driverClassName = MYSQL_DriverClass;
//    	}
//    	if(getDbType().equals(DBType.oracle)) {
//    		driverClassName = ORACLE_DriverClass;
//    	}
//    	if(getDbType().equals(DBType.sqlserver)) {
//    		driverClassName = SQLSERVER_DriverClass;
//    	}
//    	if(StringUtil.isBlank(driverClassName)) {
//    		driverClassName = MYSQL_DriverClass;
//    	}
//    	
//    	System.out.println("-------------------------------------------");
//    	System.out.println("dbUrl:"+dbUrl);
//    	System.out.println("username:"+username);
//    	System.out.println("driverClassName:"+driverClassName);
//    	System.out.println("alias:"+alias);
//    	System.out.println("ip:"+ip);
//    	System.out.println("-------------------------------------------");
//    	long s = System.currentTimeMillis();
//
//    	
//    	BasicDataSource ds = new BasicDataSource();
//		ds.setMaxActive(150);
//		ds.setDriverClassName(driverClassName);
//		
//		boolean isOracle=false;
//		
//		if (driverClassName.equalsIgnoreCase(ORACLE_DriverClass)) 
//		{
//			isOracle=true;
//			ds.setUrl("jdbc:oracle:thin:@//" + ip + ":" + port + "/"+ databaseName);
//			ds.setValidationQuery("select 1 from dual");
//		} 
//		else 
//		{
//			if(driverClassName.equalsIgnoreCase(MYSQL_DriverClass))
//			{
//				
//				ds.setUrl("jdbc:mysql://"+ip+":"+port+"/"+databaseName+"??useUnicode=true&characterEncoding=utf-8&useSSL=false&useOldAliasMetadataBehavior=true");
//				ds.setValidationQuery("select 1 ");
//			}
//			else
//			{
//				ds.setUrl("jdbc:jtds:sqlserver://" + ip+ ":"
//					+ port + ";DatabaseName=" + databaseName);
//				ds.setValidationQuery("select 1 ");
//			}
//		}
//		ds.setUsername(username);
//		ds.setPassword(password);
//		ds.setMinIdle(4);
//		ds.setMaxIdle(100);
//		ds.setMaxWait(8000);
//		
//		ds.setTestOnBorrow(Boolean.getBoolean(alias+".testOnBorrow"));
//		ds.setTestOnReturn(Boolean.getBoolean(alias+".testOnReturn"));
//		ds.setTestWhileIdle(true);
//		ds.setRemoveAbandoned(true);
//		ds.setRemoveAbandonedTimeout(Integer.getInteger(alias+".abandonedTimeout", 30));//��λ��
//		ds.setLogAbandoned(true);
//		ds.setTimeBetweenEvictionRunsMillis(Long.getLong("db.timeBetweenEvictionRunsMillis", 1000*60));
//		if(isOracle){
//			ds.setMinEvictableIdleTimeMillis(Long.getLong("db.oracle.minEvictableIdleTimeMillis", 1000*3600*2));
//		}else{
//			ds.setMinEvictableIdleTimeMillis(Long.getLong("db.minEvictableIdleTimeMillis", 1000*60));
//		}
//		DB.me().addDataSource(alias, ds, ip, databaseName);
////        DB.setShowSql(showSql);
//        Sql sql = new Sql(validationQuery);
//        try {
//			JSONObject json = DB.me().queryForJson(MyDatabaseEnum.valueOf(alias), sql);
//			logger.info("数据库查询验证："+json.toString());
//		} catch (SQLException e) {
//			e.printStackTrace();
//			logger.error("初始化数据库异常",e);
//		}
//        System.out.println("times = "+(System.currentTimeMillis() - s));
//        return DB.me();
//    }
//}
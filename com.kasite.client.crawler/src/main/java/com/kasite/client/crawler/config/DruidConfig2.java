package com.kasite.client.crawler.config;


import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.common.json.JSONObject;
import com.coreframework.db.DB;
import com.coreframework.db.Sql;
import com.kasite.core.common.config.DBType;
import com.kasite.core.common.util.StringUtil;

/**
 * 数据源2_zk_crawler_zk
 */
@Configuration
public class DruidConfig2 {

	private static final String SQLSERVER_DriverClass = "net.sourceforge.jtds.jdbc.Driver";
	private static final String ORACLE_DriverClass = "oracle.jdbc.driver.OracleDriver";
	private static final String MYSQL_DriverClass = "com.mysql.jdbc.Driver";
	
	private static Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);
    @Value("${kasite.zk.alias:#{null}}")
    private String alias;
    @Value("${kasite.zk.ip:#{null}}")
    private String ip;
    @Value("${kasite.zk.port:#{null}}")
    private String port;
    @Value("${kasite.zk.databaseName:#{null}}")
    private String databaseName;
    @Value("${kasite.zk.url:#{null}}")
    private String dbUrl;
    @Value("${kasite.zk.username: #{null}}")
    private String username;
    @Value("${kasite.zk.password:#{null}}")
    private String password;
    @Value("${kasite.zk.driverClassName:#{null}}")
    private String driverClassName;
    @Value("${kasite.zk.initialSize:#{null}}")
    private Integer initialSize;
    @Value("${kasite.zk.minIdle:#{null}}")
    private Integer minIdle;
    @Value("${kasite.zk.maxActive:#{null}}")
    private Integer maxActive;
    @Value("${kasite.zk.maxWait:#{null}}")
    private Integer maxWait;
    @Value("${kasite.zk.timeBetweenEvictionRunsMillis:#{null}}")
    private Integer timeBetweenEvictionRunsMillis;
    @Value("${kasite.zk.minEvictableIdleTimeMillis:#{null}}")
    private Integer minEvictableIdleTimeMillis;
    @Value("${kasite.zk.validationQuery:#{null}}")
    private String validationQuery;
    @Value("${kasite.zk.testWhileIdle:#{null}}")
    private Boolean testWhileIdle;
    @Value("${kasite.zk.testOnBorrow:#{null}}")
    private Boolean testOnBorrow;
    @Value("${kasite.zk.testOnReturn:#{null}}")
    private Boolean testOnReturn;
    @Value("${kasite.zk.poolPreparedStatements:#{null}}")
    private Boolean poolPreparedStatements;
    @Value("${kasite.zk.maxPoolPreparedStatementPerConnectionSize:#{null}}")
    private Integer maxPoolPreparedStatementPerConnectionSize;
    @Value("${kasite.zk.filters:#{null}}")
    private String filters;
    @Value("{kasite.zk.connectionProperties:#{null}}")
    private String connectionProperties;
    @Value("${kasite.zk.showSql:#{null}}")
    private Boolean showSql;
    @Value("${kasite.zk.dbType:#{null}}")
    private String dbType;
    /**
     * 获取数据库类型： mysql／oracle／sqlserver
     * @return
     */
    public DBType getDbType() {
		return DBType.valueOf(dbType);
	}
	@Bean(name="baseDaoEnum")
	public MyDatabaseEnum dbName(){
		return MyDatabaseEnum.valueOf(alias);
	}
    @Bean(name="zk")
    public DB dataSource(){
    	if(getDbType().equals(DBType.mysql)) {
    		driverClassName = MYSQL_DriverClass;
    	}
    	if(getDbType().equals(DBType.oracle)) {
    		driverClassName = ORACLE_DriverClass;
    	}
    	if(getDbType().equals(DBType.sqlserver)) {
    		driverClassName = SQLSERVER_DriverClass;
    	}
    	if(StringUtil.isBlank(driverClassName)) {
    		driverClassName = MYSQL_DriverClass;
    	}
    	
    	System.out.println("-------------------------------------------");
    	System.out.println("dbUrl:"+dbUrl);
    	System.out.println("username:"+username);
    	System.out.println("driverClassName:"+driverClassName);
    	System.out.println("alias:"+alias);
    	System.out.println("ip:"+ip);
    	System.out.println("-------------------------------------------");
    	long s = System.currentTimeMillis();

    	
    	BasicDataSource ds = new BasicDataSource();
		ds.setMaxActive(150);
		ds.setDriverClassName(driverClassName);
		
		boolean isOracle=false;
		
		if (driverClassName.equalsIgnoreCase(ORACLE_DriverClass)) 
		{
			isOracle=true;
			ds.setUrl("jdbc:oracle:thin:@//" + ip + ":" + port + "/"+ databaseName);
			ds.setValidationQuery("select 1 from dual");
		} 
		else 
		{
			if(driverClassName.equalsIgnoreCase(MYSQL_DriverClass))
			{
				
				ds.setUrl("jdbc:mysql://"+ip+":"+port+"/"+databaseName+"??useUnicode=true&characterEncoding=utf-8&useSSL=false&useOldAliasMetadataBehavior=true");
				ds.setValidationQuery("select 1 ");
			}
			else
			{
				ds.setUrl("jdbc:jtds:sqlserver://" + ip+ ":"
					+ port + ";DatabaseName=" + databaseName);
				ds.setValidationQuery("select 1 ");
			}
		}
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setMinIdle(4);
		ds.setMaxIdle(100);
		ds.setMaxWait(8000);
		
		ds.setTestOnBorrow(Boolean.getBoolean(alias+".testOnBorrow"));
		ds.setTestOnReturn(Boolean.getBoolean(alias+".testOnReturn"));
		ds.setTestWhileIdle(true);
		ds.setRemoveAbandoned(true);
		ds.setRemoveAbandonedTimeout(Integer.getInteger(alias+".abandonedTimeout", 30));//��λ��
		ds.setLogAbandoned(true);
		ds.setTimeBetweenEvictionRunsMillis(Long.getLong("db.timeBetweenEvictionRunsMillis", 1000*60));
		if(isOracle){
			ds.setMinEvictableIdleTimeMillis(Long.getLong("db.oracle.minEvictableIdleTimeMillis", 1000*3600*2));
		}else{
			ds.setMinEvictableIdleTimeMillis(Long.getLong("db.minEvictableIdleTimeMillis", 1000*60));
		}
		DB.me().addDataSource(alias, ds, ip, databaseName);
//        DB.setShowSql(showSql);
        Sql sql = new Sql(validationQuery);
        try {
			JSONObject json = DB.me().queryForJson(MyDatabaseEnum.valueOf(alias), sql);
			logger.info("数据库查询验证："+json.toString());
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("初始化数据库异常",e);
		}
        System.out.println("times = "+(System.currentTimeMillis() - s));
        return DB.me();
    }
}

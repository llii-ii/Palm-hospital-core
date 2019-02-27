package com.kasite.client.hospay.config;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.kasite.client.hospay.common.dao.MyDatabaseEnum;

/**
 * Druid配置
 *
 * @author chenshun
 * @email 343675979@qq.com
 * @date 2017-04-21 0:00
 */
@Configuration
public class DruidConfig {

    /** MasterDBConfig*/

    @Value("${spring.datasource.master.alias:#{null}}")
    private String alias;
    @Value("${spring.datasource.master.ip:#{null}}")
    private String ip;
    @Value("${spring.datasource.master.databaseName:#{null}}")
    private String databaseName;
    @Value("${spring.datasource.master.url:#{null}}")
    private String dbUrl;
    @Value("${spring.datasource.master.username: #{null}}")
    private String username;
    @Value("${spring.datasource.master.password:#{null}}")
    private String password;
    @Value("${spring.datasource.master.showSql:#{null}}")
    private Boolean showSql;
    @Value("${spring.datasource.master.driver-class-name:#{null}}")
    private String driverClassName;

    /** SlaveDBConfig*/
    @Value("${spring.datasource.slave.alias:#{null}}")
    private String aliasSlave;
    @Value("${spring.datasource.slave.ip:#{null}}")
    private String ipSlave;
    @Value("${spring.datasource.slave.databaseName:#{null}}")
    private String databaseNameSlave;
    @Value("${spring.datasource.slave.url:#{null}}")
    private String dbUrlSlave;
    @Value("${spring.datasource.slave.username: #{null}}")
    private String usernameSlave;
    @Value("${spring.datasource.slave.password:#{null}}")
    private String passwordSlave;
    @Value("${spring.datasource.slave.showSql:#{null}}")
    private Boolean showSqlSlave;
    @Value("${spring.datasource.slave.driver-class-name:#{null}}")
    private String driverClassNameSlave;

    /** */
    @Value("${spring.datasource.initialSize:#{null}}")
    private Integer initialSize;
    @Value("${spring.datasource.minIdle:#{null}}")
    private Integer minIdle;
    @Value("${spring.datasource.maxActive:#{null}}")
    private Integer maxActive;
    @Value("${spring.datasource.maxWait:#{null}}")
    private Integer maxWait;
    @Value("${spring.datasource.timeBetweenEvictionRunsMillis:#{null}}")
    private Integer timeBetweenEvictionRunsMillis;
    @Value("${spring.datasource.minEvictableIdleTimeMillis:#{null}}")
    private Integer minEvictableIdleTimeMillis;
    @Value("${spring.datasource.validationQuery:#{null}}")
    private String validationQuery;
    @Value("${spring.datasource.testWhileIdle:#{null}}")
    private Boolean testWhileIdle;
    @Value("${spring.datasource.testOnBorrow:#{null}}")
    private Boolean testOnBorrow;
    @Value("${spring.datasource.testOnReturn:#{null}}")
    private Boolean testOnReturn;
    @Value("${spring.datasource.poolPreparedStatements:#{null}}")
    private Boolean poolPreparedStatements;
    @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize:#{null}}")
    private Integer maxPoolPreparedStatementPerConnectionSize;
    @Value("${spring.datasource.filters:#{null}}")
    private String filters;
    @Value("{spring.datasource.connectionProperties:#{null}}")
    private String connectionProperties;
    @Value("{spring.datasource.loginPassword:#{null}}")
    private String loginPassword;
    @Value("{spring.datasource.loginUsername:#{null}}")
    private String loginUsername;


    @Bean("baseDaoEnum")
    public MyDatabaseEnum dbName(){
        return MyDatabaseEnum.valueOf(alias);
    }

    @Bean("baseDaoEnum")
    public MyDatabaseEnum dbSlaveName(){
        return MyDatabaseEnum.valueOf(aliasSlave);
    }

    @Bean(name="dataSource")
    @Primary
    public DataSource dataSource(){

        System.out.println("---------------------MasterDB----Start------------------");
        System.out.println("dbUrl:"+dbUrl);
        System.out.println("username:"+username);
        System.out.println("driverClassName:"+driverClassName);
        System.out.println("alias:"+alias);
        System.out.println("ip:"+ip);
        System.out.println("---------------------MasterDB----End------------------");
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(this.dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        //configuration
        if(initialSize != null) {
            datasource.setInitialSize(initialSize);
        }
        if(minIdle != null) {
            datasource.setMinIdle(minIdle);
        }
        if(maxActive != null) {
            datasource.setMaxActive(maxActive);
        }
        if(maxWait != null) {
            datasource.setMaxWait(maxWait);
        }
        if(timeBetweenEvictionRunsMillis != null) {
            datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        }
        if(minEvictableIdleTimeMillis != null) {
            datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        }
        if(validationQuery!=null) {
            datasource.setValidationQuery(validationQuery);
        }
        if(testWhileIdle != null) {
            datasource.setTestWhileIdle(testWhileIdle);
        }
        if(testOnBorrow != null) {
            datasource.setTestOnBorrow(testOnBorrow);
        }
        if(testOnReturn != null) {
            datasource.setTestOnReturn(testOnReturn);
        }
        if(poolPreparedStatements != null) {
            datasource.setPoolPreparedStatements(poolPreparedStatements);
        }
        if(maxPoolPreparedStatementPerConnectionSize != null) {
            datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        }

        if(connectionProperties != null) {
            datasource.setConnectionProperties(connectionProperties);
        }

        List<Filter> filters = new ArrayList<>();
        filters.add(statFilter());
        filters.add(wallFilter());
        datasource.setProxyFilters(filters);


//        DataSourceInfo dbInfo = new DataSourceInfo();
//        dbInfo.setAliaName(alias);
//        dbInfo.setIp(ip);
//        dbInfo.setName(databaseName);
//        DB.me().addDataSource(alias, dbInfo, datasource);
//        DB.setShowSql(showSql);
//        Sql sql = new Sql(validationQuery);
//        try {
//            int i = DB.me().queryForInteger(MyDatabaseEnum.valueOf(alias), sql);
//            System.out.println(i);
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        return datasource;
    }

//    @Bean(name="dataSourceSlave")
//    public DataSource dataSourceSlave(){
//
//        System.out.println("---------------------SlaveDB----Start------------------");
//        System.out.println("dbUrl:"+dbUrlSlave);
//        System.out.println("username:"+usernameSlave);
//        System.out.println("driverClassName:"+driverClassNameSlave);
//        System.out.println("alias:"+aliasSlave);
//        System.out.println("ip:"+ipSlave);
//        System.out.println("---------------------SlaveDB----End------------------");
//        DruidDataSource datasource = new DruidDataSource();
//        datasource.setUrl(this.dbUrlSlave);
//        datasource.setUsername(usernameSlave);
//        datasource.setPassword(passwordSlave);
//        datasource.setDriverClassName(driverClassNameSlave);
//        //configuration
//        if(initialSize != null) {
//            datasource.setInitialSize(initialSize);
//        }
//        if(minIdle != null) {
//            datasource.setMinIdle(minIdle);
//        }
//        if(maxActive != null) {
//            datasource.setMaxActive(maxActive);
//        }
//        if(maxWait != null) {
//            datasource.setMaxWait(maxWait);
//        }
//        if(timeBetweenEvictionRunsMillis != null) {
//            datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
//        }
//        if(minEvictableIdleTimeMillis != null) {
//            datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
//        }
//        if(validationQuery!=null) {
//            datasource.setValidationQuery(validationQuery);
//        }
//        if(testWhileIdle != null) {
//            datasource.setTestWhileIdle(testWhileIdle);
//        }
//        if(testOnBorrow != null) {
//            datasource.setTestOnBorrow(testOnBorrow);
//        }
//        if(testOnReturn != null) {
//            datasource.setTestOnReturn(testOnReturn);
//        }
//        if(poolPreparedStatements != null) {
//            datasource.setPoolPreparedStatements(poolPreparedStatements);
//        }
//        if(maxPoolPreparedStatementPerConnectionSize != null) {
//            datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
//        }
//
//        if(connectionProperties != null) {
//            datasource.setConnectionProperties(connectionProperties);
//        }
//
//        List<Filter> filters = new ArrayList<>();
//        filters.add(statFilter());
//        filters.add(wallFilter());
//        datasource.setProxyFilters(filters);
//
//        DataSourceInfo dbInfo = new DataSourceInfo();
//        dbInfo.setAliaName(aliasSlave);
//        dbInfo.setIp(ipSlave);
//        dbInfo.setName(databaseNameSlave);
//        DB.me().addDataSource(aliasSlave, dbInfo, datasource);
//        DB.setShowSql(showSqlSlave);
//        Sql sql = new Sql(validationQuery);
//        try {
//            int i = DB.me().queryForInteger(MyDatabaseEnum.valueOf(alias), sql);
//            System.out.println(i);
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return datasource;
//    }

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        StatViewServlet startViewServlet = new StatViewServlet();
        servletRegistrationBean.setServlet(startViewServlet);
        servletRegistrationBean.addUrlMappings("/druid/*");
//        //白名单
//        servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
//        //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
//        servletRegistrationBean.addInitParameter("deny", "192.168.1.73");
        //登录查看信息的账号密码.
        servletRegistrationBean.addInitParameter("loginUsername", loginUsername);
        servletRegistrationBean.addInitParameter("loginPassword", loginPassword);
        //是否能够重置数据.
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }

    @Bean
    public StatFilter statFilter(){
        StatFilter statFilter = new StatFilter();
        statFilter.setLogSlowSql(true);
        statFilter.setMergeSql(true);
        statFilter.setSlowSqlMillis(3000);
        return statFilter;
    }

    @Bean
    public WallFilter wallFilter(){
        WallFilter wallFilter = new WallFilter();
        //允许执行多条SQL
        WallConfig config = new WallConfig();
        config.setMultiStatementAllow(true);
        wallFilter.setLogViolation(true);
        wallFilter.setDbType("mysql");
        config.setDir("META-INF/druid/wall/mysql");
        wallFilter.setConfig(config);
        return wallFilter;
    }
    @Bean
    public Slf4jLogFilter slf4jLogFilter(){
        Slf4jLogFilter slf4jLogFilter = new Slf4jLogFilter();
        slf4jLogFilter.setResultSetLogEnabled(false);
        slf4jLogFilter.setStatementExecutableSqlLogEnable(true);
        return slf4jLogFilter;
    }
    @Bean
    public FilterRegistrationBean webStartFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean(new WebStatFilter());
        //添加过滤规则.
        bean.addUrlPatterns("/*");
        //添加不需要忽略的格式信息.
        bean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid2/*");
        return bean;
    }
//    /**
//     * 获取是否保存数据库日志配置项
//     * @return
//     */
//    public Boolean isSaveSqlLog(){
//    	return saveSqlLog;
//    }
}

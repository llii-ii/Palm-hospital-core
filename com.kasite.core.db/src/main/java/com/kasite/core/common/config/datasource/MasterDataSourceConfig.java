package com.kasite.core.common.config.datasource;

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
import com.kasite.core.common.bean.KasiteDataSourceConfig;
import com.kasite.core.common.config.DBType;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.validator.Assert;

/**
 * 
 * @className: MasterDataSourceConfig
 * @author: lcz
 * @date: 2018年7月18日 上午10:23:21
 */
@Configuration
public class MasterDataSourceConfig {
	
    @Value("${spring.datasource.alias:#{null}}")
    private String alias;
    @Value("${spring.datasource.ip:#{null}}")
    private String ip;
    @Value("${spring.datasource.databaseName:#{null}}")
    private String databaseName;
    @Value("${spring.datasource.url:#{null}}")
    private String dbUrl;
    @Value("${spring.datasource.username: #{null}}")
    private String username;
    @Value("${spring.datasource.password:#{null}}")
    private String password;
    @Value("${spring.datasource.driverClassName:#{null}}")
    private String driverClassName;
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
    @Value("${spring.datasource.showSql:#{null}}")
    private Boolean showSql;
    @Value("${spring.datasource.dbType:#{null}}")
    private String dbType;
    @Value("${kasite.appConfig.appSecret:#{null}}")
    private String appSecret;

    public String getAlias() {
		return alias;
	}


	public String getIp() {
		return ip;
	}


	public String getDatabaseName() {
		return databaseName;
	}


	public String getDbUrl() {
		return dbUrl;
	}


	public String getUsername() {
		return username;
	}


	public String getPassword() {
		return password;
	}


	public String getDriverClassName() {
		return driverClassName;
	}


	public Integer getInitialSize() {
		return initialSize;
	}


	public Integer getMinIdle() {
		return minIdle;
	}


	public Integer getMaxActive() {
		return maxActive;
	}


	public Integer getMaxWait() {
		return maxWait;
	}


	public Integer getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}


	public Integer getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}


	public String getValidationQuery() {
		return validationQuery;
	}


	public Boolean getTestWhileIdle() {
		return testWhileIdle;
	}


	public Boolean getTestOnBorrow() {
		return testOnBorrow;
	}


	public Boolean getTestOnReturn() {
		return testOnReturn;
	}


	public Boolean getPoolPreparedStatements() {
		return poolPreparedStatements;
	}


	public Integer getMaxPoolPreparedStatementPerConnectionSize() {
		return maxPoolPreparedStatementPerConnectionSize;
	}


	public String getFilters() {
		return filters;
	}


	public String getConnectionProperties() {
		return connectionProperties;
	}


	public Boolean getShowSql() {
		return showSql;
	}


	public String getAppSecret() {
		return appSecret;
	}


	/**
     * 获取数据库类型： mysql／oracle／sqlserver
     * @return
     */
    public DBType getDbType() {
		return DBType.valueOf(dbType);
	}
    
    
    @Bean(name = "masterDataSource")
    @Primary
    public DataSource masterDataSource(KasiteConfig kasiteConfig) {
    	KasiteDataSourceConfig config = KasiteConfig.getKasiteDataSourceConfigs();
 		if(null == appSecret && null != config) {
			appSecret = config.getAppSecret();
		}
		if (databaseName == null && null != config) {
			databaseName = config.getDatabaseName();
		}
		if (driverClassName == null && null != config) {
			driverClassName = config.getDriverClassName();
		}
		if (dbType == null && null != config) {
			dbType = config.getDbType();
		}
		if (dbUrl == null && null != config) {
			dbUrl = config.getUrl();
		}
		if ((StringUtil.isBlank(username) || username.trim().equals("")) && null != config) {
			username = config.getUsername();
		}
		if (password == null && null != config) {
			password = config.getPassword();
		}
		if (initialSize == null && null != config) {
			initialSize = config.getInitialSize();
		}
		if (minIdle == null && null != config) {
			minIdle = config.getMinIdle();
		}
		if (maxActive == null && null != config) {
			maxActive = config.getMaxActive();
		}
		if (maxWait == null && null != config) {
			maxWait = config.getMaxWait();
		}
		if (timeBetweenEvictionRunsMillis == null && null != config) {
			timeBetweenEvictionRunsMillis = config.getTimeBetweenEvictionRunsMillis();
		}
		if (minEvictableIdleTimeMillis == null && null != config) {
			minEvictableIdleTimeMillis = config.getMinEvictableIdleTimeMillis();
		}
		if (validationQuery == null && null != config) {
			validationQuery = config.getValidationQuery();
		}
		if (testWhileIdle == null && null != config) {
			testWhileIdle = config.getTestWhileIdle();
		}
		if (testOnBorrow == null && null != config) {
			testOnBorrow = config.getTestOnBorrow();
		}
		if (testOnReturn == null && null != config) {
			testOnReturn = config.getTestOnReturn();
		}
		if (poolPreparedStatements == null && null != config) {
			poolPreparedStatements = config.getPoolPreparedStatements();
		}
		if (maxPoolPreparedStatementPerConnectionSize == null && null != config) {
			maxPoolPreparedStatementPerConnectionSize = config.getMaxPoolPreparedStatementPerConnectionSize();
		}
		if (connectionProperties == null && null != config) {
			connectionProperties = config.getConnectionProperties();
		}
		Assert.isBlank(username, " 数据库：用户名 不能为空，请修改配置。");
		Assert.isBlank(password, " 数据库：密码 不能为空，请修改配置。");
		Assert.isBlank(dbUrl, " 数据库：url链接字符串 不能为空，请修改配置。");
    	KasiteConfig.setDataBaseName(this.databaseName);
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(this.driverClassName);
        dataSource.setUrl(this.dbUrl);
        dataSource.setUsername(this.username);
        dataSource.setPassword(this.password);
        if(initialSize != null) {
        	dataSource.setInitialSize(initialSize);
        }
        if(minIdle != null) {
            dataSource.setMinIdle(minIdle);
        }
        if(maxActive != null) {
            dataSource.setMaxActive(maxActive);
        }
        if(maxWait != null) {
            dataSource.setMaxWait(maxWait);
        }
        if(timeBetweenEvictionRunsMillis != null) {
            dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        }
        if(minEvictableIdleTimeMillis != null) {
            dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        }
        if(validationQuery!=null) {
            dataSource.setValidationQuery(validationQuery);
        }
        if(testWhileIdle != null) {
            dataSource.setTestWhileIdle(testWhileIdle);
        }
        if(testOnBorrow != null) {
            dataSource.setTestOnBorrow(testOnBorrow);
        }
        if(testOnReturn != null) {
            dataSource.setTestOnReturn(testOnReturn);
        }
        if(poolPreparedStatements != null) {
            dataSource.setPoolPreparedStatements(poolPreparedStatements);
        }
        if(maxPoolPreparedStatementPerConnectionSize != null) {
            dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        }

        if(connectionProperties != null) {
            dataSource.setConnectionProperties(connectionProperties);
        }

        List<Filter> filters = new ArrayList<>();
        filters.add(statFilter());
        filters.add(wallFilter());
        dataSource.setProxyFilters(filters);

        return dataSource;
    }
    
    @Bean
    @Primary
    public StatFilter statFilter(){
        StatFilter statFilter = new StatFilter();
        statFilter.setLogSlowSql(true);
        statFilter.setMergeSql(true);
        statFilter.setSlowSqlMillis(3000);
        return statFilter;
    }

    @Bean
    @Primary
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
    @Primary
    public ServletRegistrationBean<StatViewServlet> druidServlet() {
        ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean<StatViewServlet>();
        StatViewServlet startViewServlet = new StatViewServlet();
        servletRegistrationBean.setServlet(startViewServlet);
        servletRegistrationBean.addUrlMappings("/druid/*");
//        //白名单
//        servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
//        //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
//        servletRegistrationBean.addInitParameter("deny", "192.168.1.73");
        //登录查看信息的账号密码.
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", appSecret);
        //是否能够重置数据.
        servletRegistrationBean.addInitParameter("resetEnable", "true");
        return servletRegistrationBean;
    }
    
    @Bean
    @Primary
    public Slf4jLogFilter slf4jLogFilter(){
	    	Slf4jLogFilter slf4jLogFilter = new Slf4jLogFilter();
	    	slf4jLogFilter.setResultSetLogEnabled(true);
	    	slf4jLogFilter.setStatementExecutableSqlLogEnable(true);
	    	return slf4jLogFilter;
    }
    @Bean
    @Primary
    public FilterRegistrationBean<WebStatFilter> webStartFilter(){
    	  FilterRegistrationBean<WebStatFilter> bean = new FilterRegistrationBean<WebStatFilter>(new WebStatFilter());
    	  //添加过滤规则.
          bean.addUrlPatterns("/*");
          //添加不需要忽略的格式信息.
          bean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid2/*");
          return bean;
    }
}

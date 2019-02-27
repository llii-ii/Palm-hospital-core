package com.kasite.core.common.config.datasource;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import tk.mybatis.spring.annotation.MapperScan;

/**
 * 
 * @className: MybatisConfig
 * @author: lcz
 * @date: 2018年7月18日 上午11:47:59
 */
@Configuration
@EnableTransactionManagement
//配置参考：https://github.com/abel533/Mapper/wiki/3.config
@MapperScan(value=MybatisConfig.PACKAGE,
	sqlSessionFactoryRef = "masterSqlSessionFactory",
	properties= {
		//notEmpty	insertSelective 和 updateByPrimaryKeySelective 中，是否判断字符串类型 !=''。
		"notEmpty=true",
		//IDENTITY 取回主键的方式，MYSQL: SELECT LAST_INSERT_ID()
		"IDENTITY=MYSQL",
		 //style 实体和表转换时的默认规则 	normal：原值，
		"style=normal",
		//safeDelete 配置为 true 后，delete 和 deleteByExample 都必须设置查询条件才能删除，否则会抛出异常。
		"safeDelete=true",
		//safeUpdate 配置为 true 后，updateByExample 和 updateByExampleSelective 都必须设置查询条件才能删除，否则会抛出异常
		"safeUpdate=true"}) 
public class MybatisConfig {
	//每个表对应的  接口 Mapper目录
	protected static final String PACKAGE = "com.kasite.**.dao";
	protected static final String MAPPERLOCATION = "classpath:com/kasite/**/dao/mapper/*.xml";
//	protected static final String CONFIGLOCATION = "mybatis-config.xml";
	
	@Autowired
	@Qualifier("masterDataSource")
	private DataSource masterDataSource;
	
	
    @Bean(name = "masterSqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(masterDataSource);
//        sqlSessionFactoryBean.setTypeAliasesPackage(PACKAGE);
//        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(CONFIGLOCATION));
        //mapper xml配置文件所在目录
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPERLOCATION));
        sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        return sqlSessionFactoryBean.getObject();
    }
    
    /**
     * 配置事务管理器
     */
    @Bean
    @Primary
    public DataSourceTransactionManager transactionManager() throws Exception {
        return new DataSourceTransactionManager(masterDataSource);
    }
}

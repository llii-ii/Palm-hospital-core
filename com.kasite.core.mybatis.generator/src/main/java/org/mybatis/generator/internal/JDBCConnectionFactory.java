package org.mybatis.generator.internal;

import org.mybatis.generator.api.*;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.util.*;
import java.util.*;
import org.mybatis.generator.internal.util.messages.*;
import java.sql.*;

public class JDBCConnectionFactory implements ConnectionFactory
{
    private String userId;
    private String password;
    private String connectionURL;
    private String driverClass;
    private Properties otherProperties;
    
    public JDBCConnectionFactory(final JDBCConnectionConfiguration config) {
        this.userId = config.getUserId();
        this.password = config.getPassword();
        this.connectionURL = config.getConnectionURL();
        this.driverClass = config.getDriverClass();
        this.otherProperties = config.getProperties();
    }
    
    public JDBCConnectionFactory() {
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        final Properties props = new Properties();
        if (StringUtility.stringHasValue(this.userId)) {
            props.setProperty("user", this.userId);
        }
        if (StringUtility.stringHasValue(this.password)) {
            props.setProperty("password", this.password);
        }
        props.putAll(this.otherProperties);
        final Driver driver = this.getDriver();
        final Connection conn = driver.connect(this.connectionURL, props);
        if (conn == null) {
            throw new SQLException(Messages.getString("RuntimeError.7"));
        }
        return conn;
    }
    
    private Driver getDriver() {
        Driver driver;
        try {
            final Class<?> clazz = ObjectFactory.externalClassForName(this.driverClass);
            driver = (Driver)clazz.newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException(Messages.getString("RuntimeError.8"), e);
        }
        return driver;
    }
    
    @Override
    public void addConfigurationProperties(final Properties properties) {
        this.userId = properties.getProperty("userId");
        this.password = properties.getProperty("password");
        this.connectionURL = properties.getProperty("connectionURL");
        this.driverClass = properties.getProperty("driverClass");
        (this.otherProperties = new Properties()).putAll(properties);
        this.otherProperties.remove("userId");
        this.otherProperties.remove("password");
        this.otherProperties.remove("connectionURL");
        this.otherProperties.remove("driverClass");
    }
}

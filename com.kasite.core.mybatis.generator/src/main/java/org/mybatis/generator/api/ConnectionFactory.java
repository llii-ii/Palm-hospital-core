package org.mybatis.generator.api;

import java.sql.*;
import java.util.*;

public interface ConnectionFactory
{
    Connection getConnection() throws SQLException;
    
    void addConfigurationProperties(final Properties p0);
}

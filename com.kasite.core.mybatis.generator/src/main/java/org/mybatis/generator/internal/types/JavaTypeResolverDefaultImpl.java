package org.mybatis.generator.internal.types;

import org.mybatis.generator.config.*;
import org.mybatis.generator.api.dom.java.*;
import java.util.*;
import java.math.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.*;

public class JavaTypeResolverDefaultImpl implements JavaTypeResolver
{
    protected List<String> warnings;
    protected Properties properties;
    protected Context context;
    protected boolean forceBigDecimals;
    protected boolean useJSR310Types;
    protected Map<Integer, JdbcTypeInformation> typeMap;
    private static final int TIME_WITH_TIMEZONE = 2013;
    private static final int TIMESTAMP_WITH_TIMEZONE = 2014;
    
    public JavaTypeResolverDefaultImpl() {
        this.properties = new Properties();
        (this.typeMap = new HashMap<Integer, JdbcTypeInformation>()).put(2003, new JdbcTypeInformation("ARRAY", new FullyQualifiedJavaType(Object.class.getName())));
        this.typeMap.put(-5, new JdbcTypeInformation("BIGINT", new FullyQualifiedJavaType(Long.class.getName())));
        this.typeMap.put(-2, new JdbcTypeInformation("BINARY", new FullyQualifiedJavaType("byte[]")));
        this.typeMap.put(-7, new JdbcTypeInformation("BIT", new FullyQualifiedJavaType(Boolean.class.getName())));
        this.typeMap.put(2004, new JdbcTypeInformation("BLOB", new FullyQualifiedJavaType("byte[]")));
        this.typeMap.put(16, new JdbcTypeInformation("BOOLEAN", new FullyQualifiedJavaType(Boolean.class.getName())));
        this.typeMap.put(1, new JdbcTypeInformation("CHAR", new FullyQualifiedJavaType(String.class.getName())));
        this.typeMap.put(2005, new JdbcTypeInformation("CLOB", new FullyQualifiedJavaType(String.class.getName())));
        this.typeMap.put(70, new JdbcTypeInformation("DATALINK", new FullyQualifiedJavaType(Object.class.getName())));
        this.typeMap.put(91, new JdbcTypeInformation("DATE", new FullyQualifiedJavaType(Date.class.getName())));
        this.typeMap.put(3, new JdbcTypeInformation("DECIMAL", new FullyQualifiedJavaType(BigDecimal.class.getName())));
        this.typeMap.put(2001, new JdbcTypeInformation("DISTINCT", new FullyQualifiedJavaType(Object.class.getName())));
        this.typeMap.put(8, new JdbcTypeInformation("DOUBLE", new FullyQualifiedJavaType(Double.class.getName())));
        this.typeMap.put(6, new JdbcTypeInformation("FLOAT", new FullyQualifiedJavaType(Double.class.getName())));
        this.typeMap.put(4, new JdbcTypeInformation("INTEGER", new FullyQualifiedJavaType(Integer.class.getName())));
        this.typeMap.put(2000, new JdbcTypeInformation("JAVA_OBJECT", new FullyQualifiedJavaType(Object.class.getName())));
        this.typeMap.put(-16, new JdbcTypeInformation("LONGNVARCHAR", new FullyQualifiedJavaType(String.class.getName())));
        this.typeMap.put(-4, new JdbcTypeInformation("LONGVARBINARY", new FullyQualifiedJavaType("byte[]")));
        this.typeMap.put(-1, new JdbcTypeInformation("LONGVARCHAR", new FullyQualifiedJavaType(String.class.getName())));
        this.typeMap.put(-15, new JdbcTypeInformation("NCHAR", new FullyQualifiedJavaType(String.class.getName())));
        this.typeMap.put(2011, new JdbcTypeInformation("NCLOB", new FullyQualifiedJavaType(String.class.getName())));
        this.typeMap.put(-9, new JdbcTypeInformation("NVARCHAR", new FullyQualifiedJavaType(String.class.getName())));
        this.typeMap.put(0, new JdbcTypeInformation("NULL", new FullyQualifiedJavaType(Object.class.getName())));
        this.typeMap.put(2, new JdbcTypeInformation("NUMERIC", new FullyQualifiedJavaType(BigDecimal.class.getName())));
        this.typeMap.put(1111, new JdbcTypeInformation("OTHER", new FullyQualifiedJavaType(Object.class.getName())));
        this.typeMap.put(7, new JdbcTypeInformation("REAL", new FullyQualifiedJavaType(Float.class.getName())));
        this.typeMap.put(2006, new JdbcTypeInformation("REF", new FullyQualifiedJavaType(Object.class.getName())));
        this.typeMap.put(5, new JdbcTypeInformation("SMALLINT", new FullyQualifiedJavaType(Short.class.getName())));
        this.typeMap.put(2002, new JdbcTypeInformation("STRUCT", new FullyQualifiedJavaType(Object.class.getName())));
        this.typeMap.put(92, new JdbcTypeInformation("TIME", new FullyQualifiedJavaType(Date.class.getName())));
        this.typeMap.put(93, new JdbcTypeInformation("TIMESTAMP", new FullyQualifiedJavaType(Date.class.getName())));
        this.typeMap.put(-6, new JdbcTypeInformation("TINYINT", new FullyQualifiedJavaType(Byte.class.getName())));
        this.typeMap.put(-3, new JdbcTypeInformation("VARBINARY", new FullyQualifiedJavaType("byte[]")));
        this.typeMap.put(12, new JdbcTypeInformation("VARCHAR", new FullyQualifiedJavaType(String.class.getName())));
        this.typeMap.put(2013, new JdbcTypeInformation("TIME_WITH_TIMEZONE", new FullyQualifiedJavaType("java.time.OffsetTime")));
        this.typeMap.put(2014, new JdbcTypeInformation("TIMESTAMP_WITH_TIMEZONE", new FullyQualifiedJavaType("java.time.OffsetDateTime")));
    }
    
    @Override
    public void addConfigurationProperties(final Properties properties) {
        this.properties.putAll(properties);
        this.forceBigDecimals = StringUtility.isTrue(properties.getProperty("forceBigDecimals"));
        this.useJSR310Types = StringUtility.isTrue(properties.getProperty("useJSR310Types"));
    }
    
    @Override
    public FullyQualifiedJavaType calculateJavaType(final IntrospectedColumn introspectedColumn) {
        FullyQualifiedJavaType answer = null;
        final JdbcTypeInformation jdbcTypeInformation = this.typeMap.get(introspectedColumn.getJdbcType());
        if (jdbcTypeInformation != null) {
            answer = jdbcTypeInformation.getFullyQualifiedJavaType();
            answer = this.overrideDefaultType(introspectedColumn, answer);
        }
        return answer;
    }
    
    protected FullyQualifiedJavaType overrideDefaultType(final IntrospectedColumn column, final FullyQualifiedJavaType defaultType) {
        FullyQualifiedJavaType answer = defaultType;
        switch (column.getJdbcType()) {
            case -7: {
                answer = this.calculateBitReplacement(column, defaultType);
                break;
            }
            case 91: {
                answer = this.calculateDateType(column, defaultType);
                break;
            }
            case 2:
            case 3: {
                answer = this.calculateBigDecimalReplacement(column, defaultType);
                break;
            }
            case 92: {
                answer = this.calculateTimeType(column, defaultType);
                break;
            }
            case 93: {
                answer = this.calculateTimestampType(column, defaultType);
                break;
            }
        }
        return answer;
    }
    
    protected FullyQualifiedJavaType calculateDateType(final IntrospectedColumn column, final FullyQualifiedJavaType defaultType) {
        FullyQualifiedJavaType answer;
        if (this.useJSR310Types) {
            answer = new FullyQualifiedJavaType("java.time.LocalDate");
        }
        else {
            answer = defaultType;
        }
        return answer;
    }
    
    protected FullyQualifiedJavaType calculateTimeType(final IntrospectedColumn column, final FullyQualifiedJavaType defaultType) {
        FullyQualifiedJavaType answer;
        if (this.useJSR310Types) {
            answer = new FullyQualifiedJavaType("java.time.LocalTime");
        }
        else {
            answer = defaultType;
        }
        return answer;
    }
    
    protected FullyQualifiedJavaType calculateTimestampType(final IntrospectedColumn column, final FullyQualifiedJavaType defaultType) {
        FullyQualifiedJavaType answer;
        if (this.useJSR310Types) {
            answer = new FullyQualifiedJavaType("java.time.LocalDateTime");
        }
        else {
            answer = defaultType;
        }
        return answer;
    }
    
    protected FullyQualifiedJavaType calculateBitReplacement(final IntrospectedColumn column, final FullyQualifiedJavaType defaultType) {
        FullyQualifiedJavaType answer;
        if (column.getLength() > 1) {
            answer = new FullyQualifiedJavaType("byte[]");
        }
        else {
            answer = defaultType;
        }
        return answer;
    }
    
    protected FullyQualifiedJavaType calculateBigDecimalReplacement(final IntrospectedColumn column, final FullyQualifiedJavaType defaultType) {
        FullyQualifiedJavaType answer;
        if (column.getScale() > 0 || column.getLength() > 18 || this.forceBigDecimals) {
            answer = defaultType;
        }
        else if (column.getLength() > 9) {
            answer = new FullyQualifiedJavaType(Long.class.getName());
        }
        else if (column.getLength() > 4) {
            answer = new FullyQualifiedJavaType(Integer.class.getName());
        }
        else {
            answer = new FullyQualifiedJavaType(Short.class.getName());
        }
        return answer;
    }
    
    @Override
    public String calculateJdbcTypeName(final IntrospectedColumn introspectedColumn) {
        String answer = null;
        final JdbcTypeInformation jdbcTypeInformation = this.typeMap.get(introspectedColumn.getJdbcType());
        if (jdbcTypeInformation != null) {
            answer = jdbcTypeInformation.getJdbcTypeName();
        }
        return answer;
    }
    
    @Override
    public void setWarnings(final List<String> warnings) {
        this.warnings = warnings;
    }
    
    @Override
    public void setContext(final Context context) {
        this.context = context;
    }
    
    public static class JdbcTypeInformation
    {
        private String jdbcTypeName;
        private FullyQualifiedJavaType fullyQualifiedJavaType;
        
        public JdbcTypeInformation(final String jdbcTypeName, final FullyQualifiedJavaType fullyQualifiedJavaType) {
            this.jdbcTypeName = jdbcTypeName;
            this.fullyQualifiedJavaType = fullyQualifiedJavaType;
        }
        
        public String getJdbcTypeName() {
            return this.jdbcTypeName;
        }
        
        public FullyQualifiedJavaType getFullyQualifiedJavaType() {
            return this.fullyQualifiedJavaType;
        }
    }
}

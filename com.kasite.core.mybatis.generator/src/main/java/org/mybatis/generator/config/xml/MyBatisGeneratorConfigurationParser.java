package org.mybatis.generator.config.xml;

import org.mybatis.generator.exception.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.internal.util.messages.*;
import org.mybatis.generator.internal.*;
import java.net.*;
import java.io.*;
import org.w3c.dom.*;
import org.mybatis.generator.config.*;
import java.util.*;

public class MyBatisGeneratorConfigurationParser
{
    private Properties extraProperties;
    private Properties configurationProperties;
    
    public MyBatisGeneratorConfigurationParser(final Properties extraProperties) {
        if (extraProperties == null) {
            this.extraProperties = new Properties();
        }
        else {
            this.extraProperties = extraProperties;
        }
        this.configurationProperties = new Properties();
    }
    
    public Configuration parseConfiguration(final Element rootNode) throws XMLParserException {
        final Configuration configuration = new Configuration();
        final NodeList nodeList = rootNode.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            final Node childNode = nodeList.item(i);
            if (childNode.getNodeType() == 1) {
                if ("properties".equals(childNode.getNodeName())) {
                    this.parseProperties(configuration, childNode);
                }
                else if ("classPathEntry".equals(childNode.getNodeName())) {
                    this.parseClassPathEntry(configuration, childNode);
                }
                else if ("context".equals(childNode.getNodeName())) {
                    this.parseContext(configuration, childNode);
                }
            }
        }
        return configuration;
    }
    
    protected void parseProperties(final Configuration configuration, final Node node) throws XMLParserException {
        final Properties attributes = this.parseAttributes(node);
        final String resource = attributes.getProperty("resource");
        final String url = attributes.getProperty("url");
        if (!StringUtility.stringHasValue(resource) && !StringUtility.stringHasValue(url)) {
            throw new XMLParserException(Messages.getString("RuntimeError.14"));
        }
        if (StringUtility.stringHasValue(resource) && StringUtility.stringHasValue(url)) {
            throw new XMLParserException(Messages.getString("RuntimeError.14"));
        }
        try {
            URL resourceUrl;
            if (StringUtility.stringHasValue(resource)) {
                resourceUrl = ObjectFactory.getResource(resource);
                if (resourceUrl == null) {
                    throw new XMLParserException(Messages.getString("RuntimeError.15", resource));
                }
            }
            else {
                resourceUrl = new URL(url);
            }
            final InputStream inputStream = resourceUrl.openConnection().getInputStream();
            this.configurationProperties.load(inputStream);
            inputStream.close();
        }
        catch (IOException e) {
            if (StringUtility.stringHasValue(resource)) {
                throw new XMLParserException(Messages.getString("RuntimeError.16", resource));
            }
            throw new XMLParserException(Messages.getString("RuntimeError.17", url));
        }
    }
    
    private void parseContext(final Configuration configuration, final Node node) {
        final Properties attributes = this.parseAttributes(node);
        final String defaultModelType = attributes.getProperty("defaultModelType");
        final String targetRuntime = attributes.getProperty("targetRuntime");
        final String introspectedColumnImpl = attributes.getProperty("introspectedColumnImpl");
        final String id = attributes.getProperty("id");
        final ModelType mt = (defaultModelType == null) ? null : ModelType.getModelType(defaultModelType);
        final Context context = new Context(mt);
        context.setId(id);
        if (StringUtility.stringHasValue(introspectedColumnImpl)) {
            context.setIntrospectedColumnImpl(introspectedColumnImpl);
        }
        if (StringUtility.stringHasValue(targetRuntime)) {
            context.setTargetRuntime(targetRuntime);
        }
        configuration.addContext(context);
        final NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            final Node childNode = nodeList.item(i);
            if (childNode.getNodeType() == 1) {
                if ("property".equals(childNode.getNodeName())) {
                    this.parseProperty(context, childNode);
                }
                else if ("plugin".equals(childNode.getNodeName())) {
                    this.parsePlugin(context, childNode);
                }
                else if ("commentGenerator".equals(childNode.getNodeName())) {
                    this.parseCommentGenerator(context, childNode);
                }
                else if ("jdbcConnection".equals(childNode.getNodeName())) {
                    this.parseJdbcConnection(context, childNode);
                }
                else if ("connectionFactory".equals(childNode.getNodeName())) {
                    this.parseConnectionFactory(context, childNode);
                }
                else if ("javaModelGenerator".equals(childNode.getNodeName())) {
                    this.parseJavaModelGenerator(context, childNode);
                }
                else if ("javaTypeResolver".equals(childNode.getNodeName())) {
                    this.parseJavaTypeResolver(context, childNode);
                }
                else if ("sqlMapGenerator".equals(childNode.getNodeName())) {
                    this.parseSqlMapGenerator(context, childNode);
                }
                else if ("javaClientGenerator".equals(childNode.getNodeName())) {
                    this.parseJavaClientGenerator(context, childNode);
                }
                else if ("table".equals(childNode.getNodeName())) {
                    this.parseTable(context, childNode);
                }
            }
        }
    }
    
    protected void parseSqlMapGenerator(final Context context, final Node node) {
        final SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
        context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);
        final Properties attributes = this.parseAttributes(node);
        final String targetPackage = attributes.getProperty("targetPackage");
        final String targetProject = attributes.getProperty("targetProject");
        sqlMapGeneratorConfiguration.setTargetPackage(targetPackage);
        sqlMapGeneratorConfiguration.setTargetProject(targetProject);
        final NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            final Node childNode = nodeList.item(i);
            if (childNode.getNodeType() == 1) {
                if ("property".equals(childNode.getNodeName())) {
                    this.parseProperty(sqlMapGeneratorConfiguration, childNode);
                }
            }
        }
    }
    
    protected void parseTable(final Context context, final Node node) {
        final TableConfiguration tc = new TableConfiguration(context);
        context.addTableConfiguration(tc);
        final Properties attributes = this.parseAttributes(node);
        final String catalog = attributes.getProperty("catalog");
        if (StringUtility.stringHasValue(catalog)) {
            tc.setCatalog(catalog);
        }
        final String schema = attributes.getProperty("schema");
        if (StringUtility.stringHasValue(schema)) {
            tc.setSchema(schema);
        }
        final String tableName = attributes.getProperty("tableName");
        if (StringUtility.stringHasValue(tableName)) {
            tc.setTableName(tableName);
        }
        final String domainObjectName = attributes.getProperty("domainObjectName");
        if (StringUtility.stringHasValue(domainObjectName)) {
            tc.setDomainObjectName(domainObjectName);
        }
        final String alias = attributes.getProperty("alias");
        if (StringUtility.stringHasValue(alias)) {
            tc.setAlias(alias);
        }
        final String enableInsert = attributes.getProperty("enableInsert");
        if (StringUtility.stringHasValue(enableInsert)) {
            tc.setInsertStatementEnabled(StringUtility.isTrue(enableInsert));
        }
        final String enableSelectByPrimaryKey = attributes.getProperty("enableSelectByPrimaryKey");
        if (StringUtility.stringHasValue(enableSelectByPrimaryKey)) {
            tc.setSelectByPrimaryKeyStatementEnabled(StringUtility.isTrue(enableSelectByPrimaryKey));
        }
        final String enableSelectByExample = attributes.getProperty("enableSelectByExample");
        if (StringUtility.stringHasValue(enableSelectByExample)) {
            tc.setSelectByExampleStatementEnabled(StringUtility.isTrue(enableSelectByExample));
        }
        final String enableUpdateByPrimaryKey = attributes.getProperty("enableUpdateByPrimaryKey");
        if (StringUtility.stringHasValue(enableUpdateByPrimaryKey)) {
            tc.setUpdateByPrimaryKeyStatementEnabled(StringUtility.isTrue(enableUpdateByPrimaryKey));
        }
        final String enableDeleteByPrimaryKey = attributes.getProperty("enableDeleteByPrimaryKey");
        if (StringUtility.stringHasValue(enableDeleteByPrimaryKey)) {
            tc.setDeleteByPrimaryKeyStatementEnabled(StringUtility.isTrue(enableDeleteByPrimaryKey));
        }
        final String enableDeleteByExample = attributes.getProperty("enableDeleteByExample");
        if (StringUtility.stringHasValue(enableDeleteByExample)) {
            tc.setDeleteByExampleStatementEnabled(StringUtility.isTrue(enableDeleteByExample));
        }
        final String enableCountByExample = attributes.getProperty("enableCountByExample");
        if (StringUtility.stringHasValue(enableCountByExample)) {
            tc.setCountByExampleStatementEnabled(StringUtility.isTrue(enableCountByExample));
        }
        final String enableUpdateByExample = attributes.getProperty("enableUpdateByExample");
        if (StringUtility.stringHasValue(enableUpdateByExample)) {
            tc.setUpdateByExampleStatementEnabled(StringUtility.isTrue(enableUpdateByExample));
        }
        final String selectByPrimaryKeyQueryId = attributes.getProperty("selectByPrimaryKeyQueryId");
        if (StringUtility.stringHasValue(selectByPrimaryKeyQueryId)) {
            tc.setSelectByPrimaryKeyQueryId(selectByPrimaryKeyQueryId);
        }
        final String selectByExampleQueryId = attributes.getProperty("selectByExampleQueryId");
        if (StringUtility.stringHasValue(selectByExampleQueryId)) {
            tc.setSelectByExampleQueryId(selectByExampleQueryId);
        }
        final String modelType = attributes.getProperty("modelType");
        if (StringUtility.stringHasValue(modelType)) {
            tc.setConfiguredModelType(modelType);
        }
        final String escapeWildcards = attributes.getProperty("escapeWildcards");
        if (StringUtility.stringHasValue(escapeWildcards)) {
            tc.setWildcardEscapingEnabled(StringUtility.isTrue(escapeWildcards));
        }
        final String delimitIdentifiers = attributes.getProperty("delimitIdentifiers");
        if (StringUtility.stringHasValue(delimitIdentifiers)) {
            tc.setDelimitIdentifiers(StringUtility.isTrue(delimitIdentifiers));
        }
        final String delimitAllColumns = attributes.getProperty("delimitAllColumns");
        if (StringUtility.stringHasValue(delimitAllColumns)) {
            tc.setAllColumnDelimitingEnabled(StringUtility.isTrue(delimitAllColumns));
        }
        final String mapperName = attributes.getProperty("mapperName");
        if (StringUtility.stringHasValue(mapperName)) {
            tc.setMapperName(mapperName);
        }
        final String sqlProviderName = attributes.getProperty("sqlProviderName");
        if (StringUtility.stringHasValue(sqlProviderName)) {
            tc.setSqlProviderName(sqlProviderName);
        }
        final NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            final Node childNode = nodeList.item(i);
            if (childNode.getNodeType() == 1) {
                if ("property".equals(childNode.getNodeName())) {
                    this.parseProperty(tc, childNode);
                }
                else if ("columnOverride".equals(childNode.getNodeName())) {
                    this.parseColumnOverride(tc, childNode);
                }
                else if ("ignoreColumn".equals(childNode.getNodeName())) {
                    this.parseIgnoreColumn(tc, childNode);
                }
                else if ("ignoreColumnsByRegex".equals(childNode.getNodeName())) {
                    this.parseIgnoreColumnByRegex(tc, childNode);
                }
                else if ("generatedKey".equals(childNode.getNodeName())) {
                    this.parseGeneratedKey(tc, childNode);
                }
                else if ("domainObjectRenamingRule".equals(childNode.getNodeName())) {
                    this.parseDomainObjectRenamingRule(tc, childNode);
                }
                else if ("columnRenamingRule".equals(childNode.getNodeName())) {
                    this.parseColumnRenamingRule(tc, childNode);
                }
            }
        }
    }
    
    private void parseColumnOverride(final TableConfiguration tc, final Node node) {
        final Properties attributes = this.parseAttributes(node);
        final String column = attributes.getProperty("column");
        final ColumnOverride co = new ColumnOverride(column);
        final String property = attributes.getProperty("property");
        if (StringUtility.stringHasValue(property)) {
            co.setJavaProperty(property);
        }
        final String javaType = attributes.getProperty("javaType");
        if (StringUtility.stringHasValue(javaType)) {
            co.setJavaType(javaType);
        }
        final String jdbcType = attributes.getProperty("jdbcType");
        if (StringUtility.stringHasValue(jdbcType)) {
            co.setJdbcType(jdbcType);
        }
        final String typeHandler = attributes.getProperty("typeHandler");
        if (StringUtility.stringHasValue(typeHandler)) {
            co.setTypeHandler(typeHandler);
        }
        final String delimitedColumnName = attributes.getProperty("delimitedColumnName");
        if (StringUtility.stringHasValue(delimitedColumnName)) {
            co.setColumnNameDelimited(StringUtility.isTrue(delimitedColumnName));
        }
        final String isGeneratedAlways = attributes.getProperty("isGeneratedAlways");
        if (StringUtility.stringHasValue(isGeneratedAlways)) {
            co.setGeneratedAlways(Boolean.parseBoolean(isGeneratedAlways));
        }
        final NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            final Node childNode = nodeList.item(i);
            if (childNode.getNodeType() == 1) {
                if ("property".equals(childNode.getNodeName())) {
                    this.parseProperty(co, childNode);
                }
            }
        }
        tc.addColumnOverride(co);
    }
    
    private void parseGeneratedKey(final TableConfiguration tc, final Node node) {
        final Properties attributes = this.parseAttributes(node);
        final String column = attributes.getProperty("column");
        final boolean identity = StringUtility.isTrue(attributes.getProperty("identity"));
        final String sqlStatement = attributes.getProperty("sqlStatement");
        final String type = attributes.getProperty("type");
        final GeneratedKey gk = new GeneratedKey(column, sqlStatement, identity, type);
        tc.setGeneratedKey(gk);
    }
    
    private void parseIgnoreColumn(final TableConfiguration tc, final Node node) {
        final Properties attributes = this.parseAttributes(node);
        final String column = attributes.getProperty("column");
        final String delimitedColumnName = attributes.getProperty("delimitedColumnName");
        final IgnoredColumn ic = new IgnoredColumn(column);
        if (StringUtility.stringHasValue(delimitedColumnName)) {
            ic.setColumnNameDelimited(StringUtility.isTrue(delimitedColumnName));
        }
        tc.addIgnoredColumn(ic);
    }
    
    private void parseIgnoreColumnByRegex(final TableConfiguration tc, final Node node) {
        final Properties attributes = this.parseAttributes(node);
        final String pattern = attributes.getProperty("pattern");
        final IgnoredColumnPattern icPattern = new IgnoredColumnPattern(pattern);
        final NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            final Node childNode = nodeList.item(i);
            if (childNode.getNodeType() == 1) {
                if ("except".equals(childNode.getNodeName())) {
                    this.parseException(icPattern, childNode);
                }
            }
        }
        tc.addIgnoredColumnPattern(icPattern);
    }
    
    private void parseException(final IgnoredColumnPattern icPattern, final Node node) {
        final Properties attributes = this.parseAttributes(node);
        final String column = attributes.getProperty("column");
        final String delimitedColumnName = attributes.getProperty("delimitedColumnName");
        final IgnoredColumnException exception = new IgnoredColumnException(column);
        if (StringUtility.stringHasValue(delimitedColumnName)) {
            exception.setColumnNameDelimited(StringUtility.isTrue(delimitedColumnName));
        }
        icPattern.addException(exception);
    }
    
    private void parseDomainObjectRenamingRule(final TableConfiguration tc, final Node node) {
        final Properties attributes = this.parseAttributes(node);
        final String searchString = attributes.getProperty("searchString");
        final String replaceString = attributes.getProperty("replaceString");
        final DomainObjectRenamingRule dorr = new DomainObjectRenamingRule();
        dorr.setSearchString(searchString);
        if (StringUtility.stringHasValue(replaceString)) {
            dorr.setReplaceString(replaceString);
        }
        tc.setDomainObjectRenamingRule(dorr);
    }
    
    private void parseColumnRenamingRule(final TableConfiguration tc, final Node node) {
        final Properties attributes = this.parseAttributes(node);
        final String searchString = attributes.getProperty("searchString");
        final String replaceString = attributes.getProperty("replaceString");
        final ColumnRenamingRule crr = new ColumnRenamingRule();
        crr.setSearchString(searchString);
        if (StringUtility.stringHasValue(replaceString)) {
            crr.setReplaceString(replaceString);
        }
        tc.setColumnRenamingRule(crr);
    }
    
    protected void parseJavaTypeResolver(final Context context, final Node node) {
        final JavaTypeResolverConfiguration javaTypeResolverConfiguration = new JavaTypeResolverConfiguration();
        context.setJavaTypeResolverConfiguration(javaTypeResolverConfiguration);
        final Properties attributes = this.parseAttributes(node);
        final String type = attributes.getProperty("type");
        if (StringUtility.stringHasValue(type)) {
            javaTypeResolverConfiguration.setConfigurationType(type);
        }
        final NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            final Node childNode = nodeList.item(i);
            if (childNode.getNodeType() == 1) {
                if ("property".equals(childNode.getNodeName())) {
                    this.parseProperty(javaTypeResolverConfiguration, childNode);
                }
            }
        }
    }
    
    private void parsePlugin(final Context context, final Node node) {
        final PluginConfiguration pluginConfiguration = new PluginConfiguration();
        context.addPluginConfiguration(pluginConfiguration);
        final Properties attributes = this.parseAttributes(node);
        final String type = attributes.getProperty("type");
        pluginConfiguration.setConfigurationType(type);
        final NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            final Node childNode = nodeList.item(i);
            if (childNode.getNodeType() == 1) {
                if ("property".equals(childNode.getNodeName())) {
                    this.parseProperty(pluginConfiguration, childNode);
                }
            }
        }
    }
    
    protected void parseJavaModelGenerator(final Context context, final Node node) {
        final JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
        context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);
        final Properties attributes = this.parseAttributes(node);
        final String targetPackage = attributes.getProperty("targetPackage");
        final String targetProject = attributes.getProperty("targetProject");
        javaModelGeneratorConfiguration.setTargetPackage(targetPackage);
        javaModelGeneratorConfiguration.setTargetProject(targetProject);
        final NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            final Node childNode = nodeList.item(i);
            if (childNode.getNodeType() == 1) {
                if ("property".equals(childNode.getNodeName())) {
                    this.parseProperty(javaModelGeneratorConfiguration, childNode);
                }
            }
        }
    }
    
    private void parseJavaClientGenerator(final Context context, final Node node) {
        final JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);
        final Properties attributes = this.parseAttributes(node);
        final String type = attributes.getProperty("type");
        final String targetPackage = attributes.getProperty("targetPackage");
        final String targetProject = attributes.getProperty("targetProject");
        final String implementationPackage = attributes.getProperty("implementationPackage");
        javaClientGeneratorConfiguration.setConfigurationType(type);
        javaClientGeneratorConfiguration.setTargetPackage(targetPackage);
        javaClientGeneratorConfiguration.setTargetProject(targetProject);
        javaClientGeneratorConfiguration.setImplementationPackage(implementationPackage);
        final NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            final Node childNode = nodeList.item(i);
            if (childNode.getNodeType() == 1) {
                if ("property".equals(childNode.getNodeName())) {
                    this.parseProperty(javaClientGeneratorConfiguration, childNode);
                }
            }
        }
    }
    
    protected void parseJdbcConnection(final Context context, final Node node) {
        final JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);
        final Properties attributes = this.parseAttributes(node);
        final String driverClass = attributes.getProperty("driverClass");
        final String connectionURL = attributes.getProperty("connectionURL");
        jdbcConnectionConfiguration.setDriverClass(driverClass);
        jdbcConnectionConfiguration.setConnectionURL(connectionURL);
        final String userId = attributes.getProperty("userId");
        if (StringUtility.stringHasValue(userId)) {
            jdbcConnectionConfiguration.setUserId(userId);
        }
        final String password = attributes.getProperty("password");
        if (StringUtility.stringHasValue(password)) {
            jdbcConnectionConfiguration.setPassword(password);
        }
        final NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            final Node childNode = nodeList.item(i);
            if (childNode.getNodeType() == 1) {
                if ("property".equals(childNode.getNodeName())) {
                    this.parseProperty(jdbcConnectionConfiguration, childNode);
                }
            }
        }
    }
    
    protected void parseClassPathEntry(final Configuration configuration, final Node node) {
        final Properties attributes = this.parseAttributes(node);
        configuration.addClasspathEntry(attributes.getProperty("location"));
    }
    
    protected void parseProperty(final PropertyHolder propertyHolder, final Node node) {
        final Properties attributes = this.parseAttributes(node);
        final String name = attributes.getProperty("name");
        final String value = attributes.getProperty("value");
        propertyHolder.addProperty(name, value);
    }
    
    protected Properties parseAttributes(final Node node) {
        final Properties attributes = new Properties();
        final NamedNodeMap nnm = node.getAttributes();
        for (int i = 0; i < nnm.getLength(); ++i) {
            final Node attribute = nnm.item(i);
            final String value = this.parsePropertyTokens(attribute.getNodeValue());
            attributes.put(attribute.getNodeName(), value);
        }
        return attributes;
    }
    
    private String parsePropertyTokens(final String string) {
        final String OPEN = "${";
        final String CLOSE = "}";
        String newString = string;
        if (newString != null) {
            for (int start = newString.indexOf("${"), end = newString.indexOf("}"); start > -1 && end > start; start = newString.indexOf("${", end), end = newString.indexOf("}", end)) {
                final String prepend = newString.substring(0, start);
                final String append = newString.substring(end + "}".length());
                final String propName = newString.substring(start + "${".length(), end);
                final String propValue = this.resolveProperty(propName);
                if (propValue != null) {
                    newString = prepend + propValue + append;
                }
            }
        }
        return newString;
    }
    
    protected void parseCommentGenerator(final Context context, final Node node) {
        final CommentGeneratorConfiguration commentGeneratorConfiguration = new CommentGeneratorConfiguration();
        context.setCommentGeneratorConfiguration(commentGeneratorConfiguration);
        final Properties attributes = this.parseAttributes(node);
        final String type = attributes.getProperty("type");
        if (StringUtility.stringHasValue(type)) {
            commentGeneratorConfiguration.setConfigurationType(type);
        }
        final NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            final Node childNode = nodeList.item(i);
            if (childNode.getNodeType() == 1) {
                if ("property".equals(childNode.getNodeName())) {
                    this.parseProperty(commentGeneratorConfiguration, childNode);
                }
            }
        }
    }
    
    protected void parseConnectionFactory(final Context context, final Node node) {
        final ConnectionFactoryConfiguration connectionFactoryConfiguration = new ConnectionFactoryConfiguration();
        context.setConnectionFactoryConfiguration(connectionFactoryConfiguration);
        final Properties attributes = this.parseAttributes(node);
        final String type = attributes.getProperty("type");
        if (StringUtility.stringHasValue(type)) {
            connectionFactoryConfiguration.setConfigurationType(type);
        }
        final NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            final Node childNode = nodeList.item(i);
            if (childNode.getNodeType() == 1) {
                if ("property".equals(childNode.getNodeName())) {
                    this.parseProperty(connectionFactoryConfiguration, childNode);
                }
            }
        }
    }
    
    private String resolveProperty(final String key) {
        String property = null;
        property = System.getProperty(key);
        if (property == null) {
            property = this.configurationProperties.getProperty(key);
        }
        if (property == null) {
            property = this.extraProperties.getProperty(key);
        }
        return property;
    }
}

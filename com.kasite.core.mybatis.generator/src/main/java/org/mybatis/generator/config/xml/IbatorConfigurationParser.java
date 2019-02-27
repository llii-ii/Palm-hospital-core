package org.mybatis.generator.config.xml;

import java.util.*;
import org.w3c.dom.*;
import org.mybatis.generator.exception.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.config.*;

public class IbatorConfigurationParser extends MyBatisGeneratorConfigurationParser
{
    public IbatorConfigurationParser(final Properties properties) {
        super(properties);
    }
    
    public Configuration parseIbatorConfiguration(final Element rootNode) throws XMLParserException {
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
                else if ("ibatorContext".equals(childNode.getNodeName())) {
                    this.parseIbatorContext(configuration, childNode);
                }
            }
        }
        return configuration;
    }
    
    private void parseIbatorContext(final Configuration configuration, final Node node) {
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
                else if ("ibatorPlugin".equals(childNode.getNodeName())) {
                    this.parseIbatorPlugin(context, childNode);
                }
                else if ("commentGenerator".equals(childNode.getNodeName())) {
                    this.parseCommentGenerator(context, childNode);
                }
                else if ("jdbcConnection".equals(childNode.getNodeName())) {
                    this.parseJdbcConnection(context, childNode);
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
                else if ("daoGenerator".equals(childNode.getNodeName())) {
                    this.parseDaoGenerator(context, childNode);
                }
                else if ("table".equals(childNode.getNodeName())) {
                    this.parseTable(context, childNode);
                }
            }
        }
    }
    
    private void parseIbatorPlugin(final Context context, final Node node) {
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
    
    private void parseDaoGenerator(final Context context, final Node node) {
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
}

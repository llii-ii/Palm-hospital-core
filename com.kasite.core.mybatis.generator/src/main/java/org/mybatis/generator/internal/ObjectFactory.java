package org.mybatis.generator.internal;

import org.mybatis.generator.internal.util.messages.*;
import java.net.*;
import org.mybatis.generator.internal.types.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.dom.*;
import org.mybatis.generator.config.*;
import org.mybatis.generator.codegen.ibatis2.*;
import org.mybatis.generator.codegen.mybatis3.*;
import org.mybatis.generator.runtime.dynamic.sql.*;
import org.mybatis.generator.api.*;
import java.util.*;

public class ObjectFactory
{
    private static List<ClassLoader> externalClassLoaders;
    
    public static void reset() {
        ObjectFactory.externalClassLoaders.clear();
    }
    
    public static synchronized void addExternalClassLoader(final ClassLoader classLoader) {
        ObjectFactory.externalClassLoaders.add(classLoader);
    }
    
    public static Class<?> externalClassForName(final String type) throws ClassNotFoundException {
        for (final ClassLoader classLoader : ObjectFactory.externalClassLoaders) {
            try {
                final Class<?> clazz = Class.forName(type, true, classLoader);
                return clazz;
            }
            catch (Throwable t) {
                continue;
            }
            //break;
        }
        return internalClassForName(type);
    }
    
    public static Object createExternalObject(final String type) {
        Object answer;
        try {
            final Class<?> clazz = externalClassForName(type);
            answer = clazz.newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException(Messages.getString("RuntimeError.6", type), e);
        }
        return answer;
    }
    
    public static Class<?> internalClassForName(final String type) throws ClassNotFoundException {
        Class<?> clazz = null;
        try {
            final ClassLoader cl = Thread.currentThread().getContextClassLoader();
            clazz = Class.forName(type, true, cl);
        }
        catch (Exception ex) {}
        if (clazz == null) {
            clazz = Class.forName(type, true, ObjectFactory.class.getClassLoader());
        }
        return clazz;
    }
    
    public static URL getResource(final String resource) {
        for (final ClassLoader classLoader : ObjectFactory.externalClassLoaders) {
            final URL url = classLoader.getResource(resource);
            if (url != null) {
                return url;
            }
        }
        final ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL url = cl.getResource(resource);
        if (url == null) {
            url = ObjectFactory.class.getClassLoader().getResource(resource);
        }
        return url;
    }
    
    public static Object createInternalObject(final String type) {
        Object answer;
        try {
            final Class<?> clazz = internalClassForName(type);
            answer = clazz.newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException(Messages.getString("RuntimeError.6", type), e);
        }
        return answer;
    }
    
    public static JavaTypeResolver createJavaTypeResolver(final Context context, final List<String> warnings) {
        final JavaTypeResolverConfiguration config = context.getJavaTypeResolverConfiguration();
        String type;
        if (config != null && config.getConfigurationType() != null) {
            type = config.getConfigurationType();
            if ("DEFAULT".equalsIgnoreCase(type)) {
                type = JavaTypeResolverDefaultImpl.class.getName();
            }
        }
        else {
            type = JavaTypeResolverDefaultImpl.class.getName();
        }
        final JavaTypeResolver answer = (JavaTypeResolver)createInternalObject(type);
        answer.setWarnings(warnings);
        if (config != null) {
            answer.addConfigurationProperties(config.getProperties());
        }
        answer.setContext(context);
        return answer;
    }
    
    public static Plugin createPlugin(final Context context, final PluginConfiguration pluginConfiguration) {
        final Plugin plugin = (Plugin)createInternalObject(pluginConfiguration.getConfigurationType());
        plugin.setContext(context);
        plugin.setProperties(pluginConfiguration.getProperties());
        return plugin;
    }
    
    public static CommentGenerator createCommentGenerator(final Context context) {
        final CommentGeneratorConfiguration config = context.getCommentGeneratorConfiguration();
        String type;
        if (config == null || config.getConfigurationType() == null) {
            type = DefaultCommentGenerator.class.getName();
        }
        else {
            type = config.getConfigurationType();
        }
        final CommentGenerator answer = (CommentGenerator)createInternalObject(type);
        if (config != null) {
            answer.addConfigurationProperties(config.getProperties());
        }
        return answer;
    }
    
    public static ConnectionFactory createConnectionFactory(final Context context) {
        final ConnectionFactoryConfiguration config = context.getConnectionFactoryConfiguration();
        String type;
        if (config == null || config.getConfigurationType() == null) {
            type = JDBCConnectionFactory.class.getName();
        }
        else {
            type = config.getConfigurationType();
        }
        final ConnectionFactory answer = (ConnectionFactory)createInternalObject(type);
        if (config != null) {
            answer.addConfigurationProperties(config.getProperties());
        }
        return answer;
    }
    
    public static JavaFormatter createJavaFormatter(final Context context) {
        String type = context.getProperty("javaFormatter");
        if (!StringUtility.stringHasValue(type)) {
            type = DefaultJavaFormatter.class.getName();
        }
        final JavaFormatter answer = (JavaFormatter)createInternalObject(type);
        answer.setContext(context);
        return answer;
    }
    
    public static XmlFormatter createXmlFormatter(final Context context) {
        String type = context.getProperty("xmlFormatter");
        if (!StringUtility.stringHasValue(type)) {
            type = DefaultXmlFormatter.class.getName();
        }
        final XmlFormatter answer = (XmlFormatter)createInternalObject(type);
        answer.setContext(context);
        return answer;
    }
    
    public static IntrospectedTable createIntrospectedTable(final TableConfiguration tableConfiguration, final FullyQualifiedTable table, final Context context) {
        final IntrospectedTable answer = createIntrospectedTableForValidation(context);
        answer.setFullyQualifiedTable(table);
        answer.setTableConfiguration(tableConfiguration);
        return answer;
    }
    
    public static IntrospectedTable createIntrospectedTableForValidation(final Context context) {
        String type = context.getTargetRuntime();
        if (!StringUtility.stringHasValue(type)) {
            type = IntrospectedTableMyBatis3Impl.class.getName();
        }
        else if ("Ibatis2Java2".equalsIgnoreCase(type)) {
            type = IntrospectedTableIbatis2Java2Impl.class.getName();
        }
        else if ("Ibatis2Java5".equalsIgnoreCase(type)) {
            type = IntrospectedTableIbatis2Java5Impl.class.getName();
        }
        else if ("Ibatis3".equalsIgnoreCase(type)) {
            type = IntrospectedTableMyBatis3Impl.class.getName();
        }
        else if ("MyBatis3".equalsIgnoreCase(type)) {
            type = IntrospectedTableMyBatis3Impl.class.getName();
        }
        else if ("MyBatis3Simple".equalsIgnoreCase(type)) {
            type = IntrospectedTableMyBatis3SimpleImpl.class.getName();
        }
        else if ("MyBatis3DynamicSql".equalsIgnoreCase(type)) {
            type = IntrospectedTableMyBatis3DynamicSqlImpl.class.getName();
        }
        final IntrospectedTable answer = (IntrospectedTable)createInternalObject(type);
        answer.setContext(context);
        return answer;
    }
    
    public static IntrospectedColumn createIntrospectedColumn(final Context context) {
        String type = context.getIntrospectedColumnImpl();
        if (!StringUtility.stringHasValue(type)) {
            type = IntrospectedColumn.class.getName();
        }
        final IntrospectedColumn answer = (IntrospectedColumn)createInternalObject(type);
        answer.setContext(context);
        return answer;
    }
    
    static {
        ObjectFactory.externalClassLoaders = new ArrayList<ClassLoader>();
    }
}

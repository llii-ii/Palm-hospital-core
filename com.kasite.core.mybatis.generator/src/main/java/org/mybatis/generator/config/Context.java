package org.mybatis.generator.config;

import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.internal.util.messages.*;
import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.internal.db.*;
import java.util.*;
import java.sql.*;
import org.mybatis.generator.internal.*;
import org.mybatis.generator.api.*;

public class Context extends PropertyHolder
{
    private String id;
    private JDBCConnectionConfiguration jdbcConnectionConfiguration;
    private ConnectionFactoryConfiguration connectionFactoryConfiguration;
    private SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration;
    private JavaTypeResolverConfiguration javaTypeResolverConfiguration;
    private JavaModelGeneratorConfiguration javaModelGeneratorConfiguration;
    private JavaClientGeneratorConfiguration javaClientGeneratorConfiguration;
    private ArrayList<TableConfiguration> tableConfigurations;
    private ModelType defaultModelType;
    private String beginningDelimiter;
    private String endingDelimiter;
    private CommentGeneratorConfiguration commentGeneratorConfiguration;
    private CommentGenerator commentGenerator;
    private PluginAggregator pluginAggregator;
    private List<PluginConfiguration> pluginConfigurations;
    private String targetRuntime;
    private String introspectedColumnImpl;
    private Boolean autoDelimitKeywords;
    private JavaFormatter javaFormatter;
    private XmlFormatter xmlFormatter;
    private List<IntrospectedTable> introspectedTables;
    
    public Context(final ModelType defaultModelType) {
        this.beginningDelimiter = "\"";
        this.endingDelimiter = "\"";
        if (defaultModelType == null) {
            this.defaultModelType = ModelType.CONDITIONAL;
        }
        else {
            this.defaultModelType = defaultModelType;
        }
        this.tableConfigurations = new ArrayList<TableConfiguration>();
        this.pluginConfigurations = new ArrayList<PluginConfiguration>();
    }
    
    public void addTableConfiguration(final TableConfiguration tc) {
        this.tableConfigurations.add(tc);
    }
    
    public JDBCConnectionConfiguration getJdbcConnectionConfiguration() {
        return this.jdbcConnectionConfiguration;
    }
    
    public JavaClientGeneratorConfiguration getJavaClientGeneratorConfiguration() {
        return this.javaClientGeneratorConfiguration;
    }
    
    public JavaModelGeneratorConfiguration getJavaModelGeneratorConfiguration() {
        return this.javaModelGeneratorConfiguration;
    }
    
    public JavaTypeResolverConfiguration getJavaTypeResolverConfiguration() {
        return this.javaTypeResolverConfiguration;
    }
    
    public SqlMapGeneratorConfiguration getSqlMapGeneratorConfiguration() {
        return this.sqlMapGeneratorConfiguration;
    }
    
    public void addPluginConfiguration(final PluginConfiguration pluginConfiguration) {
        this.pluginConfigurations.add(pluginConfiguration);
    }
    
    public void validate(final List<String> errors) {
        if (!StringUtility.stringHasValue(this.id)) {
            errors.add(Messages.getString("ValidationError.16"));
        }
        if (this.jdbcConnectionConfiguration == null && this.connectionFactoryConfiguration == null) {
            errors.add(Messages.getString("ValidationError.10", this.id));
        }
        else if (this.jdbcConnectionConfiguration != null && this.connectionFactoryConfiguration != null) {
            errors.add(Messages.getString("ValidationError.10", this.id));
        }
        else if (this.jdbcConnectionConfiguration != null) {
            this.jdbcConnectionConfiguration.validate(errors);
        }
        else {
            this.connectionFactoryConfiguration.validate(errors);
        }
        if (this.javaModelGeneratorConfiguration == null) {
            errors.add(Messages.getString("ValidationError.8", this.id));
        }
        else {
            this.javaModelGeneratorConfiguration.validate(errors, this.id);
        }
        if (this.javaClientGeneratorConfiguration != null) {
            this.javaClientGeneratorConfiguration.validate(errors, this.id);
        }
        IntrospectedTable it = null;
        try {
            it = ObjectFactory.createIntrospectedTableForValidation(this);
        }
        catch (Exception e) {
            errors.add(Messages.getString("ValidationError.25", this.id));
        }
        if (it != null && it.requiresXMLGenerator()) {
            if (this.sqlMapGeneratorConfiguration == null) {
                errors.add(Messages.getString("ValidationError.9", this.id));
            }
            else {
                this.sqlMapGeneratorConfiguration.validate(errors, this.id);
            }
        }
        if (this.tableConfigurations.size() == 0) {
            errors.add(Messages.getString("ValidationError.3", this.id));
        }
        else {
            for (int i = 0; i < this.tableConfigurations.size(); ++i) {
                final TableConfiguration tc = this.tableConfigurations.get(i);
                tc.validate(errors, i);
            }
        }
        for (final PluginConfiguration pluginConfiguration : this.pluginConfigurations) {
            pluginConfiguration.validate(errors, this.id);
        }
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setJavaClientGeneratorConfiguration(final JavaClientGeneratorConfiguration javaClientGeneratorConfiguration) {
        this.javaClientGeneratorConfiguration = javaClientGeneratorConfiguration;
    }
    
    public void setJavaModelGeneratorConfiguration(final JavaModelGeneratorConfiguration javaModelGeneratorConfiguration) {
        this.javaModelGeneratorConfiguration = javaModelGeneratorConfiguration;
    }
    
    public void setJavaTypeResolverConfiguration(final JavaTypeResolverConfiguration javaTypeResolverConfiguration) {
        this.javaTypeResolverConfiguration = javaTypeResolverConfiguration;
    }
    
    public void setJdbcConnectionConfiguration(final JDBCConnectionConfiguration jdbcConnectionConfiguration) {
        this.jdbcConnectionConfiguration = jdbcConnectionConfiguration;
    }
    
    public void setSqlMapGeneratorConfiguration(final SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration) {
        this.sqlMapGeneratorConfiguration = sqlMapGeneratorConfiguration;
    }
    
    public ModelType getDefaultModelType() {
        return this.defaultModelType;
    }
    
    public XmlElement toXmlElement() {
        final XmlElement xmlElement = new XmlElement("context");
        xmlElement.addAttribute(new Attribute("id", this.id));
        if (this.defaultModelType != ModelType.CONDITIONAL) {
            xmlElement.addAttribute(new Attribute("defaultModelType", this.defaultModelType.getModelType()));
        }
        if (StringUtility.stringHasValue(this.introspectedColumnImpl)) {
            xmlElement.addAttribute(new Attribute("introspectedColumnImpl", this.introspectedColumnImpl));
        }
        if (StringUtility.stringHasValue(this.targetRuntime)) {
            xmlElement.addAttribute(new Attribute("targetRuntime", this.targetRuntime));
        }
        this.addPropertyXmlElements(xmlElement);
        for (final PluginConfiguration pluginConfiguration : this.pluginConfigurations) {
            xmlElement.addElement(pluginConfiguration.toXmlElement());
        }
        if (this.commentGeneratorConfiguration != null) {
            xmlElement.addElement(this.commentGeneratorConfiguration.toXmlElement());
        }
        if (this.jdbcConnectionConfiguration != null) {
            xmlElement.addElement(this.jdbcConnectionConfiguration.toXmlElement());
        }
        if (this.connectionFactoryConfiguration != null) {
            xmlElement.addElement(this.connectionFactoryConfiguration.toXmlElement());
        }
        if (this.javaTypeResolverConfiguration != null) {
            xmlElement.addElement(this.javaTypeResolverConfiguration.toXmlElement());
        }
        if (this.javaModelGeneratorConfiguration != null) {
            xmlElement.addElement(this.javaModelGeneratorConfiguration.toXmlElement());
        }
        if (this.sqlMapGeneratorConfiguration != null) {
            xmlElement.addElement(this.sqlMapGeneratorConfiguration.toXmlElement());
        }
        if (this.javaClientGeneratorConfiguration != null) {
            xmlElement.addElement(this.javaClientGeneratorConfiguration.toXmlElement());
        }
        for (final TableConfiguration tableConfiguration : this.tableConfigurations) {
            xmlElement.addElement(tableConfiguration.toXmlElement());
        }
        return xmlElement;
    }
    
    public List<TableConfiguration> getTableConfigurations() {
        return this.tableConfigurations;
    }
    
    public String getBeginningDelimiter() {
        return this.beginningDelimiter;
    }
    
    public String getEndingDelimiter() {
        return this.endingDelimiter;
    }
    
    @Override
    public void addProperty(final String name, final String value) {
        super.addProperty(name, value);
        if ("beginningDelimiter".equals(name)) {
            this.beginningDelimiter = value;
        }
        else if ("endingDelimiter".equals(name)) {
            this.endingDelimiter = value;
        }
        else if ("autoDelimitKeywords".equals(name) && StringUtility.stringHasValue(value)) {
            this.autoDelimitKeywords = StringUtility.isTrue(value);
        }
    }
    
    public CommentGenerator getCommentGenerator() {
        if (this.commentGenerator == null) {
            this.commentGenerator = ObjectFactory.createCommentGenerator(this);
        }
        return this.commentGenerator;
    }
    
    public JavaFormatter getJavaFormatter() {
        if (this.javaFormatter == null) {
            this.javaFormatter = ObjectFactory.createJavaFormatter(this);
        }
        return this.javaFormatter;
    }
    
    public XmlFormatter getXmlFormatter() {
        if (this.xmlFormatter == null) {
            this.xmlFormatter = ObjectFactory.createXmlFormatter(this);
        }
        return this.xmlFormatter;
    }
    
    public CommentGeneratorConfiguration getCommentGeneratorConfiguration() {
        return this.commentGeneratorConfiguration;
    }
    
    public void setCommentGeneratorConfiguration(final CommentGeneratorConfiguration commentGeneratorConfiguration) {
        this.commentGeneratorConfiguration = commentGeneratorConfiguration;
    }
    
    public Plugin getPlugins() {
        return this.pluginAggregator;
    }
    
    public String getTargetRuntime() {
        return this.targetRuntime;
    }
    
    public void setTargetRuntime(final String targetRuntime) {
        this.targetRuntime = targetRuntime;
    }
    
    public String getIntrospectedColumnImpl() {
        return this.introspectedColumnImpl;
    }
    
    public void setIntrospectedColumnImpl(final String introspectedColumnImpl) {
        this.introspectedColumnImpl = introspectedColumnImpl;
    }
    
    public int getIntrospectionSteps() {
        int steps = 0;
        steps = ++steps + this.tableConfigurations.size() * 1;
        return steps;
    }
    
    public void introspectTables(final ProgressCallback callback, final List<String> warnings, final Set<String> fullyQualifiedTableNames) throws SQLException, InterruptedException {
        this.introspectedTables = new ArrayList<IntrospectedTable>();
        final JavaTypeResolver javaTypeResolver = ObjectFactory.createJavaTypeResolver(this, warnings);
        Connection connection = null;
        try {
            callback.startTask(Messages.getString("Progress.0"));
            connection = this.getConnection();
            final DatabaseIntrospector databaseIntrospector = new DatabaseIntrospector(this, connection.getMetaData(), javaTypeResolver, warnings);
            for (final TableConfiguration tc : this.tableConfigurations) {
                final String tableName = StringUtility.composeFullyQualifiedTableName(tc.getCatalog(), tc.getSchema(), tc.getTableName(), '.');
                if (fullyQualifiedTableNames != null && fullyQualifiedTableNames.size() > 0 && !fullyQualifiedTableNames.contains(tableName)) {
                    continue;
                }
                if (!tc.areAnyStatementsEnabled()) {
                    warnings.add(Messages.getString("Warning.0", tableName));
                }
                else {
                    callback.startTask(Messages.getString("Progress.1", tableName));
                    final List<IntrospectedTable> tables = databaseIntrospector.introspectTables(tc);
                    if (tables != null) {
                        this.introspectedTables.addAll(tables);
                    }
                    callback.checkCancel();
                }
            }
        }
        finally {
            this.closeConnection(connection);
        }
    }
    
    public int getGenerationSteps() {
        int steps = 0;
        if (this.introspectedTables != null) {
            for (final IntrospectedTable introspectedTable : this.introspectedTables) {
                steps += introspectedTable.getGenerationSteps();
            }
        }
        return steps;
    }
    
    public void generateFiles(final ProgressCallback callback, final List<GeneratedJavaFile> generatedJavaFiles, final List<GeneratedXmlFile> generatedXmlFiles, final List<String> warnings) throws InterruptedException {
        this.pluginAggregator = new PluginAggregator();
        for (final PluginConfiguration pluginConfiguration : this.pluginConfigurations) {
            final Plugin plugin = ObjectFactory.createPlugin(this, pluginConfiguration);
            if (plugin.validate(warnings)) {
                this.pluginAggregator.addPlugin(plugin);
            }
            else {
                warnings.add(Messages.getString("Warning.24", pluginConfiguration.getConfigurationType(), this.id));
            }
        }
        if (this.introspectedTables != null) {
            for (final IntrospectedTable introspectedTable : this.introspectedTables) {
                callback.checkCancel();
                introspectedTable.initialize();
                introspectedTable.calculateGenerators(warnings, callback);
                generatedJavaFiles.addAll(introspectedTable.getGeneratedJavaFiles());
                generatedXmlFiles.addAll(introspectedTable.getGeneratedXmlFiles());
                generatedJavaFiles.addAll(this.pluginAggregator.contextGenerateAdditionalJavaFiles(introspectedTable));
                generatedXmlFiles.addAll(this.pluginAggregator.contextGenerateAdditionalXmlFiles(introspectedTable));
            }
        }
        generatedJavaFiles.addAll(this.pluginAggregator.contextGenerateAdditionalJavaFiles());
        generatedXmlFiles.addAll(this.pluginAggregator.contextGenerateAdditionalXmlFiles());
    }
    
    private Connection getConnection() throws SQLException {
        ConnectionFactory connectionFactory;
        if (this.jdbcConnectionConfiguration != null) {
            connectionFactory = new JDBCConnectionFactory(this.jdbcConnectionConfiguration);
        }
        else {
            connectionFactory = ObjectFactory.createConnectionFactory(this);
        }
        return connectionFactory.getConnection();
    }
    
    private void closeConnection(final Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            }
            catch (SQLException ex) {}
        }
    }
    
    public boolean autoDelimitKeywords() {
        return this.autoDelimitKeywords != null && this.autoDelimitKeywords;
    }
    
    public ConnectionFactoryConfiguration getConnectionFactoryConfiguration() {
        return this.connectionFactoryConfiguration;
    }
    
    public void setConnectionFactoryConfiguration(final ConnectionFactoryConfiguration connectionFactoryConfiguration) {
        this.connectionFactoryConfiguration = connectionFactoryConfiguration;
    }
}

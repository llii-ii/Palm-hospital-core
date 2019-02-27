package org.mybatis.generator.config;

import java.util.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.internal.util.messages.*;

public class TableConfiguration extends PropertyHolder
{
    private boolean insertStatementEnabled;
    private boolean selectByPrimaryKeyStatementEnabled;
    private boolean selectByExampleStatementEnabled;
    private boolean updateByPrimaryKeyStatementEnabled;
    private boolean deleteByPrimaryKeyStatementEnabled;
    private boolean deleteByExampleStatementEnabled;
    private boolean countByExampleStatementEnabled;
    private boolean updateByExampleStatementEnabled;
    private List<ColumnOverride> columnOverrides;
    private Map<IgnoredColumn, Boolean> ignoredColumns;
    private GeneratedKey generatedKey;
    private String selectByPrimaryKeyQueryId;
    private String selectByExampleQueryId;
    private String catalog;
    private String schema;
    private String tableName;
    private String domainObjectName;
    private String alias;
    private ModelType modelType;
    private boolean wildcardEscapingEnabled;
    private String configuredModelType;
    private boolean delimitIdentifiers;
    private DomainObjectRenamingRule domainObjectRenamingRule;
    private ColumnRenamingRule columnRenamingRule;
    private boolean isAllColumnDelimitingEnabled;
    private String mapperName;
    private String sqlProviderName;
    private List<IgnoredColumnPattern> ignoredColumnPatterns;
    
    public TableConfiguration(final Context context) {
        this.ignoredColumnPatterns = new ArrayList<IgnoredColumnPattern>();
        this.modelType = context.getDefaultModelType();
        this.columnOverrides = new ArrayList<ColumnOverride>();
        this.ignoredColumns = new HashMap<IgnoredColumn, Boolean>();
        this.insertStatementEnabled = true;
        this.selectByPrimaryKeyStatementEnabled = true;
        this.selectByExampleStatementEnabled = true;
        this.updateByPrimaryKeyStatementEnabled = true;
        this.deleteByPrimaryKeyStatementEnabled = true;
        this.deleteByExampleStatementEnabled = true;
        this.countByExampleStatementEnabled = true;
        this.updateByExampleStatementEnabled = true;
    }
    
    public boolean isDeleteByPrimaryKeyStatementEnabled() {
        return this.deleteByPrimaryKeyStatementEnabled;
    }
    
    public void setDeleteByPrimaryKeyStatementEnabled(final boolean deleteByPrimaryKeyStatementEnabled) {
        this.deleteByPrimaryKeyStatementEnabled = deleteByPrimaryKeyStatementEnabled;
    }
    
    public boolean isInsertStatementEnabled() {
        return this.insertStatementEnabled;
    }
    
    public void setInsertStatementEnabled(final boolean insertStatementEnabled) {
        this.insertStatementEnabled = insertStatementEnabled;
    }
    
    public boolean isSelectByPrimaryKeyStatementEnabled() {
        return this.selectByPrimaryKeyStatementEnabled;
    }
    
    public void setSelectByPrimaryKeyStatementEnabled(final boolean selectByPrimaryKeyStatementEnabled) {
        this.selectByPrimaryKeyStatementEnabled = selectByPrimaryKeyStatementEnabled;
    }
    
    public boolean isUpdateByPrimaryKeyStatementEnabled() {
        return this.updateByPrimaryKeyStatementEnabled;
    }
    
    public void setUpdateByPrimaryKeyStatementEnabled(final boolean updateByPrimaryKeyStatementEnabled) {
        this.updateByPrimaryKeyStatementEnabled = updateByPrimaryKeyStatementEnabled;
    }
    
    public boolean isColumnIgnored(final String columnName) {
        for (final Map.Entry<IgnoredColumn, Boolean> entry : this.ignoredColumns.entrySet()) {
            if (entry.getKey().matches(columnName)) {
                entry.setValue(Boolean.TRUE);
                return true;
            }
        }
        for (final IgnoredColumnPattern ignoredColumnPattern : this.ignoredColumnPatterns) {
            if (ignoredColumnPattern.matches(columnName)) {
                return true;
            }
        }
        return false;
    }
    
    public void addIgnoredColumn(final IgnoredColumn ignoredColumn) {
        this.ignoredColumns.put(ignoredColumn, Boolean.FALSE);
    }
    
    public void addIgnoredColumnPattern(final IgnoredColumnPattern ignoredColumnPattern) {
        this.ignoredColumnPatterns.add(ignoredColumnPattern);
    }
    
    public void addColumnOverride(final ColumnOverride columnOverride) {
        this.columnOverrides.add(columnOverride);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TableConfiguration)) {
            return false;
        }
        final TableConfiguration other = (TableConfiguration)obj;
        return EqualsUtil.areEqual(this.catalog, other.catalog) && EqualsUtil.areEqual(this.schema, other.schema) && EqualsUtil.areEqual(this.tableName, other.tableName);
    }
    
    @Override
    public int hashCode() {
        int result = 23;
        result = HashCodeUtil.hash(result, this.catalog);
        result = HashCodeUtil.hash(result, this.schema);
        result = HashCodeUtil.hash(result, this.tableName);
        return result;
    }
    
    public boolean isSelectByExampleStatementEnabled() {
        return this.selectByExampleStatementEnabled;
    }
    
    public void setSelectByExampleStatementEnabled(final boolean selectByExampleStatementEnabled) {
        this.selectByExampleStatementEnabled = selectByExampleStatementEnabled;
    }
    
    public ColumnOverride getColumnOverride(final String columnName) {
        for (final ColumnOverride co : this.columnOverrides) {
            if (co.isColumnNameDelimited()) {
                if (columnName.equals(co.getColumnName())) {
                    return co;
                }
                continue;
            }
            else {
                if (columnName.equalsIgnoreCase(co.getColumnName())) {
                    return co;
                }
                continue;
            }
        }
        return null;
    }
    
    public GeneratedKey getGeneratedKey() {
        return this.generatedKey;
    }
    
    public String getSelectByExampleQueryId() {
        return this.selectByExampleQueryId;
    }
    
    public void setSelectByExampleQueryId(final String selectByExampleQueryId) {
        this.selectByExampleQueryId = selectByExampleQueryId;
    }
    
    public String getSelectByPrimaryKeyQueryId() {
        return this.selectByPrimaryKeyQueryId;
    }
    
    public void setSelectByPrimaryKeyQueryId(final String selectByPrimaryKeyQueryId) {
        this.selectByPrimaryKeyQueryId = selectByPrimaryKeyQueryId;
    }
    
    public boolean isDeleteByExampleStatementEnabled() {
        return this.deleteByExampleStatementEnabled;
    }
    
    public void setDeleteByExampleStatementEnabled(final boolean deleteByExampleStatementEnabled) {
        this.deleteByExampleStatementEnabled = deleteByExampleStatementEnabled;
    }
    
    public boolean areAnyStatementsEnabled() {
        return this.selectByExampleStatementEnabled || this.selectByPrimaryKeyStatementEnabled || this.insertStatementEnabled || this.updateByPrimaryKeyStatementEnabled || this.deleteByExampleStatementEnabled || this.deleteByPrimaryKeyStatementEnabled || this.countByExampleStatementEnabled || this.updateByExampleStatementEnabled;
    }
    
    public void setGeneratedKey(final GeneratedKey generatedKey) {
        this.generatedKey = generatedKey;
    }
    
    public String getAlias() {
        return this.alias;
    }
    
    public void setAlias(final String alias) {
        this.alias = alias;
    }
    
    public String getCatalog() {
        return this.catalog;
    }
    
    public void setCatalog(final String catalog) {
        this.catalog = catalog;
    }
    
    public String getDomainObjectName() {
        return this.domainObjectName;
    }
    
    public void setDomainObjectName(final String domainObjectName) {
        this.domainObjectName = domainObjectName;
    }
    
    public String getSchema() {
        return this.schema;
    }
    
    public void setSchema(final String schema) {
        this.schema = schema;
    }
    
    public String getTableName() {
        return this.tableName;
    }
    
    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }
    
    public List<ColumnOverride> getColumnOverrides() {
        return this.columnOverrides;
    }
    
    public List<String> getIgnoredColumnsInError() {
        final List<String> answer = new ArrayList<String>();
        for (final Map.Entry<IgnoredColumn, Boolean> entry : this.ignoredColumns.entrySet()) {
            if (Boolean.FALSE.equals(entry.getValue())) {
                answer.add(entry.getKey().getColumnName());
            }
        }
        return answer;
    }
    
    public ModelType getModelType() {
        return this.modelType;
    }
    
    public void setConfiguredModelType(final String configuredModelType) {
        this.configuredModelType = configuredModelType;
        this.modelType = ModelType.getModelType(configuredModelType);
    }
    
    public boolean isWildcardEscapingEnabled() {
        return this.wildcardEscapingEnabled;
    }
    
    public void setWildcardEscapingEnabled(final boolean wildcardEscapingEnabled) {
        this.wildcardEscapingEnabled = wildcardEscapingEnabled;
    }
    
    public XmlElement toXmlElement() {
        final XmlElement xmlElement = new XmlElement("table");
        xmlElement.addAttribute(new Attribute("tableName", this.tableName));
        if (StringUtility.stringHasValue(this.catalog)) {
            xmlElement.addAttribute(new Attribute("catalog", this.catalog));
        }
        if (StringUtility.stringHasValue(this.schema)) {
            xmlElement.addAttribute(new Attribute("schema", this.schema));
        }
        if (StringUtility.stringHasValue(this.alias)) {
            xmlElement.addAttribute(new Attribute("alias", this.alias));
        }
        if (StringUtility.stringHasValue(this.domainObjectName)) {
            xmlElement.addAttribute(new Attribute("domainObjectName", this.domainObjectName));
        }
        if (!this.insertStatementEnabled) {
            xmlElement.addAttribute(new Attribute("enableInsert", "false"));
        }
        if (!this.selectByPrimaryKeyStatementEnabled) {
            xmlElement.addAttribute(new Attribute("enableSelectByPrimaryKey", "false"));
        }
        if (!this.selectByExampleStatementEnabled) {
            xmlElement.addAttribute(new Attribute("enableSelectByExample", "false"));
        }
        if (!this.updateByPrimaryKeyStatementEnabled) {
            xmlElement.addAttribute(new Attribute("enableUpdateByPrimaryKey", "false"));
        }
        if (!this.deleteByPrimaryKeyStatementEnabled) {
            xmlElement.addAttribute(new Attribute("enableDeleteByPrimaryKey", "false"));
        }
        if (!this.deleteByExampleStatementEnabled) {
            xmlElement.addAttribute(new Attribute("enableDeleteByExample", "false"));
        }
        if (!this.countByExampleStatementEnabled) {
            xmlElement.addAttribute(new Attribute("enableCountByExample", "false"));
        }
        if (!this.updateByExampleStatementEnabled) {
            xmlElement.addAttribute(new Attribute("enableUpdateByExample", "false"));
        }
        if (StringUtility.stringHasValue(this.selectByPrimaryKeyQueryId)) {
            xmlElement.addAttribute(new Attribute("selectByPrimaryKeyQueryId", this.selectByPrimaryKeyQueryId));
        }
        if (StringUtility.stringHasValue(this.selectByExampleQueryId)) {
            xmlElement.addAttribute(new Attribute("selectByExampleQueryId", this.selectByExampleQueryId));
        }
        if (this.configuredModelType != null) {
            xmlElement.addAttribute(new Attribute("modelType", this.configuredModelType));
        }
        if (this.wildcardEscapingEnabled) {
            xmlElement.addAttribute(new Attribute("escapeWildcards", "true"));
        }
        if (this.isAllColumnDelimitingEnabled) {
            xmlElement.addAttribute(new Attribute("delimitAllColumns", "true"));
        }
        if (this.delimitIdentifiers) {
            xmlElement.addAttribute(new Attribute("delimitIdentifiers", "true"));
        }
        if (StringUtility.stringHasValue(this.mapperName)) {
            xmlElement.addAttribute(new Attribute("mapperName", this.mapperName));
        }
        if (StringUtility.stringHasValue(this.sqlProviderName)) {
            xmlElement.addAttribute(new Attribute("sqlProviderName", this.sqlProviderName));
        }
        this.addPropertyXmlElements(xmlElement);
        if (this.generatedKey != null) {
            xmlElement.addElement(this.generatedKey.toXmlElement());
        }
        if (this.domainObjectRenamingRule != null) {
            xmlElement.addElement(this.domainObjectRenamingRule.toXmlElement());
        }
        if (this.columnRenamingRule != null) {
            xmlElement.addElement(this.columnRenamingRule.toXmlElement());
        }
        if (this.ignoredColumns.size() > 0) {
            for (final IgnoredColumn ignoredColumn : this.ignoredColumns.keySet()) {
                xmlElement.addElement(ignoredColumn.toXmlElement());
            }
        }
        for (final IgnoredColumnPattern ignoredColumnPattern : this.ignoredColumnPatterns) {
            xmlElement.addElement(ignoredColumnPattern.toXmlElement());
        }
        if (this.columnOverrides.size() > 0) {
            for (final ColumnOverride columnOverride : this.columnOverrides) {
                xmlElement.addElement(columnOverride.toXmlElement());
            }
        }
        return xmlElement;
    }
    
    @Override
    public String toString() {
        return StringUtility.composeFullyQualifiedTableName(this.catalog, this.schema, this.tableName, '.');
    }
    
    public boolean isDelimitIdentifiers() {
        return this.delimitIdentifiers;
    }
    
    public void setDelimitIdentifiers(final boolean delimitIdentifiers) {
        this.delimitIdentifiers = delimitIdentifiers;
    }
    
    public boolean isCountByExampleStatementEnabled() {
        return this.countByExampleStatementEnabled;
    }
    
    public void setCountByExampleStatementEnabled(final boolean countByExampleStatementEnabled) {
        this.countByExampleStatementEnabled = countByExampleStatementEnabled;
    }
    
    public boolean isUpdateByExampleStatementEnabled() {
        return this.updateByExampleStatementEnabled;
    }
    
    public void setUpdateByExampleStatementEnabled(final boolean updateByExampleStatementEnabled) {
        this.updateByExampleStatementEnabled = updateByExampleStatementEnabled;
    }
    
    public void validate(final List<String> errors, final int listPosition) {
        if (!StringUtility.stringHasValue(this.tableName)) {
            errors.add(Messages.getString("ValidationError.6", Integer.toString(listPosition)));
        }
        final String fqTableName = StringUtility.composeFullyQualifiedTableName(this.catalog, this.schema, this.tableName, '.');
        if (this.generatedKey != null) {
            this.generatedKey.validate(errors, fqTableName);
        }
        if (StringUtility.isTrue(this.getProperty("useColumnIndexes")) && this.selectByExampleStatementEnabled && this.selectByPrimaryKeyStatementEnabled) {
            final boolean queryId1Set = StringUtility.stringHasValue(this.selectByExampleQueryId);
            final boolean queryId2Set = StringUtility.stringHasValue(this.selectByPrimaryKeyQueryId);
            if (queryId1Set != queryId2Set) {
                errors.add(Messages.getString("ValidationError.13", fqTableName));
            }
        }
        if (this.domainObjectRenamingRule != null) {
            this.domainObjectRenamingRule.validate(errors, fqTableName);
        }
        if (this.columnRenamingRule != null) {
            this.columnRenamingRule.validate(errors, fqTableName);
        }
        for (final ColumnOverride columnOverride : this.columnOverrides) {
            columnOverride.validate(errors, fqTableName);
        }
        for (final IgnoredColumn ignoredColumn : this.ignoredColumns.keySet()) {
            ignoredColumn.validate(errors, fqTableName);
        }
        for (final IgnoredColumnPattern ignoredColumnPattern : this.ignoredColumnPatterns) {
            ignoredColumnPattern.validate(errors, fqTableName);
        }
    }
    
    public DomainObjectRenamingRule getDomainObjectRenamingRule() {
        return this.domainObjectRenamingRule;
    }
    
    public void setDomainObjectRenamingRule(final DomainObjectRenamingRule domainObjectRenamingRule) {
        this.domainObjectRenamingRule = domainObjectRenamingRule;
    }
    
    public ColumnRenamingRule getColumnRenamingRule() {
        return this.columnRenamingRule;
    }
    
    public void setColumnRenamingRule(final ColumnRenamingRule columnRenamingRule) {
        this.columnRenamingRule = columnRenamingRule;
    }
    
    public boolean isAllColumnDelimitingEnabled() {
        return this.isAllColumnDelimitingEnabled;
    }
    
    public void setAllColumnDelimitingEnabled(final boolean isAllColumnDelimitingEnabled) {
        this.isAllColumnDelimitingEnabled = isAllColumnDelimitingEnabled;
    }
    
    public String getMapperName() {
        return this.mapperName;
    }
    
    public void setMapperName(final String mapperName) {
        this.mapperName = mapperName;
    }
    
    public String getSqlProviderName() {
        return this.sqlProviderName;
    }
    
    public void setSqlProviderName(final String sqlProviderName) {
        this.sqlProviderName = sqlProviderName;
    }
}

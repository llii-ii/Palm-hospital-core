package org.mybatis.generator.api;

import org.mybatis.generator.internal.rules.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.config.*;
import java.util.*;
import java.io.*;

public abstract class IntrospectedTable
{
    protected TableConfiguration tableConfiguration;
    protected FullyQualifiedTable fullyQualifiedTable;
    protected Context context;
    protected Rules rules;
    protected List<IntrospectedColumn> primaryKeyColumns;
    protected List<IntrospectedColumn> baseColumns;
    protected List<IntrospectedColumn> blobColumns;
    protected TargetRuntime targetRuntime;
    protected Map<String, Object> attributes;
    protected Map<InternalAttribute, String> internalAttributes;
    protected String remarks;
    protected String tableType;
    
    public IntrospectedTable(final TargetRuntime targetRuntime) {
        this.targetRuntime = targetRuntime;
        this.primaryKeyColumns = new ArrayList<IntrospectedColumn>();
        this.baseColumns = new ArrayList<IntrospectedColumn>();
        this.blobColumns = new ArrayList<IntrospectedColumn>();
        this.attributes = new HashMap<String, Object>();
        this.internalAttributes = new HashMap<InternalAttribute, String>();
    }
    
    public FullyQualifiedTable getFullyQualifiedTable() {
        return this.fullyQualifiedTable;
    }
    
    public String getSelectByExampleQueryId() {
        return this.tableConfiguration.getSelectByExampleQueryId();
    }
    
    public String getSelectByPrimaryKeyQueryId() {
        return this.tableConfiguration.getSelectByPrimaryKeyQueryId();
    }
    
    public GeneratedKey getGeneratedKey() {
        return this.tableConfiguration.getGeneratedKey();
    }
    
    public IntrospectedColumn getColumn(final String columnName) {
        if (columnName == null) {
            return null;
        }
        for (final IntrospectedColumn introspectedColumn : this.primaryKeyColumns) {
            if (introspectedColumn.isColumnNameDelimited()) {
                if (introspectedColumn.getActualColumnName().equals(columnName)) {
                    return introspectedColumn;
                }
                continue;
            }
            else {
                if (introspectedColumn.getActualColumnName().equalsIgnoreCase(columnName)) {
                    return introspectedColumn;
                }
                continue;
            }
        }
        for (final IntrospectedColumn introspectedColumn : this.baseColumns) {
            if (introspectedColumn.isColumnNameDelimited()) {
                if (introspectedColumn.getActualColumnName().equals(columnName)) {
                    return introspectedColumn;
                }
                continue;
            }
            else {
                if (introspectedColumn.getActualColumnName().equalsIgnoreCase(columnName)) {
                    return introspectedColumn;
                }
                continue;
            }
        }
        for (final IntrospectedColumn introspectedColumn : this.blobColumns) {
            if (introspectedColumn.isColumnNameDelimited()) {
                if (introspectedColumn.getActualColumnName().equals(columnName)) {
                    return introspectedColumn;
                }
                continue;
            }
            else {
                if (introspectedColumn.getActualColumnName().equalsIgnoreCase(columnName)) {
                    return introspectedColumn;
                }
                continue;
            }
        }
        return null;
    }
    
    public boolean hasJDBCDateColumns() {
        boolean rc = false;
        for (final IntrospectedColumn introspectedColumn : this.primaryKeyColumns) {
            if (introspectedColumn.isJDBCDateColumn()) {
                rc = true;
                break;
            }
        }
        if (!rc) {
            for (final IntrospectedColumn introspectedColumn : this.baseColumns) {
                if (introspectedColumn.isJDBCDateColumn()) {
                    rc = true;
                    break;
                }
            }
        }
        return rc;
    }
    
    public boolean hasJDBCTimeColumns() {
        boolean rc = false;
        for (final IntrospectedColumn introspectedColumn : this.primaryKeyColumns) {
            if (introspectedColumn.isJDBCTimeColumn()) {
                rc = true;
                break;
            }
        }
        if (!rc) {
            for (final IntrospectedColumn introspectedColumn : this.baseColumns) {
                if (introspectedColumn.isJDBCTimeColumn()) {
                    rc = true;
                    break;
                }
            }
        }
        return rc;
    }
    
    public List<IntrospectedColumn> getPrimaryKeyColumns() {
        return this.primaryKeyColumns;
    }
    
    public boolean hasPrimaryKeyColumns() {
        return this.primaryKeyColumns.size() > 0;
    }
    
    public List<IntrospectedColumn> getBaseColumns() {
        return this.baseColumns;
    }
    
    public List<IntrospectedColumn> getAllColumns() {
        final List<IntrospectedColumn> answer = new ArrayList<IntrospectedColumn>();
        answer.addAll(this.primaryKeyColumns);
        answer.addAll(this.baseColumns);
        answer.addAll(this.blobColumns);
        return answer;
    }
    
    public List<IntrospectedColumn> getNonBLOBColumns() {
        final List<IntrospectedColumn> answer = new ArrayList<IntrospectedColumn>();
        answer.addAll(this.primaryKeyColumns);
        answer.addAll(this.baseColumns);
        return answer;
    }
    
    public int getNonBLOBColumnCount() {
        return this.primaryKeyColumns.size() + this.baseColumns.size();
    }
    
    public List<IntrospectedColumn> getNonPrimaryKeyColumns() {
        final List<IntrospectedColumn> answer = new ArrayList<IntrospectedColumn>();
        answer.addAll(this.baseColumns);
        answer.addAll(this.blobColumns);
        return answer;
    }
    
    public List<IntrospectedColumn> getBLOBColumns() {
        return this.blobColumns;
    }
    
    public boolean hasBLOBColumns() {
        return this.blobColumns.size() > 0;
    }
    
    public boolean hasBaseColumns() {
        return this.baseColumns.size() > 0;
    }
    
    public Rules getRules() {
        return this.rules;
    }
    
    public String getTableConfigurationProperty(final String property) {
        return this.tableConfiguration.getProperty(property);
    }
    
    public String getPrimaryKeyType() {
        return this.internalAttributes.get(InternalAttribute.ATTR_PRIMARY_KEY_TYPE);
    }
    
    public String getBaseRecordType() {
        return this.internalAttributes.get(InternalAttribute.ATTR_BASE_RECORD_TYPE);
    }
    
    public String getExampleType() {
        return this.internalAttributes.get(InternalAttribute.ATTR_EXAMPLE_TYPE);
    }
    
    public String getRecordWithBLOBsType() {
        return this.internalAttributes.get(InternalAttribute.ATTR_RECORD_WITH_BLOBS_TYPE);
    }
    
    public String getIbatis2SqlMapFileName() {
        return this.internalAttributes.get(InternalAttribute.ATTR_IBATIS2_SQL_MAP_FILE_NAME);
    }
    
    public String getIbatis2SqlMapNamespace() {
        return this.internalAttributes.get(InternalAttribute.ATTR_IBATIS2_SQL_MAP_NAMESPACE);
    }
    
    public String getMyBatis3SqlMapNamespace() {
        String namespace = this.getMyBatis3JavaMapperType();
        if (namespace == null) {
            namespace = this.getMyBatis3FallbackSqlMapNamespace();
        }
        return namespace;
    }
    
    public String getMyBatis3FallbackSqlMapNamespace() {
        return this.internalAttributes.get(InternalAttribute.ATTR_MYBATIS3_FALLBACK_SQL_MAP_NAMESPACE);
    }
    
    public String getIbatis2SqlMapPackage() {
        return this.internalAttributes.get(InternalAttribute.ATTR_IBATIS2_SQL_MAP_PACKAGE);
    }
    
    public String getDAOImplementationType() {
        return this.internalAttributes.get(InternalAttribute.ATTR_DAO_IMPLEMENTATION_TYPE);
    }
    
    public String getDAOInterfaceType() {
        return this.internalAttributes.get(InternalAttribute.ATTR_DAO_INTERFACE_TYPE);
    }
    
    public boolean hasAnyColumns() {
        return this.primaryKeyColumns.size() > 0 || this.baseColumns.size() > 0 || this.blobColumns.size() > 0;
    }
    
    public void setTableConfiguration(final TableConfiguration tableConfiguration) {
        this.tableConfiguration = tableConfiguration;
    }
    
    public void setFullyQualifiedTable(final FullyQualifiedTable fullyQualifiedTable) {
        this.fullyQualifiedTable = fullyQualifiedTable;
    }
    
    public void setContext(final Context context) {
        this.context = context;
    }
    
    public void addColumn(final IntrospectedColumn introspectedColumn) {
        if (introspectedColumn.isBLOBColumn()) {
            this.blobColumns.add(introspectedColumn);
        }
        else {
            this.baseColumns.add(introspectedColumn);
        }
        introspectedColumn.setIntrospectedTable(this);
    }
    
    public void addPrimaryKeyColumn(final String columnName) {
        boolean found = false;
        Iterator<IntrospectedColumn> iter = this.baseColumns.iterator();
        while (iter.hasNext()) {
            final IntrospectedColumn introspectedColumn = iter.next();
            if (introspectedColumn.getActualColumnName().equals(columnName)) {
                this.primaryKeyColumns.add(introspectedColumn);
                iter.remove();
                found = true;
                break;
            }
        }
        if (!found) {
            iter = this.blobColumns.iterator();
            while (iter.hasNext()) {
                final IntrospectedColumn introspectedColumn = iter.next();
                if (introspectedColumn.getActualColumnName().equals(columnName)) {
                    this.primaryKeyColumns.add(introspectedColumn);
                    iter.remove();
                    found = true;
                    break;
                }
            }
        }
    }
    
    public Object getAttribute(final String name) {
        return this.attributes.get(name);
    }
    
    public void removeAttribute(final String name) {
        this.attributes.remove(name);
    }
    
    public void setAttribute(final String name, final Object value) {
        this.attributes.put(name, value);
    }
    
    public void initialize() {
        this.calculateJavaClientAttributes();
        this.calculateModelAttributes();
        this.calculateXmlAttributes();
        if (this.tableConfiguration.getModelType() == ModelType.HIERARCHICAL) {
            this.rules = new HierarchicalModelRules(this);
        }
        else if (this.tableConfiguration.getModelType() == ModelType.FLAT) {
            this.rules = new FlatModelRules(this);
        }
        else {
            this.rules = new ConditionalModelRules(this);
        }
        this.context.getPlugins().initialized(this);
    }
    
    protected void calculateXmlAttributes() {
        this.setIbatis2SqlMapPackage(this.calculateSqlMapPackage());
        this.setIbatis2SqlMapFileName(this.calculateIbatis2SqlMapFileName());
        this.setMyBatis3XmlMapperFileName(this.calculateMyBatis3XmlMapperFileName());
        this.setMyBatis3XmlMapperPackage(this.calculateSqlMapPackage());
        this.setIbatis2SqlMapNamespace(this.calculateIbatis2SqlMapNamespace());
        this.setMyBatis3FallbackSqlMapNamespace(this.calculateMyBatis3FallbackSqlMapNamespace());
        this.setSqlMapFullyQualifiedRuntimeTableName(this.calculateSqlMapFullyQualifiedRuntimeTableName());
        this.setSqlMapAliasedFullyQualifiedRuntimeTableName(this.calculateSqlMapAliasedFullyQualifiedRuntimeTableName());
        this.setCountByExampleStatementId("countByExample");
        this.setDeleteByExampleStatementId("deleteByExample");
        this.setDeleteByPrimaryKeyStatementId("deleteByPrimaryKey");
        this.setInsertStatementId("insert");
        this.setInsertSelectiveStatementId("insertSelective");
        this.setSelectAllStatementId("selectAll");
        this.setSelectByExampleStatementId("selectByExample");
        this.setSelectByExampleWithBLOBsStatementId("selectByExampleWithBLOBs");
        this.setSelectByPrimaryKeyStatementId("selectByPrimaryKey");
        this.setUpdateByExampleStatementId("updateByExample");
        this.setUpdateByExampleSelectiveStatementId("updateByExampleSelective");
        this.setUpdateByExampleWithBLOBsStatementId("updateByExampleWithBLOBs");
        this.setUpdateByPrimaryKeyStatementId("updateByPrimaryKey");
        this.setUpdateByPrimaryKeySelectiveStatementId("updateByPrimaryKeySelective");
        this.setUpdateByPrimaryKeyWithBLOBsStatementId("updateByPrimaryKeyWithBLOBs");
        this.setBaseResultMapId("BaseResultMap");
        this.setResultMapWithBLOBsId("ResultMapWithBLOBs");
        this.setExampleWhereClauseId("Example_Where_Clause");
        this.setBaseColumnListId("Base_Column_List");
        this.setBlobColumnListId("Blob_Column_List");
        this.setMyBatis3UpdateByExampleWhereClauseId("Update_By_Example_Where_Clause");
    }
    
    public void setBlobColumnListId(final String s) {
        this.internalAttributes.put(InternalAttribute.ATTR_BLOB_COLUMN_LIST_ID, s);
    }
    
    public void setBaseColumnListId(final String s) {
        this.internalAttributes.put(InternalAttribute.ATTR_BASE_COLUMN_LIST_ID, s);
    }
    
    public void setExampleWhereClauseId(final String s) {
        this.internalAttributes.put(InternalAttribute.ATTR_EXAMPLE_WHERE_CLAUSE_ID, s);
    }
    
    public void setMyBatis3UpdateByExampleWhereClauseId(final String s) {
        this.internalAttributes.put(InternalAttribute.ATTR_MYBATIS3_UPDATE_BY_EXAMPLE_WHERE_CLAUSE_ID, s);
    }
    
    public void setResultMapWithBLOBsId(final String s) {
        this.internalAttributes.put(InternalAttribute.ATTR_RESULT_MAP_WITH_BLOBS_ID, s);
    }
    
    public void setBaseResultMapId(final String s) {
        this.internalAttributes.put(InternalAttribute.ATTR_BASE_RESULT_MAP_ID, s);
    }
    
    public void setUpdateByPrimaryKeyWithBLOBsStatementId(final String s) {
        this.internalAttributes.put(InternalAttribute.ATTR_UPDATE_BY_PRIMARY_KEY_WITH_BLOBS_STATEMENT_ID, s);
    }
    
    public void setUpdateByPrimaryKeySelectiveStatementId(final String s) {
        this.internalAttributes.put(InternalAttribute.ATTR_UPDATE_BY_PRIMARY_KEY_SELECTIVE_STATEMENT_ID, s);
    }
    
    public void setUpdateByPrimaryKeyStatementId(final String s) {
        this.internalAttributes.put(InternalAttribute.ATTR_UPDATE_BY_PRIMARY_KEY_STATEMENT_ID, s);
    }
    
    public void setUpdateByExampleWithBLOBsStatementId(final String s) {
        this.internalAttributes.put(InternalAttribute.ATTR_UPDATE_BY_EXAMPLE_WITH_BLOBS_STATEMENT_ID, s);
    }
    
    public void setUpdateByExampleSelectiveStatementId(final String s) {
        this.internalAttributes.put(InternalAttribute.ATTR_UPDATE_BY_EXAMPLE_SELECTIVE_STATEMENT_ID, s);
    }
    
    public void setUpdateByExampleStatementId(final String s) {
        this.internalAttributes.put(InternalAttribute.ATTR_UPDATE_BY_EXAMPLE_STATEMENT_ID, s);
    }
    
    public void setSelectByPrimaryKeyStatementId(final String s) {
        this.internalAttributes.put(InternalAttribute.ATTR_SELECT_BY_PRIMARY_KEY_STATEMENT_ID, s);
    }
    
    public void setSelectByExampleWithBLOBsStatementId(final String s) {
        this.internalAttributes.put(InternalAttribute.ATTR_SELECT_BY_EXAMPLE_WITH_BLOBS_STATEMENT_ID, s);
    }
    
    public void setSelectAllStatementId(final String s) {
        this.internalAttributes.put(InternalAttribute.ATTR_SELECT_ALL_STATEMENT_ID, s);
    }
    
    public void setSelectByExampleStatementId(final String s) {
        this.internalAttributes.put(InternalAttribute.ATTR_SELECT_BY_EXAMPLE_STATEMENT_ID, s);
    }
    
    public void setInsertSelectiveStatementId(final String s) {
        this.internalAttributes.put(InternalAttribute.ATTR_INSERT_SELECTIVE_STATEMENT_ID, s);
    }
    
    public void setInsertStatementId(final String s) {
        this.internalAttributes.put(InternalAttribute.ATTR_INSERT_STATEMENT_ID, s);
    }
    
    public void setDeleteByPrimaryKeyStatementId(final String s) {
        this.internalAttributes.put(InternalAttribute.ATTR_DELETE_BY_PRIMARY_KEY_STATEMENT_ID, s);
    }
    
    public void setDeleteByExampleStatementId(final String s) {
        this.internalAttributes.put(InternalAttribute.ATTR_DELETE_BY_EXAMPLE_STATEMENT_ID, s);
    }
    
    public void setCountByExampleStatementId(final String s) {
        this.internalAttributes.put(InternalAttribute.ATTR_COUNT_BY_EXAMPLE_STATEMENT_ID, s);
    }
    
    public String getBlobColumnListId() {
        return this.internalAttributes.get(InternalAttribute.ATTR_BLOB_COLUMN_LIST_ID);
    }
    
    public String getBaseColumnListId() {
        return this.internalAttributes.get(InternalAttribute.ATTR_BASE_COLUMN_LIST_ID);
    }
    
    public String getExampleWhereClauseId() {
        return this.internalAttributes.get(InternalAttribute.ATTR_EXAMPLE_WHERE_CLAUSE_ID);
    }
    
    public String getMyBatis3UpdateByExampleWhereClauseId() {
        return this.internalAttributes.get(InternalAttribute.ATTR_MYBATIS3_UPDATE_BY_EXAMPLE_WHERE_CLAUSE_ID);
    }
    
    public String getResultMapWithBLOBsId() {
        return this.internalAttributes.get(InternalAttribute.ATTR_RESULT_MAP_WITH_BLOBS_ID);
    }
    
    public String getBaseResultMapId() {
        return this.internalAttributes.get(InternalAttribute.ATTR_BASE_RESULT_MAP_ID);
    }
    
    public String getUpdateByPrimaryKeyWithBLOBsStatementId() {
        return this.internalAttributes.get(InternalAttribute.ATTR_UPDATE_BY_PRIMARY_KEY_WITH_BLOBS_STATEMENT_ID);
    }
    
    public String getUpdateByPrimaryKeySelectiveStatementId() {
        return this.internalAttributes.get(InternalAttribute.ATTR_UPDATE_BY_PRIMARY_KEY_SELECTIVE_STATEMENT_ID);
    }
    
    public String getUpdateByPrimaryKeyStatementId() {
        return this.internalAttributes.get(InternalAttribute.ATTR_UPDATE_BY_PRIMARY_KEY_STATEMENT_ID);
    }
    
    public String getUpdateByExampleWithBLOBsStatementId() {
        return this.internalAttributes.get(InternalAttribute.ATTR_UPDATE_BY_EXAMPLE_WITH_BLOBS_STATEMENT_ID);
    }
    
    public String getUpdateByExampleSelectiveStatementId() {
        return this.internalAttributes.get(InternalAttribute.ATTR_UPDATE_BY_EXAMPLE_SELECTIVE_STATEMENT_ID);
    }
    
    public String getUpdateByExampleStatementId() {
        return this.internalAttributes.get(InternalAttribute.ATTR_UPDATE_BY_EXAMPLE_STATEMENT_ID);
    }
    
    public String getSelectByPrimaryKeyStatementId() {
        return this.internalAttributes.get(InternalAttribute.ATTR_SELECT_BY_PRIMARY_KEY_STATEMENT_ID);
    }
    
    public String getSelectByExampleWithBLOBsStatementId() {
        return this.internalAttributes.get(InternalAttribute.ATTR_SELECT_BY_EXAMPLE_WITH_BLOBS_STATEMENT_ID);
    }
    
    public String getSelectAllStatementId() {
        return this.internalAttributes.get(InternalAttribute.ATTR_SELECT_ALL_STATEMENT_ID);
    }
    
    public String getSelectByExampleStatementId() {
        return this.internalAttributes.get(InternalAttribute.ATTR_SELECT_BY_EXAMPLE_STATEMENT_ID);
    }
    
    public String getInsertSelectiveStatementId() {
        return this.internalAttributes.get(InternalAttribute.ATTR_INSERT_SELECTIVE_STATEMENT_ID);
    }
    
    public String getInsertStatementId() {
        return this.internalAttributes.get(InternalAttribute.ATTR_INSERT_STATEMENT_ID);
    }
    
    public String getDeleteByPrimaryKeyStatementId() {
        return this.internalAttributes.get(InternalAttribute.ATTR_DELETE_BY_PRIMARY_KEY_STATEMENT_ID);
    }
    
    public String getDeleteByExampleStatementId() {
        return this.internalAttributes.get(InternalAttribute.ATTR_DELETE_BY_EXAMPLE_STATEMENT_ID);
    }
    
    public String getCountByExampleStatementId() {
        return this.internalAttributes.get(InternalAttribute.ATTR_COUNT_BY_EXAMPLE_STATEMENT_ID);
    }
    
    protected String calculateJavaClientImplementationPackage() {
        final JavaClientGeneratorConfiguration config = this.context.getJavaClientGeneratorConfiguration();
        if (config == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        if (StringUtility.stringHasValue(config.getImplementationPackage())) {
            sb.append(config.getImplementationPackage());
        }
        else {
            sb.append(config.getTargetPackage());
        }
        sb.append(this.fullyQualifiedTable.getSubPackageForClientOrSqlMap(this.isSubPackagesEnabled(config)));
        return sb.toString();
    }
    
    private boolean isSubPackagesEnabled(final PropertyHolder propertyHolder) {
        return StringUtility.isTrue(propertyHolder.getProperty("enableSubPackages"));
    }
    
    protected String calculateJavaClientInterfacePackage() {
        final JavaClientGeneratorConfiguration config = this.context.getJavaClientGeneratorConfiguration();
        if (config == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(config.getTargetPackage());
        sb.append(this.fullyQualifiedTable.getSubPackageForClientOrSqlMap(this.isSubPackagesEnabled(config)));
        return sb.toString();
    }
    
    protected void calculateJavaClientAttributes() {
        if (this.context.getJavaClientGeneratorConfiguration() == null) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(this.calculateJavaClientImplementationPackage());
        sb.append('.');
        sb.append(this.fullyQualifiedTable.getDomainObjectName());
        sb.append("DAOImpl");
        this.setDAOImplementationType(sb.toString());
        sb.setLength(0);
        sb.append(this.calculateJavaClientInterfacePackage());
        sb.append('.');
        sb.append(this.fullyQualifiedTable.getDomainObjectName());
        sb.append("DAO");
        this.setDAOInterfaceType(sb.toString());
        sb.setLength(0);
        sb.append(this.calculateJavaClientInterfacePackage());
        sb.append('.');
        if (StringUtility.stringHasValue(this.tableConfiguration.getMapperName())) {
            sb.append(this.tableConfiguration.getMapperName());
        }
        else {
            if (StringUtility.stringHasValue(this.fullyQualifiedTable.getDomainObjectSubPackage())) {
                sb.append(this.fullyQualifiedTable.getDomainObjectSubPackage());
                sb.append('.');
            }
            sb.append(this.fullyQualifiedTable.getDomainObjectName());
            sb.append("Mapper");
        }
        this.setMyBatis3JavaMapperType(sb.toString());
        sb.setLength(0);
        sb.append(this.calculateJavaClientInterfacePackage());
        sb.append('.');
        if (StringUtility.stringHasValue(this.tableConfiguration.getSqlProviderName())) {
            sb.append(this.tableConfiguration.getSqlProviderName());
        }
        else {
            if (StringUtility.stringHasValue(this.fullyQualifiedTable.getDomainObjectSubPackage())) {
                sb.append(this.fullyQualifiedTable.getDomainObjectSubPackage());
                sb.append('.');
            }
            sb.append(this.fullyQualifiedTable.getDomainObjectName());
            sb.append("SqlProvider");
        }
        this.setMyBatis3SqlProviderType(sb.toString());
        sb.setLength(0);
        sb.append(this.calculateJavaClientInterfacePackage());
        sb.append('.');
        sb.append(this.fullyQualifiedTable.getDomainObjectName());
        sb.append("DynamicSqlSupport");
        this.setMyBatisDynamicSqlSupportType(sb.toString());
    }
    
    protected String calculateJavaModelPackage() {
        final JavaModelGeneratorConfiguration config = this.context.getJavaModelGeneratorConfiguration();
        final StringBuilder sb = new StringBuilder();
        sb.append(config.getTargetPackage());
        sb.append(this.fullyQualifiedTable.getSubPackageForModel(this.isSubPackagesEnabled(config)));
        return sb.toString();
    }
    
    protected void calculateModelAttributes() {
        final String pakkage = this.calculateJavaModelPackage();
        final StringBuilder sb = new StringBuilder();
        sb.append(pakkage);
        sb.append('.');
        sb.append(this.fullyQualifiedTable.getDomainObjectName());
        sb.append("Key");
        this.setPrimaryKeyType(sb.toString());
        sb.setLength(0);
        sb.append(pakkage);
        sb.append('.');
        sb.append(this.fullyQualifiedTable.getDomainObjectName());
        this.setBaseRecordType(sb.toString());
        sb.setLength(0);
        sb.append(pakkage);
        sb.append('.');
        sb.append(this.fullyQualifiedTable.getDomainObjectName());
        sb.append("WithBLOBs");
        this.setRecordWithBLOBsType(sb.toString());
        sb.setLength(0);
        sb.append(pakkage);
        sb.append('.');
        sb.append(this.fullyQualifiedTable.getDomainObjectName());
        sb.append("Example");
        this.setExampleType(sb.toString());
    }
    
    protected String calculateSqlMapPackage() {
        final StringBuilder sb = new StringBuilder();
        final SqlMapGeneratorConfiguration config = this.context.getSqlMapGeneratorConfiguration();
        if (config != null) {
            sb.append(config.getTargetPackage());
            sb.append(this.fullyQualifiedTable.getSubPackageForClientOrSqlMap(this.isSubPackagesEnabled(config)));
            if (StringUtility.stringHasValue(this.tableConfiguration.getMapperName())) {
                final String mapperName = this.tableConfiguration.getMapperName();
                final int ind = mapperName.lastIndexOf(46);
                if (ind != -1) {
                    sb.append('.').append(mapperName.substring(0, ind));
                }
            }
            else if (StringUtility.stringHasValue(this.fullyQualifiedTable.getDomainObjectSubPackage())) {
                sb.append('.').append(this.fullyQualifiedTable.getDomainObjectSubPackage());
            }
        }
        return sb.toString();
    }
    
    protected String calculateIbatis2SqlMapFileName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.fullyQualifiedTable.getIbatis2SqlMapNamespace());
        sb.append("_SqlMap.xml");
        return sb.toString();
    }
    
    protected String calculateMyBatis3XmlMapperFileName() {
        final StringBuilder sb = new StringBuilder();
        if (StringUtility.stringHasValue(this.tableConfiguration.getMapperName())) {
            final String mapperName = this.tableConfiguration.getMapperName();
            final int ind = mapperName.lastIndexOf(46);
            if (ind == -1) {
                sb.append(mapperName);
            }
            else {
                sb.append(mapperName.substring(ind + 1));
            }
            sb.append(".xml");
        }
        else {
            sb.append(this.fullyQualifiedTable.getDomainObjectName());
            sb.append("Mapper.xml");
        }
        return sb.toString();
    }
    
    protected String calculateIbatis2SqlMapNamespace() {
        return this.fullyQualifiedTable.getIbatis2SqlMapNamespace();
    }
    
    protected String calculateMyBatis3FallbackSqlMapNamespace() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.calculateSqlMapPackage());
        sb.append('.');
        if (StringUtility.stringHasValue(this.tableConfiguration.getMapperName())) {
            sb.append(this.tableConfiguration.getMapperName());
        }
        else {
            sb.append(this.fullyQualifiedTable.getDomainObjectName());
            sb.append("Mapper");
        }
        return sb.toString();
    }
    
    protected String calculateSqlMapFullyQualifiedRuntimeTableName() {
        return this.fullyQualifiedTable.getFullyQualifiedTableNameAtRuntime();
    }
    
    protected String calculateSqlMapAliasedFullyQualifiedRuntimeTableName() {
        return this.fullyQualifiedTable.getAliasedFullyQualifiedTableNameAtRuntime();
    }
    
    public String getFullyQualifiedTableNameAtRuntime() {
        return this.internalAttributes.get(InternalAttribute.ATTR_FULLY_QUALIFIED_TABLE_NAME_AT_RUNTIME);
    }
    
    public String getAliasedFullyQualifiedTableNameAtRuntime() {
        return this.internalAttributes.get(InternalAttribute.ATTR_ALIASED_FULLY_QUALIFIED_TABLE_NAME_AT_RUNTIME);
    }
    
    public abstract void calculateGenerators(final List<String> p0, final ProgressCallback p1);
    
    public abstract List<GeneratedJavaFile> getGeneratedJavaFiles();
    
    public abstract List<GeneratedXmlFile> getGeneratedXmlFiles();
    
    public abstract boolean isJava5Targeted();
    
    public abstract int getGenerationSteps();
    
    public void setRules(final Rules rules) {
        this.rules = rules;
    }
    
    public TableConfiguration getTableConfiguration() {
        return this.tableConfiguration;
    }
    
    public void setDAOImplementationType(final String daoImplementationType) {
        this.internalAttributes.put(InternalAttribute.ATTR_DAO_IMPLEMENTATION_TYPE, daoImplementationType);
    }
    
    public void setDAOInterfaceType(final String daoInterfaceType) {
        this.internalAttributes.put(InternalAttribute.ATTR_DAO_INTERFACE_TYPE, daoInterfaceType);
    }
    
    public void setPrimaryKeyType(final String primaryKeyType) {
        this.internalAttributes.put(InternalAttribute.ATTR_PRIMARY_KEY_TYPE, primaryKeyType);
    }
    
    public void setBaseRecordType(final String baseRecordType) {
        this.internalAttributes.put(InternalAttribute.ATTR_BASE_RECORD_TYPE, baseRecordType);
    }
    
    public void setRecordWithBLOBsType(final String recordWithBLOBsType) {
        this.internalAttributes.put(InternalAttribute.ATTR_RECORD_WITH_BLOBS_TYPE, recordWithBLOBsType);
    }
    
    public void setExampleType(final String exampleType) {
        this.internalAttributes.put(InternalAttribute.ATTR_EXAMPLE_TYPE, exampleType);
    }
    
    public void setIbatis2SqlMapPackage(final String sqlMapPackage) {
        this.internalAttributes.put(InternalAttribute.ATTR_IBATIS2_SQL_MAP_PACKAGE, sqlMapPackage);
    }
    
    public void setIbatis2SqlMapFileName(final String sqlMapFileName) {
        this.internalAttributes.put(InternalAttribute.ATTR_IBATIS2_SQL_MAP_FILE_NAME, sqlMapFileName);
    }
    
    public void setIbatis2SqlMapNamespace(final String sqlMapNamespace) {
        this.internalAttributes.put(InternalAttribute.ATTR_IBATIS2_SQL_MAP_NAMESPACE, sqlMapNamespace);
    }
    
    public void setMyBatis3FallbackSqlMapNamespace(final String sqlMapNamespace) {
        this.internalAttributes.put(InternalAttribute.ATTR_MYBATIS3_FALLBACK_SQL_MAP_NAMESPACE, sqlMapNamespace);
    }
    
    public void setSqlMapFullyQualifiedRuntimeTableName(final String fullyQualifiedRuntimeTableName) {
        this.internalAttributes.put(InternalAttribute.ATTR_FULLY_QUALIFIED_TABLE_NAME_AT_RUNTIME, fullyQualifiedRuntimeTableName);
    }
    
    public void setSqlMapAliasedFullyQualifiedRuntimeTableName(final String aliasedFullyQualifiedRuntimeTableName) {
        this.internalAttributes.put(InternalAttribute.ATTR_ALIASED_FULLY_QUALIFIED_TABLE_NAME_AT_RUNTIME, aliasedFullyQualifiedRuntimeTableName);
    }
    
    public String getMyBatis3XmlMapperPackage() {
        return this.internalAttributes.get(InternalAttribute.ATTR_MYBATIS3_XML_MAPPER_PACKAGE);
    }
    
    public void setMyBatis3XmlMapperPackage(final String mybatis3XmlMapperPackage) {
        this.internalAttributes.put(InternalAttribute.ATTR_MYBATIS3_XML_MAPPER_PACKAGE, mybatis3XmlMapperPackage);
    }
    
    public String getMyBatis3XmlMapperFileName() {
        return this.internalAttributes.get(InternalAttribute.ATTR_MYBATIS3_XML_MAPPER_FILE_NAME);
    }
    
    public void setMyBatis3XmlMapperFileName(final String mybatis3XmlMapperFileName) {
        this.internalAttributes.put(InternalAttribute.ATTR_MYBATIS3_XML_MAPPER_FILE_NAME, mybatis3XmlMapperFileName);
    }
    
    public String getMyBatis3JavaMapperType() {
        return this.internalAttributes.get(InternalAttribute.ATTR_MYBATIS3_JAVA_MAPPER_TYPE);
    }
    
    public void setMyBatis3JavaMapperType(final String mybatis3JavaMapperType) {
        this.internalAttributes.put(InternalAttribute.ATTR_MYBATIS3_JAVA_MAPPER_TYPE, mybatis3JavaMapperType);
    }
    
    public String getMyBatis3SqlProviderType() {
        return this.internalAttributes.get(InternalAttribute.ATTR_MYBATIS3_SQL_PROVIDER_TYPE);
    }
    
    public void setMyBatis3SqlProviderType(final String mybatis3SqlProviderType) {
        this.internalAttributes.put(InternalAttribute.ATTR_MYBATIS3_SQL_PROVIDER_TYPE, mybatis3SqlProviderType);
    }
    
    public String getMyBatisDynamicSqlSupportType() {
        return this.internalAttributes.get(InternalAttribute.ATTR_MYBATIS_DYNAMIC_SQL_SUPPORT_TYPE);
    }
    
    public void setMyBatisDynamicSqlSupportType(final String s) {
        this.internalAttributes.put(InternalAttribute.ATTR_MYBATIS_DYNAMIC_SQL_SUPPORT_TYPE, s);
    }
    
    public TargetRuntime getTargetRuntime() {
        return this.targetRuntime;
    }
    
    public boolean isImmutable() {
        Properties properties;
        if (this.tableConfiguration.getProperties().containsKey("immutable")) {
            properties = this.tableConfiguration.getProperties();
        }
        else {
            properties = this.context.getJavaModelGeneratorConfiguration().getProperties();
        }
        return StringUtility.isTrue(properties.getProperty("immutable"));
    }
    
    public boolean isConstructorBased() {
        if (this.isImmutable()) {
            return true;
        }
        Properties properties;
        if (this.tableConfiguration.getProperties().containsKey("constructorBased")) {
            properties = this.tableConfiguration.getProperties();
        }
        else {
            properties = this.context.getJavaModelGeneratorConfiguration().getProperties();
        }
        return StringUtility.isTrue(properties.getProperty("constructorBased"));
    }
    
    public abstract boolean requiresXMLGenerator();
    
    public Context getContext() {
        return this.context;
    }
    
    public String getRemarks() {
        return this.remarks;
    }
    
    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }
    
    public String getTableType() {
        return this.tableType;
    }
    
    public void setTableType(final String tableType) {
        this.tableType = tableType;
    }
    
    public enum TargetRuntime
    {
        IBATIS2, 
        MYBATIS3, 
        MYBATIS3_DSQL;
    }
    
    protected enum InternalAttribute
    {
        ATTR_DAO_IMPLEMENTATION_TYPE, 
        ATTR_DAO_INTERFACE_TYPE, 
        ATTR_PRIMARY_KEY_TYPE, 
        ATTR_BASE_RECORD_TYPE, 
        ATTR_RECORD_WITH_BLOBS_TYPE, 
        ATTR_EXAMPLE_TYPE, 
        ATTR_IBATIS2_SQL_MAP_PACKAGE, 
        ATTR_IBATIS2_SQL_MAP_FILE_NAME, 
        ATTR_IBATIS2_SQL_MAP_NAMESPACE, 
        ATTR_MYBATIS3_XML_MAPPER_PACKAGE, 
        ATTR_MYBATIS3_XML_MAPPER_FILE_NAME, 
        ATTR_MYBATIS3_JAVA_MAPPER_TYPE, 
        ATTR_MYBATIS3_FALLBACK_SQL_MAP_NAMESPACE, 
        ATTR_FULLY_QUALIFIED_TABLE_NAME_AT_RUNTIME, 
        ATTR_ALIASED_FULLY_QUALIFIED_TABLE_NAME_AT_RUNTIME, 
        ATTR_COUNT_BY_EXAMPLE_STATEMENT_ID, 
        ATTR_DELETE_BY_EXAMPLE_STATEMENT_ID, 
        ATTR_DELETE_BY_PRIMARY_KEY_STATEMENT_ID, 
        ATTR_INSERT_STATEMENT_ID, 
        ATTR_INSERT_SELECTIVE_STATEMENT_ID, 
        ATTR_SELECT_ALL_STATEMENT_ID, 
        ATTR_SELECT_BY_EXAMPLE_STATEMENT_ID, 
        ATTR_SELECT_BY_EXAMPLE_WITH_BLOBS_STATEMENT_ID, 
        ATTR_SELECT_BY_PRIMARY_KEY_STATEMENT_ID, 
        ATTR_UPDATE_BY_EXAMPLE_STATEMENT_ID, 
        ATTR_UPDATE_BY_EXAMPLE_SELECTIVE_STATEMENT_ID, 
        ATTR_UPDATE_BY_EXAMPLE_WITH_BLOBS_STATEMENT_ID, 
        ATTR_UPDATE_BY_PRIMARY_KEY_STATEMENT_ID, 
        ATTR_UPDATE_BY_PRIMARY_KEY_SELECTIVE_STATEMENT_ID, 
        ATTR_UPDATE_BY_PRIMARY_KEY_WITH_BLOBS_STATEMENT_ID, 
        ATTR_BASE_RESULT_MAP_ID, 
        ATTR_RESULT_MAP_WITH_BLOBS_ID, 
        ATTR_EXAMPLE_WHERE_CLAUSE_ID, 
        ATTR_BASE_COLUMN_LIST_ID, 
        ATTR_BLOB_COLUMN_LIST_ID, 
        ATTR_MYBATIS3_UPDATE_BY_EXAMPLE_WHERE_CLAUSE_ID, 
        ATTR_MYBATIS3_SQL_PROVIDER_TYPE, 
        ATTR_MYBATIS_DYNAMIC_SQL_SUPPORT_TYPE;
    }
}

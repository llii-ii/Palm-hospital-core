package org.mybatis.generator.internal.rules;

import org.mybatis.generator.config.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.*;

public abstract class BaseRules implements Rules
{
    protected TableConfiguration tableConfiguration;
    protected IntrospectedTable introspectedTable;
    protected final boolean isModelOnly;
    
    public BaseRules(final IntrospectedTable introspectedTable) {
        this.introspectedTable = introspectedTable;
        this.tableConfiguration = introspectedTable.getTableConfiguration();
        final String modelOnly = this.tableConfiguration.getProperty("modelOnly");
        this.isModelOnly = StringUtility.isTrue(modelOnly);
    }
    
    @Override
    public boolean generateInsert() {
        return !this.isModelOnly && this.tableConfiguration.isInsertStatementEnabled();
    }
    
    @Override
    public boolean generateInsertSelective() {
        return !this.isModelOnly && this.tableConfiguration.isInsertStatementEnabled();
    }
    
    @Override
    public FullyQualifiedJavaType calculateAllFieldsClass() {
        String answer;
        if (this.generateRecordWithBLOBsClass()) {
            answer = this.introspectedTable.getRecordWithBLOBsType();
        }
        else if (this.generateBaseRecordClass()) {
            answer = this.introspectedTable.getBaseRecordType();
        }
        else {
            answer = this.introspectedTable.getPrimaryKeyType();
        }
        return new FullyQualifiedJavaType(answer);
    }
    
    @Override
    public boolean generateUpdateByPrimaryKeyWithoutBLOBs() {
        if (this.isModelOnly) {
            return false;
        }
        if (ListUtilities.removeGeneratedAlwaysColumns(this.introspectedTable.getBaseColumns()).isEmpty()) {
            return false;
        }
        final boolean rc = this.tableConfiguration.isUpdateByPrimaryKeyStatementEnabled() && this.introspectedTable.hasPrimaryKeyColumns() && this.introspectedTable.hasBaseColumns();
        return rc;
    }
    
    @Override
    public boolean generateUpdateByPrimaryKeyWithBLOBs() {
        if (this.isModelOnly) {
            return false;
        }
        if (ListUtilities.removeGeneratedAlwaysColumns(this.introspectedTable.getNonPrimaryKeyColumns()).isEmpty()) {
            return false;
        }
        final boolean rc = this.tableConfiguration.isUpdateByPrimaryKeyStatementEnabled() && this.introspectedTable.hasPrimaryKeyColumns() && this.introspectedTable.hasBLOBColumns();
        return rc;
    }
    
    @Override
    public boolean generateUpdateByPrimaryKeySelective() {
        if (this.isModelOnly) {
            return false;
        }
        if (ListUtilities.removeGeneratedAlwaysColumns(this.introspectedTable.getNonPrimaryKeyColumns()).isEmpty()) {
            return false;
        }
        final boolean rc = this.tableConfiguration.isUpdateByPrimaryKeyStatementEnabled() && this.introspectedTable.hasPrimaryKeyColumns() && (this.introspectedTable.hasBLOBColumns() || this.introspectedTable.hasBaseColumns());
        return rc;
    }
    
    @Override
    public boolean generateDeleteByPrimaryKey() {
        if (this.isModelOnly) {
            return false;
        }
        final boolean rc = this.tableConfiguration.isDeleteByPrimaryKeyStatementEnabled() && this.introspectedTable.hasPrimaryKeyColumns();
        return rc;
    }
    
    @Override
    public boolean generateDeleteByExample() {
        if (this.isModelOnly) {
            return false;
        }
        final boolean rc = this.tableConfiguration.isDeleteByExampleStatementEnabled();
        return rc;
    }
    
    @Override
    public boolean generateBaseResultMap() {
        if (this.isModelOnly) {
            return true;
        }
        final boolean rc = this.tableConfiguration.isSelectByExampleStatementEnabled() || this.tableConfiguration.isSelectByPrimaryKeyStatementEnabled();
        return rc;
    }
    
    @Override
    public boolean generateResultMapWithBLOBs() {
        final boolean rc = this.introspectedTable.hasBLOBColumns() && (this.isModelOnly || this.tableConfiguration.isSelectByExampleStatementEnabled() || this.tableConfiguration.isSelectByPrimaryKeyStatementEnabled());
        return rc;
    }
    
    @Override
    public boolean generateSQLExampleWhereClause() {
        if (this.isModelOnly) {
            return false;
        }
        boolean rc = this.tableConfiguration.isSelectByExampleStatementEnabled() || this.tableConfiguration.isDeleteByExampleStatementEnabled() || this.tableConfiguration.isCountByExampleStatementEnabled();
        if (this.introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.IBATIS2) {
            rc |= this.tableConfiguration.isUpdateByExampleStatementEnabled();
        }
        return rc;
    }
    
    @Override
    public boolean generateMyBatis3UpdateByExampleWhereClause() {
        return !this.isModelOnly && this.introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3 && this.tableConfiguration.isUpdateByExampleStatementEnabled();
    }
    
    @Override
    public boolean generateSelectByPrimaryKey() {
        if (this.isModelOnly) {
            return false;
        }
        final boolean rc = this.tableConfiguration.isSelectByPrimaryKeyStatementEnabled() && this.introspectedTable.hasPrimaryKeyColumns() && (this.introspectedTable.hasBaseColumns() || this.introspectedTable.hasBLOBColumns());
        return rc;
    }
    
    @Override
    public boolean generateSelectByExampleWithoutBLOBs() {
        return !this.isModelOnly && this.tableConfiguration.isSelectByExampleStatementEnabled();
    }
    
    @Override
    public boolean generateSelectByExampleWithBLOBs() {
        if (this.isModelOnly) {
            return false;
        }
        final boolean rc = this.tableConfiguration.isSelectByExampleStatementEnabled() && this.introspectedTable.hasBLOBColumns();
        return rc;
    }
    
    @Override
    public boolean generateExampleClass() {
        if (this.introspectedTable.getContext().getSqlMapGeneratorConfiguration() == null && this.introspectedTable.getContext().getJavaClientGeneratorConfiguration() == null) {
            return false;
        }
        if (this.isModelOnly) {
            return false;
        }
        final boolean rc = this.tableConfiguration.isSelectByExampleStatementEnabled() || this.tableConfiguration.isDeleteByExampleStatementEnabled() || this.tableConfiguration.isCountByExampleStatementEnabled() || this.tableConfiguration.isUpdateByExampleStatementEnabled();
        return rc;
    }
    
    @Override
    public boolean generateCountByExample() {
        if (this.isModelOnly) {
            return false;
        }
        final boolean rc = this.tableConfiguration.isCountByExampleStatementEnabled();
        return rc;
    }
    
    @Override
    public boolean generateUpdateByExampleSelective() {
        if (this.isModelOnly) {
            return false;
        }
        final boolean rc = this.tableConfiguration.isUpdateByExampleStatementEnabled();
        return rc;
    }
    
    @Override
    public boolean generateUpdateByExampleWithoutBLOBs() {
        if (this.isModelOnly) {
            return false;
        }
        final boolean rc = this.tableConfiguration.isUpdateByExampleStatementEnabled() && (this.introspectedTable.hasPrimaryKeyColumns() || this.introspectedTable.hasBaseColumns());
        return rc;
    }
    
    @Override
    public boolean generateUpdateByExampleWithBLOBs() {
        if (this.isModelOnly) {
            return false;
        }
        final boolean rc = this.tableConfiguration.isUpdateByExampleStatementEnabled() && this.introspectedTable.hasBLOBColumns();
        return rc;
    }
    
    @Override
    public IntrospectedTable getIntrospectedTable() {
        return this.introspectedTable;
    }
    
    @Override
    public boolean generateBaseColumnList() {
        return !this.isModelOnly && (this.generateSelectByPrimaryKey() || this.generateSelectByExampleWithoutBLOBs());
    }
    
    @Override
    public boolean generateBlobColumnList() {
        return !this.isModelOnly && this.introspectedTable.hasBLOBColumns() && (this.tableConfiguration.isSelectByExampleStatementEnabled() || this.tableConfiguration.isSelectByPrimaryKeyStatementEnabled());
    }
    
    @Override
    public boolean generateJavaClient() {
        return !this.isModelOnly;
    }
}

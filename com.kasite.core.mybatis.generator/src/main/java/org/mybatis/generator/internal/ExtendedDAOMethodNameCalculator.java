package org.mybatis.generator.internal;

import org.mybatis.generator.api.*;
import org.mybatis.generator.internal.rules.*;

public class ExtendedDAOMethodNameCalculator implements DAOMethodNameCalculator
{
    @Override
    public String getInsertMethodName(final IntrospectedTable introspectedTable) {
        final StringBuilder sb = new StringBuilder();
        sb.append("insert");
        sb.append(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        return sb.toString();
    }
    
    @Override
    public String getUpdateByPrimaryKeyWithoutBLOBsMethodName(final IntrospectedTable introspectedTable) {
        final StringBuilder sb = new StringBuilder();
        sb.append("update");
        sb.append(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        final Rules rules = introspectedTable.getRules();
        if (!rules.generateUpdateByPrimaryKeyWithBLOBs()) {
            sb.append("ByPrimaryKey");
        }
        else if (rules.generateRecordWithBLOBsClass()) {
            sb.append("ByPrimaryKey");
        }
        else {
            sb.append("ByPrimaryKeyWithoutBLOBs");
        }
        return sb.toString();
    }
    
    @Override
    public String getUpdateByPrimaryKeyWithBLOBsMethodName(final IntrospectedTable introspectedTable) {
        final StringBuilder sb = new StringBuilder();
        sb.append("update");
        sb.append(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        final Rules rules = introspectedTable.getRules();
        if (!rules.generateUpdateByPrimaryKeyWithoutBLOBs()) {
            sb.append("ByPrimaryKey");
        }
        else if (rules.generateRecordWithBLOBsClass()) {
            sb.append("ByPrimaryKey");
        }
        else {
            sb.append("ByPrimaryKeyWithBLOBs");
        }
        return sb.toString();
    }
    
    @Override
    public String getDeleteByExampleMethodName(final IntrospectedTable introspectedTable) {
        final StringBuilder sb = new StringBuilder();
        sb.append("delete");
        sb.append(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        sb.append("ByExample");
        return sb.toString();
    }
    
    @Override
    public String getDeleteByPrimaryKeyMethodName(final IntrospectedTable introspectedTable) {
        final StringBuilder sb = new StringBuilder();
        sb.append("delete");
        sb.append(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        sb.append("ByPrimaryKey");
        return sb.toString();
    }
    
    @Override
    public String getSelectByExampleWithoutBLOBsMethodName(final IntrospectedTable introspectedTable) {
        final StringBuilder sb = new StringBuilder();
        sb.append("select");
        sb.append(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        sb.append("ByExample");
        final Rules rules = introspectedTable.getRules();
        if (rules.generateSelectByExampleWithBLOBs()) {
            sb.append("WithoutBLOBs");
        }
        return sb.toString();
    }
    
    @Override
    public String getSelectByExampleWithBLOBsMethodName(final IntrospectedTable introspectedTable) {
        final StringBuilder sb = new StringBuilder();
        sb.append("select");
        sb.append(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        sb.append("ByExample");
        final Rules rules = introspectedTable.getRules();
        if (rules.generateSelectByExampleWithoutBLOBs()) {
            sb.append("WithBLOBs");
        }
        return sb.toString();
    }
    
    @Override
    public String getSelectByPrimaryKeyMethodName(final IntrospectedTable introspectedTable) {
        final StringBuilder sb = new StringBuilder();
        sb.append("select");
        sb.append(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        sb.append("ByPrimaryKey");
        return sb.toString();
    }
    
    @Override
    public String getUpdateByPrimaryKeySelectiveMethodName(final IntrospectedTable introspectedTable) {
        final StringBuilder sb = new StringBuilder();
        sb.append("update");
        sb.append(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        sb.append("ByPrimaryKeySelective");
        return sb.toString();
    }
    
    @Override
    public String getCountByExampleMethodName(final IntrospectedTable introspectedTable) {
        final StringBuilder sb = new StringBuilder();
        sb.append("count");
        sb.append(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        sb.append("ByExample");
        return sb.toString();
    }
    
    @Override
    public String getUpdateByExampleSelectiveMethodName(final IntrospectedTable introspectedTable) {
        final StringBuilder sb = new StringBuilder();
        sb.append("update");
        sb.append(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        sb.append("ByExampleSelective");
        return sb.toString();
    }
    
    @Override
    public String getUpdateByExampleWithBLOBsMethodName(final IntrospectedTable introspectedTable) {
        final StringBuilder sb = new StringBuilder();
        sb.append("update");
        sb.append(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        final Rules rules = introspectedTable.getRules();
        if (!rules.generateUpdateByExampleWithoutBLOBs()) {
            sb.append("ByExample");
        }
        else if (rules.generateRecordWithBLOBsClass()) {
            sb.append("ByExample");
        }
        else {
            sb.append("ByExampleWithBLOBs");
        }
        return sb.toString();
    }
    
    @Override
    public String getUpdateByExampleWithoutBLOBsMethodName(final IntrospectedTable introspectedTable) {
        final StringBuilder sb = new StringBuilder();
        sb.append("update");
        sb.append(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        final Rules rules = introspectedTable.getRules();
        if (!rules.generateUpdateByExampleWithBLOBs()) {
            sb.append("ByExample");
        }
        else if (rules.generateRecordWithBLOBsClass()) {
            sb.append("ByExample");
        }
        else {
            sb.append("ByExampleWithoutBLOBs");
        }
        return sb.toString();
    }
    
    @Override
    public String getInsertSelectiveMethodName(final IntrospectedTable introspectedTable) {
        final StringBuilder sb = new StringBuilder();
        sb.append("insert");
        sb.append(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        sb.append("Selective");
        return sb.toString();
    }
}

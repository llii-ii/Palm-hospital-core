package org.mybatis.generator.internal;

import org.mybatis.generator.api.*;
import org.mybatis.generator.internal.rules.*;

public class DefaultDAOMethodNameCalculator implements DAOMethodNameCalculator
{
    @Override
    public String getInsertMethodName(final IntrospectedTable introspectedTable) {
        return "insert";
    }
    
    @Override
    public String getUpdateByPrimaryKeyWithoutBLOBsMethodName(final IntrospectedTable introspectedTable) {
        final Rules rules = introspectedTable.getRules();
        if (!rules.generateUpdateByPrimaryKeyWithBLOBs()) {
            return "updateByPrimaryKey";
        }
        if (rules.generateRecordWithBLOBsClass()) {
            return "updateByPrimaryKey";
        }
        return "updateByPrimaryKeyWithoutBLOBs";
    }
    
    @Override
    public String getUpdateByPrimaryKeyWithBLOBsMethodName(final IntrospectedTable introspectedTable) {
        final Rules rules = introspectedTable.getRules();
        if (!rules.generateUpdateByPrimaryKeyWithoutBLOBs()) {
            return "updateByPrimaryKey";
        }
        if (rules.generateRecordWithBLOBsClass()) {
            return "updateByPrimaryKey";
        }
        return "updateByPrimaryKeyWithBLOBs";
    }
    
    @Override
    public String getDeleteByExampleMethodName(final IntrospectedTable introspectedTable) {
        return "deleteByExample";
    }
    
    @Override
    public String getDeleteByPrimaryKeyMethodName(final IntrospectedTable introspectedTable) {
        return "deleteByPrimaryKey";
    }
    
    @Override
    public String getSelectByExampleWithoutBLOBsMethodName(final IntrospectedTable introspectedTable) {
        final Rules rules = introspectedTable.getRules();
        if (!rules.generateSelectByExampleWithBLOBs()) {
            return "selectByExample";
        }
        return "selectByExampleWithoutBLOBs";
    }
    
    @Override
    public String getSelectByExampleWithBLOBsMethodName(final IntrospectedTable introspectedTable) {
        final Rules rules = introspectedTable.getRules();
        if (!rules.generateSelectByExampleWithoutBLOBs()) {
            return "selectByExample";
        }
        return "selectByExampleWithBLOBs";
    }
    
    @Override
    public String getSelectByPrimaryKeyMethodName(final IntrospectedTable introspectedTable) {
        return "selectByPrimaryKey";
    }
    
    @Override
    public String getUpdateByPrimaryKeySelectiveMethodName(final IntrospectedTable introspectedTable) {
        return "updateByPrimaryKeySelective";
    }
    
    @Override
    public String getCountByExampleMethodName(final IntrospectedTable introspectedTable) {
        return "countByExample";
    }
    
    @Override
    public String getUpdateByExampleSelectiveMethodName(final IntrospectedTable introspectedTable) {
        return "updateByExampleSelective";
    }
    
    @Override
    public String getUpdateByExampleWithBLOBsMethodName(final IntrospectedTable introspectedTable) {
        final Rules rules = introspectedTable.getRules();
        if (!rules.generateUpdateByExampleWithoutBLOBs()) {
            return "updateByExample";
        }
        if (rules.generateRecordWithBLOBsClass()) {
            return "updateByExample";
        }
        return "updateByExampleWithBLOBs";
    }
    
    @Override
    public String getUpdateByExampleWithoutBLOBsMethodName(final IntrospectedTable introspectedTable) {
        final Rules rules = introspectedTable.getRules();
        if (!rules.generateUpdateByExampleWithBLOBs()) {
            return "updateByExample";
        }
        if (rules.generateRecordWithBLOBsClass()) {
            return "updateByExample";
        }
        return "updateByExampleWithoutBLOBs";
    }
    
    @Override
    public String getInsertSelectiveMethodName(final IntrospectedTable introspectedTable) {
        return "insertSelective";
    }
}

package org.mybatis.generator.internal.rules;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.*;

public class RulesDelegate implements Rules
{
    protected Rules rules;
    
    public RulesDelegate(final Rules rules) {
        this.rules = rules;
    }
    
    @Override
    public FullyQualifiedJavaType calculateAllFieldsClass() {
        return this.rules.calculateAllFieldsClass();
    }
    
    @Override
    public boolean generateBaseRecordClass() {
        return this.rules.generateBaseRecordClass();
    }
    
    @Override
    public boolean generateBaseResultMap() {
        return this.rules.generateBaseResultMap();
    }
    
    @Override
    public boolean generateCountByExample() {
        return this.rules.generateCountByExample();
    }
    
    @Override
    public boolean generateDeleteByExample() {
        return this.rules.generateDeleteByExample();
    }
    
    @Override
    public boolean generateDeleteByPrimaryKey() {
        return this.rules.generateDeleteByPrimaryKey();
    }
    
    @Override
    public boolean generateExampleClass() {
        return this.rules.generateExampleClass();
    }
    
    @Override
    public boolean generateInsert() {
        return this.rules.generateInsert();
    }
    
    @Override
    public boolean generateInsertSelective() {
        return this.rules.generateInsertSelective();
    }
    
    @Override
    public boolean generatePrimaryKeyClass() {
        return this.rules.generatePrimaryKeyClass();
    }
    
    @Override
    public boolean generateRecordWithBLOBsClass() {
        return this.rules.generateRecordWithBLOBsClass();
    }
    
    @Override
    public boolean generateResultMapWithBLOBs() {
        return this.rules.generateResultMapWithBLOBs();
    }
    
    @Override
    public boolean generateSelectByExampleWithBLOBs() {
        return this.rules.generateSelectByExampleWithBLOBs();
    }
    
    @Override
    public boolean generateSelectByExampleWithoutBLOBs() {
        return this.rules.generateSelectByExampleWithoutBLOBs();
    }
    
    @Override
    public boolean generateSelectByPrimaryKey() {
        return this.rules.generateSelectByPrimaryKey();
    }
    
    @Override
    public boolean generateSQLExampleWhereClause() {
        return this.rules.generateSQLExampleWhereClause();
    }
    
    @Override
    public boolean generateMyBatis3UpdateByExampleWhereClause() {
        return this.rules.generateMyBatis3UpdateByExampleWhereClause();
    }
    
    @Override
    public boolean generateUpdateByExampleSelective() {
        return this.rules.generateUpdateByExampleSelective();
    }
    
    @Override
    public boolean generateUpdateByExampleWithBLOBs() {
        return this.rules.generateUpdateByExampleWithBLOBs();
    }
    
    @Override
    public boolean generateUpdateByExampleWithoutBLOBs() {
        return this.rules.generateUpdateByExampleWithoutBLOBs();
    }
    
    @Override
    public boolean generateUpdateByPrimaryKeySelective() {
        return this.rules.generateUpdateByPrimaryKeySelective();
    }
    
    @Override
    public boolean generateUpdateByPrimaryKeyWithBLOBs() {
        return this.rules.generateUpdateByPrimaryKeyWithBLOBs();
    }
    
    @Override
    public boolean generateUpdateByPrimaryKeyWithoutBLOBs() {
        return this.rules.generateUpdateByPrimaryKeyWithoutBLOBs();
    }
    
    @Override
    public IntrospectedTable getIntrospectedTable() {
        return this.rules.getIntrospectedTable();
    }
    
    @Override
    public boolean generateBaseColumnList() {
        return this.rules.generateBaseColumnList();
    }
    
    @Override
    public boolean generateBlobColumnList() {
        return this.rules.generateBlobColumnList();
    }
    
    @Override
    public boolean generateJavaClient() {
        return this.rules.generateJavaClient();
    }
}

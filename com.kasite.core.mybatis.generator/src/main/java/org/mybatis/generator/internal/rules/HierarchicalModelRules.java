package org.mybatis.generator.internal.rules;

import org.mybatis.generator.api.*;

public class HierarchicalModelRules extends BaseRules
{
    public HierarchicalModelRules(final IntrospectedTable introspectedTable) {
        super(introspectedTable);
    }
    
    @Override
    public boolean generatePrimaryKeyClass() {
        return this.introspectedTable.hasPrimaryKeyColumns();
    }
    
    @Override
    public boolean generateBaseRecordClass() {
        return this.introspectedTable.hasBaseColumns();
    }
    
    @Override
    public boolean generateRecordWithBLOBsClass() {
        return this.introspectedTable.hasBLOBColumns();
    }
}

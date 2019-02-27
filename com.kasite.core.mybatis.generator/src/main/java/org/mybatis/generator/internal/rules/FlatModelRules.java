package org.mybatis.generator.internal.rules;

import org.mybatis.generator.api.*;

public class FlatModelRules extends BaseRules
{
    public FlatModelRules(final IntrospectedTable introspectedTable) {
        super(introspectedTable);
    }
    
    @Override
    public boolean generatePrimaryKeyClass() {
        return false;
    }
    
    @Override
    public boolean generateBaseRecordClass() {
        return true;
    }
    
    @Override
    public boolean generateRecordWithBLOBsClass() {
        return false;
    }
}

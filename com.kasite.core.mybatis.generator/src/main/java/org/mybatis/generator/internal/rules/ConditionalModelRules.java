package org.mybatis.generator.internal.rules;

import org.mybatis.generator.api.*;

public class ConditionalModelRules extends BaseRules
{
    public ConditionalModelRules(final IntrospectedTable introspectedTable) {
        super(introspectedTable);
    }
    
    @Override
    public boolean generatePrimaryKeyClass() {
        return this.introspectedTable.getPrimaryKeyColumns().size() > 1;
    }
    
    @Override
    public boolean generateBaseRecordClass() {
        return this.introspectedTable.hasBaseColumns() || this.introspectedTable.getPrimaryKeyColumns().size() == 1 || this.blobsAreInBaseRecord();
    }
    
    private boolean blobsAreInBaseRecord() {
        return this.introspectedTable.hasBLOBColumns() && !this.generateRecordWithBLOBsClass();
    }
    
    @Override
    public boolean generateRecordWithBLOBsClass() {
        final int otherColumnCount = this.introspectedTable.getPrimaryKeyColumns().size() + this.introspectedTable.getBaseColumns().size();
        return otherColumnCount > 1 && this.introspectedTable.getBLOBColumns().size() > 1;
    }
}

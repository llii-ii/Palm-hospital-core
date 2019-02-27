package org.mybatis.generator.codegen.mybatis3.javamapper.elements.sqlprovider;

import java.util.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;

public class ProviderUpdateByExampleWithBLOBsMethodGenerator extends ProviderUpdateByExampleWithoutBLOBsMethodGenerator
{
    public ProviderUpdateByExampleWithBLOBsMethodGenerator(final boolean useLegacyBuilder) {
        super(useLegacyBuilder);
    }
    
    @Override
    public String getMethodName() {
        return this.introspectedTable.getUpdateByExampleWithBLOBsStatementId();
    }
    
    @Override
    public List<IntrospectedColumn> getColumns() {
        return this.introspectedTable.getAllColumns();
    }
    
    @Override
    public boolean callPlugins(final Method method, final TopLevelClass topLevelClass) {
        return this.context.getPlugins().providerUpdateByExampleWithBLOBsMethodGenerated(method, topLevelClass, this.introspectedTable);
    }
}

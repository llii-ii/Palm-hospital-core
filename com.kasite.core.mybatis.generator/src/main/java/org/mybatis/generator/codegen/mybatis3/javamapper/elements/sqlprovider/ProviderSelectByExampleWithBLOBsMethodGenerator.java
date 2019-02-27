package org.mybatis.generator.codegen.mybatis3.javamapper.elements.sqlprovider;

import java.util.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;

public class ProviderSelectByExampleWithBLOBsMethodGenerator extends ProviderSelectByExampleWithoutBLOBsMethodGenerator
{
    public ProviderSelectByExampleWithBLOBsMethodGenerator(final boolean useLegacyBuilder) {
        super(useLegacyBuilder);
    }
    
    @Override
    public List<IntrospectedColumn> getColumns() {
        return this.introspectedTable.getAllColumns();
    }
    
    @Override
    public String getMethodName() {
        return this.introspectedTable.getSelectByExampleWithBLOBsStatementId();
    }
    
    @Override
    public boolean callPlugins(final Method method, final TopLevelClass topLevelClass) {
        return this.context.getPlugins().providerSelectByExampleWithBLOBsMethodGenerated(method, topLevelClass, this.introspectedTable);
    }
}

package org.mybatis.generator.codegen.mybatis3.javamapper.elements.sqlprovider;

import org.mybatis.generator.codegen.*;
import org.mybatis.generator.api.dom.java.*;

public abstract class AbstractJavaProviderMethodGenerator extends AbstractGenerator
{
    protected static final FullyQualifiedJavaType NEW_BUILDER_IMPORT;
    protected boolean useLegacyBuilder;
    protected final String builderPrefix;
    
    public AbstractJavaProviderMethodGenerator(final boolean useLegacyBuilder) {
        this.useLegacyBuilder = useLegacyBuilder;
        if (useLegacyBuilder) {
            this.builderPrefix = "";
        }
        else {
            this.builderPrefix = "sql.";
        }
    }
    
    public abstract void addClassElements(final TopLevelClass p0);
    
    static {
        NEW_BUILDER_IMPORT = new FullyQualifiedJavaType("org.apache.ibatis.jdbc.SQL");
    }
}

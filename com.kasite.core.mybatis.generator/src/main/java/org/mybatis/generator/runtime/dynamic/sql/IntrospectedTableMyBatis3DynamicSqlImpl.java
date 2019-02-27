package org.mybatis.generator.runtime.dynamic.sql;

import org.mybatis.generator.codegen.mybatis3.*;
import java.util.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.*;

public class IntrospectedTableMyBatis3DynamicSqlImpl extends IntrospectedTableMyBatis3Impl
{
    public IntrospectedTableMyBatis3DynamicSqlImpl() {
        this.targetRuntime = TargetRuntime.MYBATIS3_DSQL;
    }
    
    @Override
    protected void calculateXmlMapperGenerator(final AbstractJavaClientGenerator javaClientGenerator, final List<String> warnings, final ProgressCallback progressCallback) {
        this.xmlMapperGenerator = null;
    }
    
    @Override
    protected AbstractJavaClientGenerator createJavaClientGenerator() {
        if (this.context.getJavaClientGeneratorConfiguration() == null) {
            return null;
        }
        return new DynamicSqlMapperGenerator();
    }
    
    @Override
    protected void calculateJavaModelGenerators(final List<String> warnings, final ProgressCallback progressCallback) {
        final AbstractJavaGenerator javaGenerator = new DynamicSqlModelGenerator();
        this.initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
        this.javaModelGenerators.add(javaGenerator);
    }
    
    @Override
    public boolean requiresXMLGenerator() {
        return false;
    }
}

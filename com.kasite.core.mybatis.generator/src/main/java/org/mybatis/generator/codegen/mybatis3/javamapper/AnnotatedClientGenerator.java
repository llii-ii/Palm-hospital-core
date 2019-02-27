package org.mybatis.generator.codegen.mybatis3.javamapper;

import org.mybatis.generator.codegen.mybatis3.javamapper.elements.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.*;
import java.util.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.codegen.*;

public class AnnotatedClientGenerator extends JavaMapperGenerator
{
    public AnnotatedClientGenerator() {
        super(false);
    }
    
    @Override
    protected void addCountByExampleMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateCountByExample()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedCountByExampleMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    protected void addDeleteByExampleMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateDeleteByExample()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedDeleteByExampleMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    protected void addDeleteByPrimaryKeyMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedDeleteByPrimaryKeyMethodGenerator(false);
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    protected void addInsertMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateInsert()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedInsertMethodGenerator(false);
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    protected void addInsertSelectiveMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateInsertSelective()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedInsertSelectiveMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    protected void addSelectByExampleWithBLOBsMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateSelectByExampleWithBLOBs()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedSelectByExampleWithBLOBsMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    protected void addSelectByExampleWithoutBLOBsMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateSelectByExampleWithoutBLOBs()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedSelectByExampleWithoutBLOBsMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    protected void addSelectByPrimaryKeyMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateSelectByPrimaryKey()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedSelectByPrimaryKeyMethodGenerator(false, false);
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    protected void addUpdateByExampleSelectiveMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateUpdateByExampleSelective()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedUpdateByExampleSelectiveMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    protected void addUpdateByExampleWithBLOBsMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateUpdateByExampleWithBLOBs()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedUpdateByExampleWithBLOBsMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    protected void addUpdateByExampleWithoutBLOBsMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateUpdateByExampleWithoutBLOBs()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedUpdateByExampleWithoutBLOBsMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    protected void addUpdateByPrimaryKeySelectiveMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedUpdateByPrimaryKeySelectiveMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    protected void addUpdateByPrimaryKeyWithBLOBsMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateUpdateByPrimaryKeyWithBLOBs()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedUpdateByPrimaryKeyWithBLOBsMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    protected void addUpdateByPrimaryKeyWithoutBLOBsMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateUpdateByPrimaryKeyWithoutBLOBs()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedUpdateByPrimaryKeyWithoutBLOBsMethodGenerator(false);
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    public List<CompilationUnit> getExtraCompilationUnits() {
        boolean useLegacyBuilder = false;
        final String prop = this.context.getJavaClientGeneratorConfiguration().getProperty("useLegacyBuilder");
        if (StringUtility.stringHasValue(prop)) {
            useLegacyBuilder = Boolean.valueOf(prop);
        }
        final SqlProviderGenerator sqlProviderGenerator = new SqlProviderGenerator(useLegacyBuilder);
        sqlProviderGenerator.setContext(this.context);
        sqlProviderGenerator.setIntrospectedTable(this.introspectedTable);
        sqlProviderGenerator.setProgressCallback(this.progressCallback);
        sqlProviderGenerator.setWarnings(this.warnings);
        return sqlProviderGenerator.getCompilationUnits();
    }
    
    @Override
    public AbstractXmlGenerator getMatchedXMLGenerator() {
        return null;
    }
}

package org.mybatis.generator.codegen.mybatis3.javamapper;

import org.mybatis.generator.codegen.*;
import org.mybatis.generator.internal.util.messages.*;
import org.mybatis.generator.api.dom.java.*;
import java.util.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.sqlprovider.*;

public class SqlProviderGenerator extends AbstractJavaGenerator
{
    private boolean useLegacyBuilder;
    
    public SqlProviderGenerator(final boolean useLegacyBuilder) {
        this.useLegacyBuilder = useLegacyBuilder;
    }
    
    @Override
    public List<CompilationUnit> getCompilationUnits() {
        this.progressCallback.startTask(Messages.getString("Progress.18", this.introspectedTable.getFullyQualifiedTable().toString()));
        final CommentGenerator commentGenerator = this.context.getCommentGenerator();
        final FullyQualifiedJavaType type = new FullyQualifiedJavaType(this.introspectedTable.getMyBatis3SqlProviderType());
        final TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(topLevelClass);
//        boolean addApplyWhereMethod = false;
//        addApplyWhereMethod |= this.addCountByExampleMethod(topLevelClass);
//        addApplyWhereMethod |= this.addDeleteByExampleMethod(topLevelClass);
//        this.addInsertSelectiveMethod(topLevelClass);
//        addApplyWhereMethod |= this.addSelectByExampleWithBLOBsMethod(topLevelClass);
//        addApplyWhereMethod |= this.addSelectByExampleWithoutBLOBsMethod(topLevelClass);
//        addApplyWhereMethod |= this.addUpdateByExampleSelectiveMethod(topLevelClass);
//        addApplyWhereMethod |= this.addUpdateByExampleWithBLOBsMethod(topLevelClass);
//        addApplyWhereMethod |= this.addUpdateByExampleWithoutBLOBsMethod(topLevelClass);
//        this.addUpdateByPrimaryKeySelectiveMethod(topLevelClass);
//        if (addApplyWhereMethod) {
//            this.addApplyWhereMethod(topLevelClass);
//        }
        final List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        if (topLevelClass.getMethods().size() > 0 && this.context.getPlugins().providerGenerated(topLevelClass, this.introspectedTable)) {
            answer.add(topLevelClass);
        }
        return answer;
    }
    
    protected boolean addCountByExampleMethod(final TopLevelClass topLevelClass) {
        boolean rc = false;
        if (this.introspectedTable.getRules().generateCountByExample()) {
            final AbstractJavaProviderMethodGenerator methodGenerator = new ProviderCountByExampleMethodGenerator(this.useLegacyBuilder);
            this.initializeAndExecuteGenerator(methodGenerator, topLevelClass);
            rc = true;
        }
        return rc;
    }
    
    protected boolean addDeleteByExampleMethod(final TopLevelClass topLevelClass) {
        boolean rc = false;
        if (this.introspectedTable.getRules().generateDeleteByExample()) {
            final AbstractJavaProviderMethodGenerator methodGenerator = new ProviderDeleteByExampleMethodGenerator(this.useLegacyBuilder);
            this.initializeAndExecuteGenerator(methodGenerator, topLevelClass);
            rc = true;
        }
        return rc;
    }
    
    protected void addInsertSelectiveMethod(final TopLevelClass topLevelClass) {
        if (this.introspectedTable.getRules().generateInsertSelective()) {
            final AbstractJavaProviderMethodGenerator methodGenerator = new ProviderInsertSelectiveMethodGenerator(this.useLegacyBuilder);
            this.initializeAndExecuteGenerator(methodGenerator, topLevelClass);
        }
    }
    
    protected boolean addSelectByExampleWithBLOBsMethod(final TopLevelClass topLevelClass) {
        boolean rc = false;
        if (this.introspectedTable.getRules().generateSelectByExampleWithBLOBs()) {
            final AbstractJavaProviderMethodGenerator methodGenerator = new ProviderSelectByExampleWithBLOBsMethodGenerator(this.useLegacyBuilder);
            this.initializeAndExecuteGenerator(methodGenerator, topLevelClass);
            rc = true;
        }
        return rc;
    }
    
    protected boolean addSelectByExampleWithoutBLOBsMethod(final TopLevelClass topLevelClass) {
        boolean rc = false;
        if (this.introspectedTable.getRules().generateSelectByExampleWithoutBLOBs()) {
            final AbstractJavaProviderMethodGenerator methodGenerator = new ProviderSelectByExampleWithoutBLOBsMethodGenerator(this.useLegacyBuilder);
            this.initializeAndExecuteGenerator(methodGenerator, topLevelClass);
            rc = true;
        }
        return rc;
    }
    
    protected boolean addUpdateByExampleSelectiveMethod(final TopLevelClass topLevelClass) {
        boolean rc = false;
        if (this.introspectedTable.getRules().generateUpdateByExampleSelective()) {
            final AbstractJavaProviderMethodGenerator methodGenerator = new ProviderUpdateByExampleSelectiveMethodGenerator(this.useLegacyBuilder);
            this.initializeAndExecuteGenerator(methodGenerator, topLevelClass);
            rc = true;
        }
        return rc;
    }
    
    protected boolean addUpdateByExampleWithBLOBsMethod(final TopLevelClass topLevelClass) {
        boolean rc = false;
        if (this.introspectedTable.getRules().generateUpdateByExampleWithBLOBs()) {
            final AbstractJavaProviderMethodGenerator methodGenerator = new ProviderUpdateByExampleWithBLOBsMethodGenerator(this.useLegacyBuilder);
            this.initializeAndExecuteGenerator(methodGenerator, topLevelClass);
            rc = true;
        }
        return rc;
    }
    
    protected boolean addUpdateByExampleWithoutBLOBsMethod(final TopLevelClass topLevelClass) {
        boolean rc = false;
        if (this.introspectedTable.getRules().generateUpdateByExampleWithoutBLOBs()) {
            final AbstractJavaProviderMethodGenerator methodGenerator = new ProviderUpdateByExampleWithoutBLOBsMethodGenerator(this.useLegacyBuilder);
            this.initializeAndExecuteGenerator(methodGenerator, topLevelClass);
            rc = true;
        }
        return rc;
    }
    
    protected void addUpdateByPrimaryKeySelectiveMethod(final TopLevelClass topLevelClass) {
        if (this.introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            final AbstractJavaProviderMethodGenerator methodGenerator = new ProviderUpdateByPrimaryKeySelectiveMethodGenerator(this.useLegacyBuilder);
            this.initializeAndExecuteGenerator(methodGenerator, topLevelClass);
        }
    }
    
    protected void addApplyWhereMethod(final TopLevelClass topLevelClass) {
        final AbstractJavaProviderMethodGenerator methodGenerator = new ProviderApplyWhereMethodGenerator(this.useLegacyBuilder);
        this.initializeAndExecuteGenerator(methodGenerator, topLevelClass);
    }
    
    protected void initializeAndExecuteGenerator(final AbstractJavaProviderMethodGenerator methodGenerator, final TopLevelClass topLevelClass) {
        methodGenerator.setContext(this.context);
        methodGenerator.setIntrospectedTable(this.introspectedTable);
        methodGenerator.setProgressCallback(this.progressCallback);
        methodGenerator.setWarnings(this.warnings);
        methodGenerator.addClassElements(topLevelClass);
    }
}

package org.mybatis.generator.codegen.mybatis3.javamapper;

import org.mybatis.generator.internal.util.messages.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.dom.java.*;
import java.util.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.*;
import org.mybatis.generator.codegen.*;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.*;

public class SimpleJavaClientGenerator extends AbstractJavaClientGenerator
{
    public SimpleJavaClientGenerator() {
        super(true);
    }
    
    public SimpleJavaClientGenerator(final boolean requiresMatchedXMLGenerator) {
        super(requiresMatchedXMLGenerator);
    }
    
    @Override
    public List<CompilationUnit> getCompilationUnits() {
        this.progressCallback.startTask(Messages.getString("Progress.17", this.introspectedTable.getFullyQualifiedTable().toString()));
        final CommentGenerator commentGenerator = this.context.getCommentGenerator();
        final FullyQualifiedJavaType type = new FullyQualifiedJavaType(this.introspectedTable.getMyBatis3JavaMapperType());
        final Interface interfaze = new Interface(type);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(interfaze);
        String rootInterface = this.introspectedTable.getTableConfigurationProperty("rootInterface");
        if (!StringUtility.stringHasValue(rootInterface)) {
            rootInterface = this.context.getJavaClientGeneratorConfiguration().getProperty("rootInterface");
        }
        if (StringUtility.stringHasValue(rootInterface)) {
            final FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(rootInterface);
            interfaze.addSuperInterface(fqjt);
            interfaze.addImportedType(fqjt);
        }
        this.addDeleteByPrimaryKeyMethod(interfaze);
        this.addInsertMethod(interfaze);
        this.addSelectByPrimaryKeyMethod(interfaze);
        this.addSelectAllMethod(interfaze);
        this.addUpdateByPrimaryKeyMethod(interfaze);
        final List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        if (this.context.getPlugins().clientGenerated(interfaze, null, this.introspectedTable)) {
            answer.add(interfaze);
        }
        final List<CompilationUnit> extraCompilationUnits = this.getExtraCompilationUnits();
        if (extraCompilationUnits != null) {
            answer.addAll(extraCompilationUnits);
        }
        return answer;
    }
    
    protected void addDeleteByPrimaryKeyMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new DeleteByPrimaryKeyMethodGenerator(true);
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addInsertMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateInsert()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new InsertMethodGenerator(true);
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addSelectByPrimaryKeyMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateSelectByPrimaryKey()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new SelectByPrimaryKeyMethodGenerator(true);
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addSelectAllMethod(final Interface interfaze) {
        final AbstractJavaMapperMethodGenerator methodGenerator = new SelectAllMethodGenerator();
        this.initializeAndExecuteGenerator(methodGenerator, interfaze);
    }
    
    protected void addUpdateByPrimaryKeyMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new UpdateByPrimaryKeyWithoutBLOBsMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void initializeAndExecuteGenerator(final AbstractJavaMapperMethodGenerator methodGenerator, final Interface interfaze) {
        methodGenerator.setContext(this.context);
        methodGenerator.setIntrospectedTable(this.introspectedTable);
        methodGenerator.setProgressCallback(this.progressCallback);
        methodGenerator.setWarnings(this.warnings);
        methodGenerator.addInterfaceElements(interfaze);
    }
    
    public List<CompilationUnit> getExtraCompilationUnits() {
        return null;
    }
    
    @Override
    public AbstractXmlGenerator getMatchedXMLGenerator() {
        return new SimpleXMLMapperGenerator();
    }
}

package org.mybatis.generator.codegen.mybatis3.javamapper;

import org.mybatis.generator.internal.util.messages.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.dom.java.*;
import java.util.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.*;
import org.mybatis.generator.codegen.*;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.*;

public class JavaMapperGenerator extends AbstractJavaClientGenerator
{
    public JavaMapperGenerator() {
        super(true);
    }
    
    public JavaMapperGenerator(final boolean requiresMatchedXMLGenerator) {
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
        String domainObjectName = this.introspectedTable.getTableConfiguration().getDomainObjectName();
        
//        if (!StringUtility.stringHasValue(rootInterface)) {
//            rootInterface = this.context.getJavaClientGeneratorConfiguration().getProperty("rootInterface");
//        }
//        if (StringUtility.stringHasValue(rootInterface)) {
//            final FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(rootInterface);
//            interfaze.addSuperInterface(fqjt);
//            interfaze.addImportedType(fqjt);
//        }
        
        String rootInterface = "Mapper<"+domainObjectName+">";
        final FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(rootInterface);
        interfaze.addSuperInterface(fqjt);
        interfaze.addImportedType(new FullyQualifiedJavaType("com.kasite.client.mybatis.entity."+domainObjectName));
        interfaze.addImportedType(new FullyQualifiedJavaType("tk.mybatis.mapper.common.Mapper"));
        
        interfaze.getType().setBaseQualifiedName(interfaze.getType().getFullyQualifiedName().replace("Mapper", "Dao"));
        interfaze.getType().setBaseShortName(interfaze.getType().getShortName().replace("Mapper", "Dao"));
//        this.addCountByExampleMethod(interfaze);
//        this.addDeleteByExampleMethod(interfaze);
//        this.addDeleteByPrimaryKeyMethod(interfaze);
//        this.addInsertMethod(interfaze);
//        this.addInsertSelectiveMethod(interfaze);
//        this.addSelectByExampleWithBLOBsMethod(interfaze);
//        this.addSelectByExampleWithoutBLOBsMethod(interfaze);
//        this.addSelectByPrimaryKeyMethod(interfaze);
//        this.addUpdateByExampleSelectiveMethod(interfaze);
//        this.addUpdateByExampleWithBLOBsMethod(interfaze);
//        this.addUpdateByExampleWithoutBLOBsMethod(interfaze);
//        this.addUpdateByPrimaryKeySelectiveMethod(interfaze);
//        this.addUpdateByPrimaryKeyWithBLOBsMethod(interfaze);
//        this.addUpdateByPrimaryKeyWithoutBLOBsMethod(interfaze);
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
    
    protected void addCountByExampleMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateCountByExample()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new CountByExampleMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addDeleteByExampleMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateDeleteByExample()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new DeleteByExampleMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addDeleteByPrimaryKeyMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new DeleteByPrimaryKeyMethodGenerator(false);
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addInsertMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateInsert()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new InsertMethodGenerator(false);
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addInsertSelectiveMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateInsertSelective()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new InsertSelectiveMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addSelectByExampleWithBLOBsMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateSelectByExampleWithBLOBs()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new SelectByExampleWithBLOBsMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addSelectByExampleWithoutBLOBsMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateSelectByExampleWithoutBLOBs()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new SelectByExampleWithoutBLOBsMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addSelectByPrimaryKeyMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateSelectByPrimaryKey()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new SelectByPrimaryKeyMethodGenerator(false);
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addUpdateByExampleSelectiveMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateUpdateByExampleSelective()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new UpdateByExampleSelectiveMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addUpdateByExampleWithBLOBsMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateUpdateByExampleWithBLOBs()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new UpdateByExampleWithBLOBsMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addUpdateByExampleWithoutBLOBsMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateUpdateByExampleWithoutBLOBs()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new UpdateByExampleWithoutBLOBsMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addUpdateByPrimaryKeySelectiveMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new UpdateByPrimaryKeySelectiveMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addUpdateByPrimaryKeyWithBLOBsMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateUpdateByPrimaryKeyWithBLOBs()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new UpdateByPrimaryKeyWithBLOBsMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addUpdateByPrimaryKeyWithoutBLOBsMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateUpdateByPrimaryKeyWithoutBLOBs()) {
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
        return new XMLMapperGenerator();
    }
}

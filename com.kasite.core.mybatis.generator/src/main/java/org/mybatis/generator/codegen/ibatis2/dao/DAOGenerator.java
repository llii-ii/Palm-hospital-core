package org.mybatis.generator.codegen.ibatis2.dao;

import org.mybatis.generator.codegen.ibatis2.dao.templates.*;
import org.mybatis.generator.internal.util.messages.*;
import org.mybatis.generator.api.dom.java.*;
import java.util.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.internal.rules.*;
import org.mybatis.generator.codegen.ibatis2.dao.elements.*;
import org.mybatis.generator.codegen.*;

public class DAOGenerator extends AbstractJavaClientGenerator
{
    private AbstractDAOTemplate daoTemplate;
    private boolean generateForJava5;
    
    public DAOGenerator(final AbstractDAOTemplate daoTemplate, final boolean generateForJava5) {
        super(true);
        this.daoTemplate = daoTemplate;
        this.generateForJava5 = generateForJava5;
    }
    
    @Override
    public List<CompilationUnit> getCompilationUnits() {
        final FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();
        this.progressCallback.startTask(Messages.getString("Progress.14", table.toString()));
        final TopLevelClass topLevelClass = this.getTopLevelClassShell();
        final Interface interfaze = this.getInterfaceShell();
        this.addCountByExampleMethod(topLevelClass, interfaze);
        this.addDeleteByExampleMethod(topLevelClass, interfaze);
        this.addDeleteByPrimaryKeyMethod(topLevelClass, interfaze);
        this.addInsertMethod(topLevelClass, interfaze);
        this.addInsertSelectiveMethod(topLevelClass, interfaze);
        this.addSelectByExampleWithBLOBsMethod(topLevelClass, interfaze);
        this.addSelectByExampleWithoutBLOBsMethod(topLevelClass, interfaze);
        this.addSelectByPrimaryKeyMethod(topLevelClass, interfaze);
        this.addUpdateByExampleParmsInnerclass(topLevelClass, interfaze);
        this.addUpdateByExampleSelectiveMethod(topLevelClass, interfaze);
        this.addUpdateByExampleWithBLOBsMethod(topLevelClass, interfaze);
        this.addUpdateByExampleWithoutBLOBsMethod(topLevelClass, interfaze);
        this.addUpdateByPrimaryKeySelectiveMethod(topLevelClass, interfaze);
        this.addUpdateByPrimaryKeyWithBLOBsMethod(topLevelClass, interfaze);
        this.addUpdateByPrimaryKeyWithoutBLOBsMethod(topLevelClass, interfaze);
        final List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        if (this.context.getPlugins().clientGenerated(interfaze, topLevelClass, this.introspectedTable)) {
            answer.add(topLevelClass);
            answer.add(interfaze);
        }
        return answer;
    }
    
    protected TopLevelClass getTopLevelClassShell() {
        final FullyQualifiedJavaType interfaceType = new FullyQualifiedJavaType(this.introspectedTable.getDAOInterfaceType());
        final FullyQualifiedJavaType implementationType = new FullyQualifiedJavaType(this.introspectedTable.getDAOImplementationType());
        final TopLevelClass answer = new TopLevelClass(implementationType);
        answer.setVisibility(JavaVisibility.PUBLIC);
        answer.setSuperClass(this.daoTemplate.getSuperClass());
        answer.addImportedType(this.daoTemplate.getSuperClass());
        answer.addSuperInterface(interfaceType);
        answer.addImportedType(interfaceType);
        for (final FullyQualifiedJavaType fqjt : this.daoTemplate.getImplementationImports()) {
            answer.addImportedType(fqjt);
        }
        final CommentGenerator commentGenerator = this.context.getCommentGenerator();
        commentGenerator.addJavaFileComment(answer);
        answer.addMethod(this.daoTemplate.getConstructorClone(commentGenerator, implementationType, this.introspectedTable));
        for (final Field field : this.daoTemplate.getFieldClones(commentGenerator, this.introspectedTable)) {
            answer.addField(field);
        }
        for (final Method method : this.daoTemplate.getMethodClones(commentGenerator, this.introspectedTable)) {
            answer.addMethod(method);
        }
        return answer;
    }
    
    protected Interface getInterfaceShell() {
        final Interface answer = new Interface(new FullyQualifiedJavaType(this.introspectedTable.getDAOInterfaceType()));
        answer.setVisibility(JavaVisibility.PUBLIC);
        String rootInterface = this.introspectedTable.getTableConfigurationProperty("rootInterface");
        if (!StringUtility.stringHasValue(rootInterface)) {
            rootInterface = this.context.getJavaClientGeneratorConfiguration().getProperty("rootInterface");
        }
        if (StringUtility.stringHasValue(rootInterface)) {
            final FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(rootInterface);
            answer.addSuperInterface(fqjt);
            answer.addImportedType(fqjt);
        }
        for (final FullyQualifiedJavaType fqjt2 : this.daoTemplate.getInterfaceImports()) {
            answer.addImportedType(fqjt2);
        }
        this.context.getCommentGenerator().addJavaFileComment(answer);
        return answer;
    }
    
    protected void addCountByExampleMethod(final TopLevelClass topLevelClass, final Interface interfaze) {
        if (this.introspectedTable.getRules().generateCountByExample()) {
            final AbstractDAOElementGenerator methodGenerator = new CountByExampleMethodGenerator(this.generateForJava5);
            this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addDeleteByExampleMethod(final TopLevelClass topLevelClass, final Interface interfaze) {
        if (this.introspectedTable.getRules().generateDeleteByExample()) {
            final AbstractDAOElementGenerator methodGenerator = new DeleteByExampleMethodGenerator(this.generateForJava5);
            this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addDeleteByPrimaryKeyMethod(final TopLevelClass topLevelClass, final Interface interfaze) {
        if (this.introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            final AbstractDAOElementGenerator methodGenerator = new DeleteByPrimaryKeyMethodGenerator(this.generateForJava5);
            this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addInsertMethod(final TopLevelClass topLevelClass, final Interface interfaze) {
        if (this.introspectedTable.getRules().generateInsert()) {
            final AbstractDAOElementGenerator methodGenerator = new InsertMethodGenerator(this.generateForJava5);
            this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addInsertSelectiveMethod(final TopLevelClass topLevelClass, final Interface interfaze) {
        if (this.introspectedTable.getRules().generateInsertSelective()) {
            final AbstractDAOElementGenerator methodGenerator = new InsertSelectiveMethodGenerator(this.generateForJava5);
            this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addSelectByExampleWithBLOBsMethod(final TopLevelClass topLevelClass, final Interface interfaze) {
        if (this.introspectedTable.getRules().generateSelectByExampleWithBLOBs()) {
            final AbstractDAOElementGenerator methodGenerator = new SelectByExampleWithBLOBsMethodGenerator(this.generateForJava5);
            this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addSelectByExampleWithoutBLOBsMethod(final TopLevelClass topLevelClass, final Interface interfaze) {
        if (this.introspectedTable.getRules().generateSelectByExampleWithoutBLOBs()) {
            final AbstractDAOElementGenerator methodGenerator = new SelectByExampleWithoutBLOBsMethodGenerator(this.generateForJava5);
            this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addSelectByPrimaryKeyMethod(final TopLevelClass topLevelClass, final Interface interfaze) {
        if (this.introspectedTable.getRules().generateSelectByPrimaryKey()) {
            final AbstractDAOElementGenerator methodGenerator = new SelectByPrimaryKeyMethodGenerator(this.generateForJava5);
            this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addUpdateByExampleParmsInnerclass(final TopLevelClass topLevelClass, final Interface interfaze) {
        final Rules rules = this.introspectedTable.getRules();
        if (rules.generateUpdateByExampleSelective() || rules.generateUpdateByExampleWithBLOBs() || rules.generateUpdateByExampleWithoutBLOBs()) {
            final AbstractDAOElementGenerator methodGenerator = new UpdateByExampleParmsInnerclassGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addUpdateByExampleSelectiveMethod(final TopLevelClass topLevelClass, final Interface interfaze) {
        if (this.introspectedTable.getRules().generateUpdateByExampleSelective()) {
            final AbstractDAOElementGenerator methodGenerator = new UpdateByExampleSelectiveMethodGenerator(this.generateForJava5);
            this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addUpdateByExampleWithBLOBsMethod(final TopLevelClass topLevelClass, final Interface interfaze) {
        if (this.introspectedTable.getRules().generateUpdateByExampleWithBLOBs()) {
            final AbstractDAOElementGenerator methodGenerator = new UpdateByExampleWithBLOBsMethodGenerator(this.generateForJava5);
            this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addUpdateByExampleWithoutBLOBsMethod(final TopLevelClass topLevelClass, final Interface interfaze) {
        if (this.introspectedTable.getRules().generateUpdateByExampleWithoutBLOBs()) {
            final AbstractDAOElementGenerator methodGenerator = new UpdateByExampleWithoutBLOBsMethodGenerator(this.generateForJava5);
            this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addUpdateByPrimaryKeySelectiveMethod(final TopLevelClass topLevelClass, final Interface interfaze) {
        if (this.introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            final AbstractDAOElementGenerator methodGenerator = new UpdateByPrimaryKeySelectiveMethodGenerator(this.generateForJava5);
            this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addUpdateByPrimaryKeyWithBLOBsMethod(final TopLevelClass topLevelClass, final Interface interfaze) {
        if (this.introspectedTable.getRules().generateUpdateByPrimaryKeyWithBLOBs()) {
            final AbstractDAOElementGenerator methodGenerator = new UpdateByPrimaryKeyWithBLOBsMethodGenerator(this.generateForJava5);
            this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addUpdateByPrimaryKeyWithoutBLOBsMethod(final TopLevelClass topLevelClass, final Interface interfaze) {
        if (this.introspectedTable.getRules().generateUpdateByPrimaryKeyWithoutBLOBs()) {
            final AbstractDAOElementGenerator methodGenerator = new UpdateByPrimaryKeyWithoutBLOBsMethodGenerator(this.generateForJava5);
            this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void initializeAndExecuteGenerator(final AbstractDAOElementGenerator methodGenerator, final TopLevelClass topLevelClass, final Interface interfaze) {
        methodGenerator.setDAOTemplate(this.daoTemplate);
        methodGenerator.setContext(this.context);
        methodGenerator.setIntrospectedTable(this.introspectedTable);
        methodGenerator.setProgressCallback(this.progressCallback);
        methodGenerator.setWarnings(this.warnings);
        methodGenerator.addImplementationElements(topLevelClass);
        methodGenerator.addInterfaceElements(interfaze);
    }
    
    @Override
    public AbstractXmlGenerator getMatchedXMLGenerator() {
        return null;
    }
}

package org.mybatis.generator.codegen.ibatis2.dao.templates;

import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import java.util.*;
import java.text.*;

public abstract class AbstractDAOTemplate
{
    private List<FullyQualifiedJavaType> interfaceImports;
    private List<FullyQualifiedJavaType> implementationImports;
    private FullyQualifiedJavaType superClass;
    private List<FullyQualifiedJavaType> checkedExceptions;
    private List<Field> fields;
    private List<Method> methods;
    private Method constructorTemplate;
    private String deleteMethodTemplate;
    private String insertMethodTemplate;
    private String updateMethodTemplate;
    private String queryForObjectMethodTemplate;
    private String queryForListMethodTemplate;
    private boolean configured;
    
    public AbstractDAOTemplate() {
        this.interfaceImports = new ArrayList<FullyQualifiedJavaType>();
        this.implementationImports = new ArrayList<FullyQualifiedJavaType>();
        this.fields = new ArrayList<Field>();
        this.methods = new ArrayList<Method>();
        this.checkedExceptions = new ArrayList<FullyQualifiedJavaType>();
        this.configured = false;
    }
    
    public final Method getConstructorClone(final CommentGenerator commentGenerator, final FullyQualifiedJavaType type, final IntrospectedTable introspectedTable) {
        this.configure();
        final Method answer = new Method();
        answer.setConstructor(true);
        answer.setName(type.getShortName());
        answer.setVisibility(this.constructorTemplate.getVisibility());
        for (final Parameter parm : this.constructorTemplate.getParameters()) {
            answer.addParameter(parm);
        }
        for (final String bodyLine : this.constructorTemplate.getBodyLines()) {
            answer.addBodyLine(bodyLine);
        }
        for (final FullyQualifiedJavaType fqjt : this.constructorTemplate.getExceptions()) {
            answer.addException(fqjt);
        }
        commentGenerator.addGeneralMethodComment(answer, introspectedTable);
        return answer;
    }
    
    public final String getDeleteMethod(final String sqlMapNamespace, final String statementId, final String parameter) {
        this.configure();
        final String answer = MessageFormat.format(this.deleteMethodTemplate, sqlMapNamespace, statementId, parameter);
        return answer;
    }
    
    public final List<FullyQualifiedJavaType> getInterfaceImports() {
        this.configure();
        return this.interfaceImports;
    }
    
    public final List<FullyQualifiedJavaType> getImplementationImports() {
        this.configure();
        return this.implementationImports;
    }
    
    public final String getInsertMethod(final String sqlMapNamespace, final String statementId, final String parameter) {
        this.configure();
        final String answer = MessageFormat.format(this.insertMethodTemplate, sqlMapNamespace, statementId, parameter);
        return answer;
    }
    
    public final String getQueryForListMethod(final String sqlMapNamespace, final String statementId, final String parameter) {
        this.configure();
        final String answer = MessageFormat.format(this.queryForListMethodTemplate, sqlMapNamespace, statementId, parameter);
        return answer;
    }
    
    public final String getQueryForObjectMethod(final String sqlMapNamespace, final String statementId, final String parameter) {
        this.configure();
        final String answer = MessageFormat.format(this.queryForObjectMethodTemplate, sqlMapNamespace, statementId, parameter);
        return answer;
    }
    
    public final FullyQualifiedJavaType getSuperClass() {
        this.configure();
        return this.superClass;
    }
    
    public final String getUpdateMethod(final String sqlMapNamespace, final String statementId, final String parameter) {
        this.configure();
        final String answer = MessageFormat.format(this.updateMethodTemplate, sqlMapNamespace, statementId, parameter);
        return answer;
    }
    
    public final List<FullyQualifiedJavaType> getCheckedExceptions() {
        this.configure();
        return this.checkedExceptions;
    }
    
    public final List<Field> getFieldClones(final CommentGenerator commentGenerator, final IntrospectedTable introspectedTable) {
        this.configure();
        final List<Field> answer = new ArrayList<Field>();
        for (final Field oldField : this.fields) {
            final Field field = new Field();
            field.setInitializationString(oldField.getInitializationString());
            field.setFinal(oldField.isFinal());
            field.setStatic(oldField.isStatic());
            field.setName(oldField.getName());
            field.setType(oldField.getType());
            field.setVisibility(oldField.getVisibility());
            commentGenerator.addFieldComment(field, introspectedTable);
            answer.add(field);
        }
        return answer;
    }
    
    public final List<Method> getMethodClones(final CommentGenerator commentGenerator, final IntrospectedTable introspectedTable) {
        this.configure();
        final List<Method> answer = new ArrayList<Method>();
        for (final Method oldMethod : this.methods) {
            final Method method = new Method();
            for (final String bodyLine : oldMethod.getBodyLines()) {
                method.addBodyLine(bodyLine);
            }
            for (final FullyQualifiedJavaType fqjt : oldMethod.getExceptions()) {
                method.addException(fqjt);
            }
            for (final Parameter parm : oldMethod.getParameters()) {
                method.addParameter(parm);
            }
            method.setConstructor(oldMethod.isConstructor());
            method.setFinal(oldMethod.isFinal());
            method.setStatic(oldMethod.isStatic());
            method.setName(oldMethod.getName());
            method.setReturnType(oldMethod.getReturnType());
            method.setVisibility(oldMethod.getVisibility());
            commentGenerator.addGeneralMethodComment(method, introspectedTable);
            answer.add(method);
        }
        return answer;
    }
    
    protected void setConstructorTemplate(final Method constructorTemplate) {
        this.constructorTemplate = constructorTemplate;
    }
    
    protected void setDeleteMethodTemplate(final String deleteMethodTemplate) {
        this.deleteMethodTemplate = deleteMethodTemplate;
    }
    
    protected void addField(final Field field) {
        this.fields.add(field);
    }
    
    protected void setInsertMethodTemplate(final String insertMethodTemplate) {
        this.insertMethodTemplate = insertMethodTemplate;
    }
    
    protected void addMethod(final Method method) {
        this.methods.add(method);
    }
    
    protected void setQueryForListMethodTemplate(final String queryForListMethodTemplate) {
        this.queryForListMethodTemplate = queryForListMethodTemplate;
    }
    
    protected void setQueryForObjectMethodTemplate(final String queryForObjectMethodTemplate) {
        this.queryForObjectMethodTemplate = queryForObjectMethodTemplate;
    }
    
    protected void setSuperClass(final FullyQualifiedJavaType superClass) {
        this.superClass = superClass;
    }
    
    protected void setUpdateMethodTemplate(final String updateMethodTemplate) {
        this.updateMethodTemplate = updateMethodTemplate;
    }
    
    protected void addInterfaceImport(final FullyQualifiedJavaType type) {
        this.interfaceImports.add(type);
    }
    
    protected void addImplementationImport(final FullyQualifiedJavaType type) {
        this.implementationImports.add(type);
    }
    
    protected void addCheckedException(final FullyQualifiedJavaType type) {
        this.checkedExceptions.add(type);
    }
    
    private void configure() {
        if (!this.configured) {
            this.configureCheckedExceptions();
            this.configureConstructorTemplate();
            this.configureDeleteMethodTemplate();
            this.configureFields();
            this.configureImplementationImports();
            this.configureInsertMethodTemplate();
            this.configureInterfaceImports();
            this.configureMethods();
            this.configureQueryForListMethodTemplate();
            this.configureQueryForObjectMethodTemplate();
            this.configureSuperClass();
            this.configureUpdateMethodTemplate();
            this.configured = true;
        }
    }
    
    protected void configureCheckedExceptions() {
    }
    
    protected void configureFields() {
    }
    
    protected void configureImplementationImports() {
    }
    
    protected void configureInterfaceImports() {
    }
    
    protected void configureMethods() {
    }
    
    protected void configureSuperClass() {
    }
    
    protected abstract void configureConstructorTemplate();
    
    protected abstract void configureInsertMethodTemplate();
    
    protected abstract void configureQueryForListMethodTemplate();
    
    protected abstract void configureQueryForObjectMethodTemplate();
    
    protected abstract void configureUpdateMethodTemplate();
    
    protected abstract void configureDeleteMethodTemplate();
}

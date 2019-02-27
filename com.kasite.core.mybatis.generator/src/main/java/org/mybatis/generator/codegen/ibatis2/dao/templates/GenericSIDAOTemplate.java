package org.mybatis.generator.codegen.ibatis2.dao.templates;

import org.mybatis.generator.api.dom.java.*;

public class GenericSIDAOTemplate extends AbstractDAOTemplate
{
    private FullyQualifiedJavaType sqlMapClientType;
    
    public GenericSIDAOTemplate() {
        this.sqlMapClientType = new FullyQualifiedJavaType("com.ibatis.sqlmap.client.SqlMapClient");
    }
    
    @Override
    protected void configureCheckedExceptions() {
        this.addCheckedException(new FullyQualifiedJavaType("java.sql.SQLException"));
    }
    
    @Override
    protected void configureConstructorTemplate() {
        final Method method = new Method();
        method.setConstructor(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addBodyLine("super();");
        this.setConstructorTemplate(method);
    }
    
    @Override
    protected void configureDeleteMethodTemplate() {
        this.setDeleteMethodTemplate("sqlMapClient.delete(\"{0}.{1}\", {2});");
    }
    
    @Override
    protected void configureFields() {
        final Field field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setType(this.sqlMapClientType);
        field.setName("sqlMapClient");
        this.addField(field);
    }
    
    @Override
    protected void configureImplementationImports() {
        this.addImplementationImport(this.sqlMapClientType);
    }
    
    @Override
    protected void configureInsertMethodTemplate() {
        this.setInsertMethodTemplate("sqlMapClient.insert(\"{0}.{1}\", {2});");
    }
    
    @Override
    protected void configureMethods() {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("setSqlMapClient");
        method.addParameter(new Parameter(this.sqlMapClientType, "sqlMapClient"));
        method.addBodyLine("this.sqlMapClient = sqlMapClient;");
        this.addMethod(method);
        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("getSqlMapClient");
        method.setReturnType(this.sqlMapClientType);
        method.addBodyLine("return sqlMapClient;");
        this.addMethod(method);
    }
    
    @Override
    protected void configureQueryForListMethodTemplate() {
        this.setQueryForListMethodTemplate("sqlMapClient.queryForList(\"{0}.{1}\", {2});");
    }
    
    @Override
    protected void configureQueryForObjectMethodTemplate() {
        this.setQueryForObjectMethodTemplate("sqlMapClient.queryForObject(\"{0}.{1}\", {2});");
    }
    
    @Override
    protected void configureUpdateMethodTemplate() {
        this.setUpdateMethodTemplate("sqlMapClient.update(\"{0}.{1}\", {2});");
    }
}

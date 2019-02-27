package org.mybatis.generator.codegen.ibatis2.dao.templates;

import org.mybatis.generator.api.dom.java.*;

public class IbatisDAOTemplate extends AbstractDAOTemplate
{
    private FullyQualifiedJavaType fqjt;
    
    public IbatisDAOTemplate() {
        this.fqjt = new FullyQualifiedJavaType("com.ibatis.dao.client.DaoManager");
    }
    
    @Override
    protected void configureConstructorTemplate() {
        final Method method = new Method();
        method.setConstructor(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(this.fqjt, "daoManager"));
        method.addBodyLine("super(daoManager);");
        this.setConstructorTemplate(method);
    }
    
    @Override
    protected void configureDeleteMethodTemplate() {
        this.setDeleteMethodTemplate("delete(\"{0}.{1}\", {2});");
    }
    
    @Override
    protected void configureImplementationImports() {
        this.addImplementationImport(this.fqjt);
    }
    
    @Override
    protected void configureInsertMethodTemplate() {
        this.setInsertMethodTemplate("insert(\"{0}.{1}\", {2});");
    }
    
    @Override
    protected void configureQueryForListMethodTemplate() {
        this.setQueryForListMethodTemplate("queryForList(\"{0}.{1}\", {2});");
    }
    
    @Override
    protected void configureQueryForObjectMethodTemplate() {
        this.setQueryForObjectMethodTemplate("queryForObject(\"{0}.{1}\", {2});");
    }
    
    @Override
    protected void configureSuperClass() {
        this.setSuperClass(new FullyQualifiedJavaType("com.ibatis.dao.client.template.SqlMapDaoTemplate"));
    }
    
    @Override
    protected void configureUpdateMethodTemplate() {
        this.setUpdateMethodTemplate("update(\"{0}.{1}\", {2});");
    }
}

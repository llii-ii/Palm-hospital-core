package org.mybatis.generator.codegen.ibatis2.dao.templates;

import org.mybatis.generator.api.dom.java.*;

public class SpringDAOTemplate extends AbstractDAOTemplate
{
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
        this.setDeleteMethodTemplate("getSqlMapClientTemplate().delete(\"{0}.{1}\", {2});");
    }
    
    @Override
    protected void configureInsertMethodTemplate() {
        this.setInsertMethodTemplate("getSqlMapClientTemplate().insert(\"{0}.{1}\", {2});");
    }
    
    @Override
    protected void configureQueryForListMethodTemplate() {
        this.setQueryForListMethodTemplate("getSqlMapClientTemplate().queryForList(\"{0}.{1}\", {2});");
    }
    
    @Override
    protected void configureQueryForObjectMethodTemplate() {
        this.setQueryForObjectMethodTemplate("getSqlMapClientTemplate().queryForObject(\"{0}.{1}\", {2});");
    }
    
    @Override
    protected void configureSuperClass() {
        this.setSuperClass(new FullyQualifiedJavaType("org.springframework.orm.ibatis.support.SqlMapClientDaoSupport"));
    }
    
    @Override
    protected void configureUpdateMethodTemplate() {
        this.setUpdateMethodTemplate("getSqlMapClientTemplate().update(\"{0}.{1}\", {2});");
    }
}

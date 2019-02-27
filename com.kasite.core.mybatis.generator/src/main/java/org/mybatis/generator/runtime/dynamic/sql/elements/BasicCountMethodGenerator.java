package org.mybatis.generator.runtime.dynamic.sql.elements;

import java.util.HashSet;
import java.util.Set;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.rules.Rules;

public class BasicCountMethodGenerator
  extends AbstractMethodGenerator
{
  private BasicCountMethodGenerator(Builder builder)
  {
    super(builder);
  }
  
  public MethodAndImports generateMethodAndImports()
  {
    if (!this.introspectedTable.getRules().generateCountByExample()) {
      return null;
    }
    Set<FullyQualifiedJavaType> imports = new HashSet();
    
    FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType("org.mybatis.dynamic.sql.select.render.SelectStatementProvider");
    FullyQualifiedJavaType adapter = new FullyQualifiedJavaType("org.mybatis.dynamic.sql.util.SqlProviderAdapter");
    FullyQualifiedJavaType annotation = new FullyQualifiedJavaType("org.apache.ibatis.annotations.SelectProvider");
    
    imports.add(parameterType);
    imports.add(adapter);
    imports.add(annotation);
    
    Method method = new Method("count");
    method.setReturnType(new FullyQualifiedJavaType("long"));
    method.addParameter(new Parameter(parameterType, "selectStatement"));
    this.context.getCommentGenerator().addGeneralMethodAnnotation(method, this.introspectedTable, imports);
    method.addAnnotation("@SelectProvider(type=SqlProviderAdapter.class, method=\"select\")");
    
    return MethodAndImports.withMethod(method)
      .withImports(imports)
      .build();
  }
  
  public boolean callPlugins(Method method, Interface interfaze)
  {
    return this.context.getPlugins().clientBasicCountMethodGenerated(method, interfaze, this.introspectedTable);
  }
  
  public static class Builder
    extends AbstractMethodGenerator.BaseBuilder<Builder, BasicCountMethodGenerator>
  {
    public Builder getThis()
    {
      return this;
    }
    
    public BasicCountMethodGenerator build()
    {
      return new BasicCountMethodGenerator(this);
    }
  }
}

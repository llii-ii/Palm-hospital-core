package org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated;

import org.mybatis.generator.codegen.mybatis3.javamapper.elements.*;
import org.mybatis.generator.api.dom.java.*;

public class AnnotatedCountByExampleMethodGenerator extends CountByExampleMethodGenerator
{
    @Override
    public void addMapperAnnotations(final Method method) {
        final FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(this.introspectedTable.getMyBatis3SqlProviderType());
        final StringBuilder sb = new StringBuilder();
        sb.append("@SelectProvider(type=");
        sb.append(fqjt.getShortName());
        sb.append(".class, method=\"");
        sb.append(this.introspectedTable.getCountByExampleStatementId());
        sb.append("\")");
        method.addAnnotation(sb.toString());
    }
    
    @Override
    public void addExtraImports(final Interface interfaze) {
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.SelectProvider"));
    }
}

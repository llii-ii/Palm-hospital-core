package org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated;

import org.mybatis.generator.codegen.mybatis3.javamapper.elements.*;
import org.mybatis.generator.config.*;
import org.mybatis.generator.api.dom.java.*;

public class AnnotatedInsertSelectiveMethodGenerator extends InsertSelectiveMethodGenerator
{
    @Override
    public void addMapperAnnotations(final Method method) {
        final FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(this.introspectedTable.getMyBatis3SqlProviderType());
        final StringBuilder sb = new StringBuilder();
        sb.append("@InsertProvider(type=");
        sb.append(fqjt.getShortName());
        sb.append(".class, method=\"");
        sb.append(this.introspectedTable.getInsertSelectiveStatementId());
        sb.append("\")");
        method.addAnnotation(sb.toString());
        final GeneratedKey gk = this.introspectedTable.getGeneratedKey();
        if (gk != null) {
            this.addGeneratedKeyAnnotation(method, gk);
        }
    }
    
    @Override
    public void addExtraImports(final Interface interfaze) {
        final GeneratedKey gk = this.introspectedTable.getGeneratedKey();
        if (gk != null) {
            this.addGeneratedKeyImports(interfaze, gk);
        }
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.InsertProvider"));
    }
}

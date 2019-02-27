package org.mybatis.generator.codegen.mybatis3.javamapper.elements.sqlprovider;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.*;
import java.util.*;

public class ProviderDeleteByExampleMethodGenerator extends AbstractJavaProviderMethodGenerator
{
    public ProviderDeleteByExampleMethodGenerator(final boolean useLegacyBuilder) {
        super(useLegacyBuilder);
    }
    
    @Override
    public void addClassElements(final TopLevelClass topLevelClass) {
        final Set<String> staticImports = new TreeSet<String>();
        final Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        if (this.useLegacyBuilder) {
            staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.BEGIN");
            staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.DELETE_FROM");
            staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SQL");
        }
        else {
            importedTypes.add(ProviderDeleteByExampleMethodGenerator.NEW_BUILDER_IMPORT);
        }
        final FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(this.introspectedTable.getExampleType());
        importedTypes.add(fqjt);
        final Method method = new Method(this.introspectedTable.getDeleteByExampleStatementId());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.addParameter(new Parameter(fqjt, "example"));
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        if (this.useLegacyBuilder) {
            method.addBodyLine("BEGIN();");
            method.addBodyLine(String.format("DELETE_FROM(\"%s\");", StringUtility.escapeStringForJava(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime())));
            method.addBodyLine("applyWhere(example, false);");
            method.addBodyLine("return SQL();");
        }
        else {
            method.addBodyLine("SQL sql = new SQL();");
            method.addBodyLine(String.format("sql.DELETE_FROM(\"%s\");", StringUtility.escapeStringForJava(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime())));
            method.addBodyLine("applyWhere(sql, example, false);");
            method.addBodyLine("return sql.toString();");
        }
        if (this.context.getPlugins().providerDeleteByExampleMethodGenerated(method, topLevelClass, this.introspectedTable)) {
            topLevelClass.addStaticImports(staticImports);
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }
}

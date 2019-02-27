package org.mybatis.generator.codegen.mybatis3.javamapper.elements.sqlprovider;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.*;
import java.util.*;

public class ProviderCountByExampleMethodGenerator extends AbstractJavaProviderMethodGenerator
{
    public ProviderCountByExampleMethodGenerator(final boolean useLegacyBuilder) {
        super(useLegacyBuilder);
    }
    
    @Override
    public void addClassElements(final TopLevelClass topLevelClass) {
        final Set<String> staticImports = new TreeSet<String>();
        final Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        if (this.useLegacyBuilder) {
            staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.BEGIN");
            staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.FROM");
            staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SELECT");
            staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SQL");
        }
        else {
            importedTypes.add(ProviderCountByExampleMethodGenerator.NEW_BUILDER_IMPORT);
        }
        final FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(this.introspectedTable.getExampleType());
        importedTypes.add(fqjt);
        final Method method = new Method(this.introspectedTable.getCountByExampleStatementId());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.addParameter(new Parameter(fqjt, "example"));
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        if (this.useLegacyBuilder) {
            method.addBodyLine("BEGIN();");
            method.addBodyLine("SELECT(\"count(*)\");");
            method.addBodyLine(String.format("FROM(\"%s\");", StringUtility.escapeStringForJava(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime())));
            method.addBodyLine("applyWhere(example, false);");
            method.addBodyLine("return SQL();");
        }
        else {
            method.addBodyLine("SQL sql = new SQL();");
            method.addBodyLine(String.format("sql.SELECT(\"count(*)\").FROM(\"%s\");", StringUtility.escapeStringForJava(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime())));
            method.addBodyLine("applyWhere(sql, example, false);");
            method.addBodyLine("return sql.toString();");
        }
        if (this.context.getPlugins().providerCountByExampleMethodGenerated(method, topLevelClass, this.introspectedTable)) {
            topLevelClass.addStaticImports(staticImports);
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }
}

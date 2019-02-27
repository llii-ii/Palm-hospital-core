package org.mybatis.generator.codegen.mybatis3.javamapper.elements.sqlprovider;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.mybatis3.*;
import org.mybatis.generator.internal.util.*;
import java.util.*;

public class ProviderSelectByExampleWithoutBLOBsMethodGenerator extends AbstractJavaProviderMethodGenerator
{
    public ProviderSelectByExampleWithoutBLOBsMethodGenerator(final boolean useLegacyBuilder) {
        super(useLegacyBuilder);
    }
    
    @Override
    public void addClassElements(final TopLevelClass topLevelClass) {
        final Set<String> staticImports = new TreeSet<String>();
        final Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        if (this.useLegacyBuilder) {
            staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.BEGIN");
            staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SELECT");
            staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SELECT_DISTINCT");
            staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.FROM");
            staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.ORDER_BY");
            staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SQL");
        }
        else {
            importedTypes.add(ProviderSelectByExampleWithoutBLOBsMethodGenerator.NEW_BUILDER_IMPORT);
        }
        final FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(this.introspectedTable.getExampleType());
        importedTypes.add(fqjt);
        final Method method = new Method(this.getMethodName());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.addParameter(new Parameter(fqjt, "example"));
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        if (this.useLegacyBuilder) {
            method.addBodyLine("BEGIN();");
        }
        else {
            method.addBodyLine("SQL sql = new SQL();");
        }
        boolean distinctCheck = true;
        for (final IntrospectedColumn introspectedColumn : this.getColumns()) {
            if (distinctCheck) {
                method.addBodyLine("if (example != null && example.isDistinct()) {");
                method.addBodyLine(String.format("%sSELECT_DISTINCT(\"%s\");", this.builderPrefix, StringUtility.escapeStringForJava(MyBatis3FormattingUtilities.getSelectListPhrase(introspectedColumn))));
                method.addBodyLine("} else {");
                method.addBodyLine(String.format("%sSELECT(\"%s\");", this.builderPrefix, StringUtility.escapeStringForJava(MyBatis3FormattingUtilities.getSelectListPhrase(introspectedColumn))));
                method.addBodyLine("}");
            }
            else {
                method.addBodyLine(String.format("%sSELECT(\"%s\");", this.builderPrefix, StringUtility.escapeStringForJava(MyBatis3FormattingUtilities.getSelectListPhrase(introspectedColumn))));
            }
            distinctCheck = false;
        }
        method.addBodyLine(String.format("%sFROM(\"%s\");", this.builderPrefix, StringUtility.escapeStringForJava(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime())));
        if (this.useLegacyBuilder) {
            method.addBodyLine("applyWhere(example, false);");
        }
        else {
            method.addBodyLine("applyWhere(sql, example, false);");
        }
        method.addBodyLine("");
        method.addBodyLine("if (example != null && example.getOrderByClause() != null) {");
        method.addBodyLine(String.format("%sORDER_BY(example.getOrderByClause());", this.builderPrefix));
        method.addBodyLine("}");
        method.addBodyLine("");
        if (this.useLegacyBuilder) {
            method.addBodyLine("return SQL();");
        }
        else {
            method.addBodyLine("return sql.toString();");
        }
        if (this.callPlugins(method, topLevelClass)) {
            topLevelClass.addStaticImports(staticImports);
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }
    
    public List<IntrospectedColumn> getColumns() {
        return this.introspectedTable.getNonBLOBColumns();
    }
    
    public String getMethodName() {
        return this.introspectedTable.getSelectByExampleStatementId();
    }
    
    public boolean callPlugins(final Method method, final TopLevelClass topLevelClass) {
        return this.context.getPlugins().providerSelectByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, this.introspectedTable);
    }
}

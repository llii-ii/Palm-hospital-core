package org.mybatis.generator.codegen.mybatis3.javamapper.elements.sqlprovider;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.mybatis3.*;
import java.util.*;

public class ProviderUpdateByExampleWithoutBLOBsMethodGenerator extends AbstractJavaProviderMethodGenerator
{
    public ProviderUpdateByExampleWithoutBLOBsMethodGenerator(final boolean useLegacyBuilder) {
        super(useLegacyBuilder);
    }
    
    @Override
    public void addClassElements(final TopLevelClass topLevelClass) {
        final Set<String> staticImports = new TreeSet<String>();
        final Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        if (this.useLegacyBuilder) {
            staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.BEGIN");
            staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.UPDATE");
            staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SET");
            staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SQL");
        }
        else {
            importedTypes.add(ProviderUpdateByExampleWithoutBLOBsMethodGenerator.NEW_BUILDER_IMPORT);
        }
        importedTypes.add(new FullyQualifiedJavaType("java.util.Map"));
        final Method method = new Method(this.getMethodName());
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(new FullyQualifiedJavaType("java.util.Map<java.lang.String, java.lang.Object>"), "parameter"));
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        if (this.useLegacyBuilder) {
            method.addBodyLine("BEGIN();");
        }
        else {
            method.addBodyLine("SQL sql = new SQL();");
        }
        method.addBodyLine(String.format("%sUPDATE(\"%s\");", this.builderPrefix, StringUtility.escapeStringForJava(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime())));
        method.addBodyLine("");
        for (final IntrospectedColumn introspectedColumn : ListUtilities.removeGeneratedAlwaysColumns(this.getColumns())) {
            final StringBuilder sb = new StringBuilder();
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            sb.insert(2, "record.");
            method.addBodyLine(String.format("%sSET(\"%s = %s\");", this.builderPrefix, StringUtility.escapeStringForJava(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn)), sb.toString()));
        }
        method.addBodyLine("");
        final FullyQualifiedJavaType example = new FullyQualifiedJavaType(this.introspectedTable.getExampleType());
        importedTypes.add(example);
        method.addBodyLine(String.format("%s example = (%s) parameter.get(\"example\");", example.getShortName(), example.getShortName()));
        if (this.useLegacyBuilder) {
            method.addBodyLine("applyWhere(example, true);");
            method.addBodyLine("return SQL();");
        }
        else {
            method.addBodyLine("applyWhere(sql, example, true);");
            method.addBodyLine("return sql.toString();");
        }
        if (this.callPlugins(method, topLevelClass)) {
            topLevelClass.addStaticImports(staticImports);
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }
    
    public String getMethodName() {
        return this.introspectedTable.getUpdateByExampleStatementId();
    }
    
    public List<IntrospectedColumn> getColumns() {
        return this.introspectedTable.getNonBLOBColumns();
    }
    
    public boolean callPlugins(final Method method, final TopLevelClass topLevelClass) {
        return this.context.getPlugins().providerUpdateByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, this.introspectedTable);
    }
}

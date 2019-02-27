package org.mybatis.generator.codegen.mybatis3.javamapper.elements.sqlprovider;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.codegen.mybatis3.*;
import java.util.*;

public class ProviderUpdateByPrimaryKeySelectiveMethodGenerator extends AbstractJavaProviderMethodGenerator
{
    public ProviderUpdateByPrimaryKeySelectiveMethodGenerator(final boolean useLegacyBuilder) {
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
            staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.WHERE");
        }
        else {
            importedTypes.add(ProviderUpdateByPrimaryKeySelectiveMethodGenerator.NEW_BUILDER_IMPORT);
        }
        final FullyQualifiedJavaType fqjt = this.introspectedTable.getRules().calculateAllFieldsClass();
        importedTypes.add(fqjt);
        final Method method = new Method(this.introspectedTable.getUpdateByPrimaryKeySelectiveStatementId());
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(fqjt, "record"));
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        if (this.useLegacyBuilder) {
            method.addBodyLine("BEGIN();");
        }
        else {
            method.addBodyLine("SQL sql = new SQL();");
        }
        method.addBodyLine(String.format("%sUPDATE(\"%s\");", this.builderPrefix, StringUtility.escapeStringForJava(this.introspectedTable.getFullyQualifiedTableNameAtRuntime())));
        method.addBodyLine("");
        for (final IntrospectedColumn introspectedColumn : ListUtilities.removeGeneratedAlwaysColumns(this.introspectedTable.getNonPrimaryKeyColumns())) {
            if (!introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
                method.addBodyLine(String.format("if (record.%s() != null) {", JavaBeansUtil.getGetterMethodName(introspectedColumn.getJavaProperty(), introspectedColumn.getFullyQualifiedJavaType())));
            }
            method.addBodyLine(String.format("%sSET(\"%s = %s\");", this.builderPrefix, StringUtility.escapeStringForJava(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn)), MyBatis3FormattingUtilities.getParameterClause(introspectedColumn)));
            if (!introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
                method.addBodyLine("}");
            }
            method.addBodyLine("");
        }
        for (final IntrospectedColumn introspectedColumn : this.introspectedTable.getPrimaryKeyColumns()) {
            method.addBodyLine(String.format("%sWHERE(\"%s = %s\");", this.builderPrefix, StringUtility.escapeStringForJava(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn)), MyBatis3FormattingUtilities.getParameterClause(introspectedColumn)));
        }
        method.addBodyLine("");
        if (this.useLegacyBuilder) {
            method.addBodyLine("return SQL();");
        }
        else {
            method.addBodyLine("return sql.toString();");
        }
        if (this.context.getPlugins().providerUpdateByPrimaryKeySelectiveMethodGenerated(method, topLevelClass, this.introspectedTable)) {
            topLevelClass.addStaticImports(staticImports);
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }
}

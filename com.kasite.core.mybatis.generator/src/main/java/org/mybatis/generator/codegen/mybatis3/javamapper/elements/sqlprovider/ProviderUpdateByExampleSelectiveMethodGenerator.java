package org.mybatis.generator.codegen.mybatis3.javamapper.elements.sqlprovider;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.codegen.mybatis3.*;
import java.util.*;

public class ProviderUpdateByExampleSelectiveMethodGenerator extends AbstractJavaProviderMethodGenerator
{
    public ProviderUpdateByExampleSelectiveMethodGenerator(final boolean useLegacyBuilder) {
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
            importedTypes.add(ProviderUpdateByExampleSelectiveMethodGenerator.NEW_BUILDER_IMPORT);
        }
        importedTypes.add(new FullyQualifiedJavaType("java.util.Map"));
        final Method method = new Method(this.introspectedTable.getUpdateByExampleSelectiveStatementId());
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(new FullyQualifiedJavaType("java.util.Map<java.lang.String, java.lang.Object>"), "parameter"));
        final FullyQualifiedJavaType record = this.introspectedTable.getRules().calculateAllFieldsClass();
        importedTypes.add(record);
        method.addBodyLine(String.format("%s record = (%s) parameter.get(\"record\");", record.getShortName(), record.getShortName()));
        final FullyQualifiedJavaType example = new FullyQualifiedJavaType(this.introspectedTable.getExampleType());
        importedTypes.add(example);
        method.addBodyLine(String.format("%s example = (%s) parameter.get(\"example\");", example.getShortName(), example.getShortName()));
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        method.addBodyLine("");
        if (this.useLegacyBuilder) {
            method.addBodyLine("BEGIN();");
        }
        else {
            method.addBodyLine("SQL sql = new SQL();");
        }
        method.addBodyLine(String.format("%sUPDATE(\"%s\");", this.builderPrefix, StringUtility.escapeStringForJava(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime())));
        method.addBodyLine("");
        for (final IntrospectedColumn introspectedColumn : ListUtilities.removeGeneratedAlwaysColumns(this.introspectedTable.getAllColumns())) {
            if (!introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
                method.addBodyLine(String.format("if (record.%s() != null) {", JavaBeansUtil.getGetterMethodName(introspectedColumn.getJavaProperty(), introspectedColumn.getFullyQualifiedJavaType())));
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            sb.insert(2, "record.");
            method.addBodyLine(String.format("%sSET(\"%s = %s\");", this.builderPrefix, StringUtility.escapeStringForJava(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn)), sb.toString()));
            if (!introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
                method.addBodyLine("}");
            }
            method.addBodyLine("");
        }
        if (this.useLegacyBuilder) {
            method.addBodyLine("applyWhere(example, true);");
            method.addBodyLine("return SQL();");
        }
        else {
            method.addBodyLine("applyWhere(sql, example, true);");
            method.addBodyLine("return sql.toString();");
        }
        if (this.context.getPlugins().providerUpdateByExampleSelectiveMethodGenerated(method, topLevelClass, this.introspectedTable)) {
            topLevelClass.addStaticImports(staticImports);
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }
}

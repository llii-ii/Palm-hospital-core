package org.mybatis.generator.codegen.mybatis3.javamapper.elements.sqlprovider;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.codegen.mybatis3.*;
import java.util.*;

public class ProviderInsertSelectiveMethodGenerator extends AbstractJavaProviderMethodGenerator
{
    public ProviderInsertSelectiveMethodGenerator(final boolean useLegacyBuilder) {
        super(useLegacyBuilder);
    }
    
    @Override
    public void addClassElements(final TopLevelClass topLevelClass) {
        final Set<String> staticImports = new TreeSet<String>();
        final Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        if (this.useLegacyBuilder) {
            staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.BEGIN");
            staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO");
            staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SQL");
            staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.VALUES");
        }
        else {
            importedTypes.add(ProviderInsertSelectiveMethodGenerator.NEW_BUILDER_IMPORT);
        }
        final FullyQualifiedJavaType fqjt = this.introspectedTable.getRules().calculateAllFieldsClass();
        importedTypes.add(fqjt);
        final Method method = new Method(this.introspectedTable.getInsertSelectiveStatementId());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.addParameter(new Parameter(fqjt, "record"));
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        if (this.useLegacyBuilder) {
            method.addBodyLine("BEGIN();");
        }
        else {
            method.addBodyLine("SQL sql = new SQL();");
        }
        method.addBodyLine(String.format("%sINSERT_INTO(\"%s\");", this.builderPrefix, StringUtility.escapeStringForJava(this.introspectedTable.getFullyQualifiedTableNameAtRuntime())));
        for (final IntrospectedColumn introspectedColumn : ListUtilities.removeIdentityAndGeneratedAlwaysColumns(this.introspectedTable.getAllColumns())) {
            method.addBodyLine("");
            if (!introspectedColumn.getFullyQualifiedJavaType().isPrimitive() && !introspectedColumn.isSequenceColumn()) {
                method.addBodyLine(String.format("if (record.%s() != null) {", JavaBeansUtil.getGetterMethodName(introspectedColumn.getJavaProperty(), introspectedColumn.getFullyQualifiedJavaType())));
            }
            method.addBodyLine(String.format("%sVALUES(\"%s\", \"%s\");", this.builderPrefix, StringUtility.escapeStringForJava(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn)), MyBatis3FormattingUtilities.getParameterClause(introspectedColumn)));
            if (!introspectedColumn.getFullyQualifiedJavaType().isPrimitive() && !introspectedColumn.isSequenceColumn()) {
                method.addBodyLine("}");
            }
        }
        method.addBodyLine("");
        if (this.useLegacyBuilder) {
            method.addBodyLine("return SQL();");
        }
        else {
            method.addBodyLine("return sql.toString();");
        }
        if (this.context.getPlugins().providerInsertSelectiveMethodGenerated(method, topLevelClass, this.introspectedTable)) {
            topLevelClass.addStaticImports(staticImports);
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }
}

package org.mybatis.generator.codegen.mybatis3.javamapper.elements.sqlprovider;

import org.mybatis.generator.api.dom.java.*;
import java.util.*;

public class ProviderApplyWhereMethodGenerator extends AbstractJavaProviderMethodGenerator
{
    private static final String[] BEGINNING_METHOD_LINES;
    private static final String[] LEGACY_ENDING_METHOD_LINES;
    private static final String[] ENDING_METHOD_LINES;
    
    public ProviderApplyWhereMethodGenerator(final boolean useLegacyBuilder) {
        super(useLegacyBuilder);
    }
    
    @Override
    public void addClassElements(final TopLevelClass topLevelClass) {
        final Set<String> staticImports = new TreeSet<String>();
        final Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        if (this.useLegacyBuilder) {
            staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.WHERE");
        }
        else {
            importedTypes.add(ProviderApplyWhereMethodGenerator.NEW_BUILDER_IMPORT);
        }
        importedTypes.add(new FullyQualifiedJavaType("java.util.List"));
        final FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(this.introspectedTable.getExampleType());
        importedTypes.add(fqjt);
        importedTypes.add(new FullyQualifiedJavaType(String.format("%s.Criteria", fqjt.getFullyQualifiedName())));
        importedTypes.add(new FullyQualifiedJavaType(String.format("%s.Criterion", fqjt.getFullyQualifiedName())));
        final Method method = new Method("applyWhere");
        method.setVisibility(JavaVisibility.PROTECTED);
        if (!this.useLegacyBuilder) {
            method.addParameter(new Parameter(ProviderApplyWhereMethodGenerator.NEW_BUILDER_IMPORT, "sql"));
        }
        method.addParameter(new Parameter(fqjt, "example"));
        method.addParameter(new Parameter(FullyQualifiedJavaType.getBooleanPrimitiveInstance(), "includeExamplePhrase"));
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        for (final String methodLine : ProviderApplyWhereMethodGenerator.BEGINNING_METHOD_LINES) {
            method.addBodyLine(methodLine);
        }
        if (this.useLegacyBuilder) {
            for (final String methodLine : ProviderApplyWhereMethodGenerator.LEGACY_ENDING_METHOD_LINES) {
                method.addBodyLine(methodLine);
            }
        }
        else {
            for (final String methodLine : ProviderApplyWhereMethodGenerator.ENDING_METHOD_LINES) {
                method.addBodyLine(methodLine);
            }
        }
        if (this.context.getPlugins().providerApplyWhereMethodGenerated(method, topLevelClass, this.introspectedTable)) {
            topLevelClass.addStaticImports(staticImports);
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }
    
    static {
        BEGINNING_METHOD_LINES = new String[] { "if (example == null) {", "return;", "}", "", "String parmPhrase1;", "String parmPhrase1_th;", "String parmPhrase2;", "String parmPhrase2_th;", "String parmPhrase3;", "String parmPhrase3_th;", "if (includeExamplePhrase) {", "parmPhrase1 = \"%s #{example.oredCriteria[%d].allCriteria[%d].value}\";", "parmPhrase1_th = \"%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}\";", "parmPhrase2 = \"%s #{example.oredCriteria[%d].allCriteria[%d].value} and #{example.oredCriteria[%d].criteria[%d].secondValue}\";", "parmPhrase2_th = \"%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{example.oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}\";", "parmPhrase3 = \"#{example.oredCriteria[%d].allCriteria[%d].value[%d]}\";", "parmPhrase3_th = \"#{example.oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}\";", "} else {", "parmPhrase1 = \"%s #{oredCriteria[%d].allCriteria[%d].value}\";", "parmPhrase1_th = \"%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}\";", "parmPhrase2 = \"%s #{oredCriteria[%d].allCriteria[%d].value} and #{oredCriteria[%d].criteria[%d].secondValue}\";", "parmPhrase2_th = \"%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}\";", "parmPhrase3 = \"#{oredCriteria[%d].allCriteria[%d].value[%d]}\";", "parmPhrase3_th = \"#{oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}\";", "}", "", "StringBuilder sb = new StringBuilder();", "List<Criteria> oredCriteria = example.getOredCriteria();", "boolean firstCriteria = true;", "for (int i = 0; i < oredCriteria.size(); i++) {", "Criteria criteria = oredCriteria.get(i);", "if (criteria.isValid()) {", "if (firstCriteria) {", "firstCriteria = false;", "} else {", "sb.append(\" or \");", "}", "", "sb.append('(');", "List<Criterion> criterions = criteria.getAllCriteria();", "boolean firstCriterion = true;", "for (int j = 0; j < criterions.size(); j++) {", "Criterion criterion = criterions.get(j);", "if (firstCriterion) {", "firstCriterion = false;", "} else {", "sb.append(\" and \");", "}", "", "if (criterion.isNoValue()) {", "sb.append(criterion.getCondition());", "} else if (criterion.isSingleValue()) {", "if (criterion.getTypeHandler() == null) {", "sb.append(String.format(parmPhrase1, criterion.getCondition(), i, j));", "} else {", "sb.append(String.format(parmPhrase1_th, criterion.getCondition(), i, j,criterion.getTypeHandler()));", "}", "} else if (criterion.isBetweenValue()) {", "if (criterion.getTypeHandler() == null) {", "sb.append(String.format(parmPhrase2, criterion.getCondition(), i, j, i, j));", "} else {", "sb.append(String.format(parmPhrase2_th, criterion.getCondition(), i, j, criterion.getTypeHandler(), i, j, criterion.getTypeHandler()));", "}", "} else if (criterion.isListValue()) {", "sb.append(criterion.getCondition());", "sb.append(\" (\");", "List<?> listItems = (List<?>) criterion.getValue();", "boolean comma = false;", "for (int k = 0; k < listItems.size(); k++) {", "if (comma) {", "sb.append(\", \");", "} else {", "comma = true;", "}", "if (criterion.getTypeHandler() == null) {", "sb.append(String.format(parmPhrase3, i, j, k));", "} else {", "sb.append(String.format(parmPhrase3_th, i, j, k, criterion.getTypeHandler()));", "}", "}", "sb.append(')');", "}", "}", "sb.append(')');", "}", "}", "" };
        LEGACY_ENDING_METHOD_LINES = new String[] { "if (sb.length() > 0) {", "WHERE(sb.toString());", "}" };
        ENDING_METHOD_LINES = new String[] { "if (sb.length() > 0) {", "sql.WHERE(sb.toString());", "}" };
    }
}

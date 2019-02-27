package org.mybatis.generator.plugins;

import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.ibatis2.*;
import java.util.*;
import java.io.*;

public class CaseInsensitiveLikePlugin extends PluginAdapter
{
    @Override
    public boolean validate(final List<String> warnings) {
        return true;
    }
    
    @Override
    public boolean modelExampleClassGenerated(final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        InnerClass criteria = null;
        for (final InnerClass innerClass : topLevelClass.getInnerClasses()) {
            if ("GeneratedCriteria".equals(innerClass.getType().getShortName())) {
                criteria = innerClass;
                break;
            }
        }
        if (criteria == null) {
            return true;
        }
        for (final IntrospectedColumn introspectedColumn : introspectedTable.getNonBLOBColumns()) {
            if (introspectedColumn.isJdbcCharacterColumn()) {
                if (!introspectedColumn.isStringColumn()) {
                    continue;
                }
                final Method method = new Method();
                method.setVisibility(JavaVisibility.PUBLIC);
                method.addParameter(new Parameter(introspectedColumn.getFullyQualifiedJavaType(), "value"));
                final StringBuilder sb = new StringBuilder();
                sb.append(introspectedColumn.getJavaProperty());
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
                sb.insert(0, "and");
                sb.append("LikeInsensitive");
                method.setName(sb.toString());
                method.setReturnType(FullyQualifiedJavaType.getCriteriaInstance());
                sb.setLength(0);
                sb.append("addCriterion(\"upper(");
                sb.append(Ibatis2FormattingUtilities.getAliasedActualColumnName(introspectedColumn));
                sb.append(") like\", value.toUpperCase(), \"");
                sb.append(introspectedColumn.getJavaProperty());
                sb.append("\");");
                method.addBodyLine(sb.toString());
                method.addBodyLine("return (Criteria) this;");
                criteria.addMethod(method);
            }
        }
        return true;
    }
}

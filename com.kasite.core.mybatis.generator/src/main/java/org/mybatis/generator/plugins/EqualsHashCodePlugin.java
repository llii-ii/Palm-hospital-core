package org.mybatis.generator.plugins;

import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.*;
import org.mybatis.generator.internal.util.*;
import java.util.*;
import java.io.*;

public class EqualsHashCodePlugin extends PluginAdapter
{
    private boolean useEqualsHashCodeFromRoot;
    
    @Override
    public void setProperties(final Properties properties) {
        super.setProperties(properties);
        this.useEqualsHashCodeFromRoot = StringUtility.isTrue(properties.getProperty("useEqualsHashCodeFromRoot"));
    }
    
    @Override
    public boolean validate(final List<String> warnings) {
        return true;
    }
    
    @Override
    public boolean modelBaseRecordClassGenerated(final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        List<IntrospectedColumn> columns;
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            columns = introspectedTable.getNonBLOBColumns();
        }
        else {
            columns = introspectedTable.getAllColumns();
        }
        this.generateEquals(topLevelClass, columns, introspectedTable);
        this.generateHashCode(topLevelClass, columns, introspectedTable);
        return true;
    }
    
    @Override
    public boolean modelPrimaryKeyClassGenerated(final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        this.generateEquals(topLevelClass, introspectedTable.getPrimaryKeyColumns(), introspectedTable);
        this.generateHashCode(topLevelClass, introspectedTable.getPrimaryKeyColumns(), introspectedTable);
        return true;
    }
    
    @Override
    public boolean modelRecordWithBLOBsClassGenerated(final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        this.generateEquals(topLevelClass, introspectedTable.getAllColumns(), introspectedTable);
        this.generateHashCode(topLevelClass, introspectedTable.getAllColumns(), introspectedTable);
        return true;
    }
    
    protected void generateEquals(final TopLevelClass topLevelClass, final List<IntrospectedColumn> introspectedColumns, final IntrospectedTable introspectedTable) {
        final Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());
        method.setName("equals");
        method.addParameter(new Parameter(FullyQualifiedJavaType.getObjectInstance(), "that"));
        if (introspectedTable.isJava5Targeted()) {
            method.addAnnotation("@Override");
        }
        if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3_DSQL) {
            this.context.getCommentGenerator().addGeneralMethodAnnotation(method, introspectedTable, topLevelClass.getImportedTypes());
        }
        else {
            this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        }
        method.addBodyLine("if (this == that) {");
        method.addBodyLine("return true;");
        method.addBodyLine("}");
        method.addBodyLine("if (that == null) {");
        method.addBodyLine("return false;");
        method.addBodyLine("}");
        method.addBodyLine("if (getClass() != that.getClass()) {");
        method.addBodyLine("return false;");
        method.addBodyLine("}");
        final StringBuilder sb = new StringBuilder();
        sb.append(topLevelClass.getType().getShortName());
        sb.append(" other = (");
        sb.append(topLevelClass.getType().getShortName());
        sb.append(") that;");
        method.addBodyLine(sb.toString());
        if (this.useEqualsHashCodeFromRoot && topLevelClass.getSuperClass() != null) {
            method.addBodyLine("if (!super.equals(other)) {");
            method.addBodyLine("return false;");
            method.addBodyLine("}");
        }
        boolean first = true;
        final Iterator<IntrospectedColumn> iter = introspectedColumns.iterator();
        while (iter.hasNext()) {
            final IntrospectedColumn introspectedColumn = iter.next();
            sb.setLength(0);
            if (first) {
                sb.append("return (");
                first = false;
            }
            else {
                OutputUtilities.javaIndent(sb, 1);
                sb.append("&& (");
            }
            final String getterMethod = JavaBeansUtil.getGetterMethodName(introspectedColumn.getJavaProperty(), introspectedColumn.getFullyQualifiedJavaType());
            if (introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
                sb.append("this.");
                sb.append(getterMethod);
                sb.append("() == ");
                sb.append("other.");
                sb.append(getterMethod);
                sb.append("())");
            }
            else if (introspectedColumn.getFullyQualifiedJavaType().isArray()) {
                topLevelClass.addImportedType("java.util.Arrays");
                sb.append("Arrays.equals(this.");
                sb.append(getterMethod);
                sb.append("(), ");
                sb.append("other.");
                sb.append(getterMethod);
                sb.append("()))");
            }
            else {
                sb.append("this.");
                sb.append(getterMethod);
                sb.append("() == null ? other.");
                sb.append(getterMethod);
                sb.append("() == null : this.");
                sb.append(getterMethod);
                sb.append("().equals(other.");
                sb.append(getterMethod);
                sb.append("()))");
            }
            if (!iter.hasNext()) {
                sb.append(';');
            }
            method.addBodyLine(sb.toString());
        }
        topLevelClass.addMethod(method);
    }
    
    protected void generateHashCode(final TopLevelClass topLevelClass, final List<IntrospectedColumn> introspectedColumns, final IntrospectedTable introspectedTable) {
        final Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName("hashCode");
        if (introspectedTable.isJava5Targeted()) {
            method.addAnnotation("@Override");
        }
        if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3_DSQL) {
            this.context.getCommentGenerator().addGeneralMethodAnnotation(method, introspectedTable, topLevelClass.getImportedTypes());
        }
        else {
            this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        }
        method.addBodyLine("final int prime = 31;");
        method.addBodyLine("int result = 1;");
        if (this.useEqualsHashCodeFromRoot && topLevelClass.getSuperClass() != null) {
            method.addBodyLine("result = prime * result + super.hashCode();");
        }
        final StringBuilder sb = new StringBuilder();
        boolean hasTemp = false;
        for (final IntrospectedColumn introspectedColumn : introspectedColumns) {
            final FullyQualifiedJavaType fqjt = introspectedColumn.getFullyQualifiedJavaType();
            final String getterMethod = JavaBeansUtil.getGetterMethodName(introspectedColumn.getJavaProperty(), fqjt);
            sb.setLength(0);
            if (fqjt.isPrimitive()) {
                if ("boolean".equals(fqjt.getFullyQualifiedName())) {
                    sb.append("result = prime * result + (");
                    sb.append(getterMethod);
                    sb.append("() ? 1231 : 1237);");
                    method.addBodyLine(sb.toString());
                }
                else if ("byte".equals(fqjt.getFullyQualifiedName())) {
                    sb.append("result = prime * result + ");
                    sb.append(getterMethod);
                    sb.append("();");
                    method.addBodyLine(sb.toString());
                }
                else if ("char".equals(fqjt.getFullyQualifiedName())) {
                    sb.append("result = prime * result + ");
                    sb.append(getterMethod);
                    sb.append("();");
                    method.addBodyLine(sb.toString());
                }
                else if ("double".equals(fqjt.getFullyQualifiedName())) {
                    if (!hasTemp) {
                        method.addBodyLine("long temp;");
                        hasTemp = true;
                    }
                    sb.append("temp = Double.doubleToLongBits(");
                    sb.append(getterMethod);
                    sb.append("());");
                    method.addBodyLine(sb.toString());
                    method.addBodyLine("result = prime * result + (int) (temp ^ (temp >>> 32));");
                }
                else if ("float".equals(fqjt.getFullyQualifiedName())) {
                    sb.append("result = prime * result + Float.floatToIntBits(");
                    sb.append(getterMethod);
                    sb.append("());");
                    method.addBodyLine(sb.toString());
                }
                else if ("int".equals(fqjt.getFullyQualifiedName())) {
                    sb.append("result = prime * result + ");
                    sb.append(getterMethod);
                    sb.append("();");
                    method.addBodyLine(sb.toString());
                }
                else if ("long".equals(fqjt.getFullyQualifiedName())) {
                    sb.append("result = prime * result + (int) (");
                    sb.append(getterMethod);
                    sb.append("() ^ (");
                    sb.append(getterMethod);
                    sb.append("() >>> 32));");
                    method.addBodyLine(sb.toString());
                }
                else {
                    if (!"short".equals(fqjt.getFullyQualifiedName())) {
                        continue;
                    }
                    sb.append("result = prime * result + ");
                    sb.append(getterMethod);
                    sb.append("();");
                    method.addBodyLine(sb.toString());
                }
            }
            else if (fqjt.isArray()) {
                sb.append("result = prime * result + (Arrays.hashCode(");
                sb.append(getterMethod);
                sb.append("()));");
                method.addBodyLine(sb.toString());
            }
            else {
                sb.append("result = prime * result + ((");
                sb.append(getterMethod);
                sb.append("() == null) ? 0 : ");
                sb.append(getterMethod);
                sb.append("().hashCode());");
                method.addBodyLine(sb.toString());
            }
        }
        method.addBodyLine("return result;");
        topLevelClass.addMethod(method);
    }
}

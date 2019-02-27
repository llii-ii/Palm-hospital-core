package org.mybatis.generator.internal.util;

import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import java.util.*;
import org.mybatis.generator.config.*;
import java.io.*;

public class JavaBeansUtil
{
    public static String getGetterMethodName(final String property, final FullyQualifiedJavaType fullyQualifiedJavaType) {
        final StringBuilder sb = new StringBuilder();
        sb.append(property);
        if (Character.isLowerCase(sb.charAt(0)) && (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1)))) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }
        if (fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getBooleanPrimitiveInstance())) {
            sb.insert(0, "is");
        }
        else {
            sb.insert(0, "get");
        }
        return sb.toString();
    }
    
    public static String getSetterMethodName(final String property) {
        final StringBuilder sb = new StringBuilder();
        sb.append(property);
        if (Character.isLowerCase(sb.charAt(0)) && (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1)))) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }
        sb.insert(0, "set");
        return sb.toString();
    }
    
    public static String getCamelCaseString(final String inputString, final boolean firstCharacterUppercase) {
        final StringBuilder sb = new StringBuilder();
        boolean nextUpperCase = false;
        for (int i = 0; i < inputString.length(); ++i) {
            final char c = inputString.charAt(i);
            switch (c) {
                case ' ':
                case '#':
                case '$':
                case '&':
                case '-':
                case '/':
                case '@':
                case '_': {
                    if (sb.length() > 0) {
                        nextUpperCase = true;
                        break;
                    }
                    break;
                }
                default: {
                    if (nextUpperCase) {
                        sb.append(Character.toUpperCase(c));
                        nextUpperCase = false;
                        break;
                    }
                    sb.append(Character.toLowerCase(c));
                    break;
                }
            }
        }
        if (firstCharacterUppercase) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }
        return sb.toString();
    }
    
    public static String getValidPropertyName(final String inputString) {
        String answer;
        if (inputString == null) {
            answer = null;
        }
        else if (inputString.length() < 2) {
            answer = inputString.toLowerCase(Locale.US);
        }
        else if (Character.isUpperCase(inputString.charAt(0)) && !Character.isUpperCase(inputString.charAt(1))) {
            answer = inputString.substring(0, 1).toLowerCase(Locale.US) + inputString.substring(1);
        }
        else {
            answer = inputString;
        }
        return answer;
    }
    
    public static Method getJavaBeansGetter(final IntrospectedColumn introspectedColumn, final Context context, final IntrospectedTable introspectedTable) {
        final FullyQualifiedJavaType fqjt = introspectedColumn.getFullyQualifiedJavaType();
        final String property = introspectedColumn.getJavaProperty();
        final Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(fqjt);
        method.setName(getGetterMethodName(property, fqjt));
        context.getCommentGenerator().addGetterComment(method, introspectedTable, introspectedColumn);
        final StringBuilder sb = new StringBuilder();
        sb.append("return ");
        sb.append(property);
        sb.append(';');
        method.addBodyLine(sb.toString());
        return method;
    }
    
    public static Field getJavaBeansField(final IntrospectedColumn introspectedColumn, final Context context, final IntrospectedTable introspectedTable) {
        final String property = introspectedColumn.getJavaProperty();
        FullyQualifiedJavaType fqjt =introspectedColumn.getFullyQualifiedJavaType();
        final Field field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setType(fqjt);
        field.setName(property);
        //时间TIMESTAMP转string
        if(introspectedColumn.getJdbcType()==93 ) {
        	introspectedColumn.setJdbcType(12);
        	 FullyQualifiedJavaType instance = new FullyQualifiedJavaType("java.lang.String");
             field.setType(instance);
             introspectedColumn.setFullyQualifiedJavaType(instance);
        }
        //主键ID
        if("ID".equals(introspectedColumn.getActualColumnName()) || introspectedColumn.isIdentity()) {
        	//Integer转Long
        	if(introspectedColumn.getJdbcType()==4 ) {
            	introspectedColumn.setJdbcType(-5);
            	 FullyQualifiedJavaType instance = new FullyQualifiedJavaType("java.lang.Long");
                 field.setType(instance);
                 introspectedColumn.setFullyQualifiedJavaType(instance);
            }
        }
        
        context.getCommentGenerator().addFieldComment(field, introspectedTable, introspectedColumn);
        return field;
    }
    
    public static Method getJavaBeansSetter(final IntrospectedColumn introspectedColumn, final Context context, final IntrospectedTable introspectedTable) {
        final FullyQualifiedJavaType fqjt = introspectedColumn.getFullyQualifiedJavaType();
        final String property = introspectedColumn.getJavaProperty();
        final Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(getSetterMethodName(property));
        method.addParameter(new Parameter(fqjt, property));
        context.getCommentGenerator().addSetterComment(method, introspectedTable, introspectedColumn);
        final StringBuilder sb = new StringBuilder();
        if (introspectedColumn.isStringColumn() && isTrimStringsEnabled(introspectedColumn)) {
            sb.append("this.");
            sb.append(property);
            sb.append(" = ");
            sb.append(property);
            sb.append(" == null ? null : ");
            sb.append(property);
            sb.append(".trim();");
            method.addBodyLine(sb.toString());
        }
        else {
            sb.append("this.");
            sb.append(property);
            sb.append(" = ");
            sb.append(property);
            sb.append(';');
            method.addBodyLine(sb.toString());
        }
        return method;
    }
    
    private static boolean isTrimStringsEnabled(final Context context) {
        final Properties properties = context.getJavaModelGeneratorConfiguration().getProperties();
        final boolean rc = StringUtility.isTrue(properties.getProperty("trimStrings"));
        return rc;
    }
    
    private static boolean isTrimStringsEnabled(final IntrospectedTable table) {
        final TableConfiguration tableConfiguration = table.getTableConfiguration();
        final String trimSpaces = tableConfiguration.getProperties().getProperty("trimStrings");
        if (trimSpaces != null) {
            return StringUtility.isTrue(trimSpaces);
        }
        return isTrimStringsEnabled(table.getContext());
    }
    
    private static boolean isTrimStringsEnabled(final IntrospectedColumn column) {
        final String trimSpaces = column.getProperties().getProperty("trimStrings");
        if (trimSpaces != null) {
            return StringUtility.isTrue(trimSpaces);
        }
        return isTrimStringsEnabled(column.getIntrospectedTable());
    }
}

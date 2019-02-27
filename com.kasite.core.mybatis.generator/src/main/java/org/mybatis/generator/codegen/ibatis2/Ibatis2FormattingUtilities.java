package org.mybatis.generator.codegen.ibatis2;

import org.mybatis.generator.api.*;
import org.mybatis.generator.internal.util.*;
import java.util.*;

public class Ibatis2FormattingUtilities
{
    public static String getEscapedColumnName(final IntrospectedColumn introspectedColumn) {
        final StringBuilder sb = new StringBuilder();
        sb.append(escapeStringForIbatis2(introspectedColumn.getActualColumnName()));
        if (introspectedColumn.isColumnNameDelimited()) {
            sb.insert(0, introspectedColumn.getContext().getBeginningDelimiter());
            sb.append(introspectedColumn.getContext().getEndingDelimiter());
        }
        return sb.toString();
    }
    
    public static String getAliasedEscapedColumnName(final IntrospectedColumn introspectedColumn) {
        if (StringUtility.stringHasValue(introspectedColumn.getTableAlias())) {
            final StringBuilder sb = new StringBuilder();
            sb.append(introspectedColumn.getTableAlias());
            sb.append('.');
            sb.append(getEscapedColumnName(introspectedColumn));
            return sb.toString();
        }
        return getEscapedColumnName(introspectedColumn);
    }
    
    public static String getParameterClause(final IntrospectedColumn introspectedColumn) {
        return getParameterClause(introspectedColumn, null);
    }
    
    public static String getParameterClause(final IntrospectedColumn introspectedColumn, final String prefix) {
        final StringBuilder sb = new StringBuilder();
        sb.append('#');
        sb.append(introspectedColumn.getJavaProperty(prefix));
        if (StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
            sb.append(",jdbcType=");
            sb.append(introspectedColumn.getJdbcTypeName());
            sb.append(",handler=");
            sb.append(introspectedColumn.getTypeHandler());
        }
        else {
            sb.append(':');
            sb.append(introspectedColumn.getJdbcTypeName());
        }
        sb.append('#');
        return sb.toString();
    }
    
    public static String getSelectListPhrase(final IntrospectedColumn introspectedColumn) {
        if (StringUtility.stringHasValue(introspectedColumn.getTableAlias())) {
            final StringBuilder sb = new StringBuilder();
            sb.append(getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" as ");
            if (introspectedColumn.isColumnNameDelimited()) {
                sb.append(introspectedColumn.getContext().getBeginningDelimiter());
            }
            sb.append(introspectedColumn.getTableAlias());
            sb.append('_');
            sb.append(escapeStringForIbatis2(introspectedColumn.getActualColumnName()));
            if (introspectedColumn.isColumnNameDelimited()) {
                sb.append(introspectedColumn.getContext().getEndingDelimiter());
            }
            return sb.toString();
        }
        return getEscapedColumnName(introspectedColumn);
    }
    
    public static String escapeStringForIbatis2(final String s) {
        final StringTokenizer st = new StringTokenizer(s, "$#", true);
        final StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens()) {
            final String token = st.nextToken();
            if ("$".equals(token)) {
                sb.append("$$");
            }
            else if ("#".equals(token)) {
                sb.append("##");
            }
            else {
                sb.append(token);
            }
        }
        return sb.toString();
    }
    
    public static String getAliasedActualColumnName(final IntrospectedColumn introspectedColumn) {
        final StringBuilder sb = new StringBuilder();
        if (StringUtility.stringHasValue(introspectedColumn.getTableAlias())) {
            sb.append(introspectedColumn.getTableAlias());
            sb.append('.');
        }
        if (introspectedColumn.isColumnNameDelimited()) {
            sb.append(StringUtility.escapeStringForJava(introspectedColumn.getContext().getBeginningDelimiter()));
        }
        sb.append(introspectedColumn.getActualColumnName());
        if (introspectedColumn.isColumnNameDelimited()) {
            sb.append(StringUtility.escapeStringForJava(introspectedColumn.getContext().getEndingDelimiter()));
        }
        return sb.toString();
    }
    
    public static String getRenamedColumnNameForResultMap(final IntrospectedColumn introspectedColumn) {
        if (StringUtility.stringHasValue(introspectedColumn.getTableAlias())) {
            final StringBuilder sb = new StringBuilder();
            sb.append(introspectedColumn.getTableAlias());
            sb.append('_');
            sb.append(introspectedColumn.getActualColumnName());
            return sb.toString();
        }
        return introspectedColumn.getActualColumnName();
    }
}

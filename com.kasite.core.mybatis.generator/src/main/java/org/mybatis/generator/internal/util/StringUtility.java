package org.mybatis.generator.internal.util;

import java.util.*;
import java.io.*;

public class StringUtility
{
    public static boolean stringHasValue(final String s) {
        return s != null && s.length() > 0;
    }
    
    public static String composeFullyQualifiedTableName(final String catalog, final String schema, final String tableName, final char separator) {
        final StringBuilder sb = new StringBuilder();
        if (stringHasValue(catalog)) {
            sb.append(catalog);
            sb.append(separator);
        }
        if (stringHasValue(schema)) {
            sb.append(schema);
            sb.append(separator);
        }
        else if (sb.length() > 0) {
            sb.append(separator);
        }
        sb.append(tableName);
        return sb.toString();
    }
    
    public static boolean stringContainsSpace(final String s) {
        return s != null && s.indexOf(32) != -1;
    }
    
    public static String escapeStringForJava(final String s) {
        final StringTokenizer st = new StringTokenizer(s, "\"", true);
        final StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens()) {
            final String token = st.nextToken();
            if ("\"".equals(token)) {
                sb.append("\\\"");
            }
            else {
                sb.append(token);
            }
        }
        return sb.toString();
    }
    
    public static String escapeStringForXml(final String s) {
        final StringTokenizer st = new StringTokenizer(s, "\"", true);
        final StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens()) {
            final String token = st.nextToken();
            if ("\"".equals(token)) {
                sb.append("&quot;");
            }
            else {
                sb.append(token);
            }
        }
        return sb.toString();
    }
    
    public static boolean isTrue(final String s) {
        return "true".equalsIgnoreCase(s);
    }
    
    public static boolean stringContainsSQLWildcard(final String s) {
        return s != null && (s.indexOf(37) != -1 || s.indexOf(95) != -1);
    }
}

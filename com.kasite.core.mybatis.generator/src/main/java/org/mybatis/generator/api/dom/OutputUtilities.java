package org.mybatis.generator.api.dom;

import org.mybatis.generator.api.dom.java.*;
import java.util.*;
import java.io.*;

public class OutputUtilities
{
    private static final String lineSeparator;
    
    public static void javaIndent(final StringBuilder sb, final int indentLevel) {
        for (int i = 0; i < indentLevel; ++i) {
            sb.append("    ");
        }
    }
    
    public static void xmlIndent(final StringBuilder sb, final int indentLevel) {
        for (int i = 0; i < indentLevel; ++i) {
            sb.append("  ");
        }
    }
    
    public static void newLine(final StringBuilder sb) {
        sb.append(OutputUtilities.lineSeparator);
    }
    
    public static Set<String> calculateImports(final Set<FullyQualifiedJavaType> importedTypes) {
        final StringBuilder sb = new StringBuilder();
        final Set<String> importStrings = new TreeSet<String>();
        for (final FullyQualifiedJavaType fqjt : importedTypes) {
            for (final String importString : fqjt.getImportList()) {
                sb.setLength(0);
                sb.append("import ");
                sb.append(importString);
                sb.append(';');
                importStrings.add(sb.toString());
            }
        }
        return importStrings;
    }
    
    static {
        String ls = System.getProperty("line.separator");
        if (ls == null) {
            ls = "\n";
        }
        lineSeparator = ls;
    }
}

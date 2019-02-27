package org.mybatis.generator.api.dom.java;

import org.mybatis.generator.api.dom.*;
import java.util.*;

public class InitializationBlock
{
    private boolean isStatic;
    private List<String> bodyLines;
    private List<String> javaDocLines;
    
    public InitializationBlock() {
        this(false);
    }
    
    public InitializationBlock(final boolean isStatic) {
        this.isStatic = isStatic;
        this.bodyLines = new ArrayList<String>();
        this.javaDocLines = new ArrayList<String>();
    }
    
    public boolean isStatic() {
        return this.isStatic;
    }
    
    public void setStatic(final boolean isStatic) {
        this.isStatic = isStatic;
    }
    
    public List<String> getBodyLines() {
        return this.bodyLines;
    }
    
    public void addBodyLine(final String line) {
        this.bodyLines.add(line);
    }
    
    public void addBodyLine(final int index, final String line) {
        this.bodyLines.add(index, line);
    }
    
    public void addBodyLines(final Collection<String> lines) {
        this.bodyLines.addAll(lines);
    }
    
    public void addBodyLines(final int index, final Collection<String> lines) {
        this.bodyLines.addAll(index, lines);
    }
    
    public List<String> getJavaDocLines() {
        return this.javaDocLines;
    }
    
    public void addJavaDocLine(final String javaDocLine) {
        this.javaDocLines.add(javaDocLine);
    }
    
    public String getFormattedContent(int indentLevel) {
        final StringBuilder sb = new StringBuilder();
        for (final String javaDocLine : this.javaDocLines) {
            OutputUtilities.javaIndent(sb, indentLevel);
            sb.append(javaDocLine);
            OutputUtilities.newLine(sb);
        }
        OutputUtilities.javaIndent(sb, indentLevel);
        if (this.isStatic) {
            sb.append("static ");
        }
        sb.append('{');
        ++indentLevel;
        final ListIterator<String> listIter = this.bodyLines.listIterator();
        while (listIter.hasNext()) {
            final String line = listIter.next();
            if (line.startsWith("}")) {
                --indentLevel;
            }
            OutputUtilities.newLine(sb);
            OutputUtilities.javaIndent(sb, indentLevel);
            sb.append(line);
            if ((line.endsWith("{") && !line.startsWith("switch")) || line.endsWith(":")) {
                ++indentLevel;
            }
            if (line.startsWith("break")) {
                if (listIter.hasNext()) {
                    final String nextLine = listIter.next();
                    if (nextLine.startsWith("}")) {
                        ++indentLevel;
                    }
                    listIter.previous();
                }
                --indentLevel;
            }
        }
        --indentLevel;
        OutputUtilities.newLine(sb);
        OutputUtilities.javaIndent(sb, indentLevel);
        sb.append('}');
        return sb.toString();
    }
}

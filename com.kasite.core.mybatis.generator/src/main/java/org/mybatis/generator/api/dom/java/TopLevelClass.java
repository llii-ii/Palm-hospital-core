package org.mybatis.generator.api.dom.java;

import org.mybatis.generator.api.dom.*;
import org.mybatis.generator.internal.util.*;
import java.util.*;

public class TopLevelClass extends InnerClass implements CompilationUnit
{
    private Set<FullyQualifiedJavaType> importedTypes;
    private Set<String> staticImports;
    private List<String> fileCommentLines;
    
    public TopLevelClass(final FullyQualifiedJavaType type) {
        super(type);
        this.importedTypes = new TreeSet<FullyQualifiedJavaType>();
        this.fileCommentLines = new ArrayList<String>();
        this.staticImports = new TreeSet<String>();
    }
    
    public TopLevelClass(final String typeName) {
        this(new FullyQualifiedJavaType(typeName));
    }
    
    @Override
    public Set<FullyQualifiedJavaType> getImportedTypes() {
        return this.importedTypes;
    }
    
    public void addImportedType(final String importedType) {
        this.addImportedType(new FullyQualifiedJavaType(importedType));
    }
    
    @Override
    public void addImportedType(final FullyQualifiedJavaType importedType) {
        if (importedType != null && importedType.isExplicitlyImported() && !importedType.getPackageName().equals(this.getType().getPackageName()) && !importedType.getShortName().equals(this.getType().getShortName())) {
            this.importedTypes.add(importedType);
        }
    }
    
    @Override
    public String getFormattedContent() {
        final StringBuilder sb = new StringBuilder();
        for (final String fileCommentLine : this.fileCommentLines) {
            sb.append(fileCommentLine);
            OutputUtilities.newLine(sb);
        }
        if (StringUtility.stringHasValue(this.getType().getPackageName())) {
            sb.append("package ");
            sb.append(this.getType().getPackageName());
            sb.append(';');
            OutputUtilities.newLine(sb);
            OutputUtilities.newLine(sb);
        }
        for (final String staticImport : this.staticImports) {
            sb.append("import static ");
            sb.append(staticImport);
            sb.append(';');
            OutputUtilities.newLine(sb);
        }
        if (this.staticImports.size() > 0) {
            OutputUtilities.newLine(sb);
        }
        final Set<String> importStrings = OutputUtilities.calculateImports(this.importedTypes);
        for (final String importString : importStrings) {
            sb.append(importString);
            OutputUtilities.newLine(sb);
        }
        if (importStrings.size() > 0) {
            OutputUtilities.newLine(sb);
        }
        sb.append(super.getFormattedContent(0, this));
        return sb.toString();
    }
    
    @Override
    public boolean isJavaInterface() {
        return false;
    }
    
    @Override
    public boolean isJavaEnumeration() {
        return false;
    }
    
    @Override
    public void addFileCommentLine(final String commentLine) {
        this.fileCommentLines.add(commentLine);
    }
    
    @Override
    public List<String> getFileCommentLines() {
        return this.fileCommentLines;
    }
    
    @Override
    public void addImportedTypes(final Set<FullyQualifiedJavaType> importedTypes) {
        this.importedTypes.addAll(importedTypes);
    }
    
    @Override
    public Set<String> getStaticImports() {
        return this.staticImports;
    }
    
    @Override
    public void addStaticImport(final String staticImport) {
        this.staticImports.add(staticImport);
    }
    
    @Override
    public void addStaticImports(final Set<String> staticImports) {
        this.staticImports.addAll(staticImports);
    }
}

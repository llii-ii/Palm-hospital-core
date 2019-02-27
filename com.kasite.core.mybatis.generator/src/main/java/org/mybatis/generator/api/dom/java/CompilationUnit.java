package org.mybatis.generator.api.dom.java;

import java.util.*;

public interface CompilationUnit
{
    String getFormattedContent();
    
    Set<FullyQualifiedJavaType> getImportedTypes();
    
    Set<String> getStaticImports();
    
    FullyQualifiedJavaType getSuperClass();
    
    boolean isJavaInterface();
    
    boolean isJavaEnumeration();
    
    Set<FullyQualifiedJavaType> getSuperInterfaceTypes();
    
    FullyQualifiedJavaType getType();
    
    void addImportedType(final FullyQualifiedJavaType p0);
    
    void addImportedTypes(final Set<FullyQualifiedJavaType> p0);
    
    void addStaticImport(final String p0);
    
    void addStaticImports(final Set<String> p0);
    
    void addFileCommentLine(final String p0);
    
    List<String> getFileCommentLines();
}

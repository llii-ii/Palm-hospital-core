package org.mybatis.generator.api.dom.java;

import java.util.*;

public class TypeParameter
{
    private String name;
    private List<FullyQualifiedJavaType> extendsTypes;
    
    public TypeParameter(final String name) {
        this(name, new ArrayList<FullyQualifiedJavaType>());
    }
    
    public TypeParameter(final String name, final List<FullyQualifiedJavaType> extendsTypes) {
        this.name = name;
        this.extendsTypes = extendsTypes;
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<FullyQualifiedJavaType> getExtendsTypes() {
        return this.extendsTypes;
    }
    
    public String getFormattedContent(final CompilationUnit compilationUnit) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        if (!this.extendsTypes.isEmpty()) {
            sb.append(" extends ");
            boolean addAnd = false;
            for (final FullyQualifiedJavaType type : this.extendsTypes) {
                if (addAnd) {
                    sb.append(" & ");
                }
                else {
                    addAnd = true;
                }
                sb.append(JavaDomUtils.calculateTypeName(compilationUnit, type));
            }
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return this.getFormattedContent(null);
    }
}

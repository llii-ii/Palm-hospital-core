package org.mybatis.generator.runtime.dynamic.sql.elements;

import org.mybatis.generator.api.dom.java.*;
import java.util.*;

public class MethodParts
{
    private List<String> annotations;
    private List<String> bodyLines;
    private Set<FullyQualifiedJavaType> imports;
    private List<Parameter> parameters;
    
    private MethodParts(final Builder builder) {
        this.imports = builder.imports;
        this.bodyLines = builder.bodyLines;
        this.parameters = builder.parameters;
        this.annotations = builder.annotations;
    }
    
    public Set<FullyQualifiedJavaType> getImports() {
        return this.imports;
    }
    
    public List<String> getAnnotations() {
        return this.annotations;
    }
    
    public List<String> getBodyLines() {
        return this.bodyLines;
    }
    
    public List<Parameter> getParameters() {
        return this.parameters;
    }
    
    public static class Builder
    {
        private List<String> bodyLines;
        private Set<FullyQualifiedJavaType> imports;
        private List<Parameter> parameters;
        private List<String> annotations;
        
        public Builder() {
            this.bodyLines = new ArrayList<String>();
            this.imports = new HashSet<FullyQualifiedJavaType>();
            this.parameters = new ArrayList<Parameter>();
            this.annotations = new ArrayList<String>();
        }
        
        public Builder withAnnotation(final String annotation) {
            this.annotations.add(annotation);
            return this;
        }
        
        public Builder withBodyLine(final String bodyLine) {
            this.bodyLines.add(bodyLine);
            return this;
        }
        
        public Builder withBodyLines(final List<String> bodyLines) {
            this.bodyLines.addAll(bodyLines);
            return this;
        }
        
        public Builder withImport(final FullyQualifiedJavaType importedType) {
            this.imports.add(importedType);
            return this;
        }
        
        public Builder withImports(final Set<FullyQualifiedJavaType> imports) {
            this.imports.addAll(imports);
            return this;
        }
        
        public Builder withParameter(final Parameter parameter) {
            this.parameters.add(parameter);
            return this;
        }
        
        public MethodParts build() {
            return new MethodParts(this);
        }
    }
}

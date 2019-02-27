package org.mybatis.generator.runtime.dynamic.sql.elements;

import org.mybatis.generator.api.dom.java.*;
import java.util.*;

public class MethodAndImports
{
    private Method method;
    private Set<FullyQualifiedJavaType> imports;
    private Set<String> staticImports;
    
    private MethodAndImports(final Builder builder) {
        this.method = builder.method;
        this.imports = builder.imports;
        this.staticImports = builder.staticImports;
    }
    
    public Method getMethod() {
        return this.method;
    }
    
    public Set<FullyQualifiedJavaType> getImports() {
        return this.imports;
    }
    
    public Set<String> getStaticImports() {
        return this.staticImports;
    }
    
    public static Builder withMethod(final Method method) {
        return new Builder().withMethod(method);
    }
    
    public static class Builder
    {
        private Method method;
        private Set<FullyQualifiedJavaType> imports;
        private Set<String> staticImports;
        
        public Builder() {
            this.imports = new HashSet<FullyQualifiedJavaType>();
            this.staticImports = new HashSet<String>();
        }
        
        public Builder withMethod(final Method method) {
            this.method = method;
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
        
        public Builder withStaticImport(final String staticImport) {
            this.staticImports.add(staticImport);
            return this;
        }
        
        public Builder withStaticImports(final Set<String> staticImports) {
            this.staticImports.addAll(staticImports);
            return this;
        }
        
        public MethodAndImports build() {
            return new MethodAndImports(this);
        }
    }
}

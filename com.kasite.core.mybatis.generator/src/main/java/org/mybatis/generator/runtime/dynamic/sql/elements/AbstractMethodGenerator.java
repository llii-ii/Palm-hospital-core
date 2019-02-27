package org.mybatis.generator.runtime.dynamic.sql.elements;

import org.mybatis.generator.config.*;
import org.mybatis.generator.api.*;
import java.util.*;
import org.mybatis.generator.api.dom.java.*;

public abstract class AbstractMethodGenerator
{
    protected Context context;
    protected IntrospectedTable introspectedTable;
    
    protected AbstractMethodGenerator(final BaseBuilder<?, ?> builder) {
        this.context = (builder).context;
        this.introspectedTable = (builder).introspectedTable;
    }
    
    protected void acceptParts(final MethodAndImports.Builder builder, final Method method, final MethodParts methodParts) {
        for (final Parameter parameter : methodParts.getParameters()) {
            method.addParameter(parameter);
        }
        for (final String annotation : methodParts.getAnnotations()) {
            method.addAnnotation(annotation);
        }
        method.addBodyLines(methodParts.getBodyLines());
        builder.withImports(methodParts.getImports());
    }
    
    public abstract MethodAndImports generateMethodAndImports();
    
    public abstract boolean callPlugins(final Method p0, final Interface p1);
    
    public abstract static class BaseBuilder<T extends BaseBuilder<T, R>, R>
    {
        private Context context;
        private IntrospectedTable introspectedTable;
        
        public T withContext(final Context context) {
            this.context = context;
            return this.getThis();
        }
        
        public T withIntrospectedTable(final IntrospectedTable introspectedTable) {
            this.introspectedTable = introspectedTable;
            return this.getThis();
        }
        
        public abstract T getThis();
        
        public abstract R build();
    }
}

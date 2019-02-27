package org.mybatis.generator.codegen;

import org.mybatis.generator.internal.util.*;
import java.util.*;
import org.mybatis.generator.api.dom.java.*;

public abstract class AbstractJavaGenerator extends AbstractGenerator
{
    public abstract List<CompilationUnit> getCompilationUnits();
    
    public static Method getGetter(final Field field) {
        final Method method = new Method();
        method.setName(JavaBeansUtil.getGetterMethodName(field.getName(), field.getType()));
        method.setReturnType(field.getType());
        method.setVisibility(JavaVisibility.PUBLIC);
        final StringBuilder sb = new StringBuilder();
        sb.append("return ");
        sb.append(field.getName());
        sb.append(';');
        method.addBodyLine(sb.toString());
        return method;
    }
    
    public String getRootClass() {
        String rootClass = this.introspectedTable.getTableConfigurationProperty("rootClass");
        if (rootClass == null) {
            final Properties properties = this.context.getJavaModelGeneratorConfiguration().getProperties();
            rootClass = properties.getProperty("rootClass");
        }
        return rootClass;
    }
    
    protected void addDefaultConstructor(final TopLevelClass topLevelClass) {
        topLevelClass.addMethod(this.getDefaultConstructor(topLevelClass));
    }
    
    protected Method getDefaultConstructor(final TopLevelClass topLevelClass) {
        final Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setConstructor(true);
        method.setName(topLevelClass.getType().getShortName());
        method.addBodyLine("super();");
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        return method;
    }
}

package org.mybatis.generator.api;

import org.mybatis.generator.api.dom.xml.*;
import java.util.*;
import org.mybatis.generator.api.dom.java.*;

public interface CommentGenerator
{
    void addConfigurationProperties(final Properties p0);
    
    void addFieldComment(final Field p0, final IntrospectedTable p1, final IntrospectedColumn p2);
    
    void addFieldComment(final Field p0, final IntrospectedTable p1);
    
    void addModelClassComment(final TopLevelClass p0, final IntrospectedTable p1);
    
    void addClassComment(final InnerClass p0, final IntrospectedTable p1);
    
    void addClassComment(final InnerClass p0, final IntrospectedTable p1, final boolean p2);
    
    void addEnumComment(final InnerEnum p0, final IntrospectedTable p1);
    
    void addGetterComment(final Method p0, final IntrospectedTable p1, final IntrospectedColumn p2);
    
    void addSetterComment(final Method p0, final IntrospectedTable p1, final IntrospectedColumn p2);
    
    void addGeneralMethodComment(final Method p0, final IntrospectedTable p1);
    
    void addJavaFileComment(final CompilationUnit p0);
    
    void addComment(final XmlElement p0);
    
    void addRootComment(final XmlElement p0);
    
    void addGeneralMethodAnnotation(final Method p0, final IntrospectedTable p1, final Set<FullyQualifiedJavaType> p2);
    
    void addGeneralMethodAnnotation(final Method p0, final IntrospectedTable p1, final IntrospectedColumn p2, final Set<FullyQualifiedJavaType> p3);
    
    void addFieldAnnotation(final Field p0, final IntrospectedTable p1, final Set<FullyQualifiedJavaType> p2);
    
    void addFieldAnnotation(final Field p0, final IntrospectedTable p1, final IntrospectedColumn p2, final Set<FullyQualifiedJavaType> p3);
    
    void addClassAnnotation(final InnerClass p0, final IntrospectedTable p1, final Set<FullyQualifiedJavaType> p2);
}

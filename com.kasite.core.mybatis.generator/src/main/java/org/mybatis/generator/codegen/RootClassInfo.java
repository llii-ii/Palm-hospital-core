package org.mybatis.generator.codegen;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.*;
import org.mybatis.generator.internal.util.messages.*;
import java.beans.*;
import org.mybatis.generator.api.*;
import java.util.*;

public class RootClassInfo
{
    private static Map<String, RootClassInfo> rootClassInfoMap;
    private PropertyDescriptor[] propertyDescriptors;
    private String className;
    private List<String> warnings;
    private boolean genericMode;
    
    public static RootClassInfo getInstance(final String className, final List<String> warnings) {
        RootClassInfo classInfo = RootClassInfo.rootClassInfoMap.get(className);
        if (classInfo == null) {
            classInfo = new RootClassInfo(className, warnings);
            RootClassInfo.rootClassInfoMap.put(className, classInfo);
        }
        return classInfo;
    }
    
    public static void reset() {
        RootClassInfo.rootClassInfoMap.clear();
    }
    
    private RootClassInfo(final String className, final List<String> warnings) {
        this.genericMode = false;
        this.className = className;
        this.warnings = warnings;
        if (className == null) {
            return;
        }
        final FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(className);
        final String nameWithoutGenerics = fqjt.getFullyQualifiedNameWithoutTypeParameters();
        if (!nameWithoutGenerics.equals(className)) {
            this.genericMode = true;
        }
        try {
            final Class<?> clazz = ObjectFactory.externalClassForName(nameWithoutGenerics);
            final BeanInfo bi = Introspector.getBeanInfo(clazz);
            this.propertyDescriptors = bi.getPropertyDescriptors();
        }
        catch (Exception e) {
            this.propertyDescriptors = null;
            warnings.add(Messages.getString("Warning.20", className));
        }
    }
    
    public boolean containsProperty(final IntrospectedColumn introspectedColumn) {
        if (this.propertyDescriptors == null) {
            return false;
        }
        boolean found = false;
        final String propertyName = introspectedColumn.getJavaProperty();
        final String propertyType = introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName();
        int i = 0;
        while (i < this.propertyDescriptors.length) {
            final PropertyDescriptor propertyDescriptor = this.propertyDescriptors[i];
            if (propertyDescriptor.getName().equals(propertyName)) {
                final String introspectedPropertyType = propertyDescriptor.getPropertyType().getName();
                if (this.genericMode && introspectedPropertyType.equals("java.lang.Object")) {
                    this.warnings.add(Messages.getString("Warning.28", propertyName, this.className));
                }
                else if (!introspectedPropertyType.equals(propertyType)) {
                    this.warnings.add(Messages.getString("Warning.21", propertyName, this.className, propertyType));
                    break;
                }
                if (propertyDescriptor.getReadMethod() == null) {
                    this.warnings.add(Messages.getString("Warning.22", propertyName, this.className));
                    break;
                }
                if (propertyDescriptor.getWriteMethod() == null) {
                    this.warnings.add(Messages.getString("Warning.23", propertyName, this.className));
                    break;
                }
                found = true;
                break;
            }
            else {
                ++i;
            }
        }
        return found;
    }
    
    static {
        RootClassInfo.rootClassInfoMap = Collections.synchronizedMap(new HashMap<String, RootClassInfo>());
    }
}

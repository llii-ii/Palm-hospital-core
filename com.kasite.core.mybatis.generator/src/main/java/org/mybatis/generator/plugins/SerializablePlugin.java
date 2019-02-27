package org.mybatis.generator.plugins;

import java.util.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;

public class SerializablePlugin extends PluginAdapter
{
    private FullyQualifiedJavaType serializable;
    private FullyQualifiedJavaType gwtSerializable;
    private boolean addGWTInterface;
    private boolean suppressJavaInterface;
    
    public SerializablePlugin() {
        this.serializable = new FullyQualifiedJavaType("java.io.Serializable");
        this.gwtSerializable = new FullyQualifiedJavaType("com.google.gwt.user.client.rpc.IsSerializable");
    }
    
    @Override
    public boolean validate(final List<String> warnings) {
        return true;
    }
    
    @Override
    public void setProperties(final Properties properties) {
        super.setProperties(properties);
        this.addGWTInterface = Boolean.valueOf(properties.getProperty("addGWTInterface"));
        this.suppressJavaInterface = Boolean.valueOf(properties.getProperty("suppressJavaInterface"));
    }
    
    @Override
    public boolean modelBaseRecordClassGenerated(final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        this.makeSerializable(topLevelClass, introspectedTable);
        return true;
    }
    
    @Override
    public boolean modelPrimaryKeyClassGenerated(final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        this.makeSerializable(topLevelClass, introspectedTable);
        return true;
    }
    
    @Override
    public boolean modelRecordWithBLOBsClassGenerated(final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        this.makeSerializable(topLevelClass, introspectedTable);
        return true;
    }
    
    protected void makeSerializable(final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        if (this.addGWTInterface) {
            topLevelClass.addImportedType(this.gwtSerializable);
            topLevelClass.addSuperInterface(this.gwtSerializable);
        }
        if (!this.suppressJavaInterface) {
            topLevelClass.addImportedType(this.serializable);
            topLevelClass.addSuperInterface(this.serializable);
            final Field field = new Field();
            field.setFinal(true);
            field.setInitializationString("1L");
            field.setName("serialVersionUID");
            field.setStatic(true);
            field.setType(new FullyQualifiedJavaType("long"));
            field.setVisibility(JavaVisibility.PRIVATE);
            if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3_DSQL) {
                this.context.getCommentGenerator().addFieldAnnotation(field, introspectedTable, topLevelClass.getImportedTypes());
            }
            else {
                this.context.getCommentGenerator().addFieldComment(field, introspectedTable);
            }
            topLevelClass.addField(field);
        }
    }
}

package org.mybatis.generator.codegen.mybatis3.javamapper;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.*;
import org.mybatis.generator.codegen.*;

public class SimpleAnnotatedClientGenerator extends SimpleJavaClientGenerator
{
    public SimpleAnnotatedClientGenerator() {
        super(false);
    }
    
    @Override
    protected void addDeleteByPrimaryKeyMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedDeleteByPrimaryKeyMethodGenerator(true);
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    protected void addInsertMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateInsert()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedInsertMethodGenerator(true);
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    protected void addSelectByPrimaryKeyMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateSelectByPrimaryKey()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedSelectByPrimaryKeyMethodGenerator(false, true);
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    protected void addSelectAllMethod(final Interface interfaze) {
        final AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedSelectAllMethodGenerator();
        this.initializeAndExecuteGenerator(methodGenerator, interfaze);
    }
    
    @Override
    protected void addUpdateByPrimaryKeyMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedUpdateByPrimaryKeyWithoutBLOBsMethodGenerator(true);
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    public AbstractXmlGenerator getMatchedXMLGenerator() {
        return null;
    }
}

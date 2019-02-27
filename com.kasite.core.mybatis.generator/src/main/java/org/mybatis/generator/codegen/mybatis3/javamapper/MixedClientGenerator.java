package org.mybatis.generator.codegen.mybatis3.javamapper;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.*;
import org.mybatis.generator.codegen.*;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.*;

public class MixedClientGenerator extends JavaMapperGenerator
{
    public MixedClientGenerator() {
        super(true);
    }
    
    @Override
    protected void addDeleteByPrimaryKeyMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedDeleteByPrimaryKeyMethodGenerator(false);
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    protected void addInsertMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateInsert()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedInsertMethodGenerator(false);
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    protected void addSelectByPrimaryKeyMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateSelectByPrimaryKey()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedSelectByPrimaryKeyMethodGenerator(true, false);
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    protected void addUpdateByPrimaryKeyWithBLOBsMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateUpdateByPrimaryKeyWithBLOBs()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedUpdateByPrimaryKeyWithBLOBsMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    protected void addUpdateByPrimaryKeyWithoutBLOBsMethod(final Interface interfaze) {
        if (this.introspectedTable.getRules().generateUpdateByPrimaryKeyWithoutBLOBs()) {
            final AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedUpdateByPrimaryKeyWithoutBLOBsMethodGenerator(false);
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    public AbstractXmlGenerator getMatchedXMLGenerator() {
        return new MixedMapperGenerator();
    }
}

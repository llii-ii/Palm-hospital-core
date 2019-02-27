package org.mybatis.generator.codegen.mybatis3;

import java.util.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.*;
import org.mybatis.generator.internal.*;
import org.mybatis.generator.codegen.mybatis3.model.*;
import org.mybatis.generator.codegen.*;

public class IntrospectedTableMyBatis3SimpleImpl extends IntrospectedTableMyBatis3Impl
{
    @Override
    protected void calculateXmlMapperGenerator(final AbstractJavaClientGenerator javaClientGenerator, final List<String> warnings, final ProgressCallback progressCallback) {
        if (javaClientGenerator == null) {
            if (this.context.getSqlMapGeneratorConfiguration() != null) {
                this.xmlMapperGenerator = new SimpleXMLMapperGenerator();
            }
        }
        else {
            this.xmlMapperGenerator = javaClientGenerator.getMatchedXMLGenerator();
        }
        this.initializeAbstractGenerator(this.xmlMapperGenerator, warnings, progressCallback);
    }
    
    @Override
    protected AbstractJavaClientGenerator createJavaClientGenerator() {
        if (this.context.getJavaClientGeneratorConfiguration() == null) {
            return null;
        }
        final String type = this.context.getJavaClientGeneratorConfiguration().getConfigurationType();
        AbstractJavaClientGenerator javaGenerator;
        if ("XMLMAPPER".equalsIgnoreCase(type)) {
            javaGenerator = new SimpleJavaClientGenerator();
        }
        else if ("ANNOTATEDMAPPER".equalsIgnoreCase(type)) {
            javaGenerator = new SimpleAnnotatedClientGenerator();
        }
        else if ("MAPPER".equalsIgnoreCase(type)) {
            javaGenerator = new SimpleJavaClientGenerator();
        }
        else {
            javaGenerator = (AbstractJavaClientGenerator)ObjectFactory.createInternalObject(type);
        }
        return javaGenerator;
    }
    
    @Override
    protected void calculateJavaModelGenerators(final List<String> warnings, final ProgressCallback progressCallback) {
        final AbstractJavaGenerator javaGenerator = new SimpleModelGenerator();
        this.initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
        this.javaModelGenerators.add(javaGenerator);
    }
}

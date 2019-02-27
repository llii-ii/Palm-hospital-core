package org.mybatis.generator.codegen.mybatis3;

import org.mybatis.generator.codegen.mybatis3.xmlmapper.*;
import org.mybatis.generator.codegen.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.*;
import org.mybatis.generator.internal.*;
import org.mybatis.generator.codegen.mybatis3.model.*;
import org.mybatis.generator.api.dom.java.*;
import java.util.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.xml.*;

public class IntrospectedTableMyBatis3Impl extends IntrospectedTable
{
    protected List<AbstractJavaGenerator> javaModelGenerators;
    protected List<AbstractJavaGenerator> clientGenerators;
    protected AbstractXmlGenerator xmlMapperGenerator;
    
    public IntrospectedTableMyBatis3Impl() {
        super(TargetRuntime.MYBATIS3);
        this.javaModelGenerators = new ArrayList<AbstractJavaGenerator>();
        this.clientGenerators = new ArrayList<AbstractJavaGenerator>();
    }
    
    @Override
    public void calculateGenerators(final List<String> warnings, final ProgressCallback progressCallback) {
        this.calculateJavaModelGenerators(warnings, progressCallback);
        final AbstractJavaClientGenerator javaClientGenerator = this.calculateClientGenerators(warnings, progressCallback);
        this.calculateXmlMapperGenerator(javaClientGenerator, warnings, progressCallback);
    }
    
    protected void calculateXmlMapperGenerator(final AbstractJavaClientGenerator javaClientGenerator, final List<String> warnings, final ProgressCallback progressCallback) {
        if (javaClientGenerator == null) {
            if (this.context.getSqlMapGeneratorConfiguration() != null) {
                this.xmlMapperGenerator = new XMLMapperGenerator();
            }
        }
        else {
            this.xmlMapperGenerator = javaClientGenerator.getMatchedXMLGenerator();
        }
        this.initializeAbstractGenerator(this.xmlMapperGenerator, warnings, progressCallback);
    }
    
    protected AbstractJavaClientGenerator calculateClientGenerators(final List<String> warnings, final ProgressCallback progressCallback) {
        if (!this.rules.generateJavaClient()) {
            return null;
        }
        final AbstractJavaClientGenerator javaGenerator = this.createJavaClientGenerator();
        if (javaGenerator == null) {
            return null;
        }
        this.initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
        this.clientGenerators.add(javaGenerator);
        return javaGenerator;
    }
    
    protected AbstractJavaClientGenerator createJavaClientGenerator() {
        if (this.context.getJavaClientGeneratorConfiguration() == null) {
            return null;
        }
        final String type = this.context.getJavaClientGeneratorConfiguration().getConfigurationType();
        AbstractJavaClientGenerator javaGenerator;
        if ("XMLMAPPER".equalsIgnoreCase(type)) {
            javaGenerator = new JavaMapperGenerator();
        }
        else if ("MIXEDMAPPER".equalsIgnoreCase(type)) {
            javaGenerator = new MixedClientGenerator();
        }
        else if ("ANNOTATEDMAPPER".equalsIgnoreCase(type)) {
            javaGenerator = new AnnotatedClientGenerator();
        }
        else if ("MAPPER".equalsIgnoreCase(type)) {
            javaGenerator = new JavaMapperGenerator();
        }
        else {
            javaGenerator = (AbstractJavaClientGenerator)ObjectFactory.createInternalObject(type);
        }
        return javaGenerator;
    }
    
    protected void calculateJavaModelGenerators(final List<String> warnings, final ProgressCallback progressCallback) {
        if (this.getRules().generateExampleClass()) {
            final AbstractJavaGenerator javaGenerator = new ExampleGenerator();
            this.initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
            this.javaModelGenerators.add(javaGenerator);
        }
        if (this.getRules().generatePrimaryKeyClass()) {
            final AbstractJavaGenerator javaGenerator = new PrimaryKeyGenerator();
            this.initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
            this.javaModelGenerators.add(javaGenerator);
        }
        if (this.getRules().generateBaseRecordClass()) {
            final AbstractJavaGenerator javaGenerator = new BaseRecordGenerator();
            this.initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
            this.javaModelGenerators.add(javaGenerator);
        }
        if (this.getRules().generateRecordWithBLOBsClass()) {
            final AbstractJavaGenerator javaGenerator = new RecordWithBLOBsGenerator();
            this.initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
            this.javaModelGenerators.add(javaGenerator);
        }
    }
    
    protected void initializeAbstractGenerator(final AbstractGenerator abstractGenerator, final List<String> warnings, final ProgressCallback progressCallback) {
        if (abstractGenerator == null) {
            return;
        }
        abstractGenerator.setContext(this.context);
        abstractGenerator.setIntrospectedTable(this);
        abstractGenerator.setProgressCallback(progressCallback);
        abstractGenerator.setWarnings(warnings);
    }
    
    /**
     * import
     */
    @Override
    public List<GeneratedJavaFile> getGeneratedJavaFiles() {
        final List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();
        for (final AbstractJavaGenerator javaGenerator : this.javaModelGenerators) {
            final List<CompilationUnit> compilationUnits = javaGenerator.getCompilationUnits();
            for (final CompilationUnit compilationUnit : compilationUnits) {
                final GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit, this.context.getJavaModelGeneratorConfiguration().getTargetProject(), this.context.getProperty("javaFileEncoding"), this.context.getJavaFormatter());
                answer.add(gjf);
            }
        }
        for (final AbstractJavaGenerator javaGenerator : this.clientGenerators) {
            final List<CompilationUnit> compilationUnits = javaGenerator.getCompilationUnits();
            for (final CompilationUnit compilationUnit : compilationUnits) {
                final GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit, this.context.getJavaClientGeneratorConfiguration().getTargetProject(), this.context.getProperty("javaFileEncoding"), this.context.getJavaFormatter());
                answer.add(gjf);
            }
        }
        return answer;
    }
    
    @Override
    public List<GeneratedXmlFile> getGeneratedXmlFiles() {
        final List<GeneratedXmlFile> answer = new ArrayList<GeneratedXmlFile>();
        if (this.xmlMapperGenerator != null) {
            final Document document = this.xmlMapperGenerator.getDocument();
            final GeneratedXmlFile gxf = new GeneratedXmlFile(document, this.getMyBatis3XmlMapperFileName(), this.getMyBatis3XmlMapperPackage(), this.context.getSqlMapGeneratorConfiguration().getTargetProject(), true, this.context.getXmlFormatter());
            if (this.context.getPlugins().sqlMapGenerated(gxf, this)) {
                answer.add(gxf);
            }
        }
        return answer;
    }
    
    @Override
    public int getGenerationSteps() {
        return this.javaModelGenerators.size() + this.clientGenerators.size() + ((this.xmlMapperGenerator != null) ? 1 : 0);
    }
    
    @Override
    public boolean isJava5Targeted() {
        return true;
    }
    
    @Override
    public boolean requiresXMLGenerator() {
        final AbstractJavaClientGenerator javaClientGenerator = this.createJavaClientGenerator();
        return javaClientGenerator != null && javaClientGenerator.requiresXMLGenerator();
    }
}

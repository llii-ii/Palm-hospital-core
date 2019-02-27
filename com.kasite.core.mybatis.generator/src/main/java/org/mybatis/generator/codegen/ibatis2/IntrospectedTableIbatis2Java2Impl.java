package org.mybatis.generator.codegen.ibatis2;

import org.mybatis.generator.codegen.ibatis2.sqlmap.*;
import org.mybatis.generator.codegen.*;
import org.mybatis.generator.codegen.ibatis2.dao.*;
import org.mybatis.generator.codegen.ibatis2.dao.templates.*;
import org.mybatis.generator.internal.*;
import org.mybatis.generator.codegen.ibatis2.model.*;
import org.mybatis.generator.api.dom.java.*;
import java.util.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.xml.*;

public class IntrospectedTableIbatis2Java2Impl extends IntrospectedTable
{
    protected List<AbstractJavaGenerator> javaModelGenerators;
    protected List<AbstractJavaGenerator> daoGenerators;
    protected AbstractXmlGenerator sqlMapGenerator;
    
    public IntrospectedTableIbatis2Java2Impl() {
        super(TargetRuntime.IBATIS2);
        this.javaModelGenerators = new ArrayList<AbstractJavaGenerator>();
        this.daoGenerators = new ArrayList<AbstractJavaGenerator>();
    }
    
    @Override
    public void calculateGenerators(final List<String> warnings, final ProgressCallback progressCallback) {
        this.calculateJavaModelGenerators(warnings, progressCallback);
        this.calculateDAOGenerators(warnings, progressCallback);
        this.calculateSqlMapGenerator(warnings, progressCallback);
    }
    
    protected void calculateSqlMapGenerator(final List<String> warnings, final ProgressCallback progressCallback) {
        this.initializeAbstractGenerator(this.sqlMapGenerator = new SqlMapGenerator(), warnings, progressCallback);
    }
    
    protected void calculateDAOGenerators(final List<String> warnings, final ProgressCallback progressCallback) {
        if (this.context.getJavaClientGeneratorConfiguration() == null) {
            return;
        }
        final String type = this.context.getJavaClientGeneratorConfiguration().getConfigurationType();
        AbstractJavaGenerator javaGenerator;
        if ("IBATIS".equalsIgnoreCase(type)) {
            javaGenerator = new DAOGenerator(new IbatisDAOTemplate(), this.isJava5Targeted());
        }
        else if ("SPRING".equalsIgnoreCase(type)) {
            javaGenerator = new DAOGenerator(new SpringDAOTemplate(), this.isJava5Targeted());
        }
        else if ("GENERIC-CI".equalsIgnoreCase(type)) {
            javaGenerator = new DAOGenerator(new GenericCIDAOTemplate(), this.isJava5Targeted());
        }
        else if ("GENERIC-SI".equalsIgnoreCase(type)) {
            javaGenerator = new DAOGenerator(new GenericSIDAOTemplate(), this.isJava5Targeted());
        }
        else {
            javaGenerator = (AbstractJavaGenerator)ObjectFactory.createInternalObject(type);
        }
        this.initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
        this.daoGenerators.add(javaGenerator);
    }
    
    protected void calculateJavaModelGenerators(final List<String> warnings, final ProgressCallback progressCallback) {
        if (this.getRules().generateExampleClass()) {
            final AbstractJavaGenerator javaGenerator = new ExampleGenerator(this.isJava5Targeted());
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
        abstractGenerator.setContext(this.context);
        abstractGenerator.setIntrospectedTable(this);
        abstractGenerator.setProgressCallback(progressCallback);
        abstractGenerator.setWarnings(warnings);
    }
    
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
        for (final AbstractJavaGenerator javaGenerator : this.daoGenerators) {
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
        final Document document = this.sqlMapGenerator.getDocument();
        final GeneratedXmlFile gxf = new GeneratedXmlFile(document, this.getIbatis2SqlMapFileName(), this.getIbatis2SqlMapPackage(), this.context.getSqlMapGeneratorConfiguration().getTargetProject(), true, this.context.getXmlFormatter());
        if (this.context.getPlugins().sqlMapGenerated(gxf, this)) {
            answer.add(gxf);
        }
        return answer;
    }
    
    @Override
    public boolean isJava5Targeted() {
        return false;
    }
    
    @Override
    public int getGenerationSteps() {
        return this.javaModelGenerators.size() + this.daoGenerators.size() + 1;
    }
    
    @Override
    public boolean requiresXMLGenerator() {
        return true;
    }
}

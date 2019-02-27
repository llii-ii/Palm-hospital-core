package org.mybatis.generator.runtime.dynamic.sql;

import org.mybatis.generator.internal.util.messages.*;
import java.util.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.runtime.dynamic.sql.elements.*;
import org.mybatis.generator.codegen.*;

public class DynamicSqlMapperGenerator extends AbstractJavaClientGenerator
{
    private FullyQualifiedJavaType recordType;
    private String resultMapId;
    private String tableFieldName;
    private FragmentGenerator fragmentGenerator;
    
    public DynamicSqlMapperGenerator() {
        super(false);
    }
    
    @Override
    public List<CompilationUnit> getCompilationUnits() {
        this.progressCallback.startTask(Messages.getString("Progress.17", this.introspectedTable.getFullyQualifiedTable().toString()));
        this.preCalculate();
        final Interface interfaze = this.createBasicInterface();
        final TopLevelClass supportClass = this.getSupportClass();
        final String staticImportString = supportClass.getType().getFullyQualifiedNameWithoutTypeParameters() + ".*";
        interfaze.addStaticImport(staticImportString);
        /*this.addBasicCountMethod(interfaze);
        this.addBasicDeleteMethod(interfaze);
        this.addBasicInsertMethod(interfaze);
        this.addBasicSelectOneMethod(interfaze);
        this.addBasicSelectManyMethod(interfaze);
        this.addBasicUpdateMethod(interfaze);
        this.addCountByExampleMethod(interfaze);
        this.addDeleteByExampleMethod(interfaze);
        this.addDeleteByPrimaryKeyMethod(interfaze);
        this.addInsertMethod(interfaze);
        this.addInsertSelectiveMethod(interfaze);
        this.addSelectByExampleMethod(interfaze);
        this.addSelectDistinctByExampleMethod(interfaze);
        this.addSelectByPrimaryKeyMethod(interfaze);
        this.addUpdateByExampleMethod(interfaze);
        this.addUpdateByExampleSelectiveMethod(interfaze);
        this.addUpdateByPrimaryKeyMethod(interfaze);
        this.addUpdateByPrimaryKeySelectiveMethod(interfaze);*/
        final List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        if (this.context.getPlugins().clientGenerated(interfaze, null, this.introspectedTable)) {
            answer.add(interfaze);
            answer.add(supportClass);
        }
        return answer;
    }
    
    private void preCalculate() {
        this.recordType = new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType());
        this.resultMapId = this.recordType.getShortNameWithoutTypeArguments() + "Result";
        this.tableFieldName = JavaBeansUtil.getValidPropertyName(this.introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        this.fragmentGenerator = new FragmentGenerator.Builder().withIntrospectedTable(this.introspectedTable).withResultMapId(this.resultMapId).build();
    }
    
    private Interface createBasicInterface() {
        final FullyQualifiedJavaType type = new FullyQualifiedJavaType(this.introspectedTable.getMyBatis3JavaMapperType());
        final Interface interfaze = new Interface(type);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        this.context.getCommentGenerator().addJavaFileComment(interfaze);
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));
        interfaze.addAnnotation("@Mapper");
        String rootInterface = this.introspectedTable.getTableConfigurationProperty("rootInterface");
        if (!StringUtility.stringHasValue(rootInterface)) {
            rootInterface = this.context.getJavaClientGeneratorConfiguration().getProperty("rootInterface");
        }
        if (StringUtility.stringHasValue(rootInterface)) {
            final FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(rootInterface);
            interfaze.addSuperInterface(fqjt);
            interfaze.addImportedType(fqjt);
        }
        return interfaze;
    }
    
    /*private void addBasicCountMethod(final Interface interfaze) {
        final BasicCountMethodGenerator generator = ((AbstractMethodGenerator.BaseBuilder<BasicCountMethodGenerator.Builder, R>)((AbstractMethodGenerator.BaseBuilder<BasicCountMethodGenerator.Builder, R>)new BasicCountMethodGenerator.Builder()).withContext(this.context)).withIntrospectedTable(this.introspectedTable).build();
        this.generate(interfaze, generator);
    }
    
    private void addBasicDeleteMethod(final Interface interfaze) {
        final BasicDeleteMethodGenerator generator = ((AbstractMethodGenerator.BaseBuilder<BasicDeleteMethodGenerator.Builder, R>)((AbstractMethodGenerator.BaseBuilder<BasicDeleteMethodGenerator.Builder, R>)new BasicDeleteMethodGenerator.Builder()).withContext(this.context)).withIntrospectedTable(this.introspectedTable).build();
        this.generate(interfaze, generator);
    }
    
    private void addBasicInsertMethod(final Interface interfaze) {
        final BasicInsertMethodGenerator generator = ((AbstractMethodGenerator.BaseBuilder<BasicInsertMethodGenerator.Builder, R>)((AbstractMethodGenerator.BaseBuilder<BasicInsertMethodGenerator.Builder, R>)new BasicInsertMethodGenerator.Builder()).withContext(this.context).withFragmentGenerator(this.fragmentGenerator)).withIntrospectedTable(this.introspectedTable).withRecordType(this.recordType).build();
        this.generate(interfaze, generator);
    }
    
    private void addBasicSelectOneMethod(final Interface interfaze) {
        final BasicSelectOneMethodGenerator generator = ((AbstractMethodGenerator.BaseBuilder<BasicSelectOneMethodGenerator.Builder, R>)((AbstractMethodGenerator.BaseBuilder<BasicSelectOneMethodGenerator.Builder, R>)new BasicSelectOneMethodGenerator.Builder()).withContext(this.context).withFragmentGenerator(this.fragmentGenerator)).withIntrospectedTable(this.introspectedTable).withRecordType(this.recordType).withResultMapId(this.resultMapId).build();
        this.generate(interfaze, generator);
    }
    
    private void addBasicSelectManyMethod(final Interface interfaze) {
        final BasicSelectManyMethodGenerator generator = ((AbstractMethodGenerator.BaseBuilder<BasicSelectManyMethodGenerator.Builder, R>)((AbstractMethodGenerator.BaseBuilder<BasicSelectManyMethodGenerator.Builder, R>)new BasicSelectManyMethodGenerator.Builder()).withContext(this.context).withFragmentGenerator(this.fragmentGenerator)).withIntrospectedTable(this.introspectedTable).withRecordType(this.recordType).build();
        this.generate(interfaze, generator);
    }
    
    private void addBasicUpdateMethod(final Interface interfaze) {
        final BasicUpdateMethodGenerator generator = ((AbstractMethodGenerator.BaseBuilder<BasicUpdateMethodGenerator.Builder, R>)((AbstractMethodGenerator.BaseBuilder<BasicUpdateMethodGenerator.Builder, R>)new BasicUpdateMethodGenerator.Builder()).withContext(this.context)).withIntrospectedTable(this.introspectedTable).build();
        this.generate(interfaze, generator);
    }
    
    private void addCountByExampleMethod(final Interface interfaze) {
        final CountByExampleMethodGenerator generator = ((AbstractMethodGenerator.BaseBuilder<CountByExampleMethodGenerator.Builder, R>)((AbstractMethodGenerator.BaseBuilder<CountByExampleMethodGenerator.Builder, R>)new CountByExampleMethodGenerator.Builder()).withContext(this.context)).withIntrospectedTable(this.introspectedTable).withTableFieldName(this.tableFieldName).build();
        this.generate(interfaze, generator);
    }
    
    private void addDeleteByExampleMethod(final Interface interfaze) {
        final DeleteByExampleMethodGenerator generator = ((AbstractMethodGenerator.BaseBuilder<DeleteByExampleMethodGenerator.Builder, R>)((AbstractMethodGenerator.BaseBuilder<DeleteByExampleMethodGenerator.Builder, R>)new DeleteByExampleMethodGenerator.Builder()).withContext(this.context)).withIntrospectedTable(this.introspectedTable).withTableFieldName(this.tableFieldName).build();
        this.generate(interfaze, generator);
    }
    
    private void addDeleteByPrimaryKeyMethod(final Interface interfaze) {
        final DeleteByPrimaryKeyMethodGenerator generator = ((AbstractMethodGenerator.BaseBuilder<DeleteByPrimaryKeyMethodGenerator.Builder, R>)((AbstractMethodGenerator.BaseBuilder<DeleteByPrimaryKeyMethodGenerator.Builder, R>)new DeleteByPrimaryKeyMethodGenerator.Builder()).withContext(this.context)).withIntrospectedTable(this.introspectedTable).withFragmentGenerator(this.fragmentGenerator).withTableFieldName(this.tableFieldName).build();
        this.generate(interfaze, generator);
    }
    
    private void addInsertMethod(final Interface interfaze) {
        final InsertMethodGenerator generator = ((AbstractMethodGenerator.BaseBuilder<InsertMethodGenerator.Builder, R>)((AbstractMethodGenerator.BaseBuilder<InsertMethodGenerator.Builder, R>)new InsertMethodGenerator.Builder()).withContext(this.context)).withIntrospectedTable(this.introspectedTable).withTableFieldName(this.tableFieldName).withRecordType(this.recordType).build();
        this.generate(interfaze, generator);
    }
    
    private void addInsertSelectiveMethod(final Interface interfaze) {
        final InsertSelectiveMethodGenerator generator = ((AbstractMethodGenerator.BaseBuilder<InsertSelectiveMethodGenerator.Builder, R>)((AbstractMethodGenerator.BaseBuilder<InsertSelectiveMethodGenerator.Builder, R>)new InsertSelectiveMethodGenerator.Builder()).withContext(this.context)).withIntrospectedTable(this.introspectedTable).withTableFieldName(this.tableFieldName).withRecordType(this.recordType).build();
        this.generate(interfaze, generator);
    }
    
    private void addSelectByExampleMethod(final Interface interfaze) {
        final SelectByExampleMethodGenerator generator = ((AbstractMethodGenerator.BaseBuilder<SelectByExampleMethodGenerator.Builder, R>)((AbstractMethodGenerator.BaseBuilder<SelectByExampleMethodGenerator.Builder, R>)new SelectByExampleMethodGenerator.Builder()).withContext(this.context).withFragmentGenerator(this.fragmentGenerator)).withIntrospectedTable(this.introspectedTable).withTableFieldName(this.tableFieldName).withRecordType(this.recordType).build();
        this.generate(interfaze, generator);
    }
    
    private void addSelectDistinctByExampleMethod(final Interface interfaze) {
        final SelectDistinctByExampleMethodGenerator generator = ((AbstractMethodGenerator.BaseBuilder<SelectDistinctByExampleMethodGenerator.Builder, R>)((AbstractMethodGenerator.BaseBuilder<SelectDistinctByExampleMethodGenerator.Builder, R>)new SelectDistinctByExampleMethodGenerator.Builder()).withContext(this.context).withFragmentGenerator(this.fragmentGenerator)).withIntrospectedTable(this.introspectedTable).withTableFieldName(this.tableFieldName).withRecordType(this.recordType).build();
        this.generate(interfaze, generator);
    }
    
    private void addSelectByPrimaryKeyMethod(final Interface interfaze) {
        final SelectByPrimaryKeyMethodGenerator generator = ((AbstractMethodGenerator.BaseBuilder<SelectByPrimaryKeyMethodGenerator.Builder, R>)((AbstractMethodGenerator.BaseBuilder<SelectByPrimaryKeyMethodGenerator.Builder, R>)new SelectByPrimaryKeyMethodGenerator.Builder()).withContext(this.context).withFragmentGenerator(this.fragmentGenerator)).withIntrospectedTable(this.introspectedTable).withTableFieldName(this.tableFieldName).withRecordType(this.recordType).build();
        this.generate(interfaze, generator);
    }
    
    private void addUpdateByExampleMethod(final Interface interfaze) {
        final UpdateByExampleMethodGenerator generator = ((AbstractMethodGenerator.BaseBuilder<UpdateByExampleMethodGenerator.Builder, R>)((AbstractMethodGenerator.BaseBuilder<UpdateByExampleMethodGenerator.Builder, R>)new UpdateByExampleMethodGenerator.Builder()).withContext(this.context).withFragmentGenerator(this.fragmentGenerator)).withIntrospectedTable(this.introspectedTable).withTableFieldName(this.tableFieldName).withRecordType(this.recordType).build();
        this.generate(interfaze, generator);
    }
    
    private void addUpdateByExampleSelectiveMethod(final Interface interfaze) {
        final UpdateByExampleSelectiveMethodGenerator generator = ((AbstractMethodGenerator.BaseBuilder<UpdateByExampleSelectiveMethodGenerator.Builder, R>)((AbstractMethodGenerator.BaseBuilder<UpdateByExampleSelectiveMethodGenerator.Builder, R>)new UpdateByExampleSelectiveMethodGenerator.Builder()).withContext(this.context).withFragmentGenerator(this.fragmentGenerator)).withIntrospectedTable(this.introspectedTable).withTableFieldName(this.tableFieldName).withRecordType(this.recordType).build();
        this.generate(interfaze, generator);
    }
    
    private void addUpdateByPrimaryKeyMethod(final Interface interfaze) {
        final UpdateByPrimaryKeyMethodGenerator generator = ((AbstractMethodGenerator.BaseBuilder<UpdateByPrimaryKeyMethodGenerator.Builder, R>)((AbstractMethodGenerator.BaseBuilder<UpdateByPrimaryKeyMethodGenerator.Builder, R>)new UpdateByPrimaryKeyMethodGenerator.Builder()).withContext(this.context).withFragmentGenerator(this.fragmentGenerator)).withIntrospectedTable(this.introspectedTable).withTableFieldName(this.tableFieldName).withRecordType(this.recordType).build();
        this.generate(interfaze, generator);
    }
    
    private void addUpdateByPrimaryKeySelectiveMethod(final Interface interfaze) {
        final UpdateByPrimaryKeySelectiveMethodGenerator generator = ((AbstractMethodGenerator.BaseBuilder<UpdateByPrimaryKeySelectiveMethodGenerator.Builder, R>)((AbstractMethodGenerator.BaseBuilder<UpdateByPrimaryKeySelectiveMethodGenerator.Builder, R>)new UpdateByPrimaryKeySelectiveMethodGenerator.Builder()).withContext(this.context).withFragmentGenerator(this.fragmentGenerator)).withIntrospectedTable(this.introspectedTable).withTableFieldName(this.tableFieldName).withRecordType(this.recordType).build();
        this.generate(interfaze, generator);
    }*/
    
    private TopLevelClass getSupportClass() {
        return DynamicSqlSupportClassGenerator.of(this.introspectedTable, this.context.getCommentGenerator()).generate();
    }
    
    private void generate(final Interface interfaze, final AbstractMethodGenerator generator) {
        final MethodAndImports mi = generator.generateMethodAndImports();
        if (mi != null && generator.callPlugins(mi.getMethod(), interfaze)) {
            interfaze.addMethod(mi.getMethod());
            interfaze.addImportedTypes(mi.getImports());
            interfaze.addStaticImports(mi.getStaticImports());
        }
    }
    
    @Override
    public AbstractXmlGenerator getMatchedXMLGenerator() {
        return null;
    }
}

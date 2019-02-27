package org.mybatis.generator.codegen.mybatis3.xmlmapper;

import org.mybatis.generator.codegen.*;
import org.mybatis.generator.internal.util.messages.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.*;
import org.mybatis.generator.api.dom.xml.*;

public class SimpleXMLMapperGenerator extends AbstractXmlGenerator
{
    protected XmlElement getSqlMapElement() {
        final FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();
        this.progressCallback.startTask(Messages.getString("Progress.12", table.toString()));
        final XmlElement answer = new XmlElement("mapper");
        final String namespace = this.introspectedTable.getMyBatis3SqlMapNamespace();
        answer.addAttribute(new Attribute("namespace", namespace));
        this.context.getCommentGenerator().addRootComment(answer);
        this.addResultMapElement(answer);
        this.addDeleteByPrimaryKeyElement(answer);
        this.addInsertElement(answer);
        this.addUpdateByPrimaryKeyElement(answer);
        this.addSelectByPrimaryKeyElement(answer);
        this.addSelectAllElement(answer);
        return answer;
    }
    
    protected void addResultMapElement(final XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateBaseResultMap()) {
            final AbstractXmlElementGenerator elementGenerator = new ResultMapWithoutBLOBsElementGenerator(true);
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addSelectByPrimaryKeyElement(final XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateSelectByPrimaryKey()) {
            final AbstractXmlElementGenerator elementGenerator = new SimpleSelectByPrimaryKeyElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addSelectAllElement(final XmlElement parentElement) {
        final AbstractXmlElementGenerator elementGenerator = new SimpleSelectAllElementGenerator();
        this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
    
    protected void addDeleteByPrimaryKeyElement(final XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            final AbstractXmlElementGenerator elementGenerator = new DeleteByPrimaryKeyElementGenerator(true);
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addInsertElement(final XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateInsert()) {
            final AbstractXmlElementGenerator elementGenerator = new InsertElementGenerator(true);
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addUpdateByPrimaryKeyElement(final XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            final AbstractXmlElementGenerator elementGenerator = new UpdateByPrimaryKeyWithoutBLOBsElementGenerator(true);
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void initializeAndExecuteGenerator(final AbstractXmlElementGenerator elementGenerator, final XmlElement parentElement) {
        elementGenerator.setContext(this.context);
        elementGenerator.setIntrospectedTable(this.introspectedTable);
        elementGenerator.setProgressCallback(this.progressCallback);
        elementGenerator.setWarnings(this.warnings);
        elementGenerator.addElements(parentElement);
    }
    
    @Override
    public Document getDocument() {
        Document document = new Document("-//mybatis.org//DTD Mapper 3.0//EN", "http://mybatis.org/dtd/mybatis-3-mapper.dtd");
        document.setRootElement(this.getSqlMapElement());
        if (!this.context.getPlugins().sqlMapDocumentGenerated(document, this.introspectedTable)) {
            document = null;
        }
        return document;
    }
}

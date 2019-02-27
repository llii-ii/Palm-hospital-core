package org.mybatis.generator.codegen.ibatis2.sqlmap;

import org.mybatis.generator.codegen.*;
import org.mybatis.generator.internal.util.messages.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.codegen.ibatis2.sqlmap.elements.*;
import org.mybatis.generator.api.dom.xml.*;

public class SqlMapGenerator extends AbstractXmlGenerator
{
    protected XmlElement getSqlMapElement() {
        final FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();
        this.progressCallback.startTask(Messages.getString("Progress.12", table.toString()));
        final XmlElement answer = new XmlElement("sqlMap");
        answer.addAttribute(new Attribute("namespace", this.introspectedTable.getIbatis2SqlMapNamespace()));
        this.context.getCommentGenerator().addRootComment(answer);
        this.addResultMapWithoutBLOBsElement(answer);
        this.addResultMapWithBLOBsElement(answer);
        this.addExampleWhereClauseElement(answer);
        this.addBaseColumnListElement(answer);
        this.addBlobColumnListElement(answer);
        this.addSelectByExampleWithBLOBsElement(answer);
        this.addSelectByExampleWithoutBLOBsElement(answer);
        this.addSelectByPrimaryKeyElement(answer);
        this.addDeleteByPrimaryKeyElement(answer);
        this.addDeleteByExampleElement(answer);
        this.addInsertElement(answer);
        this.addInsertSelectiveElement(answer);
        this.addCountByExampleElement(answer);
        this.addUpdateByExampleSelectiveElement(answer);
        this.addUpdateByExampleWithBLOBsElement(answer);
        this.addUpdateByExampleWithoutBLOBsElement(answer);
        this.addUpdateByPrimaryKeySelectiveElement(answer);
        this.addUpdateByPrimaryKeyWithBLOBsElement(answer);
        this.addUpdateByPrimaryKeyWithoutBLOBsElement(answer);
        return answer;
    }
    
    protected void addResultMapWithoutBLOBsElement(final XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateBaseResultMap()) {
            final AbstractXmlElementGenerator elementGenerator = new ResultMapWithoutBLOBsElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addResultMapWithBLOBsElement(final XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateResultMapWithBLOBs()) {
            final AbstractXmlElementGenerator elementGenerator = new ResultMapWithBLOBsElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addExampleWhereClauseElement(final XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateSQLExampleWhereClause()) {
            final AbstractXmlElementGenerator elementGenerator = new ExampleWhereClauseElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addBaseColumnListElement(final XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateBaseColumnList()) {
            final AbstractXmlElementGenerator elementGenerator = new BaseColumnListElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addBlobColumnListElement(final XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateBlobColumnList()) {
            final AbstractXmlElementGenerator elementGenerator = new BlobColumnListElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addSelectByExampleWithoutBLOBsElement(final XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateSelectByExampleWithoutBLOBs()) {
            final AbstractXmlElementGenerator elementGenerator = new SelectByExampleWithoutBLOBsElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addSelectByExampleWithBLOBsElement(final XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateSelectByExampleWithBLOBs()) {
            final AbstractXmlElementGenerator elementGenerator = new SelectByExampleWithBLOBsElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addSelectByPrimaryKeyElement(final XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateSelectByPrimaryKey()) {
            final AbstractXmlElementGenerator elementGenerator = new SelectByPrimaryKeyElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addDeleteByExampleElement(final XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateDeleteByExample()) {
            final AbstractXmlElementGenerator elementGenerator = new DeleteByExampleElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addDeleteByPrimaryKeyElement(final XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            final AbstractXmlElementGenerator elementGenerator = new DeleteByPrimaryKeyElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addInsertElement(final XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateInsert()) {
            final AbstractXmlElementGenerator elementGenerator = new InsertElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addInsertSelectiveElement(final XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateInsertSelective()) {
            final AbstractXmlElementGenerator elementGenerator = new InsertSelectiveElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addCountByExampleElement(final XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateCountByExample()) {
            final AbstractXmlElementGenerator elementGenerator = new CountByExampleElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addUpdateByExampleSelectiveElement(final XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateUpdateByExampleSelective()) {
            final AbstractXmlElementGenerator elementGenerator = new UpdateByExampleSelectiveElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addUpdateByExampleWithBLOBsElement(final XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateUpdateByExampleWithBLOBs()) {
            final AbstractXmlElementGenerator elementGenerator = new UpdateByExampleWithBLOBsElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addUpdateByExampleWithoutBLOBsElement(final XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateUpdateByExampleWithoutBLOBs()) {
            final AbstractXmlElementGenerator elementGenerator = new UpdateByExampleWithoutBLOBsElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addUpdateByPrimaryKeySelectiveElement(final XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            final AbstractXmlElementGenerator elementGenerator = new UpdateByPrimaryKeySelectiveElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addUpdateByPrimaryKeyWithBLOBsElement(final XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateUpdateByPrimaryKeyWithBLOBs()) {
            final AbstractXmlElementGenerator elementGenerator = new UpdateByPrimaryKeyWithBLOBsElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addUpdateByPrimaryKeyWithoutBLOBsElement(final XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateUpdateByPrimaryKeyWithoutBLOBs()) {
            final AbstractXmlElementGenerator elementGenerator = new UpdateByPrimaryKeyWithoutBLOBsElementGenerator();
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
        Document document = new Document("-//ibatis.apache.org//DTD SQL Map 2.0//EN", "http://ibatis.apache.org/dtd/sql-map-2.dtd");
        document.setRootElement(this.getSqlMapElement());
        if (!this.context.getPlugins().sqlMapDocumentGenerated(document, this.introspectedTable)) {
            document = null;
        }
        return document;
    }
}

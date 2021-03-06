package org.mybatis.generator.plugins;

import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.internal.util.messages.*;
import org.mybatis.generator.api.dom.xml.*;
import java.util.*;
import org.mybatis.generator.api.*;
import java.io.*;

public class SqlMapConfigPlugin extends PluginAdapter
{
    private List<String> sqlMapFiles;
    
    public SqlMapConfigPlugin() {
        this.sqlMapFiles = new ArrayList<String>();
    }
    
    @Override
    public boolean validate(final List<String> warnings) {
        boolean valid = true;
        if (!StringUtility.stringHasValue(this.properties.getProperty("targetProject"))) {
            warnings.add(Messages.getString("ValidationError.18", "SqlMapConfigPlugin", "targetProject"));
            valid = false;
        }
        if (!StringUtility.stringHasValue(this.properties.getProperty("targetPackage"))) {
            warnings.add(Messages.getString("ValidationError.18", "SqlMapConfigPlugin", "targetPackage"));
            valid = false;
        }
        return valid;
    }
    
    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles() {
        final Document document = new Document("-//ibatis.apache.org//DTD SQL Map Config 2.0//EN", "http://ibatis.apache.org/dtd/sql-map-config-2.dtd");
        final XmlElement root = new XmlElement("sqlMapConfig");
        document.setRootElement(root);
        root.addElement(new TextElement("<!--"));
        root.addElement(new TextElement("  This file is generated by MyBatis Generator."));
        root.addElement(new TextElement("  This file is the shell of an SqlMapConfig file - in many cases you will need to add"));
        root.addElement(new TextElement("    to this file before it is usable by iBATIS."));
        final StringBuilder sb = new StringBuilder();
        sb.append("  This file was generated on ");
        sb.append(new Date());
        sb.append('.');
        root.addElement(new TextElement(sb.toString()));
        root.addElement(new TextElement("-->"));
        final XmlElement settings = new XmlElement("settings");
        settings.addAttribute(new Attribute("useStatementNamespaces", "true"));
        root.addElement(settings);
        for (final String sqlMapFile : this.sqlMapFiles) {
            final XmlElement sqlMap = new XmlElement("sqlMap");
            sqlMap.addAttribute(new Attribute("resource", sqlMapFile));
            root.addElement(sqlMap);
        }
        final GeneratedXmlFile gxf = new GeneratedXmlFile(document, this.properties.getProperty("fileName", "SqlMapConfig.xml"), this.properties.getProperty("targetPackage"), this.properties.getProperty("targetProject"), false, this.context.getXmlFormatter());
        final List<GeneratedXmlFile> answer = new ArrayList<GeneratedXmlFile>(1);
        answer.add(gxf);
        return answer;
    }
    
    @Override
    public boolean sqlMapGenerated(final GeneratedXmlFile sqlMap, final IntrospectedTable introspectedTable) {
        final StringBuilder sb = new StringBuilder();
        sb.append(sqlMap.getTargetPackage());
        sb.append('.');
        final String temp = sb.toString();
        sb.setLength(0);
        sb.append(temp.replace('.', '/'));
        sb.append(sqlMap.getFileName());
        this.sqlMapFiles.add(sb.toString());
        return true;
    }
}

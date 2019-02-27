package org.mybatis.generator.config;

import org.mybatis.generator.api.dom.xml.*;
import java.util.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.internal.util.messages.*;

public class SqlMapGeneratorConfiguration extends PropertyHolder
{
    private String targetPackage;
    private String targetProject;
    
    public String getTargetProject() {
        return this.targetProject;
    }
    
    public void setTargetProject(final String targetProject) {
        this.targetProject = targetProject;
    }
    
    public String getTargetPackage() {
        return this.targetPackage;
    }
    
    public void setTargetPackage(final String targetPackage) {
        this.targetPackage = targetPackage;
    }
    
    public XmlElement toXmlElement() {
        final XmlElement answer = new XmlElement("sqlMapGenerator");
        if (this.targetPackage != null) {
            answer.addAttribute(new Attribute("targetPackage", this.targetPackage));
        }
        if (this.targetProject != null) {
            answer.addAttribute(new Attribute("targetProject", this.targetProject));
        }
        this.addPropertyXmlElements(answer);
        return answer;
    }
    
    public void validate(final List<String> errors, final String contextId) {
        if (!StringUtility.stringHasValue(this.targetProject)) {
            errors.add(Messages.getString("ValidationError.1", contextId));
        }
        if (!StringUtility.stringHasValue(this.targetPackage)) {
            errors.add(Messages.getString("ValidationError.12", "SQLMapGenerator", contextId));
        }
    }
}

package org.mybatis.generator.config;

import org.mybatis.generator.api.dom.xml.*;
import java.util.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.internal.util.messages.*;

public class JavaClientGeneratorConfiguration extends TypedPropertyHolder
{
    private String targetPackage;
    private String implementationPackage;
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
        final XmlElement answer = new XmlElement("javaClientGenerator");
        if (this.getConfigurationType() != null) {
            answer.addAttribute(new Attribute("type", this.getConfigurationType()));
        }
        if (this.targetPackage != null) {
            answer.addAttribute(new Attribute("targetPackage", this.targetPackage));
        }
        if (this.targetProject != null) {
            answer.addAttribute(new Attribute("targetProject", this.targetProject));
        }
        if (this.implementationPackage != null) {
            answer.addAttribute(new Attribute("implementationPackage", this.targetProject));
        }
        this.addPropertyXmlElements(answer);
        return answer;
    }
    
    public String getImplementationPackage() {
        return this.implementationPackage;
    }
    
    public void setImplementationPackage(final String implementationPackage) {
        this.implementationPackage = implementationPackage;
    }
    
    public void validate(final List<String> errors, final String contextId) {
        if (!StringUtility.stringHasValue(this.targetProject)) {
            errors.add(Messages.getString("ValidationError.2", contextId));
        }
        if (!StringUtility.stringHasValue(this.targetPackage)) {
            errors.add(Messages.getString("ValidationError.12", "javaClientGenerator", contextId));
        }
        if (!StringUtility.stringHasValue(this.getConfigurationType())) {
            errors.add(Messages.getString("ValidationError.20", contextId));
        }
    }
}

package org.mybatis.generator.config;

import org.mybatis.generator.api.dom.xml.*;

public class JavaTypeResolverConfiguration extends TypedPropertyHolder
{
    public XmlElement toXmlElement() {
        final XmlElement answer = new XmlElement("javaTypeResolver");
        if (this.getConfigurationType() != null) {
            answer.addAttribute(new Attribute("type", this.getConfigurationType()));
        }
        this.addPropertyXmlElements(answer);
        return answer;
    }
}

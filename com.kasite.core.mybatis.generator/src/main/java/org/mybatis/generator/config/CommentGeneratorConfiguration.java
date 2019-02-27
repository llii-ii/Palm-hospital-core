package org.mybatis.generator.config;

import org.mybatis.generator.api.dom.xml.*;

public class CommentGeneratorConfiguration extends TypedPropertyHolder
{
    public XmlElement toXmlElement() {
        final XmlElement answer = new XmlElement("commentGenerator");
        if (this.getConfigurationType() != null) {
            answer.addAttribute(new Attribute("type", this.getConfigurationType()));
        }
        this.addPropertyXmlElements(answer);
        return answer;
    }
}

package org.mybatis.generator.config;

import org.mybatis.generator.api.dom.xml.*;
import java.util.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.internal.util.messages.*;

public class PluginConfiguration extends TypedPropertyHolder
{
    public XmlElement toXmlElement() {
        final XmlElement answer = new XmlElement("plugin");
        if (this.getConfigurationType() != null) {
            answer.addAttribute(new Attribute("type", this.getConfigurationType()));
        }
        this.addPropertyXmlElements(answer);
        return answer;
    }
    
    public void validate(final List<String> errors, final String contextId) {
        if (!StringUtility.stringHasValue(this.getConfigurationType())) {
            errors.add(Messages.getString("ValidationError.17", contextId));
        }
    }
}

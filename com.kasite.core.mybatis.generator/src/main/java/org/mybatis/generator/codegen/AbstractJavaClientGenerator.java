package org.mybatis.generator.codegen;

public abstract class AbstractJavaClientGenerator extends AbstractJavaGenerator
{
    private boolean requiresXMLGenerator;
    
    public AbstractJavaClientGenerator(final boolean requiresXMLGenerator) {
        this.requiresXMLGenerator = requiresXMLGenerator;
    }
    
    public boolean requiresXMLGenerator() {
        return this.requiresXMLGenerator;
    }
    
    public abstract AbstractXmlGenerator getMatchedXMLGenerator();
}

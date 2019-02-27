package org.mybatis.generator.codegen.ibatis2.dao.elements;

import org.mybatis.generator.codegen.*;
import org.mybatis.generator.codegen.ibatis2.dao.templates.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.internal.*;
import org.mybatis.generator.internal.util.messages.*;

public abstract class AbstractDAOElementGenerator extends AbstractGenerator
{
    protected AbstractDAOTemplate daoTemplate;
    private DAOMethodNameCalculator daoMethodNameCalculator;
    private JavaVisibility exampleMethodVisibility;
    
    public abstract void addInterfaceElements(final Interface p0);
    
    public abstract void addImplementationElements(final TopLevelClass p0);
    
    public void setDAOTemplate(final AbstractDAOTemplate abstractDAOTemplate) {
        this.daoTemplate = abstractDAOTemplate;
    }
    
    public DAOMethodNameCalculator getDAOMethodNameCalculator() {
        if (this.daoMethodNameCalculator == null) {
            String type = this.context.getJavaClientGeneratorConfiguration().getProperty("methodNameCalculator");
            if (StringUtility.stringHasValue(type)) {
                if ("extended".equalsIgnoreCase(type)) {
                    type = ExtendedDAOMethodNameCalculator.class.getName();
                }
                else if ("default".equalsIgnoreCase(type)) {
                    type = DefaultDAOMethodNameCalculator.class.getName();
                }
            }
            else {
                type = DefaultDAOMethodNameCalculator.class.getName();
            }
            try {
                this.daoMethodNameCalculator = (DAOMethodNameCalculator)ObjectFactory.createInternalObject(type);
            }
            catch (Exception e) {
                this.daoMethodNameCalculator = new DefaultDAOMethodNameCalculator();
                this.warnings.add(Messages.getString("Warning.17", type, e.getMessage()));
            }
        }
        return this.daoMethodNameCalculator;
    }
    
    public JavaVisibility getExampleMethodVisibility() {
        if (this.exampleMethodVisibility == null) {
            final String type = this.context.getJavaClientGeneratorConfiguration().getProperty("exampleMethodVisibility");
            if (StringUtility.stringHasValue(type)) {
                if ("public".equalsIgnoreCase(type)) {
                    this.exampleMethodVisibility = JavaVisibility.PUBLIC;
                }
                else if ("private".equalsIgnoreCase(type)) {
                    this.exampleMethodVisibility = JavaVisibility.PRIVATE;
                }
                else if ("protected".equalsIgnoreCase(type)) {
                    this.exampleMethodVisibility = JavaVisibility.PROTECTED;
                }
                else if ("default".equalsIgnoreCase(type)) {
                    this.exampleMethodVisibility = JavaVisibility.DEFAULT;
                }
                else {
                    this.exampleMethodVisibility = JavaVisibility.PUBLIC;
                    this.warnings.add(Messages.getString("Warning.16", type));
                }
            }
            else {
                this.exampleMethodVisibility = JavaVisibility.PUBLIC;
            }
        }
        return this.exampleMethodVisibility;
    }
}

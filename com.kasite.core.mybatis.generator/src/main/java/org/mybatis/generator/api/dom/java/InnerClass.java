package org.mybatis.generator.api.dom.java;

import org.mybatis.generator.api.dom.*;
import java.util.*;

public class InnerClass extends JavaElement
{
    private List<Field> fields;
    private List<InnerClass> innerClasses;
    private List<InnerEnum> innerEnums;
    private List<TypeParameter> typeParameters;
    private FullyQualifiedJavaType superClass;
    private FullyQualifiedJavaType type;
    private Set<FullyQualifiedJavaType> superInterfaceTypes;
    private List<Method> methods;
    private boolean isAbstract;
    private List<InitializationBlock> initializationBlocks;
    
    public InnerClass(final FullyQualifiedJavaType type) {
        this.type = type;
        this.fields = new ArrayList<Field>();
        this.innerClasses = new ArrayList<InnerClass>();
        this.innerEnums = new ArrayList<InnerEnum>();
        this.typeParameters = new ArrayList<TypeParameter>();
        this.superInterfaceTypes = new HashSet<FullyQualifiedJavaType>();
        this.methods = new ArrayList<Method>();
        this.initializationBlocks = new ArrayList<InitializationBlock>();
    }
    
    public InnerClass(final String typeName) {
        this(new FullyQualifiedJavaType(typeName));
    }
    
    public List<Field> getFields() {
        return this.fields;
    }
    
    public void addField(final Field field) {
        this.fields.add(field);
    }
    
    public FullyQualifiedJavaType getSuperClass() {
        return this.superClass;
    }
    
    public void setSuperClass(final FullyQualifiedJavaType superClass) {
        this.superClass = superClass;
    }
    
    public void setSuperClass(final String superClassType) {
        this.superClass = new FullyQualifiedJavaType(superClassType);
    }
    
    public List<InnerClass> getInnerClasses() {
        return this.innerClasses;
    }
    
    public void addInnerClass(final InnerClass innerClass) {
        this.innerClasses.add(innerClass);
    }
    
    public List<InnerEnum> getInnerEnums() {
        return this.innerEnums;
    }
    
    public void addInnerEnum(final InnerEnum innerEnum) {
        this.innerEnums.add(innerEnum);
    }
    
    public List<TypeParameter> getTypeParameters() {
        return this.typeParameters;
    }
    
    public void addTypeParameter(final TypeParameter typeParameter) {
        this.typeParameters.add(typeParameter);
    }
    
    public List<InitializationBlock> getInitializationBlocks() {
        return this.initializationBlocks;
    }
    
    public void addInitializationBlock(final InitializationBlock initializationBlock) {
        this.initializationBlocks.add(initializationBlock);
    }
    
    public String getFormattedContent(int indentLevel, final CompilationUnit compilationUnit) {
        final StringBuilder sb = new StringBuilder();
        this.addFormattedJavadoc(sb, indentLevel);
        this.addFormattedAnnotations(sb, indentLevel);
        OutputUtilities.javaIndent(sb, indentLevel);
        sb.append(this.getVisibility().getValue());
        if (this.isAbstract()) {
            sb.append("abstract ");
        }
        if (this.isStatic()) {
            sb.append("static ");
        }
        if (this.isFinal()) {
            sb.append("final ");
        }
        sb.append("class ");
        sb.append(this.getType().getShortName());
        if (!this.getTypeParameters().isEmpty()) {
            boolean comma = false;
            sb.append("<");
            for (final TypeParameter typeParameter : this.typeParameters) {
                if (comma) {
                    sb.append(", ");
                }
                sb.append(typeParameter.getFormattedContent(compilationUnit));
                comma = true;
            }
            sb.append("> ");
        }
        if (this.superClass != null) {
            sb.append(" extends ");
            sb.append(JavaDomUtils.calculateTypeName(compilationUnit, this.superClass));
        }
        if (this.superInterfaceTypes.size() > 0) {
            sb.append(" implements ");
            boolean comma = false;
            for (final FullyQualifiedJavaType fqjt : this.superInterfaceTypes) {
                if (comma) {
                    sb.append(", ");
                }
                else {
                    comma = true;
                }
                sb.append(JavaDomUtils.calculateTypeName(compilationUnit, fqjt));
            }
        }
        sb.append(" {");
        ++indentLevel;
        final Iterator<Field> fldIter = this.fields.iterator();
        while (fldIter.hasNext()) {
            OutputUtilities.newLine(sb);
            final Field field = fldIter.next();
            sb.append(field.getFormattedContent(indentLevel, compilationUnit));
            if (fldIter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }
        if (this.initializationBlocks.size() > 0) {
            OutputUtilities.newLine(sb);
        }
        final Iterator<InitializationBlock> blkIter = this.initializationBlocks.iterator();
        while (blkIter.hasNext()) {
            OutputUtilities.newLine(sb);
            final InitializationBlock initializationBlock = blkIter.next();
            sb.append(initializationBlock.getFormattedContent(indentLevel));
            if (blkIter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }
        if (this.methods.size() > 0) {
            OutputUtilities.newLine(sb);
        }
        final Iterator<Method> mtdIter = this.methods.iterator();
        while (mtdIter.hasNext()) {
            OutputUtilities.newLine(sb);
            final Method method = mtdIter.next();
            sb.append(method.getFormattedContent(indentLevel, false, compilationUnit));
            if (mtdIter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }
        if (this.innerClasses.size() > 0) {
            OutputUtilities.newLine(sb);
        }
        final Iterator<InnerClass> icIter = this.innerClasses.iterator();
        while (icIter.hasNext()) {
            OutputUtilities.newLine(sb);
            final InnerClass innerClass = icIter.next();
            sb.append(innerClass.getFormattedContent(indentLevel, compilationUnit));
            if (icIter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }
        if (this.innerEnums.size() > 0) {
            OutputUtilities.newLine(sb);
        }
        final Iterator<InnerEnum> ieIter = this.innerEnums.iterator();
        while (ieIter.hasNext()) {
            OutputUtilities.newLine(sb);
            final InnerEnum innerEnum = ieIter.next();
            sb.append(innerEnum.getFormattedContent(indentLevel, compilationUnit));
            if (ieIter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }
        --indentLevel;
        OutputUtilities.newLine(sb);
        OutputUtilities.javaIndent(sb, indentLevel);
        sb.append('}');
        return sb.toString();
    }
    
    public Set<FullyQualifiedJavaType> getSuperInterfaceTypes() {
        return this.superInterfaceTypes;
    }
    
    public void addSuperInterface(final FullyQualifiedJavaType superInterface) {
        this.superInterfaceTypes.add(superInterface);
    }
    
    public List<Method> getMethods() {
        return this.methods;
    }
    
    public void addMethod(final Method method) {
        this.methods.add(method);
    }
    
    public FullyQualifiedJavaType getType() {
        return this.type;
    }
    
    public boolean isAbstract() {
        return this.isAbstract;
    }
    
    public void setAbstract(final boolean isAbtract) {
        this.isAbstract = isAbtract;
    }
}

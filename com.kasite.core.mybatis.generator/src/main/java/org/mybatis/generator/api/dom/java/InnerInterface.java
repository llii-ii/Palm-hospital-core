package org.mybatis.generator.api.dom.java;

import org.mybatis.generator.api.dom.*;
import java.util.*;

public class InnerInterface extends JavaElement
{
    private List<Field> fields;
    private FullyQualifiedJavaType type;
    private List<InnerInterface> innerInterfaces;
    private Set<FullyQualifiedJavaType> superInterfaceTypes;
    private List<Method> methods;
    
    public InnerInterface(final FullyQualifiedJavaType type) {
        this.type = type;
        this.innerInterfaces = new ArrayList<InnerInterface>();
        this.superInterfaceTypes = new LinkedHashSet<FullyQualifiedJavaType>();
        this.methods = new ArrayList<Method>();
        this.fields = new ArrayList<Field>();
    }
    
    public InnerInterface(final String type) {
        this(new FullyQualifiedJavaType(type));
    }
    
    public List<Field> getFields() {
        return this.fields;
    }
    
    public void addField(final Field field) {
        this.fields.add(field);
    }
    
    public String getFormattedContent(int indentLevel, final CompilationUnit compilationUnit) {
        final StringBuilder sb = new StringBuilder();
        this.addFormattedJavadoc(sb, indentLevel);
        this.addFormattedAnnotations(sb, indentLevel);
        OutputUtilities.javaIndent(sb, indentLevel);
        sb.append(this.getVisibility().getValue());
        if (this.isStatic()) {
            sb.append("static ");
        }
        if (this.isFinal()) {
            sb.append("final ");
        }
        sb.append("interface ");
        sb.append(this.getType().getShortName());
        if (this.getSuperInterfaceTypes().size() > 0) {
            sb.append(" extends ");
            boolean comma = false;
            for (final FullyQualifiedJavaType fqjt : this.getSuperInterfaceTypes()) {
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
        }
        if (this.fields.size() > 0 && this.methods.size() > 0) {
            OutputUtilities.newLine(sb);
        }
        final Iterator<Method> mtdIter = this.getMethods().iterator();
        while (mtdIter.hasNext()) {
            OutputUtilities.newLine(sb);
            final Method method = mtdIter.next();
            sb.append(method.getFormattedContent(indentLevel, true, compilationUnit));
            if (mtdIter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }
        if (this.innerInterfaces.size() > 0) {
            OutputUtilities.newLine(sb);
        }
        final Iterator<InnerInterface> iiIter = this.innerInterfaces.iterator();
        while (iiIter.hasNext()) {
            OutputUtilities.newLine(sb);
            final InnerInterface innerInterface = iiIter.next();
            sb.append(innerInterface.getFormattedContent(indentLevel, compilationUnit));
            if (iiIter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }
        --indentLevel;
        OutputUtilities.newLine(sb);
        OutputUtilities.javaIndent(sb, indentLevel);
        sb.append('}');
        return sb.toString();
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
    
    public FullyQualifiedJavaType getSuperClass() {
        return null;
    }
    
    public Set<FullyQualifiedJavaType> getSuperInterfaceTypes() {
        return this.superInterfaceTypes;
    }
    
    public List<InnerInterface> getInnerInterfaces() {
        return this.innerInterfaces;
    }
    
    public void addInnerInterfaces(final InnerInterface innerInterface) {
        this.innerInterfaces.add(innerInterface);
    }
    
    public boolean isJavaInterface() {
        return true;
    }
    
    public boolean isJavaEnumeration() {
        return false;
    }
}

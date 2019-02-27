package org.mybatis.generator.plugins;

import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import java.util.*;
import org.mybatis.generator.api.dom.xml.*;

public class RowBoundsPlugin extends PluginAdapter
{
    private FullyQualifiedJavaType rowBounds;
    private Map<FullyQualifiedTable, List<XmlElement>> elementsToAdd;
    
    public RowBoundsPlugin() {
        this.rowBounds = new FullyQualifiedJavaType("org.apache.ibatis.session.RowBounds");
        this.elementsToAdd = new HashMap<FullyQualifiedTable, List<XmlElement>>();
    }
    
    @Override
    public boolean validate(final List<String> warnings) {
        return true;
    }
    
    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3) {
            this.copyAndAddMethod(method, interfaze);
        }
        else if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3_DSQL) {
            this.copyAndAddSelectByExampleMethodForDSQL(method, interfaze);
        }
        return true;
    }
    
    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3) {
            this.copyAndAddMethod(method, interfaze);
        }
        return true;
    }
    
    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3) {
            this.copyAndSaveElement(element, introspectedTable.getFullyQualifiedTable());
        }
        return true;
    }
    
    @Override
    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3) {
            this.copyAndSaveElement(element, introspectedTable.getFullyQualifiedTable());
        }
        return true;
    }
    
    @Override
    public boolean sqlMapDocumentGenerated(final Document document, final IntrospectedTable introspectedTable) {
        final List<XmlElement> elements = this.elementsToAdd.get(introspectedTable.getFullyQualifiedTable());
        if (elements != null) {
            for (final XmlElement element : elements) {
                document.getRootElement().addElement(element);
            }
        }
        return true;
    }
    
    @Override
    public boolean clientBasicSelectManyMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        this.copyAndAddSelectManyMethod(method, interfaze);
        this.addNewComposedFunction(interfaze, introspectedTable, method.getReturnType());
        return true;
    }
    
    private void addNewComposedFunction(final Interface interfaze, final IntrospectedTable introspectedTable, final FullyQualifiedJavaType baseMethodReturnType) {
        interfaze.addImportedType(new FullyQualifiedJavaType("java.util.function.Function"));
        final FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("Function<SelectStatementProvider, " + baseMethodReturnType.getShortName() + ">");
        final Method method = new Method("selectManyWithRowbounds");
        method.setDefault(true);
        method.setReturnType(returnType);
        method.addParameter(new Parameter(this.rowBounds, "rowBounds"));
        method.addBodyLine("return selectStatement -> selectManyWithRowbounds(selectStatement, rowBounds);");
        this.context.getCommentGenerator().addGeneralMethodAnnotation(method, introspectedTable, interfaze.getImportedTypes());
        interfaze.addMethod(method);
    }
    
    private void copyAndAddMethod(final Method method, final Interface interfaze) {
        final Method newMethod = new Method(method);
        newMethod.setName(method.getName() + "WithRowbounds");
        newMethod.addParameter(new Parameter(this.rowBounds, "rowBounds"));
        interfaze.addMethod(newMethod);
        interfaze.addImportedType(this.rowBounds);
    }
    
    private void copyAndAddSelectManyMethod(final Method method, final Interface interfaze) {
        final List<String> annotations = new ArrayList<String>(method.getAnnotations());
        boolean inResultsAnnotation = false;
        String resultMapId = null;
        final Iterator<String> iter = annotations.iterator();
        while (iter.hasNext()) {
            final String annotation = iter.next();
            if (inResultsAnnotation) {
                if (annotation.equals("})")) {
                    inResultsAnnotation = false;
                }
                iter.remove();
            }
            else {
                if (!annotation.startsWith("@Results(")) {
                    continue;
                }
                inResultsAnnotation = true;
                iter.remove();
                final int index = annotation.indexOf("id=\"");
                final int startIndex = index + "id=\"".length();
                final int endIndex = annotation.indexOf(34, startIndex + 1);
                resultMapId = annotation.substring(startIndex, endIndex);
            }
        }
        if (resultMapId != null) {
            interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.ResultMap"));
            annotations.add("@ResultMap(\"" + resultMapId + "\")");
        }
        final Method newMethod = new Method(method);
        newMethod.getAnnotations().clear();
        for (final String annotation2 : annotations) {
            newMethod.addAnnotation(annotation2);
        }
        newMethod.setName(method.getName() + "WithRowbounds");
        newMethod.addParameter(new Parameter(this.rowBounds, "rowBounds"));
        interfaze.addMethod(newMethod);
        interfaze.addImportedType(this.rowBounds);
    }
    
    private void copyAndAddSelectByExampleMethodForDSQL(final Method method, final Interface interfaze) {
        final Method newMethod = new Method(method);
        newMethod.addParameter(new Parameter(this.rowBounds, "rowBounds"));
        interfaze.addMethod(newMethod);
        interfaze.addImportedType(this.rowBounds);
        for (int i = 0; i < newMethod.getBodyLines().size(); ++i) {
            String bodyLine = newMethod.getBodyLines().get(i);
            if (bodyLine.contains("this::selectMany")) {
                bodyLine = bodyLine.replace("this::selectMany", "selectManyWithRowbounds(rowBounds)");
                newMethod.getBodyLines().set(i, bodyLine);
                break;
            }
        }
    }
    
    private void copyAndSaveElement(final XmlElement element, final FullyQualifiedTable fqt) {
        final XmlElement newElement = new XmlElement(element);
        final Iterator<Attribute> iterator = newElement.getAttributes().iterator();
        while (iterator.hasNext()) {
            final Attribute attribute = iterator.next();
            if ("id".equals(attribute.getName())) {
                iterator.remove();
                final Attribute newAttribute = new Attribute("id", attribute.getValue() + "WithRowbounds");
                newElement.addAttribute(newAttribute);
                break;
            }
        }
        List<XmlElement> elements = this.elementsToAdd.get(fqt);
        if (elements == null) {
            elements = new ArrayList<XmlElement>();
            this.elementsToAdd.put(fqt, elements);
        }
        elements.add(newElement);
    }
}

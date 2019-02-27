package org.mybatis.generator.codegen.ibatis2.dao.elements;

import org.mybatis.generator.api.dom.java.*;

public class UpdateByExampleParmsInnerclassGenerator extends AbstractDAOElementGenerator
{
    @Override
    public void addImplementationElements(final TopLevelClass topLevelClass) {
        topLevelClass.addImportedType(new FullyQualifiedJavaType(this.introspectedTable.getExampleType()));
        final InnerClass innerClass = new InnerClass(new FullyQualifiedJavaType("UpdateByExampleParms"));
        innerClass.setVisibility(JavaVisibility.PROTECTED);
        innerClass.setStatic(true);
        innerClass.setSuperClass(this.introspectedTable.getExampleType());
        this.context.getCommentGenerator().addClassComment(innerClass, this.introspectedTable);
        Method method = new Method();
        method.setConstructor(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(innerClass.getType().getShortName());
        method.addParameter(new Parameter(FullyQualifiedJavaType.getObjectInstance(), "record"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType(this.introspectedTable.getExampleType()), "example"));
        method.addBodyLine("super(example);");
        method.addBodyLine("this.record = record;");
        innerClass.addMethod(method);
        final Field field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setType(FullyQualifiedJavaType.getObjectInstance());
        field.setName("record");
        innerClass.addField(field);
        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getObjectInstance());
        method.setName("getRecord");
        method.addBodyLine("return record;");
        innerClass.addMethod(method);
        topLevelClass.addInnerClass(innerClass);
    }
    
    @Override
    public void addInterfaceElements(final Interface interfaze) {
    }
}

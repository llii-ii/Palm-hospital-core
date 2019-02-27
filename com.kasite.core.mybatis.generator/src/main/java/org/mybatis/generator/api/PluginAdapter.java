package org.mybatis.generator.api;

import org.mybatis.generator.config.*;
import java.util.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.*;

public abstract class PluginAdapter implements Plugin
{
    protected Context context;
    protected Properties properties;
    
    public PluginAdapter() {
        this.properties = new Properties();
    }
    
    public Context getContext() {
        return this.context;
    }
    
    @Override
    public void setContext(final Context context) {
        this.context = context;
    }
    
    public Properties getProperties() {
        return this.properties;
    }
    
    @Override
    public void setProperties(final Properties properties) {
        this.properties.putAll(properties);
    }
    
    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
        return null;
    }
    
    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(final IntrospectedTable introspectedTable) {
        return null;
    }
    
    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles() {
        return null;
    }
    
    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(final IntrospectedTable introspectedTable) {
        return null;
    }
    
    @Override
    public boolean clientBasicCountMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientBasicDeleteMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientBasicInsertMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientBasicSelectManyMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientBasicSelectOneMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientBasicUpdateMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientCountByExampleMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientCountByExampleMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientDeleteByExampleMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientDeleteByExampleMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientInsertMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientInsertMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientGenerated(final Interface interfaze, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientUpdateByExampleSelectiveMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientUpdateByExampleSelectiveMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientUpdateByExampleWithBLOBsMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientUpdateByExampleWithBLOBsMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean modelBaseRecordClassGenerated(final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean modelExampleClassGenerated(final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean modelFieldGenerated(final Field field, final TopLevelClass topLevelClass, final IntrospectedColumn introspectedColumn, final IntrospectedTable introspectedTable, final ModelClassType modelClassType) {
        return true;
    }
    
    @Override
    public boolean modelGetterMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedColumn introspectedColumn, final IntrospectedTable introspectedTable, final ModelClassType modelClassType) {
        return true;
    }
    
    @Override
    public boolean modelPrimaryKeyClassGenerated(final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean modelRecordWithBLOBsClassGenerated(final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean modelSetterMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedColumn introspectedColumn, final IntrospectedTable introspectedTable, final ModelClassType modelClassType) {
        return true;
    }
    
    @Override
    public boolean sqlMapResultMapWithoutBLOBsElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean sqlMapCountByExampleElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean sqlMapDeleteByExampleElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean sqlMapDeleteByPrimaryKeyElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean sqlMapDocumentGenerated(final Document document, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean sqlMapExampleWhereClauseElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean sqlMapGenerated(final GeneratedXmlFile sqlMap, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean sqlMapInsertElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean sqlMapResultMapWithBLOBsElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean sqlMapSelectByPrimaryKeyElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean sqlMapUpdateByExampleSelectiveElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean sqlMapUpdateByExampleWithBLOBsElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean sqlMapUpdateByExampleWithoutBLOBsElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientInsertSelectiveMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientInsertSelectiveMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public void initialized(final IntrospectedTable introspectedTable) {
    }
    
    @Override
    public boolean sqlMapBaseColumnListElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean sqlMapBlobColumnListElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean providerGenerated(final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean providerApplyWhereMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean providerCountByExampleMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean providerDeleteByExampleMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean providerInsertSelectiveMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean providerSelectByExampleWithBLOBsMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean providerSelectByExampleWithoutBLOBsMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean providerUpdateByExampleSelectiveMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean providerUpdateByExampleWithBLOBsMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean providerUpdateByExampleWithoutBLOBsMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean providerUpdateByPrimaryKeySelectiveMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientSelectAllMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean clientSelectAllMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return true;
    }
    
    @Override
    public boolean sqlMapSelectAllElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        return true;
    }
}

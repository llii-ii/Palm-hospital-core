package org.mybatis.generator.internal;

import org.mybatis.generator.config.*;
import java.util.*;
import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.*;

public final class PluginAggregator implements Plugin
{
    private List<Plugin> plugins;
    
    public PluginAggregator() {
        this.plugins = new ArrayList<Plugin>();
    }
    
    public void addPlugin(final Plugin plugin) {
        this.plugins.add(plugin);
    }
    
    @Override
    public void setContext(final Context context) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void setProperties(final Properties properties) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean validate(final List<String> warnings) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean modelBaseRecordClassGenerated(final TopLevelClass tlc, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.modelBaseRecordClassGenerated(tlc, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean modelRecordWithBLOBsClassGenerated(final TopLevelClass tlc, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.modelRecordWithBLOBsClassGenerated(tlc, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean sqlMapCountByExampleElementGenerated(final XmlElement element, final IntrospectedTable table) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.sqlMapCountByExampleElementGenerated(element, table)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean sqlMapDeleteByExampleElementGenerated(final XmlElement element, final IntrospectedTable table) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.sqlMapDeleteByExampleElementGenerated(element, table)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean sqlMapDeleteByPrimaryKeyElementGenerated(final XmlElement element, final IntrospectedTable table) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.sqlMapDeleteByPrimaryKeyElementGenerated(element, table)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean modelExampleClassGenerated(final TopLevelClass tlc, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.modelExampleClassGenerated(tlc, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(final IntrospectedTable introspectedTable) {
        final List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();
        for (final Plugin plugin : this.plugins) {
            final List<GeneratedJavaFile> temp = plugin.contextGenerateAdditionalJavaFiles(introspectedTable);
            if (temp != null) {
                answer.addAll(temp);
            }
        }
        return answer;
    }
    
    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
        final List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();
        for (final Plugin plugin : this.plugins) {
            final List<GeneratedJavaFile> temp = plugin.contextGenerateAdditionalJavaFiles();
            if (temp != null) {
                answer.addAll(temp);
            }
        }
        return answer;
    }
    
    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(final IntrospectedTable introspectedTable) {
        final List<GeneratedXmlFile> answer = new ArrayList<GeneratedXmlFile>();
        for (final Plugin plugin : this.plugins) {
            final List<GeneratedXmlFile> temp = plugin.contextGenerateAdditionalXmlFiles(introspectedTable);
            if (temp != null) {
                answer.addAll(temp);
            }
        }
        return answer;
    }
    
    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles() {
        final List<GeneratedXmlFile> answer = new ArrayList<GeneratedXmlFile>();
        for (final Plugin plugin : this.plugins) {
            final List<GeneratedXmlFile> temp = plugin.contextGenerateAdditionalXmlFiles();
            if (temp != null) {
                answer.addAll(temp);
            }
        }
        return answer;
    }
    
    @Override
    public boolean modelPrimaryKeyClassGenerated(final TopLevelClass tlc, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.modelPrimaryKeyClassGenerated(tlc, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean sqlMapResultMapWithoutBLOBsElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.sqlMapResultMapWithoutBLOBsElementGenerated(element, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean sqlMapExampleWhereClauseElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.sqlMapExampleWhereClauseElementGenerated(element, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean sqlMapInsertElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.sqlMapInsertElementGenerated(element, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean sqlMapResultMapWithBLOBsElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.sqlMapResultMapWithBLOBsElementGenerated(element, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.sqlMapSelectByExampleWithoutBLOBsElementGenerated(element, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.sqlMapSelectByExampleWithBLOBsElementGenerated(element, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean sqlMapSelectByPrimaryKeyElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.sqlMapSelectByPrimaryKeyElementGenerated(element, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean sqlMapGenerated(final GeneratedXmlFile sqlMap, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.sqlMapGenerated(sqlMap, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean sqlMapUpdateByExampleSelectiveElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.sqlMapUpdateByExampleSelectiveElementGenerated(element, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean sqlMapUpdateByExampleWithBLOBsElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.sqlMapUpdateByExampleWithBLOBsElementGenerated(element, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean sqlMapUpdateByExampleWithoutBLOBsElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.sqlMapUpdateByExampleWithoutBLOBsElementGenerated(element, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.sqlMapUpdateByPrimaryKeySelectiveElementGenerated(element, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(element, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(element, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientBasicCountMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientBasicCountMethodGenerated(method, interfaze, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientBasicDeleteMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientBasicDeleteMethodGenerated(method, interfaze, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientBasicInsertMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientBasicInsertMethodGenerated(method, interfaze, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientBasicSelectManyMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientBasicSelectManyMethodGenerated(method, interfaze, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientBasicSelectOneMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientBasicSelectOneMethodGenerated(method, interfaze, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientBasicUpdateMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientBasicUpdateMethodGenerated(method, interfaze, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientCountByExampleMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientCountByExampleMethodGenerated(method, interfaze, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientCountByExampleMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientCountByExampleMethodGenerated(method, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientDeleteByExampleMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientDeleteByExampleMethodGenerated(method, interfaze, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientDeleteByExampleMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientDeleteByExampleMethodGenerated(method, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientDeleteByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientDeleteByPrimaryKeyMethodGenerated(method, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientInsertMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientInsertMethodGenerated(method, interfaze, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientInsertMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientInsertMethodGenerated(method, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientGenerated(final Interface interfaze, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientGenerated(interfaze, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientSelectAllMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientSelectAllMethodGenerated(method, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientSelectAllMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientSelectAllMethodGenerated(method, interfaze, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientSelectByExampleWithBLOBsMethodGenerated(method, interfaze, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientSelectByExampleWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientSelectByExampleWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientSelectByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientSelectByPrimaryKeyMethodGenerated(method, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientUpdateByExampleSelectiveMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientUpdateByExampleSelectiveMethodGenerated(method, interfaze, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientUpdateByExampleSelectiveMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientUpdateByExampleSelectiveMethodGenerated(method, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientUpdateByExampleWithBLOBsMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientUpdateByExampleWithBLOBsMethodGenerated(method, interfaze, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientUpdateByExampleWithBLOBsMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientUpdateByExampleWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientUpdateByExampleWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientUpdateByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientUpdateByPrimaryKeySelectiveMethodGenerated(method, interfaze, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientUpdateByPrimaryKeySelectiveMethodGenerated(method, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(method, interfaze, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean sqlMapDocumentGenerated(final Document document, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.sqlMapDocumentGenerated(document, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean modelFieldGenerated(final Field field, final TopLevelClass topLevelClass, final IntrospectedColumn introspectedColumn, final IntrospectedTable introspectedTable, final ModelClassType modelClassType) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean modelGetterMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedColumn introspectedColumn, final IntrospectedTable introspectedTable, final ModelClassType modelClassType) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.modelGetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable, modelClassType)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean modelSetterMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedColumn introspectedColumn, final IntrospectedTable introspectedTable, final ModelClassType modelClassType) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.modelSetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable, modelClassType)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.sqlMapInsertSelectiveElementGenerated(element, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientInsertSelectiveMethodGenerated(final Method method, final Interface interfaze, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientInsertSelectiveMethodGenerated(method, interfaze, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean clientInsertSelectiveMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.clientInsertSelectiveMethodGenerated(method, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public void initialized(final IntrospectedTable introspectedTable) {
        for (final Plugin plugin : this.plugins) {
            plugin.initialized(introspectedTable);
        }
    }
    
    @Override
    public boolean sqlMapBaseColumnListElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.sqlMapBaseColumnListElementGenerated(element, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean sqlMapBlobColumnListElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.sqlMapBlobColumnListElementGenerated(element, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean providerGenerated(final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.providerGenerated(topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean providerApplyWhereMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.providerApplyWhereMethodGenerated(method, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean providerCountByExampleMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.providerCountByExampleMethodGenerated(method, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean providerDeleteByExampleMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.providerDeleteByExampleMethodGenerated(method, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean providerInsertSelectiveMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.providerInsertSelectiveMethodGenerated(method, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean providerSelectByExampleWithBLOBsMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.providerSelectByExampleWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean providerSelectByExampleWithoutBLOBsMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.providerSelectByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean providerUpdateByExampleSelectiveMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.providerUpdateByExampleSelectiveMethodGenerated(method, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean providerUpdateByExampleWithBLOBsMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.providerUpdateByExampleWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean providerUpdateByExampleWithoutBLOBsMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.providerUpdateByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean providerUpdateByPrimaryKeySelectiveMethodGenerated(final Method method, final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.providerUpdateByPrimaryKeySelectiveMethodGenerated(method, topLevelClass, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
    
    @Override
    public boolean sqlMapSelectAllElementGenerated(final XmlElement element, final IntrospectedTable introspectedTable) {
        boolean rc = true;
        for (final Plugin plugin : this.plugins) {
            if (!plugin.sqlMapSelectAllElementGenerated(element, introspectedTable)) {
                rc = false;
                break;
            }
        }
        return rc;
    }
}

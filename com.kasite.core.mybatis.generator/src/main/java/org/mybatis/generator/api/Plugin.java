package org.mybatis.generator.api;

import org.mybatis.generator.config.*;
import java.util.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.*;

public interface Plugin
{
    void setContext(final Context p0);
    
    void setProperties(final Properties p0);
    
    void initialized(final IntrospectedTable p0);
    
    boolean validate(final List<String> p0);
    
    List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles();
    
    List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(final IntrospectedTable p0);
    
    List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles();
    
    List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(final IntrospectedTable p0);
    
    boolean clientGenerated(final Interface p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    boolean clientBasicCountMethodGenerated(final Method p0, final Interface p1, final IntrospectedTable p2);
    
    boolean clientBasicDeleteMethodGenerated(final Method p0, final Interface p1, final IntrospectedTable p2);
    
    boolean clientBasicInsertMethodGenerated(final Method p0, final Interface p1, final IntrospectedTable p2);
    
    boolean clientBasicSelectManyMethodGenerated(final Method p0, final Interface p1, final IntrospectedTable p2);
    
    boolean clientBasicSelectOneMethodGenerated(final Method p0, final Interface p1, final IntrospectedTable p2);
    
    boolean clientBasicUpdateMethodGenerated(final Method p0, final Interface p1, final IntrospectedTable p2);
    
    boolean clientCountByExampleMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    boolean clientCountByExampleMethodGenerated(final Method p0, final Interface p1, final IntrospectedTable p2);
    
    boolean clientDeleteByExampleMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    boolean clientDeleteByExampleMethodGenerated(final Method p0, final Interface p1, final IntrospectedTable p2);
    
    boolean clientDeleteByPrimaryKeyMethodGenerated(final Method p0, final Interface p1, final IntrospectedTable p2);
    
    boolean clientDeleteByPrimaryKeyMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    boolean clientInsertMethodGenerated(final Method p0, final Interface p1, final IntrospectedTable p2);
    
    boolean clientInsertMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    boolean clientInsertSelectiveMethodGenerated(final Method p0, final Interface p1, final IntrospectedTable p2);
    
    boolean clientInsertSelectiveMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    boolean clientSelectByExampleWithBLOBsMethodGenerated(final Method p0, final Interface p1, final IntrospectedTable p2);
    
    boolean clientSelectByExampleWithBLOBsMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    boolean clientSelectByExampleWithoutBLOBsMethodGenerated(final Method p0, final Interface p1, final IntrospectedTable p2);
    
    boolean clientSelectByExampleWithoutBLOBsMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    boolean clientSelectByPrimaryKeyMethodGenerated(final Method p0, final Interface p1, final IntrospectedTable p2);
    
    boolean clientSelectByPrimaryKeyMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    boolean clientUpdateByExampleSelectiveMethodGenerated(final Method p0, final Interface p1, final IntrospectedTable p2);
    
    boolean clientUpdateByExampleSelectiveMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    boolean clientUpdateByExampleWithBLOBsMethodGenerated(final Method p0, final Interface p1, final IntrospectedTable p2);
    
    boolean clientUpdateByExampleWithBLOBsMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(final Method p0, final Interface p1, final IntrospectedTable p2);
    
    boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(final Method p0, final Interface p1, final IntrospectedTable p2);
    
    boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(final Method p0, final Interface p1, final IntrospectedTable p2);
    
    boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(final Method p0, final Interface p1, final IntrospectedTable p2);
    
    boolean clientSelectAllMethodGenerated(final Method p0, final Interface p1, final IntrospectedTable p2);
    
    boolean clientSelectAllMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    boolean modelFieldGenerated(final Field p0, final TopLevelClass p1, final IntrospectedColumn p2, final IntrospectedTable p3, final ModelClassType p4);
    
    boolean modelGetterMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedColumn p2, final IntrospectedTable p3, final ModelClassType p4);
    
    boolean modelSetterMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedColumn p2, final IntrospectedTable p3, final ModelClassType p4);
    
    boolean modelPrimaryKeyClassGenerated(final TopLevelClass p0, final IntrospectedTable p1);
    
    boolean modelBaseRecordClassGenerated(final TopLevelClass p0, final IntrospectedTable p1);
    
    boolean modelRecordWithBLOBsClassGenerated(final TopLevelClass p0, final IntrospectedTable p1);
    
    boolean modelExampleClassGenerated(final TopLevelClass p0, final IntrospectedTable p1);
    
    boolean sqlMapGenerated(final GeneratedXmlFile p0, final IntrospectedTable p1);
    
    boolean sqlMapDocumentGenerated(final Document p0, final IntrospectedTable p1);
    
    boolean sqlMapResultMapWithoutBLOBsElementGenerated(final XmlElement p0, final IntrospectedTable p1);
    
    boolean sqlMapCountByExampleElementGenerated(final XmlElement p0, final IntrospectedTable p1);
    
    boolean sqlMapDeleteByExampleElementGenerated(final XmlElement p0, final IntrospectedTable p1);
    
    boolean sqlMapDeleteByPrimaryKeyElementGenerated(final XmlElement p0, final IntrospectedTable p1);
    
    boolean sqlMapExampleWhereClauseElementGenerated(final XmlElement p0, final IntrospectedTable p1);
    
    boolean sqlMapBaseColumnListElementGenerated(final XmlElement p0, final IntrospectedTable p1);
    
    boolean sqlMapBlobColumnListElementGenerated(final XmlElement p0, final IntrospectedTable p1);
    
    boolean sqlMapInsertElementGenerated(final XmlElement p0, final IntrospectedTable p1);
    
    boolean sqlMapInsertSelectiveElementGenerated(final XmlElement p0, final IntrospectedTable p1);
    
    boolean sqlMapResultMapWithBLOBsElementGenerated(final XmlElement p0, final IntrospectedTable p1);
    
    boolean sqlMapSelectAllElementGenerated(final XmlElement p0, final IntrospectedTable p1);
    
    boolean sqlMapSelectByPrimaryKeyElementGenerated(final XmlElement p0, final IntrospectedTable p1);
    
    boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(final XmlElement p0, final IntrospectedTable p1);
    
    boolean sqlMapSelectByExampleWithBLOBsElementGenerated(final XmlElement p0, final IntrospectedTable p1);
    
    boolean sqlMapUpdateByExampleSelectiveElementGenerated(final XmlElement p0, final IntrospectedTable p1);
    
    boolean sqlMapUpdateByExampleWithBLOBsElementGenerated(final XmlElement p0, final IntrospectedTable p1);
    
    boolean sqlMapUpdateByExampleWithoutBLOBsElementGenerated(final XmlElement p0, final IntrospectedTable p1);
    
    boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(final XmlElement p0, final IntrospectedTable p1);
    
    boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(final XmlElement p0, final IntrospectedTable p1);
    
    boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(final XmlElement p0, final IntrospectedTable p1);
    
    boolean providerGenerated(final TopLevelClass p0, final IntrospectedTable p1);
    
    boolean providerApplyWhereMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    boolean providerCountByExampleMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    boolean providerDeleteByExampleMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    boolean providerInsertSelectiveMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    boolean providerSelectByExampleWithBLOBsMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    boolean providerSelectByExampleWithoutBLOBsMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    boolean providerUpdateByExampleSelectiveMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    boolean providerUpdateByExampleWithBLOBsMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    boolean providerUpdateByExampleWithoutBLOBsMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    boolean providerUpdateByPrimaryKeySelectiveMethodGenerated(final Method p0, final TopLevelClass p1, final IntrospectedTable p2);
    
    public enum ModelClassType
    {
        PRIMARY_KEY, 
        BASE_RECORD, 
        RECORD_WITH_BLOBS;
    }
}

package org.mybatis.generator.plugins;

import java.util.*;
import org.mybatis.generator.api.*;

public class UnmergeableXmlMappersPlugin extends PluginAdapter
{
    @Override
    public boolean validate(final List<String> warnings) {
        return true;
    }
    
    @Override
    public boolean sqlMapGenerated(final GeneratedXmlFile sqlMap, final IntrospectedTable introspectedTable) {
        sqlMap.setMergeable(false);
        return true;
    }
}

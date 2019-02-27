package org.mybatis.generator.plugins;

import org.mybatis.generator.api.*;
import java.util.*;

public class VirtualPrimaryKeyPlugin extends PluginAdapter
{
    @Override
    public boolean validate(final List<String> warnings) {
        return true;
    }
    
    @Override
    public void initialized(final IntrospectedTable introspectedTable) {
        final String virtualKey = introspectedTable.getTableConfiguration().getProperty("virtualKeyColumns");
        if (virtualKey != null) {
            final StringTokenizer st = new StringTokenizer(virtualKey, ", ", false);
            while (st.hasMoreTokens()) {
                final String column = st.nextToken();
                introspectedTable.addPrimaryKeyColumn(column);
            }
        }
    }
}

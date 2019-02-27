package org.mybatis.generator.codegen.mybatis3;

import org.mybatis.generator.api.*;
import java.util.*;

public class ListUtilities
{
    public static List<IntrospectedColumn> removeGeneratedAlwaysColumns(final List<IntrospectedColumn> columns) {
        final List<IntrospectedColumn> filteredList = new ArrayList<IntrospectedColumn>();
        for (final IntrospectedColumn ic : columns) {
            if (!ic.isGeneratedAlways()) {
                filteredList.add(ic);
            }
        }
        return filteredList;
    }
    
    public static List<IntrospectedColumn> removeIdentityAndGeneratedAlwaysColumns(final List<IntrospectedColumn> columns) {
        final List<IntrospectedColumn> filteredList = new ArrayList<IntrospectedColumn>();
        for (final IntrospectedColumn ic : columns) {
            if (!ic.isGeneratedAlways() && !ic.isIdentity()) {
                filteredList.add(ic);
            }
        }
        return filteredList;
    }
}

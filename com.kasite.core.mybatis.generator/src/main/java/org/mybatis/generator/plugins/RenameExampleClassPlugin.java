package org.mybatis.generator.plugins;

import java.util.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.internal.util.messages.*;
import org.mybatis.generator.api.*;
import java.util.regex.*;

public class RenameExampleClassPlugin extends PluginAdapter
{
    private String searchString;
    private String replaceString;
    private Pattern pattern;
    
    @Override
    public boolean validate(final List<String> warnings) {
        this.searchString = this.properties.getProperty("searchString");
        this.replaceString = this.properties.getProperty("replaceString");
        final boolean valid = StringUtility.stringHasValue(this.searchString) && StringUtility.stringHasValue(this.replaceString);
        if (valid) {
            this.pattern = Pattern.compile(this.searchString);
        }
        else {
            if (!StringUtility.stringHasValue(this.searchString)) {
                warnings.add(Messages.getString("ValidationError.18", "RenameExampleClassPlugin", "searchString"));
            }
            if (!StringUtility.stringHasValue(this.replaceString)) {
                warnings.add(Messages.getString("ValidationError.18", "RenameExampleClassPlugin", "replaceString"));
            }
        }
        return valid;
    }
    
    @Override
    public void initialized(final IntrospectedTable introspectedTable) {
        String oldType = introspectedTable.getExampleType();
        final Matcher matcher = this.pattern.matcher(oldType);
        oldType = matcher.replaceAll(this.replaceString);
        introspectedTable.setExampleType(oldType);
    }
}

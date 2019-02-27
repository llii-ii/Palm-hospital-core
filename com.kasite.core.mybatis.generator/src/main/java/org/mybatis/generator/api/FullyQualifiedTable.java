package org.mybatis.generator.api;

import org.mybatis.generator.config.*;
import java.util.regex.*;
import org.mybatis.generator.internal.util.*;
import java.io.*;

public class FullyQualifiedTable
{
    private String introspectedCatalog;
    private String introspectedSchema;
    private String introspectedTableName;
    private String runtimeCatalog;
    private String runtimeSchema;
    private String runtimeTableName;
    private String domainObjectName;
    private String domainObjectSubPackage;
    private String alias;
    private boolean ignoreQualifiersAtRuntime;
    private String beginningDelimiter;
    private String endingDelimiter;
    private DomainObjectRenamingRule domainObjectRenamingRule;
    
    public FullyQualifiedTable(final String introspectedCatalog, final String introspectedSchema, final String introspectedTableName, final String domainObjectName, final String alias, final boolean ignoreQualifiersAtRuntime, final String runtimeCatalog, final String runtimeSchema, final String runtimeTableName, final boolean delimitIdentifiers, final DomainObjectRenamingRule domainObjectRenamingRule, final Context context) {
        this.introspectedCatalog = introspectedCatalog;
        this.introspectedSchema = introspectedSchema;
        this.introspectedTableName = introspectedTableName;
        this.ignoreQualifiersAtRuntime = ignoreQualifiersAtRuntime;
        this.runtimeCatalog = runtimeCatalog;
        this.runtimeSchema = runtimeSchema;
        this.runtimeTableName = runtimeTableName;
        this.domainObjectRenamingRule = domainObjectRenamingRule;
        if (StringUtility.stringHasValue(domainObjectName)) {
            final int index = domainObjectName.lastIndexOf(46);
            if (index == -1) {
                this.domainObjectName = domainObjectName;
            }
            else {
                this.domainObjectName = domainObjectName.substring(index + 1);
                this.domainObjectSubPackage = domainObjectName.substring(0, index);
            }
        }
        if (alias == null) {
            this.alias = null;
        }
        else {
            this.alias = alias.trim();
        }
        this.beginningDelimiter = (delimitIdentifiers ? context.getBeginningDelimiter() : "");
        this.endingDelimiter = (delimitIdentifiers ? context.getEndingDelimiter() : "");
    }
    
    public String getIntrospectedCatalog() {
        return this.introspectedCatalog;
    }
    
    public String getIntrospectedSchema() {
        return this.introspectedSchema;
    }
    
    public String getIntrospectedTableName() {
        return this.introspectedTableName;
    }
    
    public String getFullyQualifiedTableNameAtRuntime() {
        final StringBuilder localCatalog = new StringBuilder();
        if (!this.ignoreQualifiersAtRuntime) {
            if (StringUtility.stringHasValue(this.runtimeCatalog)) {
                localCatalog.append(this.runtimeCatalog);
            }
            else if (StringUtility.stringHasValue(this.introspectedCatalog)) {
                localCatalog.append(this.introspectedCatalog);
            }
        }
        if (localCatalog.length() > 0) {
            this.addDelimiters(localCatalog);
        }
        final StringBuilder localSchema = new StringBuilder();
        if (!this.ignoreQualifiersAtRuntime) {
            if (StringUtility.stringHasValue(this.runtimeSchema)) {
                localSchema.append(this.runtimeSchema);
            }
            else if (StringUtility.stringHasValue(this.introspectedSchema)) {
                localSchema.append(this.introspectedSchema);
            }
        }
        if (localSchema.length() > 0) {
            this.addDelimiters(localSchema);
        }
        final StringBuilder localTableName = new StringBuilder();
        if (StringUtility.stringHasValue(this.runtimeTableName)) {
            localTableName.append(this.runtimeTableName);
        }
        else {
            localTableName.append(this.introspectedTableName);
        }
        this.addDelimiters(localTableName);
        return StringUtility.composeFullyQualifiedTableName(localCatalog.toString(), localSchema.toString(), localTableName.toString(), '.');
    }
    
    public String getAliasedFullyQualifiedTableNameAtRuntime() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getFullyQualifiedTableNameAtRuntime());
        if (StringUtility.stringHasValue(this.alias)) {
            sb.append(' ');
            sb.append(this.alias);
        }
        return sb.toString();
    }
    
    public String getIbatis2SqlMapNamespace() {
        final String localCatalog = StringUtility.stringHasValue(this.runtimeCatalog) ? this.runtimeCatalog : this.introspectedCatalog;
        final String localSchema = StringUtility.stringHasValue(this.runtimeSchema) ? this.runtimeSchema : this.introspectedSchema;
        final String localTable = StringUtility.stringHasValue(this.runtimeTableName) ? this.runtimeTableName : this.introspectedTableName;
        return StringUtility.composeFullyQualifiedTableName(this.ignoreQualifiersAtRuntime ? null : localCatalog, this.ignoreQualifiersAtRuntime ? null : localSchema, localTable, '_');
    }
    
    public String getDomainObjectName() {
        if (StringUtility.stringHasValue(this.domainObjectName)) {
            return this.domainObjectName;
        }
        String finalDomainObjectName;
        if (StringUtility.stringHasValue(this.runtimeTableName)) {
            finalDomainObjectName = JavaBeansUtil.getCamelCaseString(this.runtimeTableName, true);
        }
        else {
            finalDomainObjectName = JavaBeansUtil.getCamelCaseString(this.introspectedTableName, true);
        }
        if (this.domainObjectRenamingRule != null) {
            final Pattern pattern = Pattern.compile(this.domainObjectRenamingRule.getSearchString());
            String replaceString = this.domainObjectRenamingRule.getReplaceString();
            replaceString = ((replaceString == null) ? "" : replaceString);
            final Matcher matcher = pattern.matcher(finalDomainObjectName);
            finalDomainObjectName = JavaBeansUtil.getCamelCaseString(matcher.replaceAll(replaceString), true);
        }
        return finalDomainObjectName;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FullyQualifiedTable)) {
            return false;
        }
        final FullyQualifiedTable other = (FullyQualifiedTable)obj;
        return EqualsUtil.areEqual(this.introspectedTableName, other.introspectedTableName) && EqualsUtil.areEqual(this.introspectedCatalog, other.introspectedCatalog) && EqualsUtil.areEqual(this.introspectedSchema, other.introspectedSchema);
    }
    
    @Override
    public int hashCode() {
        int result = 23;
        result = HashCodeUtil.hash(result, this.introspectedTableName);
        result = HashCodeUtil.hash(result, this.introspectedCatalog);
        result = HashCodeUtil.hash(result, this.introspectedSchema);
        return result;
    }
    
    @Override
    public String toString() {
        return StringUtility.composeFullyQualifiedTableName(this.introspectedCatalog, this.introspectedSchema, this.introspectedTableName, '.');
    }
    
    public String getAlias() {
        return this.alias;
    }
    
    public String getSubPackageForClientOrSqlMap(final boolean isSubPackagesEnabled) {
        final StringBuilder sb = new StringBuilder();
        if (!this.ignoreQualifiersAtRuntime && isSubPackagesEnabled) {
            if (StringUtility.stringHasValue(this.runtimeCatalog)) {
                sb.append('.');
                sb.append(this.runtimeCatalog.toLowerCase());
            }
            else if (StringUtility.stringHasValue(this.introspectedCatalog)) {
                sb.append('.');
                sb.append(this.introspectedCatalog.toLowerCase());
            }
            if (StringUtility.stringHasValue(this.runtimeSchema)) {
                sb.append('.');
                sb.append(this.runtimeSchema.toLowerCase());
            }
            else if (StringUtility.stringHasValue(this.introspectedSchema)) {
                sb.append('.');
                sb.append(this.introspectedSchema.toLowerCase());
            }
        }
        return sb.toString();
    }
    
    public String getSubPackageForModel(final boolean isSubPackagesEnabled) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getSubPackageForClientOrSqlMap(isSubPackagesEnabled));
        if (StringUtility.stringHasValue(this.domainObjectSubPackage)) {
            sb.append('.');
            sb.append(this.domainObjectSubPackage);
        }
        return sb.toString();
    }
    
    private void addDelimiters(final StringBuilder sb) {
        if (StringUtility.stringHasValue(this.beginningDelimiter)) {
            sb.insert(0, this.beginningDelimiter);
        }
        if (StringUtility.stringHasValue(this.endingDelimiter)) {
            sb.append(this.endingDelimiter);
        }
    }
    
    public String getDomainObjectSubPackage() {
        return this.domainObjectSubPackage;
    }
}

package org.mybatis.generator.internal.db;

import org.mybatis.generator.internal.util.*;

public class ActualTableName
{
    private String tableName;
    private String catalog;
    private String schema;
    private String fullName;
    
    public ActualTableName(final String catalog, final String schema, final String tableName) {
        this.catalog = catalog;
        this.schema = schema;
        this.tableName = tableName;
        this.fullName = StringUtility.composeFullyQualifiedTableName(catalog, schema, tableName, '.');
    }
    
    public String getCatalog() {
        return this.catalog;
    }
    
    public String getSchema() {
        return this.schema;
    }
    
    public String getTableName() {
        return this.tableName;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj != null && obj instanceof ActualTableName && obj.toString().equals(this.toString());
    }
    
    @Override
    public int hashCode() {
        return this.fullName.hashCode();
    }
    
    @Override
    public String toString() {
        return this.fullName;
    }
}

package org.mybatis.generator.config;

import org.mybatis.generator.internal.db.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.dom.xml.*;
import java.util.*;
import org.mybatis.generator.internal.util.messages.*;

public class GeneratedKey
{
    private String column;
    private String configuredSqlStatement;
    private String runtimeSqlStatement;
    private boolean isIdentity;
    private String type;
    
    public GeneratedKey(final String column, final String configuredSqlStatement, final boolean isIdentity, final String type) {
        this.column = column;
        this.type = type;
        this.isIdentity = isIdentity;
        this.configuredSqlStatement = configuredSqlStatement;
        final DatabaseDialects dialect = DatabaseDialects.getDatabaseDialect(configuredSqlStatement);
        if (dialect == null) {
            this.runtimeSqlStatement = configuredSqlStatement;
        }
        else {
            this.runtimeSqlStatement = dialect.getIdentityRetrievalStatement();
        }
    }
    
    public String getColumn() {
        return this.column;
    }
    
    public boolean isIdentity() {
        return this.isIdentity;
    }
    
    public String getRuntimeSqlStatement() {
        return this.runtimeSqlStatement;
    }
    
    public String getType() {
        return this.type;
    }
    
    public boolean isPlacedBeforeInsertInIbatis2() {
        final boolean rc = StringUtility.stringHasValue(this.type) || !this.isIdentity;
        return rc;
    }
    
    public String getMyBatis3Order() {
        return this.isIdentity ? "AFTER" : "BEFORE";
    }
    
    public XmlElement toXmlElement() {
        final XmlElement xmlElement = new XmlElement("generatedKey");
        xmlElement.addAttribute(new Attribute("column", this.column));
        xmlElement.addAttribute(new Attribute("sqlStatement", this.configuredSqlStatement));
        if (StringUtility.stringHasValue(this.type)) {
            xmlElement.addAttribute(new Attribute("type", this.type));
        }
        xmlElement.addAttribute(new Attribute("identity", this.isIdentity ? "true" : "false"));
        return xmlElement;
    }
    
    public void validate(final List<String> errors, final String tableName) {
        if (!StringUtility.stringHasValue(this.runtimeSqlStatement)) {
            errors.add(Messages.getString("ValidationError.7", tableName));
        }
        if (StringUtility.stringHasValue(this.type) && !"pre".equals(this.type) && !"post".equals(this.type)) {
            errors.add(Messages.getString("ValidationError.15", tableName));
        }
        if ("pre".equals(this.type) && this.isIdentity) {
            errors.add(Messages.getString("ValidationError.23", tableName));
        }
        if ("post".equals(this.type) && !this.isIdentity) {
            errors.add(Messages.getString("ValidationError.24", tableName));
        }
    }
    
    public boolean isJdbcStandard() {
        return "JDBC".equals(this.runtimeSqlStatement);
    }
}

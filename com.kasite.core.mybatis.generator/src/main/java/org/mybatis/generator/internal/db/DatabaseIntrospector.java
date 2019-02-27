package org.mybatis.generator.internal.db;

import org.mybatis.generator.logging.*;
import org.mybatis.generator.internal.util.messages.*;
import org.mybatis.generator.api.*;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.api.dom.java.*;
import java.util.regex.*;
import org.mybatis.generator.internal.*;
import java.util.*;
import java.sql.*;
import java.io.*;

public class DatabaseIntrospector
{
    private DatabaseMetaData databaseMetaData;
    private JavaTypeResolver javaTypeResolver;
    private List<String> warnings;
    private Context context;
    private Log logger;
    
    public DatabaseIntrospector(final Context context, final DatabaseMetaData databaseMetaData, final JavaTypeResolver javaTypeResolver, final List<String> warnings) {
        this.context = context;
        this.databaseMetaData = databaseMetaData;
        this.javaTypeResolver = javaTypeResolver;
        this.warnings = warnings;
        this.logger = LogFactory.getLog(this.getClass());
    }
    
    private void calculatePrimaryKey(final FullyQualifiedTable table, final IntrospectedTable introspectedTable) {
        ResultSet rs = null;
        try {
            rs = this.databaseMetaData.getPrimaryKeys(table.getIntrospectedCatalog(), table.getIntrospectedSchema(), table.getIntrospectedTableName());
        }
        catch (SQLException e) {
            this.closeResultSet(rs);
            this.warnings.add(Messages.getString("Warning.15"));
            return;
        }
        try {
            final Map<Short, String> keyColumns = new TreeMap<Short, String>();
            while (rs.next()) {
                final String columnName = rs.getString("COLUMN_NAME");
                final short keySeq = rs.getShort("KEY_SEQ");
                keyColumns.put(keySeq, columnName);
            }
            for (final String columnName2 : keyColumns.values()) {
                introspectedTable.addPrimaryKeyColumn(columnName2);
            }
        }
        catch (SQLException ex) {}
        finally {
            this.closeResultSet(rs);
        }
    }
    
    private void closeResultSet(final ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            }
            catch (SQLException ex) {}
        }
    }
    
    private void reportIntrospectionWarnings(final IntrospectedTable introspectedTable, final TableConfiguration tableConfiguration, final FullyQualifiedTable table) {
        for (final ColumnOverride columnOverride : tableConfiguration.getColumnOverrides()) {
            if (introspectedTable.getColumn(columnOverride.getColumnName()) == null) {
                this.warnings.add(Messages.getString("Warning.3", columnOverride.getColumnName(), table.toString()));
            }
        }
        for (final String string : tableConfiguration.getIgnoredColumnsInError()) {
            this.warnings.add(Messages.getString("Warning.4", string, table.toString()));
        }
        final GeneratedKey generatedKey = tableConfiguration.getGeneratedKey();
        if (generatedKey != null && introspectedTable.getColumn(generatedKey.getColumn()) == null) {
            if (generatedKey.isIdentity()) {
                this.warnings.add(Messages.getString("Warning.5", generatedKey.getColumn(), table.toString()));
            }
            else {
                this.warnings.add(Messages.getString("Warning.6", generatedKey.getColumn(), table.toString()));
            }
        }
        for (final IntrospectedColumn ic : introspectedTable.getAllColumns()) {
            if (JavaReservedWords.containsWord(ic.getJavaProperty())) {
                this.warnings.add(Messages.getString("Warning.26", ic.getActualColumnName(), table.toString()));
            }
        }
    }
    
    public List<IntrospectedTable> introspectTables(final TableConfiguration tc) throws SQLException {
        final Map<ActualTableName, List<IntrospectedColumn>> columns = this.getColumns(tc);
        if (columns.isEmpty()) {
            this.warnings.add(Messages.getString("Warning.19", tc.getCatalog(), tc.getSchema(), tc.getTableName()));
            return null;
        }
        this.removeIgnoredColumns(tc, columns);
        this.calculateExtraColumnInformation(tc, columns);
        this.applyColumnOverrides(tc, columns);
        this.calculateIdentityColumns(tc, columns);
        final List<IntrospectedTable> introspectedTables = this.calculateIntrospectedTables(tc, columns);
        final Iterator<IntrospectedTable> iter = introspectedTables.iterator();
        while (iter.hasNext()) {
            final IntrospectedTable introspectedTable = iter.next();
            if (!introspectedTable.hasAnyColumns()) {
                final String warning = Messages.getString("Warning.1", introspectedTable.getFullyQualifiedTable().toString());
                this.warnings.add(warning);
                iter.remove();
            }
            else if (!introspectedTable.hasPrimaryKeyColumns() && !introspectedTable.hasBaseColumns()) {
                final String warning = Messages.getString("Warning.18", introspectedTable.getFullyQualifiedTable().toString());
                this.warnings.add(warning);
                iter.remove();
            }
            else {
                this.reportIntrospectionWarnings(introspectedTable, tc, introspectedTable.getFullyQualifiedTable());
            }
        }
        return introspectedTables;
    }
    
    private void removeIgnoredColumns(final TableConfiguration tc, final Map<ActualTableName, List<IntrospectedColumn>> columns) {
        for (final Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : columns.entrySet()) {
            final Iterator<IntrospectedColumn> tableColumns = entry.getValue().iterator();
            while (tableColumns.hasNext()) {
                final IntrospectedColumn introspectedColumn = tableColumns.next();
                if (tc.isColumnIgnored(introspectedColumn.getActualColumnName())) {
                    tableColumns.remove();
                    if (!this.logger.isDebugEnabled()) {
                        continue;
                    }
                    this.logger.debug(Messages.getString("Tracing.3", introspectedColumn.getActualColumnName(), entry.getKey().toString()));
                }
            }
        }
    }
    
    private void calculateExtraColumnInformation(final TableConfiguration tc, final Map<ActualTableName, List<IntrospectedColumn>> columns) {
        final StringBuilder sb = new StringBuilder();
        Pattern pattern = null;
        String replaceString = null;
        if (tc.getColumnRenamingRule() != null) {
            pattern = Pattern.compile(tc.getColumnRenamingRule().getSearchString());
            replaceString = tc.getColumnRenamingRule().getReplaceString();
            replaceString = ((replaceString == null) ? "" : replaceString);
        }
        for (final Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : columns.entrySet()) {
            for (final IntrospectedColumn introspectedColumn : entry.getValue()) {
                String calculatedColumnName;
                if (pattern == null) {
                    calculatedColumnName = introspectedColumn.getActualColumnName();
                }
                else {
                    final Matcher matcher = pattern.matcher(introspectedColumn.getActualColumnName());
                    calculatedColumnName = matcher.replaceAll(replaceString);
                }
                if (StringUtility.isTrue(tc.getProperty("useActualColumnNames"))) {
                    introspectedColumn.setJavaProperty(JavaBeansUtil.getValidPropertyName(calculatedColumnName));
                }
                else if (StringUtility.isTrue(tc.getProperty("useCompoundPropertyNames"))) {
                    sb.setLength(0);
                    sb.append(calculatedColumnName);
                    sb.append('_');
                    sb.append(JavaBeansUtil.getCamelCaseString(introspectedColumn.getRemarks(), true));
                    introspectedColumn.setJavaProperty(JavaBeansUtil.getValidPropertyName(sb.toString()));
                }
                else {
                    introspectedColumn.setJavaProperty(JavaBeansUtil.getCamelCaseString(calculatedColumnName, false));
                }
                final FullyQualifiedJavaType fullyQualifiedJavaType = this.javaTypeResolver.calculateJavaType(introspectedColumn);
                if (fullyQualifiedJavaType != null) {
                    introspectedColumn.setFullyQualifiedJavaType(fullyQualifiedJavaType);
                    introspectedColumn.setJdbcTypeName(this.javaTypeResolver.calculateJdbcTypeName(introspectedColumn));
                }
                else {
                    boolean warn = true;
                    if (tc.isColumnIgnored(introspectedColumn.getActualColumnName())) {
                        warn = false;
                    }
                    final ColumnOverride co = tc.getColumnOverride(introspectedColumn.getActualColumnName());
                    if (co != null && StringUtility.stringHasValue(co.getJavaType())) {
                        warn = false;
                    }
                    if (warn) {
                        introspectedColumn.setFullyQualifiedJavaType(FullyQualifiedJavaType.getObjectInstance());
                        introspectedColumn.setJdbcTypeName("OTHER");
                        final String warning = Messages.getString("Warning.14", Integer.toString(introspectedColumn.getJdbcType()), entry.getKey().toString(), introspectedColumn.getActualColumnName());
                        this.warnings.add(warning);
                    }
                }
                if (this.context.autoDelimitKeywords() && SqlReservedWords.containsWord(introspectedColumn.getActualColumnName())) {
                    introspectedColumn.setColumnNameDelimited(true);
                }
                if (tc.isAllColumnDelimitingEnabled()) {
                    introspectedColumn.setColumnNameDelimited(true);
                }
            }
        }
    }
    
    private void calculateIdentityColumns(final TableConfiguration tc, final Map<ActualTableName, List<IntrospectedColumn>> columns) {
        final GeneratedKey gk = tc.getGeneratedKey();
        if (gk == null) {
            return;
        }
        for (final Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : columns.entrySet()) {
            for (final IntrospectedColumn introspectedColumn : entry.getValue()) {
                if (this.isMatchedColumn(introspectedColumn, gk)) {
                    if (gk.isIdentity() || gk.isJdbcStandard()) {
                        introspectedColumn.setIdentity(true);
                        introspectedColumn.setSequenceColumn(false);
                    }
                    else {
                        introspectedColumn.setIdentity(false);
                        introspectedColumn.setSequenceColumn(true);
                    }
                }
            }
        }
    }
    
    private boolean isMatchedColumn(final IntrospectedColumn introspectedColumn, final GeneratedKey gk) {
        if (introspectedColumn.isColumnNameDelimited()) {
            return introspectedColumn.getActualColumnName().equals(gk.getColumn());
        }
        return introspectedColumn.getActualColumnName().equalsIgnoreCase(gk.getColumn());
    }
    
    private void applyColumnOverrides(final TableConfiguration tc, final Map<ActualTableName, List<IntrospectedColumn>> columns) {
        for (final Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : columns.entrySet()) {
            for (final IntrospectedColumn introspectedColumn : entry.getValue()) {
                final ColumnOverride columnOverride = tc.getColumnOverride(introspectedColumn.getActualColumnName());
                if (columnOverride != null) {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug(Messages.getString("Tracing.4", introspectedColumn.getActualColumnName(), entry.getKey().toString()));
                    }
                    if (StringUtility.stringHasValue(columnOverride.getJavaProperty())) {
                        introspectedColumn.setJavaProperty(columnOverride.getJavaProperty());
                    }
                    if (StringUtility.stringHasValue(columnOverride.getJavaType())) {
                        introspectedColumn.setFullyQualifiedJavaType(new FullyQualifiedJavaType(columnOverride.getJavaType()));
                    }
                    if (StringUtility.stringHasValue(columnOverride.getJdbcType())) {
                        introspectedColumn.setJdbcTypeName(columnOverride.getJdbcType());
                    }
                    if (StringUtility.stringHasValue(columnOverride.getTypeHandler())) {
                        introspectedColumn.setTypeHandler(columnOverride.getTypeHandler());
                    }
                    if (columnOverride.isColumnNameDelimited()) {
                        introspectedColumn.setColumnNameDelimited(true);
                    }
                    introspectedColumn.setGeneratedAlways(columnOverride.isGeneratedAlways());
                    introspectedColumn.setProperties(columnOverride.getProperties());
                }
            }
        }
    }
    
    private Map<ActualTableName, List<IntrospectedColumn>> getColumns(final TableConfiguration tc) throws SQLException {
        final boolean delimitIdentifiers = tc.isDelimitIdentifiers() || StringUtility.stringContainsSpace(tc.getCatalog()) || StringUtility.stringContainsSpace(tc.getSchema()) || StringUtility.stringContainsSpace(tc.getTableName());
        String localCatalog;
        String localSchema;
        String localTableName;
        if (delimitIdentifiers) {
            localCatalog = tc.getCatalog();
            localSchema = tc.getSchema();
            localTableName = tc.getTableName();
        }
        else if (this.databaseMetaData.storesLowerCaseIdentifiers()) {
            localCatalog = ((tc.getCatalog() == null) ? null : tc.getCatalog().toLowerCase());
            localSchema = ((tc.getSchema() == null) ? null : tc.getSchema().toLowerCase());
            localTableName = ((tc.getTableName() == null) ? null : tc.getTableName().toLowerCase());
        }
        else if (this.databaseMetaData.storesUpperCaseIdentifiers()) {
            localCatalog = ((tc.getCatalog() == null) ? null : tc.getCatalog().toUpperCase());
            localSchema = ((tc.getSchema() == null) ? null : tc.getSchema().toUpperCase());
            localTableName = ((tc.getTableName() == null) ? null : tc.getTableName().toUpperCase());
        }
        else {
            localCatalog = tc.getCatalog();
            localSchema = tc.getSchema();
            localTableName = tc.getTableName();
        }
        if (tc.isWildcardEscapingEnabled()) {
            final String escapeString = this.databaseMetaData.getSearchStringEscape();
            final StringBuilder sb = new StringBuilder();
            if (localSchema != null) {
                final StringTokenizer st = new StringTokenizer(localSchema, "_%", true);
                while (st.hasMoreTokens()) {
                    final String token = st.nextToken();
                    if (token.equals("_") || token.equals("%")) {
                        sb.append(escapeString);
                    }
                    sb.append(token);
                }
                localSchema = sb.toString();
            }
            sb.setLength(0);
            final StringTokenizer st = new StringTokenizer(localTableName, "_%", true);
            while (st.hasMoreTokens()) {
                final String token = st.nextToken();
                if (token.equals("_") || token.equals("%")) {
                    sb.append(escapeString);
                }
                sb.append(token);
            }
            localTableName = sb.toString();
        }
        final Map<ActualTableName, List<IntrospectedColumn>> answer = new HashMap<ActualTableName, List<IntrospectedColumn>>();
        if (this.logger.isDebugEnabled()) {
            final String fullTableName = StringUtility.composeFullyQualifiedTableName(localCatalog, localSchema, localTableName, '.');
            this.logger.debug(Messages.getString("Tracing.1", fullTableName));
        }
        final ResultSet rs = this.databaseMetaData.getColumns(localCatalog, localSchema, localTableName, "%");
        boolean supportsIsAutoIncrement = false;
        boolean supportsIsGeneratedColumn = false;
        final ResultSetMetaData rsmd = rs.getMetaData();
        for (int colCount = rsmd.getColumnCount(), i = 1; i <= colCount; ++i) {
            if ("IS_AUTOINCREMENT".equals(rsmd.getColumnName(i))) {
                supportsIsAutoIncrement = true;
            }
            if ("IS_GENERATEDCOLUMN".equals(rsmd.getColumnName(i))) {
                supportsIsGeneratedColumn = true;
            }
        }
        while (rs.next()) {
            final IntrospectedColumn introspectedColumn = ObjectFactory.createIntrospectedColumn(this.context);
            introspectedColumn.setTableAlias(tc.getAlias());
            introspectedColumn.setJdbcType(rs.getInt("DATA_TYPE"));
            introspectedColumn.setLength(rs.getInt("COLUMN_SIZE"));
            introspectedColumn.setActualColumnName(rs.getString("COLUMN_NAME"));
            introspectedColumn.setNullable(rs.getInt("NULLABLE") == 1);
            introspectedColumn.setScale(rs.getInt("DECIMAL_DIGITS"));
            introspectedColumn.setRemarks(rs.getString("REMARKS"));
            introspectedColumn.setDefaultValue(rs.getString("COLUMN_DEF"));
            if (supportsIsAutoIncrement) {
                introspectedColumn.setAutoIncrement("YES".equals(rs.getString("IS_AUTOINCREMENT")));
            }
            if (supportsIsGeneratedColumn) {
                introspectedColumn.setGeneratedColumn("YES".equals(rs.getString("IS_GENERATEDCOLUMN")));
            }
            final ActualTableName atn = new ActualTableName(rs.getString("TABLE_CAT"), rs.getString("TABLE_SCHEM"), rs.getString("TABLE_NAME"));
            List<IntrospectedColumn> columns = answer.get(atn);
            if (columns == null) {
                columns = new ArrayList<IntrospectedColumn>();
                answer.put(atn, columns);
            }
            columns.add(introspectedColumn);
            if (this.logger.isDebugEnabled()) {
                this.logger.debug(Messages.getString("Tracing.2", introspectedColumn.getActualColumnName(), Integer.toString(introspectedColumn.getJdbcType()), atn.toString()));
            }
        }
        this.closeResultSet(rs);
        if (answer.size() > 1 && !StringUtility.stringContainsSQLWildcard(localSchema) && !StringUtility.stringContainsSQLWildcard(localTableName)) {
            final ActualTableName inputAtn = new ActualTableName(tc.getCatalog(), tc.getSchema(), tc.getTableName());
            final StringBuilder sb2 = new StringBuilder();
            boolean comma = false;
            for (final ActualTableName atn2 : answer.keySet()) {
                if (comma) {
                    sb2.append(',');
                }
                else {
                    comma = true;
                }
                sb2.append(atn2.toString());
            }
            this.warnings.add(Messages.getString("Warning.25", inputAtn.toString(), sb2.toString()));
        }
        return answer;
    }
    
    private List<IntrospectedTable> calculateIntrospectedTables(final TableConfiguration tc, final Map<ActualTableName, List<IntrospectedColumn>> columns) {
        final boolean delimitIdentifiers = tc.isDelimitIdentifiers() || StringUtility.stringContainsSpace(tc.getCatalog()) || StringUtility.stringContainsSpace(tc.getSchema()) || StringUtility.stringContainsSpace(tc.getTableName());
        final List<IntrospectedTable> answer = new ArrayList<IntrospectedTable>();
        for (final Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : columns.entrySet()) {
            final ActualTableName atn = entry.getKey();
            final FullyQualifiedTable table = new FullyQualifiedTable(StringUtility.stringHasValue(tc.getCatalog()) ? atn.getCatalog() : null, StringUtility.stringHasValue(tc.getSchema()) ? atn.getSchema() : null, atn.getTableName(), tc.getDomainObjectName(), tc.getAlias(), StringUtility.isTrue(tc.getProperty("ignoreQualifiersAtRuntime")), tc.getProperty("runtimeCatalog"), tc.getProperty("runtimeSchema"), tc.getProperty("runtimeTableName"), delimitIdentifiers, tc.getDomainObjectRenamingRule(), this.context);
            final IntrospectedTable introspectedTable = ObjectFactory.createIntrospectedTable(tc, table, this.context);
            for (final IntrospectedColumn introspectedColumn : entry.getValue()) {
                introspectedTable.addColumn(introspectedColumn);
            }
            this.calculatePrimaryKey(table, introspectedTable);
            this.enhanceIntrospectedTable(introspectedTable);
            answer.add(introspectedTable);
        }
        return answer;
    }
    
    private void enhanceIntrospectedTable(final IntrospectedTable introspectedTable) {
        try {
            final FullyQualifiedTable fqt = introspectedTable.getFullyQualifiedTable();
            final ResultSet rs = this.databaseMetaData.getTables(fqt.getIntrospectedCatalog(), fqt.getIntrospectedSchema(), fqt.getIntrospectedTableName(), null);
            if (rs.next()) {
                final String remarks = rs.getString("REMARKS");
                final String tableType = rs.getString("TABLE_TYPE");
                introspectedTable.setRemarks(remarks);
                introspectedTable.setTableType(tableType);
            }
            this.closeResultSet(rs);
        }
        catch (SQLException e) {
            this.warnings.add(Messages.getString("Warning.27", e.getMessage()));
        }
    }
}

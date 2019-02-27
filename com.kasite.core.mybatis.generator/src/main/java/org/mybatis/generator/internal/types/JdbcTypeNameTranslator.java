package org.mybatis.generator.internal.types;

import java.util.*;

public class JdbcTypeNameTranslator
{
    private static Map<Integer, String> typeToName;
    private static Map<String, Integer> nameToType;
    
    public static String getJdbcTypeName(final int jdbcType) {
        String answer = JdbcTypeNameTranslator.typeToName.get(jdbcType);
        if (answer == null) {
            answer = "OTHER";
        }
        return answer;
    }
    
    public static int getJdbcType(final String jdbcTypeName) {
        Integer answer = JdbcTypeNameTranslator.nameToType.get(jdbcTypeName);
        if (answer == null) {
            answer = 1111;
        }
        return answer;
    }
    
    static {
        (JdbcTypeNameTranslator.typeToName = new HashMap<Integer, String>()).put(2003, "ARRAY");
        JdbcTypeNameTranslator.typeToName.put(-5, "BIGINT");
        JdbcTypeNameTranslator.typeToName.put(-2, "BINARY");
        JdbcTypeNameTranslator.typeToName.put(-7, "BIT");
        JdbcTypeNameTranslator.typeToName.put(2004, "BLOB");
        JdbcTypeNameTranslator.typeToName.put(16, "BOOLEAN");
        JdbcTypeNameTranslator.typeToName.put(1, "CHAR");
        JdbcTypeNameTranslator.typeToName.put(2005, "CLOB");
        JdbcTypeNameTranslator.typeToName.put(70, "DATALINK");
        JdbcTypeNameTranslator.typeToName.put(91, "DATE");
        JdbcTypeNameTranslator.typeToName.put(3, "DECIMAL");
        JdbcTypeNameTranslator.typeToName.put(2001, "DISTINCT");
        JdbcTypeNameTranslator.typeToName.put(8, "DOUBLE");
        JdbcTypeNameTranslator.typeToName.put(6, "FLOAT");
        JdbcTypeNameTranslator.typeToName.put(4, "INTEGER");
        JdbcTypeNameTranslator.typeToName.put(2000, "JAVA_OBJECT");
        JdbcTypeNameTranslator.typeToName.put(-4, "LONGVARBINARY");
        JdbcTypeNameTranslator.typeToName.put(-1, "LONGVARCHAR");
        JdbcTypeNameTranslator.typeToName.put(-15, "NCHAR");
        JdbcTypeNameTranslator.typeToName.put(2011, "NCLOB");
        JdbcTypeNameTranslator.typeToName.put(-9, "NVARCHAR");
        JdbcTypeNameTranslator.typeToName.put(-16, "LONGNVARCHAR");
        JdbcTypeNameTranslator.typeToName.put(0, "NULL");
        JdbcTypeNameTranslator.typeToName.put(2, "NUMERIC");
        JdbcTypeNameTranslator.typeToName.put(1111, "OTHER");
        JdbcTypeNameTranslator.typeToName.put(7, "REAL");
        JdbcTypeNameTranslator.typeToName.put(2006, "REF");
        JdbcTypeNameTranslator.typeToName.put(5, "SMALLINT");
        JdbcTypeNameTranslator.typeToName.put(2002, "STRUCT");
        JdbcTypeNameTranslator.typeToName.put(92, "TIME");
        JdbcTypeNameTranslator.typeToName.put(93, "TIMESTAMP");
        JdbcTypeNameTranslator.typeToName.put(-6, "TINYINT");
        JdbcTypeNameTranslator.typeToName.put(-3, "VARBINARY");
        JdbcTypeNameTranslator.typeToName.put(12, "VARCHAR");
        (JdbcTypeNameTranslator.nameToType = new HashMap<String, Integer>()).put("ARRAY", 2003);
        JdbcTypeNameTranslator.nameToType.put("BIGINT", -5);
        JdbcTypeNameTranslator.nameToType.put("BINARY", -2);
        JdbcTypeNameTranslator.nameToType.put("BIT", -7);
        JdbcTypeNameTranslator.nameToType.put("BLOB", 2004);
        JdbcTypeNameTranslator.nameToType.put("BOOLEAN", 16);
        JdbcTypeNameTranslator.nameToType.put("CHAR", 1);
        JdbcTypeNameTranslator.nameToType.put("CLOB", 2005);
        JdbcTypeNameTranslator.nameToType.put("DATALINK", 70);
        JdbcTypeNameTranslator.nameToType.put("DATE", 91);
        JdbcTypeNameTranslator.nameToType.put("DECIMAL", 3);
        JdbcTypeNameTranslator.nameToType.put("DISTINCT", 2001);
        JdbcTypeNameTranslator.nameToType.put("DOUBLE", 8);
        JdbcTypeNameTranslator.nameToType.put("FLOAT", 6);
        JdbcTypeNameTranslator.nameToType.put("INTEGER", 4);
        JdbcTypeNameTranslator.nameToType.put("JAVA_OBJECT", 2000);
        JdbcTypeNameTranslator.nameToType.put("LONGVARBINARY", -4);
        JdbcTypeNameTranslator.nameToType.put("LONGVARCHAR", -1);
        JdbcTypeNameTranslator.nameToType.put("NCHAR", -15);
        JdbcTypeNameTranslator.nameToType.put("NCLOB", 2011);
        JdbcTypeNameTranslator.nameToType.put("NVARCHAR", -9);
        JdbcTypeNameTranslator.nameToType.put("LONGNVARCHAR", -16);
        JdbcTypeNameTranslator.nameToType.put("NULL", 0);
        JdbcTypeNameTranslator.nameToType.put("NUMERIC", 2);
        JdbcTypeNameTranslator.nameToType.put("OTHER", 1111);
        JdbcTypeNameTranslator.nameToType.put("REAL", 7);
        JdbcTypeNameTranslator.nameToType.put("REF", 2006);
        JdbcTypeNameTranslator.nameToType.put("SMALLINT", 5);
        JdbcTypeNameTranslator.nameToType.put("STRUCT", 2002);
        JdbcTypeNameTranslator.nameToType.put("TIME", 92);
        JdbcTypeNameTranslator.nameToType.put("TIMESTAMP", 93);
        JdbcTypeNameTranslator.nameToType.put("TINYINT", -6);
        JdbcTypeNameTranslator.nameToType.put("VARBINARY", -3);
        JdbcTypeNameTranslator.nameToType.put("VARCHAR", 12);
    }
}

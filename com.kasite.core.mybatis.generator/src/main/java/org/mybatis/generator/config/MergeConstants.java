package org.mybatis.generator.config;

public class MergeConstants
{
    public static final String[] OLD_XML_ELEMENT_PREFIXES;
    public static final String NEW_ELEMENT_TAG = "@mbg.generated";
    public static final String[] OLD_ELEMENT_TAGS;
    
    static {
        OLD_XML_ELEMENT_PREFIXES = new String[] { "ibatorgenerated_", "abatorgenerated_" };
        OLD_ELEMENT_TAGS = new String[] { "@ibatorgenerated", "@abatorgenerated", "@mbggenerated", "@mbg.generated" };
    }
}

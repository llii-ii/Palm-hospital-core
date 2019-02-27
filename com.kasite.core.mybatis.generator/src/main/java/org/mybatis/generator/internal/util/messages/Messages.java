package org.mybatis.generator.internal.util.messages;

import java.util.*;
import java.text.*;

public class Messages
{
    private static final String BUNDLE_NAME = "org.mybatis.generator.internal.util.messages.messages";
    private static final ResourceBundle RESOURCE_BUNDLE;
    
    public static String getString(final String key) {
        try {
            return Messages.RESOURCE_BUNDLE.getString(key);
        }
        catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
    
    public static String getString(final String key, final String parm1) {
        try {
            return MessageFormat.format(Messages.RESOURCE_BUNDLE.getString(key), parm1);
        }
        catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
    
    public static String getString(final String key, final String parm1, final String parm2) {
        try {
            return MessageFormat.format(Messages.RESOURCE_BUNDLE.getString(key), parm1, parm2);
        }
        catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
    
    public static String getString(final String key, final String parm1, final String parm2, final String parm3) {
        try {
            return MessageFormat.format(Messages.RESOURCE_BUNDLE.getString(key), parm1, parm2, parm3);
        }
        catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
    
    static {
        RESOURCE_BUNDLE = ResourceBundle.getBundle("org.mybatis.generator.internal.util.messages.messages");
    }
}

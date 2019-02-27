package org.mybatis.generator.api.dom.java;

import java.util.*;

public class JavaReservedWords
{
    private static Set<String> RESERVED_WORDS;
    
    public static boolean containsWord(final String word) {
        final boolean rc = word != null && JavaReservedWords.RESERVED_WORDS.contains(word);
        return rc;
    }
    
    static {
        final String[] words = { "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while" };
        JavaReservedWords.RESERVED_WORDS = new HashSet<String>(words.length);
        for (final String word : words) {
            JavaReservedWords.RESERVED_WORDS.add(word);
        }
    }
}

package org.mybatis.generator.internal.util;

import java.lang.reflect.*;

public final class HashCodeUtil
{
    public static final int SEED = 23;
    private static final int ODD_PRIME_NUMBER = 37;
    
    public static int hash(final int seed, final boolean b) {
        return firstTerm(seed) + (b ? 1 : 0);
    }
    
    public static int hash(final int seed, final char c) {
        return firstTerm(seed) + c;
    }
    
    public static int hash(final int seed, final int i) {
        return firstTerm(seed) + i;
    }
    
    public static int hash(final int seed, final long l) {
        return firstTerm(seed) + (int)(l ^ l >>> 32);
    }
    
    public static int hash(final int seed, final float f) {
        return hash(seed, Float.floatToIntBits(f));
    }
    
    public static int hash(final int seed, final double d) {
        return hash(seed, Double.doubleToLongBits(d));
    }
    
    public static int hash(final int seed, final Object o) {
        int result = seed;
        if (o == null) {
            result = hash(result, 0);
        }
        else if (!isArray(o)) {
            result = hash(result, o.hashCode());
        }
        else {
            for (int length = Array.getLength(o), idx = 0; idx < length; ++idx) {
                final Object item = Array.get(o, idx);
                result = hash(result, item);
            }
        }
        return result;
    }
    
    private static int firstTerm(final int seed) {
        return 37 * seed;
    }
    
    private static boolean isArray(final Object anObject) {
        return anObject.getClass().isArray();
    }
}

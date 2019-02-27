package org.mybatis.generator.internal.util;

public final class EqualsUtil
{
    public static boolean areEqual(final boolean b1, final boolean b2) {
        return b1 == b2;
    }
    
    public static boolean areEqual(final char c1, final char c2) {
        return c1 == c2;
    }
    
    public static boolean areEqual(final long l1, final long l2) {
        return l1 == l2;
    }
    
    public static boolean areEqual(final float f1, final float f2) {
        return Float.floatToIntBits(f1) == Float.floatToIntBits(f2);
    }
    
    public static boolean areEqual(final double d1, final double d2) {
        return Double.doubleToLongBits(d1) == Double.doubleToLongBits(d2);
    }
    
    public static boolean areEqual(final Object o1, final Object o2) {
        return (o1 == null) ? (o2 == null) : o1.equals(o2);
    }
}

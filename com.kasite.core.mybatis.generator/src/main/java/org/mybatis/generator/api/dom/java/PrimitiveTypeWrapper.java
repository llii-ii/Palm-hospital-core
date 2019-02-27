package org.mybatis.generator.api.dom.java;

public class PrimitiveTypeWrapper extends FullyQualifiedJavaType
{
    private static PrimitiveTypeWrapper booleanInstance;
    private static PrimitiveTypeWrapper byteInstance;
    private static PrimitiveTypeWrapper characterInstance;
    private static PrimitiveTypeWrapper doubleInstance;
    private static PrimitiveTypeWrapper floatInstance;
    private static PrimitiveTypeWrapper integerInstance;
    private static PrimitiveTypeWrapper longInstance;
    private static PrimitiveTypeWrapper shortInstance;
    private String toPrimitiveMethod;
    
    private PrimitiveTypeWrapper(final String fullyQualifiedName, final String toPrimitiveMethod) {
        super(fullyQualifiedName);
        this.toPrimitiveMethod = toPrimitiveMethod;
    }
    
    public String getToPrimitiveMethod() {
        return this.toPrimitiveMethod;
    }
    
    public static PrimitiveTypeWrapper getBooleanInstance() {
        if (PrimitiveTypeWrapper.booleanInstance == null) {
            PrimitiveTypeWrapper.booleanInstance = new PrimitiveTypeWrapper("java.lang.Boolean", "booleanValue()");
        }
        return PrimitiveTypeWrapper.booleanInstance;
    }
    
    public static PrimitiveTypeWrapper getByteInstance() {
        if (PrimitiveTypeWrapper.byteInstance == null) {
            PrimitiveTypeWrapper.byteInstance = new PrimitiveTypeWrapper("java.lang.Byte", "byteValue()");
        }
        return PrimitiveTypeWrapper.byteInstance;
    }
    
    public static PrimitiveTypeWrapper getCharacterInstance() {
        if (PrimitiveTypeWrapper.characterInstance == null) {
            PrimitiveTypeWrapper.characterInstance = new PrimitiveTypeWrapper("java.lang.Character", "charValue()");
        }
        return PrimitiveTypeWrapper.characterInstance;
    }
    
    public static PrimitiveTypeWrapper getDoubleInstance() {
        if (PrimitiveTypeWrapper.doubleInstance == null) {
            PrimitiveTypeWrapper.doubleInstance = new PrimitiveTypeWrapper("java.lang.Double", "doubleValue()");
        }
        return PrimitiveTypeWrapper.doubleInstance;
    }
    
    public static PrimitiveTypeWrapper getFloatInstance() {
        if (PrimitiveTypeWrapper.floatInstance == null) {
            PrimitiveTypeWrapper.floatInstance = new PrimitiveTypeWrapper("java.lang.Float", "floatValue()");
        }
        return PrimitiveTypeWrapper.floatInstance;
    }
    
    public static PrimitiveTypeWrapper getIntegerInstance() {
        if (PrimitiveTypeWrapper.integerInstance == null) {
            PrimitiveTypeWrapper.integerInstance = new PrimitiveTypeWrapper("java.lang.Integer", "intValue()");
        }
        return PrimitiveTypeWrapper.integerInstance;
    }
    
    public static PrimitiveTypeWrapper getLongInstance() {
        if (PrimitiveTypeWrapper.longInstance == null) {
            PrimitiveTypeWrapper.longInstance = new PrimitiveTypeWrapper("java.lang.Long", "longValue()");
        }
        return PrimitiveTypeWrapper.longInstance;
    }
    
    public static PrimitiveTypeWrapper getShortInstance() {
        if (PrimitiveTypeWrapper.shortInstance == null) {
            PrimitiveTypeWrapper.shortInstance = new PrimitiveTypeWrapper("java.lang.Short", "shortValue()");
        }
        return PrimitiveTypeWrapper.shortInstance;
    }
}

package org.mybatis.generator.api.dom.java;

import org.mybatis.generator.internal.util.messages.*;
import java.util.*;
import org.mybatis.generator.internal.util.*;
import java.io.*;

public class FullyQualifiedJavaType implements Comparable<FullyQualifiedJavaType>
{
    private static final String JAVA_LANG = "java.lang";
    private static FullyQualifiedJavaType intInstance;
    private static FullyQualifiedJavaType stringInstance;
    private static FullyQualifiedJavaType booleanPrimitiveInstance;
    private static FullyQualifiedJavaType objectInstance;
    private static FullyQualifiedJavaType dateInstance;
    private static FullyQualifiedJavaType criteriaInstance;
    private static FullyQualifiedJavaType generatedCriteriaInstance;
    private String baseShortName;
    private String baseQualifiedName;
    private boolean explicitlyImported;
    private String packageName;
    private boolean primitive;
    private boolean isArray;
    private PrimitiveTypeWrapper primitiveTypeWrapper;
    private List<FullyQualifiedJavaType> typeArguments;
    private boolean wildcardType;
    private boolean boundedWildcard;
    private boolean extendsBoundedWildcard;
    
    public FullyQualifiedJavaType(final String fullTypeSpecification) {
        this.typeArguments = new ArrayList<FullyQualifiedJavaType>();
        this.parse(fullTypeSpecification);
    }
    
    public boolean isExplicitlyImported() {
        return this.explicitlyImported;
    }
    
    public String getFullyQualifiedName() {
        final StringBuilder sb = new StringBuilder();
        if (this.wildcardType) {
            sb.append('?');
            if (this.boundedWildcard) {
                if (this.extendsBoundedWildcard) {
                    sb.append(" extends ");
                }
                else {
                    sb.append(" super ");
                }
                sb.append(this.baseQualifiedName);
            }
        }
        else {
            sb.append(this.baseQualifiedName);
        }
        if (this.typeArguments.size() > 0) {
            boolean first = true;
            sb.append('<');
            for (final FullyQualifiedJavaType fqjt : this.typeArguments) {
                if (first) {
                    first = false;
                }
                else {
                    sb.append(", ");
                }
                sb.append(fqjt.getFullyQualifiedName());
            }
            sb.append('>');
        }
        return sb.toString();
    }
    
    public String getFullyQualifiedNameWithoutTypeParameters() {
        return this.baseQualifiedName;
    }
    
    public List<String> getImportList() {
        final List<String> answer = new ArrayList<String>();
        if (this.isExplicitlyImported()) {
            final int index = this.baseShortName.indexOf(46);
            if (index == -1) {
                answer.add(this.calculateActualImport(this.baseQualifiedName));
            }
            else {
                final StringBuilder sb = new StringBuilder();
                sb.append(this.packageName);
                sb.append('.');
                sb.append(this.calculateActualImport(this.baseShortName.substring(0, index)));
                answer.add(sb.toString());
            }
        }
        for (final FullyQualifiedJavaType fqjt : this.typeArguments) {
            answer.addAll(fqjt.getImportList());
        }
        return answer;
    }
    
    private String calculateActualImport(final String name) {
        String answer = name;
        if (this.isArray()) {
            final int index = name.indexOf("[");
            if (index != -1) {
                answer = name.substring(0, index);
            }
        }
        return answer;
    }
    
    public String getPackageName() {
        return this.packageName;
    }
    
    public String getShortName() {
        final StringBuilder sb = new StringBuilder();
        if (this.wildcardType) {
            sb.append('?');
            if (this.boundedWildcard) {
                if (this.extendsBoundedWildcard) {
                    sb.append(" extends ");
                }
                else {
                    sb.append(" super ");
                }
                sb.append(this.baseShortName);
            }
        }
        else {
            sb.append(this.baseShortName);
        }
        if (this.typeArguments.size() > 0) {
            boolean first = true;
            sb.append('<');
            for (final FullyQualifiedJavaType fqjt : this.typeArguments) {
                if (first) {
                    first = false;
                }
                else {
                    sb.append(", ");
                }
                sb.append(fqjt.getShortName());
            }
            sb.append('>');
        }
        return sb.toString();
    }
    
    public String getShortNameWithoutTypeArguments() {
        return this.baseShortName;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FullyQualifiedJavaType)) {
            return false;
        }
        final FullyQualifiedJavaType other = (FullyQualifiedJavaType)obj;
        return this.getFullyQualifiedName().equals(other.getFullyQualifiedName());
    }
    
    @Override
    public int hashCode() {
        return this.getFullyQualifiedName().hashCode();
    }
    
    @Override
    public String toString() {
        return this.getFullyQualifiedName();
    }
    
    public boolean isPrimitive() {
        return this.primitive;
    }
    
    public PrimitiveTypeWrapper getPrimitiveTypeWrapper() {
        return this.primitiveTypeWrapper;
    }
    
    public static final FullyQualifiedJavaType getIntInstance() {
        if (FullyQualifiedJavaType.intInstance == null) {
            FullyQualifiedJavaType.intInstance = new FullyQualifiedJavaType("int");
        }
        return FullyQualifiedJavaType.intInstance;
    }
    
    public static final FullyQualifiedJavaType getNewMapInstance() {
        return new FullyQualifiedJavaType("java.util.Map");
    }
    
    public static final FullyQualifiedJavaType getNewListInstance() {
        return new FullyQualifiedJavaType("java.util.List");
    }
    
    public static final FullyQualifiedJavaType getNewHashMapInstance() {
        return new FullyQualifiedJavaType("java.util.HashMap");
    }
    
    public static final FullyQualifiedJavaType getNewArrayListInstance() {
        return new FullyQualifiedJavaType("java.util.ArrayList");
    }
    
    public static final FullyQualifiedJavaType getNewIteratorInstance() {
        return new FullyQualifiedJavaType("java.util.Iterator");
    }
    
    public static final FullyQualifiedJavaType getStringInstance() {
        if (FullyQualifiedJavaType.stringInstance == null) {
            FullyQualifiedJavaType.stringInstance = new FullyQualifiedJavaType("java.lang.String");
        }
        return FullyQualifiedJavaType.stringInstance;
    }
    
    public static final FullyQualifiedJavaType getBooleanPrimitiveInstance() {
        if (FullyQualifiedJavaType.booleanPrimitiveInstance == null) {
            FullyQualifiedJavaType.booleanPrimitiveInstance = new FullyQualifiedJavaType("boolean");
        }
        return FullyQualifiedJavaType.booleanPrimitiveInstance;
    }
    
    public static final FullyQualifiedJavaType getObjectInstance() {
        if (FullyQualifiedJavaType.objectInstance == null) {
            FullyQualifiedJavaType.objectInstance = new FullyQualifiedJavaType("java.lang.Object");
        }
        return FullyQualifiedJavaType.objectInstance;
    }
    
    public static final FullyQualifiedJavaType getDateInstance() {
        if (FullyQualifiedJavaType.dateInstance == null) {
            FullyQualifiedJavaType.dateInstance = new FullyQualifiedJavaType("java.util.Date");
        }
        return FullyQualifiedJavaType.dateInstance;
    }
    
    public static final FullyQualifiedJavaType getCriteriaInstance() {
        if (FullyQualifiedJavaType.criteriaInstance == null) {
            FullyQualifiedJavaType.criteriaInstance = new FullyQualifiedJavaType("Criteria");
        }
        return FullyQualifiedJavaType.criteriaInstance;
    }
    
    public static final FullyQualifiedJavaType getGeneratedCriteriaInstance() {
        if (FullyQualifiedJavaType.generatedCriteriaInstance == null) {
            FullyQualifiedJavaType.generatedCriteriaInstance = new FullyQualifiedJavaType("GeneratedCriteria");
        }
        return FullyQualifiedJavaType.generatedCriteriaInstance;
    }
    
    @Override
    public int compareTo(final FullyQualifiedJavaType other) {
        return this.getFullyQualifiedName().compareTo(other.getFullyQualifiedName());
    }
    
    public void addTypeArgument(final FullyQualifiedJavaType type) {
        this.typeArguments.add(type);
    }
    
    private void parse(final String fullTypeSpecification) {
        String spec = fullTypeSpecification.trim();
        if (spec.startsWith("?")) {
            this.wildcardType = true;
            spec = spec.substring(1).trim();
            if (spec.startsWith("extends ")) {
                this.boundedWildcard = true;
                this.extendsBoundedWildcard = true;
                spec = spec.substring(8);
            }
            else if (spec.startsWith("super ")) {
                this.boundedWildcard = true;
                this.extendsBoundedWildcard = false;
                spec = spec.substring(6);
            }
            else {
                this.boundedWildcard = false;
            }
            this.parse(spec);
        }
        else {
            final int index = fullTypeSpecification.indexOf(60);
            if (index == -1) {
                this.simpleParse(fullTypeSpecification);
            }
            else {
                this.simpleParse(fullTypeSpecification.substring(0, index));
                final int endIndex = fullTypeSpecification.lastIndexOf(62);
                if (endIndex == -1) {
                    throw new RuntimeException(Messages.getString("RuntimeError.22", fullTypeSpecification));
                }
                this.genericParse(fullTypeSpecification.substring(index, endIndex + 1));
            }
            this.isArray = fullTypeSpecification.endsWith("]");
        }
    }
    
    private void simpleParse(final String typeSpecification) {
        this.baseQualifiedName = typeSpecification.trim();
        if (this.baseQualifiedName.contains(".")) {
            this.packageName = getPackage(this.baseQualifiedName);
            this.baseShortName = this.baseQualifiedName.substring(this.packageName.length() + 1);
            final int index = this.baseShortName.lastIndexOf(46);
            if (index != -1) {
                this.baseShortName = this.baseShortName.substring(index + 1);
            }
            if ("java.lang".equals(this.packageName)) {
                this.explicitlyImported = false;
            }
            else {
                this.explicitlyImported = true;
            }
        }
        else {
            this.baseShortName = this.baseQualifiedName;
            this.explicitlyImported = false;
            this.packageName = "";
            if ("byte".equals(this.baseQualifiedName)) {
                this.primitive = true;
                this.primitiveTypeWrapper = PrimitiveTypeWrapper.getByteInstance();
            }
            else if ("short".equals(this.baseQualifiedName)) {
                this.primitive = true;
                this.primitiveTypeWrapper = PrimitiveTypeWrapper.getShortInstance();
            }
            else if ("int".equals(this.baseQualifiedName)) {
                this.primitive = true;
                this.primitiveTypeWrapper = PrimitiveTypeWrapper.getIntegerInstance();
            }
            else if ("long".equals(this.baseQualifiedName)) {
                this.primitive = true;
                this.primitiveTypeWrapper = PrimitiveTypeWrapper.getLongInstance();
            }
            else if ("char".equals(this.baseQualifiedName)) {
                this.primitive = true;
                this.primitiveTypeWrapper = PrimitiveTypeWrapper.getCharacterInstance();
            }
            else if ("float".equals(this.baseQualifiedName)) {
                this.primitive = true;
                this.primitiveTypeWrapper = PrimitiveTypeWrapper.getFloatInstance();
            }
            else if ("double".equals(this.baseQualifiedName)) {
                this.primitive = true;
                this.primitiveTypeWrapper = PrimitiveTypeWrapper.getDoubleInstance();
            }
            else if ("boolean".equals(this.baseQualifiedName)) {
                this.primitive = true;
                this.primitiveTypeWrapper = PrimitiveTypeWrapper.getBooleanInstance();
            }
            else {
                this.primitive = false;
                this.primitiveTypeWrapper = null;
            }
        }
    }
    
    private void genericParse(final String genericSpecification) {
        final int lastIndex = genericSpecification.lastIndexOf(62);
        if (lastIndex == -1) {
            throw new RuntimeException(Messages.getString("RuntimeError.22", genericSpecification));
        }
        final String argumentString = genericSpecification.substring(1, lastIndex);
        final StringTokenizer st = new StringTokenizer(argumentString, ",<>", true);
        int openCount = 0;
        final StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens()) {
            final String token = st.nextToken();
            if ("<".equals(token)) {
                sb.append(token);
                ++openCount;
            }
            else if (">".equals(token)) {
                sb.append(token);
                --openCount;
            }
            else if (",".equals(token)) {
                if (openCount == 0) {
                    this.typeArguments.add(new FullyQualifiedJavaType(sb.toString()));
                    sb.setLength(0);
                }
                else {
                    sb.append(token);
                }
            }
            else {
                sb.append(token);
            }
        }
        if (openCount != 0) {
            throw new RuntimeException(Messages.getString("RuntimeError.22", genericSpecification));
        }
        final String finalType = sb.toString();
        if (StringUtility.stringHasValue(finalType)) {
            this.typeArguments.add(new FullyQualifiedJavaType(finalType));
        }
    }
    
    private static String getPackage(final String baseQualifiedName) {
        final int index = baseQualifiedName.lastIndexOf(46);
        return baseQualifiedName.substring(0, index);
    }
    
    public boolean isArray() {
        return this.isArray;
    }
    
    public List<FullyQualifiedJavaType> getTypeArguments() {
        return this.typeArguments;
    }
    
    static {
        FullyQualifiedJavaType.intInstance = null;
        FullyQualifiedJavaType.stringInstance = null;
        FullyQualifiedJavaType.booleanPrimitiveInstance = null;
        FullyQualifiedJavaType.objectInstance = null;
        FullyQualifiedJavaType.dateInstance = null;
        FullyQualifiedJavaType.criteriaInstance = null;
        FullyQualifiedJavaType.generatedCriteriaInstance = null;
    }

	public void setBaseShortName(String baseShortName) {
		this.baseShortName = baseShortName;
	}

	public void setBaseQualifiedName(String baseQualifiedName) {
		this.baseQualifiedName = baseQualifiedName;
	}
    
}

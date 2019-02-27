package org.mybatis.generator.api.dom.java;

import java.util.*;

public class JavaDomUtils
{
    public static String calculateTypeName(final CompilationUnit compilationUnit, final FullyQualifiedJavaType fqjt) {
        if (fqjt.getTypeArguments().size() > 0) {
            return calculateParameterizedTypeName(compilationUnit, fqjt);
        }
        if (compilationUnit == null || typeDoesNotRequireImport(fqjt) || typeIsInSamePackage(compilationUnit, fqjt) || typeIsAlreadyImported(compilationUnit, fqjt)) {
            return fqjt.getShortName();
        }
        return fqjt.getFullyQualifiedName();
    }
    
    private static String calculateParameterizedTypeName(final CompilationUnit compilationUnit, final FullyQualifiedJavaType fqjt) {
        final String baseTypeName = calculateTypeName(compilationUnit, new FullyQualifiedJavaType(fqjt.getFullyQualifiedNameWithoutTypeParameters()));
        final StringBuilder sb = new StringBuilder();
        sb.append(baseTypeName);
        sb.append('<');
        boolean comma = false;
        for (final FullyQualifiedJavaType ft : fqjt.getTypeArguments()) {
            if (comma) {
                sb.append(", ");
            }
            else {
                comma = true;
            }
            sb.append(calculateTypeName(compilationUnit, ft));
        }
        sb.append('>');
        return sb.toString();
    }
    
    private static boolean typeDoesNotRequireImport(final FullyQualifiedJavaType fullyQualifiedJavaType) {
        return fullyQualifiedJavaType.isPrimitive() || !fullyQualifiedJavaType.isExplicitlyImported();
    }
    
    private static boolean typeIsInSamePackage(final CompilationUnit compilationUnit, final FullyQualifiedJavaType fullyQualifiedJavaType) {
        return fullyQualifiedJavaType.getPackageName().equals(compilationUnit.getType().getPackageName());
    }
    
    private static boolean typeIsAlreadyImported(final CompilationUnit compilationUnit, final FullyQualifiedJavaType fullyQualifiedJavaType) {
        final FullyQualifiedJavaType nonGenericType = new FullyQualifiedJavaType(fullyQualifiedJavaType.getFullyQualifiedNameWithoutTypeParameters());
        return compilationUnit.getImportedTypes().contains(nonGenericType);
    }
}

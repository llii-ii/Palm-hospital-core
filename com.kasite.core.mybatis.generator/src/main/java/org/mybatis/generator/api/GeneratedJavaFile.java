package org.mybatis.generator.api;

import org.mybatis.generator.api.dom.java.*;

public class GeneratedJavaFile extends GeneratedFile
{
    private CompilationUnit compilationUnit;
    private String fileEncoding;
    private JavaFormatter javaFormatter;
    
    public GeneratedJavaFile(final CompilationUnit compilationUnit, final String targetProject, final String fileEncoding, final JavaFormatter javaFormatter) {
        super(targetProject);
        this.compilationUnit = compilationUnit;
        this.fileEncoding = fileEncoding;
        this.javaFormatter = javaFormatter;
    }
    
    public GeneratedJavaFile(final CompilationUnit compilationUnit, final String targetProject, final JavaFormatter javaFormatter) {
        this(compilationUnit, targetProject, null, javaFormatter);
    }
    
    @Override
    public String getFormattedContent() {
        return this.javaFormatter.getFormattedContent(this.compilationUnit);
    }
    
    @Override
    public String getFileName() {
        return this.compilationUnit.getType().getShortNameWithoutTypeArguments() + ".java";
    }
    
    @Override
    public String getTargetPackage() {
        return this.compilationUnit.getType().getPackageName();
    }
    
    public CompilationUnit getCompilationUnit() {
        return this.compilationUnit;
    }
    
    @Override
    public boolean isMergeable() {
        return true;
    }
    
    public String getFileEncoding() {
        return this.fileEncoding;
    }
}

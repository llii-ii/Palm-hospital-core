package org.mybatis.generator.api;

public abstract class GeneratedFile
{
    protected String targetProject;
    
    public GeneratedFile(final String targetProject) {
        this.targetProject = targetProject;
    }
    
    public abstract String getFormattedContent();
    
    public abstract String getFileName();
    
    public String getTargetProject() {
        return this.targetProject;
    }
    
    public abstract String getTargetPackage();
    
    @Override
    public String toString() {
        return this.getFormattedContent();
    }
    
    public abstract boolean isMergeable();
}

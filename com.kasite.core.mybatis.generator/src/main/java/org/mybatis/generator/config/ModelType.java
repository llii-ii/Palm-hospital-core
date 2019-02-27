package org.mybatis.generator.config;

import org.mybatis.generator.internal.util.messages.*;

public enum ModelType
{
    HIERARCHICAL("hierarchical"), 
    FLAT("flat"), 
    CONDITIONAL("conditional");
    
    private final String modelType;
    
    private ModelType(final String modelType) {
        this.modelType = modelType;
    }
    
    public String getModelType() {
        return this.modelType;
    }
    
    public static ModelType getModelType(final String type) {
        if (ModelType.HIERARCHICAL.getModelType().equalsIgnoreCase(type)) {
            return ModelType.HIERARCHICAL;
        }
        if (ModelType.FLAT.getModelType().equalsIgnoreCase(type)) {
            return ModelType.FLAT;
        }
        if (ModelType.CONDITIONAL.getModelType().equalsIgnoreCase(type)) {
            return ModelType.CONDITIONAL;
        }
        throw new RuntimeException(Messages.getString("RuntimeError.13", type));
    }
}

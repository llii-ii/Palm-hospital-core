package org.mybatis.generator.api;

public interface DAOMethodNameCalculator
{
    String getInsertMethodName(final IntrospectedTable p0);
    
    String getInsertSelectiveMethodName(final IntrospectedTable p0);
    
    String getUpdateByPrimaryKeyWithoutBLOBsMethodName(final IntrospectedTable p0);
    
    String getUpdateByPrimaryKeyWithBLOBsMethodName(final IntrospectedTable p0);
    
    String getUpdateByPrimaryKeySelectiveMethodName(final IntrospectedTable p0);
    
    String getSelectByPrimaryKeyMethodName(final IntrospectedTable p0);
    
    String getSelectByExampleWithoutBLOBsMethodName(final IntrospectedTable p0);
    
    String getSelectByExampleWithBLOBsMethodName(final IntrospectedTable p0);
    
    String getDeleteByPrimaryKeyMethodName(final IntrospectedTable p0);
    
    String getDeleteByExampleMethodName(final IntrospectedTable p0);
    
    String getCountByExampleMethodName(final IntrospectedTable p0);
    
    String getUpdateByExampleSelectiveMethodName(final IntrospectedTable p0);
    
    String getUpdateByExampleWithBLOBsMethodName(final IntrospectedTable p0);
    
    String getUpdateByExampleWithoutBLOBsMethodName(final IntrospectedTable p0);
}

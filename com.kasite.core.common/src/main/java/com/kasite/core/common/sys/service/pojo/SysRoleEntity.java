package com.kasite.core.common.sys.service.pojo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 角色
 * 
 */
@Table(name="SYS_ROLE") 
public class SysRoleEntity extends BaseDbo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @Id
	@KeySql(useGeneratedKeys=true)
    @Column(name="ROLE_ID")
    private Long roleId;

    /**
     * 角色名称
     */
    @Column(name="ROLE_NAME")
    private String roleName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 部门ID
     */
    @Column(name="DEPT_ID")
    private Long deptId;

    /**
     * 部门名称
     */
    @Transient
    private String deptName;
    
    @Transient
    private List<Long> menuIdList;
    
    @Transient
    private List<Long> deptIdList;



    /**
     * 设置：备注
     * 
     * @param remark
     *            备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取：备注
     * 
     * @return String
     */
    public String getRemark() {
        return remark;
    }


    public List<Long> getMenuIdList() {
        return menuIdList;
    }

    public void setMenuIdList(List<Long> menuIdList) {
        this.menuIdList = menuIdList;
    }


    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public List<Long> getDeptIdList() {
        return deptIdList;
    }

    public void setDeptIdList(List<Long> deptIdList) {
        this.deptIdList = deptIdList;
    }

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
    
    
}

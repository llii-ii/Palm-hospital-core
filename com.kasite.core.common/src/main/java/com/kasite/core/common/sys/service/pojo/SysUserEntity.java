package com.kasite.core.common.sys.service.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 系统用户
 */
@Table(name="SYS_USER") 
public class SysUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户ID
	 */
	@Id
	@KeySql(useGeneratedKeys=true)
	private Long id;
	/**
	 * 	渠道信息 configKey 
	 */
	private String channelId;
	
	/**
	 * 兼容旧的
	 */
	private String clientId;

	/**
	 * 渠道类型
	 */
	private String channelName;
	
	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;
	/**
	 * 真实姓名
	 */
	private String realName;

	/**
	 * 盐
	 */
	private String salt;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 状态  0：禁用   1：正常
	 */
	private Integer status;
	
	/**
	 * 角色ID
	 */
	@Transient
	private Long roleId;
	@Transient
	private String roleName;

	/**
	 * 部门名称
	 */
	private String deptName;
	/**
	 * 机构名称
	 */
	private String orgName;
	
	/**
	 * 创建者ID
	 */
	private String operatorId;
	/**
	 * 创建者ID
	 */
	private String operatorName;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;

	/**
	 * 部门ID
	 */
	private String deptId;
	/**
	 * 机构ID
	 */
    private String orgId;
    
    
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
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

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 设置：用户名
	 * @param username 用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 获取：用户名
	 * @return String
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * 设置：密码
	 * @param password 密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 获取：密码
	 * @return String
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * 设置：邮箱
	 * @param email 邮箱
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 获取：邮箱
	 * @return String
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * 设置：手机号
	 * @param mobile 手机号
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * 获取：手机号
	 * @return String
	 */
	public String getMobile() {
		return mobile;
	}
	
	/**
	 * 设置：状态  0：禁用   1：正常
	 * @param status 状态  0：禁用   1：正常
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 获取：状态  0：禁用   1：正常
	 * @return Integer
	 */
	public Integer getStatus() {
		return status;
	}
	 
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
}

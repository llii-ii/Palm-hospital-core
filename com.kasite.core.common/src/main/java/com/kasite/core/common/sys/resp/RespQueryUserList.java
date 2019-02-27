package com.kasite.core.common.sys.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespQueryUserList
 * @author: lcz
 * @date: 2018年8月28日 下午8:16:16
 */
public class RespQueryUserList extends AbsResp{
	/**
	 * 用户ID
	 */
	private Long id;
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
	 * 	渠道信息
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
	private Long roleId;
	/**
	 * 角色名称
	 */
	private String roleName;

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
	 * 机构ID
	 */
    private String orgId;
    
    
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
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
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
    
    
}

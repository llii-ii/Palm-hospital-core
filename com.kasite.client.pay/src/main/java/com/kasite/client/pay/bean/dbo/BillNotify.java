package com.kasite.client.pay.bean.dbo;

import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * @author linjianfa
 * @Description: 账单通知
 * @version: V1.0  
 * 2017-6-21 下午8:03:31
 */


@Table(name="P_BILL_NOTIFY")
public class BillNotify {

	
	/**主键*/

	@Id
	@KeySql(useGeneratedKeys=true)
	private String id;

		/**科室名称*/
		private String deptName;

		/**姓名*/
		private String name;

		/**联系方式（手机号）*/
		private String mobile;

		/**新增日期*/
		private Timestamp createDate;

		/**是否删除*/
		private Integer isDeleted;

		/**更新时间*/
		private Timestamp updateDate;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}
	
	
	

}

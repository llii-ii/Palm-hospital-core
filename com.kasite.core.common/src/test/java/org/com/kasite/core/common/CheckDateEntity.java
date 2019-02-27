package org.com.kasite.core.common;

import javax.validation.constraints.NotBlank;

import com.kasite.core.common.validator.CheckCurrency;
import com.kasite.core.common.validator.CheckDate;
import com.kasite.core.common.validator.CheckEnum;
import com.kasite.core.common.validator.group.AddGroup;

public class CheckDateEntity {

	@CheckCurrency(message="price 格式不正确",groups=AddGroup.class)
	private Integer price;
	@CheckDate(message="birthday 格式不正确" ,groups=AddGroup.class, format="YYYY-MM-DD HH:mm:ss")
	private String birthday;
	@CheckEnum(message="订单状态",groups=AddGroup.class,inf=CheckDateTest.OrderStateEnum.class)
	private Integer orderState;
	/**
	 * 科室名称
	 */
	@NotBlank(message="科室名称 deptName 不能为空", groups = {AddGroup.class})
	private String deptName;
	
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Integer getOrderState() {
		return orderState;
	}

	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}
	
}

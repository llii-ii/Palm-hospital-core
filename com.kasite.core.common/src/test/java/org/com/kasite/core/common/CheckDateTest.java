package org.com.kasite.core.common;

import com.kasite.core.common.validator.ICheckEnumInf;
import com.kasite.core.common.validator.ValidatorUtils;
import com.kasite.core.common.validator.group.AddGroup;

public class CheckDateTest {

	public enum OrderStateEnum implements ICheckEnumInf {
		ORDERSTATE_0(0,"待支付"),
		ORDERSTATE_1(1,"已支付"),
		ORDERSTATE_2(2,"已完成"),
		ORDERSTATE_3(3,"退款中"),
		ORDERSTATE_4(4,"已退款"),
		;
		private int state;
		private String name;
		OrderStateEnum(int state,String name){
			this.state = state;
			this.name = name;
		}
		public int getState() {
			return state;
		}
		@Override
		public String getName() {
			return name;
		}
		public static OrderStateEnum valuesOf(int state) {
			for (OrderStateEnum f : OrderStateEnum.values()) {
				if(f.state == state) {
					return f;
				}
			}
			return null;
		}
		public String des() {
			StringBuffer sbf = new StringBuffer();
			for (OrderStateEnum f : OrderStateEnum.values()) {
				sbf.append(f.getState()).append(":").append(f.getName());
			}
			return sbf.toString();
		}
	}
	
	public static void main(String[] args) {
		CheckDateEntity entity = new CheckDateEntity();
		entity.setBirthday("2018-01-11");
		entity.setOrderState(33);
		ValidatorUtils.validateEntity(entity,AddGroup.class);
		System.out.println(entity.getBirthday());
	}
}

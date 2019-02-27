package com.kasite.core.serviceinterface.module.his.resp;

import com.kasite.core.common.validator.ICheckEnumInf;

/**
 * HIS预约记录状态
 * @author daiyanshui
 */
public enum HisRegFlag implements ICheckEnumInf {
	/**已锁号（待支付）*/
	state_0(0,"已锁号"),
	/**正常（已挂号）*/
	state_1(1,"已挂号"),
	/**已退号*/
	state_2(2,"已退号"),
	/**已停诊*/
	state_3(3,"停诊"),
	/**替诊*/
	state_4(4,"替诊"),
	/**已取号**/
	state_5(5,"已取号"),
	/**已就诊**/
	state_6(6,"已就诊"),
	;
	private int state;
	private String name;
	private HisRegFlag(int state,String name) {
		this.state = state;
		this.name = name;
	}
	@Override
	public String getName() {
		return name;
	}
	public int getState() {
		return state;
	}
	public static HisRegFlag valuesOf(int state) {
		for (HisRegFlag f : HisRegFlag.values()) {
			if(f.state == state) {
				return f;
			}
		}
		return null;
	}
	public String des() {
		StringBuffer sbf = new StringBuffer();
		for (HisRegFlag f : HisRegFlag.values()) {
			sbf.append(f.getState()).append(":").append(f.getName());
		}
		return sbf.toString();
	}
	
	
	
	
}

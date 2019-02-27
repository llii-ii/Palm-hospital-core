package com.kasite.core.serviceinterface.module.his.resp;

import com.kasite.core.common.validator.ICheckEnumInf;

/**
 * HIS排班状态
 * @author daiyanshui
 *排班时段：1 上午 2下午 3 晚上 0全天
 */
public enum HisSchTimeId implements ICheckEnumInf {
	TimeSlice_1(1,"上午"),
	TimeSlice_2(2,"下午"),
	TimeSlice_0(0,"全天"),
	;
	private int state;
	private String name;
	private HisSchTimeId(int state,String name) {
		this.state = state;
	}
	public int getState() {
		return state;
	}
	public String getName() {
		return name;
	}
	public static HisSchTimeId valuesOf(int state) {
		for (HisSchTimeId f : HisSchTimeId.values()) {
			if(f.state == state) {
				return f;
			}
		}
		return null;
	}
	public  String des() {
		StringBuffer sbf = new StringBuffer();
		for (HisSchTimeId f : HisSchTimeId.values()) {
			sbf.append(f.getState()).append(":").append(f.getName());
		}
		return sbf.toString();
	}
	
	
	
}

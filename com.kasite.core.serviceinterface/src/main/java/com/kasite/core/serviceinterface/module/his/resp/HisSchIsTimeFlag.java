package com.kasite.core.serviceinterface.module.his.resp;

import com.kasite.core.common.validator.ICheckEnumInf;

/**
 * HIS排班是否分时段
 * @author daiyanshui
 *排班时段：1 分时段预约 2无分时段预约
 */
public enum HisSchIsTimeFlag implements ICheckEnumInf {
	IsTimeFlag_1(1,"分时段预约"),
	IsTimeFlag_2(2,"无分时段预约"),
	;
	private int state;
	private String name;
//	private static Map<String, String> stateMap = new HashMap<>();
//	static{
//		HisSchIsHalt[] vs = HisSchIsHalt.values();
//		for (HisSchIsHalt hisSchIsHalt : vs) {
//			int state = hisSchIsHalt.getState();
//			String name = hisSchIsHalt.getName();
//			stateMap.put(""+state, name);
//		}
//	}
	
	private HisSchIsTimeFlag(int state,String name) {
		this.state = state;
	}
	public int getState() {
		return state;
	}
	public String getName() {
		return name;
	}
	public static HisSchIsTimeFlag valuesOf(int state) {
		for (HisSchIsTimeFlag f : HisSchIsTimeFlag.values()) {
			if(f.state == state) {
				return f;
			}
		}
		return null;
	}
	public String des() {
		StringBuffer sbf = new StringBuffer();
		for (HisSchIsTimeFlag f : HisSchIsTimeFlag.values()) {
			sbf.append(f.getState()).append(":").append(f.getName());
		}
		return sbf.toString();
	}
	
	
	
}

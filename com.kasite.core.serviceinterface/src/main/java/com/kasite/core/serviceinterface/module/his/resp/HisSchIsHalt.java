package com.kasite.core.serviceinterface.module.his.resp;

import com.kasite.core.common.validator.ICheckEnumInf;

/**
 * HIS排班状态
 * @author daiyanshui
 *排班状态：1出诊，2停诊，3替诊，4可约，5:可挂，6:申请 7:已约满，
 */
public enum HisSchIsHalt implements ICheckEnumInf {

	/**出诊*/
	state_1(1,"出诊"),
	/**停诊*/
	state_2(2,"停诊"),
	/**替诊*/
	state_3(3,"替诊"),
	/**可约*/
	state_4(4,"可约"),
	/**可挂*/
	state_5(5,"可挂"),
	/**申请*/
	state_6(6,"申请"),
	/**已约满*/
	state_7(7,"已约满"),
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
	
	private HisSchIsHalt(int state,String name) {
		this.state = state;
		this.name = name;
	}
	public int getState() {
		return state;
	}
	public String getName() {
		return name;
	}
	public static HisSchIsHalt valuesOf(int state) {
		for (HisSchIsHalt f : HisSchIsHalt.values()) {
			if(f.state == state) {
				return f;
			}
		}
		return null;
	}
	public String des() {
		StringBuffer sbf = new StringBuffer();
		for (HisSchIsHalt f : HisSchIsHalt.values()) {
			sbf.append(f.getState()).append(":").append(f.getName());
		}
		return sbf.toString();
	}
	
	
	
}

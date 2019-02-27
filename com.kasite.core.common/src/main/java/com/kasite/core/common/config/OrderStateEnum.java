package com.kasite.core.common.config;
/**
 * 订单状态枚举类
 * 
 * @author zhaoy
 *
 */
public enum OrderStateEnum {
	
	OrderState0(0,"未支付"),
	OrderState1(1,"支付中"),
	OrderState2(2,"已支付"),
	OrderState3(3,"退款中"),
	OrderState4(4,"已退款"),
	OrderState5(5,"已取消"),
	OrderState6(6,"已撤销"),
	OrderState7(7,"退款失败"),
	OrderState8(8,"已完成"),
	OrderState9(9,"未完成"),
	;
	private int key;
	private String title;
	OrderStateEnum(int key, String title){
		this.key = key;
		this.title = title;
	}
	public int getKey() {
		return key;
	}
	public String getTitle() {
		return title;
	}
}

package com.kasite.client.order.util;

import java.util.List;


/**
 * @author linjf
 * TODO
 */
public class OrderUtil {

	
	/**
	 * 根据卡余额，订单可退余额，算出用户的可退余额
	 * @param cardBalance
	 * @param refundableOrderFeeList
	 * @return
	 */
	public static Integer calculateRefundabledFee(int cardBalance,List<Integer> refundableOrderFeeList){
		int orderPriceSum = 0;
		for(Integer orderPrice : refundableOrderFeeList) {
			//可退订单总额
			orderPriceSum = orderPriceSum+orderPrice.intValue();
			//用户余额大于可退订单总额,说明不够，还可以再加
			if( cardBalance>orderPriceSum ){
				//继续
				continue;
			}else {//orderPriceSum大于等于用户余额，说明订单可退总额已满足退款要求
				//跳出循环
				break;
			}
		}
		return cardBalance>orderPriceSum? orderPriceSum:cardBalance;
	}
}

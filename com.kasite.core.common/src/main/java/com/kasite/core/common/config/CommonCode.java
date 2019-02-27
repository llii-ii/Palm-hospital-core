package com.kasite.core.common.config;

/**
 * 
 * <p>Title: CommonCode</p>  
 * <p>Description: 公共枚举类(用来定义一些文案说明)</p> 
 * <p>Company: KST</p> 
 *
 * @author zhaoy  
 * @date 2018年10月9日  
 * @version 2.0
 */
public interface CommonCode {

	/**
	 *@Description:获取消息
	 * @return
	 */
	String getMessage();

	/**
	 *@Description:获取返回标识
	 * @return
	 */
	Integer getCode();
	
	enum Descr implements CommonCode {
		
		/**
		 * 支付状态文案显示 
		 */
		PAY_STATUS_1(1,"已支付"),
		PAY_STATUS_2(2,"已完成"),
		PAY_STATUS_3(3,"退款中"),
		PAY_STATUS_4(4,"已退款"),
		
		/**
		 * 订单类型(收/退)文案显示
		 */
		ORDER_TYPE_1(5,"支付订单"),
		ORDER_TYPE_2(6,"退款订单"),
		A_ORDER_TYPE_1(7,"应收"),
		A_ORDER_TYPE_2(8,"实收"),
		R_ORDER_TYPE_1(9,"应退"),
		R_ORDER_TYPE_2(10,"实退"),
		
		/**
		 * 账单的对账状态
		 */
		BILL_CHECK_STATUS_01(111,"账平(处置后)"),
		BILL_CHECK_STATUS_0(11,"账平"),
		BILL_CHECK_STATUS_1(12,"长款"),
		BILL_CHECK_STATUS_T1(13,"短款"),
		
		/**
		 * 资金的对账状态
		 */
		BILL_MONEY_CHECK_STATUS_0(14,"账不平"),
		BILL_MONEY_CHECK_STATUS_1(15,"账平"),
		BILL_MONEY_CHECK_STATUS_2(151,"账平(处置后)"),
		
		/**
		 * 账单的对账信息文案显示
		 */
		DIFF_REASON_1(16,"医院单边账"),
		DIFF_REASON_1_DECR(17,"医院对账文件中存在的交易流水，在支付渠道对账文件中找不到对应的交易流水号;"),
		DIFF_REASON_2(18,"渠道单边账"),
		DIFF_REASON_2_DECR(19,"支付渠道对账文件中存在的交易流水，在医院对账文件中找不到对应的医院流水号;"),
		DIFF_REASON_3(20,"金额不一致"),
		DIFF_REASON_3_DECR_PAY_G(21,"渠道实收金额，大于医院应收金额;"),  //长款
		DIFF_REASON_3_DECR_PAY_T(22,"渠道实收金额，小于医院应收金额;"),  //短款
		DIFF_REASON_3_DECR_REFUND_G(23,"渠道实退金额，大于医院应退金额;"), //短款
		DIFF_REASON_3_DECR_REFUND_T(24,"渠道实退金额，小于医院应退金额;"), //长款
		
		/**
		 * 支付方式
		 */
		PAY_METHOD_WX(25,"微信支付"),
		PAY_METHOD_ZFB(26,"支付宝支付"),
		PAY_METHOD_YL(27,"银联支付"),
		PAY_METHOD_QT(28,"其他支付方式"),
		
		/**
		 * 账单的勾兑状态
		 */
		CHECK_OUT_STATE_0(29,"未勾兑"),
		CHECK_OUT_STATE_1(30,"已勾兑"),
		;

		/** 代码 */
		private Integer code;
		/** 明文 */
		private String message;
		
		Descr(Integer code, String message) {
			this.code = code;
			this.message = message;
		}
		
		@Override
		public String getMessage() {
			return message;
		}

		@Override
		public Integer getCode() {
			return code;
		}
		
	}
	
}

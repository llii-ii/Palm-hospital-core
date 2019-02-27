
package com.kasite.core.common.constant;

import com.yihu.hos.IApiModule;



/**
 * @author linjf 2017年11月14日 17:37:39
 * TODO api模块枚举类
 */
public interface ApiModule extends IApiModule{
	String getModuleName();
	String name();
	/**
	 * 获取名称
	 * @return
	 */
	String getName();
	/** 所有调用 微信接口的api 列表定义*/
	enum Zfb implements ApiModule {
		/**
		 * 支付订单查询
		 */
		pay_orderquery("zfb_pay_orderquery"),
		/**
		 * 统一支付接口
		 */
		pay_unifiedorder("zfb_pay_unifiedorder"),
		/**
		 * 关闭订单
		 */
		pay_closeorder("zfb_pay_closeorder"),
		/**
		 * 撤销订单
		 */
		alipayTradeCancel("zfb_secapi_pay_reverse"),
		/**
		 *  退款
		 */
		pay_refund("zfb_pay_refund"),
		/**
		 * 查询退款
		 */
		pay_refundquery("zfb_pay_refundquery"),
		/**
		 * 当面付
		 */
		alipay_trade_pay("zfb_pay_micropay"),
		/**
		 * 预下单接口
		 */
		tradePrecreate("zfb_tradePrecreate"),
		
		/**
		 * 获取token
		 */
		token("zfb_token"),
		/**
		 * 创建公众号菜单
		 */
		menu_create("zfb_menu_create"),
		/**
		 * 创建公众号菜单
		 */
		menu_update("zfb_menu_update"),
		/**
		 * 获取公众号菜单
		 */
		menu_get("zfb_menu_get"),
		/**
		 * 删除公众号菜单
		 */
		menu_delete("zfb_menu_delete"),
		/**
		 * oauth2鉴权
		 */
		oauth2_authorize("zfb_oauth2_authorize"),
		/**
		 * 通过oauth2鉴权后获取用户信息
		 */
		sns_oauth2_access_token("zfb_sns_oauth2_access_token"),
		/**
		 * 获取用户信息网页授权时拉取用户信息
		 */
		sns_userinfo("zfb_sns_userinfo"),
		/**
		 * 获取用户分组
		 */
		groups_get("zfb_groups_get"),
		/**
		 * 获取用户信息
		 */
		user_get("zfb_user_get"),
		/**
		 * 批量获取用户信息
		 */
		user_info_batchget("zfb_user_info_batchget"),
		/**
		 * 发送消息
		 */
		message_mass_send("zfb_message_mass_send"),
		/**
		 * 发送模版消息
		 */
		message_template_send("zfb_message_template_send"),
		/**
		 * 客服接口-发消息
		 */
		message_custom_send("zfb_message_custom_send"),
		/**
		 * 获取用户信息
		 */
		user_info("zfb_user_info"),
		/**
		 * 获得jsapi_ticket
		 */
		ticket_getticket("zfb_ticket_getticket"),
		/**
		 * 获取多媒体消息
		 */
		media_get("zfb_media_get"),

		;
		@Override
		public String getModuleName() {
			return "_Module_zfb";
		}
		private String name;
		Zfb(String name) {
			this.name = name;
		}
		@Override
		public String getName() {
			return name;
		}
	}
	/** 所有调用 微信接口的api 列表定义*/
	enum WeChat implements ApiModule {
		/**
		 * 支付订单查询
		 */
		pay_orderquery("wechat_pay_orderquery"),
		/**
		 * 下载对账单
		 */
		pay_downloadbill("wechat_pay_downloadbill"),
		/**
		 * 统一支付接口
		 */
		pay_unifiedorder("wechat_pay_unifiedorder"),
		/**
		 * 当面付
		 */
		pay_micropay("wechat_pay_micropay"),
		/**
		 * 关闭订单
		 */
		pay_closeorder("wechat_pay_closeorder"),
		/**
		 * 撤销订单
		 */
		secapi_pay_reverse("wechat_secapi_pay_reverse"),
		/**
		 *  退款
		 */
		pay_refund("wechat_pay_refund"),
		/**
		 * 查询退款
		 */
		pay_refundquery("wechat_pay_refundquery"),
		
		/**
		 * 获取token
		 */
		token("wechat_token"),
		/**
		 * 创建公众号菜单
		 */
		menu_create("wechat_menu_create"),
		/**
		 * 获取公众号菜单
		 */
		menu_get("wechat_menu_get"),
		/**
		 * 删除公众号菜单
		 */
		menu_delete("wechat_menu_delete"),
		/**
		 * oauth2鉴权
		 */
		oauth2_authorize("wechat_oauth2_authorize"),
		/**
		 * 通过oauth2鉴权后获取用户信息
		 */
		sns_oauth2_access_token("wechat_sns_oauth2_access_token"),
		/**
		 * 获取用户信息网页授权时拉取用户信息
		 */
		sns_userinfo("wechat_sns_userinfo"),
		/**
		 * 获取用户分组
		 */
		groups_get("wechat_groups_get"),
		/**
		 * 获取用户信息
		 */
		user_get("wechat_user_get"),
		/**
		 * 批量获取用户信息
		 */
		user_info_batchget("wechat_user_info_batchget"),
		/**
		 * 发送消息
		 */
		message_mass_send("wechat_message_mass_send"),
		/**
		 * 获取模板列表
		 */
		GET_ALL_PRIVATE_TEMPLATE("get_all_private_template"),
		/**
		 * 发送模版消息
		 */
		message_template_send("wechat_message_template_send"),
		/**
		 * 客服接口-发消息
		 */
		message_custom_send("wechat_message_custom_send"),
		/**
		 * 获取用户信息
		 */
		user_info("wechat_user_info"),
		/**
		 * 获得jsapi_ticket
		 */
		ticket_getticket("wechat_ticket_getticket"),
		/**
		 * 获取多媒体消息
		 */
		media_get("wechat_media_get"),
		
		
		qrcode_create("wechat_qrcode_create"),
		
		shorturl("wechat_shorturl"),
		
		;
		@Override
		public String getModuleName() {
			return "_Module_wechat";
		}
		private String name;
		
		WeChat(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}
	
	/** 所有调用 银联接口的api 列表定义*/
	enum UnionPay implements ApiModule {
		
		filedownload("unionpay_filedownload"),
		backTransReq_qrCodeConsumePassive("unionpay_backTransReq_qrCodeConsumePassive"),
		backTransReq_refund("unionpay_backTransReq_refund"),
		backTransReq_correction("unionpay_backTransReq_correction"),
		queryTrans_queryTransStatus("unionpay_queryTrans_queryTransStatus"),
		;
		@Override
		public String getModuleName() {
			return "_Module_unionPay";
		}
		private String name;
		
		UnionPay(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}
	
	enum Swiftpass implements ApiModule {
		
		payweixinjspay("swiftpass_pay.weixin.jspay"),
		payalipayjspay("swiftpass_pay.alipay.jspay"),
		unifiedtraderefund("swiftpass_unified.trade.refund"),
		unifiedtradequery("swiftpass_unified.trade.query"),
		unifiedtraderefundquery("swiftpass_unified.trade.refundquery"),
		unifiedtrademicropay("swiftpass_unified.trade.micropay"),
		filedownload("swiftpass_filedownload"),
		unifiedtradeclose("swiftpass_unified.trade.close"),
		unifiedmicropayreverse("swiftpass_unified.micropay.reverse"),
		
		;
		@Override
		public String getModuleName() {
			return "_Module_swiftpass";
		}
		private String name;
		
		Swiftpass(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}
	
	enum NetPay implements ApiModule {
		/**
		 * 一网通支付API
		 */
		NETPAY_MB_EUSERPAY("MB_EUserPay"),
		/**
		 * 查询单笔订单API
		 */
		NETPAY_QUERYSINGLEORDER("QuerySingleOrder"),
		/**
		 * 退款API
		 */
		NETPAY_DOREFUND("DoRefund"),
		/**
		 * 已处理退款查询API
		 */
		NETPAY_QUERYSETTLEDREFUND("QuerySettledRefund"),
		/**
		 * 按商户日期查询已结账订单API
		 */
		NETPAY_QUERYSETTLEDORDERBYMERCHANTDATE("QuerySettledOrderByMerchantDate"),
		/**
		 * 按退款日期查询退款API
		 */
		NETPAY_QUERYREFUNDBYDATE("QueryRefundByDate"),
		/**
		 * 查询招行公钥API
		 */
		NETPAY_DOBUSINESS("DoBusiness"),
		;
		@Override
		public String getModuleName() {
			return "_Module_netPay";
		}
		private String name;
		
		NetPay(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}
	
	enum Rel implements ApiModule {
		token("rel_token"),
		;
		@Override
		public String getModuleName() {
			return "_Module_rel";
		}
		private String name;
		
		Rel(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}
	
	/** 所有调用HIS接口的方法都在这里定义*/
	enum His implements ApiModule,ApiMethodName  {
		/**
		 * 查询门诊就诊卡
		 */
		queryClinicCard("his_queryClinicCard","查询门诊就诊卡"),
		
		/**
		 * 查询卡余额
		 */
		queryCardBalance("his_queryCardBalance","查询卡余额"),
		/**
		 * 查询住院号
		 */
		queryHospitalNo("his_queryHospitalNo","查询住院号"),
		/**
		 * 住院号是否已经解绑
		 */
		hisCheckHospitalNo("his_hisCheckHospitalNo","住院号是否已经解绑"),
		
		hosNoRecharge("his_hosNoRecharge","住院充值"),
		
		oPDRecharge("his_oPDRecharge","就诊卡充值"),
		OPDRechargeFreeze("his_OPDRechargeFreeze","his退款冻结"),
		OPDRechargeCancellationFee("his_OPDRechargeCancellationFee","his退款核销或解冻"),
		getOrderSum("his_getOrderSum","获取HIS订单金额"),
		
		queryInHospitalCostList("his_queryInHospitalCostList","查询住院费用日清单"),
		
		queryInHospitalCostType("his_queryInHospitalCostType","查询住院费用日清单明细"),
		
		queryInHospitalRechargeList("his_queryInHospitalRechargeList","预交金充值记录查询"),
		CheckHospitalNo("his_CheckHospitalNo","住院号校验"),
		queryHosOutpatientRecords("his_queryHosOutpatientRecords","门诊充值记录查询"),
		
		queryOrderPrescriptionList("his_queryOrderPrescriptionList","查询HIS订单处方列表(订单模式)"),
		
		queryOrderPrescriptionInfo("his_queryOrderPrescriptionInfo","查询HIS订单处方详情(订单模式)"),
		
		queryOrderPrescriptionInfoItems("his_queryOrderPrescriptionInfoItems","查询HIS订单处方详情下的项目明细(订单模式)"),
		
		queryOrderSettlementList("his_queryOrderSettlementList","查询HIS订单处方结算列表(预交金结算)"),
		
		queryOrderSettlementInfo("his_queryOrderSettlementInfo","查询HIS订单处方结算详情(预交金结算)"),
		
		settleOrderSettlement("his_settleOrderSettlement","结算诊间处方(预交金结算)"),
		
		queryRechargeDetailInfo("his_queryRechargeDetailInfo","院清单费用明细详情"),

		addOrderPrescription("his_addOrderPrescription","新增诊间支付单(用户未付款)"),
		
		payOrderPrescription("his_payOrderPrescription","诊间支付(用户已付款)"),
		
		cancelPayDrugOrder("his_cancelPayDrugOrder","取消已支付医嘱订单"),
		
		queryInHospitalCostTypeItem("his_queryInHospitalCostTypeItem","查询住院费用明细"),
		
		queryOutpatientCostList("his_queryOutpatientCostList","查询门诊费用日清单"),
		
		queryOutpatientCostType("his_queryOutpatientCostType","查询门诊费用日清单明细"),
		
		queryOutpatientCostTypeItem("his_queryOutpatientCostTypeItem","查询门诊费用明细"),
		
		queryQueue("his_queryQueue","查询排队信息"),
		
		queryReportCount("his_queryReportCount","查询报告数"),
		/**
		 * 查询住院患者信息
		 */
		hospitalUserInfo("his_hospitalUserInfo"," 查询住院患者信息"),
		/**锁号*/
		lock("his_lock","锁号"),
		/**挂号 */
		bookService("his_bookService","挂号"),//
		/** 取消／退号*/
		cancelOrder("his_cancelOrder","取消订单"),//
		/** 支付*/
		pay("his_pay","支付"),//
		/** 退费*/
		refund("his_refund","退费"),//
		/** 查预约记录*/
		queryRegWater("his_queryRegWater","查预约记录"),//
		/** 查科室*/
		queryDept("his_queryDept","查科室"),//
		/** 查医生*/
		queryDoctor("his_queryDoctor","查医生"),//
		/** 查停诊*/
		syncStopArrange("his_syncStopArrange","查停诊"),//
		/** 查号源*/
		queryNumber("his_queryNumber","查号源"),//
		/** 查排班*/
		queryArrange("his_queryArrange","查排班"),//
		/**查成员信息*/
		getPatInfo("his_getPatInfo","查成员信息"),
		/** 解锁*/
		unlock("his_unlock","解锁"),//
		/** 查对方服务器时间*/
		getDate("his_getDate","查对方服务器时间"),//
		/** 查询医院*/
		queryHospital("his_queryHospital","查询医院"),//
		/** his测试*/
		hisTest("his_hisTest","his测试"),//
		/** 检查锁号*/
		checkLock("his_checkLock","检查锁号"),//
		/** 注册*/
		register("his_register","注册"),//
		/** 登陆*/
		login("his_login","登陆"),//
		/** 校验*/
		patInfoCheck("his_patInfoCheck","校验患者信息"),//
		/** 修改成员信息*/
		alertPatInfo("his_alertPatInfo","修改成员信息"),//
		/** 新增成员*/
		addPatInfo("his_addPatInfo","新增成员"),//
		/** 新增替诊*/
		queryReplace("his_queryReplace","新增替诊"),//
		/** 挂号类型*/
		queryRegType("his_queryRegType","查询挂号类型"),//
		/** 更新订单信息*/
		updateOrder("his_updateOrder","更新订单信息"),//
		/** 获取短信文本*/
		getSms("his_getSms","获取短信文本"),//
		/** 发送短信*/
		sendSms("his_sendSms","发送短信"),
		/**对账接口*/
		checkAccount("his_checkAccount","对账接口"),	
		/**改诊*/
		changeDiagnosis("his_changeDiagnosis","改诊"),
		/**校验就诊卡*/
		checkClinic("his_checkClinic","校验就诊卡"),
		/**获取挂号费*/
		getGhFee("his_getGhFee","获取挂号费"),
		/**获取密码*/
		getPassWord("his_getPassWord","获取密码"),
		/**加密*/
		encrypt("his_encrypt","加密"),
		/**获取预约挂号表单项*/
		getYYItem("his_getYYItem","获取预约挂号表单项"),
		/**新增就诊卡*/
		addClinic("his_addClinic","新增就诊卡"),
		/**在线建档*/
		createMedCard("his_createMedCard","在线建档"),
		
		queryEntityCardInfo("his_queryEntityCardInfo","查询实体卡信息（卡面付用）"),
		
		/**查询检查检验报告单列表（检查报告单列表）*/
		queryCheckReportList("his_queryCheckReportList","检查报告单列表"),
		/**查询体检报告单列表*/
		queryTjReportList("his_queryTjReportList","查询体检报告单列表"),
		queryTjReportBasicInfo("his_queryTjReportBasicInfo","查询体检报告单详情"),
		queryTjReportInfo("his_queryTjReportInfo","查询体检报告单详情"),
		/**查询检查检验报告单列表（检查报告单项目列表）*/
		queryCheckReportItemList("his_queryCheckReportItemList","检查报告单项目列表"),
		/**查询检查检验报告单列表（检验报告单列表）*/
		queryTestReportList("his_queryTestReportList","检验报告单列表"),
		/**查询检查检验报告单列表（检验报告单项目列表）*/
		queryTestReportItemList("his_queryTestReportItemList","检验报告单项目列表"),
		/**查询患者就诊记录*/
		queryMedicalRecord("his_queryMedicalRecord","查询患者就诊记录"),
		/**查询His订单记录*/
		queryhisorderbilllist("his_queryhisorderbilllist","查询His订单记录"),
		/**校验His充值业务是否完成*/
		queryHisOrderBillByPayNo("his_queryHisOrderBillByPayNo","校验His充值业务是否完成"),
		
		//病历复印
		/**根据XX证号获取病人基本信息*/
		queryPatientInfoByNos("his_queryPatientInfoByNos","根据XX证号获取病人基本信息"),
		/**根据病案号获取病人病历*/
		queryMedicalRecords("his_queryMedicalRecords","根据病案号获取病人病历"),
		/**根据病历号获取病人手术信息*/
		queryOperationInfo("his_queryOperationInfo","根据病历号获取病人手术信息"),
		/**病例复印请求信息回写*/
		backInfoToHis("his_backInfoToHis","病例复印请求信息回写"),
		/**HIS提供的鉴权接口*/
		authValidation("his_authValidation","渠道接入验证"),
		
		queryOrderReceiptList("his_queryOrderReceiptList","查询单据列表"),
		/**合并支付订单*/
		mergeSettledPayReceipt("his_mergeSettledPayReceipt","单据合并支付"),
		queryMergeSettledPayReceipt("his_queryMergeSettledPayReceipt","查询单据合并支付结果"),
		
		/**查询住院次数*/
		information_Record_Query("his_information_Record_Query","查询住院次数"),
		Register_Preferential_Query("his_RegisterPreferentialQuery","获取优惠人群信息"),
		Register_RegisterCheck_Query("his_RegisterRegisterCheckQuery","挂号检查"),
		Register_CancelCheck_Query("his_RegisterCancelCheckQuery","退号检查"),
		BindCard_CreateUser_Modify("his_BindCardCreateUserModify","新病人登记"),
		Register_LimitInfo_Query("his_RegisterLimitInfoQuery","获取挂号限制条件"),
		/** 未查找到方法名称*/
		ERROR("ERROR","未定义的方法"),
		/**
		 * 获取Histoken
		 */
		getToken("his_getToken","获取Histoken"),
		QueryScheduleDate("his_QueryScheduleDate","获取可预约日期"),
		GetExamItemList("his_GetExamItemList","获取以及预约项目"),
		GetSignalSourceList("his_GetSignalSourceList","预约号源"),
		MedicalAppoint("his_MedicalAppoint","预约"),
		CancelAppoint("his_CancelAppoint","取消预约"),
		GetAppointReceiptInfo("his_GetAppointReceiptInfo","回执单"),
		/**
		 * 调用HIS接口下发验证码
		 * */
		SendPorvingCode("his_SendPorvingCode","调用HIS接口下发验证码"),
		getMZGHXXList("his_getMZGHXXList","查询门诊号"),
		
		;
		
		private String name;
		private String remark;

		@Override
		public String getApiName() {
			return remark;
		}
		@Override
		public String getModuleName() {
			return "_Module_his";
		}
		His(String name,String remark) {
			this.name = name;
		}
		public String getRemark() {
			return this.remark;
		}
		@Override
		public String getName() {
			return name;
		}
	}
	/** 预约系统 API列表*/
	enum YY implements ApiModule,ApiMethodName {
		/** 退号  yy.yygh.YYCancel*/
		CancelService("yy.yygh.CancelService","退号"),
		/** 查询预约记录  yy.yygh.QueryRegInfo*/
		QueryRegInfo("yy.yygh.QueryRegInfo","查询预约记录"),
		UnLock("yy.yygh.UnLock","解锁"),
//		QueryDictionaryInfo("basic.BasicApi.QueryDictionaryInfo",""),
		PushScheduleStop("basic.BasicApi.PushScheduleStop","推送锁号信息"),
		PushStop("yy.yygh.PushStop","停诊消息推送"),
		
		/**
		 * 解锁预约挂号
		 */
		Unlock("yy.yygh.Unlock","解锁"),
		
		/** 挂号 yy.yygh.BookService */
		BookService("yy.yygh.BookService","挂号"),
		AddOrderAndBookService("yy.yygh.AddOrderAndBookService","新增订单并挂号"),
		/** 锁号 yy.yygh.LockOrder */
		LockOrder("yy.yygh.LockOrder","锁号"),
		/** 查询预约规则信息 */
		QueryRule("yy.yygh.QueryRule","查询预约规则信息"),
		/** 查询门诊科室 */
		QueryClinicDept("yy.yygh.QueryClinicDept","查询门诊科室"),
		/** 查询门诊医生 */
		QueryClinicDoctor("yy.yygh.QueryClinicDoctor","查询门诊医生"),
		/**
		 * 搜索科室、医生
		 */
		SearchClinicDeptAndDoctor("yy.yygh.SearchClinicDeptAndDoctor","搜索科室、医生"),
		/** 查询号源信息 */
		QueryNumbers("yy.yygh.QueryNumbers","查询号源信息"),
		/** 查询门诊排版 */
		QueryClinicSchedule("yy.yygh.QueryClinicSchedule","查询门诊排版"),
		/** 查询历史医生 */
		QueryHistoryDoctor("yy.yygh.QueryHistoryDoctor","查询历史医生"), 
		YYCancel("yy.yygh.YYCancel","取消预约"),
		QueryYYRule("yy.yygh.QueryYYRule","查询预约规则"),
		/*
		 * 医技预约
		 */
		QueryExamItemList("yy.yygh.QueryExamItemList","查询项目列表"),
		QuerySignalSourceList("yy.yygh.QuerySignalSourceList","查询号源"),
		MedicalAppoint("yy.yygh.MedicalAppoint","医技预约"),
		CancelAppoint("yy.yygh.CancelAppoint","取消医技预约"),
		GetAppointReceiptInfo("yy.yygh.GetAppointReceiptInfo","回执单"),
		QueryScheduleDate("yy.yygh.QueryScheduleDate","查询可预约的日期"),
		;
		@Override
		public String getModuleName() {
			return "yy";
		}
		private String name;
		private String apiName;
		YY(String name,String apiName) {
			this.name = name;
			this.apiName = apiName;
		}

		@Override
		public String getName() {
			return name;
		}
		@Override
		public String getApiName() {
			return apiName;
		}
	}
	/** 消息系统 API列表*/
	enum Msg implements ApiModule,ApiMethodName {
		PushRecipe("msg.msgApi.PushRecipe","处方开药消息推送"),
		MsgCenterMainCount("msg.msgApi.MsgCenterMainCount","消息中心首页统计"),
		/** 添加消息队列  msg.msgApi.AddMsgQueue*/
		AddMsgQueue("msg.msgApi.AddMsgQueue","添加消息队列"),
		/** 查询消息队列  msg.msgApi.MsgQueueList*/
		MsgQueueList("msg.msgApi.MsgQueueList","查询消息队列"),
		/** 查询消息队列  msg.msgApi.MsgQueueList*/
		MsgQueueListPage("msg.msgApi.MsgQueueListPage","查询消息队列"),
		/** 添加消息队列  msg.msgApi.QueryWxTemplateList*/
		QueryWxTemplateList("msg.msgApi.QueryWxTemplateList","查询微信模板消息列表"),
		/** 添加消息场景  msg.msgApi.AddMsgScene*/
		AddMsgScene("msg.msgApi.AddMsgScene","添加消息场景"),
		/** 修改消息场景  msg.msgApi.EditMsgScene*/
		EditMsgScene("msg.msgApi.EditMsgScene","修改消息场景"),
		/** 获取消息场景列表  msg.msgApi.MsgSceneList*/
		MsgSceneList("msg.msgApi.MsgSceneList","获取消息场景列表"),
		/** 添加消息模板  msg.msgApi.AddMsgScene*/
		AddMsgTemp("msg.msgApi.AddMsgTemp","添加消息模板"),
		/** 删除消息模板  msg.msgApi.EditMsgScene*/
		DeleteMsgTemp("msg.msgApi.DeleteMsgTemp","删除消息模板"),
		/** 修改消息模板  msg.msgApi.EditMsgScene*/
		EditMsgTemp("msg.msgApi.EditMsgTemp","修改消息模板"),
		/** 获取消息模板列表  msg.msgApi.MsgSceneList*/
		MsgTempList("msg.msgApi.MsgTempList","获取消息模板列表"),
		AddMsgSource("msg.msgApi.AddMsgSource","添加消息来源"),
		/** 删除消息模板  msg.msgApi.EditMsgScene*/
		DeleteMsgSource("msg.msgApi.DeleteMsgSource","删除消息来源"),
		/** 修改消息模板  msg.msgApi.EditMsgScene*/
		EditMsgSource("msg.msgApi.EditMsgSource","修改消息来源"),
		/** 获取消息模板列表  msg.msgApi.MsgSceneList*/
		MsgSourceList("msg.msgApi.MsgSourceList","获取消息来源列表"),
		/** 增加用户openId映射  msg.msgApi.AddMsgUserOpenId*/
		AddMsgUserOpenId("msg.msgApi.AddMsgUserOpenId","添加用户openId映射"),
		/** 修改用户openId映射  msg.msgApi.EditMsgUserOpenId*/
		EditMsgUserOpenId("msg.msgApi.EditMsgUserOpenId","修改用户openId映射"),
		/** 获取用户openId映射列表  msg.msgApi.MsgUserOpenIdList*/
		MsgUserOpenIdList("msg.msgApi.MsgUserOpenIdList","获取用户openId映射列表"),
		QueryOpenId("msg.msgApi.QueryOpenId","获取用户openId列表"),
		MsgOpenIdSceneList("msg.msgApi.MsgOpenIdSceneList","获取用户消息订阅表"),
		AddMsgOpenIdScene("msg.msgApi.AddMsgOpenIdScene","用户消息订阅"),
		DeleteMsgOpenIdScene("msg.msgApi.DeleteMsgOpenIdScene","用户取消消息订阅"),
		ReadMsg("msg.msgApi.ReadMsg","用户已读消息更新"),
		/** 消息系统  msg.msgApi.SendMsg*/
		SendMsg("msg.msgApi.SendMsg","消息系统"),
		/**异常信息查询*/
		GetYctxsz("msg.msgApi.GetYctxsz","异常信息查询"),
		/** 发送短信*/
		SendSms("msg.msgApi.SendSms","发送短信"),
		/** 发送运维消息*/
		SendMaintenancenMsg("msg.msgApi.SendMaintenancenMsg","发送运维消息"),
		/**
		 * 自动回复
		 */
		QueryAutoReplayArbitrarily("msg.msgApi.QueryAutoReplayArbitrarily","自动回复"),
		/**
		 * 关注自动回复
		 */
		QueryAutoReplayByFollow("msg.msgApi.QueryAutoReplayByFollow","关注自动回复"),
		;
		@Override
		public String getModuleName() {
			return "msg";
		}
		private String name;
		private String apiName;
		@Override
		public String getApiName() {
			return apiName;
		}
		Msg(String name,String apiName) {
			this.name = name;
			this.apiName = apiName;
		}

		@Override
		public String getName() {
			return name;
		}
		
	}
	/** 预约系统 API列表*/
	enum Basic implements ApiModule ,ApiMethodName{
		/** 查询渠道信息  basic.BasicApi.QueryChannelInfoByCardId*/
		QueryMemberList("basic.BasicApi.QueryMemberList","查询用户列表"),
		QueryMemberCardList("basic.BasicApi.QueryMemberCardList","查询用户卡列表"),
		CheckPorvingCode("basic.BasicApi.CheckPorvingCode","校验验证码"),
		AddMember("basic.BasicApi.AddMember","新增成员"),
		QueryClinicCard("basic.BasicApi.QueryClinicCard","查询门诊就诊卡"),
		QueryDept("basic.BasicApi.QueryDept","查询门诊科室"),
		QueryDoctor("basic.BasicApi.QueryDoctor","查询门诊医生"),
		QueryArticle("basic.BasicApi.QueryArticle","查询文章"),
		AddArticle("basic.BasicApi.AddArticle","新增文章"),
		UpdateArticle("basic.BasicApi.EditArticle","更新文章"),
		DelArticle("basic.BasicApi.DelArticleInfo","删除文章"),
		UpdateDoctor("basic.BasicApi.UpdateDoctor","更新医生信息"),
		QueryMemberInfo("basic.BasicApi.QueryMemberInfo","更新患者信息"),
		UpdateMobile("basic.BasicApi.UpdateMobile","更新联系电话"),
		QueryHospitalLocal("basic.BasicApi.QueryHospitalLocal","查询本地医院基础信息"),
		QueryHospitalListLocal("basic.BasicApi.QueryHospitalListLocal","查询本地医院列表"),
		QueryBaseDept("basic.BasicApi.QueryBaseDept","查询基础科室列表"),
		QueryBaseDoctor("basic.BasicApi.QueryBaseDoctor","查询基础医生列表"),
		AddDictionary("basic.BasicApi.AddDictionary","新增字典"),
		QueryDictionaryInfo("basic.BasicApi.QueryDictionaryInfo","查字典"),
		DelDictionary("basic.BasicApi.DelDictionary","删除字典"),
		UpdateDictionary("basic.BasicApi.UpdateDictionary","更新字典"),
		QueryUserInfo("basic.BasicApi.QueryUserInfo","查询用户信息"),
		GetProvingCode("basic.BasicApi.GetProvingCode","获取验证码"),
		SetDefaultMember("basic.BasicApi.SetDefaultMember","设置默认成员"),
		SetDefaultCard("basic.BasicApi.SetDefaultCard","设置默认就诊卡"),
		BindHospitalNo("basic.BasicApi.BindHospitalNo","绑定住院号"),
		QueryCardBalance("basic.BasicApi.QueryCardBalance","查询余额"),
		BindClinicCard("basic.BasicApi.BindClinicCard","绑定门诊卡"),
		/** 医生职称 */
		QueryTitleInfo("basic.BasicApi.QueryTitleInfo","查询医生职称"),
		/** 删除就诊人 */
		DelMemberInfo("basic.BasicApi.DelMemberInfo","删除就诊人"),
		/** 删除就诊卡 */
		DelMemberCardInfo("basic.BasicApi.DelMemberCardInfo","删除就诊卡"),
		/**住院用户信息查询*/
		QueryHospitalUserInfo("basic.BasicApi.QueryHospitalUserInfo","院用户信息查询"),
		/** 文章列表 */
		QueryArticleList("basic.BasicApi.QueryArticleList","文章列表"),
		AddMemberWithValidate("basic.BasicApi.AddMemberWithValidate","新增虚拟就诊卡"),
		/** 住院号检验 */
		CheckHospitalNo("basic.BasicApi.CheckHospitalNo","住院号校验"),
		QueryBaseDeptLocal("basic.BasicApi.QueryBaseDeptLocal","查询本地基础科室"),
		QueryBaseDoctorLocal("basic.BasicApi.QueryBaseDoctorLocal","查询本地基础医生"),
		CheckEntityCard("basic.BasicApi.CheckEntityCard","校验实体卡信息（就诊卡加信息解码）"),
		/** 支付配置 */
		//QueryPayConfig("basic.BasicApi.QueryPayConfig","查询支付配置"),
		QueryPatientInfo("basic.BasicApi.QueryPatientInfo","查询患者信息"),
		
		;
		@Override
		public String getModuleName() {
			return "basic";
		}
		private String name;
		private String apiName;
		Basic(String name,String apiName) {
			this.apiName = apiName;
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
		@Override
		public String getApiName() {
			return apiName;
		}
	}
	
	/** 支付网关 API列表*/
	enum Pay implements ApiModule ,ApiMethodName{
		/** 退费 */
		Refund("pay.PayWs.Refund","退费"),
		/**
		 * 统一下单
		 */
		UniteOrder("pay.PayWs.UniteOrder","统一下单"),
		WapUniteOrder("pay.PayWs.WapUniteOrder","H5/WAP统一下单"),
		/**
		 * 当面付扫码
		 */
		SweepCodePay("pay.PayWs.SweepCodePay","当面付扫码"),
		/**
		 * 获取原生支付二维码
		 */
		GetPayQRCode("pay.PayWs.GetPayQRCode","获取原生支付二维码"),
		/**
		 * 生成支付二维码
		 */
		CreatePayQRCode("pay.PayWs.CreatePayQRCode","生成支付二维码"),
		/**
		 * 获取账单
		 */
		GetBill("pay.PayWs.GetBill","获取账单"),
		/**
		 * 撤销商户订单
		 */
		Revoke("pay.PayWs.Revoke","撤销商户订单"),
		/**
		 * 关闭订单
		 */
		Close("pay.PayWs.Close","关闭订单"),
		QueryFrontPayLimit("pay.PayMerchant.QueryFrontPayLimit","获取支付配置信息"),
		;
		@Override
		public String getModuleName() {
			return "pay";
		}
		private String name;
		private String apiName;
		@Override
		public String getApiName() {
			return apiName;
		}
		Pay(String name,String apiName) {
			this.apiName = apiName;
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}
	
	/** 报表网关 API列表*/
	enum ReportForms implements ApiModule ,ApiMethodName{
		/** 报表收集 */
		DataCollection("rf.rfApi.DataCollection","报表收集"),
		/** 报表收集 */
		DataCloudCollection("rf.rfApi.DataCloudCollection","报表收集"),
		/**营业报表查询**/
		GetReportFormsByDate("rf.rfApi.GetReportFormsByDate","营业报表查询"),
		;
		@Override
		public String getModuleName() {
			return "rf";
		}
		private String name;
		private String apiName;
		@Override
		public String getApiName() {
			return apiName;
		}
		ReportForms(String name,String apiName) {
			this.apiName = apiName;
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}
	/** 订单 API列表*/
	enum Order implements ApiModule ,ApiMethodName{
		/**
		 * 新增本地订单
		 */
		AddOrderLocal("order.orderApi.AddOrderLocal","新增本地订单"),
		/** 订单申请退费 */
		OrderIsCancel("order.orderApi.OrderIsCancel","订单申请退费"),
		OrderIsCancel_V1("order.orderApi.OrderIsCancel_V1","订单申请退费"),
		/** 订单业务取消、撤销 */
		BizForCancel("order.orderApi.BizForCancel","订单业务取消、撤销"),
		/** 订单业务完成接口 */
		BizForCompletion("order.orderApi.BizForCompletion","订单业务完成接口"),
		/**
		 * 退费完成接口
		 */
		CancelForCompletion("order.orderApi.CancelForCompletion","退费完成接口"),
		/**
		 * 支付完成接口
		 */
		PayForCompletion("order.orderApi.PayForCompletion","支付完成接口"),
		/**
		 * 撤销订单
		 */
		RevokeOrder("order.orderApi.RevokeOrder","撤销订单"),
		/**
		 * 撤销订单
		 */
		RevokeOrder_V1("order.orderApi.RevokeOrder_V1","撤销订单"),
		/**
		 * 取消订单
		 */
		CancelOrder("order.orderApi.CancelOrder","取消订单"),
		/**
		 * 正在支付
		 */
		OrderIsPayment("order.orderApi.OrderIsPayment","正在支付"),
		
		
		/**
		 * 查询本地订单
		 */
		OrderListLocal("order.orderApi.OrderListLocal","查询本地订单列表"),
		
		OrderDetailLocal("order.orderApi.OrderDetailLocal","查询本地订单详细"),
		
		QueryOrderProcess("order.orderApi.QueryOrderProcess","查询本地订单状态"),
		/**
		 * 查询订单状态
		 */
		QueryOrderState("order.orderApi.QueryOrderState","查询订单状态"),
		QueryOrderState_V1("order.orderApi.QueryOrderState_V1","查询订单状态"),
		QueryTuiFeiOrderState_V1("order.orderApi.QueryTuiFeiOrderState_V1","查询退费订单状态"),
		/** 预交金充值记录查询*/
		QueryInHospitalRechargeList("order.businessOrder.QueryInHospitalRechargeList","预交金充值记录查询"),
	
		/** 门诊充值记录查询 */
		QueryOutpatientRechargeList("order.businessOrder.QueryOutpatientRechargeList","门诊充值记录查询"),
		/**
		 * 住院每日清单列表查询
		 */
		QueryInHospitalCostList("order.businessOrder.QueryInHospitalCostList","住院每日清单列表查询"),		
		/**
		 * 查询每日清单，费用分类
		 */
		QueryInHospitalCostType("order.businessOrder.QueryInHospitalCostType","查询每日清单，费用分类"),
		/**
		 * 查询每日清单费用分类明细项
		 */
		QueryInHospitalCostTypeItem("order.businessOrder.QueryInHospitalCostTypeItem","查询每日清单费用分类明细项"),
		/**
		 * 门诊每日清单列表查询
		 */
		QueryOutpatientCostList("order.businessOrder.QueryOutpatientCostList","门诊每日清单列表查询"),		
		/**
		 * 查询门诊每日清单，费用分类
		 */
		QueryOutpatientCostType("order.businessOrder.QueryOutpatientCostType","查询门诊每日清单，费用分类"),
		/**
		 * 查询门诊每日清单费用分类明细项
		 */
		QueryOutpatientCostTypeItem("order.businessOrder.QueryOutpatientCostTypeItem","查询门诊每日清单费用分类明细项"),
		
		/**
		 * 订单处方列表（订单模式）
		 */
		QueryOrderPrescriptionList("order.businessOrder.QueryOrderPrescriptionList","订单处方列表（订单模式）"),
		/**
		 * 订单处方详情（订单模式）
		 */
		QueryOrderPrescriptionInfo("order.businessOrder.QueryOrderPrescriptionInfo","订单处方详情（订单模式）"),
		/**
		 * 订单处方详情（订单模式）
		 */
		AddOrderPrescription("order.businessOrder.AddOrderPrescription","订单处方详情（订单模式）"),
		/**
		 * 诊间结算列表（预交金结算）
		 */
		QueryOrderSettlementList("order.businessOrder.QueryOrderSettlementList","诊间结算列表（预交金结算）"),
		/**
		 * 诊间结算详情（预交金结算）
		 */
		QueryOrderSettlementInfo("order.businessOrder.QueryOrderSettlementInfo","诊间结算详情（预交金结算）"),
		/**
		 * 诊间处方结算（预交金结算）
		 */
		SettleOrderSettlement("order.businessOrder.SettleOrderSettlement","诊间处方结算（预交金结算）"),
		/**
		 * 住院缴费订单明细
		 */
		QueryOrderFeeDetailInfo("order.orderApi.QueryOrderFeeDetailInfo","住院缴费订单明细"),
		/**
		 * 查询HIS住院缴费订单明细详情
		 */
		QueryInHospitalFeeProjectDetail("order.orderApi.QueryInHospitalFeeProjectDetail","查询HIS住院缴费订单明细详情"),
		
		/**
		 * 强制冲正订单
		 */
		ForceCorrectOrderBiz("order.orderApi.ForceCorrectOrderBiz","强制冲正订单"),
		/**
		 * 外部订单业务通知
		 */
		OrderOutBizNotify("order.orderApi.OrderOutBizNotify","外部订单业务通知"),
		
		QueryUnsettledOrderReceiptList("order.businessOrder.QueryUnsettledOrderReceiptList","查询未结算单据列表"),
		MergeSettledPayReceipt("order.businessOrder.MergeSettledPayReceipt","合并结算支付单据"),
		QueryOrderSubList("order.orderApi.QueryOrderSubList","查询子订单"),
		QueryRefundableOrderList("order.orderApi.QueryRefundableOrderList","查询可退订单列表"),
		QueryRefundableOrderList_V1("order.orderApi.QueryRefundableOrderList_V1","查询可退订单列表(泉州儿童版)"),
		;
		@Override
		public String getModuleName() {
			return "order";
		}
		private String name;
		private String apiName;
		@Override
		public String getApiName() {
			return apiName;
		}
		Order(String name,String apiName) {
			this.apiName = apiName;
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}
	
//	/** 咨询系统 API列表*/
//	enum Consult implements ApiModule {
//		/** 保存关注医生流水*/
//		SaveAttenDocWater("ZeusConsult.QrWs.SaveAttenDocWater"),
//		
//		;
//		@Override
//		public String getModuleName() {
//			return "_Module_Consult";
//		}
//		private String name;
//		Consult(String name) {
//			this.name = name;
//		}
//
//		@Override
//		public String getName() {
//			return name;
//		}
//	}
	/** 报告单*/
	enum Report implements ApiModule,ApiMethodName{
		/** 保存关注医生流水*/
		GetReportList("report.ReportWs.GetReportList","查询报告单列表"),
		GetReportInfo("report.ReportWs.GetReportInfo","查询报告单详情"),
		GetTjReportInfo("report.ReportWs.GetTjReportInfo","查询体检报告单详情"),
		;
		@Override
		public String getModuleName() {
			return "report";
		}
		private String name;
		private String apiName;
		@Override
		public String getApiName() {
			return apiName;
		}
		Report(String name,String apiName) {
			this.apiName = apiName;
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}
	/** 预约系统 API列表*/
	enum WeiXin implements ApiModule {
		/** 获取用户组*/
		getGroups("webapp.UserApi.GetGroups"),
		/** 获取用户列表*/
		GetUserList("webapp.UserApi.GetUserList"),
		/** 获取用户列表信息*/
		BatchGetUserInfo("webapp.UserApi.BatchGetUserInfo"),
		;
		@Override
		public String getModuleName() {
			return "webapp";
		}
		private String name;
		WeiXin(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}
	/** 排队 API列表 */
	enum Queue implements ApiModule ,ApiMethodName{
		/**
		 * 获取队列信息
		 */
		GetQueueInfo("queue.QueueWs.GetQueueInfo","获取队列信息"),
		/**
		 * 设置队列提醒号序
		 */
		SetReMindNo("queue.QueueWs.SetReMindNo","设置队列提醒号序"),
		;
		@Override
		public String getModuleName() {
			return "queue";
		}
		private String name;
		private String apiName;
		@Override
		public String getApiName() {
			return apiName;
		}
		Queue(String name,String apiName) {
			this.apiName = apiName;
			this.name = name;
		}
		@Override
		public String getName() {
			return name;
		}
	}
	/** 满意度 */
	enum Survey implements ApiModule ,ApiMethodName{
		/** 获取本医院的主题列表 */
		QuerySubjectsByOrgId("survey.SurveyWs.QuerySubjectsByOrgId","取本医院的主题列表"),
		/** 根据问卷id查询主题 */
		QuerySubjectById("survey.SurveyWs.QuerySubjectById","根据问卷id查询主题"),
		/** 提交答案 */
		CommitAnswer("survey.SurveyWs.CommitAnswer","提交答案"),;
		@Override
		public String getModuleName() {
			return "survey";
		}
		private String name;
		private String apiName;
		@Override
		public String getApiName() {
			return apiName;
		}
		Survey(String name,String apiName) {
			this.apiName = apiName;
			this.name = name;
		}
		@Override
		public String getName() {
			return name;
		}
	}
	/**Bat API列表*/
	enum Bat implements ApiModule ,ApiMethodName{
		/** 新增bat用户组*/
		AddBatUser("bat.BatApi.AddBatUser","新增第三方用户（微信，支付宝）"),
//		msgSendJobFinishNotice("bat.BatApi.msgSendJobFinishNotice",""),
		/**
		 * 查询bat用户信息
		 */
		QueryBatUser("bat.BatApi.QueryBatUser","查询第三方用户信息");
		;
		@Override
		public String getModuleName() {
			return "bat";
		}
		private String name;
		private String apiName;
		@Override
		public String getApiName() {
			return apiName;
		}
		Bat(String name,String apiName) {
			this.apiName = apiName;
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}

	/** 智付 API列表 */
	enum SmartPay implements ApiModule ,ApiMethodName{
		/** 快速支付二维码 */
		QuickPaymentQR("smartPay.smartPayWs.QuickPaymentQR","快速支付二维码"),
		/** 当面付扫码 */
		SweepCodePay("smartPay.smartPayWs.SweepCodePay","当面付扫码"),
		SweepCodePay_V1("smartPay.smartPayWs.SweepCodePay_V1","当面付扫码"),
		/** 生成患者二维码 */
		CreatePatientQRCode("smartPay.smartPayWs.CreatePatientQRCode","生成患者二维码"),
		/** 获取二维码信息点信息 */
		GetGuide("smartPay.smartPayWs.GetGuide","获取二维码信息点信息"),
		/** 扫描信息点信息 */
		ScanGuide("smartPay.smartPayWs.ScanGuide","扫描信息点信息"),
		/** 生成支付二维码 */
		CreatePayQRCode("smartPay.smartPayWs.CreatePayQRCode","生成支付二维码"), 
		/** 生成处方支付二维码 */
		CreatePrescriptionQrCode("smartPay.smartPayWs.CreatePrescriptionQrCode","生成处方支付二维码"), 
		/** 生成腕带付支付二维码 */
		CreateWristBandCodePay("smartPay.smartPayWs.CreateWristBandCodePay","生成腕带付支付二维码"), 
		/** 生成空就诊卡的二维码 */
		CreateQRCode("smartPay.smartPayWs.CreateQRCode","生成空就诊卡的二维码"), 
		/** 激活患者二维码 */
		ActivatePatientQRCode("smartPay.smartPayWs.ActivatePatientQRCode","激活患者二维码"),
		PrescQrValidateBefore("smartPay.smartPayWs.PrescQrValidateBefore","处方付校验"),
		QueryMemberRefundableMoney("smartPay.smartPayWs.QueryMemberRefundableMoney","查询用户可退余额（门诊）"),
		ApplySelfServiceRefund("smartPay.smartPayWs.ApplySelfServiceRefund","申请自助退费"),
		QuerySelfRefundRecordList("smartPay.smartPayWs.QuerySelfRefundRecordList","查询自助退费记录"),
		QuerySelfRefundRecordInfo("smartPay.smartPayWs.QuerySelfRefundRecordInfo","查询退费记录详情");
		;
		@Override
		public String getModuleName() {
			return "smartPay";
		}
		private String name;
		private String apiName;
		@Override
		public String getApiName() {
			return apiName;
		}
		SmartPay(String name,String apiName) {
			this.apiName = apiName;
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}
	


/** 病案复印模块 */
enum MedicalCopy implements  ApiModule,ApiMethodName {
	UpdateOrder("medicalCopy.MedicalCopyApi.UpdateOrder","订单更新"),
	/**通过病案号获得病人信息(his)*/
	QueryPatientInfoByNos("medicalCopy.MedicalCopyApi.QueryPatientInfoByNos","通过病案号获得病人信息"),
	/**通过id获得病人信息(kst)*/
	QueryPatientInfoById("medicalCopy.MedicalCopyApi.QueryPatientInfoById","通过id获得病人信息"),
	/**通过病案号获得病人病历(his)*/
	QueryMedicalRecords("medicalCopy.MedicalCopyApi.QueryMedicalRecords","通过病案号获得病人病历"),
	/**通过病历id获得病人病历信息(kst)*/
	GetMedicalRecordsById("medicalCopy.MedicalCopyApi.GetMedicalRecordsById","通过病历id获得病人病历信息"),
	/**新增快递单(kst)*/
	AddExpressOrder("medicalCopy.MedicalCopyApi.AddExpressOrder","新增快递单"),
	/**后台获得快递单列表(kst)*/
	GetExpressOrderList("medicalCopy.MedicalCopyApi.GetExpressOrderList","后台获得快递单列表"),
	/**通过id获得订单信息(kst)*/
	QueryExpressOrderById("medicalCopy.MedicalCopyApi.QueryExpressOrderById","通过id获得订单信息"),
	/**获取费用管理信息(kst)*/
	GetPriceManageInfo("medicalCopy.MedicalCopyApi.GetPriceManageInfo","获取费用管理信息"),
	/**修改费用管理信息(kst)*/
	UpdatePriceManage("medicalCopy.MedicalCopyApi.UpdatePriceManage","修改费用管理信息"),
	/**获得交易记录(kst)*/
	GetTransactionRecord("medicalCopy.MedicalCopyApi.GetTransactionRecord","获得交易记录"),
	/**微信端获得快递单列表(kst)*/
	GetOrderList("medicalCopy.MedicalCopyApi.GetOrderList","微信端获得快递单列表"),
	/**身份证和手机验证码验证判断(kst)*/
	Verification("medicalCopy.MedicalCopyApi.Verification","身份证和手机验证码验证判断"),
	/**判断病历是否在30内已经有复印(kst)*/
	IsCopyBy30Day("medicalCopy.MedicalCopyApi.IsCopyBy30Day","判断病历是否在30内已经有复印"),
	/**退款(kst)*/
	Refund("medicalCopy.MedicalCopyApi.Refund","退款"),
	/**新增交易记录(kst)*/
	AddTransactionRecord("medicalCopy.MedicalCopyApi.AddTransactionRecord","新增交易记录"),
	/**修改交易记录(kst)*/
	UpdateTransactionRecord("medicalCopy.MedicalCopyApi.UpdateTransactionRecord","修改交易记录"),
	/**获得微信config信息*/
	GetWxConfigInfo("medicalCopy.MedicalCopyApi.GetWxConfigInfo","获得微信config信息"),
	/**下载微信服务器上的临时素材*/
	GetWechatMedia("medicalCopy.MedicalCopyApi.GetWechatMedia","下载微信服务器上的临时素材"),
	/**微信图文消息推送*/
	WxSendMsg("medicalCopy.MedicalCopyApi.WxSendMsg","微信图文消息推送"),
	/**验证身份证及户口本*/
	IdCardVerification("medicalCopy.MedicalCopyApi.IdCardVerification","验证身份证及户口本"),
	/**添加复印内容*/
	AddCopyContent("medicalCopy.MedicalCopyApi.AddCopyContent","添加复印内容"),
	/**修改复印内容*/
	UpdateCopyContent("medicalCopy.MedicalCopyApi.UpdateCopyContent","修改复印内容"),
	/**获取复印内容列表*/
	GetCopyContentList("medicalCopy.MedicalCopyApi.GetCopyContentList","获取复印内容列表"),
	/**添加复印用途*/
	AddCopyPurpose("medicalCopy.MedicalCopyApi.AddCopyPurpose","添加复印用途"),
	/**修改复印用途*/
	UpdateCopyPurpose("medicalCopy.MedicalCopyApi.UpdateCopyPurpose","修改复印用途"),
	/**获取复印用途列表*/
	GetCopyPurposeList("medicalCopy.MedicalCopyApi.GetCopyPurposeList","获取复印用途列表"),
	/**前端获取收费信息*/
	FrontGetPriceManage("medicalCopy.MedicalCopyApi.FrontGetPriceManage","前端获取收费信息"),	
	/**根据id获取基本设置*/
	GetSettingById("medicalCopy.MedicalCopyApi.GetSettingById","根据id获取基本设置"),
	/**修改基本设置*/
	UpdateSetting("medicalCopy.MedicalCopyApi.UpdateSetting","修改基本设置"),
	/**添加常用问题*/
	AddQuestion("medicalCopy.MedicalCopyApi.AddQuestion","添加常用问题"),
	/**修改常用问题*/
	UpdateQuestion("medicalCopy.MedicalCopyApi.UpdateQuestion","修改常用问题"),
	/**获取常用问题列表*/
	GetQuestionList("medicalCopy.MedicalCopyApi.GetQuestionList","获取常用问题列表"),
	/**获取枫桥物流信息*/
	GetFengQiaoLogistics("medicalCopy.MedicalCopyApi.GetFengQiaoLogistics","根据病案号(身份证)获取病人信息"),
	/**根据病案号(身份证)获取病人信息*/
	QueryPatientInfo("medicalCopy.MedicalCopyApi.QueryPatientInfo","修改常用问题"),
	/**根据病案号(身份证)获取病人病历*/
	GetMedicalRecords("medicalCopy.MedicalCopyApi.GetMedicalRecords","根据病案号(身份证)获取病人病历"),
	/**统计订单流水*/
	CountTransactionRecord("medicalCopy.MedicalCopyApi.CountTransactionRecord","统计订单流水"),
	/**导出订单流水*/
	ExportTransactionRecord("medicalCopy.MedicalCopyApi.ExportTransactionRecord","导出订单流水"),
	/**订单补缴*/
	SupplementaryPay("medicalCopy.MedicalCopyApi.SupplementaryPay","订单补缴"),
	/**新增一条复印单*/
	CreateExpressOrder("medicalCopy.MedicalCopyApi.CreateExpressOrder","新增一条复印单"),
	/**统一下单后续操作*/
	AfterUnderOrder("medicalCopy.MedicalCopyApi.AfterUnderOrder","统一下单后续操作"),
	/**导出复印申请表*/
	ExportExpressOrder("medicalCopy.MedicalCopyApi.ExportExpressOrder","导出复印申请表"),
	/**审核复印单*/
	CheckExpressOrder("medicalCopy.MedicalCopyApi.CheckExpressOrder","审核复印单"),
	/**确认订单费用*/
	CheckExpressOrderFee("medicalCopy.MedicalCopyApi.CheckExpressOrderFee","确认订单费用"),
	/**发送订单*/
	SendExpressOrder("medicalCopy.MedicalCopyApi.SendExpressOrder","发送订单"),
	;
	@Override
	public String getModuleName() {
		return "medicalCopy";
	}
	private String name;
	private String apiName;
	@Override
	public String getApiName() {
		return apiName;
	}
	MedicalCopy(String name,String apiName) {
		this.apiName = apiName;
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
	
	/** 健康之路API列表 */
	enum JKZL implements ApiModule {
		/**
		 * 发送短信
		 * smsgw.smsSendWS.send
		 */
		sendSms("smsgw.smsSendWS.send")
		;
		@Override
		public String getModuleName() {
			return "smsgw";
		}
		private String name;

		JKZL(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}
	
	/** 健康小工具 API列表 */
	enum HealthTools implements ApiModule, ApiMethodName {
		/** 查询检查单解读列表 */
		QueryCheckListList("healthTools.healthToolsApi.QueryCheckListList", "查询检查单解读列表"),
		/** 添加检查单解读 */
		AddCheckList("healthTools.healthToolsApi.AddCheckList", "添加检查单解读"),
		/** 根据ID查询检查单解读 */
		QueryCheckListById("healthTools.healthToolsApi.QueryCheckListById", "根据ID查询检查单解读"),
		/** 修改检查单解读 */
		UpdateCheckList("healthTools.healthToolsApi.UpdateCheckList", "修改检查单解读"),;
		@Override
		public String getModuleName() {
			return "healthTools";
		}

		private String name;
		private String apiName;

		@Override
		public String getApiName() {
			return apiName;
		}

		HealthTools(String name, String apiName) {
			this.apiName = apiName;
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}
	
	enum QyWeChat implements ApiModule, ApiMethodName {
		/** 获取危急值信息（供LIS调用） */
		GetWarnValue("qyWeChat.QyWeChatApi.GetWarnValue", "获取危急值信息（供LIS调用）"),
		/** 投票、问卷管理 - 获取投票、问卷列表 */
		QueryVoteQuestionList("qyWeChat.QyWeChatApi.QueryVoteQuestionList", "投票、问卷管理 - 查询投票、问卷列表"),
		/** 投票、问卷管理 - 根据ID获取投票、问卷详情 */
		GetVoteQuestionInfoById("qyWeChat.QyWeChatApi.GetVoteQuestionInfoById", "投票、问卷管理 - 根据ID获取投票、问卷详情"),
		/** 投票、问卷管理 - 修改投票、问卷 */
		UpdateVoteQuestion("qyWeChat.QyWeChatApi.UpdateVoteQuestion", "投票、问卷管理 - 修改投票、问卷"),
		/** 投票、问卷管理 - 添加投票、问卷 */
		AddVoteQuestion("qyWeChat.QyWeChatApi.AddVoteQuestion", "投票、问卷管理 - 添加投票、问卷"),
		/** 投票、问卷管理 - 复制投票、问卷 */
		CopyVoteQuestion("qyWeChat.QyWeChatApi.CopyVoteQuestion", "投票、问卷管理 - 复制投票、问卷"),
		/** 投票、问卷管理 - 删除制投票、问卷 */
		DeleteVoteQuestion("qyWeChat.QyWeChatApi.DeleteVoteQuestion", "投票、问卷管理 - 删除投票、问卷"),
		/** 投票、问卷管理 - 分析 */
		GetVoteQuestionAnalyze("qyWeChat.QyWeChatApi.GetVoteQuestionAnalyze", "投票、问卷管理 - 分析"),
		/** 投票、问卷管理 - 问题-添加问题*/
		AddQuestion("qyWeChat.QyWeChatApi.AddQuestion", "投票、问卷管理 - 问题-添加问题"),
		/** 投票、问卷管理 - 问题-修改问题*/
		UpdateQuestion("qyWeChat.QyWeChatApi.UpdateQuestion", "投票、问卷管理 - 问题-修改问题"),
		/** 投票、问卷管理 - 问题-获取问题列表*/
		GetQuestionList("qyWeChat.QyWeChatApi.GetQuestionList", "投票、问卷管理 - 问题-获取问题列表"),
		/** 投票、问卷管理 - 问题-根据ID获取问题*/
		GetQuestionById("qyWeChat.QyWeChatApi.GetQuestionById", "投票、问卷管理 - 问题-根据ID获取问题"),
		/** 投票、问卷管理 - 问题-删除问题（可批量）*/
		DeleteQuestionById("qyWeChat.QyWeChatApi.DeleteQuestionById", "投票、问卷管理 - 问题-删除问题（可批量）"),
		/** 投票、问卷管理 - 问题选项-添加问题选项 */
		AddQuestionItem("qyWeChat.QyWeChatApi.AddQuestionItem", "投票、问卷管理 -  问题选项-添加问题选项 "),
		/** 投票、问卷管理 - 问题选项-获取问题选项列表 */
		GetQuestionItemList("qyWeChat.QyWeChatApi.GetQuestionItemList", "投票、问卷管理 -  问题选项-获取问题选项列表"),
		/** 投票、问卷管理 - 问题选项-根据问题id删除问题选项 */
		DeleteQuestionItemById("qyWeChat.QyWeChatApi.DeleteQuestionItemById", "投票、问卷管理 -  问题选项-根据问题id删除问题选项 "),
		/** 投票、问卷管理 - 添加答案 */
		AddQuestionAnswer("qyWeChat.QyWeChatApi.AddQuestionAnswer", "投票、问卷管理 -  添加答案 "),
		/** 获取投票、问卷二维码地址*/
		GetVoteQuestionQRCode("qyWeChat.QyWeChatApi.GetVoteQuestionQRCode", "投票、问卷管理 -  获取投票、问卷二维码地址 "),
		/** 会议管理模块 - 申请会议*/
		ApplyMeeting("qyWeChat.QyWeChatApi.ApplyMeeting", "会议管理模块 - 申请会议"),
		/** 会议管理模块 - 查询会议列表*/
		QueryMeeting("qyWeChat.QyWeChatApi.QueryMeeting", "会议管理模块 - 查询会议列表"),
		/** 会议管理模块 - 编辑会议信息*/
		UpdateMeeting("qyWeChat.QyWeChatApi.UpdateMeeting", "会议管理模块 - 编辑会议信息"),
		/** 会议管理模块 - 设备管理 - 新增设备*/
		AddMeetingEquipment("qyWeChat.QyWeChatApi.AddMeetingEquipment", "会议管理模块 - 设备管理 - 新增设备"),
		/** 会议管理模块 - 设备管理 - 设备查询显示*/
		QueryMeetingEquipment("qyWeChat.QyWeChatApi.QueryMeetingEquipment", "会议管理模块 - 设备管理 - 设备查询显示"),
		/** 会议管理模块 - 设备管理 - 修改设备信息*/
		UpdateMeetingEquipment("qyWeChat.QyWeChatApi.UpdateMeetingEquipment", "会议管理模块 - 设备管理 - 修改设备信息"),
		/** 会议管理模块 - 会议室管理 - 新增会议室*/
		AddMeetingRoom("qyWeChat.QyWeChatApi.AddMeetingRoom", "会议管理模块 - 会议室管理 - 新增会议室"),
		/** 会议管理模块 - 会议室管理 - 查询会议室列表*/
		QueryMeetingRoom("qyWeChat.QyWeChatApi.QueryMeetingRoom", "会议管理模块 - 会议室管理 - 查询会议室列表"),
		/** 参与人员 - 查询参与人员*/
		QueryToMember("qyWeChat.QyWeChatApi.QueryToMember", "参与人员 - 查询参与人员"),
		/** 参与人员 - 添加参与人员*/
		AddToMember("qyWeChat.QyWeChatApi.AddToMember", "参与人员 - 添加参与人员"),
		/** 参与人员 - 删除参与人员*/
		DeleteToMember("qyWeChat.QyWeChatApi.DeleteToMember", "参与人员 - 删除参与人员"),
		/** 企微部门管理 - 获取部门列表*/
		QueryDepartment("qyWeChat.QyWeChatApi.QueryDepartment", "企微部门管理 - 获取部门列表"),
		/** 企微成员管理 - 获取部门成员*/
		QueryUser("qyWeChat.QyWeChatApi.QueryUser", "企微成员管理 - 获取部门成员"),
		/**获取企业微信config信息*/
		GetQyWxConfigInfo("qyWeChat.QyWeChatApi.GetQyWxConfigInfo","获取企业微信config信息"),
		/** 会议管理模块 - 学分管理 - 查询成员学分列表*/
		QueryMemberCredits("qyWeChat.QyWeChatApi.QueryMemberCredits","会议管理模块 - 学分管理 - 查询成员学分列表"),
		/** 会议管理模块 - 学分管理 - 编辑成员学分*/
		UpdateMemberCredits("qyWeChat.QyWeChatApi.UpdateMemberCredits","会议管理模块 - 学分管理 - 编辑成员学分"),
		/** 会议管理模块 - 学分管理 - 查询学分明细*/
		QueryMemberCreditsInfo("qyWeChat.QyWeChatApi.QueryMemberCreditsInfo","会议管理模块 - 学分管理 - 查询学分明细"),
		/** 会议管理模块 - 事项审批 - 查询事项列表*/
		QueryMeetingApproval("qyWeChat.QyWeChatApi.QueryMeetingApproval","会议管理模块 - 事项审批 - 查询事项列表"),
		/** 会议管理模块 - 事项审批 - 修改事项审批*/
		UpdateMeetingApproval("qyWeChat.QyWeChatApi.UpdateMeetingApproval","会议管理模块 - 事项审批 - 修改事项审批"),
		/** 会议管理模块 - 事项审批 - 添加事项审批*/
		AddMeetingApproval("qyWeChat.QyWeChatApi.AddMeetingApproval","会议管理模块 - 事项审批 - 添加事项审批"),
		/** 会议管理模块 - 会议安排 - 统计查看*/
		QueryMeetingSign("qyWeChat.QyWeChatApi.QueryMeetingSign","会议管理模块 - 会议安排 - 统计查看"),
		/** 微信端 - 会议管理模块 - 会议查询*/
		QwQueryMeeting("qyWeChat.QyWeChatApi.QwQueryMeeting","微信端 - 会议管理模块 - 会议查询"),
		/** 微信端  - 发起权限查询(可拓展)*/
		QwQueryPower("qyWeChat.QyWeChatApi.QwQueryPower","微信端  - 发起权限查询(可拓展)"),
		/** 活动管理模块 - 新建活动*/
		AddActivity("qyWeChat.QyWeChatApi.AddActivity","活动管理模块 - 新建活动"),
		/** 活动管理模块 - 活动列表查询*/
		QueryActivity("qyWeChat.QyWeChatApi.QueryActivity","活动管理模块 - 活动列表查询"),
		/** 活动管理模块 - 根据id获得活动信息*/
		QueryActivityById("qyWeChat.QyWeChatApi.QueryActivityById","活动管理模块 - 根据id获得活动信息"),
		/** 活动管理模块 - 修改活动信息*/
		UpdateActivity("qyWeChat.QyWeChatApi.UpdateActivity","活动管理模块 - 修改活动信息"),
		/** 活动管理模块 - 活动参加人员统计*/
		ActivityStatistics("qyWeChat.QyWeChatApi.ActivityStatistics","活动管理模块 - 活动参加人员统计"),
		/** 活动管理模块 - 活动列表查询 - 微信端*/
		QwQueryActivity("qyWeChat.QyWeChatApi.QwQueryActivity","活动管理模块 - 活动列表查询 - 微信端"),
		/** 微信端 - 会议管理模块 - 添加签到情况*/
		AddMeetingSign("qyWeChat.QyWeChatApi.AddMeetingSign","微信端 - 会议管理模块 - 添加签到情况"),
		/** 活动管理模块 - 进入群聊*/
		QwActivityGroupChat("qyWeChat.QyWeChatApi.QwActivityGroupChat","活动管理模块 - 进入群聊"),
		/** 活动会议 - 消息推送*/
		ActivityMeetingPush("qyWeChat.QyWeChatApi.ActivityMeetingPush","活动会议 - 消息推送"),
		/** 判断投票、问卷是否已经回答*/
		JudgeAnswerOrNot("qyWeChat.QyWeChatApi.JudgeAnswerOrNot","判断投票、问卷是否已经回答"),
		;
		@Override
		public String getModuleName() {
			return "qyWeChat";
		}

		private String name;
		private String apiName;

		@Override
		public String getApiName() {
			return apiName;
		}

		QyWeChat(String name, String apiName) {
			this.apiName = apiName;
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}
}

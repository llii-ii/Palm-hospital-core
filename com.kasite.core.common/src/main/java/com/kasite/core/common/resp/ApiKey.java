package com.kasite.core.common.resp;

public interface ApiKey {
	String getName();

	/**
	 * 锁号时将需要用到的节点保存到store中
	 * 
	 * @author daiyanshui
	 *
	 */
	enum Cache_His_Lock_Store implements ApiKey {
		numberStore("号源信息"), scheduleStore("排班信息"), lockStore("锁号信息"), isHalt("是否出诊"),

		regFee("挂号费"), treatFee("诊查费"), otherFee("其它费用"), drawPoint("取号地点"), fee("该次挂号需要收取的费用"),;
		private String name;

		public String getDesName() {
			return this.name;
		}

		Cache_His_Lock_Store(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	/**
	 * <p>Title : MODETYPE_10101122</p>
	 * <p>Description : 处方开药消息推送</p>
	 * <p>DevelopTools : Eclipse_x64_v4.7.1</p>
	 * <p>DevelopSystem : windows7</p>
	 * <p>Company : com.kst</p>
	 * @author : HongHuaYu
	 * @date : 2018年12月7日 上午11:46:50
	 * @version : 1.0.0
	 */
	enum MODETYPE_10101125 implements ApiKey {
		DeptName("开单科室名称"), DoctorName("开单医生名称"), Time("开单日期"), ReceiptNo("收据号"), HosName("医院名称"), UserName("用户名称"),
		ReceiptName("收据名称"), Price("金额"), Remark("收据描述"), URL("连接地址"),;
		private String name;
		
		public String getDesName() {
			return this.name;
		}
		
		MODETYPE_10101125(String name) {
			this.name = name;
			
		}
		
		@Override
		public String getName() {
			return this.name();
		}
	}
	/**
	 * 叫号推送 发送消息中 信息节点内容
	 * 
	 * @author daiyanshui
	 *
	 */
	enum MODETYPE_10101122 implements ApiKey {
		DeptName("科室名称"), DoctorName("医生名称"), RegisterDate("就诊日期"), sqNo("就诊序号"), HosName("医院名称"), Member("用户名称"),
		Sex("性别"), URL("连接地址"),memberName("患者姓名"),no("当前序号");
		private String name;

		public String getDesName() {
			return this.name;
		}

		MODETYPE_10101122(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	/**
	 * 当日挂号取消成功  发送消息中 信息节点内容
	 * 
	 * @author daiyanshui
	 *
	 */
	enum MODETYPE_10101113 implements ApiKey {
		UserName("用户名"), CardNo("就诊卡"), DeptName("科室名称"), DoctorName("医生名称"), RegisterDate("就诊日期"),
		HospitalName("医院名称"), CommendTime("候诊时间"), TimeIdStr("时段说明"), SqNo("预约序号"), URL("跳转地址"),;
		private String name;

		public String getDesName() {
			return this.name;
		}

		private MODETYPE_10101113(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	/**
	 * 预约挂号取消成功，发送消息中 信息节点内容
	 * 
	 * @author daiyanshui
	 */
	enum MODETYPE_10101112 implements ApiKey {
		UserName("用户名"), CardNo("就诊卡"), DeptName("科室名称"), DoctorName("医生名称"), RegisterDate("就诊日期"),
		HospitalName("医院名称"), CommendTime("候诊时间"), TimeIdStr("时段"), SqNo("号序"), URL("链接跳转地址"),;
		private String name;

		public String getDesName() {
			return this.name;
		}

		private MODETYPE_10101112(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	/**
	 * 药单支付推送 发送消息中 信息节点内容
	 * 
	 * @author daiyanshui
	 *
	 */
	enum MODETYPE_10101117 implements ApiKey {
		orderId("平台订单号"), price("交易金额"), transNo("第三方交易流水"), transTime("第三方交易时间"), channelId("渠道ID"), URL("跳转地址"),
		orderNum("订单短号"),memberName("用户姓名"),
		operatorName("操作人姓名");
		private String name;

		public String getDesName() {
			return this.name;
		}

		private MODETYPE_10101117(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	/**
	 * 预约挂号成功，发送消息中 信息节点内容
	 * 
	 * @author daiyanshui
	 *
	 */
	enum MODETYPE_10101111 implements ApiKey {
		CardNo("就诊卡"), UserName("用户名"), DeptName("科室名称"), DoctorName("医生名称"), RegisterDate("就诊日期"), CommendTime("候诊时间"),
		TimeIdStr("时段"), HospitalName("医院名称"),QueueNo("号序"),RegisterNo("挂号单号"),
		// 预约成功跳转的页面
		URL("跳转地址"),;
		private String name;

		public String getDesName() {
			return this.name;
		}

		private MODETYPE_10101111(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}
	/**
	 * 当日挂号成功
	 *
	 */
	enum MODETYPE_10101110 implements ApiKey {
		CardNo("就诊卡"), UserName("用户名"), DeptName("科室名称"), DoctorName("医生名称"), RegisterDate("就诊日期"), CommendTime("候诊时间"),
		TimeIdStr("时段"), HospitalName("医院名称"),QueueNo("号序"),RegisterNo("挂号单号"),
		// 预约成功跳转的页面
		URL("跳转地址"),;
		private String name;
		
		public String getDesName() {
			return this.name;
		}
		
		private MODETYPE_10101110(String name) {
			this.name = name;
			
		}
		
		@Override
		public String getName() {
			return this.name();
		}
	}
	/**
	 * 医技预约
	 * 
	 * @author daiyanshui
	 *
	 */
	enum MODETYPE_20101113 implements ApiKey {
		CardNo("就诊卡"), 
		HisKey("单据号"),
		Address("地址"),
		BookDate("预约日期"),
		AppointmentTime("预约时间"),
		FriendlyReminder("注意事项"),
		LabName("项目名称"),
		// 预约成功跳转的页面
		URL("跳转地址"),;
		private String name;

		public String getDesName() {
			return this.name;
		}

		private MODETYPE_20101113(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}
	/**
	 * 门诊充值 业务 发送消息中 信息节点内容
	 * 
	 * @author daiyanshui
	 *
	 */
	enum MODETYPE_10101119 implements ApiKey {
		CardTypeName("卡类型名称"), Price("交易金额"), UserName("用户名"), URL("跳转地址"),;
		private String name;

		public String getDesName() {
			return this.name;
		}

		private MODETYPE_10101119(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	/**
	 * 住院充值 业务 发送消息中 信息节点内容
	 * 
	 * @author daiyanshui
	 *
	 */
	enum MODETYPE_10101120 implements ApiKey {
		RechargeDate("缴费日期"), HospitalNo("住院号"), URL("跳转地址"),;
		private String name;

		public String getDesName() {
			return this.name;
		}

		private MODETYPE_10101120(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum getWechatShorturl implements ApiKey {
		short_url("微信短Url");
		private String name;

		public String getDesName() {
			return this.name;
		}

		private getWechatShorturl(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum uniteOrder implements ApiKey {
		/**
		 * 支付二维码
		 */
		QRCodeUrl("支付二维码URL"), appId("应用appid"), timeStamp(""), nonceStr(""), packageStr(""), paySign(""), signType("");
		private String name;

		public String getDesName() {
			return this.name;
		}

		private uniteOrder(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum checkPayOrder implements ApiKey {
		/**
		 * 支付金额
		 */
		price("交易金额"),
		/**
		 * 支付金额说明
		 */
		priceName("交易金额说明"),
		/**渠道ID*/
		clientId("渠道ID"),
		;
		private String name;

		public String getDesName() {
			return this.name;
		}

		private checkPayOrder(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	/**
	 * 发送阿里云短信验证码
	 */
	enum sendAliCodeSms implements ApiKey {
		/**
		 * 验证码
		 */
		code("验证码"),;
		private String name;

		public String getDesName() {
			return this.name;
		}

		private sendAliCodeSms(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	/**
	 * 新增订单接口出参定义
	 */
	enum AddOrderLocal implements ApiKey {
		OrderId("平台订单号"),;
		private String name;

		public String getDesName() {
			return this.name;
		}

		private AddOrderLocal(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum OrderListLocal implements ApiKey {
		BeginDate("订单开始时间"), PrescNo("处方号"), PayMoney("支付金额"), PriceName("支付金额名称"), TotalMoney("总金额"),
		OrderMemo("订单描述"), CardNo("卡号"), CardType("卡类型"), IsOnlinePay("是否在线缴费"), OrderId("订单号"), OperatorId("操作人ID"),
		OperatorName("操作人名"), ServiceId("服务代码"), PayState("支付状态"), BizState("业务状态"), OverState("订单完结状态"),
		ChannelId("渠道ID"), OrderReport("订单报表"), PayTimeLimit("支付时间限制"),;
		private String name;

		public String getDesName() {
			return this.name;
		}

		OrderListLocal(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	/**
	 * 取消订单接口出参定义
	 */
	enum OrderIsCancel implements ApiKey {
		RefundNo("取消交易支付流水号"), RefundOrderId("取消交易平台订单ID"),TransactionNo("支付订单号"),OrderId("全流程订单号");
		private String name;

		public String getDesName() {
			return this.name;
		}

		private OrderIsCancel(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum QueryClinicDept implements ApiKey {
		DeptCode("科室代码"), DeptName("科室名称"), ParentID("上级ID"), Intro("简介"), Remark("备注"), Address("地址"),
		DeptDoctors("科室下医生"),;
		private String name;

		public String getDesName() {
			return this.name;
		}

		private QueryClinicDept(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum QueryNumbers implements ApiKey {
		SourceCode("号源唯一ID"), SqNo("号序"), StartTime("开始时间"), EndTime("结束时间"), NumberInfo("号源描述-如果返回这个字段前端页面按照这个字段显示");
		private String name;

		public String getDesName() {
			return this.name;
		}

		private QueryNumbers(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum ReqHisLockOrder implements ApiKey {
		deptCode("科室代码"), doctorCode("医生工号"), regDate("就诊日期"), timeSlice("就诊时段0全天1上午2下午"), sqNo("号源序号"),
		sourceCode("号源ID"), scheduleId("排班ID"), cardType("用户卡类型"), cardNo("卡号"), regType("预约号别"), mobile("联系电话"),
		clinicCard("就诊卡号"), channelId("渠道ID"), orderId("平台订单号"),;
		private String name;

		public String getDesName() {
			return this.name;
		}

		private ReqHisLockOrder(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum LockOrder implements ApiKey {
		IsOnlinePay("是否线上缴费 1 是 2 否"), OrderId("平台订单号"), RegisterDate("就诊日期"), TimeId("时段ID 1上午2下午"),
		CommendTime("候诊时间"), DoctorName("医生名称"), DeptName("科室名称"), SqNo("号序"), Fee("费用总额"), FeeInfo("费用描述"),;
		private String name;

		public String getDesName() {
			return this.name;
		}

		private LockOrder(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum SetReMindNo implements ApiKey {
		QueryId, IfMsg, ReMindNo,;
		private String name;

		public String getDesName() {
			return this.name;
		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum querySubjectsByOrgId implements ApiKey {
		SubjectId, SubjectTitle, Overtype, Overtime, Status, QuestId, ItemId, SampleCount,;
		private String name;

		public String getDesName() {
			return this.name;
		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum judgeQuestSumBySubjectid implements ApiKey {
		Count,;
		private String name;

		public String getDesName() {
			return this.name;
		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum GetPayQRCode implements ApiKey {
//		WxQRCodeUrl,
//		AliQRCodeUrl,
		QRCodeUrl,;
		private String name;

		public String getDesName() {
			return this.name;
		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum GetGuide implements ApiKey {
		Id("信息点唯一ID"), Content("信息点描述Json"), USAGETYPE("设备类型"),OperatorId("设备ID");
		private String name;

		public String getDesName() {
			return this.name;
		}

		GetGuide(String name){
			
		}
		@Override
		public String getName() {
			return this.name();
		}
	}
	
	
	
	
	enum GetPayConfigKey implements ApiKey {
		WxPayConfigKey("微信支付商户配置ID"), 
		ZfbConfigKey("支付宝支付商户配置ID"),
		NetPayConfigKey("招行一网通商户配置ID"),
		;
		private String name;

		public String getDesName() {
			return this.name;
		}
		GetPayConfigKey(String name){
			
		}
		@Override
		public String getName() {
			return this.name();
		}
	}
	enum AddMemberNotCode implements ApiKey {
		MemberId("平台用户ID"),;
		private String name;

		public String getDesName() {
			return this.name;
		}

		private AddMemberNotCode(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}
	enum ValidateCardInfo implements ApiKey {
		HisMemberId("HIS用户ID"),
		MemberName("用户名称"),
		Sex("性别"),
		Age("年龄"),
		CardType("平台卡类型"),
		CardNo("卡号"),
		IdCardNo("身份证号码"),
		Mobile("手机号码"),
		;
		private String name;
		
		public String getDesName() {
			return this.name;
		}
		
		private ValidateCardInfo(String name) {
			this.name = name;
			
		}
		
		@Override
		public String getName() {
			return this.name();
		}
	}
	enum HISCreateQRCode implements ApiKey {
		addUrlParam("在二维码后面跟的参数"),
		hisMemberId("病人id"),
		idCardNo("身份证"),
		memberName("姓名"),
		hospitalNo("住院号"),
		sex("性别"),
		cardNo("卡号"),
		cardType("卡类型"),
		cardTypeName("卡类型名称"),
		inHospitalDept("病区"),
		inHospitalDate("入院日期"),
		inHospitalDays("入院天数"),
		balance("押金余额"),
		allFree("住院总费用"),
		status("病人状态 "
				+ "0 预约病人，"
				+ "1 入院登记（病区未受理），"
				+ "2 再院病人，"
				+ "3 出院登记病人，"
				+ "4正常出院病人，"
				+ "5出院欠费病人，"
				+ "9取消入院登记病人"),
		store("HIS返回的结果集")
		;
		private String name;

		public String getDesName() {
			return this.name;
		}

		private HISCreateQRCode(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum CreateQRCode implements ApiKey {
		QRUrl("二维码跳转URL"), QRPicUrl("二维码图片地址"), MemberId("平台用户ID"), GuideId("信息点ID"),;
		private String name;

		public String getDesName() {
			return this.name;
		}

		private CreateQRCode(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum CreatePayQRCode implements ApiKey {
		QRUrl("二维码跳转URL"), QRPicUrl("二维码图片地址"), MemberId("平台用户ID"), GuideId("信息点ID"), OrderId("平台订单号"),;
		private String name;

		public String getDesName() {
			return this.name;
		}

		private CreatePayQRCode(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum CreatePatientQRCode implements ApiKey {
		QRUrl("二维码跳转URL"), QRPicUrl("二维码图片地址"), MemberId("平台用户ID"), GuideId("信息点ID"), OrderId("平台订单号"),;
		private String name;

		public String getDesName() {
			return this.name;
		}

		private CreatePatientQRCode(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum SweepCodePay implements ApiKey {
		OrderId("平台订单号"), TotalFee("总金额"), HisOrderId("HIS订单ID"), PayTime("支付时间"), TransactionId("第三方交易流水"),
		ChargeType(""),;
		private String name;

		public String getDesName() {
			return this.name;
		}

		private SweepCodePay(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum HisUpdateOrder implements ApiKey {
		HisOrderId, HisFlowNo, HisDate, PharmacyPosition,
		/** 取药窗口 */
		ExamineRoomPosition,
		/** 缴费完成后，根据缴费项目类型提供检查检验科室院内位置 */
		PharmacyWaitings,
		/** 缴费完成后，根据缴费项目类型提供药房取药当前排队等候人数 */
		ExamineRoomWaitings,
		/** 缴费完成后，根据缴费项目类型提供检查检验科室当前排队等候人数 */
		;private String name;

		public String getDesName() {
			return this.name;
		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	/**
	 * 查询HIS门诊科室列表入参
	 * 
	 * @author daiyanshui
	 *
	 */
	enum HisQueryClinicDept implements ApiKey {
		deptCode("科室代码"), deptName("科室名称"), hosId("医院ID"), reqType("预约类别"), workDateEnd("预约结束时间"),
		workDateStart("预约开始时间"), channelId("渠道ID"), workTime("工作时间"),;
		private String name;

		public String getDesName() {
			return this.name;
		}

		private HisQueryClinicDept(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	/**
	 * 查询HIS门诊医生列表入参
	 * 
	 * @author daiyanshui
	 *
	 */
	enum HisQueryClinicDoctor implements ApiKey {
		doctorCode("医生工号"), doctorCodes("多个医生工号"), doctorName("医生名称"), doctorTitleCode("医生职称代码"), scheduleId("排班ID"),
		deptCode("科室代码"),deptName("科室名称"), hosId("医院ID"), reqType("预约类别"), workDateEnd("预约结束时间"), workDateStart("预约开始时间"),
		channelId("渠道ID"),deptCodes("多个科室"),isQueryHistory("查询历史医生");
		private String name;

		public String getDesName() {
			return this.name;
		}

		private HisQueryClinicDoctor(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return this.name();
		}
	}


	enum HisCheckEntityCard implements ApiKey {
		cardNo("卡号：实体卡的加密串"),
		cardType("卡类型"),
		memberName("姓名"),
		
		;
		private String name;

		public String getDesName() {
			return this.name;
		}

		private HisCheckEntityCard(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return this.name();
		}
	}


	enum HisGetConfigKeyByGuidInfo implements ApiKey {
		WxPayConfigKey("根据信息点获取商户配置"),
		ZfbConfigKey("根据信息点获取商户配置"),
		;
		private String name;

		public String getDesName() {
			return this.name;
		}

		private HisGetConfigKeyByGuidInfo(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return this.name();
		}
	}
	
	
	enum HisAddMember implements ApiKey {
		certNum("证件类型"), certType("证件号"),

		idCardNo("患者身份证号码"), mobile("手机号码"), patientName("患者名称"), sex("性别"), address("地址"),
		isChildren("是否儿童"),
		guardianName("监护人姓名"),
		
		;
		private String name;

		public String getDesName() {
			return this.name;
		}

		private HisAddMember(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	/**
	 * 查询HIS卡余额入参
	 * 
	 * @author daiyanshui
	 *
	 */
	enum HisQueryCardBalance implements ApiKey {
		Balance("卡余额"),
		CardNo("卡号"),
		PatientName("姓名"),
		Mobile("手机号码"),
		IdCardNo("患者身份证号码"),
		;
		
		private String name;

		public String getDesName() {
			return this.name;
		}

		private HisQueryCardBalance(String name) {
			this.name = name;

		}
		@Override
		public String getName() {
			return this.name();
		}
	}

	/**
	 * 查询HIS排班入参
	 * 
	 * @author daiyanshui
	 */
	enum HisQueryClinicSchedule implements ApiKey {
		hosId("医院ID"), deptCode("科室代码"), doctorCode("医生工号"), scheduleId("排班ID"), workDateEnd("结束时间"),
		workDateStart("开始时间"), channelId("渠道");
		private String name;

		public String getDesName() {
			return this.name;
		}

		private HisQueryClinicSchedule(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	/**
	 * 查询HIS号源入参
	 * 
	 * @author daiyanshui
	 */
	enum HisQueryNumbers implements ApiKey {
		hosId("医院id"), deptCode("科室名称"), doctorCode("医生工号"), regDate("就诊日期"), scheduleId("排班id"), timeSlice("时段id"),
		channelId("渠道"), memberName("病人姓名");
		private String name;

		public String getDesName() {
			return this.name;
		}

		private HisQueryNumbers(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	/**
	 * 查询HIS预约记录
	 * 
	 * @author daiyanshui
	 */
	enum HisQueryRegInfo implements ApiKey {
		cardType("卡类型"), cardNo("就诊卡"), hisOrderId("HIS订单号"), idCardNo("身份证"), orderId("平台订单号"), timeSlice("时段"),
		regFlag("预约状态"), startTime("开始时间"), endTime("结束时间"), channelId("渠道ID"),regDate("就诊日期"),doctorName("医生姓名"),
		bookServiceStore("如果在我们平台有挂号则传入HIS调用挂号的Store"),deptName("科室姓名"),regFee("挂号费"),deptCode("科室ID"),beginDate("下单时间");
		private String name;

		public String getDesName() {
			return this.name;
		}

		private HisQueryRegInfo(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	/**
	 * HIS挂号
	 * 
	 * @author daiyanshui
	 */
	enum HisBookService implements ApiKey {
		hosId("医院ID"), hospitalCode("his端医院ID"), sourceCode("号源ID"), scheduleId("排班ID"), deptCode("科室代码"),
		deptName("科室名称"), doctorCode("医生工号"), doctorName("医生名称"), timeSlice("时段"), timeName("时段名称"), regDate("就诊日期"),
		startTime("号源开始时间"), endTime("号源结束时间"),

		hisOrderId("his订单号"), orderId("订单号"), payFee("是否已经付费1是0否"),payMoney("实收金额"), transNo("交易流水号"),
//		transTime("第三方支付时间"),
		operatorId("操作人ID"), operatorName("操作人名称"),

		cardType("卡类型"), cardNo("卡号"), certNum("证件类型"), certType("证件号"),

		clinicCard("就诊卡号"), idCardNo("患者身份证号码"), mobile("手机号码"), hisPatientId("his端患者ID"), patientName("患者名称"),
		sex("性别"), address("地址"),

//		regFlag("1-当天挂号  2-预约"),
		channelId("渠道ID"),;
		private String name;

		public String getDesName() {
			return this.name;
		}

		HisBookService(String desc) {

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	/**
	 * HIS退号
	 * 
	 * @author lcz
	 */
	enum HisYyCancel implements ApiKey {

		hisOrderId("his端订单号"), orderId("订单号"), operatorId("操作人ID"), operatorName("操作人名称"), reason("取消原因"),
		channelId("渠道ID"),

		patientName("患者名称"), hisPatientId("his端患者ID"), mobile("患者手机号码"), idCardNo("患者身份证号码"), clinicCard("就诊卡号"),

		hosId("医院ID"), hospitalCode("his端医院编码"), deptCode("科室代码"), deptName("科室名称"), doctorCode("医生工号"),
		doctorName("医生姓名"), scheduleId("排班ID"), regDate("就诊日期"), timeSlice("时段"), timeName("时段名称"), sourceCode("号源ID"),
		sqNo("序号"), startTime("开始时间"), endTime("结束时间"),beginDate("下单时间"),

		;
		private String name;

		public String getDesName() {
			return this.name;
		}

		HisYyCancel(String desc) {

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	/**
	 * 查询HIS指定订单金额 再次确认订单金额，如果his订单金额不对则不允许继续
	 * 
	 * @author daiyanshui
	 *
	 */
	enum HisGetOrderSum implements ApiKey {
		Price;
		private String name;

		public String getDesName() {
			return this.name;
		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	/**
	 * 查询HIS处方详情
	 * 
	 * @author daiyanshui
	 *
	 */
	enum HisQueryOrderPrescriptionInfo implements ApiKey {
		hisOrderId("HIS订单id"), prescNo("处方当号"), cardNo("用户就诊卡号"), cardType("卡类型"), orderMemo("订单描述"),
		hisMemberId("HIS用户ID"),;
		private String name;

		public String getDesName() {
			return this.name;
		}

		private HisQueryOrderPrescriptionInfo(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	/**
	 * 根据住院号查询HIS住院信息
	 * 
	 * @className: HisQueryHospitalUserInfo
	 * @author: lcz
	 * @date: 2018年10月16日 下午3:21:45
	 */
	enum HisQueryHospitalUserInfo implements ApiKey {
		cardNo("卡类型"), cardType("卡号码"), memberName("用户名称"), mobile("手机号码"), idCardNo("身份证号码"), hospitalNo("住院号"),
		mcardNo("医保卡"), memberId("平台用户唯一ID"),;
		private String name;

		public String getDesName() {
			return this.name;
		}

		HisQueryHospitalUserInfo(String desc) {

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum HisGetReportList implements ApiKey {
		/** "报告单类型 1检验报告单 2为检查报告单" **/
		reportType("报告单类型  1检验报告单 2为检查报告单"), cardType("卡类型"), cardNo("卡号"), idCardNo("身份号码"), memberName("患者名称"),
		pIndex("页码"), pSize("每页条数"),diyJson("个性化对接时候传入的个性化参数"),mobile("电话"),startDate("开始时间"),endDate("结束时间");
		private String name;

		public String getDesName() {
			return this.name;
		}

		HisGetReportList(String desc) {

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum HisGetReportInfo implements ApiKey {
		/** "报告单类型 1检验报告单 2为检查报告单" **/
		reportType("报告单类型  1检验报告单 2为检查报告单"), reportId("报告单ID"), startDate("报告单时间"), eventNo("事件号"), sampleNo("样本号"),diyJson("个性化对接时候传入的个性化参数"),;
		private String name;

		public String getDesName() {
			return this.name;
		}

		HisGetReportInfo(String desc) {

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum HisGetQueueInfo implements ApiKey {
		cardType("卡类型"), cardNo("卡号"), memberName("用户名称"),;
		HisGetQueueInfo(String desc) {

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum getDCSummary implements ApiKey {
		DataType, DataValue;
		private String name;

		public String getDesName() {
			return this.name;
		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum getDataCollectionGrid implements ApiKey {
		OperTime, Type1, Type2, Type3, Type4, Type5, Type6;
		private String name;

		public String getDesName() {
			return this.name;
		}

		@Override
		public String getName() {
			return this.name();
		}
	}
	

	enum getDCLine implements ApiKey {
		OperTime, ChannelData;
		private String name;

		public String getDesName() {
			return this.name;
		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum getDCbar implements ApiKey {
		ChannelId("渠道ID"), ChannelName("渠道名称"), DataValue("");
		private String name;

		public String getDesName() {
			return this.name;
		}

		private getDCbar(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum QueryOrderState implements ApiKey {
		OrderState("订单状态"),
		TransactionNo("商户支付流水号"),
		RefundNo("商户退款流水号"),
		RefundOrderId("全流程退款订单号"),
		OrderId("全流程订单号"),
		TradeResult("交易结果");
		private String name;

		public String getDesName() {
			return this.name;
		}
		private QueryOrderState(String name) {
			this.name = name;

		}
		@Override
		public String getName() {
			return this.name();
		}
	}

	enum QueryMerchantOrder implements ApiKey {
		OrderId("平台订单号"), TransactionNo("第三方交易订单id"), PayTime("支付时间"), OrderState("订单状态"), Fee("交易金额");
		private String name;

		public String getDesName() {
			return this.name;
		}

		private QueryMerchantOrder(String name) {
			this.name = name;

		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	enum QueryMerchantRefund implements ApiKey {
		TotalFee, RefundFee, RefundStatus, RefundTime, RefundOrderId, RefundNo;
		private String name;

		public String getDesName() {
			return this.name;
		}

		@Override
		public String getName() {
			return this.name();
		}
	}

	/**
	 * 对账账单参数
	 */
	enum BillRFPro implements ApiKey {
		CurrentDate("当前时间"), YesterdayMoney("昨日统计金额"), YesterdayCount("昨日交易笔数"), YesterdayWXMoney("昨日微信统计金额"), YesterdayZFBMoney("昨日支付宝统计金额"), 
		ChannelList("渠道列表"), PayList("支付方式列表"),FilePath("文件路径参数"), BillMap("对账账单列表"), HisBillErrorMap("his错误账单池");
		private String name;

		public String getDesName() {
			return this.name;
		}
		
		private BillRFPro(String name) {
			this.name = name;

		}
		@Override
		public String getName() {
			return this.name();
		}
	}

	/**
	 * 渠道管理出参
	 */
	enum QueryChannel implements ApiKey {
		ApiList("渠道权限列表"), IpWhiteList("白名单列报表"),;
		private String name;

		public String getDesName() {
			return this.name;
		}
		private QueryChannel(String name) {
			this.name = name;

		}
		@Override
		public String getName() {
			return this.name();
		}
	}


	enum AddLocalOrderValidateBefore implements ApiKey {
		PayMoney("支付金额"),TotalMoney("总金额"),ChannelId("渠道ID"),OrderState("订单状态"),
		PrescNo("处方单号");
		private String name;

		public String getDesName() {
			return this.name;
		}

		private AddLocalOrderValidateBefore(String name) {
			this.name = name;

		}
		@Override
		public String getName() {
			return name();
		}
	}
	
	enum QueryRefundableOrderListResp implements ApiKey {
		OrderId("平台订单号"), ServiceId("服务ID"), Price("交易金额"), TotalPrice("交易总金额"),TransactionNo("商户订单号")
		,ChannelId("渠道ID"), BizState("订单业务执行状态"), PayState("订单支付状态"),OverState("订单最终状态"),ConfigKey("支付配置")
		,BeginDate("创建订单时间");
		private String name;

		public String getDesName() {
			return this.name;
		}

		private QueryRefundableOrderListResp(String name) {
			this.name = name;

		}
		@Override
		public String getName() {
			return name();
		}
	}
	
	enum QueryRefundableOrderListResp_V1 implements ApiKey {
		PayMoney("支付金额"),TotalMoney("总金额"),ChannelId("渠道ID"),OrderState("订单状态");
		private String name;

		public String getDesName() {
			return this.name;
		}

		private QueryRefundableOrderListResp_V1(String name) {
			this.name = name;

		}
		@Override
		public String getName() {
			return name();
		}
	}
	
	enum QueryMemberRefundableMoneyResp implements ApiKey {
		Balance,//余额
		RefundableBalance,//可退余额
		;
		private String name;

		public String getDesName() {
			return this.name;
		}
		@Override
		public String getName() {
			return this.name();
		}
	}
	enum GetSysRoleResp implements ApiKey {
		roleName("角色名称"),
		roleId("角色ID"),
		remark("备注")
		;
		private String name;
		GetSysRoleResp(String name){
			
		}
		public String getDesName() {
			return this.name;
		}
		@Override
		public String getName() {
			return this.name();
		}
	}
	
	enum oPDRechargeRefundFreezeResp implements ApiKey {
		OutRefundOrderId,;
		private String name;
		/**
		 * @return
		 */
		@Override
		public String getName() {
			return this.name;
		}
		
	}
	enum MsgCenterResp implements ApiKey {
		template_id("模板Id"),
		title("模板标题"),
		primary_industry("一级行业"),
		deputy_industry("二级行业"),
		content("模板内容"),
		example("模板实例");
		private String name;
		MsgCenterResp(String name){
			this.name = name;
		}
		
		public String getDesName() {
			return this.name;
		}

		/**
		 * @return
		 */
		@Override
		public String getName() {
			return this.name();
		}
		
	}
	enum YjYY implements ApiKey {
		CardNo("卡号"),
		CardType("卡类型"),
		HisKey("单据号"),
		BillDate("开单日期"),
		LabName("项目名称"),
		LabCode("项目代码"),
		FriendlyReminder("注意事项"),
		PrintAppointmentID("预约id"),
		AppointmentStatus("预约状态"),
		AppointmentTime("预约时间"),
		IsBook("是否可预约"),
		LabPrice("项目价格"),
		OrderId("订单id"),
		BillDept("开单科室"),
		ExamDept("执行科室"),
		BookDate("预约日期"),
		SourceId("号源id"),
		Sequence("排队号"),
		Time("号源时间"),
		Address("地址"),
		WeekDay("星期"),
		StartAppointmentTime("开始时间"),
		EndAppointmentTime("结束时间"),
		;
		private String name;
		YjYY(String name){
			this.name = name;
		}
		
		public String getDesName() {
			return this.name;
		}
		/**
		 * @return
		 */
		@Override
		public String getName() {
			return this.name();
		}
		
	}
	
	enum QuerySelfRefundRecordList implements ApiKey {
		RefundAmount("已退订单金额"),UpdateTime("更新时间"),RefundableBalance("可退余额"),
		RefundableCount("可退订单笔数"),RefundCount("已退订单笔数"),State("执行状态"),Id("主键");
		private String name;
		QuerySelfRefundRecordList(String name){
			this.name = name;
		}
		
		public String getDesName() {
			return this.name;
		}
		/**
		 * @return
		 */
		@Override
		public String getName() {
			return this.name();
		}
	}
	
	enum QuerySelfRefundRecordInfo implements ApiKey {
		PayState("退款状态"),OrderNum("订单短号"),RefundPrice("退款金额"),ChannelType("渠道名称"),
		RefundRemark("退费备注"),BeginDate("退款开始日期"),EndDate("退款结束日期"),FailReason("失败原因");
		
		QuerySelfRefundRecordInfo(String name){
			this.name = name;
		}
		private String name;
		/**
		 * @return
		 */
		@Override
		public String getName() {
			return this.name();
		}
		public String getDesName() {
			return this.name;
		}
	}
	
	enum HisMergeSettledPayReceiptResp implements ApiKey {
		HisFlowNo("his流水号"),;
		
		HisMergeSettledPayReceiptResp(String name){
			this.name = name;
		}
		private String name;
		/**
		 * @return
		 */
		@Override
		public String getName() {
			return this.name();
		}
		public String getDesName() {
			return this.name;
		}
	}
	enum HisBindMemberCardResp implements ApiKey {
		cardNo("卡号"),HisMemberId("HIS用户ID"),;
		
		HisBindMemberCardResp(String name){
			this.name = name;
		}
		private String name;
		/**
		 * @return
		 */
		@Override
		public String getName() {
			return this.name();
		}
		public String getDesName() {
			return this.name;
		}
	}
}

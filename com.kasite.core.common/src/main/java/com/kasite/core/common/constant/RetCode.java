package com.kasite.core.common.constant;

import com.yihu.hos.IRetCode;


/**
 * ClassName: RetCode
 *
 * @Description: TODO 返回结果定义枚举类
 * @author 無
 * @date 2018年4月24日 下午2:24:33
 */
public interface RetCode extends IRetCode {
	


	/**
	 * 调用HIS接口异常返回值
	 * 
	 * @author daiys
	 * @date 2015-1-4
	 */
	enum HIS implements RetCode {
		/**
		 * 调用 HIS 业务逻辑异常 返回 -14444 异常 前端捕获到该异常需要进行告警通知
		 * 
		 */
		ERROR_14444(-14444, "调用HIS业务逻辑异常"),
		;
		/** 代码 */
		private Integer code;
		/** 消息 */
		private String message;

		@Override
		public String getMessage() {
			return message;
		}

		HIS(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		@Override
		public Integer getCode() {
			return code;
		}

		/**
		 * @return
		 */
		@Override
		public String getExceptMsg() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @param arg0
		 * @return
		 */
		@Override
		public String getMessage(String... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	/**
	 * 调用接口成功返回
	 * 
	 * @author daiys
	 * @date 2015-1-4
	 */
	enum Success implements RetCode {
		/**
		 * 业务逻辑公共返回 Code 所有定义为 10000
		 */
		RET_10000(10000, "成功"),
		;
		/** 代码 */
		private Integer code;
		/** 消息 */
		private String message;

		@Override
		public String getMessage() {
			return message;
		}

		Success(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		@Override
		public Integer getCode() {
			return code;
		}

		/**
		 * @return
		 */
		@Override
		public String getExceptMsg() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @param arg0
		 * @return
		 */
		@Override
		public String getMessage(String... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	/**
	 * 登录接口返回异常代码
	 * 
	 * @author daiys
	 * @date 2015-1-4
	 */
	enum Login implements RetCode {
		/**
		 * 该公众号未启用，请联系管理员 
		 */
		NotStart(-120000, "该公众号未启用，请联系管理员。"),
		/**
		 * 该公众号配置异常，请联系管理员重新配置 
		 */
		NotConfig(-120001, "该公众号配置异常，请联系管理员重新配置."),
		/**
		 * 账号已被锁定,请联系管理员
		 */
		isLock(-120002, "账号已被锁定,请联系管理员。"),
		/**
		 * 账号未登录
		 */
		NoLogin(-120003, "账号未登录,请联系管理员。"),
		/**
		 * -120004 登录失败，账号或密码不正确
		 */
		NotUserOrPassErr(-120004, "登录失败，账号或密码不正确"),
		;
		/** 代码 */
		private Integer code;
		/** 消息 */
		private String message;

		@Override
		public String getMessage() {
			return message;
		}

		Login(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		@Override
		public Integer getCode() {
			return code;
		}

		/**
		 * @return
		 */
		@Override
		public String getExceptMsg() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @param arg0
		 * @return
		 */
		@Override
		public String getMessage(String... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	/**
	 * 调用接口返回代码定义 定义范围： -14001 开始
	 * 
	 * @author daiys
	 * @date 2015-1-4
	 */
	enum Common implements RetCode {

//		/**
//		 * 业务逻辑公共返回 Code 所有定义为 10000
//		 */
//		RET_10000(10000, "成功"),
		/**
		 * 业务逻辑公共返回 Code 系统异常
		 */
		ERROR_SYSTEM(-10000, "系统异常"),

		/**
		 * 弹窗异常，需要做用户提示的时候返回这个异常 必须返回异常文案
		 */
		ERROR_ALERT_MESSAGE(-50000, "弹窗异常"),
		/**
		 * 执行sql异常
		 */
		CALL_SQL_ERROR(-30000, "执行sql异常"),

		/**
		 * -20001 Json解析错误
		 */
		ERROR_JSON(-20001, "Json解析错误"),

		/**
		 * -20000 参数错误
		 */
		ERROR_PARAM(-20000, "参数错误"),
		/**
		 * -99999 未知异常
		 */
		ERROR_UNKNOWN(-99999, "未知异常"),

		/**
		 * "获取当前操作时间异常。"
		 */
		GetDate(-14001, "获取当前操作时间异常。"),
		/**
		 * "初始化配置文件出错."
		 */
		Init(-14002, "初始化配置文件出错."),
		/**
		 * 解析日期类型数据出错"
		 */
		ParseDate(-14004, "解析日期类型出现异常"),
		/**
		 * 调用医院端接口异常返回
		 */
		CallHosClientError(-14005, "调用医院端接口异常返回"),
		/**
		 * 调用Hop接口网关异常
		 */
		CallHOPClientError(-14006, "服务端接口异常"),
		/**
		 * -14007 数据库脚本执行查询异常
		 */
		EXECSqlError(-14007, "数据库脚本执行查询异常"),
		/**
		 * -14008请求的参数为空
		 */
		NULLERROR(-14008, "请求的参数为空"),
		/**
		 * -14009 XML解析异常
		 */
		ERROR_XMLERROR(-14009, "XML解析异常"),
		/**
		 * -14010 转译出参异常
		 */
		ERROR_TRANSLATIONRESP(-14010, "转译出参异常"),
		/**
		 * -14011 数据库脚本执行查询异常
		 */
		ERROR_SQLEXECERROR(-14444, "数据库脚本执行查询异常"),
		/**
		 * -14012 初始化系统配置信息异常。
		 */
		ERROR_INIT_SYSCONFIG(-14012, "初始化系统配置信息异常。"),
		/**
		 * 调用HIS接口异常
		 */
		ERROR_CALLHIS(-14013, "调用HIS接口异常"),

		/**
		 * 请求微信无响应异常
		 */
		CallWeiXinError(-99999, "请求微信无响应异常"),

		/**
		 * 调用Hop接口网关异常
		 */
		Call(-14006, "服务端接口异常"),
		/**
		 * json操作异常
		 */
		ERROR_JSONOPERTOR(-100003, "json操作异常"),
		/**
		 * -64002 没有找到锁号信息。
		 */
		ERROR_NOTLOCK(-64002, "没有找到锁号信息。"),
		/**
		 * 渠道ID 不能为空
		 */
		ERROR_NOCHANNELID(-14014, "渠道ID不能为空"),
		/**
		 * -20002 TransactionCode值为空
		 */
		NULL_TRANSACTIONCODE(-20002, "TransactionCode值为空"),
		/**
		 * 会话过期！
		 */
		ERROR_NOSESSION(-14015, "会话过期！"),
		/**
		 * 未找到入参 InterfaceMessage ！
		 */
		ERROR_NOTMESSAGE(-14016, "未找到入参 InterfaceMessage ！"),
		/**
		 * 未找定义返回值 ！
		 */
		ERROR_NOTRESPONSE(-14017, "未找定义返回值 ！"),
		/**
		 * 返回结果集为空！
		 */
		ERROR_NOTRESULT(-14018, "返回结果集为空！"),
		/**
		 * 返回结果集为空！
		 */
		OAUTH_ERROR_NOPROMIIME(-40009, "您暂无该接口的访问权限，请联系管理员开通权限！"),
		
		OAUTH_ERROR_NOAUTHODINFO(-14019, "从接口中获取鉴权信息异常未取到鉴权信息！"),

		ERROR_NOT_METHOD(-14020, "该接口不存在，请与管理员核实接口是否存在！"),

		ERROR_TOKEN_INVOKE(KstHosConstant.OAUTH_TOKEN_INVALID, "该接口不存在，请与管理员核实接口是否存在！"),
		
		ERROR_LOGIN_PASSWORD(-14021, "登录密码输入错误！"),
		
		ERROR_PAY_PASSWORD(-14022, "支付密码输入错误！"),
		
		ERROR_BILL_REVERSE(-14023, "只有支付订单可以冲正操作！"),
		
		ERROR_THIRD_PARTY(-14024,"调用第三方接口错误！"),
		
		ERROR_CALL_WEIXIN_PAY(-14025,"调用微信支付接口错误！"),
		;
		/** 代码 */
		private Integer code;
		/** 消息 */
		private String message;

		@Override
		public String getMessage() {
			return message;
		}

		Common(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		@Override
		public Integer getCode() {
			return code;
		}

		/**
		 * @return
		 */
		@Override
		public String getExceptMsg() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @param arg0
		 * @return
		 */
		@Override
		public String getMessage(String... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	public enum Balance implements RetCode {
		
		ERROR_SUMMARYTHREEPARTYBILL(-9002,"获取每日汇总账单数据总数为零,暂无数据!"),
		;

		/** 代码 */
		private Integer code;
		/** 消息 */
		private String message;

		@Override
		public String getMessage() {
			return message;
		}

		private Balance(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		@Override
		public Integer getCode() {
			return code;
		}

		/**
		 * @return
		 */
		@Override
		public String getExceptMsg() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @param arg0
		 * @return
		 */
		@Override
		public String getMessage(String... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	/**
	 * 调用接口返回代码定义 定义范围： -14001 开始
	 * 
	 * @author daiys
	 * @date 2015-1-4
	 */
	public enum YY implements RetCode {

		/**
		 * -63003 没有找到可预约的排班信息。
		 */
		ERROR_NOTSCHEDULE(-63003, "没有找到可预约的排班信息。"),
		/**
		 * -63004 没有找到可预约的号源信息。
		 */
		ERROR_NOTNUMBER(-63004, "没有找到可预约的号源信息。"),

		/**
		 * -63005 日期类型解析异常。
		 */
		ERROR_PARSEDATE(-63005, "日期类型解析异常。"),

		/**
		 * -63006 预约限制。
		 */
		ERROR_YYCHECK(-63006, "预约限制"),
		/**
		 * -63007 退号限制。
		 */
		ERROR_CANCELCHECK(-63007, "退号限制"),

		/**
		 * -63008 订单系统接口调用失败。
		 */
		ERROR_CALLORDER(-63008, "订单系统接口调用失败"),

		/**
		 * -63009 基础信息系统接口调用失败。
		 */
		ERROR_CALLBASIC(-63009, "基础信息系统接口调用失败"),
		/**
		 * -14005 调用医院接口异常。
		 */
		ERROR_CALLHIS(-14005, "调用医院接口异常"),

		/**
		 * -63011 没有找到预约信息。
		 */
		ERROR_NOTWATER(-63011, "没有找到预约信息"),

		/**
		 * -63012 没有找到患者信息。
		 */
		ERROR_PATIENT(-63012, "没有找到患者信息"),
		/**
		 * -63012 没有找到患者信息。
		 */
		ERROR_NOTYYRULE(-63013, "没有预约规则配置信息"),

		/** 锁号异常 **/
		/**
		 * -64001 您已经锁定了一个号源，不能重复锁号
		 */
		ERROR_LOCKAGAIN(-64001, "您已经锁定了一个号源，不能重复锁号。"),
		/**
		 * -64002 没有找到锁号信息。
		 */
		ERROR_NOTLOCK(-64002, "没有找到锁号信息。"),

		/**
		 * "获取当前操作时间异常。"
		 */
		GetDate(-14001, "获取当前操作时间异常。"),

		/**
		 * 调用医院端接口异常返回
		 */
		CallHosClientError(-14005, "调用医院端接口异常返回"),

		/**
		 * -64003 锁号失败。
		 */
		ERROR_LOCK(-64003, "锁号失败。"),

		/**
		 * -64004 释号失败。
		 */
		ERROR_UNLOCK(-64004, "释号失败。"),

		/**
		 * -64005 预约失败。
		 */
		ERROR_BOOK(-64005, "预约失败。"),
		/**
		 * -64006 预约失败。锁号信息过期。根据该状态码判断会退还挂号费用，慎用
		 */
		ERROR_BOOK_UNLOCK(-64006, "预约失败。锁号信息过期。"),

		/**
		 * -63000 数据库脚本执行查询异常
		 */
		ERROR_SQLEXECERROR(-64007, "数据库脚本执行查询异常"),
		/**
		 * 费用异常 不能支付 限额外的金额
		 */
		ERROR_Fee(-64007, "费用异常，不能支付限额外的金额"),
		;
		/** 代码 */
		private Integer code;
		/** 消息 */
		private String message;

		@Override
		public String getMessage() {
			return message;
		}

		YY(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		@Override
		public Integer getCode() {
			return code;
		}

		/**
		 * @return
		 */
		@Override
		public String getExceptMsg() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @param arg0
		 * @return
		 */
		@Override
		public String getMessage(String... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	/**
	 * 基础信息错误
	 * 
	 * @author Administrator
	 *
	 */
	enum Basic implements RetCode {

		/**
		 * 数据库操作异常
		 */
		ERROR_EXECUTESQL(-100001, "数据库操作异常"),
		/**
		 * 日期格式化异常
		 */
		ERROR_DATEFORMAT(-100002, "日期格式化异常"),
		/**
		 * json操作异常
		 */
		ERROR_JSONOPERTOR(-100003, "json操作异常"),
		/**
		 * 获取数据库连接异常
		 */
		ERROR_GETDBCONNECT(-100004, "获取数据库连接异常"),
		/**
		 * 数据不存在
		 */
		ERROR_CANNOTEXIST(-100005, "数据不存在"),
		/**
		 * 数据已存在
		 */
		ERROR_DATAEXIST(-100006, "数据已存在"),
		/**
		 * 交易代码不正确
		 */
		ERROR_TRANSACTIONCODE(-100036, "交易代码不匹配"),
		/**
		 * 用户名输入有误或不存在
		 */
		ERROR_USERNAME(-100007, "用户名输入有误或不存在"),
		/**
		 * 登陆失败：密码输入有误
		 */
		ERROR_LOGIN(-100008, " 登陆失败：密码输入有误"),
		/**
		 * 推送消息异常
		 */
		ERROR_SENDMSG(-100009, " 登陆失败：密码输入有推送消息异常"),
		/**
		 * 验证码输入有误
		 */
		ERROR_CHECKPORVINGCODE(-100010, "验证码输入有误"),
		/**
		 * 验证码已失效
		 */
		ERROR_PORVINGCODEFAILURE(-100011, "验证码已失效"),
		/**
		 * 本地身份验证异常
		 */
		ERROR_CHECKIDCARDNO(-100012, "本地身份验证异常"),
		/**
		 * 字符编码异常
		 */
		ERROR_ENCODE(-100013, "字符编码异常"),
		/**
		 * 通过身份证获取出生日期异常
		 */
		ERROR_GETBIRTHDAYBYIDCARDNO(-100014, "通过身份证获取出生日期异常"),
		/**
		 * 成员信息有误
		 */
		ERROR_MEMBERINFO(-100015, "成员信息有误"),
		/**
		 * 该账号已被其他用户绑定
		 */
		ERROR_binding(-100016, "该账号已被其他用户绑定"),
		/**
		 * 旧密码输入有误
		 */
		ERROR_OLDPASSWORD(-100017, "旧密码输入有误"),
		/**
		 * 身份证不合法
		 */
		ERROR_IDCARD(-100018, "身份证不合法"), 
		/**-100019 就诊卡绑定失败，院内未查询到就诊卡信息**/
		ERROR_BindClinicCard(-100019, "就诊卡绑定失败，院内未查询到就诊卡信息"), 
		/**-100019 验证码验证失败**/
		ERROR_CHECKPROVINGCODE(-100020, "验证码验证失败"),
		/**
		 * 就诊卡绑定失败
		 */
		ERROR_ADDCARD(-100021, "就诊卡绑定失败"),
		/**
		 * 住院号绑定失败，院内未查询到该住院号信息
		 */
		ERROR_BINDHOSPITALNO(-100023, "住院号绑定失败，院内未查询到该住院号信息"),
		/**
		 * 请输入住院号
		 */
		ERROR_CARDTYPE(-100024, "请输入住院号"),
		
		ERROR_CHECKHOSPITALNO(-100025, "住院号仍存在"),

		/**
		 * 未找到用户信息
		 */
		ERROR_NOTFINDMEMBER(-100026, "未找到用户"), 
		
		/**
		 *-100027 添加用户已达上限
		 */
		ERROR_ADDUSERLIMIT(-100027, "添加用户已达上限"), 
		
		;
		/** 代码 */
		private Integer code;
		/** 消息 */
		private String message;

		@Override
		public String getMessage() {
			return message;
		}

		Basic(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		@Override
		public Integer getCode() {
			return code;
		}

		/**
		 * @return
		 */
		@Override
		public String getExceptMsg() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @param arg0
		 * @return
		 */
		@Override
		public String getMessage(String... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	enum Article implements RetCode {
		/**
		 * 查询文章管理成功
		 */
		QueryArticle(12001, "查询文章管理成功"), DeleteArticle(12004, "删除文章成功"), UpdateArticle(121003,
				"修改文章管理操作成功"), QueryArticleDetail(12005, "查询文章详情成功"), UpdateArticleError(-12003,
						"修改文章管理操作失败"), AddArticleError(-12002, "新增文章管理操作失败"), QueryArticleError(-12001,
								"查询文章管理失败"), QueryArticleDetailError(-12005, "查询文章详情失败"), DeleteArticlErrore(-12004,
										"删除文章失败");
		/** 代码 */
		private Integer code;
		/** 消息 */
		private String message;

		@Override
		public String getMessage() {
			return message;
		}

		Article(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		@Override
		public Integer getCode() {
			return code;
		}

		/**
		 * @return
		 */
		@Override
		public String getExceptMsg() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @param arg0
		 * @return
		 */
		@Override
		public String getMessage(String... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	enum ReportForms implements RetCode {
		/**
		 * 数据库操作异常
		 */
		ERROR_EXECUTESQL(-500001, "数据库操作异常"),
		/**
		 * 日期格式化异常
		 */
		ERROR_DATEFORMAT(-500002, "日期格式化异常"),
		/**
		 * json操作异常
		 */
		ERROR_JSONOPERTOR(-500003, "json操作异常"),
		/**
		 * 调用查询预约记录异常
		 */
		ERROR_QUERYREGINFO(-500004, "查询预约记录异常"),
		/**
		 * 交易代码不正确
		 */
		ERROR_TRANSACTIONCODE(-500005, "交易代码不匹配"),
		/**
		 * 调用数据收集接口异常
		 */
		ERROR_CALLDataCollection(-500005, "调用数据收集接口异常"),;

		/** 代码 */
		private Integer code;
		/** 消息 */
		private String message;

		@Override
		public String getMessage() {
			return message;
		}

		ReportForms(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		@Override
		public Integer getCode() {
			return code;
		}

		/**
		 * @return
		 */
		@Override
		public String getExceptMsg() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @param arg0
		 * @return
		 */
		@Override
		public String getMessage(String... arg0) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	/**
	 * 调用接口返回代码定义 定义范围： -14001 开始
	 * 
	 * @author daiys
	 * @date 2015-1-4
	 */
	enum Order implements RetCode {
		/**
		 * 数据库操作异常
		 */
		ERROR_EXECUTESQL(-14007, "数据库操作异常"),
		/**
		 * 日期格式化异常
		 */
		ERROR_DATEFORMAT(-300002, "日期格式化异常"),
		/**
		 * json操作异常
		 */
		ERROR_JSONOPERTOR(-300003, "json操作异常"),
		/**
		 * 获取数据库连接异常
		 */
		ERROR_GETDBCONNECT(-300004, "获取数据库连接异常"),
		/**
		 * 数据不存在
		 */
		ERROR_CANNOTEXIST(-300005, "数据不存在"),
		/**
		 * 数据已存在
		 */
		ERROR_DATAEXIST(-300006, "数据已存在"),
		/**
		 * hisId数据不存在
		 */
		ERROR_HISIDCANNOTEXIST(-300007, "hisId不存在"),
		/**
		 * 交易代码不正确
		 */
		ERROR_TRANSACTIONCODE(-400006, "交易代码不匹配"),
		/**
		 * 调用退号接口异常
		 */
		ERROR_CALLTH(-400007, "调用退号接口异常"),
		/**
		 * 订单状态异常
		 */
		ERROR_ORDERSTATE(-400008, "订单状态异常"),
		/**
		 * 订单金额不正确
		 */
		ERROR_ORDERPRICE(-400009, "订单金额不正确"),
		/**
		 * 调用HIS接口异常
		 */
		ERROR_CALLHIS(-400010, "调用HIS接口异常"),
		/**
		 * 必传值未传入
		 */
		ERROR_SENDVALUE(-400011, "必传值未传入"),
		/**
		 * 调用新增订单接口异常
		 */
		ERROR_CALLADDORDER(-400012, "调用新增订单接口异常"),
		/**
		 * 卡号为空
		 */
		ERROR_CARDNOEX(-400013, "卡号为空"),
		/**
		 * 卡号校验his不通过，无效卡
		 */
		ERROR_CARDNONOTHIS(-400014, "卡号在his中无效"),
		/**
		 * -63000 数据库脚本执行查询异常
		 */
		ERROR_SQLEXECERROR(-63000, "数据库脚本执行查询异常"),

		/**
		 * -63002 XML解析异常
		 */
		ERROR_XMLERROR(-63002, "XML解析异常"),
		/**
		 * -63003 返回异常
		 */
		ERROR_RETURNERROR(-63003, "返回异常"),
		/**
		 * 调用信息推送接口超时或异常
		 */
		ERROR_CALLSENDMSG(-63004, "调用信息推送接口异常"),
		/**
		 * -63005 数据库脚本执行查询异常
		 */
		ERROR_SQLINSERTERROR(-63005, "数据库脚本执行异常"),
		/**
		 * -63006 数据库脚本执行查询异常
		 */
		ERROR_SQLNORESULTERROR(-63006, "没有数据"),
		/**
		 * 获取异常提示设置异常
		 */
		ERROR_GETYCTXSZ(-63006, "获取异常提示设置异常"),
		/**
		 * 订单未找到
		 */
		ERROR_ORDERNOTFIND(-63007, "订单未找到"),
		/**
		 * 订单类型不是门诊订单或者住院订单
		 */
		ERROR_SERVICEID_NOT_006_007(-400013, "订单类型不是门诊订单或者住院订单"),
		/**
		 * 订单取消异常
		 */
		ERROR_CANCELORDER(-63008, "订单取消异常"),
		/**
		 * 消息推送异常
		 */
		ERROR_SENDMSG(-63009, "消息推送异常"),
		/**
		 * 短信发送异常
		 */
		ERROR_SENDSMS(-63009, "消息推送异常"),
		/**
		 * 更新状态失败
		 */
		ERROR_UPDATESTATE(-63009, "更新状态失败"),
		/**
		 * 退款金额不能大于支付金额
		 */
		ERROR_REFUNDPRICEERROR(-63010, "退款金额不能大于支付金额"),
		/**
		 * 就诊卡余额不足
		 */
		ERROR_BALANCENotENOUGH(-63011, "就诊卡余额不足"),
		/**
		 * 支付失败
		 */
		ERROR_PAY(-63012, "支付失败"),
		/**
		 * 支付失败
		 */
		ERROR_NUMBERFORMATE(-63013, "数字转换异常"), 
		ERROR_NULLPARAM(-63014, "参数为空"),
		/**
		 * -63007 订单ID不能为空
		 */
		ERROR_NOORDERID(-63015, "订单ID不能为空！"),
		/**
		 * -63008 退费价格不能为空
		 */
		ERROR_REFUNDPRICE(-63016, "退费价格不能为空！"),
		/**
		 * -63009 退费价格不能为空
		 */
		ERROR_BILLCHECKSTATE(-63017, "非长款，无法退费！");
		/** 代码 */
		private Integer code;
		/** 消息 */
		private String message;

		Order(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		@Override
		public Integer getCode() {
			// TODO Auto-generated method stub
			return code;
		}

		@Override
		public String getMessage() {
			// TODO Auto-generated method stub
			return message;
		}

		/**
		 * @return
		 */
		@Override
		public String getExceptMsg() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @param arg0
		 * @return
		 */
		@Override
		public String getMessage(String... arg0) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	public enum BizDealState {
		/**
		 * HIS返回明确失败信息，需要退款的
		 */
		BIZ_DEAL_STATE_0(0),
		/**
		 * HIS返回明确成功信息，业务完成的
		 */
		BIZ_DEAL_STATE_1(1),
		/**
		 * HIS返回未知信息，或者重试信息的，需要重试的
		 */
		BIZ_DEAL_STATE_2(2),
		/**
		 * 
		 */
		HIS_DEAL_STATE_3(3),;
		private int value;

		private BizDealState(int value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}
	}

	/**
	 * 调用接口返回代码定义
	 * 
	 * @author CYH
	 * @version 1.0 2017-6-30 上午10:42:01
	 */
	enum Survery implements RetCode {
		/**
		 * 问卷不存在"
		 */
		ERROR_INFO_NOT_FIND(-10001, "问卷不存在"), ERROR_IO(-10002, "导出文件失败"), ERROR_WRITE(-10003,
				"文件写入失败"), ERROR_CHOOSE_SUBJECT(-10004, "请选择网络问卷"), ERROR_FULL_SUBJECT(-10005,
						"问卷收集已满"), ERROR_OVER_COLLECT(-10006, "问卷收集已结束"), ERROR_PHONE_REPLY_ONCE(-10007,
								"一个电脑或手机只能回复一次"), ERROR_IP_REPLY_ONCE(-10008, "一个IP只能回复一次");
		/** 代码 */
		private Integer code;
		/** 消息 */
		private String message;

		@Override
		public String getMessage() {
			return message;
		}

		Survery(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		@Override
		public Integer getCode() {
			return code;
		}

		/**
		 * @return
		 */
		@Override
		public String getExceptMsg() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @param arg0
		 * @return
		 */
		@Override
		public String getMessage(String... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	/**
	 * 调用接口返回代码定义 定义范围： -14001 开始
	 * 
	 * @author daiys
	 * @date 2015-1-4
	 */
	enum Msg implements RetCode {
		/**
		 * 数据库操作异常
		 */
		ERROR_EXECUTESQL(-400001, "数据库操作异常"),
		/**
		 * 日期格式化异常
		 */
		ERROR_DATEFORMAT(-400002, "日期格式化异常"),
		/**
		 * json操作异常
		 */
		ERROR_JSONOPERTOR(-400003, "json操作异常"),
		/**
		 * 获取数据库连接异常
		 */
		ERROR_GETDBCONNECT(-400004, "获取数据库连接异常"),
		/**
		 * 取值异常
		 */
		ERROR_GETVALUE(-400005, "取值异常"),
		/**
		 * 调用接口异常
		 */
		ERROR_CALLINTERFACE(-400006, "调用接口异常"),
		/**
		 * 交易代码不正确
		 */
		ERROR_TRANSACTIONCODE(-400007, "交易代码不匹配"),
		/**
		 * 调用查询字典异常
		 */
		ERROR_CALLQUERYDIC(-400008, "调用查询字典异常"),
		/**
		 * 查询模板异常
		 */
		ERROR_QUERYMSGTEMP(-400009, "查询模板异常"),
		/**
		 * 发送短信异常
		 */
		ERROR_SENDMSG(-400010, "发送短信异常"),
		/**
		 * 内容配置有误
		 */
		ERROR_CONFIGURE(-400011, "发送内容配置有误"),
		/**
		 * 验证码短信防护 操作过于频繁,请稍后再试!
		 */
		ERROR_PROTECTPORVINGCODE(-400012, "操作过于频繁,请稍后再试!"),
		/**
		 * 手机号不能为空!
		 */
		ERROR_MOBILEISNOTNULL(-400013, "手机号不能为空!"),
		/**
		 * 手机号有误!
		 */
		ERROR_MOBILEERROR(-400014, "手机号有误!"),;
		/** 代码 */
		private Integer code;
		/** 消息 */
		private String message;

		Msg(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		@Override
		public Integer getCode() {
			// TODO Auto-generated method stub
			return code;
		}

		@Override
		public String getMessage() {
			// TODO Auto-generated method stub
			return message;
		}

		/**
		 * @return
		 */
		@Override
		public String getExceptMsg() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @param arg0
		 * @return
		 */
		@Override
		public String getMessage(String... arg0) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	/**
	 * 调用接口返回代码定义 定义范围： -14001 开始
	 * 
	 * @author daiys
	 * @date 2015-1-4
	 */
	enum Pay implements RetCode {
		/**
		 * 错误交易状态
		 */
		ERROR_TRADESTATE(-400001, "错误的交易状态！"),

		/**
		 * 不存在商户订单！
		 */
		ERROR_MERCHANTORDER(-400002, "不存在商户订单！"),
		/**
		 * 渠道ID不正确，通过ConfigKey无法定位
		 */
		ERROR_CHANNELID(-400004, "渠道ID不正确，通过ConfigKey无法定位"),
		/**
		 */
		ERROR_REFUND(-400005, "退费失败！"),
		/**
		 * 统一下单异常
		 */
		ERROR_UNITEORDER(-400006, "统一下单异常！"),

		/**
		 * 商户号配置信息不存在，请联系管理员
		 */
		ERROR_NOTEXISTCONFIGKEY(-400007, "商户号配置信息不存在，请联系管理员！"),

		/**
		 * 该渠道不支持退费操作，请联系管理员！
		 */
		ERROR_NOTEXISTCLIENTIDCONFIGKEY(-400008, "该渠道不支持退费操作，请联系管理员！"),

		/**
		 * 该渠道不支持退费操作，请联系管理员！
		 */
		ERROR_NOTEXISTCLIENTIDCONFIGKEY_WECHAT(-400009, "该渠道未开通微信支付操作，请联系管理员！"),
		/**
		 * 该渠道不支持退费操作，请联系管理员！
		 */
		ERROR_NOTEXISTCLIENTIDCONFIGKEY_Ali(-400009, "该渠道未开通支付宝支付操作，请联系管理员！"),
		/**
		 * 当面付二维码不可用或者仅限使用一次
		 */
		ERROR_SWEEPCODEPAY_AUTHCODEINVALID(-400010, "二维码已失效！"),		
		/**
		 * 商户订单正在支付中
		 */
		ERROR_SWEEPCODEPAY_USERPAYING(-400011, "商户订单正在等待支付中！"),
		/**
		 * 付款码已过期，请退出重试！
		 */
		ERROR_SWEEPCODEPAY_PAYERROR(-400012, "付款码已过期，请退出重试！"),
		/**
		 * "处方付校验失败！"
		 */
		ERROR_PRESCQRVALIDATEBEFORE(-400013, "处方付校验失败！"),
		/**
		 * 支付限制
		 */
		ERROR_PAYRULE(-400014, "限制支付，订单支付失败！"),
		
		REFUND_EXCEPTION(-400016, "退费异常！"),
		
		SIGNATURE_FAILED(-400017, "验证签名不通过！"),
		ERROR_QUERYORDER(-400018, "查询商户订单状态异常！"),
		
		ERROR_REVOKED(-400019, "撤销订单失败！"),
		ERROR_CLOSE(-400020, "关闭订单失败！"),
		REFUNDERROR_USERACCOUNT(-400021, "用户账号异常，退款失败！"),
		;
		/** 代码 */
		private Integer code;
		/** 消息 */
		private String message;

		Pay(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		@Override
		public Integer getCode() {
			// TODO Auto-generated method stub
			return code;
		}

		@Override
		public String getMessage() {
			// TODO Auto-generated method stub
			return message;
		}

		/**
		 * @return
		 */
		@Override
		public String getExceptMsg() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @param arg0
		 * @return
		 */
		@Override
		public String getMessage(String... arg0) {
			// TODO Auto-generated method stub
			return null;
		}

	}
	
	/**
	 * 对账账单异常信息
	 * 
	 * @author zhaoy
	 *
	 */
	enum Bill implements RetCode {
		/**
		 * -73001 数据库没有查询或者更新到有效数据
		 */
		ERROR_SQLNORESULTERROR(-73001, "无数据显示!"),
		/**
		 * -73002 订单或账单ID不能为空
		 */
		ERROR_NOORDERID(-73002, "订单或账单ID不能为空！"),
		/**
		 * -73003 退费价格不能为空
		 */
		ERROR_REFUNDPRICE(-73003, "退费价格不能为空！"),
		/**
		 * -73004 只有长款的账单才能退款/冲正
		 */
		ERROR_BILLCHECKSTATE_1(-73004, "非长款，无法退费或者冲正！"),
		/**
		 * -73005 银行勾兑到款金额不能为空,且只能是数字(钱币单位精确到分)
		 */
		ERROR_PAIDEMONEYILLEGAL(-73005, "请正确填写到款金额！"),
		/**
		 * -73006 银行流水号不能为空
		 */
		ERROR_BANKFLOWNOILLEGAL(-73006, "银行流水号不能为空！"),
		/**
		 * -73007 日期条件不能为空
		 */
		ERROR_DATEISNULL(-73007, "日期条件不能为空！"),
		/**
		 * -73008 银行账号不能为空
		 */
		ERROR_BANKNOISNULL(-73008, "银行账号不能为空！"),
		/**
		 * -73009  对账账单信息不存在
		 */
		ERROR_BILLNOTEXIST(-73009, "查询核对账单异常!"),
		/**
		 * -73010  交易渠道不存在
		 */
		ERROR_CHANNELISNULL(-73010, "交易渠道条件不能为空!"),
		/**
		 * -73011  对账账单信息不存在
		 */
		ERROR_PAYMETHODISNULL(-73011, "查询核对账单异常!"),
		
		/**
		 * -73012 银行勾兑应到款金额不能为空
		 */
		ERROR_PAYABLEMONEYILLEGAL(-73012, "银行勾兑应到款金额不能为空！"),
		
		/**
		 * -73013 只有长款的账单才能退款/冲正
		 */
		ERROR_BILLCHECKSTATE_T1(-73013, "非短款，不允许财务登账！"),
		/**
		 * -73014  风控校验的支付密码为空或者错误
		 */
		ERROR_PAYKEYISERROR(-73014, "支付密码为空或者错误!"),
		/**
		 * -73015  商户流水号非空
		 */
		ERROR_NOMERCHNO(-73015, "商户流水号不能为空!"),
		;

		/** 代码 */
		private Integer code;
		/** 消息 */
		private String message;

		@Override
		public String getMessage() {
			return message;
		}

		Bill(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		@Override
		public Integer getCode() {
			return code;
		}

		@Override
		public String getExceptMsg() {
			return null;
		}

		@Override
		public String getMessage(String... params) {
			return null;
		}
	}
}

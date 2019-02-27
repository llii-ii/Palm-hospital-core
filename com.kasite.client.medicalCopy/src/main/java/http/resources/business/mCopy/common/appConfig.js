/**
 * 前端应用开关配置
 * 用一个函数域包起来，就是所谓的沙箱
 * 在这里边 var 定义的变量，属于这个函数域内的局部变量，避免污染全局
 * 注：此文件禁止修改变量名称，但可修改变量值
 * **/
(function (window,undefined) {
	
	//声明appConfig全局变量
	var _appCfg = window.appConfig;
	//声明appConfig全局变量
	var _webAppMenu = window.WebAppMenu;
//	/**
//	 * total_menus
//	 * 所有菜单配置
//	 * 注：建议不要随意修改
//	 */
//	var _tms = {
//			//预约科室
//			'yyks':'../yygh/yyghDeptList.html',
//			//预约二级科室
//			'yyejks':'../yygh/yyghTwoLevelDeptList.html',
//			//门诊缴费
//			'mzjf':'../order/orderSettlementList.html',
//			//门诊缴费
//			'mzcz':'../outpatientDept/outpatientCardAccount.html',
//			//排队候诊
//			'pdhz':'../queue/queueList.html',
//			//报告查询
//			'bgcx':'../report/reportIndex.html',
//			//院内导航
//			'yndh':'#',
//			//智能导诊
//			'zndz':'#',
//			//来院导航
//			'lydh':'../hospitalInfo/hospitalMap.html',
//			//就诊指南
//			'jzzn':'../hospitalNews/hospitalGuide.html',
//			//医院概况
//			'yygk':'../hospitalInfo/hospitalIntro.html',
//			//门诊满意度调查
//			'mzmyddc':'../survey/survey_home.html',
//			//诊间结算
//			'zjjs':'../order/orderSettlementList.html',
//			//门诊充值记录
//			'mzczjl':'../outpatientDept/outpatientPayRecordList.html',
//			//住院费用
//			'zyfy':'../inHospital/hospitalCost.html',
//			//缴费记录（住院）
//			'jfjl':'../inHospital/hospitalNoRechargeRecord.html',
//			//缴费记录（住院HIS）
////			'jfjl':'../inHospital/hospitalNoRechargeRecordHis.html',
//			//出入院流程
//			'crylc':'../hospitalNews/hospitalNotice.html',
//			//住院满意度调查
//			'zymyddc':'../survey/survey_home.html?objtype=2',
//			//住院清单
//			'zyqd':'../inHospital/hospitalCostDaily.html',
//			//住院缴费
//			'zyjf':'../inHospital/inpatientAccount.html',
//			//医院动态
//			'yydd':'../hospitalNews/news.html'
//	};
	
	appConfig = {
//			/**
//			 * 诊间支付模式：
//			 * "order",订单模式，即针对医嘱订单（或处方订单），直接微信支付，或支付宝支付
//			 * "settlement",结算模式，即针对医嘱订单（或处方订单），使用就诊卡（或医保卡）直接结算费用（无需微信或支付宝支付）。
//			 * 该配置会影响前端的菜单展示，不懂，可以咨询林建法
//			 */
//			outpatientPaymentModel:"settlement",
			
			/**
			 * 挂号模式：
			 * "onLinePay",线上挂号费支付模式，即挂号需要在公众号（或服务窗）支付挂号费。
			 * "offLinePay",线下支付挂号费模式，即线上只挂号，线下支付挂号费。
			 * "onAndOff",预约不支付挂号费，当天挂号支付挂号费。
			 */
			registerModel:"onLinePay",
			
			/**
			 * 就诊人模块配置
			 * isCheckHis:新增就诊人是否校验HIS
			 * isVirtualCard:HIS是否支持虚拟就诊卡
			 */
			memberConfig:{
				//0:新增就诊人（无绑卡时）不做HIS校验(his不支持就诊人校验)
				//1:新增就诊人（无绑卡时）需要HIS校验(his支持就诊人验证)，如果HIS系统存在该就诊人的就诊卡，则自动绑上就诊卡。
				//  如果HIS支持新增虚拟卡，则新增虚拟卡
				isCheckHis:0,
				//0:HIS不支持新增虚拟卡
				//1:HIS支持新增虚拟卡
				isVirtualCard:0
			},
//			/**
//			 * 充值相关配置
//			 */
//			rechargeConfig:{
//				//门诊缴费记录-查询本地order
//				opRechargeRecordUrl:_tms['mzczjl'],
//				//门诊缴费记录-查询HIS（未开发）
//				//opRechargeRecordUrl:_tms['mzczjl'],
//				//住院缴费记录-查询本地order
//				hRechargeRecordUrl:_tms['jfjl'],
//				//住院缴费记录-查询HIS
//				//hRechargeRecordUrl:_tms['jfjlhis']
//			},
			/**
			 * tab菜单配置
			 */
			indexMenus:function(){
//				var clinicTopMenu = [];
//				//门诊菜单配置
//				var clinicMenu = [];
//				//住院菜单配置
//	            var hospitalizationMenu = [];
//				//住院菜单配置
//		        var hospitalizationTopMenu = [];  
		        if(Commonjs.isEmpty(_webAppMenu)){
		        	_webAppMenu = Commonjs.getWebAppMenu();
		        }
				return _webAppMenu;
			}
	};
	
	
	
	
})(window);                 

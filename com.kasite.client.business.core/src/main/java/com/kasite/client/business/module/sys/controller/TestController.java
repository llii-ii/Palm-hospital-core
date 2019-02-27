package com.kasite.client.business.module.sys.controller;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.kasite.client.business.job.BillCheckSybchroJob;
import com.kasite.client.business.job.BusinessAllJob;
import com.kasite.client.business.job.DownloadHisBillJob;
import com.kasite.client.business.job.DownloadOrderCheckJob;
import com.kasite.client.business.job.GenBankMoneyCheckJob;
import com.kasite.client.business.job.GenBillCheckJob;
import com.kasite.client.business.job.GenChannelBankCheckJob;
import com.kasite.client.business.job.GenPayWayDayStatisticsJob;
import com.kasite.client.order.circuitbreaker.KasiteHystrixCommandKey;
import com.kasite.client.order.circuitbreaker.KasiteHystrixConfig;
import com.kasite.client.pay.job.DownloadBillJob;
//import com.kasite.client.business.module.his.fujian.quanzhou.fjykdxfsdeyy.hisdao.HisYuYueDao;
import com.kasite.client.wechat.service.WeiXinService;
import com.kasite.client.zfb.service.AlipayService;
import com.kasite.core.common.annotation.AuthIgnore;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.GoogleAuthenticator;
import com.kasite.core.common.config.GoogleAuthenticatorConfig;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.WXConfigEnum;
import com.kasite.core.common.constant.ApiList;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.req.PageVo;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.sys.oauth.LocalOAuthUtil;
import com.kasite.core.common.sys.verification.VerificationBuser;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.SpringContextUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.WhiteListUtil;
import com.kasite.core.common.util.wxmsg.IDSeed;
import com.kasite.core.serviceinterface.common.cache.dbo.SysCache;
import com.kasite.core.serviceinterface.common.cache.service.CacheService;
import com.kasite.core.serviceinterface.common.controller.AbsAddUserController;
import com.kasite.core.serviceinterface.module.basic.cache.BatUserCache;
import com.kasite.core.serviceinterface.module.basic.cache.IBatUserLocalCache;
import com.kasite.core.serviceinterface.module.order.IOrderService;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderIsCancel;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderListLocal;
import com.kasite.core.serviceinterface.module.order.resp.RespOrderLocalList;

/**
 * 
 * @className: TestController
 * @author: lcz
 * @date: 2018年8月7日 下午3:45:16
 */
@RestController
@RequestMapping("/api")
public class TestController extends AbsAddUserController {
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_DEFAULT);
	@Autowired
	IBatUserLocalCache batUserCache;

	@Autowired
	CacheService cacheService;
	
	@Autowired
	BusinessAllJob businessAllJob;
	
	@Autowired
	GoogleAuthenticatorConfig config;
	
	@Autowired
	BillCheckSybchroJob billCheckSybchroJob;
	
	@Autowired
	GenBillCheckJob genBillCheckJob;
	
	@Autowired
	DownloadHisBillJob downloadHisBillJob;
	
	@Autowired
	DownloadBillJob downloadBillJob;
	
	@Autowired
	DownloadOrderCheckJob downloadOrderCheckJob;
	
	@Autowired
	GenBankMoneyCheckJob genBankMoneyCheckJob;
	
	@Autowired
	GenChannelBankCheckJob genChannelBankCheckJob;
	
	@Autowired
	IOrderService orderService;
	
	@Autowired
	SqlSessionFactory masterSqlSessionFactory;
	
	@Autowired
	private WhiteListUtil whiteListUtil;
	
	@Autowired
	private GenPayWayDayStatisticsJob genPayWayDayStatisticsJob;
	
	@AuthIgnore
	@GetMapping("/{clientId}/{configKey}/{code}/initMenu.do")
	public R initMenu( @PathVariable("clientId") String clientId, 
			@PathVariable("configKey") String configKey,
			@PathVariable("code") String code) throws Exception {
		String secret = config.getAuth().getAppSecret(); 
		Boolean isTrue =  GoogleAuthenticator.authcode(code, secret);
		if(isTrue != null && isTrue) {
			String ret = null;
			com.kasite.core.common.config.ChannelTypeEnum t = KasiteConfig.getChannelType(clientId, configKey);
			switch (t) {
			case wechat:
				ret = WeiXinService.initMenu(configKey, clientId).toString();
				break;
			case zfb:
				ret = AlipayService.initMenu(configKey, clientId);
			default:
				break;
			}
			return R.ok(ret);
		}else {
			return R.error("验证码不正确，请重新输入验证码。");
		}
	}
	

	@AuthIgnore
	@GetMapping("/{orderId}/{price}/{clientId}/{code}/cancel.do")
	public R cancel( @PathVariable("orderId") String orderId, @PathVariable("price") String price, @PathVariable("clientId") String clientId, 
			@PathVariable("code") String code) throws Exception {
		String secret = config.getAuth().getAppSecret();
		Boolean isTrue = GoogleAuthenticator.authcode(code, secret);
		if(isTrue) {
			ReqOrderIsCancel o = new ReqOrderIsCancel(KasiteConfig.createJobInterfaceMsg(getClass(), null, null, KasiteConfig.createAuthInfoVo("xx")), 
					orderId, null, Integer.parseInt(price), KstHosConstant.SYSTEMMANAGER_CHANNEL_ID, KstHosConstant.SYSTEMMANAGER_CHANNEL_ID, clientId, null);
			String r = orderService.orderIsCancel(new CommonReq<ReqOrderIsCancel>(o)).toResult();
			return R.ok(r);
		}
		return R.error("验证码不正确");
	}
	@AuthIgnore
	@GetMapping("/{jobName}/{code}/execute.do")
	public R doJob( @PathVariable("jobName") String methodName, 
			@PathVariable("code") String code) throws Exception {
		String secret = config.getAuth().getAppSecret();
		Boolean isTrue = GoogleAuthenticator.authcode(code, secret);
		if(isTrue != null && isTrue) {
			Method refMethod = ReflectionUtils.findMethod(businessAllJob.getClass(),
					methodName);
			ReflectionUtils.invokeMethod(refMethod, businessAllJob);
			return R.ok();
		}else {
			return R.error("验证码不正确，请重新输入验证码。");
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static List convertList(ResultSet rs) throws SQLException {
        List list = new ArrayList();
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();
        while (rs.next()) {
            Map rowData = new HashMap();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i).toLowerCase(), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
	}

	//eq相等   ne、neq不相等，   gt大于， lt小于 gte、ge大于等于   lte、le 小于等于   not非   mod求模
	enum Q{
		eq("="),
		ne("!="),
		neq("!="),
		gt(">"),
		lt("<"),
		gte(">="),
		ge(">="),
		lte("<="),
		le("<="),
		;
		private String t;
		Q(String t){
			this.t = t;
		}
		public String getT() {
			return t;
		}
	}
	public static void main(String[] args) throws Exception {
		System.out.println("count=====>" + (16789138 - 1356931) );
//		KasiteRSAUtil.getPrivateAndPublicKey();
		
	}

	@SuppressWarnings("rawtypes")
	@AuthIgnore
	@GetMapping("/{tableName}/query.do")
	public R select(@PathVariable("tableName") String tableName,
			int pageNo,int pageSize,
			HttpServletRequest request) {
		SqlSession session = null;
		List list = null;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			StringBuffer sql = new StringBuffer("select ");
			Map<String, String[]> paramMap = request.getParameterMap();
			String _coum = request.getParameter("_COLUMN");
			if(StringUtil.isNotBlank(_coum)) {
				sql.append(_coum);
				paramMap.remove("_COLUMN");
			}else {
				sql.append('*');
			}
			sql.append(" from ").append(tableName) .append(" where 1=1 ");
			if(pageSize > 200) {
				pageSize = 200;
			}
			for (Map.Entry<String, String[]> entity : paramMap.entrySet()) {
				String key = entity.getKey();
				String[] values = entity.getValue();
				if( null != values && !"".equals(values[0]) && values.length > 0 && key.startsWith("_")) {
					String t = "=";
					String k = "";
					//eq相等   ne、neq不相等，   gt大于， lt小于 gte、ge大于等于   lte、le 小于等于   not非   mod求模
					Q[] qs = Q.values();
					for (Q q : qs) {
						String s = "_"+q.name()+"_";
						if(key.startsWith(s)) {
							k = key.substring(s.length(), key.length());
							t = q.t;
						}
					}
					sql.append(" and ").append(k).append(t).append("'");
					for (String v : values) {
						sql.append(v);
					}
					sql.append("'");
				}
			}	
			if(pageNo <=0) pageNo = 1;
			if(pageSize <=0) pageSize = 10;
			int i = (pageNo-1)*pageSize;
			sql.append(" limit ").append(i).append(",").append(pageSize);
			session = masterSqlSessionFactory.openSession(); 
			conn = session.getConnection();
			ps = conn.prepareStatement(sql.toString());
			ResultSet rs = ps.executeQuery();
			list = convertList(rs);
		}catch (Exception e) {
			e.printStackTrace();
			return R.error(e);
		}finally {
			if(null != ps)
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if(null != conn)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if(null != session) session.close();
		}
		return R.ok(list);
	}
	
	@AuthIgnore
	@GetMapping("/{code}/queryOrder.do")
	public R queryOrder(@PathVariable("code") String code,
			String orderId,
			String serviceId,
			String beginDate,
			String endDate,Integer pageIndex,Integer pageSize) throws Exception {
		String secret = config.getAuth().getAppSecret();
		Boolean isTrue =  GoogleAuthenticator.authcode(code, secret);
		if(isTrue != null && isTrue) {
			ReqOrderListLocal t = new ReqOrderListLocal(KasiteConfig.createJobInterfaceMsg(getClass(), "", "", KasiteConfig.createAuthInfoVo(IDSeed.next())));
			t.setOrderId(orderId);
			t.setServiceId(serviceId);
			t.setBeginDate(beginDate);
			t.setEndDate(endDate);
			PageVo page = new PageVo();
			if(pageSize == null) pageSize = 50;
			if(pageIndex == null) pageIndex = 0;
			page.setPIndex(pageIndex);
			page.setPSize(pageSize);
			t.setPage(page);
			CommonReq<ReqOrderListLocal> req = new CommonReq<ReqOrderListLocal>(t);
			CommonResp<RespOrderLocalList> resp = orderService.orderListLocal(req);
			return R.ok(resp.getData());
		}else {
			return R.error("验证码不正确，请重新输入验证码。");
		}
	}
	
	
	@AuthIgnore
	@GetMapping("/{code}/clearCache.do")
	public R clearCache(@PathVariable("code") String code) {
		String secret = config.getAuth().getAppSecret();
		Boolean isTrue = GoogleAuthenticator.authcode(code, secret);
		if(isTrue != null && isTrue) {
			IBatUserLocalCache userLocalCache = SpringContextUtil.getBean(IBatUserLocalCache.class);
			userLocalCache.clear();
			return R.ok();
		}else {
			return R.error("验证码不正确，请重新输入验证码。");
		}
	}
	
	
	@AuthIgnore
	@GetMapping("/{code}/getCache.do")
	public R getCache(@PathVariable("code") String code) {
		SysCache v = cacheService.getSysCache(code);
		if(null != v) {
			return R.ok(v.getValue());
		}
		return R.ok();
	}
	
	@AuthIgnore
	@GetMapping("/ping.do")
	public void ping(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	   response.getOutputStream().print("success");
	}
	
	@AuthIgnore
	@GetMapping("/{openId}/{clientId}/{configKey}/test.do")
	public void auth(@PathVariable("openId") String openId, @PathVariable("clientId") String clientId,
			@PathVariable("configKey") String configKey, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AuthInfoVo vo = login(true, openId, clientId, configKey,null, request, response);
		String toUrl = request.getParameter("toUrl");
		BatUserCache cache = new BatUserCache();
		cache.setOpenId(openId);
		cache.setNickName("测试用户");
		cache.setSex(3);
		cache.setCity("测试");
		cache.setSubscribe(1);
		cache.setSubscribeTime(0);
		cache.setUnionId("");
		cache.setConfigKey(configKey);
		batUserCache.put(openId, cache);
		addUser(openId, KasiteConfig.getWxConfig(WXConfigEnum.wx_app_id,configKey), vo, cache);
		sendRedirect(response, toUrl);
		LogUtil.info(log, "测试跳转登录:openId = " + openId + " clientId=" + clientId + " configKey=" + configKey);
	}

	@AuthIgnore
	@GetMapping("/sysConfig.do")
	R sysConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 返回当前系统配置信息
		JSONObject json = new JSONObject();
		parseJSON(json,false);
		SysUserController.QueryTimes = 50;
		return R.ok(json);
	}
	
	@AuthIgnore
	@GetMapping("/{code}/sysConfig.do")
	R sysConfigAll(@PathVariable("code") String code,HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 返回当前系统配置信息
		String secret = config.getAuth().getAppSecret();
		Boolean isTrue = GoogleAuthenticator.authcode(code, secret);
		if(isTrue != null && isTrue) {
			JSONObject json = new JSONObject();
			parseJSON(json,true);
			return R.ok(json);
		}else {
			return R.error("验证码不正确，请重新输入验证码。");
		}
	}
	
	private void parseJSON(JSONObject json,boolean isAll) throws Exception {
//		json.put("System", System.getProperties());
		json.put("channelTypes", KasiteConfig.getPayChannelTypes());
		json.put("channelInfos", KasiteConfig.getAllClientConfig());
		json.put("kasiteConfig", KasiteConfig.print(isAll));
		json.put("system.DiskInfo", KasiteConfig.getDiskInfo());
		json.put("googleAuth", config.getAuth().print(isAll));
		json.put("apiList", ApiList.me().apiJson()); 
		json.put("callReport", ApiList.me().getCallReport());
		json.put("nowTime", DateOper.getNow("yyyy-MM-dd HH:mm:sss"));
	}
	
	@AuthIgnore
	@GetMapping("/syncConfig.do")
	R syncConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 返回当前系统配置信息
		// 从云端同步配置信息
		boolean isConn = KasiteConfig.isConnectionKastieCenter();
		if(isConn) {
			VerificationBuser.create().init();
			LocalOAuthUtil.getInstall().init();
		}
		// 返回当前系统配置信息
		JSONObject json = new JSONObject();
		parseJSON(json,false);
		return R.ok(json);
	}

//	@Autowired
//	private HisYuYueDao hisYuYueDao;
	
	@AuthIgnore
	@GetMapping("/cancel.do")
	R cancel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String, String> map = new HashMap<String, String>(16);
		map.put(ApiKey.HisYyCancel.hisOrderId.getName(), "770818");
		map.put(ApiKey.HisYyCancel.orderId.getName(), "YYGH_2B57C854869BE8D6CEA9D1ECCC2");
		map.put(ApiKey.HisYyCancel.operatorId.getName(), "");
		map.put(ApiKey.HisYyCancel.operatorName.getName(), "");
		map.put(ApiKey.HisYyCancel.reason.getName(), "");
		map.put(ApiKey.HisYyCancel.channelId.getName(), "100123");
		
		map.put(ApiKey.HisYyCancel.patientName.getName(), "测试人员五号");
		map.put(ApiKey.HisYyCancel.hisPatientId.getName(), "1903241");
		map.put(ApiKey.HisYyCancel.mobile.getName(), "15280078190");
		map.put(ApiKey.HisYyCancel.idCardNo.getName(), "350124199001172857");
		map.put(ApiKey.HisYyCancel.clinicCard.getName(), "C38783677");
		
		map.put(ApiKey.HisYyCancel.hosId.getName(), "F0A7F830074B434BA698B1E3222F2CA9");
		map.put(ApiKey.HisYyCancel.hospitalCode.getName(), "225085");
		map.put(ApiKey.HisYyCancel.deptCode.getName(), "325");
		map.put(ApiKey.HisYyCancel.deptName.getName(), "门血液内科");
		map.put(ApiKey.HisYyCancel.doctorCode.getName(), "949");
		map.put(ApiKey.HisYyCancel.doctorName.getName(), "郭熙哲");
		map.put(ApiKey.HisYyCancel.scheduleId.getName(), "325_949_20181024_上午");
		map.put(ApiKey.HisYyCancel.regDate.getName(), "2018-10-24");
		map.put(ApiKey.HisYyCancel.timeSlice.getName(), "1");
		map.put(ApiKey.HisYyCancel.timeName.getName(),"上午");
		map.put(ApiKey.HisYyCancel.sourceCode.getName(), "12911999");
		map.put(ApiKey.HisYyCancel.sqNo.getName(), "");
		map.put(ApiKey.HisYyCancel.startTime.getName(), "08:20");
		map.put("channel", "健康之路");
		map.put("APPOINT_NO", "767957");//撤销
		
//		hisYuYueDao.cancel(map);
		if(StringUtil.isBlank(map.get("RESULT_CODE"))
				|| !"0000".equals(map.get("RESULT_CODE"))) {
			return R.error("失败："+map.get("RESULT_CODE")+"|||"+map.get("ERROR_MSG"));
		}
		return R.ok();
	}
	
	/**
	 * 账单下载
	 * 
	 * @param jobname
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@AuthIgnore
	@GetMapping("/{jobname}/doDownloadData.do")
	public R doDownloadData(@PathVariable("jobname") String jobname,HttpServletRequest request) throws Exception{
		String startDateStr = request.getParameter("startDate");
		String endDateStr = request.getParameter("endDate");
		Date startDate = DateOper.parse(startDateStr);
		Date endDate = DateOper.parse(endDateStr);
		int days = DateOper.getDaysBetween(startDate, endDate, TimeZone.getDefault());
		
		if("orderCheckJob".equals(jobname)){
			executeOrderCheckJob(startDateStr, endDateStr, days);
		}else if("hisBillJob".equals(jobname)){
			executeHisBillJob(startDateStr, endDateStr, days);
		}else if("billJob".equals(jobname)){
			executeBillJob(startDateStr, endDateStr, days);
		}else if("billCheckJob".equals(jobname)){
			executeBillCheckJob(startDateStr, endDateStr, days);
		}else if("billSyschroJob".equals(jobname)){
			executeSyschroJob(startDateStr, endDateStr, days);
		}else if("bankCheckJob".equals(jobname)){
			executeBankMoneyCheckJob(startDateStr, endDateStr, days);
		}else if("channelBankCheckJob".equals(jobname)) {
			executeChannelBankCheckJob(startDateStr, endDateStr, days);
		}else if("payWayDayStatisticsJob".equals(jobname)) {
			executePayWayDayStatisticsJob(startDateStr, endDateStr, days);
		}
		
		return R.ok();
	}
	
	/**
	 * 下载旧系统全流程订单
	 * 
	 * @param startDateStr
	 * @param endDateStr
	 * @param days
	 */
	private void executeOrderCheckJob(String startDateStr, String endDateStr, int days) {
		try {
			for(int i=0;i<=days;i++) {
				String theStartDate = DateOper.addDate(startDateStr, i, "yyyy-MM-dd");
				String theEndDateDel = DateOper.addDate(startDateStr, i+1, "yyyy-MM-dd");
				downloadOrderCheckJob.deal(theStartDate, theEndDateDel, theStartDate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 下载商户订单(微信、支付宝、银联)
	 * 
	 * @param startDateStr
	 * @param endDateStr
	 * @param days
	 */
	private void executeBillJob(String startDateStr, String endDateStr, int days) {
		try {
			for(int i=0;i<=days;i++) {
				String theDate = DateOper.addDate(startDateStr, i);
				Date billDate = DateOper.parse(theDate);
				downloadBillJob.downloadBill(billDate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 下载his账单
	 * 
	 * @param startDateStr
	 * @param endDateStr
	 * @param days
	 */
	private void executeHisBillJob(String startDateStr, String endDateStr, int days) {
		try {
			for(int i=0;i<=days;i++) {
				String theDate = DateOper.addDate(startDateStr.replace("-", ""), i, "yyyyMMdd");
				String theStartDateDel = DateOper.addDate(startDateStr, i, "yyyy-MM-dd");
				String theEndDateDel = DateOper.addDate(startDateStr, i+1, "yyyy-MM-dd");
				
				downloadHisBillJob.deal(theDate, theDate, theStartDateDel, theEndDateDel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 重新对账
	 * 
	 * @param startDateStr
	 * @param endDateStr
	 * @param days
	 */
	private void executeBillCheckJob(String startDateStr, String endDateStr, int days) {
		try {
			for(int i=0;i<=days;i++) {
				String theStartDate = DateOper.addDate(startDateStr, i, "yyyy-MM-dd");
				String theEndDate = DateOper.addDate(startDateStr, i+1, "yyyy-MM-dd");
				
				Date thisDate = DateOper.parse(theStartDate);
				int count = DateOper.getDaysBetween(thisDate, DateOper.getNowDate(), TimeZone.getDefault());
				Date nowDate = DateOper.getAddDays(-(count-1));
				Timestamp checkDate = new Timestamp(nowDate.getTime());
				
				genBillCheckJob.deal(theStartDate, theEndDate, theStartDate, checkDate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 账单同步
	 * 
	 * @param startDateStr
	 * @param endDateStr
	 * @param days
	 */
	private void executeSyschroJob(String startDateStr, String endDateStr, int days) {
		try {
			for(int i=0;i<=days;i++) {
				String theStartDate = DateOper.addDate(startDateStr, i, "yyyy-MM-dd");
				String theEndDate = DateOper.addDate(startDateStr, i+1, "yyyy-MM-dd");
				
				Date thisDate = DateOper.parse(theStartDate);
				int count = DateOper.getDaysBetween(thisDate, DateOper.getNowDate(), TimeZone.getDefault());
				Date nowDate = DateOper.getAddDays(-(count-1));
				Timestamp checkDate = new Timestamp(nowDate.getTime());
				
				billCheckSybchroJob.deal(theStartDate, theEndDate, theStartDate, checkDate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 银行账单勾兑
	 * 
	 * @param startDateStr
	 * @param endDateStr
	 * @param days
	 */
	private void executeBankMoneyCheckJob(String startDateStr, String endDateStr, int days) {
		try {
			for(int i=0;i<=days;i++) {
				String theStartDate = DateOper.addDate(startDateStr, i, "yyyy-MM-dd");
				String theEndDate = DateOper.addDate(startDateStr, i+1, "yyyy-MM-dd");
				
				Date thisDate = DateOper.parse(theStartDate);
				int count = DateOper.getDaysBetween(thisDate, DateOper.getNowDate(), TimeZone.getDefault());
				Date nowDate = DateOper.getAddDays(-(count-1));
				Timestamp checkDate = new Timestamp(nowDate.getTime());
				
				genBankMoneyCheckJob.deal(theStartDate, theEndDate, theStartDate, checkDate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 渠道关联银行账单统计
	 * 
	 * @param startDateStr
	 * @param endDateStr
	 * @param days
	 */
	private void executeChannelBankCheckJob(String startDateStr, String endDateStr, int days) {
		try {
			for(int i=0;i<=days;i++) {
				String theStartDate = DateOper.addDate(startDateStr, i, "yyyy-MM-dd");
				String theEndDate = DateOper.addDate(startDateStr, i+1, "yyyy-MM-dd");
				
				Date thisDate = DateOper.parse(theStartDate);
				int count = DateOper.getDaysBetween(thisDate, DateOper.getNowDate(), TimeZone.getDefault());
				Date nowDate = DateOper.getAddDays(-(count-1));
				Timestamp checkDate = new Timestamp(nowDate.getTime());
				
				genChannelBankCheckJob.deal(theStartDate, theEndDate, theStartDate, checkDate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 日账单统计
	 * 
	 * @param startDateStr
	 * @param endDateStr
	 * @param days
	 */
	private void executePayWayDayStatisticsJob(String startDateStr, String endDateStr, int days) {
		try {
			for(int i=0;i<=days;i++) {
				String startDate = DateOper.addDate(startDateStr, i, "yyyy-MM-dd");
				String endDate = DateOper.addDate(startDateStr, i+1, "yyyy-MM-dd");
				genPayWayDayStatisticsJob.deal(startDate, endDate, startDate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AuthIgnore
	@GetMapping("/addWhiteList.do")
	public R addWhiteList(String openIds) {
		if(StringUtil.isNotBlank(openIds)) {
			String[] arrs = openIds.split(",");
			for (String opId : arrs) {
				whiteListUtil.add(opId);
			}
		}
		return R.ok();
	}
	@AuthIgnore
	@GetMapping("/removeWhiteList.do")
	public R removeWhiteList(String openIds) {
		if(StringUtil.isNotBlank(openIds)) {
			String[] arrs = openIds.split(",");
			for (String opId : arrs) {
				whiteListUtil.remove(opId);
			}
		}
		return R.ok();
	}
	@AuthIgnore
	@GetMapping("/openOrCloseWhiteList.do")
	public R openOrCloseWhiteList(int operType) {
		whiteListUtil.openOrCloseWhileList(operType);
		return R.ok();
	}
	
	@AuthIgnore
	@GetMapping("/hystrixConfig.do")
	public R hystrixConfig(String hystrixGroup,String isEnable,String isForceOpen) {
		if(StringUtil.isEmpty(hystrixGroup)) {
			//为空则默认HisBizExecuteGroup
			hystrixGroup = KasiteHystrixCommandKey.HisBizExecuteGroup.name();
		} 
		//是否启用断路器功能
		if(!StringUtil.isEmpty(isEnable)) {
			KasiteHystrixConfig.setValue(hystrixGroup, "CircuitBreakerEnabled", isEnable);
			return R.ok();
		} 
		//在启用断路器功能的前提下，是否强制打开断路器，打开断路器，则请求将会被拦截。设置为false则，断路器正常工作。
		if(!StringUtil.isEmpty(isForceOpen)) {
			KasiteHystrixConfig.setValue(hystrixGroup, "CircuitBreakerForceOpen", isForceOpen);
			KasiteHystrixConfig.setValue(hystrixGroup, "IsCircuitBreakerOpen", isForceOpen);
			return R.ok();
		} 
		return R.ok();
	}
}

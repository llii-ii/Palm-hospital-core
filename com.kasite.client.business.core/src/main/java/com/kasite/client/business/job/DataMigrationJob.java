//package com.kasite.client.business.job;
//
//import java.text.ParseException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//import javax.annotation.PostConstruct;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import com.alibaba.fastjson.JSONObject;
//import com.coreframework.util.DateOper;
//import com.github.pagehelper.Page;
//import com.kasite.client.basic.dao.IBatUserMapper;
//import com.kasite.client.basic.dao.IMemberBaseMapper;
//import com.kasite.client.basic.dao.IPatientMapper;
//import com.kasite.client.basic.dao.IUserMemberMapper;
//import com.kasite.client.business.module.his.fujian.quanzhou.fjykdxfsdeyy.dbo.HisPatientCardInfo;
//import com.kasite.client.business.module.his.fujian.quanzhou.fjykdxfsdeyy.hisdao.HisPatientCardDao;
//import com.kasite.client.order.bean.dbo.Order;
//import com.kasite.client.order.dao.IOrderMapper;
//import com.kasite.core.common.config.KasiteConfig;
//import com.kasite.core.common.constant.KstHosConstant;
//import com.kasite.core.common.resp.ApiKey;
//import com.kasite.core.common.resp.CommonResp;
//import com.kasite.core.common.util.CommonUtil;
//import com.kasite.core.common.util.LogUtil;
//import com.kasite.core.common.util.StringUtil;
//import com.kasite.core.serviceinterface.module.basic.dbo.BatUser;
//import com.kasite.core.serviceinterface.module.basic.dbo.MemberBase;
//import com.kasite.core.serviceinterface.module.basic.dbo.Patient;
//import com.kasite.core.serviceinterface.module.basic.dbo.UserMember;
//import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
//import com.kasite.core.serviceinterface.module.his.resp.HisQueryHospitalUserInfo;
//import com.yihu.wsgw.api.InterfaceMessage;
//
//import tk.mybatis.mapper.entity.Example;
//
//@Component
//public class DataMigrationJob {
//	
//	private static final Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);
//	
//	//旧版数据库数据源
//	private static DruidDataSource oldDataSource;
//	//旧版jdbcTemplate
//	private static JdbcTemplate oldTemplate;
//	
//	@Autowired
//	private IMemberBaseMapper memberBaseMapper;
//	@Autowired
//	private IBatUserMapper batUserMapper;
//	@Autowired
//	private IUserMemberMapper userMemberMapper;
//	@Autowired
//	private IPatientMapper patientMapper;
//	@Autowired
//	private HisPatientCardDao hisPatientCardDao;
//	
//	@Autowired
//	private IOrderMapper orderMapper;
//	
//	
//	
//	//初始化
//	@PostConstruct
//	public void init() {
//		//旧版数据库数据源
//		oldDataSource = new DruidDataSource();
//		oldDataSource.setUrl("jdbc:mysql://172.18.20.81:3306/hos2?allowMultiQueries=true&useOldAliasMetadataBehavior=true&useUnicode=true&characterEncoding=utf-8&useSSL=false");
//		oldDataSource.setUsername("root");
//		oldDataSource.setPassword("Root1234567890_");
//		oldDataSource.setDbType("mysql");	
//		//jdbctemplate
//		oldTemplate = new JdbcTemplate(oldDataSource);
//	}
//	
//	public Page<Map<String, Object>> queryMapListByPage(String sql,int pageIndex,int pageSize,Object... params){
//		Page<Map<String, Object>> result = new Page<>();
//		//总页数
//		int totalPage = 0;
//		
//		//从哪条记录开始查询
//		int startRows = 0;
//				
//		//查询总记录数
//		String rowsql="select count(*) from ("+sql+") tb";
//		int totalRows = oldTemplate.queryForObject(rowsql, Integer.class);
//		
//		List<Map<String, Object>>  list = null;
//		if(totalRows>0) {
//			//计算总页数
//			totalPage = (int)Math.ceil((double)(totalRows / pageSize));
//			
//			if(pageIndex<=1) {
//				startRows = 0;
//			}else {
//				startRows = (pageIndex-1)*pageSize;
//			}
//			sql+=" limit "+startRows+","+pageSize;
//			//查询数据
//			list = oldTemplate.queryForList(rowsql, params);
//		}
//		result.setTotal(totalRows);
//		result.setPageSize(pageSize);
//		result.setPageNum(pageSize);
//		result.setPages(totalPage);
//		result.setStartRow(startRows);
//		result.setEndRow((startRows+pageSize-1)>totalRows?totalRows:(startRows+pageSize-1));
//		result.addAll(list);
//		return result;
//	}
//	public <T> Page<T> queryByPage(Class<T> clazz,String sql,int pageIndex,int pageSize,Object... params){
//		Page<T> result = new Page<>();
//		//总页数
//		int totalPage = 0;
//		
//		//从哪条记录开始查询
//		int startRows = 0;
//				
//		//查询总记录数
//		String rowsql="select count(*) from ("+sql+") tb";
//		int totalRows = oldTemplate.queryForObject(rowsql, Integer.class);
//		
//		List<T>  list = null;
//		if(totalRows>0) {
//			//计算总页数
//			totalPage = (int)Math.ceil((double)(totalRows / pageSize));
//			
//			if(pageIndex<=1) {
//				startRows = 0;
//			}else {
//				startRows = (pageIndex-1)*pageSize;
//			}
//			sql+=" limit "+startRows+","+pageSize;
//			//查询数据
//			list = oldTemplate.queryForList(rowsql, clazz, params);
//		}
//		result.setTotal(totalRows);
//		result.setPageSize(pageSize);
//		result.setPageNum(pageIndex);
//		result.setPages(totalPage);
//		result.setStartRow(startRows);
//		result.setEndRow((startRows+pageSize-1)>totalRows?totalRows:(startRows+pageSize-1));
//		result.addAll(list);
//		return result;
//	}
//	
//	public void dealClinicMember() {
//		String memberSql = "SELECT * FROM B_MEMBER WHERE STATE=1";
//		int pageSize = 200;
//		Page<Map<String,Object>> result = queryMapListByPage(memberSql, 1, pageSize, new Object[]{});
//		int totalPage = result.getPages();
//		for(int i=1;i<= totalPage;i++) {
//			if(i>1) {
//				result = queryMapListByPage(memberSql, i, pageSize, new Object[]{});
//			}
//			for(Map<String, Object> map : result.getResult()) {
//				String memberId = StringUtil.chMap(map, "MEMBERID");
//				String memberName = StringUtil.chMap(map, "MEMBERNAME");
//				String cardNo = StringUtil.chMap(map, "CARDNO");
//				String mobile = StringUtil.chMap(map, "MOBILE");
//				String idCardNo = StringUtil.chMap(map, "IDCARDNO");
//				String sex = StringUtil.chMap(map, "SEX");
////				String mCardNo = StringUtil.chMap(map, "MCARDNO");
//				String openId = StringUtil.chMap(map, "OPID");
//				String channelId = StringUtil.chMap(map, "CHANNELID");
//				if("100125".equals(channelId)) {
//					//微信
//					channelId = KstHosConstant.WX_CHANNEL_ID;
//				}else if("100126".equals(channelId)) {
//					//支付宝
//					channelId = KstHosConstant.ZFB_CHANNEL_ID;
//				}else {
//					continue;
//				}
//				String hisId = StringUtil.chMap(map, "HISID");
//				
//				//调用HIS验证卡是否还有效
//				Map<String, String> queryHisPatientMap = new HashMap<>();
//				queryHisPatientMap.put("cardNo", cardNo);
//				queryHisPatientMap.put("memberName", memberName);
//				queryHisPatientMap.put("status", "1");
//				HisPatientCardInfo hisPatient = hisPatientCardDao.getPatientCardInfo(queryHisPatientMap);
//				if(hisPatient==null) {
//					log.info("卡号："+cardNo+"||姓名："+memberName+"，HIS中无有效就诊人信息，不处理");
//					continue;
//				}
//				if(StringUtil.isBlank(idCardNo) && StringUtil.isNotBlank(hisPatient.getIdNo())) {
//					idCardNo = hisPatient.getIdNo();
//				}
//				String memberCode = null;
//				if(StringUtil.isNotBlank(idCardNo)) {
//					memberCode = getMemberCode(memberName, -1, "01", idCardNo, null, null);
//				}else {
//					//TODO 无身份证号码时处理
//				}
//				MemberBase queryBase = new MemberBase();
//				queryBase.setMemberCode(memberCode);
//				MemberBase memberBase = memberBaseMapper.selectOne(queryBase);
//				if(memberBase == null) {
//					MemberBase base = new MemberBase();
//					base.setMemberId(memberId);
//					base.setMemberName(memberName);
//					base.setMemberCode(memberCode);
//					base.setIdCardNo(idCardNo);
//					base.setMobile(mobile);
//					base.setSex(getSex(sex));
//					
//					memberBaseMapper.insertSelective(base);
//				}else {
//					memberId = memberBase.getMemberId();
//				}
//				
//				Example example1 = new Example(BatUser.class);
//				example1.createCriteria().andEqualTo("openId", openId);
//				int cc = batUserMapper.selectCountByExample(example1);
//				if(cc<=0) {
//					BatUser user = new BatUser();
//					user.setId(CommonUtil.getGUID());
//					user.setOpenId(openId);
//					user.setChannelId(channelId);
//					user.setSubscribe(1);
//					batUserMapper.insertSelective(user);
//				}
//				
//				
//				UserMember userMember = new UserMember();
//				userMember.setMemberId(memberId);
//				userMember.setOpenId(openId);
//				cc = userMemberMapper.selectCount(userMember);
//				if(cc<=0) {
//					userMemberMapper.insertSelective(userMember);
//				}
//				
//				Patient queryPatient = new Patient();
//				queryPatient.setMemberId(memberId);
//				queryPatient.setCardNo(cardNo);
//				queryPatient.setCardType(KstHosConstant.CARDTYPE_1);
//				cc = patientMapper.selectCount(queryPatient);
//				if(cc<=0) {
//					Patient patient = new Patient();
//					patient.setId(CommonUtil.getGUID());
//					patient.setMemberId(memberId);
//					patient.setCardType(KstHosConstant.CARDTYPE_1);
//					patient.setCardNo(cardNo);
//					patient.setCardTypeName("就诊卡");
//					patient.setHisMemberId(hisId);
//					patient.setHosId(KasiteConfig.getOrgCode());
//					patient.setState(1);
//					patientMapper.insertSelective(patient);
//				}
//			}
//		}
//	}
//	public void dealInHospitalMember() throws Exception {
//		String memberSql = "SELECT * FROM B_HOSMEMBER WHERE STATE=1";
//		int pageSize = 200;
//		Page<Map<String,Object>> result = queryMapListByPage(memberSql, 1, pageSize, new Object[]{});
//		int totalPage = result.getPages();
//		for(int i=1;i<= totalPage;i++) {
//			if(i>1) {
//				result = queryMapListByPage(memberSql, i, pageSize, new Object[]{});
//			}
//			for(Map<String, Object> map : result.getResult()) {
//				String memberId = StringUtil.chMap(map, "MEMBERID");
//				String memberName = StringUtil.chMap(map, "MEMBERNAME");
//				String cardNo = StringUtil.chMap(map, "inhospitalno");
//				String mobile = StringUtil.chMap(map, "MOBILE");
//				String idCardNo = StringUtil.chMap(map, "IDCARDNO");
//				String sex = StringUtil.chMap(map, "SEX");
//				String openId = StringUtil.chMap(map, "OPID");
//				String channelId = StringUtil.chMap(map, "CHANNELID");
//				if("100125".equals(channelId)) {
//					//微信
//					channelId = KstHosConstant.WX_CHANNEL_ID;
//				}else if("100126".equals(channelId)) {
//					//支付宝
//					channelId = KstHosConstant.ZFB_CHANNEL_ID;
//				}else {
//					continue;
//				}
//				String hisId = StringUtil.chMap(map, "inhospitalid");
//				
//				//调用HIS验证卡是否还有效
//				Map<String, String> queryHisPatientMap = new HashMap<>();
//				queryHisPatientMap.put(ApiKey.HisQueryHospitalUserInfo.hospitalNo.getName(), cardNo);
//				queryHisPatientMap.put(ApiKey.HisQueryHospitalUserInfo.memberName.getName(), memberName);
//				queryHisPatientMap.put(ApiKey.HisQueryHospitalUserInfo.idCardNo.getName(), idCardNo);
//				
//				InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(DownloadHisBillJob.class,"queryHisOrderBillList", 
//						KasiteConfig.getOrgCode(), String.valueOf(UUID.randomUUID()),null, null);
//				CommonResp<HisQueryHospitalUserInfo> hisResp = HandlerBuilder.get()
//						.getCallHisService(msg.getAuthInfo()).queryHospitalUserInfo(msg, queryHisPatientMap);
//				
//			
//				if(KstHosConstant.SUCCESSCODE.equals(hisResp.getCode()) && (hisResp.getData()==null || hisResp.getData().size()<=0)) {
//					log.info("卡号："+cardNo+"||姓名："+memberName+"，HIS中无有效就诊人信息，不处理");
//					continue;
//				}
//				HisQueryHospitalUserInfo hisUser = hisResp.getResultData();
//				if(StringUtil.isBlank(idCardNo) && StringUtil.isNotBlank(hisUser.getIdCardId())) {
//					idCardNo = hisUser.getIdCardId();
//				}
//				String memberCode = null;
//				if(StringUtil.isNotBlank(idCardNo)) {
//					memberCode = getMemberCode(memberName, -1, "01", idCardNo, null, null);
//				}else {
//					//TODO 无身份证号码时处理
//				}
//				MemberBase queryBase = new MemberBase();
//				queryBase.setMemberCode(memberCode);
//				MemberBase memberBase = memberBaseMapper.selectOne(queryBase);
//				if(memberBase == null) {
//					MemberBase base = new MemberBase();
//					base.setMemberId(memberId);
//					base.setMemberName(memberName);
//					base.setMemberCode(memberCode);
//					base.setIdCardNo(idCardNo);
//					base.setMobile(mobile);
//					base.setSex(getSex(sex));
//					
//					memberBaseMapper.insertSelective(base);
//				}else {
//					memberId = memberBase.getMemberId();
//				}
//				
//				Example example1 = new Example(BatUser.class);
//				example1.createCriteria().andEqualTo("openId", openId);
//				int cc = batUserMapper.selectCountByExample(example1);
//				if(cc<=0) {
//					BatUser user = new BatUser();
//					user.setId(CommonUtil.getGUID());
//					user.setOpenId(openId);
//					user.setChannelId(channelId);
//					user.setSubscribe(1);
//					batUserMapper.insertSelective(user);
//				}
//				
//				
//				UserMember userMember = new UserMember();
//				userMember.setMemberId(memberId);
//				userMember.setOpenId(openId);
//				cc = userMemberMapper.selectCount(userMember);
//				if(cc<=0) {
//					userMemberMapper.insertSelective(userMember);
//				}
//				
//				Patient queryPatient = new Patient();
//				queryPatient.setMemberId(memberId);
//				queryPatient.setCardNo(cardNo);
//				queryPatient.setCardType(KstHosConstant.CARDTYPE_14);
//				cc = patientMapper.selectCount(queryPatient);
//				if(cc<=0) {
//					Patient patient = new Patient();
//					patient.setId(CommonUtil.getGUID());
//					patient.setMemberId(memberId);
//					patient.setCardType(KstHosConstant.CARDTYPE_14);
//					patient.setCardNo(cardNo);
//					patient.setCardTypeName("住院号");
//					patient.setHisMemberId(hisId);
//					patient.setHosId(KasiteConfig.getOrgCode());
//					patient.setState(1);
//					patientMapper.insertSelective(patient);
//				}
//			}
//		}
//	}
//	public void execute(){
//		try {
//			//门诊用户
//			dealClinicMember();
//			//住院用户
//			dealInHospitalMember();
//		}catch (Exception e) {
//			e.printStackTrace();
//			LogUtil.error(log, e);
//		}
//		
//	}
//	
//	
//	public  void dealOrder() throws Exception {
//		String memberSql = "SELECT A.ORDERID,A.SERVICEID,A.RESERVEID,A.PRICENAME,A.PRICE,A.ORDERMEMO,A.CARDNO,A.IFONLINEPAY,A.OPERATOR,A.OPERATORNAME,A.CTYPE,A.BEGIN,A.END,B.DEPTNAME,B.DEPTID,B.DOCTORID,B.DOCTORNAME FROM O_ORDER A LEFT JOIN O_ORDERMEDICALEXTENSION B ON A.ORDERID=B.ORDERID ";
//		int pageSize = 200;
//		Page<Map<String,Object>> result = queryMapListByPage(memberSql, 1, pageSize, new Object[]{});
//		int totalPage = result.getPages();
//		for(int i=1;i<= totalPage;i++) {
//			if(i>1) {
//				result = queryMapListByPage(memberSql, i, pageSize, new Object[]{});
//			}
//			for(Map<String, Object> map : result.getResult()) {
//				String orderId = StringUtil.chMap(map, "ORDERID");
//				String serviceId = StringUtil.chMap(map, "SERVICEID");
//				String priceName = StringUtil.chMap(map, "PRICENAME");
//				String price = StringUtil.chMap(map, "PRICE");
//				String orderName = StringUtil.chMap(map, "ORDERMEMO");
//				String cardNo = StringUtil.chMap(map, "CARDNO");
//				String cType = StringUtil.chMap(map, "CTYPE");
//				String ifOnlinePay = StringUtil.chMap(map, "IFONLINEPAY");
//				String channelId = StringUtil.chMap(map, "OPERATOR");
//				String channelName = StringUtil.chMap(map, "OPERATORNAME");
//				String begin = StringUtil.chMap(map, "BEGIN");
//				String end = StringUtil.chMap(map, "END");
//				String reserveId = StringUtil.chMap(map, "RESERVEID");
//				String deptCode = StringUtil.chMap(map, "DEPTCODE");
//				String doctorCode = StringUtil.chMap(map, "DOCTORCODE");
//				String deptName = StringUtil.chMap(map, "DEPTNAME");
//				String doctorName = StringUtil.chMap(map, "DOCTORNAME");
//				String openId = null;
//				String operatorId = null;
//				String operatorName = null;
//				if("0".equals(serviceId)) {
//					//预约挂号单
//					if("jkzl".equals(channelId)) {
//						//健康之路渠道
//						channelId = KstHosConstant.JKZL_CHANNEL_ID;
//						channelName = "健康之路";
//						operatorId = KstHosConstant.JKZL_CHANNEL_ID;
//						operatorName = channelName;
//					}else {
//						openId = channelId;
//						operatorId = channelId;
//						operatorName = channelId;
//					}
//				}else {
//					if("100125".equals(channelId)) {
//						//微信
//						operatorId = channelId;
//						operatorName = channelId;
//						channelId = KstHosConstant.WX_CHANNEL_ID;
//						channelName = KstHosConstant.WX_CHANNEL_NAME;
//					}else if("100126".equals(channelId)) {
//						//支付宝
//						operatorId = channelId;
//						operatorName = channelId;
//						channelId = KstHosConstant.ZFB_CHANNEL_ID;
//						channelName = KstHosConstant.ZFB_CHANNEL_NAME;
//					}
//				}
//				
//				
//				Order order = new Order();
//				order.setBeginDate(DateOper.parse2Timestamp(begin));
//				order.setCardNo(cardNo);
//				order.setCardType(cType);
//				order.setChannelId(channelId);
//				order.setChannelName(channelName);
//				order.setEndDate(DateOper.parse2Timestamp(end));
//				order.setEqptType(null);
//				order.setHosId(KasiteConfig.getOrgCode());
//				if(StringUtil.isNotBlank(ifOnlinePay)) {
//					order.setIsOnLinePay(Integer.parseInt(ifOnlinePay));
//				}
////				order.setMemberId(memberId);
////				order.setMerchantType(merchantType);
//				
//				order.setOperatorId(operatorId);
//				order.setOperatorName(operatorName);
//				order.setOrderId(orderId);
//				order.setOpenId(openId);
//				
//				JSONObject orderMenoJs = new JSONObject();
//				orderMenoJs.put("DeptCode", deptCode);
//				orderMenoJs.put("DeptName", deptName);
//				orderMenoJs.put("DoctorCode", doctorCode);
//				orderMenoJs.put("DoctorName", doctorName);
//				orderMenoJs.put("FeeInfo", "");
//				
////				order.setPrescNo(prescNo);
//				order.setOrderMemo(orderMenoJs.toString());
//				order.setOrderNum(CommonUtil.getOrderNum());
//				order.setPrice(Integer.parseInt(price));
//				order.setPriceName(priceName);
//				if("005".equals(serviceId)) {
//					//就诊卡充值
//					order.setServiceId(KstHosConstant.ORDERTYPE_006);
//				}else if("666".equals(serviceId)){
//					//诊间支付   
//					order.setServiceId(KstHosConstant.ORDERTYPE_008);
//				}else if("007".equals(serviceId)){
//					//住院充值
//					order.setServiceId(KstHosConstant.ORDERTYPE_007);
//				}else if("0".equals(serviceId)) {
//					order.setServiceId(KstHosConstant.ORDERTYPE_0);
//				}
//				order.setTotalPrice(Integer.parseInt(price));
//				orderMapper.insertSelective(order);
//			}
//		}
//		
//	}
//	
//	/**
//	 * 性别转换
//	 * @param sex
//	 * @return
//	 */
//	private int getSex(String sex){
//		if("男".equals(sex)) {
//			return 1;
//		}else if("女".equals(sex)) {
//			return 2;
//		}else if("1".equals(sex) || "2".equals(sex)){
//			return Integer.parseInt(sex);
//		}
//		return 3;
//	}
//	/**
//	 * 获取成员编码 规则：成员名称+“_”+证件类型+“_”+证件号码（儿童，无证件类型及号码时，使用证件监护人的证件类型及号码）
//	 * @param memberName
//	 * @param isChildren
//	 * @param certType
//	 * @param certNum
//	 * @param guardianCertType
//	 * @param guardianCertNum
//	 * @return
//	 */
//	private String getMemberCode(String memberName,Integer isChildren,String certType,String certNum,String guardianCertType,String guardianCertNum) {
//		if(isChildren==null) {
//			//默认不为儿童
//			isChildren=-1;
//		}
//		if(isChildren==1 && StringUtil.isBlank(certNum)) {
//			return memberName+"_"+guardianCertType+"_"+guardianCertNum;
//		}
//		return memberName+"_"+certType+"_"+certNum;
//		
//	}
//	public static void main(String[] args) {
////		DataMigrationJob job = new DataMigrationJob();
////		job.init();
////		String rowsql="select count(*) from (SELECT * FROM B_MEMBER ) tb";
////		int total = oldTemplate.queryForObject(rowsql, Integer.class);
////		System.err.println(total);
//		
//	}
//}

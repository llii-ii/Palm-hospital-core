package com.kasite.client.pay.job;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayDataDataserviceBillDownloadurlQueryModel;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.kasite.client.pay.bean.dbo.Bill;
import com.kasite.client.pay.dao.IBillMapper;
import com.kasite.client.zfb.constants.AlipayClientFactory;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.config.SwiftpassEnum;
import com.kasite.core.common.config.ZFBConfigEnum;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.wechat.TenpayService;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.handler.IDownloadOutQLCOrder;
import com.kasite.core.serviceinterface.module.msg.IMsgService;
import com.kasite.core.serviceinterface.module.msg.req.ReqMaintenanceMsg;
import com.kasite.core.serviceinterface.module.pay.IPaymentGateWayService;
import com.kasite.core.serviceinterface.module.pay.bo.MchBill;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespDownloadBill;
import com.yihu.wsgw.api.InterfaceMessage;

import tk.mybatis.mapper.entity.Example;

/**
 * 下载BILL账单JOB
 * 
 * @author zhaoy
 *
 */
@Component
public class DownloadBillJob {
	
	private boolean flag = true;
	
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);
	
	/**类型为支付*/
	private static String ZF_TYPE = "0";
	/**重试3次*/
	private static int RETTIMES = 3;

	@Autowired
	KasiteConfigMap config;
	
	@Autowired
	IBillMapper billMapper;
	
	@Autowired
	IMsgService msgService;
	
	@Autowired
	@Qualifier("unionPay")
	private IPaymentGateWayService unionPayGateWayService;

	@Autowired
	@Qualifier("swiftpassWechatPay")
	private IPaymentGateWayService swiftpassWechatPayGateWayService;
	
	/**
	 * 商户原始账单下载
	 * 			--每天10点执行
	 * 
	 * @Description:
	 */
	public void execute(){
		try {
			if (flag && config.isStartJob(this.getClass())) {
				flag = false;
				Date billDate = DateOper.getAddDays(-1);//账单日期，昨天
				//Date billDate = DateOper.parse("2018-10-31");
				downloadBill(billDate);
				flag = true;
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			LogUtil.error(log, e);
		} finally {
			flag = true;
		}
	}
	
	/**
	 * 开始下载
	 * @throws ParseException 
	 */
	public void downloadBill(Date billDate) throws ParseException{
		String startDate =DateOper.formatDate(DateOper.getStartOfDay(billDate),"yyyy-MM-dd HH:mm:ss");
		String endDate =DateOper.formatDate( DateOper.getEndOfDay(billDate),"yyyy-MM-dd HH:mm:ss");
		//取出所有商户配置的configkey
		Map<String, ChannelTypeEnum> configkeyMap = KasiteConfig.getAllConfigKey();
		if(configkeyMap != null) {
			for (Entry<String, ChannelTypeEnum> entry : configkeyMap.entrySet()) {
				String configKey = entry.getKey();
				ChannelTypeEnum payWay = entry.getValue();
				if(ChannelTypeEnum.wechat.equals(entry.getValue())) {
					// 微信账单下载
					String downWXBillDate = DateOper.formatDate(billDate,  "yyyyMMdd");
					downloadBillWX(startDate, endDate, downWXBillDate, configKey);
				}else if(ChannelTypeEnum.zfb.equals(payWay)) {
					String downZFBBillDate = DateOper.formatDate(billDate,  "yyyy-MM-dd");
					//支付宝账单下载
					String signType = KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_signType, configKey);
					if(KstHosConstant.ALIPAY_RSA.equals(signType)) {
						downloadBillZFB_RSA(startDate, endDate, downZFBBillDate, configKey);
					}else if(KstHosConstant.ALIPAY_RSA2.equals(signType)) {
						downloadBillZFB_RSA2(startDate, endDate, downZFBBillDate, configKey);
					}
				}else if(ChannelTypeEnum.unionpay.equals(payWay) || ChannelTypeEnum.swiftpass.equals(payWay)
						|| ChannelTypeEnum.netpay.equals(payWay)) {
					String swiftpassMchType = KstHosConstant.STRING_EMPTY;
					if( ChannelTypeEnum.swiftpass.equals(payWay)) {
						swiftpassMchType = KasiteConfig.getSwiftpass(SwiftpassEnum.swiftpass_mch_type,configKey);
					}
					IPaymentGateWayService payGateWayService = HandlerBuilder.get().getPayGateWayInstance(payWay.name()+swiftpassMchType);
					//银联走通用账单下载
					commonDownloadBill(startDate,endDate,billDate,configKey,payGateWayService);
					//commonDownloadBill("2018-11-20 00:00:00", "2018-11-21 00:00:00",billDate,"777290058164492");
				}else {
					log.error("账单日期:"+DateOper.formatDate(billDate,  "yyyy-MM-dd")+",暂不支持商户类型："+payWay+"账单下载！");
				}
			}
		}else {
			
		}
		
	}
	/**
	 * 微信账单下载
	 * 
	 * @param date
	 * @param payInfo
	 */
	private void downloadBillWX(String startDate, String endDate, String billDate, String configkey) {
		log.info("开始下载微信对账单|" + configkey);
		int times = 0;
		int count = 0;
		boolean isEmptyBill = false;
		while (times++ < RETTIMES) {
			try {
				String retStr = TenpayService.downloadBill(configkey, billDate);
				String[] bills = retStr.replaceAll("`", "").split("\n");
				if(bills==null || bills.length>0 && bills[0].indexOf(KstHosConstant.WX_BILL_FAIL)>0){
					log.error(configkey + "|账单下载返回失败结果：\r\n" + retStr);
					continue;
				}
				log.info(configkey + "|账单下载返回结果：\r\n" + retStr);
				
				// 删除已经存在的
				Example example = new Example(Bill.class);
				example.createCriteria().andBetween("transDate", startDate, endDate).andEqualTo("configKey", configkey);
				int n = billMapper.deleteByExample(example);
				log.info(configkey + "|账单下载删除已存在条数：" + n);
				count = bills.length - 2;  // 统计微信账单数	
				for (int i = 1; i < bills.length - 2; i++) {
					String merchNo;
					String refundMerchNo;
					Integer orderType;
					String transactions;
					String refundPrice="0";
					String orderId;
					String refundOrderId = null;
					
					String billObj = bills[i];
					String[] param = billObj.split(",");
					orderId = param[6];
					//支付金额
					transactions = param[12];
					//微信订单号
					merchNo = param[5];
					String operatorId = param[6];
					// 不存在退款订单号，说明是付款订单
					if (ZF_TYPE.equals(param[14])) {
						//退款订单号
						refundMerchNo = "";
						//订单类型 1支付 2退费
						orderType = KstHosConstant.BILL_ORDER_TYPE_1;

					} else {
						//退款订单号
						refundMerchNo = param[14];

						orderType = KstHosConstant.BILL_ORDER_TYPE_2;
						refundPrice = param[16];
						refundOrderId = param[15];
					}
					Bill bill = new Bill();
					bill.setBillId(StringUtil.getUUID());
					bill.setMerchNo(merchNo);
					bill.setOrderId(orderId);
					bill.setRefundMerchNo(refundMerchNo);
					bill.setRefundOrderId(refundOrderId);
					String channelId = billMapper.findChannelByOrderId(orderId);
					if(StringUtil.isBlank(channelId)) {
						channelId = getChannelId(orderId);
						if(StringUtil.isBlank(channelId) ) {
							//如果还是为空，则赋默认值
							channelId = KstHosConstant.UNKNOW_CHANNEL_ID;
						}
					}
					if(StringUtil.isNotBlank(channelId)) {
						bill.setChannelId(channelId);
						bill.setChannelName(KasiteConfig.getChannelById(channelId));
					}
					bill.setConfigKey(configkey);
					bill.setOrderType(orderType);
					bill.setTransactions(StringUtil.yuanChangeFenInt(transactions));
					bill.setRefundPrice(StringUtil.yuanChangeFenInt(refundPrice));
					bill.setTransDate(DateOper.parse2Timestamp(param[0]));
					bill.setAppId(KasiteConfig.getAppId());
					bill.setTradeType(param[8]);// 调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP
					bill.setOperatorId(operatorId);
					billMapper.insertSelective(bill);
				}
				isEmptyBill = false;
				break;//下载成功，则退出循环
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e);
			}
		}
		//如果while循环执行了3次，说明账单都是下载失败。while循环执行次数小于3次说明账单下载成功
		//并且不是空账单
		if( times >3 &&!isEmptyBill) {
			try {
				InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(DownloadBillJob.class, "","<Req><Data></Data></Req>", 
						null,null,configkey);
				ReqMaintenanceMsg reqMaintenanceMsg = new ReqMaintenanceMsg(msg, KasiteConfig.getAppId(), 
						"下载账单异常!",null, "紧急",configkey + "|微信账单下载失败！请及时介入处理！", null);
				msgService.sendMaintenancenMsg(new CommonReq<ReqMaintenanceMsg>(reqMaintenanceMsg));
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e);
			}
		}
		log.info("结束下载微信对账单|" + configkey + "==>共下载微信账单" + count +"条数据");
	}

	/**
	 * 支付宝账单下载
	 */
	private void downloadBillZFB_RSA2(String startDate, String endDate, String billDate, String configkey) {
		System.out.println("开始下载支付宝对账单|"+configkey);
		log.info("开始下载支付宝对账单|"+configkey);
		int times =0;
		boolean isEmptyBill = false;
		while (times++ < RETTIMES) {
			try {
				// 账单类型，商户通过接口或商户经开放平台授权后其所属服务商通过接口可以获取以下账单类型：trade、signcustomer；
				// trade指商户基于支付宝交易收单的业务账单；signcustomer是指基于商户支付宝余额收入及支出等资金变动的帐务账单；
				String billType = "trade";
				// SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
				AlipayClient client = AlipayClientFactory.getAlipayClientInstance2(configkey);
				AlipayDataDataserviceBillDownloadurlQueryRequest alipayRequest = new AlipayDataDataserviceBillDownloadurlQueryRequest();
				
				AlipayDataDataserviceBillDownloadurlQueryModel model = new AlipayDataDataserviceBillDownloadurlQueryModel();
				model.setBillType(billType);
				model.setBillDate(billDate);
				alipayRequest.setBizModel(model);
				
				AlipayDataDataserviceBillDownloadurlQueryResponse alipayResponse = client.execute(alipayRequest);
				String billDownloadUrl = alipayResponse.getBillDownloadUrl();
				//返回账单下载URL的，利用该URL下载账单
				if (StringUtil.isNotEmpty(billDownloadUrl)) {
					String destination = null;
					// 支付宝账单是zip压缩包
					String filename = billType + "_" + billDate + "_" + System.currentTimeMillis() + ".zip";
					String tmpdir = System.getProperty("java.io.tmpdir");
					String tempFileName = "tempQLC";
					if (tmpdir.endsWith("/") || tmpdir.endsWith("\\")) {
						destination = tmpdir + tempFileName + File.separator ;
					} else {
						destination = tmpdir + File.separator + tempFileName + File.separator ;
					}
					System.out.println(destination + filename);
					ZipFile zipFile = null;
					File file = new File(destination);
					if(file.exists()){
						System.out.println("清空临时文件:"+ CommonUtil.deleteAllFilesOfDir(file));
					}else{
						System.out.println("创建临时文件:"+file.mkdirs());
						file.mkdirs();
					}
					destination = destination + filename;
					// 下载保存到临时目录
					FileUtils.copyURLToFile(new URL(billDownloadUrl), new File(destination));
					// 直接读取zip压缩文件
					zipFile = new ZipFile(destination, "GBK");
					BufferedInputStream bis = null;
					Enumeration<ZipEntry> zipEntries = zipFile.getEntries();
					ZipEntry entry;
					
					String line = null;
					while (zipEntries.hasMoreElements()) {
						entry = (ZipEntry) zipEntries.nextElement();
						if (entry.isDirectory()) {
							// 目录的过滤
							continue;
						}
						// 以"_业务明细.csv"结尾的是明细,目前只有一个明细文件
						if (entry.getName().toLowerCase().contains("业务明细")
								&& !entry.getName().toLowerCase().contains("(汇总)")) {
							bis = new BufferedInputStream(zipFile.getInputStream(entry));
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							IOUtils.copy(bis, baos);
							bis.close();
							ZipFile.closeQuietly(zipFile);
							line = baos.toString("GBK");
							break;
						}
					}
					if(StringUtil.isEmpty(line)){
						log.info("第"+(times+1)+"次下载支付宝对账单返回空!!!");
						isEmptyBill = true;
						continue;
					}
					List<String> list = Arrays.asList(line.split("\r\n"));
					if(list!=null && !list.isEmpty()){
						// 删除已经存在的
						Example example = new Example(Bill.class);
						example.createCriteria().andBetween("transDate", startDate, endDate).andEqualTo("configKey", configkey);
						int n = billMapper.deleteByExample(example);
						log.info("支付宝账单下载删除已存在条数：" + n);
						for(String detail:list){
							if(StringUtil.isNotEmpty(detail)){
								detail = detail.trim();
								if(!detail.startsWith("#") && !detail.startsWith("支付宝交易号")){
									//过滤 "#"和"支付宝交易号"开头的行,剩下的就是明细
									String[] array = detail.split(",");
									if(array!=null && array.length>=22 ){

										String merchNo;
										String refundMerchNo;
										Integer orderType;
										String transactions;
										String refundPrice="0";
										String orderId;
										//支付宝交易号,商户订单号,业务类型,商品名称,创建时间,完成时间,门店编号,门店名称,操作员,终端号,对方账户,订单金额（元）,商家实收（元）,支付宝红包（元）,集分宝（元）,支付宝优惠（元）,商家优惠（元）,券核销金额（元）,券名称,商家红包消费金额（元）,卡消费金额（元）,退款批次号/请求号,服务费（元）,分润（元）,备注
										//支付宝订单号
										merchNo = array[0].trim();
										//商户订单号
										orderId = array[1].trim();

										refundMerchNo = "";
										//交易,退款
										String orderTypeStr = array[2].trim();
										//订单金额,全部保存正数
										transactions = array[11].trim().replace("-", "");
										//渠道退款订单流水号
										refundMerchNo = array[21].trim();
										//不存在退款订单号，说明是付款订单
										if ("交易".equalsIgnoreCase(orderTypeStr)) {
											//订单类型 1支付 2退费
											orderType = KstHosConstant.BILL_ORDER_TYPE_1;

										} else if ("退款".equalsIgnoreCase(orderTypeStr)) {
											orderType = KstHosConstant.BILL_ORDER_TYPE_2;
											refundPrice = transactions;
										}else{
											//其他类型
											log.error("支付宝账单出现其他业务类型");
											continue;
										}
										Bill bill = new Bill();
										bill.setBillId(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
										bill.setMerchNo(merchNo);
										bill.setRefundMerchNo(refundMerchNo);
										bill.setOrderId(orderId);
										bill.setRefundOrderId(refundMerchNo);
										bill.setOrderType(orderType);
										bill.setTransactions(StringUtil.yuanChangeFenInt(transactions));
										String channelId = billMapper.findChannelByOrderId(orderId);
										if(StringUtil.isBlank(channelId)) {
											channelId = getChannelId(orderId);
											if(StringUtil.isBlank(channelId) ) {
												//如果还是为空，则赋默认值
												channelId = KstHosConstant.UNKNOW_CHANNEL_ID;
											}
										}
										if(StringUtil.isNotBlank(channelId)) {
											bill.setChannelId(channelId);
											bill.setChannelName(KasiteConfig.getChannelById(channelId));
										}
										bill.setRefundPrice(StringUtil.yuanChangeFenInt(refundPrice));
										bill.setTransDate(DateOper.parse2Timestamp(array[5]));
										bill.setConfigKey(configkey);
										bill.setAppId(KasiteConfig.getAppId());
										// 调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP
										bill.setTradeType(null);
										billMapper.insertSelective(bill);
									}
								}
							}
						}
						isEmptyBill = false;
						break;//下载读取完成跳出循环
					}
				}
				
			} catch (Exception e) {
				isEmptyBill = true;
				log.info("第"+(times+1)+"次获取支付宝下载账单地址返回空!!!");
			} 
		}
		log.info("结束下载支付宝对账单|" + configkey);
		//如果while循环执行了3次，说明账单都是下载失败。while循环执行次数小于3次说明账单下载成功
		//并且不是空账单
		if( times >3 && !isEmptyBill ) {
			try {
				InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(DownloadBillJob.class, "","<Req><Data></Data></Req>", 
						null,null,configkey);
				ReqMaintenanceMsg reqMaintenanceMsg = new ReqMaintenanceMsg(msg, KasiteConfig.getAppId(), 
						"下载账单异常!",null, "紧急",configkey + "|支付宝账单下载失败！请及时介入处理！", null);
				msgService.sendMaintenancenMsg(new CommonReq<ReqMaintenanceMsg>(reqMaintenanceMsg));
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e);
			}	
		}
	}
	
	/**
	 * 支付宝账单下载
	 */
	private void downloadBillZFB_RSA(String startDate, String endDate, String billDate, String configkey) {
		System.out.println("开始下载支付宝对账单|"+configkey);
		log.info("开始下载支付宝对账单|"+configkey);
		int times =0;
		boolean isEmptyBill = false;
		while (times++ < RETTIMES) {
			try {
				// 账单类型，商户通过接口或商户经开放平台授权后其所属服务商通过接口可以获取以下账单类型：trade、signcustomer；
				// trade指商户基于支付宝交易收单的业务账单；signcustomer是指基于商户支付宝余额收入及支出等资金变动的帐务账单；
				String billType = "trade";
				// SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
				AlipayClient client = AlipayClientFactory.getAlipayClientInstance(configkey);
				AlipayDataDataserviceBillDownloadurlQueryRequest alipayRequest = new AlipayDataDataserviceBillDownloadurlQueryRequest();
				
				AlipayDataDataserviceBillDownloadurlQueryModel model = new AlipayDataDataserviceBillDownloadurlQueryModel();
				model.setBillType(billType);
				model.setBillDate(billDate);
				alipayRequest.setBizModel(model);
				
				AlipayDataDataserviceBillDownloadurlQueryResponse alipayResponse = client.execute(alipayRequest);
				String billDownloadUrl = alipayResponse.getBillDownloadUrl();
				//返回账单下载URL的，利用该URL下载账单
				if (StringUtil.isNotEmpty(billDownloadUrl)) {
					String destination = null;
					// 支付宝账单是zip压缩包
					String filename = billType + "_" + billDate + "_" + System.currentTimeMillis() + ".zip";
					String tmpdir = System.getProperty("java.io.tmpdir");
					String tempFileName = "tempZFBBill";
					if (tmpdir.endsWith("/") || tmpdir.endsWith("\\")) {
						destination = tmpdir + tempFileName + File.separator ;
					} else {
						destination = tmpdir + File.separator + tempFileName + File.separator ;
					}
					System.out.println(destination + filename);
					ZipFile zipFile = null;
					File file = new File(destination);
					if(file.exists()){
						System.out.println("清空临时文件:"+ CommonUtil.deleteAllFilesOfDir(file));
					}else{
						System.out.println("创建临时文件:"+file.mkdirs());
						file.mkdirs();
					}
					destination = destination + filename;
					// 下载保存到临时目录
					FileUtils.copyURLToFile(new URL(billDownloadUrl), new File(destination));
					// 直接读取zip压缩文件
					zipFile = new ZipFile(destination, "GBK");
					BufferedInputStream bis = null;
					Enumeration<ZipEntry> zipEntries = zipFile.getEntries();
					ZipEntry entry;
					
					String line = null;
					while (zipEntries.hasMoreElements()) {
						entry = (ZipEntry) zipEntries.nextElement();
						if (entry.isDirectory()) {
							// 目录的过滤
							continue;
						}
						// 以"_业务明细.csv"结尾的是明细,目前只有一个明细文件
						if (entry.getName().toLowerCase().contains("业务明细")
								&& !entry.getName().toLowerCase().contains("(汇总)")) {
							bis = new BufferedInputStream(zipFile.getInputStream(entry));
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							IOUtils.copy(bis, baos);
							bis.close();
							ZipFile.closeQuietly(zipFile);
							line = baos.toString("GBK");
							break;
						}
					}
					if(StringUtil.isEmpty(line)){
						log.info("第"+(times+1)+"次下载支付宝对账单返回空!!!");
						isEmptyBill = true;
						continue;
					}
					List<String> list = Arrays.asList(line.split("\r\n"));
					if(list!=null && !list.isEmpty()){
						// 删除已经存在的
						Example example = new Example(Bill.class);
						example.createCriteria().andBetween("transDate", startDate, endDate).andEqualTo("configKey", configkey);
						int n = billMapper.deleteByExample(example);
						log.info("支付宝账单下载删除已存在条数：" + n);
						for(String detail:list){
							if(StringUtil.isNotEmpty(detail)){
								detail = detail.trim();
								if(!detail.startsWith("#") && !detail.startsWith("支付宝交易号")){
									//过滤 "#"和"支付宝交易号"开头的行,剩下的就是明细
									String[] array = detail.split(",");
									if(array!=null && array.length>=22 ){

										String merchNo;
										String refundMerchNo;
										Integer orderType;
										String transactions;
										String refundPrice="0";
										String orderId;
										//支付宝交易号,商户订单号,业务类型,商品名称,创建时间,完成时间,门店编号,门店名称,操作员,终端号,对方账户,订单金额（元）,商家实收（元）,支付宝红包（元）,集分宝（元）,支付宝优惠（元）,商家优惠（元）,券核销金额（元）,券名称,商家红包消费金额（元）,卡消费金额（元）,退款批次号/请求号,服务费（元）,分润（元）,备注
										//支付宝订单号
										merchNo = array[0].trim();
										//商户订单号
										orderId = array[1].trim();

										refundMerchNo = "";
										//交易,退款
										String orderTypeStr = array[2].trim();
										//订单金额,全部保存正数
										transactions = array[11].trim().replace("-", "");
										//渠道退款订单流水号
										refundMerchNo = array[21].trim();
										//不存在退款订单号，说明是付款订单
										if ("交易".equalsIgnoreCase(orderTypeStr)) {
											//订单类型 1支付 2退费
											orderType = KstHosConstant.BILL_ORDER_TYPE_1;

										} else if ("退款".equalsIgnoreCase(orderTypeStr)) {
											orderType = KstHosConstant.BILL_ORDER_TYPE_2;
											refundPrice = transactions;
										}else{
											//其他类型
											log.error("支付宝账单出现其他业务类型");
											continue;
										}
										Bill bill = new Bill();
										bill.setBillId(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
										bill.setMerchNo(merchNo);
										bill.setRefundMerchNo(refundMerchNo);
										bill.setOrderId(orderId);
										bill.setRefundOrderId(refundMerchNo);
										bill.setOrderType(orderType);
										bill.setTransactions(StringUtil.yuanChangeFenInt(transactions));
										String channelId = billMapper.findChannelByOrderId(orderId);
										if(StringUtil.isBlank(channelId)) {//如果从o_order订单查询对应渠道号为空
											channelId = getChannelId(orderId);//查询历史的订单号
											if(StringUtil.isBlank(channelId) ) {
												//如果还是为空，则赋默认值
												channelId = KstHosConstant.UNKNOW_CHANNEL_ID;
											}
										}
										if(StringUtil.isNotBlank(channelId)) {
											bill.setChannelId(channelId);
											bill.setChannelName(KasiteConfig.getChannelById(channelId));
										}
										bill.setRefundPrice(StringUtil.yuanChangeFenInt(refundPrice));
										bill.setTransDate(DateOper.parse2Timestamp(array[5]));
										bill.setConfigKey(configkey);
										bill.setAppId(KasiteConfig.getAppId());
										// 调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP
										bill.setTradeType(null);
										billMapper.insertSelective(bill);
									}
								}
							}
						}
						isEmptyBill = false;
						break;//下载读取完成跳出循环
					}
				}
				
			} catch (Exception e) {
				isEmptyBill = true;
				log.info("第"+(times+1)+"次获取支付宝下载账单地址返回空!!!");
			} 
		}
		log.info("结束下载支付宝对账单|" + configkey);
		//如果while循环执行了3次，说明账单都是下载失败。while循环执行次数小于3次说明账单下载成功
		//并且不是空账单
		if( times >3 && !isEmptyBill ) {
			try {
				InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(DownloadBillJob.class, "","<Req><Data></Data></Req>", 
						null,null,configkey);
				ReqMaintenanceMsg reqMaintenanceMsg = new ReqMaintenanceMsg(msg, KasiteConfig.getAppId(), 
						"下载账单异常!",null, "紧急",configkey + "|支付宝账单下载失败！请及时介入处理！", null);
				msgService.sendMaintenancenMsg(new CommonReq<ReqMaintenanceMsg>(reqMaintenanceMsg));
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e);
			}	
		}
	}
	

	/***
	 * 获取外部全流程订单的渠道号
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	private String getChannelId(String orderId) throws Exception{
		String channelId = billMapper.findChannelForOrderCheck(orderId);
		if(StringUtil.isBlank(channelId)) {
			String hosid = KasiteConfig.getOrgCode();
			IDownloadOutQLCOrder service = HandlerBuilder.get().getCallHisService(hosid, IDownloadOutQLCOrder.class);
			if(service != null) {
				Map<String,String> paramMap = new HashMap<String,String>(16);
				paramMap.put("orderId", orderId);
				InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(DownloadBillJob.class, "queryOrderChannelInfo", 
						KasiteConfig.getOrgCode(), String.valueOf(UUID.randomUUID()),null, null);
				channelId = service.queryOrderChannelInfo(msg, paramMap);
			}
		}
		return channelId;
	}
	
	private void commonDownloadBill(String startDate, String endDate, Date billDate, String configKey,IPaymentGateWayService paymentGateWayService) {
		AuthInfoVo authInfoVo = KasiteConfig.createAuthInfoVo(this.getClass());
		authInfoVo.setConfigKey(configKey);
		int times =0;
		boolean isSuccess = false;
		while (times++ < RETTIMES) {
			try {
				PgwRespDownloadBill pgwRespDownloadBill = paymentGateWayService.downloadBill(authInfoVo, billDate);
				if (RetCode.Success.RET_10000.equals(pgwRespDownloadBill.getRespCode())) {
					for (MchBill mchBill : pgwRespDownloadBill.getMchBillList()) {
						Bill bill = new Bill();
						bill.setBillId(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
						bill.setMerchNo(mchBill.getMchTradeNo());// queryId
						bill.setOrderId(mchBill.getOrderId());// orderId
						bill.setOrderType(mchBill.getOrderType());
						bill.setTransactions(mchBill.getPayPrice());// txnAmet
						bill.setRefundMerchNo(mchBill.getRefundMchTradeNo());// queryId
						bill.setRefundOrderId(mchBill.getRefundOrderId());// orderId
						bill.setRefundPrice(mchBill.getRefundPrice());// txnAmet
						String channelId = billMapper.findChannelByOrderId(bill.getOrderId());
						// String channelId = "100123";
						if (StringUtil.isBlank(channelId)) {
							channelId = getChannelId(bill.getOrderId());
							if (StringUtil.isBlank(channelId)) {
								// 如果还是为空，则赋默认值
								channelId = KstHosConstant.UNKNOW_CHANNEL_ID;
							}
						}
						if (StringUtil.isNotBlank(channelId)) {
							bill.setChannelId(channelId);
							bill.setChannelName(KasiteConfig.getChannelById(channelId));
						}
						
						if( KstHosConstant.BILL_ORDER_TYPE_2.equals(bill.getOrderType())
								&& StringUtil.isEmpty(bill.getRefundOrderId())) {
							//招行的退款订单没有refundorderid
							billMapper.findRefundOrderId(bill.getOrderId(),bill.getRefundMerchNo());
						}
						bill.setTransDate(mchBill.getTransDate());
						bill.setConfigKey(configKey);
						bill.setAppId(KasiteConfig.getAppId());
						// 调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP
						bill.setTradeType(mchBill.getTradeType());// payType
						billMapper.insertSelective(bill);
					}
					isSuccess = true;
				} else {
					isSuccess = false;
					LogUtil.warn(log, new LogBody(authInfoVo).set("code", pgwRespDownloadBill.getRespCode())
							.set("msg", pgwRespDownloadBill.getRespMsg()));
				}
			} catch ( ParseException e) {
				e.printStackTrace();
				LogUtil.error(log, authInfoVo, e);
				isSuccess = false;
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.error(log, authInfoVo, e);
				isSuccess = false;
			}
			if( times >3 && !isSuccess ) {
				try {
					InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(DownloadBillJob.class, "","<Req><Data></Data></Req>", 
							null,null,configKey);
					ReqMaintenanceMsg reqMaintenanceMsg = new ReqMaintenanceMsg(msg, KasiteConfig.getAppId(), 
							"下载账单异常!",null, "紧急",configKey + "|支付宝账单下载失败！请及时介入处理！", null);
					msgService.sendMaintenancenMsg(new CommonReq<ReqMaintenanceMsg>(reqMaintenanceMsg));
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.error(log, authInfoVo, e);
				}	
			}
		}
		
	}
}

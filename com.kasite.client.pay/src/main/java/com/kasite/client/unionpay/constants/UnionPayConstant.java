package com.kasite.client.unionpay.constants;

/**
 * 银联全局变量
 * @author linjf
 * TODO
 */
public class UnionPayConstant {

	//全渠道固定值
	public static String VERSION = "5.1.0";
	
	//默认配置的是UTF-8
	public static String ENCODING = "UTF-8";
	
	public static String SIGNMETHOD ="01";
	
	public static String PARAM_ENCODING ="encoding";
	
	/********************各种URL，使用测试地址请全部切换成带test的测试地址******************************/
//	public static String FRONTTRANSURL = "https://gateway.95516.com/gateway/api/frontTransReq.do";//前端交易地址
//	
//	public static String BACKTRANSURL = "https://gateway.95516.com/gateway/api/backTransReq.do";//后端交易地址
//	
//	public static String SINGLEQUERYURL = "https://gateway.95516.com/gateway/api/queryTrans.do";//查询交易地址
//	
//	public static String BATCHTRANSURL = "https://gateway.95516.com/gateway/api/batchTrans.do";//批量交易地址
//	
//	public static String appTransUrl = "https://gateway.95516.com/gateway/api/appTransReq.do";//app交易地址
//	
//	public static String cardTransUrl = "https://gateway.95516.com/gateway/api/cardTransReq.do";//卡交易地址
	
//	public static String fileTransUrl = "https://filedownload.95516.com";//文件下载
	
	//这些是测试的地址
	public static String FRONTTRANSURL = "https://gateway.test.95516.com/gateway/api/frontTransReq.do";//前端交易地址
	
	public static String BACKTRANSURL = "https://gateway.test.95516.com/gateway/api/backTransReq.do";//后端交易地址
	
	public static String SINGLEQUERYURL = "https://gateway.test.95516.com/gateway/api/queryTrans.do";//查询交易地址
	
	public static String BATCHTRANSURL = "https://gateway.test.95516.com/gateway/api/batchTrans.do";//批量交易地址
	
	public static String appTransUrl = "https://gateway.test.95516.com/gateway/api/appTransReq.do";//app交易地址
	
	public static String cardTransUrl = "https://gateway.test.95516.com/gateway/api/cardTransReq.do";//卡交易地址
	
	public static String fileTransUrl = "https://filedownload.test.95516.com/";//卡交易地址
	
	/********************	txnSubType交易子类******************************/
	//交易子类 06：二维码消费
	public static String TXNSUBTYPE_06 ="06"; 
	//00:
	public static String TXNSUBTYPE_00 ="00"; 
	//01:
	public static String TXNSUBTYPE_01 ="01"; 
	//02:
	public static String TXNSUBTYPE_02 ="02"; 
	
	/********************	txnType	交易类型******************************/
	//交易类型 01:消费类
	public static String TXNTYPE_01 ="01"; 
	//00 查询类
	public static String TXNTYPE_00 ="00"; 
	//04退货类
	public static String TXNTYPE_04 ="04";
	//76账单下载
	public static String TXNTYPE_76 ="76";	
	//99冲正类
	public static String TXNTYPE_99 ="99";
	
	
	/********************	bizType	产品类型******************************/
	public static String BIZTYPE_000000 ="000000"; 
	public static String BIZTYPE_000201 ="000201"; 
	
	/********************	channelType	渠道类型	******************************/
	//渠道类型07PC
	public static String CHANNELTYPE_07 ="07";
	//渠道类型 08手机
	public static String CHANNELTYPE_08 ="08"; 
	
	/********************	currencyCode交易币种	******************************/
	//渠道类型 156=人民币
	public static String CURRENCYCODE_156 ="156"; 
	
	/********************	订单查询应答码	******************************/
	
	final public static String TRANSSTATUS_00 ="00"; 
	final public static String TRANSSTATUS_03 ="03"; 
	final public static String TRANSSTATUS_04 ="04"; 
	final public static String TRANSSTATUS_05 ="05"; 
	
	
	/********************	二维码消费应答码	******************************/
	final public static String QRCODECONSUMEPASSIVE_88 ="88"; 
	final public static String QRCODECONSUMEPASSIVE_01 ="01"; 
	final public static String QRCODECONSUMEPASSIVE_33 ="33"; 
}

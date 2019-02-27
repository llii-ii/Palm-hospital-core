package com.kasite.client.hospay.module.bill.client.fjqzaeykyy;

import com.coreframework.util.ArithUtil;
import com.kasite.client.hospay.module.bill.client.init.HisService;
import com.kasite.client.hospay.module.bill.client.stub.HisClient;
import com.kasite.client.hospay.module.bill.client.stub.SoapInvocationClient;
import com.kasite.client.hospay.module.bill.entity.bill.bo.HisBill;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author cc
 * TODO 福建省泉州市爱尔眼科医院
 */
@Component
public class HisService_FJQZAEYKYY implements HisService{

	private final static Logger logger = LoggerFactory.getLogger(HisService_FJQZAEYKYY.class);

	@Autowired
	HisClient hisClient;

	@Autowired
	SoapInvocationClient soapInvocationClient;



	private static String  hisHeader="";
	/**
	 * 封装通用的头部
	 * @return
	 */
	private static String   headerParam(){
		try {
			hisHeader="<TerminalNo>wx123456</TerminalNo><SysName>健康之路</SysName><Opercode>微信</Opercode><OperTime>" + DateOper.getNow("yyyy-MM-dd hh:mm:ss") + "</OperTime>";
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return hisHeader;

	}
	/**
	 * 返回截取位置
	 * @param indexString
	 * @param result
	 * @return
	 * @throws Exception
	 */
	private String result1(String indexString, String result) throws Exception{
		// 获取第一次截取位置
		String  indexsizeString="<"+indexString+">";
		int index = result.indexOf(indexsizeString);
		//获取最后一次截取位置
		int lastindex=result.lastIndexOf("</"+indexString+">");
		//截取返回参数
		String  resultString=result.substring(index+indexsizeString.length(), lastindex);
		return   resultString;
	}
	/**
	 * 查询HIS接口获取账单列表(每日)
	 * 金额计算使用此函数(ArithUtil.mul(XMLUtil.getString(exams.get(i), "PayMoney",false),"100"))
	 * @return
	 * TODO startDate与endDate格式也许无法满足HIS接口的标准,可使用CommonUtils.dateChange("date","format")进行转换
	 * TODO 当前时间格式是yyyy-MM-dd HH:mm:ss
	 */
	@Override
	public List<HisBill> queryHisOrderBillListSoap(String startDate, String endDate) throws Exception {

		logger.info("开始拼接调用HIS账单接口的入参!!!");

		/**
		 * TODO 以下的入参都按照实施的实际情况进行修改，无需按照当前的标准
		 */

		/**
		 * 这一块是调用HIS的入参
		 */
		String param = "<Request><BeginDate>"+startDate.replaceAll("-", "").split(" ")[0]+"</BeginDate><EndDate>"+ endDate.replaceAll("-", "").split(" ")[0]+"</EndDate>"+headerParam()+"</Request>";
		/** 用于接口识别，可使用soapUi进行获取,如果无则传空字符串*/
		String soapAction = "";

		/** 用于接口方法调用，可使用soapUi进行获取，请将参数填写完整后直接调用soapInvocation（）即可，请勿修改soapInvocation（）方法*/
		StringBuilder sb = new StringBuilder("");
		sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">");
		sb.append("<soapenv:Header/>");
		sb.append("<soapenv:Body>");
		sb.append("<tem:QueryHisOrderBillList>");
		sb.append("<!--Optional:-->");
		sb.append("<tem:requestXML>");
		sb.append("<![CDATA["+param+"]]>");
		sb.append("</tem:requestXML>");
		sb.append("</tem:QueryHisOrderBillList>");
		sb.append("</soapenv:Body>");
		sb.append("</soapenv:Envelope>");
		String soapParam = sb.toString();
		logger.info("调用HIS账单接口的入参拼接完成,SoapAction:{}\r\n,SoapParam:{}", soapAction, soapParam);

		/* 调用HIS下载账单接口*/
		String result = soapInvocationClient.soapInvocation(soapAction, soapParam);
		// 获取第一次截取位置
		String  indexsizeString="QueryHisOrderBillListResult";
		//截取返回参数
        String  resultString= null;
        try {
            resultString = result1(indexsizeString,result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info(result);
        logger.info(resultString);
		List<HisBill> list = new ArrayList<>();
		try {
			HisBill hisBill;
			Document xml = XMLUtil.parseXml(resultString);
			Element root = xml.getRootElement();
			List<Element> exams = root.elements("Item");
			logger.info("总记录数:{}", exams.size());
			for (int i = 0; i < exams.size(); i++) {
				hisBill = new HisBill();
				hisBill.setOrderId(XMLUtil.getString(exams.get(i), "TraBatchNo", false));
				hisBill.setRefundOrderId(" ");
				hisBill.setMerchOrderNo(XMLUtil.getString(exams.get(i), "SerialNo", false));
				hisBill.setHisOrderId(XMLUtil.getString(exams.get(i), "HisOrdNum", false));
				hisBill.setPayMoney(ArithUtil.mul(XMLUtil.getString(exams.get(i), "Amount", false),"100"));
				hisBill.setRefundMoney("0");
				hisBill.setTotalMoney(ArithUtil.mul(XMLUtil.getString(exams.get(i), "Amount", false),"100"));
				hisBill.setPriceName(XMLUtil.getString(exams.get(i), "PayLX", false));
				hisBill.setOrderMemo(XMLUtil.getString(exams.get(i), "PayLX", false));
				hisBill.setHisOrderType(XMLUtil.getString(exams.get(i), "CheckType", false));
				hisBill.setHisBizState("1");
//				hisBill.setChannelId(XMLUtil.getString(exams.get(i), "ChannelId", false));
				/** 当前对账版本此字段置空，如后期上线统一支付平台此字段必须赋值，可通过订单号去订单表里进行查询*/
				hisBill.setChannelName("");
				// 转换String的时间为TimeStamp类型
				hisBill.setTransDate(Timestamp.valueOf(XMLUtil.getString(exams.get(i), "Date", false)));
				list.add(hisBill);
			}
		} catch (AbsHosException e) {
			e.printStackTrace();
			logger.error("XML解析异常:{}", e.getMessage());
		}
		return list;
	}

	@Override
	public List<HisBill> queryHisOrderBillListHL7(String startDate, String endDate) {
		return null;
	}

	@Override
	public List<HisBill> queryHisOrderState(String orderId, String startDate, String endDate) {
		return null;
	}
}

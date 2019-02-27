package com.kasite.client.hospay.module.bill.client.sc_garmyy;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coreframework.util.ArithUtil;
import com.kasite.client.hospay.module.bill.client.dzszxyy.HisService_DZSZXYY;
import com.kasite.client.hospay.module.bill.client.init.HisService;
import com.kasite.client.hospay.module.bill.client.stub.HisClient;
import com.kasite.client.hospay.module.bill.client.stub.HisClientHL7;
import com.kasite.client.hospay.module.bill.client.stub.SoapInvocationClient;
import com.kasite.client.hospay.module.bill.entity.bill.bo.HisBill;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.httpclient.http.StringUtils;
import com.yihu.hos.exception.AbsHosException;

/**
 * <p>Title : HisService_sc_garmyy</p>
 * <p>Description : 四川大学华西广安医院</p>
 * <p>DevelopTools : Eclipse_x64_v4.7.1</p>
 * <p>DevelopSystem : windows7</p>
 * <p>Company : com.kst</p>
 * @author : HongHuaYu
 * @date : 2018年12月27日 下午4:46:53
 * @version : 1.0.0
 */
@Component
public class HisService_sc_garmyy implements HisService{


	private final static Logger logger = LoggerFactory.getLogger(HisService_sc_garmyy.class);

	@Autowired
	HisClient hisClient;

	@Autowired
	SoapInvocationClient soapInvocationClient;

	@Autowired
	HisClientHL7 hisClientHL7;

	/**
	 * 查询HIS接口获取账单列表(每日)
	 * 金额计算使用此函数(ArithUtil.mul(XMLUtil.getString(exams.get(i), "PayMoney",false),"100"))
	 * @return
	 * TODO startDate与endDate格式也许无法满足HIS接口的标准,可使用CommonUtils.dateChange("date","format")进行转换
	 * TODO 当前时间格式是yyyy-MM-dd HH:mm:ss
	 */
	@Override
	public List<HisBill> queryHisOrderBillListSoap(String startDate, String endDate) {

		logger.info("开始拼接调用HIS账单接口的入参!!!");

		/**
		 * TODO 以下的入参都按照实施的实际情况进行修改，无需按照当前的标准
		 */

		/**
		 * 这一块是调用HIS的入参
		 */
		String param = "<Req>" + "<TransactionCode>QueryHisOrderBillList</TransactionCode>" + "<Data>"
				+ "<Page>"
				+ "<PIndex>0</PIndex>"
				+ "<PSize>100000</PSize>"
				+ "</Page>"
				+ "<OrderId></OrderId>" 
				+ "<RefundOrderId></RefundOrderId>" 
				+ "<ChanncelOrderId></ChanncelOrderId >"
				+ "<HisOrderId>"
				+ "</HisOrderId>" 
				+ "<BeginDate>" + startDate + "</BeginDate>" 
				+ "<EndDate>" + endDate
				+ "</EndDate>" 
				+ "<HisOrderType></HisOrderType>" 
				+ "</Data>" 
				+ "</Req>";
		/** 用于接口识别，可使用soapUi进行获取,如果无则传空字符串*/
		String soapAction = "http://tempuri.org/QueryHisOrderBillList";

		/** 用于接口方法调用，可使用soapUi进行获取，请将参数填写完整后直接调用soapInvocation（）即可，请勿修改soapInvocation（）方法*/
		StringBuilder sb = new StringBuilder("");
		sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" ")
				.append("xmlns:tem=\"http://tempuri.org/\" ")
				.append("><soapenv:Header/><soapenv:Body>")
				.append("<tem:QueryHisOrderBillList>").append("<tem:info>").append("<![CDATA[" + param + "]]>")
				.append("</tem:info> </tem:QueryHisOrderBillList></soapenv:Body></soapenv:Envelope>");
		String soapParam = sb.toString();

		logger.info("调用HIS账单接口的入参拼接完成,SoapAction:{}\r\n,SoapParam:{}", soapAction, soapParam);
        List<HisBill> list = new ArrayList<>();
		/** 调用HIS下载账单接口*/
		try {
		String result = soapInvocationClient.soapInvocation(soapAction, soapParam);
		System.out.println(result);
		
		if (StringUtils.isNotBlank(result) && result.contains("<?xml version=\"1.0\"?>")) {
			int begin = result.indexOf("<?xml version=\"1.0\"?>");
			int end = result.indexOf("</QueryHisOrderBillListResult>");
			result = result.substring(begin + 21, end);
		}


			HisBill hisBill;
			Document xml = XMLUtil.parseXml(result);
			Element root = xml.getRootElement();
//			List<Element> exams = root.selectNodes("//Data//Data");
			List<Element> exams = root.elements("Data");
			logger.info("总记录数:{}", exams.size());
			for (int i = 0; i < exams.size(); i++) {
				hisBill = new HisBill();
				hisBill.setOrderId(XMLUtil.getString(exams.get(i), "OrderId", false));
				hisBill.setRefundOrderId(XMLUtil.getString(exams.get(i), "RefundOrderId", false));
				hisBill.setMerchOrderNo(XMLUtil.getString(exams.get(i), "MerchOrderNo", false));
				hisBill.setHisOrderId(XMLUtil.getString(exams.get(i), "HisOrderId", false));
				hisBill.setPayMoney(removeTrim(XMLUtil.getString(exams.get(i), "PayMoney", false)));
				hisBill.setRefundMoney(com.kasite.core.common.util.StringUtil.isEmpty(XMLUtil.getString(exams.get(i), "RefundMoney", false))?"":removeTrim(XMLUtil.getString(exams.get(i), "RefundMoney", false)));
				hisBill.setTotalMoney(removeTrim(XMLUtil.getString(exams.get(i), "TotalMoney", false)));
				hisBill.setPriceName(XMLUtil.getString(exams.get(i), "PriceName", false));
				hisBill.setOrderMemo(XMLUtil.getString(exams.get(i), "OrderMemo", false));
				hisBill.setHisOrderType(XMLUtil.getString(exams.get(i), "HisOrderType", false));
				hisBill.setHisBizState(XMLUtil.getString(exams.get(i), "HisBizState", false));
				hisBill.setChannelId(XMLUtil.getString(exams.get(i), "ChannelId", false));
				/** 当前对账版本此字段置空，如后期上线统一支付平台此字段必须赋值，可通过订单号去订单表里进行查询*/
				hisBill.setChannelName("");
				// 转换String的时间为TimeStamp类型
				hisBill.setTransDate(Timestamp.valueOf(XMLUtil.getString(exams.get(i), "TransDate", false)));
				list.add(hisBill);
			}
		} catch (AbsHosException e) {
			e.printStackTrace();
			logger.error("XML解析异常:{}", e.getMessage());
		} catch (Exception e) {
            e.printStackTrace();
        }
        return list;
	}

	@Override
	public List<HisBill> queryHisOrderBillListHL7(String startDate, String endDate) {
		return null;
	}

	public static String removeTrim(String str){
	    if(str.indexOf(".") > 0){
	      str = str.replaceAll("0+?$", "");//去掉多余的0
	      str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
	    }
	    int money = Integer.parseInt(str);
	    return money > 0 ? str:-money + "";
	  }

	@Override
	public List<HisBill> queryHisOrderState(String orderId, String startDate, String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

}

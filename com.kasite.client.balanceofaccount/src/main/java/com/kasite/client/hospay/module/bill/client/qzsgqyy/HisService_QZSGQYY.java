package com.kasite.client.hospay.module.bill.client.qzsgqyy;

import static com.kasite.client.hospay.module.bill.client.stub.HisClientHL7.getNowDateStr;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kasite.client.hospay.module.bill.client.init.HisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coreframework.util.ArithUtil;
import com.kasite.client.hospay.common.constant.Constant;
import com.kasite.client.hospay.module.bill.client.dzszxyy.HisService_DZSZXYY;
import com.kasite.client.hospay.module.bill.client.stub.HisClient;
import com.kasite.client.hospay.module.bill.client.stub.HisClientHL7;
import com.kasite.client.hospay.module.bill.client.stub.SoapInvocationClient;
import com.kasite.client.hospay.module.bill.entity.bill.bo.HisBill;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.DateOper;

/**
 *
 * @author max
 * TODO 泉州市光前医院HIS账单接口实现类
 */
@Component
public class HisService_QZSGQYY implements HisService{
	private final static Logger logger = LoggerFactory.getLogger(HisService_QZSGQYY.class);
	@Autowired
	HisClient hisClient;

	@Autowired
	SoapInvocationClient soapInvocationClient;

	@Autowired
	HisClientHL7 hisClientHL7;

	@Override
	public List<HisBill> queryHisOrderBillListSoap(String startDate, String endDate) {
		return null;
	}

	/**
	 * 获取HIS账单
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Override
	public List<HisBill> queryHisOrderBillListHL7(String startDate, String endDate) throws Exception {

		List<String> reqResultMap = new ArrayList<String>();
		Map<String, String> paramMap = new HashMap<>(16);
		paramMap.clear();
		/** 处理入参*/
		paramMap.put("endDate",endDate.split(" ")[0].replace("-", "") );

		paramMap.put("startDate", startDate.split(" ")[0].replace("-", ""));

		paramMap.put("type", "1");
		String nowDateStr = getNowDateStr();
		String reqParam = "MSH|^~\\&|APP|健康之路|HIS|接收设备名称|" + nowDateStr + "||QBP^Q13^QBP_Q13|QRY_Reconciliation-"
			+ nowDateStr.replace(".", "") + "|P|2.7\n"
			+ "QPD|Q13^QRY_Reconciliation^HL7|QR0001|{startDate}|{endDate}|{type}|\n"
			+ "RDF|12|单据号^NM^50~姓名^ST^50~缴费金额^NM^50~支付方式^ST^20~出票单位^ST^50~出票开户银行^ST^50~票据流水号^NM^50~操作日期^DMT^50~操作时间^DMT^50~操作员^ST^50~病人余额^NM^50~机构名称^ST^50\n"
			+ "RCP|I|页数^PG&每页记录数";

		/** 参数格式化*/
		String reqParam1 = CommonUtil.formateReqParam(reqParam, paramMap);
		reqResultMap.add(reqParam1);
		paramMap.put("type", "3");
		reqParam1 =CommonUtil.formateReqParam(reqParam, paramMap);
		reqResultMap.add(reqParam1);
		logger.info("调用HIS账单接口入参:{}", "门诊："+reqResultMap.get(0)+"住院："+reqResultMap.get(1));

		/** 调用多次HIS接口统一由进行封装调用*/
		List<List<String[]>> allBills = new ArrayList<>();
		for (int i = 0; i < reqResultMap.size(); i++) {
			List<String[]> list = hisClientHL7.queryAllBills(reqResultMap.get(i));
			allBills.add(list);
		}

		logger.info("调用HIS账单接口结果集:{}", allBills.size());
		List<HisBill> list = new ArrayList<>();

		HisBill hisBill;
		//先循环结果集
		for (List<String[]> bill : allBills) {
			for (String[] allBill : bill) {
				hisBill = new HisBill();
				if (allBill.length >= 13) {
					hisBill.setOrderId(allBill[13]);
				}
				hisBill.setRefundOrderId("");
				hisBill.setMerchOrderNo(allBill[7]);
				hisBill.setHisOrderId(allBill[1]);
				hisBill.setTotalMoney(ArithUtil.mul(allBill[3], "100"));
				hisBill.setRefundMoney(ArithUtil.mul(allBill[3], "100"));
				hisBill.setPayMoney(ArithUtil.mul(allBill[3], "100"));
				if (allBill.length >= 13) {
					hisBill.setPriceName(allBill[12]);
					hisBill.setOrderMemo(allBill[12]);
				}
				hisBill.setHisOrderType(Constant.CHARGEORDERSTATE);
				hisBill.setHisBizState(Constant.CHARGEORDERSTATE);
				/** 这个需要根据项目的情况填写,目前默认微信*/
				hisBill.setChannelId(Constant.WXCHANNELID);
				/** 当前对账版本此字段置空，如后期上线统一支付平台此字段必须赋值，可通过订单号去订单表里进行查询*/
				hisBill.setChannelName("");
				try {
					hisBill.setTransDate(Timestamp.valueOf(DateOper.formatDate(allBill[8] + " " + allBill[9],
						"yyyyMMdd HH:mm:ss", "yyyy-MM-dd HH:mm:ss")));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				list.add(hisBill);
			}
		}
		return list;
	}

	@Override
	public List<HisBill> queryHisOrderState(String orderId, String startDate, String endDate) {
		return null;
	}

	/**
	 * 接口测试demo
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		// ************************HL7调用demo**************************
		String startDate = "20180928";
		String endDate = "20180928";
		String type = "1";
		HisService_QZSGQYY hisService1 = new HisService_QZSGQYY();
		System.out.println(hisService1.queryHisOrderBillListHL7(startDate, endDate));

	}







}

package com.kasite.client.pay.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.kasite.client.pay.bean.dbo.Bill;
import com.kasite.client.pay.dao.IBillMapper;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.resp.RespQueryHisBill;
import com.kasite.core.serviceinterface.module.pay.IBillService;
import com.kasite.core.serviceinterface.module.pay.dto.BillVo;
import com.kasite.core.serviceinterface.module.pay.req.ReqGetBill;
import com.kasite.core.serviceinterface.module.pay.resp.RespGetBill;
import com.yihu.hos.service.CommonService;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO
 */
@Service("bill.BillWs")
public class BillServiceImpl  extends CommonService implements IBillService{

	private final static Log log = LogFactory.getLog(BillServiceImpl.class);
	
	@Autowired
	IBillMapper billMapper;
	
	@Override
	public String QueryHisOrderBill(InterfaceMessage msg) throws Exception {
		return this.queryHisOrderBill(new CommonReq<ReqGetBill>(new ReqGetBill(msg))).toResult();
	}
	
	/**
	 * @param msg
	 * @return
	 * @throws Exception 
	 */
	@Override
	public String GetBill(InterfaceMessage msg) throws Exception {
		return this.getBill(new CommonReq<ReqGetBill>(new ReqGetBill(msg))).toResult();
	}

	@Override
	public CommonResp<RespGetBill> getBill(CommonReq<ReqGetBill> commReq) throws Exception {
		ReqGetBill req = commReq.getParam();
		if(req.getPage()!=null && req.getPage().getPSize()>0) {
			PageHelper.startPage(req.getPage().getPIndex(), req.getPage().getPSize());
		}
		List<Bill> list = billMapper.findBillList(req.getServiceId(), req.getBillDate());
		if(req.getPage()!=null && req.getPage().getPSize()>0) {
			req.getPage().initPCount(list);
		}
		
		List<RespGetBill> respList = new ArrayList<RespGetBill>();
		for (Bill bill : list) {
			RespGetBill resp = new RespGetBill();
			resp.setChannelId(bill.getChannelId());
			resp.setMerchOrderNo(bill.getMerchNo());
			resp.setOrderId(bill.getOrderId());
			resp.setOrderType(bill.getOrderType());
			resp.setRefundOrderId(bill.getRefundOrderId());
			resp.setRefundPrice(bill.getRefundPrice());
			resp.setTransactions(bill.getTransactions());
			if(bill.getTransDate()!=null) {
				resp.setTransDate(DateOper.formatDate(bill.getTransDate(), "yyyy-MM-dd HH:mm:ss"));
			}
			respList.add(resp);
		}
		return new CommonResp<RespGetBill>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList,req.getPage());
	}

	@Override
	public CommonResp<RespQueryHisBill> queryHisOrderBill(CommonReq<ReqGetBill> commReq) throws Exception {
		ReqGetBill req = commReq.getParam();
		Map<String, String> paramMap = new HashMap<String, String>(16);
		paramMap.put("beginTime", req.getBeginDate() + " 00:00");
		paramMap.put("endTime", DateOper.addDate(req.getBeginDate(), 1) + " 00:00");
		paramMap.put("type", "0");
		CommonResp<RespQueryHisBill> resp = HandlerBuilder.get().getCallHisService(commReq.getParam().getAuthInfo()).queryHisOrderBillList(req.getMsg(), paramMap);
		return resp;
	}
	
	@Override
	public CommonResp<RespMap> queryAllBill(CommonReq<ReqGetBill> commReq) throws Exception {
		ReqGetBill req = commReq.getParam();
		String startDate = req.getStartDate();
		String endDate = req.getEndDate();
		Map<String, BillVo> map = new LinkedHashMap<String, BillVo>();
		Map<String, BillVo> hisErrorMap = new LinkedHashMap<String, BillVo>();
		
		List<BillVo> payHisBillList = billMapper.findPayHisBillList(startDate, endDate);
		List<BillVo> refundHisBillList = billMapper.findRefundHisBillList(startDate, endDate);
		List<BillVo> payBillList = billMapper.findPayBillList(startDate, endDate);
		List<BillVo> refundBillList = billMapper.findRefundBillList(startDate, endDate);
		
		for (BillVo hisBillVo : refundHisBillList) {
			map.put(hisBillVo.getRefundOrderId(), hisBillVo);
		}
		for (BillVo billVo : refundBillList) {
			map.put(billVo.getRefundOrderId(), billVo);
		}
		for (BillVo hisBillVo : payHisBillList) {
			/*if(map != null && map.containsKey(hisBillVo.getOrderId())) {
				BillVo hisBillVoThis = map.get(hisBillVo.getOrderId());
				//针对His对同一笔支付订单重复充值的账单
				if(hisBillVoThis != null && KstHosConstant.BILL_ORDER_TYPE_1.equals(hisBillVoThis.getOrderType())
						&& !hisBillVo.getHisOrderId().equals(hisBillVoThis.getHisOrderId())) {
					hisErrorMap.put(hisBillVoThis.getHisOrderId()+"_"+hisBillVoThis.getOrderId(), hisBillVo);
				}
			}*/
			map.put(hisBillVo.getOrderId(), hisBillVo);
		}
		for (BillVo billVo : payBillList) {
			map.put(billVo.getOrderId(), billVo);
		}
		
		RespMap respMap = new RespMap();
		respMap.add(ApiKey.BillRFPro.BillMap, map);
		respMap.add(ApiKey.BillRFPro.HisBillErrorMap, hisErrorMap);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respMap);
	}

}

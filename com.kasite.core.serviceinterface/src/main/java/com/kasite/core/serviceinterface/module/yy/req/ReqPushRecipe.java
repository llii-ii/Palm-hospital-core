/**
 * 
 */
package com.kasite.core.serviceinterface.module.yy.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.handler.IPushService;
import com.kasite.core.serviceinterface.module.his.resp.HisRecipeClinicList;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;


/**
 * <p>Title : ReqPushRecipe</p>
 * <p>Description : 医院推送处方开药信息入参</p>
 * <p>DevelopTools : Eclipse_x64_v4.7.1</p>
 * <p>DevelopSystem : windows7</p>
 * <p>Company : com.kst</p>
 * @author : HongHuaYu
 * @date : 2018年12月6日 下午5:33:03
 * @version : 1.0.0
 */
public class ReqPushRecipe extends AbsReq {
	
	private CommonResp<HisRecipeClinicList> pushRecipe;
	
	public ReqPushRecipe(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		IPushService service = HandlerBuilder.get().getCallHisService(getAuthInfo(), IPushService.class);
		if(null != service) {
			this.pushRecipe = service.parseRecipe(msg);
		}else {
			Element ser = root.element(KstHosConstant.DATA);
			if(ser==null){
				throw new ParamException("传入参数中[Data]节点不能为空。");
			}
			HisRecipeClinicList req = new HisRecipeClinicList();
			String idCard = XMLUtil.getString(ser, "IdCard", false);	
			String hisMemberId = XMLUtil.getString(ser, "HisMemberId", false);	
			String cardNo = XMLUtil.getString(ser, "CardNo", false);	
			String receiptNo = XMLUtil.getString(ser, "ReceiptNo", true);	
			String receiptName = XMLUtil.getString(ser, "ReceiptName", true);
			String receiptTime = XMLUtil.getString(ser, "ReceiptTime", false);
			Integer totalPrice = XMLUtil.getInt(ser, "TotalPrice", true);
			String receiptDeptName = XMLUtil.getString(ser, "ReceiptDeptName", false);		
			String receiptDoctorName = XMLUtil.getString(ser, "ReceiptDoctorName", false);	
			String serviceId = XMLUtil.getString(ser, "ServiceId", true);	
			if(StringUtil.isBlank(idCard) && 
					StringUtil.isBlank(hisMemberId) &&
					StringUtil.isBlank(cardNo) 
					) {
				throw new RRException("参数：idCard HisMemberId  CardNo 不能都为空 ");
			}
			
			if(StringUtil.isBlank(receiptNo) && 
					StringUtil.isBlank(totalPrice) 
					) {
				throw new RRException("参数不能都为空：ReceiptNo   TotalPrice ");
			}
			req.setCardNo(cardNo);
			req.setIdCard(idCard);
			req.setHisMemberId(hisMemberId);
			req.setReceiptDeptName(receiptDeptName);
			req.setReceiptDoctorName(receiptDoctorName);
			req.setReceiptName(receiptName);
			req.setReceiptNo(receiptNo);
			req.setReceiptTime(receiptTime);
			req.setTotalPrice(totalPrice);
			req.setServiceId(serviceId);
			pushRecipe = new CommonResp<>(msg,null,RetCode.Success.RET_10000, req);
		}
	}

	public CommonResp<HisRecipeClinicList> getPushRecipe() {
		return pushRecipe;
	}

	public void setPushRecipe(CommonResp<HisRecipeClinicList> pushRecipe) {
		this.pushRecipe = pushRecipe;
	}


	
	
}

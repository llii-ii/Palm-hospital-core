package com.kasite.client.order.service.handle;

import org.springframework.stereotype.Service;
import com.kasite.core.common.service.BusinessTypeEnum;

/**
 * @author linjf
 * 非药品结算支付
 */
@Service
public class PayNonMedicineBizExecuteHandler extends PayPrescBizExecuteHandler{


	/**
	 * @return
	 */
	@Override
	public BusinessTypeEnum accept() {
		return BusinessTypeEnum.ORDERTYPE_004;
	}

}

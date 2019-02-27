package com.kasite.client.order.service.handle;


import org.springframework.stereotype.Service;

import com.kasite.core.common.service.BusinessTypeEnum;
/**
 * @author linjf
 * 西药结算
 */
@Service
public class PayWesternMedicineBizExecuteHandler extends PayPrescBizExecuteHandler{


	/**
	 * @return
	 */
	@Override
	public BusinessTypeEnum accept() {
		return BusinessTypeEnum.ORDERTYPE_001;
	}

}

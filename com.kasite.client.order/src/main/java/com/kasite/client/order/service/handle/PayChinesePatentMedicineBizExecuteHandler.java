package com.kasite.client.order.service.handle;

import org.springframework.stereotype.Service;
import com.kasite.core.common.service.BusinessTypeEnum;

/**
 * @author linjf
 * 中成药结算
 */
@Service
public class PayChinesePatentMedicineBizExecuteHandler extends PayPrescBizExecuteHandler{


	/**
	 * @return
	 */
	@Override
	public BusinessTypeEnum accept() {
		return BusinessTypeEnum.ORDERTYPE_002;
	}

}

package com.kasite.client.order.service.handle;

import org.springframework.stereotype.Service;
import com.kasite.core.common.service.BusinessTypeEnum;

/**
 * @author linjf
 * 草药结算
 */
@Service
public class PayHerbalMedicineBizExecuteHandler extends PayPrescBizExecuteHandler{


	/**
	 * @return
	 */
	@Override
	public BusinessTypeEnum accept() {
		return BusinessTypeEnum.ORDERTYPE_003;
	}

}

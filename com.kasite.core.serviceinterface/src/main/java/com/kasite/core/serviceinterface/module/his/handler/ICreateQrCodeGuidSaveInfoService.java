package com.kasite.core.serviceinterface.module.his.handler;

import java.util.Map;

import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.service.ICallHis;
import com.kasite.core.common.util.StringUtil;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 创建二维码的时候需要查询的HIS的接口信息可以通过实现此接口
 * @author daiyanshui
 */
public interface ICreateQrCodeGuidSaveInfoService extends ICallHis{

	/**
	 * 校验接口返回值是否正确
	 * @param respMap
	 * @return
	 */
	default boolean checkCreateQrCodeGuidSaveInfoServiceResult(AuthInfoVo vo,RespMap respMap) {
		//必须返回字段的判断
		String cardNo = respMap.getString(ApiKey.HISCreateQRCode.cardNo);
		String hisMemberId = respMap.getString(ApiKey.HISCreateQRCode.hisMemberId);
		String memberName = respMap.getString(ApiKey.HISCreateQRCode.memberName);
		
		if(StringUtil.isBlank(cardNo)) {
			throw new RRException("卡号不能为空");
		}
		if(StringUtil.isBlank(hisMemberId)) {
			throw new RRException("HIS用户唯一ID不能为空");
		}
		if(StringUtil.isBlank(memberName)) {
			throw new RRException("用户姓名不能为空");
		}
		 
		 return true;
    }
	/**
	 * 通过二维码创建的时候查询his信息保存到信息点中
	 * @param msg
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> createQrCodeGuidSaveInfoService(InterfaceMessage msg,Map<String, String> paramMap)throws Exception;
	
}

package com.kasite.core.serviceinterface.module.basic.req;

import org.dom4j.Element;

import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf 2017年11月14日 17:25:08
 *
 * TODO 获取验证码请求封装对象
 */
public class ReqGetProvingCode extends AbsReq{
	
	private String mobile;

	public String getMobile() {
		return mobile;
	}
	
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public ReqGetProvingCode(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(null != msg && msg.getParamType() == 0) {
			JSONObject obj = JSONObject.parseObject(msg.getParam());
			JSONObject data = obj.getJSONObject("Req").getJSONObject("Data");
			if(null != data) {
				this.mobile = data.getString("Mobile");
			}
		}
		if(null != root) {
			Element ser = root.element(KstHosConstant.DATA);
			if(ser==null){
				throw new ParamException("传入参数中[Data]节点不能为空。");
			}
			this.mobile = XMLUtil.getString(ser, "Mobile", false);
		}
	}
	
}

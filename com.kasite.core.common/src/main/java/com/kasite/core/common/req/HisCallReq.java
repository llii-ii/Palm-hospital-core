package com.kasite.core.common.req;

import java.util.Map;

import com.yihu.wsgw.api.InterfaceMessage;

public class HisCallReq extends AbsReq{

	public static CommonReq<HisCallReq> get(InterfaceMessage msg,Map<String, String> map) {
		try {
			return new CommonReq<>(new HisCallReq(msg));
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonReq<>(null);
		}
	}
	public static CommonReq<HisCallReq> get(InterfaceMessage msg) {
		try {
			return new CommonReq<>(new HisCallReq(msg));
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonReq<>(null);
		}
	}
	public HisCallReq(InterfaceMessage msg) throws Exception {
		super(msg);
	}

}

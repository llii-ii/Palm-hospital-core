package com.kasite.client.business.module.sys.check;

import com.kasite.core.common.config.KasiteConfig;

public class Test {

	public static void main(String[] args) {
		CheckInterfaceUtil.get().add(CheckInterfaceUtil.CheckUrlType.http,"172.18.20.81:3306", new CheckInterfaceHandler() {
			@Override
			public void notify(InterfaceStatusVo vo) {
				KasiteConfig.print(vo.getUrl()+"_接口状态_"+vo.isStatus());
			}
		});
	}
}

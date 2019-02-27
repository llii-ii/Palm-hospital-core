package com.kasite.server.verification.module.ws;

import org.springframework.stereotype.Component;

import com.yihu.wsgw.api.InterfaceMessage;

@Component("bat.BatApi")
public class Test {

	public String test(InterfaceMessage msg) {
		System.out.println(msg.getParam());
		return "xxxxxx";
	}
}

package test.com.kasite.client.crawler.micro;

public class TestCallLog {

	public static void sendTest() {
		try {
			WsgwLogBus bus = WsgwLogBus.me();
			bus.setApi("a.b.c")//必须
			.setAuthInfoVo("sessionKey", "clientId", "sign", "clientVersion", "configKey")//必须
			.setCallType("callType")
			.setClassName("className")//必须
			.setClientIp("clientIp")
			.setIsSuccess(false)
			.setMethodName("methodName")//必须
			.setMills(100)//必须
			.setOrderId("orderId")
			.setOutType(1)//必须
			.setParam("测试中文乱码")//必须
			.setParamType(1)//必须
			.setReferer("referer")
			.setResult("result")//必须
			.setUniqueReqId("uniqueReqId")//必须
			.setUserAgent("userAgent")
			.setV("v").send();
		}catch (Exception e) {
			// TODO: 调用异常的时候请记录文本日志

		}
	}
	
	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 200; i++) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int j = 0; j < 2000; j++) {
						sendTest();
					}
				}
			});
			t.start();
		}
	}
}

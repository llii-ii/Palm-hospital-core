//package test.com.kasite.core.httpclient;
//
//import com.kasite.core.httpclient.http.HttpRequestBus;
//import com.kasite.core.httpclient.http.RequestType;
//
//import junit.framework.TestCase;
//
//public class TestHttp extends TestCase {
//
//	public static void testPost() throws Exception{
////		String url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQF87zwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyd3dQUE1vektic1QxOFR0SWhyY2QAAgT7nCxbAwQ8AAAA";
////		File file = new File("/Users/daiyanshui/Desktop/KASITE/server/"+UUID.randomUUID().toString()+".jpg");
////		//下载文件demo
////		HttpRequestBus.create(url, RequestType.get).downLoad(file);//send();
////		
////		//普通http请求demo
////		HttpRequstBusSender sender = HttpRequestBus.create(url, RequestType.post)
////				.addHttpParam("", "");//往httpbody 里加参数传递 param
////		sender.setHeaderHttpParam("", "");//往http请求头里 加内容
//		
////		String param = "<soapenvaddurl>http://172.18.20.83:8080/his/QUERYCARD</soapenvaddurl><Req><TransactionCode></TransactionCode><Data><CardType>1</CardType><CardNo>2235565</CardNo><CardTypeName>就诊卡</CardTypeName><MemberName>测试01</MemberName><Mobile>15280078190</Mobile><IdCardNo>350124199001172857</IdCardNo></Data></Req>";
////		HttpRequstBusSender sender = HttpRequestBus.create("", RequestType.post)
////		.contentType("application/xml").setHeaderHttpParam("Accept-Encoding", "")
////				.setParam(param);//往httpbody 里加参数传递 param
////		
////		String reslut = sender.send().getResult();
////		System.out.println(reslut);
//		
//		
//		/*String url = "https://open.weixin.qq.com/connect/qrconnect?appid=wxc89392b731d6f359&redirect_uri=https://wechatcenter.kasitesoft.com/login.html&response_type=code&scope=snsapi_login&state=STATE";
//		String response = "";//HttpRequestBus.create(url, RequestType.get).send().getResult();
//		String url = "https://open.weixin.qq.com/connect/qrconnect?appid=wxc89392b731d6f359&redirect_uri=https://wechatcenter.kasitesoft.com/login.html&response_type=code&scope=snsapi_login&state=STATE";
//		String response = HttpRequestBus.create(url, RequestType.get).send().getResult();
//		System.out.println(response);
//		String strS = response.substring(response.indexOf("uuid=")+5, response.length());
//		String uuid = (strS.substring(0, strS.indexOf("+")-1));
//		url="https://long.open.weixin.qq.com/connect/l/qrconnect?uuid="+uuid+"&_="+System.currentTimeMillis();
//		response = HttpRequestBus.create(url, RequestType.get).send().getResult();
//		System.out.println(response);//001xptKT0bOuMZ1dc0MT0TpkKT0xptKl*/
////		System.out.println(response);//001xptKT0bOuMZ1dc0MT0TpkKT0xptKl
//		
//	}
//}

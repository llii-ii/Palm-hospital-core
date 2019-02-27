//package test.com.kasite.core.common;
//
//import com.kasite.core.common.sys.KBus;
//import com.kasite.core.httpclient.http.SoapResponseVo;
//
//import junit.framework.TestCase;
//
//public class KBusTester extends TestCase {
//	public KBusTester(String testName) {
//		super(testName);
//		try {
//			InitTestInfo.init();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
//	
//	public void testKbus() throws Exception {
//		for (int i = 0; i < 1; i++) {
//			SoapResponseVo ret = KBus.create("basic.test.testA", "param").sendhttp("basic");
//			System.out.println(ret.getResult());
//		}
//	}
//}

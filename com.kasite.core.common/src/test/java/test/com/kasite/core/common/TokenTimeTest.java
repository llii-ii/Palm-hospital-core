//package test.com.kasite.core.common;
//
//import java.text.ParseException;
//
//import com.kasite.core.common.sys.KBus;
//import com.kasite.core.common.util.DateOper;
//import com.yihu.wsgw.bus.KBusSender;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//public class TokenTimeTest extends TestCase {
//	public TokenTimeTest(String testName) {
//		super(testName);
//		InitTestInfo.init();
//	}
//
//	public static Test suite() {
//		return new TestSuite(TokenTimeTest.class);
//	}
//
//	/**
//	 * 测试获取Token
//	 * @throws Exception 
//	 */
//	public void testGetToken() throws Exception {
//		
//	
//		
//	}
//	
//	public void testRpc() {
//		
//		
//	}
//	
//	public void testTokenExp() throws InterruptedException, ParseException {
//		String create_time = "2018-05-14 15:00:02";
//		String exp_ime = "2018-05-14 15:00:01";
//		System.out.println(DateOper.addMinute(create_time, 120, "yyyy-MM-dd HH:mm:ss"));
//		System.out.println(create_time.compareTo(exp_ime)); //A > B  then >0  A = B then 0 A < B then < 0
//		System.out.println(exp_ime.compareTo(create_time));
//		
//	}
//}

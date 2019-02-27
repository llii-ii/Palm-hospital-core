package test.com.kasite.server.verification;

import java.text.ParseException;

import com.kasite.core.common.util.DateOper;

import junit.framework.TestCase;

public class TestAccess_Token_InvalidTime extends TestCase {

	
	public void testTime() throws Exception {
		
		String invalidTime = "2018-05-15 16:50:00";
		String nowTime = DateOper.getNow("yyyy-MM-dd HH:mm:ss"); 
		
		System.out.println(invalidTime.compareTo(nowTime) );
		
	}
}

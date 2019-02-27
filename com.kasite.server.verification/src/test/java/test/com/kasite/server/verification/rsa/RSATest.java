package test.com.kasite.server.verification.rsa;

import java.security.Key;
import java.util.HashMap;
import java.util.UUID;

import com.kasite.core.common.sys.verification.RSA;

import junit.framework.TestCase;

public class RSATest extends TestCase {
	
	public void testRsa() throws Exception {
		HashMap<String, Key> generateKeys = RSA.generateKeys();
		Key k1 = generateKeys.get(RSA.PUBLIC_KEY);
		Key k2 = generateKeys.get(RSA.PRIVATE_KEY);
		String publicKey = RSA.encodeKey(k1);
		String privateKey = RSA.encodeKey(k2);
		//appId = KASTIE-CLIENT-BALANCEOFACCOUNT.514  appSecret = 2LdBF9viPghharPoD32Y  泉州儿童医院对账系统V1.0  
		//publicKey = MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtlR82WKjACp0oJfX2biFJOaZcM86GWt4/CoQbv5bRkzSZquEQFHYMAonfxk+bq0UlC/2O5BXT1NYIcYS4vFjXN/B4TWw7qe5UDj0vIO9XCeoA68X9k80lU9d+rG7qvBQVJnmYJnkbN9IDhFnZM9fvWqKiKRx78BQmeC+PQ/3+eqESMmeOkJv+3OBJbDQ9hca9ynefucYGrN+eDrI5aM9lmMBUd2ws7WEYdc6VainjgCEYvIWFrEeStnbtQzcHt1XouiFcv17PX4up5y1vsuQM6AA+QM4JBHTL/QnP8d8ZLj4CHITxOXB/gvBsOlg82KW0VEGwYan4lY+oZm8wNj8ewIDAQAB
		//centerUrl = https://verification.kasitesoft.com,http://verification2.kasitesoft.com
		
//		String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiPYdkSpHNdOgpANrBCzklqQSSnS8fpl8z4gI5qgGj5Iksd/QoMuO4NEP3PmCn9cmrJVYpp5wIYpyX3vnnhYy6dASciqXKEWHigToUHy/oBUVH6fpj+RjSYkaUHUu4AobgWtCzbQ8TPgxcf9P7F3Y70nzMQDpEApBUHdTDDGSiDpBwfjBZDmfpx5oOCSygpRJAVmk5ortTtU0HvzQmbFEv87KLOmZaRdrYeS0fvikv1i1frD+OshURTQlTl+P4/glycVDOCwRUryXdrDO6/lEkABPUU8R5AQ/JcHLYX+dDffiNnlqU/KYfniDGsLg80pLKVOIwpEz2dtuD55hO0ZkowIDAQAB";
//		String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCI9h2RKkc106CkA2sELOSWpBJKdLx+mXzPiAjmqAaPkiSx39Cgy47g0Q/c+YKf1yaslVimnnAhinJfe+eeFjLp0BJyKpcoRYeKBOhQfL+gFRUfp+mP5GNJiRpQdS7gChuBa0LNtDxM+DFx/0/sXdjvSfMxAOkQCkFQd1MMMZKIOkHB+MFkOZ+nHmg4JLKClEkBWaTmiu1O1TQe/NCZsUS/zsos6ZlpF2th5LR++KS/WLV+sP46yFRFNCVOX4/j+CXJxUM4LBFSvJd2sM7r+USQAE9RTxHkBD8lwcthf50N9+I2eWpT8ph+eIMawuDzSkspU4jCkTPZ224PnmE7RmSjAgMBAAECggEASFGUiStG0KA/SXtYjaraxMQ7uD7trPeE1NxivtBoLnUflSC2O8FgX0tk0eyZ0aa8kBKH7erpEHzXKZ32Th4wog8xPkFI8YXNLM6skJteX7viHIYAKO+SkaioUWWe68PKhErLymxqDsZa7XO/kvJ28tscY3q0lbikhkWLf3vJgM5eljELyQP2f9CirnVzT9tA1fl2EaJqfNa5yxi8gkkcUDZNG8/yEO40iLsCl4j3B/7/dnjqeZje7u3/5YlWUXwI5osihadggSEAD7nlar4c2SgdP1UI4YjaUfVK1BCYqPhE1fjk4lkcSPjel0AWdrKZlnjG31zne3cu2xWR4zp4wQKBgQDuUT8hSs6Z3bvew2XUl2aYQ4H3Qhl79/Go8gBY9gWq+x47VoeoIKr6rA+ThgabT5aZxMMoWYjS3BMMLSxLDKW/f3SDf/MTGa40MsvKlwk3FWV8kakeazIePdahlG9sNbuvzsa3jGORAtiky7cjaJKtfjFdDIPtDeGJ9oj7iu7Z4QKBgQCTH6XwqR1CwsFo4+z+Yp5nuU4yndzoyDELo6/dxdnRSUaEEMApVjYGtUJF72InQdy6wLXCvtTAbwxKHSE4K4czuOPDr7dHgGG+ldrYC0JklfJ9xF+TD1hmM8kgsRTrhfjwEUOyIHFpR+fOvjMmhJOdv9Rq5uLfp34nSFhF9/q3AwKBgGGnq03SIJzOTpi2yvh1XdYQ+6W9UUIKVN1c5SWPEr2Aj1/TUl1jnYyAixVxey2TTGwQQ8Q6BCYbhjacICVUtmqlKLJl8M4n3MmI8xc+Esmem+qSqZFMbcoUnumxMINP9WvSOH2oKniWiP3meFIrHMe5CTNO2JR/9qaY/oLM6JkhAoGAKhJRzKk/sdipoEoVeoVPERA/mV/lTTf6Nn7fisbKS/vtS1TS7r81geORiRg0dE90RH9ZOtCBXGolFIiR+FMSPaswjsQP4EWJLgwCYeeOW985Ude3EQHBYLCMYmnhNpzAHD6Dokt+wwGJ+2gOjoQ5BlJV9yABXr2x6J/ahl+sdWsCgYA+k+3beqLvXHuh858ZDQp5AWMWQCgmP7A9bhZfQmM+5JHeQRDw92H2WNOgpVGBpsE2Trr3IV0NRIc3YBuAmJDphEY4lVZWpgWf7tkuOErQW+ELkJ4prQYmqleqb1Ui6eLKGb5iof4hEEPuzKtee/AyksydgRZhF2TfGM1YqRK/bw==";
		System.out.println("公钥："+ publicKey);
		System.out.println("私钥："+ privateKey);
		
		String encryptStr = getEncryptStr(publicKey, UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10));
		System.out.println("加密后的密文："+ encryptStr);
		String data = RSA.decrypt(encryptStr, RSA.genPrivateKey(privateKey));
		System.out.println("解密后的明文："+ data);
		
		
		//服务端解密密文的时候 如果是返回的密文中有 +号 会被 http协议转成空格 所以会将 空格再转成 + 号
//		if(com.kasite.core.httpclient.http.StringUtils.isNotBlank(encrypt)) {
//			encrypt = encrypt.replace(" ","+");
//		}
//		logger.debug(encrypt);
		
	}
	//通过 公钥加密后获得私钥并向平台获取一个 token
	private static String getEncryptStr(String publicKey,String data) throws Exception {
		//加密后的密文
		String encryptStr = RSA.encrypt(data, RSA.genPublicKey(publicKey));
		return encryptStr;
	}
}

package test.com.kasite.core.common;

import com.kasite.core.common.util.rsa.KasiteRSAException;
import com.kasite.core.common.util.rsa.KasiteRSAUtil;

public class RSATest {

	public static void main(String[] args) throws KasiteRSAException {
		
		KasiteRSAUtil.getPrivateAndPublicKey();
	}
}

//package test.com.kasite.core.common;
//
//import com.kasite.core.common.config.KasiteConfig;
//import com.kasite.core.common.sys.KBus;
//import com.kasite.core.common.sys.verification.VerificationBuser;
//import com.kasite.core.httpclient.http.SoapResponseVo;
//
//public class InitTestInfo {
//
//	public static void init() throws Exception {
//		String testerPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhEJRYdtg9ipmUJ5d/JNBJdGctLquG2zTxTSA7qz7g+b/RXSsPYPj18GVtjMhsGymKEBm75raxAlw21A5qV+UGX/5uEmqn2hZCBfwzlScNofxs/uErXUOTrwLGRHdMVShuGH8k1KTx95M1b8RiUCZCOh3K0nk11slWiYdEDIFDdp4eDUP3YAmxHhuBiH80Nyf/D/EqhcK9POCD2HVRkP0O2SPTmZeBBtMROrAVUa8rzHAyOzG9tormbdNK5a4c/tjjISfvv7gqfCw9U7mlgEjo3tevEnJH3jVmgroPDVE8Pb9yuWyZ1ip5mYYeY7zzCG/XEYQHRedhUkfnOU7zmLozQIDAQAB";
////		KasiteConfig.setCenterServerUrl("127.0.0.1:8666");
//		KasiteConfig.setCenterServerUrl("http://verification2.kasitesoft.com");
//		KasiteConfig.setAppId("KASITE-CLIENT-BUSINESS.1024727");
//		KasiteConfig.setOrgCode("1024727");
//		KasiteConfig.localConfigPath("/Users/daiyanshui/Desktop/KasiteConfig");
//		KasiteConfig.setAppSecret("Xv1m6XyDtz1D0KIEUo4GI");
//		KasiteConfig.setPublicKey(testerPublicKey);
//		KasiteConfig.isConnectionKastieCenter("true");
//		try {
//			VerificationBuser.create().init();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		SoapResponseVo ret = KBus.create("guard.init.test", "<abc></abc><a></a><c></c>").sendhttp("KASITE-CLIENT-BUSINESS.1024727");
//		System.out.println(ret.getResult());
//		
//		
//	}
//	
//	public static void main(String[] args) throws Exception {
//		try {
//			init();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
////		String result = KBus.create("guard.test.test", "param").send().awaitReceive();
////		System.out.println(result);
//		System.exit(-1);
//	}
//}

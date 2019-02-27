//package test.com.kasite.core.common;
//
//import com.coreframework.util.IDSeed;
//import com.kasite.core.common.sys.verification.VerificationBuser;
//
//public class TestGetRouteThread {
//
//	public static void main(String[] args) {
//		InitTestInfo.init();		
//		Thread t = new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				while(true) {
//				 try {
//					long s = System.currentTimeMillis();
//					String routes = "";//VerificationBuser.create().getRouteByOrgCode("KASITE_APP",IDSeed.next());
//					long e = System.currentTimeMillis();
//					System.out.println(("times:"+(e-s)));
//					System.out.println(routes);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				}
//			}
//		});
//		t.start();
//		
//	}
//}

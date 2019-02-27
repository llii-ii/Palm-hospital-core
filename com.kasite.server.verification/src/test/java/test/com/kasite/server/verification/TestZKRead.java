//package test.com.kasite.server.verification;
//
//import com.kasite.server.verification.module.zk.KasiteServiceRouteParser;
//
//public class TestZKRead {
//
//	public static void main(String[] args) {
//		Thread t = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				while(true) {
//					try {
////						MappingRouteVo o = ZKRouteParser.getInstance("127.0.0.1:2181").queryMappingRouteVo("gh.yygh.test", "abc");
////						
////						if(null != o) {
////							System.out.println(o.getUrls());
////						}else {
////							System.out.println("null");
////						}
//						String r = KasiteServiceRouteParser.instance("127.0.0.1:2181", "KASITE").queryAllEncryptRoute();
//						if(null != r) {
//							System.out.println(r);
//						}else {
//							System.out.println("null");
//						}
//					} catch (Exception e1) {
//						e1.printStackTrace();
//					}
//					try {
//						Thread.sleep(5000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		});
//		t.start();
//	}
//
//}

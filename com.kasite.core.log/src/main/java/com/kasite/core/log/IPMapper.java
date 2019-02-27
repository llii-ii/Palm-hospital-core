package com.kasite.core.log;
//package com.kasite.client.business.module.logconsumer;
//
//
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//import net.sf.json.JSONObject;
//
//import com.yihu.zk.ZkClientFactory;
//import com.yihu.zk.ZkClientWrapper;
//
//public class IPMapper {
//	private static Map<String,String> map=new ConcurrentHashMap<String,String>();
//	
//	static{
//		Thread t=new Thread(new Runnable(){
//
//			@Override
//			public void run() {
//				for(;;){
//					try {
//						Map<String,String> temp=new ConcurrentHashMap<String,String>();
//						ZkClientWrapper zk = ZkClientFactory.getInstance().getZkClient(ConfigUtil.getInstance().getCenterServerUrl(),20000);
//						List<String> paths=zk.getChildren("/Sys_Guards/健康之路");
//						for(String path:paths){
//							Object o=zk.readData("/Sys_Guards/健康之路/"+path);
//							String str=(String)o;
//							JSONObject json=JSONObject.fromObject(str);
//							String sysId=json.optString("attachType");
//							if(sysId==null||sysId.isEmpty()){
//								continue;
//							}
//							temp.put(path, sysId);
//						}
//						map=temp;
//						System.out.println("reload sysID end");
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					try {
//						Thread.sleep(1000*60*10);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//				
//			}
//			
//		});
//		t.setDaemon(true);
//		t.start();
//	}
//	public static String getSysId(String ip){
//		return map.get(ip);
//	}
//	
//	public static void main(String[] args) throws InterruptedException {
//		for(int i=0;i<10;i++){
//			System.out.println(map);
//			Thread.sleep(3000);
//		}
//	}
//}

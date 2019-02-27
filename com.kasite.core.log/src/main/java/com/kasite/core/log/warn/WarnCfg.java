package com.kasite.core.log.warn;
//package com.kasite.client.business.module.logconsumer.warn;
//
///**
// * 
// */
//
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import com.coreframework.db.DB;
//import com.coreframework.db.Sql;
//import com.yihu.zk.AbstractZKListener;
//import com.yihu.zk.ZkClientFactory;
//
///**
// * @author zhangzz
// * @company yihu.com
// * 2015-4-22����4:22:36
// */
//public class WarnCfg {
//	
//	public static void init() throws Exception{
//		initWarnCfg();
//		timerUpdateApiWarnCfg();
//	}
//	/**
//	 * ��ʱ����APIԤ����ֵ����
//	 */
//	private static void timerUpdateApiWarnCfg(){
//		Timer timer = new Timer("loadwarncfg",true);
//		TimerTask task = new TimerTask(){
//			public void run() {
//				try {
//					loadWarnType();
//					loadWarnConfig();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				
//			}
//		};
//		long period=10;
////		try {
////			period = ConfigUtil.getInstance().getPeriod();
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
//		timer.scheduleAtFixedRate(task, period*60*1000, period*60*1000);
//		
//	}
//
//	private static void initWarnCfg() throws Exception {
//		String  zkService=ConfigUtil.getInstance().getCenterServerUrl().toString();
//		loadWarnType();
//		loadWarnConfig();
//	}
//	private static void loadWarnType(){
//		try{
//			Sql sql = DB.me().createSql(MySqlNameEnum.queryWarnType);
//			List<WarnTypeVo> list = DB.me().queryForBeanList(MyDatabaseEnum.OpenPlatform, sql, WarnTypeVo.class);
//			if(list != null){
//				WarnTypeCache.me().refresh(list);
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	/*
//	 * 10分钟重新加载一次
//	 */
//	private static void loadWarnConfig(){
//		
//	}
//}

//package com.yihu.hos.util;
//
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.coreframework.db.DatabaseEnum;
//import com.yihu.hos.exception.AbsHosException;
//import com.yihu.hos.exception.SysParamConfigException;
//import com.yihu.hos.service.CommonServiceRetCode;
//
///**
// * 从数据库加载  sys_config 系统表数据加载到这
// * 通过线程每分钟重新从数据库加载一次。
// * @author daiys
// * @date 2015-5-27
// */
//public class SysConfigManager {
//	
//	private static Map<String, Map<String, SysConfig>> maps;
//
//	/**
//	 * 实例化的对象
//	 */
//	private static volatile SysConfigManager instance;
//	
//	private static DatabaseEnum db;
//	
//	public static SysConfigManager getInstance(DatabaseEnum db) throws AbsHosException{
//		if(null == instance ){
//			instance = new SysConfigManager(db);
//		}
//		return instance;
//	}
//	public void desable(DatabaseEnum db){
//		try {
//			init(db);
//		} catch (SysParamConfigException e) {
//			e.printStackTrace();
//		}
//	}
//	private static Thread load = null;
//	private SysConfigManager(final DatabaseEnum db) throws AbsHosException{
//		if (null == load) {
//			RouteWatch runnable = new RouteWatch(new RouteChange() {
//				public void onChange() {
//					try {
//						init(db);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			});
//			Thread thread = new Thread(runnable);
//			thread.setName("SysConfig-Thread");
//			thread.start();
//			load = thread;
//		}
//		init(db);
//	}
//	private void init(DatabaseEnum db) throws SysParamConfigException {
//		try {
//			SysConfig config = new SysConfig();
//			config.setState(1);
//			List<SysConfig> list = CommonUtilsDao.queryConfig(db,config);
//			if(null == maps){
//				maps = new HashMap<String, Map<String,SysConfig>>();
//			}
//			for (SysConfig cfg : list) {
//				String hosid = cfg.getHosid();
//				Map<String,SysConfig> hosmap =maps.get(hosid);
//				if(null == hosmap){
//					hosmap = new HashMap<String, SysConfig>();
//				}
//				String nodetype = cfg.getApi()+cfg.getIsout();
//				hosmap.put(nodetype, cfg);
//				maps.put(hosid, hosmap);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new SysParamConfigException(CommonServiceRetCode.Common.ERROR_INIT_SYSCONFIG,"从数据库中加载系统配置信息出现异常。"+e.getMessage());
//		}
//		
//	}
//	/**
//	 * 获取配置信息
//	 * @param hosid 医院id
//	 * @param nodetype 类型id
//	 * @return
//	 */
//	public SysConfig getSysConfig(String hosid,String key){
//		Map<String, SysConfig> map = maps.get(hosid);
//		if(null != map){
//			return map.get(key);
//		}
//		return null;
//	}
//	
//}

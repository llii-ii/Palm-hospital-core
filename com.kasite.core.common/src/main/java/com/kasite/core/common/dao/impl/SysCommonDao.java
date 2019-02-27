//package com.kasite.core.common.dao.impl;
//
//import java.sql.SQLException;
//
//import com.coreframework.db.DB;
//import com.coreframework.db.Sql;
//import com.kasite.core.common.bean.bo.ChannelInfo;
//import com.kasite.core.common.dao.KstHosTableEnum;
//import com.kasite.core.common.dao.MyDatabaseEnum;
//
///**
// * 系统模块数据库操作类
// * 
// * @author 無
// * @version V1.0
// * @date 2018年4月24日 下午3:00:40
// */
//public class SysCommonDao {
//
//	/**
//	 * 获取ChannelInfo对象
//	 * 
//	 * @param channelInfo
//	 * @return
//	 * @throws SQLException
//	 */
//	public static ChannelInfo getChannelInfo(ChannelInfo channelInfo) throws SQLException {
//		Sql sql = DB.me().createSelect(channelInfo, KstHosTableEnum.SYS_CHANNELINFO);
//		return DB.me().queryForBean(MyDatabaseEnum.hos, sql, ChannelInfo.class);
//	}
//}

package com.kasite.client.crawler.modules.manage.dao.workTime;

import java.sql.SQLException;

import com.coreframework.db.DB;
import com.coreframework.db.Sql;
import com.kasite.client.crawler.config.MyDatabaseEnum;
import com.kasite.client.crawler.config.MyTableEnum;
import com.kasite.client.crawler.modules.manage.bean.workTime.dbo.WorkTime;

/**
 * 作业时间数据库操作类
 * 
 * @author cjy
 * @version V1.0
 * @date 2018年8月7日 上午17:54:00
 */
public class WorkTimeDao {
	
	//新增
	public static void addWorkTime(WorkTime workTime) throws SQLException {
		Sql sql = DB.me().createInsertSql(workTime, MyTableEnum.tb_work_time);
		DB.me().insert(MyDatabaseEnum.crawler_zk, sql);
	}
	
	
	//修改
	public static void updateWorkTime(WorkTime workTime) throws SQLException {
		String id = workTime.getId();
		workTime.setId(null);
		Sql sql = DB.me().createUpdateSql(workTime,  MyTableEnum.tb_work_time, " id = ? ");
		sql.addParamValue(id);
		DB.me().insert(MyDatabaseEnum.crawler_zk, sql);
	}
	
	//查询是否存在记录
	public static WorkTime selectWorkTime(WorkTime workTime) throws SQLException {
		Sql sql = DB.me().createSelect(workTime, MyTableEnum.tb_work_time);
		return DB.me().queryForBean(MyDatabaseEnum.crawler_zk, sql,WorkTime.class);
	}
}

//package com.kasite.client.rf.dao;
//
//import java.sql.SQLException;
//import java.text.ParseException;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import com.coreframework.db.DB;
//import com.coreframework.db.Sql;
//import com.coreframework.util.DateOper;
//import com.kasite.client.rf.bean.dto.Report;
//import com.kasite.client.rf.bean.dto.ReportDate;
//import com.kasite.client.rf.bean.dto.RfCloudData;
//import com.kasite.core.common.dao.KstHosTableEnum;
//import com.kasite.core.common.dao.MyDatabaseEnum;
//import com.kasite.core.serviceinterface.module.rf.req.ReqDataCollection;
//import com.kasite.core.serviceinterface.module.rf.req.ReqDataCloudCollection;
//
///**
// * 报表模块公用数据库操作类
// * 
// * @author 無
// * @version V1.0
// * @date 2018年4月24日 下午2:57:59
// */
//public class ReportFormsCommonDao {
//
//	/**
//	 * 新增报表数据
//	 * 
//	 * @param report
//	 * @return
//	 * @throws SQLException
//	 */
//	public static int addReport(Report report) throws SQLException {
//		Sql sql = DB.me().createInsertSql(report, KstHosTableEnum.RF_REPORT);
//		return DB.me().insert(MyDatabaseEnum.hos, sql, false);
//	}
//
//	/**
//	 * 插入报表汇总数据
//	 * 
//	 * @param reqDataCollection
//	 * @throws ParseException
//	 * @throws SQLException
//	 */
//	public static int addReportDate(ReportDate reportDate) throws ParseException, SQLException {
//		Sql sql = DB.me().createInsertSql(reportDate, KstHosTableEnum.RF_REPORTDATE);
//		return DB.me().insert(MyDatabaseEnum.hos, sql, false);
//	}
//
//	/**
//	 * 更新报表汇总数据
//	 * 
//	 * @param reqDataCollection
//	 * @throws SQLException
//	 * @throws ParseException
//	 */
//	public static int updateReportDate(ReqDataCollection reqDataCollection) throws SQLException, ParseException {
//		Sql sql = new Sql("update RF_REPORTDATE set DATAVALUE=DATAVALUE+ @a where 1=1 and @b ");
//		sql.addVar("@a", reqDataCollection.getDataValue());
//		sql.addVar("@b", "CHANNELID=? and  DATATYPE=? and SUMDATE=?");
//		sql.addParamValue(reqDataCollection.getChannelId());
//		sql.addParamValue(reqDataCollection.getDataType());
//		sql.addParamValue(DateOper.formatDate(new Date(), "yyyy-MM-dd"));
//		return DB.me().update(MyDatabaseEnum.hos, sql);
//	}
//
//	public static List<Map<String, Object>> getDataCollection4() throws SQLException {
//
//		Sql sql = new Sql(
//				"select distinct a.cardno,a.operatorid,a.operatorname,a.channelid from O_ORDER a left join O_PAYORDER b on a.ORDERID = b.ORDERID where b.paystate=2");
//		List<Map<String, Object>> list = DB.me().queryForMapList(MyDatabaseEnum.hos, sql);
//		return list;
//	}
//
//	/**
//	 * 查询报表数据
//	 * 
//	 * @param reqDataCollection
//	 * @return
//	 * @throws SQLException
//	 * @throws ParseException
//	 * @author 無
//	 * @date 2018年4月24日 下午2:56:37
//	 */
//	public static ReportDate queryReportData(ReqDataCollection reqDataCollection) throws SQLException, ParseException {
//		ReportDate reportDate = null;
//		Sql sql = new Sql("select @a from RF_REPORTDATE where 1=1 and @b");
//		sql.addVar("@a", "*");
//		sql.addVar("@b", "CHANNELID=? and  DATATYPE=? and SUMDATE=?");
//		sql.addParamValue(reqDataCollection.getChannelId());
//		sql.addParamValue(reqDataCollection.getDataType());
//		sql.addParamValue(DateOper.formatDate(new Date(), "yyyy-MM-dd"));
//		reportDate = DB.me().queryForBean(MyDatabaseEnum.hos, sql, ReportDate.class);
//		return reportDate;
//	}
//
//	/**
//	 * 更新云报表汇总数据
//	 * 
//	 * @param reqDataCollection
//	 * @throws SQLException
//	 * @throws ParseException
//	 */
//	public static int updateCloudReportDate(ReqDataCloudCollection cloudData) throws SQLException, ParseException {
//
//		Sql sql = new Sql("update RF_CLOUD_DATA set DATACOUNT=DATACOUNT+ @a , updateDate = ? where 1=1 and @b ");
//		sql.addVar("@a", "" + cloudData.getDataCount());
//		sql.addVar("@b", "CHANNELID=? and  DATATYPE=? and DATE=?");
//		sql.addParamValue(cloudData.getUpdateDate());
//		sql.addParamValue(cloudData.getChannelId());
//		sql.addParamValue(cloudData.getDataType());
//		sql.addParamValue(DateOper.formatDate(new Date(), "yyyy-MM-dd"));
//		return DB.me().update(MyDatabaseEnum.hos, sql);
//	}
//
//	public static RfCloudData queryCloudReportData(ReqDataCloudCollection reqCloudData) throws SQLException, ParseException {
//		RfCloudData reportDate = null;
//		Sql sql = new Sql("select @a from RF_CLOUD_DATA where 1=1 and @b");
//		sql.addVar("@a", "*");
//		sql.addVar("@b", "CHANNELID=? and  DATATYPE=? and date=?");
//		sql.addParamValue(reqCloudData.getChannelId());
//		sql.addParamValue(reqCloudData.getDataType());
//		sql.addParamValue(DateOper.formatDate(new Date(), "yyyy-MM-dd"));
//		reportDate = DB.me().queryForBean(MyDatabaseEnum.hos, sql, RfCloudData.class);
//		return reportDate;
//	}
//
//	/**
//	 * 插入报表汇总数据
//	 * 
//	 * @param reqDataCollection
//	 * @throws ParseException
//	 * @throws SQLException
//	 */
//	public static void addCloudReportDate(RfCloudData cloudData) throws ParseException, SQLException {
//		Sql sql = DB.me().createInsertSql(cloudData, KstHosTableEnum.RF_CLOUD_DATA);
//		DB.me().insert(MyDatabaseEnum.hos, sql, false);
//	}
//
//}

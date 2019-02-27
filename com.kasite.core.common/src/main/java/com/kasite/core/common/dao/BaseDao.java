//package com.kasite.core.common.dao;
//
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Map;
//
//import com.coreframework.db.DatabaseEnum;
//import com.coreframework.db.JdbcConnection;
//import com.coreframework.db.Sql;
//import com.coreframework.db.SqlNameEnum;
//import com.coreframework.db.TableEnum;
//import com.kasite.core.common.dao.vo.ReqPage;
//import com.kasite.core.common.dao.vo.RespList;
//
///**
// * 基础Dao(还需在XML文件里，有对应的SQL语句)
// * 
// * @author daiys
// * @date 2017-08-03
// */
//public interface BaseDao<T> {
//	
//	/**
//	 * 获取一个新的链接。注意链接获取后需要关闭链接
//	 * @return
//	 * @throws SQLException
//	 */
//	JdbcConnection getConn() throws SQLException;
//	/**
//	 * 新增 
//	 * 通过指定连接新增
//	 * @param conn 数据库连接
//	 * @param t 需要新增的对象
//	 * @return 默认返回新增对象的主键
//	 * @throws SQLException 当异常时抛出
//	 */
//	Object save(JdbcConnection conn,T t) throws SQLException;
//	/**
//	 * 新增 
//	 * @param t 需要新增的对象
//	 * @return 默认返回新增对象的主键
//	 * @throws SQLException 当异常时抛出
//	 */
//	Object save(T t) throws SQLException;
//	/**
//	 * 新增 
//	 * 通过指定连接新增
//	 * @param conn 数据库连接
//	 * @param map 需要新增的对象的Sql 中的 参数
//	 * @return 默认返回新增对象的主键
//	 * @throws SQLException 当异常时抛出
//	 */
//	Object save_Map(JdbcConnection conn,Map<String, Object> map) throws SQLException;
//	
//	Object save_Map(Map<String, Object> map) throws SQLException;
//	
//	Object saveBatch(List<T> list) throws SQLException;
//	
//	void update(JdbcConnection conn,T t,String whereString) throws SQLException;
//	
//	int update(T t,String whereString) throws SQLException;
//	
//	int update_Map(JdbcConnection conn,Map<String, Object> map) throws SQLException;
//	
//	int update_Map(Map<String, Object> map) throws SQLException;
//	
//	void delete(JdbcConnection conn,Object id) throws SQLException;
//	
//	int delete(Object id) throws SQLException;
//	
//	void delete_Map(JdbcConnection conn,Map<String, Object> map) throws SQLException;
//	
//	int delete_Map(Map<String, Object> map) throws SQLException;
//	
//	int deleteBatch(Object[] id) throws SQLException;
//
//	T queryObject(Object id) throws SQLException;
//	
//	List<T> queryList_Map(Map<String, Object> map) throws SQLException;
//	
//	int queryTotal_Map(Map<String, Object> map) throws SQLException;
//
//	int queryTotal() throws SQLException;
//	/**删除对象接口，返回受影响记录数**/
//    public int delete(Object id,SqlNameEnum sqlEnum)throws SQLException;
//    public int delete(Object id,DatabaseEnum dbEnum,SqlNameEnum sqlEnum)throws SQLException;
//    public int delete(SqlNameEnum sqlEnum, String where, Object... objs) throws SQLException;
//    public int delete(DatabaseEnum dbEnum, SqlNameEnum sqlEnum,String where, Object... objs) throws SQLException;
//	/**公用保存对象接口，存在自增主键时，返回自增主键ID**/
//	public int save(Object obj, TableEnum table)throws SQLException;
//	public int save(Object obj,DatabaseEnum dbName, TableEnum table,boolean hasGeneratedKey)throws SQLException;
//	
//	/**修改对象接口，返回受影响记录数**/
//	public int update(Object req,TableEnum table,String where,Object... objs)throws SQLException;
//	public int update(Object req,DatabaseEnum dbName,TableEnum table,String where,Object... objs)throws SQLException;
//	public int update(String columns, String where, DatabaseEnum dbEnum, SqlNameEnum sqlEnum, Object... objs) throws SQLException;
//	public int update(String columns, String where, List<Object> list, DatabaseEnum dbEnum, SqlNameEnum sqlEnum) throws SQLException;
//	
//	/**查询记录数接口**/
//	public int getCount(SqlNameEnum sqlName,String where,Object... objs)throws SQLException;
//	public int getCount(DatabaseEnum dbName,SqlNameEnum sqlName,String where,Object... objs)throws SQLException;
//	public int getCount(SqlNameEnum sqlName,String where,List<Object> ll)throws SQLException;
//	public int getCount(DatabaseEnum dbName,SqlNameEnum sqlName,String where,List<Object> list)throws SQLException;
//	public int getCount(DatabaseEnum dbName,SqlNameEnum sqlName,String columns,String where,List<Object> list)throws SQLException;
//	public int getCount(SqlNameEnum sqlName,String columns,String where,List<Object> ll)throws SQLException;
//	
//	/**查询列表接口**/
//	public RespList getRespList(Class<?> cls,DatabaseEnum dbName,SqlNameEnum sqlName,ReqPage page,String columns,String where,Object... objs)throws SQLException;
//	public RespList getRespList(Class<?> cls,SqlNameEnum sqlName,ReqPage page,String columns,String where,Object... objs)throws SQLException;
//	public RespList getRespList(Class<?> cls,DatabaseEnum dbName,SqlNameEnum sqlName,ReqPage page,String columns,String where,List<Object> list)throws SQLException;
//	public RespList getRespList(Class<?> cls,SqlNameEnum sqlName,ReqPage page,String columns,String where,List<Object> list)throws SQLException;
//	public RespList getRespList(Class<?> cls,DatabaseEnum dbName,Sql sql,ReqPage page)throws SQLException;
//	public RespList getRespList(Class<?> cls,Sql sql,ReqPage page)throws SQLException;
//	
//	public List<T> getList(Class<T> cls, SqlNameEnum sqlName, ReqPage page, String columns, String where, List<Object> list) throws SQLException ;
//	public List<T> getList(Class<T> cls, DatabaseEnum dbName, SqlNameEnum sqlName, ReqPage page, String columns, String where, List<Object> list) throws SQLException;
//	public List<T> getList(Class<T> cls, SqlNameEnum sqlName, ReqPage page, String columns, String where, Object... objs) throws SQLException ;
//	public List<T> getList(Class<T> cls, DatabaseEnum dbName, SqlNameEnum sqlName, ReqPage page, String columns, String where, Object... objs) throws SQLException;
//	public List<T> getList(Class<T> cls, Sql sql, ReqPage page) throws SQLException;
//	public List<T> getList(Class<T> cls, DatabaseEnum dbName, Sql sql, ReqPage page) throws SQLException;
//	
//	
//	/**查询记录是否存在接口**/
//	public boolean isExists(SqlNameEnum sqlName,String where,Object... objs)throws SQLException;
//	public boolean isExists(DatabaseEnum dbName,SqlNameEnum sqlName,String where,Object... objs)throws SQLException;
//	
//	/**查询字符串**/
//	public String getString(SqlNameEnum sqlName,String columns,String where,Object... objs)throws SQLException;
//	public String getString(SqlNameEnum sqlName,String columns,String where,List<Object> ll)throws SQLException;
//	public String getString(DatabaseEnum dbName,SqlNameEnum sqlName,String columns,String where,Object... objs)throws SQLException;
//	public String getString(DatabaseEnum dbName,SqlNameEnum sqlName,String columns,String where,List<Object> list)throws SQLException;
//	
//	/**查询对象**/
//	public Object getObject(Class<?> cls,DatabaseEnum dbName,SqlNameEnum sqlName,String columns,String where,List<Object> list)throws SQLException;
//	public Object getObject(Class<?> cls,SqlNameEnum sqlName,String columns,String where,List<Object> list)throws SQLException;
//	public Object getObject(Class<?> cls,DatabaseEnum dbName,SqlNameEnum sqlName,String columns,String where,Object... objs)throws SQLException;
//	public Object getObject(Class<?> cls,SqlNameEnum sqlName,String columns,String where,Object... objs)throws SQLException;
//    
//    
//	public Map<String,Object> getMap(DatabaseEnum dbName,SqlNameEnum sqlName,String columns,String where,Object... objs)throws SQLException;
//	public Map<String,Object> getMap(SqlNameEnum sqlName,String columns,String where,Object... objs)throws SQLException;
//	public Map<String,Object> getMap(DatabaseEnum dbName,SqlNameEnum sqlName,String columns,String where,List<Object> list)throws SQLException;
//	public Map<String,Object> getMap(SqlNameEnum sqlName,String columns,String where,List<Object> list)throws SQLException;
//	
//	
//	public List<Map<String,Object>> getMapList(DatabaseEnum dbName,SqlNameEnum sqlName,String columns,String where,Object... objs)throws SQLException;
//	public List<Map<String,Object>> getMapList(SqlNameEnum sqlName,String columns,String where,Object... objs)throws SQLException;
//	public List<Map<String,Object>> getMapList(DatabaseEnum dbName,SqlNameEnum sqlName,String columns,String where,List<Object> list)throws SQLException;
//	public List<Map<String,Object>> getMapList(SqlNameEnum sqlName,String columns,String where,List<Object> list)throws SQLException;
//	
//	
//	
//	public List<Map<String,Object>> getMapList(SqlNameEnum sqlName, ReqPage page, String columns, String where, List<Object> list) throws SQLException ;
//	public List<Map<String,Object>> getMapList(DatabaseEnum dbName, SqlNameEnum sqlName, ReqPage page, String columns, String where, List<Object> list) throws SQLException;
//	public List<Map<String,Object>> getMapList(SqlNameEnum sqlName, ReqPage page, String columns, String where, Object... objs) throws SQLException ;
//	public List<Map<String,Object>> getMapList(DatabaseEnum dbName, SqlNameEnum sqlName, ReqPage page, String columns, String where, Object... objs) throws SQLException;
//	
//	/**分组查询列表*/
//    public RespList getRespListGroupBy(Class<?> cls, DatabaseEnum dbName, SqlNameEnum sqlName, ReqPage page,
//            String columns, String where, List<Object> list) throws SQLException;
//    public RespList getRespListGroupBy(Class<?> cls,SqlNameEnum sqlName, ReqPage page,
//            String columns, String where, List<Object> list) throws SQLException;
//}

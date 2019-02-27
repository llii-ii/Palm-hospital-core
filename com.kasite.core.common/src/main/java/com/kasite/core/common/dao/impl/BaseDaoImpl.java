//package com.kasite.core.common.dao.impl;
//
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.coreframework.db.DB;
//import com.coreframework.db.DatabaseEnum;
//import com.coreframework.db.JdbcConnection;
//import com.coreframework.db.Sql;
//import com.coreframework.db.SqlNameEnum;
//import com.coreframework.db.TableEnum;
//import com.coreframework.remoting.standard.DateOper;
//import com.kasite.core.common.dao.BaseDao;
//import com.kasite.core.common.dao.vo.ReqPage;
//import com.kasite.core.common.dao.vo.RespList;
//import com.kasite.core.common.util.StringUtil;
//
//public abstract class BaseDaoImpl<T> implements BaseDao<T> {
//
//	private final static String SQL_UPDATE_MAP= "update_Map";
//	private final static String SQL_SAVE_MAP= "save_Map";
//	private final static String SQL_DELETE= "delete";
//	private final static String SQL_DELETE_MAP= "delete_Map";
//	private final static String SQL_DELETE_DELETEBATCH= "deleteBatch";
//	private final static String SQL_QUERY_QUERYOBJECT= "queryObject";
//	private final static String SQL_QUERY_QUERYLIST_MAP= "queryList_Map";
//	private final static String SQL_QUERY_QUERYTOTAL_MAP= "queryTotal_Map";
//	private final static String SQL_QUERY_QUERYTOTAL= "queryTotal";
//	
//	public final static String SQL_QUERY_PAGEINDEX =  "offset";
//	public final static String SQL_QUERY_PAGESIZE =  "limit";
//	public final static String SQL_QUERY_ORDERBY =  "sidx_order";
//	
//	private static final String SQLFORMAT_START = "@";
//	@Autowired
//	protected DatabaseEnum baseDaoEnum;
//	
//    // 泛型反射类
//	protected Class<T> entityClass;
//
//    /**
//     * 
//getClass().getGenericSuperclass()返回表示此 Class 所表示的实体
//    （类、接口、基本类型或 void）的直接超类的 Type然后将其转换ParameterizedType
//    getActualTypeArguments()返回表示此类型实际类型参数的 Type 对象的数组。
//    [0]就是这个数组中第一个了。。
//    简而言之就是通过反射获取子类确定的泛型类
//     * */ 
//    @SuppressWarnings("unchecked")
//	public BaseDaoImpl() {
//        Type genType = getClass().getGenericSuperclass();
//        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
//        entityClass = (Class<T>) params[0];
//    }
//    
//    public JdbcConnection getConn() throws SQLException{
//    	return DB.me().getConnection(baseDaoEnum);
//    }
//    
//	/**
//	 * 返回查询语句中的 in 查询语句的拼接。 
//	 */
//	public void formatWhereInSql(Sql sql,String nameSpace,Object[] objs){
//		
//		if(objs != null && objs.length > 0){
//			int i = 0;
//			String whererinstr = "";
//			for (Object o : objs) {
//				if(i > 0){
//					whererinstr += ",";
//				}
//				whererinstr += "?";
//				sql.addParamValue(o);
//			}
//			sql.addVar(nameSpace, whererinstr);
//		}
//	}
//	
//    /**
//     * 通过实体类的注释 @Table 来获取相应对象的 表名
//     * @return
//     * @throws SQLException
//     */
//    public abstract TableEnum getTableName() throws SQLException;//{
////    	
////    	if(null != entityClass){
////    		Table tb = entityClass.getAnnotation(Table.class);
////    		if(null == tb){
////    			throw new SQLException("Please check you Entity,It's not find entity annotation javax.persistence.Table. The Entity is :"+ entityClass.getName());
////    		}
////    		return tb.name();
////    	}else{
////    		throw new SQLException("It's not find entityClass in the BaseDaoImpl ("+this.getClass().getName()+ ")");
////    	}
////    }
//    /**
//     * 获取约定的 *.sql.xml 文件中的 Sql语句
//     * @param methodName 约定格式是 【表名】+【_】+ 方法名称
//     * @throws SQLException
//     */
//    public String getConfigSql(String methodName) throws SQLException{
//    	String name = getTableName() + "_"+ methodName;
//    	return DB.me().getSql(name);
//    }
//    
//    /**
//     * 获取约定的 *.sql.xml 文件中的 Sql语句
//     * @param methodName 约定格式是 【表名】+【_】+ 方法名称
//     * @throws SQLException
//     */
//    public Sql getConfigSql(SqlNameEnum sqlNameEnum) throws SQLException{
//    	return DB.me().createSql(sqlNameEnum);
//    }
//    
//    
//    /**
//     * 获取约定的 *.sql.xml 文件中的 Sql语句
//     * @param methodName 约定格式是dao层的 【表名】+【_】+ 方法名称
//     * @throws SQLException
//     */
//    public Sql getConfigSqlstr2Sql(String methodName) throws SQLException{
//    	String name = getTableName() + "_"+ methodName;
//    	return new Sql(DB.me().getSql(name));
//    }
//    
//	
//	public void update(JdbcConnection conn, T t,String whereString) throws SQLException {
//		Sql sql = DB.me().createUpdateSql(t, getTableName(), whereString);
//		DB.me().update(conn, sql);
//	}
//
//	
//	public int update(T t,String whereString) throws SQLException {
//		Sql sql = DB.me().createUpdateSql(t, getTableName(), whereString);
//		return DB.me().update(this.baseDaoEnum, sql);
//	}
//	
//	
//	public int update_Map(JdbcConnection conn,Map<String, Object> map) throws SQLException {
//		Sql sql = formatSql(getConfigSql(SQL_UPDATE_MAP), map);
//		return DB.me().update(this.baseDaoEnum, sql);
//	}
//	
//	
//	public int update_Map(Map<String, Object> map) throws SQLException {
//		Sql sql = formatSql(getConfigSql(SQL_UPDATE_MAP), map);
//		return DB.me().update(this.baseDaoEnum, sql);
//	}
//	/** 属性比较类型. 
//	 * EQ 表示=
//	 * LIKE 表示like
//	 * LT 表示<
//	 * GT 表示>
//	 * LE 表示<=
//	 * GE 表示>=
//	 * */
//	public enum MatchType {
//		EQ("="), LIKE("like"), LT("<"), GT(">"), LE("<="), GE(">="), IN("in");
//		private String type;
//		private MatchType(String type){
//			this.type = type;
//		}
//		public String getTypeName(){
//			return type;
//		}
//	}
//	/** 属性数据类型. */
//	public enum PropertyType {
//		S(String.class), I(Integer.class), L(Long.class), N(Double.class), D(Date.class), B(Boolean.class), O(List.class);
//		private Class<?> clazz;
//		private PropertyType(Class<?> clazz) {
//			this.clazz = clazz;
//		}
//		public Class<?> getValue() {
//			return clazz;
//		}
//	}
//	/** 属性数据类型. */
//	public enum PropertyMustType {
//		M("!M");
//		private String type;
//		private PropertyMustType(String type){
//			this.type = type;
//		}
//		public String getTypeName(){
//			return type;
//		}
//	}
//	
//	
//	@SuppressWarnings("unchecked")
//	private static Sql formatSql(Sql sql,String sqlstr,String string,FilterVo vo,Object o) throws SQLException{
//		String format_filter = string.substring(1, string.length());
//		String fieldName = vo.getFieldName();
//		MatchType t = vo.getMatchType();
//		PropertyType p = vo.getPropertyType();
//		PropertyMustType pm = vo.getPropertyMustType();
//		if(PropertyMustType.M.equals(pm) && StringUtil.isBlank(o)){
//			throw new SQLException("The Sql=【"+sqlstr+"】 【"+ string +"】 Must Not Null Field = "+ fieldName +"  value = "+ o);
//		}
//		String sqlfilterstr ="and "+ fieldName + " " + t.getTypeName() +" ?";
//		switch (t) {
//			case IN:{
//				sqlfilterstr = "and "+fieldName + " " + t.getTypeName() +"( ";
//				if(o instanceof List){
//					List<Object> os = (List<Object>) o;
//					int i = 0;
//					for (Object object : os) {
//						if(i > 0){
//							sqlfilterstr += ",";
//						}
//						sqlfilterstr += "?";
//						sql.addParamValue(object);
//						i++;
//					}
//				}
//				sqlfilterstr +=" )";
//				sql.addVar(string, sqlfilterstr);
//				break;
//			}
//			case LIKE:{
//				if(null != o){
//					sqlfilterstr ="and "+ fieldName + " " + t.getTypeName() +" CONCAT('%',(?),'%') ";
//					sql.addParamValue(o);
//					sql.addVar(string, sqlfilterstr);
//				}else{
//					sql.addVar(string, "");
//				}
//				break;
//			}
//		default:
//			sql.addVar(string, sqlfilterstr);
//			formatFilterPrivateType(p, o, sql, format_filter);
//			break;
//		}
//		return sql;
//	}
//	
//	public static Sql formatSql(String sqlstr,List<Object> o) throws SQLException{
//		Sql sql = new Sql(sqlstr);
//		String[] ss = sqlstr.split(" ");
//		if(StringUtil.isNotBlank(sqlstr))
//		for (String string : ss) {
//			if(StringUtil.isNotBlank(string)){
//				String c = string.substring(0, 1);
//				if(SQLFORMAT_START.equals(c)){
//					String format_filter = string.substring(1, string.length());
//					FilterVo vo = FilterVo.getFilterVo(format_filter);
//					if(null != vo && null != o){
////						Object o = map.get(vo.getMapKey());
//						formatSql(sql, sqlstr, string, vo, o);
//					}else{
//						sql.addVar(string, "");
//					}
//				}
//			}	
//		}
//		return sql;
//	}
//	
//	public static Sql formatSql(String sqlstr,Map<String, Object> map) throws SQLException{
//		Sql sql = new Sql(sqlstr);
//		if(StringUtil.isBlank(sqlstr)){
//			throw new SQLException("SQL查询语句为空。");
//		}
//		String[] ss = sqlstr.split(" ");
//		if(StringUtil.isNotBlank(sqlstr))
//		for (String string : ss) {
//			if(StringUtil.isNotBlank(string.trim())){
//				String c = string.trim().substring(0, 1);
//				if(SQLFORMAT_START.equals(c)){
//					String format_filter = string.substring(1, string.length());
//					FilterVo vo = FilterVo.getFilterVo(format_filter);
//					if(null != vo){
//						Object o = map.get(vo.getMapKey());
//						if(StringUtil.isNotBlank(o)){
//							formatSql(sql, sqlstr, string, vo, o);
//						}else{
//							sql.addVar(string, "");
//						}
//					}else{
//						Object o = map.get(format_filter);
//						if(StringUtil.isNotBlank(o)){
//							if(SQL_QUERY_ORDERBY.equalsIgnoreCase(format_filter)){
//								sql.addVar(string, o.toString());
//							}else{
//								sql.addVar(string, "?");
//								sql.addParamValue(o);
//							}
//						}else{
//							sql.addVar(string, "");
//						}
//					}
//				}
//			}	
//		}
//		return sql;
//	}
//	
//	private static void formatFilterPrivateType(PropertyType p,Object o,Sql sql,String format_filter) throws SQLException{
//		try{
//			switch (p) {
//			case S:{
//				sql.addParamValue(o.toString());
//				break;
//			}
//			case B:{
//				Boolean v = Boolean.valueOf(o.toString());
//				sql.addParamValue(v);
//				break;
//			}
//			case D:{
//				Date v = DateOper.parse(o.toString());
//				sql.addParamValue(v);
//				break;
//			}
//			case I:{
//				Integer v = Integer.valueOf(o.toString());
//				sql.addParamValue(v);
//				break;
//			}
//			case L:{
//				Long v = Long.valueOf(o.toString());
//				sql.addParamValue(v);
//				break;
//			}
//			case N:{
//				Double v = Double.valueOf(o.toString());
//				sql.addParamValue(v);
//				break;
//			}
//			default:
//				break;
//			}
//		}catch(Exception e){
//			throw new SQLException("This Sql 【"+ format_filter +"】 Value is not :"+p.getValue().getName() +" The Value is :"+ o);
//		}
//	}
//	
//	public static void main(String[] args) throws SQLException {
//		//_EQ_
////		String sqlstr = "update set @name_EQ!S_name where @dept_id_IN!L!M_deptId";
////		Map<String, Object> map = new HashMap<String, Object>();
////		map.put("name", "t");
////		List<String> ids = new ArrayList<String>();
////		ids.add("1");
////		ids.add("2");
////		map.put("deptId", ids);
////		Sql sql = formatSql(sqlstr,map);
////		KasiteConfig.print(sql.getSqlString());
////		for (Object obj : sql.getParamValueList()) {
////			KasiteConfig.print(obj);
////		}
////		
////		Sql sql = new Sql("select * from sys_dept where dept_id in ( ? )");
//		
////		Sql sql = new Sql("update set @set_name_EQ_name where @where_dept_id_EQ_deptId");
////		Map<String, Object> map = new HashMap<String, Object>();
////		map.put("name", "t");
////		map.put("deptId", "d");
////		for (Map.Entry<String, Object> entity : map.entrySet()) {
////			String key = entity.getKey();
////			Object value = entity.getValue();
////			KasiteConfig.print(key);
////			sql.addVar("#"+key, value+"");
////		}
////		KasiteConfig.print(sql.toString());
//	}
//
//	
//	
//	public void delete(JdbcConnection conn, Object id) throws SQLException {
//		Sql sql = getConfigSqlstr2Sql(SQL_DELETE);
//		sql.addParamValue(id);
//		DB.me().update(conn, sql);
//	}
//
//	
//	public int delete(Object id) throws SQLException {
//		Sql sql = getConfigSqlstr2Sql(SQL_DELETE);
//		sql.addParamValue(id);
//		return DB.me().update(this.baseDaoEnum,sql);
//	}
//
//	
//	public void delete_Map(JdbcConnection conn, Map<String, Object> map)
//			throws SQLException {
//		Sql sql = formatSql(getConfigSql(SQL_DELETE_MAP), map);
//		DB.me().update(conn, sql);
//	}
//
//	
//	public int delete_Map(Map<String, Object> map) throws SQLException {
//		Sql sql = formatSql(getConfigSql(SQL_DELETE_MAP), map);
//		return DB.me().update(this.baseDaoEnum, sql);
//	}
//
//	
//	public int deleteBatch(Object[] ids) throws SQLException {
//		List<Object> list = new ArrayList<Object>(ids.length);
//		for (Object o : ids) {
//			list.add(o);
//		}
//		Sql sql = formatSql(getConfigSql(SQL_DELETE_DELETEBATCH), list);
//		return DB.me().update(baseDaoEnum, sql);
//	}
//
//	
//	public T queryObject(Object id) throws SQLException {
//		Sql sql = getConfigSqlstr2Sql(SQL_QUERY_QUERYOBJECT);
//		sql.addParamValue(id);
//		return DB.me().queryForBean(baseDaoEnum, sql, entityClass);
//	}
//
//	
//	public List<T> queryList_Map(Map<String, Object> map) throws SQLException {
//		Sql sql = formatSql(getConfigSql(SQL_QUERY_QUERYLIST_MAP), map);
//		//判断分页参数：offset  limit
//		Object start = map.get(SQL_QUERY_PAGEINDEX);
//		Object pageSize = map.get(SQL_QUERY_PAGESIZE);
//		if(null != start && null != pageSize){
//			Integer istart = Integer.parseInt(start.toString());
//			Integer ipageSize = Integer.parseInt(pageSize.toString());
//			return DB.me().queryForBeanList(baseDaoEnum, sql, entityClass,istart,ipageSize);
//		}
//		return DB.me().queryForBeanList(baseDaoEnum, sql, entityClass);
//	}
//
//	
//	public int queryTotal_Map(Map<String, Object> map) throws SQLException {
//		String sqlstr = getConfigSql(SQL_QUERY_QUERYTOTAL_MAP);
//		if(StringUtil.isBlank(sqlstr)){
//			throw new SQLException("配置文件中不存在Sql语句："+ getTableName() + "_"+ SQL_QUERY_QUERYTOTAL_MAP);
//		}
//		Sql sql = formatSql(sqlstr, map);
//		return DB.me().queryForInteger(baseDaoEnum, sql);
//	}
//
//	
//	public int queryTotal() throws SQLException {
//		Sql sql = getConfigSqlstr2Sql(SQL_QUERY_QUERYTOTAL);
//		return DB.me().queryForInteger(baseDaoEnum, sql);
//	}
//
//
//	
//	public Object save(JdbcConnection conn, T t) throws SQLException {
//		Sql sql = DB.me().createInsertSql(t, getTableName());
//		return DB.me().insert(conn, sql);
//	}
//
//
//	
//	public Object save(T t) throws SQLException {
//		Sql sql = DB.me().createInsertSql(t, getTableName());
//		return DB.me().insert(baseDaoEnum, sql);
//	}
//
//
//	
//	public Object save_Map(Map<String, Object> map) throws SQLException {
//		Sql sql = formatSql(getConfigSql(SQL_SAVE_MAP), map);
//		return DB.me().queryForInteger(baseDaoEnum, sql);
//	}
//
//
//	
//	public Object saveBatch(List<T> list) throws SQLException {
//		int size = 0;
//		for (T t : list) {
//			Sql sql = DB.me().createInsertSql(t, getTableName());
//			DB.me().insert(baseDaoEnum, sql);
//			size ++;
//		}
//		return size;
//	}
//
//
//	
//	public Object save_Map(JdbcConnection conn, Map<String, Object> map)
//			throws SQLException {
//		Sql sql = formatSql(getConfigSql(SQL_SAVE_MAP), map);
//		return DB.me().queryForInteger(baseDaoEnum, sql);
//	}
//
//	
//    public int delete(Object id, SqlNameEnum sqlEnum) throws SQLException {
//        return delete(id, baseDaoEnum, sqlEnum);
//    }
//
//    
//    public int delete(Object id, DatabaseEnum dbEnum, SqlNameEnum sqlEnum) throws SQLException {
//        Sql sql = DB.me().createSql(sqlEnum);
//        sql.addParamValue(id);
//        return DB.me().update(dbEnum, sql);
//    }
//    
//    public int delete(SqlNameEnum sqlEnum, String where, Object... objs) throws SQLException {
//        return delete(baseDaoEnum, sqlEnum, where, objs);
//    }
//    
//    public int delete(DatabaseEnum dbEnum, SqlNameEnum sqlEnum, String where, Object... objs) throws SQLException {
//        Sql sql = DB.me().createSql(sqlEnum);
//        sql.addVar("@a", where);
//        for (Object object : objs) {
//            sql.addParamValue(object);
//        }
//        return DB.me().update(dbEnum, sql);
//    }
//
//    
//    public int save(Object obj, TableEnum table) throws SQLException {
//        return save(obj, baseDaoEnum, table,true);
//    }
//
//    
//    public int save(Object obj, DatabaseEnum dbName, TableEnum table,boolean hasGeneratedKey) throws SQLException {
//        Sql sql = DB.me().createInsertSql(obj, table);
//        return DB.me().insert(dbName, sql,hasGeneratedKey);
//    }
//
//    
//    public int update(Object req, TableEnum table, String where, Object... objs) throws SQLException {
//
//        return update(req, baseDaoEnum, table, where, objs);
//    }
//
//    
//    public int update(Object req, DatabaseEnum dbName, TableEnum table, String where, Object... objs)
//            throws SQLException {
//        Sql sql = DB.me().createUpdateSql(req, table, where);
//        for (Object object : objs) {
//            sql.addParamValue(object);
//        }
//        return DB.me().update(dbName, sql);
//    }
//
//    
//    public int update(String columns, String where, DatabaseEnum dbEnum, SqlNameEnum sqlEnum, Object... objs)
//            throws SQLException {
//        Sql sql = DB.me().createSql(sqlEnum);
//        sql.addVar("@a", columns);
//        sql.addVar("@b", where);
//        for (Object object : objs) {
//            sql.addParamValue(object);
//        }
//        return DB.me().update(dbEnum, sql);
//    }
//
//    
//    public int update(String columns, String where, List<Object> list, DatabaseEnum dbEnum, SqlNameEnum sqlEnum)
//            throws SQLException {
//        Sql sql = DB.me().createSql(sqlEnum);
//        sql.addVar("@a", columns);
//        sql.addVar("@b", where);
//        for (Object object : list) {
//            sql.addParamValue(object);
//        }
//        return DB.me().update(dbEnum, sql);
//    }
//
//    
//    public int getCount(SqlNameEnum sqlName, String where, Object... objs) throws SQLException {
//        return getCount(baseDaoEnum, sqlName, where, objs);
//    }
//
//    
//    public int getCount(DatabaseEnum dbName, SqlNameEnum sqlName, String where, Object... objs) throws SQLException {
//        Sql sql = DB.me().createSql(sqlName);
//        sql.addVar("@a", "count(1)");
//        sql.addVar("@b", where);
//        for (Object object : objs) {
//            sql.addParamValue(object);
//        }
//        return DB.me().queryForInteger(dbName, sql);
//    }
//
//    
//    public RespList getRespList(Class<?> cls, SqlNameEnum sqlName, ReqPage page, String columns, String where,
//            Object... objs) throws SQLException {
//        return getRespList(cls, baseDaoEnum, sqlName, page, columns, where, objs);
//    }
//
//    
//    public RespList getRespList(Class<?> cls, DatabaseEnum dbName, SqlNameEnum sqlName, ReqPage page,
//            String columns, String where, Object... objs) throws SQLException {
//        RespList resp = new RespList();
//        Integer pageIndex = 0;
//        Integer pagesize = 0;
//        String sorts = null;
//        String orders = null;
//        if (page != null) {
//            pageIndex = page.getPageIndex();
//            pagesize = page.getPageSize();
//            sorts = page.getSorts();
//            orders = page.getOrders();
//        }
//        Sql sql = DB.me().createSql(sqlName);
//        sql.addVar("@a", columns);
//        if (StringUtil.isNotBlank(sorts)) {
//            // 排序控制
//            String[] arr1 = sorts.split(",");
//            String[] arr2 = orders.split(",");
//            StringBuffer oSb = new StringBuffer(where).append(" order by ");
//            for (int i = 0; i < arr1.length; i++) {
//                oSb.append(arr1[i]).append(("-1".equals(arr2[i]) ? " desc " : " asc ")).append(",");
//            }
//            sql.addVar("@b", oSb.substring(0, oSb.length() - 1));
//        } else {
//            sql.addVar("@b", where);
//        }
//        if (pageIndex != null && pageIndex > 0 && pagesize != null && pagesize > 0) {
//            // 查询总记录数
//            Sql countSql = DB.me().createSql(sqlName);
//            countSql.addVar("@a", "count(1)");
//            countSql.addVar("@b", where);
//            if (objs != null) {
//                for (Object obj : objs) {
//                    sql.addParamValue(obj);
//                    countSql.addParamValue(obj);
//                }
//            }
//            int total = DB.me().queryForInteger(dbName, countSql);
//            resp.setTotal(total);
//
//            // 分页查询
//            resp.setResult(DB.me().queryForBeanList(dbName, sql, cls, (pageIndex - 1) * pagesize, pagesize));
//        } else {
//            if (objs != null) {
//                for (Object obj : objs) {
//                    sql.addParamValue(obj);
//                }
//            }
//            List<?> list = DB.me().queryForBeanList(dbName, sql, cls);
//            resp.setTotal(list.size());
//            resp.setResult(list);
//        }
//        return resp;
//    }
//
//    
//    public RespList getRespList(Class<?> cls, SqlNameEnum sqlName, ReqPage page, String columns, String where,
//            List<Object> list) throws SQLException {
//        return getRespList(cls, baseDaoEnum, sqlName, page, columns, where, list);
//    }
//
//    
//    public RespList getRespList(Class<?> cls, DatabaseEnum dbName, SqlNameEnum sqlName, ReqPage page,
//            String columns, String where, List<Object> list) throws SQLException {
//        RespList resp = new RespList();
//        Integer pageIndex = 0;
//        Integer pagesize = 0;
//        String sorts = null;
//        String orders = null;
//        if (page != null) {
//            pageIndex = page.getPageIndex();
//            pagesize = page.getPageSize();
//            sorts = page.getSorts();
//            orders = page.getOrders();
//        }
//        Sql sql = DB.me().createSql(sqlName);
//        sql.addVar("@a", columns);
//        if (StringUtil.isNotBlank(sorts)) {
//            // 排序控制
//            String[] arr1 = sorts.split(",");
//            String[] arr2 = orders.split(",");
//            StringBuffer oSb = new StringBuffer(where).append(" order by ");
//            for (int i = 0; i < arr1.length; i++) {
//                oSb.append(arr1[i]).append(("-1".equals(arr2[i]) ? " desc " : " asc ")).append(",");
//            }
//            sql.addVar("@b", oSb.substring(0, oSb.length() - 1));
//        } else {
//            sql.addVar("@b", where);
//        }
//        if (pageIndex != null && pageIndex > 0 && pagesize != null && pagesize > 0) {
//            // 查询总记录数
//            Sql countSql = DB.me().createSql(sqlName);
//            countSql.addVar("@a", "count(1)");
//            countSql.addVar("@b", where);
//            if (list != null) {
//                for (Object obj : list) {
//                    sql.addParamValue(obj);
//                    countSql.addParamValue(obj);
//                }
//            }
//            int total = DB.me().queryForInteger(dbName, countSql);
//            resp.setTotal(total);
//
//            // 分页查询
//            resp.setResult(DB.me().queryForBeanList(dbName, sql, cls, (pageIndex - 1) * pagesize, pagesize));
//        } else {
//            if (list != null) {
//                for (Object obj : list) {
//                    sql.addParamValue(obj);
//                }
//            }
//            List<?> ll = DB.me().queryForBeanList(dbName, sql, cls);
//            resp.setTotal(ll.size());
//            resp.setResult(ll);
//        }
//        return resp;
//    }
//    
//    
//    public RespList getRespListGroupBy(Class<?> cls, DatabaseEnum dbName, SqlNameEnum sqlName, ReqPage page,
//            String columns, String where, List<Object> list) throws SQLException {
//        RespList resp = new RespList();
//        Integer pageIndex = 0;
//        Integer pagesize = 0;
//        String sorts = null;
//        String orders = null;
//        if (page != null) {
//            pageIndex = page.getPageIndex();
//            pagesize = page.getPageSize();
//            sorts = page.getSorts();
//            orders = page.getOrders();
//        }
//        Sql sql = DB.me().createSql(sqlName);
//        sql.addVar("@a", columns);
//        if (StringUtil.isNotBlank(sorts)) {
//            // 排序控制
//            String[] arr1 = sorts.split(",");
//            String[] arr2 = orders.split(",");
//            StringBuffer oSb = new StringBuffer(where).append(" order by ");
//            for (int i = 0; i < arr1.length; i++) {
//                oSb.append(arr1[i]).append(("-1".equals(arr2[i]) ? " desc " : " asc ")).append(",");
//            }
//            sql.addVar("@b", oSb.substring(0, oSb.length() - 1));
//        } else {
//            sql.addVar("@b", where);
//        }
//        if (pageIndex != null && pageIndex > 0 && pagesize != null && pagesize > 0) {
//            // 查询总记录数
//            Sql countSql = DB.me().createSql(sqlName);
//            countSql.addVar("@a", columns);
//            countSql.addVar("@b", where);
//            if (list != null) {
//                for (Object obj : list) {
//                    sql.addParamValue(obj);
//                    countSql.addParamValue(obj);
//                }
//            }
//            countSql = new Sql("select count(1) from (" + countSql.getSqlString() + ") a");
//            int total = DB.me().queryForInteger(dbName, countSql);
//            resp.setTotal(total);
//            
//            // 分页查询
//            resp.setResult(DB.me().queryForBeanList(dbName, sql, cls, (pageIndex - 1) * pagesize, pagesize));
//        } else {
//            if (list != null) {
//                for (Object obj : list) {
//                    sql.addParamValue(obj);
//                }
//            }
//            List<?> ll = DB.me().queryForBeanList(dbName, sql, cls);
//            resp.setTotal(ll.size());
//            resp.setResult(ll);
//        }
//        return resp;
//    }
//    
//    public RespList getRespListGroupBy(Class<?> cls,SqlNameEnum sqlName, ReqPage page,
//            String columns, String where, List<Object> list) throws SQLException{
//        return getRespListGroupBy(cls, baseDaoEnum, sqlName, page, columns, where, list);
//    }
//
//    
//    public boolean isExists(SqlNameEnum sqlName, String where, Object... objs) throws SQLException {
//        return isExists(baseDaoEnum, sqlName, where, objs);
//    }
//
//    
//    public boolean isExists(DatabaseEnum dbName, SqlNameEnum sqlName, String where, Object... objs)
//            throws SQLException {
//        int c = getCount(dbName, sqlName, where, objs);
//        if (c > 0) {
//            return true;
//        }
//        return false;
//    }
//
//    
//    public int getCount(DatabaseEnum dbName, SqlNameEnum sqlName, String where, List<Object> list)
//            throws SQLException {
//        Sql sql = DB.me().createSql(sqlName);
//        sql.addVar("@a", "count(1)");
//        sql.addVar("@b", where);
//        for (Object object : list) {
//            sql.addParamValue(object);
//        }
//        return DB.me().queryForInteger(dbName, sql);
//    }
//
//    
//    public int getCount(SqlNameEnum sqlName, String where, List<Object> list) throws SQLException {
//
//        return getCount(baseDaoEnum, sqlName, where, list);
//    }
//
//    
//    public String getString(DatabaseEnum dbName, SqlNameEnum sqlName, String columns, String where,
//            List<Object> list) throws SQLException {
//        Sql sql = DB.me().createSql(sqlName);
//        sql.addVar("@a", columns);
//        sql.addVar("@b", where);
//        for (Object object : list) {
//            sql.addParamValue(object);
//        }
//        return DB.me().queryForString(dbName, sql);
//    }
//
//    
//    public String getString(DatabaseEnum dbName, SqlNameEnum sqlName, String columns, String where, Object... objs)
//            throws SQLException {
//        Sql sql = DB.me().createSql(sqlName);
//        sql.addVar("@a", columns);
//        sql.addVar("@b", where);
//        for (Object object : objs) {
//            sql.addParamValue(object);
//        }
//        return DB.me().queryForString(dbName, sql);
//    }
//
//    
//    public String getString(SqlNameEnum sqlName, String columns, String where, List<Object> list) throws SQLException {
//        return getString(baseDaoEnum, sqlName, columns, where, list);
//    }
//
//    
//    public String getString(SqlNameEnum sqlName, String columns, String where, Object... objs) throws SQLException {
//        return getString(baseDaoEnum, sqlName, columns, where, objs);
//    }
//
//    
//    public Object getObject(Class<?> cls, DatabaseEnum dbName, SqlNameEnum sqlName, String columns, String where,
//            List<Object> list) throws SQLException {
//        Sql sql = DB.me().createSql(sqlName);
//        sql.addVar("@a", columns);
//        sql.addVar("@b", where);
//        for (Object obj : list) {
//            sql.addParamValue(obj);
//        }
//        return DB.me().queryForBean(dbName, sql, cls);
//    }
//
//    
//    public Object getObject(Class<?> cls, SqlNameEnum sqlName, String columns, String where, List<Object> list)
//            throws SQLException {
//        return getObject(cls, baseDaoEnum, sqlName, columns, where, list);
//    }
//
//    
//    public Object getObject(Class<?> cls, DatabaseEnum dbName, SqlNameEnum sqlName, String columns, String where,
//            Object... objs) throws SQLException {
//        Sql sql = DB.me().createSql(sqlName);
//        sql.addVar("@a", columns);
//        sql.addVar("@b", where);
//        for (Object obj : objs) {
//            sql.addParamValue(obj);
//        }
//        return DB.me().queryForBean(dbName, sql, cls);
//    }
//
//    
//    public Object getObject(Class<?> cls, SqlNameEnum sqlName, String columns, String where, Object... objs)
//            throws SQLException {
//        return getObject(cls, baseDaoEnum, sqlName, columns, where, objs);
//    }
//
//    
//    public List<T> getList(Class<T> cls, SqlNameEnum sqlName, ReqPage page, String columns, String where,
//            List<Object> list) throws SQLException {
//        return getList(cls, baseDaoEnum, sqlName, page, columns, where, list);
//    }
//
//    
//    public List<T> getList(Class<T> cls, DatabaseEnum dbName, SqlNameEnum sqlName, ReqPage page,
//            String columns, String where, List<Object> list) throws SQLException {
//        Integer pageIndex = 0;
//        Integer pagesize = 0;
//        String sorts = null;
//        String orders = null;
//        if (page != null) {
//            pageIndex = page.getPageIndex();
//            pagesize = page.getPageSize();
//            sorts = page.getSorts();
//            orders = page.getOrders();
//        }
//        Sql sql = DB.me().createSql(sqlName);
//        sql.addVar("@a", columns);
//        if (StringUtil.isNotBlank(sorts)) {
//            // 排序控制
//            String[] arr1 = sorts.split(",");
//            String[] arr2 = orders.split(",");
//            StringBuffer oSb = new StringBuffer(where).append(" order by ");
//            for (int i = 0; i < arr1.length; i++) {
//                oSb.append(arr1[i]).append(("-1".equals(arr2[i]) ? " desc " : " asc ")).append(",");
//            }
//            sql.addVar("@b", oSb.substring(0, oSb.length() - 1));
//        } else {
//            sql.addVar("@b", where);
//        }
//        if (pageIndex != null && pageIndex > 0 && pagesize != null && pagesize > 0) {
//            if (list != null) {
//                for (Object obj : list) {
//                    sql.addParamValue(obj);
//                }
//            }
//            // 分页查询
//            return DB.me().queryForBeanList(dbName, sql, cls, (pageIndex - 1) * pagesize, pagesize);
//        } else {
//            if (list != null) {
//                for (Object obj : list) {
//                    sql.addParamValue(obj);
//                }
//            }
//            return DB.me().queryForBeanList(dbName, sql, cls);
//        }
//    }
//
//    
//    public List<T> getList(Class<T> cls, DatabaseEnum dbName, SqlNameEnum sqlName, ReqPage page,
//            String columns, String where, Object... objs) throws SQLException {
//        Integer pageIndex = 0;
//        Integer pagesize = 0;
//        String sorts = null;
//        String orders = null;
//        if (page != null) {
//            pageIndex = page.getPageIndex();
//            pagesize = page.getPageSize();
//            sorts = page.getSorts();
//            orders = page.getOrders();
//        }
//        Sql sql = DB.me().createSql(sqlName);
//        sql.addVar("@a", columns);
//        if (StringUtil.isNotBlank(sorts)) {
//            // 排序控制
//            String[] arr1 = sorts.split(",");
//            String[] arr2 = orders.split(",");
//            StringBuffer oSb = new StringBuffer(where).append(" order by ");
//            for (int i = 0; i < arr1.length; i++) {
//                oSb.append(arr1[i]).append(("-1".equals(arr2[i]) ? " desc " : " asc ")).append(",");
//            }
//            sql.addVar("@b", oSb.substring(0, oSb.length() - 1));
//        } else {
//            sql.addVar("@b", where);
//        }
//        if (pageIndex != null && pageIndex > 0 && pagesize != null && pagesize > 0) {
//            if (objs != null) {
//                for (Object obj : objs) {
//                    sql.addParamValue(obj);
//                }
//            }
//            // 分页查询
//            return DB.me().queryForBeanList(dbName, sql, cls, (pageIndex - 1) * pagesize, pagesize);
//        } else {
//            if (objs != null) {
//                for (Object obj : objs) {
//                    sql.addParamValue(obj);
//                }
//            }
//            return DB.me().queryForBeanList(dbName, sql, cls);
//        }
//    }
//
//    
//    public List<T> getList(Class<T> cls, SqlNameEnum sqlName, ReqPage page, String columns, String where,
//            Object... objs) throws SQLException {
//
//        return getList(cls, baseDaoEnum, sqlName, page, columns, where, objs);
//    }
//
//    
//    public Map<String, Object> getMap(DatabaseEnum dbName, SqlNameEnum sqlName, String columns, String where,
//            List<Object> list) throws SQLException {
//        Sql sql = DB.me().createSql(sqlName);
//        sql.addVar("@a", columns);
//        sql.addVar("@b", where);
//        if (list != null) {
//            for (Object object : list) {
//                sql.addParamValue(object);
//            }
//        }
//        return DB.me().queryForMap(dbName, sql);
//    }
//
//    
//    public Map<String, Object> getMap(DatabaseEnum dbName, SqlNameEnum sqlName, String columns, String where,
//            Object... objs) throws SQLException {
//        Sql sql = DB.me().createSql(sqlName);
//        sql.addVar("@a", columns);
//        sql.addVar("@b", where);
//        if (objs != null) {
//            for (Object object : objs) {
//                sql.addParamValue(object);
//            }
//        }
//        return DB.me().queryForMap(dbName, sql);
//    }
//
//    
//    public Map<String, Object> getMap(SqlNameEnum sqlName, String columns, String where, List<Object> list)
//            throws SQLException {
//        return getMap(baseDaoEnum, sqlName, columns, where, list);
//    }
//
//    
//    public Map<String, Object> getMap(SqlNameEnum sqlName, String columns, String where, Object... objs)
//            throws SQLException {
//        return getMap(baseDaoEnum, sqlName, columns, where, objs);
//    }
//
//    
//    public List<Map<String, Object>> getMapList(DatabaseEnum dbName, SqlNameEnum sqlName, String columns,
//            String where, List<Object> list) throws SQLException {
//        Sql sql = DB.me().createSql(sqlName);
//        sql.addVar("@a", columns);
//        sql.addVar("@b", where);
//        if (list != null) {
//            for (Object object : list) {
//                sql.addParamValue(object);
//            }
//        }
//        return DB.me().queryForMapList(dbName, sql);
//    }
//
//    
//    public List<Map<String, Object>> getMapList(DatabaseEnum dbName, SqlNameEnum sqlName, String columns,
//            String where, Object... objs) throws SQLException {
//        Sql sql = DB.me().createSql(sqlName);
//        sql.addVar("@a", columns);
//        sql.addVar("@b", where);
//        if (objs != null) {
//            for (Object object : objs) {
//                sql.addParamValue(object);
//            }
//        }
//        return DB.me().queryForMapList(dbName, sql);
//    }
//
//    
//    public List<Map<String, Object>> getMapList(SqlNameEnum sqlName, String columns, String where, List<Object> list)
//            throws SQLException {
//
//        return getMapList(baseDaoEnum, sqlName, columns, where, list);
//    }
//
//    
//    public List<Map<String, Object>> getMapList(SqlNameEnum sqlName, String columns, String where, Object... objs)
//            throws SQLException {
//
//        return getMapList(baseDaoEnum, sqlName, columns, where, objs);
//    }
//
//    
//    public List<Map<String, Object>> getMapList(DatabaseEnum dbName, SqlNameEnum sqlName, ReqPage page,
//            String columns, String where, List<Object> list) throws SQLException {
//        Integer pageIndex = 0;
//        Integer pagesize = 0;
//        String sorts = null;
//        String orders = null;
//        if (page != null) {
//            pageIndex = page.getPageIndex();
//            pagesize = page.getPageSize();
//            sorts = page.getSorts();
//            orders = page.getOrders();
//        }
//        Sql sql = DB.me().createSql(sqlName);
//        sql.addVar("@a", columns);
//        if (StringUtil.isNotBlank(sorts)) {
//            // 排序控制
//            String[] arr1 = sorts.split(",");
//            String[] arr2 = orders.split(",");
//            StringBuffer oSb = new StringBuffer(where).append(" order by ");
//            for (int i = 0; i < arr1.length; i++) {
//                oSb.append(arr1[i]).append(("-1".equals(arr2[i]) ? " desc " : " asc ")).append(",");
//            }
//            sql.addVar("@b", oSb.substring(0, oSb.length() - 1));
//        } else {
//            sql.addVar("@b", where);
//        }
//        if (pageIndex != null && pageIndex > 0 && pagesize != null && pagesize > 0) {
//            if (list != null) {
//                for (Object obj : list) {
//                    sql.addParamValue(obj);
//                }
//            }
//            // 分页查询
//            return DB.me().queryForMapList(dbName, sql, (pageIndex - 1) * pagesize, pagesize);
//        } else {
//            if (list != null) {
//                for (Object obj : list) {
//                    sql.addParamValue(obj);
//                }
//            }
//            return DB.me().queryForMapList(dbName, sql);
//        }
//    }
//
//    
//    public List<Map<String, Object>> getMapList(DatabaseEnum dbName, SqlNameEnum sqlName, ReqPage page,
//            String columns, String where, Object... objs) throws SQLException {
//        Integer pageIndex = 0;
//        Integer pagesize = 0;
//        String sorts = null;
//        String orders = null;
//        if (page != null) {
//            pageIndex = page.getPageIndex();
//            pagesize = page.getPageSize();
//            sorts = page.getSorts();
//            orders = page.getOrders();
//        }
//        Sql sql = DB.me().createSql(sqlName);
//        sql.addVar("@a", columns);
//        if (StringUtil.isNotBlank(sorts)) {
//            // 排序控制
//            String[] arr1 = sorts.split(",");
//            String[] arr2 = orders.split(",");
//            StringBuffer oSb = new StringBuffer(where).append(" order by ");
//            for (int i = 0; i < arr1.length; i++) {
//                oSb.append(arr1[i]).append(("-1".equals(arr2[i]) ? " desc " : " asc ")).append(",");
//            }
//            sql.addVar("@b", oSb.substring(0, oSb.length() - 1));
//        } else {
//            sql.addVar("@b", where);
//        }
//        if (pageIndex != null && pageIndex > 0 && pagesize != null && pagesize > 0) {
//            if (objs != null) {
//                for (Object obj : objs) {
//                    sql.addParamValue(obj);
//                }
//            }
//            // 分页查询
//            return DB.me().queryForMapList(dbName, sql, (pageIndex - 1) * pagesize, pagesize);
//        } else {
//            if (objs != null) {
//                for (Object obj : objs) {
//                    sql.addParamValue(obj);
//                }
//            }
//            return DB.me().queryForMapList(dbName, sql);
//        }
//    }
//
//    
//    public List<Map<String, Object>> getMapList(SqlNameEnum sqlName, ReqPage page, String columns, String where,
//            List<Object> list) throws SQLException {
//
//        return getMapList(baseDaoEnum, sqlName, page, columns, where, list);
//    }
//
//    
//    public List<Map<String, Object>> getMapList(SqlNameEnum sqlName, ReqPage page, String columns, String where,
//            Object... objs) throws SQLException {
//        return getMapList(baseDaoEnum, sqlName, page, columns, where, objs);
//    }
//
//    
//    public int getCount(DatabaseEnum dbName, SqlNameEnum sqlName, String columns, String where, List<Object> list)
//            throws SQLException {
//        Sql sql = DB.me().createSql(sqlName);
//        sql.addVar("@a", columns);
//        sql.addVar("@b", where);
//        for (Object object : list) {
//            sql.addParamValue(object);
//        }
//        return DB.me().queryForInteger(dbName, sql);
//    }
//
//    
//    public int getCount(SqlNameEnum sqlName, String columns, String where, List<Object> ll) throws SQLException {
//        return getCount(baseDaoEnum, sqlName, columns, where, ll);
//    }
//
//    
//    public List<T> getList(Class<T> cls, DatabaseEnum dbName, Sql sql, ReqPage page) throws SQLException {
//        Integer pageIndex = 0;
//        Integer pagesize = 0;
//        String sorts = null;
//        String orders = null;
//        if (page != null) {
//            pageIndex = page.getPageIndex();
//            pagesize = page.getPageSize();
//            sorts = page.getSorts();
//            orders = page.getOrders();
//        }
//        if (StringUtil.isNotBlank(sorts)) {
//            // 排序控制
//            String[] arr1 = sorts.split(",");
//            String[] arr2 = orders.split(",");
//            StringBuffer oSb = new StringBuffer(" order by ");
//            for (int i = 0; i < arr1.length; i++) {
//                oSb.append(arr1[i]).append(("-1".equals(arr2[i]) ? " desc " : " asc ")).append(",");
//            }
//            sql.setSqlString(sql.getSqlString() + oSb.substring(0, oSb.length() - 1));
//        }
//        if (pageIndex != null && pageIndex > 0 && pagesize != null && pagesize > 0) {
//            // 分页查询
//            return DB.me().queryForBeanList(dbName, sql, cls, (pageIndex - 1) * pagesize, pagesize);
//        } else {
//            return DB.me().queryForBeanList(dbName, sql, cls);
//        }
//    }
//
//    
//    public List<T> getList(Class<T> cls, Sql sql, ReqPage page) throws SQLException {
//        return getList(cls, baseDaoEnum, sql, page);
//    }
//
//    
//    public RespList getRespList(Class<?> cls, DatabaseEnum dbName, Sql sql, ReqPage page) throws SQLException {
//        RespList resp = new RespList();
//        Integer pageIndex = 0;
//        Integer pagesize = 0;
//        String sorts = null;
//        String orders = null;
//        if (page != null) {
//            pageIndex = page.getPageIndex();
//            pagesize = page.getPageSize();
//            sorts = page.getSorts();
//            orders = page.getOrders();
//        }
//        if (pageIndex != null && pageIndex > 0 && pagesize != null && pagesize > 0) {
//            // 查询总记录数
//            Sql countSql = new Sql("select count(1) from (" + sql.getSqlString() + ") a");
//            countSql.setParamValueList(sql.getParamValueList());
//            int total = DB.me().queryForInteger(dbName, countSql);
//            resp.setTotal(total);
//
//            // 分页查询
//            if (StringUtil.isNotBlank(sorts)) {
//                // 排序控制
//                String[] arr1 = sorts.split(",");
//                String[] arr2 = orders.split(",");
//                StringBuffer oSb = new StringBuffer(" order by ");
//                for (int i = 0; i < arr1.length; i++) {
//                    oSb.append(arr1[i]).append(("-1".equals(arr2[i]) ? " desc " : " asc ")).append(",");
//                }
//                sql.setSqlString(sql.getSqlString() + oSb.substring(0, oSb.length() - 1));
//            }
//            resp.setResult(DB.me().queryForBeanList(dbName, sql, cls, (pageIndex - 1) * pagesize, pagesize));
//        } else {
//            if (StringUtil.isNotBlank(sorts)) {
//                // 排序控制
//                String[] arr1 = sorts.split(",");
//                String[] arr2 = orders.split(",");
//                StringBuffer oSb = new StringBuffer(" order by ");
//                for (int i = 0; i < arr1.length; i++) {
//                    oSb.append(arr1[i]).append(("-1".equals(arr2[i]) ? " desc " : " asc ")).append(",");
//                }
//                sql.setSqlString(sql.getSqlString() + oSb.substring(0, oSb.length() - 1));
//            }
//            List<?> list = DB.me().queryForBeanList(dbName, sql, cls);
//            resp.setTotal(list.size());
//            resp.setResult(list);
//        }
//        return resp;
//    }
//
//    
//    public RespList getRespList(Class<?> cls, Sql sql, ReqPage page) throws SQLException {
//        return getRespList(cls, baseDaoEnum, sql, page);
//    }
//
//}
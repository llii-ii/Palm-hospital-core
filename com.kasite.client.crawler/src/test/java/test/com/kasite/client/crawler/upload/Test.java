//package test.com.kasite.client.crawler.upload;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import javax.sql.DataSource;
//
//import org.apache.commons.dbcp.BasicDataSource;
//
//import com.coreframework.db.DB;
//import com.coreframework.db.Sql;
//import com.coreframework.util.JsonUtil;
//import com.kasite.client.crawler.config.CheckDictBuser;
//import com.kasite.client.crawler.config.data.vo.Data15PkVo;
//import com.kasite.client.crawler.modules.client.shangrao.shangraoshiliyiyuan.SRSLYYDatabaseEnum;
//import com.kasite.core.common.util.FileUtils;
//
//import ch.qos.logback.core.util.FileUtil;
//import net.sf.json.JSONObject;
//import oracle.jdbc.pool.OracleDataSource;
//import test.com.kasite.client.crawler.MyDatabaseEnum;
//
//public class Test {
//
//	/**
//	 * oracle数据库地址
//	 * 医院政务网IP：119.19.19.3:1521
//	 */
//	private final static String url="jdbc:oracle:thin:@//119.19.19.3:1521/hisdb";  
//    /**
//     * 数据库账号
//     */
//    private final static  String user="yihu";  
//    /**
//     * 数据库密码
//     */
//    private final static String password="yihu";  
//    /**
//     * 数据库类型
//     */
//    private final static String driver = "oracle.jdbc.driver.OracleDriver";
//    /**
//     * 获取数据库连接
//     * @return
//     */
//    public Connection getConnection(){  
//    	Connection conn = null;
//        try {
//            //初始化驱动包  
//            Class.forName(driver);  
//            //根据数据库连接字符，名称，密码给conn赋值  
//            conn = DriverManager.getConnection(url, user, password);  
//            return conn;
//        } catch (Exception e) {  
//            e.printStackTrace(); 
//        } 
//        return conn;
//    }
//	
//	
//	
//	/**
//	 * 测试连接是否正常
//	 */
//    private void netTest()throws Exception{
//    	Test test=new Test();  
//    	Connection  conn= test.getConnection();  
//    	if(conn==null){  
//            System.err.println("与oracle数据库连接失败！");  
//        }else{
//        	conn.close();
//            System.out.println("与oracle数据库连接成功！");  
//        }  
//    }
//	
//    
//	public List<JSONObject> queryPatientList(){
//		CheckDictBuser checkDictBuser = new CheckDictBuser();
//		Map<String, Data15PkVo> data15PkVo = checkDictBuser.getData15Map("data_V_1.50.xls");
//		List<JSONObject> PatientList = new ArrayList<JSONObject>();
//		Connection conn = null; 
//		Statement st = null;
//		ResultSet rs = null;
//		int page = 1;//页数
//		int rows = 1000;//每页显示行数
//		try {
//			for(int i = 0; i < page; i++) {
//				int rownum = page*rows+1;
//				int rn = (page-1)*rows;
//				StringBuffer sqlBuffer = new StringBuffer("select * from "
//						+ "(select a.*,rownum RN from (select * from srhis.HDSA00_01) A "
//						+ "where ROWNUM <"+rownum+")where RN>="+rn+"");
//		    	conn = getConnection(); 
//		        st = (Statement) conn.createStatement(); 
//		        rs = st.executeQuery(sqlBuffer.toString()); 
//		        if(rs.next()) {
//		        	page++;
//		        }
//		        while (rs.next()) { 
//		        	JSONObject json = new JSONObject();
//		        	
////					for (Map.Entry<String, Data15PkVo> voMap : data15PkVo.entrySet()) {
////						Data15PkVo vo = voMap.getValue();
////						
////						int iskey = vo.getIsKey();//主键(1主键;0非主键)
////						int isNotNum = vo.getIsNotNum();//是否可为空(0表示不可为空；1表示可为空)
////						String privateDes = vo.getPrivateDes();//数据元名称
////						String privateDictName = vo.getPrivateDictName();//术语范围值
////						String privateType = vo.getPrivateType();//列类型
////						String privateName = vo.getPrivateName();//内部标识
////						
////						Object value = rs.getObject(privateName);
////						
////						json.put(privateName, value);
////					}
//		        	
//		        	json.put("HDSA00_01_003",rs.getObject("HDSA00_01_003"));
//		        	json.put("JDSA00_01_001",rs.getObject("HDSA00_01_003"));
//		        	json.put("HDSA00_01_009",rs.getObject("HDSA00_01_009"));
//		        	json.put("HDSA00_01_016",rs.getObject("HDSA00_01_016"));
//		        	json.put("HDSA00_01_017",rs.getObject("HDSA00_01_017"));
//		        	json.put("HDSA00_01_011",rs.getObject("HDSA00_01_011"));
//		        	json.put("HDSA00_01_012",rs.getObject("HDSA00_01_012"));
//		        	json.put("HDSA00_11_051",rs.getObject("HDSA00_11_051"));
//		        	json.put("HDSA00_01_013",rs.getObject("HDSA00_01_013"));
//		        	json.put("HDSA00_01_014",rs.getObject("HDSA00_01_014"));
//		        	json.put("HDSA00_01_035",rs.getObject("HDSA00_01_035"));
//		        	json.put("JDSA00_01_002",rs.getObject("JDSA00_01_002"));
//		        	json.put("HDSA00_01_036",rs.getObject("HDSA00_01_036"));
//		        	json.put("JDSA00_01_003",rs.getObject("JDSA00_01_003"));
//		        	json.put("HDSA00_01_019",rs.getObject("HDSA00_01_019"));
//		        	json.put("HDSA00_01_021",rs.getObject("HDSA00_01_021"));
//		        	json.put("HDSA00_01_039",rs.getObject("HDSA00_01_039"));
//		        	json.put("HDSA00_01_038",rs.getObject("HDSA00_01_038"));
//		        	json.put("HDSA00_01_015",rs.getObject("HDSA00_01_015"));
//		        	json.put("HDSA00_01_037",rs.getObject("HDSA00_01_037"));
//		        	json.put("HDSA00_01_048",rs.getObject("HDSA00_01_048"));
//		        	json.put("HDSA00_01_049",rs.getObject("HDSA00_01_049"));
//		        	json.put("HDSA00_01_050",rs.getObject("HDSA00_01_050"));
//		        	json.put("HDSB01_01_001",rs.getObject("HDSB01_01_001"));
//		        	json.put("JDSA00_01_004",rs.getObject("JDSA00_01_004"));
//		        	json.put("JDSA00_01_005",rs.getObject("JDSA00_01_005"));
//		        	json.put("JDSA00_01_006",rs.getObject("JDSA00_01_006"));
//		        	json.put("JDSA00_01_007",rs.getObject("JDSA00_01_007"));
//		        	json.put("JDSA00_01_008",rs.getObject("JDSA00_01_008"));
//		        	json.put("JDSA00_01_009",rs.getObject("JDSA00_01_009"));
//		        	json.put("JDSA00_01_010",rs.getObject("JDSA00_01_010"));
//		        	json.put("JDSA00_01_011",rs.getObject("JDSA00_01_011"));
//		        	json.put("JDSA00_01_012",rs.getObject("JDSA00_01_012"));
//		        	json.put("JDSA00_01_013",rs.getObject("JDSA00_01_013"));
//		        	json.put("JDSA00_01_014",rs.getObject("JDSA00_01_014"));
//		        	json.put("JDSA00_01_015",rs.getObject("JDSA00_01_015"));
//		        	json.put("JDSA00_01_016",rs.getObject("JDSA00_01_016"));
//		        	json.put("JDSA00_01_017",rs.getObject("JDSA00_01_017"));
//		        	json.put("JDSA00_01_018",rs.getObject("JDSA00_01_018"));
//		        	json.put("JDSA00_01_019",rs.getObject("JDSA00_01_019"));
//		        	json.put("HDSD00_02_005",rs.getObject("HDSD00_02_005"));
//		        	json.put("HDSD00_02_019",rs.getObject("HDSD00_02_019"));
//		        	json.put("HDSD00_20_017",rs.getObject("HDSD00_20_017"));
//
//		        	
//		        	PatientList.add(json);
//		        }
//		        
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//	    }finally{
//	    	if(rs!=null){
//	    		try {
//					rs.close();
//				} catch (SQLException e) {
//				}
//	    	}
//	    	if(st!=null){
//	    		try {
//					st.close();
//				} catch (SQLException e) {
//				}
//	    	}
//	    	if(conn!=null){
//	    		try {
//					conn.close();//关闭数据库连接     
//				} catch (SQLException e) {
//				}
//	    	}
//	    }    
//		try {
////			FileUtil.writeText("D://HDSA00_01.txt", PatientList.toString());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return PatientList;
//	}
//    public static void installDataSource(String alias,String ip, String dbName) throws SQLException {
//    		BasicDataSource ds = new BasicDataSource();
//		ds.setMaxActive(150);
//		ds.setDriverClassName(driver);
//		boolean isOracle=false;
//		ds.setUrl(url);
//		ds.setValidationQuery("select 1 from dual");
//		ds.setUsername(user);
//		ds.setPassword(password);
//		ds.setMinIdle(4);
//		ds.setMaxIdle(100);
//		ds.setMaxWait(8000);
//		
//		ds.setTestOnBorrow(Boolean.getBoolean(alias+".testOnBorrow"));
//		ds.setTestOnReturn(Boolean.getBoolean(alias+".testOnReturn"));
//		ds.setTestWhileIdle(true);
//		ds.setRemoveAbandoned(true);
//		ds.setRemoveAbandonedTimeout(Integer.getInteger(alias+".abandonedTimeout", 30));
//		ds.setLogAbandoned(true);
//		ds.setTimeBetweenEvictionRunsMillis(Long.getLong("db.timeBetweenEvictionRunsMillis", 1000*60));
//		if(isOracle){
//			ds.setMinEvictableIdleTimeMillis(Long.getLong("db.oracle.minEvictableIdleTimeMillis", 1000*3600*2));
//		}else{
//			ds.setMinEvictableIdleTimeMillis(Long.getLong("db.minEvictableIdleTimeMillis", 1000*60));
//		}
//		
//		DB.me().addDataSource(alias, ds, ip, dbName);
//		ds.getConnection();
//		
//		
//    }
//    
//    
//    public static void main(String[] args) {
//		try {
////			Test test = new Test();
////			test.netTest();
//			installDataSource(SRSLYYDatabaseEnum.hisdb.name(), "119.19.19.3", "hisdb");
//			
//			System.out.println(DB.me().queryForJson(SRSLYYDatabaseEnum.hisdb, new
//					 Sql("select 1 from dual")));
//			
//			
//			
////			test.queryPatientList();
//			System.out.println("End");
//			
//			
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//}

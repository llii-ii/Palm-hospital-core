package test.com.kasite.client.crawler;

import org.springframework.boot.SpringApplication;

import com.coreframework.db.DB;
import com.coreframework.db.DatabaseEnum;
import com.coreframework.db.Sql;
import com.kasite.ServerConsole;
import com.kasite.client.crawler.config.MyDatabaseEnum;

public class TestDB {

	public static void main(String[] args) throws Exception {
		System.setProperty("DB_TIMEOUT","120000");
		System.setProperty(MyDatabaseEnum.hisdb.name()+".abandonedTimeout", "120");
		SpringApplication.run(ServerConsole.class, args);
		
		
		com.common.json.JSONObject queryJson = null;
		DatabaseEnum db = MyDatabaseEnum.hisdb;
		StringBuffer selectSql = new StringBuffer("select * from [Table]");
		try {
			System.out.println("开始查询");
			queryJson = DB.me().queryForJson(db, new Sql(selectSql.toString()));
			System.out.println("queryJson="+queryJson);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

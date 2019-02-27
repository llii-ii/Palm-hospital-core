package com.kasite.core.log;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.StringUtil;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.wxmsg.IDSeed;
import com.kasite.core.elastic.ElasticRestClientUtil;

public class EsUtil {
	protected static final Logger logger = LoggerFactory.getLogger(EsUtil.class);
	public static Map<String, String> esIndexsMap = new HashMap<>();
	public enum ESIndex{
		report("{\"aliases\":{},\"mappings\":{\"logtable\":{\"dynamic_date_formats\":[\"YYYY-MM-dd HH:mm:ss.SSS || YYYY-MM-dd HH:mm:ss\"],\"dynamic_templates\":[{\"_inserttime\":{\"match\":\"_inserttime\",\"mapping\":{\"format\":\"YYYY-MM-dd HH:mm:ss.SSS || YYYY-MM-dd HH:mm:ss\",\"type\":\"date\"}}}],\"properties\":{\"_tj_api\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"_tj_clientid\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"_tj_datetime\":{\"type\":\"date\",\"format\":\"YYYY-MM-dd\"},\"_inserttime\":{\"type\":\"date\",\"format\":\"YYYY-MM-dd HH:mm:ss.SSS || YYYY-MM-dd HH:mm:ss\"},\"_mills\":{\"type\":\"long\"},\"_tj_mills_0_100\":{\"type\":\"long\"},\"_tj_mills_100_300\":{\"type\":\"long\"},\"_tj_mills_300_500\":{\"type\":\"long\"},\"_tj_mills_500_1000\":{\"type\":\"long\"},\"_tj_mills_1000_3000\":{\"type\":\"long\"},\"_tj_mills_3000_8000\":{\"type\":\"long\"},\"_tj_mills_8000\":{\"type\":\"long\"},\"_tj_total\":{\"type\":\"long\"},\"_totalMills\":{\"type\":\"long\"},\"_tj_app_fail\":{\"type\":\"long\"},\"_tj_success_count\":{\"type\":\"long\"},\"_tj_exec_success_count\":{\"type\":\"long\"},\"_tj_exec_success_mills\":{\"type\":\"long\"},\"_tj_fail\":{\"type\":\"long\"}}}},\"settings\":{\"index\":{\"search.slowlog.threshold.query.info\":\"1s\",\"search.slowlog.threshold.fetch.info\":\"1s\",\"number_of_shards\":2,\"number_of_replicas\":0,\"refresh_interval\":\"20s\"},\"translog\":{\"durability\":\"ASYNC\",\"sync_interval\":\"10s\"},\"analysis\":{\"normalizer\":{\"lowerkey\":{\"filter\":[\"lowercase\"],\"type\":\"custom\",\"char_filter\":[]}},\"analyzer\":{\"whitespaceText\":{\"filter\":\"lowercase\",\"type\":\"custom\",\"tokenizer\":\"whitespace\"}}}}}"),
		diy("{\"aliases\":{},\"mappings\":{\"logtable\":{\"dynamic_date_formats\":[\"YYYY-MM-dd HH:mm:ss.SSS || YYYY-MM-dd HH:mm:ss\"],\"dynamic_templates\":[{\"appName\":{\"match\":\"appName\",\"mapping\":{\"normalizer\":\"lowerkey\",\"type\":\"keyword\"}}},{\"ip\":{\"match\":\"ip\",\"mapping\":{\"normalizer\":\"lowerkey\",\"type\":\"keyword\"}}},{\"sysId\":{\"match\":\"sysId\",\"mapping\":{\"normalizer\":\"lowerkey\",\"type\":\"keyword\"}}},{\"appId\":{\"match\":\"appId\",\"mapping\":{\"normalizer\":\"lowerkey\",\"type\":\"keyword\"}}},{\"licenseKey\":{\"match\":\"licenseKey\",\"mapping\":{\"normalizer\":\"lowerkey\",\"type\":\"keyword\"}}},{\"_inserttime\":{\"match\":\"_inserttime\",\"mapping\":{\"format\":\"YYYY-MM-dd HH:mm:ss.SSS || YYYY-MM-dd HH:mm:ss\",\"type\":\"date\"}}},{\"createDate\":{\"match\":\"createDate\",\"mapping\":{\"format\":\"YYYY-MM-dd HH:mm:ss.SSS || YYYY-MM-dd HH:mm:ss\",\"type\":\"date\"}}},{\"gid\":{\"match\":\"gid\",\"mapping\":{\"analyzer\":\"whitespaceText\",\"type\":\"text\"}}}],\"properties\":{\"_appname\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"_classname\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"_inserttime\":{\"type\":\"date\",\"format\":\"YYYY-MM-dd HH:mm:ss.SSS || YYYY-MM-dd HH:mm:ss\"},\"_ip\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"_level\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"_linenumber\":{\"type\":\"integer\"},\"_methodname\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"_modulename\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"content\":{\"type\":\"text\"},\"gid\":{\"type\":\"text\",\"analyzer\":\"whitespaceText\"},\"licenseKey\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"_sessionkey\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"_sign\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"_configkey\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"_clientid\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"}}}},\"settings\":{\"index\":{\"search.slowlog.threshold.query.info\":\"1s\",\"search.slowlog.threshold.fetch.info\":\"1s\",\"number_of_shards\":2,\"number_of_replicas\":0,\"refresh_interval\":\"20s\"},\"translog\":{\"durability\":\"ASYNC\",\"sync_interval\":\"10s\"},\"analysis\":{\"normalizer\":{\"lowerkey\":{\"filter\":[\"lowercase\"],\"type\":\"custom\",\"char_filter\":[]}},\"analyzer\":{\"whitespaceText\":{\"filter\":\"lowercase\",\"type\":\"custom\",\"tokenizer\":\"whitespace\"}}}}}"),
		req("{\"aliases\":{},\"mappings\":{\"logtable\":{\"dynamic_date_formats\":[\"YYYY-MM-dd HH:mm:ss.SSS || YYYY-MM-dd HH:mm:ss\"],\"dynamic_templates\":[{\"appName\":{\"match\":\"appName\",\"mapping\":{\"normalizer\":\"lowerkey\",\"type\":\"keyword\"}}},{\"ip\":{\"match\":\"ip\",\"mapping\":{\"normalizer\":\"lowerkey\",\"type\":\"keyword\"}}},{\"sysId\":{\"match\":\"sysId\",\"mapping\":{\"normalizer\":\"lowerkey\",\"type\":\"keyword\"}}},{\"appId\":{\"match\":\"appId\",\"mapping\":{\"normalizer\":\"lowerkey\",\"type\":\"keyword\"}}},{\"licenseKey\":{\"match\":\"licenseKey\",\"mapping\":{\"normalizer\":\"lowerkey\",\"type\":\"keyword\"}}},{\"_inserttime\":{\"match\":\"_inserttime\",\"mapping\":{\"format\":\"YYYY-MM-dd HH:mm:ss.SSS || YYYY-MM-dd HH:mm:ss\",\"type\":\"date\"}}},{\"createDate\":{\"match\":\"createDate\",\"mapping\":{\"format\":\"YYYY-MM-dd HH:mm:ss.SSS || YYYY-MM-dd HH:mm:ss\",\"type\":\"date\"}}},{\"gid\":{\"match\":\"gid\",\"mapping\":{\"analyzer\":\"whitespaceText\",\"type\":\"text\"}}}],\"properties\":{\"_api\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"_classname\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"_clientid\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"_clienturl\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"_clientversion\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"_code\":{\"type\":\"long\"},\"_inserttime\":{\"type\":\"date\",\"format\":\"YYYY-MM-dd HH:mm:ss.SSS || YYYY-MM-dd HH:mm:ss\"},\"_message\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"_methodname\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"_mills\":{\"type\":\"long\"},\"_outtype\":{\"type\":\"integer\"},\"_paramtype\":{\"type\":\"integer\"},\"_sequenceno\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"_sessionkey\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"_sign\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"_uniquereqid\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"_url\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"_v\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"_wsgwurl\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"gid\":{\"type\":\"text\",\"analyzer\":\"whitespaceText\"},\"licenseKey\":{\"type\":\"keyword\",\"normalizer\":\"lowerkey\"},\"param\":{\"type\":\"text\"},\"resp_mills\":{\"type\":\"long\"},\"result\":{\"type\":\"text\"}}}},\"settings\":{\"index\":{\"search.slowlog.threshold.query.info\":\"1s\",\"search.slowlog.threshold.fetch.info\":\"1s\",\"number_of_shards\":2,\"number_of_replicas\":0,\"refresh_interval\":\"20s\"},\"translog\":{\"durability\":\"ASYNC\",\"sync_interval\":\"10s\"},\"analysis\":{\"normalizer\":{\"lowerkey\":{\"filter\":[\"lowercase\"],\"type\":\"custom\",\"char_filter\":[]}},\"analyzer\":{\"whitespaceText\":{\"filter\":\"lowercase\",\"type\":\"custom\",\"tokenizer\":\"whitespace\"}}}}}"),
		;
		private String script;
		ESIndex(String script) {
			this.script = script;
		}
		public String getScript() {
			return this.script;
		}
	}
	public static boolean existIndex(String name) throws Exception {
		 return ElasticRestClientUtil.getInstall(KasiteConfig.getESUrl()).isExists(name);
	}
	/**
	 * 创建表索引对象
	 * @throws Exception
	 * @throws URISyntaxException
	 */
	public static boolean createESTable(String tablename,String script) throws Exception, URISyntaxException {
		return ElasticRestClientUtil.getInstall(KasiteConfig.getESUrl()).createIndex(tablename, script);
	}
	
	
	private static boolean createDiyTable(String tableName,ESIndex index) throws Exception {
		return createESTable(tableName, index.getScript());
	}
	
	public static void insertReqBatch(List<JSONObject> jsons) {
		
		
	}
	private static final String DATEFORMAT = "MMdd";
	/**
	 * 通过配置到ES枚举判断 ES数据库里是否有存在，如果不存在则新建一个如果存在则返回
	 * @param index
	 * @return
	 */
	public static String getEsIndex(ESIndex index,String date) {
		try {
			String indexDate = DateOper.getNow(DATEFORMAT);
			if(StringUtil.isNotBlank(date)) {
				indexDate = date.substring(5, 7) + date.substring(8, 10);
			}
			String diyMoth = index.name()+indexDate;
			String v = esIndexsMap.get(diyMoth);
			if(null == v) {
				//判断数据库中是否存在这张表如果存在则保存到缓存中，如果不存在则新增一张
				if(!existIndex(diyMoth)) {
					if(!createDiyTable(diyMoth, index)) {
						throw new RRException("新建表："+diyMoth+"失败。"); 
					}else {
						esIndexsMap.put(diyMoth, System.currentTimeMillis()+"");
					}
				}else {
					esIndexsMap.put(diyMoth, System.currentTimeMillis()+"");
				}
			}
			return diyMoth;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新建ES库表异常："+index,e);
		}
		return "diy00";
	}
	
	/**
	 * 数据新增到ES数据库
	 * @param list
	 * @throws Exception 
	 */
	public static void insertBatch(Collection<JSONObject> list,ESIndex index) throws Exception {
		long now=System.currentTimeMillis();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (JSONObject json : list) {
			try {
				String d = json.getString(LogKey.inserttime);
				if(null == d) {
					d = DateOper.getNow("yyyy-MM-dd HH:mm:ss");
				}
				Date date;
				try {
					date = f.parse(d);
				} catch (ParseException e) {
					e.printStackTrace();
					continue;
				}
				if(Math.abs(now-date.getTime())>1000*3600*24*15){
					KasiteConfig.print(date+" is too far away from now,so discard this push log");
					continue;
				}
				json.put(LogKey.inserttime, f.format(date));
				ElasticRestClientUtil.getInstall(KasiteConfig.getESUrl()).insert(getEsIndex(index,null), json, IDSeed.next());
			}catch (Exception e) {
				e.printStackTrace();
				logger.error("es 执行插入异常："+json.toJSONString(),e);
			}
		}
	}
//	public static void insertAsyncBatch(List<JSONObject> list) {
//		long now=System.currentTimeMillis();
//		SimpleDateFormat f= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		for (JSONObject json : list) {
//			try {
//				String d=json.getString("SendTime");
//				Date date;
//				try {
//					date = f.parse(d);
//				} catch (ParseException e) {
//					e.printStackTrace();
//					continue;
//				}
//				if(Math.abs(now-date.getTime())>1000*3600*24*15){
//					KasiteConfig.print(date+" is too far away from now,so discard this async log");
//					continue;
//				}
//				json.put(LogKey.inserttime, f.format(date));
//				ElasticRestClientUtil.getInstall(KasiteConfig.getESUrl()).insert(getEsIndex(ESIndex.diy,null), json, IDSeed.next());
//			}catch (Exception e) {
//				e.printStackTrace();
//				logger.error("es 执行插入异常："+json.toJSONString(),e);
//			}
//		}
//	}
	/**
	 * 调用日志
	 */
	public static String ES_INDEX_REQ = "req";//使用别名
	public static String ES_TYPE_REQ = "logtable";
	/**
	 * 异步调用日志
	 */
	public static String ES_INDEX_ASYN = "asyncbus_";//使用别名
	public static String ES_TYPE_ASYN = "logtable";
	/**
	 * 自定义日志
	 */
	public static String ES_INDEX_DIY = "diy";//使用别名
	public static String ES_TYPE_DIY = "logtable";	

	/**
	 * 数据库日志
	 */
	public static String ES_INDEX_DB = "dblog";//使用别名
	public static String ES_TYPE_DB = "logtable";
	
	/**
	 * JVM日志 
	 */
	public static String ES_INDEX_JVM = "jvm";//使用别名
	public static String ES_TYPE_JVM = "logtable";
	
	/**
	 * SQL日志(通用版) 
	 */
	public static String ES_INDEX_SQL = "sql";//使用别名
	public static String ES_TYPE_SQL = "logtable";
	
	/**
	 * WEB请求 
	 */
	public static String ES_INDEX_WEB = "web";//使用别名
	public static String ES_TYPE_WEB = "logtable";

}

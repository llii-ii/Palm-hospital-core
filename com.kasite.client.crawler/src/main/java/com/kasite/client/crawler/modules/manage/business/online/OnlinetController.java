package com.kasite.client.crawler.modules.manage.business.online;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kasite.client.crawler.config.ConfigBuser;
import com.kasite.client.crawler.config.Convent;
import com.kasite.client.crawler.config.DataCloudConfig;
import com.kasite.client.crawler.config.XMDataCloudConfig;
import com.kasite.client.crawler.modules.manage.bean.online.dbo.Online;
import com.kasite.client.crawler.modules.manage.bean.standard.dbo.Standard;
import com.kasite.client.crawler.modules.manage.dao.online.OnlineDao;
import com.kasite.client.crawler.modules.manage.dao.standard.StandardDao;
import com.kasite.client.crawler.modules.upload.datacloud.DataCloudFileUtil;
import com.kasite.client.crawler.modules.utils.DateUtils;
import com.kasite.client.crawler.modules.utils.FileOper;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 设备在线情况控制类
 * 
 * @author 無
 * @version V1.0
 * @date 2018年6月21日 上午11:50:32
 */
@Controller
@RequestMapping(value = "/online")
public class OnlinetController {
	public static Map<String, Map<String ,Object>> hosMap = new HashMap<String, Map<String ,Object>>();
	public static Map<String, Map<String ,String>> sqlMap = new HashMap<String, Map<String ,String>>();
	private final static Log log = LogFactory.getLog(OnlinetController.class);
	@Autowired
	private XMDataCloudConfig xMDataCloudConfig;
	@Autowired
	private DataCloudConfig dataCloudConfig;
	private Map<String, String> judgeMap = new HashMap<>();

	//页面显示数据，包括分页、查询
	@RequestMapping(value = "/getOnlineList.do")
	@ResponseBody
	public String getOnlineList(HttpServletRequest request, HttpServletResponse response,Online online) {
		//获取前端传上来的所有数据
		Integer pageSize = StringUtil.intEmptyToNull(request.getParameter("PageSize"));//每页显示条数
		Integer pageStart = StringUtil.intEmptyToNull(request.getParameter("PageStart"));//当前页
		JSONObject json = null;
		try {
			json = OnlineDao.getOnlineList(online,pageStart,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return CommonUtil.getRetVal(RetCode.Success.RET_10000.getCode(), RetCode.Success.RET_10000.getMessage(), json);
	}
	
	
	//获得所有的医院
	@RequestMapping(value = "/getAllHos.do")
	@ResponseBody
	public String getAllHos(HttpServletRequest request, HttpServletResponse response) {
		String county = request.getParameter("county");
		String name = request.getParameter("name");
		//获取前端传上来的所有数据
		JSONObject json = null;
		try {
			json = OnlineDao.getAllHos(county,name);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return CommonUtil.getRetVal(RetCode.Success.RET_10000.getCode(), RetCode.Success.RET_10000.getMessage(), json);
	}
	
	
	@RequestMapping(value = "/onlineDetailById.do")
	@ResponseBody
	public String onlineDetailById(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		Integer pageSize = StringUtil.intEmptyToNull(request.getParameter("PageSize"));
		Integer pageStart = StringUtil.intEmptyToNull(request.getParameter("PageStart"));
		JSONObject json = null;
		try {
			json = OnlineDao.onlineDetailById(id,pageStart,pageSize,startTime,endTime);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return CommonUtil.getRetVal(RetCode.Success.RET_10000.getCode(), RetCode.Success.RET_10000.getMessage(), json);
	}
	
	/**
	 * 执行sqlStr语句-直连
	 *
	 * @param sqlStr
	 * @return
	 * @author 無
	 * @date 2018年6月25日 上午10:08:48
	 */
	@RequestMapping(value = "/sql.do")
	@ResponseBody
	public String sendSql(HttpServletRequest request, HttpServletResponse response) {
		String hosId = request.getParameter("id");
		String sqlStr = request.getParameter("sql");
		String db = request.getParameter("db");
		int PageStart = Integer.parseInt(request.getParameter("PageStart"));
		int pageSize = Integer.parseInt(request.getParameter("PageSize"));
		JSONObject json = null;
		if(StringUtil.isNotBlank(sqlStr)) {
			try {
				Map<String, Map<String, Object>> hosMap = getHosMap();
				Map<String, Object> hos = hosMap.get(hosId);
				if(null==hos){
					return CommonUtil.getRetVal(-1, "请刷新页面后重试！", json);
				}
				SoapResponseVo respon = HttpRequestBus.create(hos.get("url")+"job/sql", RequestType.get)
						.addHttpParam("sqlStr", sqlStr)
						.addHttpParam("PageStart", PageStart+"")
						.addHttpParam("pageSize", pageSize+"")
						.addHttpParam("db", db)
						.send();
				json = JSONObject.fromObject(respon.getResult());
				if(null == json.get("result") && null !=json.get("msg")){
					return CommonUtil.getRetVal(RetCode.Common.ERROR_SYSTEM.getCode(), json.get("msg").toString(), new JSONObject());
				}
				json = JSONObject.fromObject(json.get("result"));
			}catch (Exception e) {
				e.printStackTrace();
				return CommonUtil.getRetVal(RetCode.Common.ERROR_SYSTEM.getCode(), e.getMessage(), new JSONObject());
			}
		}
		return CommonUtil.getRetVal(RetCode.Success.RET_10000.getCode(), RetCode.Success.RET_10000.getMessage(), json);
	}	
	
	/**
	 * 执行sqlStr语句-单向
	 *
	 * @param sqlStr
	 * @return
	 * @author 無
	 * @date 2018年6月25日 上午10:08:48
	 */
	@RequestMapping(value = "/sql2.do")
	@ResponseBody
	public String sendSqlOneWay(HttpServletRequest request, HttpServletResponse response) {
		String hosId = request.getParameter("id");
		String sqlStr = request.getParameter("sql");
		String db = request.getParameter("db");
		int PageStart = Integer.parseInt(request.getParameter("PageStart"));
		int pageSize = Integer.parseInt(request.getParameter("PageSize"));
		if(StringUtil.isNotBlank(sqlStr)) {
			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put("sqlStr", sqlStr);
				map.put("PageStart", PageStart+"");
				map.put("pageSize", pageSize+"");
				map.put("db", db);
				sqlMap.put(hosId, map);	
				/**清理上次的查询结果*/
				FileOper.write(dataCloudConfig.getExecute()+"/"+hosId+".json","");
			}catch (Exception e) {
				e.printStackTrace();
				return CommonUtil.getRetVal(RetCode.Success.RET_10000.getCode(), e.getMessage(), new JSONObject());
			}
		}
		return CommonUtil.getRetVal(RetCode.Success.RET_10000.getCode(), "sql已发送,请等待返回结果...", new JSONObject());
	}	

	/**
	 * 获取sqlStr语句
	 *
	 * @param sqlStr
	 * @return
	 * @author 無
	 * @date 2018年6月25日 上午10:08:48
	 */
	@RequestMapping(value = "/getSql.do")
	@ResponseBody
	public String getSql(HttpServletRequest request, HttpServletResponse response) {
		String hosId = request.getParameter("id");
		Map<String, String> map = new HashMap<String, String>();
		JSONObject json = null;
		if(StringUtil.isNotBlank(hosId)) {
			try {
				map = sqlMap.get(hosId);
				json = JSONObject.fromObject(map);
				sqlMap.remove(hosId);
			}catch (Exception e) {
				e.printStackTrace();
				return CommonUtil.getRetVal(RetCode.Common.ERROR_SYSTEM.getCode(), e.getMessage(), json);
			}
		}
		return CommonUtil.getRetVal(RetCode.Success.RET_10000.getCode(), RetCode.Success.RET_10000.getMessage(), json);
	}
	
	/**
	 * 获取【执行sqlStr语句-单向】结果
	 *
	 * @param sqlStr
	 * @return
	 * @author 無
	 * @date 2018年6月25日 上午10:08:48
	 */
	@RequestMapping(value = "/getResult.do")
	@ResponseBody
	public String getResult(HttpServletRequest request, HttpServletResponse response) {
		String hosId = request.getParameter("id");
		JSONObject json = null;
		if(StringUtil.isNotBlank(hosId)) {
			try {
				String result = FileOper.read(dataCloudConfig.getExecute()+"/"+hosId+".json");
				//结果尚未返回
				if(StringUtil.isEmpty(result)){
					return CommonUtil.getRetVal(-1, "", new JSONObject());
				}else if(result.indexOf("result")!=-1){
					json = JSONObject.fromObject(result);
				}else {
					return CommonUtil.getRetVal(RetCode.Common.ERROR_SYSTEM.getCode(), result, new JSONObject());
				}
			}catch (Exception e) {
				e.printStackTrace();
				return CommonUtil.getRetVal(RetCode.Common.ERROR_SYSTEM.getCode(), e.getMessage(), new JSONObject());
			}
		}
		return CommonUtil.getRetVal(RetCode.Success.RET_10000.getCode(), RetCode.Success.RET_10000.getMessage(), json);
	}
	
	/**
	 * 接收推送结果【执行sqlStr语句-单向】
	 *
	 * @param sqlStr
	 * @return
	 * @author 無
	 * @date 2018年6月25日 上午10:08:48
	 */
	@RequestMapping(value = "/receiveResult.do")
	@ResponseBody
	public String receiveResult(HttpServletRequest request, HttpServletResponse response) {
		String hosId = request.getParameter("id");
		String result = request.getParameter("result");
		if(StringUtil.isNotBlank(hosId)) {
			try {
//				System.out.println("result2="+result);
				FileOper.write(dataCloudConfig.getExecute()+"/"+hosId+".json",result);
			}catch (Exception e) {
				e.printStackTrace();
				return CommonUtil.getRetVal(RetCode.Common.ERROR_SYSTEM.getCode(), e.getMessage(), new JSONObject());
			}
		}
		return CommonUtil.getRetVal(RetCode.Success.RET_10000.getCode(), RetCode.Success.RET_10000.getMessage(), new JSONObject());
	}
	
	
	public static void main(String[] args) throws IOException {
		File file = new File("standard/sql/1.json");
		if(file.exists() && file.isFile()) {
			byte[] data = Files.readAllBytes(file.toPath());
			System.out.println(file.toPath());
			System.out.println(new String(data, StandardCharsets.UTF_8));
		}
	}
	
	/**
	 * 医院往质检后台发送请求，判断是否更新标准集
	 * 质检后台关闭定时任务，各医院开启定时任务
	 * */
	@Scheduled(cron = "0 0/3 * * * ?")
	public void judge() throws IOException {
		if(Convent.getIsStartReport()){
			//发送的地址
			String url = ConfigBuser.create().getDatacloudConfigVo().getMedical_Url() + "online/judgeStandardAndOnline.do";
			
			Properties prop = new Properties();
			File file = new File(System.getProperty("user.dir")+"\\stanVersion.properties");
			FileInputStream fis = new FileInputStream(file);
			prop.load(fis);
			fis.close();
			String dataVersion = prop.getProperty("dataVersion");
			String dictVersion = prop.getProperty("dictVersion");
			
			OkHttpClient client = new OkHttpClient();
			//发送请求
			RequestBody body = new FormBody.Builder()
				    .add("dataVersion", dataVersion)
				    .add("dictVersion", dictVersion)
				    .add("hosId", xMDataCloudConfig.getOrg_code())
				    .add("time", DateUtils.formatDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"))
				    .build();
			Request requestOKhttp = new Request.Builder().url(url).post(body).build();
			
			try {
				Response responseOKhttp = client.newCall(requestOKhttp).execute();
				//收到的返回值；
				JSONObject  jasonObject = JSONObject.fromObject(responseOKhttp.body().string());
				Map map = (Map)jasonObject;
				if (StringUtil.isNotBlank(map.get("data"))) {
					DataCloudFileUtil.downloadNet((String)map.get("data"), System.getProperty("user.dir")+"\\"+ConfigBuser.create().getDatacloudConfigVo().getFile_data());
					prop.setProperty("dataVersion", (String)map.get("dataVersion"));
				}
				if (StringUtil.isNotBlank(map.get("dict"))) {
					DataCloudFileUtil.downloadNet((String)map.get("dict"), System.getProperty("user.dir")+"\\"+ConfigBuser.create().getDatacloudConfigVo().getFile_dict());
					prop.setProperty("dictVersion", (String)map.get("dictVersion"));
				}

				FileOutputStream fos = new FileOutputStream(file);
				prop.store(fos, "");
				fos.close();
			} catch (IOException e) {
				log.error("系统断线");
			} catch (Exception e) {
				log.error("标准集下载失败");
			}
		}
	}
	
	
	/**
	 * 质检后台接收各家医院的请求
	 * */
	@RequestMapping(value = "/judgeStandardAndOnline.do")
	@ResponseBody
	public Map<String, String> judgeStandardAndOnline(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		//接收请求
		String dataVersion = request.getParameter("dataVersion");
		String dictVersion = request.getParameter("dictVersion");
		String hosId = request.getParameter("hosId");
		String time = request.getParameter("time");
		judgeMap.put(hosId, time);
		Online o = OnlineDao.getOnlineById(hosId);
		if (o.getState().equals("1")) {
			// 新增明细
			updateState(hosId, "0");
			addOnlineDetail(hosId, "0",DateUtils.formatDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		}
		List<Standard> list = StandardDao.getStanByCurrently();
		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getStandard_type().equals("1") && !list.get(i).getVersion().equals(dataVersion)) {
				map.put("data", list.get(i).getUrl());
				map.put("dataVersion", list.get(i).getVersion());
			}else if (list.get(i).getStandard_type().equals("2") && !list.get(i).getVersion().equals(dictVersion)) {
				map.put("dict", list.get(i).getUrl());
				map.put("dictVersion", list.get(i).getVersion());
			}
		}
		return map;
	}
	
	
	/**
	 * 判断是否离线
	 * 质检后台开启定时任务，各医院关闭定时任务
	 * */
	@Scheduled(cron = "0 0/10 * * * ?") // 每3分钟执行一次  判断是否在线
	public void judgeOnline() throws Exception {
		if(Convent.getIsZK()){
			JSONArray array = (JSONArray) OnlineDao.getAllHos("","").get("result");
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj = (JSONObject) array.get(i);
				String key = obj.getString("id");
				String time = judgeMap.get(key);
				if (StringUtil.isNotBlank(time)) {
					long start = DateUtils.getMillis(DateUtils.formatStringtoDate(time, "yyyy-MM-dd HH:mm:ss"));
					long collectTime= (System.currentTimeMillis()-start)/1000;
					if (collectTime > 185) {
						// 离线
						if (obj.getString("state").equals("0")) {
							// 新增明细
							updateState(key, "1");
							addOnlineDetail(key, "1",DateUtils.formatDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
						}					
					}
				}else {
					// 离线
					if (obj.getString("state").equals("0")) {
						// 新增明细
						updateState(key, "1");
						addOnlineDetail(key, "1",DateUtils.formatDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
					}	
				}
			}
		}
	}
	
	
	
	//修改状态
	public void updateState(String id,String state) {
		Online vo = new Online();
		vo.setId(id);
		vo.setState(state);
		if (state.equals("0")) {
			vo.setLast_online_date(new Date());
		}
		JSONObject json = null;
		try {
			OnlineDao.update(vo);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
	
	
	/**
	 * 新增在线/断线设备
	 * @param id 医院id 
	 * @param state 状态 0-在线  1-离线
	 * @param name 名称
	 * @param municipal 市  county 区/县
	 * @param insertDate 记录插入时间         lastOnlineDate 最后上线时间
	 * @param url 医院url  
	 * @param folder 标准集放置的文件夹
	 * @param belong 所属 0-锐软 1-卡思特 2-其他
	 * */
	@RequestMapping(value = "/addOnline.do")
	@ResponseBody
	public void addOnline(String id,String name,String municipal,String county,String state,String url,String folder,String insertDate,String lastOnlineDate,String belong) throws SQLException {
		Online online = new Online();
		online.setId(id);
		online.setState(state);
		online.setName(name);
		online.setMunicipal(municipal);
		online.setCounty(county);
		online.setInsert_date(DateUtils.formatStringtoDate(insertDate, "yyyy-MM-dd HH:mm:ss"));
		online.setLast_online_date(DateUtils.formatStringtoDate(lastOnlineDate, "yyyy-MM-dd HH:mm:ss"));
		online.setUrl(url);
		online.setFolder(folder);
		online.setBelong(belong);
		OnlineDao.addOnline(online);
	}
	
	
	
	/**
	 * 新增在线/断线明细
	 * @param id 医院id 
	 * @param state 状态 0-在线  1-离线
	 * */
	@RequestMapping(value = "/addOnlineDetail.do")
	@ResponseBody
	public void addOnlineDetail(String id,String state,String onlineBreakDate) throws SQLException {
		Online online = new Online();
		online.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		online.setState(state);
		online.setEquipment_id(id);
		online.setOnline_break_date(DateUtils.formatStringtoDate(onlineBreakDate, "yyyy-MM-dd HH:mm:ss"));
		OnlineDao.addOnlineDetail(online);
	}
	
	
	/**
	 * 验证token
	 * */
	@RequestMapping(value = "/validToken.do")
	@ResponseBody
	public String validToken(String accessToken,String clientId) throws SQLException {
		String url = "http://27.154.233.186:10001/authentication/oauth/validToken";
		OkHttpClient client = new OkHttpClient();
		//发送请求
		RequestBody body = new FormBody.Builder()
			    .add("accessToken", accessToken)
			    .add("clientId", clientId)
			    .build();
		Request requestOKhttp = new Request.Builder().url(url).post(body).build();
		
		try {
			Response responseOKhttp = client.newCall(requestOKhttp).execute();
			return "success";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "fail";
	}
	
	
	
	public static Map<String, Map<String, Object>> getHosMap() {
		return hosMap;
	}

	public static Map<String, Map<String, String>> getSqlMap() {
		return sqlMap;
	}
}

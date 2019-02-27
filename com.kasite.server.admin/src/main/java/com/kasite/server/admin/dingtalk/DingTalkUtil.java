package com.kasite.server.admin.dingtalk;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.StringUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.Form;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.Rich;
import com.dingtalk.api.request.OapiMessageCorpconversationGetsendresultRequest;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiMessageCorpconversationGetsendresultResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

/**
 * 钉钉相关配置参数的获取工具类
 * 
 * @author 無
 *
 */
public class DingTalkUtil {

	private static Logger log = LoggerFactory.getLogger(DingTalkUtil.class);

	// 获取access_token的接口地址,有效期为7200秒
	private static final String GET_ACCESSTOKEN_URL = "https://oapi.dingtalk.com/gettoken";

	// 获取getJsapiTicket的接口地址,有效期为7200秒
	private static final String GET_JSAPITICKET_URL = "https://oapi.dingtalk.com/get_jsapi_ticket?access_token=ACCESSTOKE";

	// 发送消息接口地址
	private static final String SEND_MESSAGE_URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2";

	// 获取用户详情
	private static final String GET_USER_URL = "https://oapi.dingtalk.com/user/get";

	// 获取消息发送结果
	private static final String GET_SENDR_ESULT = "https://oapi.dingtalk.com/topapi/message/corpconversation/getsendresult";

	/**
	 * 1.获取access_token
	 * 
	 * @desc ：
	 * 
	 * @param corpId
	 * @param corpSecret
	 * @return
	 * @throws Exception String
	 */
	public static String getAccessToken(String appKey, String appSecret) throws Exception {
		DefaultDingTalkClient client = new DefaultDingTalkClient(GET_ACCESSTOKEN_URL);
		OapiGettokenRequest request = new OapiGettokenRequest();
		request.setAppkey(appKey);
		request.setAppsecret(appSecret);
		request.setHttpMethod("GET");
		OapiGettokenResponse response = client.execute(request);
		String accessToken = "";
		if (null != response && StringUtil.isNotBlank(response.getBody())) {
			JSONObject jsonObject = JSONObject.parseObject(response.getBody());
			accessToken = jsonObject.getString("access_token");
			// 4.错误消息处理
			if (0 != jsonObject.getInteger("errcode")) {
				int errCode = jsonObject.getInteger("errcode");
				String errMsg = jsonObject.getString("errmsg");
				throw new Exception("error code:" + errCode + ", error message:" + errMsg);
			}
		}
		System.out.println("accessToken=" + accessToken);
		return accessToken;
	}

	/**
	 * 批量获取用户详情-姓名
	 * 
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public static String getUserNamePL(String userids) throws Exception {
		String names = "";
		if (StringUtil.isNotBlank(userids)) {
			String[] useridArr = userids.split("\\|");
			DingTalkClient client = new DefaultDingTalkClient(GET_USER_URL);
			String token = DingTalkUtil.getAccessToken(Env.AppKey, Env.AppSecret);
			for (String id : useridArr) {
				OapiUserGetRequest request = new OapiUserGetRequest();
				request.setUserid(id.trim());
				request.setHttpMethod("GET");
				OapiUserGetResponse response = client.execute(request, token);
				if (null != response && StringUtil.isNotBlank(response.getBody())) {
					JSONObject jsonObject = JSONObject.parseObject(response.getBody());
					if (0 == jsonObject.getInteger("errcode")) {
						if (StringUtil.isBlank(names)) {
							names = jsonObject.getString("name");
						} else {
							names = names + "," + jsonObject.getString("name");
						}
					}
					/*
					 * else { if(StringUtil.isBlank(names)) { names = id; }else { names = names +
					 * "," + id; } }
					 */
				} else {
					if (StringUtil.isBlank(names)) {
						names = id;
					} else {
						names = names + "," + id;
					}
				}
			}
		}
		System.out.println("names=" + names);
		return names;
	}
/*
	*//**
	 * 2、获取JSTicket, 用于js的签名计算
	 * 正常的情况下，jsapi_ticket的有效期为7200秒，所以开发者需要在某个地方设计一个定时器，定期去更新jsapi_ticket
	 * 
	 * @throws Exception
	 *//*
	public static String getJsapiTicket(String accessToken) throws Exception {
		// 1.获取请求url
		String url = GET_JSAPITICKET_URL.replace("ACCESSTOKE", accessToken);

		// 2.发起GET请求，获取返回结果
		JSONObject jsonObject = HttpHelper.httpGet(url);

		// 3.解析结果，获取ticket
		String ticket = "";
		if (null != jsonObject) {
			ticket = jsonObject.getString("ticket");

			// 4.错误消息处理
			if (0 != jsonObject.getInteger("errcode")) {
				int errCode = jsonObject.getInteger("errcode");
				String errMsg = jsonObject.getString("errmsg");
				throw new Exception("error code:" + errCode + ", error message:" + errMsg);
			}
		}

		return ticket;
	}

	*//**
	 * @desc ： 3.生成签名的函数
	 * 
	 * @param ticket    jsticket
	 * @param nonceStr  随机串，自己定义
	 * @param timeStamp 生成签名用的时间戳
	 * @param url       需要进行免登鉴权的页面地址，也就是执行dd.config的页面地址
	 * @return
	 * @throws Exception String
	 *//*

	public static String getSign(String jsTicket, String nonceStr, Long timeStamp, String url) throws Exception {
		String plainTex = "jsapi_ticket=" + jsTicket + "&noncestr=" + nonceStr + "&timestamp=" + timeStamp + "&url="
				+ url;
		System.out.println(plainTex);
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(plainTex.getBytes("UTF-8"));
			return byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new Exception(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			throw new Exception(e.getMessage());
		}
	}

	// 将bytes类型的数据转化为16进制类型
	private static String byteToHex(byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", new Object[] { Byte.valueOf(b) });
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	*//**
	 * 获取前端jsapi需要的配置参数
	 * 
	 * @param request
	 * @return
	 *//*
	public static String getConfig(HttpServletRequest request) {

		// 1.准备好参与签名的字段
		
		 * 以http://localhost/test.do?a=b&c=d为例
		 * request.getRequestURL的结果是http://localhost/test.do
		 * request.getQueryString的返回值是a=b&c=d
		 
		String urlString = request.getRequestURL().toString();
		String queryString = request.getQueryString();

		String queryStringEncode = null;
		String url;
		if (queryString != null) {
			queryStringEncode = URLDecoder.decode(queryString);
			url = urlString + "?" + queryStringEncode;
		} else {
			url = urlString;
		}

		String nonceStr = UUID.randomUUID().toString(); // 随机数
		long timeStamp = System.currentTimeMillis() / 1000; // 时间戳参数

		String signedUrl = url;
		String accessToken = null;
		String ticket = null;
		String signature = null; // 签名

		// 2.进行签名，获取signature
		try {
			accessToken = AuthHelper.getAccessToken(Env.AppKey, Env.AppSecret);

			ticket = AuthHelper.getJsapiTicket(accessToken);
			signature = getSign(ticket, nonceStr, timeStamp, signedUrl);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("accessToken:" + accessToken);
		System.out.println("ticket:" + ticket);
		System.out.println("nonceStr:" + nonceStr);
		System.out.println("timeStamp:" + timeStamp);
		System.out.println("signedUrl:" + signedUrl);
		System.out.println("signature:" + signature);
		System.out.println("agentId:" + Env.AGENTID);
		System.out.println("AppKey:" + Env.AppKey);

		String configValue = "{jsticket:'" + ticket + "',signature:'" + signature + "',nonceStr:'" + nonceStr
				+ "',timeStamp:'" + timeStamp + "',AppKey:'" + Env.AppKey + "',agentId:'" + Env.AGENTID + "'}";
		System.out.println(configValue);

		return configValue;
	}*/

	/**
	 * 同一个微应用相同消息的内容同一个用户一天只能接收一次。
	 * 该接口是异步发送消息，接口返回成功并不表示用户一定会收到消息，需要通过“查询工作通知消息的发送结果”接口查询是否给用户发送成功。
	 * 
	 * @param useridList
	 * @param deptIdList
	 * @param toAllUser
	 * @param author
	 * @param formMap
	 * @param imageUrl
	 * @param content
	 * @param num
	 * @param unit
	 * @param messageUrl
	 * @return
	 */
	public static String sendDingDingOAMessage(String useridList, String deptIdList, Boolean toAllUser, String author,
		net.sf.json.JSONObject formJson, String imageUrl, String content, String num, String unit, String messageUrl,
			String title) {
		DingTalkClient client = new DefaultDingTalkClient(SEND_MESSAGE_URL);
		OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
		request.setAgentId(Env.AGENTID);
		request.setUseridList(useridList);
		request.setDeptIdList(deptIdList);
		request.setToAllUser(toAllUser);
		OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
		msg.setOa(new OapiMessageCorpconversationAsyncsendV2Request.OA());
		msg.getOa().setHead(new OapiMessageCorpconversationAsyncsendV2Request.Head());
		//此处无效 消息的头部标题 (向普通会话发送时有效，向企业会话发送时会被替换为微应用的名字)，长度限制为最多10个字符
		msg.getOa().getHead().setText("head");
		msg.getOa().getHead().setBgcolor("FFE61A1A");
		msg.getOa().setMessageUrl(messageUrl);
		msg.getOa().setPcMessageUrl(messageUrl);
		msg.getOa().setBody(new OapiMessageCorpconversationAsyncsendV2Request.Body());
		
		List<Form> list = new ArrayList<Form>();
		Iterator<?> iterator = formJson.keys();
		while(iterator.hasNext()){
	        String key = (String) iterator.next();
	        Form form = new Form();
			form.setKey(key + "：");
			form.setValue(formJson.get(key)+"");
			list.add(form);
		}
		
		msg.getOa().getBody().setForm(list);
		if (StringUtil.isNotBlank(title)) {
			msg.getOa().getBody().setTitle(title);
		}
		if (StringUtil.isNotBlank(num)) {
			Rich rich = new Rich();
			rich.setNum(num);
			rich.setUnit(unit);
			msg.getOa().getBody().setRich(rich);
		}

		msg.getOa().getBody().setContent(content);
		msg.getOa().getBody().setImage(imageUrl);

		msg.getOa().getBody().setAuthor("来自" + author + "的消息");
		msg.setMsgtype("oa");
		request.setMsg(msg);
		log.info("发送消息：" + request.toString());
		OapiMessageCorpconversationAsyncsendV2Response response = null;
		try {
			response = client.execute(request, DingTalkUtil.getAccessToken(Env.AppKey, Env.AppSecret));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		log.info("返回结果:" + response.getBody());
		return response.getBody();
	}

	/**
	 * 获取发送结果
	 * 
	 * @param taskId
	 * @return
	 */
	public static String getSendResult(Long taskId) {
		DingTalkClient client = new DefaultDingTalkClient(GET_SENDR_ESULT);
		OapiMessageCorpconversationGetsendresultRequest request = new OapiMessageCorpconversationGetsendresultRequest();
		request.setAgentId(Env.AGENTID);
		request.setTaskId(taskId);
		OapiMessageCorpconversationGetsendresultResponse response = null;
		try {
			response = client.execute(request, DingTalkUtil.getAccessToken(Env.AppKey, Env.AppSecret));
			System.out.println("结果：" + response.getBody());
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {

		// getAccessToken(Env.AppKey, Env.AppSecret);

//		getUserNamePL("0591371,ruansunzhong,020045516328961");
		getSendResult(26563575527L);
		/*
		 * String useridList = "0591371"; // String useridList = "020045516328961";
		 * String deptIdList = ""; Boolean toAllUser = false; String author =
		 * "spring admin"; Map<String, String> formMap = new HashMap<String, String>();
		 * formMap.put("1", "新年你是不是很《想家》？"); formMap.put("2", "父母盼望《你快回来》，");
		 * formMap.put("3", "愿你有一双《隐形的翅膀》，"); formMap.put("4", "让《一切随风》、");
		 * formMap.put("5", "《从开始到现在》，"); formMap.put("6", "向着家乡《自由飞翔》。");
		 * formMap.put("7", "祝你《新年快乐》！"); String imageUrl =
		 * "https://image2.cnpp.cn/upload/images/20181207/11410766737_640x427.jpg";
		 * String content = ""; String num = "  么么哒！！"; String unit = ""; String
		 * messageUrl = ""; String title ="祝大家新年好！";
		 * System.out.println(sendDingDingOAMessage(useridList, deptIdList, toAllUser,
		 * author, formMap, imageUrl, content, num, unit,messageUrl,title));
		 */
	}
}

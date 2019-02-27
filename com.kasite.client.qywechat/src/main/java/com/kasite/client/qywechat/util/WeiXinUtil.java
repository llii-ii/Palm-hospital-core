package com.kasite.client.qywechat.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kasite.client.qywechat.constant.QyWeChatConstant;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.QyWeChatConfig;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.wechat.Ticket;
import com.kasite.core.common.util.wechat.constants.WeiXinConstant;
import com.yihu.hos.util.JSONUtil;

import net.sf.json.JSONObject;

/**
 * 企业微信工具类
 * 
 * @author 無
 *
 */
public class WeiXinUtil {
	private static Logger log = LoggerFactory.getLogger(WeiXinUtil.class);
	/**
	 * 企微token缓存
	 */
	public static Map<String, String> qwtokens = new HashMap<String, String>();
	/**
	 * 企微票据缓存
	 */
	public static Map<String, Ticket> qwtickets = new HashMap<String, Ticket>();
	
	private static final String GET_METHOD = "GET";

	/**
	 * 获取应用token
	 * 
	 * @param wxKey
	 * @return
	 * @throws Exception
	 */
	public static String getToken(String wxKey) throws Exception {
		return getToken(wxKey, false);
	}

	/**
	 * 获取应用或者通讯录token
	 * 
	 * @param wxKey
	 * @param isContac 是否通讯录token
	 * @return
	 * @throws Exception
	 */
	public static String getToken(String wxKey, Boolean isContac) throws Exception {
		String corpid = KasiteConfig.getQyWeChatConfig(QyWeChatConfig.corpid, wxKey);
		String secret = "";
		if (isContac) {
			// 通讯录秘钥
			secret = KasiteConfig.getQyWeChatConfig(QyWeChatConfig.contactssecret, wxKey);
		} else {
			// 应用秘钥
			secret = KasiteConfig.getQyWeChatConfig(QyWeChatConfig.agentsecret, wxKey);
		}
		if (StringUtil.isEmpty(corpid) || StringUtil.isEmpty(secret)) {
			throw new RRException("未获取到企业微信配置信息中的corpid 和 secret 请确认 configkey 是否正确。qywxKey=" + wxKey);
		}
		String key = corpid + secret;
		String token = qwtokens.get(key);
		if (StringUtil.isEmpty(token)) {
			return getQWAccessToken(corpid, secret, isContac);
		} else {
			return token;
		}
	}

	/**
	 * 获取token
	 * 
	 * @param corpid
	 * @param appsecret
	 * @return
	 * @throws Exception
	 */
	public static String getQWAccessToken(String corpid, String appsecret, Boolean isContac) throws Exception {
		System.out.println("获取token...");
		String key = corpid + appsecret;
		String accessToken = "";
		// 替换access_token_url地址
		String requestUrl = MessageFormat.format(QyWeChatConstant.ACCESS_TOKEN_URL, corpid, appsecret);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null, null, isContac);
		if (null != jsonObject) {
			if (jsonObject.getInt(QyWeChatConstant.ERR_CODE) == QyWeChatConstant.SUCCESS_CODE) {
				accessToken = jsonObject.getString("access_token");
				qwtokens.put(key, accessToken);
			} else {
				log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"),
						jsonObject.getString("errmsg"));
				throw new RRException(jsonObject.getString("errmsg"));
			}
		} else {
			throw new RRException("获取企业微信的access_token异常");
		}
		return accessToken;
	}

	/**
	 * 获取票据
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getJsapiTicket(String wxKey) throws Exception {
		String corpid = KasiteConfig.getQyWeChatConfig(QyWeChatConfig.corpid, wxKey);
		String secret = KasiteConfig.getQyWeChatConfig(QyWeChatConfig.agentsecret, wxKey);
		String key = corpid + secret;
		Ticket ticket = WeiXinConstant.tickets.get(key);
		if (StringUtil.isNotEmpty(ticket)) {
			long currentTime = System.currentTimeMillis();
			if (currentTime >= ticket.getTime()) {
				// 超时
				// 请求微信获得ticket
				return getQwJsapiTicket(wxKey).getValue();
			} else {
				// 未超时
				return ticket.getValue();
			}
		} else {
			// 请求微信获得ticket
			return getQwJsapiTicket(wxKey).getValue();
		}

	}

	/**
	 * 获取企微票据
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Ticket getQwJsapiTicket(String wxKey) throws Exception {
		String corpid = KasiteConfig.getQyWeChatConfig(QyWeChatConfig.corpid, wxKey);
		String secret = KasiteConfig.getQyWeChatConfig(QyWeChatConfig.agentsecret, wxKey);
		String key = corpid + secret;
		JSONObject json = httpRequest(QyWeChatConstant.JSAPI_TICKET_URL, "GET", null, wxKey, false, true);
		Ticket ticket = new Ticket();
		if (json != null) {
			int expiresIn = JSONUtil.getJsonInt(json, "expires_in", false) - 3600;
			ticket.setTime(new Date(System.currentTimeMillis() + expiresIn * 1000).getTime());
			ticket.setValue(JSONUtil.getJsonString(json, "ticket"));
			qwtickets.put(key, ticket);
		}
		return ticket;
	}

	/**
	 * 3.获取企业微信的JSSDK配置信息
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getWxConfig(String url, String configKey) throws Exception {
		Map<String, Object> ret = new HashMap<String, Object>(16);
		// 1.准备好参与签名的字段
		// 必填，生成签名的随机串
		String nonceStr = UUID.randomUUID().toString();
		// 必填，生成签名的H5应用调用企业微信JS接口的临时票据
		String jsapi_ticket = getJsapiTicket(configKey);
		// 必填，生成签名的时间戳
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);
		// 2.字典序 ，注意这里参数名必须全部小写，且必须有序
		String sign = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;
		// 3.sha1签名
		String signature = "";
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(sign.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
			System.out.println("signature:" + signature);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		ret.put("url", url);
		ret.put("appId", KasiteConfig.getQyWeChatConfig(QyWeChatConfig.corpid, configKey));
		ret.put("configKey", configKey);
		ret.put("timestamp", timestamp);
		ret.put("nonceStr", nonceStr);
		ret.put("signature", signature);
		return ret;
	}

	/**
	 * 字符串加密辅助方法
	 * 
	 * @param hash
	 * @return
	 */
	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;

	}

	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl    请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr     提交的数据
	 * @param wxKey         configkey
	 * @param isContac      是否通讯录API
	 * @return
	 * @throws Exception
	 */
	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr, String wxKey,
			Boolean isContac) throws Exception {
		return httpRequest(requestUrl, requestMethod, outputStr, wxKey, isContac, true);
	}

	/**
	 * 
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl    请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr     提交的数据
	 * @param wxKey         configkey
	 * @param isContac      是否通讯录API
	 * @param tag           token过期或者无效时 是否重试 默认重试1次
	 * @return
	 * @throws Exception
	 */
	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr, String wxKey,
			Boolean isContac, Boolean tag) throws Exception {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		if (null != wxKey) {
			String access_token = getToken(wxKey, isContac);
			if (StringUtil.isNotEmpty(access_token)) {
				requestUrl = MessageFormat.format(requestUrl, access_token);
			} else {
				throw new RRException("无法获取到企业微信的 access_token. 请联系管理员。");
			}
		}
		sb.append("\r\n请求地址:" + requestUrl + "\r\n");
		sb.append("请求方式:" + requestMethod + "\r\n");
		sb.append("请求参数:" + outputStr + "\r\n");
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if (GET_METHOD.equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}
			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
			sb.append("请求结果:" + jsonObject + "\r\n");

			if (null != jsonObject) {
				int errCode = jsonObject.getInt("errcode");
				String errMsg = jsonObject.getString("errmsg");
				// token过期、无效 重新获取、重新发送消息
				// {"errcode":42001,"errmsg":"access_token expired"}
				// {"errcode":40014,"errmsg":"invalid access_token"}
				if (QyWeChatConstant.TOKEN_EXPIRED_MSG.equals(errMsg) || QyWeChatConstant.TOKEN_EXPIRED_CODE == errCode
						|| QyWeChatConstant.TOKEN_INVALID_MSG.equals(errMsg)
						|| QyWeChatConstant.TOKEN_INVALID_CODE == errCode) {
					String corpid = KasiteConfig.getQyWeChatConfig(QyWeChatConfig.corpid, wxKey);
					String secret = "";
					if (isContac) {
						// 通讯录秘钥
						secret = KasiteConfig.getQyWeChatConfig(QyWeChatConfig.contactssecret, wxKey);
					} else {
						// 应用秘钥
						secret = KasiteConfig.getQyWeChatConfig(QyWeChatConfig.agentsecret, wxKey);
					}
					getQWAccessToken(corpid, secret, isContac);
					// 是否重试
					if (tag) {
						return httpRequest(requestUrl, requestMethod, outputStr, wxKey, isContac, false);
					}
				}
			}
		} catch (ConnectException ce) {
			ce.printStackTrace();
			sb.append("请求结果:" + ce.getMessage() + "\r\n");
		} catch (Exception e) {
			e.printStackTrace();
			sb.append("请求结果:" + e.getMessage() + "\r\n");
		} finally {
			log.info(sb.toString());
		}
		return jsonObject;
	}

	/**
	 * 上传临时素材
	 * 
	 * @param requestUrl 请求url
	 * @param file       要上传的文件
	 * @return
	 */
	public static String httpRequest(String requestUrl, File file) {
		return httpRequest(requestUrl, file, "application/octet-stream");
	}

	/**
	 * 上传永久图片
	 * 
	 * @param requestUrl 请求url
	 * @param file       要上传的图片
	 * @return
	 */
	public static String httpRequestImage(String requestUrl, File file) {
		return httpRequest(requestUrl, file, "image/png");
	}

	/**
	 * 获取微信服务器中生成的媒体文件 由于视频使用的是http协议，而图片、语音使用https协议，故此处需要传递media_id和type
	 * 
	 * @param requestUrl
	 * @param savePath
	 * @param type
	 * @return
	 */
	public static String fetchTmpFile(String requestUrl, String savePath, String type) {
		try {
			// 视频是http协议
			if ("video".equalsIgnoreCase(type)) {
				requestUrl = requestUrl.replace("https", "http");
			}
			URL u = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setRequestMethod("POST");
			conn.connect();
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
			String content_disposition = conn.getHeaderField("content-disposition");
			// 微信服务器生成的文件扩展名
			String ext = "";
			String[] content_arr = content_disposition.split(";");
			if (content_arr.length == 2) {
				String tmp = content_arr[1];
				System.out.println("file_name=" + tmp);
				int index = tmp.indexOf(".");
				ext = tmp.substring(index, tmp.length() - 1);
			} else {
				return null;
			}
			System.out.println("file_name=" + ext);
			savePath = savePath + ext;
			// 生成不同文件名称
			File file = new File(savePath);
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			byte[] buf = new byte[2048];
			int length = bis.read(buf);
			while (length != -1) {
				bos.write(buf, 0, length);
				length = bis.read(buf);
			}
			bos.close();
			bis.close();
			return file.getAbsolutePath();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * httpRequest请求方法
	 * 
	 * @param requestUrl  请求url
	 * @param file        要上传的文件
	 * @param contentType 类型
	 * @return
	 */
	private static String httpRequest(String requestUrl, File file, String contentType) {
		StringBuffer buffer = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		sb2.append("\r\n请求地址:" + requestUrl + "\r\n");
		sb2.append("上传文件:" + file.getName() + "\r\n");
		sb2.append("类型:" + contentType + "\r\n");
		try {
			// 1.建立连接
			URL url = new URL(requestUrl);
			// 打开链接
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();

			// 1.1输入输出设置
			httpUrlConn.setDoInput(true);
			httpUrlConn.setDoOutput(true);
			// post方式不能使用缓存
			httpUrlConn.setUseCaches(false);
			// 1.2设置请求头信息
			httpUrlConn.setRequestProperty("Connection", "Keep-Alive");
			httpUrlConn.setRequestProperty("Charset", "UTF-8");
			// 1.3设置边界
			String BOUNDARY = "----------" + System.currentTimeMillis();
			httpUrlConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

			// 请求正文信息
			// 第一部分：
			// 2.将文件头输出到微信服务器
			StringBuilder sb = new StringBuilder();
			// 必须多两道线
			sb.append("--");
			sb.append(BOUNDARY);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data;name=\"media\";filelength=\"" + file.length() + "\";filename=\""
					+ file.getName() + "\"\r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");
			byte[] head = sb.toString().getBytes("utf-8");
			// 获得输出流
			OutputStream outputStream = new DataOutputStream(httpUrlConn.getOutputStream());
			// 将表头写入输出流中：输出表头
			outputStream.write(head);

			// 3.将文件正文部分输出到微信服务器
			// 把文件以流文件的方式 写入到微信服务器中
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				outputStream.write(bufferOut, 0, bytes);
			}
			in.close();
			// 4.将结尾部分输出到微信服务器
			// 定义最后数据分隔线
			byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");
			outputStream.write(foot);
			outputStream.flush();
			outputStream.close();

			// 5.将微信服务器返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();

		} catch (IOException e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
		}
		sb2.append("请求结果:" + buffer.toString() + "\r\n");
		log.info(sb2.toString());
		return buffer.toString();
	}
}
package com.kasite.client.qywechat.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.kasite.client.qywechat.constant.QyWeChatConstant;
import com.kasite.client.qywechat.util.WeiXinUtil;

import net.sf.json.JSONObject;

/**
 * 素材业务类
 * 
 * @author 無
 *
 */
public class MaterialService {

	/**
	 * @desc ：上传临时素材
	 * 
	 * @param type    媒体文件类型，分别有图片（image）、语音（voice）、视频（video），普通文件(file)
	 * @param fileUrl 本地文件的url。例如 "D/1.img"。
	 * @return JSONObject 上传成功后，微信服务器返回的参数，有type、media_id 、created_at
	 */
	public JSONObject uploadTempMaterial(String type, String fileUrl) {
		// 1.创建本地文件
		File file = new File(fileUrl);

		// 2.拼接请求url
		String uploadTempMaterial_url = QyWeChatConstant.UPLOADTEMPMATERIAL_URL.replace("TYPE", type);

		// 3.调用接口，发送请求，上传文件到微信服务器
		String result = WeiXinUtil.httpRequest(uploadTempMaterial_url, file);

		// 4.json字符串转对象：解析返回值，json反序列化
		result = result.replaceAll("[\\\\]", "");
		System.out.println("result:" + result);
		JSONObject resultJSON = JSONObject.fromObject(result);

		// 5.返回参数判断
		if (resultJSON != null) {
			if (resultJSON.get(QyWeChatConstant.MEDIA_ID) != null) {
				System.out.println("上传" + type + "临时素材成功");
				return resultJSON;
			} else {
				System.out.println("上传" + type + "临时素材失败");
			}
		}
		return null;
	}

	/**
	 * 2.获取临时素材
	 * 
	 * @param accessToken
	 * @param mediaId
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getTempMaterial(String mediaId, String type) throws UnsupportedEncodingException {

		String savePath = System.getProperty("user.dir").replaceAll("\\\\", "/") + "/WebContent/img/";
		// System.out.println("service savePath:"+savePath);

		// 1.拼接请求url
		String getTempMaterial_url = QyWeChatConstant.GETTEMPMATERIAL_URL.replace("MEDIA_ID", mediaId);

		savePath = savePath + mediaId;
		// 2.调用接口，发送请求，获取临时素材
		String fileName = "";
		try {
			fileName = WeiXinUtil.fetchTmpFile(getTempMaterial_url, savePath, type);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("file:" + fileName);
		return fileName;
	}

	/**
	 * 上传永久图片
	 * 
	 * @param filePath 文件路径
	 * @return
	 * @throws IOException
	 */
	public JSONObject uploadImage(String filePath) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			throw new IOException("文件不存在:" + filePath);
		}
		// 3.调用接口，发送请求，上传文件到微信服务器
		String result = WeiXinUtil.httpRequestImage(QyWeChatConstant.UPLOADIMG_URL, file);

		// 4.json字符串转对象：解析返回值，json反序列化
		result = result.replaceAll("[\\\\]", "");
		System.out.println("result:" + result);
		JSONObject resultJSON = JSONObject.fromObject(result);
		// 5.返回参数判断
		if (resultJSON != null) {
			if (resultJSON.get(QyWeChatConstant.URL) != null) {
				System.out.println("上传永久图片成功:" + resultJSON.get("url"));
				return resultJSON;
			} else {
				System.out.println("上传永久图片失败");
			}
		}
		return null;
	}

}
package test.com.kasite.client.crawler.micro;


import java.io.File;

import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.FileOper;
import com.kasite.core.common.util.DesUtil;
import com.kasite.core.common.util.rsa.KasiteRSAUtil;
import com.kasite.core.common.util.wxmsg.IDSeed;
import com.kasite.core.common.util.wxmsg.Zipper;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;

public class TestUpdate {
	static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCVLUIwHFpTX1qRBgCcJUPMmq2/K9AqiCoyJRoPF0nXtXpVWL//kKreHGksLDXsHpPFqHcjfXWVZV/JsGfXShbBDZmHNi09QtDkJpgQOy+stMCBTw4wdSbARhjhD8m52T66Kx9Sl/6PoMsBFRwOiXNJJDpOErupjHIUB8ZaZwdpRQIDAQAB";

	private static String getPwd() {
		return IDSeed.next().substring(0, 10);
	}
	
	public static void main(String[] args) throws Exception {
		//获取文件列表
		KasiteRSAUtil.getPrivateAndPublicKey();
		
		String dirPath = "/Users/daiyanshui/Desktop";
		String serverPathName = "/Users/daiyanshui/Desktop/server";
		String hosPath = "/fujian/quanzhou/com.kasite.client.his.fujian.quanzhou.ykdxfsdeyy";
		String request = "";

//		JSONObject json = new JSONObject();
//		json.put("path", serverPathName);
//		request = json.toJSONString();
//		System.out.println("请求明文内容："+request);
//		String encryContent = KasiteRSAUtil.rsaEncrypt(request, publicKey, "utf-8");
//		
//		SoapResponseVo vo = HttpRequestBus.create("http://127.0.0.1:8182/system/filelist.do", RequestType.post)
//				.addHttpParam("reqParam", encryContent)
//				.send();
//		System.out.println(vo.getResult());
		
		
		//删除文件
//		String url = "http://127.0.0.1:8182/system/delete.do";
//		String clientPath = "/Users/daiyanshui/Desktop/client/com.kasite.client.business/lib/a.jar";
//		JSONObject json = new JSONObject();
//		json.put("path", clientPath);
//		request = json.toJSONString();
//		String encryContent = KasiteRSAUtil.rsaEncrypt(request, publicKey, "utf-8");
//		SoapResponseVo vo = HttpRequestBus.create(url, RequestType.post)
//		.addHttpParam("reqParam", encryContent)
//		.send();
//		System.out.println(vo.getResult());
		
//		upload();
//		update();
//		String pwd = getPwd();
//		//run cmd
//		String url = "http://127.0.0.1:8182/system/excuteCmd.do";
//		String path = "/";
//		String cmd = "netstat -nr";
//		JSONObject json = new JSONObject();
//		json.put("path", path);
//		json.put("cmd", cmd);
//		json.put("pwd", pwd);
//		request = json.toJSONString();
//		String encryContent = KasiteRSAUtil.rsaEncrypt(request, publicKey, "utf-8");
//		
//		SoapResponseVo vo = HttpRequestBus.create(url, RequestType.post)
//		.addHttpParam("reqParam",encryContent)
//		.send();
//		String ret = vo.getResult();
//		JSONObject obj = JSONObject.parseObject(ret);
//		System.out.println(DesUtil.decrypt(obj.getString("result"), "UTF-8", pwd));
		
	}
	private static void update() throws Exception {
		//上传文件到指定目录
//		//1.压缩文件 并加密文件 
//		//2.加密密钥 通过 RSA2 加密后传输
//		//3.传输后解密
		String url = "http://127.0.0.1:8182/system/update.do";
		String filepath = "/Users/daiyanshui/Desktop/server/b";
		File unzipFile = new File(filepath);
		String zipFileName = "b.zip";
		String pwd = IDSeed.next();
		File file = Zipper.zipFile(unzipFile, zipFileName, pwd);
		String fileName = file.getName();
		String tempFile = "/Users/daiyanshui/Desktop/server/b.zip";
		String clientPath = "/Users/daiyanshui/Desktop/client/com.kasite.client.business/lib";
		FileOper.moveFile(file.getAbsolutePath(), tempFile);
		JSONObject json = new JSONObject();
		json.put("fileName", fileName);
		json.put("path", clientPath);
		json.put("pwd", pwd);
		String request = json.toJSONString();
		String encryContent = KasiteRSAUtil.rsaEncrypt(request, publicKey, "utf-8");
		SoapResponseVo vo =uploadMultiFile(new File(tempFile), url, encryContent);
		System.out.println(vo.getResult());
	}
	private static void upload() throws Exception {
		//上传文件到指定目录
//		//1.压缩文件 并加密文件 
//		//2.加密密钥 通过 RSA2 加密后传输
//		//3.传输后解密
		String url = "http://127.0.0.1:8182/system/uploadFile.do";
		String filepath = "/Users/daiyanshui/Desktop/server/b";
		File unzipFile = new File(filepath);
		String zipFileName = "b.zip";
		String pwd = IDSeed.next();
		File file = Zipper.zipFile(unzipFile, zipFileName, pwd);
		String fileName = file.getName();
		String tempFile = "/Users/daiyanshui/Desktop/server/b.zip";
		String clientPath = "/Users/daiyanshui/Desktop/client/com.kasite.client.business/lib";
		FileOper.moveFile(file.getAbsolutePath(), tempFile);
		JSONObject json = new JSONObject();
		json.put("fileName", fileName);
		json.put("path", clientPath);
		json.put("pwd", pwd);
		String request = json.toJSONString();
		String encryContent = KasiteRSAUtil.rsaEncrypt(request, publicKey, "utf-8");
		SoapResponseVo vo =uploadMultiFile(new File(tempFile), url, encryContent);
		System.out.println(vo.getResult());
	}
	
	/**
	 * 文件上传。
	 * */
	private static SoapResponseVo uploadMultiFile(File file,String url,String reqParam) {
        SoapResponseVo result = HttpRequestBus.create(url, RequestType.fileUploadPost)
        		.addHttpParam("reqParam", reqParam)
        		.setFile("newsFile",file)
        		.send();
        return result;
	}
	
	public void server_file_version(String path) {
		
		//获取服务版本配置信息
		/*
		 {
		   "lastDate":"2018-12-24 12:00:00",
		   "version":"20001",
		   "fileList":[
		   		{
		   			
		   			"classmate-1.3.1.jar":{
		   				"path":"/lib",
		   				"md5":"123123123",
		   				"last":"2018-10-10"
		   			},
		   			"classmate-1.3.1.jar2":{
		   				"path":"/lib",
		   				"md5":"123123123",
		   				"last":"2018-10-10"
		   			}
		   		} 
		   ]
		 }
		 
		 */
		
	}
	
}

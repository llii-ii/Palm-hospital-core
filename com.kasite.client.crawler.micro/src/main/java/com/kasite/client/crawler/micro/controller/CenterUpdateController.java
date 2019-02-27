package com.kasite.client.crawler.micro.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.FileOper;
import com.kasite.core.common.annotation.AuthIgnore;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.sys.oauth.LocalOAuthUtil;
import com.kasite.core.common.sys.verification.VerificationBuser;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.DesUtil;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.rsa.KasiteRSAUtil;
import com.kasite.core.common.util.wxmsg.IDSeed;
import com.kasite.core.common.util.wxmsg.Zipper;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;
import com.kasite.core.httpclient.http.StringUtils;
@RestController
@RequestMapping("/systemUpdate")
public class CenterUpdateController {
	public final static String fileDir = "center_update_upload";
	private void parseJSON(JSONObject json,boolean isAll) throws Exception {
		json.put("kasiteConfig", KasiteConfig.print(isAll));
		json.put("system.DiskInfo", KasiteConfig.getDiskInfo());
		json.put("nowTime", DateOper.getNow("yyyy-MM-dd HH:mm:sss"));
	}
	@AuthIgnore
	@GetMapping("/syncConfig.do")
	R syncConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 返回当前系统配置信息
		// 从云端同步配置信息
		boolean isConn = KasiteConfig.isConnectionKastieCenter();
		if(isConn) {
			VerificationBuser.create().init();
			LocalOAuthUtil.getInstall().init();
		}
		// 返回当前系统配置信息
		JSONObject json = new JSONObject();
		parseJSON(json,false);
		return R.ok(json);
	}
	@AuthIgnore
	@GetMapping("/sysConfig.do")
	R sysConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 返回当前系统配置信息
		JSONObject json = new JSONObject();
		parseJSON(json,false);
		return R.ok(json);
	}
	
	@PostMapping("/appList.do")
	R appList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 返回当前系统配置信息
		String diyConfigJsonStr = KasiteConfig.getDiyVal("GuardConfig");
		if(StringUtils.isNotBlank(diyConfigJsonStr)) {
			JSONArray jsonArr = new JSONArray();
			JSONObject obj = JSONObject.parseObject(diyConfigJsonStr).getJSONObject("AppList");
			for (Map.Entry<String, Object> entity : obj.entrySet()) {
				String key = entity.getKey();
				JSONObject appInfoJson = (JSONObject) entity.getValue();
				JSONObject appInfo = new JSONObject();
				appInfo.put("appId", key);
				appInfo.put("name", appInfoJson.getString("name"));
				appInfo.put("path", appInfoJson.getString("path"));
				jsonArr.add(appInfo);
			}
			return R.ok(jsonArr);
		}
		return R.ok();
	}
	
	private static String getPwd() {
		return IDSeed.next().substring(0, 10);
	}

	private String getPublicKey(String appId) {
		return getAppInfo(appId).getString("publicKey");
	}
	
	private JSONObject getAppInfo(String key) {
		String jsonstr = KasiteConfig.getDiyVal("GuardConfig");
		JSONObject json = JSONObject.parseObject(jsonstr);
		JSONObject appList = json.getJSONObject("AppList");
		return appList.getJSONObject(key);
	}

	private JSONObject paresRequest(HttpServletRequest request) {
		Map<String,String[]> paraMap =request.getParameterMap();
		JSONObject retJson = new JSONObject();
		if(null != paraMap && paraMap.size() > 0) {
			if(paraMap!=null && !paraMap.isEmpty()){
				Set<String> keySet = paraMap.keySet();
				for(String key:keySet){
					Object value = request.getParameter(key);
					retJson.put(key,value);
				}
			}
		}
		String pwd = getPwd();
		retJson.put("pwd", pwd);
		return retJson;
	}
	
	private String getUrl(String appId,String module,String name) {
		JSONObject appJson = getAppInfo(appId);
		String url = appJson.getString("url");
		StringBuffer sbf = new StringBuffer();
		sbf.append(url);
		sbf.append("/").append(module);
		sbf.append("/").append(name);
		sbf.append(".do");
		return sbf.toString();
	}
	
	/**
	 * 上传文件
	 * @param reqParam
	 * @param newsFile
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/uploadFile.do")
	R upload(HttpServletRequest request) throws Exception {
		List<MultipartFile> files =((MultipartHttpServletRequest)request).getFiles("file"); 
		JSONObject reqParamJson = paresRequest(request);
		String pwd = reqParamJson.getString("pwd");
		//先存放临时目录 
		StringBuffer rootDir = KasiteConfig.getLocalConfigPathByName(fileDir, true, true, true);
		rootDir.append(File.separator).append(pwd);
		File tempFileDir = new File(rootDir.toString());
		if(!tempFileDir.exists()) {
			tempFileDir.mkdirs();
		}
		MultipartFile mtFile = null;
        BufferedOutputStream stream = null;
        for (int i =0; i< files.size(); ++i) { 
        	mtFile = files.get(i); 
            if (!mtFile.isEmpty()) { 
                try { 
                	String fileOrgigName = mtFile.getOriginalFilename();
                	File f = new File(rootDir.toString()+ (File.separator) +fileOrgigName);
                    byte[] bytes = mtFile.getBytes(); 
                    stream = new BufferedOutputStream(new FileOutputStream(f)); 
                    stream.write(bytes); 
                    stream.close(); 
                } catch (Exception e) { 
                	if(null != stream) stream.close();
                    stream =  null;
                    return R.error("You failed to upload " + i + " =>" + e.getMessage()); 
                } 
            } else { 
                return R.error(); 
            } 
        } 
        if(files.size() > 0 ) {
    		String appId = reqParamJson.getString("appId");
    		String module = "systemApi";
    		String name = "update";
    		String filepath = rootDir.toString();
    		File unzipFile = new File(filepath);
    		String zipFileName = IDSeed.next()+"_temp.zip";
    		File file = Zipper.zipFile(unzipFile, zipFileName, pwd);
    		String fileName = file.getName();
    		String tempFile =filepath + (File.separator) +zipFileName;
    		FileOper.moveFile(file.getAbsolutePath(), tempFile);
    		reqParamJson.put("fileName", fileName);
    		String content = reqParamJson.toJSONString();
    		String encryContent = KasiteRSAUtil.rsaEncrypt(content, getPublicKey(appId), "utf-8");
    		SoapResponseVo vo =uploadMultiFile(new File(tempFile), getUrl(appId, module, name), encryContent);
    		String ret = vo.getResult();
    		JSONObject obj = JSONObject.parseObject(ret);
            return R.ok(DesUtil.decrypt(obj.getString("result"), "UTF-8", pwd)); 
        }
        return R.ok(); 
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
	
	
	/*
	 *下载文件
	 */
	@PostMapping("/{appId}/{module}/{name}/download.do")
	void download(@PathVariable("appId") String appId, @PathVariable("module") String module,
			@PathVariable("name") String name,HttpServletRequest request,HttpServletResponse response) throws Exception {
		InputStream fis = null;
		OutputStream toClient = null;
		try {
			JSONObject reqParamJson = paresRequest(request);
			String pwd = reqParamJson.getString("pwd");
			String path = reqParamJson.getString("path");
			String ext = path.substring(path.lastIndexOf(".") + 1).toUpperCase();
			
			String content = reqParamJson.toJSONString();
			String encryContent = KasiteRSAUtil.rsaEncrypt(content, getPublicKey(appId), "utf-8");
			//先存放临时目录 
			StringBuffer rootDir = KasiteConfig.getLocalConfigPathByName(fileDir, true, true, true);
			rootDir.append(File.separator).append(pwd).append(File.separator);
			File tempFile = new File(rootDir.toString());
			if(!tempFile.exists()) {
				tempFile.mkdirs();
			}
			tempFile = new File(rootDir.toString()+pwd+"."+ext);
			HttpRequestBus.create(getUrl(appId, module, name), RequestType.post)
					.addHttpParam("reqParam", encryContent).downLoad(tempFile);
					
			// 以流的形式下载文件。
	        fis = new BufferedInputStream(new FileInputStream(tempFile.getAbsolutePath()));
	        byte[] buffer = new byte[fis.available()];
	        fis.read(buffer);
	        fis.close();
	        // 清空response
	        response.reset();
	        // 设置response的Header
	        response.addHeader("Content-Disposition", "attachment;filename=" + new String((pwd+"."+ext).getBytes()));
	        response.addHeader("Content-Length", "" + tempFile.length());
	        toClient = new BufferedOutputStream(response.getOutputStream());
	        response.setContentType("application/octet-stream");
	        toClient.write(buffer);
	        toClient.flush();
	        toClient.close();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(null != toClient) {
				toClient.close();
			}
			if(null != fis) {
				fis.close();
			}
		}
		return;
	}
	
	/*
	 * 获取指定文件目录的文件列表
	 */
	@PostMapping("/{appId}/{module}/{name}/call.do")
	R call(@PathVariable("appId") String appId, @PathVariable("module") String module,
			@PathVariable("name") String name,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		R r = R.ok();
		JSONObject reqParamJson = paresRequest(request);
		String pwd = reqParamJson.getString("pwd");
		String content = reqParamJson.toJSONString();
		String encryContent = KasiteRSAUtil.rsaEncrypt(content, getPublicKey(appId), "utf-8");
		SoapResponseVo vo = HttpRequestBus.create(getUrl(appId, module, name), RequestType.post)
				.addHttpParam("reqParam", encryContent).send();
		String ret = vo.getResult();
		JSONObject obj = JSONObject.parseObject(ret);
		r.put("result", DesUtil.decrypt(obj.getString("result"), "UTF-8", pwd));
		return r;
	}
}

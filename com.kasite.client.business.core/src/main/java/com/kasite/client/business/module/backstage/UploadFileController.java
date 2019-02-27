package com.kasite.client.business.module.backstage;

import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kasite.client.wechat.service.WeiXinService;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.controller.AbstractController;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.wxmsg.IDSeed;

/**
 *
 * @className: UploadFileController
 * @author: zhaoy
 * @date: 2018年8月27日  下午15:19:16
 */
@RestController
public class UploadFileController extends AbstractController {

	public final static String fileDir = "uploadFile";
	
	@PostMapping("/upload/uploadFile.do")
	R upload(@RequestParam("newsFile") MultipartFile newsFile, HttpServletRequest request) throws Exception {
		StringBuffer rootDir = KasiteConfig.getLocalConfigPathByName(fileDir, true, true, true);
		String id = IDSeed.next().replaceAll("_", "");
		String fileName = newsFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        fileName = id+suffix;
        String filePath = rootDir.append("/").toString();
        String filePath2 = filePath.substring(filePath.indexOf(fileDir))+(fileName);
        this.uploadFile(newsFile.getBytes(), filePath, fileName);
		return R.ok().put("url", filePath2);
	}
	@PostMapping("/upload/uploadWxFile.do")
	R uploadWxFile(String configKey,String mediaId,String mediaName,HttpServletRequest request) throws Exception {
		StringBuffer rootDir = KasiteConfig.getLocalConfigPathByName(fileDir, true, true, true);
        String filePath = rootDir.append("/").toString();
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        String filePath2 = filePath.substring(filePath.indexOf(fileDir))+(mediaName);
		WeiXinService.getMedia(configKey, mediaId, filePath+mediaName);
		return R.ok().put("url", filePath2);
	}
	public void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }
}

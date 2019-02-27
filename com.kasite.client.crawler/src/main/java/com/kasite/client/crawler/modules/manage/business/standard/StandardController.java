package com.kasite.client.crawler.modules.manage.business.standard;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.kasite.client.crawler.config.ConfigBuser;
import com.kasite.client.crawler.config.XMDataCloudConfig;
import com.kasite.client.crawler.modules.manage.bean.standard.dbo.Standard;
import com.kasite.client.crawler.modules.manage.dao.standard.StandardDao;
import com.kasite.client.crawler.modules.utils.DateUtils;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.StringUtil;

import net.sf.json.JSONObject;

/**
 * 数据标准集控制类
 * 
 * @author cjy
 * @version V1.0
 * @date 2018年6月27日 上午16:54:30
 */
@Controller
@RequestMapping(value = "/standard")
public class StandardController {
	
	private final static Log log = LogFactory.getLog(StandardController.class);
	
	@Autowired
	private XMDataCloudConfig xMDataCloudConfig;
	
	@RequestMapping(value = "/getStandardList.do")
	@ResponseBody
	public String getStandardList(HttpServletRequest request, HttpServletResponse response) {
		Integer standardType = StringUtil.intEmptyToNull(request.getParameter("standardType"));
		Integer pageSize = StringUtil.intEmptyToNull(request.getParameter("pageSize"));
		Integer pageStart = StringUtil.intEmptyToNull(request.getParameter("PageStart"));
		JSONObject json = null;
		try {
			json = StandardDao.getStandardList(pageStart,pageSize,standardType);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return CommonUtil.getRetVal(RetCode.Success.RET_10000.getCode(), RetCode.Success.RET_10000.getMessage(), json);
	}
	
	
	@RequestMapping(value = "/delStandard.do")
	@ResponseBody
	public String delStandard(HttpServletRequest request, HttpServletResponse response) {
		String id = StringUtil.emptyToNull(request.getParameter("id"));

		Standard vo = new Standard();
		vo.setId(id);
		vo.setDel_flag("1");
		JSONObject json = null;
		try {
			Standard standard = StandardDao.getStandardById(id);
			if (standard.getCurrently().equals("1")) {
				return "该标准集已经发布，不能删除！";
			}
			StandardDao.delStandard(vo);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return CommonUtil.getRetVal(RetCode.Success.RET_10000.getCode(), RetCode.Success.RET_10000.getMessage(), json);
	}
	
	@RequestMapping(value = "/updateStandard.do")
	@ResponseBody
	public String updateStandard(HttpServletRequest request, HttpServletResponse response,String id,String name,String oldName,String describe) {
		Standard vo = new Standard();
		vo.setId(id);
		vo.setStandard_describe(describe);
		vo.setName(name);
		JSONObject json = null;
		try {
			StandardDao.delStandard(vo);//修改名字和描述；
			//文件名字修改后要将upload上传的文件改名
			String path = request.getServletContext().getRealPath("/") + "upload\\";
			File oldFile = new File(path+oldName);
			File newFile = new File(path+name);
			oldFile.renameTo(newFile); //重命名
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return CommonUtil.getRetVal(RetCode.Success.RET_10000.getCode(), RetCode.Success.RET_10000.getMessage(), json);
	}
	
	
	@RequestMapping(value = "/getStandardById")
	@ResponseBody
	public Standard getStandardById(HttpServletRequest request, HttpServletResponse response,String id) {
		Standard standard = null;
		try {
			standard = StandardDao.getStandardById(id);
			
		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return standard;
	}
	
	
	
	/**
	 * 新增标准集
	 * */
	@RequestMapping(value = "/uploadFile.do")
	@ResponseBody
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = null;
		String tips = "";
		String savePath = request.getServletContext().getRealPath("/") + "upload\\";
		StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest)request;
		String version = req.getParameter("version");
		String describe = req.getParameter("describe");
		String type = req.getParameter("type");
		if (StringUtil.isEmpty(version)) {
			return CommonUtil.getRetVal(RetCode.Common.ERROR_PARAM.getCode(), "版本号不能为空！",json);
		}
		
		try {
	        // 遍历文件参数（即formData的file）
	        Iterator<String> iterator = req.getFileNames();
	        if (!iterator.hasNext()) {
	        	return CommonUtil.getRetVal(RetCode.Common.ERROR_PARAM.getCode(), "标准集不能为空！",json);
			}
	        while (iterator.hasNext()) {
	            MultipartFile file = req.getFile(iterator.next());
	            
	            String fileNames = file.getOriginalFilename();
	            // 文件名
	            fileNames = new String(fileNames.getBytes("UTF-8"));
	            
	            int split = fileNames.lastIndexOf(".");
		        //文件前缀
		        String fileName = fileNames.substring(0, split);
		        //文件后缀
		        String fileType = fileNames.substring(split + 1, fileNames.length());
	            	
		        //时间字符串    
		        String timeStr = DateUtils.formatDateToString(new Date(), "yyyy-MM-dd-HH-mm-ss");	
		        fileNames = fileName + "_"+ timeStr + "." +fileType;

	            //saveFile.renameTo(new File(rename));
		
     
	            File saveFile = new File(savePath+fileNames);
	            // 文件内容 
	            byte[] content = file.getBytes();           
	            
	            //将文件存到当前目录下
	            FileUtils.writeByteArrayToFile(saveFile, content);
	            
	            Standard vo = new Standard();
	            String id = UUID.randomUUID().toString().replaceAll("-", "");
	            vo.setId(id);
	            vo.setName(fileNames);
	            vo.setStandard_describe(describe);
	            vo.setVersion(version);
	            vo.setDel_flag("0");
	            vo.setCurrently("0");
	            String url = ConfigBuser.create().getDatacloudConfigVo().getMedical_Url() + "upload/";
	            vo.setUrl(url+fileNames);
	            vo.setStandard_type(type);
	            tips = StandardDao.addStandard(vo);
	        }
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return CommonUtil.getRetVal(RetCode.Success.RET_10000.getCode(), tips, json);
	}
	
	/**
	 * 发布 
	 * */
	@RequestMapping(value = "/releaseFile.do")
	@ResponseBody
	public String releaseFile(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		
/*		JSONObject json = null;
		String id = request.getParameter("id");
		String hosId = request.getParameter("hosId");
		Online hos = OnlineDao.getOnlineById(hosId);
		
		Standard standard = StandardDao.getStandardById(id);
		String filePath = System.getProperty("user.dir") + "\\upload\\" + standard.getName();

		File file = new File(filePath);
		
		MediaType mediaType = MediaType.parse("multipart/form-data");		
	    RequestBody requestBody = new MultipartBody.Builder()
	            .setType(MultipartBody.FORM)
	            .addFormDataPart("folder", hos.getFolder())
	            .addFormDataPart("file", standard.getName(),
	                RequestBody.create(mediaType, file))
	            .build();
		
	    Request requestOKhttp = new Request.Builder()
	    		.url(hos.getUrl()+"standard/receiveFile.do")
	            .post(requestBody)
	            .build();
	    
		OkHttpClient okHttpClient = new OkHttpClient();
		
		try {
			Response responseOKhttp = okHttpClient.newCall(requestOKhttp).execute();
		    if (!responseOKhttp.isSuccessful()) 
		    	throw new IOException("Unexpected code " + responseOKhttp);
		    
		} catch (IOException e) {
			
			e.printStackTrace();
		}*/
		String id = request.getParameter("id");
		String standard_type = request.getParameter("type");
		Standard vo = new Standard();
		vo.setId(id);
		vo.setStandard_type(standard_type);
		JSONObject json = null;
		try {
			StandardDao.updateCurrently(vo);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return CommonUtil.getRetVal(RetCode.Success.RET_10000.getCode(), RetCode.Success.RET_10000.getMessage(), json);

	}
	
	/**
	 * 发布 - 接收
	 * */
/*	@RequestMapping(value = "/receiveFile.do")
	@ResponseBody
	public String receiveFile(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		JSONObject json = null;
		String folder = request.getParameter("folder");
		String savePath = System.getProperty("user.dir") + "\\standard\\" + "\\" + folder + "\\";
		StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest)request;
		try {
	        // 遍历文件参数（即formData的file）
	        Iterator<String> iterator = req.getFileNames();
	        while (iterator.hasNext()) {
	            MultipartFile file = req.getFile(iterator.next());
	            String fileNames = file.getOriginalFilename();
	            int split = fileNames.lastIndexOf(".");
		        //文件前缀
		        String fileName = fileNames.substring(0, split);//xxx_time
		        //获得原始文件的文件名
		        int split_ = fileName.lastIndexOf("_");
		        String oldFileName = fileName.substring(0, split_);
		        //文件后缀
		        String fileType = fileNames.substring(split + 1, fileNames.length());
	            
		        fileNames = oldFileName + "." + fileType;
		        // 文件内容 
	            byte[] content = file.getBytes();
	            //讲文件存到当前目录下
	            FileUtils.writeByteArrayToFile(new File(savePath+fileNames), content);
	        }
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return CommonUtil.getRetVal(RetCode.Success.RET_10000.getCode(), RetCode.Success.RET_10000.getMessage(), json);
	}*/
	

}

package com.kasite.core.common.util.wxmsg;
import java.io.File;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
public class WxMsgUtil {

	public enum Channel{
		/**
		 * 微信事件回调
		 */
		WX,
		/**
		 * 支付宝事件回调
		 */
		ZFB,
		/**
		 * 微信支付回调
		 */
		WXPay,
		/**
		 * 微信退款回调
		 */
		WXRefund,
		/**
		 * 支付宝支付回调
		 */
		ZFBPay,
		/**
		 * 支付宝退款回调
		 */
		ZFBRefund
		
		
	}
	
	private final static Object object = new Object();
	private static WxMsgUtil install;
	private String uploadFileDir;
	private String uploadFileDir_Temp_log;
	private String uploadFileDir_log;
	private String uploadFileDir_zip;
	private int size = 20;
	private final static String LOGFILESEIXNAME = ".wechatlog";

	private WxMsgUtil(String uploadFileDir) {
		this.uploadFileDir = uploadFileDir;
		initUploadDir();
	}

	private void initUploadDir() {
		// 如果是空的 则直接用系统工作空间目录
		if (StringUtil.isBlank(uploadFileDir)) {
			uploadFileDir = System.getProperty("user.dir");
		}
		uploadFileDir_Temp_log = uploadFileDir + File.separator + "temp";
		uploadFileDir_log = uploadFileDir + File.separator + "log";
		uploadFileDir_zip = uploadFileDir + File.separator + "zip";
		
		File tempFileDir = new File(uploadFileDir_Temp_log);
		// 如果文件夹不存在则创建文件夹
		if (!tempFileDir.exists() || !tempFileDir.isDirectory()) {
			tempFileDir.mkdirs();
		}
		File uploadFileDir = new File(uploadFileDir_log);
		if (!uploadFileDir.exists() || !uploadFileDir.isDirectory()) {
			uploadFileDir.mkdirs();
		}
		File zipFileDir = new File(uploadFileDir_zip);
		if (!zipFileDir.exists() || !zipFileDir.isDirectory()) {
			zipFileDir.mkdirs();
		}
		
	}

	public static WxMsgUtil create(String uploadFileDir) {
		if (null != install) {
			return install;
		}
		synchronized (object) {
			install = new WxMsgUtil(uploadFileDir);
		}
		return install;
	}

	public void write(String content,Channel channel) {
		//去掉字符串中的换行符
		content = content.replaceAll("\r\n|\r|\n","").trim()+"="+channel.name();
		initUploadDir();
		// 生成一个随机文件名
		String nowStr = null;
		Date now = getNowDate();
		try {
			nowStr = formatDate(now, "yyyyMMddHH");
		} catch (ParseException e) {
			e.printStackTrace();
			nowStr = IDSeed.next();
		}
		String fileName = nowStr+LOGFILESEIXNAME;
		String tempFilePath = uploadFileDir_Temp_log + File.separator + fileName;
		content = IDSeed.next() +"="+content+"\r\n";
		write(tempFilePath, content, true); 
	}
	public static String formatDate(Date date,String strFormat) throws ParseException
	{
		 
        SimpleDateFormat format = new SimpleDateFormat(strFormat);
        return format.format(date);
	}
	public static java.sql.Date getNowDate()
	{
		return new java.sql.Date(new Date().getTime());
	}
	public static java.sql.Timestamp getNowDateTime()
	{
		
		return new java.sql.Timestamp(new Date().getTime());
	}
	public static String getNow(String strFormat) throws ParseException
	{
		 	Date date = new Date();
	        SimpleDateFormat format = new SimpleDateFormat(strFormat);
	        return format.format(date);
	}
	private static boolean write(String path, String content, boolean hasAppend) {
		File writefile;
		try {
			writefile = new File(path);
			if (writefile.exists() == false) // 如果文本文件不存在则创建它
			{
				writefile.createNewFile();
				writefile = new File(path); // 重新实例化
			}
			FileWriter filewriter = new FileWriter(writefile, hasAppend);
			//System.out.println(content);
			filewriter.write(content);
			filewriter.flush();
			filewriter.close();
		} catch (Exception d) {
			d.printStackTrace();
			return false;
		}
		return true;
	}
	private static void deleteFile(File file){ 
	   if(file.exists()){ 
	    if(file.isFile()){ 
	     file.delete(); 
	    }else if(file.isDirectory()){ 
	     File files[] = file.listFiles(); 
	     for(int i=0;i<files.length;i++){ 
	      deleteFile(files[i]); 
	     } 
	    } 
	    file.delete(); 
	   }else{ 
		   System.out.println("所删除的文件不存在！"+'\n'); 
	   } 
	} 
	private void moveTempFiles() {
		File tempdir = new File(uploadFileDir_Temp_log);
		//查询临时文件夹目录 小于当前小时的文件都移走
		File[] tempfss = tempdir.listFiles();
		List<File> tempfs = new ArrayList<File>(); 
		if(null != tempfss && tempfss.length > 0) {
			for (File file : tempfss) {
				tempfs.add(file);
			}
		}
		if(null != tempfs) {
			Collections.sort(tempfs, new Comparator<File>() {
				public int compare(File f1, File f2) {
					long diff = f1.lastModified() - f2.lastModified();
					if (diff < 0)
						return 1;
					else if (diff == 0)
						return 0;
					else
						return -1;
				}
				public boolean equals(Object obj) {
					return true;
				}
			});
		}
		
		if(tempfs.size() == 1) {
			//如果只有一个的时候判断是否创建时间大于2个小时是的话就挪走
			File fi = tempfs.get(0);
			long lastmodify = fi.lastModified();
			Date now = getNowDate();
			long hours2 = 1000 * 60 * 60 * 2;
			if(now.getTime() - lastmodify < hours2) {
				tempfs.remove(0);
			}
		}else if(tempfs.size() > 1){
			//移除最新的文件，其它文件都移走
			tempfs.remove(0);
		}
		if(null != tempfs) {
			for (File file : tempfs) {
				String filePath = file.getAbsolutePath();
				String filName = file.getName();
				String newFilePath = uploadFileDir_log + File.separator + filName;
				moveFile(filePath, newFilePath);
			}
		}
	}
	
	/**
	 * 压缩文件
	 * @throws Exception 
	 */
	public File zipFiles() throws Exception {
		initUploadDir();
		moveTempFiles();
		File uploadFileDir = new File(uploadFileDir_log);
		// 查询文件数量
		File[] lfs = uploadFileDir.listFiles();
		List<File> fs = new ArrayList<File>();
		for (int i = 0; i < lfs.length; i++) {
			//判断后缀名为定义的文件才移动否则不移动
			File f = lfs[i];
			fs.add(f);
		}
		List<File> uploadFileList = new ArrayList<File>();
		if(null != fs && fs.size() > size) {
			Collections.sort(fs, new Comparator<File>() {
				public int compare(File f1, File f2) {
					long diff = f1.lastModified() - f2.lastModified();
					if (diff > 0)
						return 1;
					else if (diff == 0)
						return 0;
					else
						return -1;
				}
				public boolean equals(Object obj) {
					return true;
				}
			});
			for (int i = 0; i < size; i++) {
				uploadFileList.add(fs.get(i));
			}
		}else {
			uploadFileList = fs;  
		}
		//创建压缩文件夹目录
		if(uploadFileList.size() > 0) {
			String filename = "Zip_"+IDSeed.next();
			String zipFileDir = uploadFileDir_zip + File.separator + filename;
			File zipFile = new File(zipFileDir);
			if(!zipFile.exists() || !zipFile.isDirectory()) {
				zipFile.mkdirs();
			}
			//把文件都移动到需要压缩的文件夹
			for (File file2 : uploadFileList) {
				String fileName = file2.getName();
				String filepath = file2.getAbsolutePath();
				String newfilepath = zipFileDir+File.separator+fileName;
				moveFile(filepath, newfilepath);
			}
			//把其它文件移动到需要压缩的文件夹
			File f = Zipper.zipFileForAll(zipFile, uploadFileDir_zip + File.separator + filename + ".zip",null);
			//如果文件正常生成 则删除旧的文件
			if(f.exists() && f.isFile()) {
				deleteFile(zipFile);
				return f;
			}else {
				return null;
			}
		}
		return null;
	}
	/**
	 * 移动一个文件或文件夹
	 * */
	private static void moveFile(String filepath,String newfilepath){
		File f = new File(filepath);
		if(f.exists()){
			f.renameTo(new File(newfilepath));
		}
	}

	
	public static void main(String[] args) throws Exception {
//		final String uploadFileDir = "/Users/daiyanshui/Desktop/KASITE/Test";
//		new Thread(()->{
//			while(true) {
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				WxMsgUtil.create(uploadFileDir).write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
//						"<xml><ToUserName><![CDATA[gh_3b864faf50cb]]></ToUserName>\n" + 
//						"<FromUserName><![CDATA[oI-7ct3oh2kODLzLsRBpiWPNrVRc]]></FromUserName>\n" + 
//						"<CreateTime>1530176424</CreateTime>\n" + 
//						"<MsgType><![CDATA[event]]></MsgType>\n" + 
//						"<Event><![CDATA[subscribe]]></Event>\n" + 
//						"<EventKey><![CDATA[qrscene_aa266efa1cf94cda8514859a5743b544]]></EventKey>\n" + 
//						"<Ticket><![CDATA[gQEP8TwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAycUpjdE4zektic1QxMDAwMHcwN0gAAgTXnDRbAwQAAAAA]]></Ticket>\n" + 
//						"</xml>",Channel.WX);
//			}
//		}
//		).start();
//		
//		new Thread(()->{
//			while(true) {
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				try {
//					WxMsgUtil.create(uploadFileDir).zipFiles();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		).start();
//		
		
	}
}

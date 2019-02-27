package com.kasite.core.common.util;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 文件处理工具
 * @author zgc
 *
 */
public class FileTool {
	
	/**
	 * 写文件到硬盘
	 * @param is
	 * @param savePath
	 * @param fnewname
	 * @return
	 */
	public static int writeFile(InputStream is,String savePath,String fnewname){
		OutputStream os = null;
		int result = 0 ;
		try{
			File saveDir = new File(savePath);
			if(!saveDir.exists()){
				saveDir.mkdirs();//如果文件不存在则创建文件夹
			}
			File file = new File(saveDir,fnewname); //创建新文件
			file.createNewFile();

			//使用流的形式上传文件
			is = new BufferedInputStream(is);//获得文件输入流
			os = new BufferedOutputStream(new FileOutputStream(file));//创建输入流
			byte data[] = new byte[1024];
			int len = 0;
			while((len=is.read(data))>0){//起到缓冲作用
				os.write(data,0,len);
			}
			os.flush();
			os.close();
			is.close();
			result = 1;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(os!=null){
				try {
					os.close();
					os=null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(is!=null){
				try {
					is.close();
					is=null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}

	/**
	 * 写文件到硬盘
	 * @param is
	 * @param newFile
	 * @return
	 */
	public static int writeFile(InputStream is,File newFile){
		OutputStream os = null;
		int result = 0 ;
		try{
			newFile.createNewFile();

			//使用流的形式上传文件
			is = new BufferedInputStream(is);//获得文件输入流
			os = new BufferedOutputStream(new FileOutputStream(newFile));//创建输入流
			byte data[] = new byte[1024];
			int len = 0;
			while((len=is.read(data))>0){//起到缓冲作用
				os.write(data,0,len);
			}
			os.flush();
			os.close();
			is.close();
			result = 1;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(os!=null){
				try {
					os.close();
					os=null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(is!=null){
				try {
					is.close();
					is=null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 创建存放文件的文件夹和文件
	 * @param foldname 原来文件名称
	 * @param request
	 * @return
	 */
	public static Map<String, Object> getStoreFile(String foldname,HttpServletRequest request,String gameType) 
	{	
		Map<String, Object> map = null;
		try
		{
			map = new HashMap<String, Object>(16);
			 
			//对上传的文件重命名							
			Date date = new Date();
			String expand = foldname.substring(foldname.lastIndexOf("."),foldname.length());			
			String fnewname = UUID.randomUUID().toString()+expand; //新的文件名
							
			//获得项目根目录所在的真实路径
			String path = request.getSession().getServletContext().getRealPath("/");
			
			//根据上传时间创建文件目录，把文件打散存储到不同目录，可以加快查询速度，否则同一个文件夹下太多文件服务器速度会变慢
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int y = c.get(Calendar.YEAR);
			int m = c.get(Calendar.MONTH)+1;
			int d = c.get(Calendar.DATE);
			String p = "files/"+gameType+"/"+y+"/"+m+"/"+d+"/";//存放文件的目录："files/游戏类型/年/月/日/"
			path = path+p;
			
			File saveDir = new File(path);
			if(!saveDir.exists()){
				saveDir.mkdirs();//如果文件不存在则创建文件夹
			}
					
			File file=new File(saveDir,fnewname); //创建新文件
			file.createNewFile();
			map.put("file", file);//添加新创建的文件
			map.put("fileurl", p+fnewname);////添加文件存放的文件目录
			map.put("newname", fnewname);//新文件名称
			
		}
		catch(Exception e)
		{
			return null;
		}
			
		return map;
	}
	
	/**
	 * 下载文件
	 * @param response
	 * @param coding 编码
	 * @param savePath 文件路径(包含文件名称)
	 * @param fnewname 文件名称(下载时显示名称)
	 * @return
	 */
	public static int downloadFile(HttpServletResponse response,String coding,String savePath,String fnewname){
		int result = 0 ;
		InputStream is = null;
		OutputStream os = null;
		try{
			if(coding==null || "".equals(coding)){
				coding = "GBK";
			}
			File file  = new File(savePath);
			if(file!=null && file.exists()){
				//设置Response对象参数
				//response.reset();
				response.setContentType("application/x-download;charset="+coding);
				
				//设置下载文件名是中文
				response.setHeader("Content-Disposition", "attachment;filename="+new String(fnewname.getBytes(coding),"iso8859-1"));
				response.setHeader("filename", URLEncoder.encode(fnewname,coding));
				response.setHeader("filesize", file.length()+"");
				response.setCharacterEncoding(coding);
				is = new BufferedInputStream(new FileInputStream(file));//创建输入流
				os = new BufferedOutputStream(response.getOutputStream());//通过response获得输出流
				byte data[] = new byte[1024];
				int len = 0;
				while((len=is.read(data))>0){
					os.write(data, 0, len);					
				}
				os.flush();
				os.close();
				is.close();
				result = 1;
			}

			
		}catch(Exception e){
			//e.printStackTrace();
		}finally{
			if(os!=null){
				try {
					os.close();
					os=null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(is!=null){
				try {
					is.close();
					is=null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	/**
     * 复制文件
     * @param srcFile 原文件
     * @param destFile 目标文件
     * @param srcCoding 源文件编码
     * @param destCoding 目标文件编码
     * @throws IOException
     */
    public static void copyFile(File srcFile, File destFile, String srcCoding, String destCoding) throws IOException {
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile), srcCoding));
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFile), destCoding));
            char[] cbuf = new char[1024];
            int len = cbuf.length;
            int off = 0;
            int ret = 0;
            while ((ret = br.read(cbuf, off, len)) > 0) {
                off += ret;
                len -= ret;
            }
            bw.write(cbuf, 0, off);
            bw.flush();
        } finally {
            if (br != null){
                br.close();
            }
            if (bw != null){
                bw.close();
            }
        }
    }
    
    /**
     * 复制文件
     * @param sourceFile 原文件
     * @param targetFile 目标文件
     * @throws IOException
     */
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null){
                inBuff.close();
            }
            if (outBuff != null){
                outBuff.close();
            }
        }
    }
    
    /**
     * 删除文件夹
     * @param filepath
     * @throws IOException
     */
    public static void del(String filepath) throws IOException {
        File f = new File(filepath);// 定义文件路径
        if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
            if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
                f.delete();
            } else {// 若有则把文件放进数组，并判断是否有下级目录
                File[] delFile = f.listFiles();
                int i = f.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (delFile[j].isDirectory()) {
                        del(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
                    }
                    delFile[j].delete();// 删除文件
                }
                f.delete();
            }
        }
    }
    

   /**
    * 读取目录及子目录下指定文件名的路径, 返回一个文件路径List 
    * @param path 文件路径 
    * @param suffix 后缀名, 为空则表示所有文件 
    * @param isdepth 是否遍历子目录 
    * @return
    */
   public static List<String> getListFiles(String path, String suffix,
			boolean isdepth) {
		List<String> lstFileNames = new ArrayList<String>();
		File file = new File(path);
		return getListFiles(lstFileNames, file, suffix, isdepth);
	}
	private static List<String> getListFiles(List<String> lstFileNames, File f,
			String suffix, boolean isdepth) {
		// 若是目录, 采用递归的方法遍历子目录
		if (f.isDirectory()) {
			File[] t = f.listFiles();
			for (int i = 0; i < t.length; i++) {
				if (isdepth || t[i].isFile()) {
					getListFiles(lstFileNames, t[i], suffix, isdepth);
				}
			}
		} else {
			String filePath = f.getAbsolutePath();
			if (suffix!=null && !"".equals(suffix)) {
				int begIndex = filePath.lastIndexOf("."); // 最后一个.(即后缀名前面的.)的索引
				String tempsuffix = "";
				if (begIndex != -1) {
					tempsuffix = filePath.substring(begIndex + 1, filePath.length()).toLowerCase();
					if (tempsuffix.equals(suffix.toLowerCase())) {
						lstFileNames.add(filePath);
					}
				}
			} else {
				lstFileNames.add(filePath);
			}
		}
		return lstFileNames;
	} 
	
	/**
	 * 读取目录及子目录下指定文件名的路径, 返回一个文件对象List 
	 * @param path 文件路径
	 * @param suffix  后缀名, 为空则表示所有文件
	 * @param isdepth  是否遍历子目录
	 * @return
	 */
	public static List<File> getFileList(String path, String suffix,
			boolean isdepth) {
		List<File> fileList = new ArrayList<File>();
		File file = new File(path);
		return getFileList(fileList, file, suffix, isdepth);
	}
	private static List<File> getFileList(List<File> fileList, File f,
			String suffix, boolean isdepth) {
		// 若是目录, 采用递归的方法遍历子目录
		if (f.isDirectory()) {
			File[] t = f.listFiles();
			for (int i = 0; i < t.length; i++) {
				if (isdepth || t[i].isFile()) {
					getFileList(fileList, t[i], suffix, isdepth);
				}
			}
		} else {
			String filePath = f.getAbsolutePath();
			if (suffix!=null && !"".equals(suffix)) {
				int begIndex = filePath.lastIndexOf("."); // 最后一个.(即后缀名前面的.)的索引
				String tempsuffix = "";
				if (begIndex != -1) {
					tempsuffix = filePath.substring(begIndex + 1, filePath.length()).toLowerCase();
					if (tempsuffix.equals(suffix.toLowerCase())) {
						fileList.add(f);
					}
				}
			} else {
				fileList.add(f);
			}
		}
		return fileList;
	}
	
    
    /**
	 * 复制文件
	 * 
	 * @param sourceUrl
	 *            原文件路径
	 * @param targetFile
	 *            目标文件
	 * @throws IOException
	 */
    public static boolean copyFile(String sourceUrl, File targetFile){
    	boolean result = true;
    	try{
        	File sourceFile = new File(sourceUrl);
    		if(sourceFile.exists()){
    			targetFile.createNewFile();//在临时目录下创建安装文件
    			FileTool.copyFile(sourceFile, targetFile);//复制文件
    		}
    	}catch(Exception e){
    		result = false;
    		e.printStackTrace();
    	}
    	return result;
    }
    
    /**
     * 复制文件夹
     * @param sourceDir
     * @param targetDir
     * @throws IOException
     */
    public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
                copyFile(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }
    
    /**
     * 
     * @param sourceDirPath
     * @param targetDirPath
     * @throws IOException
     */
    public static void copyDir(String sourceDirPath, String targetDirPath) throws IOException {
        // 创建目标文件夹
    	File targetFile = new File(targetDirPath);
    	if(!targetFile.exists()){
    		targetFile.mkdirs();
    	}
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDirPath)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 复制文件
                //String type = file[i].getName().substring(file[i].getName().lastIndexOf(".") + 1);
                FileTool.copyFile(file[i], new File(targetDirPath + file[i].getName()));
            }
            if (file[i].isDirectory()) {
                // 复制目录
                String sourceDir = sourceDirPath + File.separator + file[i].getName();
                String targetDir = targetDirPath + File.separator + file[i].getName();
                FileTool.copyDirectiory(sourceDir, targetDir);
            }
        }
    }
    
    /**
	 * 剪切文件
	 * @param srcFile 原文件
	 * @param newDirPath 新目录路径
	 * @return 操作结果
	 */
	public static boolean movetoFile(File srcFile,String newDirPath)
	{
		boolean flag = false;
		try
		{
			if(srcFile!=null && srcFile.exists()){
				File newDir = new File(newDirPath); 				
				if(!newDir.exists()) 
				{
					newDir.mkdirs(); //如果不存在创建文件夹 
				}					
				//将文件移到新文件里 
				String fileName=srcFile.getName();
				File fnew = new File(newDirPath +"/"+fileName); 
				if(fnew.exists())
				{
					fnew.delete();
				}
				flag = srcFile.renameTo(fnew);
				//System.out.println("--fileName-->"+fileName+"--flag------>"+flag);
			}
		}
		catch(Exception e)
		{
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
    
	/**
	 * 传送通信字符
	 * @param response
	 * @param Content
	 */
	public static void outPrint(HttpServletResponse response, String coding,String content) 
	{
		PrintWriter pw = null;
		try
		{ 
			if(coding==null || "".equals(coding)){
				coding = "GBK";
			}
			response.setCharacterEncoding(coding);
			pw = response.getWriter(); 
			pw.print(content);
			pw.flush();
			pw.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(pw!=null){
				pw.flush();
				pw.close();
				pw=null;
			}
		}
		
	}
	
	/**
	 * 获得随机数,随机数的范围为:0--(maxValue-1)
	 * @param maxValue
	 * @return
	 */
	public static int getRandom(int maxValue){
		Random random = new Random();
		return Math.abs(random.nextInt())%maxValue;
	}
	
	/**
	 * 将文件List列表转换成Map
	 * @param files 文件列表
	 * @return 文件名称做key(全部转小写)，文件做value的Map
	 */
	public static Map<String, File> getListToMap(List<File> files){
		Map<String, File> fileMap = new HashMap<String, File>(16);
		if(files!=null && files.size()>0){
			for(File file : files){
				String fileName = file.getName();
				fileName = fileName.trim().toLowerCase();
				fileMap.put(fileName, file);
			}
		}
		return fileMap;
	}
	
	/**
	 * 重新打乱顺序
	 * @param wordBeanList
	 * @return
	 */
	public static List<File> reSequence(List<File> picList){
		List<File> list = null;		
	    int size = picList.size();
	    if(size>0){
	    	list = new ArrayList<File>();	
			Map<Integer, Boolean> indexMap = new HashMap<Integer, Boolean>(16);//存储已经存在的序号
			for(int i=0;i<size;i++){
				Integer index = getRandom(size);
				while(indexMap.get(index)!=null){
					index = getRandom(size);
				}
				list.add(picList.get(index));
				indexMap.put(index, true);
			}
	    }
	    return list;
	}
	
	/**
	 *  重新打乱字符列表顺序
	 * @param strList
	 * @return
	 */
	public static List<String> reStrSequence(List<String> strList){
		List<String> list = null;		
	    int size = strList.size();
	    if(size>0){
	    	list = new ArrayList<String>();	
			Map<Integer, Boolean> indexMap = new HashMap<Integer, Boolean>(16);//存储已经存在的序号
			for(int i=0;i<size;i++){
				Integer index = getRandom(size);
				while(indexMap.get(index)!=null){
					index = getRandom(size);
				}
				list.add(strList.get(index));
				indexMap.put(index, true);
			}
	    }
	    return list;
	}
	
	public static void main(String[] args) {
		String path = "D:/Program Files/apache-tomcat-6.0.24/webapps/rzxt_bm/files/picPicMatching/2013/10/18/pics";
		List<File> list = getFileList(path,"jpg",true);
		System.out.println("---size--->"+list.size());
	}
}

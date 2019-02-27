package com.kasite.client.crawler.modules.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileOper {

	public static void main(String[] args) {

	}

	/**
	 * 写文件
	 * 
	 * @param pathName
	 *            文件名路径
	 * @param content
	 *            内容
	 * @return
	 */
	public static boolean write(String pathName, String content) {
		return write(pathName, content, "UTF-8");
	}

	/**
	 * 写文件
	 * 
	 * @param pathName
	 *            文件名路径
	 * @param content
	 *            内容
	 * @param charsetName
	 *            编码
	 * @return
	 */
	public static boolean write(String pathName, String content, String charsetName) {
		try {
			return write(pathName, content.getBytes(charsetName));
		} catch (UnsupportedEncodingException e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	/**
	 * 写文件
	 * 
	 * @param pathName
	 *            文件名路径
	 * @param content
	 *            内容
	 * @param charsetName
	 *            编码
	 * @return
	 */
	public static boolean write(String pathName, byte[] content ) {
		try {
			File writefile = new File(pathName);
			if (!writefile.exists()) {
				writefile.createNewFile();
				writefile = new File(pathName);
			}
			FileOutputStream outSTr = new FileOutputStream(writefile);
			BufferedOutputStream Buff = new BufferedOutputStream(outSTr);
			Buff.write(content);
			Buff.flush();
			Buff.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 移动文件、重命名文件
	 * 
	 * @param filepath
	 *            原文件名
	 * @param newfilepath
	 *            新文件名
	 * @return
	 */
	public static boolean moveFile(String filepath, String newfilepath) {
		try {

			File f = new File(filepath);
			if (f.exists()) {
				if (f.renameTo(new File(newfilepath))) {

				} else {
					System.out.println("重命名失败:" + filepath + "->" + newfilepath);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 文件读取
	 *
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 * @author 無
	 * @date 2018年6月25日 上午10:08:39
	 */
	public static String read(String filePath) throws Exception {
		File file = new File(filePath);
		if (file.exists() && file.isFile()) {
			byte[] data = Files.readAllBytes(file.toPath());
			return new String(data, StandardCharsets.UTF_8);
		}
		return null;
	}
}

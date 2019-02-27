package test.com.kasite.core.common;

import com.kasite.core.common.util.FileToBase64;

/**
 * 测试将证书转成base64字符串
 * 将字符串转证书
 * @author daiyanshui
 *
 */
public class TestFileToBase64 {

	
	static void fileToBase64(String filePath) {
		String s = FileToBase64.encryptToBase64(filePath);
		System.out.println("将文件转base64 字符串\r\n"+s);
	}
	static void base64ToFile(String base64Str,String filePath) throws Exception {
		FileToBase64.toFile(base64Str, filePath);
	}
	
	public static void main(String[] args) {
		
		fileToBase64("/Users/daiyanshui/Desktop/mCopy/apiclient_cert.p12");
		
	}
}

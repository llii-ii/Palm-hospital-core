package com.kasite.core.common.util;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

import org.springframework.util.Base64Utils;

import com.coreframework.util.FileOper;
public class FileToBase64 {
    /**
    * <p>将文件转成base64 字符串</p> 
    * @param path 文件路径
    * @return
    * @throws Exception
    */
//    public static String encodeBase64File(String path) throws Exception {
//        File file = new File(path);
//        FileInputStream inputFile = new FileInputStream(file);
//        byte[] buffer = new byte[(int)file.length()];
//        inputFile.read(buffer);
//        inputFile.close();
//        return new BASE64Encoder().encode(buffer);
//    }

	public static String encryptToBase64(String filePath) {
		if (filePath == null) {
			return null;
		}
		try {
			byte[] b = Files.readAllBytes(Paths.get(filePath));
			return Base64.getEncoder().encodeToString(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
    /**
    * <p>将base64字符解码保存文件</p>
    * @param base64Code
    * @param targetPath
    * @throws Exception
    */
//    public static void decoderBase64File(String base64Code,String targetPath) throws Exception {
//        byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
//        FileOutputStream out = new FileOutputStream(targetPath);
//        out.write(buffer);
//        out.close();
//    }
    public static String decryptByBase64(String base64, String filePath) {
		if (base64 == null && filePath == null) {
            return "生成文件失败，请给出相应的数据。";
		}
		try {
			Files.write(Paths.get(filePath), Base64.getDecoder().decode(base64),StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "指定路径下生成文件成功！";
    }
    /**
    * <p>将base64字符保存文本文件</p>
    * @param base64Code
    * @param targetPath
    * @throws Exception
    */
    public static void toFile(String base64Code,String targetPath) throws Exception {
        byte[] b3 = Base64Utils.decodeFromString(base64Code);
		File file2 = new File(targetPath);
		FileOutputStream fos = new FileOutputStream(file2);
		fos.write(b3);
		fos.flush();
		fos.close();
//        FileOutputStream out = new FileOutputStream(targetPath);
//        out.write(buffer);
//        out.close();
    }
    
    public static void main(String[] args) {
        try {
            String base64Code =encryptToBase64("/Users/daiyanshui/Downloads/cert-jkzl/apiclient_cert.p12");
            System.out.println(base64Code);
            decryptByBase64(base64Code, "/Users/daiyanshui/Downloads/cert-jkzl/test/apiclient_cert.p12");
            FileOper.write("/Users/daiyanshui/Downloads/cert-jkzl/test/apiclient_cert.txt", base64Code, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String base64Code =encryptToBase64("/Users/daiyanshui/Downloads/cert-kst/apiclient_cert.p12");
            System.out.println(base64Code);
            decryptByBase64(base64Code, "/Users/daiyanshui/Downloads/cert-kst/test/apiclient_cert.p12");
            FileOper.write("/Users/daiyanshui/Downloads/cert-kst/test/apiclient_cert.txt", base64Code, false);//(base64Code, );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
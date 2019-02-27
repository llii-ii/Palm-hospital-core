package test.com.kasite.client.crawler;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

 

/**
 * 用于数字ID的加密和解密函数 by lch20150609
 */
public class SecretUtil {
	private final static String password = "zkzlcom1yihu67890123";
	private final static byte[] passwordByte = password.getBytes();
	private static Cipher cipher = null;
	private static Cipher desCipher = null;
	private static String DefaultEncoding="UTF-8";

	static{
		SecureRandom random = new SecureRandom();
		DESKeySpec desKey;
		try {
			desKey = new DESKeySpec(passwordByte);

			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成加密操作
			cipher = Cipher.getInstance("DES");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
	
			// DES算法要求有一个可信任的随机数源
			SecureRandom desRandom = new SecureRandom();
			// 创建一个DESKeySpec对象
			DESKeySpec desDesKey = new DESKeySpec(passwordByte);
			// 创建一个密匙工厂
			SecretKeyFactory desKeyFactory = SecretKeyFactory.getInstance("DES");
			// 将DESKeySpec对象转换成SecretKey对象
			SecretKey desSecurekey = keyFactory.generateSecret(desDesKey);
			// Cipher对象实际完成解密操作
			desCipher = Cipher.getInstance("DES");
			// 用密匙初始化Cipher对象
			desCipher.init(Cipher.DECRYPT_MODE, desSecurekey, desRandom);	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	// 测试
	public static void main(String args[]) throws Exception {
		


		// 待加密内容
		String str = "yihu";

		String result = SecretUtil.encrypt(str);
		System.out.println("加密后：" + new String(result));

		// 直接将如上内容解密
		String decryResult = SecretUtil.decrypt(result);
		System.out.println("解密后：" +decryResult);


	}

	/**
	 * 加密
	 * 
	 * @param 需要加密的字符串
	 * @return 加密之后的字符串
	 */
	public static String encrypt(String str) {
		return encrypt(str,SecretUtil.DefaultEncoding);
	}

	/**
	 * 解密
	 * @param 需要解密的字符串
	 * @return 解密之后的字符串
	 */
	public static String decrypt(String src)  {
		return decrypt(src,SecretUtil.DefaultEncoding);
	}
	
	/**
	 * 加密
	 * 
	 * @param 需要加密的字符串
	 * @return 加密之后的字符串
	 */
	public static String encrypt(String str,String encoding) {
		try {

			// 现在，获取数据并加密
			// 正式执行加密操作
			byte[] datasource=str.getBytes(encoding);
			byte[] result=cipher.doFinal(datasource);
			return new String(new Base64().encode(result),encoding);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * @param 需要解密的字符串
	 * @return 解密之后的字符串
	 */
	public static String decrypt(String src,String encoding)  {
		try{
			Base64 decoder = new Base64(); 
			byte[] b =decoder.decode(src);
			// 真正开始解密操作
			return new String(desCipher.doFinal(b),encoding);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
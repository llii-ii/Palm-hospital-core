/**
 * 
 */
package com.yihu.hos.util;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author Administrator
 *
 */
public class DES {

	Key key;
	public DES(String str) {
	    try {
			setKey(str);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//生成密匙
	}
	public DES() {
		String key = "siyue_qisiyue_qisiyue_qi";
	    try {
			setKey(key);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//	/**
//	 * 根据参数生成KEY 
//	 */
//	public void setKey(String strKey) {
//		try {
//			KeyGenerator _generator = KeyGenerator.getInstance("DES");
//			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
//			random.setSeed(strKey.getBytes("UTF-8"));
//			_generator.init(128,random); 
//			this.key = _generator.generateKey();
//			_generator = null;
//		} catch (Exception e) {
//			throw new RuntimeException(
//					"Error initializing SqlMap class. Cause: " + e);
//		}
//	}
	/**
	   * 根据参数生成KEY 
	 * @throws NoSuchAlgorithmException 
	   */
	public void setKey(String key) throws NoSuchAlgorithmException {
		  if (null == key || key.length() == 0) {
	            throw new NullPointerException("key not is null");
	        }
	        SecretKeySpec key2 = null;
	        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
	        random.setSeed(key.getBytes());
	        try {
	            KeyGenerator kgen = KeyGenerator.getInstance("AES");
	            kgen.init(128, random);
	            SecretKey secretKey = kgen.generateKey();
	            byte[] enCodeFormat = secretKey.getEncoded();
	            key2 = new SecretKeySpec(enCodeFormat, "AES");
	        } catch (NoSuchAlgorithmException ex) {
	            throw new NoSuchAlgorithmException();
	        }
	        this.key = key2;

	}
	
	
	/**
	   * 加密String明文输入,String密文输出
	   */
	public String getEncString(String strMing) {
	      byte[] byteMi = null;
	      byte[] byteMing = null;
	      String strMi = "";
	      BASE64Encoder base64en = new BASE64Encoder();
	      try {
	        byteMing = strMing.getBytes("UTF-8");
	        byteMi = this.getEncCode(byteMing);
	        strMi = base64en.encode(byteMi);
	      } catch (Exception e) {
	        throw new RuntimeException(
	            "Error initializing SqlMap class. Cause: " + e);
	      } finally {
	        base64en = null;	
	        byteMing = null;
	        byteMi = null;
	      }
	      return replaceBlank(strMi);
	}
	/**
	   * 解密 以String密文输入,String明文输出
	   * @param strMi
	   * @return
	   */
	public String getDesString(String strMi) {
	      BASE64Decoder base64De = new BASE64Decoder();
	      byte[] byteMing = null;
	      byte[] byteMi = null;
	      String strMing = "";
	      try {
	        byteMi = base64De.decodeBuffer(strMi);
	        byteMing = this.getDesCode(byteMi);
	        strMing = new String(byteMing, "UTF-8");
	      } catch (Exception e) {
	        throw new RuntimeException(
	            "Error initializing SqlMap class. Cause: " + e);
	      } finally {
	        base64De = null;
	        byteMing = null;
	        byteMi = null;
	      }
	      return strMing;
	}
	/**
	   * 加密以byte[]明文输入,byte[]密文输出
	   * @param byteS
	   * @return
	   */
	private byte[] getEncCode(byte[] byteS) {
	      byte[] byteFina = null;
	      Cipher cipher;
	      try {
	        cipher = Cipher.getInstance("AES");
	        cipher.init(Cipher.ENCRYPT_MODE, key);
	        byteFina = cipher.doFinal(byteS);
	      } catch (Exception e) {
	        throw new RuntimeException(
	            "Error initializing SqlMap class. Cause: " + e);
	      } finally {
	        cipher = null;
	      }
	      return byteFina;
	}
	/**
	   * 解密以byte[]密文输入,以byte[]明文输出
	   * @param byteD
	   * @return
	   */
	private byte[] getDesCode(byte[] byteD) {
	      Cipher cipher;
	      byte[] byteFina = null;
	      try {
	        cipher = Cipher.getInstance("AES");
	        cipher.init(Cipher.DECRYPT_MODE, key);
	        byteFina = cipher.doFinal(byteD);
	      } catch (Exception e) {
	        throw new RuntimeException(
	            "Error initializing SqlMap class. Cause: " + e);
	      } finally {
	        cipher = null;
	      }
	      return byteFina;
	}
	
	public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
